package com.kodlabs.doktorumyanimda.utils;

import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.integrations.NotificationData;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;

import java.util.List;

public final class NotificationUtils {
    public static void sendNotifications(NotificationLog log,boolean isLogged, boolean isDoctor){
        if(isLogged){
            Managers.notificationManager.addNotificationLog(log, isDoctor);
        }
        List<String> notificationIDs = Managers.notificationManager.getNotificationIDs(log.getUserID());
        if(notificationIDs != null && !notificationIDs.isEmpty()){
            for (String ids: notificationIDs){
                NotificationData entity = new NotificationData(ids, log.getTitle().concat("\n").concat(log.getBody()), log.getAttributes());
                IIntegrations notificationIntegration = IntegrationsFactory.getIntegrations(entity, IntegrationsFactory.NOTIFICATION);
                if(notificationIntegration != null){
                    notificationIntegration.sendMessage();
                }
            }
        }
    }
}
