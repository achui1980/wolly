Ext.onReady(function() {

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "custId"
			}, {
				name : "custNo"
			}, {
				name : "custName"
			}, {
				name : "eleNameEn"
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
							url : "cotelements.do?method=loadCustNoInfo&eleId="
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
					sortable : true
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Client",
							dataIndex : "custName",
							width : 250
						}, {
							header : "Customer Art No.",
							dataIndex : "custNo",
							width : 250
						}, {
							header : "Description",
							dataIndex : "eleNameEn",
							width : 250
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}录',
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
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		emptyText : "Client",
		editable : true,
		isSearchField : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 5,
		selectOnFocus : true,
		sendMethod : "post",
		width : 150,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custId'
	});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', customerBox, {
							xtype : 'searchcombo',
							width : 1,
							cls : 'hideCombo',
							editable : false,
							isJsonType : false,
							store : ds
						}]
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "custNoGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border:false,
				viewConfig : {
					forceFit : true
				}
			});

	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
})