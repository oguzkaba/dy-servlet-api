package com.kodlabs.doktorumyanimda.servlet;

import com.iyzipay.model.CheckoutForm;
import com.iyzipay.request.RetrieveCheckoutFormRequest;
import com.kodlabs.doktorumyanimda.config.IyzicoConfig;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/iyzico-callback")
public class IyzicoCallbackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        resp.getWriter().write("GET istegi basarili! Gelen Token: " + token);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null || token.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/web/payment-status.jsp?status=failure");
            return;
        }

        RetrieveCheckoutFormRequest request = new RetrieveCheckoutFormRequest();
        request.setToken(token);

        CheckoutForm checkoutForm = CheckoutForm.retrieve(request, IyzicoConfig.getOptions());

        if (checkoutForm != null) {
            // 1. Temel Başarı Kontrolü (Teknik başarı + Para çekildi mi?)
            boolean isSuccessful = "success".equalsIgnoreCase(checkoutForm.getStatus())
                    && "SUCCESS".equalsIgnoreCase(checkoutForm.getPaymentStatus());

            // 2. Eğer temel ödeme başarılıysa ve bir 3D durumu (mdStatus) varsa, onu da
            // teyit et.
            // Eğer mdStatus null ise, bu zaten 3D'siz başarılı bir işlemdir.
            if (isSuccessful && checkoutForm.getMdStatus() != null) {
                if (checkoutForm.getMdStatus() != 1) {
                    // 3D akışı var ama doğrulama başarısız (mdStatus 1 değilse)
                    isSuccessful = false;
                }
            }

            String status = isSuccessful ? "SUCCESS" : "FAILURE";

            String basketId = checkoutForm.getBasketId();
            Long appointmentId = 0L;
            String patientId = "";
            if (basketId != null && basketId.contains(":")) {
                String[] parts = basketId.split(":");
                appointmentId = Long.parseLong(parts[0]);
                patientId = parts[1];
            }

            String paymentId = checkoutForm.getPaymentId();

            // Handle empty paymentItems gracefully (happens on failures)
            String transactionId = "";
            if (checkoutForm.getPaymentItems() != null && !checkoutForm.getPaymentItems().isEmpty()) {
                transactionId = checkoutForm.getPaymentItems().get(0).getPaymentTransactionId();
            }

            BigDecimal price = checkoutForm.getPrice();
            String rawResult = new Gson().toJson(checkoutForm);

            ResponseEntity response = Managers.paymentManager.processPaymentResult(
                    appointmentId,
                    patientId,
                    paymentId,
                    transactionId,
                    price,
                    status,
                    rawResult);

            if (response.isSuccess && "SUCCESS".equals(status)) {
                resp.sendRedirect(req.getContextPath() + "/web/payment-status.jsp?status=success");
            } else {
                String msg = response.message != null ? response.message : checkoutForm.getErrorMessage();
                if (msg == null)
                    msg = "Odeme islemi basarisiz oldu veya aktivasyon sirasinda bir hata olustu.";
                resp.sendRedirect(req.getContextPath() + "/web/payment-status.jsp?status=failure&message="
                        + java.net.URLEncoder.encode(msg, "UTF-8"));
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/web/payment-status.jsp?status=failure&message="
                    + java.net.URLEncoder.encode("Iyzico'dan gecersiz cevap alindi.", "UTF-8"));
        }
    }
}
