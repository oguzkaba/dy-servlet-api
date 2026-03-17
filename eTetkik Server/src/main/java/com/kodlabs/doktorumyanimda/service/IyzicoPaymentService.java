package com.kodlabs.doktorumyanimda.service;

import com.iyzipay.model.CheckoutFormInitialize;
import com.iyzipay.model.Locale;
import com.iyzipay.model.PaymentGroup;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.kodlabs.doktorumyanimda.config.IyzicoConfig;
import com.kodlabs.doktorumyanimda.model.payment.PaymentInitializeRequest;

import java.math.BigDecimal;
import java.util.Map;

public class IyzicoPaymentService {

    public CheckoutFormInitialize initializeForm(PaymentInitializeRequest request, Map<String, Object> patientInfo,
            BigDecimal finalPrice) {

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
        iyzicoRequest.setPrice(finalPrice);
        iyzicoRequest.setPaidPrice(finalPrice);
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
        firstBasketItem.setPrice(finalPrice);
        basketItems.add(firstBasketItem);
        iyzicoRequest.setBasketItems(basketItems);

        return CheckoutFormInitialize.create(iyzicoRequest, IyzicoConfig.getOptions());
    }
}