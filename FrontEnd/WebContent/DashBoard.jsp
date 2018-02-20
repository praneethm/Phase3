<%@page import="main.core.MasterBean"%>
<%@page import="main.core.Dashboard"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="main.database.communication.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<!-- Bootstrap core CSS -->
  <link href="https://getbootstrap.com/dist/css/bootstrap.min.css"
	rel="stylesheet"> 
 <link href="css/dashboard_bkp.css" rel="stylesheet"> 

<title>MIP Connector</title>



</head>

<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
<a class="navbar-brand" href="DashBoard.jsp">Dashboard</a>
<button class="navbar-toggler" type="button" data-toggle="collapse"
	data-target="#navbarsExampleDefault"
	aria-controls="navbarsExampleDefault" aria-expanded="false"
	aria-label="Toggle navigation">
	<span class="navbar-toggler-icon"></span>
</button>

<div class="collapse navbar-collapse" id="navbarsExampleDefault">
	<ul class="navbar-nav mr-auto">
		<li class="nav-item active"><a class="nav-link" href="#">Edit
				User Details <span class="sr-only">(current)</span>

				<li class="nav-item dropdown" disabled="disabled"><a
					class="nav-link dropdown-toggle" href="http://example.com"
					id="dropdown01" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false">End Applications</a>
					<div class="dropdown-menu" aria-labelledby="dropdown01">


						<%
							Dashboard dashboard = new Dashboard();
							MasterBean mb = new MasterBean();
							mb.getSystems();
						%>

						<%=dashboard.getApplications()%>

					</div></li>
	</ul>

</div>
</nav>
<div class="container-fluid">
	<div class="row">
		<%
			if (dashboard.isSetupDone()) {
		%>
		<jsp:include page="ActionBar.jsp"></jsp:include>
		<main class="col-sm-9 ml-sm-auto col-md-10 pt-3" role="main">
		<div id="welcometext"></div>

		<%
			} else {
		%> <jsp:forward page="/setup" /> <%
 	}
 %>
		
	</div>
</div>




<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
	integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
	crossorigin="anonymous"></script>
<script>
	window.jQuery
			|| document
					.write('<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"><\/script>')
</script>

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script
	src="http://getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js"></script>
<script src="http://code.jquery.com/jquery-latest.js">
	
</script>
<script src="http://malsup.github.com/jquery.form.js"></script>
<script src="http://getbootstrap.com/assets/js/vendor/popper.min.js"></script>
<script src="http://getbootstrap.com/dist/js/bootstrap.min.js"></script>
<script>
	/* 	$(document).ready(function() {

	 $('.dropdown-ite').click(function(event) {
	 $.get('Credentials.jsp', function(responseText) {
	 var $response = $(responseText);
	 $('#welcometext').html(responseText);
	 });
	 });
	 }); */
</script>
<script type="text/javascript">
	function here(obj) {

		$.get('Credentials.jsp', {
			system : $(obj).text()
		}, function(responseText) {
			var $response = $(responseText);
			$('#welcometext').html(responseText);
		});

	}
	function status(obj) {

		$.get('/FrontEnd/rest/status/details', {
			system : $(obj).text()
		}, function(responseText) {
			var $response = $(responseText);
			$('#welcometext').html(responseText);
		});

	}

	/* 	$(document).ready(function() {
	
	 $.get('/FrontEnd/rest/setupCredentials/list', function(responseText) {
	 var $response = $(responseText);
	 $('#welcometext').html(responseText);
	 });

	 }); 
	 function display(obj) {

	 $.get('/FrontEnd/rest/setupCredentials/list', function(responseText) {
	 var $response = $(responseText);
	 $('#welcometext').html(responseText);
	 });



	 }
	 */
</script>


</body>
</html>