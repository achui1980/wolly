<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>电子邮箱</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="../../extcommon.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="common/css/swfUpload.css" />
    <link rel="stylesheet" type="text/css" href="common/css/SwfUploadPanel.css" />
     <script type="text/javascript" src="common/js/swfupload_2.2.js"></script>
    <script type="text/javascript" src="common/js/SwfUploadPanel.js"></script>    
    <script type="text/javascript" src="mail/import/js/import.js"></script>
  </head>
  
  <body>
  </body>
</html>
