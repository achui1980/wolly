<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>样品单编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type="text/javascript">
			getFacPodom('cotgiven.do');
		</script>
		
		 <!-- 单个文件上传css -->
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/file-upload.css" />
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/chooser.css" />
		<!-- 单个文件上传js -->
		<script type='text/javascript' src='<%=webapp%>/common/ext/ux/FileUploadField.js'></script>
		<!-- 下列多选框 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/MultiSelect.css"/> 
		<script type="text/javascript" src="<%=webapp%>/common/ext/ux/MultiSelect.js"></script> 
		
		<!-- 上传图片 -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/contexUtil.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/SwfUploadPanel.css" />
		<script type="text/javascript" src="<%=webapp %>/common/js/swfupload_2.2.js"></script>
		<script type='text/javascript' src='<%=webapp %>/common/js/SwfUploadPanel.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/handlers.js"></script>
		<link href="<%=webapp %>/common/css/swfUpload.css" rel="stylesheet" type="text/css" />
		
		<!-- 图片鼠标拖拉选择框 -->
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/DataView-more.js"></script>

		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotGivenService.js'></script>
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
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/importPicService.js'></script>
		
		<!-- 导入ECTable -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/panWin.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/tongChooseWin.js'></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/uploadPan.js'></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/givenEdit.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/priceGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/orderGrid.js'></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/givenGridTogiven.js'></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/reportPanel.js'></script>
		<script type='text/javascript' src='<%=webapp %>/given/js/importPanel.js'></script>
		<script type='text/javascript' src='<%=webapp %>/commonQuery/js/importPicPanel.js'></script>
		<script type="text/javascript" src="<%=webapp %>/given/js/eleQuery.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
	</head>
	<%
		String id = request.getParameter("id");
		String cusId = request.getParameter("custId");
		String busiId = request.getParameter("busiId");
	%>
	<body onload="mask('Loading')">
		<input type="hidden" id="pId" name="pId" value="<%=id%>" />
		<input type="hidden" id="insertType" name="insertType" />
		<input type="hidden" name="sizeFollow" id="sizeFollow"/>
		<input type="hidden" id="con20" />
		<input type="hidden" id="con40" />
		<input type="hidden" id="con40H" />
		<input type="hidden" id="con45" />
		<input type="hidden" id="cusId" name="cusId" value="<%=cusId%>" />
		<input type="hidden" id="busiId" name="busiId" value="<%=busiId%>" />
		
		<input type="hidden" id="noPrice" name="noPrice" />
		<input type="hidden" id="havePrice" name="havePrice" />
		
	</body>
</html>

