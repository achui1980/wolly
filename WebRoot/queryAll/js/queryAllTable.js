var form = null;
DWREngine.setAsync(false);
var currencyMap;
baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(res) {
			currencyMap = res;
		});
var facMap;
baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(res) {
			facMap = res;
		});
var priMap;
baseDataUtil.getBaseDicDataMap("CotCustContact", "id", "contactPerson",
		function(res) {
			priMap = res;
		});
DWREngine.setAsync(true);

Ext.onReady(function() {
	
	// 大类
	var typeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "Dep. Of PRODUCT",
				editable : true,
				valueField : "id",
				displayField : "typeEnName",
				emptyText : 'Choose',
				mode : 'remote',
				pageSize : 10,
				anchor:'100%',
				tabIndex:10,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 中类
	var type2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Group",
				editable : true,
				tabIndex:11,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				anchor:'100%',
				mode : 'remote',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	
	form = new Ext.form.FormPanel({
		title : "Query",
		labelWidth : 60,
		labelAlign : "right",
		layout : "column",
		region : "north",
		height : 190,
		keys : [{
					key : Ext.EventObject.ENTER,
					fn : query
				}],
		collapsible : true,
		buttonAlign : "center",
		padding : "5px",
		hideBorders : true,
		frame : true,
		fbar : [{
					text : "Search",
					width : 65,
					iconCls : "page_sel",
					handler : query
				}, {
					text : "Reset",
					width : 65,
					iconCls : "page_reset",
					handler : function() {
						form.getForm().reset();
					}
				}],
		items : [{
			xtype : "panel",
			title : "",
			columnWidth : 0.2,
			layout : "form",
			labelWidth : 90,
			hideBorders : true,
			items : [{
						xtype : "textfield",
						fieldLabel : "No.",
						id : "opNo",
						name : "opNo",
						anchor : "100%"
					}, {
						xtype : "textfield",
						fieldLabel : "Art No.",
						id : "eleId",
						name : "eleId",
						anchor : "100%"
					}, typeBox, {
						xtype : "panel",
						title : "",
						layout : "column",
						labelSeparator : " ",
						hideBorders : true,
						items : [{
									xtype : "panel",
									title : "",
									layout : "form",
									columnWidth : 0.65,
									labelWidth : 90,
									hideBorders : false,
									items : [{
												xtype : "textfield",
												fieldLabel : "Product_L",
												id : "boxLS",
												name : "boxLS",
												anchor : "100%"
											}]
								}, {
									xtype : "panel",
									layoutConfig : {
										labelSeparator : " "
									},
									title : "",
									layout : "form",
									columnWidth : 0.35,
									labelWidth : 10,
									items : [{
												xtype : "textfield",
												fieldLabel : "-",
												id : "boxLE",
												name : "boxLE",
												anchor : "100%"
											}]
								}]
					}]
		}, {
			xtype : "panel",
			title : "",
			layout : "form",
			columnWidth : 0.2,
			labelWidth : 50,
			hideBorders : true,
			items : [{
				xtype : "combo",
				store : new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
						method : "POST",
						url : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do"
					}),
					reader : new Ext.data.JsonReader({
								root : "data",
								totalProperty : "totalCount",
								idProperty : "id"
							}, [{
										name : "id"
									}, {
										name : "customerShortName"
									}])
				}),
				forceSelection : true,
				selectOnFocus : true,

				editable : true,
				pageSize : 10,
				mode : "remote",
				valueField : "id",
				displayField : "customerShortName",
				listWidth : 350,
				emptyText : "Choose",
				triggerAction : "all",
				hiddenName : "custId",
				id : "custIdCP",
				minChars : 1,
				fieldLabel : "Client",
				anchor : "100%"
			}, {
				xtype : "textfield",
				fieldLabel : "Cust.No.",
				id : "custNo",
				name : "custNo",
				anchor : "100%"
			}, {
				xtype : "textfield",
				fieldLabel : "Design",
				id : "eleCol",
				name : "eleCol",
				anchor : "100%"
			}, {
				xtype : "panel",
				title : "",
				layout : "column",
				hideBorders : true,
				items : [{
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.6,
							labelWidth : 50,
							items : [{
										xtype : "textfield",
										fieldLabel : "Product_W",
										id : "boxWS",
										name : "boxWS",
										anchor : "100%"
									}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.4,
							labelWidth : 10,
							items : [{
										xtype : "textfield",
										fieldLabel : "-",
										anchor : "100%",
										id : "boxWE",
										name : "boxWE",
										labelSeparator : " "
									}]
						}]
			}]
		}, {
			xtype : "panel",
			title : "",
			layout : "form",
			columnWidth : 0.2,
			labelWidth : 50,
			hideBorders : true,
			items : [{
				xtype : "combo",
				store : new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
						method : "POST",
						url : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter"
					}),
					reader : new Ext.data.JsonReader({
								root : "data",
								totalProperty : "totalCount",
								idProperty : "id"
							}, [{
										name : "id"
									}, {
										name : "empsName"
									}])
				}),
				valueField : "id",
				displayField : "empsName",
				listWidth : 350,
				emptyText : "Choose",
				pageSize : 10,
				mode : "remote",
				triggerAction : "all",
				minChars : 1,
				hiddenName : "businessPerson",
				id : "businessPersonCP",
				fieldLabel : "Sales",
				anchor : "100%"
			}, {
				xtype : "textfield",
				fieldLabel : "Material",
				id : "eleName",
				name : "eleName",
				anchor : "100%"
			}, {
				xtype : "textfield",
				fieldLabel : "Level",
				id : "eleGrade",
				name : "eleGrade",
				anchor : "100%"
			}, {
				xtype : "panel",
				title : "",
				layout : "column",
				hideBorders : true,
				items : [{
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.6,
							items : [{
										xtype : "textfield",
										fieldLabel : "Product_H",
										id : "boxHS",
										name : "boxHS",
										anchor : "100%"
									}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.4,
							labelWidth : 10,
							items : [{
										xtype : "textfield",
										fieldLabel : "-",
										anchor : "100%",
										id : "boxHE",
										name : "boxHE",
										labelSeparator : " "
									}]
						}]
			}]
		}, {
			xtype : "panel",
			title : "",
			layout : "form",
			columnWidth : 0.2,
			items : [{
						xtype : "datefield",
						fieldLabel : "Op Date",
						id : "startTime",
						name : "startTime",
						format : 'Y-m-d',
						anchor : "100%"
					}, {
						xtype : "textfield",
						fieldLabel : "Description",
						id : "eleNameEn",
						name : "eleNameEn",
						anchor : "100%"
					},type2Box, {
						xtype : "textfield",
						fieldLabel : "Barcode",
						id : "barcode",
						name : "barcode",
						anchor : "100%"

					}]
		}, {
			xtype : "panel",
			title : "",
			layout : "form",
			columnWidth : 0.2,
			items : [{
						xtype : "datefield",
						fieldLabel : "--------",
						id : "endTime",
						name : "endTime",
						anchor : "100%",
						format : 'Y-m-d',
						labelSeparator : " "
					}, {
						xtype : "combo",
						store : new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
								method : "POST",
								url : "servlet/DataSelvert?tbname=CotFactory&key=shortName"
							}),
							reader : new Ext.data.JsonReader({
										root : "data",
										totalProperty : "totalCount",
										idProperty : "id"
									}, [{
												name : "id"
											}, {
												name : "shortName"
											}])
						}),
						pageSize : 10,
						mode : "remote",
						valueField : "id",
						displayField : "shortName",
						listWidth : 350,
						emptyText : "Choose",
						triggerAction : "all",
						hiddenName : "factoryId",
						id : "factoryIdCP",
						minChars : 1,
						fieldLabel : "Supplier",
						anchor : "100%"
					}, {
						xtype : "textfield",
						id : "eleForPerson",
						name : "eleForPerson",
						fieldLabel : "Designer",
						anchor : "100%"
					}, {
						xtype : "textfield",
						fieldLabel : "Size(cm)",
						id : "eleSizeDesc",
						name : "eleSizeDesc",
						anchor : "100%"

					}]
		}]
	});
	var frameName = null;
	var tabs = new Ext.TabPanel({
				activeTab : -1,
				region : "center",
				deferredRender : false,
				border : false,
				defaults : {
					autoScroll : true
				},
				defaultType : "iframepanel",
				id : "maincontent",
				items : [{
					title : "Cust.No.",
					id : 'sampleDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'sampleinfo'
						}
					},
					listeners : {
						"activate" : loadInfo
								.createDelegate(
										this,
										['sampleinfo',
												'cotqueryall.do?method=querySampledetail'])
					}
				}
//				, {
//					title : "送样记录",
//					id : 'givenDiv',
//					defaultSrc : "",
//					frameConfig : {
//						autoCreate : {
//							id : 'givenInfo'
//						}
//					},
//					listeners : {
//						"activate" : loadInfo
//								.createDelegate(
//										this,
//										['givenInfo',
//												'cotqueryall.do?method=queryGivenDetail'])
//					}
//				}
//				, {
//					title : "征样记录",
//					id : 'signDiv',
//					defaultSrc : "",
//					frameConfig : {
//						autoCreate : {
//							id : 'signInfo'
//						}
//					},
//					listeners : {
//						"activate" : loadInfo
//								.createDelegate(
//										this,
//										['signInfo',
//												'cotqueryall.do?method=querySignDetail'])
//					}
//				}
				, {
					title : "Offer",
					id : 'priceDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'priceInfo'
						}
					},
					listeners : {
						"activate" : loadInfo
								.createDelegate(
										this,
										['priceInfo',
												'cotqueryall.do?method=queryPriceDetail'])
					}
				}, {
					title : "Order",
					id : 'orderDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'orderInfo'
						}
					},
					listeners : {
						"activate" : loadInfo
								.createDelegate(
										this,
										['orderInfo',
												'cotqueryall.do?method=queryOrderDetail'])
					}
				}, {
					title : "Purchase",
					id : 'orderfacDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'orderfacinfo'
						}
					},
					listeners : {
						"activate" : loadInfo
								.createDelegate(
										this,
										['orderfacinfo',
												'cotqueryall.do?method=queryOrderFacdetail'])
					}
				}, {
					title : "Shipment",
					id : 'orderoutDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'orderoutinfo'
						}
					},
					listeners : {
						"activate" : loadInfo
								.createDelegate(
										this,
										['orderoutinfo',
												'cotqueryall.do?method=queryOrderOutdetail'])
					}
				}, {
					title : "Quotations from Suppliers",
					id : 'facRecDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'facRecinfo'
						}
					},
					listeners : {
						"activate" : loadInfo
								.createDelegate(
										this,
										['facRecinfo',
												'cotqueryall.do?method=queryFacRecdetail'])
					}
				}]
			});
	var viewport = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [form, tabs]
			});
	function loadInfo(modelName, sel) {

		var frame = window.frames[modelName]
		frameName = modelName;
		frame.location.href = sel;
	}
	function query() {
		var activeTab = tabs.getActiveTab();
		if (activeTab == null) {
			tabs.setActiveTab(2)
		} else {
			var frame = window.frames[frameName];
			var checkds = frame.grid.getStore();
			checkds.reload();
		}
	};
	// 获取焦点时自动展开
//	var eleTypeidLv1CP = Ext.getCmp("eleTypeidLv1CP");
//	eleTypeidLv1CP.on("focus", function(sel) {
//				sel.onTriggerClick();
//			});
	var custIdCP = Ext.getCmp("custIdCP");
	custIdCP.on("focus", function(sel) {
				sel.onTriggerClick();
			});
	var businessPersonCP = Ext.getCmp("businessPersonCP");
	businessPersonCP.on("focus", function(sel) {
				sel.onTriggerClick();
			});
//	var eleTypeidLv2CP = Ext.getCmp("eleTypeidLv2CP");
//	eleTypeidLv2CP.on("focus", function(sel) {
//				sel.onTriggerClick();
//			});
	var factoryIdCP = Ext.getCmp("factoryIdCP");
	factoryIdCP.on("focus", function(sel) {
				sel.onTriggerClick();
			});
})