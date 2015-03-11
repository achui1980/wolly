<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>费用结算方式添加页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotBalanceTypeService.js'></script>
		<script type="text/javascript">
  //权限判断变量，对应于cot_module表中的URL字段
var url = "cotbalancetype.do?method=query"; 
			//添加
			function add()
			{	
				var obj = DWRUtil.getValues("cotBalanceTypeForm");
				//验证表单
				var form = document.forms['cotBalanceTypeForm'];
				var b = false;
				b = Validator.Validate(form,1);
				if(!b)
				{
					return;
				}
				var list = new Array();
				list.push(obj); 
				cotBalanceTypeService.addBalanceTypes(list,function(res){
					if(res){
						alert("添加成功！");
						closeandreflashEC(true,"balancetypeTable",false);
					}else{
						alert("添加失败！费用结算方式名称已存在!");
						$('balanceName').select();
					}
					
				})
			}
			
			//初始化表单验证字段
			function validForm()
			{
				appendattribute("balanceName","费用结算方式不能为空","Require");
			}
			
			//页面加载调用
			function initForm()
			{
				 //表单验证
				 validForm();
			}
		</script>
	</head>

	<body onload="initForm()">
		<table style="width: 100%;">
		<div class="navline">
			<label>
				&nbsp;
			</label>
		</div>			
			<tr>
				<td align="center">
					<form name="cotBalanceTypeForm" id="cotBalanceTypeForm"
						action="/budgetmanager/executeRecord.do">
						<div style="width: 100%; float: left;margin-top:10px;">
							<label class="label_25" style="color: red;text-align:right">
								费用结算方式：
							</label>
							<input class="input_65" type="text" name="balanceName"
								id="balanceName" maxlength="20" />
							<div style="width: 100%; float: left;">
								<label class="label_25" style="text-align:right;color:#000;">
									备  注：
								</label>
								<textarea class="select_65" rows="3" name="balanceRemark"
									id="balanceRemark"
									onKeyDown="if(this.value.length>200)this.value=this.value.substr(0,200)"
									onKeyUp="if(this.value.length>200)this.value=this.value.substr(0,200)"></textarea>
							</div>
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div style="margin-left:10%;margin-top:5px;">&nbsp;
						<a onclick="add()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_save.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="javascript:closeandreflashEC(true,'balancetypeTable',false);"
								style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_cancel.gif" border="0"
								height="21px" width="61px"></a>	
					<!-- 
						<button class="confirmBtn" onclick="add()">
							保存
						</button>
						<button class="cancelBtn"
							onclick="javascript:closeandreflashEC(true,'balancetypeTable',false);">
							关闭
						</button>
					-->
						<input type="hidden" name="eId" id="eId" value="" />
					</div>
				<td>
		</table>

	</body>
</html>
