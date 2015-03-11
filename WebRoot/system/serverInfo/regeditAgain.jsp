<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Re-registration page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="../../simplecommom.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotRegistService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotModuelService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/system/serverInfo/js/regeditAgain.js'></script>
		<script type="text/javascript">
		//保存
		function add()
		{
		    //表单验证结束
		  　 DWREngine.setAsync(false); 
		    var cotServerInfo = new CotServerInfo();
		    var list = new Array();
		    var mechineKey = DWRUtil.getValue("mechineKey");
		    var attachKey = DWRUtil.getValue("regeditModule");
		    var regeditKey = DWRUtil.getValue("regeditKey").trim();
		    var serverNo = DWRUtil.getValue("serverNo").trim();
		    var isStandAlone = DWRUtil.getValue("isStangAlone");
		    var softVer = DWRUtil.getValue("softVer"); 
		    //可选模块
		   	var attachModule = ""
		   	//获取模块注册码信息
		   	var checkGroup = Ext.getCmp("attachModule").getValue();
		   	Ext.each(checkGroup,function(item){
		   		attachModule += item.getRawValue();
		   	});
		   	var errorMsg = "";
		    cotRegistService.getAttachModuleByRegeditedKey(attachKey,function(res){
		    	if(res == null && attachModule != ""){
		    		errorMsg = "Module registration code is wrong, no additional modules module License Information！"
		    	}
		    	if(res != null){
		    		var module = res.replace("|","");
		    		if(module != attachModule){
		    			errorMsg = "Module registration key or select the add-on module in error, please re-select or register";
		    		}
		    	}
		    });
		    if(errorMsg != ""){
		    	alert(errorMsg);
		    	return;
		    }
		    var rightSoftVer = '';
		    //将注册码进行解密，并获取软件版本
		    cotRegistService.getSoftVerByRegeditKey(regeditKey,function(res){
		    	rightSoftVer = res;
		    	if(rightSoftVer!=softVer){
		    		if(rightSoftVer == 'sample'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase samples system, pls. select the fsamples  system version！");
		    			return;
		    		}
		    		if(rightSoftVer == 'price'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase quotation system, pls. select the quotation system version！");
		    			return;
		    		}
		    		if(rightSoftVer == 'trade'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase foreign trade system, pls. select the foreign trade system version！");
		    			return;
		    		}
		    		if(rightSoftVer == 'trade_f'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase Enhanced foreign trade system, pls. select the Enhanced foreign trade system version！");
		    			return;
		    		}
		    		if(rightSoftVer == 'email'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase Mail System, pls. select the Mail System version！");
		    			return;
		    		}
		    		if(rightSoftVer == null){
		    			Ext.Msg.alert("Message","Sorry, registration code error, please re-register！");
		    			return;
		    		}
		    	}else{
		    		//获取mechineKey对应ID
				    var id = "";
				    cotRegistService.getIdByMechineKey(mechineKey,function(res){
				    	id = res.id;
				    }) 
				    //将十进制serverNo转化为6位十六进制serverNo
				    cotRegistService.getHexServerNo(serverNo,function(res){
				    	serverNo = res;
				    }) 
				    
				    //用mechineKey，6位十六进制serverNo获取正确注册码（rKey）,验证输入的注册码是否正确
				    var rKey = "";
					
				    if(isStandAlone == "1"){
				    	cotServerInfo.mechineKey = mechineKey;
				    	cotServerInfo.softVer = softVer;
				    	var iserverNo = parseInt(DWRUtil.getValue("serverNo").trim());
						var month = parseInt(DWRUtil.getValue("month").trim());
						var time = parseInt(DWRUtil.getValue("userTime").trim());
				    	cotRegistService.getDemoRegeditKey(cotServerInfo,month,time,iserverNo,function(res){
				    		rKey = res
				    	})
				    }
				    else
				    {
				    	cotRegistService.getRegeditKey(mechineKey,softVer,serverNo,function(res){
						    	 rKey = res;
						    }) 
					   
				    }
				     if(rKey!=regeditKey){
					    	    Ext.Msg.alert("Message","Registration code is incorrect, please re-register！")
					    		$("regeditKey").value="";
					    		return;
					    }
			 		//将十六进制serverNo加密
				    cotRegistService.getKiEncryptServerNo(serverNo,function(res){
				    	serverNo = res;
				    })  
			         
				    //获取当时间
				    var addTime = "";
				    cotRegistService.getTime(function(res){
				    	addTime = res;
				    })  
				    cotServerInfo.id = id;
				    cotServerInfo.mechineKey = mechineKey;
				    cotServerInfo.regeditKey = regeditKey;
				    cotServerInfo.serverNo = serverNo;
				    cotServerInfo.isStangAlone = isStandAlone;
				    cotServerInfo.softVer = softVer;
				    cotServerInfo.addTime = addTime; 
				    cotServerInfo.regeditModule = attachKey;
				    list.push(cotServerInfo);
				    
				    if(isStandAlone == "0")//永久版本
				    {
					    cotRegistService.addRegistInfo(list,function(res){
							Ext.Msg.alert("Message","Registration success!");
							
						});
					}
					else if(isStandAlone == "1")
					{
						var iserverNo = parseInt(DWRUtil.getValue("serverNo").trim());
						var month = parseInt(DWRUtil.getValue("month").trim());
						var time = parseInt(DWRUtil.getValue("userTime").trim());
						cotRegistService.saveDemoRegedit(cotServerInfo,iserverNo,month,time,function(res){
							Ext.Msg.alert("Message","Registration success!");
							
						})
					}
					if(rightSoftVer == "")
						rightSoftVer = "sample";
					
					//更新版本菜单
					cotModuelService.updateModuleBySoftVerOpen(rightSoftVer,function(res){});
					cotModuelService.updateModuleBySoftVerClose(rightSoftVer,function(res){});　
					//开启附加模块,仅在填写了模块注册码时，操作
					if(attachKey != "" && attachKey.length == 16){
						if($('emailmodule').checked){
							cotModuelService.updateModuleBySoftVerOpen("email",function(res){});
						}
						else{
							cotModuelService.updateModuleBySoftVerClose("email",function(res){});
						}
					}
					location.href = "<%=webapp %>/index.jsp";
		    	}
		    }) 
		    DWREngine.setAsync(true); 
		}
		
		 
		
		function reloadForm()
		{
			 $("regeditKey").value="";
			 $("serverNo").value="";
		}	
		
		//返回登陆页面
		function returnBack()
		{
			window.location.href = "<%=webapp %>/index.jsp";
		}
		
		
		//初始化表单验证字段
		function validForm()
		{
			appendattribute("regeditKey","Please enter the registration code！","Require");
			appendattribute("serverNo","Please enter the number of clients, and the number of clients must be a number!","Double");
		}
				
		function initForm()
		{
			DWREngine.setAsync(false); 
		    var mechineKey = "";
		    //获取本机MAC地址（注册序列号）
		    cotRegistService.getMechineKey(function(res){
				mechineKey = res;
				DWRUtil.setValue("mechineKey",mechineKey);
			})
			//获取十六位解密的ServerNo（十六进制）
			cotRegistService.getServerNo(mechineKey,function(res){
				var HexServerNo = "0x"+res;
				DWRUtil.setValue("serverNo",parseInt(HexServerNo,16));
			});
			//获取软件版本信息
			var attachKey = "";
			cotRegistService.getCotServerInfo(mechineKey,function(serverInfo){
				attachKey = serverInfo.regeditModule;			
			    DWRUtil.setValue("isStangAlone",serverInfo.isStandAlone);
			    DWRUtil.setValue("softVer",serverInfo.softVer);
			});
			cotRegistService.getMachineKey(mechineKey,function(res){
				DWRUtil.setValues(res);
				
				if(res.isStangAlone == "1")
				{
					Ext.getCmp("tempreg").setVisible(true);
				}
			});
			cotRegistService.getAttachModuleByRegeditedKey(attachKey,function(res){
		    	if(res != null){
		    		var list  = res.split("|");
		    		for(var i=0; i<list.length; i++){
		    			if(list[0] == "A")//邮件模块
		    				var mailModule = Ext.getCmp("emailmodule");
		    				mailModule.setValue(true);
		    		}
		    	}
		    });
			//validForm();
			DWREngine.setAsync(true); 
		}		
		</script>
	</head>

	<body onload="">

		
	</body>
</html>
