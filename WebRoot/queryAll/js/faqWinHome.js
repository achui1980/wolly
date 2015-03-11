FaqWinHome = function(cfg) {
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
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 200,
					cgk : '2',
					startTime:showdate(-1)
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotfaq.do?method=queryHome"
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
					header : "W&C P.I",
					dataIndex : "orderNo",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var rpt = '<a href="javascript:openOrder('
								+ record.get("orderId") + ')">' + value
								+ '</a>';
						return rpt;
					},
					width : 100
				}, {
					header : "state",
					dataIndex : "flag",
					width : 100,
					renderer : function(value) {
						if (value == 1 || value == 11) {
							return "<font color='red'>Pre-Production</font>";
						} else if (value == 2) {
							return "<font color='blue'>Artwork Approval</font>";
						} else if (value == 3 || value == 33) {
							return "<font color='#10418C'>Shipment Samples</font>";
						}else if (value == 4) {
							return "<font color='blown'>QC</font>";
						}else if (value == 5) {
							return "<font color='green'>Shipment</font>";
						}else if (value == 9) {
							return "<font color='yellowgreen'>Payment</font>";
						}
					}
				},{
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
							return Ext.util.Format.date(new Date(value.time),
									"d-m-Y");
						}
					}
				}, {
					header : "Suppiler",
					dataIndex : "factoryId",
					width : 90,
					renderer : function(value) {
						return facData[value];
					}
				}, {
					header : "Client",
					dataIndex : "custId",
					width : 90,
					renderer : function(value) {
						return custData[value];
					}
				}, {
					header : "Product",
					dataIndex : "allPinName",
					width : 90
				}, {
					header : "Shipment",
					dataIndex : "orderLcDate",
					width : 80,
					renderer : function(value) {
						if (value != null) {
							return Ext.util.Format.date(new Date(value.year,
											value.month, value.day), "d-m-Y");
						}
					}
				}, {
					header : "Comment CN",
					dataIndex : "queryText",
					width : 280,
					renderer:function(value){
						if (value != null) {
							return value.replace(/\n/ig, "<br>");
						}
					}
				}]
	});

	var pageBar = new Ext.PagingToolbar({
				pageSize : 200,
				store : questionDs,
				displayInfo : true,
				displaySize : '5|10|15|200|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

//	var empBox = new BindCombox({
//		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
//		cmpId : 'queryPerson',
//		emptyText : "Sales",
//		valueField : "id",
//		displayField : "empsName",
//		pageSize : 10,
//		width : 100,
//		sendMethod : "post",
//		selectOnFocus : true,
//		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//		listWidth : 250,// 下
//		triggerAction : 'all',
//		isSearchField : true,
//		searchName : 'queryPerson'
//	});
			
			// Department
	var typeLv1IdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
				cmpId : 'typeLv1IdFind',
				emptyText : "Department",
				editable : true,
				valueField : "id",
				displayField : "typeEnName",
				pageSize : 10,
				width : 120,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'typeLv1IdFind'
			});

	// 哪个页签
	var stateStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'All State'], [1, 'Pre-Production'],
						[2, 'Artwork Approval'], [3, 'Shipment Samples'], [4, 'QC'], [5, 'Shipment'],[9, 'Payment'],
						[11, 'Password Pre-Production'],[33, 'Password Shipment Samples']]
			});
	var stateBox = new Ext.form.ComboBox({
				name : 'flag',
				emptyText : 'All State',
				editable : false,
				store : stateStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 120,
				hiddenName : 'flag',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'flag'
			});

	// faq状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'All Comments'], [2, 'Comment CN'], [3, 'Comment DK']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'cgk',
				emptyText : 'All Comments',
				editable : false,
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				value : 2,
				validateOnBlur : true,
				triggerAction : 'all',
				width : 90,
				hiddenName : 'cgk',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'cgk'
			});

	var toolBar = new Ext.ux.SearchComboxToolbar({
				items : [stateBox, shenBox, typeLv1IdBox,  {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 100,
							format : "d-m-Y",
							id : 'startTimeFaq',
							vtype : 'daterange',
							endDateField : 'endTimeFaq',
							isSearchField : true,
							value:new Date(showdate(-1)),
							searchName : 'startTimeFaq'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 100,
							format : "d-m-Y",
							id : 'endTimeFaq',
							vtype : 'daterange',
							startDateField : 'startTimeFaq',
							isSearchField : true,
							searchName : 'endTimeFaq'
						},{
							xtype : 'searchcombo',
							width : 100,
							emptyText : "P.I No.",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : questionDs
						}, '->', {
							text : "提问",
							hidden : true,
							handler : add,
							iconCls : "page_add"
						}, '-', {
							text : "Comment DK",
							handler : answer,
							iconCls : "page_table_row_insert"
						}, '-', {
							text : "Delete",
							handler : deleteQuestion,
							iconCls : "page_del"
						}]
			});
	var questionGrid = new Ext.grid.GridPanel({
				title : "Comment CN",
				id : "questionGrid",
				flex : 2.5,
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
								limit : 200
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
							width : 90,
							renderer : function(value) {
								return empData["" + value];
							}
						}, {
							header : "Date",
							dataIndex : "anwserTime",
							width : 100,
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
							width : 190,
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
				pageSize : 200,
				store : anwDs,
				displayInfo : true,
				displaySize : '5|10|15|200|all',
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
		cfg.pDs = questionDs;
		cfg.pId = null;
		cfg.questionId = recs[0].id;

		cfg.orderNo = recs[0].get("orderNo");
		cfg.factoryId = recs[0].get("factoryId");
		cfg.custId = recs[0].get("custId");
		cfg.allPinName = recs[0].get("allPinName");
		var orderLcDate = recs[0].get("orderLcDate");
		var date = Ext.util.Format.date(new Date(orderLcDate.year,
						orderLcDate.month, orderLcDate.day), "d-m-Y");
		cfg.orderLcDate = date;
		cfg.queryText = recs[0].get("queryText");
		var win = new AnswerWin(cfg);
		win.show();
	}

	function getPopedomByOpType(url, opType) {
		var result = 1;
		DWREngine.setAsync(false);
		cotPopedomService.getLoginEmpId(function(res) {
					if (res == "admin") {//admin用户不做权限判断
						return 1;
					} else {
						cotPopedomService.getPopedomByMenu(url, function(
										mapPopedom) {
									try {
										var chk = mapPopedom[opType];
										if (typeof(chk) == "undefined") {
											result = 0;
										}
									} catch (e) {
										return 0;
									}
								});
					}
				});
		DWREngine.setAsync(true);
		return result;
	}

	// 打开生产合同编辑页面
	window.openOrder = function(orderId) {
		var isPopedom = getPopedomByOpType('cotorder.do', "SEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message",
					"You do not have permission to view PI!");
		} else {
			openFullWindow('cotorder.do?method=addOrder&id=' + orderId);
		}
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
									if (typeof(cfg.dsPa) != 'undefined') {
										cfg.dsPa.reload();
									}
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
									questionDs.reload();
									if (typeof(cfg.dsPa) != 'undefined') {
										cfg.dsPa.reload();
									}
								});
					}
				});
	}
	
	var popedom = getPopedomByOpType('cotorderstatus.do','DAYREPORT');
	var con = {layout : "hbox",
		layoutConfig : {
			align : 'stretch'
		},
		items : [new Ext.Panel()]
	}
	if(popedom == 1){
		// 表单
		con = {
			layout : "hbox",
			layoutConfig : {
				align : 'stretch'
			},
			items : [questionGrid, anwGrid],
			listeners : {
				'afterrender' : function(pnl) {
					// 加载表格数据
					//				questionDs.load({
					//							params : {
					//								start : 0,
					//								limit : 20
					//							}
					//						});
				}
			}
		};
	}
		
	Ext.apply(con, cfg);
	FaqWinHome.superclass.constructor.call(this, con);
};
Ext.extend(FaqWinHome, Ext.Panel, {});