package com.kodlabs.doktorumyanimda.messages;

public enum ErrorMessage {
    //System
    INVALID_DATA("Hata: Geçersiz veri lütfen tekrar deneyin.", 1),
    OPERATION_FAILED("Hata: İşlem başarısız lütfen tekrar deneyin.", 2),

    //Doctor
    NOT_FOUND_DOCTOR_INFORMATION("Hata: Doktor bilgisi bulunamadı.", 300),
    //Doctor Recipe
    RECIPE_INFORMATION_NOT_FOUND("Hata: Doctor reçete bilgisi bulunamadı.", 700);
    private String message;
    private int code;

    ErrorMessage(String message, int code){
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
