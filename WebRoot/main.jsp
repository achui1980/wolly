<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webapp=request.getContextPath();
%>
<head>
	<title>旗航外贸管理系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<script type="text/javascript" >
	<!--
			window.onbeforeunload=function(){  
		        //用户点击浏览器右上角关闭按钮
		        //if(event.clientX>document.body.clientWidth&&event.clientY<0||event.altKey){   
		        if(document.body.clientWidth-event.clientX< 170&&event.clientY< 0||event.altKey){
		            return "退出旗航软件吗?";
		        }else if(event.clientY > document.body.clientHeight || event.altKey){ //用户点击任务栏，右键关闭
		           	return "退出旗航软件吗?";
		        }else{ //其他情况为刷新
		        }   
	      	} 
			
		    window.onunload=function(){
		        //在这里处理关闭页面前的动作
		        window.location.reload("<%=webapp %>/index.do?method=logoutAction");
		    }
	//-->
	</script>
</head>

<FRAMESET border='0' frameSpacing='0' rows='1000,*' frameBorder='no'>
	<FRAME name="title" id="top" marginWidth=0 marginHeight=0
		src="cotmodule.do?method=queryTest" frameBorder=no noResize
		scrolling=no target="f2">
	<FRAME id='mainF' name='f2' src="cotmessage.do?method=queryPlatForm">

</FRAMESET>


