<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Quotations from suppliers</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='dwr/interface/cotPriceFacService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/system/factory/js/cotpricefac.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("factoryId");
	 %>
	<body>
	
		<input type="hidden" id="factoryId" name="factoryId" value="<%=id %>"/>
	</body>
</html>
