<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>客户查询表格页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFittingsService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/fittings/js/fitEleTable.js'></script>
		<script type="text/javascript" src="<%=webapp %>/commonQuery/js/eleTable.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

		<script type="text/javascript">
			
		</script>
	</head>
	<%
		String fitId = request.getParameter("fitId");
	%>
	<body>
	<div id="fitInfo"></div>
	<div id="fitTbl"></div>
	<div id="fitBtn"></div>
	<div id="eee" style="position:absolute;"></div>
		<input type="hidden" name="fitId" id="fitId" value="<%=fitId%>">
	</body>
</html>
