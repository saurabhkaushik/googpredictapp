<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>

<%@ page import="com.gpa.common.*" %>

<html> 
<head> 
    <link type="text/css" rel="stylesheet" href="css/main.css"/>
    <title>  Google Prediction App  </title>
</head>

<body>
<p> <h2> Google Prediction App : Configure </h2>
<p> 

<form action="predictconfig" method="post">
	<div>Google Project Name 			: <input type="text" name="PROJECT_NAME" value="<%= Constants.PROJECT_NAME %>"> </div>
	<div>Google Account Key 			: <input type="text" name="GCP_ACCOUNT_KEY" value="<%= Constants.GCP_ACCOUNT_KEY %>"> </div>
	<div>Google Account Id  			: <input type="text" name="GCP_ACCOUNT_ID" value="<%= Constants.GCP_ACCOUNT_ID %>"> </div>
	<div>Google Storage Bucket Name 	: <input type="text" name="GCP_STORAGE_BUCKET" value="<%= Constants.GCP_STORAGE_BUCKET %>"> </div>	
	<div>Training File Name 			: <input type="text" name="GCP_STORAGE_TRAINING_FILENAME" value="<%= Constants.GCP_STORAGE_TRAINING_FILENAME %>"> </div>
	<div>Testing File Name 				: <input type="text" name="GCP_STORAGE_TESTING_FILENAME" value="<%= Constants.GCP_STORAGE_TESTING_FILENAME %>"> </div>
    <div><input type="submit" value="Save"/></div>
</form>

<p> <a href="index.jsp"> Go Back </a>
</body>
</html>