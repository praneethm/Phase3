

<%@page import="main.core.MasterBean"%>
<h1>Edit Credentials</h1>

<!-- <div id="CredentialEdit"></div> -->
<form action="/FrontEnd/update" method="get">

	<table>
		<%
			MasterBean mb = new MasterBean();

			for (String string : mb.getHolder().get(request.getParameter("system")).keySet()) {
				if(!string.equalsIgnoreCase("OFFSET")){
				if ((null != mb.getHolder().get(request.getParameter("system")).get(string))) {
		%>

		<%
			if (string.equalsIgnoreCase("CODE")) {
		%>
		<tr>
			<td>Disable Offset:</td>
			<td><input type="checkbox" name="OFFSET" id="OFFSET"
				value="true" checked></td>
		</tr>
		<tr>
			<td>Select Cash Account:</td>
			<td><select class="quantity" id="CODE" name="CODE"><option
						value=<%=mb.getHolder().get(request.getParameter("system")).get(string) %>><%=mb.getHolder().get(request.getParameter("system")).get(string) %></option></select></td>
		</tr>
		<%
			}else if(string.equalsIgnoreCase("USEMIPONLINE")){ %>
		<tr>
			<td>Use MIP Online Version:</td>
			<td><input type="checkbox" checked name="USEMIPONLINE"
				id="USEMIPONLINE" value="true"></td>
		</tr>

		<%
			} else {
		%>
		<tr>
			<td><%=string%> :</td>
			<td><input type="text"
				value="<%=mb.getHolder().get(request.getParameter("system")).get(string)%>"
				name="<%=string%>" id="<%=string%>" /></td>
			<%
				}
			%>
		</tr>
		<%
			} else {
				if(string.equalsIgnoreCase("CODE")){
				
		%>
		<tr>
			<td>Disable Offset:</td>
			<td><input type="checkbox" name="OFFSET" id="OFFSET"
				value="false"></td>
		</tr>
		<tr>
			<td>Select Cash Account:</td>
			<td><select class="quantity" id="CODE" name="CODE"
				disabled="disabled"><option value="11000">Disable offset for selection</option></select></td>
		</tr>
		<%
			}else if(string.equalsIgnoreCase("UseMIPOnline")){ %>
		<tr>
			<td>Use MIP Online Version:</td>
			<td><input type="checkbox" name="USEMIPONLINE" id="USEMIPONLINE"
				value="true"></td>
		</tr>

		<%} else {%>
		<tr>
			<td><%=string%></td>
			<td><input type="text" name=<%=string%> id="<%=string%>" /></td>
		</tr>
		<%
		}
			}
			}
			}
		%>
		<tr>
			<td><input type="hidden" name="system" value="${param.system}" /></td>
		</tr>
	</table>
	<input type="submit" value="Submit" onclick="update(event)" />
</form>
<link href="NewFile.css" rel="stylesheet">
<script type="text/javascript">



$( document ).ready(function() {
	$.get('http://localhost:8080/FrontEnd/rest/CredentialEdit/v1', {
		system : '${param.system}'
	}, function(responseText) {
		var $response = $(responseText);
		$('#CredentialEdit').html(responseText);
		offset();
		miponline();
	});

});



function offset(){
var $checkBox = $('#OFFSET');
$select = $('#CODE');
<%-- Condition for mip- logic for code check box to pull mip Cash accounts from MIP  --%>
$checkBox.on('change',function(e){
		if ($(this).is(':checked')){
		$.get('/FrontEnd/Codevalues', {	
		system :"${param.system}"},
function(responseText){
	$('#CODE').html(responseText);
	$(".overlays").show();
	alert(responseText);
	$select.removeAttr('disabled');
	$checkBox.attr('value','true');
	$(".overlays").hide();
});
}else{
$select.attr('disabled','disabled');
$checkBox.attr('value','false');
$('#CODE').html("<select class='quantity' id='CODE' name='CODE' disabled='disabled'><option value='<%=mb.getHolder().get(request.getParameter("system")).get("CODE") %>' ><%=mb.getHolder().get(request.getParameter("system")).get("CODE") %></option></select></td>"); 
}
});
}




<%-- this is condition for MIP to switch between online and cloud --%>
function miponline(){
var $checkBox1 = $('#USEMIPONLINE');
$select1 = $('#MIPINSTANCEURL');
$checkBox1.on('change',function(e){
if ($(this).is(':checked')){
	alert("Using MIP Online version");
	$checkBox1.attr('value','true');
$select1.attr('readonly','readonly');
}else{
	alert("Please enter MIPINSTANCEURL");
	$checkBox1.attr('value','false');
$select1.removeAttr('readonly');
}
});

}
function update(e){
	alert("submitting form");
	var data = $('form').serialize();
	$.get('/FrontEnd/update', data);
	e.preventDefault();
}


</script>







