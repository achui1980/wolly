<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>其他费用</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=path %>/dwr/interface/cotGivenService.js'></script>
		<script type="text/javascript"
			src="<%=path %>/given/js/finaceOther.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/popedom.js"></script>
	</head>
	<%
		String fkId = request.getParameter("fkId");
		if(fkId == null) fkId = "";
		String pId = request.getParameter("pId");
		if(pId == null) pId = "";
	 %>
	<body>
		<input type="hidden" id="fkId" value="<%=fkId %>"/>
		<input type="hidden" id="pId" value="<%=pId %>"/>
	</body>
</html>
