package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfileUpdate;

import java.sql.SQLException;

public interface IDoctorProfileDal {
    boolean existsProfile(String userID) throws ConnectionException, SQLException;
    ResponseEntitySet<DoctorProfile> profile(String userID) throws ConnectionException;
    ResponseEntity updateProfile(String userID, DoctorProfile profile) throws ConnectionException;

    ResponseEntity updateProfileV2(String doctorID, DoctorProfileUpdate profileUpdate) throws ConnectionException;

}
