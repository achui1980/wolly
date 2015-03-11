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
						name : "calName"
					}, {
						name : "expressionOut"
					}, {
						name : "remark"
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
									url : "cotcalculation.do?method=query"
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
									header : "Formula Name",
									dataIndex : "calName",
									width : 120
								}, {
									header : "External Expressions",
									dataIndex : "expressionOut",
									width : 160
								}, {
									header : "Remarks",
									dataIndex : "remark",
									width : 300
								}, {
									header : "Operation",
									dataIndex : "id",
									renderer : function(value) {
										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="javascript:del('
												+ value + ')">Delete</a>';
										return del + nbsp;
									}
								}]
					});
			var toolBar = new Ext.PagingToolbar({
						pageSize : 20,
						store : ds,
						displayInfo : true,
						displayMsg : 'Showing {0} - {1} {2} record total records',
						emptyMsg : "No records",
						listeners : {
							beforechange : function(pTbar, params) {
								pTbar.store.setBaseParam('limit', params.limit);
							}
						}
					});

			var tb = new Ext.ux.SearchComboxToolbar({
						items : ['-', {
									xtype : 'searchcombo',
									width : 100,
									emptyText : "Formula Name",
									isSearchField : true,
									searchName : 'calNameFind',
									isJsonType : false,
									store : ds
								}, '->', {
									xtype : 'label',
									style : 'color:blue',
									text : 'Note: Calculation of inverse margin named----'
								}, {
									xtype : 'label',
									style : 'color:red',
									text : 'InverseMargin'
								}, '-', {
									text : "Create",
									cls : "SYSOP_ADD",
									iconCls : "page_add",
									handler : windowopenAdd
								}, '-', {
									text : "Update",
									cls : "SYSOP_MOD",
									iconCls : "gird_edit",
									handler : windowopenMod.createDelegate(
											this, [null])
								}, '-', {
									text : "Delete",
									cls : "SYSOP_DEL",
									iconCls : "page_del",
									handler : deleteBatch
								}]
					});
			var grid = new Ext.grid.GridPanel({
						id : "calculationGrid",
						stripeRows : true,
						store : ds, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
						loadMask : true, // 是否显示正在加载
						tbar : tb,
						border : false,
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
						windowopenMod(record.get("id"));
					});

			// 获得选择的记录
			function getIds() {
				var list = Ext.getCmp("calculationGrid").getSelectionModel()
						.getSelections();
				var res = new Array();
				Ext.each(list, function(item) {
							var cotCalculation = new CotCalculation();
							cotCalculation.id = item.id
							res.push(cotCalculation);
						});
				return res;
			}

			// 批量删除
			function deleteBatch() {
				var list = getIds();
				if (list.length == 0) {
					Ext.Msg.alert("Message", "Pls. select records");
					return;
				}
				Ext.MessageBox.confirm('Message', 'Sure to delete the selected quotation formulas?', function(btn) {
							if (btn == 'yes') {
								cotCalculationService.deleteCalculation(list,
										function(res) {
											result = res;
											if (result == 0) {
												Ext.Msg.alert("Message", "Deleted successfully");
												reloadGrid("calculationGrid");
												clearForm("calculationFormId");
											} else {
												Ext.Msg.alert("Message",
														"Other records have been used to the record, can not be removed")
												return;
											}
										});
							} else {
								return;
							}
						});
			}

			// 打开新增页面
			function windowopenAdd() {
				// 添加权限判断
				var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
				if (isPopedom == 0)// 没有添加权限
				{
					Ext.Msg.alert("Message", "orry, you do not have Authority!");
					return;
				}
				openWindowBase(400, 740, 'cotcalculation.do?method=add');
			}

			// 打开编辑页面
			function windowopenMod(obj) {
				// 添加权限判断
				var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
				if (isPopedom == 0)// 没有修改权限
				{
					Ext.Msg.alert("Message", "orry, you do not have Authority!");
					return;
				}
				if (obj == null) {
					var ids = getIds();
					if (ids.length == 0) {
						Ext.Msg.alert("Message", "Please select a record");
						return;
					} else if (ids.length > 1) {
						Ext.Msg.alert("Message", "Choose only one record!")
						return;
					} else {
						obj = ids[0].id;
					}
				}
				openWindowBase(400, 740, 'cotcalculation.do?method=add&id='
								+ obj);
			}
		});

// 删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	var isPopedom = 1
	if (isPopedom == 0)// 没有删除权限
	{
		alert("Choose only one record");
		return;
	}
	var cotCalculation = new CotCalculation();
	var list = new Array();
	cotCalculation.id = id;
	list.push(cotCalculation);
	Ext.MessageBox.confirm('Message', 'Sure to delete the selected quotation formulas?', function(btn) {
				if (btn == 'yes') {
					cotCalculationService.deleteCalculation(list,
							function(res) {
								if (res == 0) {
									reloadGrid("calculationGrid");
									clearForm("calculationFormId");
								} else {
									Ext.Msg.alert("Message", "Other records have been used to the record, can not be removed");
								}
							})
				}
			});
}