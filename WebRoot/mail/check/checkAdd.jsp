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
    <!-- page specific  -->
    <script type='text/javascript' src='dwr/interface/mailCheckService.js'></script>
    <script type="text/javascript" src="mail/check/js/checkAdd.js"></script>
  </head>
  <%
		String id = request.getParameter("id");
	%>
  <body>
  	<input type="hidden" id="ruleId" name="ruleId" value="<%=id%>" />
  	<input type="hidden" id="custId" name="custId" value="" />
  	<input type="hidden" id="type" name="type" value="" />
  	<input type="hidden" id="flag" name="flag" value="" />
  </body>
</html>
