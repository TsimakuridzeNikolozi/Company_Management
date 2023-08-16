<%@ page import="data.entity.Person" %>
<%@ page import="java.util.List" %>
<%@ page import="data.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 19-Jul-23
  Time: 20:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<html>
    <script>
        var userID;

        function showModal(person) {
            // Set person information in the modal
            userID = person.id;
            document.getElementById('personName').innerText = person.firstName + ' ' + person.lastName;
            document.getElementById('personId').innerText = person.idNumber;
            document.getElementById('personBirthDate').innerText = person.birthDate;
            document.getElementById('personGender').innerText = person.gender;
            document.getElementById('personSalary').value = person.salary;
            document.getElementById('personHireDate').innerText = person.hireDate;
            document.getElementById('personPosition').innerText = person.position;
            document.getElementById('personPhoneNumber').innerText = person.phoneNumber;

            // Show the modal
            document.getElementById('personModal').style.display = 'block';
        }

        function closeModal() {
            // Hide the modal
            document.getElementById('personModal').style.display = 'none';
        }

        function saveChanges() {
            var salary = document.getElementById('personSalary').value;
            var position = document.getElementById('position').value;

            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'userBranch', true);
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // Handle the response from the server
                    console.log(xhr.responseText);
                }
            };

            var params = 'salary=' + encodeURIComponent(salary) +
                '&position=' + encodeURIComponent(position) +
                '&id=' + encodeURIComponent(userID);
            console.log(params);
            xhr.send(params);
        }

        document.addEventListener('DOMContentLoaded', function() {
            var errorMessage = document.querySelector('.error');
            if (errorMessage) {
                errorMessage.addEventListener('click', function() {
                    errorMessage.style.display = 'none';
                });
            }
        });
    </script>

    <head>
        <title>My Branch</title>
        <style>
            <%@include file="/WEB-INF/css/navbar.css"%>
            <%@include file="/WEB-INF/css/table.css"%>
            @import url('https://fonts.googleapis.com/css?family=Poppins:400,500,600,700&display=swap');

            body {
                padding: 0;
                margin: 0;
            }
            /* The Modal (background) */
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
                width: 60%;
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

            /* Style for the "Save" button */
            button {
                background-color: #4070f4; /* Green background color */
                color: white; /* White text color */
                padding: 8px 16px; /* Padding around the button text */
                border: none; /* Remove the button border */
                border-radius: 4px; /* Rounded corners */
                cursor: pointer; /* Show a pointer cursor when hovering over the button */
                font-size: 16px; /* Font size */
            }

            /* Hover effect */
            button:hover {
                background-color: #3665CB; /* Darker green on hover */
            }

            /* Active effect */
            button:active {
                background-color: #2c5ba3; /* Even darker green when button is clicked */
            }


        </style>

        <%@include file="/WEB-INF/HTML/navbar.html"%>
        <script>
            var id = "nav-myBranch";
            <%@include file="/WEB-INF/javascript/navSelectItem.js"%>
        </script>
    </head>
    <body>
    <!-- The modal -->
    <div id="personModal" class="modal">
        <div class="modal-content">
            <form id="personForm">
                <span class="close" onclick="closeModal()">&times;</span>
                <h2 id="personName"></h2>
                <p>ID: <span id="personId"></span></p>
                <p>Birth Date: <span id="personBirthDate"></span></p>
                <p>Gender: <span id="personGender"></span></p>
                <p>Salary: <input type="text" id="personSalary" /></p>
                <p>Hire Date: <span id="personHireDate"></span></p>
                <p>Position: <span id="personPosition"></span>
                    <select id= "position"class="dropdown-content" name="post_name">
                        <option disabled selected value="">Choose a position</option>
                        <%
                            List<String> options = (List<String>) request.getAttribute("options");
                            if(options!=null){
                                for (String option : options) { %>
                        <option value="<%= option %>"><%= option %></option>
                        <% }} %>
                    </select>
                </p>
                <p>Phone Number: <span id="personPhoneNumber"></span></p>
            </form>
            <button onclick="saveChanges()">Save</button>
        </div>
        <% if (request.getAttribute("message") != null && !((String) request.getAttribute("message")).isEmpty()) { %>
        <div class="response">
            <%= request.getAttribute("message") %>
        </div>
        <% } %>
    </div>


    <table id="tableId">
            <thead>
            <tr>
                <th>First name</th>
                <th>Last name</th>
                <th>Post</th>
            </tr>
            </thead>
            <tbody>
                <% for (Person person : (List<Person>) request.getAttribute("userBranch")) { %>
                <tr onclick="showModal({
                        id: '<%=person.getId() %>',
                        firstName: '<%=person.getFirstName() %>',
                        lastName: '<%= person.getLastName() %>',
                        idNumber: '<%= person.getIdNumber() %>',
                        birthDate: '<%= person.getBirthDate() %>',
                        gender: '<%= person.getGender() %>',
                        salary: <%= person.getSalary() %>,
                        hireDate: '<%= person.getHireDate() %>',
                        position: '<%= person.getPost().getPostName() %>',
                        phoneNumber: '<%= person.getPhoneNumber() %>'
                        })">
                        <td><%= person.getFirstName() %></td>
                        <td><%= person.getLastName() %></td>
                        <td><%= person.getPost().getPostName() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </body>
</html>
