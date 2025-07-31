<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tansiyon ve Nabız Takibi Kullanımı</title>
    <style>
        *{font-family: Arial, Helvetica, sans-serif;}
        h1{font-size: 1.45rem;}
        h2{font-size: 1.30rem;}
        h3{font-size: 1.15rem;}
        p{font-size: 1rem;}
        .list li{font-size: 1rem;list-style: none;}
        body{margin: 0 5%;}
        @media (min-width: 1201px){
            body{
                margin: 0 30%;
            }
        }
        @media (max-width: 1200px){
            body{
                margin: 0 20%;
            }
        }
        @media (max-width: 768px){
            body{
                margin: 0 10%;
            }
        }
        @media (max-width: 480px){
            body{
                margin: 0 5%;
            }
        }
    </style>
</head>
<body>
<h1>Tansiyon ve Nabız Uyarı Bildirimi Gönderme Mantığı</h1>
<h2>Amaç</h2>
<p>Doktorum Yanımda uygulaması hastaların sisteme girmiş olduğu tansiyon ve nabız değerlerini analiz ederek hem hastaya
    hem de doktora uyarı bildirimi göndermektedir. Bu dokümanda hangi durumlarda uyarı bildirimi gönderileceği
    anlatılacaktır.</p>
<p>Uygulama içindeki tansiyon nabız girişi sayfası aşağıdaki gibidir:</p>
<img src="${pageContext.request.contextPath}/assets/img/blood_pressure_screen.jpg" alt="" width="300px">
<h2>Uyarı Bildirimi Kararı Ne Zaman Verilir</h2>
<p>Uyarılar gönderilme zamanlarına göre ikiye ayrılır:
<ol>
    <li>Periyodik uyarılar. Bunlar haftada bir Cuma akşamı gönderilir. Periyodik uyarılar hastaların son bir hafta
        içinde girmiş olduğu ölçüm değerlerinin ortalaması baz alınarak hesaplanır.</li>
    <li>Anlık uyarılar. Bu uyarılar hastalar ölçüm değerlerini sisteme gönderdikleri anda tetiklenir. Ölçüm değerleri
        eğer belli sınırların altında ya da üstünde ise gönderilirler.</li>
</ol>
</p>
<p>Her iki tip uyarı da hem hastaya hem de hastanın doktoruna gönderilir.</p>
<h2>Uyarı Bildirimine Nasıl Karar Verilir</h2>
<ol>
    <li>
        Periyodik uyarılar
        <p>
            Hastanın son bir hafta içinde girmiş olduğu ölçüm değerlerinin ortalamasına bakılır. Kaç tane ölçüm değeri olduğuna bakılmaz. 1 tane de ölçüm değeri de olabilir 14 tane de.
        </p>
        <p>
            Son bir haftalık;
        <ul>
            <li>
                BT değerlerinin ortalamsı <strong>135</strong>'den ya da
            </li>
            <li>
                KT değerlerinin ortalamsı <strong>85</strong>'den ya da
            </li>
            <li>
                nabız değerlerinin ortalamsı <strong>90</strong>'den büyükse uyarı bildirimi gönderilir.
            </li>
        </ul>
        </p>
    </li>
    <li>
        Anlık uyarılar
        <p>
        <ul>
            <li>
                <p>Anlık yüksek değer uyarısı. Bu uyarı anlık ölçüm değerleri aşağıdaki üst sınır değerleriyle karşılaştırılarak verilir.</p>
                <p>
                    <strong>Üst Sınır Değerleri</strong><br>
                    BT: 180<br>
                    KT: 100<br>
                    Nabız: 120
                </p>
                <p>
                    Hastanın tansiyon ve nabız anlık değerlerinden en az birisi yukarıdaki üst sınır değerlerinden yüksekse
                </p>
                <ul>
                    <li>
                        Önce hastaya mobil uygulama üzerinde aşağıdaki mesajlardan uygun olanı verilir:
                        <ul>
                            <li>
                                Tansiyonunuz çok yüksek çıkmıştır, 10 dk istirahat edip ölçümü tekrarlayınız
                            </li>
                            <li>
                                Nabzınız çok yüksek çıkmıştır, 10 dk istirahat edip ölçümü tekrarlayınız Girilen bu 1. ölçüm değerleri sunucuya gönderilmez.
                            </li>
                        </ul>
                    </li>
                    <li>
                        İkinci ölçüm değerini girmemeniz halinde doktora uyarı yollanmayacaktır.
                        <ul>
                            <li>
                                Ölçümlerinizde ciddi yükseklik saptandı, hekiminize danışınız. Şikayetiniz varsa acile başvurunuz.
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
            <li>
                <p>Anlık düşük değer uyarısı. Bu uyarı anlık ölçüm değerleri aşağıdaki alt sınır değerleriyle karşılaştırılarak verilir.</p>
                <p>
                    <strong>Alt Sınır Değerleri</strong><br>
                    BT: 90<br>
                    KT: 50<br>
                    Nabız: 50
                </p>
                <p>
                    Hastanın tansiyon ve nabız anlık değerlerinden en az birisi yukarıdaki alt sınır değerlerinden düşükse
                </p>
                <ul>
                    <li>
                        Önce hastaya mobil uygulama üzerinde aşağıdaki mesajlardan uygun olanı verilir:
                        <ul>
                            <li>
                                Tansiyonunuz çok düşük çıkmıştır, 10 dakika sonra tekrar ölçünüz
                            </li>
                            <li>
                                Nabzınız çok düşük çıkmıştır, 10 dakika sonra tekrar ölçünüz
                            </li>
                        </ul>
                    </li>
                    <li>
                        İkinci ölçüm değerini girmemeniz halinde doktora uyarı yollanmayacaktır.
                        <ul>
                            <li>
                                Ölçümlerinizde ciddi düşüklük saptandı, hekiminize danışınız. Şikayetiniz varsa acile başvurunuz.
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>
        </p>
    </li>
</ol>
<p>
    <strong>
        Aplikasyon tüm özellikleri ve uyarı bildirimleri sadece bilgilendirme amaçlıdır. Hekiminiz aplikasyon üzerinde size tedavi önermez. Şikayet varlığında en yakın sağlık kurumuna başvurmanız gerekmektedir.
    </strong>
</p>
</body>
</html>

