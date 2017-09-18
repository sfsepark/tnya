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
		<div id = 'header'>
		
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
		%><%
				LoginManager loginManager = LoginManager.getInstance();
			
				if(loginManager.isLogined(session))
				{
			%>
					<%
						out.write(loginManager.getUser(session).getUser_display_name() + " ");
					%>
					<a href = "http://tnya.kr:8080/logout">
						로그아웃
					</a>
			<%
				}
				else
				{
			%>
					<a href = "http://tnya.kr:8080/login/streamer">스트리머로 로그인</a>
					<a href = "http://tnya.kr:8080/login/user">시청자로 로그인</a>  
			<%
				}
			%>
			</div>
			<div id = 'context'>
				<center>
					<%
						out.write("<img src = \"" + (String) request.getAttribute("streamer_logo") + "\">");
						out.write((String)request.getAttribute("streamer_display_name"));
					%>
				</center>
			</div>
		<%
		}
		%>
	</div>
</body>
</html>



