<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
	//String empId = request.getSession().getAttribute("empId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotFittingsService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/sample/elements/js/eleFittingGrid.js'></script>
		<script type='text/javascript' src='<%=webapp%>/sample/elements/js/fitInputGrid.js'></script>
		
		<!-- 合计行 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
		
		
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	
	</head>
	<%
		String eleid = request.getParameter("mainId");
		String eleName = request.getParameter("eleId");
		String flag = request.getParameter("Flag");
		String childId = request.getParameter("childId");
	%>
<body>
	<input type="hidden" id="eleid" name="eleid" value="<%=eleid %>" />
	<input type="hidden" id="eleName" name="eleName" value="<%=eleName %>" />
	<input type="hidden" id="Flag" name="Flag" value="<%=flag %>"/>
	<input type="hidden" id="childId" name="childId" value="<%=childId %>"/>
</body>
</html>