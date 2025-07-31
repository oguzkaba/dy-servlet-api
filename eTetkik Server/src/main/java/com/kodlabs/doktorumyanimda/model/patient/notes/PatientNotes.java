package com.kodlabs.doktorumyanimda.model.patient.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientNotes {
    private int id;
    private String patientID;
    private String doctorID;
    private String noteID;
    private String createDateTime;
}
