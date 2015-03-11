// 全局样品变量
var oldElement = null;
// 禁止鼠标右键
document.oncontextmenu = new Function("event.returnValue=false;");
// 修改
function mod() {
	if ($('displayMsg').innerText != '') {
		Ext.Msg.alert("Message",'Article already exists, please re-enter!');
		return;
	}
	// 验证表单
	var formEle = document.forms['cotElementsForm'];
	var b = false;
	b = Validator.Validate(formEle, 1);
	if (!b) {
		return;
	}
	if ($('priceFac').value == '') {
		$('priceFac').value = 0;
	}
	if ($('priceOut').value == '') {
		$('priceOut').value = 0;
	}
	if ($('boxIbCount').value == '') {
		$('boxIbCount').value = 0;
	}
	if ($('boxMbCount').value == '') {
		$('boxMbCount').value = 0;
	}
	if ($('boxObCount').value == '') {
		$('boxObCount').value = 0;
	}
	if ($('boxPbCount').value == '') {
		$('boxPbCount').value = 0;
	}
	if ($('tuiLv').value == '') {
		$('tuiLv').value = 0;
	}
	if ($('liRun').value == '') {
		$('liRun').value = 0;
	}
	// 主样品表单
	var ele = DWRUtil.getValues("cotElementsForm");
	for (var p in ele) {
		if (p != 'eleProTime') {
			oldElement[p] = ele[p];
		}
	}
	var basePath = $('basePath').value;
	var filePath = $('picPath').src;
	var s = filePath.indexOf(basePath);
	var fPath = filePath.substring(s + basePath.length, 111111);
	fPath=decodeURI(fPath);
	oldElement.picPath = fPath;

	oldElement.boxIbType = $('boxIbTypeId').options[$('boxIbTypeId').selectedIndex].innerText;
    oldElement.boxMbType = $('boxMbTypeId').options[$('boxMbTypeId').selectedIndex].innerText;
    oldElement.boxObType = $('boxObTypeId').options[$('boxObTypeId').selectedIndex].innerText;
    oldElement.boxPbType = $('boxPbTypeId').options[$('boxPbTypeId').selectedIndex].innerText;
    
	// 更新
	cotElementsService.modifyElements(oldElement, $('eleProTime').value,
			function(res) {
				if (res) {
					Ext.Msg.alert("Message","Successfully modified！");
					// 刷新父页面表格
					//closeandreflashEC(true, 'elementsTable', false);
				} else {
					Ext.Msg.alert("Message","modified failed！Art No. already exists!");
					$('eleId').select();
				}
			})
}

// 删除样品
function delParent(id) {
	var list = new Array();
	list.push(id);
	var flag = window.confirm("are you sure to delete all of articles?");
	if (flag) {
		cotElementsService.deleteElements(list, function(res) {
			if (res) {
				Ext.Msg.alert("Message","Deleted successfully");
				closeandreflashEC(true, 'elementsTable', false);
			} else {
				Ext.Msg.alert("Message","Deletedm failed，because it has been used.");
			}
		})
	}
}

// 包装类型的值改变事件
function showType(value) {
	if (value == '') {
		return;
	}
	cotElementsService.getBoxTypeById(value, function(res) {
		if(res!=null){
		    if(res.boxIName==null){
			    $('boxIbTypeId').value = '';
			}else{
			    $('boxIbTypeId').value = res.boxIName;
			}
			if(res.boxMName==null){
		    	$('boxMbTypeId').value = '';
			}else{
			    $('boxMbTypeId').value = res.boxMName;
			}
			if(res.boxOName==null){
		    	$('boxObTypeId').value = '';
			}else{
			    $('boxObTypeId').value = res.boxOName;
			}
			if(res.boxPName==null){
		    	$('boxPbTypeId').value = '';
			}else{
			    $('boxPbTypeId').value = res.boxPName;
			}
			calPrice();
		}});
};

// 初始化表单验证字段
function validForm() {
	appendattribute("eleId", "Articel No. must not be null", "Require");
	appendattribute("boxNetWeigth", "N.W. must be numeric types", "Double", 'false');
	appendattribute("boxGrossWeigth", "G.W. must be numeric types", "Double", 'false');
	appendattribute("boxCbm", "CBM must be numeric types", "Double", 'false');
	appendattribute("boxCuft", "CUFT must be numeric types", "Double", 'false');
	appendattribute("boxIbCount", "Packing must be numeric types", "Double", 'false');
	appendattribute("boxMbCount", "Packing must be numeric types", "Double", 'false');
	appendattribute("boxObCount", "Packing must be numeric types", "Double", 'false');
	appendattribute("box20Count", "20''container must be numeric types", "Double", 'false');
	appendattribute("box40Count", "40''container must be numeric types", "Double", 'false');
	appendattribute("box40hqCount", "40HQcontainer must be numeric types", "Double", 'false');
	appendattribute("box45Count", "45''container must be numeric types", "Double", 'false');
	appendattribute("priceFac", "Purchase price must be numeric types", "Double", 'false');
	appendattribute("priceOut", "Sale price must be numeric types", "Double", 'false');
	appendattribute("eleMod", "MOQ must be numeric types", "Integer", 'false');
	appendattribute("boxWeigth", "Weight must be numeric types", "Double", 'false');
	appendattribute("eleUnitNum", "Weight must be numeric types", "Integer", 'false');
	// appendattribute("boxHandleH","把高只能为数字","Double",'false');
	appendattribute("boxL", "Product Size  must be numeric types", "Double", 'false');
	appendattribute("boxW", "Product Size  must be numeric types", "Double", 'false');
	appendattribute("boxH", "Product Size  must be numeric types", "Double", 'false');
	appendattribute("boxLInch", "Product Size  must be numeric types", "Double", 'false');
	appendattribute("boxWInch", "Product Size  must be numeric types", "Double", 'false');
	appendattribute("boxHInch", "Product Size  must be numeric types", "Double", 'false');
	appendattribute("boxIbL", "Leight of Inner box must be numeric types", "Double", 'false');
	appendattribute("boxIbW", "Width of Inner box must be numeric types", "Double", 'false');
	appendattribute("boxIbH", "Height of Inner box must be numeric types", "Double", 'false');
	appendattribute("boxIbLInch", "Leight of Inner box must be numeric types", "Double", 'false');
	appendattribute("boxIbWInch", "Widtht of Inner box must be numeric types", "Double", 'false');
	appendattribute("boxIbHInch", "Height of Inner box must be numeric types", "Double", 'false');
	appendattribute("boxMbL", "Leight of middle box must be numeric types", "Double", 'false');
	appendattribute("boxMbW", "Width of middle box must be numeric types", "Double", 'false');
	appendattribute("boxMbH", "Height of middle box must be numeric types", "Double", 'false');
	appendattribute("boxMbLInch", "Leight of middle box must be numeric types", "Double", 'false');
	appendattribute("boxMbWInch", "Width of middle box must be numeric types", "Double", 'false');
	appendattribute("boxMbHInch", "Height of middle box must be numeric types", "Double", 'false');
	appendattribute("boxObL", "Leight of middle box must be numeric types", "Double", 'false');
	appendattribute("boxObW", "Width of middle box must be numeric types", "Double", 'false');
	appendattribute("boxObH", "Height of middle box must be numeric types", "Double", 'false');
	appendattribute("boxObLInch", "Leight of middle box must be numeric types", "Double", 'false');
	appendattribute("boxObWInch", "Width of middle box must be numeric types", "Double", 'false');
	appendattribute("boxObHInch", "Height of middle box must be numeric types", "Double", 'false');
}

// 加载主样品的图片数据
function loadPicture() {
	// 设置权限为空
	mapPopedom = null;
	var isSelPic = getPopedomByOpType("cotpicture.do", "SEL");
	if (isSelPic == 0)// 没有查看图片信息权限
	{
		Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
		return;
	} else {
		var id = $("eId").value;
		var frame = document.picturemation;
		frame.location.href = "cotpicture.do?method=query&id=" + id;
	}
}

var elementId = '';
var boxIbId = '';
var boxMbId = '';
var boxObId = '';
var boxPbId = '';
// 加载样品图片浏览
function showPicture() {
	// 设置权限为空
	mapPopedom = null;
	var isPopedom = getPopedomByOpType("cotpicture.do", "SEL");
	if (isPopedom == 0) // 没有查看图片权限
	{
		Ext.Msg.alert("Message","Sorry, you do not have Authority!");
		return;
	} else {
		var id = $('eId').value;
		var picFrame = document.showPic;
		picFrame.location.href = "cotelements.do?method=showPicture&eId="
				+ id;
	}
}

// 加载主样品的文件信息
function loadFileInfo() {
	// 设置权限为空
	mapPopedom = null;
	var isSelFile = getPopedomByOpType("cotfile.do", "SEL");
	if (isSelFile == 0)// 没有查看文件信息权限
	{
		Ext.Msg.alert("Message","Sorry, you do not have Authority!");
		return;
	} else {
		var eleId = $("eId").value;
		var frame = document.showfile;
		frame.location.href = "cotfile.do?method=query" + "&eleId=" + eleId;
	}
}

// 加载主样品的报价记录
function loadPriceInfo() {
	// 获取注册软件版本
	var softVer;
	cotElementsService.getSoftVer(function(res) {
		softVer = res;
	})
	if (softVer == "sample") {
		Ext.Msg.alert("Message","Sorry, you do not have Authority!");
		return;
	} else {
		// 设置权限为空
		mapPopedom = null;
		var isSel = getPopedomByOpType("cotprice.do", "SEL");
		if (isSel == 0)// 没有查看报价记录权限
		{
			Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
			return;
		} else {
			var eleId = $("eleId").value;
			var frame = document.priceInfo;
			frame.location.href = "cotelements.do?method=loadPriceInfo"
					+ "&eleId=" + eleId;
		}
	}
}

// 加载主样品的订单记录
function loadOrderInfo() {
	// 获取注册软件版本
	var softVer;
	cotElementsService.getSoftVer(function(res) {
		softVer = res;
	})
	if (softVer == "sample") {
		Ext.Msg.alert("Message","Sorry, you do not have Authority!");
		return;
	} else {
		// 设置权限为空
		mapPopedom = null;
		var isSel = getPopedomByOpType("cotorder.do", "SEL");
		if (isSel == 0)// 没有查看报价记录权限
		{
			Ext.Msg.alert("Message","Sorry, you do not have Authority!");
			return;
		} else {
			var eleId = $("eleId").value;
			var frame = document.orderInfo;
			frame.location.href = "cotelements.do?method=loadOrderInfo"
					+ "&eleId=" + eleId;
		}
	}
}

// 加载主样品的征样记录
function loadSignInfo() {
	// 获取注册软件版本
	var softVer;
	cotElementsService.getSoftVer(function(res) {
		softVer = res;
	})
	if (softVer == "sample") {
		Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
		return;
	} else {
		// 设置权限为空
		mapPopedom = null;
		var isSel = getPopedomByOpType("cotsign.do", "SEL");
		if (isSel == 0)// 没有查看征样记录权限
		{
			Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
			return;
		} else {
			var eleId = $("eleId").value;
			var frame = document.signInfo;
			frame.location.href = "cotelements.do?method=loadSignInfo"
					+ "&eleId=" + eleId;
		}
	}
}

// 加载主样品的送样记录
function loadGivenInfo() {
	// 获取注册软件版本
	var softVer;
	cotElementsService.getSoftVer(function(res) {
		softVer = res;
	})
	if (softVer == "sample") {
		Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
		return;
	} else {
		// 设置权限为空
		mapPopedom = null;
		var isSel = getPopedomByOpType("cotgiven.do", "SEL");
		if (isSel == 0)// 没有查看送样记录权限
		{
			Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
			return;
		} else {
			var eleId = $("eleId").value;
			var frame = document.givenInfo;
			frame.location.href = "cotelements.do?method=loadGivenInfo"
					+ "&eleId=" + eleId;
		}
	}
}

// 加载主样品的客号
function loadcustNoInfo() {
	var eleId = $("eleId").value;
	var frame = document.custNoInfo;
	frame.location.href = "cotelements.do?method=loadCustNoInfo" + "&eleId="
			+ eleId;
}

// 加载主样品的子货号
function loadChildInfo() {
	var eleId = $("eleId").value;
	var frame = document.childInfo;
	frame.location.href = "cotelements.do?method=loadChildInfo" + "&mainId="
			+ $('eId').value;
}

// 加载主样品的厂家报价历史
function loadPriceFacInfo() {
	var eleId = $("eleId").value;
	var frame = document.priceFacInfo;
	frame.location.href = "cotpricefac.do?method=query" + "&mainId="+ $('eId').value;
}

// 加载主样品的配件信息
function loadFittingInfo() {
	var eleId = $("eleId").value;
	var frame = document.eleFittingInfo;
	frame.location.href = "cotelements.do?method=loadFittingInfo" + "&Flag=parent&mainId="+ $('eId').value;
}

// 加载主样品的成本信息
function loadElePrice() {
	var frame = document.elePriceFrame;
	frame.location.href = "coteleprice.do?method=query" + "&mainId="+ $('eId').value;
}

// 加载主样品的包材信息
function loadPackingInfo() {
	var eleId = $("eleId").value;
	var frame = document.elePackingFrame;
	frame.location.href = "cotelements.do?method=loadPackingInfo&mainId="+ $('eId').value;
}

// 图片上传
function upload() {
	clearFrom();
	var name = uploadCheck($("file").value);
	if (name.charAt(0) != '_') {
		alert(name);
		return;
	}
	join(false);
	var src = './showPicture.action?flag=ele&elementId='+ $('eId').value;
 	startShow('picPath',src,1,false);
	$("uploadTable").style.display = 'none';
	$("uploadTable_forHide").style.display = 'none';
}

// 点击图片上传层外的地方后隐藏该层
function hideDiv() {
	var down = event.srcElement.id;
	if (down != "uploadTable" && down != "uploadChildPicBtn"
			&& down != "showUploadBtn" && down != "controlPanel"
			&& down != "file" && down != "uploadButton"
			&& down != "uploadParentPicBtn" && down != "bigParentImg"){
				$("uploadTable").style.display = 'none';
				$("uploadTable_forHide").style.display = 'none';
			}
	$("picDiv").style.display = 'none';
}

// 显示上传图片层
function showPicDiv(e) {
	var obj = $("uploadTable").style.display;
	if (obj == 'none' || obj == '') {
		// 设置层显示位置
		// 设置层显示位置
		var t = e.offsetTop;
		var l = e.offsetLeft;
		while (e = e.offsetParent) {
			t += e.offsetTop;
			l += e.offsetLeft;
		}
		$("uploadTable").style.top = t + 20;
		$("uploadTable").style.left = l - 200;
		$("uploadTable").style.display = "block";
	} else {
		$("uploadTable").style.display = "none";
	}
	addDivForSel("uploadTable");
}

// 删除主样品图片
function delPic(eId) {
	var flag = window.confirm("are you sure to delete them?");
	if (flag) {
		cotElementsService.deletePicImg(parseInt(eId), function(res) {
			if (res) {
				$('picPath').src = "common/images/zwtp.png";
			} else {
				Ext.Msg.alert("Message","deleted failed!");
			}
		})
	} else {
		return;
	}
}

// 显示大图片层
function showBigPicDiv(e) {
	var width = window.screen.availWidth / 5;
	var height = window.screen.availHeight / 5;
	document.getElementById("picDiv").style.display = "block";
	$('bigParentImg').src = $('picPath').src;
	document.getElementById("picDiv").style.top = height;
	document.getElementById("picDiv").style.left = width;
}

// 组合中英文包装类型名称
function composeBoxName(res, boxType) {
	// 清空所有Option
	DWRUtil.removeAllOptions(boxType);
	var ary = new Array();
	var templist = {};
	templist.value = '';
	templist.key = 'Choose';
	ary.push(templist);
	if (res != null) {
		for (var i = 0; i < res.length; i++) {
			if (res[i].valueEn == null) {
				res[i].valueEn = '';
			}
			var name = res[i].value + '(' + res[i].valueEn + ')';
			var temp = {};
			temp.value = res[i].id;
			temp.key = name;
			ary.push(temp);
		}
	}
	DWRUtil.addOptions(boxType, ary, 'value', 'key');
}

// 加载下拉框数据
function bindSel() {
	// 加载包装类型
	cotElementsService.getBoxTypeList(function(res) {
		bindSelect('boxTypeId', res, 'id', 'typeName');
	});
	// 加载内包装名称
	cotBoxTypeService.getBoxINameList(function(res) {
		composeBoxName(res, 'boxIbTypeId');
	});
	//加载中包装名称
	cotBoxTypeService.getBoxMNameList(function(res) {
		composeBoxName(res, 'boxMbTypeId');
	});
	// 加载外包装名称
	cotBoxTypeService.getBoxONameList(function(res) {
		composeBoxName(res, 'boxObTypeId');
	});
	//加载产品包装名称
	cotBoxTypeService.getBoxPNameList(function(res) {
		composeBoxName(res, 'boxPbTypeId');
	});	
	// 加载报价币种
	sysdicutil.getDicListByName('currency', function(res) {
		bindSelect('priceFacUint', res, 'id', 'curNameEn');
		bindSelect('priceOutUint', res, 'id', 'curNameEn');
	});
}
// 页面加载调用
function initForm() {
	// 因为这个初始化方法比popedom.js中的初始化事件早，在取权限时取不到登录人，所以另外再调用一次
	getLoginEmpid();
	mapPopedom = null;
	var isUpdate = getPopedomByOpType("cotelements.do", "MOD");
	if (isUpdate == 0)// 没有查看权限
	{
		$('updateBtn').style.display = 'none';
	}
	// 判断用户是否有权限操作报价信息
	var isSelFac = getPopedomByOpType("cotpricefac.do", "SEL");
	if (isSelFac == 0)// 没有查看权限
	{
		$('priceFacDiv').style.display = 'none';
	}
	if (isSelFac == 1) {
		var isModFac = getPopedomByOpType("cotpricefac.do", "MOD");
		if (isModFac == 0) {// 没有修改权限
			$('priceFac').disabled = true;
			$('priceFacUint').disabled = true;
		} else {
			$('priceFac').disabled = false;
			$('priceFacUint').disabled = false;
		}
	}
	// 设置权限为空，popedom.js中的初始化事件还会重新根据element.do得到用户对样品的权限控制
	mapPopedom = null;
	var isSelOut = getPopedomByOpType("cotpriceout.do", "SEL");
	if (isSelOut == 0)// 没有查看权限
	{
		$('priceOutDiv').style.display = 'none';
	}
	if (isSelOut == 1) {
		var isModOut = getPopedomByOpType("cotpriceout.do", "MOD");
		if (isModOut == 0) {// 没有修改权限
			$('priceOut').disabled = true;
			$('priceOutUint').disabled = true;
		} else {
			$('priceOut').disabled = false;
			$('priceOutUint').disabled = false;
		}
	}
	mapPopedom = null;
	var isSelBox = getPopedomByOpType("cotelements.do", "BOX");
	if (isSelBox == 0) // 没有查看包装信息权限
	{
		$('eleBoxDiv').style.display = 'none';
	} else {
		$('eleBoxDiv').style.display = 'block';
	}

	// 判断是否有修改图片的权限
	mapPopedom = null;
	var isModPic = getPopedomByOpType("cotpicture.do", "MOD");
	if (isModPic == 0) {// 没有修改图片信息权限
		$('elePicModBtnDiv').style.display = 'none';
	} else {
		$('elePicModBtnDiv').style.display = 'block';
	}

	// 判断是否有删除图片的权限
	mapPopedom = null;
	var isModPic = getPopedomByOpType("cotpicture.do", "DEL");
	if (isModPic == 0) {// 没有修改图片信息权限
		$('elePicDelBtnDiv').style.display = 'none';
	} else {
		$('elePicDelBtnDiv').style.display = 'block';
	}

	// 设置权限为空，popedom.js中的初始化事件还会重新根据element.do得到用户对样品的权限控制
	mapPopedom = null;
	// 表单验证
	validForm();
	// 加载主样品表单
	var id = $("eId").value;
	DWREngine.setAsync(false);
	cotElementsService.getEleById(parseInt(id), function(res) {
		if (res != null) {
			// 加载下拉框数据
			bindSel();
			oldElement = res;
			DWRUtil.setValues(res);
			// 创建时间
			var addDate = new Date(res.eleAddTime);
			$('addTime').value = addDate.getYear() + '-'
					+ (addDate.getMonth() + 1) + '-' + addDate.getDate();

			var isSelPic = getPopedomByOpType("cotpicture.do", "SEL");
			if (isSelPic == 0) {// 没有查看图片信息权限
				$('picPath').src = "common/images/noElePicSel.png";
			} else {
				var rdm = Math.round(Math.random() * 10000);
				$('picPath').src = './showPicture.action?flag=ele&elementId='
						+ id + '&tmp=' + rdm;

			}
			mapPopedom = null;
			var isModPic = getPopedomByOpType("cotpicture.do", "MOD");
			if (isModPic == 0) {// 没有修改图片信息权限
				$('uploadParentPicBtn').disabled = true;
			}
			mapPopedom = null;
			var isDelPic = getPopedomByOpType("cotpicture.do", "DEL");
			if (isDelPic == 0) {// 没有删除图片信息权限
				$('deleteParentPicBtn').disabled = true;
			}
			mapPopedom = null;
			changeUnitNum();
			if (res.eleProTime != null) {
				var date = new Date(res.eleProTime);
				$('eleProTime').value = date.getYear() + '-'
						+ (date.getMonth() + 1) + '-' + date.getDate();
			}
			// 如果是子货号,隐藏子货号标签
			if (res.eleFlag == 2) {
				$('childEleDiv').style.display = 'none';
			}
			if (res.eleTypeidLv1 != null) {
				showText('CotTypeLv1', 'typeEnName', res.eleTypeidLv1,
						'typeName');
			}
			if (res.factoryId != null) {
				showText('CotFactory', 'shortName', res.factoryId, 'shortName');
			}
			if (res.eleHsid != null) {
				showText('CotEleOther', 'cnName', res.eleHsid, 'hscode');
			}
			if (res.eleTypeidLv2 != null) {
				showText('CotTypeLv2', 'typeName', res.eleTypeidLv2, 'typeName2');
			}
		} else {
			closeandreflashEC('true', 'elementsTable', false);
		}
	});
	// 初始化样品ID
	elementId = $("eId").value;
	
	cotElementsService.getDefaultList(function(res) {
		if (res.length != 0) {
			//设置是否自动生成包装尺寸标识
			if(res[0].sizeFollow!=null){
				$('sizeFollow').value = res[0].sizeFollow;
			}
		}
	});
	
	DWREngine.setAsync(true);
}

// 套件选择为是/否
function changeUnitNum() {
	if ($('eleFlag').value == 0) {
		$('eleUnitNum').value = 1;
		$('eleUnitNum').disabled = true;
	} else {
		$('eleUnitNum').disabled = false;
	}
}

// 验证产品货号是否存在
function checkEleId() {
	if ($('eleId').value == '') {
		return;
	}
	$('eleId').value = $('eleId').value.toUpperCase();
	cotElementsService.findIsExistEleId($('eleId').value, $('eId').value,
			function(res) {
				if (res) {
					$('displayMsg').innerText = 'Articel already exists, please re-enter!';
				} else {
					$('displayMsg').innerText = '';
				}
			});
}

// 设置外销价
function setPriceOut() {
	// var flag = document.getElementById('calCheck').checked;
	// if(flag == true ){
	var priceFac = $('priceFac').value;
	var tuiLv = $('tuiLv').value;
	var priceFacUnit = $('priceFacUint').value;
	var priceOutUnit = $('priceOutUint').value;
	var liRun = $('liRun').value;
	var cbm = $('boxCbm').value;
	if (cbm == '' || cbm == null) {
		Ext.Msg.alert("Message",'Please enter CBM，it must not be zero！');
		return;
	}
	//azan添加 
	var boxObCount = $('boxObCount').value;
	if(boxObCount=='' || isNaN(boxObCount)){
		boxObCount=0;
	}
	DWREngine.setAsync(false);
	cotElementsService.getPriceOut(parseFloat(priceFac), parseFloat(tuiLv),
			parseInt(priceFacUnit), parseInt(priceOutUnit), parseFloat(liRun),
			parseFloat(cbm), boxObCount,function(res) {
				if (res != null) {
					$('priceOut').value = res.toFixed("2");
				}
			});
	DWREngine.setAsync(true);
	// }
}

// 按模板货号添加
function addByModel(e) {
	var obj = $('importDiv').style.display;
	if (obj == '' || obj == 'none') {
		// 设置层显示位置
		var t = e.offsetTop;
		var l = e.offsetLeft;
		while (e = e.offsetParent) {
			t += e.offsetTop;
			l += e.offsetLeft;
		}
		$("importDiv").style.top = t - 60;
		$("importDiv").style.left = l;
		$("importDiv").style.display = "block";
		$('modelEle').focus();
	} else {
		$('importDiv').style.display = 'none';
	}
}

// 回车事件
function addWithMolByEnter() {
	if (event.keyCode == 13) {
		addWithMol();
	}
}

// 关闭导入选择层
function closeModDiv() {
	document.getElementById("importDiv").style.display = "none";
}

// 按新货号另存
function addWithMol() {
	var model = $('modelEle').value.toUpperCase();
	if (model == '') {
		Ext.Msg.alert("Message",'please enter article No.!');
		return;
	}
	// 判断该货号是否存在
	cotElementsService.findIsExistEleId(model, 0, function(res) {
		if (res == true) {
			Ext.Msg.alert("Message",'Articel No.already exists, please re-enter!');
			$('modelEle').select();
			return;
		} else {
			// 验证表单
			var formEle = document.forms['cotElementsForm'];
			var b = false;
			b = Validator.Validate(formEle, 1);
			if (!b) {
				return;
			}
			if ($('priceFac').value == '' && $('priceFacUint').value != '') {
				Ext.Msg.alert("Message",'please enter Purchase Price!');
				return;
			}
			if ($('priceFac').value != '' && $('priceFacUint').value == '') {
				Ext.Msg.alert("Message",'Please choose currency!');
				return;
			}
			if ($('priceOut').value == '' && $('priceOutUint').value != '') {
				Ext.Msg.alert("Message",'please enter sale Price!');
				return;
			}
			if ($('priceOut').value != '' && $('priceOutUint').value == '') {
				Ext.Msg.alert("Message",'Please choose currency!');
				return;
			}

			// 主样品表单
			var ele = DWRUtil.getValues("cotElementsForm");
			var elements = new CotElementsNew();
			for (var p in ele) {
				if (p != 'eleProTime') {
					elements[p] = ele[p];
				}
			}
			elements.id = $('eId').value;
			cotElementsService.saveByCopy(elements, model,
					$('eleProTime').value, function(res) {
						if (res == null) {
							Ext.Msg.alert("Message",'The article does not exist, please re-enter!');
						} else {
							Ext.Msg.alert("Message",'saved successfullly');
							$('importDiv').style.display = 'none';
						}
					});
		}
	});
}
// 关闭图片层
function closePicDiv() {
	document.getElementById('picDiv').style.display = 'none';
}
// 鼠标滚轮改变图片大小
function onWheelZoom(obj) {
	zoom = parseFloat(obj.style.zoom);
	tZoom = zoom + (event.wheelDelta > 0 ? 0.05 : -0.05);
	if (tZoom < 0.15)
		return true;
	obj.style.zoom = tZoom;
	return false;
}
// 实际大小
function originalSize() {
	document.getElementById('bigParentImg').style.zoom = 1;
	document.getElementById('origiDiv').style.display = 'none';
	document.getElementById('initDiv').style.display = 'block';
}
// 初始大小
function initSize() {
	document.getElementById('bigParentImg').style.zoom = 0.5;
	document.getElementById('initDiv').style.display = 'none';
	document.getElementById('origiDiv').style.display = 'block';
}
// 获取图片路径
function savaPicture() {
	var eleId = $('eId').value
	var picPath = "./downPicture.action?flag=ele&elementId=" + eleId;
	var form = document.getElementById("picForm");
	form.action = picPath;
	form.target = "";
	form.method = "post";
	form.submit();
}

// 显示查找厂家的层
function showFactoryDiv(e) {
	var obj = $("factoryDiv").style.display;
	if (obj == 'none') {
		// 加载加工厂家查询表格
		var frame = document.facTabFrame;
		frame.location.href = "cotquery.do?method=queryFactory";
		document.getElementById("factoryDiv").style.top = $('shortName').offsetHeight
				+ 58;
		document.getElementById("factoryDiv").style.left = $('shortName').offsetLeft;
		document.getElementById("factoryDiv").style.display = "block";
	} else {
		$("factoryDiv").style.display = 'none';
	}
}

// 根据选择的厂家编码自动填写表单
function setFactoryForm(id, shortName) {
	// 隐藏查询加工厂家层
	$("factoryDiv").style.display = 'none';
	$('factoryId').value = id;
	$('shortName').value = shortName;
}

// 根据海关编码设置退税率
function setTaxRate() {
	var eleHsid = $('eleHsid').value;
	DWREngine.setAsync(false);
	if (eleHsid != "") {
		cotElementsService.getTaxRate(eleHsid, function(res) {
			if (res != null) {
				$('tuiLv').value = res;
			} else {
				cotElementsService.getDefaultList(function(elecfg) {
					if (elecfg.length != 0) {
						$('tuiLv').value = elecfg[0].tuiLv;
					}
				});
			}
		});
		DWREngine.setAsync(true);
		// setPriceOut()
	}
}
//根据选择的工厂编号自动填写表单
function setFactoryForm(id){

	//根据
	cotElementsService.getFactoryById(parseInt(id),function(res){
		DWRUtil.setValues(res);
		$('factoryId').value = res.id;

		// 设置该厂号
		//if (res.factoryNo != null) {
		//	$('factoryNo').value = res.factoryNo;
		//}
	})
}

// 点击选择模糊查询结果后方法
function handleComon(id, val, fno, txtId) {
	if (txtId == 'shortName') {
		$('factoryId').value = id;
		$('shortName').value = val;
		//setFactoryForm(id);
	}
	if (txtId == 'typeName') {
		$('eleTypeidLv1').value = id;
		$('typeName').value = val;
	}
	if (txtId == 'hscode') {
		$('eleHsid').value = id;
		$('hscode').value = val;
		setTaxRate();
	}
	if (txtId == 'typeName2') {
		$('eleTypeidLv2').value = id;
		$('typeName2').value = val;
	}
	$(txtId).style.background = '#E2F4FF';
	$("search").style.display = "none";// 隐藏列表
}
// 获取子货号中英文规格
function getEleSizeDesc() {
	var eleFlag = $('eleFlag').value;
	if (eleFlag == 1 || eleFlag == 3) {
		DWREngine.setAsync(false);
		/*
		 * cotElementsService.getEleById(parseInt($('eId').value),function(ele){
		 * if(ele.eleSizeDesc != null && ele.eleSizeDesc != ""){
		 * $('eleSizeDesc').value = ele.eleSizeDesc +"\n"; } if(ele.eleInchDesc !=
		 * null && ele.eleInchDesc != ""){ $('eleInchDesc').value =
		 * ele.eleInchDesc +"\n"; } });
		 */
		$('eleSizeDesc').value = '';
		$('eleInchDesc').value = '';
		cotElementsService.getEleSizeAndInchDesc(parseInt($('eId').value),
				function(res) {
					if (res.length != 0) {
						for (i = 0; i < res.length; i++) {
							if (res[i].eleSizeDesc != null
									&& res[i].eleSizeDesc != "") {
								$('eleSizeDesc').value += res[i].eleSizeDesc
										+ "\n";
							}
							if (res[i].eleInchDesc != null
									&& res[i].eleInchDesc != "") {
								$('eleInchDesc').value += res[i].eleInchDesc
										+ "\n";
							}
						}
					}
				});
		DWREngine.setAsync(true);
	}
}
//检查利润率的输入位数
function checkLiRun(obj){
	var liRun = 0;
	DWREngine.setAsync(false);
	cotElementsService.getDefaultList(function(res) {
		if (res.length != 0) {
			if (res[0].priceProfit != null) {
				liRun = res[0].priceProfit;					
			}
		}
	});
	if(obj.value == '' || obj.value ==null){
		$('liRun').value = liRun;
	}else if(parseFloat(obj.value)>=1000){
		Ext.Msg.alert("Message",'Sorry，Profit margins can  enter only the values below 1000');
		$('liRun').value = liRun;
	}
	DWREngine.setAsync(true);
}

//计算包材价格
function calPrice(){
	var ele = DWRUtil.getValues('cotElementsForm');
	var elements = new CotElementsNew();
	for (var p in elements) {
		if (p != 'eleProTime') {
			elements[p] = ele[p];
		}
	}
	if($('eId').value!=''){
		elements.id = $('eId').value;
	}
	cotElementsService.calPriceAll(elements,function(res){
		$('boxPbPrice').value = res[0];
		$('boxIbPrice').value = res[1];
		$('boxMbPrice').value = res[2];
		$('boxObPrice').value = res[3];
		$('packingPrice').value = res[4];
		if(res[5]!=-1){
			$('priceFac').value = res[5];
		}
		$('inputGridPrice').value = res[6];
	});
}
//手动修改各个包材价格时修改单个价格及生产价
function calPackPriceAndPriceFac(){
	
	var packingPrice = $('packingPrice').value;
	var pbPrice = $('boxPbPrice').value;
	var ibPrice = $('boxIbPrice').value;
	var mbPrice = $('boxMbPrice').value;
	var obPrice = $('boxObPrice').value;
	
	var boxObCount = $('boxObCount').value;
	var boxMbCount = $('boxMbCount').value;
	var boxIbCount = $('boxIbCount').value;
	var boxPbCount = $('boxPbCount').value;
	
	var boxIbPrice =0;
	var boxMbPrice =0;
	var boxObPrice =0;
	var boxPbPrice =0;
	
	if(boxIbCount>0 && boxObCount>0 && ibPrice != '' && ibPrice != null){
		boxIbPrice = (parseFloat(ibPrice)*parseInt(boxObCount)/parseInt(boxIbCount)).toFixed(3);
	}
	if(boxMbCount>0 && boxObCount>0 && mbPrice != '' && mbPrice != null){
		boxMbPrice = (parseFloat(mbPrice)*parseInt(boxObCount)/parseInt(boxMbCount)).toFixed(3);
	}
	if(boxObCount>0 && obPrice != '' && obPrice != null){
		boxObPrice = parseFloat(obPrice).toFixed(3);
	}
	if(boxPbCount>0 && boxObCount>0 && pbPrice != '' && pbPrice != null){
		boxPbPrice = (parseFloat(pbPrice)*parseInt(boxObCount)/parseInt(boxPbCount)).toFixed(3);
	}
	
	if(boxObCount != 0 && boxObCount != ''){
		packingPrice = ((parseFloat(boxIbPrice)+ parseFloat(boxMbPrice) 
						+ parseFloat(boxPbPrice) + parseFloat(boxObPrice))/parseInt(boxObCount)).toFixed(3);
	}
	$('packingPrice').value = packingPrice;
	cotElementsService.calPriceFacByPackPrice($('eId').value,packingPrice,function(facprice){
		$('priceFac').value = facprice;
	});
}

function calPriceByPakingPrice(){
	var packingPrice = 0.0;
	if($('packingPrice').value != null && $('packingPrice').value !=''){
		packingPrice = $('packingPrice').value;
	}
	if($('eId').value != null && $('eId').value != ''){
		cotPriceService.calPriceFacByPackPrice($('eId').value,packingPrice,function(facprice){
			$('priceFac').value = facprice;
		});
	}
}	
