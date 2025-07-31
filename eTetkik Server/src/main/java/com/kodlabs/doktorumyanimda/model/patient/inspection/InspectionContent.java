package com.kodlabs.doktorumyanimda.model.patient.inspection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionContent {
    private String id;
    private String inspectionID;
    private String title;
    private String symptom;
    private String complaint;
    private String anamnesis;
    private String updateDateTime;
    private String createDateTime;

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(symptom) && !TextUtils.isEmpty(complaint) && !TextUtils.isEmpty(anamnesis);
    }
}
