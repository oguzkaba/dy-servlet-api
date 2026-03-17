<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<title ng-bind="title">eTetkik</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" media="all"/>
<link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet" media="all"/>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/img/icon/favicon.ico" type="image/x-icon">
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/angular_material/1.2.1/angular-material.min.css">

<!--PWA-->
<link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
<meta name="theme-color" content="#0d6efd">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-title" content="etetkik.com">
<meta name="apple-mobile-web-app-status-bar-style" content="#0d6efd">
<link href="${pageContext.request.contextPath}/assets/img/pwa/iphone5_splash.png" media="(device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2)" rel="apple-touch-startup-image" />
<link href="${pageContext.request.contextPath}/assets/img/pwa/iphone6_splash.png" media="(device-width: 375px) and (device-height: 667px) and (-webkit-device-pixel-ratio: 2)" rel="apple-touch-startup-image" />
<link href="${pageContext.request.contextPath}/assets/img/pwa/iphone_plus_splash.png" media="(device-width: 621px) and (device-height: 1104px) and (-webkit-device-pixel-ratio: 3)" rel="apple-touch-startup-image" />
<link href="${pageContext.request.contextPath}/assets/img/pwa/iphone_x_splash.png" media="(device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3)" rel="apple-touch-startup-image" />
<link href="${pageContext.request.contextPath}/assets/img/pwa/ipad_splash.png" media="(device-width: 768px) and (device-height: 1024px) and (-webkit-device-pixel-ratio: 2)" rel="apple-touch-startup-image" />
<link href="${pageContext.request.contextPath}/assets/img/pwa/ipad_pro1_splash.png" media="(device-width: 834px) and (device-height: 1112px) and (-webkit-device-pixel-ratio: 2)" rel="apple-touch-startup-image" />
<link href="${pageContext.request.contextPath}/assets/img/pwa/ipad_pro2_splash.png" media="(device-width: 1024px) and (device-height: 1366px) and (-webkit-device-pixel-ratio: 2)" rel="apple-touch-startup-image" />
<link rel="apple-touch-icon" sizes="128x128" href="${pageContext.request.contextPath}/assets/img/pwa/128x128.png">
<link rel="apple-touch-icon-precomposed" sizes="128x128" href="${pageContext.request.contextPath}/assets/img/pwa/128x128.png">
<link rel="icon" sizes="192x192" href="${pageContext.request.contextPath}/assets/img/pwa/192x192.png">
<link rel="icon" sizes="128x128" href="${pageContext.request.contextPath}/assets/img/pwa/128x128.png">