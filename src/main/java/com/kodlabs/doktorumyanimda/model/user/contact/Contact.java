package com.kodlabs.doktorumyanimda.model.user.contact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private String phone;
    private String email;

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email);
    }
}
