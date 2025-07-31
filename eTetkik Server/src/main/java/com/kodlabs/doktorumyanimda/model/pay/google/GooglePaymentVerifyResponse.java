package com.kodlabs.doktorumyanimda.model.pay.google;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GooglePaymentVerifyResponse {
    private String kind;
    private String developerPayload;
    private String orderId;
    private int purchaseState;
    private int consumptionState;
    private int purchaseType;
}
