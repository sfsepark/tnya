<%@page import="tpacePackage.LoginManager"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>
	<meta charset = "utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />	
	<title> 트위치 챌린지  </title>
	<link rel = "stylesheet" type="text/css" href = "css/style.css?ver=5"/>
	<script src="http://code.jquery.com/jquery-1.11.0.js"></script>

</head>
<body>
	<div id = 'test'>
		<div id = 'context'>
			<%
				if(request.getAttribute("streamer_display_name") == null){
			%>
					<script type="text/javascript" charset="utf-8">
						window.location="/index.jsp";
					</script>
			<%
				}
				else
				{
			%>
					<div style="overflow:auto; width:500px; height:600px">
						
					</div>
			<%
				}
			%>

		</div>
	</div>
</body>
</html>



