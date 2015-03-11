Ext.onReady(function() {
	// cbm保留几位小数
	var cbmNum = getDeNum("cbmPrecision");
	var cbmStr = getDeStr(cbmNum);
	var record_start = 0;

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
			}, {
				name : "totalCount"
			}, {
				name : "totalContainer"
			}, {
				name : "totalCbm",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "totalMoney",
				convert : numFormat.createDelegate(this, ["0.0"], 3)
			}, {
				name : "orderStatus"
			},{
				name:'orderRemark'
			},{
				name:'preNum'
			},{
				name:'artNum'
			},{
				name:'samNum'
			},{
				name:'qcNum'
			},{
				name:'shipNum'
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
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotfactory.do?method=queryOrderFacFen&factoryId="+$('facId').value
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
						},new Ext.grid.RowNumberer({
							header : "Sort No.",
							width : 55,
							renderer : function(value, metadata, record,
									rowIndex) {
								return record_start + 1 + rowIndex;
							}
						}), {
							header : "P.O No.",
							dataIndex : "orderNo",
							width : 180,
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "Pre-production",
							dataIndex : "preNum",
							width : 120,
							summaryType : 'average'
						},  {
							header : "Artwork",
							dataIndex : "artNum",
							width : 100,
							summaryType : 'average'
						}, {
							header : "Photo samples&Shipment samples",
							dataIndex : "samNum",
							width : 220,
							summaryType : 'average'
						}, {
							header : "QC",
							dataIndex : "qcNum",
							width : 80,
							summaryType : 'average'
						}, {
							header : "Shipment check&advice",
							dataIndex : "shipNum",
							width : 160,
							summaryType : 'average'
						}, {
							header : "Total",
							dataIndex : "id",
							width : 120,
							renderer : function(value, metaData, record, rowIndex,
								colIndex, store) {
									var preNum = record.data.preNum;
									var artNum = record.data.artNum;
									var samNum = record.data.samNum;
									var qcNum = record.data.qcNum;
									var shipNum = record.data.shipNum;
									var count=0;
									if(preNum!=0){
										count++;
									}
									if(artNum!=0){
										count++;
									}
									if(samNum!=0){
										count++;
									}
									if(qcNum!=0){
										count++;
									}
									if(shipNum!=0){
										count++;
									}
									return (preNum+artNum+samNum+qcNum+shipNum)/count;
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
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
				pageSize : 5,
				selectOnFocus : true,
				sendMethod : "post",
				width : 90,
				emptyText : "Sales",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'businessPerson'
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
							emptyText : "End Date",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, busiBox, {
							xtype : 'searchcombo',
							width : 90,
							emptyText : "P.O.No",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Print",
							handler : showPrint,
							iconCls : "page_print",
							cls : "SYSOP_PRINT"
						}, '-', {
							text : "View customer",
							handler : showCustInfo,
							iconCls : "group"

						}

				]
			});

			 // 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "orderfacGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : [summary],
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	viewport.doLayout();

	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	function showCustInfo() {
		var record = sm.getSelected();
		if (record == null) {
			Ext.Msg.alert("消息提示", "请选择一条记录");
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
		Ext.MessageBox.alert("提示信息", '您没有打印权限！');
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
// 打开编辑页面
function windowopenMod(obj) {
	openFullWindow('cotorderfac.do?method=add&id=' + obj);
}