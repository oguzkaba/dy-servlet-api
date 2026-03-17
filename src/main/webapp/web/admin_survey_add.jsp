<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="admin_survey_add_app" ng-init="title='Anket Ekle'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body ng-controller="admin_survey_add_controller">
    <%@include file="alert.jsp"%>
    <div class="wrapper">
      <%@include file="header/admin_sidebar.jsp"%>
      <div class="main-panel bg-white">
        <div class="header bg-primary d-flex align-items-center justify-content-between">
          <div class="d-flex justify-content-start">
            <div class="header-logo d-block d-md-none">
              <a class="color-white p-2 fs-5" href="#"><img src="${pageContext.request.contextPath}/assets/img/icon/logo_white.svg" width="32px" height="32px"/>eTakip</a>
            </div>
          </div>
          <div class="header-content d-flex">
            <a class="p-2 icon-btn" href="" data-bs-toggle="modal" data-bs-target="#logoutModal"><img src="${pageContext.request.contextPath}/assets/img/icon/logout.svg" width="24px" height="24px" alt=""></a>
            <a id="menuBtn" class="p-2 icon-btn" href="javascript:openCloseMenu()"><img src="${pageContext.request.contextPath}/assets/img/icon/menu.svg" width="24px" height="24px" alt=""></a>
          </div>
        </div>
            <div class="content d-flex align-items-lg-center align-items-start justify-content-center">
                <div class="container-fluid position-relative w-auto">
                  <form ng-submit="addSurvey()" method="post" class="d-grid gap-3 my-4" style="min-width: 20rem; width: 100%; max-width: 37rem;">
                    <h5 class="fs-3 mb-4 text-center">Anket Ekle</h5>
                    <div class="mb-3">
                      <label for="surveyCode" class="col-form-label">Anket Kodu:</label>
                      <input name="title" id="surveyCode" type="text" class="form-control" ng-model="surveyCode" required>
                    </div>
                    <div class="mb-3">
                      <label for="surveyBody" class="col-form-label">Anket İçeriği:</label>
                      <textarea name="message" id="surveyBody" class="form-control" style="min-height: 200px; max-height: 37rem;" ng-model="surveyBody" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal">EKLE</button>
                  </form>
                </div>
            </div>
        </div>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>