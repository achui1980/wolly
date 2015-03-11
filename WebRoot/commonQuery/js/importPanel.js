ImportPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	// 加载报价表格
	var priceGrid = new PriceGrid(cfg);

	// 加载订单表格
	var orderGrid = new OrderGrid(cfg);

	// 加载送样表格
	var givenGrid = new GivenGrid(cfg);
	
	// 加载excel面板
	var reportPanel = new ReportPanel(cfg);
	
	// 加载子样品面板
	var childGrid = new ChildGrid(cfg);
	
	// 加载图片导入面板
	var picPanel = new ImportPicPanel(cfg);
	
	// 加载询盘表格
	var panEleGrid = new PanEleGrid(cfg);

	var tbl = new Ext.TabPanel({
				height : 450,
				activeTab :-1,
				bodyStyle:'background:#dfe8f6',
				defaults:{
					border:false
				},
				items : [{
							title : "EXCEL",
							name:'excelTab',
							layout : 'fit'
						},{
							title : 'Qutation',
							name:'priceTab',
							layout : 'fit'
						}, {
							title : 'Order',
							name:'orderTab',
							layout : 'fit'
						},
//						, {
//							title : 'Sample',
//							name:'givenTab',
//							layout : 'fit'
//						}
//						, {
//							title : "Sub Item",
//							name:'childTab',
//							layout : 'fit'
//						},
						{
							title : "Images",
							name:'picTab',
							layout : 'fit'
						}, {
							title : 'PRS',
							name:'prsTab',
							layout : 'fit'
						}]
			});
	//激活面板时
	tbl.items.get(0).on('activate',function(pnl){
		if (!reportPanel.isVisible()) {
				pnl.add(reportPanel);
				pnl.doLayout();
			}
	});
	tbl.items.get(1).on('activate',function(pnl){
		if (!priceGrid.isVisible()) {
			pnl.add(priceGrid);
			pnl.doLayout();
			priceGrid.load();
		}
	});
	tbl.items.get(2).on('activate',function(pnl){
		if (!orderGrid.isVisible()) {
			pnl.add(orderGrid);
			pnl.doLayout();
			orderGrid.load();
		}
	});
//	tbl.items.get(3).on('activate',function(pnl){
//		if (!givenGrid.isVisible()) {
//				givenGrid.custId=cfg.custId;
//				pnl.add(givenGrid);
//				pnl.doLayout();
//				givenGrid.load();
//			}
//	});
//	tbl.items.get(4).on('activate',function(pnl){
//		if (!childGrid.isVisible()) {
//				pnl.add(childGrid);
//				pnl.doLayout();
//				childGrid.load();
//			}
//	});
	tbl.items.get(3).on('activate',function(pnl){
		if(cfg.pId!=0){
			if (!picPanel.isVisible()) {
					pnl.add(picPanel);
					pnl.doLayout();
				}
		}else{
			_self.close();
			Ext.MessageBox.alert('Message', 'The record has not saved, you can not import pictures!');
		}
	});
	tbl.items.get(4).on('activate',function(pnl){
		if (!panEleGrid.isVisible()) {
			pnl.add(panEleGrid);
			pnl.doLayout();
			panEleGrid.load();
		}
	});
	
	//右键菜单时显示
	this.openPnl=function(type){
		tbl.setActiveTab(type);
	}

	// 表单
	var con = {
		title : 'Import Item',
		layout : 'fit',
		width : 900,
		height : 500,
		border : false,
		modal : true,
		id : "importPanel",
		//closeAction : 'hide',
		items : [tbl],
		onEsc : function(k, e){
	        e.stopEvent();
	    }
	};

	Ext.apply(con, cfg);
	ImportPanel.superclass.constructor.call(this, con);
};
Ext.extend(ImportPanel, Ext.Window, {});
