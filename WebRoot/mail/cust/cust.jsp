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
	<!-- overrides to base library -->
    <link rel="stylesheet" type="text/css" href="common/ext/resources/css/examples.css"/>
    <link rel="stylesheet" type="text/css" href="common/css/email.css"/>
    <script type="text/javascript" src="common/ext/ux/CheckColumn.js"></script>
    <script type="text/javascript" src="common/ext/ux/ProgressBarPager.js"></script>
    <script type="text/javascript" src="common/ext/examples.js"></script>

    <script type="text/javascript" src="dwr/interface/mailLocalService.js"></script>
    <script type="text/javascript" src="dwr/interface/cotContactService.js"></script>
    <script type="text/javascript" src="dwr/interface/cotCustContactService.js"></script>
    
    <!-- page specific -->
    <script type="text/javascript" src="mail/common/common.js"></script>
    <script type="text/javascript" src="mail/cust/js/cust.js"></script>
  </head>
  
  <body>
  	<div id="spanButtonPlaceholder" ></div>&nbsp; 
  	<input type="hidden" id="sessionId" name="sessionId" value='<%= request.getSession().getId()%>'>
  	<input type="hidden" id="custId" name="custId" value='<%= request.getParameter("custId")%>'>
  	<form  style="display:none" id="printForm">
  		<input type="hidden" id="printcontent" name="printcontent" />
  	</form>
  </body>
</html>
