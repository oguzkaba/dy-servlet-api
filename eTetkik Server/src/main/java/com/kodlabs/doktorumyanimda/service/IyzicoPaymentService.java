package com.kodlabs.doktorumyanimda.service;

import com.iyzipay.Options;
import com.iyzipay.model.CheckoutFormInitialize;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.kodlabs.doktorumyanimda.model.payment.PaymentInitializeRequest;
import java.util.ArrayList;
import java.util.List;

public class IyzicoPaymentService {

    public CheckoutFormInitialize initializePayment(PaymentInitializeRequest req, Options options) {
        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setLocale("tr");
        request.setConversationId(req.getAppointmentID().toString());
        request.setPrice(new java.math.BigDecimal(req.getPrice()));
        request.setPaidPrice(new java.math.BigDecimal(req.getPrice()));
        request.setCurrency("TRY");
        request.setBasketId(req.getAppointmentID().toString());
        request.setPaymentGroup("LISTING");
        request.setCallbackUrl(req.getCallbackUrl());

        // Alıcı Bilgileri (Statik veya DB'den gelen hasta verisiyle doldurulmalı)
        com.iyzipay.model.Buyer buyer = new com.iyzipay.model.Buyer();
        buyer.setId(req.getPatientID());
        buyer.setName("Hasta Adı"); // DB'den çekilebilir
        buyer.setSurname("Soyadı");
        buyer.setEmail("email@test.com");
        buyer.setIdentityNumber("11111111111");
        buyer.setRegistrationAddress("Adres Bilgisi");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        request.setBuyer(buyer);

        // Sepet İçeriği (Randevu tek kalem ürün gibi eklenir)
        List<com.iyzipay.model.BasketItem> basketItems = new ArrayList<>();
        com.iyzipay.model.BasketItem item = new com.iyzipay.model.BasketItem();
        item.setId(req.getAppointmentID().toString());
        item.setName("Doktor Randevu Hizmeti");
        item.setCategory1("Sağlık");
        item.setItemType("VIRTUAL");
        item.setPrice(new java.math.BigDecimal(req.getPrice()));
        basketItems.add(item);
        request.setBasketItems(basketItems);

        return CheckoutFormInitialize.create(request, options);
    }
}