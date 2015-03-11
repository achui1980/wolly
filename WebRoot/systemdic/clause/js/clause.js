Ext.onReady(function() {

	var calMap;
	baseDataUtil.getBaseDicDataMap("CotCalculation", "id", "calName", function(
					res) {
				calMap = res;
			});

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "clauseName"
			}, {
				name : "calId"
			}, {
				name : "clauseRemark"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotclause.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
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
							header : "Delivery",
							dataIndex : "clauseName",
							width : 120
						}, {
							header : "Formula Name",
							dataIndex : "calId",
							width : 120,
							hidden:true,
							renderer : function(value) {
								return calMap[value];
							}
						}, {
							header : "Remark",
							dataIndex : "clauseRemark",
							width : 300
						}, {
							header : "Operation",
							dataIndex : "id",
							renderer : function(value) {
								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:del(' + value
										+ ')">Delete</a>';
								return del + nbsp;
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true
				////displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				//emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Del",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}]
			});
	var grid = new Ext.grid.GridPanel({

				margins : '0 2 0 0',
				region : "center",
				id : "clauseGrid",
				// autoHeight:true,
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				// selModel: new
				// Ext.grid.RowSelectionModel({singleSelect:false}),
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				// layout:"fit",
				viewConfig : {
					forceFit : true
				}
			});

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});
	/*----------------------------------------------------*/

	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	var calName = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCalculation",
				valueField : "id",
				fieldLabel : "Formula Name",
				sendMethod : "post",
				hidden:true,
				hideLabel:true,
				value:1,
				// allowBlank : false,
				// blankText : "公式名称不能为空",
				displayField : "calName",
				cmpId : "calId",
				emptyText : 'Choose',
				tabIndex : 4,
				anchor : "100%"
			});

	var form = new Ext.form.FormPanel({
				title : "Add|Edit",
				region : 'east',
				id : "clauseFormId",
				formId : "clauseForm",
				frame : true,
				collapsible : true,
				monitorValid : true,
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				buttonAlign : 'center',
				labelWidth : 60,
				items : [{
							xtype : 'textfield',
							fieldLabel : "Delivery",
							id : "clauseName",
							name : "clauseName",
							allowBlank : false,
							blankText : "Delivery can not be empty",
							anchor : "100%"
						}, calName, {
							xtype : "textarea",
							fieldLabel : "Remark",
							id : "clauseRemark",
							name : "clauseRemark",
							anchor : "100%"
						}, new Ext.form.Hidden({
									id : "id",
									name : "id"
								})],
				buttons : [{
							formBind : true,
							text : "Create",
							id : 'saveBtn',
							cls : "SYSOP_ADD",
							iconCls : "page_add",
							handler : saveOrUpdate
						}, {
							text : "Reset",
							iconCls : "page_reset",
							handler : function() {
								form.getForm().reset();
								Ext.getCmp('saveBtn').setText("Create");
								Ext.getCmp('saveBtn').setIconClass('page_add');
							}
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid, form]
			});

	// ds.on('beforeload', function() {
	// ds.baseParams = form.getForm().getValues();
	// });

	// 单击修改信息 start
	grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				form.getForm().loadRecord(record);
				if (record.data.calId == 0) {
					calName.bindValue('');
				} else {
					calName.bindValue(record.data.calId);
				}
				Ext.getCmp('saveBtn').setText("Update");
				Ext.getCmp('saveBtn').setIconClass('page_mod');
			});
		// 单击修改信息 end
	});
// 添加或修改记录
function saveOrUpdate() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var name = Ext.get("clauseName").getValue();
	var form = Ext.getCmp("clauseFormId").getForm();
	if (!form.isValid())
		return;
	var obj = DWRUtil.getValues("clauseForm");
	var list = new Array();
	list.push(obj);
	if (id == null || id == "") {
		var isExist = false;
		DWREngine.setAsync(false);
		cotClauseService.findExistByName(name, function(res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.Msg.alert("Message", "Terms of the name of the same name already exists");
			return;
		}
		cotClauseService.addClause(list, function(res) {
					Ext.Msg.alert("Message", "Successfully added!");
					reloadGrid("clauseGrid");
				});
		DWREngine.setAsync(true);
	} else {
		cotClauseService.modifyClause(list, function(res) {
					if (res) {
						Ext.Msg.alert("Message", "Successfully modified!");
						reloadGrid("clauseGrid");
					} else {
						Ext.Msg.alert("Message", "Change failed!");
					}
				});
	}
}
// 删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	var isPopedom = 1
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "'Sorry, you do not have Authority!");
		return;
	}
	var cotClause = new CotClause();
	var list = new Array();
	cotClause.id = id;
	list.push(cotClause);
	Ext.MessageBox.confirm('Message', 'You sure you want to delete?', function(btn) {
				if (btn == 'yes') {
					cotClauseService.deleteClause(list, function(res) {
								if (res == 0) {
									reloadGrid("clauseGrid");
									clearForm("clauseFormId");
								} else {
									Ext.Msg.alert("Message", "Can not delete");
								}
							})
				}
			});
}
function getIds() {
	var list = Ext.getCmp("clauseGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotClause = new CotClause();
				cotClause.id = item.id
				res.push(cotClause);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Please select a record");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure you want to delete?', function(btn) {
				if (btn == 'yes') {
					cotClauseService.deleteClause(list, function(res) {
								result = res;
								if (result == 0) {
									Ext.Msg.alert("Message", "Deleted successfully");
									reloadGrid("clauseGrid");
									clearForm("clauseFormId");
								} else {
									Ext.Msg.alert("Message", "Can not delete")
									return;
								}
							});
				} else {
					return;
				}
			});
}
