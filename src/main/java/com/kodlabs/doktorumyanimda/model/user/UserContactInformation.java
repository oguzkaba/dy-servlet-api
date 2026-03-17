package com.kodlabs.doktorumyanimda.model.user;

import com.kodlabs.doktorumyanimda.model.contact.Contact;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserContactInformation extends Contact {
    private String name;

    public UserContactInformation(String mail, String phone, String name) {
        super(mail, phone);
        this.name = name;
    }
}
