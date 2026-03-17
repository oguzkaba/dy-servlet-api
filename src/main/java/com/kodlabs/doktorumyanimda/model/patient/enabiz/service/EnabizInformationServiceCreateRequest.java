package com.kodlabs.doktorumyanimda.model.patient.enabiz.service;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Data;

@Data
public class EnabizInformationServiceCreateRequest extends EnabizServiceInformation{
    public boolean valid(){
        return !TextUtils.isEmpty(getSysTakipNo()) && !TextUtils.isEmpty(getName()) && !TextUtils.isEmpty(getServiceReferenceNo());
    }
}
