package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.*;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IUserDal;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlUserDal implements IUserDal {
    @Override
    public boolean isExistsUser(String no, byte role) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean isAvailable;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL isExists(?, ?)  }");
            statement.setString(1, no);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isAvailable = resultSet.getBoolean("result");
            } else {
                isAvailable = false;
            }
        } catch (SQLException e) {
            isAvailable = false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isAvailable;
    }

    @Override
    public boolean deviceVerifyCountControl(String deviceID) throws ConnectionException, SQLException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean result;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL deviceVerifyCountControl(?) }");
            statement.setString(1, deviceID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getBoolean("result");
            } else {
                throw new SQLException(ErrorMessages.operationFailed);
            }
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ResponseEntity deviceVerifyCountIncrease(String deviceID) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResponseEntity response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL deviceVerifyCountIncrease(?) }");
            statement.setString(1, deviceID);
            statement.execute();
            response = new ResponseEntity();
        } catch (SQLException e) {
            response = new ResponseEntity(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public String getFullName(String userNo, byte role) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String result;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL fullName(?, ?) }");
            statement.setString(1, userNo);
            statement.setInt(2, role);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("fullName");
            } else {
                result = null;
            }
        } catch (SQLException e) {
            result = null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Object getAttribute(String userNo, byte role, String attribute) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Object result;
        try {
            String query;
            if (role == 0) {
                query = "select " + attribute + " from patient where phone = ? OR tc_number OR userID = ?";
            } else {
                query = "select " + attribute + " from doctor where phone = ? OR tc_number = ? OR userID = ?";
            }
            statement = MysqlConnection.getInstance().prepareStatement(query);
            statement.setString(1, userNo);
            statement.setString(2, userNo);
            statement.setString(3, userNo);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString(attribute);
            } else {
                result = null;
            }
        } catch (SQLException e) {
            result = null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    @Override
    public Map<String, Object> getAttributes(String userNo, byte role, List<String> attributes)
            throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        Map<String, Object> result = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("select ");
            boolean firstItem = true;
            for (String attribute : attributes) {
                if (firstItem)
                    firstItem = false;
                else
                    query.append(",");
                query.append(attribute);
            }
            query.append(" from ")
                    .append(role == Role.PATIENT.value()
                            ? "patient LEFT patient_profile ON patient.userID = patient_profile.userID"
                            : "doctor LEFT JOIN doctor_profile ON doctor.userID = doctor_profile.doctorID")
                    .append(role == Role.PATIENT.value() ? " WHERE patient.uname = ? or patient.userID = ?"
                            : " WHERE doctor.uname = ? or doctor.userID = ?");
            statement = MysqlConnection.getInstance().prepareStatement(query.toString());
            statement.setString(1, userNo);
            statement.setString(2, userNo);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = new HashMap<>();
                for (String attribute : attributes) {
                    result.put(attribute, resultSet.getString(attribute));
                }
            }
        } catch (SQLException e) {
            result = new HashMap<>();
            result.put("isSuccess", false);
            result.put("message", e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean setAttribute(String userNo, byte role, String attribute, Object value)
            throws ConnectionException, SQLException {
        if (MysqlConnection.getInstance() == null)
            throw new ConnectionException();
        PreparedStatement statement = null;
        boolean isSuccess;
        try {
            String query;
            if (role == 0)
                query = "update patient set " + attribute + " = ? where phone = ? or userID = ?";
            else
                query = "update doctor set " + attribute + " = ? where phone = ? or userID = ?";

            statement = MysqlConnection.getInstance().prepareStatement(query);
            statement.setObject(1, value);
            statement.setString(2, userNo);
            statement.setString(3, userNo);
            int result = statement.executeUpdate();
            isSuccess = result != 0;
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    @Override
    public ResponseEntitySet<List<UserInformation>> allUser(String type) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<List<UserInformation>> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL allUser(?) }");
            statement.setString(1, type);
            List<UserInformation> informationList = new ArrayList<>();
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                informationList.add(new UserInformation(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("userID"),
                        resultSet.getBoolean("active"),
                        resultSet.getString("createDate"),
                        resultSet.getString("lastLogin"),
                        resultSet.getString("type")));
            }
            response = new ResponseEntitySet<>(informationList);
        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Boolean> phoneExists(String phone, byte role) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Boolean> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL phoneExists(?, ?) }");
            statement.setString(1, phone);
            statement.setByte(2, role);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response = new ResponseEntitySet<>(resultSet.getBoolean("result"));
            } else {
                throw new ConnectionException(ErrorMessages.operationFailed);
            }
        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity changePassword(String userID, byte role, ChangePassword data) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL changePassword(?, ?, ?, ?) }");
            statement.setString(1, userID);
            statement.setInt(2, role);
            statement.setString(3, Functions.toSHA256(data.getCurrentPassword()));
            statement.setString(4, Functions.toSHA256(data.getNewPassword()));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getBoolean("result")) {
                    response = new ResponseEntity();
                } else {
                    response = new ResponseEntity(false, ErrorMessages.operationFailed);
                }
            } else {
                throw new SQLException(ErrorMessages.operationFailed);
            }
        } catch (SQLException e) {
            response = new ResponseEntity(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<AvailableContact> availableContact(String uname, byte role) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<AvailableContact> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL availableContact(?, ?) }");
            statement.setString(1, uname);
            statement.setByte(2, role);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response = new ResponseEntitySet<>(
                        new AvailableContact(
                                resultSet.getBoolean("availableMail"),
                                resultSet.getBoolean("availableSms")));
            } else {
                throw new SQLException(ErrorMessages.notAccessUserInformation);
            }
        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<UserContactInformation> userContactInformation(String uname, byte role)
            throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<UserContactInformation> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL userContactInformation(?, ?) }");
            statement.setString(1, uname);
            statement.setByte(2, role);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response = new ResponseEntitySet<>(
                        new UserContactInformation(
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("fullName")));
            } else {
                throw new SQLException(ErrorMessages.notAccessUserInformation);
            }
        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<UserInformation> userInformation(String uname, byte role) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<UserInformation> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL userInformation(?, ?) }");
            statement.setString(1, uname);
            statement.setByte(2, role);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response = new ResponseEntitySet<>(
                        new UserInformation(
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("userID"),
                                resultSet.getBoolean("active"),
                                resultSet.getString("createDate"),
                                resultSet.getString("lastLogin"),
                                resultSet.getString("type")));
            } else {
                throw new SQLException(ErrorMessages.notAccessUserInformation);
            }
        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntity resetPassword(String userID, byte role, ResetPassword data) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntity response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL resetPassword(?, ?, ?) }");
            statement.setString(1, userID);
            statement.setByte(2, role);
            statement.setString(3, Functions.toSHA256(data.getPassword()));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getBoolean("result")) {
                    response = new ResponseEntity();
                } else {
                    response = new ResponseEntity(false, ErrorMessages.operationFailed);
                }
            } else {
                throw new SQLException(ErrorMessages.operationFailed);
            }
        } catch (SQLException e) {
            response = new ResponseEntity(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public ResponseEntitySet<Boolean> userPasswordVerify(String userID, byte role, String password)
            throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResponseEntitySet<Boolean> response;
        try {
            statement = MysqlConnection.getInstance().prepareStatement("{ CALL userPasswordVerify(?, ?, ?) }");
            statement.setString(1, userID);
            statement.setByte(2, role);
            statement.setString(3, Functions.toSHA256(password));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response = new ResponseEntitySet<>(
                        resultSet.getBoolean("result"));
            } else {
                throw new SQLException(ErrorMessages.operationFailed);
            }
        } catch (SQLException e) {
            response = new ResponseEntitySet<>(false, e.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
