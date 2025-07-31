package com.kodlabs.doktorumyanimda.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDoctor extends User{
    private String id;

    public UserDoctor(String userID, String id) {
        super(userID);
        this.id = id;
    }
}
