<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
  <jsp:include page="../../extcommon.jsp"></jsp:include>
  <script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>
  <script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustContactService.js'></script>
  <script type='text/javascript' src='<%=webapp %>/customer/custcontact/js/custcontactlist.js'></script>
  <script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
  
  <script type="text/javascript">
 //权限判断变量，对应于cot_module表中的URL字段
   var url = "custcontact.do?method=query"; 
	function windowopen(obj)
	{
		//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
		if(isPopedom == 0)//没有修改权限
		{
			Ext.Msg.alert("Message","Sorry,you do not have authority!");
			return;
		}
		if(obj == null)
		{
			var ids = getIds(); 
			if(ids.length == 0)
			{	
				Ext.Msg.alert("Message","Please select a record!");
				return;
			}
			else if(ids.length > 1)
			{
				Ext.Msg.alert("Message","Sorry,you can select only one record!")
				return;
			}
			else
				obj = ids[0].id;
				 
		} 
	    var custId = $("cid").value;
	    openWindowBase(150,600,'custcontact.do?method=modify&id='+obj+'&custId='+custId);
	} 
	
	function windowopenAdd()
	{
	    var custId = $("cid").value;
		openWindowBase(150,600,'custcontact.do?method=add&custId='+custId);
	}
    
	 //删除
	 function  del(id)
	{   
		//添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
		if(isPopedom == 0)//没有删除权限
		{
			Ext.Msg.alert("Message","Sorry,you do not have authority!");
			return;
		}
	    var flag = window.confirm("Are you sure to delete selected items?");
	    if(flag){
	    	var list  = new Array();
		    var cotCustContact = new CotCustContact();
		     cotCustContact.id = id;
		     list.push(cotCustContact)
			 cotCustContactService.deleteCustContact(list,function(res){
				Ext.Msg.alert("Message","Deleted successfully!");
				reloadGrid('contactGrid');
			})
	    }else{
	      return;
	    }
	}
 
	
	function getIds()
	{
		var list = Ext.getCmp("contactGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list,function(item){
			var cotCustContact = new CotCustContact();
			cotCustContact.id = item.id
			res.push(cotCustContact);
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
	   Ext.MessageBox.confirm('Message', "Are you sure to delete selected items?", function(btn) {
			if (btn == 'yes') {
				cotCustContactService.deleteCustContact(list,function(res){
					Ext.Msg.alert("Message","Deleted successfully!");
					reloadGrid('contactGrid');
				});
			}
		});
	}
		
 </script>
  </head>
     <%
		String id = request.getParameter("cusid");
		if(id == null)
			id = "";
	%>
  <body>
         <input type="hidden" name="cid" id="cid" value="<%=id%>" />
 </body>
</html>
