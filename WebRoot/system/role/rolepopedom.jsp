<%@ page contentType="text/html; charset=utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<jsp:include page="/extcommon.jsp"></jsp:include>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<script type="text/javascript" src="<%=webapp %>/common/xtree/map.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/xtree/checkboxTreeItem.js"></script>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotRolePopedomService.js'></script>

<script>
//权限判断变量，对应于cot_module表中的URL字段
var url = "cotrole.do?method=query";
function save()
{
	DWRUtil.useLoadingMessage("Configuring Authority, please wait..........."); 
	var list = new Array();
	var ids = new Array();
	var idList = getAllCheckBox();
	var roleId = ${"rolepopedomid"}.value;
	for(var i=0; i<idList.length; i++)
	{
		var popedom = new CotRolePopedom();
		popedom.roleId = parseInt(roleId);
		popedom.moduleId = parseInt(idList[i]);
		list[i] = popedom;
	}
	var rootpopedom = new CotRolePopedom();
	rootpopedom.empsId = parseInt(roleId);
	rootpopedom.moduleId = parseInt(tree_root.nodeValue);
	list.push(rootpopedom);
	DWREngine.setAsync(false); 
	
	cotRolePopedomService.deletePopedomByRoleId(parseInt(roleId),function(res){});
	//更新员工权限
	cotRolePopedomService.updateEmpsPopedom(parseInt(roleId),list,function(res){});
	cotRolePopedomService.addRolePopedom(list,function(res){
		if(res == 0)
		{
			Ext.Msg.alert("Message","Save Authority configuration successful!");
		}
		else
		{
			Ext.Msg.alert("Message","Save Authority configuration failed!");
		}
	});
	DWREngine.setAsync(false); 
}
function initTree()
{
	DWRUtil.useLoadingMessage("Loading data, please wait..........."); 
	var roleId = ${"rolepopedomid"}.value;
	cotRolePopedomService.getPopedomByRoleId(parseInt(roleId),function(res){
		for(var i=0;i<res.length;i++)
		{
			var popedom = new CotRolePopedom();
			popedom = res[i];
			var chekbox = getCheckBox(popedom.moduleId);
			if(chekbox == null || typeof(chekbox) == "undefined")
				continue;
			if(chekbox.root == 1)//如果是根节点,则不做onclick操作,因为根节点选中后,其子节点也会一并选中
				continue;
			chekbox.checked = true;
			//chekbox.fireEvent("onclick");
		}
	})
}
</script>
</HEAD>
<%
	String roleId = request.getParameter("id");
 %>

<BODY onload="initTree()">
<table width="100%" border="0">
	<div class="navline">
		<label>
			&nbsp;
		</label>
	</div>	
	<tr align="center">
		<td >
		   <table width="100%" border="0" style="text-align:center">
		   	<tr>
				<td>
					<label>Role Authority configured-&gt;</label>
					<label>Current role-></label>
					<label id="roleName">${rolename}</label>
				</td>
			</tr>
			<tr>
			   <td ><%= request.getAttribute("chkRolePopedomTreeScript") %></td>
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
    		    <button class="confirmBtn" onclick="save()" >保存</button>
    		    <button class="cancelBtn" onclick="window.close();">关闭</button>
    		    -->
    	      </td>
    	    </tr>
          </table>
        </td>
    </tr>

</table>
<input type="hidden" id="rolepopedomid" value="<%=roleId %>"/>
</BODY>
</HTML>


