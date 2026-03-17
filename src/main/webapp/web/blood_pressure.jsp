<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_blood_pressure" ng-init="title='Tansiyon'; userID='<%=Functions.getCookieValue(request,"userID")%>'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="blood_pressure_controller">
<%@include file="alert.jsp"%>
<%@include file="header/patient_sidebar.jsp"%>
<div class="main-panel bg-light">
    <%@include file="header/patient_header.jsp"%>
    <div class="content">
        <div class="container-fluid position-relative d-flex flex-column justify-content-center">
            <h5 class="fs-4 my-4 w-100 text-center fw-bold">Tansiyon ve Nabız Girişi</h5>
            <form ng-submit="saveBloodPressure()" class="d-flex flex-column justify-content-center mx-auto h-100 bg-white rounded-2 border-0 shadow-sm py-2 px-4" style="min-width: 18rem;">
                <div class="row mb-1">
                    <div class="col-4">
                        <label for="majorValue" class="fs-6 mb-3 fw-bold align-bottom">Büyük Tansiyon</label>
                    </div>
                    <div class="col-4">
                        <label for="minorValue" class="fs-6 mb-3 fw-bold align-bottom">Küçük Tansiyon</label>
                    </div>
                    <div class="col-4">
                        <label for="pulseValue" class="fs-6 mb-3 fw-bold align-bottom">Nabız</label>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-4 px-1">
                        <input class="w-100 mw-100" type="number" ng-model="majorValue" style="height: 45px;" id="majorValue" min="0" max="250" required/>
                    </div>
                    <div class="col-4 px-1">
                        <input class="w-100 mw-100" type="number" ng-model="minorValue" style="height: 45px;" id="minorValue" min="0" max="250" required/>
                    </div>
                    <div class="col-4 px-1">
                        <input class="w-100 mw-100" type="number" ng-model="pulseValue" style="height: 45px;" id="pulseValue" min="0" max="250" required/>
                    </div>
                </div>
                <h5 class="mb-4 fs-5 text-center my-3" ng-bind="dateTime"></h5>
                <button type="submit" class="btn btn-primary mb-3 w-auto mx-auto" style="max-width: 10rem">Gönder</button>
                <a href="" class="btn btn-link text-decoration-none" ng-click="openBloodPressureHistory()">Önceki ölçümlerim</a>
            </form>
        </div >
    </div>
</div>
<div class="modal fade" id="bloodPressureHistoryModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="bloodPressureHistoryModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-body" ng-if="isAvailable">
                <div class="row">
                    <span class="col fw-bold text-center">Son 7 Gün</span>
                    <span class="col"></span>
                    <span class="col"></span>
                </div>
                <div class="row">
                    <span class="col fw-bold text-center">Ortalama</span>
                    <span class="col text-center" ng-bind="averageBP" ng-class="{ 'text-danger': (averageBP | isBloodPressureAverageWarning) }"></span>
                    <span class="col text-center" ng-bind="averagePulse" ng-class="{ 'text-danger': (averagePulse | isPulseAverageWarning) }"></span>
                </div>
                <div class="row">
                    <span class="col fw-bold text-center">Tarih</span>
                    <span class="col fw-bold text-center">Tansiyon</span>
                    <span class="col fw-bold text-center">Nabız</span>
                </div>
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
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="closeBtn" data-bs-dismiss="modal" ng-click="removeHistory()">Kapat</button>
            </div>
        </div>
    </div>
</div>
<%@include file="header/scripts.jsp"%>
</body>
</html>