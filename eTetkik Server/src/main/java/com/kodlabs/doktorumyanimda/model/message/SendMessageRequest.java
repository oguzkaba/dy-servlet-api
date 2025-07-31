package com.kodlabs.doktorumyanimda.model.message;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    private String doctorID;
    private String patientID;
    private MessageContent messageContent;

    public boolean isValid(){
        return !TextUtils.isEmpty(doctorID) && !TextUtils.isEmpty(patientID) && messageContent != null && messageContent.isValid();
    }
}