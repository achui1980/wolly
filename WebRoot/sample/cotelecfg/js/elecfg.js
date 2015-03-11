Ext.onReady(function() {
	var _self = this;
	var proxy1 = new Ext.dd.DDProxy("calculatorDiv");
	var proxy2 = new Ext.dd.DDProxy("calculatorfacDiv");

	//外销价公式配置
	var pl1 = new Ext.Window({
		title : "Sales Price Formula",
		resizable:false,
		width : 445,
		height : 275,
		closeAction:'hide',
		html : '<iframe id="calculatorFrame" src="./sample/cotelecfg/calculator.jsp" '
				+ 'width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
	});

	//成本价公式配置
	var pl2 = new Ext.Window({
		title : "成本价公式配置",
		resizable:false,
		width : 445,
		height : 275,
		closeAction:'hide',
		html : '<iframe id="calculatorfacFrame" src="./sample/cotelecfg/faccalculator.jsp" '
				+ 'width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
	});

	var facUnit = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				valueField : "id",
				fieldLabel : "Purchase Currency",
				allowBlank : true,
				tabIndex : 4,
				displayField : "curNameEn",
				cmpId : "elePriceFacUnit",
				emptyText : 'Choose',
				anchor : "100%"
			});

	var outUnit = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				valueField : "id",
				fieldLabel : "Sales Currency",
				allowBlank : false,
				tabIndex : 5,
				triggerAction : "all",
				displayField : "curNameEn",
				cmpId : "elePriceOutUnit",
				emptyText : 'Choose',
				anchor : "100%"
			});
	var boxPacking = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType&key=typeName",
				valueField : "id",
				fieldLabel : "Packing Way",
				editable:true,
				hidden:true,
				hideLabel:true,
				mode : 'remote',// 默认local
				allowBlank : true,
				sendMethod:"POST",
				pageSize:10,
				tabIndex : 11,
				displayField : "typeName",
				cmpId : "boxTypeId",
				emptyText : 'Choose',
				anchor : "100%",
				selectOnFocus : true,
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	var inputGridTypeIdBox = new BindCombox({
			dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IG",
			valueField : "id",
			fieldLabel : "插格类型",
			autoShow : true,
			allowBlank : true,
			hidden:true,
			hideLabel:true,
			tabIndex : 12,
			displayField : "value",
			cmpId : "inputGridTypeId",
			emptyText : 'Choose',
			anchor : "100%"
	});

	var boxPbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=PB",
				valueField : "id",
				fieldLabel : "/",
				autoShow : true,
				allowBlank : true,
				hidden:true,
				hideLabel:true,
				tabIndex : 14,
				displayField : "value",
				cmpId : "boxPbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "99.9%"
			});

	var boxIbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IB",
				valueField : "id",
				fieldLabel : "/",
				autoShow : true,
				allowBlank : true,
				hidden:true,
				hideLabel:true,
				tabIndex : 16,
				displayField : "value",
				cmpId : "boxIbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "99.9%"
			});

	var boxMbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=MB",
				valueField : "id",
				fieldLabel : "/",
				autoShow : true,
				allowBlank : true,
				hidden:true,
				hideLabel:true,
				tabIndex : 18,
				displayField : "value",
				cmpId : "boxMbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "99.9%"
			});

	var boxObTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=OB",
				valueField : "id",
				fieldLabel : "/",
				autoShow : true,
				allowBlank : true,
				hidden:true,
			hideLabel:true,
				tabIndex : 20,
				displayField : "value",
				cmpId : "boxObTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "99.9%"
			});

	// 下拉框级联操作
	boxPacking.on('select', function() {
				if(boxPacking.value == "") return;
				DWREngine.setAsync(false);
				cotEleCfgService.getBoxTypeById(boxPacking.value,
						function(res) {
							if (res != null) {
								if (res.boxIName == null) {
									boxIbTypeBox.setValue('');
								} else {
									boxIbTypeBox.bindValue(res.boxIName);
								}
								if (res.boxOName == null) {
									boxObTypeBox.setValue('');
								} else {
									boxObTypeBox.bindValue(res.boxOName);
								}
								if (res.boxMName == null) {
									boxMbTypeBox.setValue('');
								} else {
									boxMbTypeBox.bindValue(res.boxMName);
								}
								if (res.boxPName == null) {
									boxPbTypeBox.setValue('');
								} else {
									boxPbTypeBox.bindValue(res.boxPName);
								}
							}
						});
				DWREngine.setAsync(true);
			});

	var typeLv1Name = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeId',
				fieldLabel : "Material",
				editable : true,
				hidden:true,
				hideLabel:true,
				valueField : "id",
				sendMethod:"POST",
				displayField : "typeName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 10,
				anchor : "100%",
				tabIndex : 1,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var facName = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'eleFacId',
				fieldLabel : "Supplier",
				sendMethod:"POST",
				editable : true,
				hidden:true,
				hideLabel:true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 10,
				anchor : "100%",
				tabIndex : 2,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	/***************************************************************************
	 * 描述: 本地下拉列表
	 **************************************************************************/
	var eleCfgStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '单件'], [1, '套件'], [3, '组件']]
			});

	var eleCfgField = new Ext.form.ComboBox({
				name : 'eleFlag',
				fieldLabel : '组合方式',
				editable : false,
				hidden : true,
				hideLabel : true,
				store : eleCfgStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 9,
				emptyText : 'Choose',
				hiddenName : 'eleFlag',
				selectOnFocus : true
			});

	eleCfgField.on('select', function() {
				if ($('eleFlag').value != 0) {
					Ext.getCmp("eleUnitnum").setDisabled(false);
				}
				if ($('eleFlag').value == 0) {
					Ext.getCmp("eleUnitnum").setValue(1);
					Ext.getCmp("eleUnitnum").setDisabled(true);
				}
			});

	this.cfgForm = new Ext.form.FormPanel({
				layout : "form",
				width : 400,
				formId : "eleCfgForm",
				id : "eleCfgFormId",
				autoHeight : true,
				frame : true,
				buttonAlign : "center",
				labelWidth : 120,
				labelAlign : "right",
				fbar : [{
							iconCls : "page_table_save",
							handler : mod,
							width : 65,
							text : "Save"
						}, {
							xtype : 'button',
							text : "Reset",
							iconCls : "page_reset",
							width : 65,
							handler : function() {
								_self.cfgForm.getForm().reset()
							}
						}],
				items : [typeLv1Name, facName, {
							xtype : "textfield",
							fieldLabel : "Unit",
							anchor : "100%",
							name : "eleUnit",
							id : "eleUnit",
							tabIndex : 3,
							maxLength : 20
						}, facUnit, outUnit, {
							xtype : "panel",
							layout : "column",
							items : [{
										xtype : "panel",
										columnWidth : 1,
										layout : "form",
										//labelWidth : 60,
										items : [{
													xtype : "numberfield",
													fieldLabel : "Profit(%)",
													anchor : "100%",
													name : "priceProfit",
													id : "priceProfit",
													maxValue : 999999.99,
													decimalPrecision : 2,
													tabIndex : 6,
													maxLength : 99999999.99
												}, eleCfgField]
									}, {
										xtype : "panel",
										columnWidth :1,
										layout : "form",
										//labelWidth : 60,
										items : [{
													xtype : "numberfield",
													fieldLabel : "Tax rate%",
													anchor : "100%",
													name : "tuiLv",
													id : "tuiLv",
													hidden : true,
													hideLabel : true,
													maxValue : "",
													decimalPrecision : 2,
													maxText : "",
													decimalSeparator : "",
													tabIndex : 7,
													maxLength : 99999999.99
												}, {
													xtype : "textfield",
													fieldLabel : "Pieces",
													anchor : "100%",
													name : "eleUnitnum",
													id : "eleUnitnum",
													tabIndex : 10,
													maxLength : 99999999999
												}]
									}]
						}, boxPacking, inputGridTypeIdBox,
						{
							xtype : "panel",
							layout : "column",
							items : [{
										xtype : "panel",
										columnWidth : 1,
										layout : "form",
										items : [{
													xtype : "numberfield",
													fieldLabel : "Packing",
													anchor : "100%",
													name : "boxPbCount",
													hidden:true,
													hideLabel:true,
													tabIndex : 13,
													maxLength : 99999999999
												}, {
													xtype : "numberfield",
													fieldLabel : "Packing",
													anchor : "100%",
													hidden:true,
													hideLabel:true,
													name : "boxIbCount",
													tabIndex : 15,
													maxLength : 99999999999
												}, {
													xtype : "numberfield",
													fieldLabel : "Packing",
													anchor : "100%",
													hidden:true,
													hideLabel:true,
													name : "boxMbCount",
													tabIndex : 17,
													maxLength : 99999999999
												}, {
													xtype : "numberfield",
													fieldLabel : "Packing",
													anchor : "100%",
													name : "boxObCount",
													tabIndex : 19,
													maxLength : 99999999999
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.55,
										layout : "form",
										hidden:true,
										labelWidth : 3,
										items : [boxPbTypeBox, boxIbTypeBox,
												boxMbTypeBox, boxObTypeBox]
									}]

						}, {
							xtype : "checkbox",
							name : "sizeFollow",
							id : "sizeFollow",
							labelWidth : 1,
							hidden : true,
							hideLabel : true,
							boxLabel : "Product Size--> Packing Size",
							anchor : "100%",
							labelSeparator : " "
						}, {
							xtype : "numberfield",
							fieldLabel : "Weight factor(kg):",
							anchor : "100%",
							emptyText : "0.00",
							name : "grossNum",
							decimalPrecision : 2,
							tabIndex : 21,
							labelSeparator : "",
							maxLength : 999999.99,
							listeners : {
								"render" : function(obj) {
									var tip = new Ext.ToolTip({
												target : obj.getEl(),
												anchor : 'left',
												maxWidth : 180,
												minWidth : 180,
												html : 'G.W=N.W+Weight factor!'
											});
								}
							}
						}, {
							xtype : "textarea",
							fieldLabel : "Purchase Price Formula",
							anchor : "100%",
							hidden:true,
							hideLabel:true,
							id : "expressionFacOut",
							name : "expressionFacOut",
							listeners : {
								'focus' : function(t) {
									//ShowFacCal(t);
									if(pl1.isVisible()){
										pl1.hide();
									}
									var po = t.getPosition();
									pl2.setPosition(po[0]+280,po[1]-130);
									pl2.show();
								}
							},
							tabIndex : 22
						}, {
							xtype : 'hidden',
							name : 'expessionFacIn',
							id : "expessionFacIn"
						}, {
							xtype : 'hidden',
							name : 'checkFacCalculation',
							id : "checkFacCalculation"
						}, {
							xtype : "textarea",
							fieldLabel : "Sales Price Formula",
							anchor : "100%",
							id : "expressionOut",
							name : "expressionOut",
							listeners : {
								'focus' : function(t) {
									//Show(t);
									if(pl2.isVisible()){
										pl2.hide();
									}
									var po = t.getPosition();
									pl1.setPosition(po[0]+280,po[1]-130);
									pl1.show();
								}
							},
							tabIndex : 23
						},{
							xtype : "textarea",
							fieldLabel : "样品默认成本名称",
							anchor : "100%",
							hidden : true,
							hideLabel : true,
							id : "costName",
							name : "costName",
							tabIndex : 24
						
						}, {
							xtype : 'hidden',
							name : 'expessionIn',
							id : "expessionIn"
						}, {
							xtype : 'hidden',
							name : 'checkCalculation',
							id : 'checkCalculation'
						}, {
							xtype : 'hidden',
							name : 'id',
							id : 'id'
						}]
			})

	var mainPnl = new Ext.Panel({
				layout:"hbox",
				width:"100%",
				autoScroll:true,
				ctCls:'x-panel-mc',
				height:Ext.getBody().getHeight(),
				layoutConfig:{
					pack : 'center',

					align:"top"
				},
				items:[this.cfgForm]
			})

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [mainPnl]
			});

	// 初始化页面
	function initForm() {
		// var isMod = getPopedomByOpType(vaildUrl,"MOD");
		// if(isMod==0){//没有修改权限
		// Ext.MessageBox.alert('提示框','您没有修改的权限！');
		// return;
		// }
		// 加载主样品表单
		DWREngine.setAsync(false);
		cotEleCfgService.getEleCfg(function(res) {
					if (res != null) {
						var obj = res;
						DWRUtil.setValues(obj);

						facUnit.bindValue(obj.elePriceFacUnit);
						outUnit.bindValue(obj.elePriceOutUnit);
						boxPacking.bindValue(obj.boxTypeId);
						boxPbTypeBox.bindValue(obj.boxPbTypeId);
						boxIbTypeBox.bindValue(obj.boxIbTypeId);
						boxMbTypeBox.bindValue(obj.boxMbTypeId);
						boxObTypeBox.bindValue(obj.boxObTypeId);
						inputGridTypeIdBox.bindValue(obj.inputGridTypeId);
						typeLv1Name.bindPageValue("CotTypeLv1", "id",
								obj.eleTypeId);
						facName.bindPageValue("CotFactroy", "id", obj.eleFacId);
						eleCfgField.setValue(obj.eleFlag);
						if (obj.eleFlag == 0) {
							// Ext.getDom('eleUnitNum').value = 1;
							Ext.getCmp("eleUnitnum").setValue(1);
							Ext.getCmp("eleUnitnum").setDisabled(true)
							// $('eleUnitNum').disabled = true;
						} else {
							Ext.getCmp("eleUnitnum").setValue(obj.eleUnitnum);
							Ext.getCmp("eleUnitnum").setDisabled(false)
							// $('eleUnitNum').disabled = false;
							// Ext.getDom('eleUnitNum').value = obj.eleUnitnum;
						}
					}
				})
		DWREngine.setAsync(true);
	}
	// 初始化页面
	initForm();
	j('#expressionOut').setCaret();
	j('#expressionFacOut').setCaret();
	// 修改
	function mod() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("Message","Sorry, you do not have Authority!!");
			return;
		}

		var outexp = $("expressionOut").value;
		var checkExp = $("expressionOut").value;

		if (simple != null) {
			for (var p in simple) {
				var val = '\#{' + simple[p] + '}';
				var re = eval('/' + p + '/g');

				outexp = outexp.replace(re, val);
				checkExp = checkExp.replace(re, "0.00001");
			}
			$("expessionIn").value = outexp;
			$("checkCalculation").value = checkExp;

		}

		var outFacexp = $("expressionFacOut").value;
		var checkFacExp = $("expressionFacOut").value;

		if (simpleFac != null) {
			for (var p in simpleFac) {
				var val = '\#{' + simpleFac[p] + '}';
				var re = eval('/' + p + '/g');

				outFacexp = outFacexp.replace(re, val);
				checkFacExp = checkFacExp.replace(re, "0.00001");
			}
			$("expessionFacIn").value = outFacexp;
			$("checkFacCalculation").value = checkFacExp;

		}

		// 表单验证结束
		// 验证成本价公式是否正确
		var str2 = DWRUtil.getValue("checkFacCalculation");
		var checkRes2;
		if (str2.length > 0) {
			cotEleCfgService.checkCalculation(str2, function(res) {
						checkRes2 = res
					})
		}
		if (checkRes2 == false) {
			Ext.Msg.alert("提示信息","成本价公式有错误，请重新输入!");
			return;
		}
		// 验证外销价公式是否正确
		var str = DWRUtil.getValue("checkCalculation");
		var checkRes;
		if (str.length > 0) {
			cotEleCfgService.checkCalculation(str, function(res) {
						checkRes = res
					})
		}

		if (checkRes == false) {
			Ext.Msg.alert("提示信息","外销价公式有错误，请重新输入!");
			return;
		}
		var obj = DWRUtil.getValues("eleCfgForm");
		var cfg = new CotEleCfg();
		for (var p in cfg) {
			cfg[p] = obj[p];
		}
		DWREngine.setAsync(false);

		var id = Ext.getDom('id').value;
		// 是否生成包装尺寸
		if ($("sizeFollow").checked == true) {
			cfg.sizeFollow = 1;
		} else {
			cfg.sizeFollow = 0;
		}

		cfg.boxIbType = boxIbTypeBox.getRawValue();
		cfg.boxMbType = boxMbTypeBox.getRawValue();
		cfg.boxObType = boxObTypeBox.getRawValue();
		cfg.boxPbType = boxPbTypeBox.getRawValue();

		cotEleCfgService.modifyEleCfg(cfg, function(res) {
			Ext.MessageBox.alert("Message", "Save Success!");
				// closeandreflashEC('true','elecfgTable',false);
			})

		DWREngine.setAsync(true);
	}
	
	this.closeCal=function(type){
		if(type==0){
			pl1.hide();
		}else{
			pl2.hide();
		}
	}
});

var simple = null;// 简单字段缓存
var simpleFac = null;
// 填写内部表达式表单
function setCheckCalculation(e) {
	$("checkCalculation").value = $("checkCalculation").value + e;
}

// 填写内部表达式表单
function setExpessionInForm(e) {
	$("expessionIn").value = $("expessionIn").value + e;
}

// 填写对外表达式表单
function setExpressionOutForm(e) {
	j('#expressionOut').insertAtCaret(e);
}

// 清空内部表达式&对外表达式表单
function clearForm() {
	$("expessionIn").value = "";
	$("expressionOut").value = "";
	$("checkCalculation").value = "";
}

function Show(e) {
	$('calculatorDiv').style.display = 'block';
	var div = Ext.get('calculatorDiv');
	div.addClass('div2');
	var dialog = Ext.getCmp("calculator"); // 根据ID获取组件
	var dialog1 = Ext.getCmp("calculatorfac"); // 根据ID获取组件
	dialog.show(); // 得到组件后,就可以对其进行操作
	dialog1.hide();
	$('calculatorfacDiv').style.display = 'none';
}

function Hide() {
	var dialog = Ext.getCmp("calculator");
	dialog.hide();
	$('calculatorDiv').style.display = 'none';
}

// 填写内部表达式表单
function setCheckFacCalculation(e) {
	$("checkFacCalculation").value = $("checkFacCalculation").value + e;
}

// 填写内部表达式表单
function setExpessionFacInForm(e) {
	$("expessionFacIn").value = $("expessionFacIn").value + e;
}

// 填写对外表达式表单
function setExpressionFacOutForm(e) {
	j('#expressionFacOut').insertAtCaret(e);
}

// 清空内部表达式&对外表达式表单
function clearFacForm() {
	$("expessionFacIn").value = "";
	$("expressionFacOut").value = "";
	$("checkFacCalculation").value = "";
}

function ShowFacCal(e) {
	$('calculatorfacDiv').style.display = 'block';
	var div = Ext.get('calculatorfacDiv');
	div.addClass('div1');
	var dialog = Ext.getCmp("calculatorfac"); // 根据ID获取组件
	var dialog1 = Ext.getCmp("calculator"); // 根据ID获取组件
	dialog.show(); // 得到组件后,就可以对其进行操作
	dialog1.hide(); // 得到组件后,就可以对其进行操作
	$('calculatorDiv').style.display = 'none';
}

function HideFacCal() {
	var dialog = Ext.getCmp("calculatorfac");
	dialog.hide();
	$('calculatorfacDiv').style.display = 'none';
}


