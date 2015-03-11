Ext.namespace('Ext.mail');

Ext.mail.HistoryCard = Ext.extend(Ext.Panel,{
	layout:'card',
	activeItem:0,
	defaults: {
        border:false
    },
	initComponent:function(){
		this.items = [{
	    	layout:'border',
	    	items:[{
	    		region:'center',
	    		xtype:'comeandgomail',
	    		title:'往来邮件',
	    		ref:'../grid'
	    	},{
	    		region:'south',
	    		xtype:'comeandgoattach',
	    		ref:'../attachGrid',
	    		title:'往来附件',
	    		collapseMode:'mini',
	    		split:true,
	    		height:200
	    	}]
	    },{
    		xtype:'maildefault',
    		ref:'mailDefault'
    	}];
		Ext.mail.HistoryCard.superclass.initComponent.call(this);
		this.mailDefault.on({
			'closeBtn':{
				fn:function(mailDefault){
					this.layout.setActiveItem(0);
				},
				scope:this
			},
			'pageBtn':{
				fn:function(mailDefault,hanType){
					navHandler(hanType,this.grid,mailDefault);
				},
				scope:this
			}
		});
	}
});
Ext.reg('historycard',Ext.mail.HistoryCard);
