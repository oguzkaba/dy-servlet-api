package com.kodlabs.doktorumyanimda.model.report.userdrug;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDrug {
    private String id;
    private UserDrugReport report;
    private String date;
}
