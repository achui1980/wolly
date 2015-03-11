ImportPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var facData;
	DWREngine.setAsync(false);
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	DWREngine.setAsync(true);

	DWREngine.setAsync(false);
	var curMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);

	DWREngine.setAsync(false);
	var empMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empMap = res;
			});
	DWREngine.setAsync(true);
	
	//存储左边表格点击的类型
	this.clickType="";
	// 加载产品采购单
	var facGrid = new OrderFacGrid(cfg);

	// 加载配件采购单
	var fitGrid = new OrderFitGrid(cfg);

	// 加载包材采购单
	var packGrid = new OrderPackGrid(cfg);

	var tbl = new Ext.TabPanel({
				height : 505,
				activeTab : -1,
				items : [{
							title : '产品采购单',
							name : 'facTab',
							layout : 'fit'
						}, {
							title : '配件采购单',
							name : 'fitTab',
							layout : 'fit'
						}, {
							title : '包材采购单',
							name : 'packTab',
							layout : 'fit'
						}]
			});
	// 激活面板时
	tbl.on('tabchange', function(tb, pnl) {
				if (pnl.name == 'facTab') {
					if (!facGrid.isVisible()) {
						//facGrid.orderfacIds = cfg.orderfacIds;
						pnl.add(facGrid);
						pnl.doLayout();
						facGrid.load();
					}
				}
				if (pnl.name == 'fitTab') {
					if (!fitGrid.isVisible()) {
						//fitGrid.fitorderIds = cfg.fitorderIds;
						pnl.add(fitGrid);
						pnl.doLayout();
						fitGrid.load();
					}
				}
				if (pnl.name == 'packTab') {
					if (!packGrid.isVisible()) {
						//packGrid.packorderIds = cfg.packorderIds;
						pnl.add(packGrid);
						pnl.doLayout();
						packGrid.load();
					}
				}
			});
	// =============================================================
	// 右边表格
	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var otherRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "factoryId"
			}, {
				name : "finaceName"
			}, {
				name : "flag"
			}, {
				name : "amount"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}]);

	// 创建数据源
	var other_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryFacOther"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, otherRecord)
			});
	// 创建复选框列
	var other_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var other_cm = new Ext.grid.ColumnModel([
			other_sm,
			{
				header : "ID",
				dataIndex : "id",
				width : 50,
				hidden : true
			}, {
				header : "厂家",
				dataIndex : "factoryId",
				width : 130,
				renderer : function(value) {
					return facData[value];
				},
				sortable : true
			}, {
				header : "费用名称",
				dataIndex : "finaceName",
				width : 130,
				sortable : true
			}, {
				header : "加/减",
				dataIndex : "flag",
				width : 55,
				renderer : function(value) {
					if (value == 'A') {
						return '加';

					}
					if (value == 'M') {
						return '减';
					}
				},
				sortable : true
			}, {
				header : "金额",
				dataIndex : "amount",
				width : 80,
				sortable : true
			}, {
				header : "剩余金额",
				dataIndex : "remainAmount",
				width : 80,
				renderer : function(value) {
					return "<span style='color:red;font-weight:bold;'>" + value
							+ "</span>";
				},
				sortable : true
			}, {
				header : "币种",
				dataIndex : "currencyId",
				width : 60,
				renderer : function(value) {
					return curMap[value]
				},
				sortable : true
			}]);
	var other_toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : other_ds,
				displayInfo : true,
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});
	var other_tb = new Ext.Toolbar({
				items : ['->', {
							text : "导入",
							handler : insertOther,
							iconCls : "page_add"
						}]
			});

	var other_grid = new Ext.grid.EditorGridPanel({
				region : "north",
				id : "otherGrid",
				height : 160,
				stripeRows : true,
				store : other_ds, // 加载数据源
				cm : other_cm, // 加载列
				sm : other_sm,
				width : '100%',
				loadMask : true, // 是否显示正在加载
				tbar : other_tb,
				bbar : other_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var dealRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "factoryId"
			}, {
				name : "finaceName"
			}, {
				name : "amount"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}, {
				name : "realAmount"
			}, {
				name : "zhRemainAmount"
			}, {
				name : "amountDate"
			}, {
				name : "businessPerson"
			}]);
	// 创建数据源
	var deal_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryFacDeal"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, dealRecord)
			});
	// 创建复选框列
	var deal_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var deal_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [deal_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "帐款单号",
							dataIndex : "finaceNo",
							width : 120
						}, {
							header : "厂家",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return facData[value];
							}
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 100
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 80
						}, {
							header : "未付金额",
							dataIndex : "remainAmount",
							width : 80,
							renderer : function(value) {
								return "<span style='color:red;font-weight:bold;'>"
										+ value + "</span>";
							}
						}, {
							header : "流转后剩余金额",
							dataIndex : "zhRemainAmount",
							width : 100,
							renderer : function(value) {
								return "<span style='color:red;font-weight:bold;'>"
										+ value + "</span>";
							}
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value]
							}
						}, {
							header : "已付金额",
							dataIndex : "realAmount",
							width : 80,
							renderer : function(value) {
								return "<span style='color:green;font-weight:bold;'>"
										+ value + "</span>";
							}
						}, {
							header : "账单日期",
							dataIndex : "amountDate",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "业务员",
							dataIndex : "businessPerson",
							width : 80,
							renderer : function(value) {
								return empMap[value]
							}
						}]
			});
	var deal_toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : deal_ds,
				displayInfo : true,
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});
	var deal_tb = new Ext.Toolbar({
				items : ['->', {
							text : "导入",
							handler : insertDeal,
							iconCls : "page_add"
						}]
			});

	var deal_grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "dealGrid",
				height : 185,
				stripeRows : true,
				margins : "0 0 0 0",
				bodyStyle : 'width:100%',
				store : deal_ds, // 加载数据源
				cm : deal_cm, // 加载列
				sm : deal_sm,
				width : '100%',
				loadMask : true, // 是否显示正在加载
				tbar : deal_tb,
				bbar : deal_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var overfeeRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "orderNo"
			}, {
				name : "factoryId"
			}, {
				name : "finaceName"
			}, {
				name : "flag"
			}, {
				name : "amount"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}]);

	// 创建数据源
	var overfee_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryOverFee"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, overfeeRecord)
			});
	// 创建复选框列
	var overfee_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var overfee_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [overfee_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "付款单号",
							dataIndex : "orderNo",
							width : 150
						}, {
							header : "厂家",
							dataIndex : "factoryId",
							width : 120,
							renderer : function(value) {
								return facData[value];
							}
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 120
						}, {
							header : "加/减",
							dataIndex : "flag",
							width : 50,
							renderer : function(value) {
								if (value == 'A') {
									return '加';
								}
								if (value == 'M') {
									return '减';
								}
							}
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 70
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value]
							}
						}]
			});
	var overfee_toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : overfee_ds,
				displayInfo : true,
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});
	var overfee_tb = new Ext.Toolbar({
				items : ['->', {
							text : "导入",
							handler : insertFee,
							iconCls : "page_add"
						}]
			});

	var overfee_grid = new Ext.grid.EditorGridPanel({
				region : "south",
				id : "overfeeGrid",
				height : 160,
				stripeRows : true,
				margins : "0 0 0 0",
				// bodyStyle : 'width:100%',
				store : overfee_ds, // 加载数据源
				cm : overfee_cm, // 加载列
				sm : overfee_sm,
				width : '100%',
				loadMask : true, // 是否显示正在加载
				tbar : overfee_tb,
				bbar : overfee_toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	var pl1 = new Ext.Panel({
				border : false,
				region : 'west',
				margins : '0 5 0 0 ',
				width : 310,
				layout : 'fit',
				items : [tbl]
			});

	// var pl2 = new Ext.Panel({
	// region : 'center',
	// width : 500,
	// height : 500,
	// border : false,
	// layout : 'border',
	// // frame : true,
	// items : [other_grid, deal_grid, overfee_grid]
	// });

	var pl2 = new Ext.TabPanel({
				region : 'center',
				items : [{
							title : "未生成应付款的其他费用",
							name : 'facTab',
							layout : 'fit',
							items : [other_grid]
						}, {
							title : "未完成冲帐的应付款",
							name : 'fitTab',
							layout : 'fit',
							items : [deal_grid]
						}, {
							title : "溢付款",
							name : 'packTab',
							layout : 'fit',
							items : [overfee_grid]
						}]
			});

	this.openPnl = function(type) {
		tbl.setActiveTab(type);
	}

	// =================导入操作==============================================
	// 获得选择的记录
	var getIds = function() {
		var list = other_sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 添加
	function insertOther() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		var ary = other_sm.getSelections();
		cfg.bar.insertSelect(ary, 'other',clickType);
	}
	// 添加
	function insertFee() {
		var ary = overfee_sm.getSelections();
		if(ary.length == 0) {
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		cfg.bar.insertSelect(ary, 'overfee',clickType);
	}
	
	// 获得选择的记录
	var getDealIds = function() {
		var list = deal_sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 添加
	function insertDeal() {
		var list = getDealIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		var ary = deal_sm.getSelections();
		cfg.bar.insertSelect(ary, 'deal',clickType);
	}

	// 表单
	var con = {
		title : '导入费用',
		layout : 'border',
		width : 740,
		height : 470,
		border : false,
		modal : true,
		padding : "0",
		// closeAction : 'hide',
		items : [pl1, pl2]
	};

	Ext.apply(con, cfg);
	ImportPanel.superclass.constructor.call(this, con);
};
Ext.extend(ImportPanel, Ext.Window, {});
