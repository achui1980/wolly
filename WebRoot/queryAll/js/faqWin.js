FaqWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	DWREngine.setAsync(false);
	var empData, custData, facData;
	// 加载价格条款表缓存
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName",
			function(res) {
				custData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	DWREngine.setAsync(true);

	var questionRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "queryPerson"
			}, {
				name : "queryText"
			}, {
				name : "queryTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "orderId"
			}, {
				name : "flag"
			}, {
				name : "orderNo"
			}, {
				name : "allPinName"
			}, {
				name : "custId"
			}, {
				name : "factoryId"
			}, {
				name : 'orderLcDate'
			}]);

	// 创建数据源
	var questionDs = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotfaq.do?method=query&flag=" + cfg.flag
									+ "&orderId=" + cfg.orderId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, questionRecord)
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
							header : "PI No.",
							dataIndex : "orderNo",
							width : 90
						}, {
							header : "From",
							dataIndex : "queryPerson",
							width : 80,
							renderer : function(value) {
								return empData["" + value];
							}
						}, {
							header : "Date",
							dataIndex : "queryTime",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format
											.date(new Date(value.time),
													"d-m-Y");
								}
							}
						}, {
							header : "Suppiler",
							dataIndex : "factoryId",
							width : 80,
							renderer : function(value) {
								return facData[value];
							}
						}, {
							header : "Client",
							dataIndex : "custId",
							width : 80,
							renderer : function(value) {
								return custData[value];
							}
						}, {
							header : "Product",
							dataIndex : "allPinName",
							width : 80
						}, {
							header : "Shipment",
							dataIndex : "orderLcDate",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d-m-Y");
								}
							}
						}, {
							header : "Comment CN",
							dataIndex : "queryText",
							width : 210,
							renderer:function(value){
								if (value != null) {
									return value.replace(/\n/ig, "<br>");
								}
							}
						}]
			});

	var pageBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : questionDs,
				displayInfo : true,
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var toolBar = new Ext.Toolbar({
				items : ['->', {
							text : "Add",
							handler : add,
							iconCls : "page_add"
						}, '-', {
							text : "Answer",
							hidden : true,
							handler : answer,
							iconCls : "page_table_row_insert"
						}, {
							text : "Delete",
							handler : deleteQuestion,
							iconCls : "page_del"
						}]
			});
	var questionGrid = new Ext.grid.GridPanel({
				title : "Comment CN",
				id : "questionGrid",
				flex : 3,
				stripeRows : true,
				margins : "0 5 0 0",
				border : false,
				cls : 'rightBorder',
				store : questionDs, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : toolBar,
				bbar : pageBar,
				viewConfig : {
					forceFit : false
				},
				listeners : {
					rowdblclick : {
						// 表格双击
						fn : function(grid, rowIndex, event) {
							var record = grid.getStore().getAt(rowIndex);
							cfg.ds = questionDs;
							cfg.pId = record.get("id");
							var win = new QuestionWin(cfg);
							win.show();
							win.setFormValue(record);
						}
					}
				}
			});

	// 行点击时加载右边折叠面板表单
	sm.on('rowselect', function(model, rowIndex, record) {
				anwDs.on('beforeload', function(store, options) {
							anwDs.baseParams.questionId = record.id;
						});
				anwDs.load({
							params : {
								start : 0,
								limit : 20
							}
						});
			});

	// 答复表格
	var anwRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "anwserPerson"
			}, {
				name : "anwserText"
			}, {
				name : "anwserTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "questionId"
			}]);

	// 创建数据源
	var anwDs = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotfaq.do?method=queryAnsw"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, anwRecord)
			});
	// 创建复选框列
	var anwSm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var anwCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [anwSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "From",
							dataIndex : "anwserPerson",
							width : 80,
							renderer : function(value) {
								return empData["" + value];
							}
						}, {
							header : "Date",
							dataIndex : "anwserTime",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format
											.date(new Date(value.time),
													"d-m-Y");
								}
							}
						}, {
							header : "Comment DK",
							dataIndex : "anwserText",
							width : 140,
							renderer:function(value){
								if (value != null) {
									return value.replace(/\n/ig, "<br>");
								}
							}
						}]
			});
	var anwToolBar = new Ext.Toolbar({
				items : ['->', {
							text : "Delete",
							handler : deleteAnswer,
							iconCls : "page_del"
						}]
			});

	var anwPageBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : anwDs,
				displayInfo : true,
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var anwGrid = new Ext.grid.GridPanel({
				title : "Comment DK",
				id : "anwGrid",
				flex : 1,
				stripeRows : true,
				border : false,
				cls : 'leftBorder',
				store : anwDs, // 加载数据源
				cm : anwCm, // 加载列
				sm : anwSm,
				loadMask : true, // 是否显示正在加载
				tbar : anwToolBar,
				bbar : anwPageBar,
				viewConfig : {
					forceFit : false
				},
				listeners : {
					rowdblclick : {
						// 表格双击
						fn : function(grid, rowIndex, event) {
							var record = grid.getStore().getAt(rowIndex);
							cfg.ds = anwDs;
							var recs = sm.getSelections();
							if (recs.length != 1) {
								Ext.MessageBox.alert('Message',
										"Please choose one record!");
								return;
							} else {
								cfg.questionId = recs[0].id;
								cfg.pId = record.get("id");
								var win = new AnswerWin(cfg);
								win.show();
								win.setFormValue(record);
							}
						}
					}
				}
			});

	function add() {
		cfg.ds = questionDs;
		cfg.pId = null;
		var win = new QuestionWin(cfg);
		win.show();
	}
	function answer() {
		var recs = sm.getSelections();
		if (recs.length != 1) {
			Ext.MessageBox.alert('Message', "Please choose one record!");
			return;
		}
		cfg.ds = anwDs;
		cfg.pId = null;
		cfg.questionId = recs[0].id;
		var win = new AnswerWin(cfg);
		win.show();
	}

	function deleteQuestion() {
		Ext.MessageBox.confirm('Message',
				"Are you sure you delete these records?", function(btn) {
					if (btn == 'yes') {
						var recs = sm.getSelections();
						var ary = new Array();
						Ext.each(recs, function(item) {
									ary.push(item.id);
								});
						cotFaqService.deleteQuestion(ary, function(res) {
									questionDs.reload();
									cfg.dsPa.reload();
								});
					}
				});
	}
	function deleteAnswer() {
		Ext.MessageBox.confirm('Message',
				"Are you sure you delete these records?", function(btn) {
					if (btn == 'yes') {
						var recs = anwSm.getSelections();
						var ary = new Array();
						Ext.each(recs, function(item) {
									ary.push(item.id);
								});
						cotFaqService.deleteAnswer(ary, function(res) {
									anwDs.reload();
									cfg.dsPa.reload();
								});
					}
				});
	}

	// 表单
	var con = {
		title : cfg.orderNo + '--Comment CN',
		width : 1000,
		height : 420,
		frame : true,
		modal : true,
		layout : "hbox",
		layoutConfig : {
			align : 'stretch'
		},
		items : [questionGrid, anwGrid],
		listeners : {
			'afterrender' : function(pnl) {
				// 加载表格数据
				questionDs.load({
							params : {
								start : 0,
								limit : 20
							}
						});
			}
		}
	};
	Ext.apply(con, cfg);
	FaqWin.superclass.constructor.call(this, con);
};
Ext.extend(FaqWin, Ext.Window, {});