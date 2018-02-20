<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<title>SELECT Operation</title>
<style>
h1 {
	text-align: center;
}
</style>
<link
	href="http://getbootstrap.com/docs/4.0/examples/dashboard/dashboard.css"
	rel="stylesheet">
</head>
<% String var=request.getParameter("system"); %>

<body>

<h1><%=var %></h1>

	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-6">
				<jsp:include page="CredentialsEdit.jsp" >
				 <jsp:param name="system" value="${param.system}"/>
				</jsp:include>
			</div>
			<div class="col-xs-6">

				<jsp:include page="CredentialsView.jsp" >
				<jsp:param name="system" value="${param.system}"/>
				</jsp:include>
			</div>
		</div>

	</div>
</html>