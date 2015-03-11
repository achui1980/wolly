Ext.onReady(function() {
			var nationMap;
			baseDataUtil.getBaseDicDataMap("CotNation", "id", "nationName",
					function(res) {
						nationMap = res;
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
						name : "provinceName"
					}, {
						name : "nationId",
						type : "int"
					}, {
						name : "provinceRemark"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotprovince.do?method=query"
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
			var cm = new Ext.grid.ColumnModel([
					sm,// 添加复选框列
					{
						header : "ID", // 表头
						dataIndex : "id", // 数据列索引，对应于recordType对象中的name属性
						width : 50,
						sortable : true,// 是否排序
						hidden : true
					}, {
						header : "Province Name", // 表头
						dataIndex : "provinceName", // 数据列索引，对应于recordType对象中的name属性
						width : 120,
						sortable : true
						// 是否排序
				}	, {
						header : "Country", // 表头
						dataIndex : "nationId", // 数据列索引，对应于recordType对象中的name属性
						width : 130,
						renderer : function(value) {
							return nationMap[value];
						},
						sortable : false
						// 是否排序
				}	, {
						header : "Remark", // 表头
						dataIndex : "provinceRemark", // 数据列索引，对应于recordType对象中的name属性
						width : 300,
						sortable : false
						// 是否排序
				}	, {
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
				// displayMsg: '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all'
					// emptyMsg: "No data to display"
				});
			var tb = new Ext.Toolbar({
						items : ['->', {
									text : "Bulk Delete",
									handler : deleteBatch,
									iconCls : "page_del"
								}]
					});
			var cb = new Ext.form.ComboBox()
			var grid = new Ext.grid.GridPanel({
						iconCls : "gird_list",
						region : "center",
						id : "provinceGrid",
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

			/*----------------------------------------------------*/
			DWREngine.setAsync(false);
			
			// 国家
			var countryBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotNation&key=nationName",
				cmpId : 'nationId',
				fieldLabel : "Country*",
				editable : true,
				valueField : "id",
				displayField : "nationName",
				emptyText : 'Choose',
				mode : 'remote',
				pageSize : 10,
				anchor : "95%",
				allowBlank : false,
				blankText : "Please Choose Country",
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 250,// 下
				triggerAction : 'all'
			});
			
			// 绑定下拉框,需要设置同步，不然无法绑定
			bindNationSel();
			var form = new Ext.form.FormPanel({
						title : "Add|Edit|Query(带*号为可查询项)",
						region : 'east',
						id : "provinceFormId",
						formId : "provinceForm",
						frame : true,
						width : 260,
						monitorValid : true,
						autoHeight : true,
						buttonAlign : "center",
						labelAlign : 'right',
						labelWidth : 100,
						defaultType : 'textfield',
						defaults : {
							width : 200,
							allowBlank : false
						},
						items : [{
									fieldLabel : "Province Name*",
									id : "provinceName",
									name : "provinceName",
									allowBlank : false,
									blankText : "Please Enter Province Name",
									anchor : "95%"

								},countryBox, {
									xtype : "textarea",
									fieldLabel : "Remark",
									id : "provinceRemark",
									name : "provinceRemark",
									allowBlank : true,
									anchor : "95%"
								}, new Ext.form.Hidden({
											id : "id",
											name : "id"
										})],
						buttons : [{
									enableToggle : true,
									width : 65,
									text : "Create",
									cls : "SYSOP_ADD",
									iconCls : "page_add",
									id : "saveBtn",
									formBind : true,
									handler : saveOrUpdate
								}, {
									enableToggle : true,
									iconCls : "page_sel",
									width : 65,
									text : "query",
									handler : function() {
										ds.reload({
													params : {
														start : 0,
														limit : 15
													}
												})
									}
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
						countryBox.bindPageValue('CotNation','id',record.data.nationId);
						Ext.getCmp("saveBtn").setText("Update");
						Ext.getCmp("saveBtn").setIconClass("page_mod");
					});

		});
// 添加或修改记录
function saveOrUpdate() {
	// 表单验证
	var id = Ext.get("id").getValue();
	var roleName = Ext.get("provinceName").getValue();
	var form = Ext.getCmp("provinceFormId").getForm();
	if (!form.isValid())
		return;
	if (id == null || id == "") {
		// 表单验证结束
		var obj = DWRUtil.getValues("provinceForm");
		var cotProvince = new CotProvince();
		var list = new Array();
		for (var p in obj) {
			cotProvince[p] = obj[p];
		}
		list.push(cotProvince);
		var isExist = false;
		DWREngine.setAsync(false);
		cotProvinceService.findExistByName(cotProvince.provinceName, function(
						res) {
					isExist = res;
				});
		// 判断是否同名
		if (isExist) {
			Ext.Msg.alert("提示信息", "已存在同名记录");
			return;
		}
		// 添加员工
		cotProvinceService.addProvince(list, function(res) {
					clearForm("provinceFormId");
					reloadGrid("provinceGrid");
				});
		DWREngine.setAsync(true);
	} else {
		// 表单验证结束
		var obj = DWRUtil.getValues("provinceForm");
		var cotProvince = new CotProvince();
		var list = new Array();
		for (var p in obj) {
			cotProvince[p] = obj[p];
		}
		cotProvince.id = id;
		list.push(cotProvince);
		cotProvinceService.modifyProvince(list, function(res) {
					Ext.Msg.alert("提示信息", "修改成功");
					clearForm("provinceFormId");
					reloadGrid("provinceGrid");
				});
	}

}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示信息", "您没有删除权限");
		return;
	}
	var flag = window.confirm("确定删除?");
	if (flag) {
		var obj = DWRUtil.getValues("provinceForm");
		var cotProvince = new CotProvince();
		var list = new Array();
		cotProvince.id = id;
		list.push(cotProvince);
		cotProvinceService.deleteProvince(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.Msg.alert("提示信息", "已有其它记录使用到该记录,无法删除");
						return;
					}
					Ext.Msg.alert("提示信息", "删除成功");
					clearForm("provinceFormId");
					reloadGrid("provinceGrid");

				});
	} else {
		return;
	}
}
function getIds() {
	var list = Ext.getCmp("provinceGrid").getSelectionModel().getSelections();
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
		Ext.Msg.alert("提示信息", "请选择记录");
		return;
	}
	var flag = window.confirm("确定删除?");
	if (flag) {

		cotProvinceService.deleteProvince(list, function(res) {
					result = res;
					if (result == -1) {
						Ext.Msg.alert("提示信息", "已有其它记录使用到该记录,无法删除");
						return;
					}
					Ext.Msg.alert("提示信息", "删除成功");
					clearForm("provinceFormId");
					reloadGrid("provinceGrid");

				});
	} else {
		return;
	}
}
