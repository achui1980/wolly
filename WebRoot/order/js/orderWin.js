OrderWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	/** ******EXT创建grid步骤******** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "eleName"
			}, {
				name : "boxCount"
			}, {
				name : "containerCount"
			}, {
				name : "boxPbCount"
			}, {
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
			}, {
				name : "boxRemark"
			}, {
				name : "boxTypeId"
			}, {
				name : "fitCount",
				type : 'int',
				defaultValue : 1
			}, {
				name : "orderId"
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ds = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotquery.do?method=queryOrderDetail&orderId="
								+ cfg.orderId
					},
					listeners : {
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("Message", "Operation failed！");
							} else {
								ds.reload();
							}
							unmask();
						},
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							mask();
						}
					}
				}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, roleRecord),
		writer : writer
	});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [sm, {
							header : "Sort No.",
							dataIndex : "id",
							width : 50,
							sortable : true,
							hidden : true
						}, {
							header : "Art No.",
							width : 100,
							dataIndex : "eleId"
						}, {
							header : "Cust.No.",
							width : 100,
							hidden : true,
							dataIndex : "custNo"
						}, {
							header : "Description",
							width : 130,
							dataIndex : "eleName"
						}, {
							header : "Quantity",
							width : 80,
							dataIndex : "boxCount"
						}, {
							header : "<font color=blue>Quantity</font>",
							width : 70,
							dataIndex : "fitCount",
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "orderId",
							dataIndex : "orderId",
							hidden : true
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Create",
							handler : addToGrid,
							iconCls : "page_add"
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				clicksToEdit : 1,
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 行双击直接添加一行
	grid.on("rowdblclick", function(grid, rowIndex, e) {
				var fitAnysGrid = Ext.getCmp("fitAnysGrid");
				var fitDs = fitAnysGrid.getStore();
				var item = ds.getAt(rowIndex);
				var fitCount = item.data.fitCount;
				for (var i = 0; i < fitCount; i++) {
					var u = new fitDs.recordType({
								eleId : item.data.eleId,
								eleName : item.data.eleName,
								anyFlag : "U",
								fitNo : "",
								fitName : "",
								fitDesc : "",
								boxCount : item.data.boxCount,
								fitUsedCount : "",
								fitCount : "",
								fitBuyUnit : "",
								orderFitCount : "",
								facId : "",
								fitPrice : "",
								totalAmount : "",
								remark : "",
								fittingId : "",
								orderId : item.data.orderId,
								orderdetailId : item.id
							});
					fitDs.add(u);
				}
			});

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	// 添加到分析表格中
	function addToGrid() {
		var fitAnysGrid = Ext.getCmp("fitAnysGrid");
		var fitDs = fitAnysGrid.getStore();
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					var fitCount = item.data.fitCount;
					for (var i = 0; i < fitCount; i++) {
						var u = new fitDs.recordType({
									eleId : item.data.eleId,
									eleName : item.data.eleName,
									anyFlag : "U",
									fitNo : "",
									fitName : "",
									fitDesc : "",
									boxCount : item.data.boxCount,
									fitUsedCount : "",
									fitCount : "",
									fitBuyUnit : "",
									orderFitCount : "",
									facId : "",
									fitPrice : "",
									totalAmount : "",
									remark : "",
									fittingId : "",
									orderId : item.data.orderId,
									orderdetailId : item.id
								});
						fitDs.add(u);
					}
				});
	}

	var con = {
		width : 420,
		height : 400,
		title : "选择需要新增配件的订单明细",
		modal : true,
		layout : 'fit',
		items : [grid]
	}
	Ext.apply(con, cfg);
	OrderWin.superclass.constructor.call(this, con);
};

Ext.extend(OrderWin, Ext.Window, {});