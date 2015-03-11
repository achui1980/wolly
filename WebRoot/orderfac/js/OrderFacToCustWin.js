OrderFacToCustWin = function(cfg){
	var _self = this;
	if (!cfg)
		cfg = {};
	// 加载客户信息表缓存
	DWREngine.setAsync(false);
	var custMap;
	baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName", function(
					res) {
				custMap = res;
			});
	DWREngine.setAsync(true);
	var orderRecord = new Ext.data.Record.create([
		{name : "custId",type : "int"},
		{name : "orderNoOrd"},
		{name : "poNo"}
	]);
	// 创建数据源
	var _dorder = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=showOrderCustByFac&orderfacId="
									+ cfg.orderfacId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount"
							//idProperty : "id"
						}, orderRecord)
			});

	// 创建需要在表格显示的列
	var order_cm = new Ext.grid.ColumnModel([{
				header : "Customers",
				dataIndex : "custId",
				width : 80,
				renderer:function(value){
					return custMap[value];
				},
				sortable : true

			},{
				header : "Order No",
				dataIndex : "orderNoOrd",
				width : 150,
				sortable : true

			
			},{
				//header : "PO#",
				header:'ClientP/O',
				dataIndex : "poNo",
				width : 80,
				sortable : true
			}]);

	var order_grid = new Ext.grid.GridPanel({
				region : "center",
				id : "orderGrid",
				stripeRows : true,
				bodyStyle : 'width:100%;',
				autoScroll : true,
				store : _dorder, // 加载数据源
				cm : order_cm,// 加载列
				loadMask : true, // 是否显示正在加载
				viewConfig : {
					forceFit : true
				}
			});

	// 分页基本参数
	_dorder.load();
	var order = new Ext.Panel({
				region : 'center',
				border : false,
				width : 300,
				layout : 'border',
				items : [order_grid]
	});
	// 表单
	var con = {
		title : 'Corresponding to customer information production contract',
		layout : 'border',
		width : 450,
		height : 180,
		border : false,
		modal : true,
		items : [order]
	};

	Ext.apply(con, cfg);
	OrderFacToCustWin.superclass.constructor.call(this, con);
}
Ext.extend(OrderFacToCustWin, Ext.Window, {});
