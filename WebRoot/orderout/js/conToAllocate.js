Ext.onReady(function() {

	var facData;
	var curData;

	DWREngine.setAsync(false);
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	DWREngine.setAsync(true);
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	DWREngine.setAsync(true);

	/** ******EXT创建grid步骤******** */
	var outDetailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "orderName"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "orderPrice",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "taoUnit"
			}, {
				name : "eleUnit"
			}, {
				name : "boxCount",
				type : "int"
			}, {
				name : "containerCount",
				type : "int"
			}, {
				name : "totalMoney",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "unSendNum",
				type : "int"
			}, {
				name : "boxCbm",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.0000"], 3)
			}, {
				name : "factoryId"
			}, {
				name : "factoryNo"
			}, {
				name : "priceFac",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "priceFacUint"
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "boxIbCount",
				type : "int"
			}, {
				name : "boxMbCount",
				type : "int"
			}, {
				name : "boxObCount",
				type : "int"
			}, {
				name : "boxObL",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObW",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObH",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxGrossWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "boxNetWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "orderDetailId"
			}, {
				name : "orderNoid"
			}, {
				name : "eleRemark"
			}, {
				name : "sortNo"
			}]);

	// 创建数据源
	var outdetail_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryOrderDetail"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, outDetailRecord)
			});
	// 创建复选框列
	var outdetail_sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var outdetail_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [outdetail_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Sort NO.",
							dataIndex : "sortNo",
							width : 30
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 100,
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "Client No.",
							dataIndex : "custNo",
							width : 120
						}, {
							header : "Des. of Category",
							dataIndex : "orderName",
							width : 120,
							hidden : true
						}, {
							header : "中文名",
							dataIndex : "eleName",
							width : 120
						}, {
							header : "Description",
							dataIndex : "eleNameEn",
							width : 120
						}, {
							header : "Sale Price",
							dataIndex : "orderPrice",
							width : 80,
							hidden : true
						}, {
							header : "Unit",
							dataIndex : "eleUnit",
							width : 80,
							hidden : true
						}, {
							header : "报价单位",
							dataIndex : "taoUnit",
							width : 80,
							hidden : true
						}, {
							header : "Quantity",
							dataIndex : "boxCount",
							width : 60,
							summaryType : 'sum'
						}, {
							header : "Cartons",
							dataIndex : "containerCount",
							width : 60,
							summaryType : 'sum'
						}, {
							header : "Amount",
							dataIndex : "totalMoney",
							width : 60,
							summaryType : 'sum'
						}, {
							header : "<font color=red>未出货数</font>",
							dataIndex : "unSendNum",
							width : 60,
							summaryType : 'sum'
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							width : 80,
							renderer : function(value) {
								return facData[value];
							},
							hidden : true
						}, {
							header : "Supplier Art No.",
							dataIndex : "factoryNo",
							width : 70,
							hidden : true
						}, {
							header : "Purchase Price",
							dataIndex : "priceFac",
							width : 60,
							hidden : true
						}, {
							header : "Currency",
							dataIndex : "priceFacUint",
							width : 60,
							renderer : function(value) {

								return curData[value];
							},
							hidden : true
						}, {
							header : "inner",
							dataIndex : "boxIbCount",
							width : 60
						}, {
							header : "Middle",
							dataIndex : "boxMbCount",
							width : 60
						}, {
							header : "outer",
							dataIndex : "boxObCount",
							width : 60
						}, {
							header : "Length of outer carton",
							dataIndex : "boxObL",
							width : 60,
							hidden : true
						}, {
							header : "Width of outer carton",
							dataIndex : "boxObW",
							width : 60,
							hidden : true
						}, {
							header : "Height of outer carton",
							dataIndex : "boxObH",
							width : 60,
							hidden : true
						}, {
							header : "CBM",
							dataIndex : "boxCbm",
							width : 60,
							hidden : true
						}, {
							header : "G.W.",
							dataIndex : "boxGrossWeigth",
							width : 60,
							hidden : true
						}, {
							header : "N.W.",
							dataIndex : "boxNetWeigth",
							width : 60,
							hidden : true
						}, {
							header : "Size Description",
							dataIndex : "eleSizeDesc",
							width : 90,
							hidden : true
						}, {
							header : "Size Description",
							dataIndex : "eleInchDesc",
							width : 90,
							hidden : true
						}, {
							header : "orderDetailId",
							dataIndex : "orderDetailId",
							hidden : true
						}, {
							header : "orderNoid",
							dataIndex : "orderNoid",
							hidden : true
						}, {
							header : "Remark",
							dataIndex : "eleRemark",
							width : 200
						}]
			});

	var outdetail_toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : outdetail_ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 出货明细表格
	var outdetail_grid = new Ext.grid.EditorGridPanel({
				title : "Shipping Details",
				region : "west",
				width : 450,
				id : "outDetailGrid",
				stripeRows : true,
				margins : "0 1 0 0",
				bodyStyle : 'width:100%',
				store : outdetail_ds,
				cm : outdetail_cm,
				sm : outdetail_sm,
				plugins : [summary],
				loadMask : true,
				border : false,
				// height : 275,
				bbar : outdetail_toolBar,
				viewConfig : {
					forceFit : false
				}

			});

	// ============================================================
	/** ******EXT创建grid步骤******** */
	var facDetailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "factoryNo"
			}, {
				name : "factoryId"
			}, {
				name : "boxCount"
			}, {
				name : "outHasOut"
			}, {
				name : "orderNo"
			}, {
				name : "orderfacDetailId"
			}, {
				name : "orderTime"
			}]);

	// 创建数据源
	var facdetail_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryOrderFac"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, facDetailRecord)
			});
	// 创建复选框列
	var facdetail_sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var facdetail_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [facdetail_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 80
						}, {
							header : "W & C Order No.",
							dataIndex : "orderNo",
							width : 180
						}, {
							header : "P.O Date",
							dataIndex : "orderTime",
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
							},
							width : 80
						}, {
							header : "Supplier's Article No",
							dataIndex : "factoryNo",
							width : 80,
							hidden : true
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							renderer : function(value) {
								return facData[value];
							},
							width : 80
						}, {
							header : "P.O Quantity",
							dataIndex : "boxCount",
							width : 80
						}, {
							header : "已指定数量",
							dataIndex : "outHasOut",
							renderer : function(value) {
								return "<span style='color:green;font-weight:bold;'>"
										+ value + "</span>";
							},
							width : 80
						}, {
							header : "orderfacDetailId",
							dataIndex : "orderfacDetailId",
							hidden : true
						}]
			});

	var facdetail_toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : facdetail_ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	var facdetail_tb = new Ext.Toolbar({
				items : [{
							text : "数量指定",
							handler : allocateToFactory,
							iconCls : "page_allocated"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 采购明细表格
	var facdetail_grid = new Ext.grid.EditorGridPanel({
				title : "Purchase History Records",
				region : "center",
				id : "facDetailGrid",
				stripeRows : true,
				margins : "0 5 0 0",
				cls : 'rightBorder bottomBorder',
				bodyStyle : 'width:100%',
				store : facdetail_ds,
				cm : facdetail_cm,
				sm : facdetail_sm,
				plugins : [summary],
				loadMask : true,
				border : false,
				bbar : facdetail_toolBar,
				tbar : facdetail_tb,
				viewConfig : {
					forceFit : false
				}
			});

	//
	var facRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "factoryId"
			}, {
				name : "outCurrent"
			}, {
				name : "priceFac",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}]);

	var fac_writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var fac_ds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryAllocateDetail",
								destroy : "cotorderout.do?method=removeOrderFacdetail"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, facRecord),
				writer : fac_writer
			});
	// 创建复选框列
	var fac_sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var fac_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [fac_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							renderer : function(value) {
								return facData[value];
							},
							width : 120
						}, {
							header : "<font color='red'>本次出货数量</font>",
							dataIndex : "outCurrent",
							renderer : function(value) {
								return "<span style='color:red;font-weight:bold;'>"
										+ value + "</span>";
							},
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0
									}),
							width : 120
						}, {
							header : "sale price ",
							dataIndex : "priceFac",
							width : 80

						}, {
							header : "Amount",
							dataIndex : "amount",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								return Ext.util.Format.number(record
												.get("priceFac")
												* record.get("outCurrent"),
										"0.00");
							},
							width : 120

						}]
			});

	var fac_toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : fac_ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	var fac_tb = new Ext.Toolbar({
				items : [{
							text : "Yes",
							handler : updateFacDetail,
							iconCls : "page_mod"
						}, '-', {
							text : "Delete",
							handler : deleteSelectRow,
							iconCls : "page_del"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 数量指定
	var fac_grid = new Ext.grid.EditorGridPanel({
				region : "north",
				id : "facGrid",
				height : 300,
				cls : 'rightBorder bottomBorder',
				stripeRows : true,
				margins : "0 5 0 0",
				store : fac_ds,
				cm : fac_cm,
				sm : fac_sm,
				plugins : [summary],
				loadMask : true,
				border : false,
				bbar : fac_toolBar,
				tbar : fac_tb,
				viewConfig : {
					forceFit : false
				}
			});

	// ===========配件===================================
	var fitDetailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "fitNo"
			}, {
				name : "facId"
			}, {
				name : "orderCount"
			}, {
				name : "outHasOut"
			}, {
				name : "fitPrice",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "orderNo"
			}, {
				name : "fittingOrderNo"
			}, {
				name : "fitName"
			}]);

	// 创建数据源
	var fitdetail_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryFitOrder"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, fitDetailRecord)
			});
	// 创建复选框列
	var fitdetail_sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var fitdetail_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [fitdetail_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 80
						}, {
							header : "Fitting No.",
							dataIndex : "fitNo",
							width : 80
						}, {
							header : "Fiitting Name",
							dataIndex : "fitName",
							width : 150
						}, {
							header : "W&C P/O.",
							dataIndex : "fittingOrderNo",
							width : 150
						}, {
							header : "Supplier",
							dataIndex : "facId",
							renderer : function(value) {
								return facData[value];
							},
							width : 80
						}, {
							header : "Quality",
							dataIndex : "orderCount",
							width : 80
						}, {
							header : "已指定数量",
							dataIndex : "outHasOut",
							renderer : function(value) {
								return "<span style='color:green;font-weight:bold;'>"
										+ value + "</span>";
							},
							width : 80
						}, {
							header : "Price",
							dataIndex : "fitPrice",
							width : 80
						}, {
							header : "orderDetailId",
							dataIndex : "orderDetailId",
							hidden : true
						}]
			});

	var fitdetail_toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : fitdetail_ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	var fitdetail_tb = new Ext.Toolbar({
				items : [{
							text : "数量指定",
							handler : allocateToFit,
							iconCls : "page_allocated"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 采购明细表格
	var fitdetail_grid = new Ext.grid.EditorGridPanel({
				title : "产品配件采购记录",
				region : "center",
				id : "fitDetailGrid",
				stripeRows : true,
				cls : 'rightBorder bottomBorder',
				margins : "0 5 0 0",
				store : fitdetail_ds,
				cm : fitdetail_cm,
				sm : fitdetail_sm,
				plugins : [summary],
				loadMask : true,
				border : false,
				height : 215,
				bbar : fitdetail_toolBar,
				tbar : fitdetail_tb,
				viewConfig : {
					forceFit : false
				}
			});

	// =================
	var fitRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "facId"
			}, {
				name : "outCurrent"
			}, {
				name : "fitPrice",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}]);

	var fit_writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var fit_ds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryFitFacDetail"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, fitRecord),
				writer : fit_writer
			});
	// 创建复选框列
	var fit_sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var fit_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [fit_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 120
						}, {
							header : "Supplier",
							dataIndex : "facId",
							renderer : function(value) {
								return facData[value];
							},
							width : 120
						}, {
							header : "指定数量",
							dataIndex : "outCurrent",
							renderer : function(value) {
								return "<span style='color:red;font-weight:bold;'>"
										+ value + "</span>";
							},
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0
									}),
							width : 120
						}, {
							header : "Price",
							dataIndex : "fitPrice",
							width : 80

						}, {
							header : "Amount",
							dataIndex : "amount",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								return Ext.util.Format.number(record
												.get("fitPrice")
												* record.get("outCurrent"),
										"0.00");
							},
							width : 120

						}]
			});

	var fit_toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : fit_ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	var fit_tb = new Ext.Toolbar({
				items : [{
							text : "Yes",
							handler : updateFitDetail,
							iconCls : "page_mod"
						}, '-', {
							text : "Delete",
							handler : deleteFitSelectRow,
							iconCls : "page_del"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 数量指定
	var fit_grid = new Ext.grid.EditorGridPanel({
				id : "fitGrid",
				cls : 'rightBorder bottomBorder',
				stripeRows : true,
				margins : "0 5 0 0",
				store : fit_ds,
				cm : fit_cm,
				sm : fit_sm,
				plugins : [summary],
				loadMask : true,
				border : false,
				height : 200,
				bbar : fit_toolBar,
				tbar : fit_tb,
				viewConfig : {
					forceFit : true
				}
			});

	// ===========包材===================================
	var packDetailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "factoryId"
			}, {
				name : "packCount"
			}, {
				name : "outHasOut"
			}, {
				name : "packPrice",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "packingOrderNo"
			}, {
				name : "orderDetailId"
			}]);

	// 创建数据源
	var packdetail_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryPackOrder"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, packDetailRecord)
			});
	// 创建复选框列
	var packdetail_sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var packdetail_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [packdetail_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 80
						}, {
							header : "Client No.",
							dataIndex : "custNo",
							width : 80
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							renderer : function(value) {
								return facData[value];
							},
							width : 80
						}, {
							header : "W&C P/O.",
							dataIndex : "packingOrderNo",
							width : 180
						}, {
							header : "Quality",
							dataIndex : "packCount",
							width : 80
						}, {
							header : "已指定数量",
							dataIndex : "outHasOut",
							renderer : function(value) {
								return "<span style='color:green;font-weight:bold;'>"
										+ value + "</span>";
							},
							width : 80
						}, {
							header : "Price",
							dataIndex : "packPrice",
							width : 80
						}, {
							header : "orderDetailId",
							dataIndex : "orderDetailId",
							hidden : true
						}]
			});

	var packdetail_toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : packdetail_ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	var packdetail_tb = new Ext.Toolbar({
				items : [{
							text : "数量指定",
							handler : allocateToPack,
							iconCls : "page_allocated"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 采购明细表格
	var packdetail_grid = new Ext.grid.EditorGridPanel({
				title : "产品包材记录",
				region : "center",
				id : "packDetailGrid",
				stripeRows : true,
				cls : 'bottomBorder',
				margins : "0 5 0 0",
				store : packdetail_ds,
				cm : packdetail_cm,
				sm : packdetail_sm,
				plugins : [summary],
				loadMask : true,
				border : false,
				height : 215,
				bbar : packdetail_toolBar,
				tbar : packdetail_tb,
				viewConfig : {
					forceFit : false
				}
			});

	// =================
	var packRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "facId"
			}, {
				name : "outCurrent"
			}, {
				name : "packPrice",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}]);

	var pack_writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var pack_ds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryFitFacDetail"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, packRecord),
				writer : pack_writer
			});
	// 创建复选框列
	var pack_sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var pack_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [pack_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 120
						}, {
							header : "Supplier",
							dataIndex : "facId",
							renderer : function(value) {
								return facData[value];
							},
							width : 120
						}, {
							header : "指定数量",
							dataIndex : "outCurrent",
							width : 120
						}, {
							header : "Price",
							dataIndex : "packPrice",
							width : 80

						}, {
							header : "Amount",
							dataIndex : "amount",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								return Ext.util.Format.number(record
												.get("packPrice")
												* record.get("outCurrent"),
										"0.00");
							},
							width : 120

						}]
			});

	var pack_toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : pack_ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	var pack_tb = new Ext.Toolbar({
				items : [{
							text : "OK",
							handler : updatePackDetail,
							iconCls : "page_mod"
						}, '-', {
							text : "Delete",
							handler : deletePackSelectRow,
							iconCls : "page_del"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 数量指定
	var pack_grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "packGrid",
				stripeRows : true,
				margins : "0 5 0 0",
				cls : 'rightBorder bottomBorder',
				store : pack_ds,
				cm : pack_cm,
				sm : pack_sm,
				plugins : [summary],
				loadMask : true,
				border : false,
				height : 200,
				bbar : pack_toolBar,
				tbar : pack_tb,
				viewConfig : {
					forceFit : true
				}
			});

	outdetail_grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				var orderId = record.data.orderNoid;// 订单主单id
				var eleId = record.data.eleId;
				var orderDetailId = record.data.orderDetailId;// 订单明细id
				var boxCount = record.data.boxCount;
				// 判断出货数量是否等于采购数量
				DWREngine.setAsync(false);

				cotOrderOutService.checkIsEqualCount(parseInt(orderId), eleId,
						parseInt(boxCount), function(equal) {
							if (equal == true) {
								cotOrderOutService.updateOrderFacOutRemain(
										parseInt(orderId), eleId,
										parseInt($('pId').value),
										function(res) {
										});
								cotOrderOutService.updateFitOrderOutRemain(
										parseInt(orderId), function(fit) {
										});
								cotOrderOutService.updatePackOrderOutRemain(
										parseInt(orderId), function(fit) {
										});
							}
						});
				DWREngine.setAsync(true);

				// ====产品=======
				facdetail_ds.load({
							params : {
								start : 0,
								limit : 1000,
								orderDetailid : orderDetailId,
								eleId : eleId
							}
						});

				fac_ds.load({
							params : {
								start : 0,
								limit : 1000,
								orderDetailId : orderDetailId,
								eleId : eleId
							}
						})
				facdetail_grid.setTitle("产品" + eleId + "采购记录");
				// ===配件======
				fitdetail_ds.load({
							params : {
								start : 0,
								limit : 1000,
								orderId : orderId
							}
						});

				fit_ds.load({
							params : {
								start : 0,
								limit : 1000,
								orderId : orderId
							}
						})
				fitdetail_grid.setTitle("产品" + eleId + "配件采购记录");
				// ===包材======
				packdetail_ds.load({
							params : {
								start : 0,
								limit : 1000,
								orderId : orderId
							}
						});

				pack_ds.load({
							params : {
								start : 0,
								limit : 1000,
								orderId : orderId
							}
						})
				packdetail_grid.setTitle("产品" + eleId + "包材采购记录");
			});

	outdetail_ds.load({
				params : {
					start : 0,
					limit : 1000,
					flag : 'orderOutDetail',
					orderId : $('pId').value
				}
			})

	var tabPln = new Ext.TabPanel({
				activeTab : 0,
				region : "center",
				items : [{
							title : "产品分配",
							items : [facdetail_grid, fac_grid]
						}, {
							title : "配件分配",
							items : [fitdetail_grid, fit_grid]
						}, {
							title : "包材分配",
							items : [packdetail_grid, pack_grid]
						}]
			})
	// ======布局======================
	var btnForm = new Ext.form.FormPanel({
				region : "south",
				height : 40,
				buttonAlign : "center",
				fbar : [{
							width : 65,
							text : "生成货款",
							handler : addOtherAndDeal,
							iconCls : "page_add",
							id : "upmod"
						}, {
							width : 65,
							text : "取消",
							handler : closeAllocateDiv,
							iconCls : "page_cancel",
							id : "updel"
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [outdetail_grid, tabPln, btnForm],
				listeners : {
					"afterrender" : function(panel) {
						var height = panel.getHeight()
								- tabPln.getFrameHeight() - btnForm.getHeight();
						var width = panel.getWidth();
						fac_grid.setHeight(Math.ceil(height / 2));
						facdetail_grid.setHeight(Math.ceil(height / 2) - 1)
						fit_grid.setHeight(Math.ceil(height / 2));
						fitdetail_grid.setHeight(Math.ceil(height / 2) - 1)
						pack_grid.setHeight(Math.ceil(height / 2));
						packdetail_grid.setHeight(Math.ceil(height / 2) - 1)
						outdetail_grid.setWidth(Math.ceil(width / 2));
					}
				}
			});
	viewport.doLayout();
	// ================end====================

	// ==============方法============================================
	// 关闭合同分配层
	function closeAllocateDiv() {
		closeandreflashEC('true', 'otherfeeGrid', false);
	}

	// 保存费用及应付款
	function addOtherAndDeal() {
		var id = $('pId').value;
		Ext.MessageBox.confirm('提示消息', '是否确定合同分配?', function(btn) {
			if (btn == 'yes') {
				DWREngine.setAsync(false);
				cotOrderOutService.getOrderOutdetail(id, function(res) {
					for (var i = 0; i < res.length; i++) {
						// 获取所有采购明细
						cotOrderOutService.getOrderFacDetail(
								res[i].orderDetailId, function(orderfacdetail) {
									for (var j = 0; j < orderfacdetail.length; j++) {
										if (orderfacdetail[j].outHasOut > 0) {
											cotOrderOutService
													.saveOrderFacOther(
															parseInt(orderfacdetail[j].id),
															parseInt($('pId').value),
															function(other) {
															});
										} else {
											cotOrderOutService.saveOthers(
													res[i].orderDetailId, id,
													function(list) {
													});
										}
									}
								});

						// 获取所有配件采购明细
						cotOrderOutService.getFitOrderDetail(res[i].orderNoid,
								function(fitdetailList) {
									for (var j = 0; j < fitdetailList.length; j++) {
										if (fitdetailList[j].outHasOut > 0) {
											cotOrderOutService
													.saveFitOrderOther(
															parseInt(fitdetailList[j].id),
															parseInt($('pId').value),
															function(fitother) {
															});
										} else {
											cotOrderOutService
													.saveFitOthers(
															parseInt(fitdetailList[j].id),
															id, function(list) {
															});
										}
									}
								});

						// 获取所有包材采购明细
						cotOrderOutService.getPackOrderDetail(res[i].orderNoid,
								function(packdetailList) {
									for (var j = 0; j < packdetailList.length; j++) {
										if (packdetailList[j].outHasOut > 0) {
											cotOrderOutService
													.savePackOrderOther(
															parseInt(packdetailList[j].id),
															parseInt($('pId').value),
															function(packother) {
															});
										} else {
											cotOrderOutService
													.savePackOthers(
															parseInt(packdetailList[j].id),
															id, function(list) {
															});
										}
									}
								});
					}
				});
				DWREngine.setAsync(true);
				closeandreflashEC('true', 'otherfeeGrid', false);
			}
		});
	}

	// =========产品========================================
	// 判断该厂家是否已指定数量
	function checkIsExist(factoryId) {

		var flag = 0;
		fac_ds.each(function(rec) {
					if (rec.data.factoryId == factoryId) {
						flag++;
					}
				});
		return flag;
	}

	// 获得采购表格选择的记录
	function getIds() {
		var list = Ext.getCmp("facDetailGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var orderfac = new VOrderOrderfacId();

					orderfac.id = item.id;
					orderfac.factoryId = item.data.factoryId;
					orderfac.boxCount = item.data.boxCount;
					var outHasOut = item.data.outHasOut;
					orderfac.orderfacDetailId = item.data.orderfacDetailId;

					if (outHasOut == null || outHasOut == '') {
						orderfac.outHasOut = 0;
					}

					res.push(orderfac);
				});
		return res;
	}

	// 指定产品采购数量
	function allocateToFactory() {

		var list = getIds();
		if (list.length == 0) {
			Ext.Msg.alert("Message", "Please select a record");
			return;
		}
		for (var i = 0; i < list.length; i++) {
			var flag = checkIsExist(list[i].factoryId);

			if (flag == 0) {
				var obj = new Object();
				obj.factoryId = list[i].factoryId;
				obj.outCurrent = parseInt(list[i].boxCount)
						- parseInt(list[i].outHasOut);
				obj.id = list[i].orderfacDetailId;
				// 添加到表格中
				setObjToGrid(obj);
			}
		}
	}

	// 添加对象到表格
	function setObjToGrid(obj) {
		var u = new fac_ds.recordType(obj);
		fac_ds.add(u);
	}

	// 获得采购表格选择的记录
	function getChkNumIds() {
		var list = Ext.getCmp("facGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 删除表格选择的行
	function deleteSelectRow() {

		var ids = getChkNumIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Please select the article！");
			return;
		}

		DWREngine.setAsync(false);
		for (var i = 0; i < ids.length; i++) {
			cotOrderOutService.updateFacDetailForDel(parseInt(ids[i]),
					function(res) {
					});
		}

		DWREngine.setAsync(true);

		var cord = fac_sm.getSelections();
		Ext.each(cord, function(item) {
					fac_ds.remove(item);
				});

		facdetail_ds.reload();
		fac_ds.reload();
	}

	// 更新采购明细已出货数量
	function updateFacDetail() {

		fac_ds.each(function(rec) {

					var orderfacDetailId = rec.data.id;
					var factoryId = rec.data.factoryId;
					var outCurrent = rec.data.outCurrent;
					DWREngine.setAsync(false);
					if (orderfacDetailId != null && orderfacDetailId != '') {
						cotOrderOutService.updateFacDetail(
								parseInt(orderfacDetailId),
								parseInt(outCurrent), function(res) {
								});
					}
					DWREngine.setAsync(true);
				});
		facdetail_ds.reload();
		fac_ds.reload();
	}
	// =========产品====end====================================

	// =========配件========================================
	// 判断该厂家是否已指定数量
	function checkIsFitFacExist(factoryId) {

		var flag = 0;
		fit_ds.each(function(rec) {
					if (rec.data.factoryId == factoryId) {
						flag++;
					}
				});
		return flag;
	}

	// 获得采购表格选择的记录
	function getFitIds() {
		var list = Ext.getCmp("fitDetailGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var fitorder = new VOrderFitorderId();

					fitorder.id = item.id;
					fitorder.factoryId = item.data.facId;
					fitorder.orderCount = item.data.orderCount;
					var outHasOut = item.data.outHasOut;
					fitorder.eleId = item.data.eleId;

					if (outHasOut == null || outHasOut == '') {
						fitorder.outHasOut = 0;
					}

					res.push(fitorder);
				});
		return res;
	}

	// 指定配件采购数量
	function allocateToFit() {

		var list = getFitIds();
		if (list.length == 0) {
			Ext.Msg.alert("Message", "Please select a purchase records");
			return;
		}
		for (var i = 0; i < list.length; i++) {
			var flag = checkIsFitFacExist(list[i].factoryId);

			if (flag == 0) {
				var obj = new Object();
				obj.eleId = list[i].eleId;
				obj.facId = list[i].factoryId;
				obj.outCurrent = parseInt(list[i].boxCount)
						- parseInt(list[i].outHasOut);
				obj.id = list[i].id;
				// 添加到表格中
				setObjToFitGrid(obj);
			}
		}
	}

	// 添加对象到表格
	function setObjToFitGrid(obj) {
		var u = new fit_ds.recordType(obj);
		fit_ds.add(u);
	}

	// 获得采购表格选择的记录
	function getChkFitNumIds() {
		var list = Ext.getCmp("fitGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 删除表格选择的行
	function deleteFitSelectRow() {

		var ids = getChkFitNumIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Please select the records！");
			return;
		}

		DWREngine.setAsync(false);
		for (var i = 0; i < ids.length; i++) {
			cotOrderOutService.updateFitDetailForDel(parseInt(ids[i]),
					function(res) {
					});
		}

		DWREngine.setAsync(true);

		var cord = fit_sm.getSelections();
		Ext.each(cord, function(item) {
					fit_ds.remove(item);
				});

		fitdetail_ds.reload();
		fit_ds.reload();
	}

	// 更新采购明细已出货数量
	function updateFitDetail() {

		fit_ds.each(function(rec) {

					var fitorderDetailId = rec.data.id;
					var factoryId = rec.data.facId;
					var outCurrent = rec.data.outCurrent;

					$('fitDetailId').value = fitorderDetailId;
					DWREngine.setAsync(false);
					if (fitorderDetailId != null && fitorderDetailId != '') {
						cotOrderOutService.updateFitDetail(
								parseInt(fitorderDetailId),
								parseInt(outCurrent), function(res) {
								});
					}
					DWREngine.setAsync(true);
				});
		fitdetail_ds.reload();
		fit_ds.reload();
	}
	// =========配件====end====================================

	// =========包材========================================
	// 判断该厂家是否已指定数量
	function checkIsPackFacExist(factoryId) {

		var flag = 0;
		pack_ds.each(function(rec) {
					if (rec.data.factoryId == factoryId) {
						flag++;
					}
				});
		return flag;
	}

	// 获得采购表格选择的记录
	function getPackIds() {
		var list = Ext.getCmp("packDetailGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var packorder = new VOrderPackorderId();

					packorder.id = item.id;
					packorder.factoryId = item.data.factoryId;
					packorder.packCount = item.data.packCount;
					var outHasOut = item.data.outHasOut;
					packorder.eleId = item.data.eleId;

					if (outHasOut == null || outHasOut == '') {
						packorder.outHasOut = 0;
					}

					res.push(packorder);
				});
		return res;
	}

	// 指定包材采购数量
	function allocateToPack() {

		var list = getPackIds();
		if (list.length == 0) {
			Ext.Msg.alert("Message", "Please select a purchase record");
			return;
		}
		for (var i = 0; i < list.length; i++) {
			var flag = checkIsPackFacExist(list[i].factoryId);

			if (flag == 0) {
				var obj = new Object();
				obj.eleId = list[i].eleId;
				obj.facId = list[i].factoryId;
				obj.outCurrent = parseInt(list[i].boxCount)
						- parseInt(list[i].outHasOut);
				obj.id = list[i].id;
				// 添加到表格中
				setObjToPackGrid(obj);
			}
		}
	}

	// 添加对象到表格
	function setObjToPackGrid(obj) {
		var u = new pack_ds.recordType(obj);
		pack_ds.add(u);
	}

	// 获得采购表格选择的记录
	function getChkPackNumIds() {
		var list = Ext.getCmp("packGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 删除表格选择的行
	function deletePackSelectRow() {

		var ids = getChkPackNumIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "plesase select a record！");
			return;
		}

		DWREngine.setAsync(false);
		for (var i = 0; i < ids.length; i++) {
			cotOrderOutService.updatePackDetailForDel(parseInt(ids[i]),
					function(res) {
					});
		}

		DWREngine.setAsync(true);

		var cord = pack_sm.getSelections();
		Ext.each(cord, function(item) {
					pack_sm.remove(item);
				});

		packdetail_ds.reload();
		pack_ds.reload();
	}

	// 更新采购明细已出货数量
	function updatePackDetail() {

		pack_ds.each(function(rec) {

					var packorderDetailId = rec.data.id;
					var factoryId = rec.data.factoryId;
					var outCurrent = rec.data.outCurrent;

					$('packDetailId').value = packorderDetailId;
					DWREngine.setAsync(false);
					if (packorderDetailId != null && packorderDetailId != '') {
						cotOrderOutService.updatePackDetail(
								parseInt(packorderDetailId),
								parseFloat(outCurrent), function(res) {
								});
					}
					DWREngine.setAsync(true);
				});
		packdetail_ds.reload();
		pack_ds.reload();
	}
		// =========包材====end====================================

});
