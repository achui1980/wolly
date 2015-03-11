var facNum = getDeNum("facPrecision");
var outNum = getDeNum("outPrecision");
var cbmNum = getDeNum("cbmPrecision");
Ext.onReady(function() {
	// 材质
	var typeLvBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "Material",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 2,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

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
				emptyText : 'Choose',
				editable : true,
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
				hidden:hideFac,
				hideLabel:hideFac,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				tabIndex : 7,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
//				listeners : {
//					"select" : setFactoryNo
//				}
			});

	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Categories",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
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

	// 海关编码
	var eleHsidBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "HS code",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "cnName",
				emptyText : 'Choose',
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
							cotElementsService.getEleTax(rec.data.id,function(res){
								if(res!=null){
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
				tabIndex : 22
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
				tabIndex : 24
			});

	// 计算包材价格
	var calPrice = function(pp) {
		var ele = DWRUtil.getValues('cotElementsForm');
		var elements = new CotElementsNew();
		for (var p in elements) {
			if (p != 'eleProTime') {
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
		//点击Choose
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
				fieldLabel : "Package",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 36,
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
		editable : true,
		allowBlank : true,
		displayField : "value",
		emptyText : 'Choose',
		autoLoad : true,
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

	// 内包装类型
	var boxIbTypeIdBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotBoxPacking&type=IB",
		cmpId : 'boxIbTypeId',
		fieldLabel : "/",
		labelSeparator : " ",
		valueField : "id",
		editable : true,
		autoLoad : true,
		allowBlank : true,
		displayField : "value",
		emptyText : 'Choose',
		anchor : "100%",
		tabIndex : 58,
		selectOnFocus : true,
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
		editable : true,
		autoLoad : true,
		allowBlank : true,
		displayField : "value",
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
		autoLoad : true,
		allowBlank : true,
		displayField : "value",
		emptyText : 'Choose',
		anchor : "100%",
		tabIndex : 76,
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
		allowBlank : true,
		editable : true,
		displayField : "value",
		emptyText : 'Choose',
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
		var str="";
		if($('boxL').value!='' && $('boxL').value!='0'){
			str+=$('boxL').value + "x";
		}
		if($('boxW').value!='' && $('boxW').value!='0'){
			str+=$('boxW').value + "x";
		}
		if($('boxH').value!='' && $('boxH').value!='0'){
			str+=$('boxH').value+"cm";
			$('eleInchDesc').value =str;
		}else{
			if(str==""){
				$('eleInchDesc').value ="";
			}else{
				$('eleInchDesc').value =str.substring(0,str.length-1)+"cm";
			}
		}
	};

	// 样品默认配置,是否自动生成产品包装尺寸
	var sizeFollow = 0;

	// 计算英寸
	var calInch = function(txt, newVal, oldVal) {
//		var inch = (newVal / 2.54).toFixed("2");
//		var needId = txt.name + "Inch";
//		$(needId).value = inch;
		changeSizeTxt();
//		if (sizeFollow == 1) {
//			// 产品包装尺寸跟着变化
//			if (needId == 'boxLInch') {
//				$('boxPbL').value = $('boxL').value;
//				$('boxPbLInch').value = $('boxLInch').value;
//			}
//			if (needId == 'boxWInch') {
//				$('boxPbW').value = $('boxW').value;
//				$('boxPbWInch').value = $('boxWInch').value;
//			}
//			if (needId == 'boxHInch') {
//				$('boxPbH').value = $('boxH').value;
//				$('boxPbHInch').value = $('boxHInch').value;
//			}
//		}
//		calPrice();
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
		cotElementsService.getDefaultList(function(res) {
					var gross = newVal * boxObCount / 1000;
					if (res.length != 0) {
						if (res[0].grossNum != null) {
							gross = gross + res[0].grossNum;
						}
					}
					$('boxNetWeigth').value = newVal * boxObCount / 1000;
					$('boxGrossWeigth').value = gross;
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
		var model = $('modelEle').value;
		cotElementsService.getEleByEleId(model, function(res) {
					if (res == null) {
						Ext.MessageBox.alert("Message", 'The Art No. does not exist, please re-enter!');
					} else {
						var temp = $('eleId').value;
						var picSrc = $('picPath').src;

						myForm.getForm().setValues(res);

						typeLvBox.bindPageValue("CotTypeLv1", "id",
								res.eleTypeidLv1);
						boxTypeIdBox.bindPageValue("CotBoxType", "id",
								res.boxTypeId);
						facBox.bindPageValue("CotFactory", "id", res.factoryId);

						$('eleId').value = temp;
						$('picPath').src = picSrc;
						modlPanel.hide();
					}
				});
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
		$('priceFac').value = packingPrice;
	}
	var calPriceByPakingPrice = function() {
		var packingPrice = 0.0;
		if ($('packingPrice').value != null && $('packingPrice').value != '') {
			packingPrice = $('packingPrice').value;
		}
		$('priceFac').value = packingPrice;
	}

	// 添加
	var add = function() {
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

		//主样品表单
		var ele = DWRUtil.getValues("cotElementsForm");
		if (oldElement == null) {
			oldElement = ele;
		} else {
			for (var p in ele) {
				oldElement[p] = ele[p];
			}
		}
		oldElement.eleAddTime = null;
		oldElement.eleProTime = null;
		var map = {};
		map.eleProTime = $('eleProTime').value;//开发时间
		map.type = $('parentType').value;//样品转换后的类型
		map.newEleId = $("newEleId").value;//拷贝的货号

		//判断上传路径是否含有'sampleImg',如果有就是更改过
		var filePath = $('picPath').src;
		var s = filePath.indexOf('sampleImg');
		if (s > -1) {
			var basePath = $('basePath').value;
			var s = filePath.indexOf(basePath);
			var fPath = filePath.substring(s + basePath.length, 111111);
			map.pic = decodeURI(fPath);//有更改过图片
		} else {
			map.pic = ''//没有更改过图片
			var d = filePath.indexOf('showPicture.action');
			if (d < 0) {
				map.newEleId = "hkdlfenfof";//找不到货号后会用"没图片"的代替
			}
		}
		oldElement.boxIbType = boxIbTypeIdBox.getRawValue();
		oldElement.boxMbType = boxMbTypeIdBox.getRawValue();
		oldElement.boxObType = boxObTypeIdBox.getRawValue();
		oldElement.boxPbType = boxPbTypeIdBox.getRawValue();

		Ext.MessageBox.confirm('Message', 'Do you want to save?', function(btn) {
					if (btn == 'yes') {
						DWREngine.setAsync(false);
						mask("Saving... ...");
						DWREngine.setAsync(true);
						//更新(将样品对象转为明细对象)
						queryService.modifyElementsToDetail(oldElement, map,
								function(res) {
									if (res != null) {
										self.opener.newEleAdd(res);
										self.close();
									} else {
										Ext.MessageBox.alert("Message", 'Add failed!');
									}
									unmask();
								})
					}
				});

	}

	var isExist = true;
	// 主表单
	var myForm = new Ext.form.FormPanel({
		formId : "cotElementsForm",
		labelWidth : 60,
		labelAlign : "right",
		layout : "form",
		width : "100%",
		padding : "5px",
		frame : true,
		buttonAlign : 'center',
		buttons : [{
					text : "Save",
					cls : "SYSOP_ADD",
					iconCls : "page_add",
					handler : add
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					handler : function() {
						window.close();
					}
				}],
		items : [{
			xtype : "panel",
			layout : "border",
			width : "100%",
			height : 266,
			items : [{
				xtype : "fieldset",
				region : "center",
				title : "Basic information",
				layout : "column",
				//frame:true,
				padding : "5",
				buttons : [{
							text : '',
							hidden : true
						}],
				items : [{
					xtype : "panel",
					columnWidth : 0.46,
					layout : "column",
					items : [{
						xtype : "panel",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 60,
						buttonAlign : "left",
						labelAlign : "right",
						items : [{
							xtype : "textfield",
							fieldLabel : "<font color=red>Art No.</font>",
							anchor : "100%",
							tabIndex : 1,
							allowBlank : false,
							blankText : "Please enter the Art No.",
							maxLength : 100,
							maxLengthText : "",
							readOnly : true,
							name : "eleId",
							id : "eleId",
							invalidText : "Art No. already exists, please re-enter!",
							validationEvent : 'change',
							validator : function(thisText) {
								if (thisText != '') {
									cotElementsService.findIsExistEleId(
											thisText, 0, function(res) {
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
						xtype : "panel",
						columnWidth : 0.5,
						layout : "form",
						items : [typeLvBox]
					}, {
						xtype : "panel",
						columnWidth : 1,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "Name",
									anchor : "100%",
									allowBlank : true,
									tabIndex : 6,
									id : "eleNameEn",
									name : "eleNameEn",
									maxLength : 200
								}, {
									xtype : "textfield",
									fieldLabel : "Name-cn",
									anchor : "100%",
									allowBlank : true,
									tabIndex : 9,
									id : "eleName",
									name : "eleName",
									maxLength : 200
								}]
					}, {
						xtype : "panel",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "datefield",
									fieldLabel : "Date",
									anchor : "100%",
									value : new Date(),
									allowBlank : true,
									format : "Y-m-d",
									tabIndex : 12,
									id : "eleProTime",
									name : "eleProTime"
								}, typeLv2Box]
					}, {
						xtype : "panel",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "Obje",
									anchor : "100%",
									allowBlank : true,
									tabIndex : 13,
									id : "eleForPerson",
									name : "eleForPerson",
									maxLength : 50
								}, {
									xtype : "textfield",
									fieldLabel : "Year",
									anchor : "100%",
									allowBlank : true,
									tabIndex : 17,
									id : "eleTypenameLv2",
									name : "eleTypenameLv2",
									maxLength : 100
								}]
					}, {
						xtype : "panel",
						columnWidth : 0.33,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "P.O price",
									anchor : "100%",
									allowBlank : true,
									decimalPrecision : 3,
									tabIndex : 21,
									id : "priceFac",
									name : "priceFac",
									maxLength : 99999999.99
								}]
					}, {
						xtype : "panel",
						columnWidth : 0.17,
						layout : "form",
						items : [facCurBox]
					}, {
						xtype : "panel",
						columnWidth : 0.33,
						layout : "form",
						items : [{
							xtype : "numberfield",
							fieldLabel : "<a onclick=setPriceOut() ext:qwidth=100 ext:qtip='点击后根据默认配置的公式计算外销价' style='color:blue;cursor: hand;text-decoration: underline'>外销价</a>",
							anchor : "100%",
							allowBlank : true,
							decimalPrecision : 3,
							tabIndex : 23,
							id : "priceOut",
							name : "priceOut",
							maxLength : 10000000
						}]
					}, {
						xtype : "panel",
						columnWidth : 0.17,
						layout : "form",
						items : [outCurBox]
					}, {
						xtype : "panel",
						columnWidth : 1,
						layout : "form",
						items : [{
									xtype : "textarea",
									fieldLabel : "Description",
									anchor : "100%",
									height : 40,
									allowBlank : true,
									tabIndex : 28,
									id : "eleDesc",
									name : "eleDesc",
									maxLength : 500
								}]
					}]
				}, {
					xtype : "panel",
					columnWidth : 0.54,
					layout : "column",
					items : [{
								xtype : "panel",
								columnWidth : 0.46,
								layout : "form",
								items : [{
											xtype : "textfield",
											fieldLabel : "Unit",
											anchor : "100%",
											allowBlank : true,
											tabIndex : 3,
											maxLength : 20,
											id : "eleUnit",
											name : "eleUnit"
										}, facBox, {
											xtype : "textfield",
											fieldLabel : "Source",
											anchor : "100%",
											allowBlank : true,
											tabIndex : 10,
											id : "eleFrom",
											name : "eleFrom",
											maxLength : 100
										}, {
											xtype : "textfield",
											fieldLabel : "Level",
											anchor : "100%",
											allowBlank : true,
											tabIndex : 14,
											id : "eleGrade",
											name : "eleGrade",
											maxLength : 30
										}, eleHsidBox, {
											xtype : "textfield",
											fieldLabel : "Barcode",
											anchor : "100%",
											allowBlank : true,
											tabIndex : 25,
											id : "barcode",
											name : "barcode",
											maxLength : 30
										}]
							}, {
								xtype : "panel",
								columnWidth : 0.54,
								layout : "column",
								items : [{
											xtype : "panel",

											columnWidth : 0.65,
											layout : "form",
											items : [eleFlagComb]
										}, {
											xtype : "panel",
											columnWidth : 0.35,
											layout : "form",
											labelWidth : 35,
											items : [{
														xtype : "numberfield",
														fieldLabel : "套数",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 0,
														tabIndex : 5,
														id : "eleUnitNum",
														name : "eleUnitNum",
														maxLength : 99999999999
													}]
										}, {
											xtype : "panel",
											columnWidth : 1,
											layout : "form",
											items : [{
														xtype : "textfield",
														fieldLabel : "Supplier Art No.",
														anchor : "100%",
														allowBlank : true,
														tabIndex : 8,
														id : "factoryNo",
														name : "factoryNo",
														maxLength : 200
													}, {
														xtype : "textfield",
														fieldLabel : "Design",
														anchor : "100%",
														allowBlank : true,
														tabIndex : 11,
														id : "eleCol",
														name : "eleCol",
														maxLength : 50
													}, {
														xtype : "numberfield",
														fieldLabel : "MOQ",
														anchor : "100%",
														decimalPrecision : 0,
														tabIndex : 15,
														id : "eleMod",
														name : "eleMod",
														maxLength : 99999999999
													}]
										}, {
											xtype : "panel",
											columnWidth : 0.5,
											layout : "form",
											items : [{
														xtype : "numberfield",
														fieldLabel : "/G",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 19,
														id : "boxWeigth",
														name : "boxWeigth",
														maxLength : 999999.99,
														listeners : {
															"change" : calWeight
														}
													}, {
														xtype : "numberfield",
														fieldLabel : "Tax rate%",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 26,
														id : "tuiLv",
														name : "tuiLv",
														maxLength : 99999999.99
													}]
										}, {
											xtype : "panel",
											columnWidth : 0.5,
											layout : "form",
											hideCollapseTool : false,
											labelWidth : 60,
											items : [{
														xtype : "numberfield",
														fieldLabel : "Volume",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 20,
														id : "cube",
														name : "cube",
														maxLength : 999999.99
													}, {
														xtype : "numberfield",
														fieldLabel : "Margin%",
														anchor : "100%",
														allowBlank : true,
														decimalPrecision : 2,
														tabIndex : 27,
														id : "liRun",
														name : "liRun",
														maxLength : 999999.99
													}]
										}]
							}, {
								xtype : "panel",
								columnWidth : 1,
								layout : "form",
								height : "",
								items : [{
											xtype : "textarea",
											fieldLabel : "Remark",
											anchor : "100%",
											height : 40,
											allowBlank : true,
											tabIndex : 29,
											id : "eleRemark",
											name : "eleRemark",
											maxLength : 500
										},{
											xtype:'hidden',
											id : "boxTDesc",
											name : "boxTDesc"
										},{
											xtype:'hidden',
											id : "boxBDesc",
											name : "boxBDesc"
										}]
							}]
				}]
			}, {
				xtype : "fieldset",
				title : "Picture Info",
				region : "east",
				width : "23%",
				//frame:true,
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
					width : 140,
					buttonAlign : "center",
					html : '<div align="center" style="width: 135px; height: 132px;">'
							+ '<img src="common/images/zwtp.png" id="picPath" name="picPath"'
							+ 'onload="javascript:DrawImage(this,135,132)" onclick="showBigPicDiv(this)"/></div>',
					buttons : [{
								width : 60,
								text : "Update",
								id : "upmod",
								iconCls : "upload-icon",
								handler : showUploadPanel
							}, {
								width : 60,
								text : "Del",
								id : "updel",
								iconCls : "upload-icon-del",
								handler : delPic
							}]
				}]
			}]
		}, {
			xtype : "panel",
			layout : "border",
			width : "100%",
			height : 306,
			items : [{
				xtype : "fieldset",
				title : "Packaging Information",
				region : "center",
				layout : "column",
				padding : "5",
				buttons : [{
							text : '',
							hidden : true
						}],
				items : [{
							xtype : "panel",
							columnWidth : 0.63,
							layout : "column",
							items : [{
										xtype : "panel",
										columnWidth : 0.26,
										layout : "form",
										labelWidth : 71,
										items : [{
													xtype : "numberfield",
													fieldLabel : "Size(cm)",
													anchor : "100%",
													allowBlank : true,
													decimalPrecision : 2,
													tabIndex : 30,
													id : "boxL",
													name : "boxL",
													maxLength : 10000,
													listeners : {
														"change" : calInch
													}
												}, {
													xtype : "numberfield",
													fieldLabel : "Packing Size(cm)",
													anchor : "100%",
													allowBlank : true,
													decimalPrecision : 2,
													tabIndex : 38,
													id : "boxPbL",
													name : "boxPbL",
													maxLength : 999.99,
													listeners : {
														"change" : calInch
													}
												}, {
													xtype : "numberfield",
													fieldLabel : "Inner box size(cm)",
													anchor : "100%",
													allowBlank : true,
													decimalPrecision : 2,
													tabIndex : 47,
													id : "boxIbL",
													name : "boxIbL",
													maxLength : 10000,
													listeners : {
														"change" : calInch
													}
												}, {
													xtype : "numberfield",
													fieldLabel : "中盒包装CM",
													anchor : "100%",
													allowBlank : true,
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
													fieldLabel : "Carton size(cm)",
													anchor : "100%",
													allowBlank : true,
													decimalPrecision : 2,
													tabIndex : 69,
													id : "boxObL",
													name : "boxObL",
													maxLength : 10000,
													listeners : {
														"change" : calOSize
													}
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.12,
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
													tabIndex : 31,
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
													tabIndex : 39,
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
													tabIndex : 48,
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
													decimalPrecision : 2,
													tabIndex : 70,
													id : "boxObW",
													name : "boxObW",
													maxLength : 10000,
													listeners : {
														"change" : calOSize
													}
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.12,
										layout : "form",
										labelWidth : 4,
										items : [{
													xtype : "numberfield",
													fieldLabel : "x",
													anchor : "100%",
													labelSeparator : " ",
													allowBlank : true,
													decimalPrecision : 2,
													tabIndex : 32,
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
													tabIndex : 40,
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
													tabIndex : 49,
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
													tabIndex : 62,
													id : "boxMbH",
													name : "boxMbH",
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
													tabIndex : 71,
													id : "boxObH",
													name : "boxObH",
													maxLength : 10000,
													listeners : {
														"change" : calOSize
													}
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.26,
										layout : "form",
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
										xtype : "panel",
										columnWidth : 0.5,
										layout : "form",
										items : [{
													xtype : "textarea",
													fieldLabel : "Size(cm)",
													anchor : "100%",
													height : 50,
													allowBlank : true,
													tabIndex : 78,
													id : "eleSizeDesc",
													name : "eleSizeDesc",
													maxLength : 500
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.5,
										layout : "form",
										labelWidth : 60,
										items : [{
													xtype : "textarea",
													fieldLabel : "Size(inch)",
													anchor : "100%",
													height : 50,
													allowBlank : true,
													tabIndex : 79,
													id : "eleInchDesc",
													name : "eleInchDesc",
													maxLength : 500
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.5,
										layout : "form",
										items : [{
													xtype : "textarea",
													fieldLabel : "Package(cn)",
													anchor : "100%",
													height : 50,
													allowBlank : true,
													tabIndex : 84,
													id : "boxRemarkCn",
													name : "boxRemarkCn",
													maxLength : 500
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.5,
										layout : "form",
										items : [{
													xtype : "textarea",
													fieldLabel : "Package(en)",
													anchor : "100%",
													height : 50,
													allowBlank : true,
													tabIndex : 84,
													id : "boxRemark",
													name : "boxRemark",
													maxLength : 500
												}]
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.37,
							layout : "column",
							items : [{
										xtype : "panel",

										columnWidth : 0.71,
										layout : "form",
										items : [boxTypeIdBox]
									}, {
										xtype : "panel",

										columnWidth : 0.29,
										layout : "form",
										labelWidth : 35,
										items : [{
											xtype : "numberfield",
											fieldLabel : "Price",
											anchor : "100%",
											allowBlank : true,
											decimalPrecision : 3,
											tabIndex : 37,
											id : "packingPrice",
											name : "packingPrice",
											maxLength : 10000000,
											listeners : {
												"change" : calPriceByPakingPrice
											}
										}]
									}, {
										xtype : "panel",
										columnWidth : 0.35,
										layout : "form",
										items : [{
													xtype : "numberfield",
													fieldLabel : "产品装数",
													anchor : "100%",
													allowBlank : true,
													decimalPrecision : 0,
													tabIndex : 44,
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
													fieldLabel : "内盒装数",
													anchor : "100%",
													allowBlank : true,
													decimalPrecision : 0,
													tabIndex : 57,
													id : "boxIbCount",
													name : "boxIbCount",
													maxLength : 99999999,
													listeners : {
														"change" : function() {
															calPrice();
														}
													}
												}, {
													xtype : "numberfield",
													fieldLabel : "中盒装数",
													anchor : "100%",
													allowBlank : true,
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
													xtype : "numberfield",
													fieldLabel : "外箱装数",
													anchor : "100%",
													allowBlank : true,
													decimalPrecision : 0,
													tabIndex : 75,
													id : "boxObCount",
													name : "boxObCount",
													maxLength : 99999999,
													listeners : {
														'change' : calWandC
													}
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.36,
										layout : "form",
										labelWidth : 4,
										items : [boxPbTypeIdBox,
												boxIbTypeIdBox, boxMbTypeIdBox,
												boxObTypeIdBox]
									}, {
										xtype : "panel",
										columnWidth : 0.29,
										layout : "form",
										labelWidth : 35,

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
										xtype : "panel",
										columnWidth : 0.5,
										layout : "form",
										items : [{
											xtype : "numberfield",
											fieldLabel : "CBM",
											anchor : "100%",
											allowBlank : true,
											decimalPrecision : 3,
											tabIndex : 80,
											id : "boxCbm",
											name : "boxCbm",
											maxLength : 1000000,
											listeners : {
												"change" : function(txt,
														newVal, oldVal) {
													calCuft(newVal);
												}
											}
										}, {
											xtype : "numberfield",
											fieldLabel : "N.W./KG",
											anchor : "100%",
											allowBlank : true,
											decimalPrecision : 3,
											tabIndex : 82,
											id : "boxNetWeigth",
											name : "boxNetWeigth",
											maxLength : 999999.99
										}]
									}, {
										xtype : "panel",
										columnWidth : 0.5,
										layout : "form",
										items : [{
											xtype : "numberfield",
											fieldLabel : "CU'FT",
											anchor : "100%",
											allowBlank : true,
											decimalPrecision : 3,
											tabIndex : 81,
											id : "boxCuft",
											name : "boxCuft",
											maxLength : 1000000,
											listeners : {
												"change" : function(txt,
														newVal, oldVal) {
													calCbm(newVal);
												}
											}
										}, {
											xtype : "numberfield",
											fieldLabel : "G.W./KG",
											anchor : "100%",
											allowBlank : true,
											decimalPrecision : 3,
											tabIndex : 83,
											id : "boxGrossWeigth",
											name : "boxGrossWeigth",
											maxLength : 999999.99
										}]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.71,
										items : [inputGridTypeIdBox]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.29,
										labelWidth : 35,
										items : [{
											xtype : "numberfield",
											id : "inputGridPrice",
											name : "inputGridPrice",
											fieldLabel : "Price",
											tabIndex : 86,
											decimalPrecision : 3,
											maxLength : 99999999.99,
											anchor : "100%",
											listeners : {
												"change" : calPackPriceAndPriceFac
											}
										}]
									}, {
										xtype : "panel",
										title : "",
										layout : "column",
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
																	"change" : function() {
																		calPrice();
																	}
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
																	"change" : function() {
																		calPrice();
																	}
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
																	"change" : function() {
																		calPrice();
																	}
																}
															}]
												}]
									}]
						}]
			}, {
				xtype : "fieldset",
				title : "其他信息",
				region : "east",
				width : "23%",
				layout : "form",
				padding : "5",
				labelWidth : 70,
				buttonAlign : 'center',
				items : [{
							xtype : "numberfield",
							fieldLabel : "20\"装数",
							anchor : "100%",
							decimalPrecision : 0,
							allowBlank : true,
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "box20Count",
							name : "box20Count",
							maxLength : 10000000000000
						}, {
							xtype : "numberfield",
							fieldLabel : "40''",
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
							fieldLabel : "40HQ",
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
							fieldLabel : "45",
							anchor : "100%",
							allowBlank : true,
							disabled : true,
							disabledClass : 'combo-disabled',
							decimalPrecision : 0,
							id : "box45Count",
							name : "box45Count",
							maxLength : 100000000000000
						}, {
							xtype : "datefield",
							fieldLabel : "Create Date",
							value : new Date(),
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "addTime",
							name : "addTime",
							anchor : "100%",
							allowBlank : true,
							format : "Y-m-d"
						}],
				buttons : [{
					iconCls : "page_calculator",
					width : 70,
					text : "<span ext:qwidth=100 ext:qtip='According to the weight of the container can be loaded to calculate the number of packing'>weight</span>",
					handler : getMaxBoxCount
				}, {
					text : "<span ext:qwidth=100 ext:qtip='According to the maximum capacity of the container volume of the packing number'>CBM</span>",
					width : 70,
					iconCls : "page_calculator",
					handler : function() {
						calCuft($("boxCbm").value);
					}
				}]
			}]
		}]
	});
	
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
			Ext.MessageBox.alert('Message', 'Please enter the gross weight!')
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

	// 打开上传面板
	function showUploadPanel() {
		var win = new UploadWin({
					params : {
						pEId : null
					},
					waitMsg : "Uploading......",
					opAction : "insert",
					imgObj : $('picPath'),
					uploadType : "image",
					loadImgStream : false,
					uploadUrl : './uploadElePicture.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}
	// 删除图片
	function delPic() {
		$('picPath').src = "common/images/zwtp.png";
	}
	DWREngine.setAsync(false);
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [myForm]
			});
	DWREngine.setAsync(true);

	//全局样品变量
	var oldElement = null;
	function initForm() {
		//加载主样品表单
		var newEleId = $("newEleId").value;
		DWREngine.setAsync(false);
		cotElementsService.getEleByEleId(newEleId, function(res) {
			if (res != null) {
				oldElement = res;
				DWRUtil.setValues(res);
				Ext.getCmp("eleProTime").setValue(res.eleProTime);
				var isSelPic = getPopedomByOpType("cotpicture.do", "SEL");
				if (isSelPic == 0) {// 没有查看图片信息权限
					$('picPath').src = "common/images/noElePicSel.png";
				} else {
					var rdm = Math.round(Math.random() * 10000);
					$('picPath').src = './showPicture.action?flag=ele&elementId='
							+ res.id + '&tmp=' + rdm;

				}
				typeLvBox.bindPageValue("CotTypeLv1", "id", res.eleTypeidLv1);
				typeLv2Box.bindPageValue("CotTypeLv2", "id", res.eleTypeidLv2);
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
				$('eleId').value = $('wantEleId').value;
				//changeUnitNum();
				cotElementsService.getDefaultList(function(ak) {
							if (ak.length != 0) {
								//设置是否自动生成包装尺寸标识
								if (ak[0].sizeFollow != null) {
									sizeFollow = ak[0].sizeFollow;
								}
							}
						});
			} else {
				//初始化币种和材质和厂家的默认值
				cotElementsService.getDefaultList(function(res) {
					if (res.length != 0) {
						typeLvBox.bindPageValue("CotTypeLv1", "id",
								res[0].eleTypeId);
						facCurBox.bindValue(res[0].elePriceFacUnit);
						outCurBox.bindValue(res[0].elePriceOutUnit);
						if (res[0].eleUnit != null) {
							$('eleUnit').value = res[0].eleUnit;
						}
						if (res[0].eleFlag != null) {
							eleFlagComb.setValue(res[0].eleFlag);
						}
						if (res[0].tuiLv != null) {
							$('tuiLv').value = res[0].tuiLv;
						} else {
							$('tuiLv').value = 0;
						}
						if (res[0].priceProfit != null) {
							$('liRun').value = res[0].priceProfit;
						} else {
							$('liRun').value = 0;
						}
						if (res[0].boxIbCount != null) {
							$('boxIbCount').value = res[0].boxIbCount;
						} else {
							$('boxIbCount').value = 0;
						}
						if (res[0].boxMbCount != null) {
							$('boxMbCount').value = res[0].boxMbCount;
						} else {
							$('boxMbCount').value = 0;
						}
						if (res[0].boxObCount != null) {
							$('boxObCount').value = res[0].boxObCount;
						} else {
							$('boxObCount').value = 0;
						}
						if (res[0].boxPbCount != null) {
							$('boxPbCount').value = res[0].boxPbCount;
						} else {
							$('boxPbCount').value = 0;
						}
						boxIbTypeIdBox.bindValue(res[0].boxIbTypeId);
						boxMbTypeIdBox.bindValue(res[0].boxMbTypeId);
						boxObTypeIdBox.bindValue(res[0].boxObTypeId);
						boxTypeIdBox.bindPageValue("CotBoxType", "id",
								res[0].boxTypeId);
						boxPbTypeIdBox.bindValue(res[0].boxPbTypeId);
						inputGridTypeIdBox.bindValue(res.inputGridTypeId);
						if (res[0].eleFacId != null) {
							facBox.bindPageValue("CotFactory", "id",
									res[0].eleFacId);
							cotElementsService.getFactoryById(res[0].eleFacId,
									function(res) {
										// 设置该厂号
										if (res.factoryNo != null) {
											$('factoryNo').value = res.factoryNo;
										}
									})
						}
						if ($('eleFlag').value == 0) {
							$('eleUnitNum').value = 1;
							$('eleUnitNum').disabled = true;
						} else {
							$('eleUnitNum').disabled = false;
							$('eleUnitNum').value = res[0].eleUnitnum;
						}

						// 设置是否自动生成包装尺寸标识
						if (res[0].sizeFollow != null) {
							sizeFollow = res[0].sizeFollow;
						}
					}
					$('eleId').value = $('wantEleId').value;
					if ($('eleFlag').value == 0) {
						$('eleUnitNum').value = 1;
						$('eleUnitNum').disabled = true;
					}
				});
			}
		});
		DWREngine.setAsync(true);
	}
	initForm();
});
// 设置外销价
function setPriceOut() {
	var priceFac = $('priceFac').value;
	if (priceFac == '' || priceFac == null) {
		priceFac = 0;
	}
	var tuiLv = $('tuiLv').value;
	var priceFacUnit = $('priceFacUint').value;
	var priceOutUnit = $('priceOutUint').value;
	var liRun = $('liRun').value;
	var cbm = $('boxCbm').value;
	if (cbm == '' || cbm == null) {
		cbm=0;
//		alert('请填写CBM，且不能为零！');
//		return;
	}
	// azan添加
	var boxObCount = $('boxObCount').value;
	if (boxObCount == '' || isNaN(boxObCount)) {
		boxObCount = 0;
	}
	DWREngine.setAsync(false);
	cotElementsService.getPriceOut(parseFloat(priceFac), parseFloat(tuiLv),
			parseInt(priceFacUnit), parseInt(priceOutUnit), parseFloat(liRun),
			parseFloat(cbm), boxObCount, function(res) {
				if (res != null) {
					$('priceOut').value = res.toFixed("2");
				}
			});
	DWREngine.setAsync(true);
	// }
}