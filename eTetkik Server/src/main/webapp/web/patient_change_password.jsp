<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="patient_app_change_password" ng-init="title='Şifre Değiştir'; userID='<%=Functions.getCookieValue(request,"userID")%>'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="change_password_controller">
    <%@include file="alert.jsp"%>
    <%@include file="header/patient_sidebar.jsp"%>
    <div class="main-panel bg-light">
        <%@include file="header/patient_header.jsp"%>
        <div class="content">
            <div class="container-fluid position-relative">
                <h5 class="fs-4 my-4 w-100 text-center fw-bold">Şifre Değiştir</h5>
                <form ng-submit="changePassword()" class="mx-auto bg-white rounded-2 border-0 shadow-sm py-2 px-3" style="min-width: 18rem; max-width: 24rem">
                    <div class="w-100 my-3">
                        <input class="form-control" name="available_password" ng-model="available_password" ng-required="true" type="password" placeholder="Mevcut Şifre" required >
                    </div>
                    <div class="w-100 mb-3">
                        <input class="form-control" name="new_password" ng-model="new_password" type="password" ng-required="true" placeholder="Şifre" required >
                    </div>
                    <div class="w-100 mb-5">
                        <input class="form-control" name="new_password_confirm" ng-model="new_password_confirm" ng-required="true" type="password" placeholder="Şifre (Tekrar)" required >
                    </div>
                    <div class="w-100 d-flex justify-content-center">
                        <button type="submit" class="btn btn-primary mb-3" style="width: 10rem">Değiştir</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>