package com.kodlabs.doktorumyanimda.model.patient.systakipno;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientSysTakipNo {
    private int id;
    private String tcNumber;
    private String doctorID;
    private String sysTakipNo;
    private boolean active;
    private String updateDateTime;
    private String createDateTime;

    @JsonIgnore
    protected boolean isValid(){
        return !TextUtils.isEmpty(tcNumber) && !TextUtils.isEmpty(doctorID) && !TextUtils.isEmpty(sysTakipNo);
    }
}
