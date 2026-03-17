package com.kodlabs.doktorumyanimda.dal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IUserDal {
    boolean isExistsUser(String no,byte role) throws ConnectionException;
    boolean deviceVerifyCountControl(String deviceID) throws ConnectionException, SQLException;
    ResponseEntity deviceVerifyCountIncrease(String deviceID) throws ConnectionException;
    String getFullName(String userNo,byte role)  throws ConnectionException;
    Object getAttribute(String userNo,byte role, String attribute)  throws ConnectionException;
    Map<String,Object> getAttributes(String userNo,byte role, List<String> attributes)  throws ConnectionException;
    boolean setAttribute(String userNo,byte role, String attribute, Object value)  throws ConnectionException, SQLException;

    ResponseEntitySet<List<UserInformation>> allUser(String type) throws ConnectionException;

    ResponseEntitySet<Boolean> phoneExists(String phone, byte role) throws ConnectionException;

    ResponseEntity changePassword(String userID, byte role, ChangePassword data) throws ConnectionException;

    ResponseEntitySet<AvailableContact> availableContact(String uname, byte role) throws ConnectionException;

    ResponseEntitySet<UserContactInformation> userContactInformation(String uname, byte role) throws ConnectionException;

    ResponseEntitySet<UserInformation> userInformation(String uname, byte role) throws ConnectionException;

    ResponseEntity resetPassword(String userID, byte role, ResetPassword data) throws ConnectionException;

    ResponseEntitySet<Boolean> userPasswordVerify(String userID, byte role, String password) throws ConnectionException;
}