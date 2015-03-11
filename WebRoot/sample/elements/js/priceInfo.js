Ext.onReady(function() {
	DWREngine.setAsync(false);
	var currencyMap;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				currencyMap = res;
			});
	var boxMap;
	baseDataUtil.getBaseDicDataMap("CotBoxType", "id", "typeName",
			function(res) {
				boxMap = res;
			});
	var empsMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empsMap = res;
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
				name : "priceTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "priceNo"
			}, {
				name : "businessPerson"
			}, {
				name : "customerShortName"
			}, {
				name : "pricePrice"
			}, {
				name : "currencyId"
			}, {
				name : "boxTypeId"
			}, {
				name : "validMonths"
			}, {
				name : "priceCompose"
			}, {
				name : "priceRate"
			}, {
				name : "priceFac"
			}, {
				name : "priceFacUint"
			}, {
				name : "priceStatus"
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
							url : "cotelements.do?method=loadPriceInfo&eleId="
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
			sortable : true,
			width : 100
		},
		columns : [sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : " Date",
					width : 80,
					dataIndex : "priceTime",
					renderer : function(value, meta, rec, rowIdx, colIdx, ds) {
						if (value != null)
							return Ext.util.Format.date(new Date(value.time),
									"Y-m-d");
					}
				}, {
					header : "Offer No",
					dataIndex : "priceNo",
					width : 180
				}, {
					header : " Sales",
					dataIndex : "businessPerson",
					renderer : function(value) {
						return empsMap[value];
					}
				}, {
					header : " Client",
					dataIndex : "customerShortName"
				}, {
					header : " Sales Price",
					dataIndex : "pricePrice",
					align : "right",
					width : 100,
					renderer : function(value) {
						return "<font color=blue>"
								+ Ext.util.Format.number(value, "0.000")
								+ "</font>"
					}
				}, {
					header : " Currency",
					dataIndex : "currencyId",
					renderer : function(value) {
						return "<font color=red>" + currencyMap[value]
								+ "</font>"
					}
				}, {
					header : "Packing Way",
					dataIndex : "boxTypeId",
					width : 100,
					renderer : function(value) {
						return boxMap[value];
					}
				}, {
					header : "Vaild Date",
					dataIndex : "validMonths",
					width : 70
				}, {
					header : "Price Terms",
					dataIndex : "priceCompose",
					width : 150
				}, {
					header : "rate",
					dataIndex : "priceRate",
					width : 60,
					renderer : function(value) {
						return Ext.util.Format.number(value, "0.000");
					}
				}, {
					header : "Purchase Price",
					dataIndex : "priceFac",
					renderer : function(value) {
						return Ext.util.Format.number(value, "0.000");
					},
					width : 100
				}, {
					header : "Currency",
					dataIndex : "priceFacUint",
					width : 80,
					renderer : function(value) {
						return "<font color=blue>" + currencyMap[value]
								+ "</font>"
					}
				}, {
					header : "Stutas",
					dataIndex : "priceStatus",
					width : 80,
					renderer : function(value) {
						if (value == 0) {
							return "<font color='green'>Non-reviewed</font>";
						} else if (value == 1) {
							return "<font color='red'>Review without</font>";
						} else if (value == 2) {
							return "<font color='blue'>reviewed</font>";
						} else if (value == 3) {
							return "<font color='#10418C'>Need to be reviewed</font>";
						} else if (value == 9) {
							return "<font color='green'>Not review</font>";
						}
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

	// 客户
	var customerBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		emptyText : "Client",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 5,
		width : 120,
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
				cmpId : 'businessPersonFind',
				emptyText : "Sales",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				width : 120,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'businessPersonFind'
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
						}, customerBox, busiBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Offer No.",
							isSearchField : true,
							searchName : 'priceNoFind',
							isJsonType : false,
							store : ds
						}]
			});
	var grid = new Ext.grid.GridPanel({
				id : "priceGrid",
				stripeRows : true,
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
				layout : "fit",
				items : [grid]
			});
	grid.on("rowdblclick", function(thisgrid, rowIdx) {
				var rec = thisgrid.getStore().getAt(rowIdx);
				windowopen(rec.get("id"));
			});
});
// 打开报价单编辑页面
function windowopen(obj) {
	// 查看的权限
	var isPopedom = getPopedomByOpType('cotprice.do', "SEL");
	if (isPopedom == 0)// 查看的权限
	{
		alert("Sorry, you do not have Authority!!");
		return;
	}
	openFullWindow('cotprice.do?method=addPrice&id=' + obj);
}