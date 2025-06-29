<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LOGIN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div class="login-container">
    <div class="login-header">
        <h1>Accedi</h1>
        <p>Inserisci le tue credenziali per continuare</p>
    </div>

    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <div class="error-message">
        <%= errorMessage %>
    </div>
    <%
        }
    %>

    <form method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text"
                   id="username"
                   name="username"
                   required
                   placeholder="Inserisci il tuo username"
                   value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>">
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password"
                   id="password"
                   name="password"
                   required
                   placeholder="Inserisci la tua password">
        </div>

        <button type="submit" class="btn-login">
            Accedi
        </button>
    </form>
</div>
</body>
</html>