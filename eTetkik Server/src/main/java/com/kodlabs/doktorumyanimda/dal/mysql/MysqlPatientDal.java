package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.contact.Contact;
import com.kodlabs.doktorumyanimda.model.patient.Patient;
import com.kodlabs.doktorumyanimda.model.patient.PatientForAdmin;
import com.kodlabs.doktorumyanimda.model.patient.PatientStatusUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizInformationServiceCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.enabiz.service.EnabizServiceInformation;
import com.kodlabs.doktorumyanimda.model.patient.inspection.Inspection;
import com.kodlabs.doktorumyanimda.model.patient.inspection.InspectionContent;
import com.kodlabs.doktorumyanimda.model.patient.inspection.TimeSlot;
import com.kodlabs.doktorumyanimda.model.patient.notes.*;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNo;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoCreateRequest;
import com.kodlabs.doktorumyanimda.model.patient.systakipno.PatientSysTakipNoUpdateRequest;
import com.kodlabs.doktorumyanimda.model.patient.user.PatientLoginV2Request;
import com.kodlabs.doktorumyanimda.model.patient.user.PatientLoginVerifyRequest;
import com.kodlabs.doktorumyanimda.model.patient.user.PatientSingUpV2Request;
import com.kodlabs.doktorumyanimda.model.user.LoginData;
import com.kodlabs.doktorumyanimda.model.user.UserPatient;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPatientDal;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MysqlPatientDal implements IPatientDal {
    @Override
    public ResponseEntitySet<List<PatientForAdmin>> getAllPatients() throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        ResponseEntitySet<List<PatientForAdmin>> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patients() }");
            resultSet = statement.executeQuery();
            List<PatientForAdmin> patientList = new ArrayList<>();
            while(resultSet.next()){
                final PatientForAdmin patient = new PatientForAdmin(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("userID"),
                        Functions.encodeBase64(resultSet.getBytes("picture")),
                        resultSet.getString("gender"),
                        resultSet.getInt("age"),
                        resultSet.getString("createDate")
                );
                patientList.add(patient);
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
    public ResponseEntitySet<List<Patient>> getAllDoctorPatients(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntitySet<List<Patient>> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorPatients(?) }");
            statement.setString(1,doctorID);
            resultSet = statement.executeQuery();
            List<Patient> patientList = new ArrayList<>();
            while(resultSet.next()){
                final Patient patient = new Patient(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("patientID"),
                        resultSet.getString("createDate"),
                        Functions.encodeBase64(resultSet.getBytes("picture")),
                        resultSet.getString("gender"),
                        resultSet.getInt("age")
                );
                patientList.add(patient);
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
    public ResponseEntitySet<List<Patient>> doctorOldPatients(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntitySet<List<Patient>> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL doctorOldPatients(?) }");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            List<Patient> patientList = new ArrayList<>();
            while(resultSet.next()){
                final Patient patient = new Patient(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("patientID"),
                        null,
                        Functions.encodeBase64(resultSet.getBytes("picture")),
                        resultSet.getString("gender"),
                        resultSet.getInt("age")
                );
                patientList.add(patient);
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
    public ResponseEntitySet<LoginData> loginV2(PatientLoginV2Request request, String verifyCode) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<LoginData> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientLoginV2(?, ?, ?, ?) }");
            statement.setString(1, request.getUname());
            statement.setString(2, Functions.toSHA1(request.getPassword()));
            statement.setString(3, request.getLoginType());
            statement.setString(4, verifyCode);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new LoginData(
                                resultSet.getString("phone"),
                                verifyCode
                        )
                );
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            System.out.println(e.getErrorCode());
            response = new ResponseEntitySet<>(false, e.getErrorCode(), e.getLocalizedMessage());
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
    public ResponseEntitySet<UserPatient> singUpV2(PatientSingUpV2Request request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<UserPatient> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientSingUp(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            statement.setString(1, request.getTcNumber());
            statement.setString(2, request.getPhone());
            statement.setString(3, Functions.toSHA1(request.getPassword()));
            statement.setString(4, request.getName());
            statement.setString(5, request.getSurname());
            statement.setString(6, request.getEmail());
            statement.setString(7, request.getBirdDate());
            statement.setString(8, request.getGender());
            statement.setBoolean(9, request.isKvkk());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new UserPatient(
                                resultSet.getString("userID")
                        )
                );
            }else{
                throw new SQLException(ErrorMessages.operationFailed);
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
    public ResponseEntitySet<String> updateVerifyCode(String phone, String verifyCode) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientUpdateVerifyCode(?, ?) }");
            statement.setString(1, phone);
            statement.setString(2, verifyCode);
            statement.execute();
            response = new ResponseEntitySet<>(
                    verifyCode
            );
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
    public ResponseEntitySet<String> create(String phone, String verifyCode) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientRegister(?, ?) }");
            statement.setString(1, phone);
            statement.setString(2, verifyCode);
            statement.execute();
            response = new ResponseEntitySet<>(
                    verifyCode
            );
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
    public ResponseEntitySet<UserPatient> loginVerify(PatientLoginVerifyRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<UserPatient> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientLoginVerifyCode(?, ?) }");
            statement.setString(1, request.getPhone());
            statement.setString(2, request.getVerifyCode());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new UserPatient(
                                resultSet.getString("userID")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
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
    public ResponseEntitySet<Contact> contact(String phoneOrUserID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Contact> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL contact(? ,?) }");
            statement.setString(1, phoneOrUserID);
            statement.setInt(2, Role.PATIENT.value());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new Contact(
                                resultSet.getString("email"),
                                resultSet.getString("phone")
                        )
                );
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
    public ResponseEntitySet<Patient> information(String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        ResponseEntitySet<Patient> response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientInformation(?) }");
            statement.setString(1, patientID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new Patient(
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("phone"),
                                resultSet.getString("email"),
                                resultSet.getString("userID"),
                                Functions.encodeBase64(resultSet.getBytes("picture")),
                                resultSet.getString("gender"),
                                resultSet.getInt("age")
                        )
                );
            }else{
                response = new ResponseEntitySet<>(false, ErrorMessages.notAccessPatientInformation);
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
    public ResponseEntity patientStatusUpdate(PatientStatusUpdateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientStatusUpdate(?, ?, ?) }");
            statement.setString(1, request.getPatientID());
            statement.setString(2, request.getFieldName());
            statement.setBoolean(3, request.isValue());
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
    public ResponseEntitySet<String> patientGetStatus(String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientStatus(?) }");
            statement.setString(1,patientID);
            resultSet = statement.executeQuery();
            if(resultSet != null && resultSet.next()){
                return new ResponseEntitySet<>(resultSet.getString("status"));
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null)
                    statement.close();
                if(resultSet != null)
                    resultSet.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<List<Inspection>> inspectionList(String patientID, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Inspection>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT inspection.*, `date`, start_hour, end_hour, start_minute, end_minute FROM inspection LEFT JOIN et_time_slot ON inspection.appointmentID = et_time_slot.appointment_id WHERE  inspection.patientID = ? AND inspection.doctorID = ?");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();
            List<Inspection> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(new Inspection(
                    resultSet.getString("id"),
                    resultSet.getString("patientID"),
                    resultSet.getString("doctorID"),
                    resultSet.getInt("appointmentID"),
                    Functions.timeStampToLocalDateTime(resultSet.getTimestamp("createDateTime"), "GMT+3"),
                        new TimeSlot(resultSet.getString("date"),
                                resultSet.getInt("start_hour"),
                                resultSet.getInt("start_minute"),
                                resultSet.getInt("end_hour"),
                                resultSet.getInt("end_minute"))
                ));
            }
            response = new ResponseEntitySet<>(result);
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
    public ResponseEntity inspectionDelete(String inspectionID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("DELETE FROM inspection WHERE id = ?");
            statement.setString(1, inspectionID);
            int result = statement.executeUpdate();
            if(result > 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
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
    public boolean inspectionExists(String inspectionID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT IF(EXISTS(SELECT * FROM inspection WHERE id = ?), 1, 0) AS result");
            statement.setString(1, inspectionID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                result = false;
            }
        }catch (SQLException e){
            result = false;
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
    public ResponseEntity inspectionContentDelete(String inspectionContentID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("DELETE FROM inspection_content WHERE id = ?");
            statement.setString(1, inspectionContentID);
            int result = statement.executeUpdate();
            if(result > 0){
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
    public ResponseEntitySet<String> inspectionContentCreate(InspectionContent inspectionContent) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL inspectionContentCreate(?, ?, ?, ?, ?)}");
            statement.setString(1, inspectionContent.getInspectionID());
            statement.setString(2, inspectionContent.getTitle());
            statement.setString(3, inspectionContent.getSymptom());
            statement.setString(4, inspectionContent.getComplaint());
            statement.setString(5, inspectionContent.getAnamnesis());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                String generateId = resultSet.getString("generateId");
                if(!TextUtils.isEmpty(generateId)){
                    response = new ResponseEntitySet<>(generateId);
                }else{
                    response = new ResponseEntitySet<>(false, ErrorMessages.operationFailed);
                }
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
    public boolean inspectionContentExists(String contentID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT IF(EXISTS(SELECT * FROM inspection_content WHERE id = ?), 1, 0) AS result");
            statement.setString(1, contentID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                result = false;
            }
        }catch (SQLException e){
            result = false;
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
    public ResponseEntitySet<List<InspectionContent>> inspectionContentList(String inspectionID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<InspectionContent>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM inspection_content WHERE inspection_id = ?");
            statement.setString(1, inspectionID);
            resultSet = statement.executeQuery();
            List<InspectionContent> result = new ArrayList<>();
            while(resultSet.next()){
                result.add(new InspectionContent(
                        resultSet.getString("id"),
                        resultSet.getString("inspection_id"),
                        resultSet.getString("title"),
                        resultSet.getString("symptom"),
                        resultSet.getString("complaint"),
                        resultSet.getString("anamnesis"),
                        Functions.timeStampToLocalDateTime(resultSet.getTimestamp("updateDateTime"), "GMT+3"),
                        Functions.timeStampToLocalDateTime(resultSet.getTimestamp("createDateTime"), "GMT+3")
                ));
            }
            response = new ResponseEntitySet<>(result);
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
    public ResponseEntity inspectionContentUpdate(InspectionContent inspectionContent) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("UPDATE inspection_content SET title = ?, symptom = ?, complaint = ?, anamnesis = ? WHERE id = ?");
            statement.setString(1, inspectionContent.getTitle());
            statement.setString(2, inspectionContent.getSymptom());
            statement.setString(3, inspectionContent.getComplaint());
            statement.setString(4, inspectionContent.getAnamnesis());
            statement.setString(5, inspectionContent.getId());
            int result = statement.executeUpdate();
            if(result > 0){
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


    /* Patient Notes */

    @Override
    public ResponseEntitySet<String> notesCreate(String patientID, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL createPatientNotes(?, ?)}");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getString("result"));
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
    public boolean noteExist(String patientID, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
           statement = MysqlConnection.getInstance().prepareStatement("SELECT IF(EXISTS(SELECT * FROM patient_notes WHERE patientID = ? AND doctorID = ?), 1, 0) AS result");
           statement.setString(1, patientID);
           statement.setString(2, doctorID);
           resultSet = statement.executeQuery();
           if(resultSet.next()){
               result = resultSet.getBoolean("result");
           }else{
               result = false;
           }
        }catch (SQLException e){
            result = false;
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
                if(resultSet !=  null){
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ResponseEntitySet<String> notes(String patientID, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM patient_notes WHERE patientID = ? AND doctorID = ?");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getString("noteID"));
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
    public ResponseEntitySet<PatientNotes> note(String patientID, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<PatientNotes> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM patient_notes WHERE  patientID  = ? AND doctorID = ?");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new PatientNotes(
                                resultSet.getInt("id"),
                                resultSet.getString("patientID"),
                                resultSet.getString("doctorID"),
                                resultSet.getString("noteID"),
                                resultSet.getString("createDateTime")
                        )
                );
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
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<PatientNotesContent> noteContentCreate(PatientNotesContentCreateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<PatientNotesContent> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL createPatientNotesContent(?, ?, ?)}");
            statement.setString(1, request.getTitle());
            statement.setString(2, request.getContent());
            statement.setString(3, request.getNoteID());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(new PatientNotesContent(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getString("contentID"),
                        resultSet.getString("noteID"),
                        Functions.timeStampToLocalDateTime(resultSet.getTimestamp("updateDateTime"), "GMT+3"),
                        Functions.timeStampToLocalDateTime(resultSet.getTimestamp("createDateTime"), "GMT+3")
                ));
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
    public ResponseEntity noteContentUpdate(PatientNotesContentUpdateRequest request) throws ConnectionException{
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("UPDATE patient_notes_content SET title = ?, content = ? WHERE contentID = ?");
            statement.setString(1, request.getTitle());
            statement.setString(2, request.getContent());
            statement.setString(3, request.getContentID());
            int result = statement.executeUpdate();
            if(result > 0){
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
    public ResponseEntitySet<List<PatientNotesContent>> noteContents(String noteID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<PatientNotesContent>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT  * FROM patient_notes_content WHERE noteID = ?");
            statement.setString(1, noteID);
            resultSet = statement.executeQuery();
            List<PatientNotesContent> results = new ArrayList<>();
            while (resultSet.next()){
                results.add(new PatientNotesContent(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getString("contentID"),
                        resultSet.getString("noteID"),
                        resultSet.getString("updateDateTime"),
                        resultSet.getString("createDateTime")
                ));
            }
            response = new ResponseEntitySet<>(results);
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
    public ResponseEntity noteContentDelete(String contentID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("DELETE FROM patient_notes_content WHERE contentID = ?");
            statement.setString(1, contentID);
            int result = statement.executeUpdate();
            if(result > 0){
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

    /* Patient Notes End */

    /* Patient SysTakipNo */
    @Override
    public ResponseEntitySet<List<PatientSysTakipNo>> sysTakipNoList(String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<PatientSysTakipNo>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM systakipno WHERE doctorID = ? AND active = true");
            statement.setString(1, doctorID);
            resultSet = statement.executeQuery();
            List<PatientSysTakipNo> results = new ArrayList<>();
            while(resultSet.next()){
                results.add(new PatientSysTakipNo(
                        resultSet.getInt("id"),
                        resultSet.getString("tc_number"),
                        resultSet.getString("doctorID"),
                        resultSet.getString("sys_takip_no"),
                        resultSet.getBoolean("active"),
                        Functions.timeStampToLocalDateTime(resultSet.getTimestamp("updateDateTime"), "GMT+3"),
                        Functions.timeStampToLocalDateTime(resultSet.getTimestamp("createDateTime"), "GMT+3")
                ));
            }
            response = new ResponseEntitySet<>(results);
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
    public ResponseEntitySet<String> sysTakipNo(String tcNumber, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT sys_takip_no FROM systakipno WHERE tc_number = ? AND doctorID = ? AND active = 1");
            statement.setString(1, tcNumber);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(resultSet.getString("sys_takip_no"));
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
    public ResponseEntity sysTakipNoCreate(PatientSysTakipNoCreateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("INSERT INTO systakipno(tc_number, doctorID, sys_takip_no, active, createDateTime) VALUE(?, ?, ?, true, NOW())");
            statement.setString(1, request.getTcNumber());
            statement.setString(2, request.getDoctorID());
            statement.setString(3, request.getSysTakipNo());
            int result = statement.executeUpdate();
            if(result > 0){
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
    public ResponseEntity sysTakipNoUpdate(PatientSysTakipNoUpdateRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("UPDATE systakipno SET active = ? WHERE  tc_number = ? AND doctorID = ? AND sys_takip_no = ?");
            statement.setBoolean(1, request.isActive());
            statement.setString(2, request.getTcNumber());
            statement.setString(3, request.getDoctorID());
            statement.setString(4, request.getSysTakipNo());
            int result = statement.executeUpdate();
            if(result > 0){
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
    public boolean sysTakipNoExists(String tcNumber, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT IF(EXISTS(SELECT * FROM systakipno WHERE tc_number = ? AND  doctorID = ? AND active = 1), 1, 0) AS result");
            statement.setString(1, tcNumber);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                result = false;
            }
        }catch (SQLException e){
            result = false;
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
    public boolean sysTakipNoExists(String sysTakipNo) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT IF(EXISTS(SELECT  * FROM systakipno WHERE sys_takip_no = ? AND active = 1), 1, 0) AS result");
            statement.setString(1, sysTakipNo);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                result = false;
            }
        }catch (SQLException e){
            result = false;
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
    /* Patient SysTakipNo End*/

    /* Patient Enabiz Service Information */

    @Override
    public ResponseEntitySet<List<EnabizServiceInformation>> getEnabizServiceInformation(String sysTakipNo) throws ConnectionException {
       if(MysqlConnection.getInstance() == null){
           throw new ConnectionException();
       }
       PreparedStatement statement = null;
       ResultSet resultSet = null;
       ResponseEntitySet<List<EnabizServiceInformation>> response;
       try{
           statement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM enabiz_service_information WHERE sys_takip_no = ? AND active = 1");
           statement.setString(1, sysTakipNo);
           resultSet = statement.executeQuery();
           List<EnabizServiceInformation> informations = new ArrayList<>();
           while(resultSet.next()){
               informations.add(new EnabizServiceInformation(
                       resultSet.getInt("id"),
                       resultSet.getString("service_name"),
                       resultSet.getString("service_reference_no"),
                       resultSet.getString("sys_takip_no"),
                       resultSet.getString("update_date"),
                       resultSet.getString("create_date"),
                       resultSet.getBoolean("active")

               ));
           }
           response = new ResponseEntitySet<>(informations);
       }catch (SQLException e){
           response = new ResponseEntitySet<>(false, e.getErrorCode(), e.getLocalizedMessage());
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
    public ResponseEntity createEnabizServiceInformation(EnabizInformationServiceCreateRequest request) throws ConnectionException {
       if(MysqlConnection.getInstance() == null){
           throw new ConnectionException();
       }
       PreparedStatement statement = null;
       ResponseEntity response;
       try{
           statement = MysqlConnection.getInstance().prepareStatement("INSERT INTO enabiz_service_information(service_name, service_reference_no, sys_takip_no, create_date, active) VALUE (? , ? ,?, NOW(), 1)");
           statement.setString(1, request.getName());
           statement.setString(2, request.getServiceReferenceNo());
           statement.setString(3, request.getSysTakipNo());
           int result = statement.executeUpdate();
           if(result > 0){
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
       return  response;
    }

    @Override
    public boolean existsEnabizServiceReferenceNumber(String serviceReferenceNumber) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("SELECT IF(EXISTS(SELECT * FROM enabiz_service_information WHERE service_reference_no = ?  AND active = 1), 1, 0) AS result");
            statement.setString(1, serviceReferenceNumber);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean("result");
            }else{
                result = false;
            }
        }catch (SQLException e){
            result = false;
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
    public ResponseEntity deleteEnabizServiceInformation(String serviceReferenceNumber) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("UPDATE enabiz_service_information SET active = 0 WHERE service_reference_no = ?");
            statement.setString(1, serviceReferenceNumber);
            int result = statement.executeUpdate();
            if(result > 0){
                response = new ResponseEntity();
            }else{
                response = new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }catch (SQLException e){
            response = new ResponseEntity(false, e.getErrorCode(), e.getLocalizedMessage());
        }finally {
            try{
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                if(e != null){
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
    /* Patient Enabiz Service Information End*/
}
