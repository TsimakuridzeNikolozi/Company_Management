<%@ page import="data.entity.ReverseTreeNode" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.List" %>
<%@ page import="data.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<html>
    <head>
        <% List<ReverseTreeNode> data = (List<ReverseTreeNode>) request.getAttribute("data"); %>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <title>Tree</title>
        <style>
            @import url('https://fonts.googleapis.com/css?family=Poppins:400,500,600,700&display=swap');
            <%@include file="/WEB-INF/css/navbar.css"%>
            <%@include file="/WEB-INF/css/tree.css"%>
        </style>
        <%@include file="/WEB-INF/HTML/navbar.html"%>
        <script>
            var id = "nav-tree";
            <%@include file="/WEB-INF/javascript/navSelectItem.js"%>
        </script>
    </head>
    <body>
        <h1>Employee Tree</h1>
        <div id="tree"></div>
        <script>
            <%@include file="/WEB-INF/javascript/buildTree.js"%>
            <% for(ReverseTreeNode node: data) { %>
                    buildNests(<%=new Gson().toJson(node)%>, $('#tree'));
            <% } %>
        </script>
    </body>
</html>