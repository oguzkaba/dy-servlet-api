package com.kodlabs.doktorumyanimda.model.doctor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorInformation {
    private String degree;
    private String name;
    private String surname;
    private String mail;
    private String phone;
    private String doctorID;
    private String picture;
    private String branch;
    private String address;
    private int dayHourStart;
    private int dayHourEnd;
    private int dayMinutePeriod;
    private java.math.BigDecimal appointmentPrice;
}
