<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>冲帐明细</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/finace/deal/js/dealDetail.js'></script>

		<script type="text/javascript">
		//显示来源页面
		function showSourceWin(fkId){
			if(fkId != null && fkId != ''){
				openCustWindow('cotfinancegiven.do?method=add&id='+fkId);
			}
		}				
		</script>
	</head>
	<body>
		
	</body>
</html>
