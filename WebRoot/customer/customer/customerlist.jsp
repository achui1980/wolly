<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Client List</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/customer/customer/js/customerlist.js'></script>
		<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
	</head>
	<script type="text/javascript">
	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cotcustomer.do?method=query"; 

	//删除	
	function  del(id)
	{   
		//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
		if(isPopedom == 0)//没有删除权限
		{
			Ext.Msg.alert("Message","Sorry, you do not have Authority");
			return;
		}
		var flag = window.confirm("are you sure to delete the selected items?");
		if(flag){
			cotCustomerService.deleteCustomerById(id,function(res){
		        if(res!=-1){
					Ext.Msg.alert("Message","Deleted successfully");
					reloadGrid('custGrid');
				}else{
					Ext.Msg.alert("Message","It has been used,you can't delete it!");
				}
			}) 
		}else{
				return;
		}
	}
	
	//导出方法
	function exportCust()
	{
		//导出权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"OUTPUT");
		if(isPopedom == 0)//没有导出权限
		{
			Ext.Msg.alert("Message","Sorry, you do not have Authority");
			return;
		}
		
		var form = document.getElementById("queryForm");
		form.action = "./exportCust.action";
		form.target = "";
		form.method = "post";
		form.submit();
	}
	
	</script>
	<body>

		<input
			type="hidden"
			name="all"
			id="all"
			value="${all}" />
		<input
			type="hidden"
			name="dept"
			id="dept"
			value="${dept}" />
		<input
			type="hidden"
			name="addPerId"
			id="addPerId"
			value="${emp.id}" />
		<input
			type="hidden"
			name="deptId"
			id="deptId"
			value="${emp.deptId}" />
		<input
			type="hidden"
			name="empNo"
			id="empNo"
			value="${emp.empsId}" />
		<div
			style="width: 100%;">
			<!-- 
			<div style="float: left; cursor: hand;" onclick="showMainQueryDiv()">
				<label style="color:#000;margin-bottom:0px;">
					搜索
				</label>
				<img src="<%=webapp%>/common/images/down.jpg" id="flagPicMain" />
			</div>	
			 -->
			<input
				type="hidden"
				name="custId"
				id="custId" />
	</body>
</html>
