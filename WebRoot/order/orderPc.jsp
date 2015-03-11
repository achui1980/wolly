<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/chooser.css" />
		<!-- 图片鼠标拖拉选择框 -->
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/DataView-more.js"></script>
		
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/order/js/orderPc.js'></script>
		<script type='text/javascript' src='<%=webapp%>/order/js/custPcToOrderPc.js'></script>
		<script type='text/javascript' src='<%=webapp%>/order/js/custPic.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
	</head>
	<%
		String orderId = request.getParameter("orderId");
		String custId = request.getParameter("custId");
		String orderNo = request.getParameter("orderNo");
	%>
	<body>
		<input type="hidden" id="orderId" value="<%=orderId%>" />
		<input type="hidden" id="custId" value="<%=custId%>" />
		<input type="hidden" id="orderNo" value="<%=orderNo%>" />
	</body>
</html>
