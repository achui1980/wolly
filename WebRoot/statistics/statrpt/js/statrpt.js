Ext.onReady(function() {
	var dataMap;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotStatistics", "id", "statName",
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
				name : "statName"
			}, {
				name : "statType"
			},{
				name : "statParent",
				type : "int"
			},{
				name : "statFile"
			},{
				name : "statUrl"
			},{
				name : "statOrderBy"
			},{
				name : "statOrder"
			},{
				name : "statLv"
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
							url : "cotstatistics.do?method=querystatistics"
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
		columns : [sm,{
					header : "Operation",
					dataIndex : "id",
					width:150,
					renderer : function(value) {
						var mod = '<a href="javascript:modwindowopen(' + value
								+ ')">Update</a>';
						var nbsp = "&nbsp &nbsp &nbsp"
						var del = '<a href="javascript:del(' + value
								+ ')">Delete</a>';
						return mod + nbsp + del;
					}
				}, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "Report Type",
					dataIndex : "statParent",
					width:250,
					renderer : function(value) {
						return dataMap[value];
					}
				}, {
					header : "Report Name",
					dataIndex : "statName",
					width:300
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No records",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 报表类型
	var rptTypeBox = new BindCombox({
		        dataUrl : "./servlet/DataSelvert?tbname=CotStatistics&key=statName&typeName=statFile&type=0",				
				cmpId : 'rptType',
				emptyText : "Report Type",
				editable : true,
				valueField : "id",
				displayField : "statName",
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

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', rptTypeBox,{
							xtype : 'searchcombo',
							width : 140,
							emptyText : "Report Name",
							searchName : 'statName',
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
				id : "statisGrid",
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
					forceFit : false
				}
			});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
		var record = grid.getStore().getAt(rowIndex);
		openWindowBase(300, 670, 'cotstatistics.do?method=modify&id=' + record.id);
			
	});
});
// 删除
function del(id) {
    var res = new Array();
    res.push(id);
	Ext.MessageBox.confirm('Message', 'Are you sure to delete the report?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					cotStatPopedomService.delStatsById(res,
							function(res) {
								if(res==0){
									Ext.MessageBox.alert("Message", "Deleted successfully!");
								}else{
									Ext.MessageBox.alert("Message", "The record is referenced, can not be removed!");
								}
							});
					reloadGrid("statisGrid");
					DWREngine.setAsync(true);
				}
			});

}
function getIds() {
	var list = Ext.getCmp("statisGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert("Message", "Please select record!");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure to delete the selected report?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					var list = getIds();
					cotStatPopedomService.delStatsById(list,
							function(res) {
								if(res==0){
									Ext.MessageBox.alert("Message", "Deleted successfully!");
								}else{
									Ext.MessageBox.alert("Message", "There are "+res+ " records have been cited, can not be removed!");
								}
								
								
							})
					reloadGrid("statisGrid")
				}
			});
}
function addwindowopen() {
	openWindowBase(300, 670, 'cotstatistics.do?method=modify');
}
function modwindowopen(value) {
	openWindowBase(300, 670, 'cotstatistics.do?method=modify&id=' + value);
}

