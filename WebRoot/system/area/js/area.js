Ext.onReady(function() {
			var cityMap;
			baseDataUtil.getBaseDicDataMap("CotCity", "id", "cityName",
					function(res) {
						cityMap = res;
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
						name : "areaName"
					}, {
						name : "cityId",
						type : "int"
					}, {
						name : "areaCode"
					}, {
						name : "remark"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotarea.do?method=query"
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
									header : "Region Name",
									dataIndex : "areaName",
									width : 120
								}, {
									header : "Area code",
									dataIndex : "areaCode",
									width : 130
								}, {
									header : "Respective cities",
									dataIndex : "cityId",
									width : 130,
									renderer : function(value) {
										return cityMap[value];
									}
								}, {
									header : "Remarks",
									dataIndex : "remark",
									width : 300
								}, {
									header : "Operation",
									dataIndex : "id",
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
						items : ['->', {
									text : "Delete",
									handler : deleteBatch,
									iconCls : "page_del"
								}]
					});
			var cb = new Ext.form.ComboBox()
			var grid = new Ext.grid.GridPanel({
						iconCls : "gird_list",
						region : "center",
						id : "areaGrid",
						margins : "0 5 0 0",
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
			DWREngine.setAsync(false);
			// 绑定下拉框,需要设置同步，不然无法绑定
			bindSel();
			var form = new Ext.form.FormPanel({
						title : "Query / edit",
						region : 'east',
						id : "areaFormId",
						formId : "areaForm",
						frame : true,
						width : 260,
						autoHeight : true,
						monitorValid : true,
						buttonAlign : "center",
						labelAlign : 'right',
						labelWidth : 60,
						defaultType : 'textfield',
						defaults : {
							width : 200,
							allowBlank : false
						},
						items : [{

									// 添加布局
									fieldLabel : "Region Name",
									id : "areaName",
									name : "areaName",
									allowBlank : false,
									blankText : "Region Name can not be empty",
									anchor : "95%"

								}, {

									// 添加布局
									fieldLabel : "Area code",
									id : "areaCode",
									name : "areaCode",
									allowBlank : false,
									blankText : "Region Name can not be empty",
									anchor : "95%"

								}, {
									xtype : "combo",
									fieldLabel : 'Respective provinces',
									transform : "cityId",
									triggerAction : 'all',
									lazyRender : true,
									editable : false,
									mode : 'local',
									allowBlank : false,
									blankText : "Region Name can not be empty",
									hiddenName : "cityId",
									anchor : "95%"
								}, {
									xtype : "textarea",
									fieldLabel : "Remarks",
									id : "remark",
									name : "remark",
									allowBlank : true,
									anchor : "95%"
								}, new Ext.form.Hidden({
											id : "id",
											name : "id"
										})],
						buttons : [

						{
									enableToggle : true,
									pressed : true,
									width : 65,
									text : "New",
									id : "saveBtn",
									cls : "SYSOP_ADD",
									iconCls : "page_add",
									formBind : true,
									handler : saveOrUpdate
								}, {
									enableToggle : true,
									pressed : true,
									iconCls : "page_sel",
									width : 65,
									text : "Search",
									handler : function() {
										ds.reload({params : {
												start : 0,
												limit : 15
											}})
									}
								}, {
									text : "Reset",
									width : 65,
									iconCls : "page_reset",
									handler : function() {
										form.getForm().reset();
										Ext.getCmp('saveBtn').setText("Added");
										Ext.getCmp('saveBtn')
												.setIconClass('page_add');
									}
								}]
					});
			DWREngine.setAsync(true);
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
			grid.on('rowclick', function(grid, rowIndex, event) {
						var record = grid.getStore().getAt(rowIndex);
						form.getForm().loadRecord(record);
						Ext.getCmp("saveBtn").setText("Update");
						Ext.getCmp("saveBtn").setIconClass("page_mod");
					});

		});
// 添加或修改记录
function saveOrUpdate() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var roleName = Ext.get("areaName").getValue();
	var form = Ext.getCmp("areaFormId").getForm();
	if (!form.isValid())
		return;
	if (id == null || id == "") {
		// 表单验证结束
		var obj = DWRUtil.getValues("areaForm");
		var cotArea = new CotArea();
		var list = new Array();
		for (var p in obj) {
			cotArea[p] = obj[p];
		}
		list.push(cotArea);
		var isExist = false;
		DWREngine.setAsync(false);
		cotAreaService.findExistByName(cotArea.areaName, function(res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.Msg.alert("Message", "City name already exists");
			return;
		}
		// 添加员工
		cotAreaService.addArea(list, function(res) {
					clearForm("areaFormId");
					reloadGrid("areaGrid");
				});
		DWREngine.setAsync(true);
	} else {
		// 表单验证结束
		var obj = DWRUtil.getValues("areaForm");
		var cotArea = new CotArea();;
		var list = new Array();
		for (var p in obj) {
			cotArea[p] = obj[p];
		}
		cotArea.id = id;
		list.push(cotArea);
		cotAreaService.modifyArea(list, function(res) {
					Ext.Msg.alert("Message", "Successfully modified");
					clearForm("areaFormId");
					reloadGrid("areaGrid");
				});
	}

}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
		return;
	}
	var flag = window.confirm("Confirm to delete?");
	if (flag) {
		var obj = DWRUtil.getValues("areaForm");
		var cotArea = new CotArea();
		var list = new Array();
		cotArea.id = id;
		list.push(cotArea);
		cotAreaService.deleteArea(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.Msg.alert("Message", "Other records have been used to the record, can not be removed");
						return;
					}
					Ext.Msg.alert("Message", "Deleted successfully");
					clearForm("areaFormId");
					reloadGrid("areaGrid");

				});
	} else {
		return;
	}
}
function getIds() {
	var list = Ext.getCmp("areaGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotArea = new CotArea();
				cotArea.id = item.id
				res.push(cotArea);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Pls. select records");
		return;
	}
	var flag = window.confirm("Confirm to delete?");
	if (flag) {

		cotAreaService.deleteArea(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.Msg.alert("Message", "Other records have been used to the record, can not be removed");
						return;
					}
					Ext.Msg.alert("Message", "Deleted successfully");
					reloadGrid("areaGrid");
					clearForm("areaFormId");

				});
	} else {
		return;
	}
}
