<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/queryAll/js/fileManage.js'></script>
		<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
	</head>
	<body>
	</body>
</html>
