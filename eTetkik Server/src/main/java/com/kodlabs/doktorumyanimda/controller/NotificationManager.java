package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.INotificationDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;

import java.util.List;

public class NotificationManager {
    private INotificationDal notificationDal;
    public NotificationManager(INotificationDal notificationDal){
        this.notificationDal = notificationDal;
    }
    public ResponseEntity pushNotification(String userID, String notificationID){
        if(TextUtils.isEmpty(userID) || TextUtils.isEmpty(notificationID))
            return new ResponseEntity(false, ErrorMessages.inValidData);
        try {
            return this.notificationDal.addNotification(userID, notificationID);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntity cancelNotification(String notificationID){
        if(TextUtils.isEmpty(notificationID)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.notificationDal.removeNotification(notificationID);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntity deleteNotificationLog(String logID, byte role){
        if(TextUtils.isEmpty(logID) || role < 0) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.notificationDal.deleteNotificationLog(logID, role);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public List<String> getNotificationIDs(String userID){
        if(TextUtils.isEmpty(userID)) {
            return null;
        }
        try{
            return this.notificationDal.getNotificationIDs(userID);
        }catch (ConnectionException e){
            return null;
        }
    }
    public ResponseEntitySet<String> addNotificationLog(NotificationLog log, boolean isDoctor){
        try{
            return this.notificationDal.addNotificationLog(log, isDoctor);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<NotificationLog>> getAllNotificationLogs(String userID, byte role){
        if(TextUtils.isEmpty(userID) || role < 0) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            return this.notificationDal.getAllNotificationLog(userID, role);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Integer> notificationCount(String userID, byte role){
        if(TextUtils.isEmpty(userID) || role < 0) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            return this.notificationDal.notificationCount(userID,role);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity readNotificationLog(String logID, byte role) {
        if(TextUtils.isEmpty(logID) || role < 0){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.notificationDal.readNotificationLog(logID, role);
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity readPeakNotificationLog(String peakID){
        if(TextUtils.isEmpty(peakID)){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.notificationDal.readPeakNotificationLog(peakID);
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
}
