Ext.onReady(function() {
	DWREngine.setAsync(false);
	var fac = null; // 供应商类别
	// 加载厂家类表缓存
	// 1：产品，2：配件，3包材
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				fac = res;
			});
	DWREngine.setAsync(true);
	/** ******************************订单明细表格*********************************************** */
	var orderRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "factoryShortName"
			}, {
				name : "boxCount"
			}, {
				name : "unBoxCount4OrderFac"
			}, {
				name : "outHasBoxCount4OrderFac"
			}]);
	// 创建数据源
	var orderds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorder.do?method=queryOrderToOrderFac&orderId="
									+ $('orderId').value
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, orderRecord)
			});
	// 创建复选框列
	var ordersm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var ordercm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [ordersm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "W&C Art No.",
							dataIndex : "eleId",
							width : 75
						}, {
							header : "Descripiton",
							dataIndex : "eleName",
							width : 125
						}, {
							header : "Supplier",
							dataIndex : "factoryShortName",
							width : 80
						}, {
							header : "Quantity",
							dataIndex : "boxCount",
							width : 65
						}, {
							header : "未分数量",
							dataIndex : "unBoxCount4OrderFac",
							width : 65,
							renderer : function(value) {
								return "<span style='color:red;font-weight:bold;'>"
										+ value + "</span>";
							}
						}, {
							header : "已分数量",
							dataIndex : "outHasBoxCount4OrderFac",
							width : 65,
							renderer : function(value) {
								return "<span style='color:green;font-weight:bold;'>"
										+ value + "</span>";
							},
							sortable : false
						}]
			});
	var ordertoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : orderds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var ordertb = new Ext.Toolbar({
				items : [ 
					{
							xtype : "checkbox",
							boxLabel : "Decompose by Art No.",
							style:'margin-top: 0px;',
							id : "chkEle",
							name : "chkEle",
							listeners : {
								'check' : function(chkbox, checked) {
									if (checked) {
										showProgressWin();
									}
								}
							}
						},'->', 
						'-', {
							text : "Decompose",
							handler : orderToOrderfac,
							iconCls : "page_fen"
						}, '-', {
							text : "Assign",
							handler : windowopenAdd,
							iconCls : "page_jiao"
						}]
			});
	var ordergrid = new Ext.grid.GridPanel({
				title : "Order detail",
				region : "west",
				id : "orderGrid",
				width : 500,
				// autoHeight:true,
				stripeRows : true,
				margins : "0 5 0 0",
				bodyStyle : 'width:100%',
				store : orderds, // 加载数据源
				cm : ordercm, // 加载列
				sm : ordersm,
				// selModel: new
				// Ext.grid.RowSelectionModel({singleSelect:false}),
				loadMask : true, // 是否显示正在加载
				tbar : ordertb,
				bbar : ordertoolBar,
				// layout:"fit",
				viewConfig : {
					forceFit : false
				}
			});

	/** ************************生产合同表格***************************************************** */
	var orderfacRecord = new Ext.data.Record.create([

	{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "factoryId"
			}, {
				name : "orderNo"
			}, {
				name : "orderTime"
			}, {
				name : "sendTime"
			}

	]);

	// 创建数据源
	var orderfacds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorderfac.do?method=queryOrderFac"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, orderfacRecord)
			});
	// 创建复选框列
	var orderfacsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var orderfaccm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [orderfacsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return fac[value]
							}
						}, {
							header : "W & C Order No.",
							dataIndex : "orderNo",
							width : 180
						}, {
							header : "Date",
							dataIndex : "orderTime",
							width : 100,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
							}
						}, {
							header : "Delivery Date",
							dataIndex : "sendTime",
							width : 100,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
							}
						}]
			});
	var orderfactoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : orderfacds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var orderfactb = new Ext.Toolbar({
				items : ['->', {
							text : "Refresh",
							handler : function() {
								orderfacds.on("beforeload", function(store,
												options) {
											orderfacds.baseParams.facIds = allIds;
										});
								orderfacds.load({
											params : {
												start : 0,
												limit : 15
											}
										})
							},
							iconCls : "page_refresh"
						}, '-', {
							text : "Save",
							handler : save,
							iconCls : "page_table_save"
						}, '-', {
							text : "Update",
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "gird_edit"
						}, '-', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del"
						}]
			});
	var orderfacgrid = new Ext.grid.GridPanel({
				title : "Product purchase order",
				region : "north",
				id : "orderfacGrid",
				// autoHeight:true,
				height : 350,
				stripeRows : true,
				margins : "0 0 0 0",
				bodyStyle : 'width:100%',
				store : orderfacds, // 加载数据源
				cm : orderfaccm, // 加载列
				sm : orderfacsm,
				// selModel: new
				// Ext.grid.RowSelectionModel({singleSelect:false}),
				loadMask : true, // 是否显示正在加载
				tbar : orderfactb,
				bbar : orderfactoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** *********************************生产合同明细表格*************************************************** */
	var detailRecord = new Ext.data.Record.create([

	{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "eleId"
			}, {
				name : "factoryNo"
			}, {
				name : "eleName"
			}, {
				name : "boxCount"
			}, {
				name : "priceFac",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "eleUnit"
			}, {
				name : "rdm"
			}

	]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var detailds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
					url : "cotorderfac.do?method=queryDetail",
					listeners : {
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							if (res.status == 200) {
								reloadGrid('orderGrid');
							}
						}
					}
				}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, detailRecord),
				writer : writer
			});
	// 创建复选框列
	var detailsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var detailcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [detailsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 80
						}, {
							header : "Supplier's Art No.",
							dataIndex : "factoryNo",
							width : 80
						}, {
							header : "Descripiton",
							dataIndex : "eleName",
							width : 150
						}, {
							header : "<font color=red>Quantity</font>",
							dataIndex : "boxCount",
							width : 65,
							editor : new Ext.form.NumberField({
										maxValue : 9999999999
									}),
							renderer : function(value) {
								return "<span style='color:red;font-weight:bold;'>"
										+ value + "</span>";
							}
						}, {
							header : "Unit Price",
							dataIndex : "priceFac",
							width : 65
						}, {
							header : "Size Description",
							dataIndex : "eleSizeDesc",
							width : 180
						}, {
							header : "Size Description",
							dataIndex : "eleInchDesc",
							width : 180
						}, {
							header : "Unit",
							dataIndex : "eleUnit",
							width : 65
						}, {
							header : "rdm",
							dataIndex : "rdm",
							width : 65,
							hidden : true
						}]
			});
	var detailtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : detailds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var detailgrid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "detailGrid",
				stripeRows : true,
				store : detailds, // 加载数据源
				cm : detailcm, // 加载列
				sm : detailsm,
				loadMask : true, // 是否显示正在加载
				bbar : detailtoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** ************************************************************************************** */
	var panel = new Ext.Panel({
				layout : "border",
				region : "center",
				items : [orderfacgrid, detailgrid]
			})
	var viewport = new Ext.Viewport({
				layout : "border",
				items : [ordergrid, panel]
			});
	orderds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// 订单表格单击事件
	ordergrid.on("rowclick", function(gridpanel, rowIndex) {
				var record = gridpanel.getStore().getAt(rowIndex);
				DWREngine.setAsync(false);
				cotOrderService.getOrderFacIdsByDetailId(parseInt(record
								.get("id")), function(facids) {
							$('orderfacIds').value = facids.substring(0,
									facids.length - 1);
						});
				orderfacds.on("beforeload", function(store, options) {
							orderfacds.baseParams.facIds = $('orderfacIds').value;
						});
				orderfacds.load({
							params : {
								start : 0,
								limit : 15
							}
						})
				DWREngine.setAsync(true);
			});
	// 生产合同表格单击事件
	orderfacgrid.on("rowclick", function(gridpanel, rowIndex) {
				var record = gridpanel.getStore().getAt(rowIndex);
				var orderfacId = record.get("id");
				$('orderfacId').value = orderfacId;
				var queryPara = {
					'flag' : 'orderToOrderFac',
					'orderId' : orderfacId
				};
				detailds.on("beforeload", function(store, options) {
							detailds.baseParams = queryPara;
						});
				detailds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
			});
	orderfacgrid.on("rowdblclick", function(gridpanel, rowIndex) {
				var record = orderfacds.getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	var rowCurrent = -1;
	// 单元格点击后,记住当前行
	detailgrid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				rowCurrent = rowIndex;
			});

	// 单元格编辑后
	detailgrid.on("afteredit", function(e) {
				cotOrderFacService.updateOrderFacMapValueByRdm(
						e.record.data.rdm, e.field, e.value, function(res) {
						});
			});

	// 初始化
	var allIds = "";// 该单的所有生产合同单号
	function init() {
		DWREngine.setAsync(false);
		cotOrderService.getOrderFacIds(parseInt($('orderId').value), function(
						facids) {
					$('orderfacIds').value = facids.substring(0, facids.length
									- 1);
					allIds = $('orderfacIds').value;
				});
		DWREngine.setAsync(true);
	}
	init();

	orderfacds.load({
				params : {
					start : 0,
					limit : 15,
					facIds : $('orderfacIds').value
				}
			});

	// 保存
	function save() {

		// 更改修改action参数
		var urlMod = '&orderId=' + $('orderfacId').value;

		detailds.proxy.setApi({
					update : "cotorderfac.do?method=modifyDetail" + urlMod
				});
		detailds.save();
		Ext.MessageBox.alert("Message", "Save successful！")
		//reloadGrid('orderGrid');

	}

});
function decodeOrderByEleIdList() {
	showProgressWin();
	var orderId = $('orderId').value;
	var store = Ext.getCmp("orderGrid").getStore();
	var ids = new Array();
	// var i=0
	for (var i = 1; i < store.getCount() + 1; i++) {
		var rec = store.getAt(i - 1);
		var orderdetailId = parseInt(rec.get("id"));
		orderId = parseInt(orderId);
		var eleId = rec.get("eleId");
		var ms = 5000 / store.getCount();
		setTimeout("decodeOrder(" + i + "," + orderdetailId + "," + orderId
						+ ")", i * ms);
	}
	// store.each(function(rec){
	//		
	// var orderdetailId = parseInt(rec.get("id"));
	// orderId = parseInt(orderId);
	// setTimeout("decodeOrder("+orderdetailId+","+orderId+")",count*ms);
	//		
	// });
	// cotOrderFacService.savaOrderFacByEleIdList(ids,parseInt(orderId),function(res){
	//		
	// });
}
function decodeOrder(v, orderdetailId, orderId, eleId) {
	var store = Ext.getCmp("orderGrid").getStore();
	var rec = store.getAt(v - 1);
	var eleId = rec.get("eleId");
	cotOrderFacService.savaOrderFacByEleId(orderdetailId, orderId,
			function(res) {
				var msg = '第' + (v) + '个货号:' + eleId + '分解完成,共 '
						+ store.getCount() + '个'
				pbar1.updateProgress((v) / store.getCount(), msg);
			});
}
var pbar1 = new Ext.ProgressBar({
			text : 'Pls. click the decompose button to decompose...',
			listeners : {
				'update' : function(pbar, number, text) {
					if (number == 1) {
						pbar1.updateText("decompose complete!");
						self.location.reload();
					}
					// alert(number)
				}
			}
		});
function showProgressWin() {
	var win = new Ext.Window({
				layout : "fit",
				width : 350,
				// height:100,
				closeAction : "hide",
				items : [pbar1]
			});
	pbar1.reset()
	win.show();
	// pbar1.updateProgress(0, 'Loading item ' + 0 + ' of '+0+'...');
}
