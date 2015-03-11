Ext.namespace('Ext.mail');

Ext.mail.Config = Ext.extend(Ext.form.FormPanel ,{
xtype:"form",
	title:"公共邮箱配置",
	labelWidth:100,
	labelAlign:"right",
	layout:"form",
	width:670,
	renderTo:'form',
	height:300,
	padding:"10px",
	frame:true,
	initComponent: function(){
		var isEmpAction = Ext.getDom('isEmpAction');
		this.fbar=[{
			text:"测试连接",
			scope:this,
			handler:function(){
				var formPanel = this;
				var form = formPanel.getForm();
				if(!form.isValid())
					return ;
				var mailConfig = {
					popServerUrl:form.findField('defaultHost').getValue(),
					smtpServerUrl:form.findField('defaultHostSmtp').getValue(),
					emailAccount:form.findField('defaultAccount').getValue(),
					emailPassword:form.findField('defaultPwd').getValue(),
					smtpAuthentication:true,
					popSSL:form.findField('defaultIsSslpop3').getValue()?1:0,
					smtpSSL:form.findField('defaultIsSslsmtp').getValue()?1:0,
					popPort:form.findField('defaultPop3port').getValue(),
					smtpPort:form.findField('defaultSmtpPort').getValue()
				};
				var loadMask = new Ext.LoadMask(formPanel.getEl(),{msg:'测试连接中。。。'});
				loadMask.show();
				mailCfgService.connTest(mailConfig,function(result){
					loadMask.hide();
					Ext.mail.connectInfo(formPanel,result,'连接');
				});
			}
		},{
			text:"保存",
			scope:this,
			handler:function(){
				var formPanel = this;
				var form = formPanel.getForm();
				if(!form.isValid())
					return ;
				var mailConfig = {
					id:form.findField('id').getValue(),
					defaultHost:form.findField('defaultHost').getValue(),
					defaultHostSmtp:form.findField('defaultHostSmtp').getValue(),
					defaultAccount:form.findField('defaultAccount').getValue(),
					defaultPwd:form.findField('defaultPwd').getValue(),
					defaultMintues:form.findField('defaultMintues').getValue(),
					defaultAuth:form.findField('defaultAuth').getValue()?1:0,
					defaultIsSslpop3:form.findField('defaultIsSslpop3').getValue()?1:0,
					defaultIsSslsmtp:form.findField('defaultIsSslsmtp').getValue()?1:0,
					defaultPop3port:form.findField('defaultPop3port').getValue(),
					defaultSmtpPort:form.findField('defaultSmtpPort').getValue()
				};
				var cfgService = isEmpAction.value == 'true'
						? mailCfgService.updateEmpCfg : mailCfgService.updateCfg;
				var loadMask = new Ext.LoadMask(formPanel.getEl(),{msg:'保存中。。。'});
				loadMask.show();
				cfgService(mailConfig,function(result){
					loadMask.hide();
					Ext.mail.connectInfo(formPanel,result,'保存');
				});
			}
		}]
		this.items=[
			{
				xtype:"panel",
				title:"",
				titleCollapse:true,
				layout:"column",
				items:[
					{
//						xtype:"form",
						title:"",
						labelWidth:130,
						labelAlign:"left",
						layout:"form",
						columnWidth:0.5,
						items:[
							{
								xtype:'hidden',
								name:'id'
							},
							{
								xtype:"textfield",
								fieldLabel:"POP3服务器地址",
								allowBlank:false,
								maxLength : 100,
								anchor:"100%", 
								name:"defaultHost"
							},
							{
								xtype:"textfield",
								fieldLabel:"SMTP服务器地址",
								allowBlank:false,
								anchor:"100%",
								name:"defaultHostSmtp"
							},
							{
								xtype:"textfield",
								fieldLabel:"账号",
								allowBlank:false,
								anchor:"100%",
								regex:getEmailOrNullRegex(),
								regexText:'E-mail必须是电子邮件地址，格式如： "user@example.com"',
								ref:'fffdsfdfsdf',
								name:"defaultAccount"
							},
							{
								xtype:"textfield",
								fieldLabel:"密码",
								allowBlank:false,
								anchor:"100%",
								inputType:'password',
								name:"defaultPwd"
							},
							{
								xtype:"numberfield",
								fieldLabel:'自动接收间隔时间(分)',
								width:100,
								allowBlank:false,
								value:30,
								minValue:5,
								maxValue:99,
								name:"defaultMintues"
							},
							{
//								xtype:"panel",
								title:"",
								layout:"column",
//								hidden:isEmpAction.value=='true',
								hidden:true,
								titleCollapse:true,
								items:[
									{
										xtype:"label",
										text:"SMTP需要身份认证：",
										style:"font-size:12px"
									},
									{
										xtype:"radio",
										fieldLabel:"标签",
										boxLabel:"需要",
										checked:true,
										name:"defaultAuth"
									},
									{
										xtype:"radio",
										fieldLabel:"标签",
										boxLabel:"不需要",
										style:"",
										name:"defaultAuth"
									}
								]
							}
						]
					},
					{
//						xtype:"panel",
						title:"",
						titleCollapse:true,
						columnWidth:0.5,
						layout:"column",
						items:[
							{
//								xtype:"form",
								title:"",
								labelWidth:130,
								labelAlign:"left",
								layout:"form",
								width:160,
								items:[
									{
										xtype:"checkbox",
										fieldLabel:"要求安全连接(SSL)",
										boxLabel:"",
										anchor:"100%",
										name:"defaultIsSslpop3",
										listeners:{
											scope:this,
											check:function(checkbox,checked){
												var pop3port = this.getForm().findField('defaultPop3port');
												pop3port.setDisabled(!checked);
												if(!checked)
													pop3port.setValue(110);
											}
										}
									},
									{
										xtype:"checkbox",
										fieldLabel:"要求安全连接(SSL)",
										boxLabel:"",
										anchor:"100%",
										name:"defaultIsSslsmtp",
										listeners:{
											scope:this,
											check:function(checkbox,checked){
												var smtpPort = this.getForm().findField('defaultSmtpPort');												
												smtpPort.setDisabled(!checked);
												if(!checked)
													smtpPort.setValue(25);
											}
										}
									}
								]
							},
							{
//								xtype:"form",
								title:"",
								labelWidth:40,
								labelAlign:"left",
								layout:"form",
								items:[
									{
										xtype:"numberfield",
										fieldLabel:"端口",
										disabled:true,
										allowBlank:false,
										name:"defaultPop3port",
										value:'110',
										maxValue:'999',
										minValue:'0',
										width:60
									},
									{
										xtype:"numberfield",
										fieldLabel:"端口",
										disabled:true,
										allowBlank:false,
										name:"defaultSmtpPort",
										maxValue:'999',
										minValue:'0',
										value:'25',
										width:60
									}
								]
							}
						]
					}
				]
			}
		]
		Ext.mail.Config.superclass.initComponent.call(this);
	}
});
Ext.mail.connectInfo = function(formPanel,result,type){
	var msg = '';
	var fieldName = '';
	switch(result){
		case MAIL_CONNECT_NO_ERROR_STATUS:
			msg = type+'成功!';
			break;
		case MAIL_CONNECT_ERROR_STATUS:
			msg = type+'失败!';
			break;
		case MAIL_CONNECT_ERROR_POP_HOST_STATUS:
			msg = 'POP3服务器地址连接失败！';
			fieldName = 'defaultHost';
			break;
		case MAIL_CONNECT_ERROR_POP_PORT_STATUS:
			msg = 'POP3服务器端口连接被拒绝！';
			fieldName = 'defaultPop3port';
			break;
		case MAIL_CONNECT_ERROR_LOGIN_FAILED_FREQ_STATUS:
		case MAIL_CONNECT_ERROR_SMTP_HOST_STATUS:
			msg = 'SMTP服务器地址连接失败！';
			fieldName = 'defaultHostSmtp';
			break;
		case MAIL_CONNECT_ERROR_SMTP_PORT_STATUS:
			msg = 'SMTP服务器端口连接被拒绝！';
			fieldName = 'defaultSmtpPort';
			break;
		case MAIL_CONNECT_ERROR_LOGIN_FAILED_STATUS:
		case MAIL_CONNECT_ERROR_LOGON_STATUS:
			msg = '账号或密码错误！';
			fieldName = 'defaultAccount';
			break;
	}
	Ext.Msg.show({
		buttons:Ext.Msg.OK,
		icon:result==MAIL_CONNECT_NO_ERROR_STATUS?Ext.Msg.INFO:Ext.Msg.ERROR,
		title:type+(result==MAIL_CONNECT_NO_ERROR_STATUS?'成功':'失败'),
		msg:msg,
		fn:function(btn){
			var form = formPanel.getForm();
			if(fieldName)
				form.findField(fieldName).focus(true);
		}
	});
}