Ext.onReady(function() {
	var custBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custIdFind',
		emptyText : "Cust Name",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 10,
		width : 120,
		allowBlank : true,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custIdFind'
	});

	var emailBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustContact&key=contactEmail",
				cmpId : 'emailId',
				emptyText : "E-mail",
				editable : true,
				valueField : "id",
				displayField : "contactEmail",
				pageSize : 10,
				width : 120,
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'emailId'
			});

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
				name : "contactPerson"
			}, {
				name : "contactDuty"
			}, {
				name : "contactNbr"
			}, {
				name : "contactFax"
			}, {
				name : "contactEmail"
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
							url : "custcontact.do?method=query&custId="
									+ $('cid').value
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
							header : "Contact",
							dataIndex : "contactPerson",
							width : 100
						}, {
							header : "Duties",
							dataIndex : "contactDuty",
							width : 100
						}, {
							header : "Phone",
							dataIndex : "contactNbr",
							width : 200
						}, {
							header : "Fax",
							dataIndex : "contactFax",
							width : 200
						}, {
							header : "E-mail",
							dataIndex : "contactEmail",
							width : 280
						}, {
							header : "Operation",
							dataIndex : "id",
							width : 100,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var del = '<a href="javascript:del(' + value
										+ ')">Delete</a>';
								return del;
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'the {0} - {1}records total{2}records',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No records",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', custBox, emailBox, {
							xtype : 'searchcombo',
							width : 120,
							emptyText : "Contact",
							isSearchField : true,
							searchName : 'contactName',
							isJsonType : false,
							store : ds
						},  '->', {
							text : "Create",
							iconCls : "page_add",
							handler : windowopenAdd,
							cls : "SYSOP_ADD"
						}, '-', {
							text : "Update",
							iconCls : "page_mod",
							cls : "SYSOP_MOD",
							handler : windowopen.createDelegate(this, [null])
						}, '-', {
							text : "Delete",
							iconCls : "page_del",
							handler : deleteBatch,
							cls : "SYSOP_DEL"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				id : "contactGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			})

	function query() {
		if ($('cid').value == "" || $('cid').value == "null")
			ds.proxy.setUrl("custcontact.do?method=query");
		ds.reload();
	}
	grid.on("rowdblclick", function(grid, index) {
				var rec = ds.getAt(index);
				windowopen(rec.get("id"));
			})
});