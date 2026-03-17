<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_register" ng-init="title='Kayıt Ol'">
<head>
  <%@include file="header/header.jsp"%>
</head>
<body ng-controller="register_controller">
    <%@include file="alert.jsp"%>
    <div class="container-fluid vh-100 d-flex justify-content-center align-items-start align-items-lg-center">
      <form ng-submit="register()" class="d-grid gap-3 py-2" style="width: 20rem;">
        <h5 class="fs-3 mb-4 text-center">Kayıt Ol</h5>
        <div class="form-input">
          <input class="form-control" name="userName" ng-model="userName" type="text" placeholder="Kullanıcı Adı" pattern="[A-Za-z0-9]{3,11}" required>
          <div class="information">
            Kullanıcı adı en az 3, en fazla 11 haneden oluşur ve (A-Z, a-z, 0-9) karakterlerini kabul eder
          </div>
        </div>
        <div class="row g-2">
          <div class="col">
            <input class="form-control" name="name"  ng-model="name" type="text" placeholder="Ad" required>
          </div>
          <div class="col">
            <input class="form-control" name="surname" ng-model="surname" type="text" placeholder="Soyad" required>
          </div>
        </div>
        <div class="form-input">
          <input class="form-control" name="doctorCode" type="text" ng-model="doctorCode" placeholder="Doktor Kodu" pattern="[A-Za-z0-9]{6,11}" required>
          <div class="information">
            Doktor kodu en az 6 en fazla 11 haneden oluşur ve (A-Z, a-z, 0-9) karakterlerini kabul eder
          </div>
        </div>
        <div class="form-input">
          <span>Cinsiyet:</span>
          <div class="d-flex flex-row">
            <label><input class="form-check-input" type="radio" id="maleBtn" name="gender" value="Erkek" checked/> Erkek</label>
            <label><input class="form-check-input mx-2" type="radio" id="femaleBtn" value="Kadın" name="gender"/> Kadın</label>
          </div>
        </div>
        <div class="form-input">
          <input class="form-control" type="password" ng-model="password" placeholder="Şifre" required>
        </div>
        <div class="form-input">
          <input class="form-control" type="password" ng-model="password_confirm" placeholder="Şifre (Tekrar)" required>
        </div>
        <div class="d-flex flex-column">
          <a href="" data-bs-toggle="modal" data-bs-target="#termsModal" class="text-decoration-none col fs-6" ng-click="openDialog('${pageContext.request.contextPath}/terms')">Şartlar ve Koşulları'</a>
          <label class="fs-6 col"> Okudum ve kabul ediyorum. <input type="checkbox" class="form-check-input" id="termsAccept" required></label>
        </div>
        <button type="submit" class="btn btn-primary">KAYIT OL</button>
      </form>
      <div class="modal fade" id="termsModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
          <div class="modal-content">
            <div class="modal-body">
              <ng-include src="dContent"></ng-include>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" id="closeBtn" data-bs-dismiss="modal">Kapat</button>
              <button data-bs-dismiss="modal" class="btn btn-primary" ng-click="termsAccept()">Kabul Ediyorum</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>