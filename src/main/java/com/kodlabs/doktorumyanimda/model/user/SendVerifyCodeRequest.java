package com.kodlabs.doktorumyanimda.model.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendVerifyCodeRequest {
    private String uname;
    private byte role;
    private String contactType;
    public boolean isValid(){
        return !TextUtils.isEmpty(uname) && !TextUtils.isEmpty(contactType);
    }
}
