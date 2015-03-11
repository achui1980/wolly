<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Set page order number</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotSeqService.js'></script>
		
		<script type='text/javascript' src='<%=webapp %>/system/seq/js/seq.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>

	<body>
	</body>
</html>
