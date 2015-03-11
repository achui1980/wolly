<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Database backup</title>
		<jsp:include page="../../extcommon.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/setNoService.js'></script>
		<script type='text/javascript' src='<%=webapp%>/system/combination/js/combination.js'></script>
		<script type="text/javascript"> 
		
		//合并工厂
		function combinationFac(form){
			//表单验证
			if($('srcFacId').value == ''){
				alert('The original supplier can not be empty！');
				return;
			}
			if($('desFacId').value == ''){
				alert('The target supplier can not be empty！');
				return;
			}
						
			//表单验证结束		
			var flag = window.confirm("Sure you want to merge data? (WARNING: The merger will not be restored)");
			if(flag){
				var loginForm = Ext.getCmp('combinationId');
				loginForm.form.submit({
					waitMsg : 'Merging',
					waitTitle : 'Tips',
					url : 'cotdatatone.do?method=combinationFac',
					method:'POST',
					success:function(form,action){
						Ext.Msg.alert('Tips',action.result.msg);
						//loginForm.form.reset();
						
					},
					failure:function(form,action){
						Ext.Msg.alert('Tips',action.result.msg);
					}
			    });				
			}
		}
		
		//合并客户
		function combinationCust(){
			//表单验证
			if($('srcCustId').value == ''){
				alert('Former clients can not be empty！');
				return;
			}
			if($('desCustId').value == ''){
				alert('Target clients can not be empty
！');
				return;
			}
						
			//表单验证结束		
			var flag = window.confirm("To determine the data to be merged？");
			if(flag){
			//	var form = document.getElementById('combinationForm');
			//	form.action = "cotdatatone.do?method=combinationCust"
			//	form.submit();
				var loginForm = Ext.getCmp('combinationId');
				loginForm.form.submit({
					waitMsg : 'Merging',
					waitTitle : 'Tips',
					url : 'cotdatatone.do?method=combinationCust',
					method:'POST',
					success:function(form,action){
						Ext.Msg.alert('Tips',action.result.msg);
						//loginForm.form.reset();
					},
					failure:function(form,action){
						Ext.Msg.alert('Tips',action.result.msg);
					}
			    });
			}
		}
	    </script>
	</head>

	<body>
	<!-------- 下拉隐藏层 ----------->
		
	</body>
</html>
