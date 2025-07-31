package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.message.Message;
import com.kodlabs.doktorumyanimda.model.message.MessageContent;
import com.kodlabs.doktorumyanimda.model.message.MessageTypeContent;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IMessageDal;
import com.kodlabs.doktorumyanimda.model.message.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MysqlMessageDal implements IMessageDal {
    @Override
    public ResponseEntitySet<String> createMessage(String doctorID, String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        ResponseEntitySet<String> response;
        PreparedStatement statement = null;
        try{
            String messageID = UUID.randomUUID().toString();
            statement = MysqlConnection.getInstance().prepareStatement("insert into message(id, doctorID, patientID, isRead) values (?, ?, ?, ?)");
            statement.setString(1, messageID);
            statement.setString(2, doctorID);
            statement.setString(3, patientID);
            statement.setBoolean(4,false);
            int result = statement.executeUpdate();
            if(result != 0)
                response = new ResponseEntitySet<>(messageID);
            else
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null)
                    statement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public String existsMessage(String doctorID, String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String isAvailable;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL messageExists(?, ?) }");
            statement.setString(1, doctorID);
            statement.setString(2, patientID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                isAvailable = resultSet.getString("id");
            }else{
                isAvailable = null;
            }
        }catch (SQLException e){
            isAvailable = null;
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return isAvailable;
    }

    @Override
    public ResponseEntitySet<String> createMessageContent(String messageID, MessageContent content) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL createMessageContent(?, ? ,? ) }");
            statement.setString(1,messageID);
            statement.setString(2, Common.gson.toJson(content.getContent()));
            statement.setBoolean(3, content.isDirection());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(messageID);
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
           response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<Message>> getAllMessage(String userID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntitySet<List<Message>> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL messageList(?, ?)}");
            statement.setString(1, userID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            List<Message> messages = new ArrayList<>();
            while(resultSet.next()){
                messages.add(
                        new Message(
                                resultSet.getString("id"),
                                resultSet.getString("otherUserID"),
                                resultSet.getString("fullName"),
                                resultSet.getBoolean("lastDirection"),
                                resultSet.getString("lastDate"),
                                resultSet.getString("lastContent"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getString("peakDate"),
                                resultSet.getInt("peakDay"),
                                resultSet.getInt("process"),
                                resultSet.getBoolean("isRead")
                        )
                );
            }
            response = new ResponseEntitySet<>(messages);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Message> getMessage(String userID, String otherUser, Byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntitySet<Message> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL messageInformation(?, ? ,?) }");
            statement.setString(1, userID);
            statement.setString(2, otherUser);
            statement.setByte(3, role);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new Message(
                                resultSet.getString("id"),
                                resultSet.getString("otherUserID"),
                                resultSet.getString("fullName"),
                                resultSet.getBoolean("lastDirection"),
                                resultSet.getString("lastDate"),
                                resultSet.getString("lastContent"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getBoolean("isRead")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(null);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<MessageContent>> getMessageContent(String messageID, int lastID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntitySet<List<MessageContent>> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL messageContentList(?, ?) }");
            statement.setString(1,messageID);
            statement.setInt(2, lastID);
            resultSet = statement.executeQuery();
            List<MessageContent> contentList = new ArrayList<>();
            while(resultSet.next()){
                contentList.add(
                        new MessageContent(
                                resultSet.getInt("id"),
                                resultSet.getBoolean("direction"),
                                Common.gson.fromJson(resultSet.getString("content"), MessageTypeContent.class),
                                resultSet.getString("date")
                        )
                );
            }
            response = new ResponseEntitySet<>(contentList);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Integer> isUnReadMessageCount(String userID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Integer> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL unReadMessageCount(?, ?)}");
            statement.setString(1, userID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getInt("count"));
            }else{
                response = new ResponseEntitySet<>(null);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity updateAttribute(String messageID, String attribute, Object value) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntity response;
        PreparedStatement statement = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("update message set " + attribute + " = ? where id = ?");
            setValueType(statement,1, value);
            statement.setString(2, messageID);
            int updateResult = statement.executeUpdate();
            if(updateResult != 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false,e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity updateAttributes(String messageID, Map<String, Object> attributes) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntity response;
        PreparedStatement statement = null;
        try{
            StringBuilder query = new StringBuilder();
            query.append("update message set ");
            Iterator<String> attributeKeys = attributes.keySet().iterator();
            boolean first = true;
            while(attributeKeys.hasNext()){
                String attr = attributeKeys.next();
                if(!first)
                    query.append(", ");
                query.append(attr).append("=?");
                first = false;
            }
            query.append(" where id = ?");
            statement = MysqlConnection.getInstance().prepareStatement(query.toString());
            attributeKeys = attributes.keySet().iterator();
            int index = 0;
            while(attributeKeys.hasNext()){
                String attr = attributeKeys.next();
                setValueType(statement,++index,attributes.get(attr));
            }
            statement.setString(++index,messageID);
            int updateResult = statement.executeUpdate();
            if(updateResult != 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Boolean> messageIsActive(String messageID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Boolean> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL messageIsActive(?) } ");
            statement.setString(1, messageID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getBoolean("result")
                );
            }else{
                response  = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    private void setValueType(PreparedStatement statement,int index,Object value) throws SQLException {
        if(statement == null) {
            return;
        }
        if(value instanceof Boolean) {
            statement.setBoolean(index, (Boolean) value);
        }
        else if(value instanceof String) {
            statement.setString(index, (String) value);
        }
        else if(value instanceof Integer) {
            statement.setInt(index, (Integer) value);
        }
        else if(value instanceof Double) {
            statement.setDouble(index, (Double) value);
        }
    }
}
