var empBox = null;
var curBox = null;
var targetPortBox = null;
var invoiceNoBox = null;
Ext.onReady(function() {
	var auditstatus = [["1", "空白"], ["2", "领单"], ["3", "交单"], ["4", "核销"],
			["5", "逾期"], ["6", "迟交"]];
	// 业务员
	empBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName",
				disabled : true,
				disabledClass : 'combo-disabled',
				cmpId : 'businessPerson',
				fieldLabel : "业务员",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				allowBlank : false,
				blankText : "请选择业务员！",
				sendMethod : "post",
				selectOnFocus : true,
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 币种
	curBox = new BindCombox({
				cmpId : 'currencyId',
				id : "currencyId_cmp",
				disabled : true,
				disabledClass : 'combo-disabled',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : '请选择',
				fieldLabel : "币种",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				allowBlank : true,
				blankText : "请选择币种！",
				listeners : {
					"select" : function(com, rec, index) {
						composeChange();
						priceBlurByCurreny(rec.data.id);
					}
				}
			});
	// 目的港
	targetPortBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotTargetPort&key=targetPortEnName",
		cmpId : 'targetPortId',
		id : "targetPortId_cmp",
		fieldLabel : "目的港",
		disabled : true,
		disabledClass : 'combo-disabled',
		editable : true,
		valueField : "id",
		allowBlank : true,
		displayField : "targetPortEnName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "100%",
		sendMethod : "post",
		selectOnFocus : true,
		
		
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350
	});
	// 出货货号下拉框
	invoiceNoBox = new BindCombox({
				dataUrl : "cotorderout.do?method=queryOrderOutNo",
				cmpId : 'invoiceNo',
				id : "invoiceNo_cmp",
				fieldLabel : "发票编号",
				disabled : true,
				disabledClass : 'combo-disabled',
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "orderNo",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				triggerAction : "all",
				listWidth : 350,// 下
				listeners : {
					"select" : function(com, rec, index) {
						setOrderFacNo(rec.get("id"));
					}
				}
			});
	var form = new Ext.form.FormPanel({
		title : "",
		labelWidth : 60,
		labelAlign : "right",
		layout : "form",
		formId : "auditForm",
		width : 822,
		buttonAlign : "center",
		renderTo : Ext.getBody(),
		padding : "5px",
		autoHeight : true,
		autoWidth : true,
		frame : true,
		fbar : [{
					text : "保存",
					iconCls : "page_table_save",
					handler : save
				}, {
					text : "取消"
				}],
		items : [{
					xtype : "panel",
					title : "",
					layout : "column",
					hideBorders : true,
					items : [{
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								labelWidth : 70,
								items : [{
											xtype : "textfield",
											disabled : true,
											disabledClass : 'combo-disabled',
											fieldLabel : "核销单编号",
											anchor : "100%",
											id : "auditNo",
											name : "auditNo"
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "datefield",
											fieldLabel : "领单日期",
											disabled : true,
											disabledClass : 'combo-disabled',
											anchor : "100%",
											id : "receiveDate",
											name : "receiveDate",
											format : "Y-m-d",
											tabIndex : ""
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "datefield",
											fieldLabel : "有效日期",
											disabled : true,
											disabledClass : 'combo-disabled',
											anchor : "100%",
											id : "effectDate",
											name : "effectDate",
											format : "Y-m-d",
											tabIndex : ""
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "datefield",
											fieldLabel : "领用日期",
											disabled : true,
											disabledClass : 'combo-disabled',
											anchor : "100%",
											id : "usedDate",
											name : "usedDate",
											format : "Y-m-d",
											tabIndex : ""
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "combo",
											disabled : true,
											disabledClass : 'combo-disabled',
											store : new Ext.data.ArrayStore({
														fields : ['value',
																'text'],
														data : auditstatus
													}),
											mode : 'local',
											id : "auditStatus_Cmp",
											valueField : 'value',
											displayField : 'text',
											triggerAction : "all",
											hiddenName : "auditStatus",
											emptyText : "请选择",
											fieldLabel : "单据状态",
											anchor : "100%"

										}]
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "column",
					id : "submitDiv",
					// hidden:true,
					items : [{
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "datefield",
											disabled : true,
											disabledClass : 'combo-disabled',
											fieldLabel : "交单日期",
											anchor : "100%",
											id : "submitDate",
											name : "submitDate",
											format : "Y-m-d",
											tabIndex : ""
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [invoiceNoBox]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [curBox]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [targetPortBox]
							}, {
								xtype : "panel",
								title : "",
								columnWidth : 0.2,
								layout : "form",
								items : [{
											xtype : "numberfield",
											disabled : true,
											disabledClass : 'combo-disabled',
											fieldLabel : "收汇金额",
											anchor : "100%",
											id : "totalMoney",
											name : "totalMoney",
											tabIndex : ""
										}]
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "column",
					id : "auditDiv",
					// hidden:true,
					items : [{
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "datefield",
											disabled : true,
											disabledClass : 'combo-disabled',
											fieldLabel : "核销日期",
											anchor : "100%",
											allowBlank : true,
											id : "auditDate",
											name : "auditDate",
											format : "Y-m-d",
											tabIndex : 1
										}, {
											xtype : "numberfield",
											fieldLabel : "现汇金额",
											disabled : true,
											disabledClass : 'combo-disabled',
											anchor : "100%",
											allowBlank : true,
											id : "cashFee",
											name : "cashFee",
											maxValue : 10000000,
											tabIndex : 6
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "datefield",
											disabled : true,
											disabledClass : 'combo-disabled',
											fieldLabel : "结算日期",
											anchor : "100%",
											allowBlank : true,
											id : "balanceDate",
											name : "balanceDate",
											format : "Y-m-d",
											tabIndex : 2
										}, {
											layout : "column",
											hideBorders : true,
											items : [{
												xtype : "panel",
												title : "",
												layout : "form",
												columnWidth : 0.9,
												items : [{
															xtype : "numberfield",
															disabled : true,
															disabledClass : 'combo-disabled',
															fieldLabel : "退税金额",
															anchor : "100%",
															allowBlank : true,
															id : "totalTui",
															name : "totalTui",
															maxValue : 10000000,
															tabIndex : 7
														}]
											}, {
												xtype : "panel",
												title : "",
												layout : "column",
												columnWidth : 0.1,
												items : [{
															xtype : "button",
															iconCls : "cal",
															handler : calTuiLv
														}]
											}]

										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "numberfield",
											disabled : true,
											disabledClass : 'combo-disabled',
											fieldLabel : "结汇次数",
											anchor : "100%",
											allowBlank : true,
											id : "calCount",
											name : "calCount",
											maxValue : 99,
											tabIndex : 3
										}, empBox]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "numberfield",
											disabled : true,
											disabledClass : 'combo-disabled',
											fieldLabel : "银行费用",
											anchor : "100%",
											allowBlank : true,
											id : "bankFee",
											name : "bankFee",
											maxValue : 10000000,
											tabIndex : 4
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "numberfield",
											disabled : false,
											fieldLabel : "佣金回扣",
											anchor : "100%",
											allowBlank : true,
											id : "commisionScal",
											name : "commisionScal",
											maxValue : 10000000,
											tabIndex : 5
										}]
							}]
				}, {
					xtype : "panel",
					title : "",
					// hidden:true,
					layout : "form",
					height : "",
					id : "timeoutDiv",
					items : [{
								xtype : "textarea",
								fieldLabel : "逾期原因",
								anchor : "100%",
								id : "auditReason",
								disabled : false,
								name : "auditReason"
							}]
				}, {
					xtype : "panel",
					title : "",
					// hidden:true,
					layout : "form",
					id : "delayDiv",
					items : [{
								xtype : "textarea",
								id : "auditReasonDelay",
								name : "auditReasonDelay",
								disabled : false,
								fieldLabel : "迟交原因",
								anchor : "100%"
							}]
				}]
	});
	initForm();
});
// 页面加载调用
function initForm() {
	var id = $('mainId').value;
	DWREngine.setAsync(false);
	if (id != 'null' && id != '') {
		cotAuditService.getCotAuditById(id, function(res) {
			if (res.auditStatus == 4) {
				Ext.getCmp("submitDiv").setVisible(true);
				Ext.getCmp("auditDiv").setVisible(true);
				Ext.getCmp("delayDiv").setVisible(false);
				Ext.getCmp("timeoutDiv").setVisible(false);
			}
			if (res.auditStatus == 5) {
				Ext.getCmp("auditReason").setDisabled(false);
				Ext.getCmp("submitDiv").setVisible(true);
				Ext.getCmp("auditDiv").setVisible(true);
				Ext.getCmp("timeoutDiv").setVisible(true);;
				Ext.getCmp("delayDiv").setVisible(false);
				Ext.getCmp("auditStatus_Cmp").setValue("5");
			}
			if (res.auditStatus == 6) {
				Ext.getCmp("auditReasonDelay").setDisabled(false);
				Ext.getCmp("submitDiv").setVisible(true);
				Ext.getCmp("auditDiv").setVisible(true);
				Ext.getCmp("delayDiv").setVisible(true);
				Ext.getCmp("timeoutDiv").setVisible(false);
				Ext.getCmp("auditStatus_Cmp").setValue("6");
			}

			if (res.receiveDate != null) {
				var receiveDate = new Date(res.receiveDate);
				$('receiveDate').value = Ext.util.Format.date(receiveDate,
						'Y-m-d');
			}
			if (res.effectDate != null) {
				var effectDate = new Date(res.effectDate);
				$('effectDate').value = Ext.util.Format.date(effectDate,
						'Y-m-d');
			}
			if (res.usedDate != null) {

				var usedDate = new Date(res.usedDate);
				$('usedDate').value = Ext.util.Format.date(usedDate, 'Y-m-d');
			}
			if (res.submitDate != null) {
				var submitDate = new Date(res.submitDate);
				$('submitDate').value = Ext.util.Format.date(submitDate,
						'Y-m-d');
			}
			if (res.auditDate != null) {
				var auditDate = new Date(res.auditDate);
				$('auditDate').value = Ext.util.Format.date(auditDate, 'Y-m-d');
			}
			if (res.balanceDate != null) {
				var balanceDate = new Date(res.balanceDate);
				$('balanceDate').value = Ext.util.Format.date(balanceDate,
						'Y-m-d');
			}
			if (res.currencyId != null) {
				curBox.bindValue(res.currencyId);
			}
			if (res.targetPortId != null) {
				targetPortBox.bindPageValue("CotTargetPort", "id",
						res.targetPortId)
			}
			if (res.invoiceNo != null) {
				invoiceNoBox.bindPageValue("CotOrderOut", "orderNo",
						res.invoiceNo)
			}

			DWRUtil.setValues(res);
			if (res.businessPerson != null) {
				empBox.bindPageValue("CotEmps", "id", res.businessPerson);
			} else {
				cotAuditService.getEmps(function(res) {

							empBox.bindPageValue("CotEmps", "id", res.id);
						});
			}
		});
	}
	if ($('flag').value == 2) {
		Ext.getCmp("auditStatus_Cmp").setValue("2")
		Ext.getCmp("usedDate").setDisabled(true);
		$('usedDate').value = getCurrentDate('yyyy-MM-dd');
		Ext.getCmp("submitDiv").setVisible(false);
		Ext.getCmp("auditDiv").setVisible(false);
		Ext.getCmp("timeoutDiv").setVisible(false);
		Ext.getCmp("delayDiv").setVisible(false);
	}
	if ($('flag').value == 3) {
		Ext.getCmp("auditStatus_Cmp").setValue("3");
		Ext.getCmp("submitDate").setDisabled(false);
		Ext.getCmp("invoiceNo_cmp").setDisabled(false);
		Ext.getCmp("currencyId_cmp").setDisabled(false);
		Ext.getCmp("targetPortId_cmp").setDisabled(false);
		Ext.getCmp("totalMoney").setDisabled(false);
		Ext.getCmp("submitDiv").setVisible(true);
		Ext.getCmp("auditDiv").setVisible(false);
		Ext.getCmp("timeoutDiv").setVisible(false);
		Ext.getCmp("delayDiv").setVisible(false);
		$('submitDate').value = getCurrentDate('yyyy-MM-dd');
	}
	if ($('flag').value == 4) {
		Ext.getCmp("auditStatus_Cmp").setValue("4");
		Ext.getCmp("auditDate").setDisabled(false);
		Ext.getCmp("balanceDate").setDisabled(false);
		Ext.getCmp("totalTui").setDisabled(false);
		Ext.getCmp("calCount").setDisabled(false);
		Ext.getCmp("bankFee").setDisabled(false);
		Ext.getCmp("bankFee").setDisabled(false);
		Ext.getCmp("cashFee").setDisabled(false);
		Ext.getCmp("cashFee").setDisabled(false);
		Ext.getCmp("submitDiv").setVisible(true);
		Ext.getCmp("auditDiv").setVisible(true);
		Ext.getCmp("timeoutDiv").setVisible(false);
		Ext.getCmp("delayDiv").setVisible(false);
		$('auditDate').value = getCurrentDate('yyyy-MM-dd');
	}

	DWREngine.setAsync(true);
}
//根据选择的编号自动填写表单,供查询表格调用
function setOrderFacNo(orderOutId) {
	//隐藏查询客户层
	cotAuditService.getOrderOutById(orderOutId, function(res) {
				if (res.targetportId != null) {// 设置目的港
					targetPortBox.bindPageValue("CotTargetPort", "id",
							res.targetPortId);
				}
				if (res.currencyId != null) {// 设置币种
					curBox.bindValue(res.currencyId);
				}
				if (res.totalMoney != null) {
					$('totalMoney').value = res.totalMoney;
				}
			});
	//invoiceNoBox.bindPageValue("CotOrderOut","id",orderOutId);
}
//计算退税金额
function calTuiLv() {
	var orderNo = Ext.getCmp("invoiceNo_cmp").getValue();
	alert(orderNo)
	cotAuditService.calTotalTuiLv(orderNo, function(res) {
				$('totalTui').value = res;
			});
}