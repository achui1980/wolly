<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Picture List</title>
		<!-- 导入公共js和css -->   
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/chooser.css" />
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/customer/customer/js/orderPic.js'></script>
	
	</head>
	<%
		String cid = request.getParameter("cid");
	 %>
	<body>
		<input type="hidden" id="cid" value="<%=cid %>"/>
	</body>
</html>
