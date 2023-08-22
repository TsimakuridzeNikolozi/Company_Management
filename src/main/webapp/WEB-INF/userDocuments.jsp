<%@ page import="data.entity.PersonDocument" %>
<%@ page import="java.util.List" %>
<%@ page import="data.entity.User" %>
<%@ page import="java.util.HashMap" %>

<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 16-Aug-23
  Time: 18:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Documents</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <style>
        <%@include file="/WEB-INF/css/navbar.css"%>
        <%@include file="/WEB-INF/css/table.css"%>
        <%@include file="/WEB-INF/css/redirect-button.css"%>
        body {
            padding: 0;
            margin: 0;
        }

        .error {
            position: absolute;
            background-color: #f44336;
            color: white;
            padding: 10px;
            border-radius: 4px;
            width: 300px;
            text-align: center;
            margin: 20px auto 10px;
            cursor: pointer;
            top: 6%;
            left: 50%;
            transform: translateX(-50%);
        }

        .error p {
            font-size: 16px;
            font-weight: bold;
            font-family: Arial, sans-serif;
        }
    </style>

    <%@include file="/WEB-INF/HTML/navbar.html"%>
    <script>
        var id = "nav-myDocuments";
        <%@include file="/WEB-INF/javascript/navSelectItem.js"%>
    </script>
</head>
<body>
<form action="userDocumentsServlet" method="post" id="documentForm">
    <table>
        <thead>
        <tr>
            <th>Document Number</th>
            <th>Issue Date</th>
            <th>Expiration Date</th>
            <th>Document Type</th>
        </tr>
        </thead>
        <tbody>
        <% List<PersonDocument> userDocuments = (List<PersonDocument>) request.getAttribute("userDocuments");%>
        <% for (PersonDocument document : userDocuments) { %>
        <input type="hidden" name="documentId" value="" id="clickedDocumentId">
        <tr onclick="submitForm({id: '<%=document.getId()%>'})">
            <td><%= document.getDocumentNumber() %></td>
            <td><%= document.getIssueDate() %></td>
            <td><%= document.getExpirationDate() %></td>
            <td><%= document.getDocumentType() %></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</form>


<a class="redirect-button" type="button" href="fileUploadServlet">
    <i class="material-icons">upload_file</i> Upload File
</a>

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
    function submitForm(clickedDocument) {
        document.getElementById('clickedDocumentId').value = clickedDocument.id;
        document.getElementById('documentForm').submit()
    }

    // Adding event listener for the result messages
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
