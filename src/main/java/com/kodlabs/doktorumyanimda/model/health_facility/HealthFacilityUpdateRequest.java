package com.kodlabs.doktorumyanimda.model.health_facility;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthFacilityUpdateRequest {
    private String userID;
    private int id;
    private HealthFacilityCU data;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && id != -1 && data != null && data.isValid();
    }
}
