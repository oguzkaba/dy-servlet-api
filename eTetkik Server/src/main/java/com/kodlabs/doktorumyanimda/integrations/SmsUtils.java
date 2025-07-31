package com.kodlabs.doktorumyanimda.integrations;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.integrations.SmsData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SmsUtils {
    public static ResponseEntity loginVerify(String phone, String deviceID, String verifyCode){
        IIntegrations integration = IntegrationsFactory.getIntegrations(new SmsData(String.format(Messages.smsLoginVerifyMessage, verifyCode), phone), IntegrationsFactory.SMS);
        if(integration != null){
            boolean result = integration.sendMessage();
            if(result){
                ResponseEntity res = Managers.userManager.deviceVerifyCountIncrease(deviceID);
                if(!res.isSuccess){
                    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss\n", Locale.getDefault());
                    System.out.println(format.format(Calendar.getInstance().getTime()).concat("Login verify increase error: ".concat(res.message)));
                }
                return new ResponseEntity();
            }
        }
        return new ResponseEntity(false, ErrorMessages.smsSendFailed);
    }
}
