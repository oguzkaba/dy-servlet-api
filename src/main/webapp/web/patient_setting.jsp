<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" ng-app="patient_app_setting" ng-init="title='Ayarlar'; userID='<%=Functions.getCookieValue(request,"userID")%>'">
<head>
    <%@include file="header/header.jsp"%>
</head>
<body class="bg-light">
    <%@include file="alert.jsp"%>
    <%@include file="header/patient_sidebar.jsp"%>
    <div class="main-panel bg-light">
        <%@include file="header/patient_header.jsp"%>
        <div class="content">
            <div class="container-fluid position-relative" ng-controller="app_setting_controller">
                <div class="d-grid gap-3 mx-auto" style="min-width: 20rem; max-width: 30rem;">
                    <div class="card">
                        <div class="card-body d-flex flex-column align-items-center justify-content-center">
                            <img class="rounded-circle" src="${pageContext.request.contextPath}/assets/img/icon/profile_default.svg" id="profilePictureImage" width="96px" height="96px" alt="" data-bs-target="#changeProfilePictureModal" data-bs-toggle="modal" style="cursor: pointer"/>
                            <h4 class="card-title mt-3" ng-bind="fullName" ng-cloak></h4>
                            <button onclick="window.location.href='${pageContext.request.contextPath}/patient/password/change'" class="btn btn-link text-decoration-none fw-bold align-self-start">Şifre değiştir</button>
                            <div class="w-100 border border-light border-start-0 border-end-0 px-3 py-2">
                                <div class="d-flex flex-row align-items-center justify-content-between">
                                    <h5 class="fs-6 fw-bold">E-Mail</h5>
                                    <button class="btn btn-link text-decoration-none fw-bold fs-6" data-bs-target="#mailModal" data-bs-toggle="modal">Değiştir</button>
                                </div>
                                <span class="w-100 fs-6" ng-bind="contact.mail">-</span>
                            </div>
                            <div class="w-100 border border-light border-start-0 border-end-0 px-3 py-2">
                                <div class="d-flex flex-row align-items-center justify-content-between">
                                    <h5 class="fs-6 fw-bold">Telefon</h5>
                                    <button class="btn btn-link text-decoration-none fw-bold fs-6" data-bs-target="#phoneModal" data-bs-toggle="modal">Değiştir</button>
                                </div>
                                <span class="w-100 fs-6" ng-bind="contact.phone">-</span>
                            </div>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-body d-flex flex-column px-4">
                            <h4 class="card-title fw-bold mt-3">Doktor Bilgisi</h4>
                            <div class="row">
                                <div class="col-5">
                                    <h5 class="w-100 text-start fs-6 fw-bold">Doktor</h5>
                                </div>
                                <div class="col-7">
                                    <span class="w-100 text-start" ng-bind="information.doctorFullName">-</span>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-5">
                                    <h5 class="w-100 text-start fs-6 fw-bold">Doktor Kodu</h5>
                                </div>
                                <div class="col-7">
                                    <span class="w-100 text-start" ng-bind="information.doctorID">-</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-body d-flex flex-column">
                            <button class="btn btn-link w-100 text-start text-decoration-none fs-6"  data-bs-toggle="modal" data-bs-target="#dialog" ng-click="includeContent('${pageContext.request.contextPath}/tr/privacy')">Gizlilik Sözleşmesi</button>
                            <button class="btn btn-link w-100 text-start text-decoration-none fs-6"  data-bs-toggle="modal" data-bs-target="#dialog" ng-click="includeContent('${pageContext.request.contextPath}/terms')">Şartlar ve Koşullar</button>
                            <button class="btn btn-link w-100 text-start text-decoration-none fs-6"  data-bs-toggle="modal" data-bs-target="#dialog" ng-click="includeContent('${pageContext.request.contextPath}/informedConsentAgreement')">Bilgilendirilmiş Onam Sözleşmesi</button>
                            <button class="btn btn-link w-100 text-start text-decoration-none fs-6"  data-bs-toggle="modal" data-bs-target="#dialog" ng-click="includeContent('${pageContext.request.contextPath}/useOfBloodPressureAndPulse')">Tanisyon ve Nabız Takibi Kullanımı</button>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="dialog" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-scrollable">
                        <div class="modal-content">
                            <div class="modal-body">
                                <ng-include src="dContent"></ng-include>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" id="closeBtn" data-bs-dismiss="modal" ng-click="removeContent()">Kapat</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="mailModal" tabindex="-1" aria-labelledby="mailModalLabel" aria-hidden="true" ng-controller="change_mail_controller">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="mailModalLabel">E-mail Güncelle</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <input name="title" type="email" class="form-control" placeholder="E-Mail" ng-model="mail">
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Kapat</button>
                                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal" ng-click="changeMail()">Güncelle</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="phoneModal" tabindex="-1" aria-labelledby="phoneModalLabel" aria-hidden="true" ng-controller="change_phone_controller">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="phoneModalLabel">Telefon Numarası Güncelle</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <input name="title" type="tel" class="form-control" placeholder="(xxx) xxx xx xx" ng-model="phone" minlength="10" maxlength="10" required>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Kapat</button>
                                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal" ng-click="changePhone()">Güncelle</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div ng-controller="change_profile_picture">
                    <div class="modal fade" id="changeProfilePictureModal" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-body">
                                    <div class="mb-3">
                                        Profil fotoğrafını güncelle
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" data-bs-toggle="modal" data-bs-target="#addImageModal">Değiştir</button>
                                        <button type="submit" class="btn btn-primary" data-bs-dismiss="modal" ng-click="removeProfilePicture()">Sil</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="addImageModal" tabindex="-1" aria-labelledby="addImageModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="addImageModalLabel">Profil Resmi Ekle</h5>
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
                                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal" ng-click="changeProfilePicture()">Ekle</button>
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