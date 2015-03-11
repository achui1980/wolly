Ext.onReady(function() {
	var dataMap;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotRptType", "id", "rptName",
			function(res) {
				dataMap = res;
			});
	DWREngine.setAsync(true);

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
				name : "rptName"
			}, {
				name : "rptType",
				type : "int"
			}, {
				name : "flag",
				type : "int"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotrptfile.do?method=query"
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
					header : "Type",
					dataIndex : "rptType",
					width : 120,
					renderer : function(value) {
						return dataMap["" + value];
					}
				}, {
					header : "Name",
					dataIndex : "rptName",
					width : 120
				}, {
					header : "Default",
					dataIndex : "flag",
					width : 120,
					renderer : function(value) {
						if (value == 1) {
							return "<span style='color:green;font-weight:bold;'>Yes</span>";
						} else {
							return "<span style='color:red;font-weight:bold;'>No</span>";
						}
					}
				}, {
					header : "Operation",
					dataIndex : "id",
					renderer : function(value) {
						var mod = '<a href="javascript:modwindowopen(' + value
								+ ')">Update</a>';
						var nbsp = "&nbsp &nbsp &nbsp"
						var del = '<a href="javascript:del(' + value
								+ ')">Delete</a>';
						var def = '<a href="javascript:setRptDefault(' + value
								+ ')">default</a>';
						return mod + nbsp + del + nbsp + def;
					}
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 报表类型
	var rptTypeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotRptType&key=rptName",
				cmpId : 'rptType',
				emptyText : "Type",
				editable : true,
				valueField : "id",
				displayField : "rptName",
				pageSize : 5,
				width : 150,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'rptType'
			});
	// 报表名称
	var rptFileBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotRptFile&key=rptName",
				cmpId : 'rptFile',
				emptyText : "Name",
				editable : true,
				valueField : "id",
				displayField : "rptName",
				pageSize : 5,
				width : 150,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'rptFile'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', rptTypeBox, rptFileBox, {
							xtype : 'searchcombo',
							width : 1,
							cls : 'hideCombo',
							editable : false,
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							handler : addwindowopen,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}, '-', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				iconCls : "gird_list",
				region : "center",
				id : "rptGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				border : false,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
		var record = grid.getStore().getAt(rowIndex);
		openWindowBase(180, 500, 'cotrptfile.do?method=modify&id=' + record.id);
			// var record = grid.getStore().getAt(rowIndex);
			// form.getForm().loadRecord(record);
	});
});
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.MessageBox.alert("Message", "You do not have permission to delete!");
		return;
	}

	Ext.MessageBox.confirm('Message', 'Are you sure to delete the report?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					cotRptFileService.delRptFileById(parseInt(id),
							function(res) {
							})
					Ext.MessageBox.alert("Message", "Deleted successfully!");
					reloadGrid("rptGrid");
					DWREngine.setAsync(true);
				}
			});

}
function getIds() {
	var list = Ext.getCmp("rptGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotRptFile = new CotRptFile();
				cotRptFile.id = item.id
				res.push(cotRptFile);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert("Message", "Please select a record");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure to delete the report?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					var list = getIds();
					for (var i = 0; i < list.length; i++) {
						var id = list[i].id;
						cotRptFileService.delRptFileById(parseInt(id),
								function(res) {
								})
					}
					Ext.MessageBox.alert("Message", "Deleted successfully!");
					reloadGrid("rptGrid")
				}
			});
}
function addwindowopen() {
	openWindowBase(380, 500, 'cotrptfile.do?method=add');
}
function modwindowopen(value) {
	openWindowBase(380, 500, 'cotrptfile.do?method=modify&id=' + value);
}

// 设置模板为默认
function setRptDefault(id, flag) {
	if (flag == '1') {
		return;
	}
	cotRptFileService.setRptDefault(id, function(res) {
				if (res == true) {
					reloadGrid("rptGrid");
					Ext.MessageBox.alert("Message", "Setting success!");
				} else {
					Ext.MessageBox.alert("Message", "Setting Default Fail!");
				}
			});
}
