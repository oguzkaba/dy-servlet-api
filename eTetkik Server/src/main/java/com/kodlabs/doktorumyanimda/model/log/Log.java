package com.kodlabs.doktorumyanimda.model.log;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    private int id;
    private String packageName;
    private String className;
    private String methodName;
    private String userID;
    private byte userType;
    private String sourceIp;
    private String targetIp;
    private String eventDescription;

    private String createDate;

    public Log(String packageName, String className, String methodName, String userID,
               byte userType, String sourceIp, String targetIp, String eventDescription) {
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.userID = userID;
        this.userType = userType;
        this.sourceIp = sourceIp;
        this.targetIp = targetIp;
        this.eventDescription = eventDescription;
    }

    public boolean isValid(){
        return !TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(className) && !TextUtils.isEmpty(methodName) &&
                !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(sourceIp) &&
                !TextUtils.isEmpty(targetIp) && !TextUtils.isEmpty(eventDescription);
    }
}
