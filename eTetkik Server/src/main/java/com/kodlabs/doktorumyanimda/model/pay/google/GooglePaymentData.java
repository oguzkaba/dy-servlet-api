package com.kodlabs.doktorumyanimda.model.pay.google;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GooglePaymentData {
    private String skuID;
    private String token;
    private String packageName;

    public boolean isValid(){
        return !TextUtils.isEmpty(skuID) && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(packageName);
    }
}
