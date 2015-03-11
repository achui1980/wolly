Ext.onReady(function() {

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
						name : "claimTime"
					}, {
						name : "claimMoney"
					}, {
						name : "claimReason"
					}, {
						name : "claimDeal"
					}, {
						name : "claimRemark"
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
									url : "cotcustomer.do?method=queryClaim&custId="
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
									header : "The Time Of Claim",
									dataIndex : "claimTime",
									width : 150,
									renderer : function(value) {
										return Ext.util.Format
												.date(	new Date(value.year,
																value.month,
																value.day),
														'Y-m-d')
									}
								}, {
									header : "Claim Amount",
									dataIndex : "claimMoney",
									renderer:function(value){
										return Ext.util.Format.number(value,"0.00");
									},
									width : 100
								}, {
									header : "The Reasons For The Claim",
									dataIndex : "claimReason",
									width : 160
								}, {
									header : "Claims Results",
									dataIndex : "claimDeal",
									width : 120
								}, {
									header : "Remark",
									dataIndex : "claimRemark",
									width : 310
								}, {
									header : "Operation",
									dataIndex : "id",
									width : 60,
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
						displayMsg : 'the {0} - {1}records Total{2}records',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No records",
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
						id : "claimGrid",
						stripeRows : true,
						store : ds, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
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
		Ext.Msg.alert("Message", "Message: 'Sorry, you do not have Authority!'");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Please select a record!");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("Message", "please select only one record!")
			return;
		} else
			obj = ids[0];
	}
	var custId = $("cid").value;
	openWindowBase(300, 600, 'cotcustomer.do?method=claimEdit&id=' + obj);
}

// 新增
function windowopenAdd() {
	var custId = $("cid").value;
	openWindowBase(300, 600, 'cotcustomer.do?method=claimEdit&custId=' + custId);
}

// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "Sorry,you do not have permission to delete it!");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure you want to delete it?', function(btn) {
				if (btn == 'yes') {
					var ids = new Array();
					ids.push(id)
					cotCustomerService.deleteClaimByList(ids, function(res) {
								Ext.Msg.alert("Message", "Successfully removed!");
								reloadGrid("claimGrid");
								clearForm("claimFormId");
							})
				} else {
					return;
				}
			});
}

function getIds() {
	var list = Ext.getCmp("claimGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}

// 批量删除
function deleteBatch() {
	var ids = getIds();
	if (ids.length == 0) {
		Ext.Msg.alert("Message", "Please select a record!");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure you want to delete the selected items?', function(btn) {
				if (btn == 'yes') {
					cotCustomerService.deleteClaimByList(ids, function(res) {
								Ext.Msg.alert("Message", "Successfully removed!");
								reloadGrid("claimGrid");
								clearForm("claimFormId");
							});
				} else {
					return;
				}
			});
}