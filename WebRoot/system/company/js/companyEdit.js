MAIL_CONNECT_NO_ERROR_STATUS = 0;	// 连接正确
MAIL_CONNECT_ERROR_STATUS = 1;	// 连接错误，但是未发现的错误信息
MAIL_CONNECT_ERROR_POP_HOST_STATUS = 2;
MAIL_CONNECT_ERROR_POP_PORT_STATUS = 3;
MAIL_CONNECT_ERROR_SMTP_HOST_STATUS = 4;
MAIL_CONNECT_ERROR_SMTP_PORT_STATUS = 5;
MAIL_CONNECT_ERROR_LOGIN_STATUS = 6;
MAIL_CONNECT_ERROR_LOGIN_FAILED_STATUS = 7;
MAIL_CONNECT_ERROR_LOGIN_FAILED_FREQ_STATUS = 8;

MAIL_ERROR_INFO_MAP = {
	'-4':'Fail to connect to SMPT server',
	'-3':'Sending',
	'-2':'Waiting for apporval',
	'-1':'Fail to send e-mail',
	0:'Success',
	1:'Connection error',
	2:'Fail to connect to POP3 server',
	3:'POP3 server port connection is rejected',
	4:'Fail to connect to SMPT Server',
	5:'SMTP server port connection is rejected',
	6:'Error account or password',
	7:'Error account or password',
	8:'Fail to connect to SMPT server'
};
Ext.onReady(function() {

	/***************************************************************************
	 * 描述: 本地下拉列表
	 **************************************************************************/
	var defaultStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[1, 'Yes'], [0, 'No']]
			});

	var defaultField = new Ext.form.ComboBox({
				name : 'companyIsdefault',
				fieldLabel : 'Default',
				editable : false,
				store : defaultStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				value : 0,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "95%",
				tabIndex : 3,
				emptyText : 'Choose',
				hiddenName : 'companyIsdefault',
				selectOnFocus : true
			});

	// 判断单号是否重复参数
	var isExist = true;
	var form = new Ext.form.FormPanel({
		labelWidth : 90,
		labelAlign : "right",
		layout : "form",
		// autoWidth : true,
		// autoHeight : true,
		padding : "5px",
		// renderTo : "modpage",
		formId : "companyForm",
		id : "companyFormId",
		frame : true,
		buttonAlign : "center",
		monitorValid : true,
		fbar : [{
					text : "Save",
					formBind : true,
					handler : mod,
					iconCls : "page_table_save"
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					handler : function() {
						closeandreflashEC(true, 'companyGrid', false)
					}
				}],
		items : [{
			xtype : "panel",
			layout : "column",
			padding : "5",
			border : false,
			items : [{
				xtype : "panel",
				columnWidth : 0.75,
				layout : "column",
				items : [{
							xtype : "panel",
							columnWidth : 0.33,
							layout : "form",
							hideCollapseTool : true,
							items : [{
								xtype : "textfield",
								fieldLabel : "Short Title",
								anchor : "95%",
								allowBlank : false,
								blankText : "Please enter your company short",
								name : "companyShortName",
								id : "companyShortName",
								tabIndex : 1,
								maxLength : 100,
								listeners : {
									'render' : function(txt) {
										txt.focus();
									},
									'change' : function(thisText) {
										if (thisText != '') {
											val();
										}
									}
								}
									// invalidText : "公司简称已存在,请重新输入！",
									// validationEvent : 'change',
									// validator : function(thisText) {
									// if (thisText != '') {
									// val(thisText);
									// }
									// return isExist;
									// }
								}, {
								xtype : "textfield",
								fieldLabel : "Tel.",
								anchor : "95%",
								allowBlank : true,
								name : "companyNbr",
								tabIndex : 4,
								maxLength : 100
							}]
						}, {
							xtype : "panel",
							columnWidth : 0.33,
							layout : "form",
							hideCollapseTool : true,
							items : [{
										xtype : "textfield",
										fieldLabel : "Corporate",
										anchor : "95%",
										name : "companyCorporation",
										tabIndex : 2,
										allowBlank : true,
										hidden : true,
										hideLabel : true,
										maxLength : 100
									}, {
										xtype : "textfield",
										fieldLabel : "No.",
										anchor : "95%",
										allowBlank : true,
										name : "companyNo",
										tabIndex : 2,
										maxLength : 100
									}, {
										xtype : "textfield",
										fieldLabel : "Fax",
										anchor : "95%",
										allowBlank : true,
										name : "companyFax",
										tabIndex : 5,
										maxLength : 100
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.34,
							layout : "form",
							items : [defaultField, {
										xtype : "textfield",
										fieldLabel : "Zip code",
										anchor : "95%",
										allowBlank : true,
										name : "companyPost",
										tabIndex : 6,
										maxLength : 100
									}]
						}, {
							xtype : "panel",
							columnWidth : 1,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "Company",
										anchor : "98%",
										blankText : "Please enter the company name！",
										name : "companyEnName",
										tabIndex : 6,
										maxLength : 100
									}, {
										xtype : "textfield",
										fieldLabel : "Company_En",
										anchor : "98%",
										hidden : true,
										hideLabel : true,
										allowBlank : true,
										name : "companyName",
										tabIndex : 8,
										maxLength : 100
									}, {
										xtype : "textarea",
										fieldLabel : "Address",
										anchor : "98%",
										height:70,
										allowBlank : true,
										name : "companyEnAddr",
										tabIndex : 9,
										maxLength : 200
									}, {
										xtype : "textfield",
										fieldLabel : "Address_En",
										name : "companyAddr",
										hidden : true,
										hideLabel : true,
										anchor : "98%",
										tabIndex : 10,
										allowBlank : true,
										maxLength : 200
									}]
						}, {
							xtype : "panel",
							columnWidth : 1,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "Website",
										anchor : "98%",
										allowBlank : true,
										name : "companyWebsite",
										tabIndex : 11,
										maxLength : 100
									}]
						}, {
							xtype : "panel",
							columnWidth : .4,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "E-mail",
										anchor : "98%",
										tabIndex : 12,
										maxLength : 100,
										name : "companyEmail"
									}]
						}, {
							xtype : "panel",
							columnWidth : .3,
							layout : "form",
							labelWidth : 50,
							items : [{
										xtype : "textfield",
										fieldLabel : "account",
										anchor : "98%",
										tabIndex : 12,
										maxLength : 100,
										name : "account"
									}]
						}, {
							xtype : "panel",
							columnWidth : .3,
							layout : "form",
							labelWidth : 50,
							items : [{
										xtype : "textfield",
										inputType : 'password',
										fieldLabel : "mailPwd",
										anchor : "95%",
										tabIndex : 12,
										maxLength : 100,
										name : "mailPwd"
									}]
						}, {
							xtype : "panel",
							columnWidth : .4,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "smtpHost",
										anchor : "98%",
										tabIndex : 12,
										maxLength : 100,
										name : "smtpHost"
									}]
						}, {
							xtype : "panel",
							columnWidth : .15,
							layout : "form",
							labelWidth : 50,
							items : [{
										xtype : "numberfield",
										fieldLabel : "smtpPort",
										anchor : "98%",
										maxValue : 999,
										minValue : 0,
										value : '25',
										disabled : true,
										disabledClass : 'combo-disabled',
										tabIndex : 12,
										maxLength : 100,
										name : "smtpPort"
									}]
						}, {
							xtype : "panel",
							columnWidth : .3,
							layout : "form",
							labelWidth : 120,
							items : [{
								xtype : "checkbox",
								fieldLabel : "Is required SSL",
								boxLabel : "",
								anchor : "95%",
								height : 22,
								id : "smtpIss",
								name : "smtpIss",
								tabIndex : 18,
								listeners : {
									check : function(checkbox, checked) {
										var smtpPort = form.getForm()
												.findField('smtpPort');
										smtpPort.setDisabled(!checked);
										if (!checked)
											smtpPort.setValue(25);
									}
								}
							}]
						},{
							xtype : "panel",
							columnWidth : .15,
							layout : "form",
							labelWidth : 120,
							items : [{
								xtype : "button",
								text:'Test E-Mail',
								handler : function() {
												var fm = form.getForm();
												var smtpServerUrl = fm
														.findField('smtpHost')
														.getValue();
												var emailAccount = fm
														.findField('account')
														.getValue();
												var emailPassword = fm
														.findField('mailPwd')
														.getValue();
												var smtpSSL = fm
														.findField('smtpIss')
														.getValue() ? 1 : 0;
												var smtpPort = fm
														.findField('smtpPort')
														.getValue();
												if (!emailAccount) {
													fm
															.findField('account')
															.focus();
													return;
												}
												if (!emailPassword) {
													fm
															.findField('mailPwd')
															.focus();
													return;
												}
												if (!smtpServerUrl) {
													fm
															.findField('smtpHost')
															.focus();
													return;
												}
												if (!smtpPort) {
													fm
															.findField('smtpPort')
															.focus();
													return;
												}

												var mailConfig = {
//													popServerUrl : popServerUrl,
													smtpServerUrl : smtpServerUrl,
													emailAccount : emailAccount,
													emailPassword : emailPassword,
													smtpAuthentication : true,
//													popSSL : popSSL,
													smtpSSL : smtpSSL,
//													popPort : popPort,
													smtpPort : smtpPort
												};
												var loadMask = new Ext.LoadMask(
														form.getEl(), {
															msg : 'connecting...'
														});
												loadMask.show();
												mailCfgService.connTest(
														mailConfig, function(
																result) {
															loadMask.hide();
															mailConnectInfoFn(
																	form,
																	result,
																	'Connect');
														});
											}
							}]
						}]
			}, {
				xtype : "fieldset",
				bodyStyle : 'padding-left:25px',
				title : "Company LOGO",
				layout : "form",
				labelWidth : 60,
				height : 200,
				columnWidth : 0.25,
				items : [{
					xtype : "panel",
					html : '<div align="center" style="margin-bottom:-5px; margin-top: 2px; width: 135px; height: 132px;">'
							+ '<img src="common/images/zwtp.png" id="picPath" name="picPath"'
							+ 'onload="javascript:DrawImage(this,135,132)" onclick="showBigPicDiv(this)"/></div>',
					fbar : [{
								width : 65,
								text : "Update",
								id : "upmod",
								iconCls : "upload-icon",
								handler : showUploadPanel
							}, {
								width : 65,
								text : "Delete",
								id : "updel",
								iconCls : "upload-icon-del",
								handler : delPic
							}]
				}]
			}, {
				xtype : "panel",
				columnWidth : 1,
				layout : "form",
				items : [{
							xtype : "starHtmleditor",
							fieldLabel : "Remarks",
							enableSourceEdit : false,
							anchor : "100%",
							id : "companyRemark",
							name : "companyRemark",
							height : 150,
							tabIndex : 18,
							allowBlank : true

						}]
			}]
		}]
	});

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
	viewport.doLayout();

	// 初始化页面
	function initForm() {
		var isMod = getPopedomByOpType(vaildUrl, "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").setVisible(false);
			Ext.getCmp("updel").setVisible(false);
		}
		// 加载主样品表单
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotCompanyService.getCompanyById(parseInt(id), function(res) {
				if (res != null) {
					DWRUtil.setValues(res);
					var rdm = Math.round(Math.random() * 10000);
					$('picPath').src = './showPicture.action?flag=companyLogo&detailId='
							+ id + '&tmp=' + rdm;
					defaultField.setValue(res.companyIsdefault);
				}
			});
		} else {
			// $('tabPane2').style.display='none';
		}
		$('companyShortName').focus();
	}
	// 初始化页面
	initForm();
	
	function mailConnectInfoFn(formPanel, result, type) {
		var msg = '';
		var fieldName = '';
		switch (result) {
			case MAIL_CONNECT_NO_ERROR_STATUS :
				msg = type + 'success!';
				if (type == 'Connect')
					return true;
				break;
			case MAIL_CONNECT_ERROR_STATUS :
				msg = type + 'fail!';
				break;
			case MAIL_CONNECT_ERROR_POP_HOST_STATUS :
				msg = 'Fail to connect to POP3 server!';
				fieldName = 'empsMailHost';
				break;
			case MAIL_CONNECT_ERROR_POP_PORT_STATUS :
				msg = 'POP3 server port connection is rejected!';
				fieldName = 'empsPop3Port';
				break;
			case MAIL_CONNECT_ERROR_LOGIN_FAILED_FREQ_STATUS :
			case MAIL_CONNECT_ERROR_SMTP_HOST_STATUS :
				msg = 'Fail to connect to SMTP server';
				fieldName = 'empsSmtpHost';
				break;
			case MAIL_CONNECT_ERROR_SMTP_PORT_STATUS :
				msg = 'SMTP server port connection is rejected!';
				fieldName = 'empsSmtpPort';
				break;
			case MAIL_CONNECT_ERROR_LOGIN_FAILED_STATUS :
			case MAIL_CONNECT_ERROR_LOGON_STATUS :
				msg = 'Error account or password!';
				fieldName = 'empsAccount';
				break;
		}
		Ext.Msg.show({
					buttons : Ext.Msg.OK,
					icon : result == MAIL_CONNECT_NO_ERROR_STATUS
							? Ext.Msg.INFO
							: Ext.Msg.ERROR,
					title : type
							+ (result == MAIL_CONNECT_NO_ERROR_STATUS
									? 'Success'
									: 'Fail'),
					msg : msg,
					fn : function(btn) {
						var fm = form.getForm();
						if (fieldName)
							fm.findField(fieldName).focus(true);
					}
				});
	}
});

// 验证
function val() {
	var shortflag = false;
	var shortName = $('companyShortName').value;
	DWREngine.setAsync(false);
	cotCompanyService.findIsExistShortName(shortName, $('eId').value, function(
					res) {
				if (res != null) {
					shortflag = true;
				}
			});
	if (shortflag) {
		Ext.MessageBox.alert("Message",
				"The company referred to already exists, please re-enter！");
		return;
	}
	DWREngine.setAsync(true);
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

	var shortflag = false;
	var shortName = $('companyShortName').value;
	DWREngine.setAsync(false);
	cotCompanyService.findIsExistShortName(shortName, $('eId').value, function(
					res) {
				if (res != null) {
					shortflag = true;
				}
			});
	DWREngine.setAsync(true);
	if (shortflag) {
		Ext.MessageBox.alert("Message",
				"The company referred to already exists, please re-enter!");
		return;
	}

	// 表单信息
	var obj = DWRUtil.getValues("companyForm");
	var cotCompany = new CotCompany();
	for (var p in obj) {
		cotCompany[p] = obj[p];
	}
	var fPath = "";
	var id = $("eId").value;
	if (id != 'null' && id != '') {
		cotCompany.id = id;
	} else {
		var basePath = $('basePath').value;
		var filePath = $('picPath').src;
		var s = filePath.indexOf(basePath);
		fPath = filePath.substring(s + basePath.length, 111111);
	}

	DWREngine.setAsync(false);
	cotCompanyService.findIsExistdefault(function(res) {
				if (res != 0 && res != id && cotCompany.companyIsdefault == 1) {
					shortflag = true;
				}
			});
	DWREngine.setAsync(true);
	if (shortflag) {
		Ext.MessageBox
				.alert('Message',
						"Already exists in the default company, the company can not add a default！")
		return;
	}
	cotCompany.companyRemark = Ext.getCmp('companyRemark').getValue();
	if (Ext.getCmp("smtpIss").checked)
			cotCompany.smtpIss = 1;
		else
			cotCompany.smtpIss = 0;
	DWREngine.setAsync(false);
	cotCompanyService.saveOrUpdateCompany(cotCompany, fPath, function(res) {
				if (res) {
					reflashParent('companyGrid');
					Ext.MessageBox.alert('Message', "Successfully saved！");
				} else {
					Ext.MessageBox.alert('Message', "Save failed");
					$('companyShortName').select();
				}
			});
	DWREngine.setAsync(true);
}

// 打开上传面板
function showUploadPanel() {
	var id = $('eId').value;
	var opAction = "insert";
	if (id == 'null' || id == '')
		opAction = "insert";
	else
		opAction = "modify";
	var win = new UploadWin({
				params : {
					companyId : id
				},
				waitMsg : "Photo upload......",
				opAction : opAction,
				imgObj : $('picPath'),
				imgUrl : './showPicture.action?flag=companyLogo&detailId=' + id
						+ '&tmp=' + Math.random(),
				uploadType : "image",
				loadImgStream : true,
				uploadUrl : './uploadCompanyLogo.action',
				validType : "jpg|png|bmp|gif"
			})
	win.show();
}
// 删除图片
function delPic() {
	var eId = $('eId').value;
	Ext.MessageBox.confirm('Message', 'Are you sure to delete this picture?',
			function(btn) {
				if (btn == 'yes') {
					if (eId == null || eId == '' || eId == 'null') {
						$('picPath').src = "common/images/zwtp.png";
					} else {
						cotCompanyService.deletePicImg(parseInt(eId), function(
								res) {
							if (res) {
								$('picPath').src = "common/images/zwtp.png";
							} else {
								Ext.MessageBox
										.alert('Message',
												"Picture does not exist, delete the image failed!");
							}
						})
					}
				}
			})
}
