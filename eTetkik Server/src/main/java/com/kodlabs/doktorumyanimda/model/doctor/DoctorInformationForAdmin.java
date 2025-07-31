package com.kodlabs.doktorumyanimda.model.doctor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorInformationForAdmin extends DoctorInformation{
    private String userID;
    private String createDate;
    public DoctorInformationForAdmin(String degree, String name, String surname, String mail, String phone, String doctorID, String picture, String branch, String address, String createDate, String userID) {
        super(degree, name, surname, mail, phone, doctorID, picture, branch, address, 0, 0, 0);
        this.userID = userID;
        this.createDate = createDate;
    }
}
