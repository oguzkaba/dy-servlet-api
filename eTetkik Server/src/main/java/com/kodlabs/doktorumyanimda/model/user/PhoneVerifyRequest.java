package com.kodlabs.doktorumyanimda.model.user;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneVerifyRequest {
    private String phone;
    private String code;

    public boolean isValid(){
        return !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code);
    }
}
