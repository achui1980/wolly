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
<title>其他字典修改页面</title>
<jsp:include page="../../common.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotSysDicService.js'></script>
<script type="text/javascript">
 //权限判断变量，对应于cot_module表中的URL字段
var url = "cotcity.do?method=query";
function  del()
{
    var flag = window.confirm("确定删除此地区吗?");
    if(flag){
	     var obj = DWRUtil.getValues("queryCityForm");
	    var cotSysDic = new CotSysDic();
	    var list = new Array();
	    for(var p in obj)
	    {
	    	cotSysDic[p] = obj[p];
	    }
	    cotSysDic.id = ${"sysdicid"}.value;
	    list.push(cotCity)
	    cotSysDicService.deleteSysDic(list,function(res){
			result = res;
			if(result == -1)
		    {
		    	alert("已有其它记录使用到该记录,无法删除")
		    	return; 
		    }
			alert("删除成功");
			closeandreflash(true,"sysdicTable",false);
		});	
		 
    }else{
       return;
    }
    
}

 

function mod()
{
    var obj = DWRUtil.getValues("queryCityForm");
    var cotSysDic = new CotSysDic();
    var list = new Array();
    for(var p in obj)
    {
    	cotSysDic[p] = obj[p];
    }
    cotSysDic.id = ${"sysdicid"}.value;
     if(cotSysDic.dicTypeid == 0)
    	cotSysDic.dicValue = $("ibType").value+";"+$("mbType").value+";"+$("obType").value;
    list.push(cotCity)
     
    cotSysDicService.modifySysDic(list,function(res){
		alert("修改成功");
		closeandreflash(true,"sysdicTable",false);
	});	
    DWREngine.setAsync(true);  
}

//初始化表单验证字段
function validForm()
{
	//appendattribute("cityName","城市名称不能为空","Require");
}
function initForm()
{
	DWRUtil.useLoadingMessage("正在加载数据，请等待..........."); 
	DWREngine.setAsync(false); 
	validForm();
	bindDicTypeSel();
	var id = $("sysdicid").value;
	var typeid = document.getElementById("typeid").value;
  	cotSysDicService.getSysDicById(parseInt(id),function(res){
  	var obj = res;
  	DWRUtil.setValues(obj);
  	if(typeid == "0")//包装方式需要特殊处理
    {
    	var arr = obj.dicValue.split(";");
    	$("commonValue").style.display = "none";
    	$("composeType").style.display = "block";
    	$("ibType").value = arr[0];
    	$("mbType").value = arr[1];
    	$("obType").value = arr[2];
    	
    }
    else
    {
    	$("commonValue").style.display = "block";
    	$("composeType").style.display = "none";
    }
  	DWREngine.setAsync(true); 
  });
}
function bindDicTypeSel()
{
    cotSysDicService.getDicTypeList(function(res){
	   bindSelect('dicTypeid',res,'id','dicType');
  })
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
	<%
		String id = request.getParameter("id");
		String typeid = request.getParameter("dicTypeid");	
	
	%>
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
					<div>
						<button class="modBtn" onclick="mod()"/>修改</button>
				        <button class="delBtn" onclick="del()"/>删除</button>
						<button class="cancelBtn" 
						        onclick="javascript:closeandreflash(true,'sysdicTable',false);">关闭
						</button>
					</div>
				</td>
			</tr>	
		</table>
		<input type="hidden" name="sysdicid" id="sysdicid" value="<%=id%>" />
		<input type="hidden" name="typeid" id="typeid" value="<%=typeid%>" />
	</body>
</html>