package com.kodlabs.doktorumyanimda.model.integrations;

import com.kodlabs.doktorumyanimda.model.notification.NotificationButton;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class NotificationData  implements IIntegrationsEntity{
    public List<String> playerIDs = new ArrayList<>();
    public Map<String,Object> data;
    public List<NotificationButton> buttons = new ArrayList<>();
    public String message;

    public NotificationData(String playerID, String message, Map<String, Object> data) {
        this.playerIDs.add(playerID);
        this.data = data;
        this.message = message;
    }
}