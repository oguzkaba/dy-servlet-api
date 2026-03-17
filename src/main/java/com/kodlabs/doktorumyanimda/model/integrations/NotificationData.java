package com.kodlabs.doktorumyanimda.model.integrations;

import com.kodlabs.doktorumyanimda.model.notification.NotificationButton;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data // Getter, Setter ve ToString için
@NoArgsConstructor
@AllArgsConstructor
public class NotificationData implements IIntegrationsEntity {
    public List<String> playerIDs = new ArrayList<>();
    public Map<String, Object> data;
    public List<NotificationButton> buttons = new ArrayList<>();
    public String message;

    // Tekli gönderimler için (Geriye dönük uyumluluk)
    public NotificationData(String playerID, String message, Map<String, Object> data) {
        this.playerIDs = new ArrayList<>();
        this.playerIDs.add(playerID);
        this.data = data;
        this.message = message;
    }

    // Toplu gönderimler için (Yeni eklenen constructor)
    public NotificationData(List<String> playerIDs, String message, Map<String, Object> data) {
        this.playerIDs = playerIDs;
        this.data = data;
        this.message = message;
    }
}