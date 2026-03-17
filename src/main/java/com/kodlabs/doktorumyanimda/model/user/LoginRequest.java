package com.kodlabs.doktorumyanimda.model.user;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String uname;
    public LoginRequest(){}

    public boolean isValid(){
        return !TextUtils.isEmpty(uname);
    }
}
