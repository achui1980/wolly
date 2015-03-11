<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="../../extcommon.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotRptFileService.js'></script>
<script type='text/javascript' src='<%=webapp %>/sample/rptfile/js/rptfile.js'></script>
<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
<script type="text/javascript">
 
 
	 //关闭详细信息
	/*function closeWindow()
	{
		$("editDiv").style.display = "none";
	}*/
	
	//刷新页面
	/*function refresh()
	{
	 	window.location.reload();
	}*/


	//显示修改页面
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
				Ext.Msg.alert("提示信息","只能选择一条记录!")
				return;
			}
			else
				obj = ids[0].id;
		} 
	    openWindowBase(350,500,'cotrptfile.do?method=modify&id='+obj);
	}

</script>
</head>
<body >
    
    </body>
</html>