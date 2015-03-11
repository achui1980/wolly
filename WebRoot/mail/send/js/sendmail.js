Ext.namespace('Ext.mail');
/**
 * 引入扩展上传组件
 * @type Number
 */
$import('common/js/swfupload_2.2.js');
$import('common/js/SwfUploadPanel.js');
$import('common/js/handlers.js');
$import('common/js/SearchComboxField.js');

$import('mail/common/mailConstants.js');
$import('mail/common/mailFn.js');

$import('mail/send/js/mailObject.js');
$import('mail/send/js/mailStore.js');
$import('mail/send/js/addressGrid.js');
$import('mail/send/js/addressTab.js');
$import('mail/send/js/mailForm.js');
$import('mail/send/js/mailOutbound.js');
$import('mail/send/js/importPic.js');
$import('mail/send/js/mailDelUpload.js');

window.onbeforeunload = function(e) {
	var formPanel = Ext.getCmp(EXT_MAIL_ID_OUTBOUND_FROMPANEL);
	var form = formPanel.getForm();
	var to = Ext.mail.sendPersonTo(form.findField('to')); 
	var cc = [],bcc = [];
	if(!formPanel.ccField.hidden)
		cc = Ext.mail.sendPersonTo(form.findField('cc')); 
	if(!formPanel.bccField.hidden)
		bcc = Ext.mail.sendPersonTo(form.findField('bcc')); 
	var subject = form.findField('subject').getValue();
	var body = fckGetValue(fckeditorBody);
	var isEmpty = false;
	if(to.length !=0||cc.length!=0||bcc.length!=0||subject||body|| Ext.getDom("mail_send_attach_panel").style.display!="none"){
		isEmpty = true;
	}
	if(formPanel.sending)
		isEmpty = false;
	
	var msg = "确定要离开发送页面吗?\n\n提示：未保存的内容将会丢失。";
	// 用户点击浏览器右上角关闭按钮
	if (Ext.isIE) {
		if (document.body.clientWidth - event.clientX < 170
				&& event.clientY < 0 || event.altKey) {
			if(isEmpty)
				return msg;
		} else if (event.clientY > document.body.clientHeight || event.altKey) { // 用户点击任务栏，右键关闭
			if(isEmpty)
				return msg;
		} else { // 其他情况为刷新
		}
	} else if (Ext.isChrome || Ext.isOpera) {
		if(isEmpty)
			return msg;
	} else if (Ext.isGecko) {
		
	}
}
Ext.onReady(function(){
    var viewport = new Ext.Viewport({
		layout : 'fit',
		items : [{
			xtype:"panel",
			titleCollapse:true,
			autoScroll:true,
			border:false,
			items:{
	    		xtype:'mailOuntbound',
	    		id:EXT_MAIL_ID_OUTBOUND_FROMPANEL
	    	}
		}]
	});
	
});

