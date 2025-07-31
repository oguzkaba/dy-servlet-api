package com.kodlabs.doktorumyanimda.model.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageTypeContent {
    private String fileName;
    private String content;
    private int type;
    @JsonIgnore
    public boolean isValid(){
        if(type == MessageType.DOCUMENT.ordinal()){
            return !TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(content);
        }else{
           return !TextUtils.isEmpty(content) && (type == MessageType.TEXT.ordinal() || type == MessageType.RECORD.ordinal() ||  type == MessageType.IMAGE.ordinal());
        }
    }
}
