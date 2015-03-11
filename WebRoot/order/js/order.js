Ext.onReady(function() {
	var deNum = getDeNum("orderPricePrecision");
	var cbmNum = getDeNum("cbmPrecision");
	var strNum = getDeStr(deNum);
	var cbmStr = getDeStr(cbmNum);
	var curData;
	var payData;
	var clauseData;
	var factoryData;
	var custMap;
	var typeLv1Map;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	// 加载付款方式表缓存
	baseDataUtil.getBaseDicDataMap("CotPayType", "id", "payName",
			function(res) {
				payData = res;
			});
	// 加载条款类型表缓存
	baseDataUtil.getBaseDicDataMap("CotClause", "id", "clauseName", function(
					res) {
				clauseData = res;
			});

	// 加载厂家表
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				factoryData = res;
			});
	// 加载客户表
	baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName",
			function(res) {
				factoryData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeEnName", function(
					res) {
				typeLv1Map = res;
			});
	DWREngine.setAsync(true);

	/** ******EXT创建grid步骤******** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "sendTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "customerShortName"
			}, {
				name : "orderNo"
			}, {
				name : "poNo"
			}, {
				name : "empsName"
			}, {
				name : "currencyId"
			}, {
				name : 'clauseTypeId'
			}, {
				name : "custId"
			}, {
				name : "canOut"
			}, {
				name : "factoryId"
			}, {
				name : "orderStatus"
			}, {
				name : "payTypeId"
			}, {
				name : "totalCount"
			}, {
				name : 'orderLcDelay',
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'orderLcDate',
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'allPinName'
			}, {
				name : "totalContainer"
			}, {
				name : "totalCBM",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "totalGross",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "totalMoney"
			}, {
				name : "ftm"
			}, {
				name : 'typeLv1Id'
			},{
				name:'chk'
			},{
				name:'newRemark'
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 300,
					canOut : 0,
					con : $('configValue').value
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorder.do?method=query"
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
					header : "编号",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "Operation",
					dataIndex : "id",
					width : 140,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var mod = '<a href="javascript:windowopenMod(' + value
								+ ')">Update</a>';
						var status = record.data.orderStatus;
						var canOut = record.data.canOut;
						var nbsp = "&nbsp &nbsp";
						var del = '<a href="javascript:del(' + value + ','
								+ status + ')">Delete</a>';
						//								var rpt = downRptFile(record.get("orderNo"),"order");
						//										+ '\',\'order\')">Report</a>';
						var rpt = "";
						var po = '<a href="javascript:openFac(' + value
								+ ')">PO</a>';

						var str = mod + nbsp + del;

						if (status == 2) {
							str += nbsp + rpt + nbsp + po;
						}
						return str;
						// if (canOut == 1 || canOut == 2) {
						// var invoice = '<a
						// href="javascript:createInvoice('
						// + value + ')">Invoice</a>';
						// return str += nbsp + invoice;
						// } else {
						// return str;
						// }
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
					header : "W&C P/I.",
					dataIndex : "orderNo",
					width : 120

				}, {
					header : "Client P/O",
					dataIndex : "poNo",
					width : 90
				}, {
					header : "Client",
					dataIndex : "custId",
					width : 90,
					renderer : function(value) {
						return factoryData["" + value];
					}
				}, {
					header : "Supplier",
					dataIndex : "factoryId",
					width : 60,
					hidden : true,
					renderer : function(value) {
						return factoryData["" + value];
					}
				}, {
					header : "Date",
					dataIndex : "orderTime",
					width : 80,
					hidden : true,
					renderer : function(value) {
						if (value != null) {
							return Ext.util.Format.date(new Date(value.time),
									"d-m-Y");
						}
					},
					summaryRenderer : function(v, params, data) {
						return "Total：";
					}
				}, {
					header : "Product",
					dataIndex : "allPinName",
					width : 90
				}, {
					header : "Amount",
					dataIndex : "totalMoney",
					renderer : Ext.util.Format.numberRenderer("0,0.00"),
					summaryType : 'sum',
					summaryRenderer : Ext.util.Format.numberRenderer("0,0.00"),
					width : 80,
					hidden : getPopedomByOpType("cotorder.do", "SELPIPRICE") == 0
							? true
							: false
				}, {
					header : "PO Amount",
					dataIndex : "ftm",
					hidden : getPopedomByOpType("cotorder.do", "SELPRI") == 0
							? true
							: false,
					renderer : Ext.util.Format.numberRenderer("0,0.00"),
					hidden : getPopedomByOpType("cotorder.do", "SELPIPRICE") == 0
							? true
							: false,
					summaryType : 'sum',
					summaryRenderer : Ext.util.Format.numberRenderer("0,0.00"),
					width : 100
				}, {
					header : "Currency",
					dataIndex : "currencyId",
					width : 60,
					hidden : getPopedomByOpType("cotorder.do", "SELPIPRICE") == 0
							? true
							: false,
					renderer : function(value) {
						return curData["" + value];
					}
				}, {
					header : "Shipment",
					dataIndex : "orderLcDate",
					width : 75,
					renderer : function(value) {
						if (value != null) {
							return Ext.util.Format.date(new Date(value.year,
											value.month, value.day), "d-m-Y");
						}
					}
				}, {
					header : "Delivery Date",
					dataIndex : "sendTime",
					width : 85,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (value != null) {
							var chk = record.data.chk;
							if (chk == 1) {
								return "<font color=red>"
										+ Ext.util.Format
												.date(	new Date(value.year,
																value.month,
																value.day),
														"d-m-Y") + "</font>";
							} else {
								return Ext.util.Format.date(new Date(
												value.year, value.month,
												value.day), "d-m-Y");
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
							return Ext.util.Format.date(new Date(value.year,
											value.month, value.day), "d/m/Y");
						}
					}
				}, {
					header : "Del.Term",
					dataIndex : "clauseTypeId",
					width : 65,
					renderer : function(value) {
						return clauseData["" + value];
					}
				}, {
					header : "Pay.Term",
					dataIndex : "payTypeId",
					width : 65,
					renderer : function(value) {
						return payData["" + value];
					}
				},{
					header : "Remark",
					dataIndex : "newRemark",
					width : 200,
					renderer:function(value){
						return '<a href="#">'+value+'</a>'
					}
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 300,
				store : ds,
				displayInfo : true,
				// //displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|300|500',
				// emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 审核状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'All'], [1, 'Waiting & Approval'], [2, 'Approved']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				editable : false,
				//value : 1,
				emptyText : "Status",
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				listWidth : 200,
				hiddenName : 'orderStatus',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'orderStatus'
			});

	// 出货状态
	var chuStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'Running'], [2, 'Shipped']]
			});
	var chuBox = new Ext.form.ComboBox({
				name : 'canOut',
				editable : false,
				emptyText : "Invoice Status",
				store : chuStore,
				value : 0,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				hiddenName : 'canOut',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'canOut'
			});

	// 客户
	var customerBox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		emptyText : "Client",
		editable : true,
		isSearchField : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 5,
		selectOnFocus : true,
		sendMethod : "post",
		width : 100,
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
				emptyText : "Sales",
				width : 90,
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				selectOnFocus : true,
				sendMethod : "post",
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
	//大类
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
				width : 90,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'companyId'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				autoScroll : true,
				enableOverflow:false,
				items : ['-',companyBox, {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 90,
							format : "d-m-Y",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 90,
							format : "d-m-Y",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						},customerBox, busiBox, typeLv1IdBox, curBox, shenBox,
						chuBox, {
							xtype : "textfield",
							emptyText : "Client P/O",
							width : 95,
							isSearchField : true,
							searchName : 'poNo'
						},{
							xtype : "datefield",
							emptyText : "Start app.Date",
							width : 90,
							format : "d-m-Y",
							id : 'startCheckTime',
							vtype : 'daterange',
							endDateField : 'endCheckTime',
							isSearchField : true,
							searchName : 'startCheckTime'
						}, {
							xtype : "datefield",
							emptyText : "End app.Date",
							width : 90,
							format : "d-m-Y",
							id : 'endCheckTime',
							vtype : 'daterange',
							startDateField : 'startCheckTime',
							isSearchField : true,
							searchName : 'endCheckTime'
						}, {
							xtype : 'searchcombo',
							width : 95,
							emptyText : "W&C P/I",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}],
				listeners:{
					render:function(){
						var tbar2 = new Ext.Toolbar({
							items:[ '->', {
									text : "Create",
									handler : windowopenAdd,
									iconCls : "page_add",
									cls : "SYSOP_ADD"
								}, '-', {
									text : "Update",
									handler : windowopenMod
											.createDelegate(this, [null]),
									iconCls : "page_mod",
									cls : "SYSOP_MOD"
								}, '-', {
									text : "Delete",
									handler : deleteBatch,
									iconCls : "page_del",
									cls : "SYSOP_DEL"
								}, '-', {
									text : "Print",
									handler : showPrint,
									iconCls : "page_print",
									cls : "SYSOP_PRINT"
								}]
						})
						tbar2.render(grid.tbar);
					}
				}
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				id : "orderGrid",
				stripeRows : true,
				region : 'center',
				bodyStyle : 'width:100%',
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
						// 审核不通过
						// if (record.get("orderStatus") == 1) {
						// return "x-grid-record-red";
						// }
						// 审核通过
						// if (record.get("orderStatus") == 2) {
						// if (record.get("canOut") == 1) {
						// return "x-grid-record-canOut";
						// } else {
						// return "x-grid-record-yellow";
						// }
						// }
						// 请求审核
						if (record.get("orderStatus") == 3) {
							return "x-grid-record-qing";
						}
						// 未审核
						if (record.get("orderStatus") == 0) {
							return "x-grid-record-noShen";
						}

					}
				}
			});

	var colorPnl = new Ext.Panel({
		layout : 'hbox',
		region : 'north',
		height : 28,
		frame : true,
		layoutConfig : {
			//padding : '5',
			//pack : 'center',
			align : 'middle'
		},
		items : [{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:darkgray">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 80,
			html : '<font color=blue>In Progress</font>'
		}, {
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:khaki">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 120,
			html : '<font color=blue>Waiting Approval</font>'
		}, {
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:white">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 80,
			html : '<font color=blue>Complete</font>'
		}]
	})

	// 构造
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [colorPnl, grid]
			});
	viewport.doLayout();

	// 右键菜单
	// var rightMenu = new Ext.menu.Menu({
	// id : "rightMenu",
	// items : [{
	// text : "P.O",
	// handler : orderToFac
	// }]
	// });
	// grid.on("rowcontextmenu", function(client, rowIndex, e) {
	// e.preventDefault();
	// rightMenu.showAt(e.getXY());
	// });

	// 表格双击
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});
	grid.on('cellclick', function(grid,  rowIndex,  columnIndex, e ) {
		var record = grid.getStore().getAt(rowIndex);
		if(columnIndex == 19){
			var win = new Ext.Window({
				title:'Remark',
				closeAction:'close',
				layout:'fit',
				width:500,
				height:400,
				items:[new OrderRemarkGrid({
					orderId:record.id,
					parentStore:grid.getStore()
				})]
			});
			win.show();
		}//remark列
	});

	// 批量删除 
	function deleteBatch() {
		var list = Ext.getCmp("orderGrid").getSelectionModel().getSelections();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", "Please select records");
			return;
		}
		var ary = new Array();
		Ext.each(list, function(item) {
					var status = item.data.orderStatus;
					if (status == 2 && loginEmpId != "admin") {
					} else {
						ary.push(item.id);
					}
				});
		if (ary.length == 0) {
			Ext.MessageBox
					.alert('Message',
							"You choose to order have been approved, can not be deleted!");
			return;
		}

		Ext.MessageBox
				.confirm(
						'Message',
						'You sure to delete the selected order (for approval and can not be deleted)?',
						function(btn) {
							if (btn == 'yes') {
								cotOrderService.deleteOrders(ary,
										function(res) {
											if (res != null) {
												var str = "";
												if (res[0] > 0) {
													str += "["
															+ res[0]
															+ "]nums has P.O!<br/>";
												}
												if (res[1] > 0) {
													str += "有["
															+ res[1]
															+ "]条订单已生成应收帐款!<br/>";
												}
												if (res[2] > 0) {
													str += "["
															+ res[2]
															+ "]条nums has Invoice!<br/>";
												}
												if (res[3] > 0) {
													str += "有["
															+ res[3]
															+ "]条订单已做配件采购!<br/>";
												}
												if (res[4] > 0) {
													str += "有["
															+ res[4]
															+ "]条订单已做包材采购!<br/>";
												}
												if (str != "") {
													if (res[5] > 0) {
														reloadGrid("orderGrid");
														Ext.MessageBox.alert(
																'Message',
																"Order to delete some unsuccessful:<br/>"
																		+ str);
													} else {
														Ext.MessageBox.alert(
																'Message',
																"You can not delete the selected orders:<br/>"
																		+ str);
													}
												} else {
													reloadGrid("orderGrid");
													Ext.MessageBox
															.alert('Message',
																	"Deleted successfully!");
												}
											}
										});
							}
						});

	}

	// 新增
	function windowopenAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0)// 没有添加权限
		{
			Ext.MessageBox
					.alert("Message", "You do not have permission to add");
			return;
		}
		openFullWindow('cotorder.do?method=addOrder');
	}

	// 显示订单采购层
	var orderSelPnl;
	function showOrderToFacDiv(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message",
					'You do not have permission to print！');
			return;
		}
		var ids = getIds();
		if (ids.length != 1) {
			Ext.MessageBox.alert("Message", 'Please check a record!');
			return;
		}
		var list = sm.getSelections();
		var orderStatus = list[0].data.orderStatus;
		if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
			Ext.MessageBox.alert("Message",
					'Sorry, the order has not approved, can not purchase!');
			return;
		}
		if (orderSelPnl == null) {
			if ($("orderSelPnlDiv") == null) {
				Ext.DomHelper.append(document.body, {
					html : '<div id="orderSelPnlDiv" style="position:absolute;z-index:9500;"></div>'
				}, true);
			}
			orderSelPnl = new Ext.Panel({
						title : "Purchases",
						buttonAlign : 'right', // 按钮居右显示
						labelAlign : "right",
						layout : 'fit',
						width : 300,
						autoHeight : true,
						frame : true,
						tools : [{
									handler : function(event, toolEl, p) {
										p.hide();
									}
								}],
						items : [{
									xtype : 'fieldset',
									// title : '分类',
									autoHeight : true,
									layout : 'form',
									labelWidth : 80, // 标签宽度
									items : [{
												xtype : 'radiogroup',
												fieldLabel : "",
												hideLabel : true,
												items : [{
															boxLabel : 'Purchases P.O',
															inputValue : 0,
															name : 'showHeader',
															id : "product",
															checked : true

														}
												// , {
												// boxLabel : '配件采购',
												// id : "fitting",
												// inputValue : 1,
												// name : 'showHeader'
												// }, {
												// boxLabel : '包材采购',
												// id : "packing",
												// inputValue : 2,
												// name : 'showHeader'
												// }
												]
											}]

								}],
						buttonAlign : 'center',
						buttons : [{
									text : 'OK',
									scope : this,
									width : 65,
									handler : okToFac,
									iconCls : "gird_save",
									handler : okToFac
								}, {
									text : 'cancel',
									scope : this,
									width : 65,
									iconCls : "page_cancel",
									handler : function() {
										orderSelPnl.hide();
									}
								}]

					});

			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("orderSelPnlDiv").setLeftTop(left - 250, top + 25);
			orderSelPnl.render($("orderSelPnlDiv"));
			var proxy1 = new Ext.dd.DDProxy("orderSelPnlDiv");
		} else {
			if (!orderSelPnl.isVisible()) {
				orderSelPnl.show();
			} else {
				orderSelPnl.hide();
			}
		}
	};

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message",
					'You do not have permission to print！');
			return;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'order'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0] - 200, po[1] + 25);
			printWin.show();
		} else {
			printWin.hide();
		}
	};

	// 订单采购
	function okToFac() {
		if ($('product').checked) {
			orderToFac();
		}
		if ($('fitting').checked) {
			fitAnys();
		}
		if ($('packing').checked) {
			packingAnys();
		}
	}

	// 配件采购分析
	function fitAnys() {
		var isMod = getPopedomByOpType(vaildUrl, "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.MessageBox
					.alert('Message',
							'You do not modify the permissions, you can not break down the order!');
			return;
		}
		var ids = getIds();
		if (ids.length != 1) {
			Ext.MessageBox.alert('Message', 'Please check a record!');
			return;
		} else {
			var list = sm.getSelections();
			var orderStatus = list[0].data.orderStatus;
			if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
				Ext.MessageBox
						.alert('Message',
								'Sorry, the order has not approved, the procurement of parts can not be!');
				return;
			}
			openFullWindow('cotfitanys.do?method=query&orderId=' + ids[0]);
		}
	}

	// 显示包材采购分析界面
	function packingAnys() {
		// $('menu').style.display = 'none';
		var isMod = getPopedomByOpType(vaildUrl, "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.MessageBox.alert('Message', '您没有修改权限,不能分解订单!');
			return;
		}
		var ids = getIds();
		if (ids.length != 1) {
			Ext.MessageBox.alert('Message', '请先勾选一条订单记录!');
			return;
		} else {
			var list = sm.getSelections();
			var orderStatus = list[0].data.orderStatus;
			if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
				Ext.MessageBox.alert('Message', '对不起,该订单还没审核通过,不能进行包材采购分析!');
				return;
			}
			openFullWindow('cotpackanys.do?method=query&orderId=' + ids[0]);
		}
	}

	function test() {
		cotOrderService.testCache(function(res) {
				});
	}

});
// 获得勾选的记录
function getIds() {
	var list = Ext.getCmp("orderGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
// 打开订单编辑页面
function windowopenMod(obj) {
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		} else
			obj = ids[0];
	}
	openFullWindow('cotorder.do?method=addOrder&id=' + obj);
}

// 打开生产合同编辑页面
function openFac(orderId) {
	var isPopedom = getPopedomByOpType('cotorderfac.do', "SEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message",
				"You do not have permission to view PO!");
	} else {
		if (orderId == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.MessageBox.alert("Message", "Please select a record");
				return;
			} else if (ids.length > 1) {
				Ext.MessageBox.alert("Message", "Choose only one record!")
				return;
			} else {
				orderId = ids[0];
			}
		}
		cotOrderService.getOrderFacByOrderId(orderId, function(res) {
					if (res != null) {
						openFullWindow('cotorderfac.do?method=add&id=' + res
								+ "&oId=" + orderId);
					} else {
						Ext.MessageBox.alert('Message',
								'This PI did not have PO!');
					}
				});
	}
}

// 删除
function del(id, status) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", "You do not have permission to delete");
		return;
	}
	// 判断该单是否已被审核
	if (status == 2 && loginEmpId != "admin") {
		Ext.MessageBox
				.alert("Message",
						"Sorry, this one has already been audited, can not be removed!");
		return;
	}
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('Message', 'Delete this order do?', function(btn) {
		if (btn == 'yes') {
			cotOrderService.deleteOrders(list, function(res) {
				if (res != null) {
					var str = "";
					if (res[0] > 0) {
						str += "has P.O!";
					}
					if (res[1] > 0) {
						str += "该订单已生成应收帐款!";
					}
					if (res[2] > 0) {
						str += "has Invoice!";
					}
					if (res[3] > 0) {
						str += "该订单已做配件采购!";
					}
					if (res[4] > 0) {
						str += "该订单已做包材采购!";
					}
					if (str != "") {
						if (res[5] > 0) {
							reloadGrid("orderGrid");
							Ext.MessageBox.alert('Message',
									"Order to delete some unsuccessful:" + str);
						} else {
							Ext.MessageBox.alert('Message', "Failed orders:"
											+ str);
						}
					} else {
						reloadGrid("orderGrid");
						Ext.MessageBox
								.alert('Message', "Deleted successfully!");
					}
				}
			})
		}
	});
}
// 显示货号分解界面
function orderToFac() {
	// $('menu').style.display = 'none';
	var isMod = getPopedomByOpType(vaildUrl, "MOD");
	if (isMod == 0) {// 没有修改权限
		Ext.MessageBox
				.alert("Message",
						'You do not modify the permissions, you can not break down the order!');
		return;
	}
	var ids = getIds();
	if (ids.length != 1) {
		Ext.MessageBox.alert("Message", 'Please check a record orde!');
		return;
	} else {
		var record = Ext.getCmp("orderGrid").getSelectionModel().getSelected();
		var orderStatus = record.get("orderStatus");
		if (orderStatus != 2 && orderStatus != 9 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert("Message",
							'Sorry, the order has not approved, the procurement of products can not be!');
			return;
		}
		openCustWindow('cotorder.do?method=queryOrderToOrderFac&orderId='
				+ ids[0]);
	}
}

// 生成出货单
function createInvoice(orderId) {
	var isPopedom = getPopedomByOpType('cotorderout.do', "ADD");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message",
				"You do not have permission to add Invoice!");
	} else {
		cotOrderService.saveInvoice(orderId, false, function(res) {
					if (res != -1) {
						// 打开出货编辑页面
						openFullWindow('cotorderout.do?method=addOrder&id='
								+ res);
					} else {
						Ext.MessageBox.alert("Message",
								'create Invoice Failed!');
						return;
					}
				})
	}
}