package com.kodlabs.doktorumyanimda.model.report.complaint;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintUpdateContentRequest {
    private String id;
    private ComplaintReport report;

    public boolean isValid(){
        return !TextUtils.isEmpty(id) && report != null && report.isValid();
    }
}
