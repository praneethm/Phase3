<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>

<nav class="col-md-2 d-none d-md-block bg-light sidebar">
	<div class="sidebar-sticky">
          <ul class="nav flex-column">
            <div id="welcome"/>
          </ul>
    </div>
        </nav>
        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script type="text/javascript">
        $(document).ready(function here(){
        	
        	$.get('/FrontEnd/rest/status/update',function(responseText) {
        		var $response = $(responseText);
        		$('#welcome').html(responseText);
        		setTimeout(here,5000);
        	});

        	
        	
        	
        });
        
        
        </script>
        <link
	href="Status.css"
	rel="stylesheet">

</body>
</html>