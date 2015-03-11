Ext.onReady(function() {
	DWREngine.setAsync(false);
	var fac = null; // 供应商类别
	var facData = [];
	// 1：产品，2：配件，3包材
	// 加载厂家类表缓存
	baseDataUtil.getBaseDicMapById("CotFactory", "id", "shortName",
			"factroyTypeidLv1", "2", function(res) {
				fac = res;
				for (var p in fac)
					facData.push(["" + p, fac[p]]);
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
				name : "addTime"
			}, {
				name : "priceOut"
			}, {
				name : "facId",
				type : "int"
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
					limit : 15
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotfittings.do?method=loadPriceInfo&fitId="
									+ $('fitId').value
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
							header : "报价日期",
							dataIndex : "addTime",
							width : 150,
							renderer : function(value) {
								return value.year + "-" + (value.month + 1)
										+ "-" + value.day;
							}
						}, {
							header : "报价",
							dataIndex : "priceOut",
							width : 150,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00");
							}
						}, {
							header : "供应商",
							dataIndex : "facId",
							width : 180,
							renderer : function(value) {
								return fac["" + value];
							}
						}, {
							header : "备注",
							dataIndex : "remark",
							width : 500
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
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

	// 厂家数据列表
	var comboFac = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2",
		sendMethod : "post",
		cmpId : "facIdFind",
		valueField : "id",
		displayField : "shortName",
		emptyText : "供应商",
		pageSize : 10,
		width : 100,
		editable : true,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'facIdFind'

	});
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "起始时间",
							width : 90,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "结束时间",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, comboFac, {
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
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "删除",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}

				]
			});
	var grid = new Ext.grid.GridPanel({
				id : "hisGrid",
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
				windowopenMod(record.get("id"));
			});
	DWREngine.setAsync(true);

});
function getIds() {
	var list = Ext.getCmp("hisGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
// 批量删除
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert('提示消息', "请选择记录");
		return;
	}
	Ext.MessageBox.confirm('提示消息', "是否确定删除该报价信息?", function(btn) {
				if (btn == 'yes') {
					cotFittingsService.deletePriceOuts(list, function(res) {
								Ext.MessageBox.alert('提示消息', "删除成功");
								reloadGrid('hisGrid');
							});
				}
			});
}
// 打开其他厂家报价添加页面
function windowopenAdd() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.MessageBox.alert('提示消息', "您没有添加权限");
		return;
	}
	var fitName = $("fitName").value;
	openWindowBase(250, 450, 'cotfittings.do?method=addPriceOut&fitId='
					+ $('fitId').value + '&fitName=' + encodeURI(fitName));
}
// 打开开其他厂家报价编辑页面
function windowopenMod(obj) {
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert('提示消息', "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert('提示消息', "只能选择一条记录!")
			return;
		} else
			obj = ids[0];

	}
	var fitName = $("fitName").value;
	openWindowBase(250, 450, 'cotfittings.do?method=addPriceOut&id=' + obj
					+ '&fitId=' + $('fitId').value + '&fitName='
					+ encodeURI(fitName));
}