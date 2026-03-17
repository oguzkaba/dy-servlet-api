package com.kodlabs.doktorumyanimda.model.ckys;

import com.kodlabs.doktorumyanimda.model.nvi.NVIEntity;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CkysRequest implements NVIEntity {
    private String tcNumber;
    private String serialNo;
    private String registrationNo;
    private String name;
    private String surname;
    private String birdDate;

    public boolean valid(){
        return !TextUtils.isEmpty(tcNumber) || !TextUtils.isEmpty(serialNo) ||
                !TextUtils.isEmpty(registrationNo) || !TextUtils.isEmpty(name) ||
                !TextUtils.isEmpty(surname) || !TextUtils.isEmpty(birdDate);
    }
}
