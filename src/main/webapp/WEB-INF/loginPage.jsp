<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>OOP HR</title>
    <style>
        <%@include file="/WEB-INF/css/loginPage.css"%>
    </style>
</head>
<body>

<h2>Welcome to HR</h2>

<p>Please log in.</p>


<div class="form-container">
    <!-- Add the action attribute to point to the LoginServlet -->
    <form action="login" method="post">
        <label for="username">User name:</label>
        <input type="text" id="username" name="username"><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password"><br><br>
        <input type="submit" value="Login">
    </form>
</div>


<div class="create-account">
    <a href="/createUser">Create New Account</a>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var errorMessage = document.querySelector('.error');
        if (errorMessage) {
            errorMessage.addEventListener('click', function() {
                errorMessage.style.display = 'none';
            });
        }
    });
</script>

 <!-- Show the response if available -->
 <% if (request.getAttribute("error") != null) { %>
     <div class="error">
         <%= request.getAttribute("error") %>
     </div>
 <% } %>

</body>
</html>
