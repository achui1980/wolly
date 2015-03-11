<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Quotaion List</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotPriceService.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/customer/customer/js/price.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("custId");
		String busiId = request.getParameter("busiId");
	%>
	<body>	
		<input type="hidden" name="custId" id="custId" value="<%=id %>"/>	
		<input type="hidden" name="busiId" id="busiId" value="<%=busiId %>"/>
	</body>
</html>
