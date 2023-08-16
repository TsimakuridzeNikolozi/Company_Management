<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: kh0kho
  Date: 07.07.23
  Time: 23:24
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<style>
    <%@ include file="/WEB-INF/css/createUser.css"%>
</style>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const emailInput = document.getElementById("email");
        const emailError = document.getElementById("email-error");

        emailInput.addEventListener("input", function() {
            const email = emailInput.value.trim().toLowerCase();
            const domain = "@oop_hr.com";

            if (email.endsWith(domain)) {
                emailError.style.display = "none";
            } else {
                emailError.style.display = "inline";
            }
        });
    });
</script>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> Registration </title>
</head>
<body>
<div class="wrapper">
    <h2>Registration</h2>
    <form method="post" action="createUser">
        <div class="input-box">
            <input type="text"  id="first_name" name="first_name"  placeholder="Enter your first name" required>
        </div>
        <div class="input-box">
            <input type="text"  id="last_name" name="last_name"  placeholder="Enter your last name" required>
        </div>
        <div class="input-box">
            <input type="text"  id="username" name="username"  placeholder="Enter your username" required>
        </div>
        <div class="input-box">
            <input type="text" id="email" name="email" placeholder="Enter your email" required>
            <span class="error-message" id="email-error">Email must end with @oop_hr.com</span>
        </div>
        <div class="input-box">
            <input type="password"  id="password"  name="password" placeholder="Create password" required>
        </div>
<%--        <div class="input-box">--%>
<%--            <input type="text" id="id_number" name="id_number" placeholder="Enter your ID number" pattern="^\d{11}$" title="ID number must be exactly 11 digits" required>--%>
<%--        </div>--%>

        <div class="dropdown">
            <select class="dropdown-content" name="post_name">
                <option disabled selected value="">Choose a position</option>
                <%
                    List<String> options = (List<String>) request.getAttribute("options");
                    if(options!=null){
                    for (String option : options) { %>
                <option value="<%= option %>"><%= option %></option>
                <% }} %>
            </select>
        </div>

        <div class="input-box button">
            <input type="Submit" value="Register Now">
        </div>

        <div class="text">
            <h3>Already have an account? <a href="/">Login now</a></h3>
        </div>
    </form>

    <!-- Show the response if available -->
    <% if (request.getAttribute("response") != null) { %>
    <div class="response">
        <%= request.getAttribute("response") %>
    </div>
    <% } %>
</div>
</body>
</html>


