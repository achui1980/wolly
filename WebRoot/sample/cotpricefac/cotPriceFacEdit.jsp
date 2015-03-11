<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<title>Quotations from Suppliers</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotPriceFacService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/sample/cotpricefac/js/cotpricefacAdd.js'></script>
		<script type="text/javascript">
			
			
			
		</script>
	</head>
	<%
		String id = request.getParameter("id");
		String eleId = request.getParameter("eleId");
	%>
	<body>
		
		<input type="hidden" name="mainId" id="mainId" value="<%=id%>" />
		<input type="hidden" name="eleId" id="eleId" value="<%=eleId %>" />
	</body>
</html>
