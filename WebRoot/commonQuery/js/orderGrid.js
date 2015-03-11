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
				name : "orderTime",
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
				name : "orderPrice",
				convert : numFormat.createDelegate(this, [strNum],3)
			}, {
				name : "currencyId"
			}, {
				name : "custNo"
			}, {
				name : "boxTypeId"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
		baseParams : {
			limit : 15,
			custIdFind:cfg.custId
		},
		proxy : new Ext.data.HttpProxy({
			url : "cotquery.do?method=queryOrderAndDetail"
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
							dataIndex : "orderTime",
							width : 80,
							sortable : true,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(value.time),
											"Y-m-d");
								}
							}
						}, {
							header : "Order No.",
							dataIndex : "orderNo",
							width : 130
						}, {
							header : "Client",
							dataIndex : "customerShortName",
							width : 100
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
							header : "Cust No.",
							dataIndex : "custNo",
							width : 60
						}, {
							header : "Name",
							dataIndex : "eleNameEn",
							width : 60
						}, {
							header : "Price",
							width : 80,
							dataIndex : "orderPrice",
							renderer : function(value) {
								return "<font color=red>"+value+"</font>";
							}
						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return "<font color=red>"+curData["" + value]+"</font>";
							}
						}, {
							header : "Packing",
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
			
	// 客户名称
	var custBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custIdFind',
		emptyText : "Client",
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
		searchName : 'custIdFind',
		listeners : {
			"select" : function(com, rec, index) {
				orderBox.reset();
				orderBox.setLoadParams(null, null, rec.get("id"));
				//重新刷新数据
				orderBox.reflashData();
			},
			"afterbindvalue":function(combo,value){
				tb.searchField.onTrigger2Click();
				ds.load();
			}
		}
	});
	//单号下拉框
	var orderBox = new BindCombox({
		dataUrl : "cotquery.do?method=queryOrderByCust&table=order",
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
				items : ['-',custBox,orderBox, {
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
		var ary = sm.getSelections();
		var ids='';
		Ext.each(ary, function(res) {
				ids+=res.data.id+",";
				});
		//查找所有报价明细
		DWREngine.setAsync(false);
		queryService.findOrderDetailByIds(ids,function(res){
			if(res.length!=0){
				for(var i=0;i<res.length;i++){
					cfg.bar.insertByBatch(res[i],'order');
				}
			}
		});
		DWREngine.setAsync(true);
		if(cfg.type=='order'){
			cfg.bar.priceBlur();
		}
		unmask();
	}

	this.load = function() {
		custBox.bindPageValue("CotCustomer", "id", cfg.custId);
		orderBox.setLoadParams(null, null, cfg.custId);
		ds.load({
					params : {
						start : 0
					}
				});
	}

	// 表单
	var con = {
		layout : 'fit',
		width : 600,
		height : 490,
		border:false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	OrderGrid.superclass.constructor.call(this, con);
};
Ext.extend(OrderGrid, Ext.Panel, {});
