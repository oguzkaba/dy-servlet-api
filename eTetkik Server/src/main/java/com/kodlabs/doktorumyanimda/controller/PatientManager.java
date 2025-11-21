package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizInformationServiceCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizServiceInformation;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoCreateRequest;
import com.kodlabs.doktorumyanimda.events.EventsFactory;
import com.kodlabs.doktorumyanimda.events.EventsType;
import com.kodlabs.doktorumyanimda.events.IEvents;
import com.kodlabs.doktorumyanimda.events.azure.AzureEventsEntity;
import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.integrations.MailTypes;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.integrations.MailData;
import com.kodlabs.doktorumyanimda.model.integrations.SmsData;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.model.log.LogEventDescription;
import com.kodlabs.doktorumyanimda.model.patient.Patient;
import com.kodlabs.doktorumyanimda.model.patient.PatientForAdmin;
import com.kodlabs.doktorumyanimda.model.patient.PatientStatusUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.inspection.Inspection;
import com.kodlabs.doktorumyanimda.model.patient.inspection.InspectionContent;
import com.kodlabs.doktorumyanimda.model.patient.notes.PatientNotesContent;
import com.kodlabs.doktorumyanimda.model.patient.notes.PatientNotesContentCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.notes.PatientNotesContentUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNo;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.user.*;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.contact.Contact;
import com.kodlabs.doktorumyanimda.model.user.ContactType;
import com.kodlabs.doktorumyanimda.model.user.LoginData;
import com.kodlabs.doktorumyanimda.model.user.LoginType;
import com.kodlabs.doktorumyanimda.model.user.UserContactInformation;
import com.kodlabs.doktorumyanimda.model.user.UserPatient;
import com.kodlabs.doktorumyanimda.service.NVIService;
import com.kodlabs.doktorumyanimda.utils.*;
import com.google.api.Http;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPatientDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

public class PatientManager {
    private final IPatientDal patientDal;

    public PatientManager(IPatientDal patientDal) {
        this.patientDal = patientDal;
    }

    public ResponseEntitySet<String> login(PatientLoginRequest request) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if (!Patterns.PHONE.matcher(request.getUname()).matches()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidPhone);
        }
        try {
            String verifyCode = Functions.generateCode();
            if (Managers.userManager.isExistsUser(request.getUname(), Role.PATIENT.value())) {
                if (Managers.userManager.deviceVerifyCountControl(request.getDeviceID())) {
                    return updateVerifyCode(request.getUname(), verifyCode);
                } else {
                    return new ResponseEntitySet<>(false, ErrorMessages.deviceVerifyCountLimitOut);
                }
            } else {
                return new ResponseEntitySet<>(false, "not_register");
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> create(PatientRegisterRequest request) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if (!Patterns.PHONE.matcher(request.getPhone()).matches()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidPhone);
        }
        try {
            String verifyCode = Functions.generateCode();
            if (Managers.userManager.isExistsUser(request.getPhone(), Role.PATIENT.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.alreadyAccount);
            } else {
                return this.create(request.getPhone(), verifyCode);
            }
        } catch (ConnectionException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    private ResponseEntitySet<String> create(String phone, String verifyCode) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(verifyCode)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.patientDal.create(phone, verifyCode);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> loginV2(PatientLoginV2Request request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        switch (LoginType.getInstance(request.getLoginType())) {
            case PHONE:
                if (!Patterns.PHONE.matcher(request.getUname()).matches()) {
                    patientLogSend("loginV2", request.getUname(), Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr),
                            LogEventDescription.LOGIN_INVALID_PHONE_UNAME.getMessage(), Role.PATIENT);
                    return new ResponseEntitySet<>(false, ErrorMessages.inValidPhone);
                }
                break;
            case TC_NUMBER:
                if (!Patterns.TC_NO.matcher(request.getUname()).matches()) {
                    patientLogSend("loginV2", request.getUname(), Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr), LogEventDescription.LOGIN_INVALID_TC_UNAME.getMessage(),
                            Role.PATIENT);
                    return new ResponseEntitySet<>(false, ErrorMessages.inValidTcNumber);
                }
                break;
            default:
                return new ResponseEntitySet<>(false, ErrorMessages.notValidLoginType);
        }
        try {
            String verifyCode = Functions.generateCode();
            ResponseEntitySet<LoginData> response = this.patientDal.loginV2(request, verifyCode);
            if (response.isSuccess) {
                LoginData loginData = response.getData();
                if (!Common.isLocal && !Phones.isContains(request.getUname(), false)) {
                    IIntegrations integration = IntegrationsFactory
                            .getIntegrations(new SmsData(String.format(Messages.smsLoginVerifyMessage,
                                    loginData.getCode()), loginData.getPhone()), IntegrationsFactory.SMS);
                    if (integration != null) {
                        boolean result = integration.sendMessage();
                        if (!result) {
                            return new ResponseEntitySet<>(false, ErrorMessages.smsSendFailed);
                        } else {
                            return new ResponseEntitySet<>(loginData.getPhone());
                        }
                    } else {
                        return new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
                    }
                } else {
                    return new ResponseEntitySet<>(loginData.getPhone());
                }
            } else {
                patientLogSend("loginV2", request.getUname(), Functions.getClientIpAddress(hsr),
                        Functions.getBrowserOrDevice(hsr), LogEventDescription.LOGIN_FAILED.getMessage(), Role.PATIENT);
                return new ResponseEntitySet<>(false, response.errorCode, response.message);
            }
        } catch (ConnectionException | NullPointerException e) {
            patientLogSend("loginV2", request.getUname(), Functions.getClientIpAddress(hsr),
                    Functions.getBrowserOrDevice(hsr), LogEventDescription.LOGIN_FAILED.getMessage(), Role.PATIENT);
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<UserPatient> singUpV2(PatientSingUpV2Request request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            // Verify NVI isLocal
            if (Common.isLocal) {
                ResponseEntitySet<UserPatient> response = this.patientDal.singUpV2(request);
                if (response.isSuccess && !Phones.isContains(request.getPhone(), false)) {
                    new Thread(() -> {
                        Map<String, Object> info = new HashMap<>();
                        String phone = request.getPhone().trim();
                        String dialCode = phone.substring(0, phone.length() - 10);
                        info.put("CountryCode", dialCode.replace("+", ""));
                        IEvents events = EventsFactory
                                .getEvents(new AzureEventsEntity(EventsType.NEW_USER_EVENTS, "NewUser", info));
                        if (events != null) {
                            events.insert();
                        }
                    }).start();
                }
                if (response.isSuccess) {
                    ResponseEntitySet<UserContactInformation> responseContact = Managers.userManager
                            .userContactInformation(request.getTcNumber(), Role.PATIENT.value());
                    if (responseContact.isSuccess) {
                        IIntegrations integrations;
                        String userName = Functions.nameFormat(responseContact.getData().getName());
                        MailData mailData = MailData.builder()
                                .title(Messages.newAccountTitle)
                                .address(responseContact.getData().getMail())
                                .type(MailTypes.VERIFY.getType())
                                .userName(userName != null ? userName : "").build();
                        switch (ContactType.MAIL) {
                            case MAIL:
                                integrations = IntegrationsFactory.getIntegrations(
                                        mailData,
                                        IntegrationsFactory.MAIL);
                                break;
                            default:
                                integrations = null;
                        }
                        if (integrations != null) {
                            boolean isSuccess = integrations.sendMessage();
                            if (!isSuccess) {
                                System.out.println("Patient singUpV2 send mail error: ".concat(response.message));
                            }
                        }
                    }

                    patientLogSend("singUpV2", response.getData().getUserID(), Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr), LogEventDescription.SING_UP.getMessage(), Role.PATIENT);
                }
                return response;
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notVerifyUserIdentity);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        } catch (NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity newVerifyCode(String phone, String deviceID, String code) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(deviceID) || TextUtils.isEmpty(code)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(phone, Role.PATIENT.value())) {
                if (Managers.userManager.deviceVerifyCountControl(deviceID)) {
                    ResponseEntity response = updateVerifyCode(phone, code);
                    if (!Common.isLocal && response.isSuccess && !Phones.isContains(phone, false)) {
                        IIntegrations integration = IntegrationsFactory.getIntegrations(
                                new SmsData(String.format(Messages.smsLoginVerifyMessage, code), phone),
                                IntegrationsFactory.SMS);
                        if (integration != null) {
                            boolean result = integration.sendMessage();
                            if (result) {
                                ResponseEntity res = Managers.userManager.deviceVerifyCountIncrease(deviceID);
                                if (!res.isSuccess) {
                                    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss\n",
                                            Locale.getDefault());
                                    System.out.println(format.format(Calendar.getInstance().getTime())
                                            .concat("Patient Login New Code increase error: ".concat(res.message)));
                                }
                            } else {
                                response.isSuccess = false;
                                response.message = ErrorMessages.smsSendFailed;
                            }
                        }
                    }
                    return response;
                } else {
                    return new ResponseEntity(false, ErrorMessages.deviceVerifyCountLimitOut);
                }
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessPatientInformation);
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    private ResponseEntitySet<String> updateVerifyCode(String phone, String verifyCode) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(verifyCode)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.patientDal.updateVerifyCode(phone, verifyCode);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Contact> contact(String phoneOrUserID) {
        if (TextUtils.isEmpty(phoneOrUserID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.patientDal.contact(phoneOrUserID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<UserPatient> loginVerify(PatientLoginVerifyRequest request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            ResponseEntitySet<UserPatient> response = this.patientDal.loginVerify(request);
            if (response.isSuccess && !Common.isLocal) {
                new Thread(() -> {
                    Map<String, Object> info = new HashMap<>();
                    IEvents events = EventsFactory
                            .getEvents(new AzureEventsEntity(EventsType.APPROVAL_CODE_EVENTS, "ApprovalCode", info));
                    if (events != null) {
                        events.insert();
                    }
                }).start();
            }
            if (response.isSuccess) {
                patientLogSend("loginVerify", response.getData().getUserID(), Functions.getClientIpAddress(hsr),
                        Functions.getBrowserOrDevice(hsr), LogEventDescription.LOGIN.getMessage(), Role.PATIENT);
            } else {
                patientLogSend("loginVerify", request.getPhone(), Functions.getClientIpAddress(hsr),
                        Functions.getBrowserOrDevice(hsr), LogEventDescription.LOGIN_VERIFY_FAILED.getMessage(),
                        Role.PATIENT);
            }
            return response;
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    // Admin & Facility Admin Patient Delete
    public ResponseEntity delete(String userID, Byte role, String patientID, HttpServletRequest hsr) {
        if (TextUtils.isEmpty(userID) || TextUtils.isEmpty(patientID) || role == null
                || !(role == Role.ADMIN.value() || role == Role.FACILITY_ADMIN.value())) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, role)) {
                if (Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())) {
                    String patientName = Managers.userManager.getFullName(patientID, Role.PATIENT.value());
                    String tempPatientID = patientID;
                    String fullName = Managers.userManager.getFullName(userID, role);
                    ResponseEntity response = this.patientDal.delete(userID, role, patientID);
                    if (response.isSuccess) {
                        patientLogSend("delete", tempPatientID, Functions.getClientIpAddress(hsr),
                                Functions.getBrowserOrDevice(hsr),
                                String.format(LogEventDescription.ACCOUNT_DELETE_FOR_ADMIN.getMessage(),
                                        TextUtils.isEmpty(fullName) ? userID : fullName),
                                Role.ADMIN);
                        return response;
                    } else {
                        patientLogSend("delete", tempPatientID, Functions.getClientIpAddress(hsr),
                                Functions.getBrowserOrDevice(hsr),
                                String.format(LogEventDescription.ACCOUNT_DELETE_FAILED_FOR_ADMIN.getMessage(),
                                        TextUtils.isEmpty(fullName) ? userID : fullName),
                                Role.ADMIN);
                        return new ResponseEntity(false, ErrorMessages.operationFailed);

                    }
                } else {
                    return new ResponseEntity(false, ErrorMessages.notAccessPatientInformation);
                }
            } else {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    // Patient Self Delete
    public ResponseEntity selfDelete(String userID, HttpServletRequest hsr) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, Role.PATIENT.value())) {
                String patientName = Managers.userManager.getFullName(userID, Role.PATIENT.value());
                String patientID = userID;
                ResponseEntity response = this.patientDal.selfDelete(userID);
                if (response.isSuccess) {
                    patientLogSend("selfDelete", patientID, Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr),
                            String.format(LogEventDescription.ACCOUNT_DELETE.getMessage(),
                                    TextUtils.isEmpty(patientName) ? patientID : patientName),
                            Role.PATIENT);
                    return response;
                } else {
                    patientLogSend("selfDelete", patientID, Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr),
                            String.format(LogEventDescription.ACCOUNT_DELETE_FAILED.getMessage(),
                                    TextUtils.isEmpty(patientName) ? patientID : patientName),
                            Role.PATIENT);
                    return new ResponseEntity(false, ErrorMessages.operationFailed);

                }
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessPatientInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Patient>> getAllDoctorPatient(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return this.patientDal.getAllDoctorPatients(doctorID);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<PatientForAdmin>> getAllPatient(String userID) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, Role.FACILITY_ADMIN.value()) ||
                    Managers.userManager.isExistsUser(userID, Role.ADMIN.value())) {
                return this.patientDal.getAllPatients();
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Patient> information(String patientID) {
        if (TextUtils.isEmpty(patientID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.patientDal.information(patientID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    /* Patient Status */
    public ResponseEntity patientStatusUpdate(PatientStatusUpdateRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getPatientID(), Role.PATIENT.value())) {
                return this.patientDal.patientStatusUpdate(request);
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessPatientInformation);
            }
        } catch (ConnectionException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> patientGetStatus(String patientID) {
        if (TextUtils.isEmpty(patientID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.patientDal.patientGetStatus(patientID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Patient>> doctorOldPatients(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return this.patientDal.doctorOldPatients(doctorID);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    private void patientLogSend(String methodName, final String phoneOrUserID, String ip, String browserOrDevice,
            String eventDescription, Role role) {
        new Thread(() -> {
            try {
                Managers.logManager.create(
                        new Log(
                                "com.kodlabs.doktorumyanimda.controller",
                                PatientManager.class.getSimpleName(),
                                methodName,
                                phoneOrUserID,
                                role.value(),
                                ip,
                                Functions.getIpAddress(),
                                eventDescription,
                                browserOrDevice));
            } catch (UnknownHostException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();
    }
    /* End */

    /* Inspection */
    public ResponseEntitySet<List<Inspection>> patientInspectionList(String patientID, String doctorID) {
        if (TextUtils.isEmpty(patientID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_PATIENT_ID);
        }
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_DOCTOR_ID);
        }
        try {
            if (!Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessPatientInformation);
            }
            if (!Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessDoctorInformation);
            }
            return this.patientDal.inspectionList(patientID, doctorID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity inspectionDelete(String inspectionID) {
        if (TextUtils.isEmpty(inspectionID)) {
            return new ResponseEntity(false, ErrorMessages.inValidInspectionId);
        }
        try {
            return this.patientDal.inspectionDelete(inspectionID);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<InspectionContent>> inspectionContentList(String id) {
        if (TextUtils.isEmpty(id)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidInspectionId);
        }
        try {
            if (this.patientDal.inspectionExists(id)) {
                return this.patientDal.inspectionContentList(id);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notFoundInspectionInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity inspectionContentCreate(HttpServletRequest hsr, InspectionContent inspectionContent) {
        if (inspectionContent == null || !inspectionContent.isValid()
                || TextUtils.isEmpty(inspectionContent.getInspectionID())) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (this.patientDal.inspectionExists(inspectionContent.getInspectionID())) {
                ResponseEntitySet<String> response = this.patientDal.inspectionContentCreate(inspectionContent);
                if (response.isSuccess) {
                    String id = new String(response.getData());
                    if (TextUtils.isEmpty(id)) {
                        id = new String(inspectionContent.getInspectionID());
                    }
                    patientLogSend("inspectionContentCreate", id, Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr),
                            LogEventDescription.INSPECTION_CONTENT_CREATE.getMessage(), Role.DOCTOR);
                }
                return response;
            } else {
                return new ResponseEntity(false, ErrorMessages.notFoundInspectionInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity inspectionContentUpdate(HttpServletRequest hsr, InspectionContent inspectionContent) {
        if (inspectionContent == null || !inspectionContent.isValid() || TextUtils.isEmpty(inspectionContent.getId())) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (this.patientDal.inspectionContentExists(inspectionContent.getId())) {
                ResponseEntity response = this.patientDal.inspectionContentUpdate(inspectionContent);
                if (response.isSuccess) {
                    patientLogSend("inspectionContentUpdate", inspectionContent.getId(),
                            Functions.getClientIpAddress(hsr), Functions.getBrowserOrDevice(hsr),
                            LogEventDescription.INSPECTION_CONTENT_UPDATE.getMessage(), Role.DOCTOR);
                }
                return response;
            } else {
                return new ResponseEntity(false, ErrorMessages.notFoundInspectionContentInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity inspectionContentDelete(HttpServletRequest hsr, String inspectionContentID) {
        if (TextUtils.isEmpty(inspectionContentID)) {
            return new ResponseEntity(false, ErrorMessages.inValidInspectionContentId);
        }
        try {
            ResponseEntity response = this.patientDal.inspectionContentDelete(inspectionContentID);
            if (response.isSuccess) {
                patientLogSend("inspectionContentDelete", inspectionContentID, Functions.getClientIpAddress(hsr),
                        Functions.getBrowserOrDevice(hsr), LogEventDescription.INSPECTION_CONTENT_DELETE.getMessage(),
                        Role.DOCTOR);
            }
            return response;
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    /* Inspection End */

    /* Patient Notes */
    public ResponseEntitySet<String> notesCreate(String patientID, String doctorID) {
        if (TextUtils.isEmpty(patientID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_PATIENT_ID);
        }
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_DOCTOR_ID);
        }
        try {
            if (!Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.INVALID_PATIENT_ID);
            }
            if (!Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.INVALID_DOCTOR_ID);
            }
            if (this.patientDal.noteExist(patientID, doctorID)) {
                return this.patientDal.notes(patientID, doctorID);
            } else {
                return this.patientDal.notesCreate(patientID, doctorID);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<PatientNotesContent>> noteContents(String noteID) {
        if (TextUtils.isEmpty(noteID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_PATIENT_NOTES_CONTENT_ID);
        }
        try {
            return this.patientDal.noteContents(noteID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<PatientNotesContent> noteContentCreate(PatientNotesContentCreateRequest request) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.patientDal.noteContentCreate(request);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity noteContentUpdate(PatientNotesContentUpdateRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.patientDal.noteContentUpdate(request);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity noteContentDelete(String contentID) {
        if (TextUtils.isEmpty(contentID)) {
            return new ResponseEntity(false, ErrorMessages.INVALID_PATIENT_NOTES_CONTENT_ID);
        }
        try {
            return this.patientDal.noteContentDelete(contentID);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    /* Patient Notes End */

    /* Patient SysTakipNo */
    public ResponseEntitySet<List<PatientSysTakipNo>> sysTakipNoList(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_DOCTOR_ID);
        }
        try {
            if (!Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessDoctorInformation);
            }
            return this.patientDal.sysTakipNoList(doctorID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity sysTakipNoCreate(HttpServletRequest hsr, PatientSysTakipNoCreateRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (!Patterns.TC_NO.matcher(request.getTcNumber()).matches()) {
                return new ResponseEntity(false, ErrorMessages.inValidTcNumber);
            }
            if (!Managers.userManager.isExistsUser(request.getTcNumber(), Role.PATIENT.value())) {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
            if (!Managers.userManager.isExistsUser(request.getDoctorID(), Role.DOCTOR.value())) {
                return new ResponseEntity(false, ErrorMessages.notAccessDoctorInformation);
            }
            if (this.patientDal.sysTakipNoExists(request.getTcNumber(), request.getDoctorID())) {
                return new ResponseEntity(false, ErrorMessages.ALREADY_SYSTAKIPNO_FOR_INFORMATION);
            }
            ResponseEntity response = this.patientDal.sysTakipNoCreate(request);
            if (response.isSuccess) {
                patientLogSend("sysTakipNoCreate", request.getDoctorID(), Functions.getClientIpAddress(hsr),
                        Functions.getBrowserOrDevice(hsr),
                        String.format(LogEventDescription.SYS_TAKIP_NO_CREATE.getMessage(), request.getTcNumber()),
                        Role.DOCTOR);
            }
            return response;
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> sysTakipNo(String tcNumber, String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_DOCTOR_ID);
        }
        if (TextUtils.isEmpty(tcNumber) || !Patterns.TC_NO.matcher(tcNumber).matches()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidTcNumber);
        }
        try {
            if (!Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessDoctorInformation);
            }
            if (!Managers.userManager.isExistsUser(tcNumber, Role.PATIENT.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessPatientInformation);
            }
            if (!patientDal.sysTakipNoExists(tcNumber, doctorID)) {
                return new ResponseEntitySet<>(false, ErrorMessages.NOT_ACCESS_SYSTAKIPNO_INFORMATION);
            }
            return this.patientDal.sysTakipNo(tcNumber, doctorID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity sysTakipNoUpdate(HttpServletRequest hsr, PatientSysTakipNoUpdateRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (!Patterns.TC_NO.matcher(request.getTcNumber()).matches()) {
                return new ResponseEntity(false, ErrorMessages.inValidTcNumber);
            }
            if (!Managers.userManager.isExistsUser(request.getTcNumber(), Role.PATIENT.value())) {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
            if (!Managers.userManager.isExistsUser(request.getDoctorID(), Role.DOCTOR.value())) {
                return new ResponseEntity(false, ErrorMessages.notAccessDoctorInformation);
            }
            if (!this.patientDal.sysTakipNoExists(request.getTcNumber(), request.getDoctorID())) {
                return new ResponseEntity(false, ErrorMessages.NOT_ACCESS_SYSTAKIPNO_INFORMATION);
            }
            ResponseEntity response = this.patientDal.sysTakipNoUpdate(request);
            if (response.isSuccess) {
                if (response.isSuccess) {
                    patientLogSend("sysTakipNoUpdate", request.getDoctorID(), Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr),
                            String.format(LogEventDescription.SYS_TAKIP_NO_CREATE.getMessage(), request.getTcNumber()),
                            Role.DOCTOR);
                }
            }
            return response;
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    /* Patient SysTakipNo End */

    /* Patient Enabiz Service Information */
    public ResponseEntitySet<List<EnabizServiceInformation>> getEnabizServiceInformation(String sysTakipNo) {
        if (TextUtils.isEmpty(sysTakipNo)) {
            return new ResponseEntitySet<>(false, ErrorMessages.INVALID_SYSTAKIPNO);
        }
        try {
            if (this.patientDal.sysTakipNoExists(sysTakipNo)) {
                return this.patientDal.getEnabizServiceInformation(sysTakipNo);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.INVALID_SYSTAKIPNO);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity createEnabizServiceInformation(EnabizInformationServiceCreateRequest request) {
        if (!request.valid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (!patientDal.sysTakipNoExists(request.getSysTakipNo())) {
                return new ResponseEntity(false, ErrorMessages.INVALID_SYSTAKIPNO);
            }
            return this.patientDal.createEnabizServiceInformation(request);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity deleteEnabizServiceInformation(String serviceReferenceNumber) {
        if (TextUtils.isEmpty(serviceReferenceNumber)) {
            return new ResponseEntity(false, ErrorMessages.INVALID_SERVICE_REFERENCE_NUMBER);
        }
        try {
            if (this.patientDal.existsEnabizServiceReferenceNumber(serviceReferenceNumber)) {
                return this.patientDal.deleteEnabizServiceInformation(serviceReferenceNumber);
            } else {
                return new ResponseEntity(false, ErrorMessages.INVALID_SERVICE_REFERENCE_NUMBER);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    /* Patient Enabiz Service Information End */
}
