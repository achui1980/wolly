Ext.onReady(function() {
			// 数据集
			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "hscode"
					}, {
						name : "cnName"
					}, {
						name : "taxRate",
						convert : numFormat.createDelegate(this, ["0.00"], 3)
					}, {
						name : "enName"
					}, {
						name : "remark"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "coteleother.do?method=query"
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
			// 操作工具栏
			var tb = new Ext.Toolbar({
						items : ['->', {
									text : "删除",
									cls : "SYSOP_DEL",
									iconCls : "page_del",
									handler : deleteBatch
								}]
					});

			// 创建复选框列
			var sm = new Ext.grid.CheckboxSelectionModel();
			// 表格
			var grid = new Ext.grid.GridPanel({
						// title : 'HS编码',
						// iconCls : "gird_list",
						id : 'typeGrid',
						store : ds,
						region : "center",
						// frame : true,
						stripeRows : true,
						margins : '0 5 0 0',
						loadMask : true,
						columnLines : false,
						tbar : tb,
						bbar : pb,
						sm : sm,
						columns : [sm, {
									header : "HS海关编码",
									sortable : true,
									resizable : true,
									dataIndex : "hscode",
									width : 150
								}, {
									header : "报关中文名称",
									sortable : true,
									resizable : true,
									dataIndex : "cnName",
									width : 150
								}, {
									header : "报关英文名称",
									sortable : true,
									resizable : true,
									dataIndex : "enName",
									width : 150
								}, {
									header : "退税率",
									sortable : true,
									resizable : true,
									dataIndex : "taxRate",
									align : "right",
									width : 80
								}, {
									header : "备注",
									sortable : true,
									resizable : true,
									dataIndex : "remark",
									width : 180
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
							forceFit : false
						}
					});
			// 查询表单
			var form = new Ext.FormPanel({
						title : "新增|编辑|查询(带*号为可查询项)",
						padding : 5,
						id : "addFormId",
						formId : "addForm",
						xtype : "form",
						labelWidth : 90,
						labelAlign : "right",
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
									id : 'hscode',
									name : 'hscode',
									fieldLabel : "HS海关编码*",
									anchor : "95%",
									allowBlank : false,
									maxLength : 100,
									tabIndex : 1,
									blankText : "HS海关编码不能为空"
								}, {
									xtype : "textfield",
									id : 'cnName',
									name : 'cnName',
									fieldLabel : "报关中文名称*",
									anchor : "95%",
									allowBlank : false,
									maxLength : 100,
									tabIndex : 2,
									blankText : "报关中文名称不能为空"
								}, {
									xtype : "textfield",
									id : 'enName',
									name : 'enName',
									fieldLabel : "报关英文名称*",
									anchor : "95%",
									maxLength : 100,
									tabIndex : 3
								}, {
									id : 'taxRate',
									name : 'taxRate',
									baseCls : 'x-plain',
									fieldLabel : "退税率",
									tabIndex : 4,
									maxValue : 99.9,
									decimalPrecision : 1,
									xtype : "numberfield",
									anchor : '95%',
									validateOnBlur : false,
									listeners : {
										"change" : changeEle
									}
								}, {
									xtype : "textarea",
									id : 'remark',
									name : 'remark',
									fieldLabel : "备注",
									anchor : "95%",
									maxLength : 500,
									tabIndex : 5
								}, {
									xtype : 'hidden',
									id : 'id',
									name : 'id'
								}],
						buttons : [{
									text : "新增",
									cls : "SYSOP_ADD",
									id : "saveBtn",
									width : 65,
									iconCls : "page_add",
									formBind : true,// 验证不通过时,按钮不能点击
									handler : save
								}, {
									iconCls : "page_sel",
									width : 65,
									text : "查询",
									handler : function() {
										ds.reload({params : {
												start : 0,
												limit : 15
											}})
									}
								}, {
									text : "重置",
									width : 65,
									iconCls : "page_reset",
									handler : function() {
										form.getForm().reset();
										Ext.getCmp("saveBtn").setText("新增");
										Ext.getCmp("saveBtn")
												.setIconClass("page_add");
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
				// 判断报关中文名是否重复
				cotEleOtherService.findExistByNo(formData.cnName, formData.id,
						function(res) {
							if (res != null) {
								Ext.MessageBox.show({
											title : '提示信息',
											msg : '报关中文名已经存在，请重新输入！',
											width : 300,
											buttons : Ext.MessageBox.OK,
											icon : Ext.MessageBox.WARNING,
											fn : function() {
												// 选中编号
												Ext.getCmp("cnName")
														.selectText();
											}
										});
							} else {
								cotEleOtherService.saveOrUpdate(formData,
										function(res) {
											if (res) {
												clearForm("addFormId");
												reloadGrid("typeGrid");
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
				Ext.MessageBox.confirm('提示信息', '确定删除选择的海关编码吗?', function(btn) {
							if (btn == 'yes') {
								cotEleOtherService.deleteByIds(list, function(
												res) {
											clearForm("addFormId");
											ds.reload();
											Ext.MessageBox.alert("提示信息",
													'删除成功！');
										});
							}
						});
			}

			// 更新样品档案的退税率
			function changeEle(txt, newVal, oldVal) {
				var id = $('id').value;
				if(id!=''){
					Ext.MessageBox.confirm('提示消息', '是否同步更新样品档案的退税率?',
							function(btn) {
								if (btn == 'yes') {
									cotEleOtherService.updateEleTax(newVal,id,function(res) {
												if (res>0) {
													Ext.MessageBox.alert("提示信息",
															'更新成功'+res+'条样品记录!');
												} else {
													Ext.MessageBox.alert("提示信息",
															'没有样品档案引用该海关编码,不用更新！');
												}
											});
								}
							})
				}
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
	Ext.MessageBox.confirm('提示信息', '确定删除此海关编码吗?', function(btn) {
				if (btn == 'yes') {
					var list = new Array();
					list.push(id);
					cotEleOtherService.deleteByIds(list, function(delres) {
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
