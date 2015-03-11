Ext.onReady(function() {
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
				name : "roleName"
			}, {
				name : "roleStatus",
				type : "int"
			}, {
				name : "roleRemark"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotrole.do?method=query"
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
							header : "Role name",
							dataIndex : "roleName",
							width : 120
						}, {
							header : "Status",
							dataIndex : "roleStatus",
							width : 130,
							renderer : function(value) {
								if (value == 0)
									return "Disable";
								else if (value == 1)
									return "Enable";
							}
						}, {
							header : "Remarks",
							dataIndex : "roleRemark",
							width : 300
						}, {
							header : "Operation",
							dataIndex : "id",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var popedom = '<a href="javascript:openRolePopedomWin('
										+ value + ')">Authority</a>';
								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:del(' + value
										+ ')">Delete</a>';
								return del + nbsp + popedom;
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				iconCls : "gird_list",
				region : "center",
				id : "roleGrid",
				stripeRows : true,
				margins : "0 5 0 0",
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	ds.on('beforeload', function() {
				ds.baseParams = {};
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});
	/*----------------------------------------------------*/

	var form = new Ext.form.FormPanel({
				title : "Add | Edit",
				region : 'east',
				id : "roleFormId",
				formId : "roleForm",
				frame : true,
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				labelWidth : 60,
				defaultType : 'textfield',
				monitorValid : true,
				buttonAlign : "center",
				defaults : {
					width : 200,
					allowBlank : false
				},
				items : [{

							// 添加布局
							fieldLabel : "Role name",
							id : "roleName",
							name : "roleName",
							allowBlank : false,
							blankText : "Role name can not be empty",
							anchor : "95%"

						}, {
							xtype : "combo",
							fieldLabel : 'Status',
							transform : "roleStatus",
							triggerAction : 'all',
							lazyRender : true,
							editable : false,
							mode : 'local',
							hiddenName : "roleStatus",
							anchor : "95%"
							// emptyText:'请选择'
					}	, {
							xtype : "textarea",
							fieldLabel : "Remarks",
							id : "roleRemark",
							name : "roleRemark",
							allowBlank : true,
							anchor : "95%"
						}, new Ext.form.Hidden({
									id : "id",
									name : "id"
								})],
				buttons : [{
							text : "Added",
							iconCls : "page_add",
							cls : "SYSOP_MOD",
							formBind : true,
							id : "saveBtn",
							handler : saveOrUpdate
						}, {
							text : "Reset",
							iconCls : "page_reset",
							handler : function() {
								form.getForm().reset();
								Ext.getCmp('saveBtn').setText("Added");
								Ext.getCmp('saveBtn').setIconClass('page_add');
							}
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid, form]
			});

	// 单击修改信息 start
	grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				form.getForm().loadRecord(record);
				Ext.getCmp("saveBtn").setText("Update");
				Ext.getCmp("saveBtn").setIconClass("page_mod");
			});
});
// 添加或修改记录
function saveOrUpdate() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var roleName = Ext.get("roleName").getValue();
	var form = Ext.getCmp("roleFormId").getForm();
	if (!form.isValid())
		return;
	if (id == null || id == "") {
		// 表单验证结束
		var obj = DWRUtil.getValues("roleForm");
		var cotRole = new CotRole();
		var list = new Array();
		for (var p in obj) {
			cotRole[p] = obj[p];
		}
		list.push(cotRole);
		var isExist = false;
		DWREngine.setAsync(false);
		cotRoleService.findExistByName(cotRole.roleName, function(res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.Msg.alert("Message", "Employees the same name already exists");
			return;
		}
		// 添加员工
		cotRoleService.addRole(list, function(res) {
					reloadGrid("roleGrid");
				});
		DWREngine.setAsync(true);
	} else {
		// 表单验证结束
		var obj = DWRUtil.getValues("roleForm");
		var cotRole = new CotRole();
		var list = new Array();
		for (var p in obj) {
			cotRole[p] = obj[p];
		}
		cotRole.id = id;
		cotRole.roleStatus = parseInt(obj.roleStatus);
		list.push(cotRole);
		cotRoleService.modifyRole(list, function(res) {
					if (res) {
						Ext.Msg.alert("Message", "Successfully modified");
						reloadGrid("roleGrid");
					} else {
						Ext.Msg.alert("Message", "Change fails, the role of existing staff!");
					}
				});
	}

}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure you remove the role?', function(btn) {
				if (btn == 'yes') {
					var obj = DWRUtil.getValues("roleForm");
					var cotRole = new CotRole();
					var list = new Array();
					for (var p in obj) {
						cotRole[p] = obj[p];
					}
					cotRole.id = id;
					list.push(cotRole);
					cotRoleService.deleteRole(list, function(res) {
								result = res;
								if (result == -1) {
									Ext.Msg.alert("Message", "Other records have been used to the record, can not be removed")
									return;
								}
								Ext.Msg.alert("Message", "Deleted successfully");
								reloadGrid("roleGrid");
								clearForm("roleFormId");
							});
				}
			});
}
function getIds() {
	var list = Ext.getCmp("roleGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotRole = new CotRole();
				cotRole.id = item.id
				res.push(cotRole);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Please select records");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure to delete the selected role?', function(btn) {
				if (btn == 'yes') {
					cotRoleService.deleteRole(list, function(res) {
								Ext.Msg.alert("Message", "Deleted successfully!");
								reloadGrid("roleGrid");
								clearForm("roleFormId");

							});
				}
			});
}
function openRolePopedomWin(obj, rolename) {
	openWindowBase(600, 400, 'cotpopedom.do?method=queryRolePopedom&id=' + obj
					+ '&rolename=' + rolename);
}
