<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
	//String path = request.getContextPath();
	//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//String empId = request.getSession().getAttribute("empId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotFileService.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=webapp%>/sample/file/js/fileGrid.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
<style type="text/css">
   .div1 {
         margin-top: 20px;
         margin-left: 270px;
   }
</style>
	</head>
	<%
		String eleid = request.getParameter("eleId");
	%>
<body>
	
	<!-------------------------- 上传文件的隐藏层 ------------------------------------->
	<div id="uploadFileDiv" style="position:absolute;z-index:9500;"></div>
	<!-------------------------- 上传文件的隐藏层(结束) -------------------------------------> 
	<input type="hidden" id="eleid" name="eleid" value="<%=eleid %>" />
</body>
</html>