package com.kodlabs.doktorumyanimda.pay.verify;

public interface IPaymentVerify {
    boolean verify(int fee, String paymentData);
}
