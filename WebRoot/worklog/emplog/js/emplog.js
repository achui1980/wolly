Ext.onReady(function() {
			DWREngine.setAsync(false);
			var empData;
			// 加载价格条款表缓存
			baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName",
					function(res) {
						empData = res;
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
						name : "empId"
					}, {
						name : "logDate"
					}, {
						name : "logContent"
					}, {
						name : "logAdvise"
					}, {
						name : "remark"
					}, {
						name : "logCheck"
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
									url : "cotemplog.do?method=query"
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
									header : "员工",
									dataIndex : "empId",
									width : 130,
									renderer : function(value) {
										return empData["" + value];
									}
								}, {
									header : "日期",
									dataIndex : "logDate",
									width : 100,
									renderer : function(value) {
										if (value != null) {
											return Ext.util.Format.date(
													new Date(value.time),
													"Y-m-d");
										}
									}
								}, {
									header : "工作内容",
									dataIndex : "logContent",
									width : 200
								}, {
									header : "是否批注",
									dataIndex : "logCheck",
									width : 80,
									renderer : function(value) {
										if (value != 1) {
											return "<font color='green'>未批注</font>";
										} else {
											return "<font color='red'>已批注</font>";
										}
									}

								}, {
									header : "主管批注",
									dataIndex : "logAdvise",
									width : 200
								}, {
									header : "备注",
									dataIndex : "remark",
									width : 200
								}, {
									header : "操作",
									dataIndex : "id",
									renderer : function(value, metaData,
											record, rowIndex, colIndex, store) {
										var mod = '<a href="javascript:windowopenMod('
												+ value + ')">修改</a>';
										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="javascript:del('
												+ value + ')">删除</a>';
										return mod + nbsp + del;
									}
								}]
					});
			var toolBar = new Ext.PagingToolbar({
						pageSize : 20,
						store : ds,
						displayInfo : false,
						//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display",
						listeners : {
							beforechange : function(pTbar, params) {
								pTbar.store.setBaseParam('limit', params.limit);
							}
						}
					});

			// 是否批注
			var shenStore = new Ext.data.SimpleStore({
						fields : ["tp", "name"],
						data : [['', '批注状态'], [-1, '未批注'], [1, '已批注']]
					});
			var shenBox = new Ext.form.ComboBox({
						emptyText : '批注状态',
						editable : false,
						store : shenStore,
						valueField : "tp",
						displayField : "name",
						mode : 'local',
						validateOnBlur : true,
						triggerAction : 'all',
						width : 120,
						hiddenName : 'logCheck',
						selectOnFocus : true,
						isSearchField : true,
						searchName : 'logCheck'
					});
			// 业务员
			var busiBox = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
						cmpId : 'empId',
						emptyText : "业务员",
						editable : true,
						valueField : "id",
						displayField : "empsName",
						pageSize : 5,
						width : 120,
						selectOnFocus : true,
						sendMethod : "post",
						minChars : 1,
						listWidth : 350,
						triggerAction : 'all',
						isSearchField : true,
						searchName : 'empId'
					});

			var tb = new Ext.ux.SearchComboxToolbar({
						items : ['-', {
									xtype : "datefield",
									emptyText : "起始时间",
									width : 100,
									format : "Y-m-d",
									id : 'startTime',
									vtype : 'daterange',
									endDateField : 'endTime',
									isSearchField : true,
									searchName : 'startTime'
								}, {
									xtype : "datefield",
									emptyText : "结束时间",
									width : 100,
									format : "Y-m-d",
									id : 'endTime',
									vtype : 'daterange',
									startDateField : 'startTime',
									isSearchField : true,
									searchName : 'endTime'
								}, busiBox, shenBox, {
									xtype : 'searchcombo',
									width : 1,
									cls : 'hideCombo',
									editable : false,
									isJsonType : false,
									store : ds
								}, '->', {
									text : "新建",
									handler : windowopenAdd,
									iconCls : "page_add",
									cls : "SYSOP_ADD"
								}, '-', {
									text : "修改",
									handler : windowopenMod.createDelegate(
											this, [null]),
									iconCls : "page_mod",
									cls : "SYSOP_MOD"
								}, '-', {
									text : "删除",
									handler : deleteBatch,
									iconCls : "page_del",
									cls : "SYSOP_DEL"
								}]
					});

			var grid = new Ext.grid.GridPanel({
						region : "center",
						id : "empLogGrid",
						stripeRows : true,
						bodyStyle : 'width:100%',
						store : ds, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
						loadMask : true, // 是否显示正在加载
						tbar : tb,
						border : false,
						bbar : toolBar,
						viewConfig : {
							forceFit : false
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
					Ext.MessageBox.alert("提示信息", "请选择记录");
					return;
				}
				Ext.MessageBox.confirm('提示信息', "是否确定删除勾选的工作日志?", function(btn) {
							if (btn == 'yes') {
								cotEmpLogService.deleteEmpLogByList(list,
										function(res) {
											Ext.MessageBox
													.alert("提示信息", "删除成功");
											reloadGrid("empLogGrid");
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
					Ext.MessageBox.alert("提示信息", "您没有添加权限");
					return;
				}
				openWindowBase(300, 500, 'cotemplog.do?method=add');
			}

		});
// 表格中删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("提示消息", "您没有删除权限");
		return;
	}
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('提示信息', "是否确定删除此工作日志?", function(btn) {
				if (btn == 'yes') {
					cotEmpLogService.deleteEmpLogByList(list, function(res) {
								if (res == 0) {
									Ext.MessageBox.alert("提示消息", "删除成功!");
									reloadGrid("empLogGrid");
								} else {
									Ext.MessageBox.alert("提示消息", "删除失败!");
								}
							});
				}
			});
}
function getIds() {
	var list = Ext.getCmp("empLogGrid").getSelectionModel().getSelections();
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
			Ext.MessageBox.alert("提示消息", "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("提示消息", "只能选择一条记录!")
			return;
		} else
			obj = ids[0];

	}
	openWindowBase(300, 500, 'cotemplog.do?method=add&id=' + obj);
}
