<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>General inquiries P.I</title>
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
		
		<script type='text/javascript' src='<%=webapp %>/queryAll/js/queryOrder.js'></script>
		<script type='text/javascript' src='<%=webapp %>/queryAll/js/orderToOrderOutWin.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<body >
	
	</body>
</html>
