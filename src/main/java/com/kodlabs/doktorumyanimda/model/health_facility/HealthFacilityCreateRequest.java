package com.kodlabs.doktorumyanimda.model.health_facility;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthFacilityCreateRequest {
    private String userID;
    private HealthFacilityCU data;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && data != null && data.isValid();
    }
}
