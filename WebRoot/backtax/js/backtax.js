Ext.onReady(function() {
			// 加载厂家类表缓存
			var auditstatus = [["1", "相符"], ["2", "不符"]];
			var auditMap = {
				"1" : "相符",
				"2" : "不符"
			}
			DWREngine.setAsync(false);
			var emps = null;
			baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName",
					function(res) {
						emps = res;
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
						name : "taxNo"
					}, {
						name : "taxDate"
					}, {
						name : "taxStatus"
					}, {
						name : "taxAmount"
					}, {
						name : "empId"
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
									url : "cotbacktax.do?method=query"
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
									header : "退税单号",
									dataIndex : "taxNo",
									width : 130
								}, {
									header : "退税日期",
									dataIndex : "taxDate",
									width : 120,
									renderer : function(value) {
										if (value)
											return Ext.util.Format.date(
													new Date(value.time),
													'Y-m-d');
										else
											return value;
									}
								}, {
									header : "退税金额",
									dataIndex : "taxAmount",
									width : 80
								}, {
									header : "状态",
									dataIndex : "taxStatus",
									width : 60,
									renderer : function(value) {
										return auditMap[value]

									}
								}, {
									header : "退税人",
									dataIndex : "empId",
									width : 80,
									renderer : function(value) {
										return emps[value]

									}
								}, {
									header : "操作",
									dataIndex : "id",
									renderer : function(value, metaData,
											record, rowIndex, colIndex, store) {
										var mod = '<a href="javascript:windowopen('
												+ value + ')">修改</a>';
										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="javascript:del('
												+ value + ')">删除</a>';

										return mod + nbsp + del
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

			// 核销状态
			var shenStore = new Ext.data.SimpleStore({
						fields : ["tp", "name"],
						data : [['', '核销状态'], [1, '相符'], [2, '不符']]
					});
			var shenBox = new Ext.form.ComboBox({
						emptyText : '核销状态',
						editable : false,
						store : shenStore,
						valueField : "tp",
						displayField : "name",
						mode : 'local',
						validateOnBlur : true,
						triggerAction : 'all',
						width : 120,
						hiddenName : 'taxStatus',
						selectOnFocus : true,
						isSearchField : true,
						searchName : 'taxStatus'
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
								}, shenBox, {
									xtype : 'searchcombo',
									width : 1,
									cls : 'hideCombo',
									editable : false,
									isJsonType : false,
									store : ds
								}, '->', {
									text : "新建",
									handler : add,
									iconCls : "page_add",
									cls : "SYSOP_ADD"
								}, "-", {
									text : "修改",
									handler : windowopen.createDelegate(this,
											[null]),
									iconCls : "page_mod",
									cls : "SYSOP_MOD"
								}, "-", {
									text : "删除",
									handler : deleteBatch,
									iconCls : "page_del",
									cls : "SYSOP_DEL"
								}

						]
					});

			var grid = new Ext.grid.GridPanel({
						region : "center",
						id : "backtaxGrid",
						stripeRows : true,
						bodyStyle : 'width:100%',
						store : ds, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
						loadMask : true, // 是否显示正在加载
						tbar : tb,
						bbar : toolBar,
						border : false,
						viewConfig : {
							forceFit : true
						}
					});

			var viewport = new Ext.Viewport({
						layout : "fit",
						items : [grid]
					});

			grid.on('rowdblclick', function(grid, rowIndex, event) {
						var record = grid.getStore().getAt(rowIndex);
						windowopen(record.get("id"))
					});
		});
function getIds() {
	var list = Ext.getCmp("backtaxGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotBackTax = new CotBacktax();
				cotBackTax.id = item.id;
				res.push(cotBackTax);
			});
	return res;
}
function add() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有添加退税单权限!");
		return;
	} else {
		openWindowBase(600, 800, 'cotbacktax.do?method=modify');
	}
}
// 修改
function windowopen(obj) {
	// 修改权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有修改权限!");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("提示信息", "只能选择一条记录!");
			return;
		} else
			obj = ids[0].id;
	}
	openWindowBase(600, 800, 'cotbacktax.do?method=modify&id=' + obj);
}
// 批量删除
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("提示信息", "请选择记录!");
		return;
	}
	var flag = window.confirm("确定删除?");
	if (flag) {
		var list = getIds();
		cotBackTaxService.deleteBackTax(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.Msg.alert("提示信息", "已有其它记录使用到该记录,无法删除!");
						return;
					}
					Ext.Msg.alert("提示信息", "删除成功!");
					reloadGrid('backtaxGrid');
				});
	} else {
		return;
	}
}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示信息", "您没有删除权限!");
		return;
	}
	var flag = window.confirm("确定删除?");
	if (flag) {
		var cotBackTax = new CotBacktax();
		var list = new Array();
		cotBackTax.id = id;
		list.push(cotBackTax)
		cotBackTaxService.deleteBackTax(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.Msg.alert("提示信息", "已有其它记录使用到该记录,无法删除!");
						return;
					}
					Ext.Msg.alert("提示信息", "删除成功!");
					reloadGrid('backtaxGrid');
				});
	} else {
		return;
	}
}