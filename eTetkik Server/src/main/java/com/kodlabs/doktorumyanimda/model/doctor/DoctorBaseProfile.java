package com.kodlabs.doktorumyanimda.model.doctor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorBaseProfile {
    private String doctorID;
    private String doctorCode;
    private String degree;
    private String phone;
    private String email;
    private String picture;
    private String branch;
    private String introduceYourSelf;
    private String education;
    private String areaExperts;
    private String address;
    private String webAddress;
    private int facilityID;
    private int doctorType;
    private boolean anonymous;
    private String tcNumber;

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(tcNumber) && !TextUtils.isEmpty(doctorID) && !TextUtils.isEmpty(doctorCode) && !TextUtils.isEmpty(degree) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(branch) &&
                !TextUtils.isEmpty(education) && !TextUtils.isEmpty(introduceYourSelf) && !TextUtils.isEmpty(areaExperts) && !TextUtils.isEmpty(address);
    }
}
