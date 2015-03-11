// 禁止鼠标右键
document.oncontextmenu = new Function("event.returnValue=true;");
// 打开出货单添加页面
function windowopenAdd() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.MessageBox.alert('Message', "Sorry, you do not have Authority! ");
		return;
	}
	openFullWindow('cotorderout.do?method=addOrder');
}
// 打开出货单编辑页面
function windowopenMod(obj,odNo) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.MessageBox.alert('Message', "Sorry, you do not have Authority! ");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert('Message', "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert('Message', "Sorry,you can select only one record!")
			return;
		} else
			obj = ids[0][0];

	}
	openFullWindow('cotorderout.do?method=addOrder&id=' + obj+'&odNo='+odNo);
	// $('detailDiv').style.display = 'none';
}

// 删除
function del(id, status) {
	if($("invalidNo").value == ""){
		Ext.Msg.alert("Message","CREDIT NOTE is requered");
		return;
	}
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.MessageBox.alert('Message', "Sorry, you do not have Authority! ");
		return;
	}
	// 判断该单是否已被审核
//	if (status == 2 && loginEmpId != "admin") {
//		Ext.MessageBox.alert('Message', 'Sorry, It has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
//		return;
//	}
	// 判断出货单是否有应收帐,应付帐,是否被排载(0可删除,1有应收帐,2有应付帐,3有排载)
//	cotOrderOutService.checkCanDelete(id, function(flag) {
//				if (flag == 1) {
//					Ext.MessageBox.alert('Message', 'This Invoice invalided already!');
//					return;
//				}
//				if (flag == 0) {
					var list = new Array();
					list.push(id);
					Ext.MessageBox.confirm('Message', "Do you invalid this Invoice?",
							function(btn) {
								if (btn == 'yes') {
									cotOrderOutService.deleteOrders(list,$("invalidNo").value,
											function(res) {
												if (res) {
													reloadGrid('orderoutGrid');
													invalidWin.close();
													Ext.MessageBox.alert(
															'Message', "Invalid Success");
												} else {
													Ext.MessageBox.alert(
															'Message', "Invalid Failed");
												}
											})
								}
							});
//				}
//			})

}

// 批量删除
function deleteBatch() {
	var list = Ext.getCmp("orderoutGrid").getSelectionModel().getSelections();
	if (list.length == 0) {
		Ext.MessageBox.alert('Message', "Please select a record!");
		return;
	}
	var ary = new Array();
	Ext.each(list, function(item) {
				ary.push(item.id);
			});
	DWREngine.setAsync(false);
	cotOrderOutService.checkCanDeleteBatch(ary, function(flag) {
		if(flag.length==0){
			Ext.MessageBox.alert('Message', "All Invoices has Invalided!");
		}else{
			var str = "Do you invalid this Invoice?";
			if(flag.length!=ary.length){
				str = "Some Invoices has Invalided, Do you want invalid other invoices?";
			}
			Ext.MessageBox.confirm('Message', str, function(btn) {
				if (btn == 'yes') {
					cotOrderOutService.deleteOrders(flag, function(res) {
								Ext.MessageBox.alert('Message', "Invalid Success");
								reloadGrid('orderoutGrid');
							});
				}
			});
		}
	});
	DWREngine.setAsync(true);
	
}
function getIds() {
	var list = Ext.getCmp("orderoutGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var ary = new Array();
				ary.push(item.id);
				ary.push(item.get("orderStatus"));
				res.push(ary);
			});
	return res;
}

// 导出方法
function exportRpt() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert('Message', 'Please select a record!');
		return;
	} else if (list.length > 1) {
		Ext.MessageBox.alert('Message', 'Sorry,you can select only one record!');
		return;
	}
	var orderStatus = list[0][1];
	if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
		Ext.MessageBox.alert('Message', 'Sorry,it is not reviewd to export!');
		return;
	}
	var model = $('reportTemple').value;
	if (model == '') {
		Ext.MessageBox.alert('Message', 'please select templates!');
		return;
	}
	if ($('allPage').checked) {
		headerFlag = true;
	} else {
		headerFlag = false;
	}
	// var factoryId = $('factoryId').value;
	var factoryId = "";
	var printType = $('printType').value;
	var record = Ext.getCmp("orderoutGrid").getSelectionModel().getSelections()[0];
	var orderOutNo = record.get("orderNo");
	openWindow("downOrderOut.action?reportTemple=" + model + "&printType="
			+ printType + "&factoryId=" + factoryId + "&orderId=" + list[0][0]
			+ "&headerFlag=" + headerFlag + "&orderOutNo=" + orderOutNo);
}

// 预览方法
function viewRpt() {
	var _height = window.screen.height;
	var _width = window.screen.width;
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert('Message', 'Please select a record!');
		return;
	} else if (list.length > 1) {
		Ext.MessageBox.alert('Message', 'Sorry,you can select only one record!');
		return;
	}
	var model = $('reportTemple').value;
	if (model == '') {
		Ext.MessageBox.alert('Message', 'please select templates!');
		return;
	}

	// var headerFlag = $('headerFlag').value;
	if ($('allPage').checked) {
		headerFlag = true;
	} else {
		headerFlag = false;
	}
	var printType = $('printType').value;
	// var factoryId = $('factoryId').value;
	var factoryId = "";
	var ids = getIds();
	var orderStatus = ids[0][1];
	var printFlag = 1;
	if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
		printFlag = 0;// 审核没有通过，不在Applet页面显示打印按钮
	}
	openMaxWindow(_height, _width,
			"previewrpt.do?method=queryEleRpt&reportTemple=" + model
					+ "&orderOutId=" + list[0][0] + "&flag=ORDEROUT"
					+ "&factoryId=" + factoryId + "&headerFlag=" + headerFlag
					+ "&printFlag=" + printFlag);
}

// 所需参数
var mail = {};
// 发邮件
function sendMail() {
	if ($('reportTemple').value == '') {
		Ext.MessageBox.alert('Message', 'please select templates!');
		return;
	}
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert('Message', 'Please select a record!');
		return;
	} else if (list.length > 1) {
		Ext.MessageBox.alert('Message', 'Sorry,you can select only one record!');
		return;
	}
	// 获得选择行的出货号
	var orderNo = '';
	var objs = document.getElementsByName("checkOrderID");
	for (var i = 0; i < objs.length; i++) {
		var chk = objs[i];
		if (chk.checked) {
			var p = chk.parentNode.parentNode;
			var cells = p.cells;
			orderNo = cells[1].innerText;
		}
	}
	DWREngine.setAsync(false);
	// 判断客户是否有邮箱
	cotOrderOutService.getCusByOrderId(list[0][0], function(res) {
		if (res == null || res.customerEmail == null || res.customerEmail == '') {
			Ext.MessageBox.alert('Message', '该客户还没有邮箱,请先去客户管理中添加!');
			return;
		}
		mail.custEmail = res.customerEmail;
		mail.custId = res.id;
		mail.custName = res.customerShortName;
		mail.orderId = list[0][0];
		mail.orderNo = orderNo;
		mail.reportTemple = $('reportTemple').value;
		mail.printType = $('printType').value;
		mail.factoryId = $('factoryId').value;
		// 设置层显示位置
		var width = window.screen.availWidth / 3;
		var height = window.screen.availHeight / 4;
		$("mailDiv").style.top = height;
		$("mailDiv").style.left = width;
		$('mailDiv').style.display = 'block';
		setTimeout("openMail()", 600);
	});
	DWREngine.setAsync(true);
}
// 打开邮件发送页面
function openMail() {
	DWREngine.setAsync(false);
	cotOrderOutService.saveMail(mail, function(res) {
				$('mailDiv').style.display = 'none';
				var id = res;
				if (id == null) {
					Ext.MessageBox.alert('Message', '邮件配置失败,请联系管理员!');
				} else {
					openWindow('cotmailbox.do?method=modifyMail&id=' + id);
				}
				$('mailDiv').style.display = 'none';
			});
	DWREngine.setAsync(true);
}

// 清空主单查询form
function clearPriceForm() {
	$('queryForm').reset();
}

// 初始化页面
function initForm() {
	bindSel();
}
// 显示打印层
function showPrintDiv(e) {
	var ids = getIds();
	if (ids.length == 0) {
		Ext.MessageBox.alert('Message', "Please select a record");
		return;
	} else if (ids.length > 1) {
		Ext.MessageBox.alert('Message', "Sorry,you can select only one record!")
		return;
	} else {
		var orderStatus = ids[0][1];
		cotOrderOutService.getFactorysByMainId(ids[0][0], function(res) {
					bindSelect('factoryId', res, 'id', 'shortName');
				});
		// 设置层显示位置
		var t = e.offsetTop;
		var l = e.offsetLeft;
		while (e = e.offsetParent) {
			t += e.offsetTop;
			l += e.offsetLeft;
		}
		document.getElementById("printDiv").style.top = t + 20;
		document.getElementById("printDiv").style.left = l - 260;
		document.getElementById("printDiv").style.display = "block";
	}
}
// 关闭打印层
function closePrintDiv() {
	document.getElementById("printDiv").style.display = "none";
}

// 查询主单
function queryMain() {
	if (event.keyCode == 13) {
		query();
	}
}

// 点击选择模糊查询结果后方法
function handleComon(id, val, fno, txtId) {
	if (txtId == 'customerShortName') {
		$('customerShortName').value = val;
		$('custId').value = id;
		query();
	} else {
		$('businessPerson').value = id;
		$('empsName').value = val;
		query();
	}
	$(txtId).style.background = '#E2F4FF';
	$("search").style.display = "none";// 隐藏列表
}
