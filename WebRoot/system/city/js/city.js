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
						name : "cityName"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotcity.do?method=query"
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
			var cm = new Ext.grid.ColumnModel([sm, {
						header : "ID",
						dataIndex : "id",
						width : 50,
						sortable : true,
						hidden : true
					}, {
						header : "City Name",
						dataIndex : "cityName",
						width : 120,
						sortable : true
					}, {
						header : "Operation",
						dataIndex : "id",
						renderer : function(value) {
							// var mod = '<a
							// href="javascript:modTypeById('+value+')">Update</a>';
							var nbsp = "&nbsp &nbsp &nbsp"
							var del = '<a href="javascript:del(' + value
									+ ')">Delete</a>';
							return del + nbsp;
						}
					}]);
			var toolBar = new Ext.PagingToolbar({
						pageSize : 15,
						store : ds,
						displayInfo : true,
						displayMsg : 'Displaying {0} - {1} of {2}',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display"
					});
			var tb = new Ext.Toolbar({
						items : ['->', {
									text : "Delete",
									handler : deleteBatch,
									iconCls : "page_del"
								}]
					});
			var grid = new Ext.grid.GridPanel({
						region : "center",
						id : "cityGrid",
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
							forceFit : true
						}
					});
			ds.on('beforeload', function() {

						ds.baseParams = {
						// TODO：
						};

					});
			// 分页基本参数
			ds.load({
						params : {
							start : 0,
							limit : 15
						}
					});
			/*----------------------------------------------------*/

			var form = new Ext.form.FormPanel({
						title : "Add | Edit",
						region : 'east',
						id : "cityFormId",
						formId : "cityForm",
						frame : true,
						width : 260,
						buttonAlign : "center",
						autoHeight : true,
						labelAlign : 'right',
						labelWidth : 60,
						monitorValid : true,
						defaultType : 'textfield',
						defaults : {
							width : 200,
							allowBlank : false
						},
						items : [{

									// 添加布局
									fieldLabel : "City Name",
									id : "cityName",
									name : "cityName",
									allowBlank : false,
									blankText : "City Name can not be empty",
									anchor : "95%"

								}, new Ext.form.Hidden({
											id : "id",
											name : "id"
										})],
						buttons : [{
									enableToggle : true,
									pressed : true,
									text : "Create",
									width : 65,
									iconCls : "page_add",
									id : "saveBtn",
									formBind : true,
									handler : saveOrUpdate
								}, {
									text : "Reset",
									width : 65,
									iconCls : "page_reset",
									handler : function() {
										form.getForm().reset();
										Ext.getCmp('saveBtn').setText("Create");
										Ext.getCmp('saveBtn')
												.setIconClass('page_add');
									}
								}]
					});

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [grid, form]
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
	var roleName = Ext.get("cityName").getValue();
	var form = Ext.getCmp("cityFormId").getForm();
	if (!form.isValid())
		return;
	if (id == null || id == "") {
		// 表单验证结束
		var obj = DWRUtil.getValues("cityForm");
		var cotCity = new CotCity();
		var list = new Array();
		for (var p in obj) {
			cotCity[p] = obj[p];
		}
		list.push(cotCity);
		var isExist = false;
		DWREngine.setAsync(false);
		cotCityService.findExistByName(cotCity.roleName, function(res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.MessageBox.alert("Message", 'Region the same name already exists！');
			return;
		}
		// 添加员工
		cotCityService.addCity(list, function(res) {
					reloadGrid("cityGrid");
				});
		DWREngine.setAsync(true);
	} else {
		// 表单验证结束
		var obj = DWRUtil.getValues("cityForm");
		var cotCity = new CotCity();
		var list = new Array();
		for (var p in obj) {
			cotCity[p] = obj[p];
		}
		cotCity.id = id;
		list.push(cotCity);
		cotCityService.modifyCity(list, function(res) {
					Ext.MessageBox.alert("Message", 'Successfully modified！');
					reloadGrid("cityGrid");
				});
	}

}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.MessageBox.alert("Message", 'You do not have permission to delete！');
		return;
	}
	var flag = window.confirm("Are you sure to delete?");
	if (flag) {
		var obj = DWRUtil.getValues("roleForm");
		var cotCity = new CotCity();
		var list = new Array();
		cotCity.id = id;
		list.push(cotCity);
		cotCityService.deleteCity(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.MessageBox.alert("Message", 'Other records have been used to the record, can not be removed！');
						return;
					}
					Ext.MessageBox.alert("Message", 'Deleted successfully！');
					clearForm("cityFormId");
					reloadGrid("cityGrid");

				});
	} else {
		return;
	}
}
function getIds() {
	var list = Ext.getCmp("cityGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotCity = new CotCity();
				cotCity.id = item.id
				res.push(cotCity);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert("Message", 'Please select records！');
		return;
	}
	var flag = window.confirm("Are you sure to delete?");
	if (flag) {

		cotCityService.deleteCity(list, function(res) {
					if (res == -1) {
						Ext.MessageBox.alert("Message", 'Other records have been used to the record, can not be deleted!');
						return;
					}
					Ext.MessageBox.alert("Message", 'Deleted successfully！');
					clearForm("cityFormId");
					reloadGrid("cityGrid");

				});
	} else {
		return;
	}
}
