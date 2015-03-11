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
		<title>Sale Price Edit Page</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotPriceOutService.js'></script>

		<script type="text/javascript">
 
//修改
function mod(eleId)
{
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
		closeandreflash(true,"priceOutTable",false); 
    })
	DWREngine.setAsync(true);   
}
//删除 
function  del(eleId)
{
    var flag = window.confirm("are you sure to delete it?");
    if(flag){
        var cotElementsNew = new CotElementsNew();
		DWREngine.setAsync(false); 
		cotPriceOutService.getElementsNewById(parseInt(eleId),function(res){
			cotElementsNew = res;
		})
		 cotPriceOutService.delPriceOut(cotElementsNew,function(res){
			alert("Deleted successfully
!");
			closeandreflash(true,"priceOutTable",false);
		})
		DWREngine.setAsync(true); 
    }else{
        return;
    }
}

//初始化表单验证字段
function validForm()
{
	appendattribute("priceOutUint","Currency can not be null
!","Require");
	appendattribute("priceOut","Sale Price can not be null","Double");
}

//加载所属节点下拉框数据
function bindSel()
{
	cotPriceOutService.getCotCurrencyList(function(res){
	bindSelect('priceOutUint',res,'id','curNameEn');
	})
} 
 
function initForm()
{    
    DWREngine.setAsync(false);  
    validForm();
    bindSel();
    var eleId = $("eleId").value;
    cotPriceOutService.getElementsNewById(parseInt(eleId),function(res){
		var obj = res;
	 	DWRUtil.setValues(obj);
	})
    DWREngine.setAsync(true); 
}

 
</script>
		<style type="text/css">
</style>
	</head>
	<%
		String eleId = request.getParameter("eleId");
	%>
	<body onload="initForm()">
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
							<input class="input_20" type="text" name="priceOut" id="priceOut" />
							 
							<input type="hidden" name=eleId id="eleId" value="<%=eleId%>" />
						</div>
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div>
						<button class="modBtn" onclick="mod($(eleId).value)" />Edit
						</button>
						<button class="delBtn" onclick="del($(eleId).value)" />Delete
						</button>
						<button class="cancelBtn"
							onclick="javascript:closeandreflash(true,'priceOutTable',false);">Close
						</button>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>