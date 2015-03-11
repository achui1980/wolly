Ext.onReady(function() {
	DWREngine.setAsync(false);
	var companyMap;
	baseDataUtil.getBaseDicDataMap("CotCompany", "id", "companyShortName",
			function(res) {
				companyMap = res;
			});
	var companyName = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCompany&flag=pic",
				valueField : "id",
				fieldLabel : "Company",
				autoShow : true,
				allowBlank : true,
				displayField : "companyShortName",
				cmpId : "companyId",
				emptyText : 'Choose',
				anchor : "95%"
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
				name : "deptName"
			}, {
				name : "companyId",
				type : "int"
			}, {
				name : "deptStatus",
				type : "int"
			}, {
				name : "deptRemark"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotdept.do?method=query"
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
							header : "Dept Name",
							dataIndex : "deptName",
							width : 130
						}, {
							header : "Company",
							dataIndex : "companyId",
							width : 130,
							renderer : function(value) {
								// printObject(companyMap)
								return companyMap[value];
							}
						}, {
							header : "Status",
							dataIndex : "deptStatus",
							width : 120,
							renderer : function(value) {
								if (value == 0) {
									return "Disable"
								} else if (value == 1) {
									return "Enable"
								}
							}
						}, {
							header : "Remarks",
							dataIndex : "deptRemark",
							width : 200
						}, {
							header : "Operation",
							dataIndex : "id",
							width : 60,
							renderer : function(value) {
								// var mod = '<a
								// href="javascript:modTypeById('+value+')">修改</a>';

								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:del(' + value
										+ ')">Delete</a>';
								return del + nbsp;
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}' ,
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
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

				// title:'部门列表',
				// frame:true,
				iconCls : "gird_list",
				margins : '0 2 0 0',
				region : "center",
				id : "deptGrid",
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
					forceFit : true
				}
			});

	/*----------------------------------------------------*/
	// 绑定下拉框,需要设置同步，不然无法绑定
	// bindSel();
	var statusStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'Disable'], [1, 'Enable']]
			});

	var statusField = new Ext.form.ComboBox({
				name : 'deptStatus',
				fieldLabel : 'Status',
				editable : false,
				store : statusStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "95%",
				emptyText : 'Choose',
				hiddenName : 'deptStatus',
				selectOnFocus : true
			});
	var form = new Ext.form.FormPanel({
				title : "Add | Edit | Enquiry",
				region : 'east',
				id : "deptFormId",
				formId : "deptForm",
				frame : true,
				monitorValid : true,
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				collapsible : true,
				labelWidth : 60,
				defaultType : 'textfield',
				buttonAlign : 'center',
				defaults : {
					width : 220
					// allowBlank: false
				},
				items : [{

							// 添加布局
							fieldLabel : "Dept Name",
							id : "deptName",
							name : "deptName",
							allowBlank : false,
							blankText : "Department name can not be empty",
							anchor : "95%"

						}, companyName, statusField, {
							// 添加布局
							fieldLabel : "Remarks",
							xtype : "textarea",
							id : "deptRemark",
							name : "deptRemark",
							anchor : "95%"

						}, new Ext.form.Hidden({
									id : "id",
									name : "id"
								})],
				buttons : [{
							// enableToggle:true,
							formBind : true,
							text : "Added",
							cls : "SYSOP_ADD",
							id : 'saveBtn',
							iconCls : "page_add",
							handler : saveOrUpdate
						}, {
							// enableToggle:true,
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
								Ext.getCmp('saveBtn').setText("Added");
								Ext.getCmp('saveBtn').setIconClass('page_add');
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
		form.getForm().loadRecord(record);
		companyName.bindValue(record.data.companyId);
		Ext.getCmp('saveBtn').setText("Update");
		Ext.getCmp('saveBtn').setIconClass('page_mod');
			// loadForm(form,record.id);
		});
	DWREngine.setAsync(true);
});

// 添加或修改记录
function saveOrUpdate() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var name = Ext.get("deptName").getValue();
	var form = Ext.getCmp("deptFormId").getForm();
	if (!form.isValid())
		return;
	var obj = DWRUtil.getValues("deptForm");
	var list = new Array();
	list.push(obj);
	if (id == null || id == "") {
		var isExist = false;
		DWREngine.setAsync(false);
		cotDeptService.isNotExistDeptName(name, function(res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.Msg.alert("Message", "Department name already exists");
			return;
		}
		cotDeptService.saveCotDeptByList(list, function(res) {
					Ext.Msg.alert("Message", "Successfully added！");
					clearForm("deptFormId");
					reloadGrid("deptGrid");
				});
		DWREngine.setAsync(true);
	} else {
		cotDeptService.updateCotDeptByList(list, function(res) {
					Ext.Msg.alert("Message", "Successfully modified！");
					clearForm("deptFormId");
					reloadGrid("deptGrid");
				});
	}
}
// 删除
function del(id) {
	// var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	var isPopedom = 1
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "Sorry, you do not have Authority! ");
		return;
	}
	var cotDept = new CotDept();
	var list = new Array();
	cotDept.id = id;
	list.push(cotDept);
	Ext.MessageBox.confirm('Message', 'You sure to delete the selected sectors?', function(btn) {
				if (btn == 'yes') {
					cotDeptService.deleteCotDeptByList(list, function(res) {
								if (res == 0) {
									reloadGrid("deptGrid");
									clearForm("deptFormId");
								} else {
									Ext.Msg.alert("Message", "Other records have been used to the record, can not be removed");
								}
							})
				}
			});
}
function getIds() {
	var list = Ext.getCmp("deptGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotDept = new CotDept();
				cotDept.id = item.id
				res.push(cotDept);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Please select records");
		return;
	}
	Ext.MessageBox.confirm('Message', 'You sure to delete the selected sectors?', function(btn) {
				if (btn == 'yes') {
					cotDeptService.deleteCotDeptByList(list, function(res) {
								result = res;
								if (result == 0) {
									Ext.Msg.alert("Message", "Deleted successfully");
									reloadGrid("deptGrid");
									clearForm("deptFormId");
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
