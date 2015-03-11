// 订单记录表格导入
OrderGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	// 获取报价和订单的单价要保留几位小数
	var deNum = getDeNum("orderPricePrecision");
	var strNum ="0.000";
	if(deNum==0){
		strNum = "0";
	}
	if(deNum==1){
		strNum = "0.0";
	}
	if(deNum==2){
		strNum = "0.00";
	}
	if(deNum==4){
		strNum = "0.0000";
	}
		
	// 加载币种表缓存
	DWREngine.setAsync(false);
	var curData;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	DWREngine.setAsync(true);
	
	// 加载包装方式
	DWREngine.setAsync(false);
	var typeData;
	baseDataUtil.getBaseDicDataMap("CotBoxType", "id", "typeName", function(
					res) {
				typeData = res;
			});
	DWREngine.setAsync(true);

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "orderTimeFac",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "customerShortName"
			}, {
				name : "empsName"
			}, {
				name : "eleId"
			}, {
				name : "eleNameEn"
			}, {
				name : "boxCount"
			}, {
				name : "totalMoney",
				convert : numFormat.createDelegate(this, [strNum],3)
			}, {
				name : "priceFac",
				convert : numFormat.createDelegate(this, [strNum],3)
			}, {
				name : "currencyId",
				type : "int"
			}, {
				name : "custNo"
			}, {
				name : "boxTypeId"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : 200
		},
		proxy : new Ext.data.HttpProxy({
			url : "cotquery.do?method=queryOrderFacAndDetail"
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
							header : "Date", // 表头
							dataIndex : "orderTimeFac",
							width : 80,
							sortable : true,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(value.year,
											value.month, value.day), "d-m-Y");
								}
							}
						}, {
							header : "Order No.",
							dataIndex : "orderNo",
							width : 130
						}, {
							header : "Sales",
							hidden:true,
							dataIndex : "empsName",
							width : 100
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 60
						}, {
							header : "Name",
							dataIndex : "eleNameEn",
							width : 60
						}, {
							header : "Price",
							width : 80,
							dataIndex : "priceFac",
							renderer : function(value) {
								return "<font color=red>"+value+"</font>";
							}
						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							hidden:true,
							renderer : function(value) {
								return "<font color=red>"+curData["" + value]+"</font>";
							}
						}, {
							header : "Packaging",
							dataIndex : "boxTypeId",
							width : 80,
							renderer : function(value) {
								return typeData["" + value];
							}
						}, {
							header : "Qty",
							dataIndex : "boxCount",
							width : 60
						}, {
							header : "Amount",
							dataIndex : "totalMoney",
							width : 100
						}]
			});
			
	var facGridBox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1&validUrl=cotfactory.do",
		cmpId : 'factoryFacId',
		labelSeparator : " ",
		editable : true,
		sendMethod : "post",
		autoLoad : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Supplier',
		pageSize : 10,
		width:100,
		tabIndex : 35,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'factoryFacId',
		listeners : {
			"select" : function(com, rec, index) {
				orderBox.reset();
				orderBox.setLoadParams(null, null, rec.get("id"));
				//重新刷新数据
				orderBox.reflashData();
			}		
		}
	});
	
	
	//单号下拉框
	var orderBox = new BindCombox({
		dataUrl : "cotquery.do?method=queryOrderFacByFac&table=orderfac",
		cmpId : 'noFind',
		emptyText : "Order No.",
		editable : true,
		valueField : "orderNo",
		displayField : "orderNo",
		pageSize : 10,
		width:100,
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
				items : ['-',facGridBox,orderBox, {
							xtype : "daterangefield",
							emptyText : "Start Date",
							width : 90,
							format : "Y-m-d",
							id : 'startTime',
							endDateId : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "daterangefield",
							emptyText : "End Date",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							startDateId : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						},{
							xtype : 'searchcombo',
							width : 95,
							emptyText : "Art No.",
							isSearchField : true,
							searchName : 'eleIdFind',
							isJsonType : false,
							store : ds
						},'->', {
							text : "Import",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function(){
								var list = getIds();
								if (list.length == 0) {
									Ext.MessageBox.alert("Message", 'Please select a record!');
									return;
								}
								mask();
								setTimeout(function(){
									insert();
								}, 500);
							}
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
			//	displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
			//	emptyMsg : "No data to display",
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
				bbar : toolBar,
				border:false,
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
			Ext.MessageBox.alert("Message", 'Please select a record!');
			unmask();
			return;
		}
		var ary = sm.getSelections();
		cfg.bar.insertByBatch(ary);
		//Ext.MessageBox.alert("Message", 'Import succeeded!');
	}

	// 表单
	var con = {
		title:'PO',
		layout : 'fit',
		width : 800,
		height : 490,
		modal:true,
		items : [grid]
	};

	Ext.apply(con, cfg);
	OrderGrid.superclass.constructor.call(this, con);
};
Ext.extend(OrderGrid, Ext.Window, {});
