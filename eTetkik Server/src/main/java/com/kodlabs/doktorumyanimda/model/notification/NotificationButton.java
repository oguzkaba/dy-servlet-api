package com.kodlabs.doktorumyanimda.model.notification;

public class NotificationButton {
    public String id;
    public String text;
    public String icon;
    public NotificationButton(){}
    public NotificationButton(String id, String text,String icon){
        this.id = id;
        this.text = text;
        this.icon = icon;
    }
}
