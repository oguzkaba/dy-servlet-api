package com.kodlabs.doktorumyanimda.model.report.warning;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WarningsPatientUpdateRequest {
    private String patientID;
    private WarningUpdate warningUpdate;
    public boolean isValid(){
        return !TextUtils.isEmpty(patientID) && warningUpdate != null && warningUpdate.isValid();
    }
}
