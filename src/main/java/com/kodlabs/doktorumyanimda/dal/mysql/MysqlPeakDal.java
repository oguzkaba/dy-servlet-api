package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPeakDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.peak.*;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlPeakDal implements IPeakDal {
    @Override
    public ResponseEntitySet<String> create(CreatePeakRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        CallableStatement statement = null;
        ResponseEntitySet<String> response;
        try{
            // process 1 - wait
            statement = MysqlConnection.getInstance().prepareCall("{ CALL peakCreateV2(?, ?, ?, ?, ?, ?)}");
            statement.setString(1, request.getPatientID());
            statement.setString(2, request.getDoctorID());
            statement.setInt(3, request.getFee());
            statement.setInt(4, request.getPeakDay());
            statement.setString(5, request.getNote());
            statement.registerOutParameter(6, Types.VARCHAR);
            statement.execute();
            String id = statement.getString(6);
            if(!TextUtils.isEmpty(id)){
                response = new ResponseEntitySet<>(id);
            }else{
                response = new ResponseEntitySet<>(ErrorMessages.operationFailed);
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
    public ResponseEntitySet<String> waitUpdate(CreatePeakRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        CallableStatement statement = null;
        ResponseEntitySet<String> response;
        try{
            // process 1 - wait
            statement = MysqlConnection.getInstance().prepareCall("{ CALL peakWaitUpdateV2(?, ?, ?, ?, ?, ?)}");
            statement.setString(1, request.getPatientID());
            statement.setString(2, request.getDoctorID());
            statement.setInt(3, request.getFee());
            statement.setInt(4, request.getPeakDay());
            statement.setString(5, request.getNote());
            statement.registerOutParameter(6, Types.VARCHAR);
            statement.execute();
            String id = statement.getString(6);
            if(!TextUtils.isEmpty(id)){
                response = new ResponseEntitySet<>(id);
            }else{
                response = new ResponseEntitySet<>(ErrorMessages.operationFailed);
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
    public boolean isExistsActiveOrWait(String patientID, String doctorID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            // process : 1 - wait, 2 - active
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakIsExistsActiveOrWait(?, ?) }");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
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
    public boolean isExists(String patientID, String doctorID) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            // process : 1 - wait, 2 - active
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakIsExists(?, ?) }");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
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
    public boolean isExistsPeakUser(String peakID, String userID, byte role) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL isExistsPeakUser(?, ?, ?)}");
            statement.setString(1, peakID);
            statement.setString(2, userID);
            statement.setInt(3, role);
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
    public ResponseEntitySet<Boolean> isFirstVisitFree(String patientID, String doctorID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Boolean> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL peakFirstVisitIsFree(?, ?) } ");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getBoolean("result")
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
    public ResponseEntitySet<List<Peak>> list(String userID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Peak>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL peakList(?, ?) } ");
            statement.setString(1, userID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            List<Peak> peaks = new ArrayList<>();
            while(resultSet.next()){
                peaks.add(
                        new Peak(
                            resultSet.getString("id"),
                            resultSet.getString("fullName"),
                            resultSet.getString("otherUserID"),
                            resultSet.getString("createDate"),
                            resultSet.getInt("process"),
                            resultSet.getInt("fee"),
                            resultSet.getInt("peakDay"),
                            resultSet.getString("note"),
                            Functions.encodeBase64(resultSet.getBytes("picture"))
                        )
                );
            }
            response = new ResponseEntitySet<>(peaks);
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
    public ResponseEntitySet<Peak> detail(String peakID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Peak> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakDetail(?, ?) }");
            statement.setString(1, peakID);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        new Peak(
                                resultSet.getString("id"),
                                resultSet.getString("fullName"),
                                resultSet.getString("otherUserID"),
                                resultSet.getString("createDate"),
                                resultSet.getInt("process"),
                                resultSet.getInt("fee"),
                                resultSet.getInt("peakDay"),
                                resultSet.getString("note"),
                                Functions.encodeBase64(resultSet.getBytes("picture"))
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
    public ResponseEntitySet<PeakPayInformation> pay(PeakPayRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<PeakPayInformation> response;

        try{
            // process 2 - active
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakPay(?, ?, ?, ?) }");
            statement.setString(1, request.getPeakID());
            statement.setInt(2, request.getFee());
            statement.setInt(3, request.getPayment().getType());
            statement.setString(4, request.getPayment().getData());
            resultSet = statement.executeQuery();
            if(resultSet.next() && resultSet.getBoolean("status")){
                response = new ResponseEntitySet<>(
                        new PeakPayInformation(
                                resultSet.getString("doctorID"),
                                resultSet.getString("fullName")
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
    public boolean feeControl(String patientID, String doctorID, int peakDay, int fee) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL feeControl(?, ?, ?, ?) }");
            statement.setString(1, patientID);
            statement.setString(2, doctorID);
            statement.setInt(3, peakDay);
            statement.setInt(4, fee);
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
    public boolean payControl(String peakID, int fee) throws ConnectionException, SQLException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL payControl(?, ?) }");
            statement.setString(1, peakID);
            statement.setInt(2, fee);
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
    public ResponseEntitySet<DemandVerify> peakDemandVerify(PeakDemandVerifyRequest request) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<DemandVerify>  response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakDemandVerify(?, ?, ?) }");
            statement.setString(1, request.getPeakID());
            statement.setBoolean(2, request.isVerify());
            statement.setString(3, request.getNote());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                DemandVerify verify;
                int status = resultSet.getInt("status");
                if(status != -1){
                    verify = new DemandVerify(
                            status,
                            resultSet.getInt("fee"),
                            resultSet.getInt("peakDay")
                    );
                }else{
                    verify = new DemandVerify(status);
                }
                response = new ResponseEntitySet<> (verify);
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
    public ResponseEntitySet<List<String>> patientDoctorIDs(String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<String>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL patientDoctorIDs(?) }");
            statement.setString(1, patientID);
            resultSet = statement.executeQuery();
            List<String> ids = new ArrayList<>();
            while (resultSet.next()){
                ids.add(
                        resultSet.getString("doctorID")
                );
            }
            response = new ResponseEntitySet<>(ids);
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
    public ResponseEntitySet<List<Integer>> feeList() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Integer>> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL feeList() }");
            resultSet = statement.executeQuery();
            List<Integer> list = new ArrayList<>();
            while(resultSet.next()){
                list.add(resultSet.getInt("fee"));
            }
            response = new ResponseEntitySet<>(list);
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
    public ResponseEntitySet<List<Integer>> peakDayList() throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<Integer>> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakDayList() }");
            resultSet = statement.executeQuery();
            List<Integer> list = new ArrayList<>();
            while(resultSet.next()){
                list.add(resultSet.getInt("day"));
            }
            response = new ResponseEntitySet<>(list);
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
    public ResponseEntitySet<List<GoogleProduct>> googleProductList() throws ConnectionException{
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<GoogleProduct>> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL googleProductList() }");
            resultSet = statement.executeQuery();
            List<GoogleProduct> list = new ArrayList<>();
            while (resultSet.next()){
                list.add(
                        new GoogleProduct(
                                resultSet.getString("product_name"),
                                resultSet.getInt("fee")
                        )
                );
            }
            response = new ResponseEntitySet<>(list);
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
    public ResponseEntitySet<String> firstVisitPeakActiveStatus(String patientID) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<String> response;
        try{
            statement = MysqlConnection.getInstance().prepareStatement(" { CALL patientFirstVisitPeakActive(?) } ");
            statement.setString(1, patientID);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                response = new ResponseEntitySet<>(
                        resultSet.getString("id")
                );
            }else{
                response = new ResponseEntitySet<>(
                        null
                );
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
    public String roleID(String peakID, byte role) throws ConnectionException {
        if(MysqlConnection.getInstance() == null){
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String result;
        try{
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL peakRoleID(?, ?) } ");
            statement.setString(1, peakID);
            statement.setByte(2, role);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getString("id");
            }else{
                result = null;
            }
        }catch (SQLException e){
            result = null;
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
}
