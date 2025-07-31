package com.kodlabs.doktorumyanimda.model.patient.notes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

public class PatientNotesContentCreateRequest extends PatientNotesContent{
    @JsonIgnore
    public boolean isValid(){
        return super.isValid() && !TextUtils.isEmpty(getNoteID());
    }
}
