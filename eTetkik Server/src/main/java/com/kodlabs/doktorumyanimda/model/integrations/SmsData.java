package com.kodlabs.doktorumyanimda.model.integrations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsData implements IIntegrationsEntity{
    private String msg;
    private String phone;
}
