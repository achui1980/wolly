<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>费用结算方式编辑页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotBalanceTypeService.js'></script>
		<script type="text/javascript">
  //权限判断变量，对应于cot_module表中的URL字段
var url = "cotbalancetype.do?method=query";
var emp = null; 
			//修改
			function mod()
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
				obj.id = $("eId").value;
				var list = new Array();
				list.push(obj);
				cotBalanceTypeService.modifyBalanceTypes(list,function(res){
					if(res){
						alert("修改成功！");
						closeandreflashEC(true,"balancetypeTable",false);
					}else{
						alert("修改失败！该费用结算方式已存在!");
						$('balanceName').select();
					}
				})
			}
			//删除
			function del()
			{
				var cotBalanceType = new CotBalanceType();
				var list = new Array();
			    cotBalanceType.id = ${"eId"}.value;
			    list.push(cotBalanceType);
			    var flag = window.confirm("是否确定删除?");
			    if(flag){
					cotBalanceTypeService.deleteBalanceTypes(list,function(res){
						alert("删除成功");
						closeandreflashEC(true,"balancetypeTable",true);
					})
			    }
			}
			
			//初始化表单验证字段
			function validForm()
			{
				appendattribute("balanceName","费用结算方式不能为空","Require");
			}
			
			//页面加载调用
			function initForm()
			{
				var id = $("eId").value;
				 //表单验证
				 validForm();
	  			 //加载表单
				 cotBalanceTypeService.getBalanceTypeById(parseInt(id),function(res){
				  	DWRUtil.setValues(res);
				 });
			}
		</script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
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
						<div style="width: 100%;margin-top:10px;">
							<label class="label_25" style="color: red;text-align:right">
								费用结算方式：
							</label>
							<input class="input_65" type="text" name="balanceName"
								id="balanceName" maxlength="20" />
						</div>
						<div>
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
						<a onclick="mod()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_modi.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="del()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_del.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="javascript:closeandreflashEC(true,'balancetypeTable',false);" 
							style="cursor: hand;">
						<img src="<%=webapp %>/common/images/_cancel.gif" border="0"
								height="21px" width="61px"></a>	
					<!-- 
						<button class="modBtn" id="modBtn" onclick="mod()">
							修改
						</button>
						<button class="delBtn" id="delBtn" onclick="del()">
							删除
						</button>
						<button class="cancelBtn"
							onclick="javascript:closeandreflashEC(true,'balancetypeTable',false);">
							关闭
						</button>
					-->
						<input type="hidden" name="eId" id="eId" value="<%=id%>">
					</div>
				<td>
		</table>

	</body>
</html>
