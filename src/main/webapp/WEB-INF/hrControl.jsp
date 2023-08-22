<%@ page import="data.entity.Person" %>
<%@ page import="data.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 21-Aug-23
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>HR Control</title>
    <%@include file="/WEB-INF/HTML/navbar.html"%>
    <style>
        <%@include file="/WEB-INF/css/navbar.css"%>
        <%@include file="/WEB-INF/css/table.css"%>
        * {
            font-family: 'Poppins', sans-serif;
        }

        body {
            padding: 0;
            margin: 0;
        }

        .table-div {
            float: left; /* Float the divs to make them sit side by side */
            width: 31%; /* Set the width to one-third of the container's width (adjust as needed) */
            margin: 0 0 2% 1.75%; /* Add some margin between the divs */
            box-sizing: border-box; /* Include padding and border in the width calculation */
        }

        h1 {
            display: flex;
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

        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
        }

        /* Modal Content */
        .modal-content {
            background-color: white;
            margin: 10% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 40%;
        }

        /* Close Button */
        .close {
            color: #aaaaaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: #000;
            text-decoration: none;
            cursor: pointer;
        }

        #submit-button {
            background-color: #4070f4; /* Green background color */
            color: white; /* White text color */
            padding: 8px 16px; /* Padding around the button text */
            border: none; /* Remove the button border */
            border-radius: 4px; /* Rounded corners */
            cursor: pointer; /* Show a pointer cursor when hovering over the button */
            font-size: 16px; /* Font size */
        }

        /* Hover effect */
        #submit-button:hover {
            background-color: #3665CB; /* Darker green on hover */
        }

        /* Active effect */
        #submit-button:active {
            background-color: #2c5ba3; /* Even darker green when button is clicked */
        }
    </style>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Symbols+Outlined" />
</head>
<body>
    <div class="table-div">
        <h1>
            Birthdays
            <i class="material-icons" style="font-size:40px;color:#4070f4;">celebration</i>
        </h1>
        <table id="birthday-table">
            <thead>
            <tr>
                <th>First name</th>
                <th>Last name</th>
                <th>Turned</th>
            </tr>
            </thead>
            <tbody>
            <% Map<Person, Integer> birthdayPeople =  (Map<Person, Integer>) request.getAttribute("birthdayPeople");%>
            <% for (Person person : birthdayPeople.keySet()) { %>
            <tr>
                <td><%= person.getFirstName() %></td>
                <td><%= person.getLastName() %></td>
                <td><%= birthdayPeople.get(person) %></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <div class="table-div">
        <h1>
            Contract Expiration
            <span class="material-symbols-outlined" style="font-size:40px;color:#4070f4;">contract_edit</span>
        </h1>
        <table id="contract-expiration-table">
            <thead>
            <tr>
                <th>First name</th>
                <th>Last name</th>
                <th>Expires In</th>
            </tr>
            </thead>
            <tbody>
            <% Map<Person, Integer> contractExpirePeople =  (Map<Person, Integer>) request.getAttribute("contractExpirePeople");%>
            <% for (Person person : contractExpirePeople.keySet()) { %>
            <tr>
                <td><%= person.getFirstName() %></td>
                <td><%= person.getLastName() %></td>
                <td><%= contractExpirePeople.get(person) %></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <form action="fileUploadServlet" method="post">
        <div class="table-div">
            <h1>
                Contract Absent
                <span class="material-symbols-outlined" style="font-size:40px;color:#4070f4;">contract_delete</span>
            </h1>
            <table id="no-contract-table">
                <thead>
                <tr>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>Hire Date</th>
                </tr>
                </thead>
                <tbody>
                <% for (Person person : (List<Person>) request.getAttribute("noContractPeople")) { %>
                <tr onclick="showModal({
                        firstName: '<%= person.getFirstName()%>',
                        lastName: '<%= person.getLastName()%>',
                        id: '<%=person.getId()%>'
                        })">
                    <td><%= person.getFirstName() %></td>
                    <td><%= person.getLastName() %></td>
                    <td><%= person.getHireDate() %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </form>

    <div id="upload-contract-modal" class="modal">
        <div class="modal-content" >
            <span class="close" onclick="closeModal()">&times;</span>
            <h2>Upload a document for <span id="person-name"></span> ?</h2>
            <form action="fileUploadServlet" method="get" style="display: flex; justify-content: center">
                <input type="hidden" id="person-id" name="personIdForDocument" value="">
                <input type="submit" id="submit-button" value="Yes">
            </form>
        </div>
    </div>

    <script>
        function showModal(params) {
            // Set person information in the modal
            document.getElementById('person-name').innerText = params.firstName + " " + params.lastName;
            document.getElementById('person-id').value = params.id;
            // Show the modal
            document.getElementById('upload-contract-modal').style.display = 'block';
        }

        function closeModal() {
            document.getElementById('upload-contract-modal').style.display = 'none';
        }
    </script>
</body>
</html>
