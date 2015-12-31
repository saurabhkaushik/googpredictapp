<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="css/main.css"/>
        <title> Google Prediction App : Results </title>
</head>

<body>
<p> <h2> Google Prediction App </h2>

<p>Message : <%= request.getAttribute("message") %>
<p>Model Id : <%= request.getAttribute("modelid") %>
<p>Results :  <%= request.getAttribute("results") %>

<p> <a href="index.jsp"> Go Back </a>
</body>
</html>