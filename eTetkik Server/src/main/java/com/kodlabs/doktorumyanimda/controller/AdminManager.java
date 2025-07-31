package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IAdminDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;
import com.kodlabs.doktorumyanimda.model.log.LogEventDescription;
import com.kodlabs.doktorumyanimda.model.user.LoginData;
import com.kodlabs.doktorumyanimda.model.admin.user.AdminLoginRequest;
import com.kodlabs.doktorumyanimda.model.admin.user.AdminLoginVerifyRequest;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.net.UnknownHostException;

public class AdminManager {
    private IAdminDal adminDal;
    public AdminManager(IAdminDal adminDal){
        this.adminDal = adminDal;
    }

    public ResponseEntitySet<LoginData> login(AdminLoginRequest request){
        if(!request.isValid()){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            String role = Managers.adminManager.isExists(request.getUname());
            if(TextUtils.isEmpty(role)){
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }else{
                String phone = this.adminDal.login(request);
                if(!TextUtils.isEmpty(phone)){
                    String code = Functions.generateCode();
                    ResponseEntity updateResponse = verifyCodeUpdate(request.getUname(), code);
                    if(updateResponse.isSuccess){
                        return new ResponseEntitySet<>(new LoginData(phone, code));
                    }else{
                        return new ResponseEntitySet<>(false, updateResponse.message);
                    }
                }else{
                    return new ResponseEntitySet<>(false, ErrorMessages.uNameOrPasswordInCorrect);
                }
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public Object verifyCode(AdminLoginVerifyRequest request, String ip){
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            String role = isExists(request.getUname());
            if(TextUtils.isEmpty(role)){
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }else{
                Object response = this.adminDal.verifyCode(request.getUname(), request.getCode());
                if(response instanceof ResponseEntity){
                    if(((ResponseEntity)response).isSuccess){
                        adminLogSend("verifyCode", request.getUname(), ip, LogEventDescription.LOGIN.getMessage());
                    }
                }
                return response;
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public String isExists(String unameOrUserID) throws ConnectionException, NullPointerException{
        if(TextUtils.isEmpty(unameOrUserID)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.adminDal.isExists(unameOrUserID);
    }

    public ResponseEntity verifyCodeUpdate(String uname, String code) throws ConnectionException, NullPointerException{
        if(TextUtils.isEmpty(uname) || TextUtils.isEmpty(code)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.adminDal.verifyCodeUpdate(uname, code);
    }

    private void adminLogSend(String methodName, String phoneOrUserID, String ip, String eventDescription){
        new Thread(()->{
            try {
                Managers.logManager.create(
                        new Log(
                                "com.kodlabs.doktorumyanimda.controller",
                                AdminManager.class.getSimpleName(),
                                methodName,
                                phoneOrUserID,
                                Role.ADMIN.value(),
                                ip,
                                Functions.getIpAddress(),
                                eventDescription
                        )
                );
            } catch (
                    UnknownHostException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();
    }
}
