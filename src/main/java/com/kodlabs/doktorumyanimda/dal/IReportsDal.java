package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.BloodPressure;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.BloodPressurePatientInformation;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.BloodPressureUpdateReport;
import com.kodlabs.doktorumyanimda.model.report.bloodpressure.MeasureAverageValues;
import com.kodlabs.doktorumyanimda.model.report.complaint.Complaint;
import com.kodlabs.doktorumyanimda.model.report.complaint.ComplaintUpdateContentRequest;
import com.kodlabs.doktorumyanimda.model.report.complaint.ComplaintUpdateRequest;
import com.kodlabs.doktorumyanimda.model.report.examination.Examination;
import com.kodlabs.doktorumyanimda.model.report.examination.ExaminationUpdateContentRequest;
import com.kodlabs.doktorumyanimda.model.report.examination.ExaminationUpdateRequest;
import com.kodlabs.doktorumyanimda.model.report.userdrug.UserDrug;
import com.kodlabs.doktorumyanimda.model.report.userdrug.UserDrugRequest;
import com.kodlabs.doktorumyanimda.model.report.warning.PatientWarning;
import com.kodlabs.doktorumyanimda.model.report.warning.WarningContent;
import com.kodlabs.doktorumyanimda.model.report.warning.WarningUpdate;

import java.sql.SQLException;
import java.util.List;

public interface IReportsDal {
    ResponseEntity updateBloodPressure(BloodPressureUpdateReport report) throws ConnectionException;
    ResponseEntitySet<List<BloodPressure>> bloodPressureList(String patientID, Integer state) throws ConnectionException;
    ResponseEntitySet<List<BloodPressure>> bloodPressureListLimit(String patientID,int limit) throws ConnectionException;
    MeasureAverageValues bloodPressureAverageValue(String patientID) throws ConnectionException,SQLException;
    List<BloodPressurePatientInformation> getAvailableBloodPressurePatients() throws ConnectionException,SQLException;

    ResponseEntity addUserDrug(UserDrugRequest uploadRequest) throws ConnectionException;
    ResponseEntitySet<List<UserDrug>> userDrugs(String patientID) throws ConnectionException;
    boolean isExistsUserDrug(String userDrugID) throws ConnectionException, SQLException;
    ResponseEntity removeUserDrug(String userDrugID) throws ConnectionException;

    ResponseEntitySet<List<PatientWarning>> getWarningsPatients(String doctorID) throws ConnectionException;
    ResponseEntitySet<String> patientWarningID(String patientID) throws ConnectionException;
    String isAvailableWarningPatient(String patientID) throws ConnectionException,SQLException;
    ResponseEntitySet<Integer> isWarningsPatientCount(String doctorID) throws ConnectionException;
    String addWarningPatient(String patientID) throws ConnectionException,SQLException;
    ResponseEntity updateWarningPatientRead(String warningID, boolean isRead) throws ConnectionException;
    ResponseEntity addWarningContent(String warningID, WarningUpdate warningUpdate) throws ConnectionException;
    ResponseEntitySet<List<WarningContent>> warningContents(String warningID) throws ConnectionException;

    ResponseEntitySet<String> updateExamination(ExaminationUpdateRequest request) throws ConnectionException;
    ResponseEntity removeExamination(String examinationID) throws ConnectionException;
    boolean existsExamination(ExaminationUpdateRequest request) throws ConnectionException, SQLException;
    boolean existsExaminationID(String examinationID) throws ConnectionException, SQLException;
    ResponseEntity updateExaminationContent(ExaminationUpdateContentRequest request) throws ConnectionException;
    ResponseEntitySet<List<Examination>> listExamination(String userID) throws ConnectionException;
    ResponseEntitySet<List<String>> listExaminationContents(String id) throws ConnectionException;

    ResponseEntitySet<String> updateComplaint(ComplaintUpdateRequest request) throws ConnectionException;
    ResponseEntity updateComplaintContent(ComplaintUpdateContentRequest request) throws ConnectionException;
    boolean existsComplaintID(String complaintID) throws ConnectionException, SQLException;
    ResponseEntitySet<List<Complaint>> listComplaint(String userID) throws ConnectionException;
    ResponseEntity removeComplaint(String complaintID) throws ConnectionException;
    ResponseEntitySet<List<String>> listComplaintContents(String id) throws ConnectionException;
}
