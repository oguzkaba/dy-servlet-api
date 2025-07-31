<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="header bg-primary d-flex align-items-center justify-content-between" ng-controller="header_controller">
    <div class="d-flex justify-content-start">
        <div class="header-logo d-block d-md-none">
            <a class="color-white p-2 fs-5" href="#"><img src="${pageContext.request.contextPath}/assets/img/icon/logo_white.svg" width="32px" height="32px"/>eTakip</a>
        </div>
    </div>
    <div class="header-content d-flex">
        <a class="p-2 icon-btn position-relative" href="" ng-click="$parent.notificationOpen()">
            <img src="${pageContext.request.contextPath}/assets/img/icon/notification_white.svg" data-count="0" width="24px" height="24px" alt=""/>
            <span class="number-icon-btn" ng-if="showNotificationCount" ng-bind="notificationCount" ng-cloak></span>
        </a>
        <a class="p-2 icon-btn position-relative" href=""  ng-click="$parent.messagesOpen()">
            <img src="${pageContext.request.contextPath}/assets/img/icon/mail_white.svg" data-count="0" width="24px" height="24px" alt="">
            <span class="number-icon-btn" ng-if="showMessageCount" ng-bind="messageCount" ng-cloak></span>
        </a>
        <a class="p-2 icon-btn" href="" data-bs-toggle="modal" data-bs-target="#logoutModal"><img src="${pageContext.request.contextPath}/assets/img/icon/logout.svg" width="24px" height="24px" alt=""></a>
        <a id="menuBtn" class="p-2 icon-btn" href="javascript:openCloseMenu()"><img src="${pageContext.request.contextPath}/assets/img/icon/menu.svg" width="24px" height="24px" alt=""></a>
    </div>
    <div class="offcanvas offcanvas-end" tabindex="-1" id="notificationLayout" aria-labelledby="notificationLabel">
        <div class="offcanvas-header">
            <h5 id="notificationLabel">Bildirimler</h5>
            <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body" style="overflow-y: auto">
            <div class="container" ng-repeat="item in $parent.notificationList" ng-click="openNotificationItem(item)" style="cursor: pointer">
                <div class="row">
                    <div class="col-3 d-flex align-items-center justify-content-center px-1">
                        <div class="message-icon">
                            <img src="${pageContext.request.contextPath}/assets/img/icon/notification_blue.svg" width="24px" height="24px" alt="">
                        </div>
                    </div>
                    <div class="col-9 px-2">
                        <div class="row">
                            <span class="col-12 fw-bold" ng-bind="item.title" ng-class="{'text-secondary': item.isRead}"></span>
                            <span class="col-12 fs-6" ng-bind="item.body | textAbbreviation" ng-class="{'text-secondary': item.isRead}"></span>
                            <span class="col-12" ng-bind="item.createDate | changeDateLong | date:'dd MMM yyyy HH:mm:ss'" ng-class="{'text-secondary': item.isRead}"></span>
                        </div>
                    </div>
                </div>
                <div class="row bg-secondary mt-3" style="height: 1px"></div>
            </div>
        </div>
    </div>
    <div class="offcanvas offcanvas-end" tabindex="-1" id="messagesLayout" aria-labelledby="messagesLabel">
        <div class="offcanvas-header">
            <h5 id="messagesLabel">Mesajlar</h5>
            <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body" style="overflow-y: auto">
            <div class="container" ng-repeat="item in $parent.messageList" style="cursor: pointer">
                <div class="row">
                    <div class="col-3 d-flex align-items-center justify-content-center px-1">
                        <div class="message-icon">
                            <img src="${pageContext.request.contextPath}/assets/img/icon/mail_blue.svg" width="24px" height="24px" alt="">
                        </div>
                    </div>
                    <div class="col-9 px-2">
                        <div class="row">
                            <span class="col-12 fw-bold" ng-bind="item.messageTitle"  ng-class="{'text-secondary': item.isRead}"></span>
                            <span class="col-12 fs-6" ng-bind="item.lastMessageContent | textAbbreviation" ng-class="{'text-secondary': item.isRead}"></span>
                            <span class="col-12" ng-bind="item.lastUpdateDate | changeDateLong | date:'dd MMM yyyy HH:mm:ss'" ng-class="{'text-secondary': item.isRead}"></span>
                        </div>
                    </div>
                </div>
                <div class="row bg-secondary mt-3" style="height: 1px"></div>
            </div>
        </div>
    </div>
    <div class="notification-modal" ng-if="$parent.notificationShow" ng-cloak>
        <div class="notification-dialog">
            <div class="notification-icon">
                <img src="${pageContext.request.contextPath}/assets/img/icon/notification_red.svg" ng-if="($parent.notificationTitle | isWarningTitle)" alt=""/>
                <img src="${pageContext.request.contextPath}/assets/img/icon/notification_blue.svg" alt="" ng-if="!($parent.notificationTitle | isWarningTitle)"/>
            </div>
            <div class="notification-header mt-3">
                <h5 ng-bind="$parent.notificationTitle"></h5>
            </div>
            <div class="notification-body">
                <p ng-bind="$parent.notificationBody"></p>
            </div>
            <div class="notification-footer">
                <button class="btn btn-primary" ng-click="$parent.closeNotificationItem()">Anladım</button>
            </div>
        </div>
    </div>
</div>