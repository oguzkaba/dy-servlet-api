package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;

import java.util.List;

public interface ILogDal {
    ResponseEntity create(Log log) throws ConnectionException;

    ResponseEntitySet<List<Log>> logs(String startDateTime, String endDateTime) throws ConnectionException;
}
