<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>The set of Modules Column</title>
		<!-- 导入公共js和css -->
		<jsp:include page="../../extcommon.jsp"></jsp:include>
	     
		<script type='text/javascript' src='dwr/interface/cotCustomizeService.js'></script>
		<script type='text/javascript'
			src='system/customize/js/customize.js'></script>
		<script type="text/javascript" src="common/js/popedom.js"></script>
	</head>
	<body>
	    <input type="hidden" id="nodeText" value=''>
	    <input type="hidden" id="type" name="type" value=''>
	    <input type="hidden" id="empsId" value='<%= request.getAttribute("empsId")%>'>
	</body>
</html>