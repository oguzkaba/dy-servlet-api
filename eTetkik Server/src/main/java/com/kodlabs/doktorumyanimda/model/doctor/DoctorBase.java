package com.kodlabs.doktorumyanimda.model.doctor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorBase {
    private int doctorType;
    private boolean anonymous;
    private boolean privateDoctor = false;
    private boolean hospital = false;
    private int facilityID;

    @JsonIgnore
    public boolean isValid(){
        return DoctorType.isAvailable(doctorType) && facilityID != -1;
    }
}
