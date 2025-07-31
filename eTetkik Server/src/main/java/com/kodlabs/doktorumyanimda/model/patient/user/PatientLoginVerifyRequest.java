package com.kodlabs.doktorumyanimda.model.patient.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientLoginVerifyRequest {
    private String phone;
    private String verifyCode;

    public boolean isValid(){
        return !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(verifyCode);
    }
}
