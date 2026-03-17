package com.kodlabs.doktorumyanimda.model.patient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientForAdmin extends Patient{
    private String createDate;

    public PatientForAdmin(String name, String surname, String phone, String email, String userID, String picture, String gender, int age, String createDate) {
        super(name, surname, phone, email, userID, picture, gender, age);
        this.createDate = createDate;
    }
}
