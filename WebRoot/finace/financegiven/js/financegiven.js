Ext.onReady(function() {
	var empsMap = null;
	var currencyMap = null;
	var facMap = null;
	var payTypeMap = null;
	var companyMap = null;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				currencyMap = res;
			});
	// 加载公司表缓存
	baseDataUtil.getBaseDicDataMap("CotCompany", "id", "companyShortName",
			function(res) {
				companyMap = res;
			});
	// 加载员工表缓存
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empsMap = res;
			});
	// 加载供应商表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	// 加载付款方式表缓存
	baseDataUtil.getBaseDicDataMap("CotPayType", "id", "payName",
			function(res) {
				payTypeMap = res;
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
				name : "finaceNo"
			}, {
				name : "companyId"
			}, {
				name : "factoryId"
			}, {
				name : "finaceRecvDate",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "payTypeid"
			}, {
				name : "givenPerson"
			}, {
				name : "amount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "remainAmount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "currencyId"
			}, {
				name : "addPerson"
			}, {
				name : "addTime",
				sortType : timeSortType.createDelegate(this)
			}

	]);
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
							url : "cotfinancegiven.do?method=query"
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
							header : "付款单号",
							dataIndex : "finaceNo",
							width : 150,
							summaryRenderer : function(v, params, data) {
								return "合计：";
							}
						}, {
							header : "厂家",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "付款日期",
							dataIndex : "finaceRecvDate",
							width : 80,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d')
							}
						}, {
							header : "付款方式",
							dataIndex : "payTypeid",
							width : 80,
							renderer : function(value) {
								return payTypeMap[value];
							}
						}, {
							header : "付款人",
							dataIndex : "givenPerson",
							width : 80,
							renderer : function(value) {
								return empsMap[value];
							}
						}, {
							header : "付款金额",
							dataIndex : "amount",
							summaryType : 'sum',
							width : 100
						}, {
							header : "溢付金额",
							dataIndex : "remainAmount",
							summaryType : 'sum',
							width : 100
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 70,
							renderer : function(value) {
								return currencyMap[value];
							}
						}, {
							header : "制单人",
							dataIndex : "addPerson",
							width : 80,
							renderer : function(value) {
								return empsMap[value];
							}
						}, {
							header : "制单日期",
							dataIndex : "addTime",
							width : 80,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d')
							}
						}, {
							header : "操作",
							dataIndex : "id",
							width : 100,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var mod = '<a href="javascript:windowopenMod('
										+ value + ')">修改</a>';
								var nbsp = "&nbsp &nbsp &nbsp"
								var factoryId = record.get("factoryId")
								var del = '<a href="javascript:del(' + value
										+ ',' + factoryId + ')">删除</a>';
								return mod + nbsp + del;
							}
						}]
			});
	DWREngine.setAsync(true);
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

	// 厂家
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
		cmpId : 'factoryId',
		emptyText : "厂家",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		sendMethod : "post",
		pageSize : 5,
		width : 120,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'factoryId'
	});
	// 业务员
	var busiBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'recvPerson',
		emptyText : "付款人",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		sendMethod : "post",
		pageSize : 5,
		width : 120,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'recvPerson'
	});
	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : "币种",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				width : 100,
				isSearchField : true,
				searchName : 'currencyId'
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
						}, facBox, busiBox, curBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "付款单号",
							isSearchField : true,
							searchName : 'financeNo',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "新建",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : windowopenAdd
						}, '-', {
							text : "修改",
							iconCls : "page_mod",
							cls : "SYSOP_MOD",
							handler : windowopenMod
									.createDelegate(this, [null])
						}, '-', {
							text : "删除",
							iconCls : "page_del",
							cls : "SYSOP_DEL",
							handler : deleteBatch
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "givenGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : [summary],
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	var view = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	// 双击弹出编辑页面
	grid.on("rowdblclick", function(gridpanel, idx) {
				var rec = gridpanel.getStore().getAt(idx);
				windowopenMod(rec.get("id"));
			});

	// 打开添加页面
	function windowopenAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0)// 没有添加权限
		{
			Ext.Msg.alert("提示信息", "您没有添加权限");
			return;
		}
		openFullWindow('cotfinancegiven.do?method=addFinacegiven');
	}

	// 批量删除
	function deleteBatch() {
		var recs = sm.getSelections();
		var list = new Array();
		Ext.each(recs, function(item) {
					var child = new Array();
					child.push(item.get("id"));
					child.push(item.get("custId"));
					list.push(child)
				});

		if (list.length == 0) {
			Ext.Msg.alert("提示信息", "请选择记录");
			return;
		}
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.Msg.alert("提示信息", "您没有删除权限");
			return;
		}
		DWREngine.setAsync(false);
		var listNew = new Array();
		for (var i = 0; i < list.length; i++) {
			// 查询该付款记录的溢付款是否转移到出货,有就不能删除
			cotFinanceGivenService.findIsYiOut(list[i][0], list[i][1],
					function(check) {
						if (check == 0) {
							listNew.push(list[i][0]);
						}
					});
		}

		// 判断是否有可删除的
		if (listNew.length > 0) {
			var str = "";
			if (listNew.length == list.length) {
				str = "是否删除选择的付款记录?";
			} else {
				str = "部分付款记录的溢付款有金额转移到出货或者冲帐明细中的预付货款有金额转移到出货,是否删除其他付款记录?";
			}
			Ext.MessageBox.confirm('提示消息', str, function(btn) {
						if (btn == 'yes') {
							cotFinanceGivenService.deleteFinacerecvs(listNew,
									function(res) {
										if (res == 0) {
											Ext.Msg.alert("提示信息", "删除成功");
											reloadGrid('givenGrid');
										} else {
											Ext.Msg.alert("提示信息", "删除失败");
										}
									});
						}
					});
		}
		DWREngine.setAsync(true);
	}

});

function getIds() {
	var list = Ext.getCmp("givenGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var child = new Array();
				child.push(item.get("id"));
				child.push(item.get("custId"));
				res.push(child)
			});
	return res;
}

// 打开编辑页面
function windowopenMod(obj) {
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0) {
		Ext.Msg.alert("提示信息", "您没有修改权限");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示信息", "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("提示信息", "只能选择一条记录!")
			return;
		} else
			obj = ids[0][0];

	}
	openFullWindow('cotfinancegiven.do?method=addFinacegiven&id=' + obj);
}

// 删除
function del(id, factoryId) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.Msg.alert("提示信息", "您没有删除权限");
		return;
	}
	// 查询该付款记录的溢付款是否转移到出货,有就不能删除
	cotFinanceGivenService.findIsYiOut(id, factoryId, function(check) {
				if (check == 1) {
					Ext.Msg.alert("提示信息", "该付款记录的溢付款的部分金额转移到出货的其他费用,不能删除!");
				} else if (check == 2) {
					Ext.Msg.alert("提示信息", "冲帐明细中的预付货款有金额转移到出货,不能删除!");
				} else {
					var list = new Array();
					list.push(id);
					Ext.MessageBox.confirm('提示消息', '是否确定删除该条付款记录?', function(
									btn) {
								if (btn == 'yes') {
									cotFinanceGivenService.deleteFinacerecvs(
											list, function(res) {
												if (res == 0) {
													Ext.Msg.alert("提示信息",
															"删除成功");
													reloadGrid("givenGrid")
												} else {
													Ext.Msg.alert("提示信息",
															"删除失败");
												}
											})
								}
							});
				}
			});
}
