<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Staff Edit page</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotEmpsService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/system/emps/js/changepwd.js'></script>
	</head>
	<script type="text/javascript">
	function changePwd()
	{
		var oldPwd = document.getElementById("oldpwd").value;
		var newpwd = document.getElementById("newpwd").value;
		var confirmpwd = document.getElementById("confirmpwd").value;
		if(newpwd != confirmpwd)
		{
			Ext.Msg.alert("Message","New Password and Confirm Password inconsistent! Please re-confirm");
			document.getElementById("confirmpwd").value = "";
			document.getElementById("confirmpwd").onfocus();
			return;
		}
		cotEmpsService.modifyPwdByEmpId(oldPwd,newpwd,function(res){
			if(res == 1)
			{
				Ext.Msg.alert("Message","Old password is incorrect, please re-enter");
			}
			else
			{
				Ext.Msg.alert("Message","Modified successfully, please re-visit!");
				parent.document.location.href = "index.do?method=logoutAction";
			}
		});
	}
	</script>
<body>

</body>
<html>