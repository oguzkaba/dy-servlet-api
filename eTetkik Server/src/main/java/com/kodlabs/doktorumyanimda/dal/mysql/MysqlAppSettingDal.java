package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.setting.AppSetting;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IAppSettingDal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlAppSettingDal implements IAppSettingDal {
    @Override
    public ResponseEntitySet<AppSetting> appSetting() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<AppSetting> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM app_setting");
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new AppSetting(
                                resultSet.getBoolean("coin_type")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
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
}
