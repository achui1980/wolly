Ext.onReady(function() {

	// 客户名称
	var custBox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		fieldLabel : "<font color=red>客户名称</font>",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "请选择客户！",
		tabIndex : 1,
		sendMethod : "post",
		selectOnFocus : true,
		
		
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'empId',
				fieldLabel : "业务员",
				disabled:true,
				disabledClass : 'combo-disabled',
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				anchor : "100%",
				selectOnFocus : true,
				sendMethod : "post",
				emptyText : '请选择',
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	
	
	var form = new Ext.form.FormPanel({
				labelWidth : 70,
				labelAlign : "right",
				layout : "form",
				padding : "5px",
				formId : "custLogForm",
				frame : true,
				buttonAlign : "center",
				monitorValid : true,
				fbar : [{
							text : "保存",
							formBind : true,
							handler : save,
							iconCls : "page_table_save"
						}, {
							text : "删除",
							iconCls : "page_del",
							cls : "SYSOP_DEL",
							handler : del
						}, {
							text : "取消",
							iconCls : "page_cancel",
							handler : function() {
								closeandreflashEC(true, 'custLogGrid', false)
							}
						}],
				items : [{
							xtype : "panel",
							layout : "column",
							items : [{
										xtype : "panel",
										layout : "form",
										labelWidth : 70,
										columnWidth : 0.5,
										items : [custBox]
									}, {
										xtype : "panel",
										layout : "form",
										labelWidth : 70,
										columnWidth : 0.5,
										items : [busiBox]
									}]
						}, {
							xtype : "panel",
							layout : "form",
							labelWidth : 70,
							items : [{
													xtype : "datefield",
													fieldLabel : "日期",
													anchor : "100%",
													id : "logDate",
													value : new Date(),
													name : "logDate",
													tabIndex : 2,
													format : "Y-m-d H:i:s",
													listeners:{
														'select':function(field,date){
															var dt = new Date(date);
															var date = dt.format('Y-m-d');
															var dt2 = new Date();
															var time = dt2.format('H:i:s');
															field.setValue(date+" "+time);
														}
													}
												}]
						}, {
							xtype : "panel",
							layout : "form",
							labelWidth : 70,
							items : [{
										xtype : "textarea",
										fieldLabel : "工作内容",
										anchor : "100%",
										height : 50,
										tabIndex : 3,
										id : "logContent",
										name : "logContent",
										maxLength : 200
									}]
						}, {
							xtype : "panel",
							layout : "form",
							labelWidth : 70,
							items : [{
										xtype : "textarea",
										fieldLabel : "备注",
										anchor : "100%",
										height : 50,
										tabIndex : 4,
										id : "remark",
										name : "remark",
										maxLength : 200
									}]
						}, {
							xtype : "fieldset",
							title : '主管批注',
							layout : 'form',
							collapsed : true,
							checkboxToggle : true,
							id : "chk",
							name : "chk",
							labelWidth : 55,
							tabIndex : 5,
							labelAlign : 'right',
							items : [{
										xtype : "textarea",
										fieldLabel : "批注内容",
										anchor : "100%",
										height : 50,
										tabIndex : 3,
										id : "logAdvise",
										name : "logAdvise",
										maxLength : 200
									}]
						}, {
							xtype : 'hidden',
							name : 'id'
						}]
			});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
	viewport.doLayout();

	// 初始化页面
	function initForm() {
		//判断是否有批注权限
		var isSelBox = getPopedomByOpType("cotcustlog.do", "CHECK");
		if (isSelBox == 0) {
			Ext.getCmp("chk").setDisabled(true);
		}
		
		// 加载主表单
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotCustLogService.geCustLogById(parseInt(id), function(res) {
						if (res != null) {
							DWRUtil.setValues(res);
							custBox.bindPageValue("CotCustomer", "id", res.custId);
							busiBox.bindPageValue("CotEmps", "id", res.empId);
							if (res.logCheck == 1) {
								Ext.getCmp('chk').expand();
							}
						}
					});
		}else{
			busiBox.bindPageValue("CotEmps", "id",logId);
		}
	}
	// 初始化页面
	initForm();

	function save() {
		var popedom = checkAddMod($('eId').value);
		if (popedom == 1) {
			Ext.Msg.alert("提示消息", '对不起,您没有添加权限!请联系管理员!');
			return;
		} else if (popedom == 2) {
			Ext.Msg.alert("提示消息", '对不起,您没有修改权限!请联系管理员!');
			return;
		}
		// 验证表单
		var formData = getFormValues(form, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		Ext.MessageBox.confirm('提示信息', '您是否要保存该工作日志？', function(btn) {
					if (btn == 'yes') {
						var log = DWRUtil.getValues('custLogForm');
						var cotCustLog = new CotCustLog();
						for (var p in cotCustLog) {
							if (p != 'logDate') {
								cotCustLog[p] = log[p];
							}
						}
						if ($('eId').value != 'null' && $('eId').value != '') {
							cotCustLog.id = $('eId').value;
						}

						var chk = Ext.getCmp('chk').checkbox;
						if (chk) {
							if (chk.dom.checked) {
								cotCustLog.logCheck = 1;
							} else {
								cotCustLog.logCheck = 0;
							}
						}

						DWREngine.setAsync(false);
						cotCustLogService.saveOrUpdateCustLog(cotCustLog,
								$('logDate').value, function(res) {
									if (res != null) {
										$('eId').value = res;
										Ext.Msg.alert("提示消息", "保存成功！");
										closeandreflashEC(true, 'custLogGrid', false);
									} else {
										Ext.MessageBox.alert('提示消息', '保存失败');
									}
								});
						DWREngine.setAsync(true);
					}
				});
	}

	// 表格中删除
	function del() {
		if ($('eId').value == 'null' || $('eId').value == '') {
			closeandreflashEC(true, 'custLogGrid', false)
			return;
		}
		
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("提示消息", "您没有删除权限");
			return;
		}
		
		var list = new Array();
		list.push($('eId').value);
		Ext.MessageBox.confirm('提示信息', "是否确定删除此工作日志?", function(btn) {
					if (btn == 'yes') {
						cotCustLogService.deleteCustLogByList(list,
								function(res) {
									if (res == 0) {
										Ext.MessageBox.alert("提示消息", "删除成功!");
										closeandreflashEC(true, 'custLogGrid', false)
									} else {
										Ext.MessageBox.alert("提示消息", "删除失败!");
									}
								});
					}
				});
	}
});
