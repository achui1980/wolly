Ext.onReady(function() {
	// cbm保留几位小数
	var cbmNum = getDeNum("cbmPrecision");
	var cbmStr = getDeStr(cbmNum);
	DWREngine.setAsync(false);
	var fac = null;
	var emps = null;
	var currencyMap = null;
	var payData;
	var clauseData;
	var typeLv1Map;
	var statusMap = {
		"0" : "<font color='green'>Non-reviewed  </font>",
		"1" : "<font color='red'>Review without </font>",
		"2" : "<font color='blue'>reviewed</font>",
		"3" : "<font color='#10418C'>Need to be reviewed </font>",
		"9" : "Not review "
	};
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				fac = res;
			});
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				emps = res;
			});
//	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
//					res) {
//				currencyMap = res;
//			});
	// 加载付款方式表缓存
	baseDataUtil.getBaseDicDataMap("CotPayType", "id", "payName",
			function(res) {
				payData = res;
			});
	// 加载条款类型表缓存
	baseDataUtil.getBaseDicDataMap("CotClause", "id", "clauseName",
			function(res) {
				clauseData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeEnName", function(
					res) {
				typeLv1Map = res;
			});
	DWREngine.setAsync(true);

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "factoryId"
			}, {
				name : "orderNo"
			}, {
				name : "currencyId"
			}, {
				name : "orderTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "sendTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "businessPerson"
			},{
				name : "checkPerson"
			},{
				name : "totalMoney",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "orderStatus"
			},{
				name  :"orderId"
			},{
				name  :"piStatus"
			},{
				name : "poNo"
			},{
				name : "allPinName"
			},{
				name : "shipmentDate",
//				type:'jsondate'
				sortType : timeSortType.createDelegate(this)
			},{
				name : "payTypeId"
			},{
				name : "clauseTypeId"
			},{
				name:"orderLcDelay",
//				type:'jsondate'
				sortType : timeSortType.createDelegate(this)
			},{
				name:'typeLv1Id'
			},{
				name:'newRemark'
			}

	]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 300,
					canOut : 0,
					con : $('configValue').value
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorderfac.do?method=query"
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
					sortable : true
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Operation",
							dataIndex : "id",
							width : 80,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var mod = '<a href="javascript:windowopenMod('
										+ value + ')">Update</a>';
								var nbsp = "&nbsp &nbsp";
								var status = record.data.orderStatus;
								var del = '<a href="javascript:del(' + value
										+ ',' + status + ')">Delete</a>';
//								var rpt = '<a href="javascript:downRptFile(\''+ record.get("orderNo")
//										+ '\',\'orderfac\')">Report</a>';
								//var rpt = downRptFile(record.get("orderNo"),"orderfac");
								var pi = '<a href="javascript:openOrder(' + record.data.orderId
								+ ')">PI</a>';
								
								return mod + nbsp+pi
							}
						},{
							header : "orderId",
							dataIndex : "orderId",
							width : 50,
							hidden : true
						}, {
							header : "Sales",
							hidden:true,
							dataIndex : "businessPerson",
							width : 50,
							renderer : function(value) {
								return emps[value]
							}
						}, {
							header : "Department",
							width:80,
							dataIndex : "typeLv1Id",
							renderer : function(value) {
								return typeLv1Map[value];
							}
						},{
							header : "W&C P/O",
							dataIndex : "orderNo",
							width : 140
						}, {
							header : "ClientP/O",
							dataIndex : "poNo",
							width : 140
						}, {
							header : "P.O Date",
							dataIndex : "orderTime",
							width : 100,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d-m-Y");
								else
									return value;
							}
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return fac[value];
							}
						}, {
							header : "Product",
							dataIndex : "allPinName",
							width : 160
						}, {
							header : "P.O AMOUNT",
							dataIndex : "totalMoney",
							width : 100,
//							hidden:getPopedomByOpType("cotorder.do", "SELPIPRICE") == 0?true:false,
							renderer : Ext.util.Format.numberRenderer("0,0.00"),
							summaryType : 'sum',
							summaryRenderer : Ext.util.Format
									.numberRenderer("0,0.00")
						},{
							header : "Shipment",
							dataIndex : "shipmentDate",
							width : 100,
//							renderer:Ext.util.Format.dateRenderer("d-m-Y")
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d/m/Y");
								}
							}
						},{
							header : "ETA",
							dataIndex : "orderLcDelay",
							width : 80,
//							renderer:Ext.util.Format.dateRenderer("d-m-Y"),
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d/m/Y");
								}
							}
						}, {
							header : "Delivery Term",
							dataIndex : "clauseTypeId",
							width : 120,
							renderer : function(value) {
								return clauseData[value];
							}
						},{
							header : "Approve Person",
							dataIndex : "checkPerson",
							width : 120,
							renderer : function(value) {
								return  emps[value];
							}
						},{
							header : "Remark",
							dataIndex : "newRemark",
							width : 200,
							renderer:function(value){
								return '<a href="#">'+value+'</a>'
							}
						},{
							header : "Approval Status",
							dataIndex : "orderStatus",
							width : 80,
							hidden:true,
							renderer : function(value) {
								return statusMap[value]
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 300,
				store : ds,
				displayInfo : true,
				//displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|300|500',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 采购厂家
	var facComb = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&validUrl=cotfactory.do",
		cmpId : 'factoryId',
		emptyText : "Supplier",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		pageSize : 10,
		width : 90,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'factoryId'
	});
	
	// 审核状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'All'],[1, 'Waiting & Approval'],[2, 'Approved']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				editable : false,
				//value:1,
				emptyText : "Audit Status",
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width:100,
				listWidth : 220,
				hiddenName : 'orderStatus',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'orderStatus'
			});

	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPerson',
				editable : true,
				valueField : "id",
				displayField : "empsName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 10,
				selectOnFocus : true,
				sendMethod : "post",
				width : 90,
				emptyText : 'Sales',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'businessPerson'
			});
			
//	var themeBox = new BindCombox({
//				dataUrl : "./servlet/DataSelvert?tbname=CotCommisionType&key=commisionName",
//				cmpId : 'themes',
//				editable : true,
//				valueField : "id",
//				displayField : "commisionName",
//				mode : 'remote',// 默认local
//				autoLoad : false,// 默认自动加载
//				pageSize : 10,
//				selectOnFocus : true,
//				sendMethod : "post",
//				width : 120,
//				emptyText : 'Themes',
//				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//				listWidth : 350,// 下
//				triggerAction : 'all',
//				isSearchField : true,
//				searchName : 'themes'
//			});
			
	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				emptyText : "Currency",
				width : 80,
				isSearchField : true,
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				isSearchField : true,
				searchName : 'currencyId'
			});
    // 出货状态
	var chuStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'Running'], [2, 'Shipped']]
			});
	var chuBox = new Ext.form.ComboBox({
				name : 'canOut',
				editable : false,
				emptyText : "Invoice Status",
				store : chuStore,
				value : 0,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				hiddenName : 'canOut',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'canOut'
			});
	//大类
//	var typeLv1IdBox = new BindCombox({
//		dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
//		cmpId : 'typeLv1Id',
//		emptyText : "Department",
//		editable : true,
//		valueField : "id",
//		displayField : "typeEnName",
//		pageSize : 10,
//		width : 120,
//		sendMethod : "post",
//		selectOnFocus : false,
//		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//		listWidth : 350,// 下
//		triggerAction : 'all',
//		isSearchField : true,
//		searchName : 'typeLv1IdFind'
//	});
	var typeLv1IdBox = {
		xtype : "textfield",
		emptyText : "Department",
		width : 95,
		isSearchField : true,
		searchName : 'typeLv1IdFind'
	}
	// 出口公司
	var companyBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
				cmpId : 'companyId',
				emptyText : "Company",
				editable : true,
				valueField : "id",
				displayField : "companyShortName",
				pageSize : 10,
				width : 90,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'companyId'
			});
	
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', companyBox,{
							xtype : "datefield",
							emptyText : "Orders start",
							width : 100,
							format : "d-m-Y",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "Orders end",
							width : 100,
							format : "d-m-Y",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, facComb, busiBox,typeLv1IdBox, shenBox,chuBox, curBox,{
							xtype : 'textfield',
							width : 100,
							emptyText : "Themes",
							isSearchField : true,
							searchName : 'themeStr'
						},{
							xtype : "datefield",
							emptyText : "Start app.Date",
							width : 90,
							format : "d-m-Y",
							id : 'startCheckTime',
							vtype : 'daterange',
							endDateField : 'endCheckTime',
							isSearchField : true,
							searchName : 'startCheckTime'
						}, {
							xtype : "datefield",
							emptyText : "End app.Date",
							width : 90,
							format : "d-m-Y",
							id : 'endCheckTime',
							vtype : 'daterange',
							startDateField : 'startCheckTime',
							isSearchField : true,
							searchName : 'endCheckTime'
						},{
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Production contract number",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}
				],
				listeners:{
					render:function(){
						var tbar2 = new Ext.Toolbar({
										items:[ '->', {
										text : "Create",
										handler : windowopenAdd,
										hidden:true,
										iconCls : "page_add",
										cls : "SYSOP_ADD"
									}, '-', {
										text : "Update",
										handler : windowopenMod
												.createDelegate(this, [null]),
										iconCls : "page_mod",
										cls : "SYSOP_MOD"
									}, '-', {
										text : "Print",
										handler : showPrint,
										iconCls : "page_print",
										cls : "SYSOP_PRINT"
									}, 
			//						'-',
			//						{
			//							text : "Delete",
			//							handler : deleteBatch,
			//							iconCls : "page_del",
			//							cls : "SYSOP_DEL"
			//						}, 
									'-', {
										text : "View Client",
										handler : showCustInfo,
										iconCls : "group"
			
									}]
						})
						tbar2.render(grid.tbar);
					}
				}
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				id : "orderfacGrid",
				stripeRows : true,
				region:'center',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : summary,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : true,
					getRowClass : function(record, index) {
//						if (record.get("orderStatus") == 1)// 审核不通过
//							return "x-grid-record-red";
//						else if (record.get("orderStatus")==2)
//							return "x-grid-record-yellow";// 审核通过
//						else if (record.get("orderStatus")==3)
//							return "x-grid-record-qing";// 请求审核
//						DWREngine.setAsync(false);
						var approve = false;
//						cotOrderFacService.getApprove(record.get('id'),function(canApprove){
//							approve = canApprove;
//						});
						console.log("order_status:"+record.get('piStatus'));
						if(record.get('piStatus') != 2){
							return "x-grid-record-waitpi";
						}else{
						// 请求审核
							if (record.get("orderStatus") == 3) {
								return "x-grid-record-qing";
							}
							// 未审核
							if (record.get("orderStatus") == 0) {
								return "x-grid-record-noShen";
							}
						}
//						DWREngine.setAsync(true);

					}
				}
			});
			var colorPnl = new Ext.Panel({
		layout : 'hbox',
		region : 'north',
		height : 28,
		frame : true,
		layoutConfig : {
			//padding : '5',
			//pack : 'center',
			align : 'middle'
		},
		items : [{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:darkgray">&nbsp;&nbsp;</div>'
		},{
			xtype : "panel",
			width : 80,
			html : '<font color=blue>In Progress</font>'
		},{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:khaki">&nbsp;&nbsp;</div>'
		},{
			xtype : "panel",
			width : 120,
			html : '<font color=blue>Waiting Approval</font>'
		},{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:white">&nbsp;&nbsp;</div>'
		},{
			xtype : "panel",
			width : 80,
			html : '<font color=blue>Complete</font>'
		},{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:yellowgreen">&nbsp;&nbsp;</div>'
		},{
			xtype : "panel",
			width : 80,
			html : '<font color=blue>Wait for PI</font>'
		}]
	})
			
	var viewport = new Ext.Viewport({
				layout : "border",
				items : [colorPnl,grid]
			});
	viewport.doLayout();
	// //右键菜单
	// var rightMenu = new Ext.menu.Menu({
	// id:"rightMenu",
	// items:[{
	// text:"查看对应客户",
	// handler:showCustInfo
	// }]
	// });
	//
	// grid.on("rowcontextmenu", rightClickFn);
	//
	// function rightClickFn(client, rowIndex, e) {
	// alert("d6d")
	// //e.stopEvent();
	// e.preventDefault();
	// rightMenu.showAt(e.getXY());
	// }
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"),record.get("orderId"));
			});
	grid.on('cellclick', function(grid,  rowIndex,  columnIndex, e ) {
		var record = grid.getStore().getAt(rowIndex);
		if(columnIndex == 16){
			var win = new Ext.Window({
				title:'Remark',
				closeAction:'close',
				layout:'fit',
				width:500,
				height:400,
				items:[new OrderRemarkGrid({
					orderId:record.data.orderId,
					parentStore:grid.getStore(),
					module:'ORDERFAC'
				})]
			});
			win.show();
		}//remark列
	});
	function query() {
		ds.reload()
	}
	function showCustInfo() {
		var record = sm.getSelected();
		if (record == null) {
			Ext.Msg.alert("Message", "Please select a record");
			return;
		}
		var cfg = {
			orderfacId : record.get("id")
		}
		var custInfo = new OrderFacToCustWin(cfg);
		custInfo.show();
	}

});
// 显示打印面板
var printWin;
var showPrint = function(item) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", 'Sorry, you do not have Authority!');
		return;
	}
	if (printWin == null) {
		printWin = new PrintWin({
					type : 'orderfac'
				});
	}
	if (!printWin.isVisible()) {
		var po = item.getPosition();
		printWin.setPosition(po[0] - 200, po[1] + 25);
		printWin.show();
	} else {
		printWin.hide();
	}
};
// 打开外销合同编辑页面
function openOrder(orderId) {
	var isPopedom = getPopedomByOpType('cotorder.do', "SEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message",
				"You do not have permission to view PI!");
	} else {
		openFullWindow('cotorder.do?method=addOrder&id=' + orderId);
	}
}