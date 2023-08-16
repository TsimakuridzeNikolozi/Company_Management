<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="data.entity.Message" %>
<%@ page import="java.util.List" %>
<%@ page import="data.entity.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <title>Outbox</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <style>
        <%@include file="/WEB-INF/css/navbar.css"%>
        <%@include file="/WEB-INF/css/table.css"%>
        <%@include file="/WEB-INF/css/compose-mail-button.css"%>
        <%@include file="/WEB-INF/css/modal.css"%>
        body {
            padding: 0;
            margin: 0;
        }
    </style>

    <%@include file="/WEB-INF/HTML/navbar.html"%>
    <script>
        var id = "nav-outbox";
        var userEmail = '<%=((User)request.getSession().getAttribute("user")).getEmail()%>';
        <%@include file="/WEB-INF/javascript/navSelectItem.js"%>
        <%@include file="/WEB-INF/javascript/modalFunctions.js"%>
    </script>
</head>
<body>
<%@include file="/WEB-INF/HTML/modal.html"%>
<table>
    <thead>
    <tr>
        <th>Subject</th>
        <th>Receiver</th>
        <th>Message Type</th>
        <th>Date and Time</th>
    </tr>
    </thead>
    <tbody>
    <% List<Message> sentMessages = (List<Message>) request.getAttribute("sentMessages");
       SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
    %>
    <% for (Message message : sentMessages) { %>
    <tr onclick="showModal({
            id: '<%= message.getId().toString()%>',
            subject: '<%= message.getSubject()%>',
            type: '<%=message.getMessageType()%>',
            senderEmail: '<%= message.getSender().getEmail()%>',
            receiverEmail: '<%= message.getReceiver().getEmail()%>',
            text: '<%= message.getText()%>',
            timestamp: '<%= dateFormat.format(message.getTimestamp())%>',
            })">
        <td><%= message.getSubject() %></td>
        <td><%= message.getReceiver().getUserName() %></td>
        <td><%= message.getMessageType() %></td>
        <td><%= dateFormat.format(message.getTimestamp())%></td>
    </tr>
    <% } %>
    </tbody>
</table>

<a class="compose-mail-button" type="button" href="composeMailServlet">
    <i class="material-icons">edit</i> Compose Mail
</a>
</body>
</html>

