Ext.onReady(function() {
	// var proxy = new Ext.dd.DDProxy("calculatorDiv");

	var pl = new Ext.Window({
		resizable : false,
		id : "calculator",
		title : "公式配置",
		width : 445,
		height : 275,
		closeAction:'hide',
		html : '<iframe id="calculatorFrame" src="./systemdic/boxpacking/calculator.jsp" '
				+ 'width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
	});

	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	// 厂家
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&typeName=factroyTypeidLv1&type=3",
		cmpId : 'facId',
		sendMethod : "post",
		fieldLabel : "<font color=red>Supplier</font>",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		pageSize : 5,
		anchor : "100%",
		tabIndex : 2,
		allowBlank:false,
		blankText:"Choose Suppliers ",
		selectOnFocus : true,
		emptyText : 'Choose',
		
		
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	/***************************************************************************
	 * 描述: 本地下拉列表
	 **************************************************************************/
	var typeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['IB', '内盒包装'], ['MB', '中盒包装'], ['OB', '外箱包装'],
						['PB', '产品包装'], ['IG', '插格类型']]
			});

	var typeField = new Ext.form.ComboBox({
				name : 'type',
				fieldLabel : 'Package Type',
				editable : false,
				store : typeStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 3,
				emptyText : 'Choose',
				hiddenName : 'type',
				selectOnFocus : true
			});

	var form = new Ext.form.FormPanel({
				labelWidth : 70,
				labelAlign : "right",
				layout : "column",
				// autoWidth:true,
				// autoHeight:true,
				padding : "5px",
				// renderTo:"modpage",
				formId : "boxPackForm",
				id : "boxPackFormId",
				frame : true,
				buttonAlign : "center",
				monitorValid : true,
				fbar : [{
							text : "Save",
							formBind : true,
							handler : mod,
							iconCls : "page_table_save"
						}, {
							text : "Cancel",
							iconCls : "page_cancel",
							handler : function() {
								closeandreflashEC(true, 'boxpackingGrid', false)
							}
						}],
				items : [{
							xtype : "panel",
							columnWidth : 0.4,
							layout : "form",
							border : false,
							labelWidth : 100,
							items : [{
										xtype : "textfield",
										id : "value",
										name : "value",
										fieldLabel : "Name-cn",
										anchor : "92%",
										allowBlank : false,
										tabIndex : 1,
										blankText : "Name can not be empty"
									}, {
										xtype : "textfield",
										id : "valueEn",
										name : "valueEn",
										fieldLabel : "Name-en",
										tabIndex : 4,
										anchor : "92%"
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.6,
							border : false,
							layout : "column",
							labelWidth : 100,
							items : [{
										xtype : "panel",
										columnWidth : 0.5,
										padding : "0",
										layout : "form",
										labelWidth : 60,
										border : false,
										items : [facBox, {
													xtype : "numberfield",
													id : "materialPrice",
													name : "materialPrice",
													fieldLabel : "Price",
													tabIndex : 5,
													anchor : "100%",
													maxValue : 9999999.999,
													decimalPrecision : 3,
													xtype : "numberfield",
													validateOnBlur : false
												}]
									}, {
										xtype : "panel",
										columnWidth : 0.5,
										padding : "0",
										layout : "form",
										labelWidth : 60,
										border : false,
										items : [typeField, {
													xtype : "textfield",
													id : "unit",
													name : "unit",
													fieldLabel : "Price",
													tabIndex : 6,
													anchor : "100%"
												}]
									}]
						}, {
							xtype : "panel",
							columnWidth : 1,
							layout : "form",
							labelWidth : 100,
							items : [{
								xtype : "textarea",
								id : "formulaOut",
								name : "formulaOut",
								fieldLabel : "Formula",
								tabIndex : 7,
								height : 105,
								listeners : {
									'focus' : function(t) {
										var po = t.getPosition();
										pl
												.setPosition(po[0]+30, po[1]+20);
										pl.show();
									}
								},
								anchor : "100%"

							}, {
								xtype : "textarea",
								id : "remark",
								name : "remark",
								height : 120,
								fieldLabel : "Remark",
								tabIndex : 8,
								anchor : "100%"
							}]
						}, {
							xtype : 'hidden',
							name : 'id',
							id : 'id'
						}, {
							xtype : 'hidden',
							name : 'formulaIn',
							id : "formulaIn"
						}, {
							xtype : 'hidden',
							name : 'checkCalculation',
							id : 'checkCalculation'
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
	viewport.doLayout();

	// 初始化页面
	function initForm() {
		// 加载主表单
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotBoxPackingService.getBoxPackingById(parseInt(id), function(res) {
						// printObject(res)
						if (res != null) {
							var obj = res;
							DWRUtil.setValues(obj);
							facBox.bindPageValue('CotFactory', 'id', obj.facId);
							typeField.setValue(obj.type)
						}
					});
		} else {
			// $('tabPane2').style.display='none';
		}
		$('value').focus();
	}
	// 初始化页面
	initForm();
	j('#formulaOut').setCaret();
	
	this.closeCal=function(){
		pl.hide();
	}
});

var simple = null;// 简单字段缓存
var pos = 0;// 光标位置
function mod() {
	var popedom = checkAddMod($('eId').value);
	if (popedom == 1) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority!');
		return;
	} else if (popedom == 2) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority!');
		return;
	}
	var outexp = Ext.getCmp("formulaOut").getValue();
	var checkExp = Ext.getCmp("formulaOut").getValue();
	if (simple != null) {
		for (var p in simple) {
			var val = '\#{' + simple[p] + '}';
			var re = eval('/' + p + '/g');
			outexp = outexp.replace(re, val);
			checkExp = checkExp.replace(re, "0.00001");
		}
		$("formulaIn").value = outexp;
		$("checkCalculation").value = checkExp;

	}
	// 验证公式是否正确
	DWREngine.setAsync(false);
	var str = DWRUtil.getValue("checkCalculation");
	var checkRes;
	if (str.length > 0) {
		cotBoxPackingService.checkCalculation(str, function(res) {
					checkRes = res
				})
	}
	if (checkRes == false) {
		alert("Formula has an error, please re-enter!");
		return;
	}
	var value = Ext.getCmp("value").getValue();
	var id = Ext.getCmp("id").getValue();
	cotBoxPackingService.findExistBoxPacking(id, value, function(res) {
				if (res) {
					Ext.MessageBox.show({
								title : 'Message',
								msg : 'Existing packaging materials, please re-enter！',
								height : 260,
								width : 200,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING,
								fn : function() {
									// 选中编号
									Ext.getCmp("value").selectText();
								}
							});
				} else {
					var obj = DWRUtil.getValues("boxPackForm");
					var list = new Array();
					var cotBoxPacking = new CotBoxPacking();
					for (var p in cotBoxPacking) {
						cotBoxPacking[p] = obj[p];
					}
					if (id != 'null') {
						cotBoxPacking.id = id;
					}

					list.push(cotBoxPacking);
					cotBoxPackingService.saveOrUpdateBoxPacking(cotBoxPacking,
							function(res) {
								if (res) {
									Ext.MessageBox.alert("Message", 'Successfully saved！');
									closeandreflashEC(true, 'boxpackingGrid',
											false);
								} else {
									Ext.MessageBox.alert("Message", 'Save failed！');
									Ext.getCmp("value").selectText();
								}
							});
				}
			});
	DWREngine.setAsync(true);
}

// 关闭公式编辑层
function closeCalculatorDiv() {
	$("calculatorDiv").style.display = "none";
}

// 填写内部表达式表单
function setCheckCalculation(e) {
	$("checkCalculation").value = $("checkCalculation").value + e;
}

// 填写内部表达式表单
function setExpessionInForm(e) {
	$("formulaIn").value = $("formulaIn").value + e;
}

// 填写对外表达式表单
function setExpressionOutForm(e) {
	j('#formulaOut').insertAtCaret(e);
}

// 清空内部表达式&对外表达式表单
function clearForm() {
	$("formulaIn").value = "";
	$("formulaOut").value = "";
	$("checkCalculation").value = "";
}

function Show(e) {
	$('calculatorDiv').style.display = 'block';
	var div = Ext.fly('calculatorDiv');
	div.addClass('red');
	var dialog = Ext.getCmp("calculator"); // 根据ID获取组件
	dialog.show(); // 得到组件后,就可以对其进行操作
}

function Hide() {
	var dialog = Ext.getCmp("calculator");
	dialog.hide();
	$('calculatorDiv').style.display = 'none';
}