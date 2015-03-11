// 表格原模版
var oldTemp = '';
// 初始化
function initform() {
	oldTemp = $('add_template').value;
	validForm();
	// 初始化创建webFXTabPane对象
	setupAllTabs();
	// 设置第一个tab为被选中
	webFXTabPane.setSelectedIndex(0);
	// 订单编号
	var id = $('pId').value;
	DWREngine.setAsync(false);
	bindSel();
	if (id == 'null' || id == '') {
		// 初始化送货时间为当前时间
		$('sendDate').value = getCurrentDate("yyyy-MM-dd");
		$('businessPerson').value = $('empId').value;
		showText('CotEmps', 'empsName', $('empId').value, 'empsName');
	} else {
		//禁止再选择订单
		$('changeOrder').disabled = 'disabled';
		// 加载信息
		cotLabelService.getCotLabelById(parseInt(id), function(res) {
			// 下单时间
			var date = new Date(res.sendDate);
			$('sendDate').value = date.getYear() + '-' + (date.getMonth() + 1)
					+ '-' + date.getDate();
			DWRUtil.setValues(res);
			// 加载业务员
			if (res.businessPerson != null) {
				$('businessPerson').value = res.businessPerson;
				showText('CotEmps', 'empsName', res.businessPerson, 'empsName');
			}
			// 加载厂家
			if (res.factoryId != null) {
				$('factoryId').value = res.factoryId;
				showText('CotFactory', 'shortName', res.factoryId, 'factoryShortName');
			}
		});
		// 初始化ECtable
		var ecsideObj = ECSideUtil.getGridObj("labelDetail");
		ecsideObj.init();
		// 加载报价明细信息
		var queryPara = {
			'labelId' : $('pId').value
		};
		ECSideUtil.queryECForm('labelDetail', queryPara, true);
		// 显示删除和打印按钮
		$('modBtnShow').style.display = 'block';
	}
	DWREngine.setAsync(true);
}
// 初始化表单验证字段
function validForm() {
	appendattribute("factoryShortName", "厂家不能为空", "Require");
	appendattribute("labelNo", "请选择合同号", "Require");
	appendattribute("empsName", "请选择业务员", "Require");
	appendattribute("otherMoney", "其他费用只能为数字", "Double", 'false');
}
// 加载下拉框数据
function bindSel() {
	// 查找所有公司
	sysdicutil.getDicListByName('company', function(res) {
		bindSelect('companyId', res, 'id', 'companyShortName');
	});
	// 导出模版
	queryService.getRptFileList(10, function(res) {
		bindSelect('reportTemple', res, 'rptfilePath', 'rptName');
		//查询默认的模板
		queryService.getRptDefault(10, function(def) {
			if(def!=null){
				DWRUtil.setValue('reportTemple',def.rptfilePath);
			}
		});
	});
}

// 点击选择模糊查询结果后方法
function handleComon(id, val, fno, txtId) {
	if (txtId == 'factoryShortName') {
		$('factoryId').value = id;
		$('factoryShortName').value = val;
	}
	if (txtId == 'empsName') {
		$('businessPerson').value = id;
		$('empsName').value = val;
	}
	$(txtId).style.background = '#E2F4FF';
	$("search").style.display = "none";// 隐藏列表
}

// 显示订单表格
function showOrders() {
	var obj = $("orderDiv").style.display;
	if (obj == 'none') {
		// 加载样品查询表格
		var frame = document.orderTabFrame;
		frame.location.href = "cotquery.do?method=queryOrderByLabel";
		document.getElementById("orderDiv").style.top = 45;
		document.getElementById("orderDiv").style.left = 160;
		$("orderDiv").style.display = 'block';
	} else {
		$("orderDiv").style.display = 'none';
	}
}

//导入订单明细
function findDetails(id, orderNo) {
	//移除所有旧的
	var span = {};
	span.className ='checkboxHeader';
	ECSideUtil.checkAll(span,'chk','labelDetail');
	ECSideUtil.delFromGird(null, 'labelDetail', 'chk');
	//添加新单
	cotLabelService.getOrderDetails(id, function(res) {
		$('orderId').value = id;
		$('labelNo').value = orderNo;
		for (var i = 0; i < res.length; i++) {
			var label = new CotLabeldetail();
			label.eleId = res[i].eleId;
			label.custNo = res[i].custNo;
			label.barcode = res[i].barcode;
			label.factoryId = res[i].factoryId;
			label.factoryShortName = res[i].factoryShortName;
			label.orderdetailId = res[i].id;
			
			if(res[i].boxCount!=null && res[i].boxCount>0){
				//产品标数量
				var count = res[i].boxCount;
				if(count<=1000){
					label.labelProd = Math.ceil(count*1.05);
				}else{
					label.labelProd = count+50;
				}
				//内盒
				if(res[i].boxIbCount!=null && res[i].boxIbCount!=0){
					var iNum = count/res[i].boxIbCount;
					if(iNum<=1000){
						label.labelIb = Math.ceil(iNum*1.05);
					}else{
						label.labelIb = iNum+50;
					}
				}else{
					label.labelIb = 0;
				}
				//中盒
				if(res[i].boxMbCount!=null && res[i].boxMbCount!=0){
					var iNum = count/res[i].boxMbCount;
					if(iNum<=1000){
						label.labelMb = Math.ceil(iNum*1.05);
					}else{
						label.labelMb = iNum+50;
					}
				}else{
					label.labelMb = 0;
				}
				//外盒
				if(res[i].boxObCount!=null && res[i].boxObCount!=0){
					var iNum = count/res[i].boxObCount;
					if(iNum<=1000){
						label.labelOb = Math.ceil(iNum*1.05);
					}else{
						label.labelOb = iNum+50;
					}
				}else{
					label.labelOb = 0;
				}
			}
			
			var obj = new Object();
			for (var p in label) {
				obj[p + 'temp'] = label[p];
			}
			setObjToGrid(obj);
		}
	});
}

// 用于添加数据到表格的隐藏域
var temp = '';
// 添加对象到表格
function setObjToGrid(obj) {
	if (temp == '') {
		$('detailDiv').innerHTML = oldTemp;
		// 更改name的值，后面加temp
		temp = addTempStr($('detailDiv').innerHTML);
		$('detailDiv').innerHTML = temp;
	}
	$('detailDiv').innerHTML = temp;
	// 将obj的值填充到temp里面的表单
	DWRUtil.setValues(obj);
	// 更改name的值，去掉后面的temp
	var re = /temp/g;
	var s2 = $('detailDiv').innerHTML.replace(re, '');

	// 替换大写的TPSP 为tpsp
	var re3 = /TPSP/g;
	var s3 = s2.replace(re3, 'tpsp');
	// 重新设置表格的添加模板
	$('add_template').value = s3;
	// 添加到表格中
	ECSideUtil.addToGird(null, 'add_template', 'labelDetail');
	// 还原隐藏域
	$('detailDiv').innerHTML = temp;
}

// 删除表格选择的行
function deleteSelectRow() {
	ECSideUtil.delFromGird(null, 'labelDetail', 'chk');
}

// 保存主单
function saveLabel() {
	var formOrder = document.forms['orderForm'];
	var b = false;
	b = Validator.Validate(formOrder, 1);
	if (!b)
		return false;
		
	var g = false;
	var gPrice = document.forms['labelDetail'];
	g = Validator.Validate(gPrice, 1);
	if (!g)
		return false;
		
	var flag = window.confirm('是否保存该标签单?');
	if (!flag) {
		return;
	}
	var label = DWRUtil.getValues('orderForm');
	DWREngine.setAsync(false);
	var cotLabel = new CotLabel();
	if ($('pId').value != 'null' && $('pId').value != '') {
		cotLabel.id=$('pId').value;
	}
	for (var p in cotLabel) {
		if (p!='id' && p != 'sendDate') {
			cotLabel[p] = label[p];
		}
	}
	cotLabelService.saveOrUpdateLabel(cotLabel, $('sendDate').value, function(
			res) {
		if (res != null) {
			$('pId').value = res;
			loadbar(1);
			var ecsideObj = ECSideUtil.getGridObj('labelDetail');
			var form = ecsideObj.ECForm;
			// 更改添加action参数
			var urli = form.getAttribute("insertAction");
			urli = urli + '&labelId=' + res;
			form.setAttribute("insertAction", urli);
			// 更改修改action参数
			var urli = form.getAttribute("updateAction");
			urli = urli + '&labelId=' + res;
			form.setAttribute("updateAction", urli);
			// 保存添加的样品明细
			ECSideUtil.saveGird(null, 'labelDetail', true);
			myInterval = setInterval("refresh()", 1000);
		} else {
			alert('保存失败');
			return;
		}
	});
	DWREngine.setAsync(true);
}
// 定时器
var myInterval;
function refresh() {
	var addList = ECSideUtil.getInsertRows("labelDetail");
	var updateList = ECSideUtil.getUpdatedRows("labelDetail");
	if (addList.length == 0 && updateList.length == 0) {
		// 显示删除和打印按钮
		$('modBtnShow').style.display = 'block';
		// 加载明细信息
		var queryPara = {
			'labelId' : $('pId').value
		}
		ECSideUtil.queryECForm('labelDetail', queryPara, true);
		clearInterval(myInterval);
		//统计总金额
		cotLabelService.updateTotalMoney($('pId').value,function(res){});
		loadbar(0);
	}
}

// 删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		alert("您没有删除权限");
		return;
	}
	var list = new Array();
	list.push(id);
	var flag = window.confirm("是否确定删除该张标签单?");
	if (flag) {
		DWREngine.setAsync(false);
		cotLabelService.deleteLabel(list, function(res) {
			if (res==0) {
				alert("删除成功");
				closeandreflashEC('true', 'labelTable', false);
			} else {
				alert("删除失败，该订单已经被使用中");
			}
		})
		DWREngine.setAsync(true);
	}
}

//表格单元格失去焦点时验证
function checkTableCell(obj){
	var g = false;
	var gPrice = document.forms['labelDetail'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}else{
		ECSideUtil.updateEditCell(obj);
	}
}

// 判断是否有更新表格
function isUpdateGrid() {
	var rows = ECSideUtil.getUpdatedRows('labelDetail');
	var rows2 = ECSideUtil.getInsertRows('labelDetail');
	var rows3 = ECSideUtil.getDeletedRows('labelDetail');
	if (rows == 0 && rows2 == 0 && rows3 == 0) {
		return false;
	} else {
		return true;
	}
}

// 显示打印层
function showPrintDiv(e) {
	var flag = isUpdateGrid();
	if (flag) {
		saveLabel();
	}
	var obj = $("printDiv").style.display;
	if (obj == 'none' || obj == '') {
		// 设置层显示位置
		var t = e.offsetTop;
		var l = e.offsetLeft;
		while (e = e.offsetParent) {
			t += e.offsetTop;
			l += e.offsetLeft;
		}
		$("printDiv").style.top = t - 160;
		$("printDiv").style.left = l;
		$("printDiv").style.display = "block";
	} else {
		$("printDiv").style.display = "none";
	}
}

// 关闭打印层
function closePrintDiv() {
	document.getElementById("printDiv").style.display = "none";
}

// 导出方法
function exportRpt() {
	var model = $('reportTemple').value;
	if (model == '') {
		alert('请选择导出模板!');
		return;
	}
	var printType = $('printType').value;
	openWindow("downLabel.action?reportTemple=" + model
			+ "&printType=" + printType + "&labelId=" + $('pId').value);
}

// 预览方法
function viewRpt() {
	var _height = window.screen.height;
	var _width = window.screen.width;
	var model = $('reportTemple').value;
	if (model == '') {
		alert('请选择导出模板!');
		return;
	}
	var headerFlag = $('headerFlag').value;
	if ($('allPage').checked) {
		headerFlag = true;
	} else {
		headerFlag = false;
	}
	var printType = $('printType').value;
	openMaxWindow(_height, _width,
			"previewrpt.do?method=queryEleRpt&reportTemple=" + model
					+ "&labelId=" + $('pId').value + "&headerFlag="
					+ headerFlag);
}

// 加载其他图片浏览
function showPicture() {
	// 设置权限为空
	mapPopedom = null;
	var isPopedom = getPopedomByOpType("cotlabel.do", "PIC");
	if (isPopedom == 0) // 没有查看图片权限
	{
		alert("您没有权限查看图片");
		return;
	} else {
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			alert("请先保存标签单再添加其他图片!");
		}else{
			var picFrame = document.showPic;
			picFrame.location.href = "cotlabel.do?method=showPicture&eId="
					+ id;
		}
	}
}
