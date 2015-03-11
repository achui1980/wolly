<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>收款记录编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>

		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFinacerecvService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotPriceCfgService.js'></script>
			
		<!-- 锁定表格扩展 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/LockingGridView.css" />
		<script type='text/javascript' src='<%=webapp%>/common/ext/ux/LockingGridView.js'></script>
		<script type='text/javascript' src='<%=webapp %>/finace/finacerecv/js/finacerecvEdit.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<!-- MAP -->
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
		String orderFlag = request.getParameter("orderFlag");
	%>
	<body onload="mask('Loading')">
		<input type="hidden" id="pId" name="pId" value="<%=id%>" /> <!-- 收款主单号 -->
		<input type="hidden" id="orderFlag" name="orderFlag" value="<%=orderFlag%>" /> <!-- 订单出货其他费用调用添加页面 -->
	</body>
</html>