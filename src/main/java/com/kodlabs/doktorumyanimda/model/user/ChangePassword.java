package com.kodlabs.doktorumyanimda.model.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {
    private String currentPassword;
    private String newPassword;
    public boolean isValid(){
        return !TextUtils.isEmpty(currentPassword) && !TextUtils.isEmpty(newPassword);
    }
}
