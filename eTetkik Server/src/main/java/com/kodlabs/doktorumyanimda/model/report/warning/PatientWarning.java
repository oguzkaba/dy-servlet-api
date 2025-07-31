package com.kodlabs.doktorumyanimda.model.report.warning;

import com.kodlabs.doktorumyanimda.model.patient.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientWarning {
    private Patient patient;
    private String id;
    public boolean isRead;
    public String lastUpdate;
    public int type;
}
