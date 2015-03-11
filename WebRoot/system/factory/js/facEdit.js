Ext.onReady(function() {
	// 起运港
	var shipPortBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotShipPort&key=shipPortNameEn",
				cmpId : 'shipportId',
				fieldLabel : "Port of Loading",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "shipPortNameEn",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 11,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,
				listeners : {
					"select" : function(com, rec, index) {
						composeChange();
					}
				}
			});
	//国家
	var nationBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotNation&key=nationName",
				cmpId : 'nationId',
				fieldLabel : "Country",
				editable : true,
				valueField : "id",
				displayField : "nationName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 7,
				triggerAction : 'all',
				listeners : {
					"select" : function(combo, record, index) {
						if (record.data.id != '') {
							cityBox.setDataUrl("servlet/DataSelvert?tbname=CotNationCity&key=cityName&typeName=nationId&type="+record.data.id);
							cityBox.resetData();
						}
					}
				}
			});
	
	// 省份
//	var provinceBox = new BindCombox({
//				dataUrl : "servlet/DataSelvert?tbname=CotProvince&key=provinceName&typeName=nationId&type=0",
//				cmpId : 'provinceId',
//				fieldLabel : "Provinces",
//				editable : true,
//				valueField : "id",
//				displayField : "provinceName",
//				emptyText : 'Choose',
//				anchor : "100%",
//				allowBlank : true,
//				sendMethod : "post",
//				pageSize : 10,
//				tabIndex : 8,
//				minChars : 1,
//				listWidth : 350,// 下
//				triggerAction : 'all',
//				listeners : {
//					"select" : function(combo, record, index) {
//						if (record.data.id != '') {
//							cityBox.setDataUrl("servlet/DataSelvert?tbname=CotNationCity&key=cityName&typeName=provinceId&type="+record.data.id);
//							cityBox.resetData();
//						}
//					}
//				}
//			});
	// 城市
	var cityBox = new BindCombox({
				//dataUrl : "servlet/DataSelvert?tbname=CotNationCity&key=cityName&typeName=provinceId&type=0",
				dataUrl : "servlet/DataSelvert?tbname=CotNationCity&key=cityName",
				cmpId : 'cityId',
				fieldLabel : "City",
				editable : true,
				valueField : "id",
				displayField : "cityName",
				emptyText : 'Choose',
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				pageSize : 10,
				tabIndex : 9,
				minChars : 1,
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 付款方式
	var payTypeBox = new BindCombox({
				cmpId : 'payTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotPayType",
				emptyText : 'Choose',
				fieldLabel : "<font color='red'>Pay.Terms</font>",
				allowBlank : false,
				blankText: 'Payment can not be empty',
				displayField : "payName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});
	/***************************************************************************
	 * 描述: 本地下拉列表
	 **************************************************************************/
	var typeStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [ [1, 'Enable'], [0, 'Disable']]
			});

	var typeField = new Ext.form.ComboBox({
				hiddenName : 'factroyTypeidLv1',
				fieldLabel : 'Status',
				editable : false,
				store : typeStore,
				value :0,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				tabIndex : 4,
				disabled:(getPopedomByOpType("cotfactory.do",'ENABLE') == 1)?false:true,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				selectOnFocus : true
			});

	var factoryScaleStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[1, '大型'], [2, '中型'], [3, '小型']]
			});

	var factoryScaleField = new Ext.form.ComboBox({
				name : 'factoryScale',
				hiddenName : 'factoryScale',
				fieldLabel : '厂家规模',
				editable : false,
				hidden:true,
				hideLabel:true,
				store : factoryScaleStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				tabIndex : 5,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "90%",
				// emptyText : 'Choose',
				selectOnFocus : true
			});

	var cooperateLvStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '很好'], [1, '好'], [2, '较好'], [3, '一般'], [4, '较差'],
						[5, '差'], [6, '很差']]
			});

	var cooperateLvField = new Ext.form.ComboBox({
				name : 'cooperateLv',
				hiddenName : "cooperateLv",
				fieldLabel : '配合程度',
				editable : false,
				hidden:true,
				hideLabel:true,
				store : cooperateLvStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				tabIndex : 6,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				// emptyText : 'Choose',
				selectOnFocus : true
			});
			
	//厂家简称						
	var facShortNameBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
		cmpId : 'shortName',
		fieldLabel : "<font color=red>Short Title</font>",
		editable : true,
		valueField : "shortName",
		displayField : "shortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		needReset:false,
		typeAhead : false,
		maxLength:100,
		allowBlank : false,
		blankText : "Short Title can not be empty！",
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 2,
		triggerAction : 'all'
	});		
	

	//厂家全称						
	var facFullNameBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=factoryName",
		cmpId : 'factoryName',
		fieldLabel : "<font color=red>Name</font>",
		editable : true,
		valueField : "factoryName",
		displayField : "factoryName",
		emptyText : 'Choose',
		pageSize : 10,
		maxLength:250,
		anchor : "100%",
		needReset:false,
		typeAhead : false,
		allowBlank : false,
		blankText : "Name can not be empty！",
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 3,
		triggerAction : 'all'
	});
			
	var form = new Ext.form.FormPanel({
		labelWidth : 80,
		labelAlign : "right",
		layout : "form",
		region : "north",
		autoWidth : true,
		autoHeight : true,
		padding : "5px",
		renderTo : "modpage",
		formId : "factoryForm",
		id : "factoryFormId",
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
						closeandreflashEC(true, 'factoryGrid', false)
					}
				}],
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
				xtype : "fieldset",
				title : "Base Information",
				layout : "column",
				columnWidth : 1,
				height : 280,
				items : [{
							columnWidth : .33,
							layout : 'form',
							baseCls : 'x-plain',
							border : false,
							defaultType : 'textfield',
							items : [{
										id : 'factoryNo',
										name : 'factoryNo',
										fieldLabel : "<font color=red>No.</font>",
										baseCls : 'x-plain',
										anchor : '100%',
										tabIndex : 1,
										maxLength : 200,
										allowBlank : false,
										blankText : 'No. can not be empty！'
										//validateOnBlur : false
									},{
										xtype : "panel",
									    layout : "column",
									    items:[{
									    	xtype : "panel",
											columnWidth :0.5,
											layout : "form",
											items:[payTypeBox]
									    },{
									    	xtype : "panel",
											columnWidth :0.5,
											layout : "form",
											items:[typeField]
									    }]
									}, nationBox]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							labelWidth : 110,
							labelAlign : "left",
							items : [facShortNameBox, factoryScaleField, {
										xtype : "panel",
									    layout : "column",
									    items:[{
									    	xtype : "panel",
											columnWidth :0.5,
											layout : "form",
											labelWidth : 110,
							                 labelAlign : "left",
											items:[{
												xtype:'textfield',
												name : 'factoryNbr',
												baseCls : 'x-plain',
												fieldLabel : "Tel.",
												tabIndex : 4,
												anchor : '100%',
												maxLength : 100,
												validateOnBlur : false
											}]
									    },{
									    	xtype : "panel",
											columnWidth :0.5,
											layout : "form",
											labelWidth : 60,
							                labelAlign : "left",
											items:[{
												xtype:'textfield',
												name : 'factoryFax',
												baseCls : 'x-plain',
												fieldLabel : "Fax",
												tabIndex : 5,
												anchor : '100%',
												maxLength : 100,
												validateOnBlur : false
											}]
									    }]
										
									},cityBox]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							labelWidth : 120,
							labelAlign : "left",
							items : [facFullNameBox, cooperateLvField, {
										name : 'factoryEmail',
										baseCls : 'x-plain',
										fieldLabel : "Email",
										maxLength : 500,
										anchor : '100%',
										tabIndex : 6,
										id : 'factoryEm',
										validateOnBlur : false
									},{
										name : 'post',
										baseCls : 'x-plain',
										fieldLabel : "Zip Code",
										tabIndex : 12,
										anchor : '100%',
										validateOnBlur : false
									}]
						}, {
							columnWidth : 0.66,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							items : [{
										name : 'factoryAddr',
										baseCls : 'x-plain',
										fieldLabel : "Address",
										tabIndex : 11,
										maxLength : 200,
										anchor : '100%',
										validateOnBlur : false
									}]
						},{
							columnWidth : 0.33,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							labelWidth : 120,
							labelAlign : "left",
							items : [shipPortBox]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							labelWidth : 110,
							labelAlign : "left",
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							items : [{
										name : 'beneficiaryName',
										baseCls : 'x-plain',
										fieldLabel : "Beneficiary Name",
										maxLength : 200,
										anchor : '100%',
										tabIndex : 14,
										validateOnBlur : false
									}]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							labelWidth : 120,
							labelAlign : "left",
							items : [{
										name : 'beneficiaryAddress',
										baseCls : 'x-plain',
										fieldLabel : "Beneficiary Address",
										maxLength : 200,
										anchor : '100%',
										tabIndex : 14,
										validateOnBlur : false
									}]
						},{
							columnWidth : .33,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							labelWidth : 120,
							labelAlign : "left",
							bodyStyle : 'padding-top:2px',
							items : [{
										name : 'factoryBank',
										baseCls : 'x-plain',
										fieldLabel : "Bank Name",
										maxLength : 100,
										anchor : '100%',
										tabIndex : 13,
										validateOnBlur : false
									}]
						},{
							columnWidth : .33,
							layout : 'form',
							border : false,
							labelWidth : 110,
							labelAlign : "left",
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							items : [{
										name : 'bankAddress',
										baseCls : 'x-plain',
										fieldLabel : "Bank Address",
										maxLength : 200,
										anchor : '100%',
										tabIndex : 14,
										validateOnBlur : false
									}]
						},  {
							columnWidth : .33,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							labelWidth : 120,
							labelAlign : "left",
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							items : [{
										name : 'bankAccount',
										baseCls : 'x-plain',
										fieldLabel : "Account No",
										maxLength : 100,
										anchor : '100%',
										tabIndex : 14,
										validateOnBlur : false
									}]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							labelWidth : 120,
							labelAlign : "left",
							bodyStyle : 'padding-top:2px',
							items : [{
										name : 'swiftCode',
										baseCls : 'x-plain',
										fieldLabel : "SwiftCode",
										maxLength : 100,
										anchor : '100%',
										tabIndex : 13,
										validateOnBlur : false
									}]
						},  {
							columnWidth : 1,
							layout : 'form',
							border : false,
							baseCls : 'x-plain',
							defaultType : 'textfield',
							bodyStyle : 'padding-top:2px',
							items : [{
										name : 'contactPerson',
										baseCls : 'x-plain',
										xtype : 'textfield',
										fieldLabel : "Add Person",
										tabIndex : 15,
										maxLength : 2000,
										disabled:true,
										anchor : '99%',
										validateOnBlur : false
									},{
										name : 'remark',
										baseCls : 'x-plain',
										xtype : 'textarea',
										fieldLabel : "Remark",
										tabIndex : 15,
										maxLength : 2000,
										anchor : '99%',
										validateOnBlur : false
									}]
						}, {
							xtype : 'hidden',
							name : 'id',
							id : 'id'
						}]
			}]
		}]
	});
	var tbl = new Ext.TabPanel({
				region : 'center',
				deferredRender : false,
				defaults : {
					autoScroll : true
				},
				defaultType : "iframepanel",
				heigth : 350,
				activeTab : 0,
				id : "maincontent",
				items : [{
							title : "Contact person",
							itemId : 'contactRec',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'contactInfo'
								}
							},
							loadMask : {
								msg : 'Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadContactInfo();
								}
							}
						}, {
							title : "Purchase History Records",
							itemId : 'orderFacTab',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'orderFacInfo'
								}
							},
							loadMask : {
								msg : 'Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadOrderFacInfo();
								}
							}
						}, {
							title : "Rating",
							itemId : 'ratingTab',
							defaultSrc : '',
							frameConfig : {
								autoCreate : {
									id : 'ratingInfo'
								}
							},
							loadMask : {
								msg : 'Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadFenInfo();
								}
							}
						}
//						, {
//							title : "邮件记录",
//							itemId : 'mailTab',
//							defaultSrc : '',
//							frameConfig : {
//								autoCreate : {
//									id : 'mail'
//								}
//							}, // iframe的Id
//							loadMask : {
//								msg : 'Loading...'
//							},
//							listeners : {
//								"activate" : function(panel) {
//									loadMail();
//								}
//							}
//						}
//						,{
//							title : "报价记录",
//							itemId : 'priceTab',
//							defaultSrc : '',
//							frameConfig : {
//								autoCreate : {
//									id : 'price'
//								}
//							}, // iframe的Id
//							loadMask : {
//								msg : 'Loading...'
//							},
//							listeners : {
//								"activate" : function(panel) {
//									loadPriceFac();
//								}
//							}
//						}
						]
			})
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [form, tbl]
			})

	// 获取编号
	function getFacNo() {
		cotSeqService.getFacNo(function(res) {
					Ext.getDom('factoryNo').value = res;
				})
	}
	// 初始化页面
	function initForm() {
		// 加载主表单
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotFactoryService.getFactoryById(parseInt(id), function(res) {
						if (res != null) {
							var obj = res;
							DWRUtil.setValues(obj);
							facShortNameBox.setValue(res.shortName);
							facFullNameBox.setValue(res.factoryName);
							
							nationBox.bindPageValue('CotNation','id',res.nationId);
							//provinceBox.bindPageValue('CotProvince','id',res.provinceId);
							cityBox.bindPageValue('CotNationCity','id',res.cityId);
							shipPortBox.bindPageValue('CotFactory','id',res.shipportId);
							typeField.setValue(res.factroyTypeidLv1);
							factoryScaleField.setValue(res.factoryScale);
							cooperateLvField.setValue(res.cooperateLv);
							//付款方式 cotFactory.payTypeId
							payTypeBox.bindPageValue('CotPayType','id',res.payTypeId);
						}
					});
		} else {
			// 新增时隐藏联系人标签
			tbl.hide();
			getFacNo();
		}
		$('shortName').focus();
	}
	// 初始化页面
	initForm();
	// 加载邮件记录
	function loadMail() {
		var id = $("eId").value;
		var frame = window.frames["mail"];
		frame.location.href = "cotfactory.do?method=loadMail&factoryId=" + id;
	}
	// 加载报价记录
	function loadPriceFac() {
		var id = $("eId").value;
		var frame = window.frames["price"];
		frame.location.href = "cotfactory.do?method=loadPriceFac&factoryId=" + id;
	}

	// 获取编号
	function getFacNo() {
		cotSeqService.getFacNo(function(res) {
					Ext.getDom('factoryNo').value = res;
				})
	}

	function mod() {
		var popedom = checkAddMod($('eId').value);
		if (popedom == 1) {
			Ext.MessageBox.alert('Message', 'Sorry, you do not have permission to add!');
			return;
		} else if (popedom == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, you do not modify the permissions!');
			return;
		}
		// 验证表单
		var formData = getFormValues(form, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var addressValue = Ext.getCmp('factoryEm').getValue();
		if (Ext.isEmpty(addressValue) == false) {
			var reg = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
			if (reg.test(addressValue) == false) {
				Ext.Msg.alert('Message', 'Please enter a valid email address format!');
				return;
			}
		}
		
	
		// 表单信息
		var obj = DWRUtil.getValues("factoryForm");
		var cotFactory = new CotFactory();
		for (var p in cotFactory) {
			cotFactory[p] = obj[p];
		}
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotFactory.id = id;
		}
		//付款方式
		cotFactory.payTypeId = payTypeBox.getValue();
		
		//起运港
		cotFactory.shipportId=shipPortBox.getValue();
		DWREngine.setAsync(false);
		var flag = false;
		cotFactoryService.findExistByNo(cotFactory.factoryNo, id, function(res) {
					flag = res;
				})
		// 判断是否厂家编码相同
		if (flag != null) {
			Ext.Msg.alert('Message', "No. already exists！");
			return;
		}
		var list = new Array();
		list.push(cotFactory);
		if (id != 'null' && id != '') {
			cotFactoryService.findIsExistShortName(parseInt(cotFactory.id),
					cotFactory.shortName, function(res) {
						if (!res) {
							cotFactoryService.modifyFactory(list, function(res) {
										if (res) {
											reflashParent('factoryGrid');
											Ext.Msg.alert('Message', "Successfully modified！");
										} else {
											Ext.Msg.alert('Message', "Modify Fail！");
											//Ext.getCmp("shortName").selectText();
										}
									})
						} else {
							Ext.Msg.alert('Message', "No. already exists！");
							//Ext.getCmp("shortName").selectText();
						}
					});
		} else {
			var addPersorn;
				cotPopedomService.getLoginEmpId(function(res){
				 	addPersorn = res;
				})
			cotFactory.contactPerson = addPersorn;
			cotFactoryService.findExistByName(cotFactory.shortName, function(res) {
						if (!res) {
							cotFactoryService.addFactory(list, function(res) {
										if (res) {
											reflashParent('factoryGrid');
											Ext.Msg.alert('Message', "Save Success！");
											window.close();
										} else {
											Ext.Msg.alert('Message', "Save Fail！");
											//Ext.getCmp("shortName").selectText();
										}
									});
						} else {
							Ext.Msg.alert('Message', "No. already exists！");
							//Ext.getCmp("shortName").selectText();
						}
					});
		}
		DWREngine.setAsync(true);
	}
});
// 查看联系人
function loadContactInfo() {
	var facId = $("eId").value;
	if (facId == 'null' || facId == '') {
		facId = 0;
	}
	var frame = document.contactInfo;
	if (frame.location.href == 'about:blank')
		frame.location.href = "cotfactory.do?method=queryContact&facId="
				+ facId;
}
// 查看采购记录
function loadOrderFacInfo() {
	var facId = $("eId").value;
	if (facId == 'null' || facId == '') {
		facId = 0;
	}
	var frame = document.orderFacInfo;
	if (frame.location.href == 'about:blank')
		frame.location.href = "cotfactory.do?method=queryOrderFac&facId="
				+ facId;
}
// 查看评分记录
function loadFenInfo() {
	var facId = $("eId").value;
	if (facId == 'null' || facId == '') {
		facId = 0;
	}
	var frame = document.ratingInfo;
	if (frame.location.href == 'about:blank')
		frame.location.href = "cotfactory.do?method=gotoFacFen&facId="
				+ facId;
}