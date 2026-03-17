package com.kodlabs.doktorumyanimda.model;

import com.kodlabs.doktorumyanimda.messages.ErrorMessage;

public class ResponseEntitySet <T> extends ResponseEntity{
    private T data;
    public ResponseEntitySet(){}
    public ResponseEntitySet(boolean isSuccess, String message) {
        super(isSuccess, message);
    }

    public ResponseEntitySet(boolean isSuccess, int errorCode, String message) {
        super(isSuccess, errorCode, message);
    }
    public ResponseEntitySet(boolean isSuccess, ErrorMessage errorMessage){
        super(isSuccess, errorMessage);
    }
    public ResponseEntitySet(T data) {
        super();
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
