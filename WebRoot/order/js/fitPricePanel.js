FitPricePanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	// 加载报价表格
	var fitGrid = new FitGrid(cfg);
	var elePriceGrid = new ElePriceGrid(cfg);

	var tbl = new Ext.TabPanel({
				height : 450,
				activeTab :-1,
				items : [{
							title : '配件记录',
							name:'fitTab',
							layout : 'fit'
						}, {
							title : '成本记录',
							name:'priceTab',
							layout : 'fit'
						}]
			});
	//激活面板时	
	tbl.items.get(0).on('activate',function(pnl){
		if (!fitGrid.isVisible()) {
			pnl.add(fitGrid);
			pnl.doLayout();
			fitGrid.load();
		}
	});
	tbl.items.get(1).on('activate',function(pnl){
		if (!elePriceGrid.isVisible()) {
			pnl.add(elePriceGrid);
			pnl.doLayout();
			elePriceGrid.load();
		}
	});
	
	//右键菜单时显示
	this.openPnl=function(type){
		tbl.setActiveTab(type);
	}

	// 表单
	var con = {
		title : '配件及成本信息',
		layout : 'fit',
		id:"winpanel",
		width : 800,
		height : 500,
		border : false,
		modal : true,
		padding : "0",
		//closeAction : 'hide',
		items : [tbl]
	};

	Ext.apply(con, cfg);
	FitPricePanel.superclass.constructor.call(this, con);
};
Ext.extend(FitPricePanel, Ext.Window, {});
