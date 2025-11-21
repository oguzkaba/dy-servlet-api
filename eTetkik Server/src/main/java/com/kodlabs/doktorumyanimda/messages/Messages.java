package com.kodlabs.doktorumyanimda.messages;

public final class Messages {
    public static final String bpWarningMessage = "Ölçümlerinizde ciddi yükseklik/düşüklük saptandı. Acil bir durumunuz olabilir. Şikayetiniz varsa en yakın acile başvurunuz.";
    public static final String bpMaxWarningTitle = "Çok yüksek değer uyarısı";
    public static final String bpMaxWarningMessage = "Ölçümlerinizde ciddi yükseklik saptandı, hekiminize danışınız. Şikayetiniz varsa acile başvurunuz.";
    public static final String bpMinWarningTitle = "Çok düşük değer uyarısı";
    public static final String bpMinWarningMessage = "Ölçümlerinizde ciddi düşüklük saptandı, hekiminize danışınız. Şikayetiniz varsa acile başvurunuz.";
    public static final String currentLowValueWarningMessage = "Anlık çok düşük değer uyarısı";
    public static final String currentHighValueWarningMessage = "Anlık çok yüksek değer uyarısı";
    public static final String currentWarningValueMessage = " için anlık değerler ( BT: %d,KT: %d, Nabız: %d ) ";
    public static final String bpMeasureValues = "Ölçüm değerleriniz: [BT: %d, KT: %d, Nabız: %d]";
    public static final String bpVeryLow = "çok düşük çıkmıştır.";
    public static final String bpVeryHigh = "çok yüksek çıkmıştır.";

    public static final String mailVerification = "Mail Doğrulama";

    public static final String messageYourFromDoctor = "Doktorunuzdan mesajınız var.";
    public static final String messageYourFromPatient = "Hastanızdan mesajınız var.";

    public static final String peakDemand = "Danışmanlık talebi";
    public static final String peakDemandStatus = "Danışmanlık talep durumu";
    public static final String peakDemandMessage = "Yeni bir danışmanlık talebiniz var. Lütfen kabul ediniz ya da reddediniz.";
    public static final String peakDemandRejectMessage = "Danışmanlık talebiniz danışman tarafından reddilmiştir. Reddedilme sebebi;\n";
    public static final String peakDemandSuccessTitle = "Talebiniz Onaylanmıştır";
    public static final String peakDemandSuccess = "Talep ettiğiniz %d günlük danışmanlık hizmeti danışman tarafından onaylanmıştır.\nDanışmanlık hizmetinin aktif olması için 24 saat içinde hizmet bedelini ödemeniz gerekmektedir.";
    public static final String peakDemandActiveSuccessTitle = "TEŞEKKÜR EDERİZ.";
    public static final String peakDemandActiveSuccess = "Mesajlaşma hizmetini kullanarak danışman ile iletişime geçebilirsiniz. Uygulama içine tetkikleriniz, ilaçlarınız, şikayetleriniz ve tansiyon ölçümlerinizi girmenizi öneririz. Danışmanlık hizmetinin karşılıklı muayene yerine geçmeyeceğini tekrar hatırlatmak isteriz. Geçmiş olsun.";

    public static final String peakPaymentTitle = "Danışmanlık ücret bildirimi";
    public static final String peakPaymentMessage = "%s adlı kullanıcı ödemesini gerçekleştirmiştir.";

    public static final String smsLoginVerifyMessage = "%s koduyla Doktorum Yanimda hesabiniza giris yapabilirsiniz.";

    public static final String passwordForgotCodeVerifyMessage = "%s kodunu Doktorum Yanimda uygulamasinda kullanarak sifrenizi yenileyebilirsiniz.";

    public static final String passwordReset = "Şifre Sıfırlama";

    // Test new account message
    public static final String newAccountTitle = "Yeni Hesap Bilgilendirme";
    public static final String newAccountMessage = "Doktorum Yanımda ailesine katıldığınız için teşekkür ederiz. Hesabınız başarıyla oluşturulmuştur.";
}
