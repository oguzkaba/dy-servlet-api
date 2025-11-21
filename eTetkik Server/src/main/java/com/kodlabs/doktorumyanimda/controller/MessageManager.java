package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.events.EventsFunctions;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.model.log.LogEventDescription;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.notification.NotificationType;
import com.kodlabs.doktorumyanimda.notification.Notifications;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.google.api.Http;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IMessageDal;
import com.kodlabs.doktorumyanimda.model.message.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class MessageManager {
    private IMessageDal messageDal;

    public MessageManager(IMessageDal messageDal) {
        this.messageDal = messageDal;
    }

    public ResponseEntity sendMessage(SendMessageRequest request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            String messageID = isExistsMessage(request.getDoctorID(), request.getPatientID());
            ResponseEntitySet<String> response;
            if (TextUtils.isEmpty(messageID)) {
                ResponseEntitySet<String> result = this.messageDal.createMessage(request.getDoctorID(),
                        request.getPatientID());
                if (result.isSuccess) {
                    response = this.messageDal.createMessageContent(result.getData(), request.getMessageContent());
                } else {
                    return result;
                }
            } else {
                response = this.messageDal.createMessageContent(messageID, request.getMessageContent());
            }
            if (response.isSuccess) {
                new Thread(() -> {
                    NotificationLog log = new NotificationLog();
                    boolean sendDirection = request.getMessageContent().isDirection();
                    Map<String, Object> updateAttributes = new HashMap<>();
                    if (sendDirection) {
                        log.setUserID(request.getPatientID());
                        log.setTitle(Messages.messageYourFromDoctor);
                    } else {
                        log.setUserID(request.getDoctorID());
                        log.setTitle(Messages.messageYourFromPatient);
                    }
                    updateAttributes.put("lastDirection", sendDirection);
                    updateAttributes.put("isRead", false);
                    Managers.messageManager.updateMessageAttributes(response.getData(), updateAttributes);
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("type", NotificationType.MESSAGE.ordinal());
                    String otherName;
                    if (sendDirection) {
                        attributes.put("otherID", request.getDoctorID());
                        otherName = Managers.userManager.getFullName(request.getDoctorID(), Role.DOCTOR.value());
                    } else {
                        attributes.put("otherID", request.getPatientID());
                        otherName = Managers.userManager.getFullName(request.getPatientID(), Role.PATIENT.value());
                    }
                    if (!TextUtils.isEmpty(otherName)) {
                        attributes.put("otherName", otherName);
                    }
                    attributes.put("messageID", response.getData());
                    log.setAttributes(attributes);
                    Notifications.sendMessageNotification(log, request.getMessageContent());
                    if (request.getMessageContent().getContent().getType() == MessageType.TEXT.ordinal()) {
                        if (TextUtils.isEmpty(otherName)) {
                            return;
                        }
                        String senderName, receiverName;
                        if (sendDirection) {
                            senderName = Functions.fullDoctorNameShortConvert(otherName);
                            receiverName = Managers.userManager.getFullName(request.getPatientID(),
                                    Role.PATIENT.value());
                            receiverName = Functions.fullPatientNameSortConvert(receiverName);
                        } else {
                            senderName = Functions.fullPatientNameSortConvert(otherName);
                            receiverName = Managers.userManager.getFullName(request.getDoctorID(), Role.DOCTOR.value());
                            receiverName = Functions.fullDoctorNameShortConvert(receiverName);
                        }
                        EventsFunctions.MessageEvent(receiverName, senderName,
                                request.getMessageContent().getContent().getContent());
                    }
                }).start();
                if (request.getMessageContent().isDirection()) {
                    String fullName = Managers.userManager.getFullName(request.getPatientID(), Role.PATIENT.value());
                    messageLog("sendMessage", request.getDoctorID(), Role.DOCTOR.value(),
                            Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr),
                            String.format(LogEventDescription.MESSAGE.getMessage(),
                                    TextUtils.isEmpty(fullName) ? request.getPatientID() : fullName));
                } else {
                    String fullName = Managers.userManager.getFullName(request.getDoctorID(), Role.DOCTOR.value());
                    messageLog("sendMessage", request.getPatientID(), Role.PATIENT.value(),
                            Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr),
                            String.format(LogEventDescription.MESSAGE.getMessage(),
                                    TextUtils.isEmpty(fullName) ? request.getPatientID() : fullName));
                }
            }
            return response;
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public String isExistsMessage(String doctorID, String patientID) throws ConnectionException {
        return this.messageDal.existsMessage(doctorID, patientID);
    }

    public ResponseEntitySet<List<Message>> getAllMessages(String userID, Byte role) {
        if (TextUtils.isEmpty(userID) || role == null)
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        try {
            if (Managers.userManager.isExistsUser(userID, role)) {
                return this.messageDal.getAllMessage(userID, role);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Message> getMessageInformation(String doctorID, String patientID, Byte role) {
        if (TextUtils.isEmpty(doctorID) || TextUtils.isEmpty(patientID) || role == null)
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        try {
            return this.messageDal.getMessage(doctorID, patientID, role);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<MessageContent>> getMessageContents(String messageID, int lastID) {
        if (TextUtils.isEmpty(messageID))
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        try {
            return this.messageDal.getMessageContent(messageID, lastID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity updateMessageAttribute(String messageID, String attribute, Object value) {
        if (TextUtils.isEmpty(messageID) || TextUtils.isEmpty(attribute) || value == null) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.messageDal.updateAttribute(messageID, attribute, value);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity updateMessageAttributes(String messageID, Map<String, Object> attributes) {
        if (TextUtils.isEmpty(messageID) || attributes == null || attributes.isEmpty()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.messageDal.updateAttributes(messageID, attributes);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Integer> isUnReadsMessageCount(String userID, Byte role) {
        if (TextUtils.isEmpty(userID) || role == null || role < 0) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.messageDal.isUnReadMessageCount(userID, role);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Boolean> messageIsActive(String messageID) {
        if (TextUtils.isEmpty(messageID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.messageDal.messageIsActive(messageID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    private void messageLog(String methodName, String phoneOrUserID, byte role, String ip, String browserOrDevice,
            String eventDescription) {
        new Thread(() -> {
            try {
                Managers.logManager.create(
                        new Log(
                                "com.kodlabs.doktorumyanimda.controller",
                                MessageManager.class.getSimpleName(),
                                methodName,
                                phoneOrUserID,
                                role,
                                ip,
                                Functions.getIpAddress(),
                                eventDescription,
                                browserOrDevice));
            } catch (UnknownHostException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();
    }
}
