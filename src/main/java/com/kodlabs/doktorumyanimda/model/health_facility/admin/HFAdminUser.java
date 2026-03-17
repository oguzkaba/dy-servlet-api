package com.kodlabs.doktorumyanimda.model.health_facility.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HFAdminUser extends HFAdmin {
    private String userID;

    public HFAdminUser(String uname, String name, String surname, String phone, String email, int facilityID, String userID) {
        super(uname, name, surname, phone, email, facilityID);
        this.userID = userID;
    }

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && super.isValid();
    }
}
