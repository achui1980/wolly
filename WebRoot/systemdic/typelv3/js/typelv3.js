Ext.onReady(function() {
//	DWREngine.setAsync(false);
//	var typeData;
//	baseDataUtil.getBaseDicDataMap("CotTypeLv2", "id", "typeName", function(
//					res) {
//				typeData = res;
//			});
//	DWREngine.setAsync(true);
//	// 大类
//	var typeBox = new BindCombox({
//				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeEnName",
//				cmpId : 'typeIdLv',
//				fieldLabel : "Category",
//				editable : true,
//				valueField : "id",
//				displayField : "typeEnName",
//				emptyText : 'Select',
//				mode : 'remote',
//				pageSize : 10,
//				anchor : "95%",
//				allowBlank : false,
//				blankText : "Category can not be empty!",
//				tabIndex : 1,
//				sendMethod : "post",
//				selectOnFocus : true,
//				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//				listWidth : 350,// 下
//				triggerAction : 'all'
//			});
//
//	// 中类
	var type2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'typeIdLv2',
				fieldLabel : "Group",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				mode : 'remote',
				pageSize : 10,
				anchor : "95%",
				allowBlank : false,
				blankText : "Group can not be empty",
				tabIndex : 1,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "typeIdLv2"
			}, {
				name : "typeName"
			}, {
				name : "typeRemark"
			}, {
				name : "typeNo"
			}

	]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cottypelv3.do?method=query"
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
			}
//			, {
//				header : "Group",
//				sortable : true,
//				resizable : true,
//				dataIndex : "typeIdLv2",
//				width : 200,
//				renderer : function(value) {
//					return typeData["" + value];
//				}
//			}
			, {
				header : "Category",
				dataIndex : "typeName",
				width : 120,
				sortable : true
			},{
							header : "No.",
							sortable : true,
							resizable : true,
							dataIndex : "typeNo",
							width : 120
						}, {
				header : "Remark",
				dataIndex : "typeRemark",
				width : 200,
				sortable : true
			}, {
				header : "Operation",
				dataIndex : "id",
				renderer : function(value) {
					// var mod = '<a
					// href="javascript:modTypeById('+value+')">修改</a>';
					var nbsp = "&nbsp &nbsp &nbsp"
					var del = '<a href="javascript:del(' + value + ')">Delete</a>';
					return del + nbsp;
				}
			}]);
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Lots Delete",
							handler : deleteBatch,
							iconCls : "page_del"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "typeGrid",
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

	var form = new Ext.form.FormPanel({
				title : "Create|Edit",
				region : 'east',
				id : "typeFormId",
				formId : "typeForm",
				frame : true,
				buttonAlign : "center",
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				labelWidth : 60,
				defaultType : 'textfield',
				defaults : {
					width : 200,
					allowBlank : false
				},
				monitorValid : true,
				items : [
				type2Box, 
					{
							fieldLabel : "Category",
							id : "typeName",
							name : "typeName",
							allowBlank : false,
							blankText : "Category can not be empty!",
							anchor : "95%"

						}, {
							xtype : "textfield",
							id : 'typeNo',
							name : 'typeNo',
							fieldLabel : "No.",
							anchor : "95%",
							allowBlank : false,
							maxLength : 100,
							blankText : "No. can not be empty"
						},  {
							xtype : "textarea",
							fieldLabel : "Remark",
							id : "typeRemark",
							name : "typeRemark",
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
				type2Box.bindPageValue('CotTypeLv2','id',record.data.typeIdLv2);
				Ext.getCmp("saveBtn").setText("Edit");
				Ext.getCmp("saveBtn").setIconClass("page_mod");
			});
});
// 添加或修改记录
function saveOrUpdate() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var roleName = Ext.get("typeName").getValue();
	var form = Ext.getCmp("typeFormId").getForm();
	if (!form.isValid())
		return;
	if (id == null || id == "") {
		// 表单验证结束
		var obj = DWRUtil.getValues("typeForm");
		var cotTypeLv = new CotTypeLv3();
		var list = new Array();
		for (var p in obj) {
			cotTypeLv[p] = obj[p];
		}
		list.push(cotTypeLv);
		var isExist = false;
		DWREngine.setAsync(false);
		cotTypeLv3Service.findExistByName(cotTypeLv.typeName, function(res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.MessageBox.alert("Message", 'Recorded the same name already exists!');
			return;
		}
		// 添加员工
		cotTypeLv3Service.addTypeLv(list, function(res) {
					reloadGrid("typeGrid");
				});
		DWREngine.setAsync(true);
	} else {
		// 表单验证结束
		var obj = DWRUtil.getValues("typeForm");
		var cotTypeLv = new CotTypeLv3();
		var list = new Array();
		for (var p in obj) {
			cotTypeLv[p] = obj[p];
		}
		cotTypeLv.id = id;
		list.push(cotTypeLv);
		cotTypeLv3Service.modifyTypeLv(list, function(res) {
					Ext.MessageBox.alert("Message", 'Edit Successful');
					reloadGrid("typeGrid");
				});
	}

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
	var flag = window.confirm("Are you sure to delete this record?");
	if (flag) {
		var obj = DWRUtil.getValues("typeForm");
		var cotTypeLv = new CotTypeLv3();
		var list = new Array();
		cotTypeLv.id = id;
		list.push(cotTypeLv);
		cotTypeLv3Service.deleteTypeLv(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.MessageBox.alert("Message", 'You do not have permission to delete!');
						return;
					}
					Ext.MessageBox.alert("Message", 'Deleted Successful');
					reloadGrid("typeGrid");
					clearForm("typeFormId");
				});
	} else {
		return;
	}
}
function getIds() {
	var list = Ext.getCmp("typeGrid").getSelectionModel().getSelections();
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
		Ext.MessageBox.alert("Message", 'Choose！');
		return;
	}
	var flag = window.confirm("Are you sure to delete this record?");
	if (flag) {

		cotTypeLv3Service.deleteTypeLv(list, function(res) {
					if (res == -1) {
						Ext.MessageBox.alert("Message", 'Can not Delete this Record,Data cited!');
						return;
					}
					Ext.MessageBox.alert("Message", 'Deleted Successful！');
					reloadGrid("typeGrid");
					clearForm("typeFormId");

				});
	} else {
		return;
	}
}
