<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ödeme Durumu - Doktorum Yanımda</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-color: #4F46E5;
            --success-color: #10B981;
            --error-color: #EF4444;
            --bg-color: #F9FAFB;
        }

        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--bg-color);
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
            color: #111827;
        }

        .container {
            background: white;
            padding: 2.5rem;
            border-radius: 1.5rem;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            text-align: center;
            max-width: 400px;
            width: 90%;
            transition: transform 0.3s ease;
        }

        .icon-box {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.5rem;
        }

        .success .icon-box {
            background-color: #ECFDF5;
            color: var(--success-color);
        }

        .error .icon-box {
            background-color: #FEF2F2;
            color: var(--error-color);
        }

        h1 {
            font-size: 1.5rem;
            font-weight: 600;
            margin-bottom: 0.5rem;
        }

        p {
            color: #6B7280;
            line-height: 1.5;
            margin-bottom: 2rem;
        }

        .loader {
            border: 3px solid #f3f3f3;
            border-top: 3px solid var(--primary-color);
            border-radius: 50%;
            width: 24px;
            height: 24px;
            animation: spin 1s linear infinite;
            margin: 0 auto;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .status-badge {
            display: inline-block;
            padding: 0.25rem 0.75rem;
            border-radius: 9999px;
            font-size: 0.875rem;
            font-weight: 600;
            margin-top: 1rem;
        }
        
        .success .status-badge { background-color: #D1FAE5; color: #065F46; }
        .error .status-badge { background-color: #FEE2E2; color: #991B1B; }

    </style>
</head>
<body>
    <%
        String status = request.getParameter("status");
        boolean isSuccess = "success".equals(status);
    %>

    <div class="container <%= isSuccess ? "success" : "error" %>">
        <div class="icon-box">
            <% if (isSuccess) { %>
                <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>
            <% } else { %>
                <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
            <% } %>
        </div>

        <h1><%= isSuccess ? "Ödemeniz Alındı" : "Ödeme Başarısız" %></h1>
        <p>
            <% if (isSuccess) { %>
                İşleminiz başarıyla tamamlandı. Uygulamaya yönlendiriliyorsunuz...
            <% } else { %>
                <%= request.getParameter("message") != null ? request.getParameter("message") : "Ödeme işlemi gerçekleştirilemedi. Lütfen tekrar deneyiniz." %>
            <% } %>
        </p>

        <% if (isSuccess) { %>
            <div class="loader"></div>
        <% } %>

        <div class="status-badge">
            <%= isSuccess ? "BAŞARILI" : "HATA" %>
        </div>
    </div>

    <script>
        // Flutter Webview'un URL değişimini yakalaması için bir süre bekleyip gerekirse manuel yönlendirme yapılabilir.
        setTimeout(function() {
            console.log("Status: <%= status %>");
            // Eğer bir saniye sonra hala buradaysa ana sayfaya veya uygulama içine atılabilir.
        }, 3000);
    </script>
</body>
</html>
