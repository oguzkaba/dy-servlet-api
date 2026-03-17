<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="doctor_register_app" ng-init="title='Doktor Ekle'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body ng-controller="doctor_register_controller">
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
                  <form ng-submit="doctorRegister()" method="post" class="d-grid gap-3 my-4" style="width: 20rem;">
                    <h5 class="fs-3 mb-4 text-center">Doktor Ekle</h5>
                    <div class="form-input">
                      <input class="form-control" type="text" name="tcno" ng-model="tcNo" placeholder="T.C Numarası" pattern="[0-9]{11}" required>
                    </div>
                    <div class="row g-2">
                      <div class="col">
                        <input class="form-control" type="text" name="name" ng-model="name" placeholder="Ad" required>
                      </div>
                      <div class="col">
                        <input class="form-control" type="text" name="surname" ng-model="surname" placeholder="Soyad" required>
                      </div>                      
                    </div>
                    <div class="form-input">
                      <input class="form-control" name="mail" type="email" ng-model="mail" placeholder="E-Mail">
                    </div>
                    <div class="form-input">
                      <input class="form-control" name="tel" type="tel" ng-model="phone" placeholder="Telefon">
                    </div>
                    <div class="form-input">
                      <input class="form-control" name="doctorCode" type="text" ng-model="doctorCode" placeholder="Doktor Kodu" pattern="^([A-Z]{2}[A-Za-z]{2,3}[0-9]{4,7})$" required>
                      <div class="information">
                        Doktor kodu en az 6 en fazla 11 haneden oluşur ve (A-Z, a-z, 0-9) karakterlerini kabul eder. Örnek: HTxx9900
                      </div>
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
                    <div class="form-input">
                      <span>Cinsiyet:</span>
                      <div class="d-flex flex-row">
                        <label><input class="form-check-input" type="radio" name="gender" ng-model="gender" ng-value="true" ng-checked="true"/> Erkek</label>
                        <label><input class="form-check-input mx-2" type="radio" name="gender" ng-model="gender" ng-value="false"/> Kadın</label>
                      </div>
                    </div>
                    <div class="form-input">
                      <input class="form-control" name="password" type="password" ng-model="password" placeholder="Şifre" required>
                    </div>
                    <div class="form-input">
                      <input class="form-control" name="password_confirm" type="password" ng-model="password_confirm" placeholder="Şifre (Tekrar)" required>
                    </div>
                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal">KAYDET</button>
                  </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="messageModal" tabindex="-1" aria-labelledby="messageModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="messageModalLabel">Mesaj Gönder</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <form>
                <div class="mb-3">
                  <label for="recipient-name" class="col-form-label">Başlık:</label>
                  <input type="text" class="form-control" id="recipient-name">
                </div>
                <div class="mb-3">
                  <label for="message-text" class="col-form-label">Mesaj:</label>
                  <textarea class="form-control" id="message-text" style="max-height: 200px;"></textarea>
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Kapat</button>
              <button type="button" class="btn btn-primary">Gönder</button>
            </div>
          </div>
        </div>
      </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>