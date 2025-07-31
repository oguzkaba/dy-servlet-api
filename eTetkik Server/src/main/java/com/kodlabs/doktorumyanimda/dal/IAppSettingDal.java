package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.setting.AppSetting;

public interface IAppSettingDal {
    ResponseEntitySet<AppSetting> appSetting() throws ConnectionException;
}
