package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.ILogDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.utils.Patterns;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.util.List;

public class LogManager {
    private ILogDal iLogDal;
    public LogManager(ILogDal iLogDal){
        this.iLogDal = iLogDal;
    }
    public ResponseEntity create(Log log){
        if(!log.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.iLogDal.create(log);
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Log>> logs(String userID, String startDate, String endDate){
        if(TextUtils.isEmpty(userID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if(!TextUtils.isEmpty(startDate)){
            if(!Patterns.DATE.matcher(startDate).matches()){
                return new ResponseEntitySet<>(false, String.format(ErrorMessages.inValidDateFormat, startDate));
            }
        }
        if(!TextUtils.isEmpty(endDate)){
            if(!Patterns.DATE.matcher(endDate).matches()){
                return new ResponseEntitySet<>(false, String.format(ErrorMessages.inValidDateFormat, endDate));
            }
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.ADMIN.value())){
                return this.iLogDal.logs(startDate, endDate);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
}
