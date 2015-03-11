<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Product Files</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/css/chooser.css" />
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotElementsService.js'></script>
			
		<!-- 图片鼠标拖拉选择框 -->
		<script type="text/javascript" src="<%=path %>/common/ext/ux/DataView-more.js"></script>
		
		<script type='text/javascript' src='<%=path%>/sample/elements/js/elequery.js'></script>	
		<script type='text/javascript' src='<%=path%>/sample/elements/js/batchUpdate.js'></script>
		<script type='text/javascript' src='<%=path%>/sample/elements/js/picview.js'></script>	
		<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
	</head>

	<body>
	</body>
</html>
