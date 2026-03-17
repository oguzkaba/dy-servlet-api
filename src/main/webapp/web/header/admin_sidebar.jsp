<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar bg-primary shadow-sm" id="sideBarMenu">
    <div class="header-logo d-flex justify-content-start">
        <a class="color-white p-2" href="#"><img src="${pageContext.request.contextPath}/assets/img/icon/logo_white.svg" width="48px" height="48px"/>eTakip</a>
    </div>
    <div class="d-flex flex-column overflow-auto py-4 overflow-auto">
        <ul class="nav flex-column px-3">
            <li class="nav-item custom-nav-item" style="visibility: hidden">
                <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/admin/control/main">Hastalar</a>
            </li>
            <li class="nav-item custom-nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/control/doctor/list">Doktorlar</a>
            </li>
            <li class="nav-item custom-nav-item" style="visibility: hidden">
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/control/survey/add">Anket Ekle</a>
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
                <button type="button" class="btn btn-danger" onclick="window.location.href = '${pageContext.request.contextPath}/admin/control/logout'">Evet</button>
                <button type="button" class="btn btn-success" data-bs-dismiss="modal">Hayır</button>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/menu.js"></script>