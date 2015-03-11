<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>报表类型修改页面</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotRptTypeService.js'></script>

		<script type="text/javascript">
 
//修改
function mod()
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
	var obj=DWRUtil.getValues("queryRptTypeForm");
	var cotRptType = new CotRptType();
    var list = new Array();
    for(var p in obj)
    {
    	cotRptType[p] = obj[p]
    }
    cotRptType.id=DWRUtil.getValue("rpttypeid");
    list.push(cotRptType);
    cotRptTypeService.modifyRptType(list,function(res){
    	alert("修改成功！");
    	closeandreflashEC(true,"rpttypeTable",false);
    });
}
//删除 
function  del()
{
    var flag = window.confirm("确定删除吗?");
    if(flag){
        DWREngine.setAsync(false);
        var aa = 0;
        var obj = DWRUtil.getValues("queryRptTypeForm");
		var cotRptType = new CotRptType();
	    var list = new Array();
	    for(var p in obj)
	    {
	    	cotRptType[p] = obj[p]
	    }
	     cotRptType.id =  DWRUtil.getValue("rpttypeid");
		 list.push(cotRptType);
		 cotRptTypeService.deleteRptType(list,function(res){
				  aa=res;
		 })
		if(aa==-1){
		   alert("删除失败！报表类型已被引用！")
		   return;
		}else{
			alert("删除成功!");
			closeandreflashEC(true,"rpttypeTable",false);
		}
		DWREngine.setAsync(true); 
    }else{
        return;
    }
}

//初始化表单验证字段
function validForm()
{
	appendattribute("rptName","报表类型名称不能为空!","Require");
}

function initForm()
{    
    DWRUtil.useLoadingMessage("正在加载数据，请等待..........."); 
    validForm();
   
	var id = $("rpttypeid").value;
	cotRptTypeService.getRptTypeById(parseInt(id),function(res){
		var obj = res;
		DWRUtil.setValues(obj);
	})
}


 
</script>
		<style type="text/css">
</style>
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
						<a onclick="mod()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_modi.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="del()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_del.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="javascript:closeandreflashEC(true,'rpttypeTable',false);" 
							style="cursor: hand;">
						<img src="<%=webapp %>/common/images/_cancel.gif" border="0"
								height="21px" width="61px"></a>	
					<!-- 
						<button class="modBtn" onclick="mod()" />修改
						</button>
						<button class="delBtn" onclick="del()" />删除
						</button>
						<button class="cancelBtn"
							onclick="javascript:closeandreflashEC(true,'rpttypeTable',true);">关闭
						</button>
					-->
					</div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="rpttypeid" id="rpttypeid" value="<%=id%>" />
	</body>
</html>