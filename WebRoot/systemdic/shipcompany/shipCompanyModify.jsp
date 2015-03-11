<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>船运公司修改页面</title>
<jsp:include page="../../extcommon.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotShipCompanyService.js'></script>
<script type='text/javascript' src='<%=webapp%>/systemdic/shipcompany/js/shipcompanyEdit.js'></script>
<script type="text/javascript">
	
	 
	
	function mod()
	{
	    var obj = DWRUtil.getValues("queryForm");
	    var cotShipCompany = new CotShipCompany();
	    for(var p in obj)
	    {
	    	cotShipCompany[p] = obj[p];
	    }
	    var id = ${"mainId"}.value;
	    var list = new Array();
	    if(id == "null")
	    {
	    	list.push(cotShipCompany);
		    cotShipCompanyService.addTypeList(list,function(res){
		    	Ext.Msg.alert("提示信息","添加成功!");
				closeandreflashEC(true,"shipGrid",false);
			})
	    }
	    else
	    {
		    cotShipCompany.id = ${"mainId"}.value;
		    
		    list.push(cotShipCompany);
		    cotShipCompanyService.modifyTypeList(list,function(res){
		   		Ext.Msg.alert("提示信息","修改成功!");
				closeandreflashEC(true,"shipGrid",false);
			})
		}
	    
	}

	
	function initForm()
	{
		var id = $("mainId").value;
		if(id != "null")
		{
		  	cotShipCompanyService.getShipCompanyById(parseInt(id),function(res){
			  	var obj = res;
			  	DWRUtil.setValues(obj);
			})
		}
	}
 
 
</script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body>
		
	
		<input type="hidden" name="mainId" id="mainId" value="<%=id%>" />
	</body>
</html>