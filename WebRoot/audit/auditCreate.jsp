<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<title>单据生成页面</title>
		<!-- 导入公共js和css -->
				<jsp:include page="../extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotAuditService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/audit/js/auditcreate.js'></script>
	  <script type="text/javascript">
			//添加
			function save(){	
				cotAuditService.saveAudit($('auditNo').value,$('auditNum').value,$('receiveDate').value,$('effectDate').value,function(res)
				{
					if(res==false){
						Ext.Msg.alert("提示信息","生成核销单失败!");
					}else{
						Ext.Msg.alert("提示信息","生成核销单成功！");
						closeandreflashEC('true','auditGrid',false);
					}
				})
			}		
			

			
			//页面加载调用
			function initForm(){
				$('receiveDate').value = getCurrentDate('yyyy-MM-dd');							
			}
			
		</script>
	</head>

	<body onload="initForm()">

	
	</body>
</html>
