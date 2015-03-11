Ext.onReady(function() {
	DWREngine.setAsync(false);
	var empsMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empsMap = res;
			});
	var facMap;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	DWREngine.setAsync(true);

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "givenTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "givenNo"
			}, {
				name : "empId"
			}, {
				name : "customerShortName"
			}, {
				name : "factoryid"
			}, {
				name : "signRequire"
			}, {
				name : "realGiventime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "givenAddr"
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
							url : "cotelements.do?method=loadGivenInfo&eleId="
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
							dataIndex : "givenTime",
							width : 100,
							renderer : function(value, meta, rec, rowIdx,
									colIdx, ds) {
								if (value != null)
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
							}
						}, {
							header : "No.",
							dataIndex : "givenNo",
							width : 180
						}, {
							header : "Sales",
							dataIndex : "empId",
							width : 80,
							renderer : function(value) {
								return empsMap[value];
							}
						}, {
							header : "Client",
							dataIndex : "customerShortName",
							width : 100
						}, {
							header : "Supplier",
							dataIndex : "factoryid",
							width : 100,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "Remark",
							dataIndex : "signRequire",
							width : 200
						}, {
							header : "Sent Date",
							dataIndex : "realGiventime",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return value.year + "-" + (value.month + 1)
											+ "-" + value.day;
								}
							}
						}, {
							header : "Address",
							dataIndex : "givenAddr",
							width : 100
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
		emptyText : "Cilent",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 5,
		width:100,
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
				width:100,
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
						}, customerBox, busiBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "No.",
							isSearchField : true,
							searchName : 'givenNoFind',
							isJsonType : false,
							store : ds
						}]
			});
	var grid = new Ext.grid.GridPanel({
				id : "givenGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				border : false,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});

	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	grid.on("rowdblclick", function(thisgrid, rowIdx) {
				var rec = thisgrid.getStore().getAt(rowIdx);
				windowopen(rec.get("id"));
			});
});
function windowopen(obj) {
	// 查看的权限
	var isPopedom = getPopedomByOpType('cotgiven.do', "SEL");
	if (isPopedom == 0)// 查看的权限
	{
		alert("您没有查看的权限!");
		return;
	}
	openFullWindow('cotgiven.do?method=add&id=' + obj);
}