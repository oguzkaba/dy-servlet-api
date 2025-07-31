package com.kodlabs.doktorumyanimda.model.doctor;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorUpdateRequest {
    private String adminUserID;
    private String id;
    private String degree;
    private String phone;
    private String mail;
    private String password;

    public boolean isValid(){
        return !TextUtils.isEmpty(adminUserID) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(degree);
    }
}
