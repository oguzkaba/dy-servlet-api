package com.kodlabs.doktorumyanimda.integrations;

import com.google.gson.Gson;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.model.notification.NotificationBody;
import com.kodlabs.doktorumyanimda.model.integrations.NotificationData;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Notification implements IIntegrations{
    private NotificationData data;
    public Notification(NotificationData data){
        this.data = data;
    }
    @Override
    public boolean sendMessage() {
        try {
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestMethod("POST");
            NotificationBody body = new NotificationBody();
            body.app_id = Common.oneSignalAppID;
            body.include_player_ids = data.playerIDs;
            body.headings.put("en", Common.appName);
            body.buttons = data.buttons;
            body.data = data.data;
            body.headings.put("small_icon","https://etetkik.com/img/icon/logo.png");
            body.contents.put("en", data.message);
            String bodyJson = new Gson().toJson(body);
            byte[] sendBytes = bodyJson.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);
            int httpResponse = con.getResponseCode();
            return httpResponse >= HttpURLConnection.HTTP_OK;
        } catch(Throwable t) {
            t.printStackTrace();
        }
        return false;
    }
}
