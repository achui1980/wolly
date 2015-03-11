// 报价记录表格导入报价单
GivenGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "givenNo"
			}, {
				name : "givenTime"
			}, {
				name : "customerShortName"
			}, {
				name : "empsName"
			}, {
				name : "custRequiretime"
			}, {
				name : "givenAddr"
			}, {
				name : "eleId"
			}, {
				name : "eleNameEn"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				baseParams : {
					limit : 15,
					custIdFind : cfg.custId
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryGivenAndDetail"
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
					sortable : true,
					width : 50
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "客户",
							dataIndex : "customerShortName",
							width : 120
						}, {
							header : "下单日期", // 表头
							dataIndex : "givenTime",
							width : 100,
							sortable : true,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "送样单号",
							dataIndex : "givenNo",
							width : 150
						}, {
							header : "货号",
							dataIndex : "eleId",
							width : 100
						}, {
							header : "业务员",
							dataIndex : "empsName",
							width : 100
						}, {
							header : "最迟送样日期", // 表头
							dataIndex : "custRequiretime",
							width : 100,
							sortable : true,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "Y-m-d");
								}
							}
						}, {
							header : "送样地点",
							dataIndex : "givenAddr",
							width : 80
						}, {
							header : "英文名称",
							dataIndex : "eleNameEn",
							width : 160
						}]
			});

	// 客户名称
	var custBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
		cmpId : 'custIdFind',
		emptyText : "客户名称",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 10,
		width : 100,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custIdFind',
		listeners : {
			"select" : function(com, rec, index) {
				givenBox.reset();
				givenBox.setLoadParams(null, null, rec.get("id"));
				// 重新刷新数据
				givenBox.reflashData();
			}
		}
	});
	// 单号下拉框
	var givenBox = new BindCombox({
				dataUrl : "cotquery.do?method=queryOrderByCust&table=given",
				cmpId : 'noFind',
				emptyText : "送样单号",
				editable : true,
				valueField : "orderNo",
				displayField : "orderNo",
				pageSize : 10,
				width : 100,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'noFind'
			});

	// 样品表格顶部工具栏
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', custBox, givenBox, {
							xtype : "datefield",
							emptyText : "送样起始",
							width : 90,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "送样结束",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, {
							xtype : 'searchcombo',
							width : 95,
							emptyText : "货 号",
							isSearchField : true,
							searchName : 'eleIdFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "添加",
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

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	var grid = new Ext.grid.GridPanel({
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				tbar : tb,
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	this.load = function() {
		custBox.bindPageValue("CotCustomer", "id", cfg.custId);
		givenBox.setLoadParams(null, null, cfg.custId);
		// 加载表格
		ds.load({
					params : {
						start : 0
					}
				});
	}

	// 获得选择的记录
	var getIds = function() {
		var list = sm.getSelections();
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
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		var ary = sm.getSelections();
		cfg.bar.insertSelect(ary, 'given');
	}

	// 表单
	var con = {
		layout : 'fit',
		width : 600,
		height : 490,
		border : false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	GivenGrid.superclass.constructor.call(this, con);
};
Ext.extend(GivenGrid, Ext.Panel, {});
