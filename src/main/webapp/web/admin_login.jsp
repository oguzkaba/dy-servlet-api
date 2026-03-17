<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_admin_login" ng-init="title='Admin Giriş'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body ng-controller="app_admin_login_controller">
    <%@include file="alert.jsp"%>
    <div class="wrapper d-flex align-items-center justify-content-center">
      <form ng-submit="login()" class="d-grid gap-3" style="width: 18rem;">
        <a href="#" class="text-center"><img src="${pageContext.request.contextPath}/assets/img/icon/logo.svg" width="96px" height="96px" alt=""></a>
        <input type="hidden" name="role" id="role" value="2"/>
        <div class="form-input">
          <input class="form-control" name="userName" type="text" placeholder="T.C Numarası" ng-model="userName" pattern="[0-9]{11}" ng-cloak="">
        </div>
        <div class="form-input">
          <input class="form-control" name="password" type="password" ng-model="password" placeholder="Şifre" ng-cloak="">
        </div>
        <div class="form-input">
          <label class="form-check-label">
              <input class="form-check-input" type="checkbox" name="remember_me" ng-model="remember_me"  ng-checked="rememberChecked" ng-cloak=""/> Beni hatırla
          </label>
        </div>
        <button type="submit" class="btn btn-primary">GİRİŞ YAP</button>
      </form>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>