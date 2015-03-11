<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='dwr/interface/cotTrailCarService.js'></script>
		<!-- 导入ECTable -->
		<script type='text/javascript' src='<%=webapp%>/systemdic/trailcar/js/trailcarlist.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

	</head>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cottrailcar.do?method=query"; 
	
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
				Ext.Msg.alert("提示信息","只能选择一条记录");
				return;
			}
			else
				obj = ids[0];
		} 
		openWindowBase(260,650,'cottrailcar.do?method=add&id='+obj);
	} 
	
	//添加 
	function windowopenAdd()
	{
		openWindowBase(260,650,'cottrailcar.do?method=add');
	}
	
	 
	
	function  del(id)
	{
		//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
		if(isPopedom == 0)//没有删除权限
		{
			Ext.Msg.alert("提示信息","您没有删除权限!");
			return;
		}  
	    var flag = window.confirm("确定删除?");
	    if(flag){
		    var ids = new Array();
		    ids.push(parseInt(id));
		    cotTrailCarService.deleteTrailCarByList(ids,function(res){
		    	Ext.Msg.alert("提示信息","删除成功!");
				reloadGrid('trailGrid');
		    });	
	    }
	    
	}
	
	function getIds()
	{
		var list = Ext.getCmp("trailGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list,function(item){
			res.push(item.id);
		});
		return res;
	}
	
	
	
	function deleteBatch()
	{
         var ids = getIds();
    	 if(ids.length == 0)
    	 {	
    	 	 Ext.Msg.alert("提示信息","请选择记录!");
    		 return;
    	 }
		 var flag = window.confirm("确定删除?");
	     if(flag){
		    cotTrailCarService.deleteTrailCarByList(ids,function(res){
		    	Ext.Msg.alert("提示信息","删除成功");
				reloadGrid('trailGrid');
		    });
	    }
	}
	
	 

 
    </script>
	<body >
	     	
	</body>
</html>
