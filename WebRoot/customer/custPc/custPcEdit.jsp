<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Art Work</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/css/chooser.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=path%>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/MyFileUploadField.js"></script>
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript' src='<%=path%>/customer/custPc/js/custPcEdit.js'></script>	
		<script type="text/javascript" src="<%=path %>/common/js/uploadpanel.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
		
	</head>
	<%
	String custId = request.getParameter("custId");
	String eId = request.getParameter("eId");
	%>
	<body>
		<input type="hidden" id="custId" value="<%=custId%>"/>
		<input type="hidden" id="eId" value="<%=eId%>"/>
		<input type="hidden" name="basePath" id="basePath" value="<%=basePath%>" />
	</body>
</html>
