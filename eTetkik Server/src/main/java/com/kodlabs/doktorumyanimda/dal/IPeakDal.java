package com.kodlabs.doktorumyanimda.dal;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.peak.*;
import com.kodlabs.doktorumyanimda.model.peak.*;

import java.sql.SQLException;
import java.util.List;

public interface IPeakDal {
    ResponseEntitySet<String> create(CreatePeakRequest request) throws ConnectionException;
    ResponseEntitySet<String> waitUpdate(CreatePeakRequest request) throws ConnectionException;
    boolean isExistsActiveOrWait(String patientID, String doctorID) throws ConnectionException, SQLException;
    boolean isExists(String patientID, String doctorID) throws ConnectionException, SQLException;
    boolean isExistsPeakUser(String peakID, String userID, byte role) throws ConnectionException, SQLException;
    ResponseEntitySet<Boolean> isFirstVisitFree(String patientID, String doctorID) throws ConnectionException;


    ResponseEntitySet<List<Peak>> list(String userID, byte role) throws ConnectionException;
    ResponseEntitySet<Peak> detail(String peakID, byte role) throws ConnectionException;

    ResponseEntitySet<PeakPayInformation> pay(PeakPayRequest request) throws ConnectionException;
    boolean feeControl(String patientID, String doctorID, int peakDay, int fee) throws ConnectionException, SQLException;
    boolean payControl(String peakID, int fee) throws ConnectionException, SQLException;

    ResponseEntitySet<DemandVerify> peakDemandVerify(PeakDemandVerifyRequest request) throws ConnectionException;

    ResponseEntitySet<List<String>> patientDoctorIDs(String patientID) throws ConnectionException;
    ResponseEntitySet<List<Integer>> feeList() throws ConnectionException;
    ResponseEntitySet<List<Integer>> peakDayList() throws ConnectionException;

    ResponseEntitySet<List<GoogleProduct>> googleProductList() throws ConnectionException;

    ResponseEntitySet<String> firstVisitPeakActiveStatus(String patientID) throws ConnectionException;

    String roleID(String peakID, byte role) throws ConnectionException;
}
