<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Barcode printing</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotBarcodeService.js'></script>
			
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/cotPriceService.js'></script>
		<script type='text/javascript' src='<%=path%>/barcode/js/barcode.js'></script>	
		<script type="text/javascript" src="<%=path %>/commonQuery/js/eleTable.js"></script>
		
		<script type="text/javascript" src="<%=path %>/common/js/popedom.js"></script>
	</head>

	<body>
	 	<input type="hidden" id="flag" name="flag" value="" />
	</body>
</html>
