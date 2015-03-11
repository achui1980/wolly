Ext.onReady(function() {
	// 加载厂家类表缓存
	DWREngine.setAsync(false);
	var fac = null;
	var emps = null;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				fac = res;
			});
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
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
				name : "factoryId",
				type : "int"
			}, {
				name : "packingOrderNo"
			}, {
				name : "orderDate",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "sendDate",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "empId"
			}, {
				name : "totalAmount"
			}, {
				name : "orderStatus"
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
							url : "cotpackingorder.do?method=query"
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
							header : "供应商",
							dataIndex : "factoryId",
							width : 130,
							renderer : function(value) {
								return fac[value];
							}
						}, {
							header : "采购单号",
							dataIndex : "packingOrderNo",
							width : 130
						}, {
							header : "采购日期",
							dataIndex : "orderDate",
							width : 120,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
								else
									return value;
							}
						}, {
							header : "交货日期",
							dataIndex : "sendDate",
							width : 80,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
								else
									return value;
							}
						}, {
							header : "审核状态", // 表头
							dataIndex : "orderStatus",
							width : 60,
							renderer : function(value) {
								if (value == 0) {
									return "<font color='green'>未审核</font>";
								} else if (value == 1) {
									return "<font color='red'>审核不通过</font>";
								} else if (value == 2) {
									return "<font color='blue'>审核通过</font>";
								} else if (value == 3) {
									return "<font color='#10418C'>请求审核</font>";
								} else if (value == 9) {
									return "<font color='green'>不审核</font>";
								}
							}
						}, {
							header : "采购人员",
							dataIndex : "empId",
							width : 60,
							renderer : function(value) {
								return emps[value]

							}
						}, {
							header : "总金额",
							dataIndex : "totalAmount",
							width : 80,
							align : "right",
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00")
							}
						}, {
							header : "操作",
							dataIndex : "id",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var mod = '<a href="javascript:windowopenMod('
										+ value + ')">修改</a>';
								var nbsp = "&nbsp &nbsp &nbsp";
								var status = record.data.orderStatus;
								var del = '<a href="javascript:del(' + value
										+ ',' + status + ')">删除</a>';
								return mod + nbsp + del;
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

	var facComb = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=3",
		cmpId : 'factoryId',
		emptyText : "采购厂家",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		pageSize : 5,
		width : 120,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'factoryId'
	});

	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'empId',
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				selectOnFocus : true,
				sendMethod : "post",
				width : 120,
				emptyText : '采购人',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'empId'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "采购起始",
							width : 100,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "采购结束",
							width : 100,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, facComb, busiBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "采购单号",
							isSearchField : true,
							searchName : 'packingOrderNo',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "修改",
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "打印",
							handler : showPrint,
							iconCls : "page_print",
							cls : "SYSOP_PRINT"
						}, '-', {
							text : "删除",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}

				]
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "packingGrid",
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
				windowopenMod(record.get("id"));
			});
	function query() {
		ds.reload({
					params : {
						start : 0,
						limit : 15
					}
				});
	};

	// 批量删除
	function deleteBatch() {
		var isPopedom = getPdmByOtherUrl("cotpackingorder.do", "DEL");
		if (isPopedom == 0) {
			Ext.Msg.alert("提示信息", "您没有删除权限");
			return;
		}
		var list = sm.getSelections();
		if (list.length == 0) {
			Ext.Msg.alert("提示信息", "请选择记录");
			return;
		}
		var ary = new Array();
		Ext.each(list, function(item) {
					var status = item.data.orderStatus;
					if (status == 2 && loginEmpId != "admin") {
					} else {
						ary.push(item.id);
					}
				});
		if (ary.length == 0) {
			Ext.MessageBox.alert('提示消息', "您选择的采购单都已审核通过,不能再删除!");
			return;
		}

		Ext.MessageBox.confirm('提示消息', "确定删除选中的包材采购单?", function(btn) {
					if (btn == 'yes') {
						cotPackOrderService.deletePackOrderList(ary, function(
										res) {
									if (res == 3) {
										Ext.Msg.alert("提示信息", "删除出错!");
										return;
									}
									if (res == 1) {
										ds.reload();
										Ext.Msg.alert("提示信息",
												"删除成功!部分包材采购单含有应付账款,不能删除!");
										return;
									}
									if (res == 2) {
										ds.reload();
										Ext.Msg
												.alert("提示信息",
														"删除成功!部分包材采购单有加/减费用导入到出货,不能删除!");
										return;
									}
									if (res == 0) {
										ds.reload();
										Ext.Msg.alert("提示信息", "删除成功");
									}
								});
					}
				});

	}
});

function getIds() {
	var list = Ext.getCmp("packingGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var packOrder = new CotPackingOrder();
				packOrder.id = item.id;
				res.push(packOrder);
			});
	return res;
}

// 删除
function del(id, status) {
	var isPopedom = getPdmByOtherUrl("cotpackingorder.do", "DEL");
	if (isPopedom == 0) {
		Ext.Msg.alert("提示信息", "您没有删除权限");
		return;
	}
	// 判断该单是否已被审核
	if (status == 2 && loginEmpId != "admin") {
		Ext.MessageBox.alert("提示消息", "对不起,该单已经被审核,不能删除!");
		return;
	}
	var ids = new Array();
	ids.push(id);

	Ext.MessageBox.confirm('提示消息', "确定删除选中的包材采购单?", function(btn) {
				if (btn == 'yes') {
					cotPackOrderService.deletePackOrderList(ids, function(res) {
								if (res == 3) {
									Ext.Msg.alert("提示信息", "删除出错!");
									return;
								}
								if (res == 1) {
									reloadGrid('packingGrid');
									Ext.Msg.alert("提示信息", "该包材采购单含有应付账款,不能删除!");
									return;
								}
								if (res == 2) {
									reloadGrid('packingGrid');
									Ext.Msg.alert("提示信息",
											"该包材采购单有加/减费用导入到出货,不能删除!");
									return;
								}
								if (res == 0) {
									reloadGrid('packingGrid');
									Ext.Msg.alert("提示信息", "删除成功!");
								}
							});
				}
			});

}

// 修改
function windowopenMod(obj) {
	// 修改权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert('提示消息', "您没有修改权限!");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert('提示消息', "请选择一条记录!");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert('提示消息', "只能选择一条记录!")
			return;
		} else
			obj = ids[0].id;
	}
	openFullWindow('cotpackingorder.do?method=add&id=' + obj);
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
					type : 'packing'
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