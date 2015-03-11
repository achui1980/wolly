ImportPanel = function(cfg) {

	var _self = this;
	if (!cfg)
		cfg = {};

	// 加载送样表格
	var givenGrid = new GivenGrid(cfg);

	var tbl = new Ext.TabPanel({
				height : 450,
				activeTab :0,
				items : [					 
					{
						title : '出货明细',
						name:'givenTab',
						layout : 'fit'
					}
				]
			});
	//激活面板时	
	tbl.on('tabchange',function(tb,pnl){		
		if(pnl.name=='givenTab'){
			if (!givenGrid.isVisible()) {
				pnl.add(givenGrid);
				pnl.doLayout();
				givenGrid.load();
			}
		}		
	});
	// 表单
	var con = {
		title : '导入货号',
		layout : 'fit',
		width : 700,
		height : 400,
		border : true,
		modal : true,
		constrainHeader:true,
		padding : "0",
		//closeAction : 'hide',
		items : [tbl]
	};

	Ext.apply(con, cfg);
	ImportPanel.superclass.constructor.call(this, con);
};
Ext.extend(ImportPanel, Ext.Window, {});
