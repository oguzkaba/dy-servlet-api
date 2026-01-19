package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPaymentDal;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MysqlPaymentDal implements IPaymentDal {

    @Override
    public ResponseEntity addPaymentRecord(Long appointmentId, String patientId, String paymentId, String transactionId,
            BigDecimal price, String status, String rawResult) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntity response;
        CallableStatement statement = null;
        String sql = "{CALL addPaymentRecord(?, ?, ?, ?, ?, ?, ?)}";

        try {
            statement = MysqlConnection.getInstance().prepareCall(sql);
            statement.setLong(1, appointmentId);
            statement.setString(2, patientId);
            statement.setString(3, paymentId);
            statement.setString(4, transactionId);
            statement.setBigDecimal(5, price);
            statement.setString(6, status);
            statement.setString(7, rawResult);
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
    public ResponseEntity finalizeAppointmentPayment(Long appointmentId, String paymentId) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntity response;
        CallableStatement statement = null;
        String sql = "{CALL finalizeAppointmentPayment(?, ?)}";

        try {
            statement = MysqlConnection.getInstance().prepareCall(sql);
            statement.setLong(1, appointmentId);
            statement.setString(2, paymentId);
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
    public ResponseEntity checkAppointmentStatus(Long appointmentId, String patientId) throws ConnectionException {
        if (MysqlConnection.getInstance() == null) {
            throw new ConnectionException();
        }

        ResponseEntity response;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // In EnAppointmentStatus, PAYMENT_PENDING is the 5th item (index 4)
        String sql = "SELECT a.appointment_status, a.patient_id, a.approval_date, t.date, t.start_hour, t.start_minute "
                + "FROM et_appointment a " + "LEFT JOIN et_time_slot t ON a.time_slot_id = t.id " + "WHERE a.id = ?";

        try {
            statement = MysqlConnection.getInstance().prepareStatement(sql);
            statement.setLong(1, appointmentId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String dbPatientId = resultSet.getString("patient_id");
                String status = resultSet.getString("appointment_status");
                Timestamp approvalDate = resultSet.getTimestamp("approval_date");
                java.sql.Date slotDate = resultSet.getDate("date");
                int startHour = resultSet.getInt("start_hour");
                int startMinute = resultSet.getInt("start_minute");

                LocalDateTime now = LocalDateTime.now();

                if (!patientId.equals(dbPatientId)) {
                    response = new ResponseEntity(false, "Hata: Randevu belirtilen hastaya ait degil.");
                } else if (!"PAYMENT_PENDING".equals(status)) {
                    response = new ResponseEntity(false, "Hata: Randevu odeme bekleyen durumda degil.");
                } else {
                    // 1. Randevu başlangıç saati kontrolü
                    if (slotDate != null) {
                        LocalDateTime startDateTime = LocalDateTime.of(slotDate.toLocalDate(),
                                LocalTime.of(startHour, startMinute));
                        if (now.isAfter(startDateTime)) {
                            return new ResponseEntity(false,
                                    "Hata: Randevu saati gectigi icin odeme yapilamaz. Lütfen yeni randevu aliniz.");
                        }
                    }

                    // 2. 24 Saatlik onay süresi kontrolü
                    if (approvalDate != null) {
                        LocalDateTime approvalLDT = approvalDate.toLocalDateTime();
                        if (now.isAfter(approvalLDT.plusHours(24))) {
                            return new ResponseEntity(false,
                                    "Hata: Odeme suresi (24 saat) doldugu icin odeme yapilamaz.");
                        }
                    }

                    response = new ResponseEntity();
                }
            } else {
                response = new ResponseEntity(false, "Hata: Randevu bulunamadi.");
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
}
