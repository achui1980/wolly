Ext.onReady(function() {

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
				name : 'msgFromPerson',
				id : 'msgFrom',
				cmpId : 'msgFromPerson',
				fieldLabel : "消息发送人",
				editable : true,
				valueField : "id",
				disabledClass : 'combo-disabled',
				displayField : "empsName",
				pageSize : 5,
				sendMethod : "post",
				anchor : "95%",
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
				editable : true,
				disabledClass : 'combo-disabled',
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
		layout:'form',
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
					hidden : true,
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
									format : "Y-m-d",
									anchor : "100%",
									blankText : '起始日期不能为空',
									emptyText : "请选择日期",
									allowBlank : false,
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
									format : "Y-m-d",
									anchor : "95%",
									allowBlank : false,
									blankText : '结束日期不能为空',
									emptyText : "请选择日期",
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
						id : 'statusDiv',
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
									name : "msgStatus",
									inputValue : 0,
									checked : true,
									boxLabel : "未读"
								}, {
									xtype : "radio",
									id : 'rd1',
									name : "msgStatus",
									inputValue : 1,
									boxLabel : "已读，未处理"
								}, {
									xtype : "radio",
									id : 'rd2',
									name : "msgStatus",
									inputValue : 2,
									boxLabel : "已读，已处理"
								}, {
									xtype : "radio",
									width : 140,
									name : "msgStatus",
									id : 'rd3',
									inputValue : 3,
									disabled : true,
									boxLabel : "未处理，提醒已结束"
								}, {
									xtype : "radio",
									width : 160,
									name : "msgStatus",
									id : 'rd4',
									inputValue : 4,
									hidden : true,
									boxLabel : "只读"
								}, {
									xtype : "label",
									id : 'label1',
									text : "单击该处进行处理",
									style : {
										color : 'red',
										marginTop : '3px',
										cursor : 'hand'
									},
									width : 100,
									listeners : {
										'click' : {
											fn : function(field) {
												openUrl();
											},
											scope : this
										}
									}
								}]
					}]
		}, {
			xtype : "panel",
			layout : "form",
			bodyStyle:'marginTop:10px',
			items : [{
						xtype : "textarea",
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
		var recv = false;
		cotMessageService.sendIsRecv(id, function(flag) {
					if (flag == true) {
						recv = true;
					}
				});

		cotMessageService.getMessageById(parseInt(id), function(res) {
			DWRUtil.setValues(res);
			typeField.setValue(res.msgTypeId);
			
			Ext.getCmp("msgBeginDate").setValue(res.msgBeginDate);
			Ext.getCmp("msgEndDate").setValue(res.msgEndDate);

			// 加载发送人信息
			empsBox.bindPageValue('CotEmps', 'id', res.msgFromPerson);
			Ext.getCmp('msgFrom').setDisabled(true);

			if (res.msgTypeId == 4) {
				cotMessageService.sendIsCur(id, function(sendFlag) {
							if (sendFlag) {
								Ext.getCmp("delBtn").setVisible(true);
							}
						});
				Ext.getCmp("saveBtn").setVisible(false);
				Ext.getCmp("rd0").setDisabled(true);
				Ext.getCmp("rd1").setDisabled(true);
				Ext.getCmp("rd2").setDisabled(true);
				Ext.getCmp("rd3").setDisabled(true);
				Ext.getCmp("rd4").setVisible(true);
			}

			if (res.msgStatus == 0) {
				if (res.msgTypeId == 3 || (res.msgTypeId == 1 && recv)) {
					Ext.getCmp("delBtn").setVisible(true);
				}
			}

			if (res.msgStatus == 1) {
				Ext.getCmp("rd0").setDisabled(true);
				if (res.msgTypeId == 3 || (res.msgTypeId == 1 && recv == true)) {
					Ext.getCmp("delBtn").setVisible(true);
				}
			}

			if (res.msgStatus == 2) {
				Ext.getCmp("rd0").setDisabled(true);
				if (res.msgTypeId == 3 || (res.msgTypeId == 1 && recv == true)) {
					Ext.getCmp("delBtn").setVisible(true);
				}
			}

			if (res.msgStatus == 3) {
				// Ext.getCmp("rd0").setDisabled(true);
				Ext.getCmp("rd1").setDisabled(true);
				Ext.getCmp("rd2").setDisabled(true);
				Ext.getCmp("rd3").setDisabled(false);
				if (res.msgTypeId == 3 || (res.msgTypeId == 1 && recv == true)) {
					Ext.getCmp("delBtn").setVisible(true);
				}
				if (res.msgTypeId == 1) {
					Ext.getCmp("label1").hide();
				}
				if (res.msgTypeId == 2) {
					Ext.getCmp("saveBtn").setVisible(false);
				}
				if (!recv) {
					Ext.getCmp("rd0").setDisabled(true);
				}
			}

			if (res.msgTypeId != 1) {
				Ext.getCmp("label1").hide();
			}
		});
		DWREngine.setAsync(true);
	}
	// 初始化页面
	initForm();

});

function mod() {
	var obj = DWRUtil.getValues("messageForm");
	var cotMessage = new CotMessage();
	var list = new Array();
	for (var p in cotMessage) {
		if (p != 'msgBeginDate' && p != 'msgEndDate') {
			cotMessage[p] = obj[p];
		}
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
	Ext.MessageBox.confirm('提示信息', '是否确定删除?', function(btn) {
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
	var id = DWRUtil.getValue("eid");
	cotMessageService.updateMsgStatus(id, function(res) {
				closeandreflashEC(true, "messageGrid", false);
			});
}

function openUrl() {
	var id = $("eid").value;
	cotMessageService.getMessageById(parseInt(id), function(res) {
				if (res.msgTable == 'CotMessage') {
					Ext.Msg.alert("提示框", "互发、群发及工作计划直接在此页面处理");
					return;
				} else {
					openCustWindow(res.msgEditAction);
				}
			});
}
