package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.message.Message;
import com.kodlabs.doktorumyanimda.model.message.MessageContent;

import java.util.List;
import java.util.Map;

public interface IMessageDal {
    ResponseEntitySet<String> createMessage(String doctorID, String patientID) throws ConnectionException;
    String existsMessage(String doctorID, String patientID) throws ConnectionException;
    ResponseEntitySet<String> createMessageContent(String messageID, MessageContent content) throws ConnectionException;
    ResponseEntitySet<List<Message>> getAllMessage(String userID, byte role) throws ConnectionException;
    ResponseEntitySet<Message> getMessage(String doctorID, String patientID,Byte role) throws ConnectionException;
    ResponseEntitySet<List<MessageContent>> getMessageContent(String messageID, int lastID) throws ConnectionException;
    ResponseEntitySet<Integer> isUnReadMessageCount(String userID,byte role) throws ConnectionException;
    ResponseEntity updateAttribute(String messageID, String attribute, Object value) throws ConnectionException;
    ResponseEntity updateAttributes(String messageID,Map<String,Object> attributes) throws ConnectionException;

    ResponseEntitySet<Boolean> messageIsActive(String messageID) throws ConnectionException;
}