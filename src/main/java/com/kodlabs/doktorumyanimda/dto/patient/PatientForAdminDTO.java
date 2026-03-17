package com.kodlabs.doktorumyanimda.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientForAdminDTO {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String userID;
    private String picture;
    private String gender;
    private String createDate;
    private int age;
}
