<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>系统日志详细信息</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotSysLogService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<script type="text/javascript">
		
		//加载下拉框数据
		function bindSel()
		{
			//查找所有业务员
			cotOrderService.getEmpsList(function(res){
				bindSelect('empId',res,'id','empsName');
			});
		}
		
		//初始化页面
		function initForm()
		{
			 DWREngine.setAsync(false);
		     bindSel();
			 var id = $("syslogid").value;
			 cotSysLogService.getSyslogById(parseInt(id),function(res){
			 	 DWRUtil.setValues(res);
			 	 if(res.opTime!=null){ 
					var date = new Date(res.opTime);
					$('opTime').value = date.getYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
				 }
			 });
			 DWREngine.setAsync(true);
		}	
		
		</script>
	</head>
		<%
			String id = request.getParameter("id");
		%>
	<body onload="initForm();">
	    <input type="hidden" id="syslogid" name="syslogid" value=<%= id %> />
	    <div class="navline">
			<label>
				&nbsp;
			</label>
		</div>	
		<form id="syslogForm" name="syslogForm">
			<div style="width: 100%">
				<label class="label_18" style="text-align: right;margin-top: 8">
					操作模块：
				</label>
				<select class="select_30" id="opModule" name="opModule" disabled="disabled">
					<option value=""> 请选择 </option>
					<option value='login'>系统登录</option> 
					<option value='elements'>样品</option>  
					<option value='price'>报价</option>
					<option value='order'>订单</option>
					<option value='orderOut'>出货</option>
					<option value='orderfac'>生产合同</option>
					<option value='given'>送样</option>
					<option value='sign'>征样</option>
					<option value='split'>排载</option>
				</select>
				<label class="label_18" style="text-align: right;margin-top: 8">
					操作类型：
				</label>
				<select class="select_30" id="opType" name="opType" disabled="disabled">
					<option value=""> 请选择 </option>
					<option value=0>系统登录</option>
					<option value=1>添加</option>
					<option value=2>修改</option>
					<option value=3>删除</option>
				</select>
			</div>
			<div style="width: 100%">
				<label class="label_18" style="text-align: right;margin-top: 8">
					操作时间：
				</label>
				<input class="input_30" id="opTime" name="opTime" readonly="readonly"/>
				<label class="label_18" style="text-align: right;margin-top: 8">
					操作员工：
				</label>
				<select class="select_30" id="empId" name="empId" disabled="disabled">
				</select>
			</div>
			<div style="width: 100%">
				<label class="label_18" style="text-align: right;margin-top: 8">
					日志内容：
				</label>
				<textarea class="input_80" style="width: 78%" rows="8" id="opMessage" name="opMessage" readonly="readonly"></textarea>
			</div>
		</form>
		<div style="margin-top:5px;" align="center"> 
			<a onclick="javascript:closeandreflashEC(true,'syslogTable',false);" 
			 		style="cursor: hand;"><img
					src="<%=webapp %>/common/images/_toff.gif" border="0"
					height="21px" width="61px"></a>	
		</div>
	</body>
</html>
