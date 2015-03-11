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

	var tbl = new Ext.TabPanel({
				height : 450,
				activeTab :-1,
				items : [{
							title : "EXCEL",
							name:'excelTab',
							layout : 'fit'
						},{
							title : 'Quote',
							name:'priceTab',
							layout : 'fit'
						}, {
							title : 'Order',
							name:'orderTab',
							layout : 'fit'
						}, {
							title : '送样记录',
							name:'givenTab',
							layout : 'fit'
						}, {
							title : "子货号",
							name:'childTab',
							layout : 'fit'
						}, {
							title : "Picture",
							name:'picTab',
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
			priceGrid.custId=cfg.custId;
			pnl.add(priceGrid);
			pnl.doLayout();
			priceGrid.load();
		}
	});
	tbl.items.get(2).on('activate',function(pnl){
		if (!orderGrid.isVisible()) {
			orderGrid.custId=cfg.custId;
			pnl.add(orderGrid);
			pnl.doLayout();
			orderGrid.load();
		}
	});
	tbl.items.get(3).on('activate',function(pnl){
		if (!givenGrid.isVisible()) {
				givenGrid.custId=cfg.custId;
				pnl.add(givenGrid);
				pnl.doLayout();
				givenGrid.load();
			}
	});
	tbl.items.get(4).on('activate',function(pnl){
		if (!childGrid.isVisible()) {
				pnl.add(childGrid);
				pnl.doLayout();
				childGrid.load();
			}
	});
	tbl.items.get(5).on('activate',function(pnl){
		if(cfg.pId!=0){
			if (!picPanel.isVisible()) {
					pnl.add(picPanel);
					pnl.doLayout();
				}
		}else{
			_self.close();
			Ext.MessageBox.alert('Message', 'Not save the record, can not import pictures!');
		}
	});
	
	//右键菜单时显示
	this.openPnl=function(type){
		tbl.setActiveTab(type);
	}

	// 表单
	var con = {
		title : 'Import',
		layout : 'fit',
		width : 800,
		height : 500,
		border : true,
		modal : true,
		id : "importPanel",
		//closeAction : 'hide',
		items : [tbl]
	};

	Ext.apply(con, cfg);
	ImportPanel.superclass.constructor.call(this, con);
};
Ext.extend(ImportPanel, Ext.Window, {});
