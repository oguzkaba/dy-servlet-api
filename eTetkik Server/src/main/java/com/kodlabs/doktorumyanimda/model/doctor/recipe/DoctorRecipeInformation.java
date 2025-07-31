package com.kodlabs.doktorumyanimda.model.doctor.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRecipeInformation {
    private String medulaPassword;
    private int branchCode;
    private int certificateCode;
    private int facilityID;
    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(medulaPassword) && branchCode > 0 && certificateCode > 0;
    }
}
