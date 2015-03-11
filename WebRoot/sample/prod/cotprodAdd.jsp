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
		<title>Add a product page</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotProdService.js'></script>

		<script type="text/javascript">
 
//添加
function add()
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
	var obj = DWRUtil.getValues("queryProdForm");
		var cotProd = new CotProd();
		var list=new Array();
		for(var p in obj){
				cotProd[p] = obj[p];
			} 
	     list.push(cotProd);
	     var isExist=false;
		 DWREngine.setAsync(false); 
	     cotProdService.findExistByName(cotProd.prodName,function(res){
	    	isExist = res;
	     });
	     //判断是否同名
	     if(isExist)
	     {
	    	alert("The same name already exists！");
	    	return;
	     }
	      
	     //添加文件
	      cotProdService.addProd(list,function(res){
			alert("Successfully added");
			closeandreflash(true,"prodTable",false);
		 });	 
		 DWREngine.setAsync(true); 
}
 

//初始化表单验证字段
function validForm()
{
	appendattribute("prodName","Product Name can not be empty!","Require");
	appendattribute("prodNo","Product ID can not be empty!","Require");
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
					<br />
					<div>
						<button class="confirmBtn" onclick="add()">Save</button>
						<button class="cancelBtn"
							onclick="javascript:closeandreflash(true,'prodTable',false);">Close
						</button>
					</div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="prodid" id="prodid" />
	</body>
</html>