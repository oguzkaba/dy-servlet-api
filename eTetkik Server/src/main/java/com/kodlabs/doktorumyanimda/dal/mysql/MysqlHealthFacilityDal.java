package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IHealthFacilityDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.health_facility.HealthFacility;
import com.kodlabs.doktorumyanimda.model.health_facility.HealthFacilityDetail;
import com.kodlabs.doktorumyanimda.model.health_facility.HealthFacilityCU;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminBase;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminCreate;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminUser;
import com.kodlabs.doktorumyanimda.utils.Functions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlHealthFacilityDal implements IHealthFacilityDal {
    @Override
    public ResponseEntitySet<List<HealthFacility>> list() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<HealthFacility>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityList() }");
            resultSet = statement.executeQuery();
            List<HealthFacility> facilities = new ArrayList<>();
            while(resultSet.next()){
                facilities.add(
                        new HealthFacility(
                              resultSet.getInt("id"),
                              resultSet.getString("name"),
                              resultSet.getString("description"),
                              resultSet.getString("address"),
                              resultSet.getString("phone"),
                              resultSet.getInt("facilityID"),
                              resultSet.getString("createDate")
                        )
                );
            }
            response = new ResponseEntitySet<>(facilities);
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
    public boolean isExistsForId(int id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL heathFacilityExistsForId(?) }");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new ConnectionException(ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            throw new ConnectionException(e.getLocalizedMessage());
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
        return result;
    }

    @Override
    public boolean isExistsForName(String name) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL heathFacilityExistsForName(?) }");
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new ConnectionException(ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            throw new ConnectionException(e.getLocalizedMessage());
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
        return result;
    }

    @Override
    public ResponseEntitySet<HealthFacilityDetail> detail(int id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<HealthFacilityDetail> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL heathFacilityDetail(?) }");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new HealthFacilityDetail(
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("address"),
                                resultSet.getLong("tax_number"),
                                resultSet.getString("phone"),
                                resultSet.getInt("facilityID"),
                                resultSet.getString("createDate")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notFoundHealthFacility);
            }
        }catch (SQLException  e){
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
    public boolean delete(int id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityDelete(?) }");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            throw new ConnectionException(e.getLocalizedMessage());
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
        return result;
    }

    @Override
    public ResponseEntity update(int id, HealthFacilityCU data) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityUpdate(?, ?, ?, ?, ?, ?, ?) }");
            statement.setInt(1, id);
            statement.setString(2, data.getName());
            statement.setString(3, data.getDescription());
            statement.setString(4, data.getAddress());
            statement.setLong(5, data.getTax_number());
            statement.setString(6, data.getPhone());
            statement.setInt(7, data.getFacilityID());
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
    public ResponseEntitySet<Integer> create(HealthFacilityCU data) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Integer> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityCreate(?, ?, ?, ?, ?, ?) }");
            statement.setString(1, data.getName());
            statement.setString(2, data.getDescription());
            statement.setString(3, data.getAddress());
            statement.setLong(4, data.getTax_number());
            statement.setString(5, data.getPhone());
            statement.setInt(6, data.getFacilityID());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getInt("id")
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notCreateHealthFacility);
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
    public ResponseEntity createHFAdmin(HFAdminCreate data) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityAdminCreate(?, ?, ?, ?, ?, ?, ?) }");
            statement.setString(1, data.getUname());
            statement.setString(2, Functions.toSHA1(data.getPassword()));
            statement.setString(3, data.getName());
            statement.setString(4, data.getSurname());
            statement.setString(5, data.getEmail());
            statement.setString(6, data.getPhone());
            statement.setInt(7, data.getFacilityID());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("result")){
                    response = new ResponseEntity();
                }else{
                    throw new SQLException(ErrorMessages.operationFailed);
                }
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
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
    public ResponseEntity deleteHFAdmin(String hcAdminID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityAdminDelete(?) }");
            statement.setString(1, hcAdminID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("result")){
                    response = new ResponseEntity();
                }else{
                    throw new SQLException(ErrorMessages.operationFailed);
                }
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
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
    public ResponseEntitySet<List<HFAdminUser>> listHFAdmin() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<HFAdminUser>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityAdminList() }");
            resultSet = statement.executeQuery();
            List<HFAdminUser> list = new ArrayList<>();
            while(resultSet.next()){
                list.add(new HFAdminUser(
                        resultSet.getString("uname"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getInt("facilityID"),
                        resultSet.getString("userID")
                ));
            }
            response = new ResponseEntitySet<>(list);
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
    public ResponseEntitySet<HFAdminUser> detailHFAdmin(String hfAdminID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<HFAdminUser> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityAdminDetail(?) }");
            statement.setString(1, hfAdminID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new HFAdminUser(
                                resultSet.getString("uname"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("phone"),
                                resultSet.getString("email"),
                                resultSet.getInt("facilityID"),
                                resultSet.getString("userID")
                        )
                );
            }else{
                throw new ConnectionException(ErrorMessages.notAccessHFAdminInformation);
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
        return  response;
    }

    @Override
    public ResponseEntity updateHFAdmin(String facilityAdminID, HFAdminBase data) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL healthFacilityAdminUpdate(?, ?, ?, ?, ?) }");
            statement.setString(1, facilityAdminID);
            statement.setString(2, data.getName());
            statement.setString(3, data.getSurname());
            statement.setString(4, data.getPhone());
            statement.setString(5, data.getEmail());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getBoolean("result")){
                    response = new ResponseEntity();
                }else{
                    throw new SQLException(ErrorMessages.operationFailed);
                }
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
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
