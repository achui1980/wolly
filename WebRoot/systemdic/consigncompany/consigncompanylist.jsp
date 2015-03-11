<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='dwr/interface/cotConsignCompanyService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/systemdic/consigncompany/js/consigncompanylist.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

	</head>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cotconsigncompany.do?method=query"; 
	
	function windowopen(obj)
	{
		
		if(obj == null)
		{
			var ids = getIds(); 
			if(ids.length == 0) {	
				Ext.Msg.alert("Message","Please select a record!");
				return;
			}
			else if(ids.length > 1) {
				Ext.Msg.alert("Message","Choose only one record!")
				return;
			}
			else
				obj = ids[0].id;
		} 
		 
		openWindowBase(300,750,'cotconsigncompany.do?method=modify&id='+obj);
	
	} 
	
	//添加 
	function windowopenAdd()
	{
		openWindowBase(300,750,'cotconsigncompany.do?method=modify');
	}
	
	 
	
	function  del(id)
	{
		
	    Ext.MessageBox.confirm('Message', 'Delete this order do?', function(btn){
	    	if (btn == 'yes'){
	    		var obj = DWRUtil.getValues("queryConsignCompanyForm");
			    var cotConsignCompany = new CotConsignCompany();
			    var list = new Array();
			    for(var p in obj)
			    {
			    	cotConsignCompany[p] = obj[p];
			    }
			    cotConsignCompany.id = id;
			    list.push(cotConsignCompany)
			    cotConsignCompanyService.deleteConsignCompany(list,function(res){
			    })	
				 
				Ext.Msg.alert("Message","Deleted successfully!");
				reloadGrid('consignGrid');
	    	}else{
	    		return;
	    	} 
	    });
	    
	}
	function getIds()
	{
		var list = Ext.getCmp("consignGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list,function(item){
			var cotConsignCompany = new CotConsignCompany();
			cotConsignCompany.id = item.id
			res.push(cotConsignCompany);
		});
		return res;
	}
	
	function deleteBatch()
	{
         var list = getIds();
    	 if(list.length == 0)
    	 {	
    		 Ext.Msg.alert("Message","Please select a record!");
    		 return;
    	 }
    	 Ext.MessageBox.confirm('Message', 'Delete this order do?', function(btn){
    	 		if (btn == 'yes'){
    	 			cotConsignCompanyService.deleteConsignCompany(list,function(res){
				    })
				    
					Ext.Msg.alert("Message","Deleted successfully!");
					reloadGrid('consignGrid');
    	 		}
    	 });
	}
	
    </script>
	<body >
	     
		
	</body>
</html>
