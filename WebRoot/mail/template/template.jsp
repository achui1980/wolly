<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>邮件模版</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="../../extcommon.jsp"></jsp:include>

    <script type="text/javascript" src="dwr/interface/mailTemplateService.js"></script>
    <!-- fckeditor -->
	<script type="text/javascript" src="common/fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="common/ext/FCKeditor.js"></script>
    
    <!-- page specific -->
     <script type="text/javascript" src="mail/template/js/template.js"></script>

  </head>
  
  <body>
       
  	<input type="hidden" id="sessionId" name="sessionId" value='<%= request.getSession().getId()%>'>
  	<input type="hidden" id="empsId" value='<%= request.getAttribute("empsId")%>'>
  	<input type="hidden" id="nodeText" value=''>
  	
  </body>
</html>
