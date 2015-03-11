OrderDetailWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	var facMap = null;
	DWREngine.setAsync(false);
	// 加载公司表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	DWREngine.setAsync(true);
	/** **************************订单明细记录表格*************************************************** */
	var orderDetailRecord = new Ext.data.Record.create([

	{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "eleId"
			}, {
				name : "factoryId"
			}, {
				name : "containerCount"
			}, {
				name : "boxCount"
			}, {
				name : "unBoxCount4OrderFac"
			}]);
	// 创建数据源
	var orderDetailds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryRemainOrderDetail&oId="
									+ cfg.oId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, orderDetailRecord)
			});
	// 创建复选框列
	var orderDetailsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var orderDetailcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [orderDetailsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Item No.",
							dataIndex : "eleId",
							width : 120
						}, {
							header : "Factory",
							dataIndex : "factoryId",
							width : 120,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "CTN",
							dataIndex : "containerCount",
							width : 100
						}, {
							header : "QTY",
							dataIndex : "boxCount",
							width : 100
						}, {
							header : "No Purchase QTY",
							dataIndex : "unBoxCount4OrderFac",
							width : 100,
							renderer : function(value) {
								return "<font color=red>" + value + "</font>";
							}
						}]
			});

	// 表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Import",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function() {
								mask();
								setTimeout(function() {
											insert();
										}, 500);
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : orderDetailds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No Records"
			});
	var orderDetailgrid = new Ext.grid.GridPanel({
				title : "Order Details",
				region : "center",
				id : "orderDetailGrid",
				// autoHeight:true,
				height : 200,
				stripeRows : true,
				margins : "0 0 0 0",
				bodyStyle : 'width:100%',
				store : orderDetailds, // 加载数据源
				cm : orderDetailcm, // 加载列
				sm : orderDetailsm,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				tbar : tb,
				viewConfig : {
					forceFit : false
				}
			});

	this.load = function() {
		// 加载表格
		orderDetailds.load({
					params : {
						start : 0,
						limit : 15
					}
				});
	}
	// 表单
	var con = {
		title : '',
		layout : 'border',
		id : "winpanel",
		width : 600,
		height : 400,
		border : true,
		modal : true,
		padding : "0",
		// closeAction : 'hide',
		items : [orderDetailgrid]
	};

	// 获得选择的记录
	var getIds = function() {
		var list = orderDetailsm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	//导入
	function insert() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Pls. select one record！');
			return;
		}
		var ary = orderDetailsm.getSelections();
		cfg.bar.insertSelect(ary);
	}

	Ext.apply(con, cfg);
	OrderDetailWin.superclass.constructor.call(this, con);
}
Ext.extend(OrderDetailWin, Ext.Window, {});