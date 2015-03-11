<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>应付帐款记录</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFinanceGivenService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/finace/deal/js/deal.js'></script>
		<script type='text/javascript' src='<%=webapp %>/finace/deal/js/dealDetail.js'></script>

	</head>

	<script type="text/javascript">
		//权限判断变量，对应于cot_module表中的URL字段
		var url = "cotdeal.do?method=query"; 		 
	</script>
	<%
		String cid = request.getParameter("cId");
		if (cid == null)
			cid = "";
	%>
	<body>
		
	</body>
</html>
