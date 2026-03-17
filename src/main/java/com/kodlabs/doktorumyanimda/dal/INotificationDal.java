package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;

import java.util.List;

public interface INotificationDal {
    ResponseEntity addNotification(String userID, String notificationID) throws ConnectionException;

    ResponseEntity removeNotification(String userID) throws ConnectionException;

    List<String> getNotificationIDs(String userID) throws ConnectionException;

    ResponseEntitySet<String> addNotificationLog(NotificationLog log, boolean isDoctor) throws ConnectionException;

    ResponseEntity deleteNotificationLog(String logID, byte role) throws ConnectionException;

    ResponseEntitySet<List<NotificationLog>> getAllNotificationLog(String userID, byte role) throws ConnectionException;

    ResponseEntity readNotificationLog(String logID, byte role) throws ConnectionException;

    ResponseEntitySet<Integer> notificationCount(String userID, byte role) throws ConnectionException;

    ResponseEntity readPeakNotificationLog(String peakID) throws ConnectionException;
}
