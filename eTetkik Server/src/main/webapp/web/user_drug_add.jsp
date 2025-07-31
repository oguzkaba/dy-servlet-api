<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_user_drug" ng-init="title='İlaç Ekle'; userID='<%=Functions.getCookieValue(request,"userID")%>'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="user_drug_controller">
<%@include file="alert.jsp"%>
<%@include file="header/patient_sidebar.jsp"%>
<%@include file="header/fullScreen.jsp"%>
<div class="main-panel bg-light">
    <%@include file="header/patient_header.jsp"%>
    <div class="content">
        <div class="container-fluid position-relative">
            <form ng-submit="saveUserDrugs()" class="mx-auto h-100" style="min-width: 18rem; max-width: 30rem">
                    <div class="bg-white rounded-2 border-0 shadow-sm w-100 py-2 my-2 px-4">
                        <div class="row">
                            <div class="row  my-2">
                                <div class="col-8">
                                    <input type="text" class="form-control" ng-model="userDrugText" placeholder="İlaç ismi giriniz">
                                </div>
                                <div class="col-4">
                                    <a class="btn btn-primary w-100" ng-click="addTextUserDrug()">Ekle</a>
                                </div>
                            </div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-6 p-1">
                                <button type="button" class="btn btn-primary w-100" data-bs-target="#userDrugImageModal" data-bs-toggle="modal">Fotoğraf Ekle</button>
                            </div>
                            <div class="col-6 p-1">
                                <button type="submit" class="btn btn-primary w-100">Gönder</button>
                            </div>
                    </div>
                </div>
                <div class="container overflow-auto w-100" style="max-height: 50vh" ng-if="isAvailable" ng-cloak="">
                    <div class="bg-white rounded-2 border-0 shadow-sm my-2" ng-repeat ="item in addUserDrugList">
                        <div ng-switch on="item.type">
                            <div class="w-100 row" style="min-height: 40px" ng-switch-when="1">
                                <div class="col-10  d-flex align-items-center">
                                    <span class="col-10 p-2 fs-5 align-middle" ng-bind="item.content"></span>
                                </div>
                                <a class="col-2 d-flex align-items-center justify-content-center" href="" ng-click="removeUserDrugItem(item)"><img class="icon-btn mx-auto" src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" width="24px" height="24px"/> </a>
                            </div>
                            <div class="w-100 row p-2" ng-switch-default>
                                <div class="col-10 position-relative d-flex align-items-center justify-content-center">
                                    <img ng-src="{{ item.fileSource}}" style="cursor: pointer" class="mx-auto" width="200px" height="200px" id="{{$index}}_img" ng-click="fullImageScreen($index)" alt="" ng-cloak=""/>
                                </div>
                                <a class="col-2 d-flex align-items-center justify-content-center" href="" ng-click="removeUserDrugItem(item)"><img class="icon-btn mx-auto" src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" width="24px" height="24px"/> </a>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <div class="modal fade" id="userDrugImageModal" tabindex="-1" aria-labelledby="userDrugImageModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="userDrugImageModalLabel">İlaç Ekle</h5>
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
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" ng-click="addImageUserDrug()">Ekle</button>
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