package com.kodlabs.doktorumyanimda.model.report.examination;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationUpdateContentRequest {
    public String id;
    private ExaminationReport report;

    public boolean isValid(){
        return !TextUtils.isEmpty(id) && report.isValid();
    }
}
