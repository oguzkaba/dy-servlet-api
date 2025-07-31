package com.kodlabs.doktorumyanimda.model.doctor.peak;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeakFeeRequest {
    private String userID;
    private PeakFeeDetail detail;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && detail != null && detail.isValid();
    }
}
