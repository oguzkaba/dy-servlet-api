package com.kodlabs.doktorumyanimda.dto.doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultationDoctorDTO {
    private String doctorID;
    private String degree;
    private String name;
    private String surname;
    private String picture;
    private String branch;
    private String address;
    private int dayHourStart;
    private int dayHourEnd;
    private int dayMinutePeriod;
}
