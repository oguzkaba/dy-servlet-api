package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfileUpdateV2;

import java.sql.SQLException;

public interface IPatientProfileDal {
    boolean existsProfile(String userID) throws ConnectionException, SQLException;
    ResponseEntitySet<PatientProfile> profile(String userID) throws ConnectionException;
    ResponseEntity updateProfile(String userID, PatientProfile profile) throws ConnectionException;
    ResponseEntity create(String userID, PatientProfile profile) throws ConnectionException;

    ResponseEntity updateProfileV2(String patientID, PatientProfileUpdateV2 profile) throws ConnectionException;
}
