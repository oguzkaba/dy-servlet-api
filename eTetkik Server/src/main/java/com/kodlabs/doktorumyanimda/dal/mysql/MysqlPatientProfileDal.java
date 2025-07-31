package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.PatientProfileUpdateV2;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPatientProfileDal;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlPatientProfileDal implements IPatientProfileDal {
    @Override
    public boolean existsProfile(String userID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientProfileExits(?) }");
            statement.setString(1,userID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally{
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
        return result;
    }

    @Override
    public ResponseEntitySet<PatientProfile> profile(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<PatientProfile> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientProfile(?) }");
            statement.setString(1, userID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new PatientProfile(
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("gender"),
                                resultSet.getInt("age"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getBoolean("visiblePhone")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notAccessProfileInformation);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false,e.getLocalizedMessage());
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
    public ResponseEntity updateProfile(String userID, PatientProfile profile) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientProfileUpdate(?, ?, ?, ?, ?, ?, ?, ?) }");
            statement.setString(1, Functions.nameFormat(profile.getName()));
            statement.setString(2, Functions.surnameFormat(profile.getSurname()));
            if(!TextUtils.isEmpty(profile.getPicture())){
                statement.setBinaryStream(3, new ByteArrayInputStream(Functions.decodeBase64(profile.getPicture())));
            }else{
                statement.setObject(3, null);
            }
            statement.setString(4, profile.getGender());
            statement.setInt(5, profile.getAge());
            statement.setString(6, profile.getMail());
            statement.setString(7, userID);
            statement.setBoolean(8, profile.isVisiblePhone());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
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
    public ResponseEntity create(String userID, PatientProfile profile) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientProfileCreate(?, ?, ?, ?, ?, ?, ?, ?) }");
            statement.setString(1, profile.getName());
            statement.setString(2, profile.getSurname());
            if(!TextUtils.isEmpty(profile.getPicture())){
                statement.setBinaryStream(3, new ByteArrayInputStream(Functions.decodeBase64(profile.getPicture())));
            }else{
                statement.setObject(3, null);
            }
            statement.setString(4, profile.getGender());
            statement.setInt(5, profile.getAge());
            statement.setString(6, profile.getMail());
            statement.setString(7, userID);
            statement.setBoolean(8, profile.isVisiblePhone());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
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
    public ResponseEntity updateProfileV2(String patientID, PatientProfileUpdateV2 profile) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientProfileUpdateV2(?, ?, ?, ?, ?)}");
            statement.setObject(1, Functions.decodeBase64(profile.getPicture()));
            statement.setString(2, profile.getMail());
            statement.setString(3, profile.getPhone());
            statement.setString(4, patientID);
            statement.setBoolean(5, profile.isVisiblePhone());
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
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
}
