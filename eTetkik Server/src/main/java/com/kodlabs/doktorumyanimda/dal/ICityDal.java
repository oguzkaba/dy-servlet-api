package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.address.City;
import com.kodlabs.doktorumyanimda.model.user.address.District;

import java.util.List;

public interface ICityDal {
    ResponseEntitySet<List<City>> cities() throws ConnectionException;
    ResponseEntitySet<List<District>> districts(int cityCode) throws ConnectionException;
}
