<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
		String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ webapp + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Supplier</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotFactoryService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/system/factory/js/facEdit.js'></script>	
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body scroll=no>
		<div id="modpage"></div>
		<input type="hidden" name="basePath" id="basePath" value="<%=basePath%>" />
		<input type="hidden" name="eId" id="eId" value="<%=id%>">
	</body>
</html>