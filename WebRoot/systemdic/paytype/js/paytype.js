Ext.onReady(function() {
			// 数据集
			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "payName"
					}, {
						name : "payRemark"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotpaytype.do?method=query"
								}),
						reader : new Ext.data.JsonReader({
									root : "data",
									totalProperty : "totalCount",
									idProperty : "id"
								}, roleRecord)
					});

			// 操作工具栏
			var tb = new Ext.Toolbar({
						items : ['->', {
									text : "Delete",
									cls : "SYSOP_DEL",
									iconCls : "page_del",
									handler : deleteBatch
								}]
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

			// 创建复选框列
			var sm = new Ext.grid.CheckboxSelectionModel();
			// 表格
			var grid = new Ext.grid.GridPanel({
//						title : "付款方式",
//						iconCls : "gird_list",
						id : 'typeGrid',
						store : ds,
						region : "center",
						margins : '0 5 0 0',
						stripeRows : true,
						loadMask : true,
						columnLines : false,
						tbar : tb,
						bbar : pb,
						sm : sm,
						columns : [sm,{
									header : "Operation",
									dataIndex : "id",
									renderer : function(value) {
										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="javascript:del('
												+ value + ')">Delete</a>';
										return del + nbsp;
									}
								}, {
									header : "Payment Name",
									sortable : true,
									resizable : true,
									dataIndex : "payName",
									width : 200
								}, {
									header : "Remark",
									sortable : true,
									resizable : true,
									dataIndex : "payRemark",
									width : 300,
									align : "left",
									fixed : false,
									menuDisabled : false
								}],
						viewConfig : {
							forceFit : true
						}
					});

			// 查询表单
			var form = new Ext.FormPanel({
						title : "Create|Edit",
						xtype : "form",
						labelWidth : 60,
						labelAlign : "right",
						padding : 5,
						id : "addFormId",
						formId : "addForm",
						layout : "form",
						region : "east",
						minWidth : "",
						collapsible : true,
						width : 260,
						frame : true,
						border : false,
						autoHeight : true,
						monitorValid : true,// 验证不通过时,下面绑定的按钮不能点击
						buttonAlign : 'center',
						items : [{
									xtype : "textfield",
									id : 'payName',
									name : 'payName',
									fieldLabel : "Payment",
									anchor : "95%",
									allowBlank : false,
									maxLength : 300,
									tabIndex : 1,
									blankText : "Payment can not be empty!"
								}, {
									xtype : "textarea",
									id : 'payRemark',
									name : 'payRemark',
									tabIndex : 2,
									fieldLabel : "Remark",
									anchor : "95%",
									maxLength : 500
								}, {
									xtype : 'hidden',
									id : 'id',
									name : 'id'
								}],
						buttons : [{
									text : "Create",
									cls : "SYSOP_ADD",
									id:"saveBtn",
									width : 65,
									iconCls : "page_add",
									formBind : true,// 验证不通过时,按钮不能点击
									handler : save
								}, {
									text : "Reset",
									width : 65,
									iconCls : "page_reset",
									handler : function() {
										form.getForm().reset();
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
						Ext.getCmp("saveBtn").setText("Update");
						Ext.getCmp("saveBtn").setIconClass("page_mod");
					});

			// 保存
			function save() {
				var formData = DWRUtil.getValues("addForm");
				// 判断编号是否重复
				cotPayTypeService.findExistByNo(formData.payName, formData.id,
						function(res) {
							if (res != null) {
								Ext.MessageBox.show({
											title : 'Message',
											msg : 'Number already exists, please re-enter！',
											width : 300,
											buttons : Ext.MessageBox.OK,
											icon : Ext.MessageBox.WARNING,
											fn : function() {
												// 选中编号
												Ext.getCmp("payName")
														.selectText();
											}
										});
							} else {
								cotPayTypeService.saveOrUpdate(formData,
										function(res) {
											if (res) {
												clearForm("addFormId");
												reloadGrid("typeGrid");
												// ds.reload();
												Ext.MessageBox.alert("Message",
														'Successfully saved！');
											} else {
												Ext.MessageBox.alert("Message",
														'Save failed！');
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
			// 批量删除
			function deleteBatch() {
				var list = getIds();
				if (list.length == 0) {
					Ext.MessageBox.alert("Message", 'Please select records！');
					return;
				}
				Ext.MessageBox.confirm('Message', 'You sure to delete the selected record?', function(btn) {
							if (btn == 'yes') {
								cotPayTypeService.deleteByIds(list, function(
												res) {
											clearForm("addFormId");
											ds.reload();
											Ext.MessageBox.alert("Message",
													'Deleted successfully！');
										});
							}
						});
			}
		});

// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", 'You do not have permission to delete！');
		return;
	}
	Ext.MessageBox.confirm('Message', 'You sure to delete the selected record?', function(btn) {
				if (btn == 'yes') {
					var list = new Array();
					list.push(id);
					cotPayTypeService.deleteByIds(list, function(delres) {
								if (delres) {
									clearForm("addFormId");
									reloadGrid("typeGrid");
									Ext.MessageBox.alert("Message", 'Deleted successfully！');
								} else {
									Ext.MessageBox.alert("Message", 'Delete failed！');
								}
							});
				}
			});
}
