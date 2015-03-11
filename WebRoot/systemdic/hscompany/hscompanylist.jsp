<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='dwr/interface/cotHsCompanyService.js'></script>
		<!-- 导入ECTable -->
		<script type='text/javascript' src='<%=webapp%>/systemdic/hscompany/js/hscompanylist.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

	</head>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cothscompany.do?method=query"; 
	
	function windowopen(obj)
	{
		//添加权限判断
			var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
			if(isPopedom == 0)//没有修改权限
			{
				Ext.Msg.alert("提示信息","您没有修改权限!");
				return;
			}
		if(obj == null)
		{
			var ids = getIds(); 
			if(ids.length == 0) {	
				Ext.Msg.alert("提示信息","请选择一条记录!");
				return;
			}
			else if(ids.length > 1) {
				Ext.Msg.alert("提示信息","只能选择一条记录!");
				return;
			}
			else
				obj = ids[0].id;
		} 
		 
		openWindowBase(260,400,'cothscompany.do?method=modify&id='+obj);
	
	} 
	
	//添加 
	function windowopenAdd()
	{
		openWindowBase(260,400,'cothscompany.do?method=modify');
	}
	
	 
	
	function  del(id)
	{
		//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
		if(isPopedom == 0)//没有删除权限
		{
			Ext.Msg.alert("提示信息","您没有删除权限");
			return;
		}  
	    var flag = window.confirm("确定删除?");
	    if(flag){
	    	var cotHsCompany = new CotHsCompany();
	    	var list = new Array();
		    cotHsCompany.id = id;
		    list.push(cotHsCompany)

		    cotHsCompanyService.deleteHsCompany(list,function(res){
		    Ext.Msg.alert("提示信息","删除成功");
				reloadGrid('hsGrid');
		    })
			
	    }
	}
	function getIds()
	{
		var list = Ext.getCmp("hsGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list,function(item){
			var cotHsCompany = new CotHsCompany();
			cotHsCompany.id = item.id
			res.push(cotHsCompany);
		});
		return res;
	}
	function deleteBatch()
	{
         var list = getIds();
    	 if(list.length == 0)
    	 {	
    	 	Ext.Msg.alert("提示信息","请选择记录!");
    		 return;
    	 }
		 var flag = window.confirm("确定删除?");
	     if(flag){
		    cotHsCompanyService.deleteHsCompany(list,function(res){
		    })
		    Ext.Msg.alert("提示信息","删除成功!");
			reloadGrid('hsGrid');
	    }else{
	       return;
	    }
	}
	
	 

 
    </script>
	<body >
	     	
	</body>
</html>
