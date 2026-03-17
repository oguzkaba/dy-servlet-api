package com.kodlabs.doktorumyanimda.api;


import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.health_facility.*;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminCreateRequest;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminUpdateRequest;
import com.kodlabs.doktorumyanimda.model.health_facility.admin.HFAdminUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/health/facility")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HealthFacilityApi {

    @Path("/list")
    @GET
    public ResponseEntitySet<List<HealthFacility>> list(@QueryParam("userID")String userID){
        return Managers.healthFacilityManager.list(userID);
    }

    @Path("/{id}/detail")
    @GET
    public ResponseEntitySet<HealthFacilityDetail> detail(@QueryParam("userID")String userID, @PathParam("id")int id){
        return Managers.healthFacilityManager.detail(userID, id);
    }

    @Path("/{id}/delete")
    @DELETE
    public ResponseEntity delete(@QueryParam("userID")String userID, @PathParam("id")int id){
        return Managers.healthFacilityManager.delete(userID, id);
    }

    @Path("/update")
    @POST
    public ResponseEntity update(HealthFacilityUpdateRequest request){
        return Managers.healthFacilityManager.update(request);
    }

    @Path("/create")
    @POST
    public ResponseEntitySet<Integer> create(HealthFacilityCreateRequest request){
        return Managers.healthFacilityManager.create(request);
    }

    @Path("/admin/create")
    @POST
    public ResponseEntity createHFAdmin(HFAdminCreateRequest request){
        return Managers.healthFacilityManager.createHFAdmin(request);
    }

    @Path("/admin/{HFAdminID}/delete")
    @DELETE
    public ResponseEntity deleteHFAdmin(@QueryParam("userID")String userID, @PathParam("HFAdminID")String hfAdminID){
        return Managers.healthFacilityManager.deleteHFAdmin(userID, hfAdminID);
    }

    @Path("/admin/list")
    @GET
    public ResponseEntitySet<List<HFAdminUser>> listHFAdmin(@QueryParam("userID")String userID){
        return Managers.healthFacilityManager.listHFAdmin(userID);
    }

    @Path("/admin/{HFAdminID}/detail")
    @GET
    public ResponseEntitySet<HFAdminUser> detailHFAdmin(@QueryParam("userID")String userID, @PathParam("HFAdminID")String hfAdminID){
        return Managers.healthFacilityManager.detailHFAdmin(userID, hfAdminID);
    }

    @Path("/admin/update")
    @POST
    public ResponseEntity updateHFAdmin(HFAdminUpdateRequest request){
        return Managers.healthFacilityManager.updateHFAdmin(request);
    }
}
