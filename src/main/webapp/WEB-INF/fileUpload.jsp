<%@ page import="data.entity.PersonDocument" %>
<%@ page import="java.util.List" %>
<%@ page import="data.enums.DocumentType" %>
<%@ page import="data.entity.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File Upload</title>
    <style>
        <%@include file="/WEB-INF/css/fileUpload.css"%>
        <%@include file="/WEB-INF/css/navbar.css"%>
        <%@include file="/WEB-INF/css/redirect-button.css"%>
    </style>

    <%@include file="/WEB-INF/HTML/navbar.html"%>
    <script>
        var id = "nav-myDocuments";
        <%@include file="/WEB-INF/javascript/navSelectItem.js"%>
    </script>
</head>
<body>
    <form action="fileUploadServlet" method="post" enctype="multipart/form-data">
        <h1>File Upload</h1>
        <label>
            Document Number:
            <input type="text" name="documentNumber" required>
        </label>
        <label>
            Document type:
            <select class="dropdown-content" name="documentType" required>
                <option disabled selected value="">Choose a document type</option>
                <%
                    List<DocumentType> options = DocumentType.getAllDocumentTypes();
                    for (DocumentType option : options) { %>
                        <option value="<%=option%>"><%=option%></option>
                    <% } %>
            </select>
        </label>
        <label>
            Issue Date:
            <input type="date" name="issueDate" required>
        </label>
        <label>
            Expiration Date:
            <input type="date" name="expirationDate" required>
        </label>
        <label>
            Select a file to upload:
            <input type="file" name="file" id="file">
        </label>
        <% if (request.getAttribute("personIdForDocument") != null) { %>
            <input type="hidden" name="personIdForDocument" value="<%=request.getAttribute("personIdForDocument")%>">
        <% } %>
        <input type="submit" value="Upload" name="submit">
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
