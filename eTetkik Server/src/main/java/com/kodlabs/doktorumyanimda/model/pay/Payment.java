package com.kodlabs.doktorumyanimda.model.pay;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {
    private int type;
    private String data;

    public boolean isValid(){
        return !TextUtils.isEmpty(data);
    }
}
