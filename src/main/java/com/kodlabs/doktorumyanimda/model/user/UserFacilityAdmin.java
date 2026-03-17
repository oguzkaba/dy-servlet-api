package com.kodlabs.doktorumyanimda.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFacilityAdmin extends UserAdmin{
    private String facilityName;

    public UserFacilityAdmin(String facilityName, String role, String userID) {
        super(userID, role);
        this.facilityName = facilityName;
    }
}
