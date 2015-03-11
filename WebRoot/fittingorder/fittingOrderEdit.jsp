<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>配件采购编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>

		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotFitOrderService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/queryService.js'></script>
		
		<!-- MAP -->
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
		
		<!-- 导入ECTable -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/fittingorder/js/fittingOrderEdit.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body onload="mask('Loading')">
		<div class="tab" id="talPriceDiv"
			style="position: absolute; top: 133px; left:400px;display: none;">
			<label style="font-size: 10pt; color: #10418C; font: bold">
				货款金额:
			</label>
			<label id="totalLab" style="color: red; font-size: 12; font: bold">
				0
			</label>
			<label
					style="font-size: 10pt; color: #10418C; font: bold; margin-left: 20px;">
				实际金额:
			</label>
			<label id="realLab" style="color: red; font-size: 12; font: bold">
				0
			</label>
		</div>
		<input type="hidden" id="pId" name="pId" value="<%=id%>" /> <!-- 包材采购主单号 -->
	</body>
</html>