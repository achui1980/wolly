<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.net.InetAddress"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Database backup</title>
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<!-- MAP -->
		<script type='text/javascript'
			src='<%=webapp%>/system/bak/bak.js'></script>
	</head>
<%
	String serverIp = request.getServerName();
%>
	<body onload="">
		<input type="hidden" id="ip" value="<%=serverIp %>"/>
	</body>
</html>
