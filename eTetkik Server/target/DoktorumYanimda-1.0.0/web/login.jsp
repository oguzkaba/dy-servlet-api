<%@ page import="com.kodlabs.doktorumyanimda.utils.Functions" %>
<%@ page import="org.w3c.dom.Text" %>
<%@ page import="com.kodlabs.doktorumyanimda.utils.TextUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="en" ng-app="app_login" ng-init="title='Login';role=0">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body ng-controller="app_login_controller">
    <%@include file="alert.jsp"%>
    <div class="wrapper d-flex align-items-center justify-content-center">
      <form ng-submit="login()" class="d-grid gap-3" style="width: 18rem;">
        <a href="#" id="roleIcon" class="text-center">
            <img src="${pageContext.request.contextPath}/assets/img/icon/patient.svg" width="96px" height="96px" alt="" ng-if="!role" ng-cloak>
            <img src="${pageContext.request.contextPath}/assets/img/icon/doctor.svg" width="96px" height="96px" alt="" ng-if="role" ng-cloak>
        </a>
        <input type="hidden" name="role" id="role" value="0"/>
        <div class="form-input" >
          <input class="form-control" type="text" ng-model="userName" placeholder="{{role?'T.C. Kimlik No':'T.C. Kimlik No veya Kullanıcı Adı'}}" required>
          <div class="information" ng-if="!role" ng-cloak>
            Kullanıcı adı en az 3 en fazla 11 karakterden  (A-Z, a-z, 0-9) oluşabilir
          </div>
          <div class="information" ng-if="role" ng-cloak>
              T.C. Kimlik Numarası 11 hane ve (0-9) oluşabilir.
          </div>
        </div>
        <div class="form-input">
          <input class="form-control" name="password" type="password" ng-model="password" placeholder="Şifre"  required>
        </div>
        <div class="form-input">
          <label class="form-check-label">
              <input class="form-check-input" type="checkbox" name="remember_me" ng-model="remember_me" checked/>
             Beni hatırla
          </label>
        </div>
        <div class="row">
          <button type="submit" class="btn btn-primary col mx-2" data-bs-dismiss="modal">GİRİŞ YAP</button>
          <a id="registerBtn" class="btn btn-primary col mx-2" ng-if="!role" ng-cloak href="${pageContext.request.contextPath}/account/register">KAYIT OL</a>
        </div>
        <a href="" class="text-center text-decoration-none color-secondary fs-6 my-2">Parolanızı mı unuttunuz?</a>
        <a href="" class="text-center text-decoration-none fs-6" ng-click="roleToggle()" ng-if="!role" ng-cloak>Doktor olarak giriş yap</a>
        <a href="" class="text-center text-decoration-none fs-6 " ng-click="roleToggle()" ng-if="role" ng-cloak>Hasta olarak giriş yap</a>
      </form>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>