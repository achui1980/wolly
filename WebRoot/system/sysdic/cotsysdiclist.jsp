<%@ page language="java" contentType="text/html; charset=utf-8"

    pageEncoding="utf-8" %>
<%
	String webapp=request.getContextPath();
%>
<%@ taglib uri="/e3/table/E3Table.tld" prefix="e3t" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="../../common.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotSysDicService.js'></script>
<script type="text/javascript">
//权限判断变量，对应于cot_module表中的URL字段
var url = "cotcity.do?method=query";
//关闭详细信息
function closeWindow()
		{
		    $("editDiv").style.display = "none";
		}
function windowopen(obj,dicTypeid)
{
	//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
		if(isPopedom == 0)//没有修改权限
		{
			alert("您没有修改权限");
			return;
		}
	if(obj == null)
	{
		var ids = getIds(); 
		if(ids.length == 0)
		{	
			alert("请选择一条记录");
			return;
		}
		else if(ids.length > 1)
		{
			alert("只能选择一条记录!")
			return;
		}
		else
			obj = ids[0].id;
	}else if(obj==0){
			openWindow('cotsysdic.do?method=add');
			return;
	}
	   closeWindow();

	openWindow('cotsysdic.do?method=queryDetail&id='+obj+'&dicTypeid='+dicTypeid);

} 

function windowopenAdd()
{
	openWindow('cotsysdic.do?method=add');
}
//初始化表单验证字段
function validForm()
{
	//appendattribute("cityName","城市名称不能为空","Require");
}
//加载详细信息
function initForm(id,dictypeid)
{
    $("editDiv").style.display = "block";
	DWRUtil.useLoadingMessage("正在加载数据，请等待..........."); 
	validForm();
	DWREngine.setAsync(false);
	bindDicTypeSel();
	${"sysdicid"}.value = id;
	var obj = new Object();
  	cotSysDicService.getSysDicById(parseInt(id),function(res){
  		obj = res;
    	DWRUtil.setValues(obj);
    });
    if(dictypeid == "0")//包装方式需要特殊处理
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
}

function bindDicTypeSel()
{
    cotSysDicService.getDicTypeList(function(res){
	   bindSelect('dicTypeid',res,'id','dicType');
  })
}
function mod()
{
	//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
		if(isPopedom == 0)//没有修改权限
		{
			alert("您没有修改权限");
			return;
		}
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
    list.push(cotSysDic)
    cotSysDicService.modifySysDic(list,function(res){
		alert("修改成功");
		reflashLocal(true,"sysdicTable");
		closeWindow();
	});	
     
}

function  del(id)
{
	//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
		if(isPopedom == 0)//没有修改权限
		{
			alert("您没有删除权限");
			return;
		}
    var flag = window.confirm("确定删除吗?");
    if(flag){
	    var obj = DWRUtil.getValues("queryCityForm");
	    var cotSysDic = new CotSysDic();
	    var list = new Array();
	    for(var p in obj)
	    {
	    	cotSysDic[p] = obj[p];
	    }
	    cotSysDic.id = id;
	    list.push(cotSysDic)
	    var result ;
	    DWREngine.setAsync(false); 
	    cotSysDicService.deleteSysDic(list,function(res){
	    	result = res;
		});	
		if(result == -1)
	    {
	    	alert("已有其它记录使用到该记录,无法删除")
	    	return; 
	    }
		alert("删除成功");
		reflashLocal(true,"sysdicTable");
		closeWindow();
		
    }else{
       return;
    }
    DWREngine.setAsync(true); 
    
}
function getIds()
{
	var checkCityIDObjs = document.getElementsByName("checkCityID");
	var list = new Array();
	for(var i=0; i<checkCityIDObjs.length; i++)
	{
		var cotSysDic = new CotSysDic();
		var chk = checkCityIDObjs[i];
		if(chk.checked)
		{
			cotSysDic.id = chk.value;
			list.push(cotSysDic);
		}
	}
	return list;
}
function deleteBatch()
{
	 var flag = window.confirm("确定删除?");
    if(flag){
    	var list = getIds();
    	if(list.length == 0)
    	{	
    		alert("请选择记录");
    		return;
    	}
	    cotSysDicService.deleteSysDic(list,function(res){
	    	var result=res;
			if(result == -1)
	    {
	    	alert("已有其它记录使用到该记录,无法删除")
	    	return; 
	    }
		alert("删除成功");
		reflashLocal(true,"sysdicTable");
		closeWindow();
		});	
    }else{
       return;
    }
}
function selectAll(){
  var checkCityIDsObj = document.getElementById("checkCityIDs");
  var checkCityIDObjs = document.getElementsByName("checkCityID");
  for(var i=0; i<checkCityIDObjs.length; i++){
     checkCityIDObjs[i].checked = checkCityIDsObj.checked;
 }
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
<body>
<table><tr><td ><div id="y">
</div></td></tr></table>

	<div class="toolbar">
			<div style="float: right">
				<button class="addBtn" onclick="windowopenAdd()">
				<img src="<%=webapp %>/common/images/_add+.gif" height="20px" width="20px" border="0" alt="添加记录">
				</button>&nbsp;
				<button class="modBtn" onclick="windowopen(null)">
				<img src="<%=webapp %>/common/images/_mod.jpg" height="25px" width="25px" border="0" alt="修改记录">
				</button>&nbsp;
				<button class="delBtn" onclick="deleteBatch()">
				<img src="<%=webapp %>/common/images/_del+.jpg" height="25px" width="25px" border="0" alt="删除记录">
				</button>&nbsp;
			</div>
		</div>
	<div style="width=90%">
	<e3t:table 
		items="cotSysdic" 
		id="sysdicTable" 
		var="sysdic" 
		uri="cotsysdic.do?method=query"  
		mode="ajax"
        noDataTip="没有记录" >
		<e3t:column property="chk" style="width:55px"  title="全选<input onclick='selectAll()' type='checkbox' id='checkCityIDs' />" sortable="false" >
      		<input type="checkbox" value="${sysdic.id}" name="checkCityID" id="checkbox-${sysdic.id}"/>
    	</e3t:column>
        <e3t:column property="dicTypeid" title="字典类别" mappingItem="dicTypeList"></e3t:column>
        <e3t:column property="dicName" title="属性"></e3t:column>
        <e3t:column property="dicValue" title="值"></e3t:column> 
		<e3t:column property="op" title="操作" sortable="false" style="text-align:center">
            <a href=# name="modLink" onclick="windowopen(${sysdic.id},${sysdic.dicTypeid})"><img src="<%=webapp %>/common/images/_modify.gif" height="18px" width="18px" border="0" alt="修改"></a>
			&nbsp;
			<a href=# name="delLink" onclick="del(${sysdic.id})"><img src="<%=webapp %>/common/images/_del-x_.gif" height="18px" width="18px" border="0" alt="删除"></a>
		</e3t:column>
		<e3t:row>
		　　<e3t:attribute name="onmouseover" value="this.style.backgroundColor = '#CCCCFF'" />
			<e3t:attribute name="onmouseout" value="this.style.backgroundColor = ''" />
			<e3t:attribute name="ondblclick" value="windowopen(${sysdic.id},${sysdic.dicTypeid})"></e3t:attribute>
			<e3t:attribute name="onmousedown" value="initForm(${sysdic.id},${sysdic.dicTypeid})"></e3t:attribute>
		</e3t:row>
	</e3t:table>
	</div>
	<br />
	<div>
		<div id="editDiv" style="display:none">
		<div class="detail_nav">
			&nbsp;&nbsp;详细信息
		</div>
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
				<br></td>
			</tr>
			<tr>
				<td align="center">
					<div style="margin-left: 30%">
						<button class="modBtn" onclick="mod()">修改</button>
						<button class="delBtn" onclick="del($(sysdicid).value)">删除</button>
						<button class="cancelBtn" onclick="closeWindow()">关闭</button>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<input type="hidden" name="sysdicid" id="sysdicid"  />
	</div>
</body>
</html>