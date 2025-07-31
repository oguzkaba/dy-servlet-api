package com.kodlabs.doktorumyanimda.model.report.complaint;

import com.kodlabs.doktorumyanimda.model.report.Report;
import com.kodlabs.doktorumyanimda.model.report.ReportType;

public class ComplaintReport extends Report {
    @Override
    public boolean isValid() {
        return super.isValid() && (getType() == ReportType.TEXT.ordinal() || getType() == ReportType.IMAGE.ordinal() || getType() == ReportType.RECORD.ordinal());
    }
}