package com.kodlabs.doktorumyanimda.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableContact {
    private boolean mail;
    private boolean sms;
}
