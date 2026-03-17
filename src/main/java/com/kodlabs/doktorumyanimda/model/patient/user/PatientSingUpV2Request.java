package com.kodlabs.doktorumyanimda.model.patient.user;

import com.kodlabs.doktorumyanimda.model.nvi.NVIEntity;
import com.kodlabs.doktorumyanimda.model.user.UserSingUp;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientSingUpV2Request extends UserSingUp implements NVIEntity {
    private String tcNumber;
    private String gender;
    private String birdDate;
    private boolean kvkk = false;
    public boolean isValid(){
        return !TextUtils.isEmpty(tcNumber) && !TextUtils.isEmpty(gender) && !TextUtils.isEmpty(birdDate) && Functions.dateControl(birdDate) && super.isValid();
    }
}
