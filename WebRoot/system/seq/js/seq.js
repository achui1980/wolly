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
						name : "seqCfg"
					}, {
						name : "currentSeq"
					}, {
						name : "zeroType"
					}, {
						name : "currentDay"
					}, {
						name : "hisDay"
					}, {
						name : "type"
					}, {
						name : "name"
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
									url : "cotseq.do?method=query"
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
									header : "Rule Name",
									dataIndex : "name",
									width : 120
								}, {
									header : "Expression",
									dataIndex : "seqCfg",
									width : 200
								}, {
									header : "Current sequence number",
									dataIndex : "currentSeq",
									width : 100
								}, {
									header : "Clear mode",
									dataIndex : "zeroType",
									renderer : function(value) {
										if (value == 0)
											return "System";
										else if (value == 1)
											return "Year";
										else if (value == 2)
											return "Monthly";
										else if (value == 3)
											return "Daily";
									},
									width : 100
								}, {
									header : "Operation",
									dataIndex : "id",
									renderer : function(value, metaData,
											record, rowIndex, colIndex, store) {
										var mod = '<a href="#" onclick="windowopenMod('
												+ value + ')">Update</a>';
										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="#" onclick="del('
												+ value + ')">Delete</a>';
										var zero = '<a href="#" onclick="zero('
												+ value + ')">Clear</a>';
										return zero + nbsp + mod + nbsp + del;
									}
								}]
					});
			var toolBar = new Ext.PagingToolbar({
						pageSize : 20,
						store : ds,
						displayInfo : false,
						displayMsg : 'Displaying {0} - {1} of {2}',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display",
						listeners : {
							beforechange : function(pTbar, params) {
								pTbar.store.setBaseParam('limit', params.limit);
							}
						}
					});

			var seqBox = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotSeq&key=name",
						cmpId : 'orderNoFind',
						emptyText : "The name of a  number",
						editable : true,
						valueField : "id",
						displayField : "name",
						pageSize : 5,
						width : 150,
						selectOnFocus : true,
						sendMethod : "post",
						minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
						listWidth : 350,// 下
						triggerAction : 'all',
						isSearchField : true,
						searchName : 'orderNoFind'
					});

			var tb = new Ext.ux.SearchComboxToolbar({
						items : ['-', seqBox, {
									xtype : 'searchcombo',
									width : 1,
									cls : 'hideCombo',
									editable : false,
									isJsonType : false,
									store : ds
								}, '->', {
									text : "Create",
									handler : windowopenAdd,
									iconCls : "page_add",
									cls : "SYSOP_ADD"
								}, '-', {
									text : "Update",
									handler : windowopenMod.createDelegate(
											this, [null]),
									iconCls : "page_mod",
									cls : "SYSOP_MOD"
								}, '-', {
									text : "Delete",
									handler : deleteBatch,
									iconCls : "page_del",
									cls : "SYSOP_DEL"
								}]
					});

			var grid = new Ext.grid.GridPanel({
						region : "center",
						id : "seqGrid",
						stripeRows : true,
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
			var viewport = new Ext.Viewport({
						layout : 'fit',
						items : [grid]
					});
			viewport.doLayout();

			// 表格双击
			grid.on('rowdblclick', function(grid, rowIndex, event) {
						var record = grid.getStore().getAt(rowIndex);
						windowopenMod(record.get("id"));
					});

			// 批量删除
			function deleteBatch() {
				var list = getIds();
				if (list.length == 0) {
					Ext.MessageBox.alert("Message", "Please select records");
					return;
				}
				Ext.MessageBox.confirm('Message', "Are you sure to delete the number setting checked?", function(btn) {
							if (btn == 'yes') {
								cotSeqService.delCotSeq(list, function(res) {
											Ext.MessageBox
													.alert("Message", "Deleted successfully");
											reloadGrid("seqGrid");
										});
							}
						});
			}

			// 新增
			function windowopenAdd() {
				// 添加权限判断
				var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
				if (isPopedom == 0)// 没有添加权限
				{
					Ext.MessageBox.alert("Message", "Sorry, you do not have Authority! ");
					return;
				}
				openFullWindow('cotseq.do?method=add');
			}

		});
// 表格中删除
function del(id) {
	// var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	// if (isPopedom == 0) {
	// Ext.MessageBox.alert("提示消息", "您没有删除权限");
	// return;
	// }
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('Message', "Are you sure to delete the number setting checked?", function(btn) {
				if (btn == 'yes') {
					cotSeqService.delCotSeq(list, function(res) {
								Ext.MessageBox.alert("Message", "Deleted successfully");
								reloadGrid("seqGrid");
							});
				}
			});
}
function zero(id) {
	Ext.MessageBox.confirm('Message', "Are you sure you checked the order number to set zero?", function(btn) {
				if (btn == 'yes') {
					cotSeqService.zero(id, function(res) {
								if (res >= 0) {
									Ext.MessageBox.alert("Message", "Clear success");
									reloadGrid("seqGrid");
								} else {
									Ext.MessageBox.alert("Message", "Clear failures");
								}

							});
				}
			});
}
function getIds() {
	var list = Ext.getCmp("seqGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}

// 打开订单编辑页面
function windowopenMod(obj) {
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		} else
			obj = ids[0];

	}
	openFullWindow('cotseq.do?method=add&id=' + obj);
}
