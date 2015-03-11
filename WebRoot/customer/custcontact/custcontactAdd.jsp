<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Add Contacts Page</title>
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotCustContactService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/customer/custcontact/js/custcontactAdd.js'></script>
  		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<script type="text/javascript">

	  //权限判断变量，对应于cot_module表中的URL字段
	var url = "custcontact.do?method=query";     
	
	function add()
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
	     for(var p in obj){
				cotCustContact[p] = obj[p];
			} 
			var custId = $('customerId').value;
			if(custId!=null && custId!='null'){
				cotCustContact.customerId=custId;
			}
		 DWREngine.setAsync(false); 
		 //判断是否同名
		 var isExist=false;
		 cotCustContactService.findExistByName(cotCustContact.contactPerson,function(res){
	    	isExist = res;
	     })
	     if(isExist)
	     {
	    	Ext.Msg.alert("Message","Name already exists!");
	    	return;
	     }
	     //添加联系人
	     cotCustContactService.addCustContact(cotCustContact,function(res){
			if(res!=null){
				if(Ext.isEmpty(res.contactEmail)==false){
					cotCustContactService.addRule(res.customerId,res.id,res.contactEmail);
				}
			}
			Ext.Msg.alert("Message","Successfully added");
			closeandreflashEC(true,"contactGrid",false);
		 })	 
		 DWREngine.setAsync(true); 
	}	
	

	  
</script>
	</head>
<%String custId = request.getParameter("custId"); %>
	<body>
		<input type="hidden" name="custId" id="custId" value="<%=custId %>"/>
		<input type="hidden" id="personName" value="<%= request.getAttribute("name")%>">
		<input type="hidden" id="personEmailUrl" value="<%= request.getAttribute("emailUrl")%>">
	</body>
</html>
