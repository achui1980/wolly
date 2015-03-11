Ext.onReady(function() {
	var proxy = new Ext.dd.DDProxy("calculatorDiv");

	var pl = new Ext.Window({
		title : "Formula allocation",
		width : 590,
		height : 275,
		closeAction:'hide',
		html : '<iframe id="calculatorFrame" src="./price/calculation/calculator.jsp" '
				+ 'width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
	});

	var form = new Ext.form.FormPanel({
				labelWidth : 70,
				labelAlign : "right",
				layout : "column",
				padding : "5px",
				formId : "calForm",
				id : "calFormId",
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
								closeandreflashEC(true, 'calculationGrid',
										false)
							}
						}],
				items : [{
							xtype : "panel",
							columnWidth : 1,
							layout : "form",
							border : false,
							labelWidth : 80,
							items : [{
										xtype : "textfield",
										id : "calName",
										name : "calName",
										fieldLabel : "Formula Name",
										anchor : "100%",
										allowBlank : false,
										blankText : "Formula name can not be empty"
									}]
						}, {
							xtype : "panel",
							columnWidth : 1,
							layout : "form",
							labelWidth : 80,
							items : [{
										xtype : "textarea",
										id : "expressionOut",
										allowBlank : false,
										blankText : "Please enter the formula",
										name : "expressionOut",
										fieldLabel : "External Expressions",
										height : 105,
										listeners : {
											'focus' : function(t) {
												//Show(t);
												pl.show();
											}
										},
										anchor : "100%"
									}, {
										xtype : "textarea",
										id : "remark",
										name : "remark",
										height : 120,
										fieldLabel : "Remarks",
										anchor : "100%"
									}]
						}, {
							xtype : 'hidden',
							name : 'id',
							id : 'id'
						}, {
							xtype : 'hidden',
							name : 'expessionIn',
							id : "expessionIn"
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
			cotCalculationService.getCalculationById(parseInt(id),
					function(res) {
						if (res != null) {
							var obj = res;
							DWRUtil.setValues(obj);
						}
					});
		} else {
			// $('tabPane2').style.display='none';
		}
		$('calName').focus();
	}
	// 初始化页面
	initForm();
	j('#expressionOut').setCaret();
	
	this.closeCal=function(type){
		pl.hide();
	}
	
});

var simple = null;// 简单字段缓存
var comp = null // 复合字段缓存
var pos = 0;// 光标位置
function mod() {
	var popedom = checkAddMod($('eId').value);
	if (popedom == 1) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority! Pls. contact the webmaster!');
		return;
	} else if (popedom == 2) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority! Pls. contact the webmaster!');
		return;
	}
	var outexp = Ext.getCmp("expressionOut").getValue();
	var checkExp = Ext.getCmp("expressionOut").getValue();
	if (simple != null) {
		for (var p in simple) {
			var val = '\#{' + simple[p] + '}';
			var re = eval('/' + p + '/g');
			outexp = outexp.replace(p, val);
			//alert(outexp);
			checkExp = checkExp.replace(re, "0.00001");
		}
		$("expessionIn").value = outexp;
		// alert("ddd:"+$("expessionIn").value);
		$("checkCalculation").value = checkExp;
	}
	if (comp != null) {
		for (var p in comp) {
			//alert(p);
			// alert(p+":"+comp[p])
			// var re = eval('/'+p+'/g');
			outexp = outexp.replace(p, comp[p]);
			p  = "(" + p + ")";
			 var re = eval('/'+p+'/g');
			// alert(re)
			checkExp = checkExp.replace(re, "0.00001");
		}
		 //alert("checkExp:"+checkExp)
		$("expessionIn").value = outexp;
		$("checkCalculation").value = checkExp;
	}
	// 验证公式是否正确
	DWREngine.setAsync(false);
	var str = DWRUtil.getValue("checkCalculation");
	var checkRes;
	if (str.length > 0) {
		cotCalculationService.checkCalculation(str, function(res) {
					checkRes = res
				})
	}
	if (checkRes == false) {
		alert("Formula has an error, please re-enter!");
		return;
	}
	var id = Ext.getCmp("id").getValue();

	var obj = DWRUtil.getValues("calForm");
	var list = new Array();
	var cotCalculation = new CotCalculation();
	for (var p in cotCalculation) {
		cotCalculation[p] = obj[p];
	}
	if (id != 'null' && id != '') {
		cotCalculation.id = id;
		cotCalculationService.modifyCalculation(cotCalculation, function(res) {
					if (res) {
						Ext.MessageBox.alert("Message", 'Successfully modified！');
						reflashParent('calculationGrid');
					} else {
						Ext.MessageBox.alert("Message", 'Change failed!');
						Ext.getCmp("calName").selectText();
					}
				});
	} else {
		var isExist = false;
		cotCalculationService.findExistByName(cotCalculation.calName, function(
						res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.MessageBox.alert("Message", "The  name already exists!");
			return;
		}
		cotCalculationService.addCalculation(cotCalculation, function(res) {
					Ext.MessageBox.alert("Message", 'Successfully added！');
					closeandreflashEC(true, 'calculationGrid', false)
				})
	}
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