package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.admin.user.AdminLoginRequest;
import com.kodlabs.doktorumyanimda.model.user.UserFacilityAdmin;
import com.kodlabs.doktorumyanimda.model.user.UserWebAdmin;
import com.kodlabs.doktorumyanimda.utils.AdminRole;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IAdminDal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlAdminDal implements IAdminDal {
    @Override
    public String login(AdminLoginRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String phone = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL adminLogin(?, ?) }"); // password verify
            statement.setString(1, request.getUname());
            statement.setString(2, Functions.toSHA1(request.getPassword()));
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                boolean isVerify = resultSet.getBoolean("isVerify");
                if(isVerify){
                    phone = resultSet.getString("phone");
                }
            }
        }catch (SQLException e){
            throw new ConnectionException(e.getLocalizedMessage());
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
        return phone;
    }

    @Override
    public ResponseEntitySet<String> verifyCodeUpdate(String uname, String code) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        ResponseEntitySet<String> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL adminVerifyCodeUpdate(?, ?) }");
            statement.setString(1, uname);
            statement.setString(2, code);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getString("code"));
            }else{
                throw new SQLException(ErrorMessages.notAccessUserInformation);
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

    @Override
    public Object verifyCode(String uname, String code) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        Object response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL adminVerifyCode(?, ?) }");
            statement.setString(1, uname);
            statement.setString(2, code);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                String role = resultSet.getString("role");
                switch (AdminRole.getAdminRole(role)){
                    case WEB_ADMIN:
                        response = new ResponseEntitySet<>(new UserWebAdmin(resultSet.getString("userID"), role));
                      break;
                    case FACILITY_ADMIN:
                        response = new ResponseEntitySet<>(new UserFacilityAdmin(resultSet.getString("facilityName"), role, resultSet.getString("userID")));
                      break;
                    default:
                      response = null;
                }
            }else{
                throw new SQLException(ErrorMessages.notAccessUserInformation);
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

    @Override
    public String isExists(String unameOrUserID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        String result = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL adminExists(?, ?) }");
            statement.setString(1, unameOrUserID);
            statement.setString(2, unameOrUserID);
             resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getString("result");
            }
        }catch (SQLException e){
            throw new ConnectionException(e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
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
}
