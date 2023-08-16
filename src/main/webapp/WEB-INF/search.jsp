<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="data.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="data.entity.Person" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Page</title>
    <style>
        <%@include file="/WEB-INF/css/navbar.css"%>
        <%@include file="/WEB-INF/css/profilePage.css"%>
        <%@include file="/WEB-INF/css/search.css"%>
    </style>
</head>
<body>
<%@include file="/WEB-INF/HTML/navbar.html"%>
<div id="content">
    <div id="left-column">
        <div class="frame search-filters">
            <h2>Search Filters</h2>
            <form action="" method="post">
                <div class="editable-field">
                    <input type="text" name="firstName" placeholder="First Name">
                </div>
                <div class="editable-field">
                    <input type="text" name="lastName" placeholder="Last Name">
                </div>
                <div class="editable-field">
                    <input type="text" name="email" placeholder="Email">
                </div>
                <div class="editable-field">
                    <input type="text" name="idNumber" placeholder="ID Number">
                </div>
                <div class="editable-field">
                    <input type="text" name="phoneNumber" placeholder="Phone Number">
                </div>
                <div class="dropdown">
                    <select class="dropdown-content" name="post_name">
                        <option disabled selected value="">Choose a position</option>
                        <option value="any">No Position</option>
                        <%
                            List<String> options = (List<String>) request.getAttribute("options");
                            if (options != null) {
                                for (String option : options) { %>
                        <option value="<%= option %>"><%= option %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <button type="submit">Search</button>
            </form>
        </div>
    </div>

    <div id="right-column">
        <div class="frame">
            <h2>Search Results</h2>
            <table>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Position</th>
                    <th>Phone Number</th>
                </tr>
                <tbody>
                <% Map<Person,String> searchResult = (Map<Person,String>) request.getAttribute("searchResult"); %>
                <% if (searchResult != null) { %>
                <% for (Map.Entry<Person, String> entry : searchResult.entrySet()) { %>
                <tr>
                    <td><%= entry.getKey().getIdNumber() %></td>
                    <td><%= entry.getKey().getFirstName() %></td>
                    <td><%= entry.getKey().getLastName() %></td>
                    <td><%= entry.getValue()%></td>
                    <td><%= entry.getKey().getPost().getPostName() %></td>
                    <td><%= entry.getKey().getPhoneNumber() %></td>
                </tr>
                <% } %>
                <% } else { %>
                <tr>
                    <td colspan="5">No search results found.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
