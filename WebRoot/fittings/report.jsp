<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>样品列表</title>
	</head>
	<jsp:include page="/extcommon.jsp"></jsp:include>
	<!-- 导入dwr -->
	<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
	<script type='text/javascript' src='dwr/interface/cotFittingsService.js'></script>
	<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
	<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotFittingsService.js'></script>	
	<script type='text/javascript' src='<%=webapp %>/fittings/js/report.js'></script>
	<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	
	<body style="background-color: #FBF8D5;margin:0px;">
	<div>
		<div id="uploadTable" style="float: left;width: 50%">	</div>
		<div id="msgTable" style="float: left;width: 47%;margin-top: 20;display: block">
			sdfsdfs
		</div>
	</div>
	</body>
</html>
