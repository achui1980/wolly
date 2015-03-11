Ext.onReady(function() {
	// cbm保留几位小数
	var cbmNum = getDeNum("cbmPrecision");
	var cbmStr = getDeStr(cbmNum);
	DWREngine.setAsync(false);
	var emps = null;
	var currencyMap = null;
	var record_start = 0;
	var statusMap = {
		"0" : "<font color='green'>未审核</font>",
		"1" : "<font color='red'>审核不通过</font>",
		"2" : "<font color='blue'>审核通过</font>",
		"3" : "<font color='#10418C'>请求审核</font>",
		"9" : "不审核"
	};
	var empsData = [];
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				emps = res;
				for (var p in emps) {
					empsData.push(["" + p, emps[p]]);
				}
			});
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				currencyMap = res;
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
							url : "cotorderfac.do?method=query&factoryId="+$('facId').value
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
							header : "Purchase Date",
							dataIndex : "orderTime",
							width : 100,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
								else
									return value;
							},
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "P.O No.",
							dataIndex : "orderNo",
							width : 180
						},{
							header : "Sales",
							dataIndex : "businessPerson",
							width : 100,
							renderer : function(value) {
								return emps[value]
							}
						}, {
							header : "审核状态",
							dataIndex : "orderStatus",
							width : 80,
							hidden:true,
							renderer : function(value) {
								return statusMap[value]
							}
						}, {
							header : "Amount",
							dataIndex : "totalMoney",
							summaryType : 'sum',
							width : 100
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 50,
							hidden:true,
							renderer : function(value) {
								return currencyMap[value]
							}
						}, {
							header : "交货日期",
							dataIndex : "sendTime",
							width : 100,
							hidden:true,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
								else
									return value;
							}
						}, {
							header : "Remark",
							dataIndex : "orderRemark",
							width : 170
						},  {
							header : "总数量",
							hidden:true,
							dataIndex : "totalCount",
							width : 100
						}, {
							header : "总箱数",
							hidden:true,
							dataIndex : "totalContainer",
							width : 100
						}, {
							header : "总体积",
							hidden:true,
							dataIndex : "totalCbm",
							width : 80
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