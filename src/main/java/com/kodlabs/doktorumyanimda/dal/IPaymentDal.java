package com.kodlabs.doktorumyanimda.dal;

import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import java.math.BigDecimal;

public interface IPaymentDal {
    ResponseEntity addPaymentRecord(Long appointmentId, String patientId, String paymentId, String transactionId,
            BigDecimal price, String status, String rawResult) throws ConnectionException;

    ResponseEntity finalizeAppointmentPayment(Long appointmentId, String paymentId) throws ConnectionException;

    ResponseEntity checkAppointmentStatus(Long appointmentId, String patientId) throws ConnectionException;

    ResponseEntity getAppointmentDetails(Long appointmentId) throws ConnectionException;

    BigDecimal calculateFinalPrice(String doctorId, String patientId) throws ConnectionException;
}
