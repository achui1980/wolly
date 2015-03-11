<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<!-- 导入公共js和css -->
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotShipCompanyService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/systemdic/shipcompany/js/shipcompanylist.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<script type="text/javascript">
	
		//打开产品分类添加页面
		function windowopenAdd(){
			//添加权限判断
			var isPopedom = getPopedomByOpType(vaildUrl,"ADD");
			if(isPopedom == 0)//没有添加权限
			{
				Ext.Msg.alert("提示信息","您没有添加权限");
				return;
			}
			openWindowBase(260,400,'cotshipcompany.do?method=modify');
		}
	
		function windowopen(obj)
		{
			//添加权限判断
			var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
			if(isPopedom == 0)//没有修改权限
			{
				Ext.Msg.alert("提示信息","您没有修改权限");
				return;
			}
			if(obj == null)
			{
				var ids = getIds(); 
				if(ids.length == 0)
				{	
					Ext.Msg.alert("提示信息","请选择一条记录");
					return;
				}
				else if(ids.length > 1)
				{
					Ext.Msg.alert("提示信息","只能选择一条记录");
					return;
				}
				else
					obj = ids[0].id;
			} 
		    openWindowBase(260,400,'cotshipcompany.do?method=modify&id='+obj);
		} 
	
	    //删除
		function del(id)
		{
			var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
			if(isPopedom == 0)//没有删除权限
			{
				Ext.Msg.alert("提示信息","您没有删除权限");
				return;
			}
			var cotShipCompany = new CotShipCompany();
			var list = new Array();
		    cotShipCompany.id = id;
		    list.push(cotShipCompany)
		    var flag = window.confirm("是否确定删除?");
		    if(flag){
		    	 //查询单是否被删除
				cotShipCompanyService.getShipCompanyById(id,function(res){
					if(res!=null){
						cotShipCompanyService.deleteList(list,function(res){
							result = res;
							if(result == -1){
								Ext.Msg.alert("提示信息","删除失败，该船运公司已经被使用中");
								return;
							}else{
								Ext.Msg.alert("提示信息","删除成功");
								reloadGrid("shipGrid")
							}
						})
					}else{
						return;
					}
				});
		    }
		}	
	function getIds()
	{
		var list = Ext.getCmp("shipGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list,function(item){
			var cotShipCompany = new CotShipCompany();
			cotShipCompany.id = item.id
			res.push(cotShipCompany);
		});
		return res;
	}
		
		
		//批量删除
		function deleteBatch()
		{
			var list = getIds();
	    	if(list.length == 0)
	    	{	
	    		Ext.Msg.alert("提示信息","请选择记录");
	    		return;
	    	}
			var flag = window.confirm("确定删除?");
		    if(flag){
				cotShipCompanyService.deleteList(list,function(res){
					result = res;
					if(result == -1){
						Ext.Msg.alert("提示信息","删除失败，该船运公司已经被使用中");
						return;
					}else{
						Ext.Msg.alert("提示信息","删除成功");
						reloadGrid("shipGrid")
				
					}
				})
			}
		}

</script>

	<body>
		
	</body>
</html>
