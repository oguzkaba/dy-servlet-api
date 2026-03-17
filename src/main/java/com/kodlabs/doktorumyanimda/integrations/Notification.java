package com.kodlabs.doktorumyanimda.integrations;

import com.google.gson.Gson;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.model.notification.NotificationBody;
import com.kodlabs.doktorumyanimda.model.integrations.NotificationData;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

class Notification implements IIntegrations {
    private NotificationData data;

    public Notification(NotificationData data) {
        this.data = data;
    }

    @Override
    public boolean sendMessage() {
        try {
            // 1. URL ve Bağlantı Ayarları
            URL url = new URL("https://api.onesignal.com/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(5000); // Zaman aşımı eklemek donmayı önler

            // 2. Header Bilgileri
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Key " + Common.oneSignalApiKey);
            con.setRequestMethod("POST");

            // 3. JSON Gövdesini Oluşturma
            NotificationBody body = new NotificationBody();
            body.app_id = Common.oneSignalAppID;

            // Veritabanındaki ID'ler eskiyse include_player_ids, yeniyse
            // include_subscription_ids kullanılır.
            // OneSignal şu an ikisini de kabul ediyor.
            body.include_player_ids = data.playerIDs;

            body.headings = new HashMap<>();
            body.headings.put("en", Common.appName);

            body.contents = new HashMap<>();
            body.contents.put("en", data.message);

            body.data = data.data;
            body.buttons = data.buttons;

            // DÜZELTME: small_icon hiyerarşide dışarıda ve formatı .png olmalı!
            body.small_icon = "https://doktorumyanimda.net/assets/img/icon/logo.png";

            String bodyJson = new Gson().toJson(body);

            // DEBUG: Giden JSON'u mutlaka logda gör
            System.out.println("DEBUG OneSignal Payload: " + bodyJson);

            // 4. Veriyi Gönderme
            byte[] sendBytes = bodyJson.getBytes("UTF-8");
            try (OutputStream outputStream = con.getOutputStream()) {
                outputStream.write(sendBytes);
            }

            // 5. Yanıtı Kontrol Etme
            int httpResponse = con.getResponseCode();
            System.out.println("DEBUG OneSignal Response Code: " + httpResponse);

            return httpResponse == HttpURLConnection.HTTP_OK;

        } catch (Exception t) { // Throwable yerine Exception daha spesifiktir
            System.err.println("OneSignal Hatası: " + t.getMessage());
            t.printStackTrace();
        }
        return false;
    }
}
