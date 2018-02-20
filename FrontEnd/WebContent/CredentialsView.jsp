
<%@page import="main.plugin.PluginService"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page import="main.database.communication.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<h1>Configure Plugin</h1>


<form action="/FrontEnd/updateStatus" method="GET">
	<%
		String system = request.getParameter("system");
		String status = PluginService.getStatus(request.getParameter("system"));
	%>
	<input type="hidden" value=<%=system%> name="system"> Status:<%=status%>
	<input type="hidden" name="status" value=<%=status%>>

	<%
		if (status.equalsIgnoreCase("loading")) {
	%>
	<button type="submit" name="button" disabled>Plugin is loading</button>
	<%
		}
	%>
	<%
		if (status.equalsIgnoreCase("passive")) {
	%>
	<button type="submit" name="button" disabled>Plugin is passive</button>
	<%
		}
	%>

	<%
		if (status.equalsIgnoreCase("running")) {
	%>
	<button type="submit" name="button">Stop</button>
	<%
		}
	%>
	<%
		if (status.equalsIgnoreCase("stopped")) {
	%>
	<button type="submit" name="button">start</button>
	<%
		}
	%>

</form>

