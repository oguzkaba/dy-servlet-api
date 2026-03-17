package com.kodlabs.doktorumyanimda.model.doctor.detail;

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
public class DoctorDetail {
    private String branch;
    private String introduceYourSelf;
    private String areaExperts;
    private String education;

    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(branch) && !TextUtils.isEmpty(education) && !TextUtils.isEmpty(areaExperts) && !TextUtils.isEmpty(introduceYourSelf);
    }
}