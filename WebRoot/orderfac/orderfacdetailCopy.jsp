<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ webapp + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Purchase Order edit page</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>

		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript'
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/sysdicutil.js'></script>
		<!-- 导入ECTable -->
		<script type='text/javascript' src='<%=webapp %>/orderfac/js/orderfacdetailCopy.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
		String orderId = request.getParameter("oId");
	%>
	<body onload="mask('Loading')">
	<!-------------------------- 总金额 -------------------------->
		<input type="hidden" id="pId" name="pId" value="<%=id%>" />
		<input type="hidden" id="orderId" name="orderId" value="<%=orderId%>" />
	</body>
</html>

