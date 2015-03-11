<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'typelv.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="/extcommon.jsp"></jsp:include>
	<!-- 单个文件上传css -->
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/ext/ux/css/file-upload.css" />
	<!-- 单个文件上传js -->
	<script type='text/javascript' src='<%=path%>/common/ext/ux/FileUploadField.js'></script>
	
	<script type='text/javascript' src='<%=path%>/dwr/interface/cotElePriceService.js'></script>
	<script type='text/javascript' src='<%=path%>/sample/report/js/reportElePrice.js'></script>
	<script type='text/javascript' src='<%=path%>/sample/report/js/defineReport.js'></script>
	
	<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
  </head>
  
  <body>
  </body>
</html>
