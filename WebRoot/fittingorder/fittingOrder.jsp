<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>配件采购列表</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotFitOrderService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/fittingorder/js/fittingorder.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

	</head>

	<script type="text/javascript">	
	    //权限判断变量，对应于cot_module表中的URL字段
		var url = "cotfittingorder.do?method=query";
		//新增
		function windowopenAdd(){
			//添加权限判断
			var isPopedom = getPopedomByOpType(vaildUrl,"ADD");
			if(isPopedom == 0)//没有添加权限
			{
				alert("您没有添加权限!");
				return;
			}
			openCustWindow('cotfittingorder.do?method=add');
		}	
		
				
	</script>
	<body>
	</body>
</html>
