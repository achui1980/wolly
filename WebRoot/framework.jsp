<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<title>OPS</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css"
			href="<%=webapp%>/common/ext/ux/css/Portal.css" />
		<!--[if IE]>
		<link rel="stylesheet" type="text/css" href="<%=webapp%>/common/css/ie6.css" />
		<![endif]-->
		<script type="text/javascript"
			src="<%=webapp%>/common/ext/ux/TabCloseMenu.js"></script>

		<link rel="stylesheet" type="text/css"
			href="<%=webapp%>/common/ext/ux/css/Portal.css" />
		<script type="text/javascript"
			src="<%=webapp%>/common/ext/ux/Portal.js"></script>
		<script type="text/javascript"
			src="<%=webapp%>/common/ext/ux/ToastWindow.js"></script>
		<script type="text/javascript"
			src="<%=webapp%>/common/ext/ux/PortalColumn.js"></script>
		<script type="text/javascript"
			src="<%=webapp%>/common/ext/ux/Portlet.js"></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotModuelService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotFaqService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrgService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotEmpsService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/answerWin.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/faqWinHome.js'></script>
		<script type='text/javascript' src='<%=webapp%>/queryAll/js/orgHome.js'></script>
		<script type='text/javascript' src='<%=webapp%>/framework.js'></script>
		
		<style type="text/css">
    	.x-grid3-cell-inner {   
	 			/*内容长的时候换行  */
	 			white-space: normal !important;
	 			word-wrap:normal;
	 			text-overflow:ellipsis;
			}
	    </style>

		<script type="text/javascript">
		  function getEmpInfo()
			{
				cotEmpsService.getLoginEmp(function(res){
				
					if(res == null)
					{
						document.getElementById("loginEmpId").innerHTML = "<span class='user'></span>未知";
					}
					else
					{
						document.getElementById("loginEmpId").innerHTML += "<div style='float: right'><span class='user'>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>"+res.empsId+"&nbsp;<font color=green>("+res.empsName+")</font></div>";
					}
				})
			}
			function KeyDown(){
				if(event.keyCode==116){ 
					event.keyCode = 0;
					event.cancelBubble = true;
					return false;
				}
			}
			document.onkeydown=KeyDown;	
 		 </script>
	</head>

	<body>
		<input type="hidden" name="PICount" id="PICount" value="0">
		<input type="hidden" name="POCount" id="POCount" value="0">
		<!-- 点击重新计算 -->
		<input type="hidden" name="RePICount" id="RePICount" value="0">
		<input type="hidden" name="RePOCount" id="RePOCount" value="0">
		<div id="content"></div>
		<div id="calendar_container"></div>
		<iframe name="loginaction" style="display:none"></iframe>
		<form action="index.do?method=logoutAction" method="post" id="exitForm" name="extyForm" style="display:none">
		</form>
	</body>
</html>