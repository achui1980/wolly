Ext.onReady(function() {
			/*******************************************************************
			 * 描述: 本地下拉列表
			 ******************************************************************/
			var statusStore = new Ext.data.SimpleStore({
						fields : ["id", "name"],
						data : [[0, 'At work'], [1, 'Not working']]
					});

			var statusField = new Ext.form.ComboBox({
						name : 'empsStatus',
						fieldLabel : 'Status',
						editable : false,
						store : statusStore,
						value : 0,
						valueField : "id",
						displayField : "name",
						mode : 'local',
						validateOnBlur : true,
						triggerAction : 'all',
						anchor : "92%",
						tabIndex : 7,
						emptyText : 'Choose',
						hiddenName : 'empsStatus',
						selectOnFocus : true
					});

			/*******************************************************************
			 * 描述: 下拉框
			 ******************************************************************/
			var companyName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotCompany&flag=pic",
						valueField : "id",
						fieldLabel : "Company",
						autoShow : true,
						allowBlank : true,
						displayField : "companyShortName",
						cmpId : "companyId",
						emptyText : 'Choose',
						tabIndex : 4,
						anchor : "92%"
					});
			var deptName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotDept",
						valueField : "id",
						fieldLabel : "Dept",
						autoShow : true,
						allowBlank : true,
						sendMethod : "post",
						displayField : "deptName",
						cmpId : "deptId",
						tabIndex : 5,
						emptyText : 'Choose',
						anchor : "92%"
					});
			var etOtpCbx = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEtOtp&key=barCode",
				cmpId : 'etId',
				fieldLabel : "Random Pw's barcode",
				editable : true,
				disabled : true,
				disabledClass : 'combo-disabled',
				valueField : "id",
				displayField : "barCode",
				sendMethod : "post",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "97.5%",
				tabIndex : 34,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 250,// 下
				triggerAction : 'all'
			});
			var roleName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotNation",
						valueField : "id",
						fieldLabel : "Country",
						allowBlank :false,
						//hideLabel:true,
						//hidden:true,
						autoShow : true,
						allowBlank : true,
						displayField : "nationName",
						cmpId : "roleId",
						tabIndex : 6,
						emptyText : 'Choose',
						anchor : "95%"
					});

			// 下拉框级联操作
			companyName.on('select', function() {
						deptName.reset();
						deptName.loadValueById('CotDept', 'companyId',
								companyName.value);
					});

			var formPanel = new Ext.form.FormPanel({
						labelWidth : 70,
						labelAlign : "right",
						layout : "form",
						//autoWidth : true,
						//autoHeight : true,
						padding : "5px",
						//renderTo : "modpage",
						formId : "empsForm",
						id : "empsFormId",
						frame : true,
						buttonAlign : "center",
						// monitorValid : true,
						fbar : [{
							text : "Save",
							// formBind : true,
							handler : mod,
							iconCls : "page_table_save"
						}, {
							text : "Cancel",
							iconCls : "page_cancel",
							handler : function() {
								closeandreflashEC(true, 'empsGrid',
										false)
							}
						}],
						items : [{
							xtype : "panel",
							title : "",
							layout : "column",
							items : [{
								xtype : "panel",
								title : "",
								layout : "form",
								labelWidth : 70,
								columnWidth : 0.34,
								autoWidth : false,
								height : "",
								items : [{
									xtype : "textfield",
									fieldLabel : "<font color='red'>Login</font>",
									anchor : "92%",
									blankText : "Please enter the login name",
									id : "empsId",
									regex:new RegExp("^[A-Za-z0-9_-]+$"),
									regexText:"Only by numbers, 26 English letters or underscore, a string of horizontal",
									tabIndex : 1,
									maxLength : 20,
									allowBlank : false,
									invalidText : "Login name already exists, please try again！",
									validationEvent : 'change',
									validator : function(thisText) {
										var temp = false;
										if (thisText != '') {
											DWREngine.setAsync(false);
											cotEmpsService.findIsExistEmpsId(
													thisText, $('eId').value,
													function(res) {
														temp = res;
													});
											DWREngine.setAsync(true);
										}
										return temp;
									}
								}, companyName, statusField]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								labelWidth : 60,
								columnWidth : 0.33,
								width : 217,
								items : [{
									xtype : "textfield",
									fieldLabel : "<font color='red'>Username</font>",
									anchor : "92%",
									name : "empsName",
									tabIndex : 2,
									maxLength : 20,
									allowBlank : false,
									blankText : "Please enter your user name",
									invalidText : "The user name already exists, please re-enter！",
									validationEvent : 'change',
									validator : function(thisText) {
										var temp = false;
										if (thisText != '') {
											DWREngine.setAsync(false);
											cotEmpsService.findIsExistEmpsName(
													thisText, $('eId').value,
													function(res) {
														temp = res;
													});
											DWREngine.setAsync(true);
										}
										return temp;
									}
								}, deptName, {
									xtype : "textfield",
									fieldLabel : "Phone",
									anchor : "92%",
									name : "empsPhone",
									tabIndex : 8,
									maxLength : 100,
									allowBlank : true
								}, {
									xtype : "hidden",
									//inputType : 'password',
									fieldLabel : "Pwd(mail)",
									anchor : "92%",
									name : "empsMailPwd",
									tabIndex : 11,
									maxLength : 20,
									allowBlank : true
								}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								labelWidth : 60,
								columnWidth : 0.33,
								height : "",
								items : [ {
											xtype : "textfield",
											fieldLabel : "Mobile",
											anchor : "95%",
											name : "empsMobile",
											tabIndex : 9,
											maxLength : 100,
											allowBlank : true
										},{
											xtype : "textfield",
											fieldLabel : "E-mail",
											anchor : "95%",
											name : "empsAccount",
											regex:getEmailOrNullRegex(),
											regexText:'E-mail address must be the e-mail，Formats such as： "user@example.com"',
											tabIndex : 10,
											maxLength : 100,
											allowBlank : true
										} ,{
											xtype:'panel',
											layout : "form",
								            labelWidth : 150,
								            items:[{
												xtype:"checkbox",
												fieldLabel:'Approval',
												id:'shenFlag',
												name:'shenFlag'
								            }]
										}]
							},{
								xtype : "panel",
								title : "",
								hidden : true,
								layout : "form",
								labelWidth : 70,
								columnWidth : 0.34,
								height : "",
								items : [{
											xtype : "textfield",
											fieldLabel : "POP3",
											anchor : "92%",
											grow : false,
											name : "empsMailHost",
											tabIndex : 13,
											maxLength : 100,
											allowBlank : true
										}, {
											xtype : "textfield",
											fieldLabel : "SMTP",
											anchor : "92%",
											name : "empsSmtpHost",
											tabIndex : 16,
											maxLength : 100,
											allowBlank : true
										}]
							}, {
								xtype : "panel",
								title : "",
								hidden : true,
								layout : "form",
								labelWidth : 60,
								columnWidth : 0.33,
								height : "",
								items : [{
											xtype : "numberfield",
											fieldLabel : "Port",
											anchor : "92%",
											maxValue : 999,
											minValue:0,
											value:'110',
											disabled:true,
											disabledClass : 'combo-disabled',
											name : "empsPop3Port",
											tabIndex : 14,
											allowBlank : false
										}, {
											xtype : "numberfield",
											fieldLabel : "Port",
											anchor : "92%",
											maxValue : 999,
											minValue:0,
											value:'25',
											disabled:true,
											disabledClass : 'combo-disabled',
											name : "empsSmtpPort",
											tabIndex : 17,
											allowBlank : false
										}]
							}, {
								xtype : "panel",
								title : "",
								hidden : true,
								layout : "form",
								labelWidth : 115,
								columnWidth : 0.33,
								height : "",
								width : 212,
								items : [{
											xtype : "checkbox",
											fieldLabel : "SSL",
											boxLabel : "",
											anchor : "100%",
											height : 22,
											id : "empsIsSSLSmtp",
											name : "empsIsSSLSmtp",
											tabIndex : 18,
											listeners:{
												check:function(checkbox,checked){
													var pop3port = formPanel.getForm().findField('empsPop3Port');
													pop3port.setDisabled(!checked);
													if(!checked)
														pop3port.setValue(110);
												}
											}
										}, {
											xtype : "checkbox",
											fieldLabel : "SSL",
											boxLabel : "",
											anchor : "100%",
											height : 22,
											name : "empsIsSSLPop3",
											id : "empsIsSSLPop3",
											tabIndex : 15,
											checked : false,
											listeners:{
												check:function(checkbox,checked){
													var smtpPort = formPanel.getForm().findField('empsSmtpPort');												
													smtpPort.setDisabled(!checked);
													if(!checked)
														smtpPort.setValue(25);
												}
											}
										}]
							}, 
								
									{
								xtype:'panel',
								layout:'form',
								hidden : true,
								columnWidth:0.33,
								labelWidth:130,
								items:[{
									xtype:"numberfield",
									fieldLabel:'receive interval (m)',
									width:76,
									allowBlank:false,
									value:30,
									minValue:5,
									maxValue:99,
									name:"defaultMintues"
								},{
									xtype:'hidden',
									name:'empsSign'
								},{
									xtype:'hidden',
									name:'empsMailTemplate'
								},{
									xtype:'hidden',
									name:'empsMailTemplateTransmit'
								},{
									xtype:'hidden',
									name:'empsMailTemplateReply'
								}]
							}, {
								xtype:'panel',
								layout:'form',
								columnWidth:0.25,
								labelWidth:130,
								items:[{
											xtype : "button",
											width:80,
											hidden : true,
											tabIndex : 12,
											text:'Test Connection',
											handler:function(){
												var form = formPanel.getForm();
												var popServerUrl = form.findField('empsMailHost').getValue();
												var smtpServerUrl = form.findField('empsSmtpHost').getValue();
												var emailAccount = form.findField('empsAccount').getValue();
												var emailPassword = form.findField('empsMailPwd').getValue();
												var popSSL = form.findField('empsIsSSLPop3').getValue()?1:0;
												var smtpSSL = form.findField('empsIsSSLSmtp').getValue()?1:0;
												var popPort = form.findField('empsPop3Port').getValue();
												var smtpPort = form.findField('empsSmtpPort').getValue();
												if(!emailAccount){
													form.findField('empsAccount').focus();
													return;
												}
												if(!emailPassword){
													form.findField('empsMailPwd').focus();
													return;
												}
												if(!popServerUrl){
													form.findField('empsMailHost').focus();
													return;
												}
												if(!popPort){
													form.findField('empsPop3Port').focus();
													return;
												}
												if(!smtpServerUrl){
													form.findField('empsSmtpHost').focus();
													return;
												}
												if(!smtpPort){
													form.findField('empsSmtpPort').focus();
													return;
												}
												
												var mailConfig = {
													popServerUrl:popServerUrl,
													smtpServerUrl:smtpServerUrl,
													emailAccount:emailAccount,
													emailPassword:emailPassword,
													smtpAuthentication:true,
													popSSL:popSSL,
													smtpSSL:smtpSSL,
													popPort:popPort,
													smtpPort:smtpPort
												};
												var loadMask = new Ext.LoadMask(formPanel.getEl(),{msg:'Test connection。。。'});
												loadMask.show();
												mailCfgService.connTest(mailConfig,function(result){
													loadMask.hide();
													mailConnectInfoFn(formPanel,result,'Connection');
												});
											}
										}]
							},{
								xtype:'panel',
								layout:'form',
								columnWidth:0.41,
								labelWidth:130,
								items:[]
							},{
								xtype : "panel",
								title : "",
								layout : "form",
								labelWidth : 70,
								columnWidth : 1,
								items : [roleName]
							},{
								xtype : "panel",
								columnWidth : 0.3,
								labelWidth : 70,
								layout:'form',
								items:[{
									xtype : "textfield",
									fieldLabel : "name",
									anchor : "100%",
									name : "empNameCn",
									tabIndex : 8,
									maxLength : 100,
									allowBlank : true
								}]
							},{
								xtype : "panel",
								columnWidth : 0.2,
								labelWidth : 100,
								//labelAlign:'right',
								layout:'form',
								items:[{
									xtype:"checkbox",
									fieldLabel:'Random password',
									id:'needOtp',
									name:'needOtp',
									listeners:{
										'check':function(chk,checked){
											if(checked){
												etOtpCbx.setDisabled(false);
											}else{
												etOtpCbx.setDisabled(true);
											}
										}
									}
								}]
							}, {
								xtype : "panel",
								columnWidth : 0.25,
								layout:'form',
								labelAlign:'left',
								labelWidth : 130,
								items:[etOtpCbx]
							},{
								xtype : "panel",
								columnWidth : 0.25,
								layout:'form',
								labelAlign:'left',
								labelWidth : 130,
								items:[{
									xtype : "textfield",
									fieldLabel : "Password Approval",
									inputType:'password',
									anchor : "98%",
									name : "passwordApproval",
									tabIndex : 8,
									maxLength : 20,
									allowBlank : true
								
								}]
							},{
								xtype : "panel",
								title : "",
								layout : "form",
								labelWidth : 70,
								columnWidth : 1,
								items : [{
											xtype : "textarea",
											fieldLabel : "Remarks",
											anchor : "98%",
											name : "empsRemark",
											tabIndex : 19,
											maxLength : 200,
											allowBlank : true
										}]
							},{
								xtype : "panel",
								title : "Country Right",
								layout : "form",
								labelWidth : 70,
								columnWidth : 1,
								items : [{
											xtype : "checkboxmodule",
											anchor : "98%",
											id:"nationRight",
											tbName:'CotNation',
											displayField:"nationName",
											isDWRFun:true,
											requestParam:{
												tbName:'CotNation'
											},
											valueField:"id",
											tabIndex : 19,
											columns:5,
											dataSource:cotEmpsService.getList,
											allowBlank : false
										},{
											xtype:'button',
											text:'All/Inverse All',
											handler:function(){
												var module = Ext.getCmp('nationRight');
												module.checkedAll();
											}
										}]
							},{
								xtype : "panel",
								title : "Dept Right",
								layout : "form",
								labelWidth : 70,
								columnWidth : 1,
								items : [{
											xtype : "checkboxmodule",
											anchor : "98%",
											tbName:'CotTypeLv1',
											id:"deptRight",
											displayField:"typeEnName",
											isDWRFun:true,
											requestParam:{
												tbName:'CotTypeLv1'
											},
											valueField:"id",
											tabIndex : 19,
											columns:5,
											dataSource:cotEmpsService.getList,
											allowBlank : false
										},{
											xtype:'button',
											text:'All/Inverse All',
											handler:function(){
												var module = Ext.getCmp('deptRight');
												module.checkedAll();
											}
										}]
							}]
						}, {
							xtype : 'hidden',
							name : 'id'
						}]
					});
					
			var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [formPanel]
			});
					
			var emp = null;
			// 初始化页面
			function initForm() {
				// 加载主表单
				var id = $("eId").value;
				if (id != 'null' && id != '') {
					cotEmpsService.getEmpsById(parseInt(id), function(res) {
								if (res != null) {
									var obj = res;
									if (obj.empsPop3Port == null)
										obj.empsPop3Port = 110;
									if (obj.empsSmtpPort == null)
										obj.empsSmtpPort = 25;
									// 给全局变量emp赋值
									emp = res;
									// 加载该公司的部门
									if (obj.companyId != null) {
										deptName.loadValueById('CotDept',
												'companyId', obj.companyId);
									}
									DWRUtil.setValues(obj);
									if(obj.etId!=null){
										Ext.getCmp('needOtp').setValue(true);
										etOtpCbx.bindPageValue("CotEtOtp", "id",obj.etId);
									}
									
									if (res.shenFlag = 1)
										Ext.getCmp("shenFlag").checked=true;
									else
										Ext.getCmp("shenFlag").checked=false;

									companyName.bindValue(obj.companyId);
									deptName.bindValue(obj.deptId);
									roleName.bindValue(obj.roleId);
									statusField.setValue(obj.empsStatus);
									var right = Ext.decode(emp.empRight);
									if(right != null){
										Ext.getCmp("nationRight").setCheckedValue(right["nation"]);
										Ext.getCmp("deptRight").setCheckedValue(right["dept"]);
									}
									if(obj.empsMintues)
										formPanel.getForm().findField('defaultMintues').setValue(obj.empsMintues);
									if (obj.empsIsSSLPop3 == 1) {
										Ext.get("empsIsSSLPop3").checked = true;
										Ext.get("empsPop3Port").disabled = false;
									}
									if (obj.empsIsSSLSmtp == 1) {
										Ext.get("empsIsSSLSmtp").checked = true;
										Ext.get("empsSmtpPort").disabled = false;
									}
								}
							});
				} else {
					// $('tabPane2').style.display='none';
				}
				$('empsId').focus();
			}
			// 初始化页面
			initForm();
			modifyEmpsFn = function (){
				// 表单信息
				var obj = DWRUtil.getValues("empsForm");
				var cotEmps = new CotEmps();
				for (var p in cotEmps) {
					cotEmps[p] = obj[p];
				}
				var id = $("eId").value;
				if (id != 'null' && id != '') {
					cotEmps.id = id;
				}
				if (Ext.getCmp("empsIsSSLSmtp").checked)
					cotEmps.empsIsSSLSmtp = 1;
				else
					cotEmps.empsIsSSLSmtp = 0;
				if (Ext.getCmp("empsIsSSLPop3").checked)
					cotEmps.empsIsSSLPop3 = 1;
				else
					cotEmps.empsIsSSLPop3 = 0;
				// 从全局变量取得密码
				if (id != 'null' && id != '') {
					cotEmps.empsPwd = emp.empsPwd;
				} else {
					cotEmps.empsPwd = "123456";
				}
				//判断是否需要动态密码
				var needOtp = Ext.getCmp('needOtp').checked;
				var etId = etOtpCbx.getValue();
				if(needOtp && etId!=''){
					cotEmps.etId=etId;
				}else{
					cotEmps.etId=null;
				}
				var form = formPanel.getForm();
				var emailAccount = form.findField('empsAccount').getValue();
				if(emailAccount&&emailAccount.indexOf('@')==-1){
					var popServerUrl = form.findField('empsMailHost').getValue();
					var lastMailUrl = popServerUrl.substring(popServerUrl.indexOf(".")+1);
					cotEmps.empsMail = emailAccount+"@"+lastMailUrl;
				}else{
					cotEmps.empsMail = emailAccount;
				}
				cotEmps.empsMintues = form.findField('defaultMintues').getValue();
				
				//是否审核人
				if (Ext.getCmp("shenFlag").checked)
					cotEmps.shenFlag = 1;
				else
					cotEmps.shenFlag = 0;
				cotEmps.empRight = getRight();
				var list = new Array();
				list.push(cotEmps);
				
				var popServerUrl = form.findField('empsMailHost').getValue();
				var smtpServerUrl = form.findField('empsSmtpHost').getValue();
				var emailAccount = form.findField('empsAccount').getValue();
				var emailPassword = form.findField('empsMailPwd').getValue();
				var popSSL = form.findField('empsIsSSLPop3').getValue()?1:0;
				var smtpSSL = form.findField('empsIsSSLSmtp').getValue()?1:0;
				var popPort = form.findField('empsPop3Port').getValue();
				var smtpPort = form.findField('empsSmtpPort').getValue();
				if(emailPassword||popServerUrl||smtpServerUrl){
					if(!emailAccount){
						form.findField('empsAccount').focus();
						return;
					}
					if(!emailPassword){
						form.findField('empsMailPwd').focus();
						return;
					}
					if(!popServerUrl){
						form.findField('empsMailHost').focus();
						return;
					}
					if(!popPort){
						form.findField('empsPop3Port').focus();
						return;
					}
					if(!smtpServerUrl){
						form.findField('empsSmtpHost').focus();
						return;
					}
					if(!smtpPort){
						form.findField('empsSmtpPort').focus();
						return;
					}
					var mailConfig = {
						popServerUrl:popServerUrl,
						smtpServerUrl:smtpServerUrl,
						emailAccount:emailAccount,
						emailPassword:emailPassword,
						smtpAuthentication:true,
						popSSL:popSSL,
						smtpSSL:smtpSSL,
						popPort:popPort,
						smtpPort:smtpPort
					};
					var loadMask = new Ext.LoadMask(formPanel.getEl(),{msg:'Saving。。。'});
					loadMask.show();
					mailCfgService.connTest(mailConfig,function(result){
						loadMask.hide();
						if(mailConnectInfoFn(formPanel,result,'Save')){
							saveEmps();
						}
					});
					return ;
				}
				saveEmps();
				function saveEmps(){
					if (id != 'null' && id != '') {
						cotEmpsService.modifyEmps(list, function(res) {
									if (res==0) {
										Ext.Msg.alert('Tip Box', "Successfully modified！");
										closeandreflashEC(true, 'empsGrid', false);
									} else if (res==2) {
										Ext.Msg.alert('Tip Box', "Change failed! The Password has been used by other employees");
									}else {
										Ext.Msg.alert('Tip Box', "Change failed! The login name already exists!");
										$('empsId').select();
									}
								})
					} else {
						cotEmpsService.addEmps(list, function(res) {
									if (res==0) {
										Ext.Msg.alert('Tip Box', "Successfully added！");
										closeandreflashEC(true, 'empsGrid', false);
									} else if (res==2) {
										Ext.Msg.alert('Tip Box', "Add failed! The Password has been used by other employees!");
									} else{
										Ext.Msg.alert('Tip Box', "Add failed! Employee Number already exists!");
										$('empsId').select();
									}
								})
					}
				}
				
				
			}
			mailConnectInfoFn = function(formPanel,result,type){
				var msg = '';
				var fieldName = '';
				switch(result){
					case MAIL_CONNECT_NO_ERROR_STATUS:
						msg = type+'Success!';
						if(type=='Save')
							return true;
						break;
					case MAIL_CONNECT_ERROR_STATUS:
						msg = type+'Failure!';
						break;
					case MAIL_CONNECT_ERROR_POP_HOST_STATUS:
						msg = 'POP3 server address connection fails！';
						fieldName = 'empsMailHost';
						break;
					case MAIL_CONNECT_ERROR_POP_PORT_STATUS:
						msg = 'POP3 server port connection was refused!';
						fieldName = 'empsPop3Port';
						break;
					case MAIL_CONNECT_ERROR_LOGIN_FAILED_FREQ_STATUS:
					case MAIL_CONNECT_ERROR_SMTP_HOST_STATUS:
						msg = 'SMTP server address connection failed!';
						fieldName = 'empsSmtpHost';
						break;
					case MAIL_CONNECT_ERROR_SMTP_PORT_STATUS:
						msg = 'SMTP server port connection was refused!';
						fieldName = 'empsSmtpPort';
						break;
					case MAIL_CONNECT_ERROR_LOGIN_FAILED_STATUS:
					case MAIL_CONNECT_ERROR_LOGON_STATUS:
						msg = 'Account or password is wrong!';
						fieldName = 'empsAccount';
						break;
				}
				Ext.Msg.show({
					buttons:Ext.Msg.OK,
					icon:result==MAIL_CONNECT_NO_ERROR_STATUS?Ext.Msg.INFO:Ext.Msg.ERROR,
					title:type+(result==MAIL_CONNECT_NO_ERROR_STATUS?'Success':'Failure'),
					msg:msg,
					fn:function(btn){
						var form = formPanel.getForm();
						if(fieldName)
							form.findField(fieldName).focus(true);
					}
				});
			}
			function mod() {
				var popedom = checkAddMod($('eId').value);
				if (popedom == 1) {
					Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority! ');
					return;
				} else if (popedom == 2) {
					Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority! ');
					return;
				}
				// 验证表单
				var formData = getFormValues(formPanel, true);
				// 表单验证失败时,返回
				if (!formData) {
					return;
				}
				modifyEmpsFn();
		}
		function getRight(){
			var nationRight = Ext.getCmp("nationRight");
			var json = {};
			json["nation"] = nationRight.getCheckedValue();
			var deptRight = Ext.getCmp("deptRight");
			json["dept"] = deptRight.getCheckedValue();
			return Ext.encode(json);
			
		}
});
