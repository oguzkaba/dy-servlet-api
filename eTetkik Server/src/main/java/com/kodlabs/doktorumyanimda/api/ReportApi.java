package com.kodlabs.doktorumyanimda.api;

import com.google.gson.Gson;
import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.model.report.warning.*;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.integrations.NotificationData;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.model.patient.PatientStatusUpdateRequest;
import com.kodlabs.doktorumyanimda.model.report.warning.*;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.BloodPressure;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.BloodPressureUpdateReport;
import com.kodlabs.doktorumyanimda.model.report.complaint.Complaint;
import com.kodlabs.doktorumyanimda.model.report.complaint.ComplaintUpdateContentRequest;
import com.kodlabs.doktorumyanimda.model.report.complaint.ComplaintUpdateRequest;
import com.kodlabs.doktorumyanimda.model.report.examination.Examination;
import com.kodlabs.doktorumyanimda.model.report.examination.ExaminationUpdateContentRequest;
import com.kodlabs.doktorumyanimda.model.report.examination.ExaminationUpdateRequest;
import com.kodlabs.doktorumyanimda.model.report.userdrug.UserDrug;
import com.kodlabs.doktorumyanimda.model.report.userdrug.UserDrugRequest;
import com.kodlabs.doktorumyanimda.notification.NotificationType;
import com.kodlabs.doktorumyanimda.notification.Notifications;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/reports")
public class ReportApi {
    /* Blood Pressure */
    @Path("/bp/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity bloodPressureUpdate(BloodPressureUpdateReport report){
        ResponseEntity response = Managers.reportManager.bloodPressureUpdate(report);
        if(response.isSuccess){
            new Thread(()->{
                Managers.patientManager.patientStatusUpdate(new PatientStatusUpdateRequest(report.getPatientID(),"blood_pressure",true));
            }).start();
            NotificationLog logData;
            BloodPressure bloodPressure = report.getBloodPressure();

            String pTitle = "";
            String pContent = null;

            String dTitle;
            String dContent;
            int type = WarningType.LOW.ordinal() + 1;

            boolean isAvailableWarning = false;
            boolean isLowWarning = true;
            if(bloodPressure.majorValue > Common.majorMaxValue ||
               bloodPressure.minorValue > Common.minorMaxValue ||
               bloodPressure.pulseValue > Common.pulseMaxValue){
                pTitle = Messages.bpMaxWarningTitle;
                pContent = Messages.bpMaxWarningMessage;

                type = WarningType.HIGH.ordinal();
                isAvailableWarning = true;
                isLowWarning = false;
            }else if(bloodPressure.majorValue < Common.majorMinValue ||
                    bloodPressure.minorValue < Common.minorMinValue ||
                    bloodPressure.pulseValue < Common.pulseMinValue){

                pTitle = Messages.bpMinWarningTitle;
                pContent = Messages.bpMinWarningMessage;

                isAvailableWarning = true;
            }
            if(isAvailableWarning){
                String fullName = Managers.userManager.getFullName(report.getPatientID(), Role.PATIENT.value());
                if(!TextUtils.isEmpty(fullName)){
                    pContent = pContent.concat("\n")
                            .concat(String.format(Messages.bpMeasureValues, bloodPressure.majorValue, bloodPressure.minorValue, bloodPressure.pulseValue));

                    dTitle = isLowWarning ? Messages.currentLowValueWarningMessage : Messages.currentHighValueWarningMessage;
                    dContent = fullName
                            .concat(String.format( Messages.currentWarningValueMessage ,bloodPressure.majorValue,bloodPressure.minorValue,bloodPressure.pulseValue))
                            .concat(isLowWarning ? Messages.bpVeryLow : Messages.bpVeryHigh);

                    final Map<String,Object> contents = new HashMap<>();
                    logData = new NotificationLog();
                    final Gson gson = new Gson();
                    logData.setUserID(report.getPatientID());
                    contents.put("description", dContent);
                    contents.put("majorValue", bloodPressure.majorValue);
                    contents.put("minorValue", bloodPressure.minorValue);
                    contents.put("pulseValue", bloodPressure.pulseValue);

                    final String contentsJSON = gson.toJson(contents);

                    final Map<String,Object> attributes = new HashMap<>();
                    attributes.put("type", NotificationType.NOTICE.ordinal());
                    logData.setAttributes(attributes);
                    logData.setTitle(pTitle);
                    logData.setBody(pContent);
                    WarningUpdate update = new WarningUpdate(pTitle, type, TriggerWarningType.INSTANT.ordinal(), contentsJSON);
                    WarningsPatientUpdateRequest updateRequest = new WarningsPatientUpdateRequest(report.getPatientID(), update);
                    ResponseEntity warningResponse = Managers.reportManager.updateWarning(updateRequest);
                    if(warningResponse.isSuccess){
                        new Thread(()->{
                            Managers.patientManager.patientStatusUpdate(new PatientStatusUpdateRequest(report.getPatientID(),"warning",true));
                        }).start();
                        Notifications.sendWarningNotification(logData,false);
                        ResponseEntitySet<List<String>> idsResponse = Managers.peakManager.patientDoctorIDs(updateRequest.getPatientID());
                        if(idsResponse.isSuccess){
                            List<String> ids = idsResponse.getData();
                            if(ids != null && !ids.isEmpty()){
                                for (String id: ids){
                                    List<String> notificationIDs = Managers.notificationManager.getNotificationIDs(id);
                                    attributes.put("content", contentsJSON);
                                    for (String notificationID: notificationIDs){
                                        NotificationData entity = new NotificationData(notificationID,dTitle.concat("\n").concat(dContent),attributes);
                                        IIntegrations notificationIntegration = IntegrationsFactory.getIntegrations(entity,IntegrationsFactory.NOTIFICATION);
                                        if(notificationIntegration != null)
                                            notificationIntegration.sendMessage();
                                    }
                                }
                            }
                        }

                    }
                    response.message = Messages.bpWarningMessage;
                }
            }
        }
        return response;
    }

    @Path("/pb/history")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<BloodPressure>> bloodPressureList(@QueryParam("patientID")String patientID, @QueryParam("state")Integer state){
        return Managers.reportManager.bloodPressureList(patientID, state);
    }
    @Path("/bp/history/limit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<BloodPressure>> bloodPressureListLimit(@QueryParam("patientID")String patientID,@QueryParam("limit")Integer limit){
        return Managers.reportManager.bloodPressureListLimit(patientID,limit);
    }

    /* Warning Report */
    @Path("/warning/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<PatientWarning>> warnings(@QueryParam("doctorID")String doctorID){
        return Managers.reportManager.warningPatients(doctorID);
    }

    @Path("/warning/{id}/read")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity patientWarningRead(@PathParam("id")String warningID, @QueryParam("isRead") boolean isRead){
        return Managers.reportManager.updateWarningPatientRead(warningID, isRead);
    }

    @Path("/warning/patient/id")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<String> patientWarningID(@QueryParam("patientID")String patientID){
        return Managers.reportManager.patientWarningID(patientID);
    }
    @Path("/warning/unRead/count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<Integer> patientWarningUnReadCount(@QueryParam("doctorID")String doctorID){
        return Managers.reportManager.patientWarningUnReadCount(doctorID);
    }

    @Path("/warning/content/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<WarningContent>> warningContent(@QueryParam("warningID")String warningID){
        return Managers.reportManager.warningContents(warningID);
    }


    /* UserDrug */

    @Path("/userDrug/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity userDrugAdd(UserDrugRequest request){
        ResponseEntity response = Managers.reportManager.userDrugAdd(request);
        if(response.isSuccess){
            new Thread(()->{
                Managers.patientManager.patientStatusUpdate(new PatientStatusUpdateRequest(request.getUserID(),"user_drug",true));
            }).start();
        }
        return response;
    }

    @Path("/userDrug/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<UserDrug>> userDrugList(@QueryParam("patientID")String patientID){
        return Managers.reportManager.userDrugList(patientID);
    }
    @Path("/userDrug/{id}/remove")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity userDrugRemove(@PathParam("id")String userDrugID){
        return Managers.reportManager.userDrugRemove(userDrugID);
    }
    /* Examination */
    @Path("/examination/create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ResponseEntitySet<String> examinationUpdate(ExaminationUpdateRequest request){
        ResponseEntitySet<String> response = Managers.reportManager.examinationUpdate(request);
        if(response.isSuccess){
            new Thread(()->{
                Managers.patientManager.patientStatusUpdate(new PatientStatusUpdateRequest(request.getUserID(),"examination",true));
            }).start();
        }
        return response;
    }

    @Path("/examination/{id}/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public ResponseEntity examinationRemove(@PathParam("id")String id){
        return Managers.reportManager.examinationRemove(id);
    }

    @Path("/examination/content/update")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ResponseEntity examinationContentUpdate(ExaminationUpdateContentRequest request){
        return Managers.reportManager.examinationContentUpdate(request);
    }

    @Path("/examination/list")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ResponseEntitySet<List<Examination>> examinationList(@QueryParam("patientID")String patientID){
        return Managers.reportManager.examinationList(patientID);
    }

    @Path("/examination/content/list")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ResponseEntitySet<List<String>> examinationContentList(@QueryParam("examinationID")String id){
        return Managers.reportManager.examinationContentList(id);
    }

    /* Complaints */
    @Path("/complaint/create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ResponseEntitySet<String> updateComplaint(ComplaintUpdateRequest request){
        ResponseEntitySet<String> response = Managers.reportManager.updateComplaint(request);
        if(response.isSuccess){
            new Thread(()->{
                Managers.patientManager.patientStatusUpdate(new PatientStatusUpdateRequest(request.getUserID(),"complaint",true));
            }).start();
        }
        return response;
    }
    @Path("/complaint/content/update")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ResponseEntity updateComplaintContent(ComplaintUpdateContentRequest request){
        return Managers.reportManager.updateComplaintContent(request);
    }

    @Path("/complaint/{id}/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public ResponseEntity removeComplaint(@PathParam("id")String id){
        return Managers.reportManager.removeComplaint(id);
    }

    @Path("/complaint/list")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ResponseEntitySet<List<Complaint>> listComplaint(@QueryParam("patientID")String patientID){
        return Managers.reportManager.listComplaint(patientID);
    }

    @Path("/complaint/content/list")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ResponseEntitySet<List<String>> listComplaintContents(@QueryParam("complaintID")String id){
        return Managers.reportManager.listComplaintContent(id);
    }
}
