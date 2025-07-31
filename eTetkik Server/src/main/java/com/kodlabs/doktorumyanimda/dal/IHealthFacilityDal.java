package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.health_facility.HealthFacility;
import com.kodlabs.doktorumyanimda.model.health_facility.HealthFacilityDetail;
import com.kodlabs.doktorumyanimda.model.health_facility.HealthFacilityCU;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminBase;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminCreate;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminUser;

import java.util.List;

public interface IHealthFacilityDal {
    ResponseEntitySet<List<HealthFacility>> list() throws ConnectionException;

    boolean isExistsForId(int id) throws ConnectionException;
    boolean isExistsForName(String name) throws ConnectionException;

    ResponseEntitySet<HealthFacilityDetail> detail(int id) throws ConnectionException;

    boolean delete(int id) throws ConnectionException;

    ResponseEntity update(int id, HealthFacilityCU data) throws ConnectionException;

    ResponseEntitySet<Integer> create(HealthFacilityCU data) throws ConnectionException;

    ResponseEntity createHFAdmin(HFAdminCreate data) throws ConnectionException;

    ResponseEntity deleteHFAdmin(String hcAdminID) throws ConnectionException;

    ResponseEntitySet<List<HFAdminUser>> listHFAdmin() throws ConnectionException;

    ResponseEntitySet<HFAdminUser> detailHFAdmin(String hfAdminID) throws ConnectionException;

    ResponseEntity updateHFAdmin(String facilityAdminID, HFAdminBase data) throws ConnectionException;
}
