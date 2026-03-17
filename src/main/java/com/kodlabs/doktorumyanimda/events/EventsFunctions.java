package com.kodlabs.doktorumyanimda.events;

import com.kodlabs.doktorumyanimda.events.azure.AzureEventsEntity;
import com.kodlabs.doktorumyanimda.utils.AES;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

public final class EventsFunctions {
    public static void MessageEvent(String receiver, String sender, String message){
        if(TextUtils.isEmpty(receiver) || TextUtils.isEmpty(sender) || TextUtils.isEmpty(message)){
            return;
        }
        Map<String, Object> info = new HashMap<>();
        info.put("Sender", sender);
        info.put("Receiver", receiver);
        info.put("Message", AES.encrypt(message, Common.cryptKey));
        IEvents messageEvent = EventsFactory.getEvents(new AzureEventsEntity(EventsType.MESSAGES, "NewMessage", info));
        if(messageEvent != null){
            messageEvent.insert();
        }
    }
}
