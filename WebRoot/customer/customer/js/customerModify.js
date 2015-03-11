Ext.onReady(function() {
    //货代
	var companyCodeBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotConsignCompany&key=companyNameEn",
		cmpId : 'forwardt',
		//id:'forwardingAgent',
		fieldLabel:'Forwarding Agent',
		editable : true,
		valueField : "id",
		displayField : "companyNameEn",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 9,
		triggerAction : 'all'	
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
				tabIndex : 9,
				triggerAction : 'all'
//				listeners : {
//					"select" : function(combo, record, index) {
//						if (record.data.id != '') {
//							provinceBox.setDataUrl("servlet/DataSelvert?tbname=CotProvince&key=provinceName&typeName=nationId&type="+record.data.id);
//							provinceBox.resetData();
//							cityBox.resetData();
//						}
//					}
//				}
			});
	//国家
	var inationBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotNation&key=nationName",
				cmpId : 'invoiceCountryId',
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
				tabIndex : 25,
				triggerAction : 'all'
//				listeners : {
//					"select" : function(combo, record, index) {
//						if (record.data.id != '') {
//							provinceBox.setDataUrl("servlet/DataSelvert?tbname=CotProvince&key=provinceName&typeName=nationId&type="+record.data.id);
//							provinceBox.resetData();
//							cityBox.resetData();
//						}
//					}
//				}
			})
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
//				tabIndex : 10,
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
				tabIndex : 11,
				minChars : 1,
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 城市
	var icityBox = new BindCombox({
	        //	dataUrl : "servlet/DataSelvert?tbname=CotNationCity&key=cityName&typeName=provinceId&type=0",
				dataUrl : "servlet/DataSelvert?tbname=CotNationCity&key=cityName",
				cmpId : 'invoiceCityId',
				fieldLabel : "City",
				editable : true,
				valueField : "id",
				displayField : "cityName",
				emptyText : 'Choose',
				anchor : "97%",
				allowBlank : true,
				sendMethod : "post",
				pageSize : 10,
				tabIndex : 24,
				minChars : 1,
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	var empBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'empId',
		fieldLabel : "Sales",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 4,
		triggerAction : 'all'
	});
	// 客户简称
	var custSnBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'customerShortName',
		fieldLabel : "Short Ttle",
		editable : true,
		valueField : "customerShortName",
		displayField : "customerShortName",
		emptyText : 'Choose',
		typeAhead : false,
		pageSize : 10,
		anchor : "100%",
		needReset : false,
		allowBlank : false,
		blankText : "请输入客户简称",
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 2,
		triggerAction : 'all'
	});

	// 客户中文全称
	var custFBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=fullNameCn",
				cmpId : 'fullNameCn',
				fieldLabel : "中文全称",
				editable : true,
				hidden : true,
				hideLabel : true,
				valueField : "fullNameCn",
				displayField : "fullNameCn",
				emptyText : 'Choose',
				pageSize : 10,
				needReset : false,
				typeAhead : false,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 4,
				triggerAction : 'all'
			});
	// 客户英文全称
	var custFullEnBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=fullNameEn",
				cmpId : 'fullNameEn',
				fieldLabel : "Name",
				editable : true,
				valueField : "fullNameEn",
				displayField : "fullNameEn",
				emptyText : 'Choose',
				pageSize : 10,
				needReset : false,
				typeAhead : false,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 13,
				triggerAction : 'all'
			});

	var custLvBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustomerLv&key=lvName",
				cmpId : 'custLvId',
				fieldLabel : "客户等级",
				editable : true,
				valueField : "id",
				displayField : "lvName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 28,
				triggerAction : 'all'
			});
	var custTypeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustomerType&key=typeName",
				cmpId : 'custTypeId',
				fieldLabel : "Type",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 12,
				triggerAction : 'all'
			});
	var payTypeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotPayType&key=payName",
				cmpId : 'payTypeId',
				fieldLabel : "Pay Terms",
				editable : true,
				valueField : "id",
				displayField : "payName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 16,
				triggerAction : 'all'
			});
	var clauseBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotClause&key=clauseName",
				cmpId : 'clauseId',
				fieldLabel : "Delivery Terms",
				editable : true,
				valueField : "id",
				displayField : "clauseName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 15,
				triggerAction : 'all'
			});
	var commisionTypeBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotCommisionType&key=commisionName",
		cmpId : 'commisionTypeId',
		fieldLabel : "佣金类型",
		editable : true,
		valueField : "id",
		displayField : "commisionName",
		emptyText : 'Choose',
		mode : 'remote',// 默认local
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		sendMethod : "post",
		selectOnFocus : false,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 24,
		triggerAction : 'all'
	});
	var targetPortBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotTargetPort&key=targetPortEnName",
		cmpId : 'trgportId',
		fieldLabel : "Destination",
		editable : true,
		valueField : "id",
		displayField : "targetPortEnName",
		emptyText : 'Choose',
		mode : 'remote',// 默认local
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 17,
		triggerAction : 'all'
	});

	var form = new Ext.form.FormPanel({
		labelWidth : 90,
		region : "center",
		labelAlign : "right",
		layout : "form",
		formId : "cotCustomerForm",
		padding : "5px",
		// renderTo:Ext.getBody(),
		// frame:true,
		autoHeight : true,
		autoWidth : true,
		width : 400,
		height : 250,
		items : [{
			xtype : "fieldset",
			title : "Base Information",
			buttonAlign : "center",
			layout : "column",
			fbar : [{
						text : "Save",
						handler : mod,
						iconCls : "page_table_save",
						tabIndex : 44
					}, {
						text : "Delete",
						handler : del,
						iconCls : "page_del",
						tabIndex : 45
					}, {
						text : "Cancel",
						iconCls : "page_cancel",
						tabIndex : 46,
						handler : function() {
							closeandreflashEC(true, "custGrid", false);
						}
					}],
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "<font color='red'>No.</font>",
									anchor : "100%",
									allowBlank : false,
									maxLength : 20,
									id : "customerNo",
									name : "customerNo",
									tabIndex : 1
								}, {
									xtype : "textfield",
									fieldLabel : "Tel",
									anchor : "100%",
									allowBlank : true,
									maxLength : 100,
									id : "contactNbr",
									name : "contactNbr",
									tabIndex : 5
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [custSnBox, {
									xtype : "textfield",
									fieldLabel : "Fax",
									anchor : "100%",
									allowBlank : true,
									maxLength : 255,
									id : "customerFax",
									name : "customerFax",
									tabIndex : 6
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "Contact",
									anchor : "100%",
									allowBlank : true,
									maxLength : 100,
									id : "priContact",
									name : "priContact",
									tabIndex : 3
								}, {
									xtype : "textfield",
									fieldLabel : "Email",
									anchor : "100%",
									allowBlank : true,
									maxLength : 100,
									id : "customerEmail",
									name : "customerEmail",
									tabIndex : 7
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [empBox, {
									xtype : "textfield",
									fieldLabel : "Zip Code",
									anchor : "100%",
									allowBlank : true,
									maxLength : 50,
									id : "customerPost",
									name : "customerPost",
									tabIndex : 8
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [custFBox, {
									xtype : "textfield",
									fieldLabel : "中文地址",
									anchor : "100%",
									allowBlank : true,
									hidden : true,
									hideLabel : true,
									maxLength : 100,
									id : "customerAddress",
									name : "customerAddress",
									tabIndex : 11
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [nationBox]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [cityBox]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [{
										xtype : "textfield",
										fieldLabel : "Vat No.",
										anchor : "100%",
										allowBlank : true,
										maxLength : 50,
										id : "vatNo",
										name : "vatNo",
										tabIndex : 12
									}]
								},{
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [{
										xtype : "textfield",
										fieldLabel : "Supplier No",
										anchor : "100%",
										allowBlank : true,
										maxLength : 150,
										name:'supplierNo'
									}]
								}]
					},{
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "form",
						items : [custFullEnBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "Address",
									anchor : "100%",
									allowBlank : true,
									maxLength : 255,
									id : "customerAddrEn",
									name : "customerAddrEn",
									tabIndex : 14
								}]
					},{
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "form",
						items:[companyCodeBox]
					} , {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [clauseBox]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [payTypeBox]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [targetPortBox]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : .25,
									layout : "form",
									items : [{
												xtype : "datefield",
												fieldLabel : "Added Time",
												value : new Date(),
												id : "addTime",
												name : "addTime",
												anchor : "100%",
												allowBlank : true,
												format : "Y-m-d",
												tabIndex : 18
											}]
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "form",
						items : [{
									xtype : "textarea",
									fieldLabel : "Shipping Mark",
									anchor : "100%",
									height : 60,
									allowBlank : true,
									maxLength : 500,
									id : "customerMb",
									name : "customerMb",
									tabIndex : 19

								},{
									xtype : "textarea",
									fieldLabel : "Remark",
									anchor : "100%",
									height : 40,
									allowBlank : true,
									maxLength : 500,
									id : "customerRemark",
									name : "customerRemark",
									tabIndex : 20

								},{
									xtype : "textarea",
									fieldLabel : "Produced For",
									anchor : "100%",
									height : 40,
									allowBlank : true,
									maxLength : 500,
									id : "productIn",
									name : "productIn",
									tabIndex : 20

								},{
									xtype : "textfield",
									fieldLabel : "Add Person",
									anchor : "100%",
									disabled:true,
									allowBlank : true,
									maxLength : 500,
									id : "addPersorn",
									name : "addPersorn",
									tabIndex : 20

								},{
							xtype : "fieldset",
							title : "Invoice Address",
							collapsible: true,
							layout : "column",
							items : [{
								xtype : "panel",
								columnWidth : 0.75,
								layout : "column",
								hideBorders : false,
								items : [{
											xtype : "panel",
											title : "",
											columnWidth : 1,
											layout : "form",
											items : [{
														xtype : "textfield",
														fieldLabel : "Name",
														anchor : "98%",
														allowBlank : true,
														maxLength : 200,
														id : "invoiceCustomerName",
														name : "invoiceCustomerName",
														tabIndex : 21
													}, {
														xtype : "textfield",
														fieldLabel : "Short Title",
														anchor : "98%",
														allowBlank : true,
														maxLength : 100,
														id : "invoiceShortName",
														name : "invoiceShortName",
														tabIndex : 22
													}]
										},{
											xtype : "panel",
											title : "",
											columnWidth : 1,
											layout : "column",
											items :[{
												xtype:'panel',
												title : "",
												columnWidth : 0.5,
												layout : "form",
												items:[inationBox]
											},{
												xtype:'panel',
												title : "",
												columnWidth : 0.5,
												layout : "form",
												items:[icityBox]
											}]
										},{
												xtype : "panel",
												title : "",
												columnWidth : 1,
												layout : "form",
												items : [{
															xtype : "textfield",
															fieldLabel : "Zip Code",
															anchor : "98%",
															allowBlank : true,
															maxLength : 200,
															id : "invoiceCustomerPost",
															name : "invoiceCustomerPost",
															tabIndex : 25
														}]
											},{
												xtype : "panel",
												title : "",
												columnWidth : 1,
												layout : "form",
												items : [{
															xtype : "textfield",
															fieldLabel : "Address",
															anchor : "98%",
															allowBlank : true,
															maxLength : 255,
															id : "invoiceCustomerAddress",
															name : "invoiceCustomerAddress"
														}]
											}]
							}, {
								xtype : "fieldset",
								title : "Customer Logo",
								layout : "form",
								columnWidth : 0.25,
								items : [{
									xtype : "panel",
									title : "",
									buttonAlign : "center",
									html : '<div align="center" style="padding-left:50px;width: 150px; height: 140px;">'
											+ '<img src="common/images/zwtp.png" id="customPhotoPath" name="customPhotoPath"'
											+ 'onload="javascript:DrawImage(this,150,180)" onclick="showBigPicDiv(this)"/>'
											+ '</div>',
									fbar : [{
												text : "Upload",
												iconCls : "upload-icon",
												handler : showUploadCustPanel
											}, {
												text : "Delete",
												iconCls : "upload-icon-del",
												handler : delPic
											}]
								}]
							}]
						} ]
					}]
		}, {
			xtype : "tabpanel",
			deferredRender : false,
			defaults : {
				autoScroll : true
			},
			defaultType : "iframepanel",
			// height : 350,
			activeTab : 0,
			id : "maincontent",
			items : [{
						title : "Contact",
						itemId : 'custContactTab',
						height : 470,
						defaultSrc : '',
						frameConfig : {
							autoCreate : {
								id : 'custContact'
							}
						}, // iframe的Id
						loadMask : {
							msg : 'Loading...'
						},
						listeners : {
							"activate" : function(panel) {
								// var p = panel.findParentByType("form");
								// panel.setHeight(screen.height - 350);
								loadCustContact();
							}
						}
					}
//					, {
//						title : "来访记录",
//						height : 470,
//						itemId : 'custVisitedLogTab',
//						defaultSrc : '',
//						frameConfig : {
//							autoCreate : {
//								id : 'custVisitedLog'
//							}
//						}, // iframe的Id
//						loadMask : {
//							msg : 'Loading...'
//						},
//						listeners : {
//							"activate" : function(panel) {
//								// var p = panel.findParentByType("form");
//								// panel.setHeight(screen.height - 350);
//								loadCustVisitedLog();
//							}
//						}
//					}
//					, {
//						title : "邮件记录",
//						itemId : 'mailTab',
//						defaultSrc : '',
//						height : 470,
//						frameConfig : {
//							autoCreate : {
//								id : 'mail'
//							}
//						}, // iframe的Id
//						loadMask : {
//							msg : 'Loading...'
//						},
//						listeners : {
//							"activate" : function(panel) {
//								// var p = panel.findParentByType("form");
//								// panel.setHeight(screen.height - 350);
//								loadMail();
//							}
//						}
//					}
					, {
						title : "Offer History",
						itemId : 'priceTab',
						height : 470,
						defaultSrc : '',
						frameConfig : {
							autoCreate : {
								id : 'price'
							}
						}, // iframe的Id
						loadMask : {
							msg : 'Loading...'
						},
						listeners : {
							"activate" : function(panel) {
								// var p = panel.findParentByType("form");
								// panel.setHeight(screen.height - 350);
								loadPrice();
							}
						}
					}
//					, {
//						title : "送样记录",
//						height : 470,
//						itemId : 'givenTab',
//						defaultSrc : '',
//						frameConfig : {
//							autoCreate : {
//								id : 'given'
//							}
//						}, // iframe的Id
//						loadMask : {
//							msg : 'Loading...'
//						},
//						listeners : {
//							"activate" : function(panel) {
//								// var p = panel.findParentByType("form");
//								// panel.setHeight(screen.height - 350);
//								loadGiven();
//							}
//						}
//					}
					, {
						title : "Order History",
						itemId : 'orderTab',
						height : 470,
						defaultSrc : '',
						frameConfig : {
							autoCreate : {
								id : 'order'
							}
						}, // iframe的Id
						loadMask : {
							msg : 'Loading...'
						},
						listeners : {
							"activate" : function(panel) {
								// var p = panel.findParentByType("form");
								// panel.setHeight(screen.height - 350);
								loadOrder();
							}
						}
					}
					, {
						title : "Art Work",
						itemId : 'custPhoneTab',
						height : 470,
						defaultSrc : '',
						frameConfig : {
							autoCreate : {
								id : 'custPhone'
							}
						}, 
						loadMask : {
							msg : 'Loading...'
						},
						listeners : {
							"activate" : function(panel) {
								loadCustPhone();
							}
						}
					}
//					, {
//						title : "成交图片",
//						itemId : 'orderPicTab',
//						defaultSrc : '',
//						height : 470,
//						frameConfig : {
//							autoCreate : {
//								id : 'orderPic'
//							}
//						}, // iframe的Id
//						loadMask : {
//							msg : 'Loading...'
//						},
//						listeners : {
//							"activate" : function(panel) {
//								// var p = panel.findParentByType("form");
//								// panel.setHeight(screen.height - 350);
//								loadOrderPic();
//							}
//						}
//					}, {
//						title : "客户索赔",
//						height : 470,
//						itemId : 'claimTab',
//						defaultSrc : '',
//						frameConfig : {
//							autoCreate : {
//								id : 'claim'
//							}
//						}, // iframe的Id
//						loadMask : {
//							msg : 'Loading...'
//						},
//						listeners : {
//							"activate" : function(panel) {
//								// var p = panel.findParentByType("form");
//								// panel.setHeight(screen.height - 350);
//								loadClaim()
//							}
//						}
//					}
					]
		}]
	});

	var mainPanel = new Ext.Panel({
				name : "mainPanel",
				autoScroll : true,
				layout : 'fit',
				frame : true,
				items : [form]
			})

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [mainPanel]
			});

	// 页面加载调用
	function initForm() {
		DWREngine.setAsync(false);
		var id = $("customerid").value;
		cotCustomerService.getCustomerById(parseInt(id), function(res) {
					var obj = res;
					DWRUtil.setValues(obj);
					$('customPhotoPath').src = "./showPicture.action?detailId="
							+ id + "&flag=custPhoto";
//					$('customerMbPath').src = "./showPicture.action?detailId="
//							+ id + "&flag=custMb";
//					custFBox.setValue(obj.fullNameCn);
					custSnBox.setValue(obj.customerShortName);
					custFullEnBox.setValue(obj.fullNameEn);
//					provinceBox.bindPageValue("CotProvince", "id",
//							obj.provinceId);
					nationBox.bindPageValue("CotNation", "id", obj.nationId);
					cityBox.bindPageValue("CotNationCity", "id", obj.cityId);
					inationBox.bindPageValue("CotNation", "id", obj.invoiceCountryId);
					icityBox.bindPageValue("CotNationCity", "id", obj.invoiceCityId);
					empBox.bindPageValue("CotEmps", "id", obj.empId);
//					custLvBox.bindPageValue("CotCustomerLv", "id", obj.custLvId);
					custTypeBox.bindPageValue("CotCustomerType", "id",
							obj.custTypeId);
					payTypeBox.bindPageValue("CotPayType", "id", obj.payTypeId);
					clauseBox.bindPageValue("CotClause", "id", obj.clauseId);
//					commisionTypeBox.bindPageValue("CotCommisionType", "id",obj.commisionTypeId);
					targetPortBox.bindPageValue("CotTargetPort", "id",
							obj.trgportId);
					Ext.getCmp("addTime").setValue(obj.addTime);
//					Ext.getCmp("cooperateLvId").setValue(obj.cooperateLv);
					companyCodeBox.bindPageValue("CotConsignCompany", "id", obj.forwardingAgent);

				})
		DWREngine.setAsync(true);
	}
	// 初始化页面
	initForm();
	unmask();

	// 打开上传面板
	function showUploadCustPanel() {
		var id = $('customerid').value;
		var opAction = "insert";
		if (id == 'null' || id == '')
			opAction = "insert";
		else
			opAction = "modify";
		var win = new UploadWin({
					params : {
						customerId : id,
						uploadFolder : "/customerPhoto"
					},
					waitMsg : "Uploading......",
					opAction : opAction,
					imgObj : $('customPhotoPath'),
					uploadType : "image",
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=custPhoto&tmp="
							+ Math.round(Math.random() * 10000),
					loadImgStream : true,
					uploadUrl : './uploadCustomerPhoto.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}
	// 打开上传面板
	function showUploadMbPanel() {
		var id = $('customerid').value;
		var opAction = "insert";
		if (id == 'null' || id == '')
			opAction = "insert";
		else
			opAction = "modify";
		var win = new UploadWin({
					params : {
						customerId : id,
						uploadFolder : "/customerMb"
					},
					waitMsg : "Uploading......",
					opAction : opAction,
					imgObj : $('customerMbPath'),
					uploadType : "image",
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=custMb&tmp="
							+ Math.round(Math.random() * 10000),
					loadImgStream : true,
					uploadUrl : './uploadCustomerPhoto.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}
});