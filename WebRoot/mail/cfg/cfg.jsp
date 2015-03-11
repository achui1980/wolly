<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>公共邮箱配置</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="../../extcommon.jsp"></jsp:include>

    <script type="text/javascript" src="dwr/interface/mailCfgService.js"></script>
    
    <!-- page specific -->
    <script type="text/javascript" src="mail/common/mailConstants.js"></script>
    <script type="text/javascript" src="mail/cfg/js/cfg.js"></script>
    <script type="text/javascript" src="mail/cfg/js/mailConfig.js"></script>
    
  </head>
  
  <body>
  	<div style='width:100%;height:100%;top:0;left:0;'>
       <div id="form" style='position:absolute;top:5%;left:20%;'></div> 
  	</div>	
  	<input type="hidden" id="isEmpAction" value='<%= request.getAttribute("isEmpAction")%>'>
  	<input type="hidden" id="empId" value='<%= request.getAttribute("empId")%>'>
  </body>
</html>
