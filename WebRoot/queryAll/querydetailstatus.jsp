<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Purchage Order</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type="text/javascript" src="<%=webapp %>/common/js/CustomizeDateField.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotFaqService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderStatusFileService.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/GroupHeaderPlugin.css" />
		<script type="text/javascript" src="<%=webapp%>/common/js/ext-override.js"></script>
		<script type="text/javascript" src="<%=webapp%>/common/ext/ux/GroupHeaderPlugin.js"></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/answerWin.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/questionWin.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/faqWin.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/OrderDetailStatusUploadWin.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/querydetailstatus.js'></script>
		
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

	</head>

	<script type="text/javascript">
					
	</script>
	<%
		String type = request.getParameter("type");
	%>
	<body>

		<input type="hidden" id="type" value="<%=type%>" />
	</body>
</html>
