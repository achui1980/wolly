<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Analysis Of Product Procurement</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotOrderFacService.js'></script>
		<!-- MAP -->
		<script type="text/javascript" src="<%=webapp %>/common/js/map.js"></script>
		<script type='text/javascript' src='<%=webapp %>/order/js/orderTofac.js'></script>
		<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>

		<script type="text/javascript">
		//权限判断变量，对应于cot_module表中的URL字段
		var url = "cotorderfac.do?method=query";
		
		// 根据原有EC表格修改的数据修改Map中相应的对象
		function updateMapValue(obj) {
			var b = false;
			var formPrice = document.forms['orderDetail'];
			b = Validator.Validate(formPrice, 1);
			if (!b) {
				return false;
			}
			var property = obj.name;
			var value = obj.value;
			var tr = obj.parentNode.parentNode;
			var rdm = getCellObj(tr, 'orderDetail', 'rdm', true);
			if (!ECSideUtil.hasClass(tr, "add")) {
				ECSideUtil.updateEditCell(obj);
			}
			cotOrderService.updateMapValueByEleId(rdm, property, value, function(res) {
			});
		}
		
		//刷新
		function refreshWin(){
			self.location.reload();
		}
	
		//分解
		function orderToOrderfac(){
			//查询业务配置是否有配置生产价币种
			var chkFlag=false;
			DWREngine.setAsync(false);
			cotOrderService.getList('CotPriceCfg', function(res) {
				if (res.length != 0) {
					if (res[0].facPriceUnit == null) {
						chkFlag=true;
					}
				}else{
					chkFlag=true;
				}
			});
			DWREngine.setAsync(true);
			if(chkFlag){
				Ext.Msg.alert("Message"," Pls. fill in the type of currency in the business configuration！");
				return;
			}
		
			var orderId = $('orderId').value;
			var detailIds = '';
			DWREngine.setAsync(false);
			var isCheck = Ext.getCmp("chkEle").getValue();
			// 通过主订单id获取订单明细id的集合(未分解的订单明细)
			cotOrderService.getDetailIdsByOrderId(parseInt(orderId), function(detailIdList) {
				if (detailIdList != null) {
					for (var i = 0; i < detailIdList.length; i++) {
						detailIds += detailIdList[i] + ",";
					}
				}
			});
			if (detailIds.length == 0) {
				Ext.Msg.alert("Message"," Pls. confirm if there's content or the quantity is 0 in the order!");
				return;
			} else {
				if(isCheck)//按单分解
					decodeOrderByEleIdList();
				else
				{
					var map = null;
					cotOrderFacService.saveOrderFacByDecomposeOrder(parseInt(orderId),function(res){
						// 批量修改主采购单的总数量,总箱数,总体积,总金额
						map = res;
						cotOrderFacService.modifyFacTotalByMap(map,function(res1) {
							self.location.reload();
						});
					})
				}
			}
			DWREngine.setAsync(true);
		}
		// 定时器
		var myInterval;
		function refresh() {
		
			DWREngine.setAsync(false);
			
			cotOrderService.getOrderFacIds(parseInt($('orderId').value),function(facids){
				$('orderfacIds').value = facids.substring(0,facids.length-1);
			});
			
			DWREngine.setAsync(true);
			var ids = $('orderfacIds').value;
			
			var ecsideObj = ECSideUtil.getGridObj("orderFacTable");
			ecsideObj.init();
			//分解后即加载采购信息
			if(ids != ''){
				var queryPara = {
					
					'facIds' : ids
					
				};
				ECSideUtil.queryECForm('orderFacTable', queryPara, true);
			}
	
			clearInterval(myInterval);
	
		}				
		
		
		//打开采购单编辑页面
		function windowopenMod(obj){
			//修改权限判断
			var isPopedom = getPopedomByOpType("cotorderfac.do","MOD");
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
					Ext.Msg.alert("Message","Pls. select one record");
					return;
				}
				else if(ids.length > 1)
				{
					Ext.Msg.alert("Message","You can select only one record!")
					return;
				}else{
					obj = ids[0].id;
				}
			}
			openFullWindow('cotorderfac.do?method=add&id='+obj+'&oId='+$('orderId').value);			
		}
		
		function getIds() {
			var list = Ext.getCmp("orderfacGrid").getSelectionModel().getSelections();
			var res = new Array();
			Ext.each(list, function(item) {
				var cotOrderFac = new CotOrderFac();
				cotOrderFac.id = item.id;
				res.push(cotOrderFac);
			});
			return res;
		}
		
		//批量删除主单
		function deleteBatch(){
		    	
		    var isPopedom = getPopedomByOpType("cotorderfac.do","DEL");
			if(isPopedom == 0)//没有删除权限
			{
				Ext.Msg.alert("Message","Sorry, you do not have Authority!");
				return;
			}
				
			var list = getIds();
		    if(list.length == 0){	
		    	Ext.Msg.alert("Message","Pls. select one record");
		    	return;
		    }
	
		    var checkFlag = false;
			var orderNo ='';			
			DWREngine.setAsync(false);
			cotOrderFacService.isOrderStatus(list,function(orderNos){
				if(orderNos != null){
					orderNo = orderNos;
					checkFlag = true;
				}
			});
			DWREngine.setAsync(true);
			if(checkFlag){
				Ext.Msg.alert("Message",'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster！');
				return;
			}
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
				Ext.Msg.alert("Message",'Sorry，部分采购单已存在应付帐款记录,不能删除!');
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
				Ext.Msg.alert("提示框",'对不起，部分采购单其他费用已导入出货,不能删除!');
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
				Ext.Msg.alert("提示框",'对不起，部分采购单其他费用已导入订单！');
				return;
			}
				
			Ext.MessageBox.confirm('Message', 'Do you confirm to delete the selected record?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					cotOrderFacService.deleteOrderFacs(list,function(res){
						Ext.Msg.alert("Message","Deleted successfully!");					
						reloadGrid('orderfacGrid');
						reloadGrid('detailGrid');
						reloadGrid('orderGrid');
					});	
					DWREngine.setAsync(true);
				}
			});
		}	
		
		// 根据原有EC表格修改的数据修改Map中相应的对象
		function updateOrderFacDetailMapValue(obj) {
			var b = false;
			var formPrice = document.forms['orderToOrderFac'];
			b = Validator.Validate(formPrice, 1);
			if (!b) {
				return false;
			}
			var property = obj.name;
			var value = obj.value;
			var tr = obj.parentNode.parentNode;
			var orderDetailId = getCellObj(tr, 'orderToOrderFac', 'orderDetailId');
			var ele = '';
		
			if (typeof(orderDetailId) == 'object') {
				ele = orderDetailId.value;
			} else {
				ele = getCellObj(tr, 'orderToOrderFac', 'orderDetailId', true);
				ECSideUtil.updateEditCell(obj);
			}
			cotOrderFacService.updateAddMap(ele, property, value,function(res) {
			});
		}	
		// 采购数量改变事件
		function boxCountChangeClick(obj) {
			updateOrderFacDetailMapValue(obj);
		}	
		
		function refreshOrderdetail()
		{
			var queryPara = {
				'orderId' : $('orderId').value
			}
			ECSideUtil.queryECForm('orderDetail', queryPara, true);
		}
		
		//打开采购单添加页面(指派)
		function windowopenAdd(){
			//添加权限判断
			var isPopedom = getPopedomByOpType("cotorderfac.do","ADD");
			if(isPopedom == 0)//没有添加权限
			{
				Ext.Msg.alert("Message","Sorry, you do not have Authority!");
				return;
			}
			var spFlag = true;
			DWREngine.setAsync(false);
			cotOrderService.checkUnCountIsEqual(parseInt($('orderId').value),function(flag){
				if(flag){
					spFlag = false;
				}
			})
			if(spFlag){
				Ext.Msg.alert("Message",'The order does not resolve,can not be Assigned！');
				return;
			}
			//判断该订单是否已分解完成
			if($('orderId').value != null && $('orderId').value != ''){
				cotOrderService.checkUnCountIsZero(parseInt($('orderId').value),function(flag){
					if(flag){
						openFullWindow('cotorderfac.do?method=assign&oId='+$('orderId').value);
					}else{
						Ext.Msg.alert("Message","The order has been resolved!");
					}
				});
			}
			DWREngine.setAsync(true);
		}	
		</script>
	</head>
	<%
		String orderId = request.getParameter("orderId");
	%>
	<body>
		<input type="hidden" id="orderId" name="orderId" value="<%=orderId %>" />
		<input type="hidden" id="orderfacIds" name="orderfacIds" />
		<input type="hidden" id="orderfacId" name="orderfacId" />
	</body>
</html>
