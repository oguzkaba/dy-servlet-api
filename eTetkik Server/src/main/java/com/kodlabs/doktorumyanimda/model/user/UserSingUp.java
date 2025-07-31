package com.kodlabs.doktorumyanimda.model.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSingUp {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;

    public boolean isValid(){
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }
}
