<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		 
		<title>Client Edit</title>
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/uploadpanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script> 
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotPopedomService.js'></script>	
		<script type='text/javascript' src='<%=webapp %>/customer/customer/js/customerAdd.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
		<script type="text/javascript">
			//添加
			function add()
			{	
				var addressValue = Ext.getCmp('customerEmail').getValue();
				 if(Ext.isEmpty(addressValue)==false){
					var reg = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
		            if(reg.test(addressValue)==false){
		            	Ext.Msg.alert('Message','Email invalid! Please Enter again!');
		            	return;
				    }
				  }
				//表单验证结束
				//判断客户简称是否重复
				var chkG = false;
				DWREngine.setAsync(false); 
				cotCustomerService.findIsExistShortName($('customerShortName').value,0,function(res){
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
					cotCustomerService.findIsExistEnName($('fullNameEn').value,0,function(res){
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
				
				for(var p in obj1){
						if(p!='addTime'){
							cotCustomer[p] = obj1[p];
						}
					} 
				//货代
				cotCustomer.forwardingAgent=obj1['forwardt'];
				cotCustomer.customerShortName=obj1['customerShortName'];
				cotCustomer.fullNameCn=obj1['fameCn'];
				
				//cotCustomer.fullNameEn=obj1['fameEn'];
				
				var customerMb = $('customerMb').value;//document.getElementById("customerMbPath").src;
				/*
				if(customerMb.indexOf("zwtp") > -1){
					//customerMb = "common/images/zwtp.png";
				}
				else{
					var s = customerMb.lastIndexOf("/");
					var filename=customerMb.substring(s+1,customerMb.length);
					//customerMb = "customerMb/"+filename;
				}*/
				var customPhoto = document.getElementById("customPhotoPath").src;
				if(customPhoto.indexOf("zwtp") > -1){
					customPhoto = "common/images/zwtp.png";
				}
				else{
					var s = customPhoto.lastIndexOf("/");
					 var filename=customPhoto.substring(s+1,customPhoto.length);
					customPhoto = "customerPhoto/"+filename;
				}
				var addPersorn;
				cotPopedomService.getLoginEmpId(function(res){
				 	addPersorn = res;
				})
				cotCustomer.customerMb = $('customerMb').value;
				cotCustomer.customPhoto = customPhoto;
				cotCustomer.addPersorn = addPersorn;
				cotCustomerService.saveCustomer(cotCustomer,customPhoto,customerMb,$('addTime').value,function(res){
			        if(res==false){
						Ext.Msg.alert("Message",'No. already exist!');
						$('customerNo').select();
					}else{
						Ext.Msg.alert("Message","Save Successfully!",function(id){
							if(id=='ok'||id=='cancel'){
								var flag = $("flag").value;
								if(flag=="0"){
								    //self.opener.ECSideUtil.reload("cusTable");
									window.close();
								}else{
								    closeandreflashEC(true,"custGrid",false);
								}
							}
						});
					}
				})
				DWREngine.setAsync(true); 
			}
			
			
			//删除客户照片
			function delPhoto(){　
			    var flag = window.confirm("确定删除此客户相片吗?");
			    if(flag){
					var picPath = $('customPhotoPath').src;
			    	cotCustomerService.delPicByPath(picPath,function(res){
					   if(res){
					   		$('customPhotoPath').src = "common/images/zwtp.png";
					   }else{
					   		Ext.Msg.alert("Message","删除相片失败！");
					   }
				    })
				}else{
			        return;
			    }
			}
			
			//删除唛标
			function delMb(eId){
				var flag = window.confirm("确定删除此唛标图片吗?");
			    if(flag){
			    	var picPath = $('customerMbPath').src;
			    	cotCustomerService.delPicByPath(picPath,function(res){
					   if(res){
					   		$('customerMbPath').src = "common/images/zwtp.png";
							Ext.Msg.alert("Message","删除唛标图片成功,须在保存后生效！");
					   }else{
					   		Ext.Msg.alert("Message","删除唛标图片失败！");
					   }
				    })
				}else{
			        return;
			    }
			}
			
			//关闭页面并删除上传图片
			function closeAndDelPic(){
				DWREngine.setAsync(false); 
			    if($('customerMb').value!=""){
					var picPath = $('customerMb').value;
					cotCustomerService.delPicByPath(picPath,function(res){
					})
				}　
				if($('customPhoto').value!=""){
					var picPath = $('customPhoto').value;
					cotCustomerService.delPicByPath(picPath,function(res){
					})
				}　
				closeandreflashEC(true,'customerTable',false);
				DWREngine.setAsync(true);
			}
			
			
			
			//页面加载调用
			function initForm()
			{
				getCustNo();
				$('customerShortName').focus();
			}
			function getCustNo()
			{
				cotSeqService.getCustNo(function(res){
					document.getElementById("customerNo").value = res;
				})
			}
		</script>
	</head>
		<% 
			String flag = request.getParameter("flag");
			String id = request.getParameter("id");
		%>
	<body onload="mask('Loading')">
		<input type="hidden" name="flag" id="flag" value="<%= flag %>"/>
		<input type="hidden" name="customerid" id="customerid" value="<%=id%>">	
	</body>
</html>
