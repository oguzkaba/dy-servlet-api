package com.kodlabs.doktorumyanimda.model.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Message {
    private String messageID;
    private String otherUserID;
    private String messageTitle;
    public boolean lastDirection;
    private String lastUpdateDate;
    private String lastMessageContent;
    private String picture;
    private String peakDate;
    private int peakDay;
    private int process = -1;
    public boolean isRead;

    public Message(String messageID,String otherUserID, String messageTitle, boolean lastDirection, String lastUpdateDate, String lastMessageContent, String picture, boolean isRead){
        this.messageID = messageID;
        this.otherUserID = otherUserID;
        this.messageTitle = messageTitle;
        this.lastDirection = lastDirection;
        this.lastUpdateDate = lastUpdateDate;
        this.picture = picture;
        this.lastMessageContent = lastMessageContent;
        this.isRead = isRead;
    }

    public Message(String messageID,String otherUserID, String messageTitle, boolean lastDirection, String lastUpdateDate, String lastMessageContent, String picture, String peakDate, int peakDay, int process, boolean isRead){
        this.messageID = messageID;
        this.otherUserID = otherUserID;
        this.messageTitle = messageTitle;
        this.lastDirection = lastDirection;
        this.lastUpdateDate = lastUpdateDate;
        this.picture = picture;
        this.lastMessageContent = lastMessageContent;
        this.peakDate = peakDate;
        this.peakDay = peakDay;
        this.process = process;
        this.isRead = isRead;
    }
}
