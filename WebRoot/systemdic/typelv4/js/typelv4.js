Ext.onReady(function() {
	// 数据集
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "typeName"
			}, {
				name : "typeEnName"
			}, {
				name : "typeRemark"
			}, {
				name : "typeNo"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cottypelv4.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	// 分页工具栏
	var pb = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	// Operation工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Delete",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}]
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 表格
	var grid = new Ext.grid.GridPanel({
				//title : "样品材质设置",
				//iconCls : "gird_list",
				id : 'typeGrid',
				margins : '0 5 0 0',
				store : ds,
				region : "center",
				// frame : true,
				stripeRows : true,
				loadMask : true,
				columnLines : false,
				tbar : tb,
				bbar : pb,
				sm : sm,
				columns : [sm, {
							header : "Dep.Of PRODUCT",
							sortable : true,
							resizable : true,
							dataIndex : "typeEnName",
							width : 200
						},{
							header : "No.",
							sortable : true,
							resizable : true,
							dataIndex : "typeNo",
							width : 120
						}, {
							header : "Remark",
							sortable : true,
							resizable : true,
							dataIndex : "typeRemark",
							width : 300,
							align : "left",
							fixed : false,
							menuDisabled : false
						}, {
							header : "Operation",
							dataIndex : "id",
							renderer : function(value) {
								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:del(' + value
										+ ')">Delete</a>';
								return del + nbsp;
							}
						}],
				viewConfig : {
					forceFit : true
				}
			});
	// 查询表单
	var form = new Ext.FormPanel({
				title : "Create|Edit|Query",
				padding : 5,
				id : "addFormId",
				formId : "addForm",
				xtype : "form",
				labelWidth : 80,
				labelAlign : "right",
				layout : "form",
				region : "east",
				minWidth : "",
				collapsible : true,
				width : 260,
				frame : true,
				border : false,
				titleCollapse : false,
				autoHeight : true,
				monitorValid : true,// 验证不通过时,下面绑定的按钮不能点击
				buttonAlign : 'center',
				items : [{
							xtype : "textfield",
							id : 'typeEnName',
							name : 'typeEnName',
							fieldLabel : "Dep.Of PRODUCT",
							anchor : "95%",
							allowBlank : false,
							maxLength : 100,
							tabIndex : 1,
							blankText : "Dep.Of PRODUCT can't be empty"
						},{
							xtype : "textfield",
							id : 'typeNo',
							name : 'typeNo',
							fieldLabel : "No.",
							anchor : "95%",
							allowBlank : false,
							maxLength : 100,
							tabIndex : 2,
							blankText : "No.Can't be empty"
						},  {
							xtype : "textfield",
							id : 'typeName',
							name : 'typeName',
							fieldLabel : "中文材质名",
							hidden:true,
							hideLabel:true,
							anchor : "95%",
							//allowBlank : false,
							maxLength : 100,
							tabIndex : 1
							//blankText : "中文材质名不能为空"
						}, {
							xtype : "textarea",
							id : 'typeRemark',
							name : 'typeRemark',
							tabIndex : 3,
							fieldLabel : "Remark",
							anchor : "95%",
							maxLength : 200
						}, {
							xtype : 'hidden',
							id : 'id',
							name : 'id'
						}],
				buttons : [{
							text : "Create",
							id:"saveBtn",
							cls : "SYSOP_ADD",
							width : 65,
							iconCls : "page_add",
							formBind : true,// 验证不通过时,按钮不能点击
							handler : save
						},{
							text : "Reset",
							width : 65,
							iconCls : "page_reset",
							handler : function() {
								this.ownerCt.ownerCt.getForm().reset();
								Ext.getCmp("saveBtn").setText("Create");
								Ext.getCmp("saveBtn").setIconClass("page_add");
							}
						}]

			});
	// 加载面板
	var MyViewport = new Ext.Viewport({
				layout : "border",
				items : [grid, form]
			});
	// 将查询面板的条件加到ds中
	ds.on('beforeload', function(store, options) {
				ds.baseParams = form.getForm().getValues();
			});
	// 加载表格
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// 单击修改信息 start
	grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				form.getForm().loadRecord(record);
				Ext.getCmp("saveBtn").setText("Edit");
				Ext.getCmp("saveBtn").setIconClass("page_mod");
			});

	// 保存
	function save() {
		var formData = DWRUtil.getValues("addForm");
		// 判断编号是否重复
		cotTypeLv4Service.findExistByEnNo(formData.typeEnName, formData.id,
				function(res) {
					if (res != null) {
						Ext.MessageBox.show({
									title : 'Message',
									msg : 'The same name exists!',
									width : 300,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.WARNING,
									fn : function() {
										// 选中编号
										Ext.getCmp("typeEnName").selectText();
									}
								});
					} else {
						cotTypeLv4Service.saveOrUpdate(formData,
												function(res) {
													if (res) {
														clearForm("addFormId");
														reloadGrid("typeGrid");
														Ext.MessageBox
																.alert("Message",
																		'Save Successful');
													} else {
														Ext.MessageBox
																.alert("Message",
																		'Save Failed');
													}
												});

					}
				});
	};

	// 获得选择的记录
	var getIds = function() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 批量Delete
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Select Record Pls!');
			return;
		}
		Ext.MessageBox.confirm('Message', 'Are you sure to delete this record?', function(btn) {
					if (btn == 'yes') {
						cotTypeLv4Service.deleteByIds(list, function(res) {
									clearForm("addFormId");
									ds.reload();
									Ext.MessageBox.alert("Message", 'Deleted Successful!');
								});
					}
				});
	}
});
// Delete
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", 'You do not have permission to delete!');
		return;
	}
	Ext.MessageBox.confirm('Message', 'Are you sure to delete this record?', function(btn) {
				if (btn == 'yes') {
					var list = new Array();
					list.push(id);
					cotTypeLv4Service.deleteByIds(list, function(delres) {
								if (delres) {
									clearForm("addFormId");
									reloadGrid("typeGrid");
									Ext.MessageBox.alert("Message", 'Delete Successful！');
								} else {
									Ext.MessageBox.alert("Message", 'Delete Fail!');
								}
							});
				}
			});
}
