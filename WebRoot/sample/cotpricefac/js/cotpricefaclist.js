var currencyMap;
baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(res) {
			currencyMap = res;
		});
var facMap;
baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(res) {
			facMap = res;
		});
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
				name : "addTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "facId"
			}, {
				name : "priceFac"
			}, {
				name : "priceUint"
			}, {
				name : "priceRemark"
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
							url : "cotpricefac.do?method=query&mainId="
									+ $('eleId').value
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
							header : "Date",
							dataIndex : "addTime",
							width : 100,
							renderer : function(value, meta, rec, rowIdx,
									colIdx, ds) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "Y-m-d");
								else
									return value;
							}
						}, {
							header : "Supplier",
							dataIndex : "facId",
							width : 150,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "Price",
							dataIndex : "priceFac",
							width : 80,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00")
							}
						}, {
							header : "Currency",
							dataIndex : "priceUint",
							width : 80,
							renderer : function(value) {
								return currencyMap[value];
							}
						}, {
							header : "Remark",
							dataIndex : "priceRemark",
							width : 400
						}, {
							header : "Operation",
							dataIndex : "id",
							width : 50,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var del = '<a href="javascript:deleteBatch()">Delete</a>';
								return del
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 采购厂家
	var facComb = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'facId',
				emptyText : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				pageSize : 10,
				width : 120,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'facId'
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
						},facComb, {
							xtype : 'searchcombo',
							width : 1,
							cls : 'hideCombo',
							editable : false,
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							iconCls : "page_add",
							handler : windowopen.createDelegate(this, [0]),
							cls : "SYSOP_ADD"
						}, {
							text : "Update",
							iconCls : "page_mod",
							handler : windowopen.createDelegate(this, [null]),
							cls : "SYSOP_MOD"
						}, {
							text : "Delete",
							iconCls : "page_del",
							handler : deleteBatch,
							cls : "SYSOP_DEL"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				id : "pricefacGrid",
				stripeRows : true,
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
				windowopen(record.get("id"));
			});
});
function getIds() {
	var list = Ext.getCmp("pricefacGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var priceFac = new CotPriceFac();
				priceFac.id = item.id
				res.push(priceFac);
			});
	return res;
}
// 批量删除
function deleteBatch() {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
		return;
	}
	var ids = getIds();
	if (ids.length == 0) {
		Ext.Msg.alert("Message", "Please select a record");
		return;
	}
	var flag = window.confirm("are you sure to delete it?");
	if (flag) {
		cotPriceFacService.delPriceFac(ids, function(res) {
					if (res == true) {
						Ext.Msg.alert("Message", "Deleted successfully");
						reloadGrid("pricefacGrid");
					} else {
						Ext.Msg.alert("Message", "Delete failed");
					}
				});
	}
}
function windowopen(obj) {
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("Message", "Sorry,you can select only one record!")
			return;
		} else
			obj = ids[0].id;
	} else if (obj == 0) {
		openWindowBase(260, 400, 'cotpricefac.do?method=add&eleId='
						+ $('eleId').value);
		return;
	}
	openWindowBase(260, 400, 'cotpricefac.do?method=add&id=' + obj + "&eleId="
					+ $('eleId').value);
}