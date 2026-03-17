package com.kodlabs.doktorumyanimda.model.report.complaint;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintUpdateRequest {
    private String userID;
    private String date;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(date);
    }
}
