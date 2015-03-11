Ext.onReady(function() {
			// 数据集
			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "situationName"
					}, {
						name : "situationRemark"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotpricesituation.do?method=query"
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
									text : "删除",
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
//						title : "报价场合",
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
						columns : [sm, {
									header : "报价场合名称",
									sortable : true,
									resizable : true,
									dataIndex : "situationName",
									width : 200
								}, {
									header : "场合备注",
									sortable : true,
									resizable : true,
									dataIndex : "situationRemark",
									width : 300,
									align : "left",
									fixed : false,
									menuDisabled : false
								}, {
									header : "操作",
									dataIndex : "id",
									renderer : function(value) {
										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="javascript:del('
												+ value + ')">删除</a>';
										return del + nbsp;
									}
								}],
						viewConfig : {
							forceFit : true
						}
					});

			// 查询表单
			var form = new Ext.FormPanel({
						title : "新增|编辑|查询",
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
									id : 'situationName',
									name : 'situationName',
									fieldLabel : "报价场合",
									anchor : "95%",
									allowBlank : false,
									maxLength : 20,
									tabIndex : 1,
									blankText : "报价场合不能为空"
								}, {
									xtype : "textarea",
									id : 'situationRemark',
									name : 'situationRemark',
									tabIndex : 2,
									fieldLabel : "场合备注",
									anchor : "95%",
									maxLength : 200
								}, {
									xtype : 'hidden',
									id : 'id',
									name : 'id'
								}],
						buttons : [ {
									text : "新增",
									cls : "SYSOP_ADD",
									id:"saveBtn",
									width : 65,
									iconCls : "page_add",
									formBind : true,// 验证不通过时,按钮不能点击
									handler : save
								}, {
									text : "重置",
									width : 65,
									iconCls : "page_reset",
									handler : function() {
										form.getForm().reset();
										Ext.getCmp("saveBtn").setText("新增");
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
						Ext.getCmp("saveBtn").setText("修改");
						Ext.getCmp("saveBtn").setIconClass("page_mod");
					});

			// 保存
			function save() {
				var formData = DWRUtil.getValues("addForm");
				// 判断编号是否重复
				cotPriceSituationService.findExistByNo(formData.typeName, formData.id,
						function(res) {
							if (res != null) {
								Ext.MessageBox.show({
											title : '提示信息',
											msg : '编号已经存在，请重新输入！',
											width : 300,
											buttons : Ext.MessageBox.OK,
											icon : Ext.MessageBox.WARNING,
											fn : function() {
												// 选中编号
												Ext.getCmp("typeName")
														.selectText();
											}
										});
							} else {
								cotPriceSituationService.saveOrUpdate(formData,
										function(res) {
											if (res) {
												clearForm("addFormId");
												reloadGrid("typeGrid");
												// ds.reload();
												Ext.MessageBox.alert("提示信息",
														'保存成功！');
											} else {
												Ext.MessageBox.alert("提示信息",
														'保存失败！');
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
					Ext.MessageBox.alert("提示信息", '请选择记录！');
					return;
				}
				Ext.MessageBox.confirm('提示信息', '确定删除选择的报价场合吗?', function(btn) {
							if (btn == 'yes') {
								cotPriceSituationService.deleteByIds(list, function(
												res) {
											clearForm("addFormId");
											ds.reload();
											Ext.MessageBox.alert("提示信息",
													'删除成功！');
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
		Ext.MessageBox.alert("提示信息", '您没有删除权限！');
		return;
	}
	Ext.MessageBox.confirm('提示信息', '确定删除此报价场合吗?', function(btn) {
				if (btn == 'yes') {
					var list = new Array();
					list.push(id);
					cotPriceSituationService.deleteByIds(list, function(delres) {
								if (delres) {
									clearForm("addFormId");
									reloadGrid("typeGrid");
									Ext.MessageBox.alert("提示信息", '删除成功！');
								} else {
									Ext.MessageBox.alert("提示信息", '删除失败！');
								}
							});
				}
			});
}
