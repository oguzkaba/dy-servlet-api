package com.kodlabs.doktorumyanimda.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String userID;
    private boolean active;
    private String createDate;
    private String lastLogin;
    private String type;
}
