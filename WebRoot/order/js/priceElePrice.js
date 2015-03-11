// 表格原模版
var oldTemp = '';
function init(){
	// 设置表格原模版(替换某些input为textarea)
	oldTemp = replaceToTextarea($('add_template').value, 'name="remark"');
	var isHId = $('isHId').value;
	if(isHId==0){
		$('priceTbr').style.display='none';
	}
}

function blurObj(obj){
	var g = false;
	var gPrice = document.forms['elePriceTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}
	ECSideUtil.updateEditCell(obj);
}

function validPrice(obj){
	var g = false;
	var gPrice = document.forms['elePriceTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}
	var tr = obj.parentNode.parentNode;
	if(parseFloat(obj.value)>=1000){
		alert('成本价格不能大于1000!');
		obj.value=0;
	}
	if (!ECSideUtil.hasClass(tr, "add")) {
		ECSideUtil.updateEditCell(obj);
	}
}

// 添加新的空白行
var facPriceUnit = 0;
function addNewGrid() {
	if (parent.parent.$('priceStatus').value == 2 && loginEmpId != "admin") {
		alert('对不起,该单已经被审核不能再新增成本!');
		return;
	}
	var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
	if (isPopedom == 0) {// 没有删除权限
		alert("您没有报价的修改权限");
		return;
	}
	DWREngine.setAsync(false);
	// 设置生产价币种默认值
	if (facPriceUnit == 0) {
		cotPriceService.getList('CotPriceCfg', function(res) {
			if (res.length!=0 && res[0].facPriceUnit != null) {
				facPriceUnit = res[0].facPriceUnit;
			}
		});
	}
	if (facPriceUnit != 0) {
		// 查找priceFacUint所在位置
		var t = oldTemp.indexOf('name="priceFacUint"');
		var h = oldTemp.indexOf('</select>', t);
		
		//删除之前选择的
		var str = oldTemp.substring(t,h);
		var replaceStr = str.replace('selected="selected"','');
		var newTemp = oldTemp.replace(str,replaceStr);
		
		// 查找priceFacUint下拉选项值为facPriceUnit
		var len = 'option value="' + facPriceUnit + '"';
		var g = newTemp.indexOf(len, t);
		
		if (g >= 0 && g < h) {
			oldTemp = newTemp.substring(0, g + len.length)
					+ ' selected="selected"'
					+ newTemp.substring(g + len.length);
		}
	}
	$('add_template').value = oldTemp;
	// 添加到表格中
	ECSideUtil.addToGird(null, 'add_template', 'elePriceTable');
	// 把焦点放在新增空白行
	var addList = ECSideUtil.getInsertRows("elePriceTable");
	var cells = addList[addList.length - 1].cells;
	var eInput = cells[1].getElementsByTagName("INPUT")[0];
	eInput.focus();
	DWREngine.setAsync(true);
}

// 删除表格选择的行
function deleteSelectRow() {
	if (parent.parent.$('priceStatus').value == 2 && loginEmpId != "admin") {
		alert('对不起,该单已经被审核不能再删除成本!');
		return;
	}
	var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
	if (isPopedom == 0) {// 没有删除权限
		alert("您没有报价的修改权限");
		return;
	}
	ECSideUtil.delFromGird(null, 'elePriceTable', 'chkPrice');
}

// 保存配件明细
function save() {
	if (parent.parent.$('priceStatus').value == 2 && loginEmpId != "admin") {
		alert('对不起,该单已经被审核不能再保存成本!');
		return;
	}
	var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
	if (isPopedom == 0) {// 没有删除权限
		alert("您没有报价的修改权限");
		return;
	}
	DWREngine.setAsync(false);
	var g = false;
	var gPrice = document.forms['elePriceTable'];
	g = Validator.Validate(gPrice, 1);
	if (!g) {
		return false;
	}

	var ecsideObj = ECSideUtil.getGridObj('elePriceTable');
	var form = ecsideObj.ECForm;
	//获得报价明细编号
	var isHId = $('isHId').value;
	//获得报价编号
	var oId = parent.parent.$('pId').value;
	
	// 更改添加action参数
	var urli = form.getAttribute("insertAction");
	urli = urli + '&pFId=' + isHId+ '&oId=' + oId;
	form.setAttribute("insertAction", urli);

	loadbar(1);
	// 保存添加的配件明细
	ECSideUtil.saveGird(null, 'elePriceTable', true);// true：批量 false：逐条
	myInterval = setInterval("refresh()", 1000);
	DWREngine.setAsync(true);
}
// 定时器
var myInterval;
function refresh() {
	var addList = ECSideUtil.getInsertRows("elePriceTable");
	var updateList = ECSideUtil.getUpdatedRows("elePriceTable");
	if (addList.length == 0 && updateList.length == 0) {
		var queryPara = {
			'rdm' : $('rdm').value
		}
		ECSideUtil.queryECForm('elePriceTable', queryPara, true);
		clearInterval(myInterval);
		//重新计算该报价明细的生产价
		cotOrderService.updatePriceFac($('isHId').value,$("rdm").value, function(res) {
			parent.parent.getSelectedRow($("rdm").value,res);
		});
		loadbar(0);
	}
}