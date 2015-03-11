<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>报价记录编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFittingsService.js'></script>	
		<!-- 导入js -->
		<script type='text/javascript' src='<%=webapp %>/fittings/js/hisPriceModPage.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
		String fitId = request.getParameter("fitId");
		String fitName = request.getParameter("fitName");
	%>
	<script type="text/javascript"></script>
	<body>
	<div id="priceInfo"></div>
		<input type="hidden" id="pId" name="pId" value="<%=id%>" />
		<input type="hidden" id="fittingname" name="fit" value="<%=fitName%>" />
		<input type="hidden" id="fitkey" name="fit" value="<%=fitId%>" />
	</body>
</html>
