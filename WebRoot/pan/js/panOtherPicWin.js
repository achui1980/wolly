PanOtherPicWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var panel = new Ext.Panel({
				border : false,
				items : [{
							xtype : 'iframepanel',
							defaultSrc : './cotpan.do?method=queryOtherPic&flag='+cfg.id+'&type='+cfg.type,
							width : 800,
							height : 580,
							loadMask : {
								msg : 'Loading...'
							}
						}]
			});

	var con = {
		width : 800,
		height : 580,
		title : "Additional picture",
		modal : true,
		id : "panOtherPicWin",
		//closeAction:'hide',
		items : [panel]
	}
	Ext.apply(con, cfg);
	PanOtherPicWin.superclass.constructor.call(this, con);
};

Ext.extend(PanOtherPicWin, Ext.Window, {});