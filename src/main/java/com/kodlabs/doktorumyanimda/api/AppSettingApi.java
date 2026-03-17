package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.setting.AppSetting;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/setting")
public class AppSettingApi {

    @GET
    @Path("/app")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<AppSetting> appSetting(){
        return Managers.appSettingManager.appSetting();
    }
}
