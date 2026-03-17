package com.kodlabs.doktorumyanimda.model.health_facility.admin;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HFAdmin extends HFAdminBase {
    private String uname;

    private int facilityID;

    public HFAdmin(String uname, String name, String surname, String phone, String email, int facilityID) {
        super(name, surname, phone, email);
        this.uname = uname;
        this.facilityID = facilityID;
    }

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(uname) && facilityID != -1 && super.isValid();
    }
}
