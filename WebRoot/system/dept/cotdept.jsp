<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
	//String empId = request.getSession().getAttribute("empId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Sector Management</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotDeptService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/system/dept/js/dept.js'></script>
	</head>
	<body>
	</body>
</html>