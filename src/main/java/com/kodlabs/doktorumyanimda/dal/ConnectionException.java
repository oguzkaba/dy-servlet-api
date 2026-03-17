package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;

public class ConnectionException extends Exception{
    public ConnectionException(){
        super(ErrorMessages.connectFailed);
    }
    public ConnectionException(String message){
        super(message);
    }
}
