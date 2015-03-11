Ext.namespace('Ext.mail');

$import('common/js/adv-vtypes.js');
$import('common/js/Form.js');
$import('common/js/SearchComboxField.js');

$import('mail/common/mailConstants.js');
$import('mail/common/mailFn.js');
$import('mail/common/SearchField.js');
$import('mail/common/mailStore.js');
$import('mail/common/mailDefault.js');
$import('mail/common/mailGrouping.js');
$import('mail/common/mailCard.js');
$import('mail/common/mailMenu.js');
$import('mail/common/mailRenderer.js'); 

Ext.onReady(function(){
	EXT_VIEWPORT = new Ext.Viewport({
		id:EXT_MAIL_ID_VIEWPORT,
		layout:'fit',
		items:[{
	    	margins:'5 5 5 5',
	    	layout:'card',
	    	ref:'mailCard',
	    	border:false,
	    	activeItem:0,
	    	items:[{
	    		region:'center',
	    		xtype:'checkgrid',
	    		border:false,
	    		mailType:'cust',
	    		ref:'../mailGrid'
	    	},{
	    		xtype:'maildefault',
				ref:'../dbMailDefault',
				mailType:'cust'
	    	}]	
		}]
	});
	EXT_VIEWPORT.dbMailDefault.on({
		'closeBtn':{
			fn:function(mailDefault){
				EXT_VIEWPORT.mailCard.layout.setActiveItem(0);
			}
		},
		'pageBtn':{
			fn:function(mailDefault,hanType){
				navHandler(hanType,EXT_VIEWPORT.mailGrid,mailDefault);
			}
		}
	});
	EXT_VIEWPORT.mailGrid.getStore().load({
		params:{
			start:EXT_MAIL_PAGE_START,
			limit:EXT_MAIL_PAGE_LIMIT
		}
	});
	new Ext.mail.Menu.person({
		id:EXT_MAIL_PERSON_MENU
	});
});
