TongChooseWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var panel = new Ext.Panel({
				border : false,
				items : [{
							xtype : 'iframepanel',
							defaultSrc : './cotquery.do?method=queryTong&flag='+cfg.flag,
							width : "100%",
							height : 580,
							loadMask : {
								msg : 'Data Loading...'
							}
						}]
			});

	var con = {
		width : 800,
		height : 580,
		title : "Select the information you need to synchronize",
		modal : true,
		id : "tongChooseWin",
		//closeAction:'hide',
		items : [panel]
	}
	Ext.apply(con, cfg);
	TongChooseWin.superclass.constructor.call(this, con);
};

Ext.extend(TongChooseWin, Ext.Window, {});