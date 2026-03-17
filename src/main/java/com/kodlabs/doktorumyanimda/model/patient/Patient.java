package com.kodlabs.doktorumyanimda.model.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Patient {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String userID;
    private String peakCreateDate;
    private String picture;
    private String gender;
    private int age;
    public Patient(String name, String surname,  String phone, String email, String userID, String picture, String gender, int age) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.userID = userID;
        this.picture = picture;
        this.gender = gender;
        this.age = age;
    }
}
