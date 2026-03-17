package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IReportsDal;
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
import com.kodlabs.doktorumyanimda.model.report.warning.WarningsPatientUpdateRequest;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.sql.SQLException;
import java.util.List;

public class ReportManager {
    private IReportsDal reportsDal;
    public ReportManager(IReportsDal reportsDal){
        this.reportsDal = reportsDal;
    }
    public ResponseEntity bloodPressureUpdate(BloodPressureUpdateReport report){
        if(!report.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.updateBloodPressure(report);
        }catch (ConnectionException e){
            return new ResponseEntity(false,e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<List<BloodPressure>> bloodPressureList(String patientID, Integer state){
        if(TextUtils.isEmpty(patientID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if(Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())){
                return this.reportsDal.bloodPressureList(patientID, state);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<List<BloodPressure>> bloodPressureListLimit(String patientID,Integer limit){
        if(TextUtils.isEmpty(patientID) || limit == null){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if(Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())){
                return this.reportsDal.bloodPressureListLimit(patientID, limit);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity userDrugAdd(UserDrugRequest request){
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.addUserDrug(request);
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<List<UserDrug>> userDrugList(String patientID){
        if(TextUtils.isEmpty(patientID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if(Managers.userManager.isExistsUser(patientID, Role.PATIENT.value())){
                return this.reportsDal.userDrugs(patientID);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntity userDrugRemove(String userDrugID){
        if(TextUtils.isEmpty(userDrugID)){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(existsUserDrug(userDrugID)){
                return this.reportsDal.removeUserDrug(userDrugID);
            }else{
                return new ResponseEntity(false, ErrorMessages.notAccessUserDrugInformation);
            }
        }catch (ConnectionException | SQLException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public boolean existsUserDrug(String userDrugID) throws ConnectionException, SQLException, NullPointerException{
        if(TextUtils.isEmpty(userDrugID)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.reportsDal.isExistsUserDrug(userDrugID);
    }


    /* Warnings */
    public ResponseEntitySet<List<PatientWarning>> warningPatients(String doctorID){
        if(TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false,  ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())){
                return this.reportsDal.getWarningsPatients(doctorID);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<String> patientWarningID(String patientID){
        if(TextUtils.isEmpty(patientID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(patientID,Role.PATIENT.value())){
                return this.reportsDal.patientWarningID(patientID);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<List<WarningContent>> warningContents(String warningID){
        if(TextUtils.isEmpty(warningID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.warningContents(warningID);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
    public List<BloodPressurePatientInformation> getAvailableBloodPressurePatients() throws SQLException, ConnectionException {
        return this.reportsDal.getAvailableBloodPressurePatients();
    }
    public ResponseEntity updateWarningPatientRead(String warningID, boolean isRead){
        if(TextUtils.isEmpty(warningID)) {
            return new ResponseEntity(false,  ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.updateWarningPatientRead(warningID, isRead);
        }catch (ConnectionException e){
            return new ResponseEntity(false,e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<Integer> patientWarningUnReadCount(String doctorID){
        if(TextUtils.isEmpty(doctorID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.isWarningsPatientCount(doctorID);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntity updateWarning(WarningsPatientUpdateRequest updateRequest){
        if(!updateRequest.isValid()) {
            return new ResponseEntity(false, ErrorMessages.operationFailed);
        }
        try{
            String id = isAvailableWarningPatient(updateRequest.getPatientID());
            if(TextUtils.isEmpty(id)) {
                id = addWarningsPatient(updateRequest.getPatientID());
            }
            if(!TextUtils.isEmpty(id)){
                return this.reportsDal.addWarningContent(id, updateRequest.getWarningUpdate());
            }else {
                return new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (ConnectionException | SQLException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    private String addWarningsPatient(String patientID) throws NullPointerException,SQLException,ConnectionException{
        if(TextUtils.isEmpty(patientID)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.reportsDal.addWarningPatient(patientID);
    }
    private String isAvailableWarningPatient(String patientID) throws NullPointerException,SQLException,ConnectionException{
        if(TextUtils.isEmpty(patientID)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.reportsDal.isAvailableWarningPatient(patientID);
    }
    public MeasureAverageValues bloodPressureAverageValues(String patientID) throws ConnectionException, SQLException, NullPointerException{
        if(TextUtils.isEmpty(patientID)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.reportsDal.bloodPressureAverageValue(patientID);
    }
    /* End */

    /*Examination*/

    public ResponseEntitySet<String> examinationUpdate(ExaminationUpdateRequest request){
        if(!request.isValid()){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(request.getUserID(), Role.PATIENT.value())){
                try{
                    if(this.reportsDal.existsExamination(request)){
                        return new ResponseEntitySet<>(false, ErrorMessages.alreadyExamination);
                    }else{
                        return this.reportsDal.updateExamination(request);
                    }
                }catch (SQLException e){
                    return new ResponseEntitySet<>(false, e.getLocalizedMessage());
                }
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity examinationRemove(String examinationID){
        if(TextUtils.isEmpty(examinationID)){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsExaminationID(examinationID)){
                return this.reportsDal.removeExamination(examinationID);
            }else{
                return new ResponseEntity(false, ErrorMessages.notAccessExaminationInformation);
            }

        }catch (ConnectionException | SQLException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntity examinationContentUpdate(ExaminationUpdateContentRequest request){
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.updateExaminationContent(request);
        }catch (ConnectionException e){
            return new ResponseEntity(false,e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Examination>> examinationList(String userID){
        if(TextUtils.isEmpty(userID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.PATIENT.value()))
                return this.reportsDal.listExamination(userID);
            else
                return new ResponseEntitySet<>(false,ErrorMessages.notAccessUserInformation);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<List<String>> examinationContentList(String id){
        if(TextUtils.isEmpty(id)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsExaminationID(id)){
                return this.reportsDal.listExaminationContents(id);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessExaminationInformation);
            }
        }catch (ConnectionException | SQLException | NullPointerException e){
            return new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }
    }

    public boolean isExistsExaminationID(String examinationID) throws ConnectionException, SQLException, NullPointerException{
        if(TextUtils.isEmpty(examinationID)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.reportsDal.existsExaminationID(examinationID);
    }
    /* End */


    /* Complaint */

    public ResponseEntitySet<String> updateComplaint(ComplaintUpdateRequest request){
        if(!request.isValid()){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(request.getUserID(), Role.PATIENT.value())){
                return this.reportsDal.updateComplaint(request);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }
    }

    public ResponseEntity updateComplaintContent(ComplaintUpdateContentRequest request){
        if(!request.isValid()){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.updateComplaintContent(request);
        }catch (ConnectionException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntity removeComplaint(String id){
        if(TextUtils.isEmpty(id)){
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try{
            if(isExistsComplaintID(id)){
                return this.reportsDal.removeComplaint(id);
            }else{
                return new ResponseEntity(false, ErrorMessages.notAccessComplaintInformation);
            }
        }catch (ConnectionException | SQLException | NullPointerException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public boolean isExistsComplaintID(String complaintID) throws ConnectionException, SQLException, NullPointerException{
        if(TextUtils.isEmpty(complaintID)){
            throw new NullPointerException(ErrorMessages.inValidData);
        }
        return this.reportsDal.existsComplaintID(complaintID);
    }

    public ResponseEntitySet<List<Complaint>> listComplaint(String userID){
        if(TextUtils.isEmpty(userID)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(userID, Role.PATIENT.value()))
                return this.reportsDal.listComplaint(userID);
            else
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }
    public ResponseEntitySet<List<String>> listComplaintContent(String id){
        if(TextUtils.isEmpty(id)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            return this.reportsDal.listComplaintContents(id);
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }
    }
    /* End */
}
