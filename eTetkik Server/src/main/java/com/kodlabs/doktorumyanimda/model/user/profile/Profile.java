package com.kodlabs.doktorumyanimda.model.user.profile;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {
    private String name;
    private String surname;
    private String gender;
    private String picture;
    public boolean isValid(){
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(gender);
    }
}
