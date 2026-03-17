<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-cookies.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-animate.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-aria.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-messages.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angular_material/1.2.1/angular-material.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/controller.js"></script>
<script>
    if ('serviceWorker' in navigator) {
        window.addEventListener('load', function () {
            navigator.serviceWorker.register('${pageContext.request.contextPath}/service-worker.js');
        });
    }
</script>
<script>
    window.$ = function (selector) { return document.querySelectorAll(selector); };
    const installButton = document.getElementById('install-button');
    let beforeInstallPromptEvent
    window.addEventListener('beforeinstallprompt', (e) => {
        e.preventDefault();
        beforeInstallPromptEvent = e;
        console.log(e);
        console.log(`'beforeinstallprompt' event was fired.`);
    });
    if(installButton)
        installButton.addEventListener("click", function() {
            if(beforeInstallPromptEvent)
                beforeInstallPromptEvent.prompt();
        });
</script>