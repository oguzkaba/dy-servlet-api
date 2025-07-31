package com.kodlabs.doktorumyanimda.model.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserPatient extends User{
    public UserPatient(String userID) {
        super(userID);
    }
}
