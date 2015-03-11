<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Contact Editing Interface</title>
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustContactService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/customer/custcontact/js/custcontactModify.js'></script>
  		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<script type="text/javascript">
	 //权限判断变量，对应于cot_module表中的URL字段
	var url = "custcontact.do?method=query"; 
	function  del()
	{   
	    var flag = window.confirm("Are you sure to delete selected items?");
	    if(flag){
		    var cotCustContact = new CotCustContact();
		    var list = new Array();
		     cotCustContact.id =  DWRUtil.getValue("custcontactid");
		     list.push(cotCustContact)
			 cotCustContactService.deleteCustContact(list,function(res){
				//Ext.Msg.alert("提示信息","删除成功!");
				closeandreflashEC(true,"contactGrid",false);
			})
	    }else{
	      return;
	    }
	}
	
   function mod()
	{
		 var addressValue = Ext.getCmp('contactEmail').getValue();
		 if(Ext.isEmpty(addressValue)==false){
			var reg = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
            if(reg.test(addressValue)==false){
            	Ext.Msg.alert('Message','Please enter a valid email address!');
            	return;
		    }
		 }
		//表单验证结束
        var obj = DWRUtil.getValues("queryCustContactForm");
        var cotCustContact = new CotCustContact();
		for(var p in obj)
		{
			cotCustContact[p] = obj[p];
		} 
	    cotCustContact.id =  DWRUtil.getValue("custcontactid");
	    cotCustContact.customerId = DWRUtil.getValue("customerId");
	    cotCustContactService.modifyCustContact(cotCustContact,function(res){
			//Ext.Msg.alert("提示信息","修改成功！");
			closeandreflashEC(true,"contactGrid",false);
	    })
	}

	  
</script>

	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body>
		<input type="hidden" name="custcontactid" id="custcontactid" value="<%=id%>" /> 
	</body>
</html>
