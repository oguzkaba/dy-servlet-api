package com.kodlabs.doktorumyanimda.model.health_facility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.Patterns;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Create and Update
public class HealthFacilityCU {
    private String name;
    private String description;
    private String address;
    private Long tax_number;
    private String phone;
    private int facilityID;

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(name) && facilityID != -1 && !TextUtils.isEmpty(address) && tax_number != null && tax_number > 0 && !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }
}
