<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%
String path = request.getContextPath();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/ext/ux/css/GroupSummary.css" />
		 <script type="text/javascript" src="<%=path %>/common/ext/ux/GroupSummary.js"></script>
		 <script type='text/javascript' src='<%=path%>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript'
			src='<%=path %>/dwr/interface/cotElePriceService.js'></script>
		<!-- 导入ECTable -->
		<!-- 导入js -->
		<script type='text/javascript' src='<%=path%>/dwr/interface/baseDataUtil.js'></script>
		<script type="text/javascript"
			src="<%=path %>/sample/elements/js/elePrice.js"></script>
		<style type="">
		.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
		.x-grid3-gridsummary-row-offset{width:10000px;}
		.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
	</head>
	<%
		String mainId = request.getParameter("mainId");
	%>
	<body>
		<input type="hidden" id="mainId" value="<%=mainId%>" />
	</body>
</html>