package com.kodlabs.doktorumyanimda.model.doctor.contract;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractAcceptRequest {
    private String phone;
    private String deviceID;

    public boolean isValid(){
        return !TextUtils.isEmpty(phone);
    }
}
