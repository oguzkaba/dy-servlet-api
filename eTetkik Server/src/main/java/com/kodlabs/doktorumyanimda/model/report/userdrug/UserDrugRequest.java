package com.kodlabs.doktorumyanimda.model.report.userdrug;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDrugRequest{
    private String userID;
    private UserDrugReport report;

    @JsonIgnore
    public boolean isValid(){
        if(report != null){
            return !TextUtils.isEmpty(userID) && report.isValid();
        }
        return false;
    }
}
