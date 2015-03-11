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
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 200
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryDel"
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
						},{
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
							hidden:true,
							dataIndex : "totalCount",
							width : 100
						}, {
							header : "Cartons",
							hidden:true,
							dataIndex : "totalContainer",
							width : 80
						}, {
							header : "Amount",
							hidden:true,
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
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 200,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|200|300|500',
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
				emptyText : 'Sales',
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
				data : [[0, 'All Status'],[9, 'Credit'],[8, 'Invoice']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				editable : false,
				emptyText : "Status",
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width:100,
				listWidth : 130,
				hiddenName : 'orderStatus',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'orderStatus'
			});
			
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
				items : ['-', companyBox,{
							xtype : "datefield",
							emptyText : "Start Date",
							width : 100,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 100,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						},customerBox, busiBox, curBox,shenBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Invoice No.",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Print",
							handler : showPrint,
							iconCls : "page_print",
							cls : "SYSOP_PRINT"
						}
				]
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				id : "orderoutdelGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : summary,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	DWREngine.setAsync(true);
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			})

	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"))
			});

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType("cotorderout.do", "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message", 'Sorry, you do not have Authority!');
			return;
		}
		var list = sm.getSelections();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return false;
		} else if (list.length > 1) {
			Ext.MessageBox.alert("Message", "You can only select a record!")
			return false;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'orderoutdel'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0] - 200, po[1] + 25);
//			var facData = [];
//			DWREngine.setAsync(false);
//			cotOrderOutService.getFactorysByMainId(list[0].data.id, function(
//							res) {
//						for (var p in res)
//							facData.push(["" + p, res[p]]);
//					});
//			DWREngine.setAsync(true);
//			printWin.facBox.setValue("");
//			printWin.facBox.getStore().loadData(facData);
			printWin.show();
		} else {
			printWin.hide();
		}
	};

	// 查看作废单
	function showDel() {
		var url="cotorderout.do?method=queryDel";
		var panel = parent.Ext.getCmp("maincontent");
		var iframe = Ext.getCmp("tab_orderout_del");
		if (iframe != null) {
			panel.setActiveTab("tab_orderout_del");
			iframe.setSrc(url);
		} else {
			var newpanel = panel.add({
							closable : true,
							xtype : 'iframepanel',
							id : "taborderout_del",
							title : "Invalid",
							frameConfig : {
								autoCreate : {
									id : "tab_orderout_del"
								}
							},
							loadMask : {
								msg : 'Loading...'
							},
							defaultSrc : url
						});
				panel.setActiveTab(newpanel);
		}
	}
});
