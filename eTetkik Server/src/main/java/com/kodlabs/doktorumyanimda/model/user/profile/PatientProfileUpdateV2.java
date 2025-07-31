package com.kodlabs.doktorumyanimda.model.user.profile;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileUpdateV2 implements IProfile{
    private String picture;
    private String mail;
    private String phone;
    private boolean visiblePhone = true;

    public boolean isValid(){
        return !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(phone);
    }
}
