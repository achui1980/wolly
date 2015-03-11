CustPcToOrderPc = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "sortNo"
			}, {
				name : "barcode"
			}, {
				name : "eleId"
			}, {
				name : "eleTypeidLv3"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				baseParams : {
					limit : 1000
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorder.do?method=queryOrderDetail&ctPc=false&orderId="+ $('orderId').value
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel([{
				header : "ID",
				dataIndex : "id",
				width : 50,
				hidden : true
			}, {
				header : "Sort",
				dataIndex : "sortNo",
				width : 50,
				sortable : true
			}, {
				header : "Barcode",
				dataIndex : "barcode",
				width : 110,
				sortable : true
			}, {
				header : "W&C Art No.",
				dataIndex : "eleId",
				width : 110,
				sortable : true
			}]);
			
	var toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : ds,
				displayInfo : true,
//				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : 'NONE',
//				emptyMsg : "无记录",
				enableOverflow:true
			});
			
	//货号表格
	var grid = new Ext.grid.GridPanel({
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				region : 'west',
				margins : '0 5 0 0',
				width : 280,
				viewConfig : {
					forceFit : false
				}
			});
			
	//图片
	var qcPic = new CustPic({
		region:'center'
	});
	
	// 行点击时加载右边折叠面板表单
	grid.on("rowclick", function(grid, rowIndex, e) {
					var rec=ds.getAt(rowIndex);
					qcPic.loadGrid(rec.id,rec.data.eleTypeidLv3);
			});
	
	this.loadGrid=function(){
		ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});
	}
		
	//构造
	var con = {
		title:'Client Art Work',
		layout : 'border',
		border : false,
		width:900,
		height:400,
		modal:true,
		items : [grid,qcPic]
	};

	Ext.apply(con, cfg);
	CustPcToOrderPc.superclass.constructor.call(this, con);
};
Ext.extend(CustPcToOrderPc, Ext.Window, {});