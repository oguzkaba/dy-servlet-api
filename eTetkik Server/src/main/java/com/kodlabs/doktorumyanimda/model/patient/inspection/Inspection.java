package com.kodlabs.doktorumyanimda.model.patient.inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inspection {
    private String id;
    private String patientID;
    private String doctorID;
    private int appointmentID;
    private String createDateTime;
    private TimeSlot timeSlot;
}
