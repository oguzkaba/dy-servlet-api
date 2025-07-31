package com.kodlabs.doktorumyanimda.pay.verify;

import com.kodlabs.doktorumyanimda.model.pay.PaymentType;

public class PaymentVerifyFactory {
    public static IPaymentVerify getPaymentVerify(int type){
        if(PaymentType.GOOGLE.ordinal() == type){
            return new GooglePay();
        }
        return null;
    }
}
