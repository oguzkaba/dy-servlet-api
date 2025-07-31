package com.kodlabs.doktorumyanimda.model.doctor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorAllDetail extends DoctorBaseProfile{
    private String name;
    private String surname;

    public DoctorAllDetail(String doctorID, String doctorCode, String degree, String phone, String email, String picture, String branch, String introduceYourSelf, String education, String areaExperts, String address, String webAddress, int facilityID, int doctorType, boolean anonymous, String name, String surname, String tcNumber) {
        super(doctorID, doctorCode, degree, phone, email, picture, branch, introduceYourSelf, education, areaExperts, address, webAddress, facilityID, doctorType, anonymous, tcNumber);
        this.name = name;
        this.surname = surname;
    }
}
