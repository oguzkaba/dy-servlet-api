package com.kodlabs.doktorumyanimda.model.doctor.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String uname;
    private byte role;
    private String password;

    public boolean isValid(){
        return !TextUtils.isEmpty(uname) && !TextUtils.isEmpty(password);
    }
}
