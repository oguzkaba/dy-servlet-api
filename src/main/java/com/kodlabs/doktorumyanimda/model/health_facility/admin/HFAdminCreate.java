package com.kodlabs.doktorumyanimda.model.health_facility.admin;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HFAdminCreate extends HFAdmin {
    private String password;
    public boolean isValid(){
        return !TextUtils.isEmpty(password) && super.isValid();
    }
}
