package com.kodlabs.doktorumyanimda.integrations;

import com.kodlabs.doktorumyanimda.model.integrations.IIntegrationsEntity;
import com.kodlabs.doktorumyanimda.model.integrations.MailData;
import com.kodlabs.doktorumyanimda.model.integrations.NotificationData;
import com.kodlabs.doktorumyanimda.model.integrations.SmsData;

public class IntegrationsFactory {
    public static byte MAIL = 0;
    public static byte SMS = 1;
    public static byte NOTIFICATION = 2;
    public static IIntegrations getIntegrations(IIntegrationsEntity entity, byte type){
        if(type == MAIL)
            return new Mail((MailData) entity);
        else if(type == SMS)
            return new Sms((SmsData) entity);
        else if(type == NOTIFICATION)
            return new Notification((NotificationData)entity);
        return null;
    }
}
