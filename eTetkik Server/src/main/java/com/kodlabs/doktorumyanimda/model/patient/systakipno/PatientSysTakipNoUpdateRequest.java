package com.kodlabs.doktorumyanimda.model.patient.systakipno;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class PatientSysTakipNoUpdateRequest extends PatientSysTakipNo{
    @JsonIgnore
    public boolean isValid(){
        return super.isValid();
    }
}
