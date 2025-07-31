package com.kodlabs.doktorumyanimda.model.admin.user;


import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginVerifyRequest {
    private String uname;
    private String code;

    public boolean isValid(){
        return !TextUtils.isEmpty(uname) && !TextUtils.isEmpty(code);
    }
}
