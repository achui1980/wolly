<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>排载单列表</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		
		<script type='text/javascript' src='<%=path%>/dwr/interface/cotSplitService.js'></script>
		<script type='text/javascript' src='<%=path %>/dwr/interface/setNoService.js'></script>
		<script type="text/javascript" src="<%=path %>/common/js/printPanel.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=path %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
		<script type='text/javascript' src='<%=path %>/split/js/orderOutToSplit.js'></script>
		<script type='text/javascript' src='<%=path %>/split/js/importPanel.js'></script>
		<script type='text/javascript' src='<%=path%>/split/js/split.js'></script>	
		<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
		
	</head>
<%
	String orderOutId = request.getParameter("orderId");
 %>
	<body>
	<!-- 总CBM -->
		<div  style="position: absolute;top: 5px;right: 240px;display: block" id="cbmDiv">
			<label style="font-size: 10pt;color: #10418C;font: bold">总CBM:</label>
			<label id="totalLab" style="color: red;font-size: 12;font: bold">
				0.00
			</label>
		</div>
		<input type="hidden" id="orderOutId" value="<%= orderOutId%>">
	</body>
</html>
