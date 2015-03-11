FenWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotfittings.do?method=findEleFen&fitId="
									+ cfg.fitId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
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
							header : "产品货号",
							dataIndex : "eleId",
							width : 220
						}, {
							header : "中文名称",
							dataIndex : "eleName",
							width : 260
						}, {
							header : "英文名称",
							dataIndex : "eleNameEn",
							width : 260
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '2|5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	gridFen = new Ext.grid.GridPanel({
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	var con = {
		title : "已分发货号",
		width : 500,
		height : 300,
		modal : true,
		layout : 'fit',
		items : [gridFen]
	}
	Ext.apply(con, cfg);
	FenWin.superclass.constructor.call(this, con);
};

Ext.extend(FenWin, Ext.Window, {});