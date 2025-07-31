<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doktorum Yanımda: Online Sağlık Danışmanlığı</title>
    <meta name="description" content="Türkiye’nin En İyi Online Sağlık Danışmanlığı ve Uzaktan Görüşme Uygulaması, Tetkiklerinizi – Tedavilerinizi Danışın – İkinci Görüş Alın">
    <meta name="keywords" content="doktora sor, şikayet sor, hastalık sor, online sağlık danışmanlık, ikinci görüş">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/img/icon/favicon.ico" type="image/x-icon"/>
</head>

<body>
<header class="bg-primary sticky-top">
    <div class="container-fluid container-lg">
        <nav class="navbar navbar-expand-lg navbar-light bg-primary d-flex justify-content-between">
            <img class="navbar-brand" src="${pageContext.request.contextPath}/assets/img/icon/logo.svg" alt="" width="48px" height="48px"
                 id="logo">
            <span class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation" style="border:none;outline: none">
                <i class="fa-solid fa-bars text-white"></i>
            </span>
            <div class="collapse navbar-collapse flex-grow-0" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item nav-link"><a href="#hero" class="custom-link">Anasayfa</a></li>
                    <li class="nav-item nav-link"><a href="#about" class="custom-link">Hakkında</a></li>
                    <li class="nav-item nav-link"><a href="#services" class="custom-link">Özellikler</a></li>
                    <li class="nav-item nav-link"><a href="#sss" class="custom-link">SSS</a></li>
                    <li class="nav-item nav-link"><a href="#contact" class="custom-link">İletişim</a></li>
                </ul>
            </div>
        </nav>
    </div>
</header>
<section id="hero" class="d-flex align-items-lg-center py-4">
    <div class="container-fluid">
        <div class="row align-items-center justify-content-center">
            <div class="col-12 col-lg-4">
                <div class="d-flex flex-column align-items-start">
                    <h1 class="text-white fw-bold mb-4 animation" data-animation="fade">
                        Doktorum Yanımda
                    </h1>
                    <p class="text-white fs-5 mb-4 animation" data-animation="fade">
                        Doktorum Yanımda, hastaların sağlık profesyonelleri ile etkin bir şekilde iletişim kurmasına
                        yardımcı olan bir sağlık bilgi sistemi uygulamasıdır.
                    </p>
                    <p class="text-white fs-4 mb-4 animation" data-animation="fade">Hemen Kullanmaya Başlayın</p>
                    <div class="d-flex flex-column flex-lg-row justify-content-start justify-content-lg-center">
                        <a href="https://play.google.com/store/apps/details?id=com.kodlabs.etetkik" target="_blank"><img class="btn-icon"
                                         src="${pageContext.request.contextPath}/assets/img/icon/play_store_btn.svg"
                                         width="200px" height="50px" alt=""></a>
                        <a href="https://apps.apple.com/app/id1610557265" target="_blank"><img class="btn-icon mt-4 mt-lg-0"
                                         src="${pageContext.request.contextPath}/assets/img/icon/app_store_btn.svg"
                                         width="200px" height="50px" alt=""></a>
                    </div>
                </div>
            </div>
            <div class="col-12 col-lg-4 d-none d-lg-block text-center">
                <img class="img-fluid up-down-animeted animation" data-animation="zoom"
                     src="${pageContext.request.contextPath}/assets/img/hero_phone.png" alt="" width="400px">
            </div>
        </div>
    </div>
</section>
<section id="about">
    <div class="container">
        <div class="row py-5">
            <div class="col-xl-5 col-lg-6">
                <div class="d-flex flex-column h-auto my-4 animation" data-animation="fade">
                    <ul class="fs-5" style="list-style: none;">
                        <li class="mt-2"><i class="fa-regular fa-circle-check" style="color: var(--bs-primary)"></i>
                            Uygulama üzerinden online danışma randevusu talebi göndermek.
                        </li>
                        <li class="mt-2"><i class="fa-regular fa-circle-check" style="color: var(--bs-primary)"></i>
                            Mesajlaşma arayüzü üzerinden danışmanlarla mesajlaşmak.
                        </li>
                        <li class="mt-2"><i class="fa-regular fa-circle-check" style="color: var(--bs-primary)"></i>
                            Mesajlaşma arayüzü üzerinden danışmanlara mobil cihazdaki tıbbi raporları veya görüntüleri
                            göndermek.
                        </li>
                        <li class="mt-2"><i class="fa-regular fa-circle-check" style="color: var(--bs-primary)"></i>
                            Sesli bir mesaj kaydedip bunu danışmanlara göndermek.
                        </li>
                        <li class="mt-2"><i class="fa-regular fa-circle-check" style="color: var(--bs-primary)"></i>
                            Belirli bir tarih için kan basıncı bilgilerini göndermek.
                        </li>
                    </ul>
                </div>
            </div>
            <div class="col-xl-5 col-lg-6 d-flex justify-content-center">
                <img class="img-fluid" src="${pageContext.request.contextPath}/assets/img/about-img.svg" alt=""
                     width="80%" height="auto">
            </div>
        </div>
    </div>
</section>
<section id="services" class="bg-light">
    <div class="container py-5">
        <h1 class="text-center text-uppercase fw-bold fs-1 section-title">ÖZELLİKLER</h1>
        <div class="row mt-5 px-2 py-4 mx-md-2 gap-4 justify-content-center">
            <div class="col-lg-3 col-md-12 align-items-stretch border rounded text-center bg-white px-4 py-6 animation"
                 data-animation="fade">
                <img src="${pageContext.request.contextPath}/assets/img/icon/blood_pressure_default.svg"
                     class="m-4 icon-md" alt="">
                <h3 class="text-center">Tansiyon Ölçümü</h3>
                <p>Tansiyon ölçümleriniz kaydedebilirsiniz. Günlük, haftalık tansiyon ölçüm değerlerinizin ortalaması hesaplanır. Danışmanla randevunuz aktive olduğunda danışman ölçümlerinizi görecektir.</p>
            </div>
            <div class="col-lg-3 col-md-12 align-items-stretch border rounded text-center bg-white px-4 py-6 animation"
                 data-animation="fade">
                <img src="${pageContext.request.contextPath}/assets/img/icon/medical_laboratory_default.svg"
                     class="m-4 icon-md" alt="">
                <h3 class="text-center">Tetkik</h3>
                <p>Danışmak istediğiniz tetkilerinizi ekleyiniz. Randevunuz aktive olduğunda danışmanınız tetkiklerinizi inceleyebilir ve yorumlayabilir.</p>
            </div>
            <div class="col-lg-3 col-md-12 align-items-stretch border rounded text-center bg-white px-4 py-6 animation"
                 data-animation="fade">
                <img src="${pageContext.request.contextPath}/assets/img/icon/capsule.svg" class="m-4 icon-md"
                     alt="">
                <h3 class="text-center">İlaçlar</h3>
                <p>Kullandığınız ilaçları ekleyeyiniz. Danışmanınız halen kullandığınız ilaçlara değerlendirebilir ve size önerilerde bulunabilir.</p>
            </div>
        </div>
    </div>
</section>
<section id="sss" class="custom-bg">
    <div class="container py-5">
        <h1 class="text-center text-uppercase fw-bold text-white my-5 section-title">SIK SORULAN SORULAR</h1>
        <p class="text-white">Doktorum Yanımda hakkında sık sorulan bazı soruları aşağıda bulabilirsiniz. Eğer aşağıdaki sorular yardımcı olamadıysa bize
            <a class="fw-bold text-white text-decoration-none" href="mailTo:info@kodlabs.com">info@kodlabs.com</a> adresimizden veya
            <a class="fw-bold text-white text-decoration-none" href="tel:+903129115435">+903129115435</a> numaralı destek hattımızdan ulaşabilirsiniz.</p>
        <h5 class="text-uppercase fw-bold text-white my-2 animation"  data-animation="fade">GENEL</h5>
        <div class="accordion accordion-flush animation" id="sss_accordion" data-animation="fade">
            <div class="accordion-item animation">
                <h2 class="accordion-header" id="flush-headingOne">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_1" aria-expanded="false" aria-controls="flush-collapseOne">
                      “Doktorum Yanımda” nedir?
                    </span>
                </h2>
                <div id="sss_1" class="accordion-collapse collapse" aria-labelledby="flush-headingOne" data-bs-parent="#sss_accordion">
                    <div class="accordion-body">“Doktorum Yanımda” sağlık problemleriniz konusunda uzaktan danışmanlık hizmeti almanızı sağlayan bir sağlık bilgi sistemidir. Sağlık profesyonellerinden, sağlık kuruluşuna gitmeden ve yüzyüze görüşmeye gerek kalmadan uygulama içinde
                        mesajlaşma veya görüntülü görüşme yöntemleriyle hizmet alabilirsiniz.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-headingTwo">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_2" aria-expanded="false" aria-controls="sss_2">
                      “Doktorum Yanımda” uygulamasının avantajları nelerdir?
                    </span>
                </h2>
                <div id="sss_2" class="accordion-collapse collapse" aria-labelledby="flush-headingTwo" data-bs-parent="#sss_accordion">
                    <div class="accordion-body">Son yıllarda dijital dünyada gelişmeler, tüm dünyada ve ülkemizde uzaktan sağlık hizmetlerine ilgiyi arttırmıştır. Hareket etme, transfer zorluğu olan yaşlılar, yatağa bağımlı hastalar veya COVID salgını veya farklı sebeplerle
                        evinden çıkmadan sağlık hizmeti almak isteyen bireylerin sayısı artmaktadır. Günlük hayatta kullanılan bazı platformlardan sağlık profesyonellerine ulaşmak mümkün olsa da, standart ve yeterli sağlık hizmetinin sağlanabilmesi
                        için bu hizmet için özel geliştirilmiş uygulamalara ihtiyaç duyulmaktadır. “Doktorum Yanımda” uygulaması güvenli ve kaliteli uzaktan sağlık hizmeti sağlamayı amaçlamaktadır.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-headingThree">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_3" aria-expanded="false" aria-controls="flush-sss_3">
                      Ne tür hastalıklar konusunda hizmet alabilirim?
                    </span>
                </h2>
                <div id="sss_3" class="accordion-collapse collapse" aria-labelledby="flush-headingThree" data-bs-parent="#sss_accordion">
                    <div class="accordion-body">Uzaktan sağlık hizmet yönetmeliğinde de tanımlandığı üzere, hizmeti sağlayan sağlık profesyoneline göre değişiklik göstermekle birlikte genel olarak sağlık profesyonelleri hastalığınız konusunda sizi muayene edebilir, takibe alabilir,
                        tetkik ve ölçümlerinizi (kan basıncı, şeker ölçümleri) değerlendirebilir. Sağlıklı yaşam, psikososyal konularda destek amacıyla size önerilerde bulunabilir.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-headingFour">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_4" aria-expanded="false" aria-controls="flush-sss_4">
                      “Doktorum Yanımda” uygulaması hangi hastalıklar için uygun değildir?
                    </span>
                </h2>
                <div id="sss_4" class="accordion-collapse collapse" aria-labelledby="flush-headingFour" data-bs-parent="#sss_accordion">
                    <div class="accordion-body">Uzaktan sağlık hizmetleri, teknolojinin elverdiği ölçüde size danışmanlık hizmeti sağlayabilir. Özellikle acil durumlarda “Doktorum Yanımda” uygulamasının kullanımı uygun değildir ve kesinlikle önerilmez. Böyle bir durumda, danışmanlık
                        talebinde bulunsanız bile 112 acil ambulans servisini arayıp en yakın sağlık kuruluşuna gitmeniz gerekmektedir. Bu konuda, sorumluluk size aittir ve tarafımızca sorumluluk kabul edilmez.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-headingFive">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_5" aria-expanded="false" aria-controls="flush-sss_5">
                      Fizik muayene, radyolojik görüntüleme veya kan tahlili yaptırabilir miyiz?
                    </span>
                </h2>
                <div id="sss_5" class="accordion-collapse collapse" aria-labelledby="flush-headingFive" data-bs-parent="#sss_accordion">
                    <div class="accordion-body">Sağlık profesyoneli, sizi değerlendirdikten sonra ileri fizik muayene gerek görürse sizi sağlık kurumuna yönlendirebilir. Daha önce yapılmış tetkikleriniz veya radyoloji görüntülerinizi kullanarak tanı konabilir. Ancak, mevcut
                        tetkikleriniz yeterli görülmezse, sağlık kuruluşlarına başvurup, gerekli tetkikleri yaptırmanız gerekecektir.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-headingSix">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_6" aria-expanded="false" aria-controls="flush-sss_6">
                      Danışman tetkiklerimi nasıl görecek?
                    </span>
                </h2>
                <div id="sss_6" class="accordion-collapse collapse" aria-labelledby="flush-headingSix" data-bs-parent="#sss_accordion">
                    <div class="accordion-body">Sağlık profesyonellerinin sizin tetkiklerinizi görebilmesi için, sizin uygulama içine danışmak istediğiniz konu/hastalıkla ilgili tıbbi verilerinizi eklemeniz gerekmektedir. “Doktorum Yanımda” uygulamasının sizin farklı platformlar,
                        hastane veya sağlık kuruluşlarında kayıtlı tetkiklerinize ulaşma imkanı yoktur.
                    </div>
                </div>
            </div>
        </div>
        <h5 class="text-uppercase fw-bold text-white my-2 animation" data-animation="fade">RANDEVU</h5>
        <div class="accordion accordion-flush animation"  data-animation="fade" id="sss_appointment_accordion">
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-appointment-headingOne">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_appointment_1" aria-expanded="false" aria-controls="flush-collapseOne">
                      Nasıl randevu oluşturabilirim?
                    </span>
                </h2>
                <div id="sss_appointment_1" class="accordion-collapse collapse" aria-labelledby="flush-appointment-headingOne" data-bs-parent="#sss_appointment_accordion">
                    <div class="accordion-body">“Uygulamanın ana ekranında “Danışmanlar” sekmesinde hizmet aldığımız sağlık profesyonellerini bulabilirsiniz. Uzmanlık alanları, profil bilgilerine göre danışmanınızı belirledikten sonra “Danışma Talep Et” butonuna bastıktan sonra,
                        danışmak istediğiniz konuyu kısaca özetleyerek randevu talep edebilirsiniz.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-appointment-headingTwo">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_appointment_2" aria-expanded="false" aria-controls="flush-collapseOne">
                      Sağlık profesyonelleri bana ne şekilde cevap verecek?
                    </span>
                </h2>
                <div id="sss_appointment_2" class="accordion-collapse collapse" aria-labelledby="flush-appointment-headingTwo" data-bs-parent="#sss_appointment_accordion">
                    <div class="accordion-body">Danışman talebinizi onayladığında size bildirim yollayacağız. Onay sonrasında 24 saat süresince danışman ile “MESAJLAR” ekranından metin – sesli mesaj – görüntü yollama şeklinde danışmanlık hizmeti alabilirsiniz. 24 saatin sonunda
                        mesajlaşma ekranı kapanacaktır.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-appointment-headingThree">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_appointment_3" aria-expanded="false" aria-controls="flush-collapseOne">
                      Randevularımı nasıl takip edeceğim?
                    </span>
                </h2>
                <div id="sss_appointment_3" class="accordion-collapse collapse" aria-labelledby="flush-appointment-headingThree" data-bs-parent="#sss_appointment_accordion">
                    <div class="accordion-body">“RANDEVULARIM” ekranında randevu talep ettiğiniz, aktif veya süresi dolmuş hizmetleriniz listelenmektedir. Aktif randevularınızdan mesajlaşma hizmetini kullanabilirsiniz. Süresi dolmuş randevularınız için danışmandan tekrar randevu
                        talep etmeniz gerekmektedir.
                    </div>
                </div>
            </div>
            <div class="accordion-item mt-3">
                <h2 class="accordion-header" id="flush-appointment-headingFour">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_appointment_4" aria-expanded="false" aria-controls="flush-collapseOne">
                      Randevu almak ücretli midir?
                    </span>
                </h2>
                <div id="sss_appointment_4" class="accordion-collapse collapse" aria-labelledby="flush-appointment-headingFour" data-bs-parent="#sss_appointment_accordion">
                    <div class="accordion-body">Uzaktan sağlık hizmet yönetmeliği kuralları gereğince, sağlık bilgi sistemlerinin Sağlık Bakanlığı tarafından tescil edilmesi gerekmektedir. Tescil süreci tamamlanana kadar hizmetlerimiz ücretsizdir.
                    </div>
                </div>
            </div>
        </div>

        <h5 class="text-uppercase fw-bold text-white my-2 animation" data-animation="fade">ÜYELİK</h5>
        <div class="accordion accordion-flush animation" data-animation="fade" id="sss_subscribe_accordion">
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-subscribe-headingOne">
                        <span class="accordion-button collapsed" style="user-select: none; cursor: pointer;" data-bs-toggle="collapse" data-bs-target="#sss_subscribe_1" aria-expanded="false" aria-controls="flush-collapseOne">
                            “Doktorum Yanımda” uygulamasını üye olmadan kullanabilir miyim?
                    </span>
                </h2>
                <div id="sss_subscribe_1" class="accordion-collapse collapse" aria-labelledby="flush-subscribe-headingOne" data-bs-parent="#sss_subscribe_accordion">
                    <div class="accordion-body">“Doktorum Yanımda” uygulaması sadece uygulamaya üye olan kullanıcılar tarafından kullanılabilir. Tüm kullanıcılar, uygulamaya kayıt olurken dijital olarak “Aydınlatılmış onam formu” ve “Açık rıza onam formu”nu okumuş ve onaylamış
                        olması gerekmektedir. Bu işlemleri yapmayan kullanıcılar, uygulamaya giremezler.
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<section id="contact" class="bg-light">
    <div class="container py-5">
        <h1 class="text-center text-uppercase fw-bold fs-1 section-title">İLETİŞİM</h1>
        <div class="row mt-5">
            <div class="col-md-12 col-lg-6 mt-2">
                <iframe class="mb-4 mb-lg-0 border rounded"
                        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d191.2699116795153!2d32.75017012606069!3d39.91188611396934!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x36e81c884768d63e!2zMznCsDU0JzQyLjkiTiAzMsKwNDUnMDEuNSJF!5e0!3m2!1str!2str!4v1646041578281!5m2!1str!2str"
                        style="width: 100%; height: 100%;" allowfullscreen="" loading="lazy"></iframe>


            </div>
            <div class="col-md-12 col-lg-6 mt-2">
                <div class="custom-grid justify-content-between" style="--bs-gap: .25rem 1rem;">
                    <div class="text-center py-2 bg-white border rounded">
                        <img src="${pageContext.request.contextPath}/assets/img/icon/mail.svg" class="m-2" width="32px"
                             height="32px" alt="">
                        <h5 class="text-center fw-bold mt-2">E-Posta</h5>
                        <p><a class="text-decoration-none" href="mailTo:info@kodlabs.com">info@kodlabs.com</a></p>
                    </div>
                    <div class="text-center py-2 bg-white border rounded">
                        <img src="${pageContext.request.contextPath}/assets/img/icon/phone.svg" class="m-2" width="32px"
                             height="32px" alt="">
                        <h5 class="text-center fw-bold mt-2">Tel</h5>
                        <p><a class="text-decoration-none" href="tel:+903129115435">+903129115435</a></p>
                    </div>
                    <div class="grid-span-2 py-2 px-4 text-center bg-white border rounded">
                        <img src="${pageContext.request.contextPath}/assets/img/icon/location.svg" class="m-2"
                             width="32px" height="32px" alt="">
                        <h5 class="text-center fw-bold mt-2">Adres</h5>
                        <p>ODTÜ TEKNOKENT MET YERLEŞKESİ, Mustafa Kemal Mah. Dumlupınar Bulvarı No:280 D Blok No:4 06510
                            - Çankaya/Ankara Türkiye</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/all.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.9.1/gsap.min.js"></script>
<script>
    const nav_links = document.querySelectorAll('.custom-link');
    const sections = document.querySelectorAll('section');
    const fadeAnimChildren = document.querySelectorAll('[data-animation="fade"]');

    fadeAnimChildren.forEach(child => {
        child.style.opacity = '0';
        child.style.transform = 'translateY(3rem);';
    });

    const zoomAnimChildren = document.querySelectorAll('[data-animation="zoom"]');
    zoomAnimChildren.forEach(child => {
        child.style.opacity = '0';
        child.style.transform = 'scale(0,0);';
    });
    const observe = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const target = entry.target;
                const attr = target.getAttribute("data-animated");
                const id = target.id;
                nav_links.forEach(link => {
                    if (link.classList.contains('active')) {
                        link.classList.remove('active');
                    }
                    if (link.href.indexOf(id) !== -1) {
                        link.classList.add('active');
                    }
                });
                if (!attr) {
                    const animateChildList = target.querySelectorAll('.animation');
                    if (animateChildList) {
                        if (target.querySelector('#' + id + ' [data-animation="fade"].animation')) {
                            const fadeAnimation = gsap.timeline({
                                defaults: {
                                    ease: Expo.InOut
                                }
                            });
                            fadeAnimation.fromTo('#' + id + ' [data-animation="fade"].animation', 1, {
                                y: "3rem",
                                opacity: 0
                            }, {
                                y: 0,
                                opacity: 1,
                                stagger: 0.2
                            });
                        }
                        if (target.querySelector('[data-animation="zoom"].animation')) {
                            console.log('zoom animation');
                            const zoomAnimation = gsap.timeline({
                                defaults: {
                                    ease: Expo.InOut
                                }
                            });
                            zoomAnimation.fromTo('[data-animation="zoom"].animation', 3, {
                                opacity: 0,
                                scale: 0
                            }, {
                                opacity: 1,
                                scale: 1,
                                stagger: 0.2
                            });
                        }
                    }
                    target.setAttribute('data-animated', 'true');
                }
            }
        })
    }, {
        threshold: .5
    });
    sections.forEach(section => {
        observe.observe(section);
    });
</script>
</body>

</html>