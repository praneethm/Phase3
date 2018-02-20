<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>

<nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
          <ul class="nav nav-pills flex-column">

            <div id="welcome"/>
          </ul>
        </nav>
        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script type="text/javascript">
        $(document).ready(function here(){
        	
        	$.get('/FrontEnd/rest/setup/list',function(responseText) {
        		var $response = $(responseText);
        		$('#welcome').html(responseText);
        		
        	});

        	
        	
        	
        });
        
        
        </script>
        <link
	href="Status.css"
	rel="stylesheet">

</body>
</html>