var facNum = getDeNum("facPrecision");
var outNum = getDeNum("outPrecision");
var cbmNum = getDeNum("cbmPrecision");

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
				// blankText : "Please select Dep. Of Sales！",
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
				// blankText : "Please select Group！",
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
				// blankText : "Please select Category！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 材质
	// var typeLvBox = new BindCombox({
	// dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
	// cmpId : 'eleTypeidLv1',
	// fieldLabel : "材质",
	// editable : true,
	// valueField : "id",
	// allowBlank : true,
	// hidden : true,
	// hideLabel : true,
	// displayField : "typeName",
	// emptyText : '请选择',
	// pageSize : 10,
	// anchor : "100%",
	// tabIndex : 2,
	// sendMethod : "post",
	// selectOnFocus : true,
	// minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
	// listWidth : 350,// 下
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
		hidden : hideFac,
		hideLabel : hideFac,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		// disabled : true,
		// disabledClass : 'combo-disabled',
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
				pageSize : 10,
				anchor : "100%",
				tabIndex : 16,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

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

	// 海关编码
	var eleHsidBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "Customs Code",
				editable : true,
				hidden : true,
				hideLabel : true,
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
							cotElementsService.getEleTax(rec.data.id, function(
											res) {
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
							inputGridTypeIdBox.setValue(res.inputGridType);
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
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "value",
				emptyText : 'Choose Material Type',
				anchor : "100%",
				tabIndex : 41,
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
				allowBlank : true,
				displayField : "value",
				emptyText : 'Choose Material Type',
				anchor : "100%",
				tabIndex : 43,
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
				allowBlank : true,
				hidden : true,
				hideLabel : true,
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
		var model = $('modelEle').value;
		cotElementsService.getEleByEleId(model, function(res) {
					if (res == null) {
						Ext.MessageBox
								.alert("Message",
										'The Articel No.does not exist, please re-enter!');
					} else {
						var temp = $('eleId').value;
						var picSrc = $('picPath').src;

						myForm.getForm().setValues(res);

						// typeLvBox.bindPageValue("CotTypeLv1", "id",
						// res.eleTypeidLv1);
						boxTypeIdBox.bindPageValue("CotBoxType", "id",
								res.boxTypeId);

						boxIbTypeIdBox.bindValue(res.boxIbTypeId);
						boxMbTypeIdBox.bindValue(res.boxMbTypeId);
						boxObTypeIdBox.bindValue(res.boxObTypeId);
						boxPbTypeIdBox.bindValue(res.boxPbTypeId);
						inputGridTypeIdBox.bindValue(res.inputGridTypeId);

						facBox.bindPageValue("CotFactory", "id", res.factoryId);
						facCurBox.bindValue(res.priceFacUint);
						outCurBox.bindValue(res.priceOutUnit);

						Ext.getCmp("eleAddTime").setValue(res.eleAddTime);

						// $('eleId').value = temp;
						// Ext.getCmp("eleId").clearInvalid();
						Ext.getCmp("eleId").setValue(temp);
						Ext.getCmp("barcode").setValue("");
						// 刚来源货号的pic_img生成一张图片存储在项目底下sampleImg/
						DWREngine.setAsync(false);
						cotElementsService.createPic(model, function(arg) {
									$('picPath').src = "sampleImg/" + model
											+ ".png";
								});
						DWREngine.setAsync(true);
						modlPanel.hide();
					}
				});
	}

	// 显示模板面板
	var modlPanel;
	var showModel = function(item, pressed) {
		if ($('importDiv') == null) {
			// window默认在z-index为9000
			Ext.DomHelper.append(document.body, {
				html : '<div id="importDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (modlPanel == null) {
			modlPanel = new Ext.form.FormPanel({
				labelWidth : 40,
				padding : "5px",
				height : 80,
				width : 220,
				layout : 'hbox',
				frame : true,
				title : "Please Enter src Art No.",
				labelAlign : "right",
				layoutConfig : {
					padding : '5',
					align : 'middle'
				},
				tools : [{
							id : "close",
							handler : function(event, toolEl, p) {
								p.hide();
							}
						}],
				defaults : {
					margins : '0 5 0 0'
				},
				items : [{
							xtype : "label",
							text : "src Art No.:",
							width : 80
						}, {
							xtype : "textfield",
							anchor : "95%",
							tabIndex : 1,
							allowBlank : false,
							blankText : "Please enter Art No.",
							maxLength : 100,
							maxLengthText : "",
							flex : 3,
							id : "modelEle",
							name : "modelEle",
							listeners : {
								'specialkey' : function(txt, eObject) {
									var temp = txt.getValue();
									if (temp != ""
											&& eObject.getKey() == Ext.EventObject.ENTER) {
										addWithMol();
									}
								}
							}
						}, {
							xtype : "button",
							flex : 1,
							text : "OK",
							handler : addWithMol
						}]
			});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();

			Ext.Element.fly("importDiv").setLeftTop(left, top - 80);

			modlPanel.render("importDiv");
			$('modelEle').focus();
		} else {
			if (!modlPanel.isVisible()) {
				modlPanel.show();
			} else {
				modlPanel.hide();
			}
		}
	};

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
		// 判断货号是否重复
		var isExist = false;
		DWREngine.setAsync(false);
		cotElementsService.findIsExistEleId($('eleId').value, 0, function(res) {
					if (res) {
						isExist = true;
					}
				});
		DWREngine.setAsync(true);
		if (isExist) {
			Ext.MessageBox.alert("提示消息", 'Fail Save！Art No has exist!');
			return;
		}

		var ele = DWRUtil.getValues('cotElementsForm');
		var elements = new CotElementsNew();
		for (var p in elements) {
			if (p != 'eleAddTime') {
				elements[p] = ele[p];
			}
			if (p == 'picPath') {
				var basePath = $('basePath').value;
				var filePath = $('picPath').src;
				var s = filePath.indexOf(basePath);
				var fPath = filePath.substring(s + basePath.length, 111111);
				elements[p] = decodeURI(fPath);
			}
		}
		elements.eleId = elements.eleId.toUpperCase();
		elements.boxIbType = boxIbTypeIdBox.getRawValue();
		elements.boxMbType = boxMbTypeIdBox.getRawValue();
		elements.boxObType = boxObTypeIdBox.getRawValue();
		elements.boxPbType = boxPbTypeIdBox.getRawValue();
		elements.eleForPerson = empBox.getValue();
		
		//是否锁定barcode
		if(Ext.getCmp('lockBarcode').iconCls=='lock'){
			elements.lockBar=1;
		}else{
			elements.lockBar=0;
		}
		
		DWREngine.setAsync(false);
		mask("Saving...");
		DWREngine.setAsync(true);
		cotElementsService.saveElement(elements, $('eleAddTime').value,
				function(res) {
					if (res == null) {
						Ext.MessageBox.alert("提示消息", 'Fail Save！');
					} else {
						if ($('chk').value == 0) {
							var list = new Array();
							var obj = {};
							obj.data = {};
							obj.data.eleId = $('eleId').value;
							list.push(obj)
							self.opener.insertSelect(list, true);
							window.close();
						} else {
							closeandreflashEC('true', 'eleGrid', false);
						}
						// Ext.MessageBox.alert("提示消息",'添加成功！');
					}
					unmask();
				})
	}

	// 隐藏的字段
	var hiddenItems = new Ext.Panel({
				items : [{
							xtype : "textfield",
							fieldLabel : "YY-MM-DD",
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
							fieldLabel : "Middle Box CM",
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
							fieldLabel : "中文规格",
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
				}, {
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
									fieldLabel : "N.W(Kgs)",
									anchor : "100%",
									allowBlank : true,
									decimalPrecision : 3,
									tabIndex : 20,
									id : "boxNetWeigth",
									name : "boxNetWeigth",
									maxLength : 999999.99
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
					}]
		}, {
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.6,
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
						columnWidth : 0.4,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Import Tax(%)",
									anchor : "100%",
									id : "importTax",
									name : 'importTax'
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
								value : 0,
								id : "priceFac",
								name : "priceFac",
								maxLength : 10000000,
								listeners : {
									'change' : function(num, newValue, oldValue) {
										if (!Ext.isEmpty(newValue)
												&& newValue != 0) {
											Ext.getCmp('priceOut').enable();
											setPriceOut();

										}
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
						fieldLabel : "<a onclick=setPriceOut() ext:qwidth=100 ext:qtip='According to the default configuration after clicking outside the selling price formula' style='color:blue;cursor: hand;text-decoration: underline'>Sales Price</a>",
						anchor : "100%",
						allowBlank : true,
						decimalPrecision : outNum,
						tabIndex : 24,
						disabled : true,
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
				title : "",
				iconCls : "",
				columnWidth : 0.2,
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
			}, {
				xtype : "panel",
				columnWidth : 0.2,
				layout : "form",
				items : [{
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
			}]
		}]
	})

	var isExist = true;
	// 主表单
	var myForm = new Ext.form.FormPanel({
		formId : "cotElementsForm",
		labelWidth : 90,
		labelAlign : "right",
		layout : "form",
		width : "100%",
		padding : "5px",
		frame : true,
		buttonAlign : 'center',
		buttons : [{
					text : "Save",
					cls : "SYSOP_ADD",
					iconCls : "page_table_save",
					handler : add
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					handler : function() {
						closeandreflashEC(true, 'eleGrid', false);
					}
				}, {
					text : "Copy From",
					iconCls : "page_from",
					handler : showModel
				}],
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
														allowBlank : true,
														hidden : true,
														hideLabel : true,
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
														hidden : true,
														hideLabel : true,
														decimalPrecision : 2,
														tabIndex : 31,
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
														hidden : true,
														hideLabel : true,
														decimalPrecision : 2,
														tabIndex : 34,
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
														id : "boxPbH",
														name : "boxPbH",
														hidden : true,
														hideLabel : true,
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
														id : "boxIbH",
														name : "boxIbH",
														hidden : true,
														hideLabel : true,
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
					maxLength : 500
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
								+ 'onload="javascript:DrawImage(this,135,132)" /></div>',
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
		}, hiddenItems]
	});

	// 打开上传面板
	function showUploadPanel() {
		var win = new UploadWin({
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

	// 英寸换算成尺寸
	function calNum(inch, needId) {
		if (isNaN(inch.value)) {
			inch.value = 0;
		}
		var num = (inch.value * 2.54).toFixed("2");
		$(needId).value = num;
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
					'Please enter the gross weight (Must be a numeric type!)!')
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

	DWREngine.setAsync(false);
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [myForm]
			});
	DWREngine.setAsync(true);

	// 页面加载调用
	function initForm() {
		DWREngine.setAsync(false);
		cotElementsService.getDefaultList(function(res) {
					if (res.length != 0) {
						// typeLvBox.bindPageValue("CotTypeLv1", "id",
						// res[0].eleTypeId);
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

						// 设置是否自动生成包装尺寸标识
						if (res[0].sizeFollow != null) {
							sizeFollow = res[0].sizeFollow;
						}
					}

					// 填新货号和样品类别和厂家
					$('eleId').value = $('eleNo').value.toUpperCase();
					typeBox.bindPageValue("CotTypeLv1", "id", $('byId').value);
					type2Box.bindPageValue("CotTypeLv2", "id", $('myId').value);
					type3Box.bindPageValue("CotTypeLv3", "id", $('syId').value);
					facBox.bindPageValue("CotFactory", "id", $('facId').value);

					$('factoryNo').focus();
				});
		// 采购价
		// Ext.getCmp('priceFac').setValue(0);
		DWREngine.setAsync(true);
	}
	initForm();
	unmask();
	
	//根据单号配置自动生成货号
	function setArtNo(){
		var byId=$('eleTypeidLv1').value;
		var myId=$('eleTypeidLv2').value;
		var syId=$('eleTypeidLv3').value;
		var facId=$('factoryId').value;
		if(byId=='' || myId=='' || syId=='' || facId==''){
			Ext.MessageBox.alert('Message','Please choose Dep.of PRODUCT and Group and Category and Supplier!')
		}else{
			cotSeqService.getEleNo(byId, myId,syId,facId, function(ps) {
				$('eleId').value=ps;
					});
		}
	}

});
// 设置外销价
function setPriceOut() {
	var priceFac = $('priceFac').value;
	if (priceFac == '' || priceFac == null) {
		// return;
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