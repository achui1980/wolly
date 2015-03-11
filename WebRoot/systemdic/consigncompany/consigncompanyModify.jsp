<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
	String id = request.getParameter("id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Agent Edit Page</title>
<jsp:include page="../../extcommon.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotConsignCompanyService.js'></script>
<script type='text/javascript' src='<%=webapp%>/systemdic/consigncompany/js/consigncompanyEdit.js'></script>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cotconsigncompany.do?method=query";
	function mod()
	{
	    var obj = DWRUtil.getValues("queryConsignCompanyForm");
	    var cotConsignCompany = new CotConsignCompany();
	    for(var p in obj)
	    {
	    	cotConsignCompany[p] = obj[p];
	    }
	    var id = ${"consigncompanyid"}.value;
	    if(id == "null" || id == "")
	    {
	    	cotConsignCompanyService.addConsignCompany(cotConsignCompany,function(res){
				Ext.Msg.alert("Message", "Save successful！");
				closeandreflashEC(true,"consignGrid",false);
			})
	    }
	   else
	   {
	   		cotConsignCompany.id = id;
		    cotConsignCompanyService.modifyConsignCompany(cotConsignCompany,function(res){
				Ext.Msg.alert("Message","Successfully modified!");
				closeandreflashEC(true,"consignGrid",false);
			})
	   }
	}
	
	function initForm()
	{
		var id = $("consigncompanyid").value;
		if(id == "null" || id=='')
			return ;
	  	cotConsignCompanyService.getConsignCompanyById(parseInt(id),function(res){
		  	var obj = res;
		  	DWRUtil.setValues(obj);
		})
	}
 
</script>
	</head>

	<body>		
		<input type="hidden" name="consigncompanyid" id="consigncompanyid" value="<%=id %>" />
	</body>
</html>