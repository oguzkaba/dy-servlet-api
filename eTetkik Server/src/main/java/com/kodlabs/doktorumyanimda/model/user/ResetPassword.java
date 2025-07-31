package com.kodlabs.doktorumyanimda.model.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPassword {
    private String password;
    public boolean isValid(){
        return !TextUtils.isEmpty(password);
    }
}
