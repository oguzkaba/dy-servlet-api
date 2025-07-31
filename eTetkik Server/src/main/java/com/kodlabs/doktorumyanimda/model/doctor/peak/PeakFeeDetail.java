package com.kodlabs.doktorumyanimda.model.doctor.peak;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.Fee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PeakFeeDetail {
    private String id;
    private int fee;
    private int peakDay;

    @JsonIgnore
    public boolean isValid(){
        return Fee.feeList.contains(fee) && (Fee.peakTime.contains(peakDay) || Fee.peakBigTime.contains(peakDay));
    }
}
