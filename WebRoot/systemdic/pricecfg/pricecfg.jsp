<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<jsp:include page="/jquerycommon.jsp"></jsp:include>

<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotPriceCfgService.js'></script>
<script type='text/javascript' src='<%=webapp%>/systemdic/pricecfg/js/pricecfg.js'></script>
<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
</head>

<body>
</body>
</html>