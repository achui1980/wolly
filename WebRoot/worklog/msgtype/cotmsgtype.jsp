<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
	//String empId = request.getSession().getAttribute("empId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>消息类型</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotMsgTypeService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/worklog/msgtype/js/msgtype.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<script type="text/javascript">
  //权限判断变量，对应于cot_module表中的URL字段
  var url = "cotmessagetype.do?method=query";

	</script>
	<body>
		
	</body>
</html>
