package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.*;
import com.kodlabs.doktorumyanimda.dal.mysql.*;
import com.kodlabs.doktorumyanimda.controller.*;
import com.kodlabs.doktorumyanimda.dal.mysql.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api")
public class Api extends Application {
    public Api(){
        Managers.userManager = new UserManager(new MysqlUserDal());
        Managers.notificationManager = new NotificationManager(new MysqlNotificationDal());
        Managers.reportManager = new ReportManager(new MysqlReportDal());
        Managers.adminManager = new AdminManager(new MysqlAdminDal());
        Managers.patientManager = new PatientManager(new MysqlPatientDal());
        Managers.doctorManager = new DoctorManager(new MysqlDoctorDal());
        Managers.messageManager = new MessageManager(new MysqlMessageDal());
        Managers.peakManager = new PeakManager(new MysqlPeakDal());

        Managers.doctorProfileManager = new DoctorProfileManager(new MysqlDoctorProfileDal());
        Managers.patientProfileManager = new PatientProfileManager(new MysqlPatientProfileDal());
        Managers.appSettingManager = new AppSettingManager(new MysqlAppSettingDal());
        Managers.videoChatManager = new VideoChatManager();
        Managers.healthFacilityManager = new HealthFacilityManager(new MysqlHealthFacilityDal());
        Managers.cityManager = new CityManager(new MysqlCityDal());

        Managers.logManager = new LogManager(new MysqlLogDal());
    }
}
