<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>排载单编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>

		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/setNoService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotSplitService.js'></script>
		<!-- 导入ECTable -->
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/split/js/splitEdit.js'></script>
		<script type='text/javascript' src='<%=webapp %>/split/js/orderOutToSplit.js'></script>
		<script type='text/javascript' src='<%=webapp %>/split/js/importPanel.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
		String orderId = request.getParameter("orderOutId");
	%>
	<body onload="mask('Loading')">
		<!-- 总CBM -->
		<div class="tab" style="position: absolute;top: 188px;left: 260px;display: none;" id="cbmDiv">
			<label style="font-size: 10pt;color: #10418C;font: bold">总CBM:</label>
			<label id="totalLab" style="color: red;font-size: 12;font: bold">
				0.00
			</label>
		</div>
		<input type="hidden" id="pId" name="pId" value="<%=id%>" /> <!-- 排载主单号 -->
		<input type="hidden" id="orderId" name="orderId" value="<%=orderId%>" /> <!-- 出货主单号 -->
	</body>
</html>

