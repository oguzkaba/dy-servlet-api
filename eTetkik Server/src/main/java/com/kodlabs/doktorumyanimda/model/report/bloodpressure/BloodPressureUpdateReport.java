package com.kodlabs.doktorumyanimda.model.report.bloodpressure;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloodPressureUpdateReport {
    private String patientID;
    private BloodPressure bloodPressure;

    public boolean isValid(){
        return !TextUtils.isEmpty(patientID) && bloodPressure != null && bloodPressure.isValid();
    }
}
