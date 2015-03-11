Ext.onReady(function() {
			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "visitPeople"
					}, {
						name : "visitDuty"
					}, {
						name : "visitTime",
						sortType : timeSortType.createDelegate(this)
					}, {
						name : "visitReason"
					}, {
						name : "visitNo"
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
									url : "customervisitedlog.do?method=query&custId="
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
									header : "Visitor",
									dataIndex : "visitPeople",
									width : 100
								}, {
									header : "Duties",
									dataIndex : "visitDuty",
									width : 100
								}, {
									header : "Date",
									dataIndex : "visitTime",
									width : 150,
									renderer : function(value) {
										return value.year + "-"
												+ (value.month + 1) + "-"
												+ value.day;
									}
								}, {
									header : "Reasons for visit",
									dataIndex : "visitReason",
									width : 400
								}, {
									header : "Num",
									dataIndex : "visitNo",
									width : 80
								}, {
									header : "Opration",
									dataIndex : "id",
									width : 100,
									renderer : function(value, metaData,
											record, rowIndex, colIndex, store) {
										var del = '<a href="javascript:del('
												+ value + ')">Delete</a>';
										return del;
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
									emptyText : "End Date",
									width : 100,
									format : "Y-m-d",
									id : 'endTime',
									vtype : 'daterange',
									startDateField : 'startTime',
									isSearchField : true,
									searchName : 'endTime'
								}, {
									xtype : 'searchcombo',
									width : 1,
									cls : 'hideCombo',
									editable : false,
									isJsonType : false,
									store : ds
								}, '->', '-', {
									text : "Create",
									iconCls : "page_add",
									handler : windowopenAdd,
									cls : "SYSOP_ADD"
								}, '-', {
									text : "Update",
									iconCls : "gird_edit",
									cls : "SYSOP_MOD",
									handler : windowopen.createDelegate(this,
											[null])
								}, '-', {
									text : "Delete",
									iconCls : "page_del",
									handler : deleteBatch,
									cls : "SYSOP_DEL"
								}]
					});
			var grid = new Ext.grid.GridPanel({
						id : "visitedlogGrid",
						stripeRows : true,
						bodyStyle : 'width:100%',
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
					})

			grid.on("rowdblclick", function(grid, index) {
						var rec = ds.getAt(index);
						windowopen(rec.get("id"));
					})
		});

// 修改
function windowopen(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("Message", "您没有修改权限!");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!!");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("Message", "Sorry,you can select only on record!")
			return;
		} else
			obj = ids[0].id;
	}
	var custId = $("cid").value;
	openWindowBase(180, 600, 'customervisitedlog.do?method=modify&id=' + obj);
}

// 新增
function windowopenAdd() {
	var custId = $("cid").value;
	openWindowBase(180, 600, 'customervisitedlog.do?method=modify&custId='
					+ custId);
}

// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "Sorry, you do not have Authority!!");
		return;
	}
	Ext.MessageBox.confirm('Message', 'are you sure to delete the selected items?', function(btn) {
				if (btn == 'yes') {
					customerVisitedLogService.deleteById(parseInt(id),
							function(res) {
								Ext.Msg.alert("Message", "Deleted successfully!");
								reloadGrid("visitedlogGrid");
								clearForm("queryForm");
							})
				} else {
					return;
				}
			});
}

function getIds() {
	var list = Ext.getCmp("visitedlogGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var customerVisitedLog = new CustomerVisitedLog();
				customerVisitedLog.id = item.id;
				res.push(customerVisitedLog);
			});
	return res;
}

// 批量删除
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Please select a record!");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure to delete the selected items?', function(btn) {
				if (btn == 'yes') {
					customerVisitedLogService.deleteCustomerVisitedLog(list,
							function(res) {
								Ext.Msg.alert("Message", "Deleted successfully!");
								reloadGrid("visitedlogGrid");
								clearForm("queryForm");
							});
				} else {
					return;
				}
			});
}