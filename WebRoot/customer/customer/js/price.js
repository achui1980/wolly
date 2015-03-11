Ext.onReady(function() {
	var record_start = 0;
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
				name : "priceTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "priceNo"
			}, {
				name : "empsName"
			}, {
				name : "priceCompose"
			}, {
				name : "priceRate"
			}, {
				name : "priceStatus"
			}, {
				name : "validMonths"
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
							url : "cotprice.do?method=query&custId="
									+ $('custId').value + "&flag=customerPage"
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
						},new Ext.grid.RowNumberer({
							header : "Sort No.",
							width : 55,
							renderer : function(value, metadata, record,
									rowIndex) {
								return record_start + 1 + rowIndex;
							}
						}), {
							header : "Date",
							dataIndex : "priceTime",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "Offer No.",
							dataIndex : "priceNo",
							width : 120
						}, {
							header : "业务员",
							hidden:true,
							dataIndex : "empsName",
							width : 80
						}, {
							header : "Delivery Terms",
							dataIndex : "priceCompose",
							width : 140
						}, {
							header : "汇率",
							hidden:true,
							dataIndex : "priceRate",
							width : 60
						}, {
							header : "审核状态",
							hidden:true,
							dataIndex : "priceStatus",
							width : 80,
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
							header : "Valid Date",
							dataIndex : "validMonths",
							width : 80
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

	// 业务员
	var busiBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'businessPerson',
		emptyText : "Sales",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		sendMethod : "post",
		pageSize : 10,
		selectOnFocus : true,
		width:100,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 210,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'businessPerson'
	});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 90,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, busiBox, {
							xtype : 'searchcombo',
							width : 95,
							emptyText : "Offer No.",
							isSearchField : true,
							searchName : 'priceNoFind',
							isJsonType : false,
							store : ds
						}]
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "priceGrid",
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
		Ext.MessageBox.confirm('提示信息', "是否确定删除勾选的报价单?", function(btn) {
					if (btn == 'yes') {
						cotPriceService.deletePrices(list, function(res) {
									Ext.MessageBox.alert("提示信息", "删除成功");
									reloadGrid("priceGrid");
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
		openFullWindow('cotprice.do?method=addPrice&custId='
				+ $('custId').value + '&busiId=' + $('busiId').value);
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPdmByOtherUrl("cotprice.do", "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("提示信息", '您没有打印权限！');
			return;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'price'
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

});
// 表格中删除
function del(id) {
	var isPopedom = getPdmByOtherUrl("cotprice.do", "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("提示消息", "您没有删除权限");
		return;
	}
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('提示信息', "是否确定删除此报价单?", function(btn) {
				if (btn == 'yes') {
					cotPriceService.deletePrices(list, function(res) {
								if (res) {
									Ext.MessageBox.alert("提示消息", "删除成功");
									reloadGrid("priceGrid");
								} else {
									Ext.MessageBox.alert("提示消息", "该报价单已经被使用中");
								}
							});
				}
			});
}
function getIds() {
	var list = Ext.getCmp("priceGrid").getSelectionModel().getSelections();
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
	openFullWindow('cotprice.do?method=addPrice&id=' + obj);
}