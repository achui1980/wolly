// 报价记录表格导入报价单
OrderWin = function(cfg) {

	var _self = this;
	if (!cfg)
		cfg = {};

	// 加载币种表缓存
	DWREngine.setAsync(false);
	var facData;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	DWREngine.setAsync(true);

	var orderRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}]);
	// 创建数据源
	var _dorder = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=showOrderByFac&bPerson="
									+ cfg.businessPerson
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, orderRecord)
			});

	// 创建复选框列
	var order_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var order_cm = new Ext.grid.ColumnModel([order_sm, {
				header : "ID",
				dataIndex : "id",
				width : 50,
				sortable : true,
				hidden : true
			}, {
				header : " Order No.",
				dataIndex : "orderNo",
				width : 150,
				sortable : true

			}]);
	var order_toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : _dorder,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});

	var order_grid = new Ext.grid.GridPanel({
				region : "center",
				id : "orderGrid",
				stripeRows : true,
				bodyStyle : 'width:100%;',
				autoScroll : true,
				store : _dorder, // 加载数据源
				cm : order_cm,// 加载列
				sm : order_sm,
				loadMask : true, // 是否显示正在加载
				bbar : order_toolBar,
				viewConfig : {
					forceFit : true
				}
			});

	// 分页基本参数
	_dorder.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	var order_form = new Ext.form.FormPanel({
				labelWidth : 50,
				region : "north",
				labelAlign : "right",
				layout : "column",
				width : '100%',
				height : 40,
				id : "orderFormId",
				formId : "orderQueryForm",
				frame : true,
				keys : [{
							key : Ext.EventObject.ENTER,
							fn : function() {
								_dorder.reload();
							}
						}],
				items : [{
							xtype : "panel",
							columnWidth : 0.75,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "Order No.",
										name : 'noFind',
										anchor : "98%"
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.25,
							layout : "column",
							items : [{
										xytpe : "label",
										columnWidth : 0.1,
										text : " "
									}, {
										xtype : "button",
										text : "Search",
										width : 40,
										handler : function() {
											_dorder.reload();
										}
									}]
						}]
			})

	_dorder.on('beforeload', function() {
				_dorder.baseParams = order_form.getForm().getValues();
			});

	/** ****订单明细************************************************* */
	var detailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "factoryId"
			}, {
				name : "containerCount"
			}, {
				name : "boxCount"
			}, {
				name : "unBoxCount4OrderFac"
			}]);
	// 创建数据源
	var _detail = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryOrderToOrderFac"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, detailRecord)
			});

	// 创建复选框列
	var detail_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var detail_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [detail_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : " Art No.",
							dataIndex : "eleId",
							width : 80
						}, {
							header : " Supplier's Art No.",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return facData[value];
							}
						}, {
							header : " CBMS",
							dataIndex : "containerCount",
							width : 50
						}, {
							header : " QTYS",
							dataIndex : "boxCount",
							width : 50
						}, {
							header : "Not purchase quantity",
							dataIndex : "unBoxCount4OrderFac",
							width : 90,
							renderer : function(value) {
								return "<font color=red>" + value + "</font>";
							}
						}]
			});
	var detail_toolBar = new Ext.PagingToolbar({
				pageSize : 10,
				store : _detail,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var detail_tb = new Ext.Toolbar({
				items : ['->', {
							text : "Import",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function() {
								// mask();
								setTimeout(function() {
											insert();
										}, 500);
							}
						}]
			});
	var detail_grid = new Ext.grid.GridPanel({
				region : "center",
				id : "detailGrid",
				stripeRows : true,
				height : 200,
				bodyStyle : 'width:100%',
				store : _detail, // 加载数据源
				cm : detail_cm, // 加载列
				sm : detail_sm,
				loadMask : true, // 是否显示正在加载
				tbar : detail_tb,
				bbar : detail_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 分页基本参数
	// _dsign.load({params:{start:0, limit:10}});
	_detail.load({
			params : {
				start : 0,
				limit : 10
			}
		});
//	_detail.on('beforeload', function() {
//		_detail.baseParams = detail_form.getForm().getValues();
//	});
	order_grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				_detail.on('beforeload', function() {
					_detail.baseParams = detail_form.getForm().getValues();
					_detail.baseParams.orderIdFind =  record.id;
					_detail.baseParams.factoryId =  cfg.factoryId;
				});
				_detail.reload();
				
			});

	var detail_form = new Ext.form.FormPanel({

				labelWidth : 60,
				region : "north",
				labelAlign : "right",
				layout : "column",
				height : 40,
				width : '100%',
				id : "detailFormId",
				formId : "detailQueryForm",
				frame : true,
				keys : [{
							key : Ext.EventObject.ENTER,
							fn : function() {
								_detail.reload();
							}
						}],
				items : [{
							xtype : "panel",
							columnWidth : 0.45,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "Art No.",
										name : 'eleIdFind',
										anchor : "98%"
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.15,
							layout : "column",
							items : [{
										xtype : "button",
										text : "Search",
										width : 40,
										handler : function() {
											_detail.reload();
										}
									}]
						}, {
							xtype : "panel",
							columnWidth : 0.4,
							layout : "form",
							items : [{
										xtype : "checkbox",
										name : "otherFac",
										id : "otherFac",
										labelWidth : 1,
										boxLabel : "Other suppliers",
										anchor : "100%",
										labelSeparator : " "
									}]
						}]

			})


	var order = new Ext.Panel({
				region : 'center',
				border : false,
				width : 300,
				layout : 'border',
				items : [order_form, order_grid]
			})

	var detail = new Ext.Panel({
				region : 'east',
				border : false,
				width : '70%',
				layout : 'border',
				items : [detail_form, detail_grid]
			})

	this.load = function() {
		// 将查询面板的条件加到ds中
		_dorder.on('beforeload', function(store, options) {
					// ds.baseParams.bPerson = cfg.businessPerson;
				});
		// 加载表格
		_dorder.load({
					params : {
						start : 0,
						limit : 15
					}
				});
	}

	// 获得选择的记录
	var getIds = function() {
		var list = detail_sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 添加
	function insert() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Please select records！');
			return;
		}
		var ary = detail_sm.getSelections();
		cfg.bar.insertSelect(ary);
		Ext.MessageBox.alert("Message", 'Import successful！');
	}

	// 表单
	var con = {
		title : 'Imported from the export contract',
		layout : 'border',
		width : 800,
		height : 500,
		border : false,
		modal : true,
		items : [order, detail]
	};

	Ext.apply(con, cfg);
	OrderWin.superclass.constructor.call(this, con);
};
Ext.extend(OrderWin, Ext.Window, {});
