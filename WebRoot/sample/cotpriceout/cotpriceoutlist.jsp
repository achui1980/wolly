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
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotPriceOutService.js'></script>
		<script type="text/javascript">

//关闭详细信息
function closeDiv()
		{
		    $("priceOutDiv").style.display = "none";
		}
		
function  windowopen(obj)
{   
     if(obj == null)
	{
		var ids = getIds(); 
		if(ids.length == 0)
		{	
			alert("Please select a record");
			return;
		}
		else if(ids.length > 1)
		{
			alert("you can select only one record!")
			return;
		}
		else
			obj = ids[0].id;
	}else if(obj==0){
	    
	    var eleId=$("eleId").value;
	    openWindow('cotpriceout.do?method=add&eleId='+eleId);
	    return;
   } 
    closeDiv();
    var eId=$("eleId").value;
	openWindow('cotpriceout.do?method=modify&eleId='+eId);
}
 

 
//删除	   
function  del(eleId)
{
//添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	if(isPopedom == 0)//没有修改权限
	{
		alert("Sorry, you do not have Authority!");
		return;
	}
    var flag = window.confirm("are you sure to delete it?");
    if(flag){
        var cotElementsNew = new CotElementsNew();
		DWREngine.setAsync(false); 
		cotPriceOutService.getElementsNewById(parseInt(eleId),function(res){
			cotElementsNew = res;
		})
		 cotPriceOutService.delPriceOut(cotElementsNew,function(res){
			reflashLocal(true,"priceOutTable");
			$("priceOutDiv").style.display = "none";
			alert("Deleted successfully
!");
		})
		DWREngine.setAsync(true);  
	}else{
        return;
    }
     
}

//修改
function mod(eleId)
{
//添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
	if(isPopedom == 0)//没有修改权限
	{
		alert("Sorry, you do not have Authority!");
		return;
	}
	//表单验证
    var form = document.forms["queryPriceOutForm"];
	var b = false;
	b = Validator.Validate(form,1);
	if(!b)
	{
		return;
	}
	//表单验证结束
	var cotElementsNew = new CotElementsNew();
	DWREngine.setAsync(false); 
	cotPriceOutService.getElementsNewById(parseInt(eleId),function(res){
		cotElementsNew = res;
	})
	cotElementsNew.priceOutUint = $("priceOutUint").value;
	cotElementsNew.priceOut = $("priceOut").value;
	cotPriceOutService.addOrUpdatePriceOut(cotElementsNew,function(res){
		alert("Successfully modified
！");
		reflashLocal(true,"priceOutTable");
    })
	DWREngine.setAsync(true);     
}


//初始化表单验证字段
function validForm()
{
	 appendattribute("priceOutUint","Currency can not be empty
!","Require");
	 appendattribute("priceOut","Currency can not be empty, and must be numeric type
!","Double");
} 

//加载所属节点下拉框数据
function bindSel()
{
	cotPriceOutService.getCotCurrencyList(function(res){
	bindSelect('priceOutUint',res,'id','curNameEn');
	})
} 
 
function loadForm(eleId)
{    
    $("priceOutDiv").style.display = "block";
    DWREngine.setAsync(false); 
    validForm();
    bindSel();
    cotPriceOutService.getElementsNewById(parseInt(eleId),function(res){
		var obj = res;
	 	DWRUtil.setValues(obj);
	})
	DWREngine.setAsync(true); 
}

function initForm()
{
	cotPriceOutService.findRecord(function(res){
		if(res){
			$("addBtnDiv").style.display = "none";
		}
	})
}

 
</script>
	</head>
	<%
		String eleId = request.getParameter("eleId");
	%>
	<body onload="initForm()">

		<div class="toolbar">
			<div id="addBtnDiv" style="float: right">
				<button class="addBtn" onclick="windowopen(0)">
				<img src="<%=webapp %>/common/images/_add+.gif" height="20px" width="20px" border="0" alt="add records">
				</button>&nbsp;
			</div>
		</div>
		<e3t:table items="cotPriceOut" id="priceOutTable" var="ele"
			uri="cotpriceout.do?method=query" mode="ajax"
			noDataTip="No data to display!">
			<e3t:param name="eleId" value="${param.eleId}" />

			<e3t:column property="priceOutUint" title="Currency
" mappingItem="Mapping"></e3t:column>
			<e3t:column property="priceOut" title="Sale Price"></e3t:column>
			<e3t:column property="addTime" title="Date"></e3t:column>
			 
			<e3t:column property="op" title="Opration" sortable="false" style="text-align:center">
				<a href=# name="modLink" onclick="windowopen(${ele.id})"><img src="<%=webapp %>/common/images/_modify.gif" height="18px" width="18px" border="0" alt="Edit"></a>
				&nbsp;
				<a href=# name="delLink" onclick="del(${ele.id})"><img src="<%=webapp %>/common/images/_del-x_.gif" height="18px" width="18px" border="0" alt="Delete"></a>
			</e3t:column>
			<e3t:row>
				<e3t:attribute name="onmouseover"
					value="this.style.backgroundColor = '#CCCCFF'" />
				<e3t:attribute name="onmouseout"
					value="this.style.backgroundColor = ''" />
				<e3t:attribute name="onclick" value="loadForm(${ele.id})"></e3t:attribute>
			</e3t:row>
		</e3t:table>

		<div id="priceOutDiv" style="display: none">
			<div class="detail_nav">
				&nbsp;&nbsp;Details

			</div>
			<table style="width: 100%;">
				<tr>
					<td align="center">
						<form name="queryPriceOutForm" id="queryPriceOutForm" onsubmit="return false">
							<div style="width: 100%;">
 								<label class="label_12">
									Currency:
								</label>
								<select class="select_20" name="priceOutUint" id="priceOutUint" >
							    </select>
								<label class="label_12">
									Sale Price:
								</label>
								<input class="input_20" type="text" name="priceOut"
									id="priceOut" />
								 
 								<input type="hidden" name=eleId id="eleId" value="<%=eleId%>" />
 							</div>
						</form>
					</td>
				</tr>
				<tr>
					<td align="center">
						<div style="margin-left: 30%">
							<button class="modBtn" onclick="mod($(eleId).value)">Edit</button>
							<button class="delBtn" onclick="del($(eleId).value)">Delete</button>
							<button class="cancelBtn" onclick="closeDiv()">Close</button>
						</div>
					</td>
				</tr>
			</table>

		</div>
	</body>
</html>