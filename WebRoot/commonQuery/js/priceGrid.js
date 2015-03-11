// 报价记录表格导入
PriceGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	
	// 获取报价和订单的单价要保留几位小数
	var facNum = getDeNum("facPrecision");
	var deNum = getDeNum("orderPricePrecision");
	var facNumTemp ="0.000";
	if(facNum==0){
		facNumTemp = "0";
	}
	if(facNum==1){
		facNumTemp = "0.0";
	}
	if(facNum==2){
		facNumTemp = "0.00";
	}
	if(facNum==4){
		facNumTemp = "0.0000";
	}
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
				name : "customerShortName"
			}, {
				name : "priceTime",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "eleId"
			}, {
				name : "priceNo"
			}, {
				name : "custNo"
			}, {
				name : "pricePrice",
				convert : numFormat.createDelegate(this, [strNum],3)
			}, {
				name : "currencyId"
			}, {
				name : "boxTypeId"
			}, {
				name : "priceFac",
				convert : numFormat.createDelegate(this, [facNumTemp],3)
			}, {
				name : "priceFacUint"
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleNameEn"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
		paramNames:{
			start:0
		},
		baseParams : {
			limit : 15,
			custIdFind:cfg.custId
		},
		proxy : new Ext.data.HttpProxy({
			url : "cotquery.do?method=queryPriceAndDetail"
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
							header : "Client",
							dataIndex : "customerShortName",
							width : 100
						}, {
							header : "Date", // 表头
							dataIndex : "priceTime",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(value.time),
											"Y-m-d");
								}
							}
						}, {
							header : "Quote No.",
							dataIndex : "priceNo",
							width : 130
						}, {
							header : "Art No.",
							dataIndex : "eleId",
							width : 60
						}, {
							header : "Client",
							dataIndex : "custNo",
							width : 60
						}, {
							header : "Price",
							width : 80,
							dataIndex : "pricePrice",
							renderer : function(value) {
								return "<font color=red>"+value+"</font>";
							}
						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								if(value!=0){
									return "<font color=red>"+curData["" + value]+"</font>";
								}
							}
						}, {
							header : "Packing",
							dataIndex : "boxTypeId",
							width : 80,
							renderer : function(value) {
								return typeData["" + value];
							}
						}, {
							header : "P.O price",
							width : 80,
							hidden:hidePri,
							dataIndex : "priceFac",
							renderer : function(value) {
								if(hidePri==true){
									return "*";
								}else{
									return "<font color=green>"+value+"</font>";
								}
							}
						}, {
							header : "Currency",
							dataIndex : "priceFacUint",
							width : 60,
							hidden:hidePri,
							renderer : function(value) {
								if(hidePri==true){
									return "*";
								}else{
									if(value!=0){
										return "<font color=green>"+curData["" + value]+"</font>";
									}
								}
							}
						}, {
							header : "Size(cm)",
							dataIndex : "eleSizeDesc",
							width : 150
						}, {
							header : "name-en",
							dataIndex : "eleNameEn",
							width : 150
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
				priceBox.reset();
				priceBox.setLoadParams(null, null, rec.get("id"));
				//重新刷新数据
				priceBox.reflashData();
			},
			"afterbindvalue":function(combo,value){
				tb.searchField.onTrigger2Click();
				ds.load();
			}
		}
	});
	//单号下拉框
	var priceBox = new BindCombox({
		dataUrl : "cotquery.do?method=queryOrderByCust&table=price",
		cmpId : 'noFind',
		emptyText : "Quote No.",
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
				items : ['-',custBox,priceBox, {
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
							xtype : 'textfield',
							width : 95,
							emptyText : "Size(cm)",
							isSearchField : true,
							searchName : 'eleSizeDesc'
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
				//displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				//emptyMsg : "No data to display",
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
		queryService.findPriceDetailByIds(ids,function(res){
			if(res.length!=0){
				for(var i=0;i<res.length;i++){
					cfg.bar.insertByBatch(res[i],'price');
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
		priceBox.setLoadParams(null, null, cfg.custId);
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
	PriceGrid.superclass.constructor.call(this, con);
};
Ext.extend(PriceGrid, Ext.Panel, {});
