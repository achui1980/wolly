var custMap;
Ext.onReady(function() {
	DWREngine.setAsync(false);
	// baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName",
	// function(res) {
	// custMap = res;
	// });

	// var busiMap;
	// baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res)
	// {
	// busiMap = res;
	// });
	DWREngine.setAsync(true);
	// 加载表格需要关联的外键名
	var facMap;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	var boxtypeMap;
	baseDataUtil.getBaseDicDataMap("CotBoxType", "id", "typeName",
			function(res) {
				boxtypeMap = res;
			});

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ***************************************************** */
	var givenRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "givenNo"
			}, {
				name : "givenTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "custId",
				type : "int"
			}, {
				name : "bussinessPerson",
				type : "int"
			}, {
				name : "custRequiretime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "checkComplete",
				type : "int"
			}, {
				name : "realGiventime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "customerShortName"
			}, {
				name : "empsName"
			}, {
				name : "givenIscheck",
				type : "int"
			}, {
				name : "givenStatus",
				type : "int"
			}]);
	// 创建数据源
	var _dgiven = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotgiven.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, givenRecord)
			});

	// 创建复选框列
	var given_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var given_cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [given_sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "送样单号",
					dataIndex : "givenNo",
					width : 120
				}, {
					header : "下单日期",
					dataIndex : "givenTime",
					width : 80,
					renderer : function(value) {
						if (value != null) {
							return Ext.util.Format.date(new Date(value.time),
									"Y-m-d");
						}
					}
				}, {
					header : "客户",
					dataIndex : "customerShortName",
					width : 110
					// renderer : function(value) {
				// return custMap[value];
				// }
			}	, {
					header : "业务员",
					dataIndex : "empsName",
					width : 70
					// renderer : function(value) {
				// return busiMap[value];
				// }
			}	, {
					header : "最迟送样日期",
					dataIndex : "custRequiretime",
					width : 90,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "是否送样",
					dataIndex : "checkComplete",
					width : 60,
					renderer : function(value) {
						if (value == 0) {
							return "否"
						} else if (value == 1) {
							return "<span style='color:blue;font-weight:bold;'>是</span>";
						}
					}
				}, {
					header : "送样日期",
					dataIndex : "realGiventime",
					width : 80,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "征样完成",
					dataIndex : "givenStatus",
					width : 60,
					renderer : function(value) {
						if (value == 0) {
							return "<span style='color:red;'>未完成</span>";
						} else if (value == 1) {
							return "<span style='color:green;'>已完成</span>";
						}
					}
				}]
	});
	var given_toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : _dgiven,
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

	// 客户
	var customerBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
		cmpId : 'custId',
		emptyText : "客户",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 5,
		width : 70,
		selectOnFocus : true,
		sendMethod : "post",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custId'
	});
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'bussinessPerson',
				emptyText : "业务员",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				width : 70,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'bussinessPerson'
			});

	var given_tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', customerBox, busiBox, {
							xtype : 'searchcombo',
							width : 70,
							emptyText : "单号",
							isJsonType : false,
							isSearchField : true,
							searchName : 'givenNoFind',
							store : _dgiven
						}, '->', {
							text : "新增",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : windowopenAdd
						}, '-', {
							text : "修改",
							iconCls : "gird_edit",
							cls : "SYSOP_MOD",
							handler : windowopenMod
									.createDelegate(this, [null])
						}, '-', {
							text : "删除",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}, '-', {
							text : "打印",
							iconCls : "page_print",
							cls : "SYSOP_PRINT",
							handler : showPrint
						}, '-', {
							text : "分解",
							iconCls : "page_fen",
							cls : "SYSOP_FEN",
							handler : checkIsToSign
						}]
			});

	var given_grid = new Ext.grid.GridPanel({
				region : "center",
				id : "givenGrid",
				stripeRows : true,
				bodyStyle : 'width:100%;',
				autoScroll : true,
				store : _dgiven, // 加载数据源
				cm : given_cm,// 加载列
				sm : given_sm,
				border : false,
				loadMask : true, // 是否显示正在加载
				tbar : given_tb,
				bbar : given_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 单击修改信息 start
	given_grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	/** ****征样单************************************************* */
	var signRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "signNo"
			}, {
				name : "factoryId",
				type : "int"
			}, {
				name : "signTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "requireTime"
			}, {
				name : "checkSign",
				type : "int"
			}, {
				name : "arriveTime"
			}, {
				name : "givenId",
				type : "int"
			}]);
	// 创建数据源
	var _dsign = new Ext.data.Store({
				baseParams : {
					limit : 10
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotsign.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, signRecord)
			});

	// 创建复选框列
	var sign_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var sign_cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [sign_sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "征样单号",
					dataIndex : "signNo",
					width : 120
				}, {
					header : "厂家",
					dataIndex : "factoryId",
					width : 120,
					renderer : function(value) {
						return facMap[value];
					}
				}, {
					header : "征样日期",
					dataIndex : "signTime",
					width : 120,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "送样日期",
					dataIndex : "requireTime",
					width : 120,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "是否到样",
					dataIndex : "checkSign",
					width : 120,
					renderer : function(value) {
						if (value == 0) {
							return "<span style='color:red;font-weight:bold;'>否</span>";
						} else if (value == 1) {
							return "是";
						}
					}
				}, {
					header : "到样日期",
					dataIndex : "arriveTime",
					width : 120,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "送样单号",
					dataIndex : "givenId",
					width : 50,
					hidden : true
				}

		]
	});
	var sign_toolBar = new Ext.PagingToolbar({
				pageSize : 10,
				store : _dsign,
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
		sendMethod : "POST",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		pageSize : 5,
		width : 100,
		selectOnFocus : true,
		sendMethod : "post",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'factoryId'
	});
	var sign_tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', facBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "征样单号",
							isJsonType : false,
							isSearchField : true,
							searchName : 'signNoFind',
							store : _dsign
						}, {
							xtype : 'textfield',
							text : '送样编号',
							hidden : true,
							noClear : true,
							ref : 'givenIdField',
							isSearchField : true,
							searchName : 'givenId'
						}, '->', {
							text : "新增",
							iconCls : "page_add",
							handler : openAddSign,
							cls : "SYSOP_ADD"
						}, '-', {
							text : "修改",
							iconCls : "gird_edit",
							cls : "SYSOP_MOD",
							handler : openEditSign.createDelegate(this, [null])
						}, '-', {
							text : "删除",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatchSign
						}, '-', {
							text : "打印",
							iconCls : "page_print",
							cls : "SYSOP_PRINT",
							handler : showSignPrint
						}]
			});
	var sign_grid = new Ext.grid.GridPanel({
				anchor:'100% 50%',
				id : "signGrid",
				stripeRows : true,
				border : false,
				cls : 'bottomBorder',
				store : _dsign, // 加载数据源
				cm : sign_cm, // 加载列
				sm : sign_sm,
				loadMask : true, // 是否显示正在加载
				tbar : sign_tb,
				bbar : sign_toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	// 分页基本参数
	// _dsign.load({params:{start:0, limit:10}});

	given_grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				sign_tb.givenIdField.setValue(record.id);
				_dsign.load({
							params : {
								start : 0,
								givenId : record.id
							}
						});
			});
	/** ****征样单明细************************************************* */
	var signdetailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "eleName"
			}, {
				name : "boxTypeId",
				type : "int"
			}, {
				name : "signRequire"
			}, {
				name : "signCount",
				type : "int"
			}, {
				name : "givenId",
				type : "int"
			}]);
	// 创建数据源
	var _dsigndetail = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotsign.do?method=querySignDetail"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, signdetailRecord)
			});

	// 创建需要在表格显示的列
	var signdetail_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [{
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "货号",
							dataIndex : "eleId",
							width : 80
						}, {
							header : "客号",
							dataIndex : "custNo",
							width : 80,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "中文品名",
							dataIndex : "eleName",
							width : 100
						}, {
							header : "包装方式",
							dataIndex : "boxTypeId",
							width : 100,
							renderer : function(value) {
								return boxtypeMap[value];
							}
						}, {
							header : "改样要求",
							dataIndex : "signRequire",
							width : 200
						}, {
							header : "数量",
							dataIndex : "signCount",
							width : 60
						}, {
							header : "送样单号",
							dataIndex : "givenId",
							width : 50,
							hidden : true
						}, {
							header : "操作",
							dataIndex : "id",
							width : 60,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href=javascript:delDetail('
										+ value + ',"' + record.data.givenId
										+ '")>删除</a>';
								return del + nbsp;
							}
						}]
			});
	var signdetail_toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : _dsigndetail,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});

	var signdetail_grid = new Ext.grid.GridPanel({
				anchor:'100% 50%',
				id : "signdetailGrid",
				border : false,
				//cls : 'leftBorder',
				stripeRows : true,
				store : _dsigndetail, // 加载数据源
				cm : signdetail_cm, // 加载列
				loadMask : true, // 是否显示正在加载
				bbar : signdetail_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 分页基本参数
	// _dsigndetail.load({params:{start:0, limit:15}});

	sign_grid.on('rowclick', function(grid, rowIndex, event) {
				var record = given_sm.getSelected();

				var record = grid.getStore().getAt(rowIndex);
				_dsigndetail.load({
							params : {
								start : 0,
								limit : 15,
								givenId : record.data.givenId,
								factoryId : record.data.factoryId,
								flag : 'detailTable'
							}
						});
			});

	// 单击修改信息 start
	sign_grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				openEditSign(record.get("id"));
			});

	var given = new Ext.Panel({
				title : '送样单',
				region : 'west',
				border : false,
				cls : 'rightBorder',
				margins : '0 3 0 3',
				width : '50%',
				layout : 'fit',
				items : [given_grid]
			})

	var sign = new Ext.Panel({
				title : '征样单',
				region : 'center',
				border : false,
				cls : 'leftBorder',
				width : '50%',
				layout : 'anchor',
				items : [sign_grid, signdetail_grid]
			})

	var vp = new Ext.Viewport({
				layout : 'border',
				items : [given, sign]
			})
})

// =========================征样单操作====================================
// 获得征样表格选择的记录
function getSignIds() {
	var list = Ext.getCmp("signGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotSign = new CotSign();
				cotSign.id = item.id;
				cotSign.givenId = item.data.givenId;
				cotSign.factoryId = item.data.factoryId;
				res.push(cotSign);
			});
	return res;
}
// 批量删除
function deleteBatchSign() {
	var list = getSignIds();
	if (list.length == 0) {
		alert("请选择记录");
		return;
	}
	var givenId = list[0].givenId;
	DWREngine.setAsync(false);
	Ext.MessageBox.confirm('提示信息', '确定删除选中的征样单吗?', function(btn) {
				if (btn == 'yes') {
					cotGivenService.deleteSignList(list, function(res) {
								cotGivenService.modifyGivenStatus(
										parseInt(givenId), 'new',
										function(res) {
										});
								Ext.Msg.alert('提示信息', "删除成功");
								reloadGrid("signGrid");
								reloadGrid("signdetailGrid");
								// reloadGrid("givenGrid");
								clearForm("signFormId");
							});
				} else {
					return;
				}
			});
	DWREngine.setAsync(true);
}

// 显示打印面板
var printWinSign;
var showSignPrint = function(item) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType("cotsign.do", "PRINT");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("提示信息", '您没有打印权限！');
		return;
	}
	if (printWinSign == null) {
		printWinSign = new PrintWinSign({
					type : 'sign'
				});
	}
	if (!printWinSign.isVisible()) {
		var po = item.getPosition();
		printWinSign.setPosition(po[0] - 200, po[1] + 25);
		printWinSign.show();
	} else {
		printWinSign.hide();
	}
};
// 删除征样明细
function delDetail(id, givenId) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示框", "您没有删除权限");
		return;
	}

	Ext.MessageBox.confirm('提示信息', '确定删除选中的征样明细吗?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					// 查询该寄样明细是否被删除
					cotGivenService.getGivenDetailById(id, function(res) {
								if (res != null) {
									cotGivenService.deleteSignDetail(
											parseInt(id), function(res) {
												if (res == true) {
													cotGivenService
															.modifyGivenStatus(
																	parseInt(givenId),
																	'new',
																	function(
																			res) {
																	});
													Ext.Msg
															.alert("提示框",
																	"删除成功");
													// reloadGrid("givenGrid");
													// reloadGrid("signGrid");
													reloadGrid("signdetailGrid");

												} else {
													Ext.Msg.alert("提示框",
															"删除失败，该征样明细已经被使用中");
												}
											})
								} else {
									reloadGrid("signdetailGrid");
								}
							});
					DWREngine.setAsync(true);
				}
			});
}

// 打开征样新增页面
function openAddSign() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType("cotsign.do", "ADD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.Msg.alert("提示信息", "您没有修改权限");
		return;
	}
	var ids = getGivenIds();
	var givenId = '';
	var givenNo = '';
	if (ids.length == 0) {
		Ext.Msg.alert("提示信息", "请选择一条送样记录!");
		return;
	} else if (ids.length > 1) {
		Ext.Msg.alert("提示信息", "只能选择一条送样记录!")
		return;
	} else {
		givenId = ids[0].id;
	}
	var flag = false;
	DWREngine.setAsync(false);
	cotGivenService.getGivenById(parseInt(givenId), function(cotgiven) {
				if (cotgiven.givenIscheck != 2) {
					if (cotgiven.givenIscheck != 9) {
						flag = true;
					}
				}
				givenNo = cotgiven.givenNoFind
			});
	if (flag) {
		Ext.Msg.alert("提示信息", '该送样单还未通过审核，不能新增征样记录！');
		return;
	} else {
		openFullWindow('cotsign.do?method=add&givenId=' + givenId + "&givenNo="
				+ givenNo);
	}
	DWREngine.setAsync(true);
}

// 打开征样编辑页面
function openEditSign(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType("cotsign.do", "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有修改权限");
		return;
	}
	if (obj == null) {
		var ids = getSignIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("提示信息", "只能选择一条记录!")
			return;
		} else
			obj = ids[0].id;
	}
	// 查询该征样单是否被删除
	cotSignService.getSignById(obj, function(res) {
				if (res != null) {
					openFullWindow('cotsign.do?method=add&id=' + obj);
				} else {
					reloadGrid("signGrid");
					Ext.Msg.alert("提示信息", "该征样单已被删除!");
				}
			})
}
// =========================征样单操作(结束)====================================
// 获得表格选择的记录
function getGivenIds() {
	var list = Ext.getCmp("givenGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotGiven = new CotGiven();
				cotGiven.id = item.id;
				cotGiven.custId = item.get("custId");
				cotGiven.givenNo = item.get("givenNo");
				cotGiven.givenIscheck = item.get("givenIscheck");
				res.push(cotGiven);
			});
	return res;
}

// 单据状态及权限判断
function checkIsToSign() {

	var ids = getGivenIds();
	if (ids.length == 0) {
		Ext.Msg.alert("提示框", "请选择一条送样单记录!");
		return;
	} else if (ids.length > 1) {
		Ext.Msg.alert("提示框", "只能选择一条送样单记录!")
		return;
	} else {
		DWREngine.setAsync(false);
		var givenId = ids[0].id;
		cotGivenService.getList('CotPriceCfg', function(cfg) {
			if (cfg.length != 0) {
				if (cfg[0].isCheck != 0) {
					cotGivenService.getGivenById(parseInt(givenId), function(
							cotgiven) {
						cotGivenService.checkCurrEmpsIsSuperAd(function(emps) {
									if (!emps) {
										if (cotgiven.givenIscheck != 2) {
											if (cotgiven.givenIscheck != 9) {
												Ext.Msg.alert("提示框",
														'该送样单还未通过审核，不能分解！');
												return;
											} else if (cotgiven.givenStatus == 1) {
												Ext.Msg.alert("提示框",
														'该送样单已分解过！');
												return;
											} else {
												givenToSign(givenId);
											}
										} else if (cotgiven.givenStatus == 1) {
											Ext.Msg.alert("提示框", '该送样单已分解过！');
											return;
										} else {
											givenToSign(givenId);
										}
									} else if (cotgiven.givenStatus == 1) {
										Ext.Msg.alert("提示框", '该送样单已分解过！');
										return;
									} else {
										givenToSign(givenId);
									}
								});
					});
				} else {
					givenToSign(givenId);
				}
			}
		});
	}
}

// 分解选中寄样单到征样单
function givenToSign(givenId) {
	// 通过主单id获取明细的集合
	cotGivenService.getDetailListByGivenId(parseInt(givenId), function(res) {
		if (res == null) {
			Ext.Msg.alert("提示框", "此送样单还未添加样品！");
			return;
		} else {
			var factoryIds = ''; // 厂家ids列表
			var nullEleIds = ''; // 空厂家的货号
			for (var i = 0; i < res.length; i++) {
				if (res[i].factoryId == null) {
					nullEleIds += res[i].eleId + ",";
				}
				factoryIds += res[i].factoryId + ",";
			}
			if (nullEleIds != '') {
				Ext.Msg.alert("提示框", "货号" + nullEleIds + "的厂家为空，请到详细页面指定后再分解！");
				return;
			}
			var factoryIdAry = factoryIds.split(',');// 字符串转化为数组
			// 分解生成主征样单
			cotGivenService.saveSign(factoryIdAry, parseInt(givenId), function(
							flag) {
						if (flag) {
							Ext.Msg.alert("提示框", "分解成功！");
							var flg = 'separate';
							cotGivenService.modifyGivenStatus(
									parseInt(givenId), flg, function(status) {
									});
							reloadGrid("givenGrid");
							reloadGrid("signGrid");
						} else {
							Ext.Msg.alert("提示框", "分解失败！");
						}
					});
		}
	});
	DWREngine.setAsync(true);
}

// 批量删除
function deleteBatch() {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.Msg.alert("提示框", "您没有删除权限");
		return;
	}
	var list = getGivenIds();
	if (list.length == 0) {
		Ext.Msg.alert("提示框", "请选择记录");
		return;
	}
	// 查询样品默认配置中是否审核
	var checkFlag = false;
	DWREngine.setAsync(false);
	cotGivenService.getList('CotPriceCfg', function(cfg) {
				if (cfg.length != 0) {
					if (cfg[0].isCheck != 0) {
						checkFlag = true;
					}
				}
			});
	DWREngine.setAsync(true);
	// 如果需要审核,判断选择的单据是否已审核通过
	var givenNo = '';
	if (checkFlag == true) {
		for (var i = 0; i < list.length; i++) {
			if (list[i].givenIscheck == 2) {
				givenNo = list[i].givenNo + ",";
			}
		}
	}
	if (givenNo != '') {
		Ext.Msg.alert("提示框", '部分单据:<font color=red>' + givenNo
						+ '</font>已审核通过，如要删除，请执行反审操作或联系管理员！');
		return;
	}
	// 判断该单是否含有应付款
	for (var i = 0; i < list.length; i++) {
		if (list[i].givenIscheck == 2) {
			DWREngine.setAsync(false);
			cotGivenService.getDealNumById(list[i].id, function(res) {
						if (res != -1)
							givenNo = list[i].givenNo + ",";
					});
			DWREngine.setAsync(true);
		}
	}
	if (givenNo != '') {
		Ext.Msg.alert("提示框", '部分单据:<font color=red>' + givenNo
						+ '</font>已有应付帐款记录，不能删除！');
		return;
	}

	Ext.MessageBox.confirm('提示信息', '确定删除选中的送样单吗?', function(btn) {
				if (btn == 'yes') {
					cotGivenService.deleteGivenList(list, function(res) {
								Ext.Msg.alert("提示框", "删除成功");
								reloadGrid("givenGrid");
								reloadGrid("signGrid");
								clearForm("givenFormId");
							});
				} else {
					return;
				}
			});

}

// 显示打印面板
var printWin;
var showPrint = function(item) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("提示信息", '您没有打印权限！');
		return;
	}
	if (printWin == null) {
		printWin = new PrintWin({
					type : 'given'
				});
	}
	if (!printWin.isVisible()) {
		var po = item.getPosition();
		printWin.setPosition(po[0] - 200, po[1] + 25);
		printWin.show();
	} else {
		printWin.hide();
	}
};
// 打开新增页面
function windowopenAdd() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有添加权限");
		return;
	}
	openFullWindow('cotgiven.do?method=add');
}

// 打开编辑页面
function windowopenMod(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有修改权限");
		return;
	}
	if (obj == null) {
		var ids = getGivenIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示信息", "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("提示信息", "只能选择一条记录!")
			return;
		} else {
			obj = ids[0].id;
		}
	}
	// 查询该寄样单是否被删除
	cotGivenService.getGivenById(obj, function(res) {
				if (res != null) {
					openFullWindow('cotgiven.do?method=add&id=' + obj);
				} else {
					reloadGrid("givenGrid");
					Ext.Msg.alert("提示信息", "该送样单已被删除!");
				}
			})
}