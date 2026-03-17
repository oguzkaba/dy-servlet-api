package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.ICityDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.address.City;
import com.kodlabs.doktorumyanimda.model.user.address.District;

import java.util.List;

public class CityManager {
    private final ICityDal cityDal;
    public CityManager(ICityDal cityDal){
        this.cityDal = cityDal;
    }
    public ResponseEntitySet<List<City>> cities(){
        try{
            return this.cityDal.cities();
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<District>> districts(int cityCode) {
        if(!(cityCode >= 1 && cityCode <= 81)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            return this.cityDal.districts(cityCode);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
}
