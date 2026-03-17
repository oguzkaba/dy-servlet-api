package com.kodlabs.doktorumyanimda.model;

import com.kodlabs.doktorumyanimda.messages.ErrorMessage;

public class ResponseEntity {
    public boolean isSuccess;
    public int errorCode;
    public String message;
    public Object data;

    public ResponseEntity() {
        isSuccess = true;
    }

    public ResponseEntity(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public ResponseEntity(boolean isSuccess, String message, Object data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.data = data;
    }

    public ResponseEntity(boolean isSuccess, int errorCode, String message) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.message = message;
    }

    public ResponseEntity(boolean isSuccess, ErrorMessage errorMessage) {
        this.isSuccess = isSuccess;
        this.errorCode = errorMessage.getCode();
        this.message = errorMessage.getMessage();
    }
}
