package com.kodlabs.doktorumyanimda.model.peak;

import com.kodlabs.doktorumyanimda.utils.Fee;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePeakRequest {
    private String patientID;
    private String doctorID;
    private int fee;
    private int peakDay;
    private String note;

    public boolean isValid(){
        return !TextUtils.isEmpty(doctorID) && !TextUtils.isEmpty(patientID) && Fee.feeList.contains(fee) && (Fee.peakTime.contains(peakDay) || Fee.peakBigTime.contains(peakDay)) && !TextUtils.isEmpty(note);
    }
}
