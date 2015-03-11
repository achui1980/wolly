<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>征样单编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>

		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotGivenService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotSignService.js'></script>	
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotPriceService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>	
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFactoryService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/setNoService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<!-- 导入ECTable -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/sign/sign/js/printPanelSign.js"></script>
		<script type='text/javascript' src='<%=webapp %>/sign/sign/js/signEdit.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/sign/sign/js/givenWin.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
		String givenId = request.getParameter("givenId");
		String givenNo = request.getParameter("givenNo");
	%>
	<body onload="mask('Loading')">
		<input type="hidden" id="sId" name="sId" value="<%=id%>" />
		<input type="hidden" id="givenId" name="givenId" value="<%=givenId%>" />
		<input type="hidden" id="givenNo" name="givenNo" value="<%=givenNo%>" />
		<input type="hidden" name="sizeFollow" id="sizeFollow"/>
		<input type="hidden" id="insertType" name="insertType" />
		<input type="hidden" id="con20" />
		<input type="hidden" id="con40" />
		<input type="hidden" id="con40H" />
		<input type="hidden" id="con45" />
	</body>
</html>

