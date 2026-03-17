package com.kodlabs.doktorumyanimda.model.doctor.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorLoginV2Request extends DoctorLoginRequest {
    private String password;
    private String type;
    public boolean isValid(){
        return !TextUtils.isEmpty(password) && super.isValid();
    }
}
