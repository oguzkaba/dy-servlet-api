package com.kodlabs.doktorumyanimda.model.user.profile;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorProfileUpdate {
    private String phone;
    private String mail;
    private String picture;
    private String webAddress;
    private boolean anonymous;
    private boolean firstVisitFree;
    private int dayHourStart = 8;
    private int dayHourEnd = 17;
    private int dayMinutePeriod = 15;

    public boolean isValid(){
        return !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(mail) && 0 <= dayHourStart && dayHourStart <= 24 && 0 <= dayHourEnd &&
                dayHourEnd <= 24 && 0 <= dayMinutePeriod && dayMinutePeriod <= 60;
    }
}
