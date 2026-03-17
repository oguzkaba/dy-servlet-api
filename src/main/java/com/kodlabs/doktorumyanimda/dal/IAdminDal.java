package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.admin.user.AdminLoginRequest;

public interface IAdminDal {
    String login(AdminLoginRequest request) throws ConnectionException;
    ResponseEntity verifyCodeUpdate(String uname, String code) throws ConnectionException;

    Object verifyCode(String uname, String code) throws ConnectionException;

    String isExists(String uname)throws ConnectionException;

}
