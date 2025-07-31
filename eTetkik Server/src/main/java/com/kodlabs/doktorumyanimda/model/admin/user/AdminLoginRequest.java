package com.kodlabs.doktorumyanimda.model.admin.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.model.user.LoginRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequest extends LoginRequest {
    private String password;

    public boolean isValid(){
        return !TextUtils.isEmpty(password) && super.isValid();
    }
}
