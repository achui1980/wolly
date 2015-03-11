Ext.onReady(function() {
			var typeMap;
			baseDataUtil.getBaseDicDataMap("CotBoxPacking", "id", "value",
					function(res) {
						typeMap = res;
					});
			var boxIName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IB",
						valueField : "id",
						fieldLabel : "The package type",
						autoShow : true,
						allowBlank : true,
						displayField : "value",
						cmpId : "boxIName",
						emptyText : 'Choose',
						autoLoad : true,
						anchor : "100%"
					});
			var boxMName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=MB",
						valueField : "id",
						fieldLabel : "Type of packaging in center box",
						autoShow : true,
						allowBlank : true,
						displayField : "value",
						cmpId : "boxMName",
						emptyText : 'Choose',
						autoLoad : true,
						anchor : "100%"
					});
			var boxOName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=OB",
						valueField : "id",
						fieldLabel : "Type of packaging in out box",
						autoShow : true,
						allowBlank : true,
						displayField : "value",
						cmpId : "boxOName",
						emptyText : 'Chooser',
						autoLoad : true,
						anchor : "100%"
					});
			var boxPName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=PB",
						valueField : "id",
						fieldLabel : "Type of packaging in product",
						autoShow : true,
						allowBlank : true,
						displayField : "value",
						cmpId : "boxPName",
						emptyText : 'Choose',
						autoLoad : true,
						anchor : "100%"
					});
			var inputGridName = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IG",
						valueField : "id",
						fieldLabel : "Cell type plug",
						autoShow : true,
						allowBlank : true,
						displayField : "value",
						cmpId : "inputGridType",
						emptyText : 'Choose',
						autoLoad : true,
						anchor : "100%"
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
						name : "typeName"
					}, {
						name : "typeNameEn"
					}, {
						name : "boxIName",
						type : "int"
					}, {
						name : "boxMName",
						type : "int"
					}, {
						name : "boxOName",
						type : "int"
					}, {
						name : "boxPName",
						type : "int"
					}, {
						name : "inputGridType",
						type : "int"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotboxtype.do?method=query"
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
									header : "Packaging Way (CN)",
									dataIndex : "typeName",
									width : 130
								}, {
									header : "Packaging Way (EN)",
									dataIndex : "typeNameEn",
									width : 130
								}, {
									header : "Type of packaging in inner box",
									dataIndex : "boxIName",
									width : 120,
									renderer : function(value) {
										return typeMap[value];
									}
								}, {
									header : "Type of packaging in center box",
									dataIndex : "boxMName",
									width : 120,
									renderer : function(value) {
										return typeMap[value];
									}
								}, {
									header : "Type of packaging in out box",
									dataIndex : "boxOName",
									width : 120,
									renderer : function(value) {
										return typeMap[value];
									}
								}, {
									header : "Type of packaging in product",
									dataIndex : "boxPName",
									width : 120,
									renderer : function(value) {
										return typeMap[value];
									}
								}, {
									header : "Cell type plug",
									dataIndex : "inputGridType",
									width : 120,
									renderer : function(value) {
										return typeMap[value];
									}
								}, {
									header : "Operation",
									dataIndex : "id",
									width : 60,
									renderer : function(value) {
										// var mod = '<a
										// href="javascript:modTypeById('+value+')">修改</a>';

										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="javascript:del('
												+ value + ')">Delete</a>';
										return del + nbsp;
									}
								}]
					});
			var toolBar = new Ext.PagingToolbar({
						pageSize : 15,
						store : ds,
						displayInfo : true,
						displayMsg : 'Displaying {0} - {1} of {2}',
						displaySize : '5|10|15|20|all',
						emptyMsg : "emptyMsg"
					});
			var tb = new Ext.Toolbar({
						items : ['->', '-', {
									text : "Delete",
									cls : "SYSOP_DEL",
									iconCls : "page_del",
									handler : deleteBatch
								}]
					});
			var cb = new Ext.form.ComboBox()
			var grid = new Ext.grid.GridPanel({

						margins : '0 2 0 0',
						region : "center",
						id : "boxtypeGrid",
						// autoHeight:true,
						stripeRows : true,
						bodyStyle : 'width:100%',
						store : ds, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
						// selModel: new
						// Ext.grid.RowSelectionModel({singleSelect:false}),
						loadMask : true, // 是否显示正在加载
						tbar : tb,
						bbar : toolBar,
						// layout:"fit",
						viewConfig : {
							forceFit : false
						}
					});

			/*----------------------------------------------------*/
			// 绑定下拉框,需要设置同步，不然无法绑定
			// bindSel();
			var form = new Ext.form.FormPanel({
						title : "Add | Edit | query (with * can be found items)",
						region : 'east',
						id : "boxtypeFormId",
						formId : "boxtypeForm",
						frame : true,
						monitorValid : true,
						width : 260,
						autoHeight : true,
						labelAlign : 'right',
						collapsible : true,
						labelWidth : 100,
						defaultType : 'textfield',
						buttonAlign : 'center',
						defaults : {
							width : 220
							// allowBlank: false
						},
						items : [{

									// 添加布局
									fieldLabel : "Packaging way (CN) *",
									id : "typeName",
									name : "typeName",
									allowBlank : false,
									blankText : "The packaging way of Chinese name  can not be empty",
									anchor : "100%"

								}, {
									fieldLabel : "Packaging way (EN)*",
									id : "typeNameEn",
									name : "typeNameEn",
									allowBlank : false,
									blankText : "The packaging way of English name  can not be empty",
									anchor : "100%"
								}, boxIName, boxMName, boxOName, boxPName,
								inputGridName, new Ext.form.Hidden({
											id : "id",
											name : "id"
										})],
						buttons : [{
									enableToggle : true,
									formBind : true,
									text : "New",
									cls : "SYSOP_ADD",
									id : 'saveBtn',
									iconCls : "page_add",
									handler : saveOrUpdate
								}, {
									enableToggle : true,
									text : "Search",
									iconCls : "page_sel",
									handler : function() {
										ds.reload({params : {
												start : 0,
												limit : 15
											}})
									}
								}, {
									text : "Reset",
									iconCls : "page_reset",
									handler : function() {
										form.getForm().reset();
										Ext.getCmp('saveBtn').setText("ADD");
										Ext.getCmp('saveBtn')
												.setIconClass('page_add');
									}
								}]
					});
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [grid, form]
					});
			ds.on('beforeload', function() {
						ds.baseParams = form.getForm().getValues();
					});
			// 分页基本参数
			ds.load({
						params : {
							start : 0,
							limit : 15
						}
					});
			// 单击修改信息 start
			// grid.on('rowclick', function(grid, rowIndex, event) {
			// var record = grid.getStore().getAt(rowIndex);
			// form.getForm().loadRecord(record);
			// });
			grid.on('rowclick', function(grid, rowIndex, event) {
						var record = grid.getStore().getAt(rowIndex);
						loadForm(form, record.id);
						Ext.getCmp('saveBtn').setText("Update");
						Ext.getCmp('saveBtn').setIconClass('page_mod');
					});
		});

// 加载数据
function loadForm(form, id) {
	cotBoxTypeService.getBoxTypeById(id, function(res) {
				form.getForm().setValues(res);
			});
}
// 添加或修改记录
function saveOrUpdate() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var name = Ext.get("typeName").getValue();
	var form = Ext.getCmp("boxtypeFormId").getForm();
	if (!form.isValid())
		return;
	var obj = DWRUtil.getValues("boxtypeForm");
	var list = new Array();
	var cotBoxType = new CotBoxType()
	for (p in cotBoxType) {
		cotBoxType[p] = obj[p];
	}
	if (id == null || id == "") {
		list.push(cotBoxType);
		var isExist = false;
		DWREngine.setAsync(false);
		cotBoxTypeService.findExistByName(name, function(res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.Msg.alert("Message", "Packaging way with the same name already exists");
			return;
		}
		cotBoxTypeService.addBoxType(list, function(res) {
					Ext.Msg.alert("Message", "Successfully added！");
					clearForm("boxtypeFormId");
					reloadGrid("boxtypeGrid");
				});
		DWREngine.setAsync(true);
	} else {
		var boxIName = Ext.get("boxIName").getValue();
		var boxMName = Ext.get("boxMName").getValue();
		var boxOName = Ext.get("boxOName").getValue();
		var boxPName = Ext.get("boxPName").getValue();
		var inputGridType = Ext.get("inputGridType").getValue();

		if (boxIName == 'null') {
			cotBoxType.boxIName = '';
		}
		if (boxMName == 'null') {
			cotBoxType.boxMName = '';
		}
		if (boxOName == 'null') {
			cotBoxType.boxOName = '';
		}
		if (boxPName == 'null') {
			cotBoxType.boxPName = '';
		}
		if (inputGridType == 'null') {
			cotBoxType.inputGridType = '';
		}
		cotBoxType.id = id;
		list.push(cotBoxType);
		cotBoxTypeService.modifyBoxType(list, function(res) {
					if (res) {
						Ext.Msg.alert("Message", "Successfully modified！");
						clearForm("boxtypeFormId");
						reloadGrid("boxtypeGrid");
					} else {
						Ext.Msg.alert("Message", "Modify the failure！");
					}
				});
	}
}
// 删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	var isPopedom = 1
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "'Sorry, you do not have Authority");
		return;
	}
	var cotBoxType = new CotBoxType();
	var list = new Array();
	cotBoxType.id = id;
	list.push(cotBoxType);
	Ext.MessageBox.confirm('Message', ' You Sure to delete the selected package type?', function(btn) {
				if (btn == 'yes') {
					cotBoxTypeService.deleteBoxType(list, function(res) {
								if (res == 0) {
									clearForm("boxtypeFormId");
									reloadGrid("boxtypeGrid");
								} else {
									Ext.Msg.alert("Message", "Other records have been used to the record, can not be removed");
								}
							})
				}
			});
}
function getIds() {
	var list = Ext.getCmp("boxtypeGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotBoxType = new CotBoxType();
				cotBoxType.id = item.id
				res.push(cotBoxType);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Please select records");
		return;
	}
	Ext.MessageBox.confirm('Message', 'Sure to delete the selected packaging way?', function(btn) {
				if (btn == 'yes') {
					cotBoxTypeService.deleteBoxType(list, function(res) {
								result = res;
								if (result == 0) {
									Ext.Msg.alert("Message", "Deleted successfully");
									clearForm("boxtypeFormId");
									reloadGrid("boxtypeGrid");
								} else {
									Ext.Msg.alert("Message", "Other records have been used to the record, can not be removed")
									return;
								}
							});
				} else {
					return;
				}
			});
}
