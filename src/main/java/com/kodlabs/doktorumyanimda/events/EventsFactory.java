package com.kodlabs.doktorumyanimda.events;

import com.kodlabs.doktorumyanimda.events.azure.AzureEvents;
import com.kodlabs.doktorumyanimda.events.azure.AzureEventsEntity;

public class EventsFactory {
    public static IEvents getEvents(IEventsEntity entity){
        if(entity instanceof AzureEventsEntity){
            return new AzureEvents(entity);
        }else{
            return null;
        }
    }
}
