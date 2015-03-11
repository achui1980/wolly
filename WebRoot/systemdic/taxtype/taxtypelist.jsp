<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='dwr/interface/cotTaxTypeService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/systemdic/taxtype/js/taxtypelist.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		

	</head>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cottaxtype.do?method=query"; 
	
 
    </script>
	<body >
	</body>
</html>
