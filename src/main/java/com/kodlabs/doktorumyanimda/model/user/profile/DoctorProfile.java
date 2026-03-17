package com.kodlabs.doktorumyanimda.model.user.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DoctorProfile extends Profile implements IProfile{
    private String degree;
    private String phone;
    private String mail;
    private String webAddress;
    private boolean anonymous = true;
    private boolean firstVisitFree = false;
    private int doctorType;
    private int dayHourStart;
    private int dayHourEnd;
    private int dayMinutePeriod;
    public DoctorProfile(String name, String surname, String picture, String gender, String degree,
                         boolean anonymous, boolean firstVisitFree, String phone, String mail,
                         String webAddress, int doctorType, int dayHourStart, int dayHourEnd, int dayMinutePeriod) {
        super(name, surname, gender, picture);
        this.degree = degree;
        this.anonymous = anonymous;
        this.firstVisitFree = firstVisitFree;
        this.phone = phone;
        this.mail = mail;
        this.webAddress = webAddress;
        this.doctorType = doctorType;
        this.dayHourStart = dayHourStart;
        this.dayHourEnd = dayHourEnd;
        this.dayMinutePeriod = dayMinutePeriod;
    }

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(degree) && super.isValid() && !TextUtils.isEmpty(mail);
    }
}
