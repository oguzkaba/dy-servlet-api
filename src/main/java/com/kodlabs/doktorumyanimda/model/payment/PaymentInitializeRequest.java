package com.kodlabs.doktorumyanimda.model.payment;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PaymentInitializeRequest {
    private String patientID; // Alıcı ID
    private String doctorID; // Fiyat hesaplamasında kullanılacak
    private Long appointmentID; // BasketId olarak kullanılacak
    private String callbackUrl; // Ödeme bitince Iyzico'nun yöneleceği adres

    public boolean isValid() {
        return !TextUtils.isEmpty(patientID)
                && !TextUtils.isEmpty(doctorID)
                && appointmentID != null
                && !TextUtils.isEmpty(callbackUrl);
    }
}