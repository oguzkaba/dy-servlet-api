package com.kodlabs.doktorumyanimda.model.report.bloodpressure;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BloodPressure {
    public int minorValue;
    public int majorValue;
    public int pulseValue;
    public String reportDate;


    boolean isValid(){
        return  minorValue > 0 && majorValue > 0 && pulseValue > 0;
    }
}