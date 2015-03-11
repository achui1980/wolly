Ext.onReady(function() {
	var companyMap = null;
	var currencyMap = null;
	var facMap = null;
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
	// 加载公司表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
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
				name : "companyId"
			}, {
				name : "factoryId"
			}, {
				name : "finaceNo"
			}, {
				name : "amountDate"
			}, {
				name : "finaceName"
			}, {
				name : "source"
			}, {
				name : "amount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "currencyId"
			}, {
				name : "realAmount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "remainAmount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "zhAmount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "zhRemainAmount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "addDate"
			}, {
				name : "fkId"
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
							url : "cotdeal.do?method=query"
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
							header : "公司简称",
							dataIndex : "companyId",
							width : 100,
							renderer : function(value) {
								return companyMap[value];
							}
						}, {
							header : "厂家",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "帐款单号",
							dataIndex : "finaceNo",
							width : 150
						}, {
							header : "帐款日期",
							dataIndex : "amountDate",
							width : 80,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
							}
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 150
						}, {
							header : "费用来源",
							dataIndex : "source",
							width : 80,
							renderer : function(value) {
								if (value == 'orderfac')
									return "生产合同";
								else if (value == 'orderout')
									return "出货合同";
								else if (value == 'fitorder')
									return "配件采购";
								else if (value == 'packorder')
									return "包材采购";
							}
						}, {
							header : "帐款金额",
							dataIndex : "amount",
							width : 75
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 50,
							renderer : function(value) {
								return currencyMap[value];
							}
						}, {
							header : "已付金额",
							dataIndex : "realAmount",
							width : 75
						}, {
							header : "剩余金额",
							dataIndex : "remainAmount",
							width : 75
						},

						{
							header : "流转金额",
							dataIndex : "zhAmount",
							width : 75
						}, {
							header : "流转后剩余金额",
							dataIndex : "zhRemainAmount",
							width : 100
						}, {
							header : "制单日期",
							dataIndex : "addDate",
							width : 75,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
							}
						}, {
							header : "操作",
							dataIndex : "id",
							width : 60,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var nbsp = "&nbsp &nbsp &nbsp"
								var fkId = record.get("fkId");
								var source = record.get("source");
								var del = '<a href="javascript:showSourceWin('
										+ fkId + ',\'' + source + '\')">来源</a>';
								return del + nbsp;
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

	// 厂家
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
		cmpId : 'factoryId',
		emptyText : "厂家",
		sendMethod : "post",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		autoLoad : false,// 默认自动加载
		pageSize : 5,
		width : 120,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'factoryId'
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
						}, facBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "帐款单号",
							isSearchField : true,
							searchName : 'financeNo',
							isJsonType : false,
							store : ds
						},'->',{
							xtype:'label',
							style : 'color:green;font-weight: bold;',
							text:'提示：双击表格行查看付款和流转记录！'
						}]
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "dealGrid",
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
	var view = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	grid.on("rowdblclick", function(gridpanel, rowIdx) {

				var record = gridpanel.getStore().getAt(rowIdx);
				showRecvDetailDiv(record.get("id"))
			});
});
// 显示来源页面
function showSourceWin(fkId, source) {
	if (fkId != null && fkId != '') {
		if (source == 'orderfac') {
			openFullWindow('cotorderfac.do?method=add&id=' + fkId);
		}
		if (source == 'orderout') {
			openFullWindow('cotorderout.do?method=addOrder&id=' + fkId);
		}
		if (source == 'fitorder') {
			openFullWindow('cotfittingorder.do?method=add&id=' + fkId);
		}
		if (source == 'packorder') {
			openFullWindow('cotpackingorder.do?method=add&id=' + fkId);
		}
	}
}
function showRecvDetailDiv(mainId) {
	var cfg = {};
	cfg.dealId = mainId;
	var detailWin = new DealDetailWin(cfg);
	detailWin.show();
	detailWin.load();
}