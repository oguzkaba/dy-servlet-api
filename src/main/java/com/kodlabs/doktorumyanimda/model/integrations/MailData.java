package com.kodlabs.doktorumyanimda.model.integrations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailData implements IIntegrationsEntity {
    public String title;
    public String address;
    public String message;
    public String type;
    public String userName;
}
