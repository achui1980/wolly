<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>子样品列表</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='dwr/interface/cotPriceFacService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/sample/cotpricefac/js/cotpricefaclist.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>

	<script type="text/javascript">
		//禁止鼠标右键
		//document.oncontextmenu = new Function("event.returnValue=true;");

		//删除
		function del(id)
		{
		
			var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
			if(isPopedom == 0)//没有删除权限
			{
				Ext.Msg.alert("提示信息","您没有删除权限");
				return;
			}
			var priceFac = new CotPriceFac();
			var ids = new Array();
			priceFac.id = id;
		    ids.push(priceFac);
		    var flag = window.confirm("是否确定删除?");
		    if(flag){
				    cotPriceFacService.delPriceFac(ids,function(res){
				    if(res == true){
						Ext.Msg.alert("提示信息","删除成功");
						ECSideUtil.reload('priceFacTable');
					}else{
						Ext.Msg.alert("提示信息","删除失败");
					}
				});	
		    }
		}
		
		
		
		
	</script>
	<%
		String id = request.getParameter("mainId");
	 %>
	<body>
	
		<input type="hidden" id="eleId" name="eleId" value="<%=id %>"/>
	</body>
</html>
