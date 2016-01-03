<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>


<html>
<head>
    <link type="text/css" rel="stylesheet" href="css/main.css"/>
    <title>  Google Prediction App : Main </title>	
</head>

<body>
<p> <h2> Google Prediction App </h2>
<p> 

<p> <a href="configure.jsp"> Configure </a>

<form action="predicttrain" method="get">
	<div>Model Id : <input type="text" name="modelid"> </div>
    <div><input type="submit" value="Train"/></div>
</form>

<form action="predictstatus" method="get">
	<div>Model Id : <input type="text" name="modelid"> </div>
    <div><input type="submit" value="Status"/></div>
</form>

<form action="predict" method="get">
	<div>Model Id : <input type="text" name="modelid"> </div>
    <div><input type="submit" value="Predict"/></div>
</form>

<p> <a href="index.jsp"> Go Back </a>

</body>
</html>