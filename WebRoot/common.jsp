<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
        <title></title>
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/ext-all.css" />
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common/xtree/xtree.css" />
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/common.css" />
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common/css/datepicker.css" />
        <!-- GC -->
        <!-- LIBS -->
         <!--<script type="text/javascript" src="<%=webapp %>/common/ext/adapter/yui/yui-utilities.js"></script>-->
         <!--<script type="text/javascript" src="<%=webapp %>/common/ext/adapter/yui/ext-yui-adapter.js"></script>-->
        <!-- ENDLIBS -->
        <script type="text/javascript" src="<%=webapp %>/common/ext/adapter/ext/ext-base.js"></script>
        <script type="text/javascript" src="<%=webapp %>/common/ext/ext-all.js"></script>
        <!-- script type="text/javascript" src="<%=webapp %>/common/ext/dwrproxy.js"></script-->
        <script type="text/javascript" src="<%=webapp %>/common/xtree/xtree.js"></script>
		<!-- script type="text/javascript" src="<%=webapp %>/common/ext/dwrTreeProxy.js"></script-->
        <!-- 导入dwr -->
        <script type='text/javascript' src='<%=webapp %>/dwr/engine.js'></script>
        <script type="text/javascript" src="<%=webapp %>/dwr/util.js"></script>
		<script type='text/javascript' src='dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/contexUtil.js'></script>
        <script type="text/javascript" src="<%=webapp %>/common/js/common.js"></script>
        <script type="text/javascript" src="<%=webapp %>/common/js/validator.js"></script>
        <script type='text/javascript' src='<%=webapp %>/dwr/interface/cotPopedomService.js'></script>
        <script type='text/javascript' src='<%=webapp %>/dwr/interface/cotModuelService.js'></script>
        <!--  script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>-->
		<script type="text/javascript" src="<%=webapp %>/common/js/WdatePicker.js"></script>
		<script language="javascript" src="<%=webapp %>/common/js/menu.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/drap.js"></script>
		
		<script type='text/javascript' src='dwr/interface/cotModuelService.js'></script>
		
		
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/sysdicutil.js'></script>	
        <script type="text/javascript">
        //Ext.BLANK_IMAGE_URL = '<%=webapp %>/common/ext/resources/images/default/s.gif'
        </script>    
  </head>
</html>
