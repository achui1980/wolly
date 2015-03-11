<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<%@ taglib uri="/e3/table/E3Table.tld" prefix="e3t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>其他字典添加页面</title>
<jsp:include page="../../common.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotSysDicService.js'></script>
<script type="text/javascript">
//权限判断变量，对应于cot_module表中的URL字段
var url = "cotcity.do?method=query";
function add()
{
//表单验证
	var form = document.forms["queryCityForm"];
	var b = false;
	b = Validator.Validate(form,1);
	if(!b)
	{
		return; 
	}
//表单验证结束
    var obj = DWRUtil.getValues("queryCityForm");
    var cotSysDic = new CotSysDic();
    var list = new Array();
    for(var p in obj)
    {
    	cotSysDic[p] = obj[p];
    }
    if(cotSysDic.dicTypeid == 0)
    	cotSysDic.dicValue = $("ibType").value+";"+$("mbType").value+";"+$("obType").value;
    list.push(cotSysDic);
    
    DWREngine.setAsync(false); 
    //添加城市
    cotSysDicService.addSysDic(list,function(res){
		alert("添加成功");
		closeandreflash(true,"sysdicTable",false);
	});	 
	DWREngine.setAsync(true); 
}

　
//初始化表单验证字段
function validForm()
{
	//appendattribute("cityName","城市名称不能为空","Require");
}
function bindDicTypeSel()
{
    cotSysDicService.getDicTypeList(function(res){
	   bindSelect('dicTypeid',res,'id','dicType');
  })
}
function initForm()
{
	DWRUtil.useLoadingMessage("正在加载数据，请等待..........."); 
	validForm();
	bindDicTypeSel();
}
function displaytype(obj)
{
	if(obj.value == "0")//包装方式需要特殊处理
    {
    	$("commonValue").style.display = "none";
    	$("composeType").style.display = "block";
    }
    else
    {
    	$("commonValue").style.display = "block";
    	$("composeType").style.display = "none";
    }
}
　
</script>
	</head>
	　
	<body onload="initForm()">
		<table style="width: 100%;">
			<tr>
				<td align="center">
					<form name="queryCityForm" id="queryCityForm">
						<div style="width: 100%;">
							<div>
								<label class="label_13">类别</label>
								<select class="select_85" id="dicTypeid" name="dicTypeid" onchange="displaytype(this)"></select>
							</div>
							<div>
								<label class="label_13">属性</label>
								<input class="input_85" type="text" name="dicName" id="dicName"/>
							</div>
							<div id="commonValue">
								<label class="label_13">值</label>
								<input class="input_85" type="text" name="dicValue" id="dicValue"/>
							</div>
							<div id="composeType" style="display:none">
								<label class="label_13">内箱</label>
								<input class="input_85" type="text" name="ibType" id="ibType"/>
								<label class="label_13">中箱</label>
								<input class="input_85" type="text" name="mbType" id="mbType"/>
								<label class="label_13">外箱</label>
								<input class="input_85" type="text" name="obType" id="obType"/>
							</div>
						</div>
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
				    <br/>
					 <div>
						<button class="confirmBtn" onclick="add()">保存</button>
						<button class="cancelBtn" 
						        onclick="javascript:closeandreflash(true,'sysdicTable',false);">关闭
						</button>
					</div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="sysdicid" id="sysdicid" 　/>
	</body>
</html>