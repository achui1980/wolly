<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>货币类型</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<jsp:include page="/extcommon.jsp"></jsp:include>
	<script type='text/javascript' src='<%=path%>/dwr/interface/cotCurrencyService.js'></script>
	<script type='text/javascript' src='<%=path%>/systemdic/currency/js/currency.js'></script>
	<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
  </head>
  
  <body>
  </body>
</html>