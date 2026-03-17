<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar bg-primary" id="sideBarMenu">
    <div class="header-logo d-flex justify-content-start">
        <a class="color-white p-2" href="#"><img src="${pageContext.request.contextPath}/assets/img/icon/logo_white.svg" width="48px" height="48px"/>eTakip</a>
    </div>
    <button id="install-button" class="btn btn-primary rounded-circle position-fixed bottom-0 end-0">+</button>
    <div class="d-flex flex-column overflow-auto py-4 overflow-auto">
        <ul class="nav px-4 d-grid gap-2 my-2">
            <li class="nav-item custom-nav-item">
                <img src="${pageContext.request.contextPath}/assets/img/icon/home.svg" width="24px" height="24px"/>
                <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/patient/main">Ana Sayfa</a>
            </li>
            <li class="nav-item custom-nav-item">
                <img src="${pageContext.request.contextPath}/assets/img/icon/hypertension.svg" width="24px" height="24px"/>
                <a class="nav-link" href="${pageContext.request.contextPath}/patient/bloodPressure">Tanisyon</a>
            </li>
            <li class="nav-item">
                <div class="custom-nav-item">
                    <img src="${pageContext.request.contextPath}/assets/img/icon/pills.svg" width="24px" height="24px"/>
                    <a class="nav-link collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#userDrugSubMenu" aria-expanded="false">İlaçlar</a>
                </div>
                <ul id="userDrugSubMenu" class="accordion-collapse collapse nav-item">
                    <li class="nav-item custom-nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/patient/userDrug">İlaç Ekle</a>
                    </li>
                    <li class="nav-item custom-nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/patient/userDrug/list">İlaçlarım</a></li>
                </ul>
            </li>
            <li class="nav-item">
                <div class="custom-nav-item">
                    <img src="${pageContext.request.contextPath}/assets/img/icon/examination.svg" width="24px" height="24px"/>
                    <a class="nav-link" type="button" data-bs-toggle="collapse" data-bs-target="#examinationSubMenu" aria-expanded="false" aria-controls="collapseTwo">Tetkikler</a>
                </div>
                <ul id="examinationSubMenu" class="accordion-collapse collapse nav-item">
                    <li class="nav-item custom-nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/patient/examination/add">Tetkik Ekle</a>
                    </li>
                    <li class="nav-item custom-nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/patient/examination/list">Tetkiklerim</a></li>
                </ul>
            </li>
            <li class="nav-item">
                <div class="custom-nav-item">
                    <img src="${pageContext.request.contextPath}/assets/img/icon/complaint.svg" width="24px" height="24px"/>
                    <a class="nav-link collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#complaintsSubMenu" aria-expanded="false">Şikayetler</a>
                </div>
                <ul id="complaintsSubMenu" class="accordion-collapse collapse nav-item">
                    <li class="nav-item custom-nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/patient/complaint/create">Şikayet Oluştur</a>
                    </li>
                    <li class="nav-item custom-nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/patient/complaint/list">Şikayetlerim</a></li>
                </ul>
            </li>
        </ul>
        <ul class="nav d-grid gap-2 px-4">
            <li class="nav-item custom-nav-item">
                <img src="${pageContext.request.contextPath}/assets/img/icon/settings.svg" width="24px" height="24px"/>
                <a class="nav-link" href="${pageContext.request.contextPath}/patient/setting">Ayarlar</a>
            </li>
            <li class="nav-item custom-nav-item">
                <img src="${pageContext.request.contextPath}/assets/img/icon/logout.svg" width="24px" height="24px"/>
                <a class="nav-link" href="" data-bs-toggle="modal" data-bs-target="#logoutModal">Çıkış Yap</a>
            </li>
        </ul>
    </div>
</div>
<div class="modal" id="logoutModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Hesap Çıkış</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>Hesaptan çıkış yapmak istediğinizden emin misiniz?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" onclick="window.location.href = '${pageContext.request.contextPath}/patient/account/logout'">Evet</button>
                <button type="button" class="btn btn-success" data-bs-dismiss="modal">Hayır</button>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/menu.js"></script>
