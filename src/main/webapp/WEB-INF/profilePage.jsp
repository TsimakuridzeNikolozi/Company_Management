<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.ActiveUser" %>
<%@ page import="data.entity.User" %>
<%@ page import="service.UserManagement" %>
<%
    ActiveUser activeUser = new ActiveUser(request);
%>
<html>
<head>
    <title>Profile Page</title>
    <style>
        <%@include file="/WEB-INF/css/navbar.css"%>
        <%@include file="/WEB-INF/css/profilePage.css"%>
        <%@include file="/WEB-INF/css/redirect-button.css"%>
    </style>
</head>
<body>
<%@include file="/WEB-INF/HTML/navbar.html"%>
<script>
    var id = "nav-profile";
    <%@include file="/WEB-INF/javascript/navSelectItem.js"%>
    <%@include file="/WEB-INF/javascript/ProfilePageFunctions.js"%>
</script>
<div id="content">
    <form class="frame" action="profilePage" method="post">
        <h2>Welcome to your profile page!</h2>
        <ul>
            <li class="editable-field">
                <span class="label">First Name</span>
                <input type="text" id="firstName" name="firstName" value="<%=activeUser.getFirstName()%>"
                       onkeyup="validateAlphabeticInput(this, firstNameError)" />
                <span class="error-message" id="firstNameError"></span>
            </li>
            <li class="editable-field">
                <span class="label">Last Name</span>
                <input type="text" id="lastName" name="lastName" value="<%=activeUser.getLastName()%>"
                       onkeyup="validateAlphabeticInput(this, lastNameError)" />
                <span class="error-message" id="lastNameError"></span>
            </li>
            <li class="editable-field">
                <span class="label">ID Number</span>
                <input type="text" id="idNumber" name="idNumber" value="<%=activeUser.getIdNumber()%>"
                       onkeyup="validateNumber(this, idNumberError)" />
                <span class="error-message" id="idNumberError"></span>
            </li>
            <li class="editable-field">
                <span class="label">Birth Date</span>
                <input type="date" id="birthDate" name="birthDate" value="<%=activeUser.getBirthDate()%>" />
            </li>
            <li>
                <span class="label">Gender</span>
                <div class="radio-group">
                    <div class="radio-option">
                        <input type="radio" id="male" name="gender" value="male" <% if (activeUser.getGender().equals("MALE")) out.print("checked"); %> />
                        <label for="male"></label>
                        <span>Male</span>
                    </div>
                    <div class="radio-option">
                        <input type="radio" id="female" name="gender" value="female" <% if (activeUser.getGender().equals("FEMALE")) out.print("checked"); %> />
                        <label for="female"></label>
                        <span>Female</span>
                    </div>
                </div>
            </li>
            <li>
                <span class="label">Salary</span>
                <span class="value"><%=activeUser.getSalary()%></span>
            </li>
            <li>
                <span class="label">Hire Date</span>
                <span class="value"><%=activeUser.getHireDate()%></span>
            </li>
            <li>
                <span class="label">Position</span>
                <span class="value"><%=activeUser.getPost()%></span>
            </li>
            <li class="editable-field">
                <span class="label">Phone Number</span>
                <input type="text" id="phoneNumber" name="phoneNumber" value="<%=activeUser.getPhoneNumber()%>"
                       onkeyup="validateNumber(this, phoneNumberError)" />
                <span class="error-message" id="phoneNumberError"></span>
            </li>
        </ul>
        <input type="submit" id="saveButton" value="Save">
    </form>

    <% if (UserManagement.isHr(((User)request.getSession().getAttribute("user")).getPerson())) {%>
    <a class="redirect-button" type="button" href="HRControl">
        <i class="material-icons">contacts</i> HR Page
    </a>
    <% } %>
</div>

<!-- Show the success message if available -->
<% if (request.getSession().getAttribute("successMessage") != null) { %>
<div class="success">
    <p><%= request.getSession().getAttribute("successMessage") %></p>
</div>
<% } %>

<!-- Show the error message if available -->
<% if (request.getSession().getAttribute("errorMessage") != null) { %>
<div class="error">
    <p><%= request.getSession().getAttribute("errorMessage") %></p>
</div>
<% } %>


<!-- JavaScript to hide error and success messages when clicked -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        var errorMessage = document.querySelector('.error-message');
        if (errorMessage) {
            errorMessage.addEventListener('click', function() {
                errorMessage.style.display = 'none';
            });
        }

        var error = document.querySelector('.error');
        if (error) {
            error.addEventListener('click', function() {
                error.style.display = 'none';
            });
        }

        var success = document.querySelector('.success');
        if (success) {
            success.addEventListener('click', function() {
                success.style.display = 'none';
            });
        }
    });
</script>
</body>
</html>