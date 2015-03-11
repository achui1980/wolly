<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Production contract list</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotPriceService.js'></script>
		<script type="text/javascript"
			src="<%=webapp%>/common/js/printPanel.js"></script>
			<script type='text/javascript'
			src='<%=webapp%>/orderfac/js/OrderFacToCustWin.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/system/factory/js/orderfac.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
	</head>
	<%
		String facId = request.getParameter("facId");
	%>
	<body>
		<input type="hidden" id="facId" name="facId" value="<%=facId%>"/>
	</body>
</html>
