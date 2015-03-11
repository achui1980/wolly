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

	// 加载图片导入面板
	var picPanel = new ImportPicPanel(cfg);

	var tbl = new Ext.TabPanel({
				height : 450,
				activeTab : -1,
				items : [{
							title : "EXCEL",
							name : 'excelTab',
							layout : 'fit'
						}, {
							title : '报价记录',
							name : 'priceTab',
							layout : 'fit'
						}, {
							title : '订单记录',
							name : 'orderTab',
							layout : 'fit'
						}, {
							title : '送样记录',
							name : 'givenTab',
							layout : 'fit'
						}, {
							title : "图片导入",
							name : 'picTab',
							layout : 'fit'
						}]
			});
	// 激活面板时
	tbl.on('tabchange', function(tb, pnl) {
				if (pnl.name == 'priceTab') {
					if (!priceGrid.isVisible()) {
						priceGrid.custId = cfg.custId;
						pnl.add(priceGrid);
						pnl.doLayout();
						priceGrid.load();
					}
				}
				if (pnl.name == 'orderTab') {
					if (!orderGrid.isVisible()) {
						pnl.add(orderGrid);
						pnl.doLayout();
						orderGrid.load();
					}
				}
				if (pnl.name == 'givenTab') {
					if (!givenGrid.isVisible()) {
						pnl.add(givenGrid);
						pnl.doLayout();
						givenGrid.load();
					}
				}
				if (pnl.name == 'excelTab') {
					if (!reportPanel.isVisible()) {
						pnl.add(reportPanel);
						pnl.doLayout();
					}
				}
				if (pnl.name == 'picTab') {
					if (cfg.pId != 0) {
						if (!picPanel.isVisible()) {
							pnl.add(picPanel);
							pnl.doLayout();
						}
					} else {
						_self.close();
						Ext.MessageBox.alert('提示消息', '该单还没保存,不能导入图片!');
					}
				}
			});
	// 右键菜单时显示
	this.openPnl = function(type) {
		tbl.setActiveTab(type);
	}
	// 表单
	var con = {
		title : '导入货号',
		layout : 'fit',
		width : 800,
		height : 500,
		border : false,
		modal : true,
		// closeAction : 'hide',
		items : [tbl],
		onEsc : function(k, e){
	        e.stopEvent();
	    }
	};

	Ext.apply(con, cfg);
	ImportPanel.superclass.constructor.call(this, con);
};
Ext.extend(ImportPanel, Ext.Window, {});
