ImportOtherPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	DWREngine.setAsync(false);
	var curMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);

	// 订单主单表格
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryOrderByFinace&recvIds="+cfg.recvIds
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel([{
				header : "ID",
				dataIndex : "id",
				width : 50,
				hidden : true
			}, {
				header : "主订单号",
				sortable : true,
				dataIndex : "orderNo",
				width : 180
			}]);
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});

	var orderGrid = new Ext.grid.GridPanel({
				title : '订单',
				region : "west",
				width : 200,
				stripeRows : true,
				store : ds,
				margins : '0 5 0 0',
				cm : cm,
				loadMask : true,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 其他费用
	var otherRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceName"
			}, {
				name : "flag"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}]);

	// 创建数据源
	var other_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryOther"
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
	var other_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [other_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 150
						}, {
							header : "加/减",
							dataIndex : "flag",
							width : 80,
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
							dataIndex : "remainAmount",
							width : 80,
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
						}]
			});
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
							handler : addOther,
							iconCls : "page_add"
						}]
			});

	var other_grid = new Ext.grid.EditorGridPanel({
				region : "north",
				id : "otGrid",
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

	// 应收款
	var dealRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "finaceName"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}, {
				name : "zhRemainAmount"
			}, {
				name : "amountDate"
			}]);
	// 创建数据源
	var deal_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryNoWanAccount"
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
							width : 100
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 100
						}, {
							header : "未收金额",
							dataIndex : "remainAmount",
							width : 70
						}, {
							header : "未流转金额",
							dataIndex : "zhRemainAmount",
							width : 80,
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
							header : "帐款日期",
							dataIndex : "amountDate",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
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
							handler : addRecv,
							iconCls : "page_add"
						}]
			});

	var deal_grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "dealGrid",
				height : 185,
				stripeRows : true,
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

	// 溢收款
	var overfeeRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "customerShortName"
			}, {
				name : "finaceName"
			}, {
				name : "finaceNo"
			}, {
				name : "amount"
			}, {
				name : "currencyId"
			}]);

	// 创建数据源
	var overfee_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryYiMoney&custId="+cfg.custId
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
							header : "客户",
							dataIndex : "customerShortName",
							width : 150
						}, {
							header : "收款单号",
							dataIndex : "finaceNo",
							width : 150
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 80
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
				items : [{
					xtype : 'label',
					style : 'color:#15428B;font-weight: bold;',
					text : "溢收款："
				}, '->', {
							text : "导入",
							handler : addYi,
							iconCls : "page_add"
						}]
			});

	var overfee_grid = new Ext.grid.EditorGridPanel({
				region : "south",
				id : "overfeeGrid",
				height : 160,
				stripeRows : true,
				store : overfee_ds, // 加载数据源
				cm : overfee_cm, // 加载列
				sm : overfee_sm,
				loadMask : true, // 是否显示正在加载
				tbar : overfee_tb,
				bbar : overfee_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

//	var rightPnl = new Ext.Panel({
//				region : 'center',
//				border : false,
//				layout : 'accordion',
//				defaults:{frame:true},
//				items : [other_grid, deal_grid, overfee_grid]
//			});
			
	var rightPnl = new Ext.TabPanel({
				region : 'center',
				items : [{
							title : "未生成应收款的其他费用",
							name : 'facTab',
							layout : 'fit',
							items:[other_grid]
						}, {
							title : "未完成冲帐的应收款",
							name : 'fitTab',
							layout : 'fit',
							items:[deal_grid]
						}]
			});

	// 订单表格行点击后加载右边的其他费用和应收款数据
	orderGrid.on("rowclick", function(grid, rowIndex, e) {
				var rec = ds.getAt(rowIndex);
				other_ds.proxy.setApi({
					read : "cotorderout.do?method=queryOther&orderId="+rec.id
				});
				
				// 加载其他费用数据
				other_ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
				deal_ds.proxy.setApi({
					read : "cotorderout.do?method=queryNoWanAccount&orderId="+rec.id
				});
				// 加载应收款数据
				deal_ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
			});

	// 加载订单数据
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// 加载溢收款数据
	overfee_ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// ------------------------方法-----------------
	// 导入其他费用
	function addOther() {
		var str = '';
		// 获得币种集合
		var tempRes;
		var oCur;
		DWREngine.setAsync(false);
		sysdicutil.getDicListByName('currency', function(res) {
					tempRes = res;
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == parent.$('currencyId').value) {
							oCur = res[i].curRate;
							break;
						}
					}
				});
		DWREngine.setAsync(true);
		var recs = other_sm.getSelections();
		Ext.each(recs, function(item) {
					var finaceName = item.data.finaceName;
					var remainAmount = item.data.remainAmount;
					var currencyId = item.data.currencyId;
					var id = item.id;
					var flag = item.data.flag;
					var pId = parent.$('pId').value;
					var check = false;
					if (pId != '' && pId != 'null') {
						// 后台判断是否已存在该其他费用
						DWREngine.setAsync(false);
						cotOrderOutService.findIsExistOther(
								parent.$('pId').value, id, function(res) {
									if (res == true) {
										str += finaceName+",";
										check = true;
									}
								});
						DWREngine.setAsync(true);
					}
					// 前台判断是否添加行中已存在该其他费用
					var grid = Ext.getCmp("otherGrid");
					var pDs = grid.getStore();
					pDs.each(function(item) {
								if (isNaN(item.id)) {
									if (item.data.outFlag == id
											&& item.data.source == 'orderOther') {
										str += finaceName+",";
										check = true;
										return false;
									}
								}
							});
					if (check == false) {
						// 币种转换
						for (var i = 0; i < tempRes.length; i++) {
							if (tempRes[i].id == currencyId) {
								remainAmount = tempRes[i].curRate
										* remainAmount;
								break;
							}
						}
						var u = new pDs.recordType({
									finaceName : finaceName,
									amount : (remainAmount / oCur).toFixed('2'),
									remainAmount : (remainAmount / oCur).toFixed('2'),
									currencyId : parent.$('currencyId').value,
									outFlag : id,
									flag : flag,
									source : 'orderOther'
								});
						pDs.add(u);
					}
				});

		if (str != '') {
			Ext.MessageBox.alert('提示消息', '其他费用:"' + str + '" 已经添加到出货,不能重复添加!');
		}else{
			Ext.Msg.alert("提示消息", '导入成功!');
		}
	}

	// 导入应收帐
	function addRecv() {
		var str = '';
		// 获得币种集合
		var tempRes;
		var oCur;
		DWREngine.setAsync(false);
		sysdicutil.getDicListByName('currency', function(res) {
					tempRes = res;
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == parent.$('currencyId').value) {
							oCur = res[i].curRate;
							break;
						}
					}
				});
		DWREngine.setAsync(true);
		// 前台判断是否添加行中已存在该其他费用
		var grid = Ext.getCmp("otherGrid");
		var pDs = grid.getStore();
		var recs = deal_sm.getSelections();
		Ext.each(recs, function(item) {
					var finaceName = item.data.finaceName;
					var zhRemainAmount = item.data.zhRemainAmount;
					var currencyId = item.data.currencyId;
					var id = item.id;
					var pId = parent.$('pId').value;
					var check = false;
					if (pId != '' && pId != 'null') {
						// 后台判断是否已存在该其他费用
						DWREngine.setAsync(false);
						cotOrderOutService.findIsExistRecv(
								parent.$('pId').value, id, function(res) {
									if (res == true) {
										str += finaceName+",";
										check = true;
									}
								});
						DWREngine.setAsync(true);
					}
					pDs.each(function(item) {
								if (isNaN(item.id)) {
									if (item.data.outFlag == id
											&& item.data.source == 'orderRecv') {
										str += finaceName+",";
										check = true;
										return false;
									}
								}
							});
					if (check == false) {
						// 币种转换
						for (var i = 0; i < tempRes.length; i++) {
							if (tempRes[i].id == currencyId) {
								zhRemainAmount = tempRes[i].curRate
										* zhRemainAmount;
								break;
							}
						}
						if (finaceName == '预收货款') {
							var u = new pDs.recordType({
										finaceName : finaceName,
										amount : (zhRemainAmount / oCur)
												.toFixed('2'),
										remainAmount : (zhRemainAmount / oCur).toFixed('2'),
										currencyId : parent.$('currencyId').value,
										outFlag : id,
										flag : 'M',
										source : 'orderRecv'
									});
						} else {
							var u = new pDs.recordType({
										finaceName : finaceName,
										amount : (zhRemainAmount / oCur)
												.toFixed('2'),
										remainAmount : (zhRemainAmount / oCur).toFixed('2'),
										currencyId : parent.$('currencyId').value,
										outFlag : id,
										flag : 'A',
										source : 'orderRecv'
									});
						}

						pDs.add(u);
					}
				});

		if (str != '') {
			Ext.MessageBox.alert('提示消息', '应收帐款:"' + str + '" 已经添加到出货,不能重复添加!');
		}else{
			Ext.Msg.alert("提示消息", '导入成功!');
		}
	}

	// 导入溢收款
	function addYi() {
		var str = '';
		// 获得币种集合
		var tempRes;
		var oCur;
		DWREngine.setAsync(false);
		sysdicutil.getDicListByName('currency', function(res) {
					tempRes = res;
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == parent.$('currencyId').value) {
							oCur = res[i].curRate;
							break;
						}
					}
				});
		DWREngine.setAsync(true);
		// 前台判断是否添加行中已存在该其他费用
		var grid = Ext.getCmp("otherGrid");
		var pDs = grid.getStore();
		var recs = overfee_sm.getSelections();
		Ext.each(recs, function(item) {
					var finaceNo = item.get("finaceNo");
					var finaceName = item.get("finaceName");
					var amount = item.get("amount");
					var currencyId = item.get("currencyId");
					var id = item.id;
					var flag = item.data.flag;
					var pId = parent.$('pId').value;
					var check = false;
					if (pId != '' && pId != 'null') {
						// 后台判断是否已存在该其他费用
						DWREngine.setAsync(false);
						cotOrderOutService.findIsExistYi(parent.$('pId').value,
								id, function(res) {
									if (res == true) {
										str += finaceNo+",";
										check = true;
										
									}
								});
						DWREngine.setAsync(true);
					}
					pDs.each(function(item) {
								if (isNaN(item.id)) {
									if (item.data.outFlag == id
											&& item.data.source == 'yi') {
										str += finaceNo+",";
										check = true;
										return false;
									}
								}
							});
					if (check == false) {
						// 币种转换
						for (var i = 0; i < tempRes.length; i++) {
							if (tempRes[i].id == currencyId) {
								amount = tempRes[i].curRate * amount;
								break;
							}
						}
						var u = new pDs.recordType({
									finaceName : finaceName,
									amount : (amount / oCur).toFixed('2'),
									remainAmount : (amount / oCur).toFixed('2'),
									currencyId : parent.$('currencyId').value,
									outFlag : id,
									flag : 'M',
									source : 'yi'
								});
						pDs.add(u);
					}
				});

		if (str != '') {
			Ext.MessageBox.alert('提示消息', '"' + str + '"的溢收款已经添加到出货,不能重复添加!');
		}else{
			Ext.Msg.alert("提示消息", '导入成功!');
		}
	}
	
	var topPanel = new Ext.Panel({
		region:'center',
		layout : 'border',
		border:false,
		items:[orderGrid, rightPnl]
	});

	// 表单
	var con = {
		title : '导入费用',
		layout : 'border',
		width : 740,
		height : 470,
		border : false,
		modal : true,
		constrainHeader:true,
		items : [topPanel,overfee_grid]
	};

	Ext.apply(con, cfg);
	ImportOtherPanel.superclass.constructor.call(this, con);
};
Ext.extend(ImportOtherPanel, Ext.Window, {});
