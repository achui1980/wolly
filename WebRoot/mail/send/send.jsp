<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>发送邮件</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="../../extcommon.jsp"></jsp:include>

    
    <link rel="stylesheet" type="text/css" href="common/css/swfUpload.css" />
    <link rel="stylesheet" type="text/css" href="common/css/SwfUploadPanel.css" />
    <link rel="stylesheet" type="text/css" href="common/ext/resources/css/examples.css"/>
    <script type="text/javascript" src="common/ext/examples.js"></script>
    <script type="text/javascript" src="common/ext/ux/ProgressBarPager.js"></script>

    <script type="text/javascript" src="dwr/interface/mailSendService.js"></script>
    <script type="text/javascript" src="dwr/interface/mailLocalService.js"></script>
    <script type="text/javascript" src="dwr/interface/mailTemplateService.js"></script>
    
    <!-- fckeditor -->
	<!--<script type="text/javascript" src="common/fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="common/ext/FCKeditor.js"></script>
    
    --><!-- page specific -->
     <script type="text/javascript" src="mail/send/js/sendmail.js"></script>
 
    
    
  </head>
  
  <body>
       
  	<input type="hidden" id="sessionId" name="sessionId" value='<%= request.getSession().getId()%>'>
  	
  	<input type="hidden" id="sendEmpEmail" value='<%= request.getAttribute("sendEmpEmail")%>'>
  	<input type="hidden" id="sendEmpName" value='<%= request.getAttribute("sendEmpName")%>'>
  	
  	<input type="hidden" id="mailId" value='<%= request.getParameter("mailId")%>'> 
  	<input type="hidden" id="excelKey" value='<%= request.getParameter("excelKey")%>'>
  	<input type="hidden" id="sendStatus" value='<%= request.getParameter("status")%>'>

	<input type="hidden" id="empsSign" value='<%= request.getAttribute("empsSign")%>'>
	<input type="hidden" id="empsId" value='<%= request.getAttribute("empsId")%>'>
	<input type="hidden" id="bodyTxt" value=''>
	<input type="hidden" id="fckField" name='fckField' value=''>
  </body>
</html>
