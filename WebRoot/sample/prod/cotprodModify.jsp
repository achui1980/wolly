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
		<title>Product modifications page</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotProdService.js'></script>

		<script type="text/javascript">
 
//修改
function mod()
{
	//表单验证
    var form = document.forms["queryProdForm"];
	var b = false;
	b = Validator.Validate(form,1);
	if(!b)
	{
		return;
	}
	//表单验证结束
	var obj=DWRUtil.getValues("queryProdForm");
	var cotProd = new CotProd();
    var list = new Array();
    for(var p in obj)
    {
    	cotProd[p] = obj[p]
    }
    cotProd.id=DWRUtil.getValue("prodid");
    list.push(cotProd);
    cotProdService.modifyProd(list,function(res){
    	alert("Successfully modified！");
    	closeandreflash(true,"prodTable",false);
    });
}
//删除 
function  del()
{
    var flag = window.confirm("Are you Sure to remove this product information?");
    if(flag){
        var obj = DWRUtil.getValues("queryProdForm");
		var cotProd = new CotProd();
		var list=new Array();
		for(var p in obj){
				cotProd[p] = obj[p];
			} 
	     cotProd.id =  DWRUtil.getValue("prodid");
		 list.push(cotProd);
		 cotProdService.deleteProd(list,function(res){
			alert("Deleted successfully!");
			closeandreflash(true,"prodTable",false);
		})
    }else{
        return;
    }
}

//初始化表单验证字段
function validForm()
{
	appendattribute("prodName","The name of the product can not be empty!","Require");
	appendattribute("prodNo","Product number can not be empty!","Require");
}

function initForm()
{    
    DWRUtil.useLoadingMessage("Loading data, please wait..........."); 
    validForm();
   
	var id = $("prodid").value;
	cotProdService.getProdById(parseInt(id),function(res){
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
			<tr>
				<td align="center">
					<form name="queryProdForm" id="queryProdForm" onsubmit="return false">
						<div style="width: 100%;">
							<label class="label_12">
								Product name:
							</label>
							<input class="input_36" type="text" name="prodName" id="prodName" />

							<label class="label_12">
								Product Number:
							</label>
							<input class="input_36" type="text" name="prodNo" id="prodNo" />
						</div>
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div>
						<button class="modBtn" onclick="mod()" />Modify
						</button>
						<button class="delBtn" onclick="del()" />Delete
						</button>
						<button class="cancelBtn"
							onclick="javascript:closeandreflash(true,'prodTable',false);">Close
						</button>
					</div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="prodid" id="prodid" value="<%=id%>" />
	</body>
</html>