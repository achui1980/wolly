<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		<title>Page of sample add</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type="text/javascript">
			getFacPodom('cotelements.do');
		</script>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotElementsService.js'></script>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript"
			src="<%=path%>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript"
			src="<%=path%>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=path%>/commonQuery/js/newEleAdd.js'></script>

		<script type="text/javascript" src="<%=path%>/common/js/popedom.js"></script>
	</head>

	<%
		String parentType = request.getParameter("parentType");
		String newEleId = request.getParameter("newEleId");
		String wantEleId = request.getParameter("wantEleId");
	%>
	<body>

		<input type="hidden" id="basePath" name="basePath"
			value="<%=basePath%>">
		<input type="hidden" name="newEleId" id="newEleId"
			value="<%=newEleId%>">
		<input type="hidden" name="wantEleId" id="wantEleId"
			value="<%=wantEleId%>">
		<input type="hidden" name="parentType" id="parentType"
			value="<%=parentType%>">
	</body>
</html>
