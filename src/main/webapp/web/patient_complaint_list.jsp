<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_patient_complaint" ng-init="title='Şikayetlerim'; userID='<%=Functions.getCookieValue(request,"userID")%>'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="patient_complaint_list_controller">
<%@include file="alert.jsp"%>
<%@include file="header/patient_sidebar.jsp"%>
<%@include file="header/fullScreen.jsp"%>
<div class="main-panel bg-light">
    <%@include file="header/patient_header.jsp"%>
    <div class="content">
        <div class="container overflow-auto w-100" style="max-width: 35rem;">
            <h5 ng-bind="title"></h5>
            <div class="bg-white rounded-2 border-0 shadow-sm my-2 py-1" ng-repeat ="item in complaintList" ng-cloak="" ng-click="complaintDetail(item,$index)">
                <div class="w-100 row px-2">
                    <span class="col-10 px-2">{{ item.date | changeDateLong | date:'dd MMM yyyy'}}</span>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="complaintDetailModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="complaintDetailLabel" aria-hidden="true" ng-cloak="">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-body">
                <p><strong>Şikayet Başlangıç Tarihi: </strong>{{detailDate | changeDateLong | date:'dd MMM yyyy'}}</p>
                <img src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" class="icon-btn" width="32px" height="32px" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#removeComplaintModal" alt="">
                <span class="fw-bold my-2 d-block">Şikayet İçeriği</span>
                <div class="bg-white rounded-2 border-0 shadow-sm my-2" ng-repeat ="item in complaintDetails" ng-cloak="">
                    <div ng-switch on="item.type">
                        <div class="w-100 row" ng-switch-when="1">
                            <div class="col-12  d-flex align-items-center">
                                <span class="col-10 p-2 fs-5 align-middle" ng-bind="item.content"></span>
                            </div>
                        </div>
                        <div class="w-100 row p-2" ng-switch-default>
                            <div class="col-12 position-relative d-flex align-items-center justify-content-center">
                                <img src="" ng-src="{{item.fileSource | imgArrayToBase64}}" style="cursor: pointer" class="mx-auto" width="200px" height="200px" id="{{$index}}_img" ng-click="fullImageScreen($index)" alt="" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="closeBtn" data-bs-dismiss="modal" ng-click="removeDetail()">Kapat</button>
            </div>
        </div>
    </div>
</div>
<div class="modal" id="removeComplaintModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Silme İşlemi</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>Şikayeti silmek istediğinizden emin misiniz ?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-dismiss="modal" ng-click="removeComplaint()">Evet</button>
                <button type="button" class="btn btn-success" data-bs-dismiss="modal">Hayır</button>
            </div>
        </div>
    </div>
</div>
<%@include file="header/scripts.jsp"%>
</body>
</html>