<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Order List</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/customer/customer/js/order.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
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
