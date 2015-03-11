<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Home</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/chooser.css" />
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<!-- 导入dwr -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
			.wcPnl{font-size: 10pt; color: #10418C; font: bold;}
		</style>
		
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/OrderDetailStatusUploadWin.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/orderfacdetailCopy.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/PdfComment.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/orderorderfacstatus.js'></script>
<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
	</head>

	<script type="text/javascript">
					
	</script>
	<style type="text/css">
    	.x-grid3-cell-inner {   
 			/*内容长的时候换行  */
 			white-space: nowrap !important;
 			word-wrap:normal;
 			text-overflow:ellipsis;
		}
    </style>
	<body>

	</body>
</html>
