/**
 * 询盘编辑页面主表单
 * 
 * @class QH.panAdd.FormPanel
 * @extends Ext.form.FormPanel
 */
QH.panAdd.FormPanel = Ext.extend(Ext.form.FormPanel, {
	labelAlign : "right",
	layout : "column",
	formId : "panForm",
	border : false,
	bodyStyle : 'background:#dfe8f6',
	ctCls : 'x-panel-mc',
	padding : 8,
	initComponent : function() {
		var form = this;
		var isExist = true;
		this.items = [{
					xtype : "panel",
					columnWidth : 0.15,
					layout : "form",
					labelWidth : 80,
					items : [{
						xtype : 'bindCombo',
						dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
						id : 'addPersonCombo',
						cmpId : 'addPerson',
						fieldLabel : "<font color=red>Request by</font>",
						editable : true,
						valueField : "id",
						displayField : "empsName",
						emptyText : 'Choose',
						pageSize : 10,
						anchor : "100%",
						disabled : true,
						disabledClass : 'combo-disabled',
						allowBlank : false,
						blankText : "Please choose Sale!",
						tabIndex : 4,
						sendMethod : "post",
						selectOnFocus : true,
						minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
						listWidth : 350,// 下
						triggerAction : 'all',
						listeners:{
							'afterrender':function(comb){
								if(!form.modId){
									comb.bindPageValue('CotEmps', 'id', logId);
								}
							}
						}
					}]
				}, {
					xtype : "panel",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 70,
					items : [{
								xtype : 'bindCombo',
								dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
								valueField : "id",
								fieldLabel : "<font color=red>Department</font>",
								autoShow : true,
								allowBlank : false,
								blankText : "Please select Department!",
								sendMethod : "post",
								displayField : "typeEnName",
								id : "deptCombo",
								cmpId : "deptId",
								tabIndex : 5,
								emptyText : 'Choose',
								anchor : "100%"
							}]
				},{
					xtype : "panel",
					columnWidth : 0.15,
					layout : "form",
					labelWidth : 50,
					items : [{
								xtype : "datefield",
								fieldLabel : "<font color=red>Date</font>",
								anchor : "100%",
								format : "d/m/Y",
								id : "addTime",
								name : "addTime",
								value : new Date(),
								tabIndex : 1
							}]
				},{
					xtype : "panel",
					columnWidth : 0.20,
					layout : "form",
					labelWidth : 80,
					items : [{
								xtype : "textfield",
								fieldLabel : "<font color=red>Title</font>",
								id : "priceNo",
								name : "priceNo",
								allowBlank : false,
								invalidText : "Prs enter the Title!",
								anchor : "100%",
								tabIndex : 2
							}]
				},{
					xtype : "panel",
					columnWidth : 0.15,
					layout : "form",
					labelWidth : 80,
					items : [{
								xtype : "datefield",
								fieldLabel : "<font color=red>DeadLine</font>",
								id : "valDate",
								name : "valDate",
								allowBlank : false,
								anchor : "100%",
								tabIndex : 2
							}]
				}, {
					xtype : "panel",
					columnWidth : 0.15,
					layout : "form",
					items : [{
						xtype : "panel",
						layout : "column",
						anchor : "100%",
						items : [{
							xtype : "panel",
							layout : "form",
							columnWidth : 1,
							labelWidth : 80,
							items : [{
								xtype : "textfield",
								fieldLabel : "<font color=red>Prs No.</font>",
								anchor : "100%",
								id : "prsNo",
								tabIndex : 3,
								blankText : "Please enter Prs No.!",
								name : "prsNo",
								maxLength : 100,
								allowBlank : false,
								invalidText : "Prs No. already exists, enter the new!",
								validationEvent : 'change',
								validator : function(thisText) {
									if (thisText != '') {
										cotPanService.findIsExistPanNo(
												thisText, dwr.util
														.getValue('pId'),
												function(res) {
													if (res) {
														isExist = false;
													} else {
														isExist = true;
													}
												});
									}
									return isExist;
								},
								listeners : {
									"render" : function(obj) {
										var tip = new Ext.ToolTip({
											target : obj.getEl(),
											anchor : 'left',
											maxWidth : 160,
											minWidth : 160,
											html : 'Click the right button to automatically generate the Title!'
										});
									}
								}
							}]
						}, {
							xtype : "button",
							width : 20,
							text : "",
							cls : "SYSOP_ADD",
							iconCls : "cal",
							handler : form.getPanNo.createDelegate(this),
							listeners : {
								"render" : function(obj) {
									var tip = new Ext.ToolTip({
										target : obj.getEl(),
										anchor : 'left',
										maxWidth : 160,
										minWidth : 160,
										html : 'Depending on the configuration automatically generated Title!'
									});
								}
							}
						}]
					}]
				},  {
					xtype : "panel",
					columnWidth : 0.25,
					layout : "form",
					labelWidth : 80,
					items : [{
								xtype : "textarea",
								fieldLabel : "Prefered manufactorer",
								id : "factoryStr",
								name : "factoryStr",
								anchor : "100%",
								height:45,
								tabIndex : 6,
								maxLength : 300
							}]
				}, {
					xtype : "panel",
					columnWidth : 0.25,
					layout : "form",
					labelWidth : 80,
					items : [{
						xtype : "textarea",
						height:45,
						fieldLabel : "Wolly order refefrence",
						anchor : "100%",
						id : "orderNo",
						name : "orderNo",
						tabIndex : 8,
						maxLength : 300
					}]
				}, {
					xtype : "panel",
					columnWidth : 0.25,
					layout : "form",
					labelWidth : 80,
					items : [{
						xtype : "textarea",
						height:45,
						fieldLabel : "Intended client",
						anchor : "100%",
						id : "customerStr",
						name : "customerStr",
						tabIndex : 7,
						maxLength : 300
					}]
				}, {
					xtype : "panel",
					columnWidth : 0.25,
					layout : "form",
					labelWidth : 80,
					items : [{
								xtype : "textarea",
								fieldLabel : "Comment",
								anchor : "100%",
								height:45,
								id : "remark",
								name : "remark",
								tabIndex : 9
							}]
				}]
		QH.panAdd.FormPanel.superclass.initComponent.call(this);
		this.on('afterrender',function(formPanel){
			if(formPanel.modId){
				cotPanService.getPanById(formPanel.modId, function(res) {
							var bsForm=formPanel.getForm();
							bsForm.setValues(res);
							Ext.getCmp('addPersonCombo').bindPageValue('CotEmps', 'id', res.addPerson);
							Ext.getCmp('deptCombo').bindValue(res.deptId);
						});
			}
		})
	},
	getPanNo:function() {
		var currDate = $('addTime').value;
		var empId = Ext.getCmp('addPersonCombo').getValue();
		if (currDate == "") {
			Ext.MessageBox.alert("Message", "Please select a date");
			return;
		}
		if (empId == null || empId == "") {
			Ext.MessageBox.alert("Message", "Please select a sales");
			return;
		}

		var id = $('pId').value;
		if (id == 'null' || id == '') {
			cotSeqService.getPanNo(empId, currDate, function(ps) {
						$('prsNo').value = ps;
					});
		}
	}
});

Ext.reg('panaddform', QH.panAdd.FormPanel);