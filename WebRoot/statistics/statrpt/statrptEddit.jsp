<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Statistics Report</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="../../extcommon.jsp"></jsp:include>   
	<link rel="stylesheet" type="text/css" href="common/ext/resources/css/file-upload.css" />
	<script type="text/javascript"src="common/ext/ux/FileUploadField.js"></script>
	<script type="text/javascript" src="common/js/uploadpanel.js"></script>
    <!-- page specific  -->
    <script type='text/javascript' src='dwr/interface/cotStatPopedomService.js'></script>
    <script type="text/javascript" src="statistics/statrpt/js/statrptEddit.js"></script>
  </head>
    <%
		String id = request.getParameter("id");
	%>
  <body>
  	<div style='width:100%;height:100%;top:0;left:0;'>
       <div id="form" style='position:absolute;top:5%;left:20%;'></div> 
  	</div>	
  	<input type="hidden" id="oldName" name="oldName" value="" />
  	<input type="hidden" id="eId" name="eId" value="" />
  	<input type="hidden" id="statId" name="statId" value="<%=id%>"/>
  </body>
</html>
