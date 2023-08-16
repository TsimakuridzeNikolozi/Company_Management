<%--
  Created by IntelliJ IDEA.
  User: mrtsima
  Date: 29.07.23
  Time: 23:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="data.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="data.entity.Person" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Compose Mail</title>
    <%@include file="/WEB-INF/HTML/navbar.html"%>
    <style>
        <%@include file="/WEB-INF/css/navbar.css"%>
        <%@include file="/WEB-INF/css/compose-mail-style.css"%>
    </style>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        const id = "nav-inbox";
        const parentEmail = '<%=request.getAttribute("parentEmail")%>';
        <%@include file="/WEB-INF/javascript/navSelectItem.js"%>
        <%@include file="/WEB-INF/javascript/changeEmailType.js"%>
    </script>

</head>
<body>
    <form action="composeMailServlet" method="post" onsubmit="return checkValidity()">
        <div class="email-group">
            <input type="text" id="receiverEmail" name="receiverEmail" placeholder="Receiver's Email" required>
        </div>

        <div class="email-group">
            <input type="text" id="subject" name="subject" placeholder="Subject" required>
            <div id="dropdown" class="dropdown">
                <select id="select" class="dropdown-content" name="email-type" onchange="changeEmailType()">
                    <option disabled selected value="">Email Type</option>
                    <option value="notification">Notification</option>
                    <option value="warning">Warning</option>
                    <option value="permission">Permission</option>
                </select>
            </div>
        </div>

        <textarea id="text" name="text" rows="5" maxlength="500" placeholder="Message" required></textarea><br>

        <input type="submit" value="Send">
    </form>

    <%
        Boolean flag = (Boolean) request.getAttribute("flag");
        Object errorMessageObject = request.getAttribute("error");
        String errorMessage = null;
        if (errorMessageObject != null)
            errorMessage = errorMessageObject.toString();
    %>
    <!-- Show the response if available -->
    <% if (errorMessage != null) { %>
    <div class="error" id="error">
        <script>
            var error = document.querySelector('.error');
            var flag = <%=flag%>;
            if (flag) error.style.backgroundColor = "#4CAF50";
            else error.style.backgroundColor = "#f44336";
        </script>
        <%=errorMessage%>
    </div>
    <% } %>


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
</body>
</html>

