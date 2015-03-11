<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<head>
<title>SailingSoft Management System </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<FRAMESET border='0' name=tt frameSpacing='0' cols='178,*' frameBorder='no'>
<FRAME name='contents' src="<%=webapp %>/cotarea.do?method=queryTree" noResize scrolling=auto target="f2">
<FRAME id='mainF' name='f2' src="<%=webapp %>/cotfactory.do?method=query" >
</FRAMESET>