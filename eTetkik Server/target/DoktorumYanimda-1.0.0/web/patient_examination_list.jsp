<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_patient_examination" ng-init="title='Tetkiklerim'; userID='<%=Functions.getCookieValue(request,"userID")%>'" ng-cloak="">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="examination_list_controller">
<%@include file="alert.jsp"%>
<%@include file="header/patient_sidebar.jsp"%>
<%@include file="header/fullScreen.jsp"%>
<div class="main-panel bg-light">
    <%@include file="header/patient_header.jsp"%>
    <div class="content">
        <div class="container overflow-auto w-100" style="max-width: 35rem;">
            <h5>Tetkiklerim</h5>
            <div class="bg-white rounded-2 border-0 shadow-sm my-2 py-1" ng-repeat ="item in examinationList" ng-click="examinationDetail(item)">
                <div class="w-100 row px-2">
                    <p class="col-10 px-2"><strong>Tetkik ismi: </strong>{{item.name}}</p>
                </div>
                <div class="w-100 row px-2">
                    <span class="col-10 px-2"><strong>Tarih: </strong>{{ item.date | changeDateLong | date:'dd MMM yyyy'}}</span>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="examinationDetail" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="examinationDetailLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-body">
                <p><strong>Tetkik İsmi: </strong>{{detailName}}</p>
                <p><strong>Tarih: </strong>{{detailDate | changeDateLong | date:'dd MMM yyyy'}}}</p>
                <img src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" class="icon-btn" width="32px" height="32px" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#removeExaminationModal" alt="">
                <span class="fw-bold my-2 d-block">Tetkik Resimleri</span>
                <div class="bg-white rounded-2 border-0 shadow-sm my-2" ng-repeat ="item in imageFileList">
                    <div class="w-100 row p-2">
                        <div class="col-12 position-relative d-flex align-items-center justify-content-center">
                            <img src="" ng-src="{{item.fileSource | imgArrayToBase64}}" style="cursor: pointer" class="mx-auto" width="200px" height="200px" id="{{$index}}_img" ng-click="fullImageScreen($index)" alt="" />
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
<div class="modal" id="removeExaminationModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Silme İşlemi</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>Tetkiği silmek istediğinizden emin misiniz ?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-dismiss="modal" ng-click="removeExamination()">Evet</button>
                <button type="button" class="btn btn-success" data-bs-dismiss="modal">Hayır</button>
            </div>
        </div>
    </div>
</div>
<%@include file="header/scripts.jsp"%>
</body>
</html>