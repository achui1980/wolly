<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邮件打印</title>
<%
	String webapp=request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/ext-all.css" />
<script type="text/javascript">
function PageSetup()
{
/*
	try
	{
		var HKEY_Root,HKEY_Path,HKEY_Key;
		HKEY_Root="HKEY_CURRENT_USER";
		HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
		var Wsh=new ActiveXObject("WScript.Shell");
		alert(Wsh);
		HKEY_Key="header";
		Wsh.RegWrite(HKEY_Root HKEY_Path HKEY_Key,"");
		HKEY_Key="footer";
		Wsh.RegWrite(HKEY_Root HKEY_Path HKEY_Key,"");
		alert ("您已成功完成打印边距设置!")
	}
	catch(e){alert ("页面设置失败,请手动设置!")}
	*/
}
function fPrint(){	
	document.getElementById("t1").style.display = "none";	
	document.getElementById("t2").style.display = "none";	
	window.print();	
	document.getElementById("t1").style.display = "";	
	document.getElementById("t2").style.display = "";
}
</script>
</head>
<body>
	<div id="t1">
		<button onclick="fPrint()">打印该邮件</button>
		<button onclick="window.close()">关闭该页面</button>
	</div>
	<%=request.getParameter("printcontent") %>
	<div id="t2">
		<button onclick="fPrint()">打印该邮件</button>
		<button onclick="window.close()">关闭该页面</button>
	</div>
</body>
</html>