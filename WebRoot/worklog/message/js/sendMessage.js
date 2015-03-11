Ext.onReady(function() {

	// 为组件添加事件
	Ext.form.Label.prototype.afterRender = Ext.form.Label.prototype.afterRender
			.createSequence(function() {
						this.relayEvents(this.el, ['click']);
					});

	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	// 员工列表
	var empsBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
				name : 'msgToPerson',
				id : 'msgTo',
				cmpId : 'msgToPerson',
				fieldLabel : "消息接收人",
				editable : true,
				disabled : true,
				valueField : "id",
				disabledClass : 'combo-disabled',
				displayField : "empsName",
				pageSize : 5,
				anchor : "95%",
				sendMethod : "post",
				selectOnFocus : true,
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	/***************************************************************************
	 * 描述: 本地下拉列表
	 **************************************************************************/
	var typeStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[1, '提醒'], [2, '互发'], [3, '工作计划'], [4, '群发']]
			});

	var typeField = new Ext.form.ComboBox({
				name : 'msgTypeId',
				fieldLabel : '消息类型',
				disabledClass : 'combo-disabled',
				editable : true,
				store : typeStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				disabled : true,
				emptyText : '请选择',
				hiddenName : 'msgTypeId',
				selectOnFocus : true
			});

	var form = new Ext.form.FormPanel({
		labelWidth : 90,
		labelAlign : "right",
		padding : "5px",
		formId : "messageForm",
		id : "messageFormId",
		frame : true,
		buttonAlign : "center",
		monitorValid : true,
		fbar : [{
					text : "保存",
					id : 'saveBtn',
					formBind : true,
					handler : mod,
					iconCls : "page_table_save"
				}, {
					text : "删除",
					id : 'delBtn',
					handler : del,
					iconCls : "page_del"
				}, {
					text : "取消",
					id : 'cancelBtn',
					iconCls : "page_cancel",
					handler : cancel
				}],
		items : [{
			xtype : "panel",
			layout : "column",
			items : [{
						xtype : "panel",
						layout : "form",
						columnWidth : 0.5,
						items : [{
									xtype : "datefield",
									name : 'msgBeginDate',
									id : "msgBeginDate",
									fieldLabel : "<font color=red>提醒起始日期</font>",
									blankText : '起始日期不能为空',
									emptyText : "请选择日期",
									allowBlank : false,
									format : "Y-m-d",
									anchor : "100%",
									vtype : 'daterange',
									endDateField : 'msgEndDate'
								}]
					}, {
						xtype : "panel",
						layout : "form",
						columnWidth : 0.5,
						items : [{
									xtype : "datefield",
									name : 'msgEndDate',
									id : "msgEndDate",
									fieldLabel : "<font color=red>提醒结束日期</font>",
									allowBlank : false,
									blankText : '结束日期不能为空',
									emptyText : "请选择日期",
									format : "Y-m-d",
									anchor : "95%",
									vtype : 'daterange',
									startDateField : 'msgBeginDate'
								}]
					}, {
						xtype : "panel",
						layout : "column",
						columnWidth : 1,
						items : [{
									xtype : "panel",
									title : "",
									layout : "form",
									columnWidth : 0.5,
									items : [{
												xtype : "textfield",
												name : 'msgOrderNo',
												fieldLabel : "提醒单号",
												anchor : "100%"
											}, typeField]
								}, {
									xtype : "panel",
									layout : "form",
									columnWidth : 0.5,
									items : [empsBox]
								}]
					}, {
						xtype : "panel",
						columnWidth : 1,
						layout : "column",
						items : [{
									xtype : "label",
									labelStyle : 'text-align:right',
									text : "消息状态:",
									style : {
										marginTop : '3px',
										marginLeft : '32px'
									},
									width : 65
								}, {
									xtype : "radio",
									width : 60,
									id : 'rd0',
									disabledClass : 'combo-disabled',
									name : "msgStatus",
									inputValue : 0,
									checked : true,
									boxLabel : "未读"
								}, {
									xtype : "radio",
									id : 'rd1',
									name : "msgStatus",
									inputValue : 1,
									disabledClass : 'combo-disabled',
									boxLabel : "已读，未处理"
								}, {
									xtype : "radio",
									id : 'rd2',
									name : "msgStatus",
									inputValue : 2,
									disabledClass : 'combo-disabled',
									boxLabel : "已读，已处理"
								}, {
									xtype : "radio",
									width : 160,
									name : "msgStatus",
									id : 'rd3',
									inputValue : 3,
									disabled : true,
									disabledClass : 'combo-disabled',
									boxLabel : "未处理，提醒已结束"
								}, {
									xtype : "radio",
									width : 160,
									name : "msgStatus",
									id : 'rd4',
									inputValue : 4,
									disabled : true,
									hidden : true,
									disabledClass : 'combo-disabled',
									boxLabel : "只读"
								}]
					}]
		}, {
			xtype : "panel",
			layout : "form",
			bodyStyle:'marginTop:10px',
			items : [{
						xtype : "textarea",
						id : 'msgContent',
						name : 'msgContent',
						fieldLabel : "提醒内容",
						anchor : "98%",
						height : 100
					}, {
						xtype : "hidden",
						name : 'id',
						id : 'id'
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
		DWREngine.setAsync(false);
		var id = $("eid").value;
		cotMessageService.getMessageById(parseInt(id), function(res) {
					DWRUtil.setValues(res);
					typeField.setValue(res.msgTypeId);
					// 起始时间
					Ext.getCmp("msgBeginDate").setValue(res.msgBeginDate);
					Ext.getCmp("msgEndDate").setValue(res.msgEndDate);

					// 加载接收人信息
					if (res.msgToPerson == 0) {
						Ext.getCmp('msgTo').setValue('ALL');
					} else {
						empsBox.bindPageValue('CotEmps', 'id', res.msgToPerson);
					}

					if (res.msgStatus == 0) {
						Ext.getCmp("rd1").setDisabled(true);
						Ext.getCmp("rd2").setDisabled(true);
						if (res.msgTypeId == 2) {
							Ext.getCmp('msgTo').setDisabled(false);
						}
					}

					if (res.msgStatus == 1) {
						Ext.getCmp("delBtn").setVisible(false);
						Ext.getCmp("saveBtn").setVisible(false);
						Ext.getCmp("rd0").setDisabled(true);
						Ext.getCmp("rd2").setDisabled(true);
					}

					if (res.msgStatus == 2) {
						// Ext.getCmp("delBtn").setVisible(false);
						Ext.getCmp("saveBtn").setVisible(false);
						Ext.getCmp("rd0").setDisabled(true);
						Ext.getCmp("rd1").setDisabled(true);
					}

					if (res.msgStatus == 3) {
						// Ext.getCmp("rd0").setDisabled(true);
						Ext.getCmp("rd1").setDisabled(true);
						Ext.getCmp("rd2").setDisabled(true);
						Ext.getCmp("rd3").setDisabled(false);
						if (res.msgTypeId == 1 || res.msgTypeId == 2) {
							Ext.getCmp('msgTo').setDisabled(false);
						}
					}

				});
		DWREngine.setAsync(true);
	}
	// 初始化页面
	initForm();

});

function mod() {
	var msgtoPerson = Ext.getCmp('msgTo').value;
	var obj = DWRUtil.getValues("messageForm");
	var cotMessage = new CotMessage();
	var list = new Array();
	for (var p in cotMessage) {
		if (p != 'msgBeginDate' && p != 'msgEndDate') {
			cotMessage[p] = obj[p];
		}
	}
	if (msgtoPerson == 'ALL') {
		cotMessage.msgToPerson = 0;
	}

	list.push(cotMessage)

	DWREngine.setAsync(false);
	cotMessageService.updateMessage(cotMessage, $('msgBeginDate').value,
			$('msgEndDate').value, function(res) {
				if (res != null) {
					Ext.Msg.alert('提示框', "修改成功！");
					closeandreflashEC(true, 'messageGrid', false);
				} else {
					Ext.Msg.alert('提示框', "修改失败！");
				}
			});
	DWREngine.setAsync(true);
}

function del() {
	// 删除权限
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert('提示框', "您没有删除权限");
		return;
	}
	Ext.MessageBox.confirm('提示信息', '确定删除?', function(btn) {
				if (btn == 'yes') {
					var cotMessage = new CotMessage();
					var list = new Array();
					cotMessage.id = $("eid").value;
					list.push(cotMessage)
					cotMessageService.deleteMessage(list, function(res) {
								result = res;
								if (result == -1) {
									Ext.Msg.alert('提示框', "已有其它记录使用到该记录,无法删除")
									return;
								}
								Ext.Msg.alert('提示框', "删除成功!");
								closeandreflashEC(true, "messageGrid", false);
							})
				} else {
					return;
				}
			})
}

function cancel() {
	closeandreflashEC(true, "messageGrid", false);
}
