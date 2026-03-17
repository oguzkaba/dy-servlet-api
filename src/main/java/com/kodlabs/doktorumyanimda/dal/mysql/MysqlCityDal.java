package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.ICityDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.address.City;
import com.kodlabs.doktorumyanimda.model.user.address.District;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlCityDal implements ICityDal {
    @Override
    public ResponseEntitySet<List<City>> cities() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<City>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM city");
            resultSet = statement.executeQuery();
            List<City> cities = new ArrayList<>();
            while(resultSet.next()){
                cities.add(
                        new City(
                                resultSet.getString("name"),
                                resultSet.getInt("id")
                        )
                );
            }
            response = new ResponseEntitySet<>(cities);
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
    public ResponseEntitySet<List<District>> districts(int cityCode) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<District>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM district WHERE city_id = ? ORDER BY city_id, `name` ASC");
            statement.setInt(1, cityCode);
            resultSet = statement.executeQuery();
            List<District> districts = new ArrayList<>();
            while(resultSet.next()){
                districts.add(
                        new District(
                                resultSet.getString("name")
                        )
                );
            }
            response = new ResponseEntitySet<>(districts);
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
