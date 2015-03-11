<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>包材采购分析</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 合计行 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
		
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotPackOrderService.js'></script>
		
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/order/js/orderWinForPack.js'></script>
		<script type='text/javascript' src='<%=webapp %>/order/js/packAnys.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
	String orderId = request.getParameter("orderId");
	%>
	<body onload="mask('Loading')">
		<input type="hidden" id="orderId" name="orderId" value="<%=orderId%>" />
	</body>
</html>
