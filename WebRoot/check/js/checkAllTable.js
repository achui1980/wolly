var checkStatusMap = {
	"0" : "Non-reviewed",
	"1" : "Review without",
	"2" : "reviewed ",
	"3" : "Need to be reviewed ",
	"9" : "Not review"
}
var currencyMap;
baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(res) {
			currencyMap = res;
		});
var clauseMap;
baseDataUtil.getBaseDicDataMap("CotClause", "id", "clauseName", function(res) {
			clauseMap = res;
		});
var payTypeMap;
baseDataUtil.getBaseDicDataMap("CotPayType", "id", "payName", function(res) {
			payTypeMap = res;

		});
var facMap;
baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(res) {
			facMap = res;
		});
var empsMap;
baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsId", function(res) {
			empsMap = res;
		});
var targetPortMap;
baseDataUtil.getBaseDicDataMap("CotTargetPort", "id", "targetPortEnName",
		function(res) {
			targetPortMap = res;
		});
var shipPortMap;
baseDataUtil.getBaseDicDataMap("CotShipPort", "id", "shipPortNameEn", function(
				res) {
			shipPortMap = res;
		});
var trafficMap;
baseDataUtil.getBaseDicDataMap("CotTrafficType", "id", "trafficNameEn",
		function(res) {
			trafficMap = res;
		});
var custMap;
baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName",
		function(res) {
			custMap = res;
		});
var form = null;
Ext.onReady(function() {
	form = new Ext.form.FormPanel({
		title : "",
		labelWidth : 60,
		labelAlign : "right",
		layout : "column",
		region : "north",
		height : 45,
		padding : "5px",
		keys : [{
					key : Ext.EventObject.ENTER,
					fn : query
				}],
		hideBorders : true,
		frame : true,
		items : [{
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.12,
					hideBorders : false,
					labelWidth : 40,
					items : [{
								xtype : "textfield",
								fieldLabel : "NO.",
								id : "opNo",
								name : "opNo",
								anchor : "100%"
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.12,
					items : [{
								xtype : "combo",
								transform : "status",
								triggerAction : 'all',
								lazyRender : true,
								editable : false,
								mode : 'local',
								hiddenName : "status",
								fieldLabel : "Status",
								anchor : "100%"
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.15,
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
						pageSize : 10,
						mode : "remote",
						valueField : "id",
						displayField : "customerShortName",
						listWidth : 350,
						emptyText : "Please select",
						triggerAction : "all",
						hiddenName : "custId",
						minChars : 1,
						fieldLabel : "Customers",
						anchor : "100%"
					}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.13,
					layout : "form",
					items : [{
						xtype : "combo",
						store : new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
								method : "POST",
								url : "servlet/DataSelvert?tbname=CotEmps&key=empsId&flag=filter"
							}),
							reader : new Ext.data.JsonReader({
										root : "data",
										totalProperty : "totalCount",
										idProperty : "id"
									}, [{
												name : "id"
											}, {
												name : "empsId"
											}])
						}),
						valueField : "id",
						displayField : "empsId",
						listWidth : 350,
						emptyText : "Please select",
						pageSize : 10,
						mode : "remote",
						triggerAction : "all",
						minChars : 1,
						hiddenName : "businessPerson",
						fieldLabel : "Sales",
						anchor : "100%"
					}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.12,
					layout : "form",
					items : [{
								xtype : "datefield",
								id : "startTime",
								name : "startTime",
								fieldLabel : "OP time",
								anchor : "100%"
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.1,
					labelWidth : 10,
					items : [{
								xtype : "datefield",
								id : "endTime",
								name : "endTime",
								fieldLabel : "-",
								anchor : "95%",
								labelSeparator : " "
							}]
				}, {
					layout : 'table',
					columnWidth : .2,
					layoutConfig : {
						columns : 2
					},
					items : [{
								xtype : 'button',
								text : "Search",
								width : 65,
								iconCls : "page_sel",
								handler : query
							}, {
								xtype : 'button',
								text : "Reset",
								iconCls : "page_reset",
								style : {
									marginLeft : '10px'
								},
								width : 65,
								handler : function() {
									form.getForm().reset()
								}
							}]
				}]
	});
	var frameName = null;
	var tab = new Ext.TabPanel({
				activeTab : -1,
				region : "center",
				deferredRender : false,
				defaults : {
					autoScroll : true
				},
				defaultType : "iframepanel",
				id : "maincontent",
				items : [{
					title : "Sample delivery record",
					id : 'givenDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'givenInfo'
						}
					},
					listeners : {
						"activate" : loadInfo.createDelegate(this, [
										'givenInfo',
										'cotcheck.do?method=queryGivenDetail'])
					}
				}, {
					title : "Price Records",
					id : 'priceDiv',
					defaultSrc : '',
					frameConfig : {
						autoCreate : {
							id : 'priceInfo'
						}
					},
					listeners : {
						"activate" : loadInfo.createDelegate(this, [
										'priceInfo',
										'cotcheck.do?method=queryPriceDetail'])
					}
				}, {

					title : "Export contract",
					id : 'orderDiv',
					defaultSrc : "",
					frameConfig : {
						autoCreate : {
							id : 'orderInfo'
						}
					},
					listeners : {
						"activate" : loadInfo.createDelegate(this, [
										'orderInfo',
										'cotcheck.do?method=queryOrderDetail'])
					}
				}, {

					title : "Production contract",
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
												'cotcheck.do?method=queryOrderFacdetail'])
					}

				}, {

					title : "Shipping records",
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
												'cotcheck.do?method=queryOrderOutdetail'])
					}

				}]
			});
	var viewport = new Ext.Viewport({
				layout : "border",
				items : [form, tab]
			});
	function loadInfo(modelName, sel) {

		var frame = window.frames[modelName]
		frameName = modelName;
		frame.location.href = sel;
	}
	function query() {
		var activeTab = tab.getActiveTab();
		if (activeTab == null) {
			tab.setActiveTab(1)
		} else {
			var frame = window.frames[frameName];
			var checkds = frame.grid.getStore()
			checkds.reload({
						params : {
							start : 0,
							limit : 15
						}
					});
		}
	}

});