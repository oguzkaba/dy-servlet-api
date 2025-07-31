package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.dto.patient.PatientForAdminDTO;
import com.kodlabs.doktorumyanimda.dto.patient.enabiz.service.EnabizServiceInformationDTO;
import com.kodlabs.doktorumyanimda.dto.patient.systakipno.PatientSysTakipNoDTO;
import com.kodlabs.doktorumyanimda.events.EventsFactory;
import com.kodlabs.doktorumyanimda.events.EventsType;
import com.kodlabs.doktorumyanimda.events.IEvents;
import com.kodlabs.doktorumyanimda.events.azure.AzureEventsEntity;
import com.kodlabs.doktorumyanimda.integrations.SmsUtils;
import com.kodlabs.doktorumyanimda.mapper.ModelMapper;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.patient.Patient;
import com.kodlabs.doktorumyanimda.model.patient.PatientForAdmin;
import com.kodlabs.doktorumyanimda.model.patient.PatientStatusUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizInformationServiceCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizServiceInformation;
import com.kodlabs.doktorumyanimda.model.patient.inspection.Inspection;
import com.kodlabs.doktorumyanimda.model.patient.inspection.InspectionContent;
import com.kodlabs.doktorumyanimda.model.patient.notes.PatientNotesContent;
import com.kodlabs.doktorumyanimda.model.patient.notes.PatientNotesContentCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.notes.PatientNotesContentUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNo;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.user.*;
import com.kodlabs.doktorumyanimda.model.user.UserPatient;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfileUpdateV2;
import com.kodlabs.doktorumyanimda.model.user.profile.ProfileUpdateRequest;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Phones;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/patient")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PatientApi {
    @POST
    @Path("/login")
    public ResponseEntity login(PatientLoginRequest request){
        ResponseEntitySet<String> response = Managers.patientManager.login(request);
        if(!Common.isLocal && response.isSuccess && !Phones.isContains(request.getUname(), false)){
            if(response.isSuccess){
                ResponseEntity smsResponse = SmsUtils.loginVerify(request.getUname(), request.getDeviceID(), response.getData());
                if(!smsResponse.isSuccess){
                    return smsResponse;
                }
            }
        }
        return response;
    }
    @POST
    @Path("/register")
    public ResponseEntity register(PatientRegisterRequest request){
        ResponseEntitySet<String> response = Managers.patientManager.create(request);
        if(!Common.isLocal && response.isSuccess && !Phones.isContains(request.getPhone(), false)){
            if(response.isSuccess){
                ResponseEntity smsResponse = SmsUtils.loginVerify(request.getPhone(), request.getDeviceID(), response.getData());
                if(!smsResponse.isSuccess){
                    return smsResponse;
                }
                new Thread(()->{
                    Map<String, Object> info = new HashMap<>();
                    String phone = request.getPhone().trim();
                    String dialCode = phone.substring(0, phone.length() - 10);
                    info.put("CountryCode", dialCode.replace("+", ""));
                    IEvents events = EventsFactory.getEvents(new AzureEventsEntity(EventsType.NEW_USER_EVENTS, "NewUser", info));
                    if(events != null){
                        events.insert();
                    }
                }).start();
            }
        }
        return response;
    }
    @POST
    @Path("/login/verify")
    public ResponseEntitySet<UserPatient> loginVerify(@Context HttpServletRequest hsr, PatientLoginVerifyRequest request){
        return Managers.patientManager.loginVerify(request, Functions.getClientIpAddress(hsr));
    }

    @GET
    @Path("/login/verify/new")
    public ResponseEntity newLoginVerifyCode(@QueryParam("phone") String phone, @QueryParam("deviceID")String deviceID){
       return Managers.patientManager.newVerifyCode(phone, deviceID, Functions.generateCode());
    }
    @POST
    @Path("/v2/login")
    public ResponseEntitySet<String> loginV2(@Context HttpServletRequest hsr,  PatientLoginV2Request request){
        return Managers.patientManager.loginV2(request, Functions.getClientIpAddress(hsr));
    }
    @Path("/singup")
    @POST
    public ResponseEntitySet<UserPatient> singUp(@Context HttpServletRequest hsr, PatientSingUpV2Request request){
        return Managers.patientManager.singUpV2(request, Functions.getClientIpAddress(hsr));
    }

    @Path("/list/for/doctor")
    @GET
    public ResponseEntitySet<List<Patient>> getAllDoctorPatients(@QueryParam("doctorID")String doctorID){
        return Managers.patientManager.getAllDoctorPatient(doctorID);
    }
    @Path("/list/old")
    @GET
    public ResponseEntitySet<List<Patient>> doctorOldPatients(@QueryParam("doctorID")String doctorID){
        return Managers.patientManager.doctorOldPatients(doctorID);
    }
    @Path("/list")
    @GET
    public ResponseEntitySet<List<PatientForAdminDTO>> getAllPatients(@QueryParam("userID")String userID){
        ResponseEntitySet<List<PatientForAdmin>> response = Managers.patientManager.getAllPatient(userID);
        if(response.isSuccess){
            List<PatientForAdminDTO> patients = response.getData().stream().map(v -> ModelMapper.getInstance().map(v, PatientForAdminDTO.class)).collect(Collectors.toList());
            return new ResponseEntitySet<>(patients);
        }else{
            return new ResponseEntitySet<>(false, response.message);
        }
    }

    @GET
    @Path("/profile")
    public ResponseEntitySet<PatientProfile> profile(@QueryParam("userID")String userID){
        return Managers.patientProfileManager.profile(userID);
    }

    @POST
    @Path("/profile/update")
    public ResponseEntity profileUpdate(@Context HttpServletRequest hsr, ProfileUpdateRequest<PatientProfile> request){
        return Managers.patientProfileManager.update(request, Functions.getClientIpAddress(hsr));
    }


    @PUT
    @Path("/{patientID}/profile/update")
    public ResponseEntity profileV2Update(@PathParam("patientID")String patientID, @Context HttpServletRequest hsr, PatientProfileUpdateV2 profile){
        return Managers.patientProfileManager.updateV2(patientID, profile, Functions.getClientIpAddress(hsr));
    }

    @GET
    @Path("/profile/exists")
    public ResponseEntitySet<Boolean> profileExists(@QueryParam("userID")String userID){
        return Managers.patientProfileManager.existsProfile(userID);
    }

    @Path("/information")
    @GET
    public ResponseEntitySet<Patient> information(@QueryParam("patientID") String patientID){
        return Managers.patientManager.information(patientID);
    }

    /* Patient Status */
    @Path("/{patientID}/status/{fieldName}/update")
    @PUT
    public ResponseEntity patientStatusUpdate(@PathParam("patientID")String patientID, @PathParam("fieldName")String fieldName, @QueryParam("value")Boolean value){
        return Managers.patientManager.patientStatusUpdate(new PatientStatusUpdateRequest(patientID, fieldName, value));
    }
    @Path("/status")
    @GET
    public ResponseEntitySet<String> patientGetStatus(@QueryParam("patientID")String patientID){
        return Managers.patientManager.patientGetStatus(patientID);
    }
    /* End */

    /* Inspection */
    @Path("/{patientID}/inspection/list")
    @GET
    public ResponseEntitySet<List<Inspection>> patientInspectionList(@PathParam("patientID")String patientID, @QueryParam("doctorID")String doctorID){
        return Managers.patientManager.patientInspectionList(patientID, doctorID);
    }

    @Path("/inspection/{id}")
    @DELETE
    public ResponseEntity inspectionDelete(@PathParam("id")String id){
        return Managers.patientManager.inspectionDelete(id);
    }

    @Path("/inspection/{id}/content/list")
    @GET
    public ResponseEntitySet<List<InspectionContent>> inspectionContentList(@PathParam("id")String id){
        return Managers.patientManager.inspectionContentList(id);
    }

    @Path("/inspection/content")
    @POST
    public ResponseEntity inspectionContentCreate(@Context HttpServletRequest hsr, InspectionContent inspectionContent){
        return Managers.patientManager.inspectionContentCreate(Functions.getClientIpAddress(hsr), inspectionContent);
    }

    @Path("/inspection/content/update")
    @POST
    public ResponseEntity inspectionContentUpdate(@Context HttpServletRequest hsr, InspectionContent inspectionContent){
        return Managers.patientManager.inspectionContentUpdate(Functions.getClientIpAddress(hsr), inspectionContent);
    }

    @Path("/inspection/content/{contentID}")
    @DELETE
    public ResponseEntity inspectionContentDelete(@Context HttpServletRequest hsr, @PathParam("contentID")String contentID){
        return Managers.patientManager.inspectionContentDelete(Functions.getClientIpAddress(hsr), contentID);
    }
    /* End */


    /* Patient Notes */
    @Path("/{patientID}/note")
    @GET
    public ResponseEntitySet<String> notes(@Context HttpServletRequest hsr, @PathParam("patientID") String patientID, @QueryParam("doctorID")String doctorID){
        return Managers.patientManager.notesCreate(patientID, doctorID);
    }

    @Path("/note/{noteID}/content/list")
    @GET
    public ResponseEntitySet<List<PatientNotesContent>> noteContents(@Context HttpServletRequest hsr, @PathParam("noteID")String noteID){
        return Managers.patientManager.noteContents(noteID);
    }

    @Path("/note/content")
    @POST
    public ResponseEntitySet<PatientNotesContent> noteContentCreate(@Context HttpServletRequest hsr, PatientNotesContentCreateRequest request){
        return Managers.patientManager.noteContentCreate(request);
    }

    @Path("/note/content/update")
    @POST
    public ResponseEntity noteContentUpdate(@Context HttpServletRequest hsr, PatientNotesContentUpdateRequest request){
        return Managers.patientManager.noteContentUpdate(request);
    }

    @Path("/note/content/{contentID}")
    @DELETE
    public ResponseEntity noteContentDelete(@Context HttpServletRequest hsr, @PathParam("contentID")String contentID){
        return Managers.patientManager.noteContentDelete(contentID);
    }
    /* Patient Notes End */

    /* Patient SysTakipNo */
    @Path("/systakipno/list")
    @GET
    public ResponseEntitySet<List<PatientSysTakipNoDTO>> sysTakipNoList(@QueryParam("doctorID")String doctorID){
        ResponseEntitySet<List<PatientSysTakipNo>> response = Managers.patientManager.sysTakipNoList(doctorID);
        if(response.isSuccess){
            return new ResponseEntitySet<>(
                    response.getData().stream()
                            .map(k -> ModelMapper.getInstance().map(k, PatientSysTakipNoDTO.class))
                            .collect(Collectors.toList())

            );
        }
        return new ResponseEntitySet<>(false, response.message);
    }

    @Path("/systakipno")
    @GET
    public ResponseEntitySet<String> sysTakipNoGet(@QueryParam("tcNumber")String tcNumber, @QueryParam("doctorID")String doctorID){
        return Managers.patientManager.sysTakipNo(tcNumber, doctorID);
    }
    @Path("/systakipno")
    @POST
    public ResponseEntity sysTakipNoCreate(@Context HttpServletRequest hsr, PatientSysTakipNoCreateRequest request){
       return Managers.patientManager.sysTakipNoCreate(Functions.getClientIpAddress(hsr), request);
    }

    @Path("/systakipno/update")
    @POST
    public ResponseEntity sysTakipNoUpdate(@Context HttpServletRequest hsr, PatientSysTakipNoUpdateRequest request){
        return Managers.patientManager.sysTakipNoUpdate(Functions.getClientIpAddress(hsr), request);
    }
    /* Patient SysTakipNo End*/

    /* Patient Enabiz Hizmet Bilgisi*/
    @Path("/enabiz/service/information")
    @GET
    public ResponseEntitySet<List<EnabizServiceInformationDTO>> getEnabizServiceInformation(@QueryParam("sysTakipNo")String sysTakipNo){
        ResponseEntitySet<List<EnabizServiceInformation>> response = Managers.patientManager.getEnabizServiceInformation(sysTakipNo);
        if(response.isSuccess){
            List<EnabizServiceInformationDTO> newData = new ArrayList<>();
            if(response.getData() == null){
                return new ResponseEntitySet<>(newData);
            }else{
                newData = response.getData().stream()
                        .map(data -> ModelMapper.getInstance().map(data, EnabizServiceInformationDTO.class))
                        .collect(Collectors.toList());
                return new ResponseEntitySet<>(newData);
            }
        }else{
            return new ResponseEntitySet<>(false, response.errorCode, response.message);
        }
    }
    @Path("/enabiz/service/information")
    @POST
    public ResponseEntity createEnabizServiceInformation(EnabizInformationServiceCreateRequest request){
        return Managers.patientManager.createEnabizServiceInformation(request);
    }

    @Path("/enabiz/service/information/{service_reference_number}")
    @DELETE
    public ResponseEntity deleteEnabizServiceInformation(@PathParam("service_reference_number")String serviceReferenceNumber){
        return Managers.patientManager.deleteEnabizServiceInformation(serviceReferenceNumber);
    }
}
