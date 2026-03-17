package com.kodlabs.doktorumyanimda.model.patient.user;

import com.kodlabs.doktorumyanimda.model.user.LoginRequest;
import com.kodlabs.doktorumyanimda.utils.Patterns;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientLoginV2Request extends LoginRequest {
    private String password;
    private String loginType;
    private String deviceID;

    public boolean isValid(){
        return !TextUtils.isEmpty(password) && !TextUtils.isEmpty(loginType) && !TextUtils.isEmpty(deviceID) && Patterns.DEVICE_UUID.matcher(deviceID).matches() && super.isValid();
    }
}
