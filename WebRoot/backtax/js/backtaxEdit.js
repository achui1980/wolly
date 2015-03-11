// var busiBox = null;
var summary = new Ext.ux.grid.GridSummary({
			id : "summary1"
		});
Ext.onReady(function() {

	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&flag=filter&key=empsName",
				cmpId : 'empId',
				fieldLabel : "<font color=red>退税人</font>",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				sendMethod : "post",
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,
				allowBlank : false,
				blankText : "请选择退税人",
				tabIndex : 4,
				emptyText : '请选择',
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var form = new Ext.form.FormPanel({
		title : "",
		labelWidth : 60,
		labelAlign : "right",
		height : 110,
		padding : "5px",
		region : "north",
		frame : true,
		formId : "backtaxForm",
		monitorValid : true,
		buttonAlign : "center",
		fbar : [{
					text : "保存",
					iconCls : "page_table_save",
					formBind : true,
					handler : save,
					tabIndex : 5
				}, {
					text : "取消",
					iconCls : "page_cancel",
					tabIndex : 6,
					handler : function() {
						closeandreflashEC(true, 'backtaxGrid', false);
					}
				}],
		items : [{
					xtype : "panel",
					title : "",
					layout : "column",

					items : [{
								xtype : "panel",
								title : "",
								layout : "form",
								labelWidth : 60,
								columnWidth : 0.5,
								items : [{
											xtype : "textfield",
											fieldLabel : "<font color=red>退税单号</font>",
											anchor : "100%",
											id : "taxNo",
											name : "taxNo",
											maxLength : 100,
											allowBlank : false,
											blankText : "请输入退税单号!",
											tabIndex : 1
										}, {
											xtype : "numberfield",
											fieldLabel : "<font color=red>退税金额</font>",
											anchor : "100%",
											itemId : "taxAmount",
											name : "taxAmount",
											maxValue : 99999999.99,
											blankText : "请输入退税金额",
											allowBlank : false,
											tabIndex : 3
										}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								labelWidth : 60,
								columnWidth : 0.5,
								items : [{
											xtype : "datefield",
											fieldLabel : "退税日期",
											anchor : "100%",
											id : "taxDate",
											name : "taxDate",
											format : "Y-m-d",
											allowBlank : false,
											tabIndex : 2
										}, busiBox]
							}]
				}]
	});
	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([

	{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "auditNo"
			}, {
				name : "totalTui"
			}

	]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotbacktax.do?method=addAudit&taxId="
										+ $('mainId').value,
								create : "cotbacktax.do?method=modifAudit&taxId="
										+ $('mainId').value,
								update : "cotbacktax.do?method=modifAudit&taxId="
										+ $('mainId').value,
								destroy : "cotbacktax.do?method=delAudit&taxId="
										+ $('mainId').value
							},
							listeners : {
								// 添加和修改后进入
								exception : function(proxy, type, action,
										options, res, arg) {
									// 从异常中的响应文本判断是否成功
									if (res.status != 200) {
										Ext.Msg.alert("提示消息", "操作失败！");
									} else {
										ds.reload({params : {
											start : 0,
											limit : 15
										}});
									}
								}

							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord),
				writer : writer
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "核销单号",
							dataIndex : "auditNo",
							width : 300,
							editor : new Ext.form.TextField({
										maxLength : 100,
										disabled : true
									}),
							summaryRenderer : function(v, params, data) {
								return "合计：";
							}
						}, {
							header : "退税金额",
							dataIndex : "totalTui",
							width : 200,
							editor : new Ext.form.NumberField({
										maxValue : 999999,
										decimalPrecision : 3
									}),
							summaryType : 'sum'
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : [{
							text : "导入",
							iconCls : "page_add",
							handler : showWin
						}, {
							text : "删除",
							iconCls : "page_del"
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "backtaxGrid",
				stripeRows : true,
				plugins : [summary],
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	var viewport = new Ext.Viewport({
				layout : "border",
				items : [form, grid]
			});
	ds.on('beforeload', function() {
				var queryPara = {
					'taxId' : $('mainId').value,
					'flag' : 'auditTable'
				};
				ds.baseParams = queryPara;
			});
	initForm();
	// 页面加载调用
	function initForm() {
		var id = $('mainId').value;
		DWREngine.setAsync(false);
		if (id == 'null' || id == '') {
			// 初始化退税时间为当前时间
			$('taxDate').value = getCurrentDate("yyyy-MM-dd");
			ds.load({
						params : {
							start : 0,
							limit : 15
						}
					});
			Ext.getCmp("backtaxGrid").hide();
		} else {
			// 加载退税单信息
			cotBackTaxService.getCotBackTaxById(parseInt(id), function(res) {
						DWRUtil.setValues(res);
						if (res.taxDate != null) {
							var date = new Date(res.taxDate);
							$('taxDate').value = Ext.util.Format.date(date,
									'Y-m-d')
						}
						busiBox.bindPageValue("CotEmps", "id", res.empId)
					});
			ds.load({
						params : {
							start : 0,
							limit : 15
						}
					});

		}
		DWREngine.setAsync(true);

	}
	/** *************************核销单查询表格************************************* */

	var MyForm = new Ext.form.FormPanel({
				title : "",
				labelWidth : 70,
				labelAlign : "right",
				layout : "column",
				labelSeparator : "",
				hideBorders : true,
				frame : true,
				bodyBorder : false,
				border : true,
				items : [{
							xtype : "panel",
							title : "",
							columnWidth : 0.35,
							layout : "form",
							items : [{
										xtype : "datefield",
										fieldLabel : "核销日期",
										anchor : "100%"
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.31,
							layout : "form",
							labelWidth : 10,
							items : [{
										xtype : "datefield",
										fieldLabel : "-",
										anchor : "100%",
										labelSeparator : " "
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.25,
							layout : "column",
							items : [{
										xtype : "button",
										text : "查询",
										iconCls : "page_sel",
										hanlder : function() {
											auditDs.reload();
										}
									}, {
										xtype : "button",
										text : "重置",
										iconCls : "page_reset",
										handler : function() {
											MyForm.getForm().reset();
										}
									}]
						}]
			});
	var auditRecord = new Ext.data.Record.create([

	{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "auditNo"
			}, {
				name : "totalTui"
			}

	]);
	// 创建数据源
	var auditDs = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotbacktax.do?method=queryAudit"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, auditRecord)
			});
	var auditToolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : auditDs,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var auditSm = new Ext.grid.CheckboxSelectionModel();
	var auditCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [auditSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "核销单号",
							dataIndex : "auditNo",
							width : 130
						}, {
							header : "退税金额",
							dataIndex : "totalTui",
							width : 130
						}]
			});
	var auditTb = new Ext.Toolbar({
				items : ['->', {
					text : "导入",
					handler : importData,
					iconCls : "page_add"
						// cls : "SYSOP_ADD"
					}]
			});
	var MyGrid = new Ext.grid.GridPanel({
				id : "grid",
				stripeRows : true,
				height : 400,
				bodyStyle : 'width:100%',
				store : auditDs, // 加载数据源
				cm : auditCm, // 加载列
				sm : auditSm,
				loadMask : true, // 是否显示正在加载
				tbar : auditTb,
				bbar : auditToolBar,
				viewConfig : {
					forceFit : true
				}
			});

	function showWin() {
		var auditGridWin = new Ext.Window({
					// layout:"fit",
					width : 600,
					height : 500,
					padding : "10",
					closeAction : "hide",
					items : [MyForm, MyGrid]
				});
		auditGridWin.show();
		auditDs.on('beforeload', function() {
					auditDs.baseParams = MyForm.getForm().getValues();
				});
		auditDs.load({
					params : {
						start : 0,
						limit : 15
					}
				});
	}
	function importData() {
		var list = auditSm.getSelections();
		if (list.length == 0) {
			Ext.Msg.alert("提示信息", "请选择记录");
			return;
		}
		Ext.each(list, function(rec) {
					if (ds.findExact("auditNo", rec.get("auditNo")) != -1)
						return;
					var u = new ds.recordType({
								id : parseInt(rec.get("id")),
								auditNo : rec.get("auditNo"),
								totalTui : rec.get("totalTui")
							});
					u.id = parseInt(rec.get("id"));
					ds.add(u)
				});
	}
});
