
Ext.onReady(function(){
	var configForm = new Ext.mail.Config();
	var loadMask = new Ext.LoadMask(configForm.getEl(),{msg:'读取配置。。。'});
	loadMask.show();
	var isEmpAction = Ext.getDom('isEmpAction');
	var empId = Ext.getDom('empId');
	if(isEmpAction.value=='true')
		mailCfgService.getEmpCfg(empId.value,setCfg);
	else
		mailCfgService.getCfg(setCfg);
	function setCfg(cfg){
		loadMask.hide();
		if(cfg==null)
			return ;
		var form = configForm.getForm();
		form.findField('id').setValue(cfg.id);
		form.findField('defaultHost').setValue(cfg.defaultHost);
		form.findField('defaultHostSmtp').setValue(cfg.defaultHostSmtp);
		form.findField('defaultAccount').setValue(cfg.defaultAccount);
		form.findField('defaultPwd').setValue(cfg.defaultPwd);
		form.findField('defaultIsSslpop3').setValue(cfg.defaultIsSslpop3);
		form.findField('defaultIsSslsmtp').setValue(cfg.defaultIsSslsmtp);
		form.findField('defaultMintues').setValue(cfg.defaultMintues);
		if(cfg.defaultPop3port)
			form.findField('defaultPop3port').setValue(cfg.defaultPop3port);
		if(cfg.defaultSmtpPort)
			form.findField('defaultSmtpPort').setValue(cfg.defaultSmtpPort);
		if(isEmpAction.value != 'true'){
			form.findField('defaultAuth').setValue(cfg.defaultAuth);
		}else{
			configForm.setTitle('个人邮箱配置');
		}
	};
});