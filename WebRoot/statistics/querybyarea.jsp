<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webapp = request.getContextPath();
%>
<head>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 导入公共js和css -->
	<jsp:include page="../extcommon.jsp"></jsp:include>
	<script type='text/javascript'
		src='<%=webapp%>/statistics/js/statistics.js'></script>
</head>
<body>
<form id="formId"></form>
</body>