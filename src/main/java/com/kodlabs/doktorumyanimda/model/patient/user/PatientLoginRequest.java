package com.kodlabs.doktorumyanimda.model.patient.user;

import com.kodlabs.doktorumyanimda.utils.Patterns;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.model.user.LoginRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientLoginRequest extends LoginRequest {
    private String deviceID;

    public boolean isValid(){
        return !TextUtils.isEmpty(deviceID) && Patterns.DEVICE_UUID.matcher(deviceID).matches() && super.isValid();
    }
}
