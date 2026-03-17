package com.kodlabs.doktorumyanimda.model.doctor.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String userID;
    private byte role;
    private String password;
    private String newPassword;

    public boolean isValid(){
        return userID != null && !userID.isEmpty() && password != null && !password.isEmpty() && newPassword != null && !newPassword.isEmpty();
    }
}
