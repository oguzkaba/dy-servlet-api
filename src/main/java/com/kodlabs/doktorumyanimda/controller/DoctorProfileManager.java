package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IDoctorProfileDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.model.log.LogEventDescription;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfileUpdate;
import com.kodlabs.doktorumyanimda.model.user.profile.ProfileUpdateRequest;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Patterns;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

public class DoctorProfileManager {
    private IDoctorProfileDal iDoctorProfileDal;

    public DoctorProfileManager(IDoctorProfileDal iDoctorProfileDal) {
        this.iDoctorProfileDal = iDoctorProfileDal;
    }

    public ResponseEntitySet<DoctorProfile> profile(String userID) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, Role.DOCTOR.value())) {
                if (isExists(userID)) {
                    return this.iDoctorProfileDal.profile(userID);
                } else {
                    return new ResponseEntitySet<>(false, ErrorMessages.notAccessProfileInformation);
                }
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity update(ProfileUpdateRequest<DoctorProfile> request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getUserID(), Role.DOCTOR.value())) {
                if (isExists(request.getUserID())) {
                    ResponseEntity response = this.iDoctorProfileDal.updateProfile(request.getUserID(),
                            request.getProfile());
                    if (response.isSuccess) {
                        doctorLogSend("update", request.getUserID(), Functions.getClientIpAddress(hsr),
                                Functions.getBrowserOrDevice(hsr), LogEventDescription.ACCOUNT_UPDATE.getMessage());
                    }
                    return response;
                } else {
                    return new ResponseEntity(false, ErrorMessages.notAccessProfileInformation);
                }
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public boolean isExists(String userID) throws ConnectionException, SQLException, NullPointerException {
        if (TextUtils.isEmpty(userID)) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.iDoctorProfileDal.existsProfile(userID);
    }

    public ResponseEntity updateV2(String doctorID, DoctorProfileUpdate profileUpdate, HttpServletRequest hsr) {
        if (TextUtils.isEmpty(doctorID) || !profileUpdate.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        if (!Patterns.PHONE.matcher(profileUpdate.getPhone()).matches()) {
            return new ResponseEntity(false, ErrorMessages.inValidPhone);
        }
        if (!Patterns.EMAIL.matcher(profileUpdate.getMail()).matches()) {
            return new ResponseEntity(false, ErrorMessages.inValidEmail);
        }
        try {
            if (Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                ResponseEntity response = this.iDoctorProfileDal.updateProfileV2(doctorID, profileUpdate);
                if (response.isSuccess) {
                    doctorLogSend("updateV2", doctorID, Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr), LogEventDescription.ACCOUNT_UPDATE.getMessage());
                }
                return response;
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    private void doctorLogSend(String methodName, String phoneOrUserID, String ip, String browserOrDevice,
            String eventDescription) {
        new Thread(() -> {
            try {
                Managers.logManager.create(
                        new Log(
                                "com.kodlabs.doktorumyanimda.controller",
                                DoctorProfileManager.class.getSimpleName(),
                                methodName,
                                phoneOrUserID,
                                Role.DOCTOR.value(),
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
