package com.kodlabs.doktorumyanimda.model.doctor.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegisterRequest {
    private String adminID;
    private DoctorRegister data;
    public boolean isValid(){
        return !TextUtils.isEmpty(adminID) && data != null && data.isValid();
    }
}
