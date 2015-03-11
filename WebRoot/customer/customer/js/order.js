Ext.onReady(function() {
	var record_start = 0;
	/** ******EXT创建grid步骤******** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderTime",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "sendTime",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "orderNo"
			}, {
				name : "poNo"
			}, {
				name : "empsName"
			}, {
				name : "orderCompose"
			}, {
				name : "orderRate"
			}, {
				name : "orderStatus"
			}, {
				name : "totalCount"
			}, {
				name : "totalContainer"
			}, {
				name : "totalCBM",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}, {
				name : "totalMoney",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			},{
				name:'orderEarnest'
			},{
				name : 'orderLcDate',
				sortType : timeSortType.createDelegate(this)
			}]);
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
							url : "cotorder.do?method=query&custId="+$('custId').value+"&flag=customerPage"
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
				columns : [
						sm,// 添加复选框列
						{
							header : "编号", 
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
							header : "Shipment", 
							dataIndex : "orderLcDate",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "d/m/Y");
								}
							},
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "交货日期", 
							hidden:true,
							dataIndex : "sendTime",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "Y-m-d");
								}
							}
						}, {
							header : "W&C P/I.", 
							dataIndex : "orderNo",
							width : 120

						}, {
							header : "ClientP/O", 
							dataIndex : "poNo",
							width : 90
						}, {
							header : "Sales", 
							dataIndex : "empsName",
							width : 60
						}, {
							header : "Delivery Terms", 
							dataIndex : "orderCompose",
							width : 160
						}, {
							header : "汇率",
							hidden:true,
							dataIndex : "orderRate",
							width : 45
						}, {
							header : "审核状态", 
							dataIndex : "orderStatus",
							width : 80,
							hidden:true,
							renderer : function(value) {
								if (value == 0) {
									return "<font color='green'>未审核</font>";
								} else if (value == 1) {
									return "<font color='red'>审核不通过</font>";
								} else if (value == 2) {
									return "<font color='blue'>审核通过</font>";
								} else if (value == 3) {
									return "<font color='#10418C'>请求审核</font>";
								} else if (value == 9) {
									return "<font color='green'>不审核</font>";
								}
							}
						}, {
							header : "TotalCbm", 
							dataIndex : "orderEarnest",
							summaryType : 'sum',
							width : 80
						}, {
							header : "Quantity", 
							dataIndex : "totalCount",
							summaryType : 'sum',
							width : 60
						}, {
							header : "总箱数", 
							hidden:true,
							dataIndex : "totalContainer",
							summaryType : 'sum',
							width : 60
						}, {
							header : "总体积", 
							hidden:true,
							dataIndex : "totalCBM",
							summaryType : 'sum',
							width : 60
						}, {
							header : "Amount", 
							dataIndex : "totalMoney",
							summaryType : 'sum',
							width : 80
						}
						]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
			
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
				cmpId : 'businessPerson',
				emptyText : "Sales",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				sendMethod : "post",
				pageSize : 5,
				width:100,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 210,// 下
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
							width : 95,
							emptyText : "W&C P/O",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}]
			});
			
    // 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				id : "orderGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				plugins : [summary],
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border:false,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 构造
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});
	viewport.doLayout();

	// 表格双击
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

});
// 获得勾选的记录
function getIds() {
	var list = Ext.getCmp("orderGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
// 打开订单编辑页面
function windowopenMod(obj) {
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("提示消息", "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("提示消息", "只能选择一条记录!")
			return;
		} else
			obj = ids[0];

	}
	openFullWindow('cotorder.do?method=addOrder&id=' + obj);
}
