<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="app_doctors"  ng-init="title='Doktorlar'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body ng-controller="app_doctors_controller">
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
                            <button type="button" class="btn btn-success m-2" onclick="window.location.href='${pageContext.request.contextPath}/admin/control/doctor/register'">Doktor Ekle</button>
                            <button type="button" class="btn btn-primary m-2 d-none" data-bs-toggle="modal" data-bs-target="#messageModal">Mesaj Gönder</button>
                            <span>Doktor Sayısı: <span ng-bind="listCount" ng-cloak=""></span></span>
                        </div>
                        <div class="d-flex flex-row">
                            <input type="text" class="form-control" id="searchInput" ng-model="searchInput" placeholder="Doktor Ara">
                        </div>
                    </div>
                    <div class="overflow-auto">
                      <table class="table">
                        <thead>
                          <tr>
                            <th scope="col"><input type="checkbox" class="form-check" id="allSelect"/></th>
                            <th scope="col">Ünvan</th>
                            <th scope="col">Ad</th>
                            <th scope="col">Soyad</th>
                            <th scope="col">Telefon</th>
                            <th scope="col">Doktor Kodu</th>
                            <th scope="col">İşlem</th>
                          </tr>
                        </thead>
                        <tbody id="doctorList">
                            <tr ng-class="{true: 'table-primary'}[$index % 2 === 0]" ng-repeat="doctor in list | filter : searchInput" ng-cloak="">
                                <th scope="row"><input class="form-check" id="rowItem" type="checkbox" name="patients" value="{{doctor.userID}}"/></th>
                                <td>{{doctor.degree}}</td>
                                <td>{{doctor.name}}</td>
                                <td>{{doctor.surname}}</td>
                                <td>{{doctor.phone}}</td>
                                <td>{{doctor.doctorID}}</td>
                                <td>
                                    <div class="d-flex">
                                        <a class="btn-scale mx-2" href="${pageContext.request.contextPath}/admin/control/doctor/edit?id={{doctor.doctorID}}"><img width="32px" height="32px" title="Düzenle" src="${pageContext.request.contextPath}/assets/img/icon/edit.svg"/></a>
                                        <a class="btn-scale mx-2 d-none" href="#"><img width="32px" height="32px" title="Sil" src="${pageContext.request.contextPath}/assets/img/icon/trash.svg"/></a>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                      </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="messageModal" tabindex="-1" aria-labelledby="messageModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="messageModalLabel">Mesaj Gönder</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="mb-3">
                                <label for="recipient-name" class="col-form-label">Başlık:</label>
                                <input type="text" class="form-control" id="recipient-name">
                            </div>
                            <div class="mb-3">
                                <label for="message-text" class="col-form-label">Mesaj:</label>
                                <textarea class="form-control" id="message-text" style="max-height: 200px;"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Kapat</button>
                        <button type="button" class="btn btn-primary">Gönder</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="header/scripts.jsp"%>
</body>
</html>