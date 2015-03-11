var auditGridWin = new Ext.Window({
			layout : "fit",
			padding : "10",
			items : [MyForm, MyGrid]
		})

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
			name : " auditNo"
		}, {
			name : " totalTui"
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
				// handler : createaudit,
				iconCls : "page_add"
					//cls : "SYSOP_ADD"
				}]
		});
var MyGrid = new Ext.grid.GridPanel({
			id : "grid",
			stripeRows : true,
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
