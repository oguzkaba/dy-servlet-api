package com.kodlabs.doktorumyanimda.model.peak;

import com.kodlabs.doktorumyanimda.utils.Fee;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.model.pay.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeakPayRequest {
    private String patientID;
    private String peakID;
    private int fee;
    private Payment payment;

    public boolean isValid(){
        return !TextUtils.isEmpty(patientID) && !TextUtils.isEmpty(peakID) && Fee.feeList.contains(fee) && payment != null && payment.isValid();
    }
}
