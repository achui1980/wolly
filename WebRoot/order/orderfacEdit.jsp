<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ webapp + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>订单产品指派页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>

		<!-- 合计行 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
		<!-- MAP -->
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
		
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotGivenService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotPriceService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/sysdicutil.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFactoryService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/setNoService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<!-- 导入ECTable -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/order/js/orderfacEdit.js'></script>
		<script type='text/javascript' src='<%=webapp %>/order/js/orderDetailWin.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/orderfac/js/OrderToOrderfac.js'></script>
		<script type='text/javascript' src='<%=webapp %>/orderfac/js/OrderMbToOrderfacMb.js'></script>
		<script type='text/javascript' src='<%=webapp %>/orderfac/js/importPanel.js'></script>
		<script type='text/javascript' src='<%=webapp %>/orderfac/js/hispriceGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/tongChooseWin.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
		String oId = request.getParameter("oId");
	%>
	<body onload="mask('Loading')">
	<!-------------------------- 总金额 -------------------------->
		<div class="tab" id="talPriceDiv"
			style="position: absolute; top: 163px; left:350px;display: none;">
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
		<input type="hidden" id="pId" name="pId" value="<%=id%>" />
		<input type="hidden" id="oId" name="oId" value="<%=oId%>" />
		<input type="hidden" id="con20" />
		<input type="hidden" id="con40" />
		<input type="hidden" id="con40H" />
		<input type="hidden" id="con45" />
	</body>
</html>

