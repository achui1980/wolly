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
	
//	// 省份
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
	        //	dataUrl : "servlet/DataSelvert?tbname=CotNationCity&key=cityName&typeName=provinceId&type=0",
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
				anchor : "100%",
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
		fieldLabel : "<font color='red'>Short Title</font>",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		typeAhead : false,
		needReset : false,
		allowBlank : false,
		blankText : "Pls. enter a short name",
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
				cmpId : 'fameCn',
				fieldLabel : "name-cn",
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
				fieldLabel : "Level",
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
				fieldLabel : "Payment Terms",
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
	var invoiceForm=new Ext.form.FormPanel({
	title:"Invoice Address",
	labelWidth:100,
	labelAlign:"right",
	layout:"column",
	height:101,
	padding:"5",
	frame:true,
	width:778,
	items:[
		{
			xtype:"panel",
			title:"",
			columnWidth:0.5,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"Name",
					anchor:"100%"
				}
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.5,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"Short Title",
					anchor:"100%"
				}
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.33,
			layout:"form",
			items:[
				{
					xtype:"textfield",
					fieldLabel:"Country",
					anchor:"100%"
				}
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.33,
			height:33,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"City",
					anchor:"100%"
				}
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.34,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"Zip Code",
					anchor:"100%"
				}
			]
		}
	]
})
	var form = new Ext.form.FormPanel({
		title : "",
		labelWidth : 90,
		labelAlign : "right",
		layout : "form",
		width : 400,
		height : 250,
		formId : "cotCustomerForm",
		padding : "5px",
		// frame:true,
		autoHeight : true,
		autoWidth : true,
		// renderTo:Ext.getBody(),
		// monitorValid : true,
		buttonAlign : "center",
		fbar : [{
					text : "Save",
					// formBind : true,
					handler : add,
					iconCls : "page_table_save",
					tabIndex : 44
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					tabIndex : 45,
					handler : function() {
						closeandreflashEC(true, "custGrid", false);
					}
				}],
		items : [{
			xtype : "panel",
			layout : "column",
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
								},{
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
					},{
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

								}]
					}]
		},{
			xtype : "fieldset",
			title : "Invoice Address",
			collapsible : true,
			layout : "column",
			items:[{
				xtype : "panel",
				columnWidth : 1,
				layout : "column",
				hideBorders : false,
				items : [{
							xtype : "panel",
							title : "",
							columnWidth : 0.5,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "Name",
										anchor : "100%",
										allowBlank : true,
										maxLength : 200,
										id : "invoiceCustomerName",
										name : "invoiceCustomerName",
										tabIndex : 21
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.5,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "Short Title",
										anchor : "100%",
										allowBlank : true,
										maxLength : 100,
										id : "invoiceShortName",
										name : "invoiceShortName",
										tabIndex : 22
									}]
						}]
			},{
				xtype : "panel",
				columnWidth : 1,
				layout : "column",
				hideBorders : false,
				items : [{
							xtype : "panel",
							title : "",
							columnWidth : 0.33,
							layout : "form",
							items : [inationBox]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.33,
							layout : "form",
							items : [icityBox]
						},{
							xtype : "panel",
							title : "",
							columnWidth : 0.34,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "Zip Code",
										anchor : "100%",
										allowBlank : true,
										maxLength : 200,
										id : "invoiceCustomerPost",
										name : "invoiceCustomerPost",
										tabIndex : 25
									}]
						}]
			},{
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
								id : "invoiceCustomerAddress",
								name : "invoiceCustomerAddress"
							}]
				}]
		}, {
			xtype : "fieldset",
			title : "业务信息",
			collapsible : true,
			hidden : true,
			layout : "column",
			items : [{
				xtype : "panel",
				columnWidth : 0.75,
				layout : "column",
				hideBorders : false,
				items : [{
							xtype : "panel",
							title : "",
							columnWidth : 0.3,
							layout : "column",
							items : [{
										xtype : "panel",
										title : "",
										columnWidth : 0.60,
										layout : "form",
										items : [commisionTypeBox]
									}, {
										xtype : "panel",
										title : "",
										columnWidth : 0.40,
										layout : "form",
										labelWidth : 40,
										items : [{
													xtype : "numberfield",
													fieldLabel : "比例%",
													anchor : "100%",
													allowBlank : true,
													maxValue : 999.99,
													id : "commisionScale",
													name : "commisionScale",
													tabIndex : 25
												}]
									}, {
										xtype : "panel",
										title : "",
										columnWidth : 1,
										layout : "form",
										items : [custLvBox]
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.4,
							layout : "form",
							items : [{
								xtype : "combo",
								triggerAction : "all",
								fieldLabel : "配合程度",
								store : new Ext.data.SimpleStore({
											fields : ["id", "name"],
											data : [['', 'Choose'], ['0', '很好'],
													['1', '好'], ['2', '较好'],
													['3', '一般'], ['4', '较差'],
													['5', '差'], ['6', '很差']]
										}),
								valueField : "id",
								displayField : "name",
								mode : 'local',
								value : '',
								anchor : "96%",
								allowBlank : true,
								hiddenName : "cooperateLv",
								tabIndex : 29
							}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 1,
							layout : "form",
							items : [{
										xtype : "textarea",
										fieldLabel : "船贷信息",
										anchor : "98%",
										height : 40,
										allowBlank : true,
										maxLength : 200,
										id : "shipInfo",
										name : "shipInfo",
										tabIndex : 30
									}, {
										xtype : "textarea",
										fieldLabel : "文化背景",
										anchor : "98%",
										height : 20,
										allowBlank : true,
										maxLength : 100,
										id : "cultureBackground",
										name : "cultureBackground",
										tabIndex : 31
									}]
						}]
			}, {
				xtype : "fieldset",
				title : "客户相片",
				layout : "form",
				hidden : true,
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
								text : "上传图片",
								iconCls : "upload-icon",
								handler : showUploadCustPanel
							}, {
								text : "删除图片",
								iconCls : "upload-icon-del",
								handler : delPhoto
							}]
				}]
			}]
		}, {
			xtype : "fieldset",
			title : "唛头信息",
			collapsible : true,
			hidden : true,
			layout : "column",
			columnWidth : 1,
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.38,
						layout : "form",
						items : [{
									xtype : "textarea",
									fieldLabel : "正唛",
									anchor : "100%",
									height : 100,
									allowBlank : true,
									maxLength : 200,
									id : "customerZm",
									name : "customerZm",
									tabIndex : 40
								}, {
									xtype : "textarea",
									fieldLabel : "内盒唛",
									anchor : "100%",
									height : 100,
									allowBlank : true,
									maxLength : 200,
									id : "customerNm",
									name : "customerNm",
									tabIndex : 42
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.37,
						layout : "form",
						items : [{
									xtype : "textarea",
									fieldLabel : "侧唛",
									anchor : "96%",
									height : 100,
									allowBlank : true,
									maxLength : 200,
									id : "customerCm",
									name : "customerCm",
									tabIndex : 41
								}, {
									xtype : "textarea",
									fieldLabel : "中盒唛",
									anchor : "96%",
									height : 100,
									allowBlank : true,
									maxLength : 200,
									id : "customerZhm",
									name : "customerZhm",
									tabIndex : 43
								}]
					}, {
						xtype : "fieldset",
						title : "唛标",
						layout : "form",
						columnWidth : 0.25,
						items : [{
							xtype : "panel",
							title : "",
							buttonAlign : "center",
							html : '<div align="center" style="padding-left:50px;width: 150px; height: 150px;">'
									+ '<img src="common/images/zwtp.png" id="customerMbPath" name="customerMbPath"'
									+ 'onload="javascript:DrawImage(this,160,177)" onclick="showBigPicDiv(this)"/>'
									+ '</div>',
							fbar : [{
										text : "上传唛标",
										iconCls : "upload-icon",
										handler : showUploadMbPanel
									}, {
										text : "删除唛标",
										iconCls : "upload-icon-del",
										handler : delMb
									}]
						}]
					}]
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
					waitMsg : "图片上传中......",
					opAction : opAction,
					imgObj : $('customPhotoPath'),
					uploadType : "image",
					loadImgStream : false,
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
					waitMsg : "图片上传中......",
					opAction : opAction,
					imgObj : $('customerMbPath'),
					uploadType : "image",
					loadImgStream : false,
					uploadUrl : './uploadCustomerPhoto.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}
});