package com.kodlabs.doktorumyanimda.model.patient;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientStatusUpdateRequest {
    private String patientID;
    private String fieldName;
    private boolean value;

    public boolean isValid(){
        return !TextUtils.isEmpty(patientID) && !TextUtils.isEmpty(fieldName);
    }
}
