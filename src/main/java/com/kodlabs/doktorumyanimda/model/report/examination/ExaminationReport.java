package com.kodlabs.doktorumyanimda.model.report.examination;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.model.report.Report;
import com.kodlabs.doktorumyanimda.model.report.ReportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExaminationReport extends Report {
    private String fileName;
    public boolean isValid(){
        if(getType() == ReportType.IMAGE.ordinal()){
            return super.isValid();
        }else if(getType() == ReportType.DOCUMENT.ordinal()){
            return super.isValid() && !TextUtils.isEmpty(fileName);
        }
        return false;
    }
}
