package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IHealthFacilityDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.health_facility.*;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminCreateRequest;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminUpdateRequest;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminUser;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.util.List;

public class HealthFacilityManager {
    private final IHealthFacilityDal healthFacilityDal;

    public HealthFacilityManager(IHealthFacilityDal healthFacilityDal) {
        this.healthFacilityDal = healthFacilityDal;
    }

    public ResponseEntitySet<List<HealthFacility>> list(String userID){
        if(TextUtils.isEmpty(userID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.ADMIN.value())){
                return this.healthFacilityDal.list();
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<HealthFacilityDetail> detail(String userID, int id) {
        if(TextUtils.isEmpty(userID) || id < 0){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.ADMIN.value())){
                if(isExistsForId(id)){
                    return this.healthFacilityDal.detail(id);
                }else{
                    return new ResponseEntitySet<>(false, ErrorMessages.notFoundHealthFacility);
                }
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    private boolean isExistsForId(int id) throws ConnectionException {
        return this.healthFacilityDal.isExistsForId(id);
    }
    private boolean isExistsForName(String name) throws ConnectionException {
        return this.healthFacilityDal.isExistsForName(name);
    }

    public ResponseEntity delete(String userID, int id) {
        if(TextUtils.isEmpty(userID) || id < 0){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.ADMIN.value())){
                if(isExistsForId(id)){
                    if(this.healthFacilityDal.delete(id)){
                        return new ResponseEntity();
                    }else{
                        return new ResponseEntity(false, ErrorMessages.notDeleteHealthFacility);
                    }
                }else{
                    return new ResponseEntity(false, ErrorMessages.notFoundHealthFacility);
                }
            }else{
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity update(HealthFacilityUpdateRequest request) {
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(request.getUserID(), Role.ADMIN.value())){
                if(isExistsForId(request.getId())){
                    return this.healthFacilityDal.update(request.getId(), request.getData());
                }else{
                    return new ResponseEntity(false, ErrorMessages.notFoundHealthFacility);
                }
            }else{
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Integer> create(HealthFacilityCreateRequest request) {
        if(!request.isValid()){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(request.getUserID(), Role.ADMIN.value())){
                if(isExistsForName(request.getData().getName())){
                    return new ResponseEntitySet<>(false, ErrorMessages.alreadyHealthFacilityName);
                }else{
                    return this.healthFacilityDal.create(request.getData());
                }
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity createHFAdmin(HFAdminCreateRequest request) {
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(request.getUserID(), Role.ADMIN.value())){
                String uname = request.getData().getUname();
                if(Managers.userManager.isExistsUser(uname, Role.FACILITY_ADMIN.value()) ||
                   Managers.userManager.isExistsUser(uname, Role.ADMIN.value())){
                    return new ResponseEntity(false, ErrorMessages.availableUserName);
                }else{
                    return this.healthFacilityDal.createHFAdmin(request.getData());
                }
            }else{
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity deleteHFAdmin(String userID, String hcAdminID) {
        if(TextUtils.isEmpty(userID) || TextUtils.isEmpty(hcAdminID)){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.ADMIN.value())){
                if(Managers.userManager.isExistsUser(hcAdminID, Role.FACILITY_ADMIN.value())){
                    return this.healthFacilityDal.deleteHFAdmin(hcAdminID);
                }else{
                    return new ResponseEntity(false, ErrorMessages.notAccessHFAdminInformation);
                }
            }else{
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<HFAdminUser>> listHFAdmin(String userID) {
        if(TextUtils.isEmpty(userID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.ADMIN.value())){
                return this.healthFacilityDal.listHFAdmin();
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<HFAdminUser> detailHFAdmin(String userID, String hfAdminID) {
        if(TextUtils.isEmpty(userID) || TextUtils.isEmpty(hfAdminID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.ADMIN.value())){
                if(Managers.userManager.isExistsUser(hfAdminID, Role.FACILITY_ADMIN.value())){
                    return this.healthFacilityDal.detailHFAdmin(hfAdminID);
                }else{
                    return new ResponseEntitySet<>(false, ErrorMessages.notAccessHFAdminInformation);
                }
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity updateHFAdmin(HFAdminUpdateRequest request) {
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(request.getUserID(), Role.ADMIN.value())){
                if(Managers.userManager.isExistsUser(request.getFacilityAdminID(), Role.FACILITY_ADMIN.value())){
                    return this.healthFacilityDal.updateHFAdmin(request.getFacilityAdminID(), request.getData());
                }else{
                    return new ResponseEntity(false, ErrorMessages.notAccessHFAdminInformation);
                }
            }else{
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
}
