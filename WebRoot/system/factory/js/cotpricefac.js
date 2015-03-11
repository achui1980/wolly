Ext.onReady(function() {

	DWREngine.setAsync(false);
	var currencyMap;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				currencyMap = res;
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
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "addTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "priceFac"
			}, {
				name : "priceUint"
			}, {
				name : "shortName"
			}, {
				name : "priceRemark"
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
							url : "cotfactory.do?method=queryPriceFac&factoryId="
									+ $('factoryId').value
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
							header : "Price Date",
							dataIndex : "addTime",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return value.year + "-" + (value.month + 1)
											+ "-" + value.day;
								}
							}
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 130
						}, {
							header : "Chinese name",
							dataIndex : "eleName",
							width : 130
						}, {
							header : "Quote",
							dataIndex : "priceFac",
							width : 80
						}, {
							header : "Currency",
							dataIndex : "priceUint",
							width : 80,
							renderer : function(value, meta, rec, rowIdx,
									colIdx, ds) {
								return currencyMap[value]
							}
						}, {
							header : "Remarks",
							dataIndex : "priceRemark",
							width : 80
						}

				]
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

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 100,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 100,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Art No.",
							isSearchField : true,
							searchName : 'eleIdFind',
							isJsonType : false,
							store : ds
						}]
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "pricefacGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});

	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
});
