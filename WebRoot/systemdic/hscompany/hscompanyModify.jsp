<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报关行信息修改页面</title>
<jsp:include page="../../extcommon.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotHsCompanyService.js'></script>
<script type='text/javascript' src='<%=webapp%>/systemdic/hscompany/js/hscompanyEdit.js'></script>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cothscompany.do?method=query";
	
	 
	
	function mod()
	{
	    var obj = DWRUtil.getValues("queryHsCompanyForm");
	    var cotHsCompany = new CotHsCompany();
	    for(var p in obj)
	    {
	    	cotHsCompany[p] = obj[p];
	    }
	    var id = ${"hscompanyid"}.value;
	    if(id=="null" || id=="")
	    {
	    	
	    	cotHsCompany.id = null;
	    	cotHsCompanyService.addHsCompany(cotHsCompany,function(res){
	    	Ext.Msg.alert("提示信息","添加成功!");
			closeandreflashEC(true,"hsGrid",false);
			})
	    }
	    else
	    {
	    	cotHsCompany.id = ${"hscompanyid"}.value;
		    cotHsCompanyService.modifyHsCompany(cotHsCompany,function(res){
				Ext.Msg.alert("提示信息","修改成功!");
				closeandreflashEC(true,"hsGrid",false);
			})
		}
	}
	
	function initForm()
	{
		var id = $("hscompanyid").value;
		if(id == "null" || id == "")
			return;
	  	cotHsCompanyService.getHsCompanyById(parseInt(id),function(res){
		  	var obj = res;
		  	DWRUtil.setValues(obj);
		})
	}
 
 
</script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body>
		<input type="hidden" name="hscompanyid" id="hscompanyid" value="<%=id%>" />
	</body>
</html>