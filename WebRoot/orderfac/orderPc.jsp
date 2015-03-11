<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/orderfac/js/orderPc.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
	</head>
	<%
		String orderId = request.getParameter("orderId");
	%>
	<body>
		<input type="hidden" id="orderId" value="<%=orderId%>" />
	</body>
</html>
