package com.kodlabs.doktorumyanimda.model.health_facility.admin;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HFAdminCreateRequest {
    private String userID;
    private HFAdminCreate data;
    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && data != null && data.isValid();
    }
}
