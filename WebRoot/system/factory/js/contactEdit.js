Ext.onReady(function() {

	/*******************************************************************
	 * 描述: 下拉框
	 ******************************************************************/
	// 厂家
	var combox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : "factoryId",
				fieldLabel : "Suppliers",
				editable : true,
				disabled : true,
				disabledClass : 'combo-disabled',
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载,会为get提交
				pageSize : 5,
				anchor : "90%",
				sendMethod : "post",
				tabIndex : 2,
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				allowBlank : false,
				blankText : 'Suppliers can not be empty！',
				triggerAction : 'all'

			});
			
	// 审核状态
	var defaultStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[1, 'YES'], [2, 'NO']]
			});
	var defBox = new Ext.form.ComboBox({
				name : 'mainFlag',
				tabIndex : 8,
				fieldLabel : 'Default',
				editable : false,
				value : 2,
				store : defaultStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
//				hidden:true,
//				hideLabel:true,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "90%",
				hiddenName : 'mainFlag',
				selectOnFocus : true
			});

	var form = new Ext.form.FormPanel({
				labelWidth : 60,
				labelAlign : "right",
				layout : "column",
				padding : "5px",
				formId : "contactForm",
				id : "contactFormId",
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
								closeandreflashEC(true, 'contactGrid', false)
							}
						}],
				items : [{
							columnWidth : .5,
							layout : 'form',
							baseCls : 'x-plain',
							border : false,
							defaultType : 'textfield',
							labelWidth : 60,
							items : [combox, {
										name : 'contactDuty',
										baseCls : 'x-plain',
										fieldLabel : "Position",
										tabIndex : 3,
										anchor : '90%',
										validateOnBlur : false
									}, {
										name : 'contactPhone',
										baseCls : 'x-plain',
										fieldLabel : "Mobile",
										tabIndex : 5,
										anchor : '90%',
										validateOnBlur : false
									}]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							labelWidth : 60,
							items : [{
										id : 'contactPerson',
										name : 'contactPerson',
										fieldLabel : "<font color=red>Contact</font>",
										baseCls : 'x-plain',
										anchor : '90%',
										tabIndex : 1,
										allowBlank : false,
										blankText : 'Contact can not be empty！',
										validateOnBlur : true
									}, {
										name : 'contactNbr',
										fieldLabel : "Telephone",
										baseCls : 'x-plain',
										tabIndex : 4,
										anchor : '90%',
										validateOnBlur : false
									}, {
										name : 'contactFax',
										fieldLabel : "Fax",
										baseCls : 'x-plain',
										tabIndex : 6,
										anchor : '90%',
										validateOnBlur : false
									}]
						},  {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 60,
							items : [{
									xtype : "textfield",
									id : 'loginName',
									name : 'loginName',
									fieldLabel : "UserName",
									tabIndex : 7,
									anchor : '90%'
								}]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 60,
							items : [{
										xtype : "textfield",
										id : 'loginPwd',
										name : 'loginPwd',
										fieldLabel : "Password",
										tabIndex : 8,
										anchor : '90%',
										value : '123456',
										validateOnBlur : false
									}]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							bodyStyle : 'padding-top:2px',
							labelWidth : 60,
							items : [defBox]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							bodyStyle : 'padding-top:2px',
							labelWidth : 60,
							items : [{
										name : 'contactEmail',
										xtype : "textfield",
										baseCls : 'x-plain',
										fieldLabel : "E-mail",
										tabIndex : 9,
										anchor : '90%'
									}]
						},{
							columnWidth : 1,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							bodyStyle : 'padding-top:2px',
							labelWidth : 60,
							items : [{
										name : 'contactRemark',
										baseCls : 'x-plain',
										xtype : "textarea",
										fieldLabel : "Remarks",
										tabIndex : 10,
										anchor : '95%'
									}]
						}, {
							xtype : 'hidden',
							name : 'id'
						}]
			});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
	viewport.doLayout();

	// 初始化页面
	var emp = null;
	function initForm() {
		// 加载主表单
		var id = $("eId").value;
		var fId = $("fId").value;
		if (fId != 'null' && fId != '') {
			combox.bindPageValue('CotFactory', 'id', fId);
		}
		if (id != 'null' && id != '') {
			cotContactService.getContactById(parseInt(id), function(res) {
						if (res != null) {
							var obj = res;
							DWRUtil.setValues(obj);
							defBox.setValue(obj.mainFlag);
						}
					});
		} else {
			if (fId != 'null' && fId != '') {
				cotContactService.getFactoryById(fId, function(res) {
							if (res != null) {
								 Ext.getCmp('loginName').setValue(res.shortName);
							}
						});
			}
		}
		$('contactPerson').focus();
	}
	// 初始化页面
	initForm();
});
function mod() {
	var popedom = checkAddMod($('eId').value);
	if (popedom == 1) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority! ');
		return;
	} else if (popedom == 2) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority! ');
		return;
	}
	// 表单信息
	var obj = DWRUtil.getValues("contactForm");
	var cotContact = new CotContact();
	for (var p in obj) {
		cotContact[p] = obj[p];
	}
	var id = $("eId").value;
	if (id != 'null' && id != '') {
		cotContact.id = id;
	}
	
//	var flag = false;
//	DWREngine.setAsync(false);
//	cotContactService.findExistLoginName(cotContact.loginName, $('eId').value,
//			function(res) {
//				flag = res;
//			});
//	DWREngine.setAsync(true);
//	if (!flag) {
//		Ext.MessageBox.alert('Message', 'Sorry, UserName already exists!',function(btn){
//			if(btn=='ok'){
//				Ext.getCmp('loginName').selectText();
//			}			
//		});
//		return;
//	}
	
	var list = new Array();
	list.push(cotContact);
	DWREngine.setAsync(false);
	if (id != 'null' && id != '') {
		cotContactService.findExistByNo(cotContact.contactPerson,
				cotContact.factoryId, id, function(res) {
					if (res == null) {
						cotContactService.modifyContact(list, function(res) {
							if (res) {
								Ext.Msg.alert('Tip Box',
										"Successfully modified！");
								closeandreflashEC(true, 'contactGrid', false);
							} else {
								Ext.Msg.alert('Tip Box', "Modify the failure！");
								Ext.getCmp("contactPerson").selectText();
							}

						})
					} else {
						Ext.Msg.alert('Tip Box',
								"The supplier already exists in the contact！");
						Ext.getCmp("contactPerson").selectText();
					}
				});
	} else {
		cotContactService.findExistByName(cotContact.contactPerson,
				cotContact.factoryId, function(res) {
					if (!res) {
						cotContactService.addContact(list, function(res) {
							if (res) {
								Ext.Msg.alert('Tip Box', "Successfully added！");
								closeandreflashEC(true, 'contactGrid', false);
							} else {
								Ext.Msg.alert('Tip Box', "Add Failed！");
								Ext.getCmp("contactPerson").selectText();
							}
						});
					} else {
						Ext.Msg.alert('Tip Box',
								"The supplier already exists in the contacts!");
						Ext.getCmp("contactPerson").selectText();
					}
				});
	}
	DWREngine.setAsync(true);
}