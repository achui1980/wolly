<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Inquiry Editor</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css"
			href="<%=webapp%>/common/ext/resources/css/file-upload.css" />
			
		<style>
			.x-grid-record-green  tr{ 
			    background:lightgreen ;
			}
		</style>
		<script type="text/javascript"
			src="<%=webapp%>/common/ext/ux/FileUploadField.js"></script>
		<script type='text/javascript'
		<script type="text/javascript" src="<%=webapp %>/pan/js/panAdd.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body>
		<input type="hidden" id="pId" name="pId" value="<%=id%>" />
	</body>
</html>
