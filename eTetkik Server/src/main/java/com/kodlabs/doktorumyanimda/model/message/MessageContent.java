package com.kodlabs.doktorumyanimda.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageContent {
    private int id;
    private boolean direction;
    private MessageTypeContent content;
    private String date;

    public boolean isValid(){
        return content != null && content.isValid();
    }
}