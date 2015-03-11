Ext.onReady(function() {

	// 加载表格需要关联的外键名
	var facMap;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	DWREngine.setAsync(true);

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
				name : "value"
			}, {
				name : "valueEn"
			}, {
				name : "type"
			}, {
				name : "materialPrice",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"],3)
			}, {
				name : "unit"
			}, {
				name : "facId",
				type : 'int'
			}, {
				name : "remark"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotboxpacking.do?method=query"
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
					header : "name-cn",
					dataIndex : "value",
					width : 120
				}, {
					header : "name-en",
					dataIndex : "valueEn",
					width : 160
				}, {
					header : "type",
					dataIndex : "type",
					width : 80,
					renderer : function(value) {
						if (value == 'IB') {
							return "Inner";
						}
						if (value == 'MB') {
							return "middle";
						}
						if (value == 'OB') {
							return "Carton";
						}
						if (value == 'PB') {
							return "Product";
						}
						if (value == 'IG') {
							return "插格类型";
						}
					}
				}, {
					header : "Price",
					dataIndex : "materialPrice",
					width : 80,
					align : "right",
					renderer : numFormat.createDelegate(this, ["0.000"],3),
					renderer : function(value) {
						return "<span style='color:blue;'>" + value + "</span>";
					}
				}, {
					header : "Unit",
					dataIndex : "unit",
					width : 65
				}, {
					header : "Suppliers",
					dataIndex : "facId",
					width : 120,
					renderer : function(value) {
						return facMap[value];
					}
				}, {
					header : "Remark",
					dataIndex : "remark",
					width : 300
				}, {
					header : "Operation",
					dataIndex : "id",
					renderer : function(value) {
						var nbsp = "&nbsp &nbsp &nbsp"
						var del = '<a href="javascript:del(' + value
								+ ')">删除</a>';
						return del + nbsp;
					}
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true
				////displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				//emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Create",
							cls : "SYSOP_ADD",
							iconCls : "page_add",
							handler : windowopenAdd
						}, '-', {
							text : "Update",
							cls : "SYSOP_MOD",
							iconCls : "gird_edit",
							handler : windowopenMod
									.createDelegate(this, [null])
						}, '-', {
							text : "Del",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}]
			});
	var grid = new Ext.grid.GridPanel({
				margins : '0 2 0 0',
				region : "center",
				id : "boxpackingGrid",
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

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});
	/*----------------------------------------------------*/

	var typeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['PB', '产品包装'], ['IB', '内盒包装'], ['MB', '中盒包装'],
						['OB', '外箱包装'], ['IG', '插格类型']]
			});

	var typeField = new Ext.form.ComboBox({
				name : 'type',
				fieldLabel : '包材类型',
				editable : true,
				store : typeStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "95%",
				emptyText : 'Choose',
				hiddenName : 'type',
				selectOnFocus : true
			});
	var form = new Ext.form.FormPanel({
				title : "Query",
				region : 'east',
				id : "boxpackingFormId",
				formId : "boxpackingForm",
				collapsible : true,
				frame : true,
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				labelWidth : 100,
				monitorValid : true,
				defaultType : 'textfield',
				buttonAlign : 'center',
				defaults : {
					width : 220,
					allowBlank : true
				},
				items : [{

							// 添加布局
							fieldLabel : "Name-cn",
							id : "valueFind",
							name : "valueFind",
							anchor : "95%"

						}, {
							fieldLabel : "Name-en",
							id : "valueEnFind",
							name : "valueEnFind",
							// regex:/\w+$/,
							// //正则表达式这里假设只允许输入数字如果输入的不是数字就会出现下面定义的提示信息
							// regexText:"只能够输入数字、字母、下划线的组合", //定义不符合正则表达式的提示信息
							anchor : "95%"
						}, typeField],
				buttons : [{
							enableToggle : true,
							// pressed:true,
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
								form.getForm().reset()
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

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	// 获得选择的记录
	function getIds() {
		var list = Ext.getCmp("boxpackingGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var cotBoxPacking = new CotBoxPacking();
					cotBoxPacking.id = item.id
					res.push(parseInt(item.id));
				});
		return res;
	}

	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.Msg.alert("Message", "Please select a record");
			return;
		}
		Ext.MessageBox.confirm('Message', 'Are you sure you want to delete?', function(btn) {
					if (btn == 'yes') {
						cotBoxPackingService.deleteBoxPackingByList(list,
								function(res) {
									result = res;
									if (result == 0) {
										Ext.Msg.alert("Message", "Deleted successfully");
										reloadGrid("boxpackingGrid");
										clearForm("boxpackingFormId");
									} else {
										Ext.Msg.alert("Message",
												"Delete failed")
										return;
									}
								});
					} else {
						return;
					}
				});
	}

	// 打开新增页面
	function windowopenAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("Message", "Sorry,you do not have Authority!");
			return;
		}
		openWindowBase(400, 700, 'cotboxpacking.do?method=add');
	}

	// 打开编辑页面
	function windowopenMod(obj) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("Message", "Sorry,you do not have Authority!");
			return;
		}
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.Msg.alert("Message", "Please select a record");
				return;
			} else if (ids.length > 1) {
				Ext.Msg.alert("Message", "You can only select a record!")
				return;
			} else {
				obj = ids[0];
			}
		}
		openWindowBase(400, 700, 'cotboxpacking.do?method=add&id=' + obj);
	}
});

// 删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	var isPopedom = 1
	if (isPopedom == 0)// 没有删除权限
	{
		alert("Sorry,you do not have Authority!");
		return;
	}
	var cotBoxPacking = new CotBoxPacking();
	var list = new Array();
	cotBoxPacking.id = id;
	list.push(parseInt(id));
	Ext.MessageBox.confirm('Message', 'Are you sure you want to delete?', function(btn) {
				if (btn == 'yes') {
					cotBoxPackingService.deleteBoxPackingByList(list, function(
									res) {
								if (res == 0) {
									reloadGrid("boxpackingGrid");
									clearForm("boxpackingFormId");
								} else {
									Ext.Msg.alert("Message", "Delete failed");
								}
							})
				}
			});
}