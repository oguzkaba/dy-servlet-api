package com.kodlabs.doktorumyanimda.model.report.examination;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationUpdateRequest {
    private String userID;
    private String name;
    private String date;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(name);
    }
}
