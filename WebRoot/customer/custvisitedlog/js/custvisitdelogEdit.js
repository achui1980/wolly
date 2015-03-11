Ext.onReady(function() {

	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	// 客户
	var customerBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCustomer&key=customerShortName&flag=filter",
				cmpId : 'cotCustomerId',
				fieldLabel : "客户",
				sendMethod : "post",
				editable : true,
				valueField : "id",
				displayField : "customerShortName",
				pageSize : 5,
				anchor : "100%",
				selectOnFocus : true,
				disabled : true,
				disabledClass : 'combo-disabled',
				emptyText : 'Choose',
				tabIndex : 2,
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var form = new Ext.form.FormPanel({
				labelWidth : 60,
				labelAlign : "right",
				layout : "column",
				padding : "5px",
				formId : "visitedlogForm",
				id : "visitedlogFormId",
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
								closeandreflashEC(true, 'contactGrid', false)
							}
						}],
				items : [{
							xtype : "panel",
							title : "",
							layout : "column",
							items : [{
										xtype : "panel",
										title : "",
										columnWidth : 0.33,
										layout : "form",
										items : [{
													xtype : "textfield",
													fieldLabel : "<font color=red>Vistor</font>",
													anchor : "100%",
													allowBlank : false,
													blankText : "Please enter the visitor",
													maxLength : 100,
													name : "visitPeople",
													id : "visitPeople",
													tabIndex : 1
												}, {
													xtype : "datefield",
													fieldLabel : "Visiting time",
													anchor : "100%",
													allowBlank : true,
													value:new Date(),
													name : "visitTime",
													id : "visitTime",
													format : "Y-m-d",
													tabIndex : 4
												}]
									}, {
										xtype : "panel",
										title : "",
										columnWidth : 0.33,
										layout : "form",
										items : [customerBox, {
													xtype : "numberfield",
													fieldLabel : "Number of visitors",
													anchor : "100%",
													allowBlank : true,
													maxValue : 99,
													name : "visitNo",
													id : "visitNo",
													tabIndex : 5
												}]
									}, {
										xtype : "panel",
										title : "",
										columnWidth : 0.32,
										layout : "form",
										items : [{
													xtype : "textfield",
													fieldLabel : "Duties",
													anchor : "100%",
													allowBlank : true,
													maxLength : 50,
													name : "visitDuty",
													id : "visitDuty",
													tabIndex : 3
												}, {
													xtype : "textfield",
													fieldLabel : "Remark",
													anchor : "100%",
													allowBlank : true,
													maxLength : 300,
													name : "visitRemark",
													id : "visitRemark",
													tabIndex : 6
												}]
									}, {
										xtype : "panel",
										title : "",
										columnWidth : 1,
										layout : "form",
										items : [{
													xtype : "textarea",
													fieldLabel : "Reasons for visit",
													anchor : "98%",
													allowBlank : true,
													maxLength : 300,
													name : "visitReason",
													id : "visitReason",
													tabIndex : 7
												}, {
													xtype : "hidden",
													id : 'id',
													name : 'id'
												}]
									}]
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
		var custId = $("cId").value;
		if (custId != 'null' && custId != '') {
			customerBox.bindPageValue('CotCustomer', 'id', custId);
		}
		if (id != 'null' && id != '') {
			customerVisitedLogService.getCustomerVisitedLogById(parseInt(id),
					function(res) {
						if (res != null) {
							var obj = res;
							DWRUtil.setValues(obj);

							if (res.cotCustomerId != 'null'
									&& res.cotCustomerId != '') {
								customerBox.bindPageValue('CotCustomer', 'id',
										res.cotCustomerId);
							}
							// 来访时间
							Ext.getCmp("visitTime").setValue(res.visitTime);
						}
					});
		}
		$('visitPeople').focus();
	}
	// 初始化页面
	initForm();
});
function mod() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var form = Ext.getCmp("visitedlogFormId").getForm();
	if (!form.isValid())
		return;
	var obj = DWRUtil.getValues("visitedlogForm");
	var list = new Array();
	var customerVisitedLog = new CustomerVisitedLog();
	for (var p in customerVisitedLog) {
		customerVisitedLog[p] = obj[p];
	}

	if (id == null || id == "") {
		DWREngine.setAsync(false);
		customerVisitedLogService.getTimestamp(customerVisitedLog.visitTime,
				function(res) {
					customerVisitedLog.visitTime = res;
				})
		list.push(customerVisitedLog);
		customerVisitedLogService.addCustomerVisitedLog(list, function(res) {
					Ext.Msg.alert("Message", "Successfully added！");
					closeandreflashEC(true, "visitedlogGrid", false);
				});
		DWREngine.setAsync(true);
	} else {
		DWREngine.setAsync(false);
		customerVisitedLogService.getTimestamp(customerVisitedLog.visitTime,
				function(res) {
					customerVisitedLog.visitTime = res;
				})
		customerVisitedLog.id = DWRUtil.getValue("eId");
		list.push(customerVisitedLog)
		customerVisitedLogService.modifyCustomerVisitedLog(list, function(res) {
					Ext.Msg.alert("Message", "Successfully modified!");
					closeandreflashEC(true, "visitedlogGrid", false);
				})
		DWREngine.setAsync(true);
	}
}