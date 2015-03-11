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
						name : "companyIsdefault",
						type : "int"
					}, {
						name : "companyShortName"
					}, {
						name : "companyNbr"
					}, {
						name : "companyFax"
					}, {
						name : "companyAddr"
					}, {
						name : "companyEmail"
					},{
						name : "companyNo"
					}, {
						name : "companyWebsite"
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
									url : "cotcompany.do?method=query"
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
									header : "Default",
									dataIndex : "companyIsdefault",
									width : 40,
									renderer : function(value) {
										if (value == 1) {
											return "Yes";
										}
										if (value == 0) {
											return "No";
										}
									}
								}, {
									header : "Short Name",
									dataIndex : "companyShortName",
									width : 120
								},  {
									header : "No.",
									dataIndex : "companyNo",
									width : 120
								},{
									header : "Phone",
									dataIndex : "companyNbr",
									width : 120
								}, {
									header : "Fax Number",
									dataIndex : "companyFax",
									width : 120
								}, {
									header : "Company Address",
									dataIndex : "companyAddr",
									width : 220
								}, {
									header : "E-mail",
									dataIndex : "companyEmail",
									width : 180
								}, {
									header : "Website",
									dataIndex : "companyWebsite",
									width : 180
								}, {
									header : "Operation",
									dataIndex : "id",
									width : 80,
									renderer : function(value) {
										var del = '<a href="javascript:del('
												+ value + ')">Delete</a>';
										var nbsp = "&nbsp &nbsp &nbsp"
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
									xtype : "searchcombo",
									emptyText : "Short Name",
									width : 120,
									isSearchField : true,
									searchName : 'companyShortName',
									isJsonType : false,
									store : ds
								}, '->', {
									text : "Create",
									cls : "SYSOP_ADD",
									iconCls : "page_add",
									handler : windowopenAdd
								}, '-', {
									text : "Update",
									cls : "SYSOP_MOD",
									iconCls : "page_mod",
									handler : windowopenMod.createDelegate(
											this, [null])
								}, '-', {
									text : "Delete",
									cls : "SYSOP_DEL",
									iconCls : "page_del",
									handler : deleteBatch
								}]
					});
			var cb = new Ext.form.ComboBox()
			var grid = new Ext.grid.GridPanel({
						id : "companyGrid",
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
							forceFit : false
						}
					});

			var viewport = new Ext.Viewport({
						layout : 'fit',
						items : [grid]
					});

			// 单击修改信息 start
			grid.on('rowdblclick', function(grid, rowIndex, event) {
						var record = grid.getStore().getAt(rowIndex);
						windowopenMod(record.get("id"));
					});
		});

// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "Sorry, you do not have Authority! ");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure to delete this Company?', function(btn) {
				if (btn == 'yes') {
					var isExist = false;
					DWREngine.setAsync(false);
					// 判断是否有关联部门信息
					cotCompanyService.findDeptRecordsCount(id, function(res) {
								isExist = res
							})
					if (isExist) {
						Ext.Msg.alert('Message', "Delete failed！Relevant departments of the company have been cited！");
						return;
					}
					var list = new Array();
					var cotCompany = new CotCompany();
					cotCompany.id = id;
					list.push(cotCompany);
					cotCompanyService.deleteCotCompanyList(list, function(res) {
								if (res == -1) {
									Ext.Msg.alert('Message', "Other records have been used to the record, can not be removed")
									return;
								}
								Ext.Msg.alert('Message', "Deleted successfully!");
								reloadGrid("companyGrid");
							})
					DWREngine.setAsync(true);
				} else {
					return;
				}
			});
}

// 获取选中记录
function getIds() {
	var list = Ext.getCmp("companyGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotCompany = new CotCompany();
				cotCompany.id = item.id;
				res.push(cotCompany);
			});
	return res;
}

// 批量删除
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Pls. select records");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Sure to delete the selected company?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					cotCompanyService.deleteCotCompanyList(list, function(res) {
								if (res == -1) {
									Ext.Msg.alert('Message', "Record is already in use, can not be deleted")
									return;
								}
								Ext.Msg.alert('Message', "Deleted successfully");
								reloadGrid("companyGrid");
							})
					DWREngine.setAsync(true);
				} else {
					return;
				}
			});
}

// 打开新增页面
function windowopenAdd() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.MessageBox.alert('Message', "您没有添加权限");
		return;
	}
	openWindowBase(458, 800, 'cotcompany.do?method=modify');
}

// 打开编辑页面
function windowopenMod(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.MessageBox.alert('Message', "Sorry, you do not have Authority! ");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert('Message', "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert('Message', "Choose only one record!")
			return;
		} else
			obj = ids[0].id;

	}
	openWindowBase(458, 800, 'cotcompany.do?method=modify&id=' + obj);
}
