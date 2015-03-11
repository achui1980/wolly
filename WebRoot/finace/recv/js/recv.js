Ext.onReady(function() {
	var companyMap = null;
	var currencyMap = null;
	var custMap = null;
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
	baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName",
			function(res) {
				custMap = res;
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
				name : "custId"
			}, {
				name : "finaceNo"
			}, {
				name : "amountDate"
			}, {
				name : "finaceName"
			}, {
				name : "source"
			}, {
				name : "companyId"
			}, {
				name : "amount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "currencyId"
			}, {
				name : "realAmount",
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
							url : "cotrecv.do?method=query"
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
							header : "客户",
							dataIndex : "custId",
							width : 150,
							renderer : function(value) {
								return custMap[value];
							}
						}, {
							header : "帐款单号",
							dataIndex : "finaceNo",
							width : 150,
							summaryRenderer : function(v, params, data) {
								return "合计：";
							}
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
							width : 100
						}, {
							header : "费用来源",
							dataIndex : "source",
							width : 100,
							renderer : function(value) {
								if (value == "orderout")
									return "出货"
								else if (value == "order")
									return "外销合同"
								else if (value == "given")
									return "送样"
							}
						}, {
							header : "公司简称",
							dataIndex : "companyId",
							width : 150,
							renderer : function(value) {
								return companyMap[value];
							}
						}, {
							header : "帐款金额",
							dataIndex : "amount",
							summaryType : 'sum',
							width : 80
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 80,
							renderer : function(value) {
								return currencyMap[value];
							}
						}, {
							header : "已冲帐金额",
							dataIndex : "realAmount",
							summaryType : 'sum',
							width : 80
						}, {
							header : "未流转金额",
							dataIndex : "zhRemainAmount",
							summaryType : 'sum',
							width : 80
						}, {
							header : "制单日期",
							dataIndex : "addDate",
							width : 80,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
							}
						}, {
							header : "来源",
							dataIndex : "id",
							width : 100,
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

	// 客户
	var customerBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
		cmpId : 'custId',
		emptyText : "客户",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		sendMethod : "post",
		pageSize : 5,
		width : 100,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custId'
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
						}, customerBox, curBox, {
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
							text:'提示：双击表格行查看收款和流转记录！'
						}]
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "recvGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : [summary],
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	var viewport = new Ext.Viewport({
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
		if (source == 'given') {
			openFullWindow('cotgiven.do?method=add&id=' + fkId);
		}
		if (source == 'order') {
			openFullWindow('cotorder.do?method=addOrder&id=' + fkId);
		}
		if (source == 'orderout') {
			openFullWindow('cotorderout.do?method=addOrder&id=' + fkId);
		}
	}
}
function showRecvDetailDiv(mainId) {
	var cfg = {};
	cfg.recvId = mainId;
	var detailWin = new RecvDetailWin(cfg);
	detailWin.show();
	detailWin.load();
}