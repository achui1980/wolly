<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>拖车行信息编辑页面</title>
<jsp:include page="../../extcommon.jsp"></jsp:include>
<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotTrailCarService.js'></script>
<script type='text/javascript' src='<%=webapp%>/systemdic/trailcar/js/trailcarEdit.js'></script>
<script type="text/javascript">

	//权限判断变量，对应于cot_module表中的URL字段
	var url = "cottrailcar.do?method=query"; 
	 
    //添加或修改记录
	function saveOrUpdate()
	{   
		var name = $("name").value;
		var id = $("trailcarid").value;
		 
		cotTrailCarService.findExistTrailCar(id,name,function(res){
		     if(res){
		     	Ext.Msg.alert("提示信息","保存失败，已存在相同拖车行名称！");
		     	return;
		     }else{
		     	var obj = DWRUtil.getValues("queryTrailCarForm");
				var list = new Array();
			    var cotTrailCar = new CotTrailCar();
				for(var p in obj){
					cotTrailCar[p] = obj[p];
				} 
				if(id!= 'null'){
				    cotTrailCar.id = id;
			 	} 
				list.push(cotTrailCar);
			    cotTrailCarService.saveOrUpdateTrailCar(list,function(res){
			    	 Ext.Msg.alert("提示信息","保存成功!");
					 closeandreflashEC(true,"trailGrid",false);
				});
		     }
		});
	}
	
	 
	
	    
	//页面初始化   
	function initForm() 
	{      
	 	var id = $("trailcarid").value;
	 	if(id!='null'){
	 	    cotTrailCarService.getTrailCarById(parseInt(id),function(res){
				DWRUtil.setValues(res);
			});
	 	}
	}
 
 
</script>
	</head>
	<%
		String id = request.getParameter("id");
	 %>
	<body>
		<input type="hidden" name="trailcarid" id="trailcarid"  value="<%= id %>" />
	</body>
</html>