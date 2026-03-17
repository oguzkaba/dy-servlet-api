package com.kodlabs.doktorumyanimda.model.doctor.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorLoginVerifyRequest {
    private String phone;
    private String verifyCode;

    public boolean isValid(){
        return !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(verifyCode);
    }
}
