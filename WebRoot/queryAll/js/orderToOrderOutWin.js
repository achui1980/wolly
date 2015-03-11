OrderToOrderOutWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "boxCount"
			}, {
				name : "containerCount"
			}, {
				name : "unSendNum"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotqueryall.do?method=queryOrderOutByEleId&oId="+cfg.oId
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
	var cm = new Ext.grid.ColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				hidden : true
			}, {
				header : "Invoice No.",
				dataIndex : "orderNo",
				width : 150,
				sortable : true,
				summaryRenderer : function(v, params, data) {
								params.css = 'fg';
								return "Total：";
							}
			}, {
				header : "Art No.",
				dataIndex : "eleId",
				width : 80,
				sortable : true
			}, {
				header : "Chinese name",
				dataIndex : "eleName",
				width : 120,
				sortable : true
			}, {
				header : "QTY",
				dataIndex : "boxCount",
				width : 70,
				sortable : true,
				summaryType : 'sum'
			}, {
				header : "CTNS",
				dataIndex : "containerCount",
				width : 70,
				sortable : true,
				summaryType : 'sum'
			}]);

	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "'No data to display"
			});
	
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				border : false,
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : summary,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
			
	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});
	
	var con = {
		width : 530,
		height : 300,
		title : "P.I. &nbsp;<font color=red>"+cfg.eleId+"</font>&nbsp; for Invoice",
		modal : true,
		layout:'fit',
		items : [grid]
	}
	Ext.apply(con, cfg);
	OrderToOrderOutWin.superclass.constructor.call(this, con);
};

Ext.extend(OrderToOrderOutWin, Ext.Window, {});