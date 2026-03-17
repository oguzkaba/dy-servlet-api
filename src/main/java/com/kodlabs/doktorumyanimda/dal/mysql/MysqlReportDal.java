package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.patient.Patient;
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
import com.kodlabs.doktorumyanimda.model.report.userdrug.UserDrugReport;
import com.kodlabs.doktorumyanimda.model.report.userdrug.UserDrugRequest;
import com.kodlabs.doktorumyanimda.model.report.warning.PatientWarning;
import com.kodlabs.doktorumyanimda.model.report.warning.WarningContent;
import com.kodlabs.doktorumyanimda.model.report.warning.WarningUpdate;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IReportsDal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlReportDal implements IReportsDal {
    @Override
    public ResponseEntity updateBloodPressure(BloodPressureUpdateReport report) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            //statement = MysqlConnection.getInstance().prepareStatement("insert into blood_pressure (patientID, reportDate, minorValue,majorValue,pulseValue) values (?, now(), ?, ?, ?)");
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL createBloodPressure(?, ?, ?, ?) }");
            statement.setString(1, report.getPatientID());
            statement.setInt(2, report.getBloodPressure().minorValue);
            statement.setInt(3, report.getBloodPressure().majorValue);
            statement.setInt(4, report.getBloodPressure().pulseValue);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<BloodPressure>> bloodPressureList(String patientID, Integer state) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<BloodPressure>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL bloodPressureList(?, ?) }");
            statement.setString(1, patientID);
            statement.setObject(2, state);
            resultSet = statement.executeQuery();
            List<BloodPressure> bloodPressureList = new ArrayList<>();
            while (resultSet.next()){
                bloodPressureList.add(new BloodPressure(resultSet.getInt("minorValue"),
                                                        resultSet.getInt("majorValue"),
                                                        resultSet.getInt("pulseValue"),
                                                        resultSet.getString("reportDate")));
            }
            response = new ResponseEntitySet<>(bloodPressureList);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<BloodPressure>> bloodPressureListLimit(String patientID, int limit) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<BloodPressure>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL bloodPressureListLimit(?, ?) }");
            statement.setString(1, patientID);
            statement.setInt(2, limit);
            resultSet = statement.executeQuery();
            final List<BloodPressure> bloodPressureList = new ArrayList<>();
            while (resultSet.next()){
                bloodPressureList.add(new BloodPressure(resultSet.getInt("minorValue"),
                        resultSet.getInt("majorValue"),
                        resultSet.getInt("pulseValue"),
                        resultSet.getString("reportDate")));
            }
            response = new ResponseEntitySet<>(bloodPressureList);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false,e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity addUserDrug(UserDrugRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL userDrugAdd(?, ?) }");
            statement.setString(1, request.getUserID());
            statement.setString(2, Common.gson.toJson(request.getReport()));
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }
    @Override
    public ResponseEntitySet<List<UserDrug>> userDrugs(String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<UserDrug>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL  userDrugs(?) } ");
            statement.setString(1,patientID);
            resultSet = statement.executeQuery();
            List<UserDrug> userDrugs = new ArrayList<>();
            while(resultSet.next()){
                userDrugs.add(
                        new UserDrug(
                                resultSet.getString("id"),
                                Common.gson.fromJson(resultSet.getString("content"), UserDrugReport.class),
                                resultSet.getString("date")
                        )
                );
            }
            response = new ResponseEntitySet<>(userDrugs);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public boolean isExistsUserDrug(String userDrugID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL userDrugExists(?) }");
            statement.setString(1, userDrugID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ResponseEntity removeUserDrug(String userDrugID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL userDrugRemove(?) }");
            statement.setString(1, userDrugID);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<PatientWarning>> getWarningsPatients(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<PatientWarning>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL patientWarnings(?) } ");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            List<PatientWarning> patientList = new ArrayList<>();
            while(resultSet.next()){
                patientList.add(new PatientWarning(new Patient(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("userID"),
                        Functions.encodeBase64(resultSet.getBytes("picture")),
                        resultSet.getString("gender"),
                        resultSet.getInt("age")
                ),
                resultSet.getString("id"),
                resultSet.getBoolean("isRead"),
                resultSet.getString("last_update"),
                resultSet.getInt("type")));
            }
            response = new ResponseEntitySet<>(patientList);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }

                if(resultSet != null) {
                    resultSet.close();
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<String> patientWarningID(String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select id from patient_warnings where patientID = ?");
            statement.setString(1, patientID);
            resultSet = statement.executeQuery();
            if(resultSet != null && resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getString("id"));
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notFoundPatientWarningInformation);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<WarningContent>> warningContents(String warningID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<WarningContent>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select `title`, warning_content.date, warning_content.type, trigger_type, content from warning_content where warning_id = ? order by warning_content.date desc");
            statement.setString(1, warningID);
            resultSet = statement.executeQuery();
            List<WarningContent> contentList = new ArrayList<>();
            while(resultSet.next()){
                contentList.add(new WarningContent(resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getString("date"),
                        resultSet.getInt("type"),
                        resultSet.getInt("trigger_type")));
            }
            response = new ResponseEntitySet<>(contentList);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }
    @Override
    public ResponseEntity updateWarningPatientRead(String warningID, boolean isRead) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("UPDATE patient_warnings SET isRead = ? WHERE id = ?");
            statement.setBoolean(1, isRead);
            statement.setString(2, warningID);
            int updateResult = statement.executeUpdate();
            if(updateResult != 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public String addWarningPatient(String patientID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        CallableStatement statement = null;
        String result;
        try{
            statement = MysqlConnection.getInstance().prepareCall(" { CALL patientWarningCreate(?, ?) } ");
            statement.setString(1, patientID);
            statement.registerOutParameter(2, Types.VARCHAR);
            statement.execute();
            result = statement.getString(2);
            if(TextUtils.isEmpty(result)){
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }
    @Override
    public String isAvailableWarningPatient(String patientID) throws ConnectionException,SQLException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select id from patient_warnings where patientID = ?");
            statement.setString(1,patientID);
            resultSet = statement.executeQuery();
            if(resultSet != null && resultSet.next()){
                result = resultSet.getString("id");
            }else{
                result = null;
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }
    @Override
    public ResponseEntitySet<Integer> isWarningsPatientCount(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Integer> response;
        try{
            String query = "SELECT count(*) AS `count` FROM patient_warnings WHERE isRead = 0 AND patientID IN (SELECT patientID FROM peak WHERE doctorID = ? AND process = 2)";
            statement = MysqlConnection.getInstance().prepareStatement(query);
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getInt("count"));
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }
    @Override
    public ResponseEntity addWarningContent(String warningID, WarningUpdate warningUpdate) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("insert into warning_content (warning_id,title,content,warning_content.type,trigger_type,warning_content.date) value (?,?,?,?,?,now())");
            statement.setString(1, warningID);
            statement.setString(2,warningUpdate.getTitle());
            statement.setString(3,warningUpdate.getContent());
            statement.setInt(4,warningUpdate.getType());
            statement.setInt(5,warningUpdate.getTriggerType());
            int result = statement.executeUpdate();
            if(result != 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false,e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }


    @Override
    public MeasureAverageValues bloodPressureAverageValue(String patient) throws ConnectionException,SQLException{
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        MeasureAverageValues values = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select AVG(minorValue) as averageMinor,AVG(majorValue) as averageMajor, AVG(pulseValue) as averagePulse from blood_pressure where patientID = ? and reportDate >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)");
            statement.setString(1,patient);
            resultSet = statement.executeQuery();
            if(resultSet != null && resultSet.next()){
                values = new MeasureAverageValues(resultSet.getFloat("averageMinor"),
                                                   resultSet.getFloat("averageMajor"),
                                                 resultSet.getFloat("averagePulse"));
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return values;
    }

    @Override
    public List<BloodPressurePatientInformation> getAvailableBloodPressurePatients() throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<BloodPressurePatientInformation> patients;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select `name`, surname, userID AS patientID from patient_profile WHERE patient_profile.userID IN (select DISTINCT(patientID) from blood_pressure)");
            resultSet = statement.executeQuery();
            patients = new ArrayList<>();
            while (resultSet.next()){
                patients.add(
                        new BloodPressurePatientInformation(
                                resultSet.getString("patientID"),
                                resultSet.getString("name"),
                                resultSet.getString("surname")
                        )
                );
            }
        }finally {
            try{
               if(statement != null){
                   statement.close();
               }
               if(resultSet != null){
                   resultSet.close();
               }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return patients;
    }

    @Override
    public ResponseEntitySet<String> updateExamination(ExaminationUpdateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        CallableStatement statement = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareCall("{ CALL updateExamination(?, ?, ?, ?) }");
            statement.setString(1, request.getName());
            statement.setString(2, request.getDate());
            statement.setString(3, request.getUserID());
            statement.registerOutParameter(4, Types.VARCHAR);
            statement.execute();
            String id = statement.getString(4);
            if(!TextUtils.isEmpty(id)){
                return new ResponseEntitySet<>(id);
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }
    @Override
    public ResponseEntity removeExamination(String examinationID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL examinationRemove(?) }");
            statement.setString(1, examinationID);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
               if(statement != null){
                    statement.close();
               }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public boolean existsExamination(ExaminationUpdateRequest request) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        boolean result;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select if(exists(select * from examination where examination.name = ? and examination.patientID = ?), 1, 0) as result");
            statement.setString(1, request.getName());
            statement.setString(2, request.getUserID());
            resultSet = statement.executeQuery();
            if(resultSet != null && resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean existsExaminationID(String examinationID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL examinationExistsID (?) }");
            statement.setString(1, examinationID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ResponseEntity updateExaminationContent(ExaminationUpdateContentRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("insert into examination_content(examination_id, source) values (?,?)");
            statement.setString(1, request.getId());
            statement.setString(2, Common.gson.toJson(request.getReport()));
            int insertResult = statement.executeUpdate();
            if(insertResult != 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<Examination>> listExamination(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Examination>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL examinationList(?) }");
            statement.setString(1, userID);
            resultSet = statement.executeQuery();
            List<Examination> examinationList = new ArrayList<>();
            while(resultSet.next()){
                examinationList.add(
                        new Examination(
                                resultSet.getString("name"),
                                resultSet.getString("date"),
                                resultSet.getString("id")));
            }
            response = new ResponseEntitySet<>(examinationList);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<String>> listExaminationContents(String id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<String>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select source from examination_content where  examination_id = ?");
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            List<String> contents = new ArrayList<>();
            while(resultSet.next()){
                contents.add(
                        resultSet.getString("source")
                );
            }
            response = new ResponseEntitySet<>(contents);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<String> updateComplaint(ComplaintUpdateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        CallableStatement statement = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareCall(" CALL updateComplaint(?, ?, ?)");
            statement.setString(1,request.getUserID());
            statement.setString(2,request.getDate());
            statement.registerOutParameter(3, Types.VARCHAR);
            statement.execute();
            String id = statement.getString(3);
            if(!TextUtils.isEmpty(id)){
                response = new ResponseEntitySet<>(id);
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity updateComplaintContent(ComplaintUpdateContentRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("insert into complaint_content(complaint_id, content) values (?, ?)");
            statement.setString(1, request.getId());
            statement.setString(2, Common.gson.toJson(request.getReport()));
            int insertResult = statement.executeUpdate();
            if(insertResult != 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public boolean existsComplaintID(String complaintID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL complaintExistsID(?) }");
            statement.setString(1, complaintID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ResponseEntitySet<List<Complaint>> listComplaint(String userID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Complaint>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL complaintList(?) }");
            statement.setString(1,userID);
            resultSet = statement.executeQuery();
            List<Complaint> complaints = new ArrayList<>();
            while(resultSet.next()){
                complaints.add(
                        new Complaint(
                                resultSet.getString("date"),
                                resultSet.getString("id")
                        )
                );
            }
            response = new ResponseEntitySet<>(complaints);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity removeComplaint(String complaintID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL complaintRemove(?) } ");
            statement.setString(1, complaintID);
            statement.execute();
            response = new ResponseEntity();
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<String>> listComplaintContents(String id) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<String>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("select id, content from complaint_content where complaint_id = ?");
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            List<String> complaintContents = new ArrayList<>();
            while(resultSet.next()){
                complaintContents.add(resultSet.getString("content"));
            }
            response = new ResponseEntitySet<>(complaintContents);
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
               if(statement != null){
                   statement.close();
               }
               if(resultSet != null){
                   resultSet.close();
               }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }
}
