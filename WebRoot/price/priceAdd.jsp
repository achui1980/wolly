<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Quote edit page</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type="text/javascript">
			getFacPodom('cotprice.do');
		</script>
		 <!-- 表格中的复选框列 -->
		 <script type="text/javascript" src="<%=webapp %>/common/ext/ux/CheckColumn.js"></script>
		 <!-- 单个文件上传 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/file-upload.css" />
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/chooser.css" />
		<script type='text/javascript' src='<%=webapp%>/common/ext/ux/FileUploadField.js'></script>
		<!-- 下列多选框 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/MultiSelect.css"/> 
		<script type="text/javascript" src="<%=webapp%>/common/ext/ux/MultiSelect.js"></script> 
		
		<!-- 锁定表格扩展 -->
		<!--<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/LockingGridView.css" />
		<script type='text/javascript' src='<%=webapp%>/common/ext/ux/LockingGridView.js'></script>-->
		
		<!-- 合计行 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
		
		<!-- 图片鼠标拖拉选择框 -->
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/DataView-more.js"></script>
		
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotPriceService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/sysdicutil.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/setNoService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/importPicService.js'></script>
		
		<!-- MAP -->
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
		
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/price/js/panWin.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/tongChooseWin.js'></script>
		<script type='text/javascript' src='<%=webapp %>/price/js/uploadPan.js'></script>
		<script type='text/javascript' src='<%=webapp %>/price/js/priceAdd.js'></script>
		<script type='text/javascript' src='<%=webapp %>/order/js/panEleGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/priceGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/orderGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/givenGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/childGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/fittingGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/price/js/fitGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/price/js/hispriceGrid.js'></script>
		
		<script type='text/javascript' src='<%=webapp %>/price/js/reportPanel.js'></script>
		<script type="text/javascript" src="<%=webapp %>/commonQuery/js/eleQuery.js"></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/importPicPanel.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/importPanel.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/modPrice.js'></script>
		<script type='text/javascript' src='<%=webapp %>/price/js/fitPricePanel.js'></script>
		<script type='text/javascript' src='<%=webapp %>/price/js/elePriceGrid.js'></script>
		
		<!-- 上传图片 -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/contexUtil.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/SwfUploadPanel.css" />
		<script type="text/javascript" src="<%=webapp %>/common/js/swfupload_2.2.js"></script>
		<script type='text/javascript' src='<%=webapp %>/common/js/SwfUploadPanel.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/handlers.js"></script>
		<link href="<%=webapp %>/common/css/swfUpload.css" rel="stylesheet" type="text/css" />
		
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		
	</head>
	<%
		String id = request.getParameter("id");
		String cusId = request.getParameter("custId");
		String busiId = request.getParameter("busiId");
	%>
	<body onload="mask('Loading')">
		<input type="hidden" id="pId" name="pId" value="<%=id%>" />
		<input type="hidden" id="noPrice" name="noPrice" />
		<input type="hidden" id="havePrice" name="havePrice" />
		<input type="hidden" id="insertType" name="insertType" />
		<input type="hidden" name="oldCur" id="oldCur" />
		<input type="hidden" id="con20" />
		<input type="hidden" id="con40" />
		<input type="hidden" id="con40H" />
		<input type="hidden" id="con45" />
		<div id="rrr"></div>
		<input type="hidden" id="cusId" name="cusId" value="<%=cusId%>" />
		<input type="hidden" id="busiId" name="busiId" value="<%=busiId%>" />
		
	</body>
</html>
