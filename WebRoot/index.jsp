<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>

<head>
<title>OPS</title>
<jsp:include page="simplecommom.jsp"></jsp:include>
<script type="text/javascript" src="<%=webapp %>/common/js/cookies.js"></script>
<!-- 导入dwr -->
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotEmpsService.js'></script>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotRegistService.js'></script>

<script type='text/javascript' src='index.js'></script>
<script type="text/javascript">

function login()
{
	var username = document.getElementById("username").value;
	var pwd = document.getElementById("pwd").value;
	var otp = document.getElementById("otp").value;
	cotEmpsService.login(username,pwd,otp,function(res){
	
	if(res == 0)
		{
			Ext.Msg.alert("Message","Enter the user name does not exist");
			return;
		}
		else if(res == 1)
		{
		    Ext.Msg.alert("Message","The password is not correct");
			return;
		}
		else if(res == 6)
		{
			Ext.Msg.alert("Message","This account has been discontinued");
			return;
		}
		else if(res == 3)
		{
		    Ext.Msg.alert("Message","Exceeded the number of the largest online, can not login！");
			return;
		}
		else if(res == 5)
		{
			window.location.reload();
			return;
		}
		else if(res == 4)
		{
			window.location.reload("<%=webapp %>/system/serverInfo/cotserverInfo.jsp");
			return;
		}
		else if(res == 7)
		{
			cotRegistService.loginCheck(function(result){
				 if(result==0){
		    		Ext.Msg.alert("Message","对不起,您的注册信息受损,请重启系统以修复！")
		    	}
		    	else if(result==1){
		    		Ext.Msg.alert("Message","对不起！您的试用版本已到期，请购买正式版本！")
		    	}
		    	else if(result==2){
		    		Ext.Msg.alert("Message","对不起！您的系统时间已被修改，请联系旗航客服人员！")
		    	}
		    	else if(result==3){
		    		Ext.Msg.alert("Message","对不起！您的试用次数已用完，请购买正式版本！")
		    	}
		    	else
		    	{
		    		var loginForm = document.getElementById("loginForm");
					loginForm.method ="post";
					loginForm.action = "<%=webapp %>/index.do?method=loginAction"
					loginForm.submit();
		    	}
			});
		}
		else if(res == 8)
		{
		    Ext.Msg.alert("Message","该帐户需要输入动态密码才能登录！",function(){
			    Ext.getCmp("otp").focus();
				return;
		    });
		}
		else if(res == 9)
		{
			Ext.Msg.alert("提示信息","动态密码有误！请重新输入！若确认无误,请按shift+alt进行同步操作!",function(){
			    Ext.getCmp("otp").selectText();
				return;
		    });
		}
		else if(res == 10)
		{
		    Ext.Msg.alert("提示信息","该动态密码已使用过,请输入新动态密码！",function(){
			    Ext.getCmp("otp").focus();
				return;
		    });
		}
		else if(res == 2)
		{
			var loginForm = document.getElementById("loginForm");
			loginForm.method ="post";
			loginForm.action = "<%=webapp %>/index.do?method=loginAction"
			loginForm.submit();
		}
	})
}


//重新注册
function regeditAgain(){
	var loginForm = document.getElementById("loginForm");
	loginForm.method ="post";
	loginForm.action = "<%=webapp %>/system/serverInfo/regeditAgain.jsp"
	loginForm.submit();
}
function init()
{
	if(Ext.isIE)
	{
		Ext.Msg.alert("Message","You are not using Google Chrome, to get a better user experience, please click the link to install Google Chrome and  use the Google Chrome open software");
		var downloadurl = document.getElementById("chromedownurl");
		downloadurl.style.display = 'block';
	}else{
		//判断是否已经安装JRE
		var isJRE = judgeJRE();
		if(!isJRE){
			Ext.Msg.alert("Message","You have not installed JAVA virtual machine, install the JRE");
			var jreForm = document.getElementById("jreForm");
			jreForm.submit();
		}
	}
	var username = GetCookie("username");
	if(username == null)
		return;
	document.getElementById("username").value = username;
}
//针对非IE版本判断客户端是否安装了JRE
function judgeJRE(){
	for (j = 0; j < navigator.plugins.length; j++)
	{
		var mineType = navigator.plugins[j];
		for(var i=0; i<mineType.length; i++){
			var type = mineType[i].type;
			console.log(type)
			if(type == 'application/x-java-applet'){
				return true;
			}
		}
	}
	return false;
}
</script>


</head>
<body topmargin="0px" bottommargin="0" leftmargin="0" rightmargin="0" onload="" background="loginbg2.png">
<center>
<div style="margin-top:150px">
  <TABLE border=0 width="776" height="292"  border="0" cellpadding="0" cellspacing="0" background="loginform.png">
  <TR>

	<TD align="center" height="75px" valign="bottom"><label><font size=2 color=floralwhite>Version：2011.10.08</font></label></TD>

  </TR>
  <TR>
	<TD align="center" valign="top"><div id="login-div"></div></TD>
  </TR>
   <TR>
	<TD align="center" height="60px" valign="top">
		<label><font size=2 color=floralwhite>Copyright &copy 2002-2010 <a style="text-decoration:none" href="http://www.xmqh.net"><font size=2 color=floralwhite>XiaMen QiHang Software,Co.,Ltd</font></a> All rights reserved</font></label>
	</TD>
  </TR>
  
  </TABLE>
  <div id="chromedownurl" style="display:none">

  		<a href='chrome_installer_5.0.375.55_sta.exe'><font color=floralwhite>DownLoad（Chrome 5.0）</font></a>
  		<a href="#"><font color=floralwhite>Installation Instructions</font></a></div>

  </div>
</center>
<form action="jre-6u14-windows-i586.exe" method="post" id="jreForm"></form>
</body>
</html>
