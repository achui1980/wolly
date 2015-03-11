<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<!-- 导入公共js和css -->
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotTypeLv3Service.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/systemdic/typelv3/js/typelv3.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		</head>
	<script type="text/javascript">
 //权限判断变量，对应于cot_module表中的URL字段
 var url = "cottypelv3.do?method=query"; 
</script>

	<body>
		
	</body>
</html>
