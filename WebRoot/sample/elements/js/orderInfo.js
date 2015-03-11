Ext.onReady(function() {
	DWREngine.setAsync(false);
	var currencyMap;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				currencyMap = res;
			});
		var boxMap;
	baseDataUtil.getBaseDicDataMap("CotBoxType", "id", "typeName", function(
					res) {
				boxMap = res;
			});
	var empsMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empsMap = res;
			});
	DWREngine.setAsync(true);

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([

	{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "orderTime",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "orderNo"
			}, {
				name : "bussinessPerson"
			}, {
				name : "customerShortName"
			}, {
				name : "boxCount"
			}, {
				name : "priceFac"
			}, {
				name : "priceFacUint"
			}, {
				name : "orderPrice"
			}, {
				name : "currencyId"
			}, {
				name : "totalMoney"
			},{name:"boxTypeId" },
			  {name:"orderCompose" },
			  {name:"orderStatus" }

	]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotelements.do?method=loadOrderInfo&eleId="
									+ $('eleId').value
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
					width : 100
				},
				columns : [sm, {
							header : "ID", 
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Date", 
							dataIndex : "orderTime",
							width : 100,
							renderer : function(value, meta, rec, rowIdx,
									colIdx, ds) {
								if (value != null)
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
							}
						}, {
							header : "W&C P/O", 
							dataIndex : "orderNo",
							width : 200
						}, {
							header : "Sales", 
							hidden:true,
							dataIndex : "bussinessPerson",
							width : 100,
							renderer : function(value) {
								return empsMap[value];
							}
						}, {
							header : " Client", 
							dataIndex : "customerShortName",
							width : 100
						}, {
							header : "Quantity", 
							dataIndex : "boxCount",
							width : 80,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0");
							}
						}, {
							header : "Purchase Price", 
							dataIndex : "priceFac",
							width : 80,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00")
							}
						}, {
							header : "Currency", 
							dataIndex : "priceFacUint",
							width : 60,
							renderer : function(value) {
								return currencyMap[value]
							}
						}, {
							header : "Sales Price", 
							dataIndex : "orderPrice",
							width : 80,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00")
							}
						}, {
							header : "Currency", 
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return currencyMap[value]
							}
						}, {
							header : "Amount", 
							dataIndex : "totalMoney",
							width : 100,
							renderer : function(value) {
								return Ext.util.Format.number(value, "0.00")
							}
						},{
							header:"Packing Way", //表头
							dataIndex:"boxTypeId", //数据列索引，对应于recordType对象中的name属性
							width:100,
							renderer : function(value) {
								return boxMap[value];
						    }
						},
						{
							header:"Price Terms", //表头
							dataIndex:"orderCompose", //数据列索引，对应于recordType对象中的name属性
							width:100
						},{
							header:"Status", //表头
							dataIndex:"orderStatus", //数据列索引，对应于recordType对象中的name属性
							width:80,
							renderer : function(value) {
								if (value == 0) {
									return "<font color='green'>Non-reviewed</font>";
								} else if (value == 1) {
									return "<font color='red'>Review without </font>";
								} else if (value == 2) {
									return "<font color='blue'>reviewed</font>";
								} else if (value == 3) {
									return "<font color='#10418C'>Need to be reviewed </font>";
								} else if (value == 9) {
									return "<font color='green'>Not review</font>";
								}
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	// 客户
	var customerBox = new BindCombox({
				dataUrl : "./servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
				cmpId : 'custId',
				emptyText : "Client",
				editable : true,
				valueField : "id",
				displayField : "customerShortName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 5,
				width:120,
				selectOnFocus : true,
				sendMethod : "post",				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'custId'
			});
	// 业务员
	var busiBox = new BindCombox({				
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPersonFind',
				emptyText : "Sales",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				width:120,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'businessPersonFind'
			});	
			
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
							emptyText : "The end of Time",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, customerBox,busiBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "W&C P/O",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}]
			});
	
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "orderGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border:false,
				viewConfig : {
					forceFit : false
				}
			});

	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	grid.on("rowdblclick",function(thisgrid,rowIdx){
		var rec = thisgrid.getStore().getAt(rowIdx);
		windowopen(rec.get("id"));
	});
});
// 打开订单编辑页面
	function windowopen(obj) {
		//查看的权限
		var isPopedom = getPopedomByOpType('cotorder.do',"SEL");
		if(isPopedom == 0)//查看的权限
		{
			alert("您没有查看的权限!");
			return;
		}
		openFullWindow('cotorder.do?method=addOrder&id=' + obj);
	}		