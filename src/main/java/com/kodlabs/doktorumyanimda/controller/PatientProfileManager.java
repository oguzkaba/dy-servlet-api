package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPatientProfileDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.model.log.LogEventDescription;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfileUpdateV2;
import com.kodlabs.doktorumyanimda.model.user.profile.ProfileUpdateRequest;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

public class PatientProfileManager {
    private final IPatientProfileDal profileDal;

    public PatientProfileManager(IPatientProfileDal profileDal) {
        this.profileDal = profileDal;
    }

    public ResponseEntitySet<PatientProfile> profile(String userID) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(userID, Role.PATIENT.value())) {
                if (isExistsProfile(userID)) {
                    return this.profileDal.profile(userID);
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

    public ResponseEntity update(ProfileUpdateRequest<PatientProfile> request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(request.getUserID(), Role.PATIENT.value())) {
                ResponseEntity response;
                if (isExistsProfile(request.getUserID())) {
                    response = this.profileDal.updateProfile(request.getUserID(), request.getProfile());
                } else {
                    response = create(request);
                }

                if (response.isSuccess) {
                    patientLogSend("update", request.getUserID(), Functions.getClientIpAddress(hsr),
                            Functions.getBrowserOrDevice(hsr), LogEventDescription.ACCOUNT_UPDATE.getMessage());
                }
                return response;
            } else {
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    private ResponseEntity create(ProfileUpdateRequest<PatientProfile> request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return this.profileDal.create(request.getUserID(), request.getProfile());
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Boolean> existsProfile(String userID) {
        if (TextUtils.isEmpty(userID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return new ResponseEntitySet<>(isExistsProfile(userID));
        } catch (ConnectionException | SQLException | NullPointerException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public boolean isExistsProfile(String userID) throws ConnectionException, SQLException, NullPointerException {
        if (TextUtils.isEmpty(userID)) {
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.profileDal.existsProfile(userID);
    }

    public ResponseEntity updateV2(String patientID, PatientProfileUpdateV2 profile, HttpServletRequest hsr) {
        if (TextUtils.isEmpty(patientID) || !profile.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            if (Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())) {
                ResponseEntity response = this.profileDal.updateProfileV2(patientID, profile);
                if (response.isSuccess) {
                    patientLogSend("updateV2", patientID, Functions.getClientIpAddress(hsr),
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

    private void patientLogSend(String methodName, String phoneOrUserID, String ip, String browserOrDevice,
            String eventDescription) {
        new Thread(() -> {
            try {
                Managers.logManager.create(
                        new Log(
                                "com.kodlabs.doktorumyanimda.controller",
                                PatientProfileManager.class.getSimpleName(),
                                methodName,
                                phoneOrUserID,
                                Role.PATIENT.value(),
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
