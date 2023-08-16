<%@ page import="data.entity.PersonDocument" %>
<%@ page import="java.util.List" %>
<%@ page import="data.enums.DocumentType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File Upload</title>
    <style>
        * {
            font-family: 'Poppins', sans-serif;
        }

        body {
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background-color: #f0f0f0;
        }

        /* Style the box */
        form {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            width: 25%;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        /* Center the form elements */
        form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        /* Style the form elements */
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }

        input[type="text"],
        input[type="date"],
        select,
        input[type="file"] {
            width: 200px;
            padding: 8px;
            -webkit-box-sizing:border-box;
        }

        input[type="submit"] {
            background-color: #4070f4;
            color: #fff;
            border: none;
            padding: 12px 24px;
            border-radius: 4px;
            cursor: pointer;
            font-family: 'Helvetica', Arial, sans-serif;
            font-size: 16px;
            font-weight: bold;
        }

        input[type="submit"]:hover {
            background-color: #305bb5;
        }

        input[type="submit"]:active {
            background-color: #2c5ba3;
        }

        .error {
            background-color: #f44336;
            color: white;
            padding: 10px;
            border-radius: 4px;
            width: 300px;
            text-align: center;
            margin: 20px auto 10px;
            cursor: pointer;
        }

        .error p {
            font-size: 16px;
            font-weight: bold;
            font-family: Arial, sans-serif;
        }

        h1 {
            font-weight: 600;
            color: #333;
            text-align: center;
            margin-bottom: 20px;
            position: relative;
        }

        h1::before {
            content: '';
            position: absolute;
            bottom: 0;
            height: 3px;
            width: 28px;
            border-radius: 12px;
            background: #4070f4;
        }
    </style>
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
