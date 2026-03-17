package com.kodlabs.doktorumyanimda.model.user.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String address;
    private String district;
    private String city;

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(address) && !TextUtils.isEmpty(district) && !TextUtils.isEmpty(city);
    }
}
