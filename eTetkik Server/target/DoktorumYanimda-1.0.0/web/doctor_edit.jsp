<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="doctor_edit_app" ng-init="title='Doktor Düzenle'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body ng-controller="doctor_edit_controller">
    <%@include file="alert.jsp"%>
    <div class="wrapper">
      <%@include file="header/admin_sidebar.jsp"%>
      <div class="main-panel bg-white">
        <div class="header bg-primary d-flex align-items-center justify-content-between">
          <div class="d-flex justify-content-start">
            <div class="header-logo d-block d-md-none">
              <a class="color-white p-2 fs-5" href="#"><img src="${pageContext.request.contextPath}/assets/img/icon/logo_white.svg" width="32px" height="32px"/>eTakip</a>
            </div>
          </div>
          <div class="header-content d-flex">
            <a class="p-2 icon-btn" href="" data-bs-toggle="modal" data-bs-target="#logoutModal"><img src="${pageContext.request.contextPath}/assets/img/icon/logout.svg" width="24px" height="24px" alt=""></a>
            <a id="menuBtn" class="p-2 icon-btn" href="javascript:openCloseMenu()"><img src="${pageContext.request.contextPath}/assets/img/icon/menu.svg" width="24px" height="24px" alt=""></a>
          </div>
        </div>
            <div class="content d-flex align-items-lg-center align-items-start justify-content-center">
                <div class="container-fluid position-relative w-auto">
                  <form ng-submit="doctorUpdate()" method="post" class="d-grid gap-3 my-4" style="width: 20rem;">
                    <h5 class="fs-3 mb-4 text-center">Doktor Düzenle</h5>
                    <div class="form-input">
                      <input class="form-control" type="email" ng-model="mail" placeholder="E-Mail">
                    </div>
                    <div class="form-input">
                      <input class="form-control" type="tel" ng-model="phone" placeholder="Telefon">
                    </div>
                    <div class="form-input">
                      <select name="degree" class="form-control form-select" ng-model="degree" required>
                        <option selected>Pratisyen Doktor</option>
                        <option>Uzman Doktor</option>
                        <option>Operatör Doktor</option>
                        <option>Yardımcı Doçent</option>
                        <option>Doçent</option>
                        <option>Profesör</option>
                        <option>Ordinaryüs</option>
                      </select>
                    </div>
                    <div class="form-check">
                      <input class="form-check-input" ng-model="change_password" type="checkbox" id="changePassword">
                      <label class="form-check-label" for="changePassword">
                        Şifre Değiştir
                      </label>
                    </div>
                    <div ng-if="change_password" class="d-grid gap-3">
                      <div class="form-input">
                        <input class="form-control" type="password" id="passwd" ng-model="passwd" placeholder="Şifre">
                      </div>
                      <div class="form-input">
                        <input class="form-control" type="password" id="passwd_confirm" ng-model="passwd_confirm" placeholder="Şifre (Tekrar)">
                      </div>
                    </div>
                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal">GÜNCELLE</button>
                  </form>
                </div>
            </div>
        </div>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>