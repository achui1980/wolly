<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>送样单列表</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		
		<script type='text/javascript' src='<%=path%>/dwr/interface/cotGivenService.js'></script>
		<script type='text/javascript' src='<%=path%>/dwr/interface/cotSignService.js'></script>
		<script type='text/javascript' src='<%=path%>/given/js/givenGrid.js'></script>	
		<script type="text/javascript" src="<%=path %>/sign/sign/js/printPanelSign.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/printPanel.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
		
	</head>

	<body>
	</body>
</html>
