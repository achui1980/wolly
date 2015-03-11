Ext.onReady(function() {

	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	// 员工列表
	var empsBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
				name : 'msgToPerson',
				id : 'msgTo',
				cmpId : 'msgToPerson',
				disabledClass : 'combo-disabled',
				fieldLabel : "<font color=red>消息接收人</font>",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				sendMethod : "post",
				pageSize : 5,
				anchor : "95%",
				tabIndex : 5,
				selectOnFocus : true,
				emptyText : '请选择',
				allowBlank : false,
				blankText : '消息接收人不能为空',
				
				
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
				id : 'msgType',
				fieldLabel : '消息提醒类型',
				store : typeStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 3,
				value:1,
				emptyText : '请选择',
				hiddenName : 'msgTypeId',
				selectOnFocus : true,
				listeners : {
					"select" : function(com, rec, index) {
						empsBox.reset();
						var msgTypeId = rec.data.id;
						if (msgTypeId == 1) {
							cotMessageService.getCurEmp(function(res) {
										empsBox.bindPageValue('CotEmps', 'id',
												res.id);
									});
						}
						if (msgTypeId == 2) {
							Ext.getCmp('msgTo').setDisabled(false);
						}
						if (msgTypeId == 3) {
							cotMessageService.getCurEmp(function(res) {
										empsBox.bindPageValue('CotEmps', 'id',
												res.id);
									});
							Ext.getCmp('msgTo').setDisabled(true);
						}
						if (msgTypeId == 4) {
							Ext.getCmp('msgTo').setDisabled(true);
							Ext.getCmp('msgTo').setValue('ALL');
						}
					}
				}
			});

	var flagStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[1, '是'], [0, '否']]
			});

	var flagField = new Ext.form.ComboBox({
				name : 'msgFlag',
				fieldLabel : '是否提醒',
				editable : true,
				store : flagStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				value : 1,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 6,
				emptyText : '请选择',
				hiddenName : 'msgFlag',
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
					formBind : true,
					handler : mod,
					iconCls : "page_table_save"
				}, {
					text : "取消",
					iconCls : "page_cancel",
					handler : function() {
						closeandreflashEC(true, 'messageGrid', false)
					}
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
									id : "msgBeginDate",
									name : 'msgBeginDate',
									fieldLabel : "<font color=red>提醒起始日期</font>",
									allowBlank : false,
									value : new Date(),
									blankText : '起始日期不能为空',
									emptyText : "请选择日期",
									format : "Y-m-d",
									tabIndex : 1,
									anchor : "100%",
									vtype : 'daterange',
									endDateField : 'msgEndDate'
								}, typeField, flagField]
					}, {
						xtype : "panel",
						layout : "form",
						columnWidth : 0.5,
						items : [{
									xtype : "datefield",
									id : "msgEndDate",
									name : 'msgEndDate',
									fieldLabel : "<font color=red>提醒结束日期</font>",
									allowBlank : false,
									blankText : '结束日期不能为空',
									emptyText : "请选择日期",
									format : "Y-m-d",
									tabIndex : 2,
									anchor : "95%",
									vtype : 'daterange',
									startDateField : 'msgBeginDate'
								}, empsBox]
					}]
		}, {
			xtype : "panel",
			layout : "form",
			items : [{
						xtype : "textarea",
						name : 'msgContent',
						id : 'msgContent',
						fieldLabel : "提醒内容",
						anchor : "98%",
						tabIndex : 4,
						height : 110
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
		var flag = $('flag').value;
		typeField.setValue(flag);
		if (flag == 3) {
			empsBox.bindPageValue('CotEmps', 'id', logId);
			Ext.getCmp('msgTo').setDisabled(true);
		}
	}
	// 初始化页面
	initForm();

});

function mod() {
	var msgtoPerson = Ext.getCmp('msgTo').value;
	var obj = DWRUtil.getValues("messageForm");
	var url = "cotmessage.do?method=query";
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
	cotMessageService.saveMessage(cotMessage, url, $('msgBeginDate').value,
			$('msgEndDate').value, function(res) {
				Ext.Msg.alert('提示框', "添加成功！");
				closeandreflashEC(true, 'messageGrid', false);
			});
	DWREngine.setAsync(true);
}
