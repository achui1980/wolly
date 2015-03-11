Ext.onReady(function() {

	// 加载表格需要关联的外键名
	var facMap;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});

	var busiMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				busiMap = res;
			});

	var givenRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "givenNo"
			}, {
				name : "givenTime",
				sortType : timeSortType.createDelegate(this)
			}, {

				name : "empsName"
			}, {
				name : "custRequiretime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "givenIscheck",
				type : "int"
			}, {
				name : "realGiventime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "givenAddr"
			}, {
				name : "givenStatus",
				type : "int"
			}]);
	// 创建数据源
	var _dgiven = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotgiven.do?method=query&custId="
									+ $('custId').value + "&flag=customerPage"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, givenRecord)
			});

	// 创建复选框列
	var given_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var given_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [given_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Sort No.",
							dataIndex : "givenNo",
							width : 140
						}, {
							header : "Offer Date",
							dataIndex : "givenTime",
							width : 140,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), 'Y-m-d');
								}
							}
						}, {
							header : "Sales",
							dataIndex : "Sales",
							width : 100
						}, {
							header : "Valid Date",
							dataIndex : "custRequiretime",
							width : 140,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
								}
							}
						}, {
							header : "Status",
							dataIndex : "givenIscheck",
							width : 80,
							renderer : function(value) {
								if (value == 0) {
									return "<font color='green'>Not review</font>";
								} else if (value == 1) {
									return "<font color='red'>Review without</font>";
								} else if (value == 2) {
									return "<font color='blue'>reviewed</font>";
								} else if (value == 3) {
									return "<font color='#10418C'>Need to be reviewed</font>";
								} else if (value == 9) {
									return "<font color='green'>Not review</font>";
								}
							}
						}, {
							header : "Delivery Date",
							dataIndex : "realGiventime",
							width : 140,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
								}
							}
						}, {
							header : "Delivery Address",
							dataIndex : "givenAddr",
							width : 100
						}, {
							header : "Completed",
							dataIndex : "givenStatus",
							width : 100,
							renderer : function(value) {
								if (value == 0) {
									return "<span style='color:red;'>Completed</span>";
								} else if (value == 1) {
									return "<span style='color:green;'>Not Complete</span>";
								}
							}
						}]
			});
	var given_toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : _dgiven,
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
			
	// 业务员
	var busiBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'bussinessPerson',
		emptyText : "Sales",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		pageSize : 5,
		sendMethod : "post",
		width:120,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 210,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'bussinessPerson'
	});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 100,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "the end of time",
							width : 100,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, busiBox,{
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Delivery No",
							isSearchField : true,
							searchName : 'givenNoFind',
							isJsonType : false,
							store : _dgiven
						}]
			});

	var given_grid = new Ext.grid.GridPanel({
				id : "givenGrid",
				stripeRows : true,
				store : _dgiven, // 加载数据源
				cm : given_cm,// 加载列
				sm : given_sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border : false,
				bbar : given_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 单击修改信息 start
	given_grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	var vp = new Ext.Viewport({
				layout : 'fit',
				items : [given_grid]
			})
})

// 获得表格选择的记录
function getGivenIds() {
	var list = Ext.getCmp("givenGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotGiven = new CotGiven();
				cotGiven.id = item.id;
				res.push(cotGiven);
			});
	return res;
}

// 打开编辑页面
function windowopenMod(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
		return;
	}
	if (obj == null) {
		var ids = getGivenIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Please select a records");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("Message", "Sorry,you can select only one records!")
			return;
		} else {
			obj = ids[0].id;
		}
	}
	// 查询该送样单是否被删除
	cotGivenService.getGivenById(obj, function(res) {
				if (res != null) {
					openFullWindow('cotgiven.do?method=add&id=' + obj);
				} else {
					reloadGrid("givenGrid");
					Ext.Msg.alert("Message", "Sorry,The item have been deleted!");
				}
			})
}