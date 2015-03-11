<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webapp=request.getContextPath();
%>
<head>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="/extcommon.jsp"></jsp:include>
	<script type='text/javascript' src='<%=webapp %>/systemdic/sysdictree.js'></script>
<script type="text/javascript">
	 
	function doAction(no,url)
	{
	    if(url!='null'){
	    	parent.f2.location.href = url;
	    }else{
	    	parent.f2.location.href = "home.jsp";
	    }
	}
</script>
</head>
<body >

<div id="content"></div>
</body>