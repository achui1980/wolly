var facNum = getDeNum("facPrecision");
var outNum = getDeNum("outPrecision");
var cbmNum = getDeNum("cbmPrecision");

// 当配件tab里面点保存后把配件总成本传回该页面
var fitMoney = null;
// 当样品成本tab里面点保存后把样品总成本传回该页面
var priceMoney = null;
DWREngine.setAsync(false);
// 生成价汇率
var facCurRateMap;
// 外销价汇率
var outCurRateMap;
baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curRate", function(res) {
			facCurRateMap = res;
		});
baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curRate", function(res) {
			outCurRateMap = res;
		});

DWREngine.setAsync(true);
Ext.onReady(function() {
	// 业务员
	var empBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'bussinessPerson',
		fieldLabel : "Designer",
		editable : true,
		hidden : true,
		hideLabel : true,
		valueField : "empsName",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 16,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	// 大类
	var typeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv4&key=typeEnName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "Dep. Of PRODUCT",
				editable : true,
				valueField : "id",
				displayField : "typeEnName",
				emptyText : 'Choose',
				mode : 'remote',
				pageSize : 10,
				anchor : '100%',
				tabIndex : 10,
				sendMethod : "post",
				selectOnFocus : true,
				// allowBlank : false,
				// disabled : true,
				// disabledClass : 'combo-disabled',
				// blankText : "请先选择样品大类！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 中类
	var type2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Group",
				editable : true,
				tabIndex : 11,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				anchor : '100%',
				mode : 'remote',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				// allowBlank : false,
				// disabled : true,
				// disabledClass : 'combo-disabled',
				// blankText : "请先选择样品中类！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 小类
	var type3Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : 'eleTypeidLv3',
				fieldLabel : "Category",
				editable : true,
				tabIndex : 12,
				anchor : '100%',
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				mode : 'remote',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				// disabled : true,
				// disabledClass : 'combo-disabled',
				// allowBlank : false,
				// blankText : "请先选择样品小类！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// // 材质
	// var typeLvBox = new BindCombox({
	// dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
	// cmpId : 'eleTypeidLv1',
	// fieldLabel : "材质",
	// editable : true,
	// hidden : true,
	// hideLabel : true,
	// valueField : "id",
	// displayField : "typeName",
	// emptyText : '请选择',
	// pageSize : 10,
	// anchor : "100%",
	// tabIndex : 2,
	// sendMethod : "post",
	// selectOnFocus : true,
	// minChars : 1,
	// listWidth : 350,
	// triggerAction : 'all'
	// });

	// 套件选择为是/否
	var changeUnitNum = function() {
		if ($('eleFlag').value == 0) {
			$('eleUnitNum').value = 1;
			$('eleUnitNum').disabled = true;
		} else {
			$('eleUnitNum').disabled = false;
		}
	}

	// 组成方式
	var eleFlagStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, '单件'], [1, '套件'], [3, '组件']]
			});
	var eleFlagComb = new Ext.form.ComboBox({
				name : 'eleFlag',
				tabIndex : 4,
				fieldLabel : '组成方式',
				emptyText : '请选择',
				editable : true,
				hidden : true,
				hideLabel : true,
				store : eleFlagStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'eleFlag',
				selectOnFocus : true,
				listeners : {
					"select" : changeUnitNum
				}
			});

	// 根据选择的工厂编号自动填写表单
	var setFactoryNo = function(combo, record, index) {
		cotElementsService.getFactoryById(record.id, function(res) {
					// 设置该厂号
					if (res.factoryNo != null) {
						$('factoryNo').value = res.factoryNo;
					}
				})
	}

	// 供应厂家
	var facBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
		cmpId : 'factoryId',
		fieldLabel : "Supplier",
		editable : true,
		// disabled : true,
		// disabledClass : 'combo-disabled',
		hidden : hideFac,
		hideLabel : hideFac,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		mode : 'remote',// 默认local
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		tabIndex : 2,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
			// listeners : {
			// "select" : setFactoryNo
			// }
		});

	// 产品来源
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['WOLLY', 'WOLLY'], ['SUPPLIER', 'SUPPLIER'],
						['CUSTOMER', 'CUSTOMER']]
			});
	var sourceBox = new Ext.form.ComboBox({
				name : 'eleFrom',
				tabIndex : 13,
				fieldLabel : 'Source',
				editable : false,
				value : 'WOLLY',
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'eleFrom',
				selectOnFocus : true
			});

	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Category",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				pageSize : 10,
				anchor : "100%",
				tabIndex : 16,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 海关编码
	var eleHsidBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEleOther&key=cnName",
		cmpId : 'eleHsid',
		// fieldLabel : "海关编码",
		fieldLabel : "<a onclick=showSH() style='color:blue;cursor: hand;text-decoration: underline'>Customs Code</a>",
		editable : true,
		hidden : true,
		hideLabel : true,
		valueField : "id",
		allowBlank : true,
		displayField : "cnName",
		emptyText : 'Choose',
		mode : 'remote',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 18,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		listeners : {
			"select" : function(com, rec, index) {
				if (rec.data.id != '') {
					cotElementsService.getEleTax(rec.data.id, function(res) {
								if (res != null) {
									Ext.getCmp('tuiLv').setValue(res);
									setPriceOut();
								}
							});
				}
			}
		}
	});
	// 生产币种
	var facCurBox = new BindCombox({
				cmpId : 'priceFacUint',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				fieldLabel : "",
				anchor : "100%",
				hideLabel : true,
				allowBlank : true,
				tabIndex : 22,
				listeners : {
					"select" : function(com, rec, index) {
						setLiRun();
					}
				}
			});
	// 外销币种
	var outCurBox = new BindCombox({
				cmpId : 'priceOutUint',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				fieldLabel : "",
				anchor : "100%",
				hideLabel : true,
				allowBlank : true,
				tabIndex : 24,
				listeners : {
					"select" : function(com, rec, index) {
						setLiRun();
					}
				}
			});

	// 计算包材价格
	var calPrice = function(pp) {
		var ele = DWRUtil.getValues('cotElementsForm');
		var elements = new CotElementsNew();
		for (var p in elements) {
			if (p != 'eleAddTime') {
				elements[p] = ele[p];
			}
		}
		if (typeof(pp) != 'undefined') {
			elements.boxPbTypeId = pp.boxPName;
			elements.boxIbTypeId = pp.boxIName;
			elements.boxMbTypeId = pp.boxMName;
			elements.boxObTypeId = pp.boxOName;
			elements.inputGridTypeId = pp.inputGridType;
		}
		// 生成价修改时间
		if(Ext.isEmpty(Ext.getCmp("updateTime").getValue())){
			elements.updateTime =null;
		}else{
			elements.updateTime = Ext.getCmp("updateTime").getValue();
		}

		cotElementsService.calPriceAll(elements, function(res) {
					$('boxPbPrice').value = res[0];
					$('boxIbPrice').value = res[1];
					$('boxMbPrice').value = res[2];
					$('boxObPrice').value = res[3];
					$('packingPrice').value = res[4].toFixed(facNum);
					if (res[5] != -1) {
						$('priceFac').value = res[5].toFixed(facNum);
					}
					$('inputGridPrice').value = res[6];
				});
	}

	// 加载包装类型值
	var selectBind = function() {
		var id = $('boxTypeId').value;
		// 点击请选择
		if (id == "" || id == null)
			return;
		DWREngine.setAsync(false);
		cotElementsService.getBoxTypeById(id, function(res) {
					if (res != null) {
						if (res.boxIName == null) {
							boxIbTypeIdBox.setValue("");
						} else {
							boxIbTypeIdBox.bindValue(res.boxIName);
						}
						if (res.boxMName == null) {
							boxMbTypeIdBox.setValue("");
						} else {
							boxMbTypeIdBox.bindValue(res.boxMName);
						}
						if (res.boxOName == null) {
							boxObTypeIdBox.setValue("");
						} else {
							boxObTypeIdBox.bindValue(res.boxOName);
						}
						if (res.boxPName == null) {
							boxPbTypeIdBox.setValue("");
						} else {
							boxPbTypeIdBox.bindValue(res.boxPName);
						}
						if (res.inputGridType == null) {
							inputGridTypeIdBox.setValue("");
						} else {
							inputGridTypeIdBox.bindValue(res.inputGridType);
						}
						calPrice(res);
					}
				});
		DWREngine.setAsync(true);

	};

	// 包装方案
	var boxTypeIdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotBoxType&key=typeName",
				cmpId : 'boxTypeId',
				fieldLabel : "Packing Way",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 39,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				listeners : {
					"select" : selectBind
				}
			});

	// 产品包装类型
	var boxPbTypeIdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotBoxPacking&type=PB",
				cmpId : 'boxPbTypeId',
				fieldLabel : "/",
				labelSeparator : " ",
				valueField : "id",
				allowBlank : true,
				displayField : "value",
				emptyText : 'Choose Material Type',
				anchor : "100%",
				tabIndex : 41,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				triggerAction : 'all',
				editable : true,
				listeners : {
					"select" : function() {
						calPrice();
					}
				}
			});

	// 内包装类型
	var boxIbTypeIdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotBoxPacking&type=IB",
				cmpId : 'boxIbTypeId',
				fieldLabel : "/",
				labelSeparator : " ",
				valueField : "id",
				allowBlank : true,
				displayField : "value",
				emptyText : 'Choose Material Type',
				anchor : "100%",
				tabIndex : 43,
				selectOnFocus : true,
				editable : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						calPrice();
					}
				}
			});

	// 中包装类型
	var boxMbTypeIdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotBoxPacking&type=MB",
				cmpId : 'boxMbTypeId',
				fieldLabel : "/",
				labelSeparator : " ",
				valueField : "id",
				allowBlank : true,
				hidden : true,
				hideLabel : true,
				displayField : "value",
				editable : true,
				emptyText : 'Choose',
				anchor : "100%",
				tabIndex : 67,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						calPrice();
					}
				}
			});

	// 外包装类型
	var boxObTypeIdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotBoxPacking&type=OB",
				cmpId : 'boxObTypeId',
				fieldLabel : "/",
				labelSeparator : " ",
				valueField : "id",
				editable : true,
				allowBlank : true,
				displayField : "value",
				emptyText : 'Choose Material Type',
				anchor : "100%",
				tabIndex : 45,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						calPrice();
					}
				}
			});

	// 插格类型
	var inputGridTypeIdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotBoxPacking&type=IG",
				cmpId : 'inputGridTypeId',
				fieldLabel : "插格类型",
				valueField : "id",
				editable : true,
				allowBlank : true,
				hidden : true,
				hideLabel : true,
				displayField : "value",
				emptyText : '请选择',
				anchor : "100%",
				tabIndex : 85,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						calPrice();
					}
				}
			});

	// 中英文规格跟着变化
	var changeSizeTxt = function() {
		// $('eleInchDesc').value = $('boxLInch').value + "*"
		// + $('boxWInch').value + "*" + $('boxHInch').value;
		var str = "";
		if ($('boxL').value != '' && $('boxL').value != '0') {
			str += $('boxL').value + "x";
		}
		if ($('boxW').value != '' && $('boxW').value != '0') {
			str += $('boxW').value + "x";
		}
		if ($('boxH').value != '' && $('boxH').value != '0') {
			str += $('boxH').value + "cm";
			$('eleInchDesc').value = str;
		} else {
			if (str == "") {
				$('eleInchDesc').value = "";
			} else {
				$('eleInchDesc').value = str.substring(0, str.length - 1)
						+ "cm";
			}
		}
	};

	// 样品默认配置,是否自动生成产品包装尺寸
	var sizeFollow = 0;

	// 计算英寸
	var calInch = function(txt, newVal, oldVal) {
		// var inch = (newVal / 2.54).toFixed("2");
		// var needId = txt.name + "Inch";
		// $(needId).value = inch;
		changeSizeTxt();
		// if (sizeFollow == 1) {
		// // 产品包装尺寸跟着变化
		// if (needId == 'boxLInch') {
		// $('boxPbL').value = $('boxL').value;
		// $('boxPbLInch').value = $('boxLInch').value;
		// }
		// if (needId == 'boxWInch') {
		// $('boxPbW').value = $('boxW').value;
		// $('boxPbWInch').value = $('boxWInch').value;
		// }
		// if (needId == 'boxHInch') {
		// $('boxPbH').value = $('boxH').value;
		// $('boxPbHInch').value = $('boxHInch').value;
		// }
		// }
		// calPrice();
	}

	// 计算CM
	var calCm = function(txt, newVal, oldVal) {
		var cm = (newVal * 2.54).toFixed("2");
		var needId = txt.name.substring(0, txt.name.length - 4);
		$(needId).value = cm;
		changeSizeTxt();
		if (sizeFollow == 1) {
			// 产品包装尺寸跟着变化
			if (needId == 'boxL') {
				$('boxPbL').value = $('boxL').value;
				$('boxPbLInch').value = $('boxLInch').value;
			}
			if (needId == 'boxW') {
				$('boxPbW').value = $('boxW').value;
				$('boxPbWInch').value = $('boxWInch').value;
			}
			if (needId == 'boxH') {
				$('boxPbH').value = $('boxH').value;
				$('boxPbHInch').value = $('boxHInch').value;
			}
		}
		calPrice();
	}

	// 通过单重计算净重/毛重
	var calWeight = function(txt, newVal, oldVal) {
		var boxObCount = $('boxObCount').value;
		if (boxObCount == "") {
			boxObCount = 0;
		}
		var boxWeigth = $('boxWeigth').value;
		if (boxWeigth == "") {
			boxWeigth = 0;
		}
		cotElementsService.getDefaultList(function(res) {
			var jin = boxWeigth * boxObCount / 1000;
			$('boxNetWeigth').value = jin.toFixed(3);
			if (res.length != 0) {
				if (res[0].grossNum != null) {
					var temp = jin + res[0].grossNum;
					$('boxGrossWeigth').value = temp.toFixed(3);
				}
			}
				// else {
				// $('boxGrossWeigth').value = jin.toFixed(3);
				// }
			});
	}

	// 通过外箱尺寸计算cbm和cuft
	var calCube = function(boxObL, boxObW, boxObH) {
		if (isNaN(boxObL)) {
			boxObL = 0;
		}
		if (isNaN(boxObW)) {
			boxObW = 0;
		}
		if (isNaN(boxObH)) {
			boxObH = 0;
		}
		// 计算cbm和cuft
		var boxCbm = (boxObL * boxObW * boxObH * 0.000001).toFixed(cbmNum);
		var boxCuft = (boxCbm * 35.315).toFixed("3");
		$('boxCbm').value = boxCbm;
		$('boxCuft').value = boxCuft;
	}

	// 通过外箱尺寸计算英寸和cbm和cuft和装箱数
	var calOSize = function(txt, newVal, oldVal) {
		var inch = (newVal / 2.54).toFixed("2");
		var needId = txt.name + "Inch";
		$(needId).value = inch;
		calCube($('boxObL').value, $('boxObW').value, $('boxObH').value);
		calBoxCount($('boxCbm').value, $('boxObCount').value);
		calPrice();
	}

	// 通过外箱尺寸计算CM和cbm和cuft和装箱数
	var calOSizeCm = function(txt, newVal, oldVal) {
		var cm = (newVal * 2.54).toFixed("2");
		var needId = txt.name.substring(0, txt.name.length - 4);
		$(needId).value = cm;
		calCube($('boxObL').value, $('boxObW').value, $('boxObH').value);
		calBoxCount($('boxCbm').value, $('boxObCount').value);
		calPrice();
	}

	// 按模板
	var addWithMol = function() {
		var model = $('modelEle').value.toUpperCase();
		var formData = getFormValues(modlPanel, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		if ($('priceFac').value == '' && $('priceFacUint').value != '') {
			Ext.Msg.alert("Message", 'Please enter purchase price!');
			return;
		}
		if ($('priceFac').value != '' && $('priceFacUint').value == '') {
			Ext.Msg.alert("Message", 'please select currency!');
			return;
		}
		if ($('priceOut').value == '' && $('priceOutUint').value != '') {
			Ext.Msg.alert("Message", 'Please enter sale price!');
			return;
		}
		if ($('priceOut').value != '' && $('priceOutUint').value == '') {
			Ext.Msg.alert("Message", 'please select currency!');
			return;
		}

		// 主样品表单
		var ele = DWRUtil.getValues("cotElementsForm");
		var elements = new CotElementsNew();
		for (var p in ele) {
			if (p != 'eleAddTime') {
				elements[p] = ele[p];
			}
		}
		elements.id = $('eId').value;
		cotElementsService.saveByCopy(elements, model, $('eleAddTime').value,
				function(res) {
					if (res == null) {
						Ext.MessageBox.alert("Message",
								'The article does not exist, please re-enter!');
					} else {
						Ext.MessageBox.alert("Message", 'saved successfull!');
						$('modelEle').value = "";
						modlPanel.hide();
					}
				});
	}
	
	function saveAsEle(eleId){
		// 主样品表单
		var ele = DWRUtil.getValues("cotElementsForm");
		for (var p in ele) {
			if (p != 'eleAddTime') {
				oldElement[p] = ele[p];
			}
		}
		if (fitMoney != null) {
			oldElement.eleFittingPrice = fitMoney;
		}
		if (priceMoney != null) {
			oldElement.elePrice = priceMoney;
		}
		var basePath = $('basePath').value;
		var filePath = $('picPath').src;
		var s = filePath.indexOf(basePath);
		var fPath = filePath.substring(s + basePath.length, 111111);
		fPath = decodeURI(fPath);
		oldElement.picPath = fPath;
		oldElement.boxIbType = boxIbTypeIdBox.getRawValue();
		oldElement.boxMbType = boxMbTypeIdBox.getRawValue();
		oldElement.boxObType = boxObTypeIdBox.getRawValue();
		oldElement.boxPbType = boxPbTypeIdBox.getRawValue();
		// 生成价修改时间
		oldElement.updateTime = null;
		// oldElement.eleForPerson = empBox.getValue();
		var byId = $('byId').value;
		var myId = $('myId').value;
		var syId = $('syId').value;
		var facId = $('facId').value;
		oldElement.eleTypeidLv1 = byId;
		oldElement.eleTypeidLv2 =myId;
		oldElement.eleTypeidLv3 = syId;
		oldElement.factoryId = facId;

		cotElementsService.saveByCopy(oldElement, eleId, $('eleAddTime').value,
				function(res) {
					if (res == null) {
						Ext.MessageBox.alert("Message",
								'The article does not exist, please re-enter!');
					} else {
						Ext.MessageBox.alert("Message", 'Successfully Save!');
						modlPanel.hide();
					}
				});
	}

	// 新建
	function saveAs() {
		// 验证表单
		var formData = getFormValues(modlPanel, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var byId = $('byId').value;
		var myId = $('myId').value;
		var syId = $('syId').value;
		var facId = $('facId').value;
		var tempoBtn = $('tempoBtn').checked;
		if (tempoBtn) {
			var eleId = "Temp_" + Math.round(Math.random() * 10000000);
			saveAsEle(eleId);
		} else {
			if (byId == '' || myId == '' || syId == '' || facId == '') {
				Ext.MessageBox
						.alert('Message',
								'Please choose Dep.of PRODUCT and Group and Category and Supplier!')
			} else {
				DWREngine.setAsync(false);
				cotSeqService.getEleNo(byId, myId, syId, facId, function(ps) {
							saveAsEle(ps);
						});
				DWREngine.setAsync(true);
			}
		}
	}

	// 大类
	var typeBoxSave = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv4&key=typeEnName",
		cmpId : 'byId',
		fieldLabel : "Dep. Of PRODUCT",
		editable : true,
		valueField : "id",
		displayField : "typeEnName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : '100%',
		sendMethod : "post",
		selectOnFocus : true,
//		allowBlank : false,
//		blankText : "Choose Dep. Of Sales！",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
//		listeners : {
//			"select" : function(com, rec, index) {
//				if (rec.data.id != '') {
//					type2BoxSave
//							.setDataUrl("./servlet/DataSelvert?tbname=CotTypeLv2&key=typeName&typeName=typeIdLv1&type="
//									+ rec.data.id);
//					type2BoxSave.resetData();
//					type3BoxSave.resetData();
//				}
//			}
//		}
	});

	// 中类
	var type2BoxSave = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
		cmpId : 'myId',
		fieldLabel : "Group",
		editable : true,
		valueField : "id",
		displayField : "typeName",
		emptyText : 'Choose',
		anchor : '100%',
		pageSize : 10,
		sendMethod : "post",
		selectOnFocus : true,
//		allowBlank : false,
//		blankText : "Choose Group！",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
//		listeners : {
//			"select" : function(com, rec, index) {
//				if (rec.data.id != '') {
//					type3BoxSave
//							.setDataUrl("./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName&typeName=typeIdLv2&type="
//									+ rec.data.id);
//					type3BoxSave.resetData();
//				}
//			}
//		}
	});

	// 小类
	var type3BoxSave = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
		cmpId : 'syId',
		fieldLabel : "Category",
		editable : true,
		anchor : '100%',
		valueField : "id",
		displayField : "typeName",
		emptyText : 'Choose',
		pageSize : 10,
		sendMethod : "post",
		selectOnFocus : true,
//		allowBlank : false,
//		blankText : "Choose Category！",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 供应厂家
	var facSaveBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'facId',
				fieldLabel : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : '100%',
//				allowBlank : false,
//				blankText : "Choose a Supplier！",
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 显示模板面板
	var modlPanel;
	var isExistNew = true;
	var showModel = function(item, pressed) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0) {
			Ext.Msg.alert("Message", "You do not have permission to add");
			return;
		}

		if ($('importDiv') == null) {
			// window默认在z-index为9000
			Ext.DomHelper.append(document.body, {
				html : '<div id="importDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (modlPanel == null) {
			modlPanel = new Ext.form.FormPanel({
						labelWidth : 90,
						padding : "5px",
						height : 210,
						width : 290,
						layout : 'form',
						frame : true,
						title : "Create New No.",
						labelAlign : "right",
						tools : [{
									id : "close",
									handler : function(event, toolEl, p) {
										p.hide();
									}
								}],
						buttonAlign : 'right',
						buttons : [{
									text : "Create",
									iconCls : "page_table_save",
									handler : saveAs
								}],
						items : [{
									xtype : "checkbox",
									boxLabel : "Temporary Art No.",
									hideLabel : true,
									anchor : "100%",
									id : "tempoBtn"
								}, {
									xtype : 'panel',
									layout : 'form',
									id : 'tempoPnl',
									items : [typeBoxSave, type2BoxSave,
											type3BoxSave, facSaveBox]
								}]
					});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("importDiv").setLeftTop(left, top - 200);
			modlPanel.render("importDiv");
			// $('modelEle').focus();
		} else {
			if (!modlPanel.isVisible()) {
				modlPanel.show();
			} else {
				modlPanel.hide();
			}
		}
	};

	// 获取子货号中英文规格
	function getEleSizeDesc() {
		var eleFlag = $('eleFlag').value;
		if (eleFlag == 1 || eleFlag == 3) {
			DWREngine.setAsync(false);
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

	// 手动修改各个包材价格时修改单个价格及生产价
	var calPackPriceAndPriceFac = function() {

		var packingPrice = $('packingPrice').value;
		var pbPrice = $('boxPbPrice').value;
		var ibPrice = $('boxIbPrice').value;
		var mbPrice = $('boxMbPrice').value;
		var obPrice = $('boxObPrice').value;

		var boxObCount = $('boxObCount').value;
		var boxMbCount = $('boxMbCount').value;
		var boxIbCount = $('boxIbCount').value;
		var boxPbCount = $('boxPbCount').value;

		var boxIbPrice = 0;
		var boxMbPrice = 0;
		var boxObPrice = 0;
		var boxPbPrice = 0;
		var igPrice = 0;
		var igPrice = $('inputGridPrice').value;
		if (igPrice == null || igPrice == '') {
			igPrice = 0;
		}

		if (boxIbCount > 0 && boxObCount > 0 && ibPrice != ''
				&& ibPrice != null) {
			boxIbPrice = (parseFloat(ibPrice) * parseInt(boxObCount) / parseInt(boxIbCount))
					.toFixed(3);
		}
		if (boxMbCount > 0 && boxObCount > 0 && mbPrice != ''
				&& mbPrice != null) {
			boxMbPrice = (parseFloat(mbPrice) * parseInt(boxObCount) / parseInt(boxMbCount))
					.toFixed(3);
		}
		if (boxObCount > 0 && obPrice != '' && obPrice != null) {
			boxObPrice = parseFloat(obPrice).toFixed(3);
		}
		if (boxPbCount > 0 && boxObCount > 0 && pbPrice != ''
				&& pbPrice != null) {
			boxPbPrice = (parseFloat(pbPrice) * parseInt(boxObCount) / parseInt(boxPbCount))
					.toFixed(3);
		}

		if (boxObCount != 0 && boxObCount != '') {
			packingPrice = ((parseFloat(boxIbPrice) + parseFloat(boxMbPrice)
					+ parseFloat(boxPbPrice) + parseFloat(boxObPrice) + parseFloat(igPrice)) / parseInt(boxObCount))
					.toFixed(3);
		}
		$('packingPrice').value = packingPrice;
		var eleId = $('eId').value;
		cotElementsService.calPriceFacByPackPrice(eleId, packingPrice,
				function(facprice) {
					if (facprice != 0) {
						$('priceFac').value = facprice
					}
				});
		// $('priceFac').value = packingPrice;
	}
	var calPriceByPakingPrice = function() {
		var packPrice = $('packingPrice').value;
		if (packPrice == '' || isNaN(packPrice)) {
			packPrice = 0;
		}
		var eleId = $('eId').value;
		cotElementsService.calPriceFacByPackPrice(eleId, packPrice, function(
						facprice) {
					if (facprice != 0) {
						$('priceFac').value = facprice
					}
				});
	}

	// 添加
	// 全局样品变量
	var oldElement = null;
	var mod = function() {
		// alert($('boxIbCount'))
		var formData = getFormValues(myForm, true);
		// 表单验证失败时,返回
		if (!formData) {
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
		if ($('tuiLv').value == '') {
			$('tuiLv').value = 0;
		}
		if ($('liRun').value == '') {
			$('liRun').value = 0;
		}

		// 判断货号是否重复
		var isExist = false;
		DWREngine.setAsync(false);
		cotElementsService.findIsExistEleId($('eleId').value, $('eId').value,
				function(res) {
					if (res) {
						isExist = true;
					}
				});
		DWREngine.setAsync(true);
		if (isExist) {
			Ext.MessageBox.alert("提示消息", 'Fail Save！Art No has exist!');
			return;
		}

		// 主样品表单
		var ele = DWRUtil.getValues("cotElementsForm");
		for (var p in ele) {
			if (p != 'eleAddTime') {
				oldElement[p] = ele[p];
			}
		}
		if (fitMoney != null) {
			oldElement.eleFittingPrice = fitMoney;
		}
		if (priceMoney != null) {
			oldElement.elePrice = priceMoney;
		}

		var basePath = $('basePath').value;
		var filePath = $('picPath').src;
		var s = filePath.indexOf(basePath);
		var fPath = filePath.substring(s + basePath.length, 111111);
		fPath = decodeURI(fPath);
		oldElement.picPath = fPath;

		oldElement.boxIbType = boxIbTypeIdBox.getRawValue();
		oldElement.boxMbType = boxMbTypeIdBox.getRawValue();
		oldElement.boxObType = boxObTypeIdBox.getRawValue();
		oldElement.boxPbType = boxPbTypeIdBox.getRawValue();
		// 生成价修改时间
		if(Ext.isEmpty(Ext.getCmp("updateTime").getValue())){
			oldElement.updateTime =null;
		}else{
			oldElement.updateTime = Ext.getCmp("updateTime").getValue();
		}
		// oldElement.eleForPerson = empBox.getValue();
		//是否锁定barcode
		if(Ext.getCmp('lockBarcode').iconCls=='lock'){
			oldElement.lockBar=1;
		}else{
			oldElement.lockBar=0;
		}
		
		DWREngine.setAsync(false);
		mask("Saving... ...");
		DWREngine.setAsync(true);
		cotElementsService.modifyElements(oldElement, $('eleAddTime').value,
				function(res) {
					if (res == null) {
						Ext.MessageBox.alert("Message", 'Edited failed！');
					} else {
						// closeandreflashEC('true', 'eleGrid', false);
						reflashParent('eleGrid');
						Ext.MessageBox.alert("Message", 'Edited Successfully！');
					}
					unmask();
				})
	}

	// 隐藏的字段
	var hiddenItems = new Ext.Panel({
				items : [{
							xtype : "textfield",
							fieldLabel : "YY-MM-Dd",
							anchor : "100%",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							tabIndex : 17,
							id : "eleTypenameLv2",
							name : "eleTypenameLv2",
							maxLength : 100
						}, {
							xtype : "textarea",
							fieldLabel : "Description",
							anchor : "100%",
							height : 38,
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							tabIndex : 28,
							id : "eleDesc",
							name : "eleDesc",
							maxLength : 500
						}, {
							xtype : "numberfield",
							fieldLabel : "Tax rate%",
							anchor : "100%",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							decimalPrecision : 2,
							tabIndex : 26,
							id : "tuiLv",
							name : "tuiLv",
							maxLength : 99999999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "Cube",
							anchor : "100%",
							hidden : true,
							hideLabel : true,
							allowBlank : true,
							decimalPrecision : 2,
							tabIndex : 20,
							id : "cube",
							name : "cube",
							maxLength : 999999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "Middle box CM",
							anchor : "100%",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							decimalPrecision : 2,
							tabIndex : 60,
							id : "boxMbL",
							name : "boxMbL",
							maxLength : 10000,
							listeners : {
								"change" : calInch
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							decimalPrecision : 2,
							tabIndex : 61,
							id : "boxMbW",
							name : "boxMbW",
							maxLength : 10000,
							listeners : {
								"change" : calInch
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							decimalPrecision : 2,
							tabIndex : 62,
							id : "boxMbH",
							name : "boxMbH",
							maxLength : 10000,
							listeners : {
								"change" : calInch
							}
						}, {
							xtype : "panel",
							layout : "form",
							hidden : true,
							hideLabel : true,
							labelWidth : 70,
							items : [{
										xtype : "numberfield",
										fieldLabel : "INCH",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 33,
										id : "boxLInch",
										name : "boxLInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "INCH",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 41,
										id : "boxPbLInch",
										name : "boxPbLInch",
										maxLength : 999.99,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "INCH",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 50,
										id : "boxIbLInch",
										name : "boxIbLInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "INCH",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 63,
										id : "boxMbLInch",
										name : "boxMbLInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "INCH",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 72,
										id : "boxObLInch",
										name : "boxObLInch",
										maxLength : 10000,
										listeners : {
											"change" : calOSizeCm
										}
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.12,
							layout : "form",
							labelWidth : 4,
							hidden : true,
							hideLabel : true,
							items : [{
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 34,
										id : "boxWInch",
										name : "boxWInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 42,
										id : "boxPbWInch",
										name : "boxPbWInch",
										maxLength : 999.99,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 51,
										id : "boxIbWInch",
										name : "boxIbWInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 64,
										id : "boxMbWInch",
										name : "boxMbWInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 73,
										id : "boxObWInch",
										name : "boxObWInch",
										maxLength : 10000,
										listeners : {
											"change" : calOSizeCm
										}
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.12,
							layout : "form",
							labelWidth : 4,
							hidden : true,
							hideLabel : true,
							items : [{
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 35,
										id : "boxHInch",
										name : "boxHInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 43,
										id : "boxPbHInch",
										name : "boxPbHInch",
										maxLength : 999.99,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 56,
										id : "boxIbHInch",
										name : "boxIbHInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 65,
										id : "boxMbHInch",
										name : "boxMbHInch",
										maxLength : 10000,
										listeners : {
											"change" : calCm
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "x",
										anchor : "100%",
										labelSeparator : " ",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 74,
										id : "boxObHInch",
										name : "boxObHInch",
										maxLength : 10000,
										listeners : {
											"change" : calOSizeCm
										}
									}]
						}, {
							xtype : "textarea",
							fieldLabel : "Size Description",
							anchor : "100%",
							height : 50,
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							tabIndex : 78,
							id : "eleSizeDesc",
							name : "eleSizeDesc",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "Sales Unit",
							anchor : "100%",
							height : 50,
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							tabIndex : 84,
							id : "boxRemark",
							name : "boxRemark",
							maxLength : 500
						}, {
							xtype : "numberfield",
							fieldLabel : "Price",
							anchor : "100%",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							decimalPrecision : 3,
							tabIndex : 37,
							id : "packingPrice",
							name : "packingPrice",
							maxLength : 10000000,
							listeners : {
								"change" : calPriceByPakingPrice
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Packing",
							anchor : "100%",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							decimalPrecision : 0,
							tabIndex : 66,
							id : "boxMbCount",
							name : "boxMbCount",
							maxLength : 99999999,
							listeners : {
								"change" : function() {
									calPrice();
								}
							}
						}, {
							xtype : "panel",
							columnWidth : 0.29,
							layout : "form",
							labelWidth : 35,
							hidden : true,
							hideLabel : true,
							items : [{
										xtype : "numberfield",
										fieldLabel : "Price",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 3,
										tabIndex : 46,
										id : "boxPbPrice",
										name : "boxPbPrice",
										maxLength : 99999999.99,
										listeners : {
											"change" : calPackPriceAndPriceFac
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "Price",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 3,
										tabIndex : 59,
										id : "boxIbPrice",
										name : "boxIbPrice",
										maxLength : 99999999.99,

										listeners : {
											"change" : calPackPriceAndPriceFac
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "Price",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 3,
										tabIndex : 68,
										id : "boxMbPrice",
										name : "boxMbPrice",
										maxLength : 99999999.99,
										listeners : {
											"change" : calPackPriceAndPriceFac
										}
									}, {
										xtype : "numberfield",
										fieldLabel : "Price",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 3,
										tabIndex : 77,
										id : "boxObPrice",
										name : "boxObPrice",
										maxLength : 99999999.99,
										listeners : {
											"change" : calPackPriceAndPriceFac
										}
									}]
						}, {
							xtype : "numberfield",
							fieldLabel : "CU'FT",
							anchor : "100%",
							allowBlank : true,
							hidden : true,
							hideLabel : true,
							decimalPrecision : 3,
							tabIndex : 81,
							id : "boxCuft",
							name : "boxCuft",
							maxLength : 1000000,
							listeners : {
								"change" : function(txt, newVal, oldVal) {
									calCbm(newVal);
								}
							}
						}, {
							xtype : "numberfield",
							id : "inputGridPrice",
							name : "inputGridPrice",
							fieldLabel : "Price",
							hidden : true,
							hideLabel : true,
							tabIndex : 86,
							decimalPrecision : 3,
							maxLength : 99999999.99,
							anchor : "100%",
							listeners : {
								"change" : calPackPriceAndPriceFac
							}
						}, {
							xtype : "panel",
							title : "",
							layout : "column",
							hidden : true,
							hideLabel : true,
							columnWidth : 1,
							items : [{
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.5,
										items : [{
													xtype : "numberfield",
													fieldLabel : "摆放方式",
													id : 'putL',
													name : 'putL',
													decimalPrecision : 2,
													maxLength : 10000,
													tabIndex : 87,
													anchor : "100%",
													listeners : {
														"change" : calPrice
													}
												}]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.25,
										labelWidth : 15,
										items : [{
													xtype : "numberfield",
													id : 'putW',
													name : 'putW',
													fieldLabel : "x",
													anchor : "100%",
													tabIndex : 88,
													decimalPrecision : 2,
													maxLength : 10000,
													labelSeparator : " ",
													listeners : {
														"change" : calPrice
													}
												}]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.25,
										labelWidth : 15,
										items : [{
													xtype : "numberfield",
													id : 'putH',
													name : 'putH',
													fieldLabel : "x",
													anchor : "100%",
													tabIndex : 89,
													decimalPrecision : 2,
													maxLength : 10000,
													labelSeparator : " ",
													listeners : {
														"change" : calPrice
													}
												}]
									}]
						}, {
							xtype : "datefield",
							fieldLabel : "Added date",
							value : new Date(),
							disabled : true,
							disabledClass : 'combo-disabled',
							hidden : true,
							hideLabel : true,
							id : "addTime",
							name : "addTime",
							anchor : "100%",
							allowBlank : true,
							format : "Y-m-d"
						}, eleFlagComb, eleHsidBox, boxMbTypeIdBox,
						inputGridTypeIdBox]
			});

	var mainPanel = new Ext.Panel({
		layout : "form",
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
				xtype : "panel",
				title : "",
				columnWidth : 0.35,
				layout : "column",
				items : [{
					xtype : "panel",
					layout : "form",
					columnWidth : 1,
					items : [{
						xtype : "textfield",
						fieldLabel : "<font color=red>Art No.</font>",
						anchor : "100%",
						tabIndex : 1,
						allowBlank : false,
						blankText : "Plesae enter article no.",
						maxLength : 100,
						maxLengthText : "",
						name : "eleId",
						id : "eleId",
						invalidText : "Art No. already exists, please re-enter！",
						readOnly : true,
						listeners : {
							"blur" : function(v) {
								v.setValue(v.getValue().toUpperCase())
							}
						},
						validationEvent : 'change',
						validator : function(thisText) {
							if (thisText != '') {
								cotElementsService.findIsExistEleId(thisText,
										0, function(res) {
											if (res) {
												isExist = false;
											} else {
												isExist = true;
											}
										});
							}
							return isExist;
						}
					}]
				}, {
					xtype : "button",
					width : 20,
					text : "",
					iconCls : "cal",
					handler : setArtNo,
					listeners : {
						"render" : function(obj) {
							var tip = new Ext.ToolTip({
								target : obj.getEl(),
								anchor : 'top',
								maxWidth : 160,
								minWidth : 160,
								html : 'Depending on the configuration automatically generated Art No.!'
							});
						}
					}
				}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.3,
				layout : "form",
				items : [facBox]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.35,
				layout : "form",
				labelWidth : 130,
				items : [{
							xtype : "textfield",
							fieldLabel : "Supplier Art No.",
							anchor : "100%",
							allowBlank : true,
							tabIndex : 3,
							id : "factoryNo",
							name : "factoryNo",
							maxLength : 200
						}]
			}]
		}, {
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
				xtype : "panel",
				title : "",
				columnWidth : 0.35,
				layout : "column",
				items : [{
					xtype : "panel",
					layout : "form",
					columnWidth : 1,
					items : [{
						xtype : "textfield",
						fieldLabel : "Barcode",
						anchor : "100%",
						id : "barcode",
						name : "barcode",
						tabIndex : 4,
						maxLength : 30,
						listeners : {
							"render" : function(obj) {
								var tip = new Ext.ToolTip({
									target : obj.getEl(),
									anchor : 'top',
									maxWidth : 160,
									minWidth : 160,
									html : 'Click the right button to automatically generate the Barcode!'
								});
							}
						}
					}]
				}, {
					xtype : "button",
					width : 20,
					text : "",
					cls : "SYSOP_ADD",
					iconCls : "barcode",
					handler : setBarcodeNo,
					listeners : {
						"render" : function(obj) {
							var tip = new Ext.ToolTip({
								target : obj.getEl(),
								anchor : 'top',
								maxWidth : 160,
								minWidth : 160,
								html : 'Depending on the configuration automatically generated Barcode!'
							});
						}
					}
				},{
					xtype : "button",
					width : 20,
					text : "",
					id:'lockBarcode',
					iconCls : "unlock",
					handler : lockBarcode,
					listeners : {
						"render" : function(obj) {
							var tip = new Ext.ToolTip({
								target : obj.getEl(),
								anchor : 'top',
								maxWidth : 160,
								minWidth : 160,
								html : 'Lock or UnLock Barcode!'
							});
						}
					}
				}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.2,
				layout : "form",
				items : [{
							xtype : "numberfield",
							fieldLabel : "Pieces",
							anchor : "100%",
							allowBlank : true,
							decimalPrecision : 0,
							tabIndex : 5,
							id : "eleUnitNum",
							name : "eleUnitNum",
							maxLength : 99999999999,
							listeners : {
								'change' : function(txt, newVal, oldVal) {
									if (newVal == 1) {
										$('eleUnit').value = "PC";
									}
									if (newVal > 1) {
										$('eleUnit').value = "S/" + newVal;
									}
								}
							}
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.2,
				layout : "form",
				labelWidth : 60,
				items : [{
							xtype : "textfield",
							fieldLabel : "Unit",
							anchor : "100%",
							allowBlank : true,
							tabIndex : 6,
							maxLength : 20,
							id : "eleUnit",
							name : "eleUnit"
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.25,
				layout : "form",
				labelWidth : 50,
				items : [{
							xtype : "numberfield",
							fieldLabel : "MOQ",
							anchor : "100%",
							decimalPrecision : 0,
							tabIndex : 7,
							id : "eleMod",
							name : "eleMod",
							maxLength : 99999999999
						}]
			}]
		}, {
			xtype : "textfield",
			fieldLabel : "Description",
			anchor : "100%",
			allowBlank : true,
			tabIndex : 8,
			id : "eleNameEn",
			name : "eleNameEn",
			maxLength : 200
		}, {
			xtype : "textfield",
			fieldLabel : "Quality",
			anchor : "100%",
			allowBlank : true,
			tabIndex : 9,
			id : "eleName",
			name : "eleName",
			maxLength : 200
		}, {
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.3,
						layout : "form",
						items : [typeBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.3,
						layout : "form",
						items : [type2Box]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.4,
						layout : "form",
						items : [type3Box]
					}]
		}, {
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.3,
						layout : "form",
						items : [sourceBox, empBox, {
									xtype : "numberfield",
									fieldLabel : "Weight(g)",
									anchor : "100%",
									allowBlank : true,
									decimalPrecision : 2,
									tabIndex : 18,
									id : "boxWeigth",
									name : "boxWeigth",
									maxLength : 999999.99,
									listeners : {
										"change" : calWeight
									}
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.3,
						layout : "form",
						items : [{
									xtype : "datefield",
									fieldLabel : "Date",
									anchor : "100%",
									value : new Date(),
									readOnly : true,
									allowBlank : true,
									format : "Y-m-d",
									tabIndex : 17,
									id : "eleAddTime",
									name : "eleAddTime"
								}, {
									xtype : "hidden",
									fieldLabel : "Level",
									anchor : "100%",
									allowBlank : true,

									tabIndex : 14,
									id : "eleGrade",
									name : "eleGrade",
									maxLength : 30
								}, {
									xtype : "numberfield",
									fieldLabel : "Pcs/Ctn",
									anchor : "100%",
									allowBlank : true,
									decimalPrecision : 0,
									maxLength : 99999999,
									tabIndex : 26,
									id : "boxObCount",
									name : "boxObCount",
									listeners : {
										"change" : calWeight
									}
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.4,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "CBM",
									anchor : "100%",
									allowBlank : true,
									decimalPrecision : 3,
									tabIndex : 15,
									id : "boxCbm",
									name : "boxCbm",
									maxLength : 1000000,
									listeners : {
										"change" : function(txt, newVal, oldVal) {
											calCuft(newVal);
										}
									}
								}, {
									xtype : "panel",
									layout : "column",
									items : [{
												xtype : "panel",
												columnWidth : 0.5,
												layout : "form",
												items : [{
															xtype : "numberfield",
															fieldLabel : "G.W(Kgs)",
															anchor : "100%",
															allowBlank : true,
															decimalPrecision : 3,
															tabIndex : 21,
															id : "boxGrossWeigth",
															name : "boxGrossWeigth",
															maxLength : 999999.99
														}]
											}, {
												xtype : "panel",
												columnWidth : 0.5,
												layout : "form",
												items : [{
															xtype : "numberfield",
															fieldLabel : "N.W(Kgs)",
															anchor : "100%",
															allowBlank : true,
															decimalPrecision : 3,
															tabIndex : 20,
															id : "boxNetWeigth",
															name : "boxNetWeigth",
															maxLength : 999999.99
														}]
											}]

								}]
					}]
		}, {
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.3,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "Design",
									anchor : "100%",
									allowBlank : true,
									tabIndex : 19,
									id : "eleCol",
									name : "eleCol",
									maxLength : 50
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.3,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Import Tax(%)",
									anchor : "100%",
									id : "importTax",
									name : 'importTax'
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.4,
						layout : "form",
						items : [{
									xtype : "datefield",
									fieldLabel : 'Updated',
									id : 'updateTime',
									anchor : "100%",
									readOnly : true,
									format : "Y-m-d",
									name : 'updateTime'
								}]
					}]
		}, {
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
				xtype : "panel",
				title : "",
				columnWidth : 0.3,
				layout : "column",
				items : [{
					xtype : "panel",
					title : "",
					columnWidth : 0.7,
					layout : "form",
					items : [{
								xtype : "numberfield",
								fieldLabel : "Purchase Price",
								anchor : "100%",
								allowBlank : true,
								decimalPrecision : facNum,
								tabIndex : 22,
								id : "priceFac",
								name : "priceFac",
								maxLength : 10000000,
								listeners : {
									'change' : function() {
										Ext.getCmp('updateTime')
												.setValue(new Date());
										setPriceOut();
									}
								}
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.3,
					layout : "form",
					items : [facCurBox]
				}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.3,
				layout : "column",
				items : [{
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : "",
					columnWidth : 0.7,
					items : [{
						xtype : "numberfield",
						fieldLabel : "<a onclick=setPriceOut() ext:qwidth=200 ext:qtip='According to the default configuration after clicking outside the selling price formula' style='color:blue;cursor: hand;text-decoration: underline'>Sales Price</a>",
						anchor : "100%",
						allowBlank : true,
						decimalPrecision : outNum,
						tabIndex : 24,
						id : "priceOut",
						name : "priceOut",
						maxLength : 10000000,
						listeners : {
							'change' : function() {
								setLiRun();
							}
						}

					}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.3,
					items : [outCurBox]
				}]
			}, {
				xtype : "panel",
				layout : "column",
				columnWidth : 0.4,
				items : [{
							xtype : "panel",
							columnWidth : 1,
							layout : "form",
							items : [{
										xtype : "numberfield",
										fieldLabel : "Profit(%)",
										anchor : "100%",
										allowBlank : true,
										decimalPrecision : 2,
										tabIndex : 26,
										id : "liRun",
										name : "liRun",
										maxLength : 999999.99,
										listeners : {
											'change' : function() {
												setPriceOut();
											}
										}
									}]
						}]
			}]
		}]
	})

	var isExist = true;
	// 主表单
	var myForm = new Ext.form.FormPanel({
		formId : "cotElementsForm",
		anchor : '98%',
		labelWidth : 90,
		labelAlign : "right",
		layout : "form",
		padding : "5px",
		frame : true,
		buttonAlign : 'center',
		buttons : [{
					text : "Save",
					cls : "SYSOP_MOD",
					iconCls : "page_table_save",
					handler : mod
				}, {
					text : "Delete",
					cls : "SYSOP_DEL",
					iconCls : "page_del",
					handler : delParent
				}, {
					text : "Save As",
					cls : "SYSOP_ADD",
					iconCls : "page_table_save",
					handler : showModel
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					handler : function() {
						closeandreflashEC(true, 'eleGrid', false);
					}
				}
		// , {
		// text : "生成规格描述",
		// iconCls : "page_tong",
		// handler : getEleSizeDesc
		// }
		],
		items : [{
			xtype : "panel",
			layout : "border",
			width : "100%",
			height : 425,
			items : [{
				xtype : "fieldset",
				region : "center",
				title : "Basic information",
				layout : "form",
				buttons : [{
							text : '',
							hidden : true
						}],
				items : [mainPanel, {
					xtype : "panel",
					layout : "column",
					items : [{
								xtype : "panel",
								columnWidth : 0.5,
								layout : "column",
								items : [{
											xtype : "panel",
											columnWidth : 0.5,
											layout : "form",
											items : [{
														xtype : "numberfield",
														fieldLabel : "Product Size",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 27,
														id : "boxL",
														name : "boxL",
														maxLength : 10000,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "Packing Size",
														anchor : "100%",
														hidden : true,
														hideLabel : true,
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 30,
														id : "boxPbL",
														name : "boxPbL",
														maxLength : 999.99,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "Inner Box",
														anchor : "100%",
														allowBlank : true,
														hidden : true,
														hideLabel : true,
														decimalPrecision : 2,
														tabIndex : 33,
														id : "boxIbL",
														name : "boxIbL",
														maxLength : 10000,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "Carton Size",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 36,
														id : "boxObL",
														name : "boxObL",
														maxLength : 10000,
														listeners : {
															"change" : calOSize
														}
													}]
										}, {
											xtype : "panel",
											columnWidth : 0.25,
											layout : "form",
											labelWidth : 4,
											buttonAlign : "center",
											labelAlign : "right",
											items : [{
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 28,
														id : "boxW",
														name : "boxW",
														maxLength : 10000,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 31,
														hidden : true,
														hideLabel : true,
														id : "boxPbW",
														name : "boxPbW",
														maxLength : 999.99,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 34,
														hidden : true,
														hideLabel : true,
														id : "boxIbW",
														name : "boxIbW",
														maxLength : 10000,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 37,
														id : "boxObW",
														name : "boxObW",
														maxLength : 10000,
														listeners : {
															"change" : calOSize
														}
													}]
										}, {
											xtype : "panel",
											columnWidth : 0.25,
											layout : "form",
											labelWidth : 4,
											items : [{
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 29,
														id : "boxH",
														name : "boxH",
														maxLength : 10000,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 32,
														hidden : true,
														hideLabel : true,
														id : "boxPbH",
														name : "boxPbH",
														maxLength : 999.99,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 35,
														hidden : true,
														hideLabel : true,
														id : "boxIbH",
														name : "boxIbH",
														maxLength : 10000,
														listeners : {
															"change" : calInch
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "x",
														anchor : "100%",
														labelSeparator : " ",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 38,
														id : "boxObH",
														name : "boxObH",
														maxLength : 10000,
														listeners : {
															"change" : calOSize
														}
													}]
										}]
							}, {
								xtype : "panel",
								columnWidth : 0.5,
								layout : "form",
								items : [{
											xtype : "textarea",
											fieldLabel : "Size Description",
											anchor : "100%",
											height : 47,
											allowBlank : true,
											tabIndex : 47,
											id : "eleInchDesc",
											name : "eleInchDesc",
											maxLength : 500
										}]
							}, {
								xtype : "panel",
								columnWidth : 0.5,
								hidden : true,
								layout : "column",
								items : [{
											xtype : "panel",
											columnWidth : 1,
											layout : "form",
											items : [boxTypeIdBox]
										}, {
											xtype : "panel",
											columnWidth : 0.5,
											layout : "form",
											items : [{
														xtype : "numberfield",
														fieldLabel : "Packing",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 0,
														tabIndex : 40,
														id : "boxPbCount",
														name : "boxPbCount",
														maxLength : 99999999,
														listeners : {
															"change" : function() {
																calPrice();
															}
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "Packing",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 0,
														tabIndex : 42,
														id : "boxIbCount",
														name : "boxIbCount",
														maxLength : 99999999,
														listeners : {
															"change" : function() {
																calPrice();
															}
														}
													}]
										}, {
											xtype : "panel",
											columnWidth : 0.5,
											layout : "form",
											labelWidth : 4,
											items : [boxPbTypeIdBox,
													boxIbTypeIdBox,
													boxObTypeIdBox]
										}]
							}]
				}, {
					xtype : "textarea",
					fieldLabel : "Sales Unit",
					anchor : "100%",
					height : 38,
					allowBlank : true,
					tabIndex : 46,
					id : "boxRemarkCn",
					name : "boxRemarkCn",
					maxLength : 500
				}, {
					xtype : "textarea",
					fieldLabel : "Comment",
					anchor : "100%",
					height : 38,
					allowBlank : true,
					tabIndex : 48,
					id : "eleRemark",
					name : "eleRemark",
					maxLength : 5000
				}]
			}, {
				xtype : "panel",
				region : "east",
				width : 220,
				items : [{
					xtype : "fieldset",
					title : "Picture",
					region : "north",
					height : 240,
					layout : "hbox",
					layoutConfig : {
						padding : '5',
						pack : 'center',
						align : 'middle'
					},
					buttons : [{
								text : '',
								hidden : true
							}],
					items : [{
						xtype : "panel",
						width : 150,
						buttonAlign : "center",
						html : '<div align="center" style="width: 135px; height: 132px;">'
								+ '<img src="common/images/zwtp.png" id="picPath" name="picPath"'
								+ 'onload="javascript:DrawImage(this,135,132)" onclick="showBigPicDiv(this)"/></div>',
						buttons : [{
									width : 65,
									text : "Update",
									id : "upmod",
									iconCls : "upload-icon",
									handler : showUploadPanel
								}, {
									width : 65,
									text : "Delete",
									id : "updel",
									iconCls : "upload-icon-del",
									handler : delPic
								}]
					}]
				}, {
					xtype : "fieldset",
					title : "Container",
					region : "center",
					layout : "form",
					labelWidth : 100,
					height : 175,
					width : 220,
					buttonAlign : 'center',
					items : [{
								xtype : "numberfield",
								fieldLabel : "20\"Container",
								anchor : "100%",
								decimalPrecision : 0,
								disabled : true,
								disabledClass : 'combo-disabled',
								allowBlank : true,
								id : "box20Count",
								name : "box20Count",
								maxLength : 10000000000000
							}, {
								xtype : "numberfield",
								fieldLabel : "40\"Container",
								anchor : "100%",
								allowBlank : true,
								disabled : true,
								disabledClass : 'combo-disabled',
								decimalPrecision : 0,
								id : "box40Count",
								name : "box40Count",
								maxLength : 100000000000000
							}, {
								xtype : "numberfield",
								fieldLabel : "40 HC Container",
								anchor : "100%",
								allowBlank : true,
								disabled : true,
								disabledClass : 'combo-disabled',
								decimalPrecision : 0,
								id : "box40hqCount",
								name : "box40hqCount",
								maxLength : 100000000000000
							}, {
								xtype : "numberfield",
								fieldLabel : "45\"Container",
								anchor : "100%",
								allowBlank : true,
								disabled : true,
								disabledClass : 'combo-disabled',
								decimalPrecision : 0,
								id : "box45Count",
								name : "box45Count",
								maxLength : 100000000000000
							}],
					buttons : [{
						text : "<span ext:qwidth=100 ext:qtip='At the maximum loaded cubes of container, calculate the containers'>By Cube</span>",
						width : 70,
						iconCls : "page_calculator",
						handler : function() {
							calCuft($("boxCbm").value);
						}
					}]
				}]
			}]
		}, hiddenItems, {
			xtype : 'hidden',
			name : 'id',
			id : 'id'
		}]
	});

	// 打开上传面板
	function showUploadPanel() {
		var id = $('eId').value;
		var opAction = "insert";
		if (id == 'null' || id == '')
			opAction = "insert";
		else
			opAction = "modify";
		var win = new UploadWin({
					params : {
						pEId : id
					},
					waitMsg : "Uploading......",
					opAction : opAction,
					imgObj : $('picPath'),
					imgUrl : './showPicture.action?flag=ele&elementId=' + id
							+ '&tmp=' + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadElePicture.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}
	// 删除图片
	function delPic() {
		var eId = $('eId').value;
		Ext.MessageBox.confirm('Message',
				'are you sure to delete this picture?', function(btn) {
					if (btn == 'yes') {
						if (eId == null || eId == '' || eId == 'null') {
							$('picPath').src = "common/images/zwtp.png";
						} else {
							cotElementsService.deletePicImg(parseInt(eId),
									function(res) {
										if (res) {
											$('picPath').src = "common/images/zwtp.png";
										} else {
											Ext.Msg.alert("Message",
													"It's fail to delete it!");
										}
									})
						}
					}
				});
	}

	var tbl = new Ext.TabPanel({
				height : 450,
				anchor : '98%',
				deferredRender : false,
				defaults : {
					autoScroll : true
				},
				defaultType : "iframepanel",
				activeTab : -1,
				id : "maincontent",
				items : [{
							title : "Related Picture",
							itemId : 'showPicRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'showPic'
								}
							}, // iframe的Id
							loadMask : {
								msg : 'Data Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									showPicture();
								}
							}
						}, {
							title : "Related Documents",
							itemId : 'showfileRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'showfile'
								}
							}, // iframe的Id
							loadMask : {
								msg : 'Data Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadFileInfo();
								}
							}
						}, {
							title : "Offer History",
							itemId : 'priceInfoRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'priceInfo'
								}
							},
							loadMask : {
								msg : 'Data Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadPriceInfo();
								}
							}
						}, {
							title : "Order History",
							itemId : 'orderInfoRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'orderInfo'
								}
							}, // iframe的Id
							loadMask : {
								msg : 'Data Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadOrderInfo();
								}
							}
						}, {
							title : "Purchase History",
							itemId : 'orderfacInfoRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'orderfacInfo'
								}
							}, // iframe的Id
							loadMask : {
								msg : 'Data Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadOrderFacInfo();
								}
							}
						}
						// , {
						// title : "送样记录",
						// itemId : 'givenInfoRec',
						// defaultSrc : '',
						// frameConfig : {
						// autoCreate : {
						// id : 'givenInfo'
						// }
						// }, // iframe的Id
						// loadMask : {
						// msg : 'Loading...'
						// },
						// listeners : {
						// "activate" : function(panel) {
						// loadGivenInfo();
						// }
						// }
						// }, {
						// title : "征样记录",
						// itemId : 'signInfoRec',
						// defaultSrc : '',
						// frameConfig : {
						// autoCreate : {
						// id : 'signInfo'
						// }
						// }, // iframe的Id
						// loadMask : {
						// msg : 'Loading...'
						// },
						// listeners : {
						// "activate" : function(panel) {
						// loadSignInfo();
						// }
						// }
						// }
						, {
							title : "Cust.No.",

							itemId : 'custNoInfoRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'custNoInfo'
								}
							}, // iframe的Id
							loadMask : {
								msg : 'Data Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadcustNoInfo();
								}
							}
						}
						// , {
						// title : "子货号",
						// itemId : 'childInfoRec',
						// defaultSrc : '',
						// frameConfig : {
						// autoCreate : {
						// id : 'childInfo'
						// }
						// }, // iframe的Id
						// loadMask : {
						// msg : 'Loading...'
						// },
						// listeners : {
						// "activate" : function(panel) {
						// loadChildInfo();
						// }
						// }
						// }
						, {
							title : "Quotations from Suppliers",

							itemId : 'priceFacInfoRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'priceFacInfo'
								}
							}, // iframe的Id
							loadMask : {
								msg : 'Data Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadPriceFacInfo();
								}
							}
						}
				// ,{
				// title : "样品配件",
				// itemId : 'eleFittingInfoRec',
				// defaultSrc : '',
				// frameConfig : {
				// autoCreate : {
				// id : 'eleFittingInfo'
				// }
				// },
				// loadMask : {
				// msg : 'Loading...'
				// },
				// listeners : {
				// "activate" : function(panel) {
				// loadFittingInfo();
				// }
				// }
				// },
				// {
				// title : "样品成本",
				// itemId : 'elePriceFrameRec',
				// defaultSrc : '',
				// frameConfig : {
				// autoCreate : {
				// id : 'elePriceFrame'
				// }
				// }, // iframe的Id
				// loadMask : {
				// msg : 'Loading...'
				// },
				// listeners : {
				// "activate" : function(panel) {
				// loadElePrice();
				// }
				// }
				// }
				]
			});

	var mainPanel = new Ext.Panel({
				name : "mainPanel",
				autoScroll : true,
				layout : 'form',
				frame : true,
				items : [myForm, tbl]
			})

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [mainPanel]
			});

	// var ele = Ext.query("li[id*=showPicRec]")[0];
	// 单击标签函数
	// ele.onclick = showPicture;

	// 加载样品图片浏览
	function showPicture() {
		// 设置权限为空
		var isPopedom = getPopedomByOpType("cotpicture.do", "SEL");
		if (isPopedom == 0) // 没有查看图片权限
		{
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
			return;
		} else {
			var id = $('eId').value;

			var picFrame = window.frames["showPic"]// document.showPic;
			picFrame.location.href = "cotelements.do?method=showPicture&eId="
					+ id;
		}
	}

	// 加载主样品的文件信息
	function loadFileInfo() {
		// 设置权限为空
		var isSelFile = getPopedomByOpType("cotfile.do", "SEL");
		if (isSelFile == 0)// 没有查看文件信息权限
		{
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!!");
			return;
		} else {
			var eleId = $("eId").value;
			var frame = window.frames["showfile"]// document.showfile;
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
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
			return;
		} else {
			// 设置权限为空
			mapPopedom = null;
			var isSel = getPopedomByOpType("cotprice.do", "SEL");
			if (isSel == 0)// 没有查看报价记录权限
			{
				Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
				return;
			} else {
				var eleId = $("eleId").value;
				var frame = window.frames["priceInfo"]// document.priceInfo;
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
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
			return;
		} else {
			// 设置权限为空
			mapPopedom = null;
			var isSel = getPopedomByOpType("cotorder.do", "SEL");
			if (isSel == 0)// 没有查看报价记录权限
			{
				Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
				return;
			} else {
				var eleId = $("eleId").value;
				var frame = window.frames["orderInfo"]// document.orderInfo;
				frame.location.href = "cotelements.do?method=loadOrderInfo"
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
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
			return;
		} else {
			// 设置权限为空
			mapPopedom = null;
			var isSel = getPopedomByOpType("cotgiven.do", "SEL");
			if (isSel == 0)// 没有查看送样记录权限
			{
				Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
				return;
			} else {
				var eleId = $("eleId").value;
				var frame = window.frames["givenInfo"]// document.givenInfo;
				frame.location.href = "cotelements.do?method=loadGivenInfo"
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
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
			return;
		} else {
			// 设置权限为空
			mapPopedom = null;
			var isSel = getPopedomByOpType("cotsign.do", "SEL");
			if (isSel == 0)// 没有查看送样记录权限
			{
				Ext.Msg.alert("Message", "Sorry, you do not have Authority!!");
				return;
			} else {
				var eleId = $("eleId").value;
				var frame = window.frames["signInfo"]// document.givenInfo;
				frame.location.href = "cotelements.do?method=loadSignInfo"
						+ "&eleId=" + eleId;
			}
		}
	}

	// 加载主样品的客号
	function loadcustNoInfo() {
		var eleId = $("eleId").value;
		var frame = window.frames["custNoInfo"];// document.custNoInfo;
		frame.location.href = "cotelements.do?method=loadCustNoInfo"
				+ "&eleId=" + eleId;
	}

	// 加载主样品的子货号
	function loadChildInfo() {
		var eleId = $("eleId").value;
		var frame = window.frames["childInfo"];
		frame.location.href = "cotelements.do?method=loadChildInfo"
				+ "&mainId=" + $('eId').value;
	}

	// 加载主样品的厂家报价历史
	function loadPriceFacInfo() {
		var eleId = $("eleId").value;
		var frame = window.frames["priceFacInfo"];// document.priceFacInfo;
		frame.location.href = "cotpricefac.do?method=query" + "&mainId="
				+ $('eId').value;
	}

	// 加载主样品的配件信息
	function loadFittingInfo() {
		var eleId = $("eleId").value;
		var frame = window.frames["eleFittingInfo"]// document.eleFittingInfo;
		frame.location.href = "cotelements.do?method=loadFittingInfo"
				+ "&Flag=parent&mainId=" + $('eId').value + "&eleId="
				+ $('eleId').value;
	}
	// 加载主样品的用料管理
	function loadMaterialInfo() {
		var eleId = $("eleId").value;
		var frame = window.frames["eleMaterialInfo"]// document.eleFittingInfo;
		frame.location.href = "cotelements.do?method=loadMaterialInfo"
				+ "&Flag=parent&mainId=" + $('eId').value + "&eleId="
				+ $('eleId').value;
	}

	// 加载主样品的成本信息
	function loadElePrice() {
		var frame = window.frames["elePriceFrame"];// document.elePriceFrame;
		frame.location.href = "coteleprice.do?method=query" + "&mainId="
				+ $('eId').value;
	}

	// 加载主样品的采购记录
	function loadOrderFacInfo() {
		// 获取注册软件版本
		var softVer;
		cotElementsService.getSoftVer(function(res) {
					softVer = res;
				})
		if (softVer == "sample") {
			Ext.MessageBox
					.alert('Message', "Sorry, you do not have Authority!");
			return;
		} else {
			// 设置权限为空
			var isSel = getPdmByOtherUrl("cotorderfac.do", "SEL");
			if (isSel == 0)// 没有查看报价记录权限
			{
				Ext.MessageBox.alert('Message',
						"Sorry, you do not have Authority!");
				return;
			} else {
				var eleId = $("eleId").value;
				var frame = window.frames["orderfacInfo"];
				frame.location.href = "cotelements.do?method=loadOrderFacInfo"
						+ "&eleId=" + eleId;
			}
		}
	}
	// 删除样品
	function delParent() {
		var list = new Array();
		list.push($('eId').value);
		Ext.MessageBox.confirm('Message',
				'are you sure to delete all of the articles?', function(btn) {
					if (btn == 'yes') {
						cotElementsService.deleteElements(list, function(res) {
									if (res) {
										Ext.Msg.alert("Message",
												"Deleted successfully");
										closeandreflashEC(true, 'eleGrid',
												false);
									} else {
										Ext.Msg.alert("Message",
												"Deleted failed");
									}
								})
					}
				});
	}

	// 通过外包装数计算毛净重和装箱数
	function calWandC(txt, newVal, oldVal) {
		var boxCbm = $('boxCbm').value;
		var boxWeigth = $('boxWeigth').value;
		if (newVal == "") {
			newVal = 0;
		}
		if (boxWeigth == "") {
			boxWeigth = 0;
		}
		if (boxCbm == 0) {
			boxCbm = 0.0001;
		}
		cotElementsService.getDefaultList(function(res) {
					var jin = boxWeigth * newVal / 1000;
					$('boxNetWeigth').value = jin.toFixed(3);
					var boxGrossWeigth = 0;
					if (res.length != 0) {
						if (res[0].grossNum != null) {
							var temp = jin + res[0].grossNum;
							boxGrossWeigth = temp.toFixed(3);
						}
					} else {
						boxGrossWeigth = jin.toFixed(3);
					}
					$('boxGrossWeigth').value = boxGrossWeigth;
				});
		queryService.getContainerCube(function(res) {
					$('box20Count').value = parseInt(res[0] / boxCbm) * newVal;
					$('box40Count').value = parseInt(res[1] / boxCbm) * newVal;
					$('box40hqCount').value = parseInt(res[2] / boxCbm)
							* newVal;
					$('box45Count').value = parseInt(res[3] / boxCbm) * newVal;
				});
		calPrice();

	}

	// 通过cbm和外箱数计算装箱数,获得20/40/40HQ/45的柜体积(默认24/54/68/86)
	function calBoxCount(cbm, boxObCount) {
		if (boxObCount == "" || isNaN(boxObCount)) {
			boxObCount = 0;
		}
		if (cbm == 0) {
			cbm = 0.0001;
		}
		queryService.getContainerCube(function(res) {
					$('box20Count').value = parseInt(res[0] / cbm) * boxObCount;
					$('box40Count').value = parseInt(res[1] / cbm) * boxObCount;
					$('box40hqCount').value = parseInt(res[2] / cbm)
							* boxObCount;
					$('box45Count').value = parseInt(res[3] / cbm) * boxObCount;
				});
	}

	// 通过cbm计算cuft
	function calCuft(cbm) {
		if (isNaN(cbm) || cbm == "" || cbm == 0) {
			cbm = 0.0001;
		}
		var s = parseFloat(cbm) * 35.315;
		$('boxCuft').value = s.toFixed("3");
		calBoxCount($('boxCbm').value, $('boxObCount').value);
	}

	// 通过cuft计算cbm
	function calCbm(cuft) {
		if (isNaN(cuft)) {
			cuft = 0;
		}
		var boxCbm = (cuft / 35.315).toFixed(cbmNum);
		$('boxCbm').value = boxCbm;
		calBoxCount($('boxCbm').value, $('boxObCount').value);
	}

	// 比对柜最大装箱重量
	function getMaxBoxCount() {
		var gross = $('boxGrossWeigth').value;
		if (isNaN(gross)) {
			Ext.MessageBox.alert('Message',
					'Please enter the gross weight (Must be a numeric type)!')
			$('boxGrossWeigth').focus();
			return;
		} else {
			queryService.getContainerWeigh(function(res) {
						var boxObCount = $('boxObCount').value;
						if (isNaN(boxObCount)) {
							boxObCount = 0;
						}
						var max20 = parseInt((res[0] / gross) * boxObCount);
						var max40 = parseInt((res[1] / gross) * boxObCount);
						var max40hq = parseInt((res[2] / gross) * boxObCount);
						var max45 = parseInt((res[3] / gross) * boxObCount);

						if ($('box20Count').value != ''
								&& $('box20Count').value > max20) {
							$('box20Count').value = max20;
						}
						if ($('box40Count').value != ''
								&& $('box40Count').value > max40) {
							$('box40Count').value = max40;
						}
						if ($('box40hqCount').value != ''
								&& $('box40hqCount').value > max40hq) {
							$('box40hqCount').value = max40hq;
						}
						if ($('box45Count').value != ''
								&& $('box45Count').value > max45) {
							$('box45Count').value = max45;
						}
					});
		}
	}

	// 页面加载调用
	function initForm() {
		// 1.判断查看包装信息权限
		var isSelBox = getPopedomByOpType("cotelements.do", "BOX");
		if (isSelBox == 0) {
			Ext.getCmp("boxFs").hide();
			Ext.getCmp("otherFs").hide();
		}

		// 2.判断用户是否有权限操作生产价信息
		var isSelFac = getPdmByOtherUrl("cotpricefac.do", "SEL");
		if (isSelFac == 0) {
			Ext.getCmp("priceFacPnl").hide();
			Ext.getCmp("facCurBoxPnl").hide();
		} else {
			var isModFac = getPdmByOtherUrl("cotpricefac.do", "MOD");
			if (isModFac == 0) {
				$('priceFac').disabled = true;
				facCurBox.setDisabled(true);
			}
		}

		// 3.判断用户是否有权限操作外销价信息
		var isSelOut = getPdmByOtherUrl("cotpriceout.do", "SEL");
		if (isSelOut == 0) {
			Ext.getCmp("priceOutPnl").hide();
			Ext.getCmp("outCurBoxPnl").hide();
		} else {
			var isModOut = getPdmByOtherUrl("cotpriceout.do", "MOD");
			if (isModOut == 0) {
				$('priceOut').disabled = true;
				outCurBox.setDisabled(true);
			}
		}

		// 4.判断操作图片权限
		var isModPic = getPdmByOtherUrl("cotpicture.do", "MOD");
		if (isModPic == 0) {
			Ext.getCmp("upmod").hide();
		}
		var isDelPic = getPdmByOtherUrl("cotpicture.do", "DEL");
		if (isDelPic == 0) {
			Ext.getCmp("updel").hide();
		}

		var id = $("eId").value;

		cotElementsService.getEleById(parseInt(id), function(res) {
			if (res != null) {
				oldElement = res;
				DWRUtil.setValues(res);
				
				//是否锁定barcode
				if(res.lockBar==1){
					Ext.getCmp('lockBarcode').setIconClass('lock');
				}else{
					Ext.getCmp('lockBarcode').setIconClass('unlock');
				}
				// Ext.getCmp("addTime").setValue(res.eleAddTime);
				Ext.getCmp("eleAddTime").setValue(res.eleAddTime);
				var isSelPic = getPopedomByOpType("cotpicture.do", "SEL");
				if (isSelPic == 0) {// 没有查看图片信息权限
					$('picPath').src = "common/images/noElePicSel.png";
				} else {
					var rdm = Math.round(Math.random() * 10000);
					$('picPath').src = './showPicture.action?flag=ele&elementId='
							+ id + '&tmp=' + rdm;

				}
				// typeLvBox.bindPageValue("CotTypeLv1", "id",
				// res.eleTypeidLv1);
				// typeLv2Box.bindPageValue("CotTypeLv2", "id",
				// res.eleTypeidLv2);
				typeBox.bindPageValue("CotTypeLv1", "id", res.eleTypeidLv1);
				type2Box.bindPageValue("CotTypeLv2", "id", res.eleTypeidLv2);
				type3Box.bindPageValue("CotTypeLv3", "id", res.eleTypeidLv3);

				facCurBox.bindValue(res.priceFacUint);
				outCurBox.bindValue(res.priceOutUint);
				eleFlagComb.setValue(res.eleFlag);
				boxIbTypeIdBox.bindValue(res.boxIbTypeId);
				boxMbTypeIdBox.bindValue(res.boxMbTypeId);
				boxObTypeIdBox.bindValue(res.boxObTypeId);
				boxPbTypeIdBox.bindValue(res.boxPbTypeId);
				inputGridTypeIdBox.bindValue(res.inputGridTypeId);
				boxTypeIdBox.bindPageValue("CotBoxType", "id", res.boxTypeId);
				eleHsidBox.bindPageValue("CotEleOther", "id", res.eleHsid);
				facBox.bindPageValue("CotFactory", "id", res.factoryId);
				// empBox.bindPageValue("CotEmps", "empsName",
				// res.eleForPerson);
				// empBox.setValue(res.eleForPerson);
				// 修改时间
				Ext.getCmp("updateTime").setValue(res.updateTime);
			} else {
				closeandreflashEC('true', 'eleGrid', false);
			}
		});
		cotElementsService.getDefaultList(function(res) {
					if (res.length != 0) {
						// 设置是否自动生成包装尺寸标识
						if (res[0].sizeFollow != null) {
							sizeFollow = res[0].sizeFollow;
						}
					}
				});

		// DWREngine.setAsync(true);
	}
	initForm();
	unmask();
	window.onbeforeunload = function() {
		window.opener = null;
		CollectGarbage();
	}

	// 根据单号配置自动生成货号
	function setArtNo() {
		var byId = $('eleTypeidLv1').value;
		var myId = $('eleTypeidLv2').value;
		var syId = $('eleTypeidLv3').value;
		var facId = $('factoryId').value;
		if (byId == '' || myId == '' || syId == '' || facId == '') {
			Ext.MessageBox
					.alert('Message',
							'Please choose Dep.of PRODUCT and Group and Category and Supplier!')
		} else {
			cotSeqService.getEleNo(byId, myId, syId, facId, function(ps) {
						$('eleId').value = ps;
					});
		}
	}

});
// 显示海关编码
function showSH() {
	var form = new Ext.FormPanel({
				padding : 5,
				id : "addFormId",
				formId : "addForm",
				labelWidth : 90,
				labelAlign : "right",
				minWidth : "",
				// collapsible : true,
				frame : true,
				border : false,
				items : [{
							xtype : "textfield",
							id : 'hscode',
							name : 'hscode',
							fieldLabel : "HS海关编码*",
							anchor : "95%",
							allowBlank : false,
							maxLength : 100,
							tabIndex : 1,
							readOnly : true,
							blankText : "HS海关编码不能为空"
						}, {
							xtype : "textfield",
							id : 'cnName',
							name : 'cnName',
							fieldLabel : "报关中文名称*",
							anchor : "95%",
							allowBlank : false,
							maxLength : 100,
							tabIndex : 2,
							readOnly : true,
							blankText : "报关中文名称不能为空"
						}, {
							xtype : "textfield",
							id : 'enName',
							name : 'enName',
							fieldLabel : "报关英文名称*",
							anchor : "95%",
							maxLength : 100,
							tabIndex : 3,
							readOnly : true
						}, {
							id : 'taxRate',
							name : 'taxRate',
							baseCls : 'x-plain',
							fieldLabel : "Tax rate%",
							tabIndex : 4,
							maxValue : 99.9,
							decimalPrecision : 1,
							xtype : "numberfield",
							anchor : '95%',
							readOnly : true
						}, {
							xtype : "textarea",
							id : 'remark',
							name : 'remark',
							fieldLabel : "Remark",
							anchor : "95%",
							maxLength : 500,
							tabIndex : 5,
							readOnly : true
						}, {
							xtype : 'hidden',
							id : 'id',
							name : 'id'
						}]
			});

	var id = $('eleHsid').value;
	var wind = new Ext.Window({
				title : 'Customs Code List',
				width : 300,
				height : 300,
				frame : true,
				layout : 'fit',
				items : [form],
				buttonAlign : 'center',
				buttons : [{
							text : 'Yes',
							handler : function() {
								wind.close();
							}
						}]
			});
	DWREngine.setAsync(false);
	if (id) {
		cotEleOtherService.getObjById(id, function(rs) {
					if (rs) {
						var mainForm = Ext.getCmp('addFormId').form;
						mainForm.findField('hscode').setValue(rs.hscode);
						mainForm.findField('cnName').setValue(rs.cnName);
						mainForm.findField('enName').setValue(rs.enName);
						mainForm.findField('taxRate').setValue(rs.taxRate);
						mainForm.findField('remark').setValue(rs.remark);
					}
				})
		DWREngine.setAsync(true);
	}
	wind.show();
}

// 设置外销价
function setPriceOut() {
	var priceFac = $('priceFac').value;
	if (priceFac == '' || priceFac == null) {
		priceFac = 0;
	}
	var tuiLv = $('tuiLv').value;
	var priceFacUnit = $('priceFacUint').value;
	var priceOutUnit = $('priceOutUint').value;
	if (priceFacUnit == '' || priceOutUnit == '') {
		Ext.MessageBox.alert('Message',
				'Please Choose PurChase Currency and Sales Currency!');
		return;
	}
	var liRun = $('liRun').value;
	if (liRun == '' || liRun == null) {
		liRun = 0;
	}
	var cbm = $('boxCbm').value;
	if (cbm == '' || cbm == null) {
		cbm = 0;
		// Ext.Msg.alert("提示信息", '请填写CBM，且不能为零！');
		// return;
	}
	// azan添加
	var boxObCount = $('boxObCount').value;
	if (boxObCount == '' || isNaN(boxObCount)) {
		boxObCount = 0;
	}
	DWREngine.setAsync(false);
	cotElementsService.getPriceOut(parseFloat(priceFac), 0,
			parseInt(priceFacUnit), parseInt(priceOutUnit), liRun, 0, 0,
			function(res) {
				if (res != null) {
					$('priceOut').value = res.toFixed("2");
				}
			});
	DWREngine.setAsync(true);
	// }
}
// 计算利润
// 计算利润
function setLiRun() {
	// 如果采购价为空,不算利润率
	var fac = Ext.getCmp('priceFac').getValue();
	var outFac = Ext.getCmp('priceOut').getValue();
	if (!Ext.isEmpty(fac) && !Ext.getCmp('outFac')) {
		// 生产价币种
		var priceFacUnit = $('priceFacUint').value;
		// 外销价币种
		var priceOutUnit = $('priceOutUint').value;
		var facCurRate = facCurRateMap[priceFacUnit];
		var outCurRate = outCurRateMap[priceOutUnit];
		var priceFac = $('priceFac').value * facCurRate;
		var out = $('priceOut').value * outCurRate;
		if (out == 0) {
			$('liRun').value = 0;
		} else {
			var liRun = (out / priceFac - 1) * 100;
			$('liRun').value = liRun.toFixed("2");
		}

	}

}
// function setLiRun(){
// //生产价币种
// var priceFacUnit = $('priceFacUint').value;
// //外销价币种
// var priceOutUnit = $('priceOutUint').value;
// var facCurRate =facCurRateMap[priceFacUnit];
// var outCurRate = outCurRateMap[priceOutUnit];
// var priceFac = $('priceFac').value*facCurRate;
// var out = $('priceOut').value*outCurRate;
// if(out==0){
// $('liRun').value=0;
// }else{
// var liRun =(out/priceFac -1)*100;
// $('liRun').value= liRun.toFixed("2");
// }
// }
