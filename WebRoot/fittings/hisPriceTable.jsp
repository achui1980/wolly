<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>客户查询表格页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFittingsService.js'></script>	
		<!-- 导入ECTable -->
		<script type='text/javascript' src='<%=webapp %>/fittings/js/hisPrice.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<script type="text/javascript">
						
		</script>
	</head>
	<%
		String fitId = request.getParameter("fitId");
		String fitName = request.getParameter("fitName");
	%>
	<body>
		<input type="hidden" id="fitId" name="fitId" value="<%=fitId%>" />
		<input type="hidden" id="fitName" name="fitName" value="<%=fitName%>" />
	</body>
</html>
