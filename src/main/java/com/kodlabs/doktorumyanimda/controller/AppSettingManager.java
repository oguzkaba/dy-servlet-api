package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IAppSettingDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.setting.AppSetting;

public class AppSettingManager {
    private final IAppSettingDal iAppSettingDal;
    public AppSettingManager(IAppSettingDal iAppSettingDal){
        this.iAppSettingDal = iAppSettingDal;
    }
    public ResponseEntitySet<AppSetting> appSetting(){
        try{
            return this.iAppSettingDal.appSetting();
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
}
