package com.kodlabs.doktorumyanimda.model.health_facility.admin;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HFAdminUpdateRequest {
    private String userID;
    private String facilityAdminID;
    private HFAdminBase data;
    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(facilityAdminID) && data != null && data.isValid();
    }
}
