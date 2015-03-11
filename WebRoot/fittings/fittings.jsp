<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>配件列表</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/file-upload.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/FileUploadField.js"></script>
			
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotFittingsService.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/printPanel.js"></script>
		<script type='text/javascript' src='<%=webapp %>/fittings/js/reportWin.js'></script>
		<script type='text/javascript' src='<%=webapp %>/fittings/js/fittings.js'></script>
		<script type="text/javascript" src="<%=webapp %>/fittings/js/fenWin.js"></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

	</head>

	<script type="text/javascript">
		//加载下拉框数据
		function bindSel(){
			// 加载报表模版
			queryService.getRptFileList(11, function(res) {
				bindSelect('reportTemple', res, 'rptfilePath', 'rptName');
				//查询默认的模板
				queryService.getRptDefault(11, function(def) {
					if(def!=null){
						DWRUtil.setValue('reportTemple',def.rptfilePath);
					}
				});
			});
		}
		
		
		
		
		//所需参数
		var mail = {};
		//发邮件
		function sendMail(){
			if($('reportTemple').value==''){
				alert('请选择导出模板!');
				return;
			}
			var list = getIds();
			if(list.length==0){
				alert('请选择一条记录!');
				return;
			}else if (list.length>1){
				alert('只能选择一条记录!');
				return;
			}
			//获得选择行的出货号
			var orderNo='';
			var objs = document.getElementsByName("checkOrderID");
			for(var i=0; i<objs.length; i++){
				var chk = objs[i];
				if(chk.checked){
					var p = chk.parentNode.parentNode;
					var cells = p.cells;
					orderNo = cells[2].innerText;
				}
			}
			DWREngine.setAsync(false);
			//判断客户是否有邮箱
			cotFittingsService.getCusByOrderId(list[0],function(res){
				if(res==null || res.customerEmail==null || res.customerEmail==''){
					alert('该客户还没有邮箱,请先去客户管理中添加!');
					return;
				}
				mail.custEmail = res.customerEmail;
				mail.custId = res.id;
				mail.custName = res.customerShortName;
				mail.orderId = list[0];
				mail.orderNo = orderNo;
				mail.reportTemple = $('reportTemple').value;
				mail.printType = $('printType').value;
				//设置层显示位置
				var width = window.screen.availWidth/3;
				var height = window.screen.availHeight/4;
				$("mailDiv").style.top=height;
				$("mailDiv").style.left=width;
				$('mailDiv').style.display='block';
				setTimeout("openMail()",600);
			});
			DWREngine.setAsync(true);
		}
		//打开邮件发送页面
		function openMail(){
			DWREngine.setAsync(false);
			cotFittingsService.saveMail(mail,function(res){
				$('mailDiv').style.display='none';
				var id = res;
				if(id==null){
					alert('邮件配置失败,请联系管理员!');
				}else{
					openWindow('cotmailbox.do?method=modifyMail&id='+id);
				}
				$('mailDiv').style.display='none';
			});
			DWREngine.setAsync(true);
		}
		
		/*
		//配件分发
		function sendFitToEle(){
			var isMod = getPopedomByOpType(vaildUrl,"FEN");
			if(isMod==0){//没有修改权限
				alert('您没有配件分发权限!');
				return;
			}
			var list = getIds();
	    	if(list.length == 0 || list.length >1){	
	    		alert("一次只能分发一条配件,请您先勾选一条配件信息!");
	    		return;
	    	}
	    	var obj = $("eleFitDiv").style.display;
			if (obj == 'none') {
				// 加载样品查询表格
				var frame = document.eleFitFrame;
				frame.location.href = "cotfittings.do?method=goFitEle&fitId="
						+ list[0];
				document.getElementById("eleFitDiv").style.top = 80;
				document.getElementById("eleFitDiv").style.left = 200;
				$("eleFitDiv").style.display = 'block';
			} else {
				$("eleFitDiv").style.display = 'none';
			}
		}	
		*/
		
		//查看该配件分发了哪些货号
		function findEle(){
			var list = getIds();
	    	if(list.length == 0 || list.length >1){	
	    		alert("一次只能查看一条配件分发的货号,请您先勾选一条配件信息!");
	    		return;
	    	}
	    	var obj = $("eleFenDiv").style.display;
			if (obj == 'none') {
				// 加载样品查询表格
				var frame = document.eleFenFrame;
				frame.location.href = "cotfittings.do?method=findEleFen&fitId="
						+ list[0];
				document.getElementById("eleFenDiv").style.top = 80;
				document.getElementById("eleFenDiv").style.left = 200;
				$("eleFenDiv").style.display = 'block';
			} else {
				$("eleFenDiv").style.display = 'none';
			}
		}
		
		//excel导入
		function excelInTo(){
			var isMod = getPopedomByOpType(vaildUrl,"EXCEL");
			if(isMod==0){//没有修改权限
				alert('您没有配件导入权限!');
				return;
			}
			
	    	var obj = $("excelDiv").style.display;
			if (obj == 'none') {
				// 加载样品查询表格
				var frame = document.excelFrame;
				frame.location.href = "cotfittings.do?method=showExcel";
				document.getElementById("excelDiv").style.top = 80;
				document.getElementById("excelDiv").style.left = 200;
				$("excelDiv").style.display = 'block';
			} else {
				$("excelDiv").style.display = 'none';
			}
		}	
		
		//关闭excel层
		function closeDiv(){
			$("excelDiv").style.display = 'none';
			$("excelDiv").style.width=430;
			ECSideUtil.reload("fittingsTable");
		} 
		 
	</script>
	<body onload="">
		<div id="eee"></div>
		<div id="excelDiv"></div>
	</body>
</html>
