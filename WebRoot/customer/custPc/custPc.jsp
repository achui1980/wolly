<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/customer/custPc/js/custPc.js'></script>
		<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
	</head>
	<%
		String custId = request.getParameter("custId");
		String customerNo = request.getParameter("customerNo");
	%>
	<body>
		<input type="hidden" id="custId" value="<%=custId%>" />
		<input type="hidden" id="customerNo" value="<%=customerNo%>" />
	</body>
</html>
