<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phone Login Verify</title>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
</head>
<body>
    <div id="gCaptchaVerify" class="g-recaptcha" data-sitekey="6Lf_kHkgAAAAAMS7vJ83h_EnivP-PiFQ5yleTO5a" data-callback="onCallBack"></div>
    <script>
        function onCallBack(token) {
            console.log(token);
            AndroidRecaptcha.token(token);
        }
        function resetCaptcha() {
            if (grecaptcha) {
                grecaptcha.reset();
            }
        }
    </script>
</body>
</html>