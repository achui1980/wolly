<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Product Info</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type="text/javascript">
			getFacPodom('cotelements.do');
		</script>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotElementsService.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=path %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=path%>/sample/elements/js/elementsAdd.js'></script>	
		
		<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
	</head>
	<%
		String eleNo = request.getParameter("eleNo");
		String byId = request.getParameter("byId");
		String myId = request.getParameter("myId");
		String syId = request.getParameter("syId");
		String facId = request.getParameter("facId");
		String chk = request.getParameter("chk");
	%>
	<body onload="mask('Loading')">
	
	<input type="hidden" id="basePath" name="basePath" value="<%=basePath %>">
	<input type="hidden" id="eleNo" name="eleNo" value="<%=eleNo %>">
	<input type="hidden" id="byId" name="byId" value="<%=byId %>">
	<input type="hidden" id="myId" name="myId" value="<%=myId %>">
	<input type="hidden" id="syId" name="syId" value="<%=syId %>">
	<input type="hidden" id="facId" name="facId" value="<%=facId %>">
	<input type="hidden" id="chk" name="chk" value="<%=chk %>">
	</body>
</html>
