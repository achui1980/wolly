<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
   .div1 {
         margin-top: -440px;
         margin-left: 50%;
   }
   .div2 {
         margin-top: -400px;
         margin-left: 50%;
   }
</style>
<jsp:include page="/jquerycommon.jsp"></jsp:include>

<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotEleCfgService.js'></script>
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotBoxTypeService.js'></script>
<script type='text/javascript' src='<%=webapp %>/sample/cotelecfg/js/elecfg.js'></script>
<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
</head>

<body>
<!-------------------------- 公式编辑隐藏层 ------------------------------------->
<div id="calculatorDiv" style="width:360px;position:absolute;z-index:9500;">		
</div>
<!-------------------------- 公式编辑隐藏层(结束) ------------------------------------->
<!-------------------------- 公式编辑隐藏层 ------------------------------------->
<div id="calculatorfacDiv" style="width:360px;position:absolute;z-index:9500;">		
</div>
<!-------------------------- 公式编辑隐藏层(结束) ------------------------------------->
</body>
</html>