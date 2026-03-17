package com.kodlabs.doktorumyanimda.model.report;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Report {
    private String source;
    private int type;

    public boolean isValid(){
        return !TextUtils.isEmpty(source);
    }
}
