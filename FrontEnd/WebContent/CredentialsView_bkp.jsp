
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page import="main.database.communication.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<h1>View Credentials</h1>


<form action="/CollaborationTool/SFCredentials" method="POST">

	<%
	System.out.println("printing values");
	
	System.out.println(request.getParameter("system").toUpperCase());
		Connection conn = Mysqlconn.getConnection();
		ResultSet rs;
		PreparedStatement ps;
		ps = conn.prepareStatement("select * from view_cred(?)");
		ps.setString(1, request.getParameter("system").toUpperCase().toString());
		//ps.setString(1,"SALESFORCE");
		rs = ps.executeQuery();
	%>

	<table>
	
	<%  while(rs.next()){ %>
		<tr>
			<td><%=rs.getString(1) %>:</td>
			<td><%=rs.getString(2) %> </td>
		</tr>


<%} %>
<button type="button" class="btn btn-danger">Delete Account</button>
	</table>
</form>
