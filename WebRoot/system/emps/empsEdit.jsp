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
		<title>Staff Edit page</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type="text/javascript">
		Ext.namespace("QH.controls")
		</script>
		<script type="text/javascript" src="mail/common/mailConstants.js"></script>
		<!-- 导入dwr -->
		<script type="text/javascript" src="dwr/interface/mailCfgService.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotEmpsService.js'></script>	
		<script type='text/javascript' src='<%=webapp %>/system/emps/js/CheckBoxModule.js'></script>
		<script type='text/javascript' src='<%=webapp %>/system/emps/js/empsEdit.js'></script>
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
