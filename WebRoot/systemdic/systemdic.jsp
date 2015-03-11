<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webapp=request.getContextPath();
%>
<head>
	<title>旗航工艺品管理系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<script type="text/javascript">
</script>
	<FRAMESET name=tt cols=175,10,* border='0' frameSpacing='0' id="tt">
		<FRAME name='contents' src="<%=webapp %>/cotmodule.do?method=querySysDic" frameBorder=no  scrolling=yes target="f2" >
		<FRAME name='change' src="<%=webapp %>/middle.html" frameBorder=no noResize scrolling=no
			target="main">
		<FRAME id='mainF' name='f2' src="" frameBorder=no noResize >
	</FRAMESET>