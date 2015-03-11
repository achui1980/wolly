DealDetailWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				currencyMap = res;
			});
	DWREngine.setAsync(true);
	/** **************************付款记录表格*************************************************** */
	var dealDetailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "addTime"
			}, {
				name : "currencyId"
			}, {
				name : "currentAmount",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}]);
	// 创建数据源
	var dealDetailds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotdeal.do?method=queryDealDetail&dealId="
									+ cfg.dealId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, dealDetailRecord)
			});
	// 创建复选框列
	var dealDetailsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var dealDetailcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [dealDetailsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "付款单号",
							dataIndex : "finaceNo",
							width : 200
						}, {
							header : "冲帐日期",
							dataIndex : "addTime",
							width : 150,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
							}
						}, {
							header : " 币种",
							dataIndex : "currencyId",
							width : 100,
							renderer : function(value) {
								return currencyMap[value];
							}
						}, {
							header : "总金额",
							dataIndex : "amount",
							width : 150
						}, {
							header : "本次冲帐",
							dataIndex : "currentAmount",
							width : 150
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : dealDetailds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var dealDetailgrid = new Ext.grid.GridPanel({
				title : "付款记录",
				region : "north",
				id : "dealDetailGrid",
				height : 200,
				stripeRows : true,
				store : dealDetailds, // 加载数据源
				cm : dealDetailcm, // 加载列
				sm : dealDetailsm,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	/** *******************************流转记录表格********************************************** */
	var liuDealRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "finaceName"
			}, {
				name : "flag"
			}, {
				name : "currencyId"
			}, {
				name : "amount",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}]);
	// 创建数据源
	var liuDealds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotdeal.do?method=queryTransDetail&dealId="
									+ cfg.dealId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, liuDealRecord)
			});
	// 创建复选框列
	var liuDealsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var liuDealcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [liuDealsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "发票编号",
							dataIndex : "orderNo",
							width : 200
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 150
						}, {
							header : "加/减",
							dataIndex : "flag",
							width : 100,
							renderer : function(value) {
								if (value == 'M')
									return "减";
								else
									return "加";
							}
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 150,
							renderer : function(value) {
								return currencyMap[value];
							}
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 150
						}]
			});
	var liuDealtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : liuDealds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var liuDealgrid = new Ext.grid.GridPanel({
				title : "流转记录",
				region : "center",
				id : "liuDealGrid",
				stripeRows : true,
				store : liuDealds, // 加载数据源
				cm : liuDealcm, // 加载列
				sm : liuDealsm,
				loadMask : true, // 是否显示正在加载
				bbar : liuDealtoolBar,
				viewConfig : {
					forceFit : false
				}
			});
	this.load = function() {
		// 加载表格
		dealDetailds.load({
					params : {
						start : 0,
						limit : 15
					}
				});
		liuDealds.load({
					params : {
						start : 0,
						limit : 15
					}
				});
	}
	// 表单
	var con = {
		title : '应付帐款明细',
		layout : 'border',
		id : "winpanel",
		width : 800,
		height : 500,
		border : true,
		modal : true,
		padding : "0",
		items : [dealDetailgrid, liuDealgrid]
	};
	Ext.apply(con, cfg);
	DealDetailWin.superclass.constructor.call(this, con);
}
Ext.extend(DealDetailWin, Ext.Window, {});