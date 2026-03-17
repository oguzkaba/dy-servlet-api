package com.kodlabs.doktorumyanimda.model.log;

public enum LogEventDescription {
    LOGIN("Giriş yaptı."),
    LOGIN_FAILED("Giriş işlemi başarısız."),
    LOGIN_INVALID_TC_UNAME("Geçersiz T.C. Kimlik no"),
    LOGIN_INVALID_PHONE_UNAME("Geçersiz telefon numarası."),
    LOGIN_VERIFY_FAILED("Giriş doğrulama işlemi başarısız."),
    LOGOUT("Çıkış yaptı."),
    SING_UP(" Kayıt oluşturdu."),
    SING_UP_FOR_ADMIN("%s tarafından kayıt oluşturuldu."),
    AUTO_LOGIN("Otomatik giriş yaptı."),
    ACCEPT_CONTRACT("Sözleşmeleri kabul etti."),
    ACCOUNT_UPDATE("Hesabı güncelledi."),
    ACCOUNT_UPDATE_FOR_ADMIN("%s tarafından hesap güncellendi."),

    MESSAGE("%s isimli kişiye mesaj gönderdi."),
    ACCOUNT_DELETE("%s isimli kullanıcı hesabını sildi"),
    ACCOUNT_DELETE_FAILED("%s tarafından yapılan hesap silme işlemi başarısız."),
    ACCOUNT_DELETE_FOR_ADMIN("%s tarafından, %s isimli hastanın hesabı silindi."),
    ACCOUNT_DELETE_FAILED_FOR_ADMIN("%s tarafından yapılan, %s isimli hastanın hesap silme işlemi başarısız."),
    APPOINTMENT_CREATE("%s doktora randevu oluşturdu."),
    APPOINTMENT_DEMAND_ACCEPT("%s hastanın randevusunu kabul etti."),
    APPOINTMENT_DEMAND_REJECT("%s hastanın randevusunu reddetti."),

    INSPECTION_CONTENT_UPDATE("muayene içeriği güncellendi."),
    INSPECTION_CONTENT_DELETE("muayene içeriği oluşturuldu."),
    INSPECTION_CONTENT_CREATE("muayene içeriği oluşturuldu."),

    SYS_TAKIP_NO_CREATE("%s T.C numaralı hasta için sys takip no bilgisi oluşturuldu."),
    SYS_TAKIP_NO_UPDATE("%s T.C numaralı hasta için sys takip no bilgisi güncellendi.");

    private String message;

    LogEventDescription(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
