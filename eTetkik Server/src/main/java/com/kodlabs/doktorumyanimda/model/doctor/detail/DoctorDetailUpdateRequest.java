package com.kodlabs.doktorumyanimda.model.doctor.detail;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDetailUpdateRequest {
    private String userID;
    private DoctorDetail detail;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && detail != null && detail.isValid();
    }
}
