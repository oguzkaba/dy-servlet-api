package com.kodlabs.doktorumyanimda.model.doctor.recipe;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRecipeInformationCreate {
    private String doctorID;
    private DoctorRecipeInformation recipeInformation;

    public boolean isValid(){
        return !TextUtils.isEmpty(doctorID) && recipeInformation != null && recipeInformation.isValid();
    }
}
