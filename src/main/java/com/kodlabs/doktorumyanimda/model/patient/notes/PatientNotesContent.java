package com.kodlabs.doktorumyanimda.model.patient.notes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientNotesContent {
    private int id;
    private String title;
    private String content;
    private String contentID;
    private String noteID;
    private String updateDateTime;
    private String createDateTime;

    @JsonIgnore
    protected boolean isValid(){
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(content);
    }
}
