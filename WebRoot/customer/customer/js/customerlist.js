Ext.onReady(function() {
	DWREngine.setAsync(false);
	var empsMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empsMap = res;
			});
	var nationMap;
	baseDataUtil.getBaseDicDataMap("CotNation", "id", "nationName",
			function(res) {
				nationMap = res;
			});
	var proMap;
	baseDataUtil.getBaseDicDataMap("CotProvince", "id", "provinceName",
			function(res) {
				proMap = res;
			});
	DWREngine.setAsync(true);
	var nationBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotNation&key=nationName",
				cmpId : 'nationId',
				emptyText : "Country",
				editable : true,
				valueField : "id",
				displayField : "nationName",
				pageSize : 10,
				width : 100,
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'nationId'
			});
	var empBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'eId',
		emptyText : "Sales",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		pageSize : 10,
		width : 100,
		allowBlank : true,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'eId'
	});

	// 客户类型
	var custTypeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustomerType&key=typeName",
				cmpId : 'custTypeId',
				emptyText : "Client Type",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				pageSize : 10,
				width : 100,
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 27,
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'custTypeId'
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
				name : "customerNo"
			}, {
				name : "customerShortName"
			}, {
				name : "fullNameEn"
			}, {
				name : "fullNameCn"
			}, {
				name : "contactNbr"
			}, {
				name : "customerFax"
			}, {
				name : "custTypeId",
				type : "int"
			}, {
				name : "customerEmail"
			}, {
				name : "empId"
			}, {
				name : "nationId"
			}, {
				name : "provinceId"
			},{
				name:'customerAddrEn'
			},{
				name:'addTime'
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
							url : "cotcustomer.do?method=query"
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
				columns : [sm,{
							header : "Operation",
							dataIndex : "id",
							width : 80,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var del = '<a href="javascript:del(' + value
										+ ')">Delete</a>';
								return del;
							}
						}, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						},{
							header : "Add Time",
							dataIndex : "addTime",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), "Y-m-d");
								}
							}
						},{
							header : "No.",
							dataIndex : "customerNo",
							width : 120
						}, {
							header : "Short Title",
							dataIndex : "customerShortName",
							width : 100
						}, {
							header : "Name",
							dataIndex : "fullNameEn",
							width : 150
						},{
							header : "Sales",
							dataIndex : "empId",
							width : 90,
							renderer : function(value) {
								return empsMap[value];
							}
						}, {
							header : "Tel.",
							dataIndex : "contactNbr",
							width : 110
						}, {
							header : "Fax",
							dataIndex : "customerFax",
							width : 110
						}, {
							header : "Email",
							dataIndex : "customerEmail",
							width : 130
						},  {
							header : "Country",
							dataIndex : "nationId",
							width : 90,
							renderer : function(value) {
								return nationMap[value];
							}
						},{
							header : "Provinces",
							dataIndex : "provinceId",
							width : 90,
							hidden:true,
							renderer : function(value) {
								return proMap[value];
							}
						},{
							header : "Address",
							dataIndex : "customerAddrEn",
							width : 110
						}]
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

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : 'textfield',
							width : 95,
							emptyText : "Name",
							isSearchField : true,
							searchName : 'custShortName'
						}, nationBox, empBox, {
							xtype : "searchcombo",
							emptyText : "Cust No.",
							width : 95,
							isSearchField : true,
							searchName : 'custNo',
							isJsonType : false,
							store : ds
						}, '->', {
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
							text : "Del",
							iconCls : "page_del",
							cls : "SYSOP_DEL",
							handler : deleteBatch
						}, '-', {
							text : "Export",
							iconCls : "page_excel",
							cls : "SYSOP_EXP",
							handler : exportSelectCust
						}]
			});
	var grid = new Ext.grid.GridPanel({
				id : "custGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : true
				}
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	grid.on("rowdblclick", function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopen(record.get("id"));
			});

	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.Msg.alert("Message", "Please select a record");
			return;
		}
		Ext.MessageBox.confirm('Message', "Are you sure you want to delete", function(btn) {
					if (btn == 'yes') {
						DWREngine.setAsync(false);
						var list = getIds();
						var num = 0;
						for (var i = 0; i < list.length; i++) {
							var id = list[i].id;
							cotCustomerService.deleteCustomerById(id, function(
											res) {
										if (res == -1) {
											num++;
										}
									});
						}
						if (num > 0) {
							Ext.Msg.alert("Message", "Deleted successfully!have" + num
											+ "can not be deleted!");
						} else {
							Ext.Msg.alert("Message", "Deleted successfully!");
						}
						reloadGrid('custGrid');
						DWREngine.setAsync(true);
					}
				});

	}

	// 导出选择的客户
	function exportSelectCust() {
		var list = sm.getSelections();
		var str = "";
		for (var i = 0; i < list.length; i++) {
			var rec = list[i];
			str += rec.id;
			if (i < list.length - 1)
				str += ","
		}
		if (str == "") {
			Ext.Msg.alert("Message", "Please select to export client!");
			return;
		}
		downRpt("./exportCust.action?flag=1&ids=" + str);
	}

});
function windowopenAdd() {
	openWindowBase(550, 800, 'cotcustomer.do?method=add');
}
function getIds() {
	var list = Ext.getCmp("custGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotCustomer = new CotCustomer();
				cotCustomer.id = item.id
				res.push(cotCustomer);
			});
	return res;
}

function windowopen(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("Message", " Sorry, you do not have Authority!");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("Message", "You can only select a record!")
			return;
		}
		obj = ids[0].id;
	}
	openWindowBaseTop(660, 1020, 'cotcustomer.do?method=modify&id=' + obj);
}