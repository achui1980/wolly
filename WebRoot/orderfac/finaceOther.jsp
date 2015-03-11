<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Other costs</title>
    <!-- 导入公共js和css -->
	<jsp:include page="/extcommon.jsp"></jsp:include> 	
	<!-- 导入dwr -->
	<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderFacService.js'></script>
	
	<!-- MAP -->
	<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
	<!-- 合计行 -->
	<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
	<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
	<style>
		.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
		.x-grid3-gridsummary-row-offset{width:10000px;}
		.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
	</style>
	
	<script type='text/javascript' src='<%=webapp %>/orderfac/js/finaceOther.js'></script>
  	 <script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
  </head>
	<%
		String id = request.getParameter("fkId");
	%>
  <body> 
  <input type="hidden" id="orderfacId" name="orderfacId" value="<%=id%>" /><!--
  <input type="hidden" id="priceScal" name="priceScal" />
  <input type="hidden" id="prePrice" name="prePrice" />
  --></body>
</html>