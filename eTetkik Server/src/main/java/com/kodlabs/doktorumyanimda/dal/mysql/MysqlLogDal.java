package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.ILogDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.utils.Functions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlLogDal implements ILogDal {
    @Override
    public ResponseEntity create(Log log) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL createLog(?, ?, ?, ?, ?, ?, ?, ?) }");
            statement.setString(1, log.getPackageName());
            statement.setString(2, log.getClassName());
            statement.setString(3, log.getMethodName());
            statement.setString(4, log.getUserID());
            statement.setByte(5, log.getUserType());
            statement.setString(6, log.getSourceIp());
            statement.setString(7, log.getTargetIp());
            statement.setString(8, log.getEventDescription());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity();
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<Log>> logs(String startDateTime, String endDateTime) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Log>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL logList(?, ?)}");
            statement.setString(1, startDateTime);
            statement.setString(2, endDateTime);
            resultSet = statement.executeQuery();
            List<Log> logs = new ArrayList<>();
            while(resultSet.next()){
                logs.add(
                        new Log(
                                resultSet.getInt("id"),
                                resultSet.getString("package_name"),
                                resultSet.getString("class_name"),
                                resultSet.getString("method_name"),
                                resultSet.getString("phone_or_userID"),
                                resultSet.getByte("user_type"),
                                resultSet.getString("source_ip"),
                                resultSet.getString("target_ip"),
                                resultSet.getString("event_description"),
                                Functions.timeStampToLocalDateTime(resultSet.getTimestamp("createDate"), "GMT+3")
                        )
                );
            }
            response = new ResponseEntitySet<>(logs);
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
}
