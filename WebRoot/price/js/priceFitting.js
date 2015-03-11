// 表格原模版
var oldTemp = '';
function init(){
	// 设置表格原模版(替换某些input为textarea)
	oldTemp = replaceToTextarea($('add_template').value, 'name="fitRemark"');
	
	// 初始化创建webFXTabPane对象
	setupAllTabs();
	// 设置第一个tab为被选中
	webFXTabPane.setSelectedIndex(0);

	var isHId = $('isHId').value;
	if(isHId==0){
		$('fitTbr').style.display='none';
	}
}

 //显示报价成本层
function loadPriceElePrice() {
	var isHId = $('isHId').value;
	var rdm = $('rdm').value;
	var frame = document.priceElePriceTab;
	frame.location.href = "cotprice.do?method=queryPriceElePrice&isHId="+isHId+"&rdm="+rdm;
}

// 添加新的空白行
function addNewGrid() {
	if (parent.$('priceStatus').value == 2 && loginEmpId != "admin") {
		alert('对不起,该单已经被审核不能再新增配件!');
		return;
	}
	var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
	if (isPopedom == 0) {// 没有删除权限
		alert("您没有报价的修改权限");
		return;
	}
	DWREngine.setAsync(false);
	$('add_template').value = oldTemp;
	// 添加到表格中
	ECSideUtil.addToGird(null, 'add_template', 'fittingTable');
	// 把焦点放在新增空白行
	var addList = ECSideUtil.getInsertRows("fittingTable");
	var cells = addList[addList.length - 1].cells;
	var eInput = cells[1].getElementsByTagName("INPUT")[0];
	eInput.focus();
	DWREngine.setAsync(true);
}

// 插入表格或显示查询层
function inputToGrid(fitNo) {
	if (event.keyCode == 13) {
		if(fitNo.value==''){
			alert('请输入配件号!');
			return;
		}
		var pId = $('isHId').value;
		//判断输入的配件号是否存在.完全存在的话直接加到表格中,有模糊数据弹出层.
		cotPriceService.findIsExistFitNo(fitNo.value, pId,function(res) {
			if(res!=null){
				if(res.length==1){
					if(res[0]==0){
						alert('您输入的配件已经添加,请重新输入!');
						fitNo.value='';
					}else{
						var tr = fitNo.parentNode.parentNode;
						var flag = false;
						var fitNoAry = document.getElementsByName("fitNo");
						for (var i = 0; i < fitNoAry.length; i++) {
							if(fitNoAry[i]!=fitNo && fitNoAry[i].value==res[0].fitNo){
								flag = true;
								break;
							}
						}
						if(flag){
							alert('您输入的配件已经添加,请重新输入!');
							fitNo.value='';
						}else{
							setEleInfo(tr,res[0]);
							addNewGrid();
						}
					}
				}else{
					var obj = $("fittingDiv").style.display;
					if (obj == 'none') {
						// 加载样品查询表格
						var frame = document.fittingTabFrame;
						frame.location.href = "cotquery.do?method=queryFitting&fitNo=" + fitNo.value;
						document.getElementById("fittingDiv").style.top = 60;
						document.getElementById("fittingDiv").style.left = 80;
						$("fittingDiv").style.display = 'block';
						fitNo.value='';
					} else {
						$("fittingDiv").style.display = 'none';
					}
				}
			}else{
				alert('您输入的配件不存在,请重新输入!');
				fitNo.value='';
			}
		});
	}
}

//清除原先空白行
function removeOldEmptyGrid() {
	var addList = ECSideUtil.getInsertRows("fittingTable");
	for (var i = addList.length - 2; i >= 0; i--) {
		var obj = addList[i];
		var cells = obj.cells;
		var eInput = cells[1].getElementsByTagName("INPUT")[0];
		var eleId = eInput.value;
		if (eleId == '') {
			addList[i].parentNode.removeChild(addList[i]);
		}
	}
}

function setEleInfo(tr, obj) {
	// 来源配件编号
	var fittingId = getCellObj(tr, 'fittingTable', 'fittingId', false);
	fittingId.value = obj.id;
	// 配件号
	var fitNo = getCellObj(tr, 'fittingTable', 'fitNo', false);
	fitNo.value = obj.fitNo;
	fitNo.disabled = true;
	// 配件名称
	var fitName = getCellObj(tr, 'fittingTable', 'fitName', false);
	fitName.value = obj.fitName;
	// 规格型号
	var fitDesc = getCellObj(tr, 'fittingTable', 'fitDesc', false);
	fitDesc.value = obj.fitDesc;
	// 单位
	var fitUseUnit = getCellObj(tr, 'fittingTable', 'fitUseUnit', false);
	fitUseUnit.value = obj.useUnit;
	// 用量
	var fitUsedCount = getCellObj(tr, 'fittingTable', 'fitUsedCount', false);
	fitUsedCount.value = 1;
	// 数量
	var fitCount = getCellObj(tr, 'fittingTable', 'fitCount', false);
	fitCount.value = 1;
	// 供应商
	var facId = getCellObj(tr, 'fittingTable', 'facId', false,'select');
	if(obj.facId!=null){
		facId.value = obj.facId;
	}
	// 单价
	var fitPrice = getCellObj(tr, 'fittingTable', 'fitPrice', false);
	fitPrice.value = (obj.fitPrice/obj.fitTrans).toFixed("2");
	// 总金额
	var fitTotalPrice = getCellObj(tr, 'fittingTable', 'fitTotalPrice', false);
	fitTotalPrice.value = ((obj.fitPrice/obj.fitTrans) * fitUsedCount.value*fitCount.value).toFixed("2");
}

//添加选择的配件到可编辑表格
function insertSelect(ids) {
	var ecsideObj = ECSideUtil.getGridObj('fittingTable');
	var temp = "";
	for (var i = 0; i < ids.length; i++) {
		temp+=ids[i]+",";
	}
	DWREngine.setAsync(false);
	cotPriceService.findFittingsByIds(temp,$("rdm").value, function(res) {
		for (var i = 0; i < res.length; i++) {
			$('add_template').value = oldTemp;
			// 添加到表格中
			ECSideUtil.addToGird(null, 'add_template', 'fittingTable');
			var rows = ecsideObj.ECListBody.rows;
			var tr = rows[rows.length-1];
			setEleInfo(tr,res[i]);
		}
		addNewGrid();
	});
	DWREngine.setAsync(true);
	//关闭配件查询层
	$('fittingDiv').style.display = 'none';

}

// 删除表格选择的行
function deleteSelectRow() {
	if (parent.$('priceStatus').value == 2 && loginEmpId != "admin") {
		alert('对不起,该单已经被审核不能再删除配件!');
		return;
	}
	var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
	if (isPopedom == 0) {// 没有删除权限
		alert("您没有报价的修改权限");
		return;
	}
	ECSideUtil.delFromGird(null, 'fittingTable', 'checkFitID');
}

// 保存配件明细
function save() {
	if (parent.$('priceStatus').value == 2 && loginEmpId != "admin") {
		alert('对不起,该单已经被审核不能再保存配件!');
		return;
	}
	var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
	if (isPopedom == 0) {// 没有删除权限
		alert("您没有报价的修改权限");
		return;
	}
	DWREngine.setAsync(false);
	var g = false;
	var gPrice = document.forms['fittingTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}

	var ecsideObj = ECSideUtil.getGridObj('fittingTable');
	var form = ecsideObj.ECForm;
	//获得报价明细编号
	var isHId = $('isHId').value;
	//获得报价编号
	var oId = parent.$('pId').value;
	
	// 更改添加action参数
	var urli = form.getAttribute("insertAction");
	urli = urli + '&pFId=' + isHId+ '&oId=' + oId;
	form.setAttribute("insertAction", urli);

	loadbar(1);
	// 保存添加的配件明细
	ECSideUtil.saveGird(null, 'fittingTable', true);// true：批量 false：逐条
	myInterval = setInterval("refresh()", 1000);
	DWREngine.setAsync(true);
}
// 定时器
var myInterval;
function refresh() {
	var addList = ECSideUtil.getInsertRows("fittingTable");
	var updateList = ECSideUtil.getUpdatedRows("fittingTable");
	if (addList.length == 0 && updateList.length == 0) {
		var queryPara = {
			'rdm' : $('rdm').value
		}
		ECSideUtil.queryECForm('fittingTable', queryPara, true);
		clearInterval(myInterval);
		//重新计算该报价明细的生产价
		cotPriceService.updatePriceFac($('isHId').value,$("rdm").value, function(res) {
			parent.getSelectedRow($("rdm").value,res);
		});
		loadbar(0);
	}
}

// 用量改变金额
function fitUsedCountChange(obj) {
	var g = false;
	var gPrice = document.forms['fittingTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}
	if (obj.value == '' || isNaN(obj.value)) {
		obj.value = 0;
	}
	var tr = obj.parentNode.parentNode;
	// 数量
	var fitCount = getCellObj(tr, 'fittingTable', 'fitCount', true);
	if (fitCount == '' || isNaN(fitCount)) {
		fitCount = 0;
	}
	// 单价
	var fitPrice = getCellObj(tr, 'fittingTable', 'fitPrice', true);
	if (fitPrice == '' || isNaN(fitPrice)) {
		fitPrice = 0;
	}
	// 金额
	var fitTotalPrice = getCellObj(tr, 'fittingTable', 'fitTotalPrice', false);
	// 计算金额
	fitTotalPrice.value = (fitCount*obj.value* parseFloat(fitPrice))
			.toFixed('2');

	if (!ECSideUtil.hasClass(tr, "add")) {
		ECSideUtil.updateEditCell(fitTotalPrice);
		ECSideUtil.updateEditCell(obj);
	}
}

// 数量改变订单需求,订单需求改变金额
function fitCountChange(obj) {
	var g = false;
	var gPrice = document.forms['fittingTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}
	if (obj.value == '' || isNaN(obj.value)) {
		obj.value = 0;
	}
	var tr = obj.parentNode.parentNode;
	//用量
	var fitUsedCount = getCellObj(tr, 'fittingTable', 'fitUsedCount', true);
	if (fitUsedCount == '' || isNaN(fitUsedCount)) {
		fitUsedCount = 0;
	}
	// 单价
	var fitPrice = getCellObj(tr, 'fittingTable', 'fitPrice', true);
	if (fitPrice == '' || isNaN(fitPrice)) {
		fitPrice = 0;
	}
	// 金额
	var fitTotalPrice = getCellObj(tr, 'fittingTable', 'fitTotalPrice', false);

	// 计算金额
	fitTotalPrice.value = (obj.value*fitUsedCount* parseFloat(fitPrice))
			.toFixed('2');

	if (!ECSideUtil.hasClass(tr, "add")) {
		ECSideUtil.updateEditCell(fitTotalPrice);
		ECSideUtil.updateEditCell(obj);
	}
}

// 单价改变金额
function fitPriceChange(obj) {
	var g = false;
	var gPrice = document.forms['fittingTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}
	if (obj.value == '' || isNaN(obj.value)) {
		obj.value = 0;
	}
	var tr = obj.parentNode.parentNode;
	// 数量
	var fitCount = getCellObj(tr, 'fittingTable', 'fitCount', true);
	if (fitCount == '' || isNaN(fitCount)) {
		fitCount = 0;
	}
	//用量
	var fitUsedCount = getCellObj(tr, 'fittingTable', 'fitUsedCount', true);
	if (fitUsedCount == '' || isNaN(fitUsedCount)) {
		fitUsedCount = 0;
	}
	// 金额
	var fitTotalPrice = getCellObj(tr, 'fittingTable', 'fitTotalPrice', false);
	// 计算金额
	fitTotalPrice.value = (obj.value * fitCount*fitUsedCount).toFixed('2');

	if (!ECSideUtil.hasClass(tr, "add")) {
		ECSideUtil.updateEditCell(fitTotalPrice);
		ECSideUtil.updateEditCell(obj);
	}
}

//金额改变
function fitTotalPriceChange(obj){
	var g = false;
	var gPrice = document.forms['fittingTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}
	ECSideUtil.updateEditCell(obj);
}

//页面关闭
function clearEle(){
	$('fittingDiv').style.display='none'
}
