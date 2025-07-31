package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.address.City;
import com.kodlabs.doktorumyanimda.model.user.address.District;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("city")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CityApi {
    @GET
    @Path("/list")
    public ResponseEntitySet<List<City>> cities(){
        return Managers.cityManager.cities();
    }

    @GET
    @Path("/{cityCode}/district/list")
    public ResponseEntitySet<List<District>> districts(@PathParam("cityCode")int cityCode){
        return Managers.cityManager.districts(cityCode);
    }
}
