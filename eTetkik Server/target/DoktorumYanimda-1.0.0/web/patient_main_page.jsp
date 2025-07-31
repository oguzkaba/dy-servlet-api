<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="patient_app_main" ng-init="title='Ana Sayfa';">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="patient_app_main_controller">
    <%@include file="alert.jsp"%>
    <%@include file="header/patient_sidebar.jsp"%>
    <div class="main-panel bg-light">
        <%@include file="header/patient_header.jsp"%>
        <div class="content">
            <div class="container-fluid px-xl-5 position-relative">
                <div class="row">
                    <div class="col-12 col-xl-8">
                        <div class="row h-25 justify-content-xl-around">
                            <div class="col-12 col-xl-4 p-xl-2">
                                <div class="card rounded-2 border-0 shadow-sm">
                                    <div class="card-body d-flex flex-column align-items-center justify-content-center">
                                        <img src="${pageContext.request.contextPath}/assets/img/icon/profile_default.svg" id="profilePictureImage" width="96px" height="96px" alt="">
                                        <h4 class="card-title mt-3" ng-bind="fullName" ng-cloak>-</h4>
                                        <button class="btn btn-light mt-3" onclick="window.location.href='${pageContext.request.contextPath}/patient/setting'">Profil Düzenle</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 col-xl-7 p-xl-2">
                                <div class="card rounded-2 border-0 shadow-sm">
                                    <div class="card-body">
                                        <ul class="list-group list-group-flush">
                                            <li class="list-group-item my-2">
                                                <div class="row">
                                                    <div class="col d-flex flex-column">
                                                        <span class="fw-bold">Cinsiyet</span>
                                                        <span ng-bind="patientGender">-</span>
                                                    </div>
                                                    <div class="col d-flex flex-column">
                                                        <span class="fw-bold">Doğum Yılı</span>
                                                        <span ng-bind="birthDayYear">-</span>
                                                    </div>
                                                </div>
                                            </li>
                                            <li class="list-group-item  my-2">
                                                <div class="row">
                                                    <div class="col d-flex flex-column">
                                                        <span class="fw-bold">Boy(cm)</span>
                                                        <span ng-bind="patientLength">-</span>
                                                    </div>
                                                    <div class="col d-flex flex-column">
                                                        <span class="fw-bold">Kilo(kg)</span>
                                                        <span ng-bind="patientWeight">-</span>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-xl-4 p-xl-2">
                        <div class="d-grid gap-3">
                            <div class="card rounded-2 shadow-sm border-0">
                                <div class="card-body">
                                    <h6 class="card-title m-3 fw-bold">Son 7 Tansiyon Ölçümünüz</h6>
                                    <div class="row">
                                        <span class="col fw-bold text-center">Tarih</span>
                                        <span class="col fw-bold text-center">Tansiyon</span>
                                        <span class="col fw-bold text-center">Nabız</span>
                                    </div>
                                    <div class="container-fluid" ng-if="bpListAvailable" ng-cloak>
                                        <div class="container-fluid py-2" style="border-bottom: 1px solid #C3C3C3" ng-repeat="item in bpList">
                                            <div class="row">
                                                <span class="col fw-bold text-center">{{ item.reportDate | changeDateLong | date:'dd MMM'}}</span>
                                                <span class="col"></span>
                                                <span class="col"></span>
                                            </div>
                                            <div class="row">
                                                <span class="col text-center">{{ item.reportDate | changeDateLong | date:'HH:mm:ss'}}</span>
                                                <span class="col text-center" ng-class="{ 'text-danger': (item | isBloodPressureWarning) }">{{item.majorValue}} / {{item.minorValue}}</span>
                                                <span class="col text-center" ng-class="{ 'text-danger': (item | isPulseWarning) }">{{item.pulseValue}}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>