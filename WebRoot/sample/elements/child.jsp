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
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/sample/elements/js/child.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String mainId = request.getParameter("mainId");
	%>
<body>
	<input type="hidden" id="mainId" name="mainId" value="<%=mainId %>">
</body>
</html>