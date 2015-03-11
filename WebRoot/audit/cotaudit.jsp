<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotAuditService.js'></script>
		<!-- 导入ECTable -->
	 	<script type='text/javascript' src='<%=webapp %>/audit/js/audit.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<script type="text/javascript">
//权限判断变量，对应于cot_module表中的URL字段
var url = "cotaudit.do?method=query";
	


	
	//逾期或迟交判断
	function checkStatus(){
		var curDate = getCurrentDate('yyyy-MM-dd');
		cotAuditService.updateStatus(curDate,function(res){
		});
	}
</script>
	</head>
	<body>

	</body>
</html>