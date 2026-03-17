package com.kodlabs.doktorumyanimda.model.notification;

import java.util.Map;

public class NotificationLog {
    private String id;
    private String userID;
    private String title;
    private String body;
    private Map<String,Object> attributes;
    public String createDate;
    public boolean isRead = false;

    public NotificationLog(){}

    public NotificationLog(String title, String body, Map<String, Object> attributes, String createDate) {
        this.title = title;
        this.body = body;
        this.attributes = attributes;
        this.createDate = createDate;
    }
    public NotificationLog(String id, String title, String body, Map<String, Object> attributes, String createDate, boolean isRead) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.attributes = attributes;
        this.createDate = createDate;
        this.isRead = isRead;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
