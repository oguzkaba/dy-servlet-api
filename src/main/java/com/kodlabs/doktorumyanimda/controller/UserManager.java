package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.IUserDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.contact.Contact;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.user.*;
import com.kodlabs.doktorumyanimda.utils.*;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserManager {
    private final IUserDal userDal;
    public UserManager(IUserDal userDal){
        this.userDal = userDal;
    }

    public ResponseEntity existsUser(String unameOrUserID, byte role){
        if(TextUtils.isEmpty(unameOrUserID) || role < 0){
            return new ResponseEntity(false, ErrorMessages.operationFailed);
        }
        try{
            boolean exists = this.userDal.isExistsUser(unameOrUserID, role);
            if(exists){
                return new ResponseEntity();
            }else{
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Boolean> signUpPhoneControl(String phone, byte role){
        if(TextUtils.isEmpty(phone)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if(!Patterns.PHONE.matcher(phone).matches()){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidPhone);
        }
        try{
            return this.userDal.phoneExists(phone, role);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity singUpPhoneVerify(PhoneVerifyRequest request) {
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        if(!Patterns.PHONE.matcher(request.getPhone()).matches()){
            return new ResponseEntity(false, ErrorMessages.inValidPhone);
        }
        if(Phones.phoneVerification(request.getPhone(), request.getCode())){
            return new ResponseEntity();
        }else{
            return new ResponseEntity(false, ErrorMessages.inValidVerifyCode);
        }
    }

    public ResponseEntitySet<List<UserInformation>> allUser(String userID, String type){
        if(TextUtils.isEmpty(userID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(userID, Role.ADMIN.value())){
                return this.userDal.allUser(type);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public boolean isExistsUser(String no, byte role) throws ConnectionException {
        if(!TextUtils.isEmpty(no)){
            return this.userDal.isExistsUser(no,role);
        }
        return false;
    }

    public boolean deviceVerifyCountControl(String deviceID) throws ConnectionException, SQLException, NullPointerException{
        if(TextUtils.isEmpty(deviceID) || !Patterns.DEVICE_UUID.matcher(deviceID).matches()){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.userDal.deviceVerifyCountControl(deviceID);
    }

    public ResponseEntity deviceVerifyCountIncrease(String deviceID){
        if(TextUtils.isEmpty(deviceID)){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.userDal.deviceVerifyCountIncrease(deviceID);
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public Object getAttribute(String userNo, byte role, String attribute){
        if(userNo == null || userNo.isEmpty() || attribute == null || attribute.isEmpty())
            return null;
        try {
            if(isExistsUser(userNo,role)){
                return this.userDal.getAttribute(userNo,role,attribute);
            }
            return null;
        } catch (ConnectionException e) {
            return null;
        }
    }

    public Map<String,Object> getAttributes(String userNo, byte role, List<String > attributes){
        if(TextUtils.isEmpty(userNo) || attributes == null || attributes.isEmpty())
            return null;
        try {
            if(isExistsUser(userNo,role)){
                return this.userDal.getAttributes(userNo,role,attributes);
            }
            return null;
        } catch (ConnectionException e) {
            return null;
        }
    }

    public String getFullName(String userNo, byte role){
        if(userNo == null || userNo.isEmpty())
            return null;
        try{
            return this.userDal.getFullName(userNo,role);
        }catch (ConnectionException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean setAttribute(String userNo,byte role, String attribute, Object value) throws SQLException{
        if(userNo == null || userNo.isEmpty() || attribute == null || attribute.isEmpty() || value == null)
            return false;
        try {
            if(isExistsUser(userNo,role)){
                return this.userDal.setAttribute(userNo,role,attribute,value);
            }
            return false;
        } catch (ConnectionException e) {
            return false;
        }
    }
    public ResponseEntitySet<Contact> contactInformation(String uNameOrUserID, byte role){
        if(TextUtils.isEmpty(uNameOrUserID) || role == -1) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(uNameOrUserID,role)){
                if(role == Role.PATIENT.value()) {
                    return Managers.patientManager.contact(uNameOrUserID);
                }else {
                    return Managers.doctorManager.contact(uNameOrUserID);
                }
            }else{
                return new ResponseEntitySet<>(false,ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }
    }


    public ResponseEntity changePassword(String userID, byte role, ChangePassword data) {
        if(TextUtils.isEmpty(userID) || role < 0 || !data.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(userID, role)){
                return this.userDal.changePassword(userID, role, data);
            }else{
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<UserContactInformation> userContactInformation(String uname, byte role){
        if(TextUtils.isEmpty(uname) || role < 0){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(uname, role)){
                return this.userDal.userContactInformation(uname, role);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<AvailableContact> availableContact(String uname, byte role) {
        if(TextUtils.isEmpty(uname) || role == -1){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(uname, role)){
                return this.userDal.availableContact(uname, role);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<UserInformation> userInformation(String uname, byte role) {
        if(TextUtils.isEmpty(uname) || role < 0){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(uname, role)){
                return this.userDal.userInformation(uname, role);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity resetPassword(String userID, byte role, ResetPassword data) {
        if(TextUtils.isEmpty(userID) || role < 0 || !data.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(userID, role)){
                return this.userDal.resetPassword(userID, role, data);
            }else{
                return new ResponseEntity(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity contactCodeVerify(String address, String code) {
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(code)) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        if(ContactVerification.codeIsVerify(address, code)){
            return new ResponseEntity();
        }else{
            return new ResponseEntity(false, ErrorMessages.verifyCodeFailed);
        }
    }

    public ResponseEntitySet<Boolean> userPasswordVerify(String userID, byte role, String password) {
        if(TextUtils.isEmpty(userID) || role == -1 || TextUtils.isEmpty(password)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsUser(userID, role)){
                return this.userDal.userPasswordVerify(userID, role, password);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
}
