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
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotProdService.js'></script>
<script type="text/javascript">
 
//关闭详细信息
function closeWindow()
		{
		    $("editDiv").style.display = "none";
		}

function windowopen(obj)
{
	//添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
	if(isPopedom == 0)//没有修改权限
	{
		alert("Sorry, you do not have Authority!");
		return;
	}
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
			alert("Choose only one record!")
			return;
		}
		else
			obj = ids[0].id;
	}else if(obj==0){
		openWindow('cotprod.do?method=add');
		return;
   } 
    closeWindow();
	openWindow('cotprod.do?method=queryDetail&id='+obj);
}
 

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
    	reflashLocal(true,"prodTable");
    });
}
//删除 
function  del(id)
{
	//添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	if(isPopedom == 0)//没有删除权限
	{
		alert("Sorry, you do not have Authority!");
		return;
	}
    var flag = window.confirm("Sure you remove this product information?");
    if(flag){
        var obj = DWRUtil.getValues("queryProdForm");
		var cotProd = new CotProd();
		var list=new Array();
		for(var p in obj){
				cotProd[p] = obj[p];
			} 
	     cotProd.id = id;
		 list.push(cotProd);
		 cotProdService.deleteProd(list,function(res){
			alert("Deleted successfully!");
			reflashLocal(true,"prodTable");
			closeWindow();
		})
    }else{
        return;
    }
}

//初始化表单验证字段
function validForm()
{
	appendattribute("prodName","Product name can not be empty!","Require");
	appendattribute("prodNo","Product ID can not be empty!","Require");
}

function loadForm(id)
{
    $("editDiv").style.display = "block";
    DWRUtil.useLoadingMessage("Loading data, please wait..........."); 
    validForm();
    $("prodid").value=id;
	cotProdService.getProdById(parseInt(id),function(res){
	var obj = res;
	DWRUtil.setValues(obj);
	})
}
       
//
function getIds()
{
	var checkProdIDObjs = document.getElementsByName("checkProdID");
	var list = new Array();
	for(var i=0; i<checkProdIDObjs.length; i++)
	{
		var cotProd = new CotProd();
		var chk = checkProdIDObjs[i];
		if(chk.checked)
		{
			cotProd.id = chk.value;
			list.push(cotProd);
		}
	}
	return list;
}
//
function deleteBatch()
{
    var list = getIds();
    if(list.length == 0)
    	{	
    		alert("Please select records");
    		return;
    	}
	var flag = window.confirm("Are you sure to delete?");
    if(flag){ 
	           var list = getIds();
	           cotProdService.deleteProd(list,function(res){
			        alert("Deleted successfully!");
					reflashLocal(true,"prodTable");
			        closeWindow();
				});	
			 
    }else{
       return;
    }
}
//
function selectAll(){
  var checkProdIDsObj = document.getElementById("checkProdIDs");
  var checkProdIDObjs = document.getElementsByName("checkProdID");
  for(var i=0; i<checkProdIDObjs.length; i++){
     checkProdIDObjs[i].checked = checkProdIDsObj.checked;
      
  }
}
 	   
</script>
</head>
<body>
   <div class="toolbar">
			<div style="float: right">
				<button class="addBtn" onclick="windowopen(0)">
				<img src="<%=webapp %>/common/images/_add+.gif" height="20px" width="20px" border="0" alt="Add record">
				</button>&nbsp;
				<button class="modBtn" onclick="windowopen(null)">
				<img src="<%=webapp %>/common/images/_mod.jpg" height="25px" width="25px" border="0" alt="Revision History">
				</button>&nbsp;
				<button class="delBtn" onclick="deleteBatch()">
				<img src="<%=webapp %>/common/images/_del+.jpg" height="25px" width="25px" border="0" alt="Delete Records">
				</button>&nbsp;
			</div>
		</div>
   <e3t:table 
		items="cotProd" 
		id="prodTable" 
		var="prod" 
		uri="cotprod.do?method=query"  
		mode="ajax"
        noDataTip="No record!" >
        <e3t:column property="chk" style="width:40px;text-align:center"  title="Select All<input onclick='selectAll()' type='checkbox' id='checkProdIDs' />" sortable="false" >
      		<input type="checkbox" value="${prod.id}" name="checkProdID" id="checkbox-${prod.id}"/>
    	</e3t:column>
		<e3t:column property="prodName" title="Product name"></e3t:column>
		<e3t:column property="prodNo" title="Product Number"></e3t:column>
		<e3t:column property="op" title="Operation" sortable="false" style="text-align:center">
            <a href=# name="modLink" onclick="windowopen(${prod.id})"><img src="<%=webapp %>/common/images/_modify.gif" height="18px" width="18px" border="0" alt="Modify"></a>
			&nbsp;
			<a href=# name="delLink" onclick="del(${prod.id})"><img src="<%=webapp %>/common/images/_del-x_.gif" height="18px" width="18px" border="0" alt="Delete"></a>
		</e3t:column>
		<e3t:row>
				<e3t:attribute name="onmouseover" value="this.style.backgroundColor = '#CCCCFF'" />
				<e3t:attribute name="onmouseout" value="this.style.backgroundColor = ''" />
				<e3t:attribute name="onclick" value="loadForm(${prod.id})"></e3t:attribute>
			　　<e3t:attribute name="ondblclick" value="windowopen(${prod.id})"></e3t:attribute>
		</e3t:row>
	</e3t:table>
	<br />
    <div id="editDiv" style="display:none">
		<div class="detail_nav">
			&nbsp;&nbsp;Details
		</div>
	    <table style="width: 100%;">
				<tr>
					<td align="center">
						<form name="queryProdForm" id="queryProdForm" onsubmit="return false">
							<div style="width: 100%;">
								<label class="label_12">Product name:</label>
								<input class="input_36" type="text" name="prodName" id="prodName" />
								 
								<label class="label_12">Product Number:</label>
								<input class="input_36" type="text" name="prodNo" id="prodNo" />
							</div>
						</form>
					</td>
				</tr>
			    <tr>
					<td align="center">
						<div style="margin-left: 30%">
							<button class="modBtn" onclick="mod()">Modify</button>
							<button class="delBtn" onclick="del($(prodid).value)">Delete</button>
							<button class="cancelBtn" onclick="closeWindow()">Close</button>
						</div>
					</td>
			   </tr>
			</table>
			<input type="hidden" name="prodid" id="prodid" />
	</div>		
</body>
</html>