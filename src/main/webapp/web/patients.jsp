<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_patients"  ng-init="title='Hastalar'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body ng-controller="app_patients_controller">
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
            <div class="content">
                <div class="container-fluid position-relative">
                    <div class="d-flex flex-row flex-wrap justify-content-between">
                        <div class="d-flex flex-row flex-wrap align-items-center">
                            <button type="button" class="btn btn-success m-2" data-bs-toggle="modal" data-bs-target="#filterModal">Filtrele</button>
                            <button type="button" class="btn btn-primary m-2" data-bs-toggle="modal" data-bs-target="#sendMessageModal">Mesaj Gönder</button>
                            <button type="button" class="btn btn-secondary m-2" data-bs-toggle="modal" data-bs-target="#sendSurveyModal">Anket Gönder</button>
                            <span>Hasta Sayısı: <span ng-cloak="" ng-bind="listCount"></span></span>
                        </div>
                        <div class="d-flex flex-row">
                            <input type="text" class="form-control" id="searchInput" ng-model="searchInput" placeholder="Hasta Ara">
                        </div>
                    </div>
                    <div class="overflow-auto">
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col"><input type="checkbox" class="form-check" id="allSelect"/></th>
                                <th scope="col">Kullanıcı Adı</th>
                                <th scope="col">Ad</th>
                                <th scope="col">Soyad</th>
                                <th scope="col">Telefon</th>
                                <th scope="col">Mail</th>
                                <th scope="col">Doktor Kodu</th>
                            </tr>
                            </thead>
                            <tbody id="patientList">
                            <tr ng-class="{true: 'table-primary'}[$index % 2 === 0]" ng-repeat="patient in list | filter : searchInput" ng-cloak="">
                                <th scope="row"><input class="form-check" id="rowItem" type="checkbox" name="patients" value="{{patient.userID}}"/></th>
                                <td>{{patient.tcNo}}</td>
                                <td>{{patient.name}}</td>
                                <td>{{patient.surname}}</td>
                                <td>{{patient.phone}}</td>
                                <td>{{patient.mail}}</td>
                                <td>{{patient.treatmentDoctor}}</td>
                            </tr>
                            </tbody>
                        </table>
                      <form method="POST" ng-submit="sendMessage()">
                        <div class="modal fade" id="sendMessageModal" tabindex="-1" aria-labelledby="messageModalLabel" aria-hidden="true">
                          <div class="modal-dialog">
                            <div class="modal-content">
                              <div class="modal-header">
                                <h5 class="modal-title" id="messageModalLabel">Mesaj Gönder</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                              </div>
                              <div class="modal-body">
                                  <div class="mb-3">
                                    <label for="recipient-name" class="col-form-label">Başlık:</label>
                                    <input name="title" type="text" class="form-control" id="recipient-name" ng-model="msgTitle" required>
                                  </div>
                                  <div class="mb-3">
                                    <label for="message-text" class="col-form-label">Mesaj:</label>
                                    <textarea name="message" class="form-control" id="message-text" style="max-height: 200px;" ng-model="msgBody" required></textarea>
                                  </div>
                                  <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Kapat</button>
                                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal">Gönder</button>
                                  </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </form>
                      <form method="POST" ng-submit="sendSurvey()">
                        <div class="modal fade" id="sendSurveyModal" tabindex="-1" aria-labelledby="surveyModalLabel" aria-hidden="true">
                          <div class="modal-dialog">
                            <div class="modal-content">
                              <div class="modal-header">
                                <h5 class="modal-title" id="surveyModalLabel">Anket Gönder</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                              </div>
                              <div class="modal-body">
                                  <div class="mb-3">
                                      <label for="surveyTitle" class="col-form-label">Başlık:</label>
                                      <input name="title" id="surveyTitle" type="text" class="form-control" ng-model="surveyTitle" required>
                                  </div>
                                  <div class="mb-3">
                                    <label for="surveyBody" class="col-form-label">Anket soru yapısını ekleyiniz:</label>
                                    <textarea name="surveyBody" id="surveyBody" class="form-control" style="min-height: 200px; max-height: 300px;" ng-model="surveyBody" required></textarea>
                                  </div>
                                  <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Kapat</button>
                                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal">Gönder</button>
                                  </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="filterModal" tabindex="-1" aria-labelledby="filterModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Filtre</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <h5 class="modal-title mt-2">Hasta Anketi</h5>
                <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input filters" type="radio" name="survey" value="survey#null" checked ng-click="surveyFilterClick('null')"> Hepsi
                    </label>
                </div>
                <div class="form-check">
                    <label class="form-check-label" >
                        <input class="form-check-input filters" type="radio" name="survey" value="survey#true" ng-click="surveyFilterClick('true')"> Giren
                    </label>
                </div>
                <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input filters" type="radio" name="survey" value="survey#false" ng-click="surveyFilterClick('false')"> Girmeyen
                    </label>
                </div>
                <div class="list-group" ng-show="surveyFilter">
                    <label class="list-group-item">
                      <input class="form-check-input me-1 filters" type="checkbox" value="Sigara#İçiyorum" ng-click="filterClick()"> Sigara Kullananlar
                    </label>
                    <label class="list-group-item">
                      <input class="form-check-input me-1 filters" type="checkbox" value="Şeker hastalığım#Var" ng-click="filterClick()"> Şeker Hastalığı Olanlar
                    </label>
                </div>
                <h5 class="modal-title mt-2">İlaç Kullanımı</h5>
                <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input filters" type="radio" name="userDrug" value="userDrug#null" checked ng-click="filterClick()"> Hepsi
                    </label>
                  </div>
                  <div class="form-check">
                    <label class="form-check-label" >
                        <input class="form-check-input filters" type="radio" name="userDrug" value="userDrug#true" ng-click="filterClick()"> İlaçlarını Girenler
                    </label>
                  </div>
                  <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input filters" type="radio" name="userDrug" value="userDrug#false" ng-click="filterClick()"> İlaçlarını Hiç Girmeyenler
                    </label>
                </div>
                <h5 class="modal-title mt-2" id="filterModalLabel">Tansiyon/Nabız</h5>
                <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input filters" type="radio" name="warningBP" value="warning#null" checked ng-click="filterClick()"> Hepsi
                    </label>
                  </div>
                  <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input filters" type="radio" name="warningBP" value="warning#true" ng-click="filterClick()"> Uyarısı Alanlar
                    </label>
                  </div>
                  <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input filters" type="radio" name="warningBP" value="warning#false" ng-click="filterClick()"> Uyarısı Almayanlar
                    </label>
                </div>
                <h5 class="modal-title mt-2">Doktor Kodu</h5>
                <div class="d-flex flex-row"> 
                    <label class="form-check-label m-1">
                            <input class="form-check-input category_filters" type="radio" name="doctorCode" value="" checked ng-click="categoryClick('')"> Hepsi
                    </label>
                    <label class="form-check-label m-1">
                        <input class="form-check-input category_filters" type="radio" name="doctorCode" ng-click="categoryClick('HT')"> HT
                    </label>
                    <label class="form-check-label m-1">
                        <input class="form-check-input category_filters" type="radio" name="doctorCode" ng-click="categoryClick('CD')"> CD
                    </label>
                    <label class="form-check-label m-1">
                        <input class="form-check-input category_filters" type="radio" name="doctorCode" ng-click="categoryClick('KA')"> KA
                    </label>
                    <label class="form-check-label m-1">
                        <input class="form-check-input category_filters" type="radio" name="doctorCode" ng-click="categoryClick('MI')"> MI
                    </label>
                </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Tamam</button>
            </div>
          </div>
        </div>
      </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>