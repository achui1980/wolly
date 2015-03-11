<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>The list of production contracts</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrderFacService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript'
			src='<%=webapp%>/dwr/interface/cotPriceService.js'></script>
		<script type="text/javascript"
			src="<%=webapp%>/common/js/printPanel.js"></script>
			<script type='text/javascript'
			src='<%=webapp%>/orderfac/js/OrderFacToCustWin.js'></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotOrderStatusFileService.js'></script>
		<script type='text/javascript' src='<%=webapp %>/order/js/OrderRemarkGrid.js'></script>
		<script type='text/javascript' src='<%=webapp%>/orderfac/js/orderfac.js'></script>
		<script type="text/javascript" src="<%=webapp%>/common/js/popedom.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
	</head>
	<script type="text/javascript">
	
	    //权限判断变量，对应于cot_module表中的URL字段
		var url = "cotorderfac.do?method=query";
		
		//打开采购单添加页面
		function windowopenAdd(){
			//添加权限判断
			var isPopedom = getPopedomByOpType(vaildUrl,"ADD");
			if(isPopedom == 0)//没有添加权限
			{
				Ext.Msg.alert("Tip Box","您没有添加权限!");
				return;
			}
			openFullWindow('cotorderfac.do?method=add');
		}
		
		//打开采购单编辑页面
		function windowopenMod(obj,orderId){
			//添加权限判断
			var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
			if(isPopedom == 0)//没有修改权限
			{
				Ext.Msg.alert("Tip Box","Sorry, you do not have Authority!");
				return;
			}
			if(obj == null)
			{
				var ids = getIdsForDel(); 
				if(ids.length == 0)
				{	
					Ext.Msg.alert("Tip Box","Please select a record!");
					return;
				}
				else if(ids.length > 1)
				{
					Ext.Msg.alert("Tip Box","Choose only one record!")
					return;
				}
				else
					obj = ids[0].id;
			}
			//var orderId = $('orderId').value;
			openFullWindow('cotorderfac.do?method=add&id='+obj+"&oId="+orderId);
			//$('detailDiv').style.display = 'none';
		}
		
		//打开采购明细编辑页面
		function windowopenModDetail(obj){
			//添加权限判断
			var isPopedom = getPopedomByOpType(vaildUrl,"MOD");
			if(isPopedom == 0)//没有修改权限
			{
				Ext.Msg.alert("Message","Sorry, you do not have Authority!");
				return;
			}
			if(obj == null)
			{
				var ids = getIds(); 
				if(ids.length == 0)
				{	
					Ext.Msg.alert("Message","Please select a record!");
					return;
				}
				else if(ids.length > 1)
				{
					Ext.Msg.alert("Message","Choose only one record!")
					return;
				}
				else
					obj = ids[0][0];
			}
			openWindowBase(500,1020,'cotorderfac.do?method=openDetail&id='+obj+'&orderNo='+$('orderNo').value+'&orderId='+orderId);
		}
		
		//删除
		function del(id,status)
		{
			var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
			if(isPopedom == 0)//没有删除权限
			{
				Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
				return;
			}
			//判断该单是否已被审核
			if(status==2 && loginEmpId != "admin"){
				Ext.MessageBox.alert("Message", "Sorry, this one has already been audited, can not be removed!");
				return;
			}
	
			var dealFlag = false;
			DWREngine.setAsync(false);
			cotOrderFacService.getDealNumById(parseInt(id),function(dealNum){
				if(dealNum != -1){
					dealFlag = true;
				}
			});
			DWREngine.setAsync(true);
			if(dealFlag){
				Ext.Msg.alert("Message",'Sorry, the order has been recorded in accounts payable, can not be deleted！');
				return;
			}
			
			var otherOutFlag = false;
			DWREngine.setAsync(false);		
			cotOrderFacService.getOtherOutNumById(parseInt(id),function(orders){
				if(orders!=-1){
					otherOutFlag = true;
				}
			});
			DWREngine.setAsync(true);
			if(otherOutFlag){
				Ext.Msg.alert("Message",'Sorry, there are other costs to import the shipping can not be removed！');
				return;
			}		
			
			var otherFlag = false;
			DWREngine.setAsync(false);
			cotOrderFacService.getOtherNumById(parseInt(id),function(otherNum){
				if(otherNum != -1){
					otherFlag = true;
				}
			});
			DWREngine.setAsync(true);
			if(otherFlag){
				Ext.Msg.alert("Message",'Sorry, there are other costs into the order, can not be removed！');
				return;
			}
			
			var cotOrderFac= new CotOrderFac();
			var list = new Array();
		    cotOrderFac.id = id;
		    list.push(cotOrderFac);
		    
		     Ext.Msg.confirm('Message','Are you sure to delete?',function(btn){
		    	if(btn=='yes'){
		    		//查询该主采购单是否被删除
					cotOrderFacService.getOrderFacById(id,function(res){
						if(res!=null){
							cotOrderFacService.deleteOrderFacs(list,function(res){
								if(res){
									Ext.Msg.alert("Message","Deleted successfully!");
									reloadGrid('orderfacGrid')
								}else{
									Ext.Msg.alert("Message","Delete failed, the purchase order has been in use!");
								}
							})
						}else{
							reloadGrid('orderfacGrid')
						}
					});
		    	}
		    });
		}
		
		//删除采购明细
		function delDetail(id)
		{
			var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
			if(isPopedom == 0)//没有删除权限
			{
				Ext.Msg.alert("Message","Sorry, you do not have Authority!");
				return;
			}
			var list = new Array();
		    list.push(parseInt(id));
		    Ext.Msg.confirm('Message','Are you sure to delete?',function(btn){
		    	if(btn=='yes'){
		    		DWREngine.setAsync(false);
			    	 //查询该采购明细是否被删除
					cotOrderFacService.getOrderFacDetailById(parseInt(id),function(res){
						if(res!=null){
							cotOrderFacService.deleteDetailByIds(list,function(res){
								if(res){
									Ext.Msg.alert("Message","Deleted successfully!");
									ECSideUtil.reload("queryTable");
								}else{
									Ext.Msg.alert("Message","Delete failed, it has been used in the procurement details!");
								}
							})
						}else{
							ECSideUtil.reload("queryTable");
						}
					});
			    	 DWREngine.setAsync(true);
		    	}
		    });
		}
		
		//批量删除主单
		function deleteBatch(){
	    	
	    	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
			if(isPopedom == 0)//没有删除权限
			{
				Ext.Msg.alert("Message","Sorry, you do not have Authority!");
				return;
			}
			
			var facList = Ext.getCmp("orderfacGrid").getSelectionModel().getSelections();
			if (facList.length == 0) {
				Ext.MessageBox.alert("Message", "Please select records!");
				return;
			}
			var ary = new Array();
			Ext.each(facList, function(item) {
						var status = item.data.orderStatus;
						if(status==2 && loginEmpId != "admin"){
						}else{
							ary.push(item.id);
						}
					});
			if(ary.length==0){
				Ext.MessageBox.alert('Message', "You select the orders have reviewed, can not be deleted!");
				return;
			}
			
			var list = getIdsForDel();

			var dealFlag = false;
			var orderNos ='';
			DWREngine.setAsync(false);					
			cotOrderFacService.checkIsHaveDeal(list,function(orders){
				if(orders != null){
					orderNos = orders;
					dealFlag = true;
				}
			});
			DWREngine.setAsync(true);
			if(dealFlag){
				//alert('对不起，单号【'+orderNos+'】已存在应付帐款记录，不能删除！');
				Ext.Msg.alert("Message",'Sorry, there have been some odd numbers recorded in accounts payable, can not perform bulk delete, please delete one by one！');
				return;
			}
			
			var otherOutFlag = false;
			DWREngine.setAsync(false);		
			cotOrderFacService.checkIsOtherToOrderOut(list,function(orders){
				if(orders != null){
					otherOutFlag = true;
				}
			});
			DWREngine.setAsync(true);
			if(otherOutFlag){
				//alert('对不起，单号【'+otherNos+'】已存在应付帐款记录，不能删除！');
				Ext.Msg.alert("Message",'Sorry, part of the order issue of the other costs have been imported shipments, can not perform bulk delete, please delete one by one！');
				return;
			}
			
			var otherFlag = false;
			DWREngine.setAsync(false);		
			cotOrderFacService.checkIsOtherToOrder(list,function(orders){
				if(orders != null){
					otherFlag = true;
				}
			});
			DWREngine.setAsync(true);
			if(otherFlag){
				//alert('对不起，单号【'+otherNos+'】已存在应付帐款记录，不能删除！');
				Ext.Msg.alert("Message",'Sorry, some single-issue orders for other expenses have been imported to the order, can not perform bulk delete, please delete one by one！');
				return;
			}
			
			 Ext.Msg.confirm('Message','Are you sure to delete?',function(btn){
		    	if(btn=='yes'){
		    		 cotOrderFacService.deleteOrderFacs(list,function(res){
						Ext.Msg.alert("Message","Deleted successfully!");
						reloadGrid('orderfacGrid');
						//$('detailDiv').style.display='none';
					});	
		    	}
		    });
		}
		
		//获得采购明细表格选择的记录
		function getDetailIds()
		{
			var list = Ext.getCmp("orderfacGrid").getSelectionModel().getSelections();
			var res = new Array();
			Ext.each(list, function(item) {
				res.push(item.id);
			});
			return res;
		}
		function getIds() {
			var list = Ext.getCmp("orderfacGrid").getSelectionModel().getSelections();
			var res = new Array();
			Ext.each(list, function(item) {
				var ary = new Array();
				ary.push(item.id);
				ary.push(item.get("orderStatus"));
				ary.push(item.get("orderNo"));
				res.push(ary);
			});
			return res;
		}
		//获得表格选择的记录
		function getIdsForDel(){
			var list = Ext.getCmp("orderfacGrid").getSelectionModel().getSelections();
			var res = new Array();
			Ext.each(list, function(item) {
				var cotOrderFac = new CotOrderFac();
				cotOrderFac.id = item.id;
				cotOrderFac.orderNo = item.get("orderNo");
				res.push(cotOrderFac)
			});
			return res;
		}
		
		
		
		
		//查询采购明细单数据
		function queryDetail(){
			var params = {};
		    params.orderId = $('orderId').value;
		    params.flag='queryTable';
	   	 	ECSideUtil.queryECForm("queryTable",params,true);
		}
		
		//显示采购明细单层
		function showDetailDiv(orderId,orderNo){
			//$('detailDiv').style.display = 'block';
			$('orderId').value = orderId;
			$('orderNo').value = orderNo;
			queryDetail();
		}
		

		
		//显示打印层
		function showPrintDiv(e){
		
			var ids = getIds(); 

			if(ids.length == 0){	
				Ext.Msg.alert("Message","Please select a record!");
				return;
			}else if(ids.length > 1){
				Ext.Msg.alert("Message","Choose only one record!")
				return;
			}else{			
				$('mailDiv').style.display='none';
				$('exportMainId').value = ids[0][0];
				$('orderNo').value = ids[0][2];
				//设置层显示位置
				var t=e.offsetTop;
				var l=e.offsetLeft;
				while(e=e.offsetParent){
					t+=e.offsetTop;
					l+=e.offsetLeft;
				}						
				document.getElementById("printDiv").style.top=t+20;
				document.getElementById("printDiv").style.left=l-260;
				document.getElementById("printDiv").style.display ="block";
			}
		}
		
		//关闭打印层
		function closePrintDiv(){
 			document.getElementById("printDiv").style.display ="none";
		}
		
		//导出方法
		function exportRpt(){		
			var ids = getIds();
			var printFlag = 1;
			var orderStatus = ids[0][1];
			if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
				Ext.Msg.alert("Message",'Sorry, the order has not approved, can not print export!');
				return;
			}
		
			var headerFlag = $('headerFlag').value;
			if($('allPage').checked){
				headerFlag = true;
			}else{
				headerFlag = false;
			}
			var model = $('reportTemple').value;
			if(model==''){
				Ext.Msg.alert("Message",'Please select Export Template!');
				return;
			}
			var printType = $('printType').value;
			openWindow("<%=webapp %>/downOrderFac.action?reportTemple="+model+"&printType="+printType+"&orderId="+$('exportMainId').value+"&headerFlag="+headerFlag+"&orderFacNo="+ids[0][2]);
		}
		
		//预览方法
		function viewRpt(){
			var _height = window.screen.height;
			var _width = window.screen.width;
			var model = $('reportTemple').value;
			if(model==''){
				Ext.Msg.alert("Message",'Please select Export Template!');
				return;
			}
			
			var headerFlag = $('headerFlag').value;
			if($('allPage').checked){
				headerFlag = true;
			}else{
				headerFlag = false;
			}
			var printType = $('printType').value;
			var printFlag = 1;
			var ids = getIds();
			var orderStatus = ids[0][1];
			if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
				printFlag = 0;//审核没有通过，不在Applet页面显示打印按钮
			}
			openMaxWindow(_height,_width,"<%=webapp %>/previewrpt.do?method=queryEleRpt&reportTemple="+model+"&orderId="+$('exportMainId').value+"&headerFlag="+headerFlag+"&printFlag="+printFlag);
		}	
		
		//打印按钮
		function openPrint(){
			var model = $('reportTemple').value;
			if(model==''){
				Ext.Msg.alert("Message",'Please select Export Template!');
				return;
			}
			openWindow("<%=webapp %>/downOrderFac.action?printType=PRINT&reportTemple="+model+"&orderId="+$('exportMainId').value);
		}
		
		//发邮件
		function sendMail(){
			if($('reportTemple').value==''){
				Ext.Msg.alert("Message",'Please select Export Template!');
				return;
			}
			if($('customerEmail').value==''){
				Ext.Msg.alert("Message",'The customer has no mail, please add to customer management!');
				return;
			}
			//设置层显示位置
			var width = window.screen.availWidth/3;
			var height = window.screen.availHeight/4;
			$("mailDiv").style.top=height;
			$("mailDiv").style.left=width;
			$('mailDiv').style.display='block';
			setTimeout("openMail()",600);
		}
		
		//打开邮件发送页面
		function openMail(){
			//所需参数
			var mail = {};
			mail.custEmail = $('customerEmail').value;
			//mail.custId = $('cstId').value;
			mail.custName = $('shortName').value;
			mail.orderId = $('exportMainId').value;
			mail.orderNo = $('orderNo').value;
			mail.reportTemple = $('reportTemple').value;
			mail.printType = $('printType').value;
			DWREngine.setAsync(false);
			cotOrderFacService.saveMail(mail,function(res){
				$('mailDiv').style.display='none';
				var id = res;
				if(id==null){
					Ext.Msg.alert("Message",'Mail configuration failed, please contact the administrator!');
				}else{
					openWindow('cotmail.do?method=modify&id='+id);
				}
				$('mailDiv').style.display='none';
			});
			DWREngine.setAsync(true);
		}
		
		
		//点击报表上导出层外的地方后隐藏该层
		function hideDiv()
		{
			var down = event.srcElement.id;
			if(down!="printDiv" && down!="printBtn" && down!="reportTemple" && down!="printType"
				&& down!="exportRptBtn" && down!="viewRptBtn" && down!="openPrintBtn"){
				$("printDiv").style.display ='none';
			}
		}
		
/*			
		//加入消息提醒
		function addToMessage(){
			var ids = getIds(); 
			if(ids.length == 0){	
				alert("请选择一条记录!");
				return;
			}else if(ids.length > 1){
				alert("只能选择一条记录!")
				return;
			}else{
			    DWREngine.setAsync(false);
			 	var id = ids[0].id;
			 	cotOrderFacService.getOrderFacById(parseInt(id),function(res){
					if(res.sendTime==null||res.sendTime==''){
						showMessageDiv(id);
					}
				});
				DWREngine.setAsync(true);
			}
		}
		
		//显示添加消息提醒条件的层
		function showMessageDiv(id){
		    $("mainId").value = id;
			var obj = $("messageDiv").style.display;
			if(obj=='none'){
				//设置层显示位置
				var width = window.screen.availWidth/3;
				var height = window.screen.availHeight/4;
				$("messageDiv").style.top=height;
				$("messageDiv").style.left=width;
				$('messageDiv').style.display='block';
			}else{
				$("messageDiv").style.display='none';
			}
			return false;
		}
		
		
		//关闭添加消息提醒条件的层
		function closeMessageDiv(){
			 $("messageDiv").style.display='none';
			 $('sTime').value = '';
		}
		*/
		//将消息提醒条件保存
		function saveSign(){
		    var sendTime = $('sTime').value;
			if(sendTime == ''){
				alert("Please enter the delivery date！");
				return;
			}else{
				Ext.Msg.confirm('Message','Are you sure you save and add news to remind?',function(btn){
			    	if(btn=='yes'){
			    		DWREngine.setAsync(false);
						var id = $("mainId").value;
						cotOrderFacService.getOrderFacById(parseInt(id),function(cotOrderFac){
							cotOrderFac.sendTime = getDateType(sendTime);
							//更新交货日期
							cotOrderFacService.updateOrderFac(cotOrderFac,function(res){
							});
						});
						ECSideUtil.reload("orderFacTable");
						closeMessageDiv();
						DWREngine.setAsync(true);
			    	}else{
			    		closeMessageDiv();
			    	}
			    });
			}
		}
		
		
		//禁止鼠标右键
		document.oncontextmenu = new Function("event.returnValue=true;");
		


		

		//关闭消息提醒层
		function closeMessageDiv(){
			document.getElementById('messageDiv').style.display ='none';
		}	
		//加入消息提醒
		function addToMessage(){
			var ids = getIdsForDel(); 
			if(ids.length == 0){	
				alert("Please select a record of sending product!");
				$("messageDiv").style.display='none';
				return;
			}else if(ids.length > 1){
				alert("You can only select a record of sending product!")
				$("messageDiv").style.display='none';
				return;
			}else{
				 DWREngine.setAsync(false);
				 var id = ids[0].id;
				 cotOrderFacService.getOrderFacById(parseInt(id),function(res){
					showMessageDiv(id);
				});
				DWREngine.setAsync(true);
			}
		}
		//显示添加提醒消息页面
		function showMessageDiv(id){
		 	$("orderId").value = id;
			var obj = $("messageDiv").style.display;
			if(obj=='none'){
				//设置层显示位置
				var width = window.screen.availWidth/5;
				var height = window.screen.availHeight/5;
				//加载页面
				var frame = document.MessageTabFrame;
				frame.location.href = "cotorderfac.do?method=addToMessage&orderfacId="+id;
				document.getElementById("messageDiv").style.top=height;
				document.getElementById("messageDiv").style.left=width;
				$("messageDiv").style.display='block';
			}else{
				$("messageDiv").style.display='none';
			}
		}					
	</script>
	<%
		String cid = request.getParameter("cId");
		if (cid == null)
			cid = "";
		String con=request.getParameter("configValue");
		if(con==null){
			con="YES";
		}else{
			con="No";
		}
	%>
	<body>

		<input type="hidden" id="orderId" name="orderId" />
		<input type="hidden" id="orderNo" />
		<%--<input type="hidden" id="cstId" />
		--%>
		<input type="hidden" id="shortName" />
		<input type="hidden" id="customerEmail" />
		<input type="hidden" id="exportMainId" name="exportMainId" />
		<div id="rightMenu"></div>
		<input type="hidden" id="configValue" name="configValue" value="<%=con%>" />
	</body>
</html>
