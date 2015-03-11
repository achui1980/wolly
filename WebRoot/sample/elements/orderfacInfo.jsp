<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Purchase Records</title>
    <!-- 导入公共js和css -->
	<jsp:include page="/extcommon.jsp"></jsp:include> 
	<!-- 导入dwr -->
	<script type='text/javascript' src='<%=webapp %>/sample/elements/js/orderfacinfo.js'></script>
    <script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	<script type="text/javascript">
	
	// 打开订单编辑页面
	function windowopen(obj) {
		//查看的权限
		var isPopedom = getPdmByOtherUrl('cotorderfac.do',"SEL");
		if(isPopedom == 0)//查看的权限
		{
			alert("您没有查看的权限!");
			return;
		}
		openFullWindow('cotorderfac.do?method=add&id=' + obj);
	}			
   </script>
  </head>
  	<% 
		String eleId = request.getParameter("eleId");
		String cid = request.getParameter("cId");
		if(cid == null)
			cid = "";
	%>
  <body>
    <input type="hidden" id="eleId" name="eleId" value="<%=eleId%>" />
  </body>
</html>