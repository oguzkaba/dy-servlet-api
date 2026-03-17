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
    public static synchronized void sendMessageNotification(NotificationLog log, MessageContent content) {
        boolean direction = content.isDirection();
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("name");
        attributeNames.add("gender");

        Map<String, Object> attributesValue = Managers.userManager.getAttributes(log.getUserID(),
                direction ? (byte) 0 : (byte) 1, attributeNames);

        if (attributesValue != null) {
            String name = (String) attributesValue.get("name");
            String gender = (String) attributesValue.get("gender");
            Calendar calendar = Calendar.getInstance();
            ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault());

            if (!dateTime.getZone().getId().contains("Istanbul")) {
                calendar.add(Calendar.HOUR_OF_DAY, 3);
            }

            String messageTitle = log.getTitle() != null ? log.getTitle().trim() : "";
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            StringBuilder body = new StringBuilder();

            // Video Arama Kontrolü
            // Mesaj içeriğini ve başlığını alıyoruz
            String rawContent = (content.getContent() != null && content.getContent().getContent() != null)
                    ? content.getContent().getContent().trim()
                    : "";

            // Flutter tarafıyla aynı string: "Video Calling patient..."
            String callTrigger = "Video Calling patient...";

            if (rawContent.equals(callTrigger)) {
                String senderName = (String) log.getAttributes().get("otherName");
                if (senderName == null || senderName.isEmpty())
                    senderName = "Doktorunuz";

                body.append("📹 Gelen Görüntülü Arama\n")
                        .append(senderName);

                // Ses ve Kanal ayarları
                log.getAttributes().put("android_sound", "call_ringtone");
                log.getAttributes().put("ios_sound", "call_ringtone.wav");
                log.getAttributes().put("android_channel_id", "video_call_channel");
            } else {
                if (4 <= hour && hour <= 11)
                    body.append("Günaydın ");
                else
                    body.append("Merhaba ");

                if (gender != null && !gender.isEmpty()) {
                    if (gender.equalsIgnoreCase("Erkek")) {
                        body.append(Functions.nameFirstCharacterUpperCase(name)).append(" Bey");
                    } else {
                        body.append(name).append(" Hanım");
                    }
                }
                body.append(",\n").append("💬 ").append(messageTitle);
            }

            // --- DEĞİŞİKLİK BURADA: FOR DÖNGÜSÜNÜ KALDIRDIK ---
            List<String> notificationIDs = Managers.notificationManager.getNotificationIDs(log.getUserID());

            if (notificationIDs != null && !notificationIDs.isEmpty()) {
                // Spring tarafındaki mantığın aynısı: Tüm listeyi tek bir pakete koyuyoruz
                NotificationData entity = new NotificationData();

                // Burada entity.playerIDs = notificationIDs; mantığını kullanıyoruz
                entity.setPlayerIDs(new ArrayList<>(new HashSet<>(notificationIDs))); // Tekilleştirilmiş liste
                entity.setMessage(body.toString());
                entity.setData(log.getAttributes());

                IIntegrations notificationIntegration = IntegrationsFactory.getIntegrations(entity,
                        IntegrationsFactory.NOTIFICATION);
                if (notificationIntegration != null) {
                    // Tek bir istek, tüm cihazlara aynı anda gider
                    notificationIntegration.sendMessage();
                }
            }

            // Tarih ve içerik güncellemeleri
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            content.setDate(format.format(calendar.getTime()));
            log.getAttributes().put("messageContent", content);
        }
    }

    public static synchronized void sendWarningNotification(NotificationLog log, boolean direction) {
        ResponseEntity response = Managers.notificationManager.addNotificationLog(log, direction);
        if (response.isSuccess) {
            List<String> ids = Managers.notificationManager.getNotificationIDs(log.getUserID());
            for (String id : ids) {
                NotificationData entity = new NotificationData(id, log.getTitle() + "\n" + log.getBody(),
                        log.getAttributes());
                IIntegrations notificationIntegration = IntegrationsFactory.getIntegrations(entity,
                        IntegrationsFactory.NOTIFICATION);
                if (notificationIntegration != null) {
                    notificationIntegration.sendMessage();
                }
            }
        }
    }
}
