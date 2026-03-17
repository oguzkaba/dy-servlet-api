<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>reCAPTCHA demo: Simple page</title>
    <script src="https://www.google.com/recaptcha/api.js?onLoad=onLoadCallBack&render=6LcjO2cgAAAAALq4jIPr_8ZLzTe2R2xi4GpEx5mD"></script>
    <script type="text/javascript" defer>
        function botControl(){
            grecaptcha.ready(() => {
                grecaptcha.execute('6LcjO2cgAAAAALq4jIPr_8ZLzTe2R2xi4GpEx5mD', {
                    action: 'submit'
                }).then(function(token) {
                    if (token && token !== "") {
                        console.log(token);
                        AndroidRecaptcha.token(token);
                    }
                });

            });
        }
    </script>
</head>
<body>
</body>
</html>
