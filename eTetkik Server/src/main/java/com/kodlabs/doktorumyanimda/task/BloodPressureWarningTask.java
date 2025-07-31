package com.kodlabs.doktorumyanimda.task;

import com.google.gson.Gson;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.integrations.NotificationData;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.BloodPressurePatientInformation;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.MeasureAverageValues;
import com.kodlabs.doktorumyanimda.model.report.warning.TriggerWarningType;
import com.kodlabs.doktorumyanimda.model.report.warning.WarningType;
import com.kodlabs.doktorumyanimda.model.report.warning.WarningUpdate;
import com.kodlabs.doktorumyanimda.model.report.warning.WarningsPatientUpdateRequest;
import com.kodlabs.doktorumyanimda.notification.NotificationType;
import com.kodlabs.doktorumyanimda.notification.Notifications;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BloodPressureWarningTask {
    private static BloodPressureWarningTask task = null;
    private Timer timer = null;
    private final long PERIOD = TimeUnit.DAYS.toMillis(7);
    private final Gson gson = new Gson();
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                System.out.println("Start Task");
                List<BloodPressurePatientInformation> patientIDs = Managers.reportManager.getAvailableBloodPressurePatients();
                if(patientIDs != null && !patientIDs.isEmpty()){
                    for (BloodPressurePatientInformation information: patientIDs){
                        MeasureAverageValues values = Managers.reportManager.bloodPressureAverageValues(information.getPatientID());
                        if(values != null){
                            boolean isWarning = false;
                            String pTitle = null;
                            String pContent = null;
                            String dTitle;
                            String dContent;
                            int type = 0;
                            if(values.averageMajorValue > Common.majorAverageValue || values.averageMinorValue > Common.minorAverageValue){
                                isWarning = true;
                                pTitle = "Haftalık yüksek tansiyon uyarısı";
                                pContent = "Haftalık tansiyon ortalamanız normal değerlerin üzerindedir. Ortalamanız: "
                                            .concat(String.format("[ BT: %.1f, KT: %.1f ]",values.averageMajorValue,values.averageMinorValue));

                                type = WarningType.BLOOD_PRESSURE.ordinal() + 1;
                            }
                            if(values.averagePulseValue > Common.pulseAverageValue){
                                if(isWarning){
                                    pTitle = "Haftalık yüksek tansiyon ve nabız uyarısı";
                                    pContent = "Haftalık tansiyon ve nabız ortalamanız normal değerlerin üzerindedir. Ortalamanız: "
                                            .concat(String.format("[ BT: %.1f, KT: %.1f, Nabız: %.1f ]",values.averageMajorValue,values.averageMinorValue,values.averagePulseValue));
                                    type = WarningType.ALL.ordinal() + 1;
                                }else{
                                    pTitle = "Haftalık yüksek nabız uyarısı";
                                    pContent = "Haftalık nabız ortalamanız normal değerlerin üzerindedir. Ortalamanız: "
                                            .concat(String.format("[ Nabız: %.1f ]",values.averagePulseValue));

                                    type = WarningType.PULSE.ordinal() + 1;
                                }
                                isWarning = true;
                            }
                            if(isWarning){
                                dTitle = pTitle;
                                dContent = information.getName().concat(" ").concat(information.getSurname())
                                        .concat(" için haftalık değerler (")
                                        .concat(String.format("BT: %.1f,KT: %.1f,Nabız: %.1f",values.averageMajorValue,values.averageMinorValue,values.averagePulseValue))
                                        .concat(") yüksek çıkmıştır.");
                                final Map<String,Object> contents = new HashMap<>();
                                NotificationLog logData = new NotificationLog();
                                logData.setUserID(information.getPatientID());
                                contents.put("description",dContent);
                                contents.put("majorValue",values.averageMajorValue);
                                contents.put("minorValue",values.averageMinorValue);
                                contents.put("pulseValue",values.averagePulseValue);
                                String contentsJSON = gson.toJson(contents);
                                Map<String,Object> attributes = new HashMap<>();
                                attributes.put("type", NotificationType.NOTICE.ordinal());
                                logData.setAttributes(attributes);
                                logData.setTitle(pTitle);
                                logData.setBody(pContent);

                                WarningUpdate update = new WarningUpdate(pTitle, type, TriggerWarningType.PERIODIC.ordinal(), contentsJSON);
                                WarningsPatientUpdateRequest updateRequest = new WarningsPatientUpdateRequest(information.getPatientID(), update);
                                ResponseEntity response = Managers.reportManager.updateWarning(updateRequest);
                                if(response.isSuccess){
                                    ResponseEntitySet<List<String>> idsResponse = Managers.peakManager.patientDoctorIDs(information.getPatientID());
                                    if(idsResponse.isSuccess){
                                        List<String> ids = idsResponse.getData();
                                        if(ids != null && !ids.isEmpty()){
                                            for (String doctorID: ids){
                                                Notifications.sendWarningNotification(logData,false);
                                                List<String> notificationIDs = Managers.notificationManager.getNotificationIDs(doctorID);
                                                attributes.put("content",contentsJSON);
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
                            }
                        }
                    }
                }
            }catch (ConnectionException | SQLException e) {
                e.printStackTrace();
            }
        }
    };
    private BloodPressureWarningTask(){}

    public static synchronized BloodPressureWarningTask getInstance(){
        if(task == null)
            task = new BloodPressureWarningTask();
        return task;
    }
    public void startWarningTask(){
        if(timer != null)
            return;
        timer = new Timer();
        System.out.println("Task Startup");
        Calendar fridayTime = Calendar.getInstance();
        fridayTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        fridayTime.set(Calendar.HOUR_OF_DAY, 17); // UTC+3 20
        fridayTime.set(Calendar.MINUTE, 0);
        fridayTime.set(Calendar.SECOND, 0);
        Calendar current = Calendar.getInstance();
        long fridayTimes = fridayTime.getTime().getTime();
        long currentTimes = current.getTime().getTime();
        if(fridayTimes < currentTimes){
            fridayTime.add(Calendar.DAY_OF_WEEK,7);
        }
        timer.schedule(timerTask,fridayTime.getTime(),PERIOD);
    }
}
