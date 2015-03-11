Ext.namespace('Ext.mail');

$import('common/js/adv-vtypes.js');
$import('common/js/Form.js');
$import('common/js/SearchComboxField.js');

$import('mail/common/mailConstants.js');
$import('mail/common/SearchField.js');
$import('mail/common/mailStore.js');
$import('mail/common/mailDefault.js');
$import('mail/common/mailCard.js');
$import('mail/common/mailMenu.js');
$import('mail/common/mailRenderer.js'); 

$import('system/factory/cust/js/mailGrouping.js');

Ext.onReady(function(){
	var viewport = new Ext.Viewport({
		id:EXT_MAIL_ID_VIEWPORT,
		layout:'fit',
		items:[new Ext.mail.Card()]
	});
	viewport.card.grid.getStore().load({
		params:{
			start:EXT_MAIL_PAGE_START,
			limit:EXT_MAIL_PAGE_LIMIT
		}
	});
	new Ext.mail.Menu.person({
		id:EXT_MAIL_PERSON_MENU
	});
});
