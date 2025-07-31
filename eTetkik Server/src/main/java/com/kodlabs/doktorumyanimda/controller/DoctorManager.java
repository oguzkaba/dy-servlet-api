package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IDoctorDal;
import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.messages.ErrorMessage;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.ckys.CkysRequest;
import com.kodlabs.doktorumyanimda.model.ckys.calisma.CalismaSonuc;
import com.kodlabs.doktorumyanimda.model.ckys.calisma.CkysCalismaBilgisiResponse;
import com.kodlabs.doktorumyanimda.model.ckys.tahsil.CkysTahilBilgisiResponse;
import com.kodlabs.doktorumyanimda.model.ckys.tahsil.TahsilSonuc;
import com.kodlabs.doktorumyanimda.model.contact.Contact;
import com.kodlabs.doktorumyanimda.model.doctor.*;
import com.kodlabs.doktorumyanimda.model.doctor.contract.ContractAcceptRequest;
import com.kodlabs.doktorumyanimda.model.doctor.detail.DoctorDetail;
import com.kodlabs.doktorumyanimda.model.doctor.detail.DoctorDetailUpdateRequest;
import com.kodlabs.doktorumyanimda.model.doctor.peak.PeakFeeDetail;
import com.kodlabs.doktorumyanimda.model.doctor.peak.PeakFeeRequest;
import com.kodlabs.doktorumyanimda.model.doctor.recipe.DoctorRecipeInformation;
import com.kodlabs.doktorumyanimda.model.doctor.recipe.DoctorRecipeInformationCreate;
import com.kodlabs.doktorumyanimda.model.doctor.registration.RegistrationInformation;
import com.kodlabs.doktorumyanimda.model.doctor.user.*;
import com.kodlabs.doktorumyanimda.model.integrations.SmsData;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.model.log.LogEventDescription;
import com.kodlabs.doktorumyanimda.model.social.SocialAccount;
import com.kodlabs.doktorumyanimda.model.social.SocialRequest;
import com.kodlabs.doktorumyanimda.model.social.SocialShare;
import com.kodlabs.doktorumyanimda.model.user.LoginData;
import com.kodlabs.doktorumyanimda.model.user.LoginType;
import com.kodlabs.doktorumyanimda.model.user.UserDoctor;
import com.kodlabs.doktorumyanimda.service.CkysService;
import com.kodlabs.doktorumyanimda.service.NVIService;
import com.kodlabs.doktorumyanimda.utils.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoctorManager {
    private final IDoctorDal doctorDal;

    public DoctorManager(IDoctorDal doctorDal) {
        this.doctorDal = doctorDal;
    }

    public ResponseEntity login(DoctorLoginRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        if (!Patterns.PHONE.matcher(request.getUname()).matches()) {
            return new ResponseEntity(false, ErrorMessages.inValidPhone);
        }
        try {
            // if result = -1 contract accepted else result doctor type
            byte result = isContractAccepted(request.getUname());
            if (result == -1) {
                if (Managers.userManager.deviceVerifyCountControl(request.getDeviceID())) {
                    String verifyCode = Functions.generateCode();
                    ResponseEntitySet<String> response = updateVerifyCode(request.getUname(), verifyCode);
                    if (!Common.isLocal && response.isSuccess && !Phones.isContains(request.getUname(), true)) {
                        IIntegrations integration = IntegrationsFactory.getIntegrations(
                                new SmsData(String.format(Messages.smsLoginVerifyMessage, response.getData()),
                                        request.getUname()),
                                IntegrationsFactory.SMS);
                        if (integration != null) {
                            boolean smsmResult = integration.sendMessage();
                            if (smsmResult) {
                                ResponseEntity res = Managers.userManager
                                        .deviceVerifyCountIncrease(request.getDeviceID());
                                if (!res.isSuccess) {
                                    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss\n",
                                            Locale.getDefault());
                                    System.out.println(format.format(Calendar.getInstance().getTime())
                                            .concat("Doctor Login verify increase error: ".concat(res.message)));
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
                return new ResponseEntity(false, "contract_accept#".concat(String.valueOf(result)));
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> loginV2(DoctorLoginV2Request request, String ip) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        switch (LoginType.getInstance(request.getType())) {
            case TC_NUMBER:
                if (!Patterns.TC_NO.matcher(request.getUname()).matches()) {
                    doctorLogSend("loginV2", request.getUname(), ip,
                            LogEventDescription.LOGIN_INVALID_TC_UNAME.getMessage());
                    return new ResponseEntitySet<>(false, ErrorMessages.inValidTcNumber);
                }
                break;
            case PHONE:
                if (!Patterns.PHONE.matcher(request.getUname()).matches()) {
                    doctorLogSend("loginV2", request.getUname(), ip,
                            LogEventDescription.LOGIN_INVALID_PHONE_UNAME.getMessage());
                    return new ResponseEntitySet<>(false, ErrorMessages.inValidPhone);
                }
                break;
            default:
                return new ResponseEntitySet<>(false, ErrorMessages.inValidLoginType);
        }
        try {
            byte result = isContractAccepted(request.getUname());
            if (result == -1) {
                String verifyCode = Functions.generateCode();
                ResponseEntitySet<LoginData> response = this.doctorDal.loginV2(request, verifyCode);
                LoginData loginData = response.getData();
                if (response.isSuccess) {
                    if (!Common.isLocal && !Phones.isContains(request.getUname(), true)) {
                        IIntegrations integration = IntegrationsFactory.getIntegrations(
                                new SmsData(String.format(Messages.smsLoginVerifyMessage, loginData.getCode()),
                                        loginData.getPhone()),
                                IntegrationsFactory.SMS);
                        if (integration != null) {
                            boolean integrationResult = integration.sendMessage();
                            if (!integrationResult) {
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
                    doctorLogSend("loginV2", request.getUname(), ip, LogEventDescription.LOGIN_FAILED.getMessage());
                }
                return new ResponseEntitySet<>(false, response.errorCode, response.message);
            } else {
                return new ResponseEntitySet<>(false, "contract_accept#".concat(String.valueOf(result)));
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            doctorLogSend("loginV2", request.getUname(), ip, LogEventDescription.LOGIN_FAILED.getMessage());
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity newVerifyCode(String phone, String deviceID) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(deviceID)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(phone, Role.DOCTOR.value())) {
                if (Managers.userManager.deviceVerifyCountControl(deviceID)) {
                    String code = Functions.generateCode();
                    ResponseEntitySet<String> response = updateVerifyCode(phone, code);
                    if (!Common.isLocal && response.isSuccess && !Phones.isContains(phone, true)) {
                        IIntegrations integration = IntegrationsFactory.getIntegrations(
                                new SmsData(String.format(Messages.smsLoginVerifyMessage, response.getData()), phone),
                                IntegrationsFactory.SMS);
                        if (integration != null) {
                            boolean result = integration.sendMessage();
                            if (result) {
                                ResponseEntity res = Managers.userManager.deviceVerifyCountIncrease(deviceID);
                                if (!res.isSuccess) {
                                    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss\n",
                                            Locale.getDefault());
                                    System.out.println(format.format(Calendar.getInstance().getTime())
                                            .concat("Doctor Login New Code increase error: ".concat(res.message)));
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
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
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
            return this.doctorDal.updateVerifyCode(phone, verifyCode);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<UserDoctor> loginVerify(DoctorLoginVerifyRequest request, String ip) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            ResponseEntitySet<UserDoctor> response = this.doctorDal.loginVerify(request);
            if (response.isSuccess) {
                doctorLogSend("loginVerify", request.getPhone(), ip, LogEventDescription.LOGIN.getMessage());
            } else {
                doctorLogSend("loginVerify", request.getPhone(), ip,
                        LogEventDescription.LOGIN_VERIFY_FAILED.getMessage());
            }
            return response;
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity register(DoctorRegister request, String ip) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        if (!Patterns.DOCTOR_CODE_V2.matcher(request.getDoctorCode()).matches()) {
            return new ResponseEntity(false, ErrorMessages.inValidDoctorCode);
        }
        if (!Patterns.TC_NO.matcher(request.getTcNumber()).matches()) {
            return new ResponseEntity(false, ErrorMessages.inValidTcNumber);
        }
        if (!Patterns.PHONE.matcher(request.getContact().getPhone()).matches()) {
            return new ResponseEntity(false, ErrorMessages.inValidPhone);
        }
        try {
            if (!(Managers.userManager.isExistsUser(request.getAdminID(), Role.ADMIN.value()) ||
                    Managers.userManager.isExistsUser(request.getAdminID(), Role.FACILITY_ADMIN.value()))) {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
            if (isExistsID(request.getDoctorCode())) {
                return new ResponseEntity(false, ErrorMessages.availableDoctorCode);
            }
            if (Managers.userManager.isExistsUser(request.getContact().getPhone(), Role.DOCTOR.value())) {
                return new ResponseEntity(false, ErrorMessages.availablePhoneNumber);
            }
            if (Managers.userManager.isExistsUser(request.getTcNumber(), Role.DOCTOR.value())) {
                return new ResponseEntity(false, ErrorMessages.availableTcNumber);
            }
            ResponseEntity response = this.doctorDal.register(request);
            if (response.isSuccess) {
                String fullName = Managers.userManager.getFullName(request.getAdminID(), Role.ADMIN.value());
                doctorLogSend("register", request.getTcNumber(), ip,
                        String.format(LogEventDescription.SING_UP_FOR_ADMIN.getMessage(),
                                TextUtils.isEmpty(fullName) ? request.getAdminID() : fullName));
            }
            return response;
        } catch (ConnectionException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public boolean isExistsID(String ID) throws ConnectionException, NullPointerException {
        if (TextUtils.isEmpty(ID)) {
            throw new NullPointerException(ErrorMessages.inValidCode);
        }
        return this.doctorDal.isExistsID(ID);
    }

    public ResponseEntitySet<List<DoctorInformationForAdmin>> doctorList(String userID, String type) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        Byte role = AdminRole.getUserRole(type);
        if (role == null) {
            return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, role)) {
                return this.doctorDal.list(userID, role);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<DoctorInformation>> notAnonymousDoctorList(String branch, String city, String os) {
        try {
            return this.doctorDal.notAnonymousList(branch, city, os);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<DoctorPayInformation> payInformation(String drCodeOrID) {
        if (TextUtils.isEmpty(drCodeOrID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if (drCodeOrID.length() <= 10 && !(Patterns.DOCTOR_CODE.matcher(drCodeOrID).matches()
                || Patterns.DOCTOR_CODE_V2.matcher(drCodeOrID).matches())) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidDoctorCode);
        }
        try {
            return this.doctorDal.payInformation(drCodeOrID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<DoctorInformation> doctorInformation(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return this.doctorDal.information(doctorID);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity doctorUpdate(DoctorUpdateRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getAdminUserID(), Role.ADMIN.value())) {
                return this.doctorDal.update(request);
            } else {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Contact> contact(String uNameOrID) {
        if (TextUtils.isEmpty(uNameOrID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.contact(uNameOrID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity changePassword(ChangePasswordRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getUserID(), request.getRole())) {
                return this.doctorDal.changePassword(request);
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity resetPassword(ResetPasswordRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getUname(), request.getRole())) {
                boolean isSuccess = Managers.userManager.setAttribute(request.getUname(), request.getRole(), "password",
                        request.getPassword());
                if (isSuccess) {
                    return new ResponseEntity();
                } else {
                    return new ResponseEntity(false, ErrorMessages.changePasswordFailed);
                }
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException | SQLException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<DoctorDetail> detail(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return this.doctorDal.detail(doctorID);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity detailUpdate(DoctorDetailUpdateRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getUserID(), Role.DOCTOR.value())) {
                return this.doctorDal.detailUpdate(request);
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    /* Peak Fee */
    public ResponseEntitySet<List<PeakFeeDetail>> peakFeeDetail(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return this.doctorDal.peakFeeDetail(doctorID);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity peakFeeDetailUpdate(PeakFeeRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getUserID(), Role.DOCTOR.value())) {
                return this.doctorDal.peakFeeDetailUpdate(request);
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Integer> drBalance(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return this.doctorDal.drBalance(doctorID);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> peakFeeDetailAdd(PeakFeeRequest request) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.peakFeeDetailAdd(request);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity peakFeeDetailDelete(String doctorID, String id) {
        if (TextUtils.isEmpty(doctorID) || TextUtils.isEmpty(id)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.peakFeeDetailDelete(doctorID, id);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<SocialAccount>> socialAccounts(String userID) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.socialAccounts(userID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity socialAccountDelete(String id) {
        if (TextUtils.isEmpty(id)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.socialAccountDelete(id);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> socialAccountAdd(SocialRequest<SocialAccount> request) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.socialAccountAdd(request);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<SocialShare>> socialShares(String userID) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.socialShares(userID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity socialShareDelete(String id) {
        if (TextUtils.isEmpty(id)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.socialShareDelete(id);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<String> socialShareAdd(SocialRequest<SocialShare> request) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.socialShareAdd(request);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<String>> doctorAvailableCities() {
        try {
            return this.doctorDal.doctorAvailableCities();
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public byte isContractAccepted(String doctorID) throws NullPointerException, SQLException, ConnectionException {
        if (TextUtils.isEmpty(doctorID)) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.doctorDal.isContractAccepted(doctorID);
    }

    public ResponseEntity contractAccept(ContractAcceptRequest request, String ip) {
        if (request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            ResponseEntity response = this.doctorDal.contractAccept(request.getPhone());
            if (response.isSuccess) {
                doctorLogSend("contractAccept", request.getPhone(), ip,
                        LogEventDescription.ACCEPT_CONTRACT.getMessage());
                return Managers.doctorManager.newVerifyCode(request.getPhone(), request.getDeviceID());
            }
            return response;
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Integer> peakDemandCount(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return this.doctorDal.peakDemandCount(doctorID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Branch>> branches() {
        try {
            return this.doctorDal.branches();
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity delete(String userID, Byte role, String doctorID, String ip) {
        if (TextUtils.isEmpty(userID) || TextUtils.isEmpty(doctorID) || role == null
                || !(role == Role.ADMIN.value() || role == Role.FACILITY_ADMIN.value())) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, role)) {
                if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                    ResponseEntity response = this.doctorDal.delete(userID, role, doctorID);
                    String fullName = Managers.userManager.getFullName(userID, role);
                    doctorLogSend("delete", doctorID, ip,
                            String.format(LogEventDescription.ACCOUNT_DELETE_FOR_ADMIN.getMessage(),
                                    TextUtils.isEmpty(fullName) ? userID : fullName));
                    return response;
                } else {
                    return new ResponseEntity(false, ErrorMessages.notAccessDoctorInformation);
                }
            } else {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<DoctorAllDetail> allDetail(String userID, Byte userRole, String doctorID) {
        if (TextUtils.isEmpty(userID) || userRole == null
                || !(userRole == Role.ADMIN.value() || userRole == Role.FACILITY_ADMIN.value())
                || TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, userRole)) {
                if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                    return this.doctorDal.allDetail(userID, userRole, doctorID);
                } else {
                    return new ResponseEntitySet<>(false, ErrorMessages.notAccessDoctorInformation);
                }
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity updateSideAdmin(DoctorUpdateSideAdminRequest request, String ip) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        if (!Patterns.TC_NO.matcher(request.getProfile().getTcNumber()).matches()) {
            return new ResponseEntity(false, ErrorMessages.inValidTcNumber);
        }
        try {
            Byte role = AdminRole.getUserRole(request.getType());
            if (role == null) {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
            if (Managers.userManager.isExistsUser(request.getUserID(), role)) {
                if (Managers.userManager.isExistsUser(request.getProfile().getDoctorID(), Role.DOCTOR.value())) {
                    ResponseEntity response = this.doctorDal.updateSideAdmin(request.getUserID(), role,
                            request.getProfile());
                    String fullName = Managers.userManager.getFullName(request.getUserID(),
                            AdminRole.getUserRole(request.getType()));
                    if (response.isSuccess) {
                        doctorLogSend("updateSideAdmin", request.getProfile().getDoctorID(), ip,
                                String.format(LogEventDescription.ACCOUNT_UPDATE_FOR_ADMIN.getMessage(),
                                        TextUtils.isEmpty(fullName) ? request.getUserID() : fullName));
                    }
                    return response;
                } else {
                    return new ResponseEntity(false, ErrorMessages.notAccessDoctorInformation);
                }
            } else {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<RegistrationInformation>> registrationInformation(String tcNo, String registrationNo,
            String serialNo) {
        if (TextUtils.isEmpty(tcNo) || TextUtils.isEmpty(registrationNo) || TextUtils.isEmpty(serialNo)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if (!Patterns.TC_NO.matcher(tcNo).matches()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidTcNumber);
        }
        try {
            CkysTahilBilgisiResponse response = CkysService.getInstance().tahilBilgisiSorgula(tcNo, registrationNo,
                    serialNo);
            List<RegistrationInformation> informations = new ArrayList<>();
            if (response != null && response.getTahsilBilgisiResult() != null
                    && response.getTahsilBilgisiResult().getList() != null) {
                for (TahsilSonuc ts : response.getTahsilBilgisiResult().getList()) {
                    informations.add(new RegistrationInformation(ts.getAd(), ts.getSoyad(), ts.getDogumTarihi()));
                }
            }
            return new ResponseEntitySet<>(informations);
        } catch (IOException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<TahsilSonuc>> tahsilBilgisiSorgula(CkysRequest request) {
        if (request == null || !request.valid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if (!Patterns.TC_NO.matcher(request.getTcNumber()).matches()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidTcNumber);
        }
        try {
            if (!NVIService.getInstance().verifyNvi(request)) {
                return new ResponseEntitySet<>(false, ErrorMessages.notVerifyUserIdentity);
            }
            CkysTahilBilgisiResponse response = CkysService.getInstance().tahilBilgisiSorgula(request.getTcNumber(),
                    request.getRegistrationNo(), request.getSerialNo());
            List<TahsilSonuc> informations = new ArrayList<>();
            if (response != null && response.getTahsilBilgisiResult() != null
                    && response.getTahsilBilgisiResult().getList() != null) {
                return new ResponseEntitySet<>(Stream.of(response.getTahsilBilgisiResult().getList())
                        .collect(Collectors.toList()));
            }
            return new ResponseEntitySet<>(informations);
        } catch (IOException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<CalismaSonuc>> calismaBilgisiSorgula(String tcNo, String registrationNo,
            String serialNo) {
        if (TextUtils.isEmpty(tcNo) || TextUtils.isEmpty(registrationNo) || TextUtils.isEmpty(serialNo)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if (!Patterns.TC_NO.matcher(tcNo).matches()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidTcNumber);
        }
        try {
            CkysCalismaBilgisiResponse response = CkysService.getInstance().calismaBilgisiSorgula(tcNo, registrationNo,
                    serialNo);
            List<CalismaSonuc> informations = new ArrayList<>();
            if (response != null && response.getCalismaBilgisiResult() != null
                    && response.getCalismaBilgisiResult().getList() != null) {
                return new ResponseEntitySet<>(Stream.of(response.getCalismaBilgisiResult().getList())
                        .collect(Collectors.toList()));
            }
            return new ResponseEntitySet<>(informations);
        } catch (IOException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    private void doctorLogSend(String methodName, String phoneOrUserID, String ip, String eventDescription) {
        new Thread(() -> {
            try {
                Managers.logManager.create(
                        new Log(
                                "com.kodlabs.doktorumyanimda.controller",
                                DoctorManager.class.getSimpleName(),
                                methodName,
                                phoneOrUserID,
                                Role.DOCTOR.value(),
                                ip,
                                Functions.getIpAddress(),
                                eventDescription));
            } catch (UnknownHostException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();
    }

    public ResponseEntitySet<DoctorRecipeInformation> recipeInformation(String userID) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessage.INVALID_DATA);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, Role.DOCTOR.value())) {
                DoctorRecipeInformation information = this.doctorDal.recipeInformation(userID).orElse(null);
                if (information != null) {
                    return new ResponseEntitySet<>(information);
                } else {
                    return new ResponseEntitySet<>(false, ErrorMessage.RECIPE_INFORMATION_NOT_FOUND);
                }
            } else {
                return new ResponseEntitySet<>(false, ErrorMessage.NOT_FOUND_DOCTOR_INFORMATION);
            }
        } catch (ConnectionException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity recipeInformationUpdate(DoctorRecipeInformationCreate doctorRecipeInformationCreate) {
        if (!doctorRecipeInformationCreate.isValid()) {
            return new ResponseEntity(false, ErrorMessage.INVALID_DATA);
        }
        try {
            if (doctorDal.isExistsRecipeInformation(doctorRecipeInformationCreate.getDoctorID())) {
                this.doctorDal.recipeInformationUpdate(doctorRecipeInformationCreate);
            } else {
                this.doctorDal.recipeInformationCreate(doctorRecipeInformationCreate);
            }
            return new ResponseEntity();
        } catch (ConnectionException | SQLException e) {
            return new ResponseEntity(false, ErrorMessage.OPERATION_FAILED.getCode(), e.getLocalizedMessage());
        }
    }
}
