<%@ page contentType="text/html; charset=utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<title>Staff permissions</title>
<!-- 导入公共js和css -->
<jsp:include page="/extcommon.jsp"></jsp:include>

<script type="text/javascript" src="<%=webapp %>/common/xtree/map.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/xtree/checkboxTreeItem.js"></script>

<script>
  //权限判断变量，对应于cot_module表中的URL字段
//var url = "cotemps.do?method=queryList"; 
function save()
{
	DWRUtil.useLoadingMessage("Configuring permissions, please wait.........."); 
	var list = new Array();
	var ids = new Array();
	var idList = getAllCheckBox();
	var empid = ${"popedomid"}.value;
	for(var i=0; i<idList.length; i++)
	{
		var popedom = new CotPopedom();
		popedom.empsId = parseInt(empid);
		popedom.moduleId = parseInt(idList[i]);
		list[i] = popedom;
	}
	var rootpopedom = new CotPopedom();
	rootpopedom.empsId = parseInt(empid);
	rootpopedom.moduleId = parseInt(tree_root.nodeValue);
	list.push(rootpopedom);
	DWREngine.setAsync(false); 
	cotPopedomService.deletePopedomByEmpId(empid,function(res){});
	cotPopedomService.addPopedom(list,function(res){
		if(res == 0)
		{
			Ext.Msg.alert("Message","Save permissions configuration successful!");
		}
		else
		{
			Ext.Msg.alert("Message","Save permissions configuration failed!");
		}
	});
	DWREngine.setAsync(false); 
}

function initTree()
{
	setStyle();
	DWRUtil.useLoadingMessage("Loading data, please wait..........."); 
	var empId = ${"popedomid"}.value;
	cotPopedomService.getPopedomByEmpId(empId,function(res){
		var popedom = new CotPopedom();
		for(var i=0;i<res.length;i++)
		{
			var popedom = new CotPopedom();
			popedom = res[i];	
			var chekbox = getCheckBox(popedom.moduleId);
			//如果checkbox == null 则说明是整棵树的根节点,根节点不是checkbox类型,所以无法获取
			if(chekbox == null || typeof(chekbox) == "undefined")
				continue;
			if(chekbox.root == 1)//如果是根节点,则不做onclick操作,因为根节点选中后,其子节点也会一并选中
				continue;
			chekbox.checked = true;
		}
		
	})
}
function setStyle()
{
	var tbl = document.getElementById("tbl");
	var left = (document.body.clientWidth-tbl.clientWidth)/2;
	tbl.style.marginLeft = left;
	tbl.style.marginTop = "10px";
}
</script>
</HEAD>
<BODY onload="initTree()" style="background-color: #dfe8f6;">
<%
	String empId = request.getParameter("id");
	//String empname = request.getParameter("empname");
	
	//empname = new String(empname.getBytes("iso-8859-1"),"UTF-8");
 %>

 <table id="tbl">
 	<tr>
 		<td style="color: blue">
			<label>Current Staff-></label>
			<label id="roleName">${empname}</label>
 		</td>
 	</tr>
 	<tr>
 		<td>
 		&nbsp;&nbsp;
 		</td>
 	</tr>
 	<tr>
 		<td>
 			<%= request.getAttribute("chkTreeScript") %>
 		</td>
 	</tr>
 	<tr>
 		<td>
				<div style="width:100%;margin-top:5px;">
						<a onclick="save()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_save.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="javascript:window.close();" 
								style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_cancel.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;	
				</div>				
				<!--   		
 			<button class="confirmBtn" onclick="save()" >Save</button>
 			<button class="cancelBtn" onclick="window.close();" >取消</button>
 		-->
 		</td>
 	</tr>
 </table>



<input type="hidden" id="popedomid" value="<%=empId %>"/>
</BODY>
</HTML>


