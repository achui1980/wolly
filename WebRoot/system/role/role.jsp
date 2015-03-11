<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>User role management</title>
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotRoleService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/system/role/js/role.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

</head>
	<body>
		<select id="roleStatus" >
			<option value="0">Disable</option>
			<option value="1" selected>Enable</option>
		</select>
	</body>
</html>