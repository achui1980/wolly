<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Client Edit</title>
		<jsp:include page="/extcommon.jsp"></jsp:include> 	
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>	
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotPopedomService.js'></script>	
		<script type='text/javascript' src='<%=webapp %>/customer/customer/js/customerModify.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script> 
		 
		<script type="text/javascript">
 			//修改
			function mod()
			{	var addressValue = Ext.getCmp('customerEmail').getValue();
				 if(Ext.isEmpty(addressValue)==false){
					var reg = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
		            if(reg.test(addressValue)==false){
		            	Ext.Msg.alert('Message','Email invalid! Please Enter again!');
		            	return;
				    }
				  }
				var id = $("customerid").value;
				//表单验证结束
				//判断客户简称是否重复
				var chkG = false;
				DWREngine.setAsync(false); 
				cotCustomerService.findIsExistShortName($('customerShortName').value,id,function(res){
					if(res!=null){
						Ext.Msg.alert("Message",'Short Title already exist!');
						chkG = true;
					}
				});
				DWREngine.setAsync(true); 
				if(chkG==true){
					$('customerShortName').focus();
					return;
				}
				//判断客户英文名是否重复
				if($('fullNameEn').value!=''){
					DWREngine.setAsync(false); 
					cotCustomerService.findIsExistEnName($('fullNameEn').value,id,function(res){
						if(res!=null){
							Ext.Msg.alert("Message",'Name already exist!');
							chkG = true;
						}
					});
					DWREngine.setAsync(true); 
					if(chkG==true){
						$('fullNameEn').focus();
						return;
					}
				}
				
				DWREngine.setAsync(false); 
				var obj1 = DWRUtil.getValues("cotCustomerForm");
			    var cotCustomer = new CotCustomer();
			    cotCustomerService.getCustomerById(parseInt(id),function(res){
					for(var p in obj1) {
						if(p!='addTime'){
						    res[p] = obj1[p];
						}
					}
					cotCustomer = res;
				})
				/*var customPhoto = DWRUtil.getValue("customPhoto");
				var customerMb = DWRUtil.getValue("customerMb");
				cotCustomer.customPhoto = customPhoto;
				cotCustomer.customerMb = customerMb;*/
				//货代
				cotCustomer.forwardingAgent=obj1['forwardt'];
			    cotCustomerService.modifyCustomer(cotCustomer,$('addTime').value,function(res){
			        if(res){
			            var obj=Ext.getCmp('maincontent');
			        	if(obj.getActiveTab().title=='联系人'){
			        		loadCustContact();
			        	}
						Ext.Msg.alert("Message","Save Successfully!");
						reflashParent('custGrid');	
					}else{
						Ext.Msg.alert("Message","Save Failed! No. has existed");
						$('customerNo').select();
					}
				}) 	 
				DWREngine.setAsync(true);
			}
			 
			//删除
			function del()
			{
				var flag = window.confirm("Are you sure delete the Client?");
			    if(flag){
			         var cotCustomer = new CotCustomer();
					 var id =  DWRUtil.getValue("customerid");
					 cotCustomerService.deleteCustomerById(parseInt(id),function(res){
						if(res!=-1){
							Ext.Msg.alert("Message","Delete Successfully!");
							closeandreflashEC(true,"custGrid",false);
						}else{
							Ext.Msg.alert("Message","Delete Fail!");
						}
					}) 
				}else{
			        return;
			    }
 			}
 			
 			
			
			//删除后加载客户相片
			function loadPhoto2(){
				DWREngine.setAsync(false);
				var id = $("customerid").value;
				$('customPhotoPath').src = "./showPicture.action?detailId="+id+"&flag=custPhoto";
				Ext.Msg.alert("Message","Deleted successfully!");
				DWREngine.setAsync(true);
			}
			
			//删除客户相片
			function delPic(){
				Ext.Msg.confirm('Message','Are you sure you delete this customer photos?',function(btn){
					if(btn=='yes'){
						var id = $("customerid").value;
					    cotCustomerService.deleteCustImg(parseInt(id),function(res){
							if(res){
								$('customPhotoPath').src = "common/images/zwtp.png";
								//setTimeout("loadPhoto2()", 1200);
							}else{
								Ext.Msg.alert("Message","Picture does not exist, delete the image failed!");
							}
						})
					}
				});
			}
			
			//删除后加载唛标
			function loadMb2(){
				DWREngine.setAsync(false);
				var id = $("customerid").value;
				$('customerMbPath').src = "./showPicture.action?detailId="+id+"&flag=custMb";
				Ext.Msg.alert("Message","Deleted successfully!");
				DWREngine.setAsync(true);
			}
			
			//删除唛标
			function delMb(){
				Ext.Msg.confirm('Message','您确定删除此唛标图片吗?',function(btn){
					if(btn=='yes'){
						var id = $("customerid").value;
					    cotCustomerService.deletePicImg(parseInt(id),function(res){
							if(res){
								$('customerMbPath').src = "common/images/zwtp.png";
							}else{
								Ext.Msg.alert("Message","图片不存在，删除图片失败!");
							}
						})
					}
				});
			}
				
			
			 
			
			//加载联系人信息
			function loadCustContact(){
				var id = $("customerid").value;
				var frame = window.frames["custContact"];
				frame.location.href = "custcontact.do?method=query&cusid="+id;
			}
			
			//加载拜访信息
			function loadCustVisitedLog(){
				var id = $("customerid").value;
				var frame = window.frames["custVisitedLog"];
				frame.location.href = "customervisitedlog.do?method=query&cusid="+id;
			}  
			
			//加载客户索赔信息
			function loadClaim(){
				var custId = $("customerid").value;
				var frame = window.frames["claim"];
				frame.location.href = "cotcustomer.do?method=queryClaim&custId="+custId;
			}
			
			//加载邮件记录
			function  loadMail(){
				var id = $("customerid").value;
				var frame = window.frames["mail"];
				frame.location.href = "cotcustomer.do?method=loadMail&custId="+id;
			} 
			
			var fullNameCn = '';
			
			//加载报价记录
			function loadPrice(){
				//获取注册软件版本
				var softVer;
				cotElementsService.getSoftVer(function(res){
					softVer = res;
				})
				if(softVer=="sample"){
			 	    Ext.Msg.alert("Message","对不起，您必须升级到报价系统才可以查看!");
					return;
			 	}else{
					var id = $("customerid").value;
					var empId = $("empId").value;
					fullNameCn = $("fullNameCn").value;
					var frame = window.frames["price"];
					frame.location.href = "cotprice.do?method=query&custId="+id+"&busiId="+empId+"&flag=customerPage";
				}	
			}
			
			var custShortName = '';
			
			//加载征样记录
			function loadSign(){
				//获取注册软件版本
				var softVer;
				cotElementsService.getSoftVer(function(res){
					softVer = res;
				})
				if(softVer=="sample"){
			 	    Ext.Msg.alert("Message","对不起，您必须升级到报价系统才可以查看!");
					return;
			 	}else{
					var id = $("customerid").value;
					custShortName = $("customerShortName").value;
					var frame = document.sign;
					frame.location.href = "cotsign.do?method=query&cId="+id;
				}	
			}
			
			//加载送样记录
			function loadGiven(){
				//获取注册软件版本
				var softVer;
				cotElementsService.getSoftVer(function(res){
					softVer = res;
				})
				if(softVer=="sample"){
			 	    Ext.Msg.alert("Message","对不起，您必须升级到报价系统才可以查看!");
					return;
			 	}else{
					var id = $("customerid").value;
					var empId = $("empId").value;
					custShortName = $("customerShortName").value;
					var frame = window.frames["given"];
					frame.location.href = "cotgiven.do?method=query&custId="+id+"&busiId="+empId+"&flag=customerPage";
				}
			}
			
			//加载订单记录
			function loadOrder(){
				//获取注册软件版本
				var softVer;
				cotElementsService.getSoftVer(function(res){
					softVer = res;
				})
				if(softVer=="sample"){
			 	    Ext.Msg.alert("Message","对不起，您必须升级到报价系统才可以查看!");
					return;
			 	}else{
					var id = $("customerid").value;
					var empId = $("empId").value;
					custShortName = $("customerShortName").value;
					var frame = window.frames["order"];
					frame.location.href = "cotorder.do?method=query&custId="+id+"&busiId="+empId+"&flag=customerPage";
				}	
			}
			
			//加载客户图片记录
			function loadCustPhone(){
				var id = $("customerid").value;
				var customerNo = $("customerNo").value;
				var frame = window.frames["custPhone"];
				frame.location.href = "cotcustomer.do?method=queryCustPc&custId="+id+"&customerNo="+customerNo;
			}
			
			//加载订单图片
			function loadOrderPic(){
				//获取注册软件版本
				var softVer;
				cotElementsService.getSoftVer(function(res){
					softVer = res;
				})
				if(softVer=="sample"){
			 	    Ext.Msg.alert("Message","对不起，您必须升级到报价系统才可以查看!");
					return;
			 	}else{ 
					var custId = $("customerid").value;
					var picFrame = window.frames["orderPic"];
					picFrame.location.href = "cotcustomer.do?method=queryOrderPic&cid="+custId
				}	
			}
			
			
			
			//隐藏图片上传层(上传页面调用)
			function hideUploadDiv()
			{
				$("uploadPhotoDiv").style.display ='none';
				$("uploadMbDiv").style.display ='none';
			} 
			
			
			
			

		</script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body onload="mask('Loading')">
		<input type="hidden" name="customerid" id="customerid" value="<%=id%>">
	</body>
</html>
