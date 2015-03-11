<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>The Number edit page</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotSeqService.js'></script>
		
		<!-- 下列多选框 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/ext/ux/css/MultiSelect.css"/> 
		<script type="text/javascript" src="<%=webapp%>/common/ext/ux/MultiSelect.js"></script> 
		
		<script type='text/javascript' src='<%=webapp %>/system/seq/js/seqEdit.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String cfgId = request.getParameter("id");
	 %>
	<body>
		<input type="hidden" id="cfgId" value='<%=cfgId %>' />
		<input type="hidden" id="hisDate" />
	</body>
</html>
