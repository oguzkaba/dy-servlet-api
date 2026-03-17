package com.kodlabs.doktorumyanimda.dal.mysql;

import com.google.gson.reflect.TypeToken;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.INotificationDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.Role;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MysqlNotificationDal implements INotificationDal {
    @Override
    public ResponseEntity addNotification(String userID, String notificationID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL pushNotification(?, ?) }");
            statement.setString(1, userID);
            statement.setString(2, notificationID);
            statement.execute();
            response =  new ResponseEntity();
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
    public ResponseEntity removeNotification(String notificationID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL cancelNotification(?) }");
            statement.setString(1, notificationID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                boolean result = resultSet.getBoolean("result");
                if(result){
                    response = new ResponseEntity();
                }else{
                    response = new ResponseEntity(false, ErrorMessages.operationFailed);
                }
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
    public List<String> getNotificationIDs(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> result;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL notificationIDs(?) }");
            statement.setString(1,userID);
            resultSet = statement.executeQuery();
            result = new ArrayList<>();
            while (resultSet.next()){
                result.add(resultSet.getString("notificationID"));
            }
        }catch (SQLException e){
            result = null;
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
        return result;
    }

    @Override
    public ResponseEntitySet<String> addNotificationLog(NotificationLog log, boolean isDoctor) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntitySet<String> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL addNotificationLog(?, ?, ?, ?, ?)}");
            statement.setString(1, log.getUserID());
            statement.setString(2, log.getTitle());
            statement.setString(3, log.getBody());
            statement.setString(4, Common.gson.toJson(log.getAttributes()));
            statement.setInt(5, isDoctor ? Role.DOCTOR.value() : Role.PATIENT.value());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("status")){
                    response = new ResponseEntitySet<>(
                        resultSet.getString("id")
                    );
                }else{
                    response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
                }
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
           response = new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }finally {
            try {
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity deleteNotificationLog(String logID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        ResponseEntity response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL deleteNotificationLog(?, ?) } ");
            statement.setString(1, logID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("status")){
                    response = new ResponseEntity();
                }else{
                    response = new ResponseEntity(false, ErrorMessages.operationFailed);
                }
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
    public ResponseEntitySet<List<NotificationLog>> getAllNotificationLog(String userID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<NotificationLog> logs;
        ResponseEntitySet<List<NotificationLog>> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL notificationLogs(?, ?) } ");
            statement.setString(1, userID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            logs = new ArrayList<>();
            while (resultSet.next()){
                NotificationLog log = new NotificationLog();
                log.setId(resultSet.getString("id"));
                log.setTitle(resultSet.getString("title"));
                log.setBody(resultSet.getString("body"));
                Type type = new TypeToken<Map<String,Object>>(){}.getType();
                Map<String,Object> attributes = Common.gson.fromJson(resultSet.getString("attributes"), type);
                log.setAttributes(attributes);
                log.isRead = resultSet.getBoolean("isRead");
                log.createDate = resultSet.getString("createDate");
                logs.add(log);
            }
            response = new ResponseEntitySet<>(logs);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null)
                    statement.close();
                if(resultSet != null)
                    resultSet.close();
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        return response;
    }

    @Override
    public ResponseEntity readNotificationLog(String logID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL readNotificationLog(?, ?) } ");
            statement.setString(1, logID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("status")){
                    response = new ResponseEntity();
                }else{
                    response = new ResponseEntity(false, ErrorMessages.operationFailed);
                }
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
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

    @Override
    public ResponseEntitySet<Integer> notificationCount(String userID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Integer> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL notificationCount(?, ?) } ");
            statement.setString(1, userID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getInt("count")
                );
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
    public ResponseEntity readPeakNotificationLog(String peakID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL readPeakNotificationLog(?) } ");
            statement.setString(1, peakID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("status")){
                    response = new ResponseEntity();
                }else{
                    response = new ResponseEntity(false, ErrorMessages.operationFailed);
                }
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
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
}
