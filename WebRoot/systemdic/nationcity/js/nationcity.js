Ext.onReady(function() {
	DWREngine.setAsync(false);
	var typeData;
	baseDataUtil.getBaseDicDataMap("CotNation", "id", "nationName",
			function(res) {
				typeData = res;
			});
	DWREngine.setAsync(true);

	// 省份
	var provinceBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotProvince&key=provinceName",
				cmpId : 'provinceId',
				fieldLabel : "Province",
				editable : true,
				hidden:true,
				hideLabel:true,
				valueField : "id",
				displayField : "provinceName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "95%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	//国家
	var nationBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotNation&key=nationName",
				cmpId : 'nationId',
				fieldLabel : "Nation",
				editable : true,
				valueField : "id",
				displayField : "nationName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "95%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "provinceId"
			}, {
				name : "cityName"
			}, {
				name : "cityRemark"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotnationcity.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				width : 50,
				sortable : true,
				hidden : true
			}, {
				header : "Nation",
				sortable : true,
				resizable : true,
				hidden : true,
				dataIndex : "nationId",
				width : 200,
				renderer : function(value) {
					return typeData["" + value];
				}
			}, {
				header : "City Name",
				dataIndex : "cityName",
				width : 120,
				sortable : true
			}, {
				header : "Remark",
				dataIndex : "cityRemark",
				width : 200,
				sortable : true
			}, {
				header : "Operation",
				dataIndex : "id",
				renderer : function(value) {
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
				displaySize : '5|10|15|20|all'
			});
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Bulk Delete",
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
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	var form = new Ext.form.FormPanel({
				title : "Add|Edit",
				region : 'east',
				id : "typeFormId",
				formId : "addForm",
				frame : true,
				buttonAlign : "center",
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				labelWidth : 60,
				defaultType : 'textfield',
				defaults : {
					width : 200
					//allowBlank : false
				},
				monitorValid : true,
				items : [nationBox, {
							fieldLabel : "City Name",
							id : "cityName",
							name : "cityName",
							allowBlank : false,
							blankText : "Please Enter City Name",
							anchor : "95%"

						}, {
							xtype : "textarea",
							fieldLabel : "Remark",
							id : "cityRemark",
							name : "cityRemark",
							allowBlank : true,
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
								Ext.getCmp('saveBtn').setIconClass('page_add');
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
	var formData = DWRUtil.getValues("addForm");
	// 判断编号是否重复
	cotNationCityService.findExistByNo(formData.cityName, formData.id, function(
					res) {
				if (res != null) {
					Ext.MessageBox.show({
								title : 'Message',
								msg : 'City Name already exist!',
								width : 300,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING,
								fn : function() {
									// 选中编号
									Ext.getCmp("cityName").selectText();
								}
							});
				} else {
					cotNationCityService.saveOrUpdate(formData, function(res) {
								if (res) {
									clearForm("addFormId");
									reloadGrid("cityGrid");
									Ext.MessageBox.alert("Message", 'Save Successfully！');
								} else {
									Ext.MessageBox.alert("Message", 'Save Failed！');
								}
							});
				}
			});
}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.MessageBox.alert("Message", 'You do not have permission to delete!');
		return;
	}
	var flag = window.confirm("Are you sure delete this city?");
	if (flag) {
		var obj = DWRUtil.getValues("addForm");
		var cotTypeLv = new CotTypeLv3();
		var list = new Array();
		cotTypeLv.id = id;
		list.push(cotTypeLv);
		cotNationCityService.deleteTypeLv(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.MessageBox.alert("Message", 'Delete Failed！');
						return;
					}
					Ext.MessageBox.alert("Message", 'Delete Successfully！');
					reloadGrid("cityGrid");
					clearForm("typeFormId");
				});
	} else {
		return;
	}
}
function getIds() {
	var list = Ext.getCmp("cityGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotTypeLv = new CotTypeLv3();
				cotTypeLv.id = item.id
				res.push(cotTypeLv);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert("Message", 'Please select one record!');
		return;
	}
	var flag = window.confirm("Are you sure delete these city?");
	if (flag) {

		cotNationCityService.deleteTypeLv(list, function(res) {
					if (res == -1) {
						Ext.MessageBox.alert("Message", 'Delete Failed!');
						return;
					}
					Ext.MessageBox.alert("Message", 'Delete Successfully!');
					reloadGrid("cityGrid");
					clearForm("typeFormId");

				});
	} else {
		return;
	}
}
