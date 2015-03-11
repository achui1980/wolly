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
	var facMap;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	DWREngine.setAsync(true);

	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([
	{
				name : "id",
				type : "int"
			},
			{
				name : "orderTime",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "orderNo"
			}, {
				name : "factoryId"
			}, {
				name : "businessPerson"
			}, {
				name : "boxCount"
			}, {
				name : "priceFac"
			}, {
				name : "priceFacUint"
			}, {
				name : "boxTypeId"
			}, {
				name : "totalFac"
			}, {
				name : "orderStatus"
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
							url : "cotelements.do?method=loadOrderFacInfo&eleId="
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
							header : "Purchase Date",
							dataIndex : "orderTime",
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d')
							},
							width : 100
						}, {
							header : "Purchase Date",
							dataIndex : "orderNo",
							width : 200
						}, {
							header : "Suppliers",
							dataIndex : "factoryId",
							width : 150,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "Sales",
							hidden:true,
							dataIndex : "businessPerson",
							width : 100,
							renderer : function(value) {
								return empsMap[value];
							}
						}, {
							header : "Quantity",
							dataIndex : "boxCount",
							width : 100
						}, {
							header : "Price",
							dataIndex : "priceFac",
							width : 80,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00")
							}
						}, {
							header : "Currency",
							dataIndex : "priceFacUint",
							width : 60,
							renderer : function(value) {
								return currencyMap[value]
							}
						}, {
							header : "Packing Way",
							dataIndex : "boxTypeId",
							width : 100,
							renderer : function(value) {
								return boxMap[value];
							}
						}, {
							header : "amount",
							dataIndex : "totalFac",
							width : 80,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00")
							}
						}, {
							header : "Status",
							dataIndex : "orderStatus",
							width : 80,
							renderer : function(value) {
								if (value == 0) {
									return "<font color='green'>Non-reviewed </font>";
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
			
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPersonFind',
				emptyText : "Sales",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				width:100,
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
						}, busiBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Purchase Records",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}]
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "orderfacGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border:false,
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