<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title></title>
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common//css/common.css" />
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/ext-all.css" />
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/ext-extend.css" />
        <link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/ext-patch.css" />
        
        
        <!-- GC -->
         
        <!-- LIBS -->
       	<script type="text/javascript" src="<%=webapp %>/common/js/common.js"></script>
       	<script type="text/javascript" src="<%=webapp %>/common/jquery/jquery.js"></script>
       	<script type="text/javascript">
       		var j = jQuery.noConflict();       		
       	</script>
       	
		<script type="text/javascript" src="<%=webapp %>/common/ext/adapter/jquery/ext-jquery-adapter.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/ext/jquery-position.js"></script>
        
         
        <!-- ENDLIBS -->
        <script type="text/javascript" src="<%=webapp %>/common/ext/ext-all.js"></script>
      
        <script type='text/javascript' src='<%=webapp %>/dwr/engine.js'></script>
		<script type="text/javascript" src="<%=webapp %>/dwr/util.js"></script>
		<script type="text/javascript" src="<%=webapp %>/dwr/interface/baseDataUtil.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotPopedomService.js'></script>
		 <script type='text/javascript' src='<%=webapp %>/dwr/interface/cotModuelService.js'></script>
		 <script type='text/javascript' src='<%=webapp %>/dwr/interface/queryService.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/bindCombo.js"></script>
        <script type="text/javascript">
        Ext.BLANK_IMAGE_URL = '<%=webapp %>/common/ext/resources/images/default/s.gif'
        Ext.QuickTips.init();                       //为组件提供提示信息功能，form的主要提示信息就是客户端验证的错误信息。
		Ext.form.Field.prototype.msgTarget='qtip';         //提示的方式，枚举值为
		//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());//设置cookie，保存布局
        
        </script>
  </head>
</html>
