package com.kodlabs.doktorumyanimda.controller;

import com.iyzipay.model.CheckoutFormInitialize;
import com.iyzipay.model.Locale;
import com.iyzipay.model.PaymentGroup;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.kodlabs.doktorumyanimda.config.IyzicoConfig;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IPaymentDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.payment.PaymentInitializeRequest;
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

    public PaymentManager(IPaymentDal paymentDal) {
        this.paymentDal = paymentDal;
    }

    public ResponseEntity initializeCheckoutForm(PaymentInitializeRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, "Geçersiz istek verisi.");
        }

        // 1. & 2. Validation and Data Retrieval
        ResponseEntity appointmentCheck;
        Map<String, Object> patientInfo = null;
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

            // 3. Fetch Patient Attributes for Iyzico form enrichment
            patientInfo = Managers.userManager.getAttributes(request.getPatientID(),
                    Role.PATIENT.value(),
                    Arrays.asList("name", "surname", "email", "phone", "tc_number"));
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }

        String name = "Patient";
        String surname = "Name";
        String email = "email@example.com";
        String tcNo = "11111111111"; // Default
        String phone = "+905555555555";
        String addressLine = "Address Not Provided";
        String city = "Not Provided";
        String district = "Not Provided";

        if (patientInfo != null && !patientInfo.containsKey("isSuccess")) {
            name = (String) patientInfo.getOrDefault("name", name);
            surname = (String) patientInfo.getOrDefault("surname", surname);
            email = (String) patientInfo.getOrDefault("email", email);
            tcNo = (String) patientInfo.getOrDefault("tc_number", tcNo);
            phone = (String) patientInfo.getOrDefault("phone", phone);
            addressLine = (String) patientInfo.getOrDefault("address", addressLine);
            city = (String) patientInfo.getOrDefault("city", city);
            district = (String) patientInfo.getOrDefault("district", district);
        }

        CreateCheckoutFormInitializeRequest iyzicoRequest = new CreateCheckoutFormInitializeRequest();
        iyzicoRequest.setLocale(Locale.TR.getValue());
        iyzicoRequest.setPrice(new BigDecimal(request.getPrice()));
        iyzicoRequest.setPaidPrice(new BigDecimal(request.getPrice()));
        iyzicoRequest.setCurrency(com.iyzipay.model.Currency.TRY.name());
        iyzicoRequest.setBasketId(request.getAppointmentID() + ":" + request.getPatientID());
        iyzicoRequest.setPaymentGroup(PaymentGroup.LISTING.name());
        iyzicoRequest.setCallbackUrl(request.getCallbackUrl());

        // Alıcı Bilgileri
        com.iyzipay.model.Buyer buyer = new com.iyzipay.model.Buyer();
        buyer.setId(request.getPatientID());
        buyer.setName(name);
        buyer.setSurname(surname);
        buyer.setEmail(email);
        buyer.setIdentityNumber(tcNo);
        buyer.setRegistrationAddress(addressLine);
        buyer.setCity(city);
        buyer.setGsmNumber(phone);
        buyer.setCountry("Turkey");
        iyzicoRequest.setBuyer(buyer);

        // Fatura Adresi (Zorunlu)
        com.iyzipay.model.Address billingAddress = new com.iyzipay.model.Address();
        billingAddress.setContactName(name + " " + surname);
        billingAddress.setCity(city);
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress(addressLine);
        iyzicoRequest.setBillingAddress(billingAddress);

        // Teslimat Adresi
        com.iyzipay.model.Address shippingAddress = new com.iyzipay.model.Address();
        shippingAddress.setContactName(name + " " + surname);
        shippingAddress.setCity(city);
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress(addressLine);
        iyzicoRequest.setShippingAddress(shippingAddress);

        // Sepet İçeriği (Zorunlu)
        java.util.List<BasketItem> basketItems = new java.util.ArrayList<>();
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId(request.getAppointmentID().toString());
        firstBasketItem.setName("Randevu Hizmeti");
        firstBasketItem.setCategory1("Sağlık Konsültasyonu");
        firstBasketItem.setItemType(BasketItemType.VIRTUAL.name());
        firstBasketItem.setPrice(new BigDecimal(request.getPrice()));
        basketItems.add(firstBasketItem);
        iyzicoRequest.setBasketItems(basketItems);

        CheckoutFormInitialize checkoutFormInitialize = CheckoutFormInitialize.create(iyzicoRequest,
                IyzicoConfig.getOptions());

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