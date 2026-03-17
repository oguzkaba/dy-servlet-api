<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_patient_complaint" ng-init="title='Şikayet Oluştur'; userID='<%=Functions.getCookieValue(request,"userID")%>'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="patient_complaint_controller">
<%@include file="alert.jsp"%>
<%@include file="header/patient_sidebar.jsp"%>
<%@include file="header/fullScreen.jsp"%>
<div class="main-panel bg-light">
    <%@include file="header/patient_header.jsp"%>
    <div class="content">
        <div class="container-fluid position-relative">
            <form ng-submit="saveComplaint()" class="mx-auto h-100" style="min-width: 18rem; max-width: 30rem">
                    <div class="bg-white rounded-2 border-0 shadow-sm w-100 py-2 my-2 px-4">
                        <md-datepicker ng-model="complaintDate" class="my-2"
                                       input-aria-describedby="datepicker-description" md-placeholder="Tarih Ekle"
                                       input-aria-labelledby="datepicker-header" md-max-date="maxDate" md-is-open="isOpenComplaintDate"></md-datepicker>
                        <div class="row  my-2">
                            <div class="col-10">
                                <input type="text" class="form-control" ng-model="complaintText" placeholder="Şikayetinizi Yazınız">
                            </div>
                            <div class="col-2">
                                <a href="" ng-click="addComplaintText()"><img class="icon-btn" src="${pageContext.request.contextPath}/assets/img/icon/blue_plus.svg" width="24px" height="24px"/></a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6 p-2">
                                <a  href="" class="w-100 btn btn-primary" data-bs-toggle="modal" data-bs-target="#complaintImageModal">Fotoğraf Ekle</a>
                            </div>
                            <div class="col-6 p-2">
                                <button type="submit" class="w-100 btn btn-primary">Gönder</button>
                            </div>
                        </div>
                    </div>
                    <h5>Şikayet İçeriği</h5>
                    <div class="container overflow-auto w-100" style="max-height: 20rem" ng-if="isAvailable" ng-cloak="">
                        <div class="bg-white rounded-2 border-0 shadow-sm my-2" ng-repeat ="item in complaintList">
                            <div ng-switch on="item.type">
                                <div class="w-100 row" style="min-height: 40px" ng-switch-when="1">
                                    <div class="col-10  d-flex align-items-center">
                                        <span class="col-10 p-2 fs-5 align-middle" ng-bind="item.content"></span>
                                    </div>
                                    <a class="col-2 d-flex align-items-center justify-content-center" href="" ng-click="removeComplaintItem(item)"><img class="icon-btn mx-auto" src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" width="24px" height="24px"/> </a>
                                </div>
                                <div class="w-100 row p-2" ng-switch-default>
                                    <div class="col-10 d-flex align-items-center justify-content-center">
                                        <img src="" ng-src="{{item.fileSource}}" class="mx-auto" width="200px" height="200px" id="{{$index}}_img" alt="" ng-click="fullImageScreen($index)" />
                                    </div>
                                    <a class="col-2 d-flex align-items-center justify-content-center" href="" ng-click="removeComplaintItem(item)"><img class="icon-btn mx-auto" src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" width="24px" height="24px"/> </a>
                                </div>
                            </div>
                        </div>
                    </div>
            </form>
            <div class="modal fade" id="complaintImageModal" tabindex="-1" aria-labelledby="complaintImageModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="complaintImageModalLabel">Şikayet Resmi Ekle</h5>
                        </div>
                        <div class="modal-body">
                            <div class="download-content">
                                <input type="file" accept="Image/*" id="imageFile">
                                <div class="content-message">
                                    <img id="imageShow" src="${pageContext.request.contextPath}/assets/img/icon/cloud-upload.svg" alt="">
                                    <span class="mt-1 fs-6 text-center">Sürükle Bırak<br>veya<br>Resim Seç</span>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Kapat</button>
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" ng-click="addComplaintImage()">Ekle</button>
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