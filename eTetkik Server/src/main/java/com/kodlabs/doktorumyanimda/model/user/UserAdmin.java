package com.kodlabs.doktorumyanimda.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAdmin extends User{
    private String role;

    public UserAdmin(String userID, String role) {
        super(userID);
        this.role = role;
    }

}
