Ext.onReady(function() {
	var cbmNum = getDeNum("cbmPrecision");
	var cbmStr = getDeStr(cbmNum);
	var statusMap = {
		"0" : "<font color='green'>Non-reviewed</font>",
		"1" : "<font color='red'>Review without</font>",
		"2" : "<font color='blue'>reviewed</font>",
		"3" : "<font color='#10418C'>Need to be reviewed</font>",
		"9" : "<font color='green'>Not review</font>"
	}
	DWREngine.setAsync(false);
	var clauseMap;
	var curData;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotClause", "id", "clauseName", function(
					res) {
				clauseMap = res;
			});
	var payMap;
	baseDataUtil.getBaseDicDataMap("CotPayType", "id", "payName",
			function(res) {
				payMap = res;
			});
	var shipPortMap;
	baseDataUtil.getBaseDicDataMap("CotShipPort", "id", "shipPortNameEn",
			function(res) {
				shipPortMap = res;
			});
	var typeLv1Map;
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeEnName", function(
					res) {
				typeLv1Map = res;
			});
	DWREngine.setAsync(true);
	var form = new Ext.form.FormPanel({});

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "orderTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "orderStatus"
			}, {
				name : "customerShortName"
			}, {
				name : "custId"
			}, {
				name : "empsName"
			}, {
				name : "odNo"
			}, {
				name : "totalCount"
			}, {
				name : "totalContainer"
			}, {
				name : "totalCbm",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "trafficId"
			}, {
				name : "totalMoney"
			}, {
				name : "totalHsMoney"
			}, {
				name : "clauseTypeId"
			}, {
				name : "paTypeId"
			}, {
				name : 'typeLv1Id'
			}, {
				name : 'poNo'
			}, {
				name : 'orderLcDate',
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'sendTime',
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'orderLcDelay',
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'shortName'
			}, {
				name : 'allPinName'
			}, {
				name : 'chk'
			},{
				name:'currencyId'
			}, {
				name : 'taxTotalMoney'
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 200,
					orderStatus : 1
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Operation",
							dataIndex : "id",
							width : 170,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var mod = '<a href="javascript:windowopenMod('
										+ value + ',\'' + record.data.odNo
										+ '\')">Make Invoice</a>';
								var status = record.data.orderStatus;
								var orderNo = record.get("orderNo");
								var nbsp = "&nbsp &nbsp &nbsp";
								var invalidNo = "";
								if (orderNo != "") {
									invalidNo = '<a href="javascript:showInvalidWin('
											+ value
											+ ','
											+ status
											+ ',\''
											+ orderNo + '\')">Credit Note</a>';
									mod = '<a href="javascript:windowopenMod('
											+ value + ')">Info</a>';
								}
								// var del = '<a href="javascript:del(' + value
								// + ',' + status + ')">Credit Note</a>';
								return mod + nbsp + invalidNo;
							}
						}, {
							header : "Sales",
							hidden:true,
							dataIndex : "empsName",
							width : 50
						}, {
							header : "Department",
							width : 80,
							dataIndex : "typeLv1Id",
							renderer : function(value) {
								return typeLv1Map[value];
							}
						}, {
							header : "Invoice",
							dataIndex : "orderNo",
							width : 120,
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "W&C P/O.",
							dataIndex : "odNo",
							width : 120
						}, {
							header : "Client",
							dataIndex : "customerShortName",
							width : 100
						}, {
							header : "Client P/O",
							dataIndex : "poNo",
							width : 120
						}, {
							header : "Date",
							dataIndex : "orderTime",
							width : 80,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d-m-Y");
								else
									return value;
							}
						}, {
							header : "Shipment",
							dataIndex : "orderLcDate",
							width : 75,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d-m-Y");
								}
							}
						}, {
							header : "Delivery Date",
							dataIndex : "sendTime",
							width : 85,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								if (value != null) {
									var chk = record.data.chk;
									if (chk == 1) {
										return "<font color=red>"
												+ Ext.util.Format.date(
														new Date(value.year,
																value.month,
																value.day),
														"d-m-Y") + "</font>";
									} else {
										return Ext.util.Format
												.date(	new Date(value.year,
																value.month,
																value.day),
														"d-m-Y");
									}
								}
							}
						}, {
							header : "ETA",
							dataIndex : "orderLcDelay",
							width : 75,
							hidden : true,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d/m/Y");
								}
							}
						}, {
							header : "Supplier",
							dataIndex : "shortName",
							width : 100
						}, {
							header : "Product",
							dataIndex : "allPinName",
							width : 90
						}, {
							header : "Quantity",
							hidden : true,
							dataIndex : "totalCount",
							width : 100
						}, {
							header : "Cartons",
							hidden : true,
							dataIndex : "totalContainer",
							width : 80
						}, {
							header : "Amount",
							hidden : true,
							dataIndex : "totalMoney",
							width : 80,
							renderer : Ext.util.Format.numberRenderer("0,0.00"),
							summaryType : 'sum',
							summaryRenderer : Ext.util.Format
									.numberRenderer("0,0.00")
						}, {
							header : "Delivery Term",
							dataIndex : "clauseTypeId",
							width : 120,
							renderer : function(value) {
								return clauseMap[value]
							}
						}, {
							header : "Pay Term",
							dataIndex : "paTypeId",
							width : 90,
							renderer : function(value) {
								return payMap[value]
							}
						},  {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curData["" + value];
							}
						},{
							header : "INVOICE AMOUNT",
							dataIndex : "taxTotalMoney",
							width : 120,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var val=0;
								if (!value || value==0) {
									val=record.get('totalMoney');
								}else{
									val= value;
								}
								return val.toFixed('2');
								//return Ext.util.Format.number(val,"0,0.00");
							},
							sumAfterRenderer : true,
							summaryType : 'sum'
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 200,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|200|300|500',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 客户
	var customerBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		mode : 'remote',// 默认local
		autoLoad : false,// 默认自动加载
		pageSize : 5,
		selectOnFocus : true,
		sendMethod : "post",
		emptyText : 'Client',
		width : 120,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custId'
	});
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPerson',
				editable : true,
				valueField : "id",
				displayField : "empsName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 5,
				selectOnFocus : true,
				sendMethod : "post",
				width : 120,
				emptyText : "Sales",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'businessPerson'
			});

	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : "Currency",
				width : 80,
				isSearchField : true,
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				isSearchField : true,
				searchName : 'currencyId'
			});

	// 出货状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'All Status'], [1, 'Waitting'],
						[2, 'Complete Invoice']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				editable : false,
				value : 1,
				emptyText : "Status",
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				listWidth : 130,
				hiddenName : 'orderStatus',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'orderStatus'
			});
	// 大类
//	var typeLv1IdBox = new BindCombox({
//				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
//				cmpId : 'typeLv1Id',
//				emptyText : "Department",
//				editable : true,
//				valueField : "id",
//				displayField : "typeEnName",
//				pageSize : 10,
//				width : 120,
//				sendMethod : "post",
//				selectOnFocus : false,
//				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//				listWidth : 350,// 下
//				triggerAction : 'all',
//				isSearchField : true,
//				searchName : 'typeLv1IdFind'
//			});
	var typeLv1IdBox = {
		xtype : "textfield",
		emptyText : "Department",
		width : 95,
		isSearchField : true,
		searchName : 'typeLv1IdFind'
	}		
	// 出口公司
	var companyBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
				cmpId : 'companyId',
				emptyText : "Company",
				editable : true,
				valueField : "id",
				displayField : "companyShortName",
				pageSize : 10,
				width : 100,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'companyId'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-',companyBox, {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 100,
							format : "d-m-Y",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 100,
							format : "d-m-Y",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, customerBox, busiBox, typeLv1IdBox, curBox, shenBox,
						{
							xtype : "textfield",
							emptyText : "PI.NO",
							width : 90,
							isSearchField : true,
							searchName : 'odNo'
						}, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Invoice No.",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							hidden : true,
							handler : windowopenAdd,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}, {
							text : "Update",
							hidden : true,
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "Print",
							handler : showPrint,
							iconCls : "page_print",
							cls : "SYSOP_PRINT"
						}, '-', {
							text : "Credit Note",
							handler : showDel,
							iconCls : "gird_list"
						}

				]
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				id : "orderoutGrid",
				stripeRows : true,
				region : 'center',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : summary,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false,
					getRowClass : function(record, index) {
						// 未建立单号的出货单
						if (record.get("orderStatus") == 0) {
							return "x-grid-record-noShen";
						}
					}
				}
			});
	DWREngine.setAsync(true);

	var colorPnl = new Ext.Panel({
		layout : 'hbox',
		region : 'north',
		height : 28,
		frame : true,
		layoutConfig : {
			// padding : '5',
			// pack : 'center',
			align : 'middle'
		},
		items : [{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:darkgray">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 80,
			html : '<font color=blue>Waitting</font>'
		}, {
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:white">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 120,
			html : '<font color=blue>Complete Invoice</font>'
		}]
	})

	var viewport = new Ext.Viewport({
				layout : "border",
				items : [colorPnl, grid]
			})

	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"), record.data.odNo);
			});

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message",
					'Sorry, you do not have Authority! ！');
			return;
		}
		var list = sm.getSelections();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return false;
		} else if (list.length > 1) {
			Ext.MessageBox.alert("Message",
					"Sorry,you can select only one record!")
			return false;
		}
		var rec = list[0].data.orderNo;
		if (rec == '') {
			Ext.MessageBox.alert("Message",
					"Sorry,The Invoice do not have Invoice No.!")
			return false;
		}

		if (printWin == null) {
			printWin = new PrintWin({
						type : 'orderout'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0] - 200, po[1] + 25);
			var facData = [];
			DWREngine.setAsync(false);
			cotOrderOutService.getFactorysByMainId(list[0].data.id, function(
							res) {
						for (var p in res)
							facData.push(["" + p, res[p]]);
					});
			DWREngine.setAsync(true);
			printWin.facBox.setValue("");
			printWin.facBox.getStore().loadData(facData);
			printWin.show();
		} else {
			printWin.hide();
		}
	};

	// 查看作废单
	function showDel() {
		var url = "cotorderout.do?method=queryDel";
		var panel = parent.Ext.getCmp("maincontent");
		var iframe = Ext.getCmp("tab678");
		if (iframe != null) {
			panel.setActiveTab("tab678");
			iframe.setSrc(url);
		} else {
			var newpanel = panel.add({
						closable : true,
						xtype : 'iframepanel',
						id : "tab678",
						title : "Credit Note",
						frameConfig : {
							autoCreate : {
								id : "tab_678"
							}
						},
						loadMask : {
							msg : 'Data Loading...'
						},
						defaultSrc : url
					});
			panel.setActiveTab(newpanel);
		}
	}

});
// 获取单号
function getNewOrderNo() {
	var record = Ext.getCmp("orderoutGrid").getSelectionModel().getSelected();
	var id = record.data.id;
	cotOrderOutService.getOrderOutById(id, function(res) {
				var date = new Date(res.orderTime + "");
				var dt = date.getYear() + 1900 + "-" + (date.getMonth() + 1)
						+ "-" + date.getDate();
				cotSeqService.getOrderOutNo(res.custId, res.businessPerson, dt,
						function(ps) {
							$('invalidNo').value = ps;
						});
			});

}
// 生成作废单号
var invalidWin;
function showInvalidWin(invoiceId, status, orderNo) {
	invalidWin = new Ext.Window({
		title : "Credit Note",
		width : 400,
		height : 105,
		layout : "column",
		modal : true,
		formId : "invalidForm",
		buttonAlign : "center",
		monitorValid : true,
		frame : true,
		// padding:"10",
		fbar : [{
					text : "Save",
					formBind : true,
					handler : del.createDelegate(this, [invoiceId, status])
				}],
		items : [{
			xtype : "panel",
			layout : "form",
			columnWidth : 1,
			frame : true,
			items : [{
				xtype : "textfield",
				fieldLabel : "Credit Note",
				anchor : "100%",
				id : "invalidNo",
				blankText : "Please enter Credit Note!",
				name : "invalidNo",
				maxLength : 100,
				allowBlank : false,
				listeners : {
					"render" : function(obj) {
						var tip = new Ext.ToolTip({
							target : obj.getEl(),
							anchor : 'top',
							maxWidth : 160,
							minWidth : 160,
							html : 'Click the right button to automatically generate the W&C P/I!'
						});
					}
				}
			}]
		}, {
			xtype : "button",
			width : 20,
			text : "",
			cls : "SYSOP_ADD",
			iconCls : "cal",
			handler : getNewOrderNo,
			listeners : {
				"render" : function(obj) {
					var tip = new Ext.ToolTip({
						target : obj.getEl(),
						anchor : 'top',
						maxWidth : 160,
						minWidth : 160,
						html : 'Depending on the configuration automatically generated W&C P/I!'
					});
				}
			}
		}]
	});
	invalidWin.show();

}