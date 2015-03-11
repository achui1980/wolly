// 报价记录表格导入报价单
HisPriceGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	// 全局变量
	// this.custId;
	// this.eleId;
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "orderNo"
			}, {
				name : "customerShortName"
			}, {
				name : "orderPrice",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "orderTime"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				baseParams : {
					limit : 15,
					custId:cfg.custId
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryOrderPriceInfo&eleId="+cfg.eleId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});

	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [{
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "Item No.",
							dataIndex : "eleId",
							width : 150
						}, {
							header : "Offer No.",
							dataIndex : "orderNo",
							width : 180
						}, {
							header : "Client",
							dataIndex : "customerShortName",
							width : 150
						}, {
							header : "Price History",
							dataIndex : "orderPrice",
							width : 120
						}, {
							header : "Price Time",
							dataIndex : "orderTime",
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
								else
									return value;
							},
							width : 130
						}]
			});
			
	// 客户名称
	var custBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		emptyText : "Customer Name",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 10,
		width:100,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custId'
	});

	// 样品表格顶部工具栏
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 90,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, custBox,{
							xtype : 'searchcombo',
							width : 1,
							cls : 'hideCombo',
							editable : false,
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							hidden:true,
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function() {
								var list = getIds();
								if (list.length == 0) {
									Ext.MessageBox.alert("Message", 'Pls. select one record！');
									return;
								}
								mask();
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
				emptyMsg : "No records",
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
				loadMask : true,
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

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
			Ext.MessageBox.alert("Message", 'Pls. select one record！');
			return;
		}
		var ary = sm.getSelections();
		cfg.bar.insertSelect(ary);
	}

	this.load = function() {
		custBox.bindPageValue('CotCustomer','id',cfg.custId);
		// 加载表格
		ds.load({
					params : {
						start : 0
					}
				});

	}
	grid.on("rowdblclick", cfg.dblFn);
	// 表单
	var con = {
		layout : 'fit',
		border : false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	HisPriceGrid.superclass.constructor.call(this, con);
};
Ext.extend(HisPriceGrid, Ext.Panel, {});
