<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Offer Records</title>
    <!-- 导入公共js和css -->
	<jsp:include page="/extcommon.jsp"></jsp:include> 	
	<script type='text/javascript' src='<%=webapp %>/sample/elements/js/orderInfo.js'></script>
   <script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
  </head>
  	<% 
		String eleId = request.getParameter("eleId");
	%>
  <body> 
		<input type="hidden" id="eleId" value="<%=eleId %>"/>
  </body>
</html>
