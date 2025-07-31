package com.kodlabs.doktorumyanimda.notification;

import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.integrations.NotificationData;
import com.kodlabs.doktorumyanimda.model.message.MessageContent;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.utils.Functions;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Notifications {
    public static synchronized void sendMessageNotification(NotificationLog log, MessageContent content){
        boolean direction = content.isDirection();
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("name");
        attributeNames.add("gender");
        Map<String,Object> attributesValue = Managers.userManager.getAttributes(log.getUserID(), direction?(byte)0:(byte)1,attributeNames);
        if(attributesValue != null){
            String name = (String)attributesValue.get("name");
            String gender = (String) attributesValue.get("gender");
            Calendar calendar = Calendar.getInstance();
            ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault());
            if(!dateTime.getZone().getId().contains("Istanbul")){
                calendar.add(Calendar.HOUR_OF_DAY, 3);
            }
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            StringBuilder body = new StringBuilder();
            if( 4 <= hour && hour <= 11)
                body.append("Günaydın ");
            else
                body.append("Merhaba ");
            if(gender != null && !gender.isEmpty()){
                if(gender.equalsIgnoreCase("Erkek")){
                    body.append(Functions.nameFirstCharacterUpperCase(name))
                            .append(" Bey");
                }else{
                    body.append(name)
                            .append(" Hanım");
                }
            }
            body.append(",")
                    .append("\n")
                    .append(log.getTitle());
            List<String> notificationIDs = Managers.notificationManager.getNotificationIDs(log.getUserID());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            content.setDate(format.format(calendar.getTime()));
            log.getAttributes().put("messageContent", content);
            if(notificationIDs != null && !notificationIDs.isEmpty()){
                for (String ids: notificationIDs){
                    NotificationData entity = new NotificationData(ids,body.toString(),log.getAttributes());
                    IIntegrations notificationIntegration = IntegrationsFactory.getIntegrations(entity,IntegrationsFactory.NOTIFICATION);
                    if(notificationIntegration != null) {
                        notificationIntegration.sendMessage();
                    }
                }
            }

        }
    }
    public static synchronized  void sendWarningNotification(NotificationLog log,boolean direction){
        ResponseEntity response = Managers.notificationManager.addNotificationLog(log,direction);
        if(response.isSuccess){
            List<String> ids = Managers.notificationManager.getNotificationIDs(log.getUserID());
            for (String id: ids){
                NotificationData entity = new NotificationData(id,log.getTitle() + "\n" + log.getBody(),log.getAttributes());
                IIntegrations notificationIntegration = IntegrationsFactory.getIntegrations(entity,IntegrationsFactory.NOTIFICATION);
                if(notificationIntegration != null){
                    notificationIntegration.sendMessage();
                }
            }
        }
    }
}
