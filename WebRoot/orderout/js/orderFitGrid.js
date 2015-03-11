// 配件采购
OrderFitGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	// 全局变量
	this.custId;

	var packRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fittingOrderNo"
			}, {
				name : "orderDate",
				sortType:timeSortType.createDelegate(this)
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryFitOrderByFinace&orderOutId="+parent.$('pId').value
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, packRecord)
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "配件采购单号",
							dataIndex : "fittingOrderNo",
							width : 160
						}, {
							header : "下单日期", // 表头
							dataIndex : "orderDate",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	this.load = function() {

		// 加载表格
		ds.load({
					params : {
						start : 0,
						limit : 15,
						facIds : _self.fitorderIds
					}
				});
	}

	// 行点击时加载右边折叠面板表单
	grid.on("rowclick", function(grid, rowIndex, e) {
				clickType = "fitorder";
				var rec = ds.getAt(rowIndex);
				var sId = rec.data.id;
				var otherGrid = Ext.getCmp("otherGrid");
				var fac_ds = otherGrid.getStore();
				// 重新设置store路径
				fac_ds.proxy.setApi({
							read : "cotorderout.do?method=queryFacOther&facId="
									+ sId+"&flag=fitorder"
						});
				fac_ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
				var dealGrid = Ext.getCmp("dealGrid");
				var deal_ds = dealGrid.getStore();
				// 重新设置store路径
				deal_ds.proxy.setApi({
							read : "cotorderout.do?method=queryFacDeal&facId="
									+ sId+"&flag=fitorder"
						});
				
				deal_ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
				
				var overfeeGrid = Ext.getCmp("overfeeGrid");
				var overfee_ds = overfeeGrid.getStore();
				// 重新设置store路径
				overfee_ds.proxy.setApi({
							read : "cotorderout.do?method=queryOverFee&facId="
									+ sId+"&flag=fitorder"
						});
				overfee_ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
			});

	// 表单
	var con = {
		//title : '订单记录',
		layout : 'fit',
		width : 600,
		height : 490,
		border : false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	OrderFitGrid.superclass.constructor.call(this, con);
};
Ext.extend(OrderFitGrid, Ext.Panel, {});