<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="../../extcommon.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotNationService.js'></script>
<script type="text/javascript" src="<%=webapp %>/systemdic/nation/js/nation.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
<script type="text/javascript">
var url = "cotnation.do?method=query";
</script>
</head>
<body>
</body>
</html>