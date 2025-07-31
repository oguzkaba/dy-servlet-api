package com.kodlabs.doktorumyanimda.model.doctor.peak;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeakFeeDeleteRequest {
    private String userID;
    private String id;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(id);
    }
}
