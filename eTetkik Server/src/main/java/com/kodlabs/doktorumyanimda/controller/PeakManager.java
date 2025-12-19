package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPeakDal;
import com.kodlabs.doktorumyanimda.events.EventsFactory;
import com.kodlabs.doktorumyanimda.events.EventsFunctions;
import com.kodlabs.doktorumyanimda.events.EventsType;
import com.kodlabs.doktorumyanimda.events.IEvents;
import com.kodlabs.doktorumyanimda.events.azure.AzureEventsEntity;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.model.log.LogEventDescription;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.model.peak.*;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.notification.NotificationType;
import com.kodlabs.doktorumyanimda.utils.*;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class PeakManager {
    private final IPeakDal iPeakDal;

    public PeakManager(IPeakDal iPeakDal) {
        this.iPeakDal = iPeakDal;
    }

    public ResponseEntitySet<String> create(CreatePeakRequest request, String version, String os,
            HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if (TextUtils.isEmpty(version) || !version.equalsIgnoreCase("v2")) {
            if (TextUtils.isEmpty(os) || os.equalsIgnoreCase("ios")) {
                return new ResponseEntitySet<>(false, ErrorMessages.clientVersionUpdateRequiredOpIOS);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.clientVersionUpdateRequiredOpAndroid);
            }
        }
        try {
            if (isExistsActiveOrWait(request.getPatientID(), request.getDoctorID())) {
                return new ResponseEntitySet<>(false, ErrorMessages.alreadyPeak);
            }
            if (!feeControl(request.getPatientID(), request.getDoctorID(), request.getPeakDay(), request.getFee())) {
                return new ResponseEntitySet<>(false, ErrorMessages.inValidDoctorFee);
            }
            ResponseEntitySet<String> response;
            if (isExists(request.getPatientID(), request.getDoctorID())) {
                response = this.iPeakDal.waitUpdate(request);
            } else {
                response = this.iPeakDal.create(request);
            }
            if (response.isSuccess) {
                String fullName = Managers.userManager.getFullName(request.getDoctorID(), Role.DOCTOR.value());
                peakSendLog("create", request.getPatientID(), Role.PATIENT.value(), Functions.getClientIpAddress(hsr),
                        Functions.getBrowserOrDevice(hsr),
                        String.format(LogEventDescription.APPOINTMENT_CREATE.getMessage(),
                                TextUtils.isEmpty(fullName) ? request.getDoctorID() : fullName));
            }
            peakCreateSendNotification(request, response.getData());
            return response;
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }

    }

    private void peakCreateSendNotification(CreatePeakRequest request, String id) {
        new Thread(() -> {
            // Notification
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("type", NotificationType.PEAK_DEMAND.ordinal());
            attributes.put("id", id);
            String title = Messages.peakDemand;
            String body = Messages.peakDemandMessage;
            NotificationLog log = new NotificationLog(title, body, attributes, null);
            log.setUserID(request.getDoctorID());
            NotificationUtils.sendNotifications(log, true, true);
            if (!Common.isLocal) {
                // Events
                String drName = Managers.userManager.getFullName(request.getDoctorID(), Role.DOCTOR.value());
                String eventID = request.getPatientID().concat(request.getDoctorID());
                Map<String, Object> info = new HashMap<>();
                info.put("AppointmentRequestKey", Functions.toSHA256(eventID));
                info.put("DoctorName", Functions.fullDoctorNameShortConvert(drName));
                IEvents events = EventsFactory.getEvents(new AzureEventsEntity(
                        EventsType.NEW_APPOINTMENT_REQUEST_EVENTS, "NewAppointmentRequest", info));
                if (events != null) {
                    events.insert();
                }
                // End
                String senderName = Managers.userManager.getFullName(request.getPatientID(), Role.PATIENT.value());
                senderName = Functions.fullPatientNameSortConvert(senderName);
                String receiverName = Managers.userManager.getFullName(request.getDoctorID(), Role.DOCTOR.value());
                receiverName = Functions.fullDoctorNameShortConvert(receiverName);
                EventsFunctions.MessageEvent(receiverName, senderName, request.getNote());
            }
        }).start();
    }

    public boolean isExists(String patientID, String doctorID)
            throws ConnectionException, SQLException, NullPointerException {
        if (TextUtils.isEmpty(patientID) || TextUtils.isEmpty(doctorID)) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.iPeakDal.isExists(patientID, doctorID);
    }

    public ResponseEntitySet<Boolean> isFirstVisitFree(String patientID, String doctorID) {
        if (TextUtils.isEmpty(patientID) || TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.iPeakDal.isFirstVisitFree(patientID, doctorID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public boolean isExistsActiveOrWait(String patientID, String doctorID)
            throws ConnectionException, SQLException, NullPointerException {
        if (TextUtils.isEmpty(patientID) || TextUtils.isEmpty(doctorID)) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.iPeakDal.isExistsActiveOrWait(patientID, doctorID);
    }

    public boolean isExistsPeakUser(String peakID, String userID, byte role)
            throws ConnectionException, SQLException, NullPointerException {
        if (TextUtils.isEmpty(peakID) || TextUtils.isEmpty(userID) || role == -1) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.iPeakDal.isExistsPeakUser(peakID, userID, role);
    }

    private boolean feeControl(String patientID, String doctorID, int peakDay, int fee)
            throws ConnectionException, SQLException, NullPointerException {
        if (TextUtils.isEmpty(patientID) || TextUtils.isEmpty(doctorID)
                || !(Fee.peakTime.contains(peakDay) || Fee.peakBigTime.contains(peakDay))
                || !Fee.feeList.contains(fee)) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.iPeakDal.feeControl(patientID, doctorID, peakDay, fee);
    }

    public ResponseEntitySet<PeakPayInformation> pay(PeakPayRequest request) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (payControl(request.getPeakID(), request.getFee())) {
                return this.iPeakDal.pay(request);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.inValidPeakPay);
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public boolean payControl(String peakID, int fee) throws ConnectionException, SQLException, NullPointerException {
        if (TextUtils.isEmpty(peakID) || !Fee.feeList.contains(fee)) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.iPeakDal.payControl(peakID, fee);
    }

    public ResponseEntitySet<List<Peak>> list(String userID, byte role) {
        if (TextUtils.isEmpty(userID) || role < 0) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, role)) {
                return this.iPeakDal.list(userID, role);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Peak> detail(String peakID, String userID, byte role) {
        if (TextUtils.isEmpty(userID) || role < 0) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, role)) {
                if (isExistsPeakUser(peakID, userID, role)) {
                    return this.iPeakDal.detail(peakID, role);
                } else {
                    return new ResponseEntitySet<>(false, ErrorMessages.notAccessPeakInformation);
                }
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity peakDemandVerify(PeakDemandVerifyRequest request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getUserID(), Role.DOCTOR.value())) {
                ResponseEntitySet<DemandVerify> response = this.iPeakDal.peakDemandVerify(request);
                if (response.isSuccess) {
                    new Thread(() -> {
                        // Doctor is notification read
                        Managers.notificationManager.readPeakNotificationLog(request.getPeakID());
                        // Notification
                        DemandVerify verify = response.getData();
                        Map<String, Object> attributes = new HashMap<>();
                        attributes.put("type", NotificationType.PEAK_NOTICE.ordinal());
                        String title;
                        String body;
                        boolean decision = verify.getStatus() != -1;
                        if (verify.getStatus() == -1) {
                            title = Messages.peakDemand;
                            body = Messages.peakDemandRejectMessage.concat(request.getNote());
                        } else {
                            int status = response.getData().getStatus();
                            if (status == 1) {
                                attributes.put("peakID", request.getPeakID());
                                title = Messages.peakDemandSuccessTitle;
                                body = String.format(Messages.peakDemandSuccess, verify.getPeakDay());
                            } else {
                                attributes.put("doctorID", request.getUserID());
                                title = Messages.peakDemandActiveSuccessTitle;
                                body = Messages.peakDemandActiveSuccess;
                            }
                            attributes.put("peak_process", verify.getStatus());
                        }
                        NotificationLog log = new NotificationLog(title, body, attributes, null);
                        log.setUserID(request.getPatientID());
                        NotificationUtils.sendNotifications(log, true, false);
                        // End

                        if (!Common.isLocal) {
                            // Events
                            String doctorID = Managers.peakManager.roleID(request.getPeakID(), Role.DOCTOR.value());
                            doctorID = TextUtils.isEmpty(doctorID) ? "" : doctorID;
                            String drName = Managers.userManager.getFullName(doctorID, Role.DOCTOR.value());
                            String id = request.getPatientID().concat(doctorID);
                            Map<String, Object> info = new HashMap<>();
                            info.put("AppointmentRequestKey", Functions.toSHA256(id));
                            info.put("DoctorName", Functions.fullDoctorNameShortConvert(drName));
                            info.put("Decision", decision ? "YES" : "NO");
                            IEvents events = EventsFactory.getEvents(new AzureEventsEntity(
                                    EventsType.APPOINTMENT_DECISION_EVENTS, "AppointmentDecision", info));
                            if (events != null) {
                                events.insert();
                            }
                            // End

                            // Message Event
                            if (!request.isVerify()) {
                                String senderName = Managers.userManager.getFullName(request.getUserID(),
                                        Role.DOCTOR.value());
                                senderName = Functions.fullDoctorNameShortConvert(senderName);
                                String receiverName = Managers.userManager.getFullName(request.getPatientID(),
                                        Role.PATIENT.value());
                                receiverName = Functions.fullPatientNameSortConvert(receiverName);
                                EventsFunctions.MessageEvent(receiverName, senderName, request.getNote());
                            }
                            // End
                        }
                    }).start();
                    String eventDescription;
                    String fullName = Managers.userManager.getFullName(request.getPatientID(), Role.PATIENT.value());
                    if (response.getData().getStatus() == -1) {
                        eventDescription = String.format(LogEventDescription.APPOINTMENT_DEMAND_REJECT.getMessage(),
                                TextUtils.isEmpty(fullName) ? request.getPatientID() : fullName);
                    } else {
                        eventDescription = String.format(LogEventDescription.APPOINTMENT_DEMAND_ACCEPT.getMessage(),
                                TextUtils.isEmpty(fullName) ? request.getPatientID() : fullName);
                    }
                    peakSendLog("peakDemandVerify", request.getUserID(), Role.DOCTOR.value(),
                            Functions.getClientIpAddress(hsr), Functions.getBrowserOrDevice(hsr), eventDescription);
                }
                return response;
            } else {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<String>> patientDoctorIDs(String patientID) {
        if (TextUtils.isEmpty(patientID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
        }
        try {
            if (Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())) {
                return this.iPeakDal.patientDoctorIDs(patientID);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Integer>> feeList() {
        try {
            return this.iPeakDal.feeList();
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Integer>> peakDayList() {
        try {
            return this.iPeakDal.peakDayList();
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<GoogleProduct>> googleProductList() {
        try {
            return this.iPeakDal.googleProductList();
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> firstVisitPeakActiveStatus(String patientID) {
        if (TextUtils.isEmpty(patientID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.iPeakDal.firstVisitPeakActiveStatus(patientID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public String roleID(String peakID, byte role) {
        if (TextUtils.isEmpty(peakID)) {
            return null;
        }
        try {
            return this.iPeakDal.roleID(peakID, role);
        } catch (ConnectionException e) {
            return null;
        }
    }

    private void peakSendLog(String methodName, String phoneOrUserID, byte role, String ip, String browserOrDevice,
            String eventDescription) {
        new Thread(() -> {
            try {
                Managers.logManager.create(
                        new Log(
                                "com.kodlabs.doktorumyanimda.controller",
                                PeakManager.class.getSimpleName(),
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
