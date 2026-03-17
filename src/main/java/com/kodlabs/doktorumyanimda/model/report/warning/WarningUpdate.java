package com.kodlabs.doktorumyanimda.model.report.warning;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WarningUpdate {
    private String title;
    private String date;
    private int type;
    private int triggerType;
    private String content;

    public WarningUpdate(String title, int type, int triggerType, String content) {
        this.title = title;
        this.type = type;
        this.triggerType = triggerType;
        this.content = content;
    }
    boolean isValid(){
        return title != null && !title.isEmpty() && content != null && !content.isEmpty();
    }
}
