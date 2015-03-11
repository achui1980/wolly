<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
  <jsp:include page="../../extcommon.jsp"></jsp:include>
  <script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomerService.js'></script>
  <script type='text/javascript' src='<%=webapp %>/dwr/interface/customerVisitedLogService.js'></script>
  <script type='text/javascript' src='<%=webapp %>/customer/custvisitedlog/js/custvisitedlog.js'></script>
  <script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
  
  <script type="text/javascript">
    //权限判断变量，对应于cot_module表中的URL字段
    var url = "cotcustomer.do?method=query";  	
  </script>
  </head>
     <%
		String id = request.getParameter("cusid");
		if(id == null)
			id = "";
	%>
  <body>
         <input type="hidden" name="cid" id="cid" value="<%=id%>" />
 </body>
</html>
