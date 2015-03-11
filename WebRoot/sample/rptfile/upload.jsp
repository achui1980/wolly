<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Report Edit</title>
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css"
			href="<%=webapp%>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript"
			src="<%=webapp%>/common/ext/ux/FileUploadField.js"></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotRptFileService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotRptTypeService.js'></script>
		<script type="text/javascript"
			src="<%=webapp%>/common/js/uploadpanel.js"></script>
		<script type='text/javascript'
			src='<%=webapp%>/sample/rptfile/js/upload.js'></script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body>
		<div id="uploadbtn">
			<span id="spanButtonPlaceholder"></span>
		</div>
		<div id="upload"></div>
		<div id="uploadpanel"></div>
		<div id="button"></div>
		<input type="hidden" name="rptfileid" id="rptfileid" value="<%=id%>" />
		<input type="hidden" name="filePath" id="filePath" />
	</body>
</html>