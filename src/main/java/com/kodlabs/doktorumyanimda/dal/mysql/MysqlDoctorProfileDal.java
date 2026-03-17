package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfile;
import com.kodlabs.doktorumyanimda.model.user.profile.DoctorProfileUpdate;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IDoctorProfileDal;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlDoctorProfileDal implements IDoctorProfileDal {
    @Override
    public boolean existsProfile(String userID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorProfileExists(?) }");
            statement.setString(1,userID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally{
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
    public ResponseEntitySet<DoctorProfile> profile(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<DoctorProfile> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorProfile(?) }");
            statement.setString(1, userID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                byte[] picture = resultSet.getBytes("picture");
                response = new ResponseEntitySet<>(new DoctorProfile(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        Functions.encodeBase64(picture),
                        resultSet.getString("gender"),
                        resultSet.getString("degree"),
                        resultSet.getBoolean("anonymous"),
                        resultSet.getBoolean("isFirstVisitFree"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("webAddress"),
                        resultSet.getInt("doctorType"),
                        resultSet.getInt("dayHourStart"),
                        resultSet.getInt("dayHourEnd"),
                        resultSet.getInt("dayMinutePeriod")
                ));
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notAccessProfileInformation);
            }
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
    public ResponseEntity updateProfile(String userID, DoctorProfile profile) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL doctorProfileUpdate(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) } ");
            statement.setString(1, profile.getDegree());
            statement.setString(2, Functions.nameFormat(profile.getName()));
            statement.setString(3, Functions.surnameFormat(profile.getSurname()));
            if(!TextUtils.isEmpty(profile.getPicture())){
                statement.setBinaryStream(4, new ByteArrayInputStream(Functions.decodeBase64(profile.getPicture())));
            }else{
                statement.setObject(4, null);
            }
            statement.setString(5, profile.getGender());
            statement.setString(6, profile.getMail());
            statement.setString(7, userID);
            statement.setString(8, profile.getWebAddress());
            statement.setBoolean(9, profile.isAnonymous());
            statement.setBoolean(10, profile.isFirstVisitFree());
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
    public ResponseEntity updateProfileV2(String doctorID, DoctorProfileUpdate profileUpdate) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorProfileUpdateV2(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            statement.setString(1, doctorID);
            statement.setString(2, profileUpdate.getPhone());
            statement.setString(3, profileUpdate.getMail());
            statement.setString(4, profileUpdate.getWebAddress());
            statement.setObject(5, Functions.decodeBase64(profileUpdate.getPicture()));
            statement.setBoolean(6, profileUpdate.isAnonymous());
            statement.setBoolean(7, profileUpdate.isFirstVisitFree());
            statement.setInt(8, profileUpdate.getDayHourStart());
            statement.setInt(9, profileUpdate.getDayHourEnd());
            statement.setInt(10, profileUpdate.getDayMinutePeriod());
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
