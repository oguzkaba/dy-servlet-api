package com.kodlabs.doktorumyanimda.model.report.userdrug;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.model.report.Report;
import com.kodlabs.doktorumyanimda.model.report.ReportType;

public class UserDrugReport extends Report {
    @Override
    @JsonIgnore
    public boolean isValid() {
        return super.isValid() && (getType() == ReportType.TEXT.ordinal() || getType() == ReportType.IMAGE.ordinal());
    }
}
