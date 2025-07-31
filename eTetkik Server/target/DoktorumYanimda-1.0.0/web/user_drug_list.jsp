<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_user_drug" ng-init="title='İlaçlarım'; userID='<%=Functions.getCookieValue(request,"userID")%>'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light" ng-controller="user_drug_list_controller">
<%@include file="alert.jsp"%>
<%@include file="header/patient_sidebar.jsp"%>
<%@include file="header/fullScreen.jsp"%>
<div class="main-panel bg-light">
    <%@include file="header/patient_header.jsp"%>
    <div class="content">
        <div class="container overflow-auto w-100" style="max-width: 35rem;">
            <div class="bg-white rounded-2 border-0 shadow-sm my-2" ng-repeat ="item in userDrugHistoryList" ng-cloak="">
                <div ng-switch on="item.type">
                    <div class="w-100 row" ng-switch-when="1">
                        <div class="col-10  d-flex align-items-center">
                            <span class="col-10 p-2 fs-5 align-middle" ng-bind="item.content"></span>
                        </div>
                        <a class="col-2 d-flex align-items-center justify-content-center" href="" ng-click="removeServerUserDrugItem(item.id,$index)"><img class="icon-btn mx-auto" src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" width="24px" height="24px"/> </a>
                    </div>
                    <div class="w-100 row p-2" ng-switch-default>
                        <div class="col-10 position-relative d-flex align-items-center justify-content-center">
                            <img src="" ng-src="{{item.fileSource | imgArrayToBase64}}"  style="cursor: pointer" class="mx-auto" width="200px" height="200px" id="{{$index}}_img" ng-click="fullImageScreen($index)" alt="" />
                        </div>
                        <a class="col-2 d-flex align-items-center justify-content-center" href="" ng-click="removeServerUserDrugItem(item.id,$index)"><img class="icon-btn mx-auto" src="${pageContext.request.contextPath}/assets/img/icon/trash.svg" width="24px" height="24px"/> </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="header/scripts.jsp"%>
</body>
</html>