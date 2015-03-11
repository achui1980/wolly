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
		<title>系统消息添加页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/jquerycommon.jsp"></jsp:include>
		
		<script type="text/javascript" src="<%=webapp %>/common/ext/daterangeAndPassword.js"></script>
		
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotMessageService.js'></script>	
		<script type='text/javascript' src='<%=webapp%>/worklog/message/js/addMessage.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<style type="text/css">
        	.red {
        		background: red;
        		margin-top: -255px;
        		margin-left: 200px;
        }
        </style>
	</head>
	<%
		String flag = request.getParameter("Flag");
		String selDate = request.getParameter("selDate");
	%>
	<body scroll=no>
		<div id="modpage"></div>
		<input type="hidden" name="basePath" id="basePath" value="<%=basePath%>" />
		<input type="hidden" name="flag" id="flag" value="<%=flag%>">
		<input type="hidden" name="selDate" id="selDate" value="<%=flag%>">
				
	</body>
</html>