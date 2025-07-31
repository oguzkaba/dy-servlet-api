package com.kodlabs.doktorumyanimda.model.peak;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeakDemandVerifyRequest {
    private String userID;
    private String peakID;
    private String patientID;
    private String note;
    private boolean verify = false;

    public boolean isValid(){
        boolean baseResult = !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(peakID) && !TextUtils.isEmpty(patientID);
        if(verify){
            return baseResult;
        }else{
            return baseResult && !TextUtils.isEmpty(note);
        }
    }
}
