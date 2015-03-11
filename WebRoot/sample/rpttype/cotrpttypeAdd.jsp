<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>报表类型添加页面</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotRptTypeService.js'></script>

		<script type="text/javascript">
 
//添加
function add()
{
	//表单验证
    var form = document.forms["queryRptTypeForm"];
	var b = false;
	b = Validator.Validate(form,1);
	if(!b)
	{
		return;
	}
	//表单验证结束
	var obj = DWRUtil.getValues("queryRptTypeForm");
		 var cotRptType = new CotRptType();
		 var list = new Array();
	     for(var p in obj)
	     {
	    	cotRptType[p] = obj[p]
   		 }
	     list.push(cotRptType);
	     var isExist=false;
		 DWREngine.setAsync(false); 
	     cotRptTypeService.findExistByName(cotRptType.rptName,function(res){
	    	isExist = res;
	     });
	     //判断是否同名
	     if(isExist)
	     {
	    	alert("已存在相同报表类型名称！");
	    	return;
	     }
	      
	     //添加文件
	      cotRptTypeService.addRptType(list,function(res){
			alert("添加成功");
			closeandreflashEC(true,"rpttypeTable",false);
		 });	 
		 DWREngine.setAsync(true); 
}
 

//初始化表单验证字段
function validForm()
{
	appendattribute("rptName","报表类型名称不能为空!","Require");
}

function initForm()
{    
    validForm();
}


 
</script>
		<style type="text/css">
</style>
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
					<form name="queryRptTypeForm" id="queryRptTypeForm" onsubmit="return false">
						<div style="width: 100%;margin-top:10px;">
							<label class="label_25" style="color: red">报表类型名称：</label>
							<input class="input_65" type="text" name="rptName" id="rptName" />
						</div> 
						<div style="width: 100%;">
							<label class="label_25">备  注：</label>
							<textarea  class="input_65" name="rptRemark" id="rptRemark" rows="3" cols="50" ></textarea>
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
						<a onclick="javascript:closeandreflashEC(true,'rpttypeTable',false);" 
						 		style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_cancel.gif" border="0"
								height="21px" width="61px"></a>	
					<!-- 
						<button class="confirmBtn" onclick="add()">保存</button>
						<button class="cancelBtn"
							onclick="javascript:closeandreflashEC(true,'rpttypeTable',true);">关闭
						</button>
					-->
					</div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="rpttypeid" id="rpttypeid" />
	</body>
</html>