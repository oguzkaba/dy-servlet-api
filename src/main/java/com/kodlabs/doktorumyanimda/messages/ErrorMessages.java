package com.kodlabs.doktorumyanimda.messages;

public final class ErrorMessages {
    public static final String connectFailed = "Hata: Bağlantı sırasında bir sorun oluştu. Lütfen tekrar deneyiniz.";
    public static final String operationFailed = "Hata: İşlem başarısız... Lütfen tekrar deneyin.";
    public static final String inValidData = "Hata: Geçersiz veri lütfen tekrar deneyin.";
    public static final String inValidDateTimeFormat = "Hata: Geçersiz zaman-tarih formatı ( %s )";
    public static final String inValidDateFormat = "Hata: Geçersiz tarih formatı ( %s )";
    public static final String notAccessUserInformation = "Hata: Kullanıcı bilgisine ulaşılamadı. Lütfen tekrar deneyiniz.";
    public static final String notPermission = "Hata: İşlem için yeterli yetkilendirmeye sahip değilsiniz";
    public static final String notAccessUserData = "Hata: Kullanıcı verisine ulaşılamadı.";
    public static final String notAccessDoctorInformation = "Hata: Doctor bilgisine ulaşılamadı.";
    public static final String notAccessHFAdminInformation = "Hata: Sağlık tesis admin bilgisine ulaşılamadı.";
    public static final String notAccessProfileInformation = "Hata: Profil bilgisine ulaşılamadı.";
    public static final String inValidDoctorCode = "Hata: Geçersiz danışman kodu.";
    public static final String inValidContactType = "Hata: Geçersiz iletişim tipi.";
    public static final String availableUserName = "Hata: Kullanıcı adı zaten kayıtlı.";
    public static final String availableTcNumber = "Hata: T.C kimlik numarası zaten kayıtlı.";
    public static final String availablePhoneNumber = "Hata: Telefon numarası zaten kayıtlı.";
    public static final String availableDoctorCode = "Hata: Danışman kodu zaten mevcut";
    public static final String inValidCode = "Hata: Geçersiz ID";
    public static final String inValidVerifyCode = "Hata: Doğrulama kodu hatalı";
    public static final String inValidEmail = "Hata: Lütfen geçerli bir e-posta adresi giriniz.";
    public static final String inValidPhone = "Hata: Lütfen geçerli bir telefon numarası giriniz.";
    public static final String inValidUserName = "Hata: Lütfen geçerli bir kullanıcı adı giriniz.";
    public static final String inValidTcNumber = "Hata: Lütfen geçerli T.C. kimlik numarası giriniz.";
    public static final String inValidLoginType = "Hata: Geçersiz giriş tipi";
    public static final String uNameOrPasswordInCorrect = "Hata: Kullanıcı veya şifre yanlış.";
    public static String alreadyAccount = "Hata: Hesap zaten mevcut";

    public static final String changePasswordFailed = "Hata: Şifre değiştirme işlemi başarısız lütfen tekrar deneyin.";
    public static final String unAvailablePatientStatus = "Hata: Hasta durumu mevcut değil.";

    public static final String inValidRole = "Hata: Geçersiz rol.";
    public static final String patientLoginError = "Hata: Kullanıcı adı veya şifre yanlış. Lütfen tekrar deneyin.";
    public static final String doctorLoginError = "Hata: T.C kimlik numarası veya şifre yanlış. Lütfen tekrar deneyin.";
    public static final String adminLoginError = "Hata: T.C kimlik numarası veya şifre yanlış. Lütfen tekrar deneyin.";


    public static final String INVALID_PATIENT_ID = "Hata: Geçersiz hasta id.";
    public static final String INVALID_DOCTOR_ID = "Hata: Geçersiz doktor id.";

    /* Notification */
    public static final String alreadyNotificationID = "Bildirim id'si zaten bulunuyor";

    /* User Drug */
    public static final String notAccessUserDrugInformation = "Hata: İlaç bilgisine ulaşılamadı.";


    /* Examination */
    public static final String notAccessExaminationInformation = "Hata: Tetkik bilgisine ulaşılamadı.";
    public static final String alreadyExamination = "Hata: Tetkik zaten mevcut.";

    /* Complaint */
    public static final String notAccessComplaintInformation = "Hata: Şikayet bilgisine ulaşılamadı.";

    /* Peak */
    public static final String inValidDoctorFee = "Hata: Geçersiz danışman ücreti";
    public static final String alreadyPeak = "Hata: Muayene zaten mevcut.";
    public static final String inValidPeakPay = "Hata: Geçersiz muayene ödeme bilgisi.";
    public static final String notAccessPeakInformation = "Hata: Muayene bilgisine ulaşılamadı.";

    /* Warning */
    public static final String notFoundPatientWarningInformation = "Hata: Hastanın mevcut uyarı bilgisi bulunamadı.";

    /* Sms */
    public static final String smsSendFailed = "SMS gönderimi gerçekleştirilemedi.";

    public static final String verifyCodeFailed = "Hata: Doğrulama kodu başarısız lütfen tekrar deneyiniz.";
    public static final String deviceVerifyCountLimitOut = "Hata: Cihaz günlük doğrulama sınırına ulaşılmıştır. Günlük sınır doğrulama sınırı 5";

    public static final String notAccessPatientInformation = "Hata: Hasta bilgisine ulaşılamadı.";
    public static final String notVerifyUserIdentity = "Hata: Kimlik bilgisi doğrulanamadı.";

    /* Payment */
    public static final String inValidPaymentType = "Hata: Geçersiz ödeme tipi";
    public static final String paymentNotVerify = "Hata: Ödeme doğrulanmadı.";

    public static final String notValidLoginType = "Hata: Geçersiz giriş tipi";

    // Health Facility
    public static final String notFoundHealthFacility = "Hata: Sağlık tesisi bulunamadı.";
    public static final String notDeleteHealthFacility = "Hata: Sağlık tesisi silme işlemi sırasında bir sorun oluştu. Lütfen tekrar deneyin.";
    public static final String alreadyHealthFacilityName = "Hata: Sağlık tesis ismi kullanılıyor.";
    public static final String notCreateHealthFacility = "Hata: Sağlık tesisi kayıt işlemi sırasında bir sorun oluştu. Lütfen tekrar deneyin.";

    public static final String notVerifyBot = "Hata: Bot doğrulama başarısız.";
    public static final String clientVersionUpdateRequiredOpAndroid = "Şu anda uygulamanın eski bir versiyonunu kullanmaktasınız. Lütfen en son özelliklerden yararlanmak için uygulamanızı Play Store'dan güncelleyiniz.";
    public static final String clientVersionUpdateRequiredOpIOS = "Şu anda uygulamanın eski bir versiyonunu kullanmaktasınız. Lütfen en son özelliklerden yararlanmak için uygulamanızı App Store'dan güncelleyiniz.";
    public static final String notFoundDoctorRecipeInformation = "Doctor reçete bilgisi bulunamadı.";


    /* Inspection Messages*/
    public static final String inValidInspectionId = "Hata: Geçersiz muayene id.";
    public static final String inValidInspectionContentId = "Hata: Geçersiz muayene içerik id.";

    public static final String notFoundInspectionInformation = "Hata: Muayane bilgisi bulunamadı.";
    public static final String notFoundInspectionContentInformation = "Hata: Muayene içerik bilgisi bulunamadı.";


    /* Patient Notes Messages */
    public static final String INVALID_PATIENT_NOTES_CONTENT_ID = "Hata: Geçersiz hasta not içerik id";

    /* Patient SysTakipNo */
    public static final String ALREADY_SYSTAKIPNO_FOR_INFORMATION = "Hata: Hasta ve doktor arasında systakipno bilgisi zaten mevcut.";
    public static final String NOT_ACCESS_SYSTAKIPNO_INFORMATION = "Hata: Hasta ve doktor arasında systakipno bilgisi bulunamadı.";

    public static final String INVALID_SYSTAKIPNO = "Hata: Geçersiz Systakipno.";

    public static final String INVALID_SERVICE_REFERENCE_NUMBER = "Hata: Geçersiz referans numarası";
}
