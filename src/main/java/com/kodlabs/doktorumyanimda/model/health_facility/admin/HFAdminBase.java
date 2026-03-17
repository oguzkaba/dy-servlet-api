package com.kodlabs.doktorumyanimda.model.health_facility.admin;

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
public class HFAdminBase {
    private String name;
    private String surname;
    private String phone;
    private String email;

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email);
    }
}
