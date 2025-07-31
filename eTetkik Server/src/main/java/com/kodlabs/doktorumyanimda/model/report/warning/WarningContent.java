package com.kodlabs.doktorumyanimda.model.report.warning;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WarningContent {
    private String title;
    private String content;
    private String date;
    private int type;
    private int triggerType;
}
