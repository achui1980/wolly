<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		 <title>Registration page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="../../simplecommom.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotRegistService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotModuelService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/system/serverInfo/js/cotserverInfo.js'></script>
		<script type="text/javascript">
		//保存
		function add()
		{
		    //表单验证结束
		  　 DWREngine.setAsync(false); 
		    var cotServerInfo = new CotServerInfo();
		    var list = new Array();
		    var mechineKey = DWRUtil.getValue("mechineKey");
		    var regeditKey = DWRUtil.getValue("regeditKey").trim();
		    var attachKey = DWRUtil.getValue("regeditModule");
		    var serverNo = DWRUtil.getValue("serverNo").trim();
		    var isStandAlone = DWRUtil.getValue("isStangAlone");
		    var softVer = DWRUtil.getValue("softVer");
		    var rightSoftVer = '';
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
		    //将注册码进行解密，并获取软件版本
		    cotRegistService.getSoftVerByRegeditKey(regeditKey,function(res){
		    	rightSoftVer = res;
		    	if(rightSoftVer!=softVer){
		    		if(rightSoftVer == 'sample'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase samples system, pls. select the fsamples  system version");
		    			return;
		    		}
		    		if(rightSoftVer == 'price'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase quotation system, pls. select the quotation system version！");
		    			return;
		    		}
		    		if(rightSoftVer == 'trade'){
		    			Ext.Msg.alert("Message","Sorry, your version of the software to purchase foreign trade system, pls. select the foreign trade system version");
		    			return;
		    		}
		    		if(rightSoftVer == 'trade_f'){
		    			Ext.Msg.alert("Message","Message","Sorry, your version of the software to purchase Enhanced foreign trade system, pls. select the Enhanced foreign trade system version");
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
		    		//将十进制serverNo转化为6位十六进制serverNo
				    cotRegistService.getHexServerNo(serverNo,function(res){
				    	serverNo = res;
				    }) 
				    //用十六进制mechineKey，serverNo获取rKey,验证注册码是否正确
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
							Ext.Msg.alert("Message","Register succeed !");
							//location.href = "<%=webapp %>/index.jsp";
						});
					}
					else if(isStandAlone == "1")
					{
						var iserverNo = parseInt(DWRUtil.getValue("serverNo").trim());
						var month = parseInt(DWRUtil.getValue("month").trim());
						var time = parseInt(DWRUtil.getValue("userTime").trim());
						cotRegistService.saveDemoRegedit(cotServerInfo,iserverNo,month,time,function(res){
							Ext.Msg.alert("Message "," Register succeed!");
							//location.href = "<%=webapp %>/index.jsp";
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
		
				
		function initForm()
		{
			if(Ext.isIE)
			{
				Ext.Msg.alert("Message","You are not using Google Chrome，For better user experience, please click the link below to install Google Chrome address, and use the Google Chrome open software");
				var downloadurl = document.getElementById("chromedownurl");
				downloadurl.style.display = 'block';
			}
			
			$("message").innerHTML = $("mesg").value;
			$("messageDiv").style.display = "block";
			
		    var mechineKey = "";
		    cotRegistService.getMechineKey(function(res){
				mechineKey = res;
				DWRUtil.setValue("mechineKey",mechineKey);
			})
		}	
		</script>
	</head>

	<body>

		<input type="hidden" name="mesg" id="mesg" value="${message}" />
		<div id="messageDiv" style="display: none;" align="center">
			<label id="message" style="color: red"></label>
		</div>
		
		 <div align="center"><div style="margin-top:80px;" id='regedit'></div></div>
	     <div id="chromedownurl" style="display:none;margin-top:10px;" align="center">
	  		<a href='/CotSystem/chrome_installer_5.0.375.55_sta.exe'><font>Google Chrome（Chrome 5.0）Download</font></a>
	  		<a href="#"><font>Installation Instructions</font></a>
	     </div>
	    
	</body>
</html>
