package com.kodlabs.doktorumyanimda.model.patient.systakipno;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PatientSysTakipNoCreateRequest extends PatientSysTakipNo {
    @JsonIgnore
    public boolean isValid(){
       return super.isValid();
    }
}
