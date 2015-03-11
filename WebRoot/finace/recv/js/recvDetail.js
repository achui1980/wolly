RecvDetailWin = function(cfg) {
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
	/** **************************收款记录表格*************************************************** */
	var recvDetailRecord = new Ext.data.Record.create([{
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
	var recvDetailds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotrecv.do?method=queryRecvDetail&recvId="
									+ cfg.recvId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, recvDetailRecord)
			});
	// 创建复选框列
	var recvDetailsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var recvDetailcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [recvDetailsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : " 收款单号",
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
							header : "本次冲帐",
							dataIndex : "currentAmount",
							width : 150
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : recvDetailds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var recvDetailgrid = new Ext.grid.GridPanel({
				title : "收款记录",
				region : "north",
				id : "recvDetailGrid",
				height : 200,
				stripeRows : true,
				store : recvDetailds, // 加载数据源
				cm : recvDetailcm, // 加载列
				sm : recvDetailsm,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	/** *******************************流转记录表格********************************************** */
	var liuRecvRecord = new Ext.data.Record.create([{
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
	var liuRecvds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotrecv.do?method=queryLiuRecv&recvId="
									+ cfg.recvId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, liuRecvRecord)
			});
	// 创建复选框列
	var liuRecvsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var liuRecvcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [liuRecvsm, {
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
	var liuRecvtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : liuRecvds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var liuRecvgrid = new Ext.grid.GridPanel({
				title : "流转记录",
				region : "center",
				id : "liuRecvGrid",
				stripeRows : true,
				store : liuRecvds, // 加载数据源
				cm : liuRecvcm, // 加载列
				sm : liuRecvsm,
				loadMask : true, // 是否显示正在加载
				bbar : liuRecvtoolBar,
				viewConfig : {
					forceFit : false
				}
			});
	this.load = function() {
		// 加载表格
		recvDetailds.load({
					params : {
						start : 0,
						limit : 15
					}
				});
		liuRecvds.load({
					params : {
						start : 0,
						limit : 15
					}
				});
	}
	// 表单
	var con = {
		title : '应收帐款明细',
		layout : 'border',
		id : "winpanel",
		width : 800,
		height : 500,
		border : false,
		modal : true,
		items : [recvDetailgrid, liuRecvgrid]
	};
	Ext.apply(con, cfg);
	RecvDetailWin.superclass.constructor.call(this, con);
}
Ext.extend(RecvDetailWin, Ext.Window, {});