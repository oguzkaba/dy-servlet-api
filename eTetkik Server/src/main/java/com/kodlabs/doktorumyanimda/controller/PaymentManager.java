package com.kodlabs.doktorumyanimda.controller;

import com.iyzipay.model.CheckoutFormInitialize;
import com.kodlabs.doktorumyanimda.config.IyzicoConfig;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPaymentDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.payment.PaymentInitializeRequest;
import com.kodlabs.doktorumyanimda.service.IyzicoPaymentService;
import com.kodlabs.doktorumyanimda.utils.Role;

import java.util.Arrays;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PaymentManager {

    private final IPaymentDal paymentDal;
    private final IyzicoPaymentService iyzicoPaymentService;

    public PaymentManager(IPaymentDal paymentDal) {
        this.paymentDal = paymentDal;
        this.iyzicoPaymentService = new IyzicoPaymentService();
    }

    public ResponseEntity initializeCheckoutForm(PaymentInitializeRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, "Geçersiz istek verisi.");
        }

        // 1. & 2. Validation and Data Retrieval
        ResponseEntity appointmentCheck;
        Map<String, Object> patientInfo = null;
        BigDecimal finalPrice = BigDecimal.ZERO;

        try {
            // Patient Existence Check
            if (!Managers.userManager.isExistsUser(request.getPatientID(), Role.PATIENT.value())) {
                return new ResponseEntity(false, ErrorMessages.INVALID_PATIENT_ID);
            }

            // Appointment Existence, Ownership and Status Check
            appointmentCheck = paymentDal.checkAppointmentStatus(request.getAppointmentID(),
                    request.getPatientID());
            if (appointmentCheck == null || !appointmentCheck.isSuccess) {
                return appointmentCheck;
            }

            // Security Check: Verify that the doctorID from request matches the one in DB
            // for this appointment
            String dbDoctorId = (String) appointmentCheck.data;
            if (dbDoctorId == null || !dbDoctorId.equals(request.getDoctorID())) {
                return new ResponseEntity(false, "Hata: Belirtilen doktor randevu ile eslesmiyor.");
            }

            // 3. Dynamic Price Calculation
            finalPrice = paymentDal.calculateFinalPrice(request.getDoctorID(), request.getPatientID());

            // 4. Fetch Patient Attributes for Iyzico form enrichment
            patientInfo = Managers.userManager.getAttributes(request.getPatientID(),
                    Role.PATIENT.value(),
                    Arrays.asList("name", "surname", "email", "phone", "tc_number"));
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }

        // --- FREE APPOINTMENT FLOW ---
        // If price is 0.0, bypass Iyzico and activate directly
        if (finalPrice.compareTo(BigDecimal.ZERO) == 0) {
            String uniqueFreeId = "FREE_PROMOTION_" + request.getAppointmentID();
            ResponseEntity freeResponse = processPaymentResult(
                    request.getAppointmentID(),
                    request.getPatientID(),
                    uniqueFreeId,
                    uniqueFreeId,
                    BigDecimal.ZERO,
                    "SUCCESS",
                    "Free promotion bypass - dynamic price was 0.0");

            if (freeResponse.isSuccess) {
                // Return a special message suggesting the payment is already done
                return new ResponseEntity(true, "FREE_SUCCESS");
            } else {
                return freeResponse;
            }
        }

        // Delegate Iyzico Form initialization to service
        CheckoutFormInitialize checkoutFormInitialize = iyzicoPaymentService.initializeForm(request, patientInfo,
                finalPrice);

        if (checkoutFormInitialize.getStatus().equals("success")) {
            return new ResponseEntity(true, checkoutFormInitialize.getCheckoutFormContent());
        } else {
            return new ResponseEntity(false, checkoutFormInitialize.getErrorMessage());
        }
    }

    public ResponseEntity processPaymentResult(Long appointmentId, String patientId, String paymentId,
            String transactionId, BigDecimal price, String status, String rawResult) {
        try {
            // STEP 1: Add Payment Record (Audit) - Always record the Iyzico result
            ResponseEntity dbResponse = paymentDal.addPaymentRecord(appointmentId, patientId, paymentId, transactionId,
                    price, status, rawResult);

            if (dbResponse.isSuccess && "SUCCESS".equals(status)) {
                // STEP 2: Trigger Spring Activation and get real error messages (e.g.,
                // AlertException)
                ResponseEntity springResponse = triggerSpringConfirmation(appointmentId, patientId, transactionId);

                if (springResponse.isSuccess) {
                    // STEP 3: Finalize Appointment Status in Servlet DB ONLY if Spring activation
                    // succeeded
                    return paymentDal.finalizeAppointmentPayment(appointmentId, paymentId);
                } else {
                    // Spring activation failed (capture AlertException content)
                    return springResponse;
                }
            }
            return dbResponse;
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    private ResponseEntity triggerSpringConfirmation(Long appointmentId, String patientId, String transactionId) {
        try {
            URL url = new URL(IyzicoConfig.SPRING_BOOT_BASE_URL + "/internal/notifications/send-payment-confirmation");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Internal-Token", IyzicoConfig.INTERNAL_TOKEN);
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"appointmentId\": %d, \"patientId\": \"%s\", \"paymentTransactionId\": \"%s\"}",
                    appointmentId, patientId, transactionId);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Spring Trigger Response Code: " + responseCode);

            if (responseCode >= 400) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    String errorMsg = response.toString();
                    System.err.println("Spring Trigger Error Response: " + errorMsg);
                    // Return the actual error message from Spring (could be AlertException JSON)
                    return new ResponseEntity(false, "Aktivasyon Hatası: " + errorMsg);
                }
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Spring Trigger Success Response: " + response.toString());
                    return new ResponseEntity();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(false, "Bağlantı Hatası: " + e.getLocalizedMessage());
        }
    }
}