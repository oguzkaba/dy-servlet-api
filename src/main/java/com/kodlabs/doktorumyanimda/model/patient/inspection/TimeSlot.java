package com.kodlabs.doktorumyanimda.model.patient.inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    private String date;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
}
