Ext.onReady(function() {

	var factoryId = $('factoryId').value;
	// 加载表格需要关联的外键名
	var facMap;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
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
				name : "contactPerson"
			}, {
				name : "contactDuty"
			}, {
				name : "contactNbr"
			}, {
				name : "contactPhone"
			}, {
				name : "contactFax"
			}, {
				name : "contactEmail"
			}, {
				name : "contactRemark"
			}, {
				name : "mainFlag",
				type : "int"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotfactory.do?method=queryContact&factoryId="
									+ factoryId
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
							header : "Contact",
							dataIndex : "contactPerson",
							width : 120
						}, {
							header : "Default",
							dataIndex : "mainFlag",
							width : 60,
							renderer : function(value) {
								if (value == 1) {
									return "yes";
								} else {
									return "no";
								}
							}
						},{
							header : "Position",
							dataIndex : "contactDuty",
							width : 120
						}, {
							header : "Tel.",
							dataIndex : "contactNbr",
							width : 120
						}, {
							header : "Mobile",
							dataIndex : "contactPhone",
							width : 120
						}, {
							header : "Fax.",
							hidden:true,
							dataIndex : "contactFax",
							width : 120
						}, {
							header : "E-mail",
							dataIndex : "contactEmail",
							width : 200
						}, {
							header : "Remarks",
							dataIndex : "contactRemark",
							width : 300
						}, {
							header : "Operation",
							dataIndex : "id",
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
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Create",
							cls : "SYSOP_ADD",
							iconCls : "gird_add",
							handler : windowopenAdd
						}, '-', {
							text : "Mod",
							cls : "SYSOP_MOD",
							iconCls : "page_mod",
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
				border : true,
				region : "center",
				id : "contactGrid",
				margins : '0 2 0 0',
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
				split : true,
				// layout:"fit",
				viewConfig : {
					forceFit : false
				}
			});

	// 将查询面板的条件加到ds中
	ds.on('beforeload', function(store, options) {
				// var recdata = {};
				// form.getForm().items.each(function(item){
				// recdata[item.getName()] = item.getRawValue();
				// });
				// Ext.apply(store.baseParams, recdata);
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	// 获得选择的记录
	var getIds = function() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var contact = new CotContact();
					contact.id = item.id;
					res.push(contact);
				});
		return res;
	}
	// 删除
	this.del = function(id) {
		// 添加权限判断
		// var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
		var isPopedom = 1
		if (isPopedom == 0)// 没有删除权限
		{
			Ext.Msg.alert("Message", "You do not have permission to delete");
			return;
		}

		var config = {
			title : "Message",
			msg : "Are you sure delete the contact?",
			width : 220,
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					var list = new Array();
					var contact = new CotContact();
					contact.id = id;
					list.push(contact);
					cotContactService.deleteContact(list, function(delres) {
								if (delres) {
									reloadGrid("contactGrid");
									clearForm("contactFormId");
									Ext.MessageBox.alert("Message", 'Delete Successfully！');
								} else {
									Ext.MessageBox.alert("Message", "Delete Failed!");
								}
							});
				}
			}
		}
		Ext.MessageBox.show(config);
	}
	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.Msg.alert("Message", "Please select one record!");
			return;
		}

		var config = {
			title : "Message",
			msg : "Are you sure delete the contact?",
			width : 240,
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					cotContactService.deleteContact(list, function(delres) {
								if (delres) {
									reloadGrid("contactGrid");
									clearForm("contactFormId");
									Ext.MessageBox.alert("Message", 'Delete Successfully！');
								} else {
									Ext.MessageBox.alert("Message", "Delete Failed!");
								}
							});
				}
			}
		}

		Ext.MessageBox.show(config);

	}

	// 打开新增页面
	function windowopenAdd() {
		if (factoryId == 'null' || factoryId == '' || factoryId == 0) {
			Ext.MessageBox.alert("Message", "Please save supplier")
			return;
		}
		openWindowBase(253, 600, 'cotfactory.do?method=addContact&fId='
						+ factoryId);
	}

	// 打开编辑页面
	function windowopenMod(obj) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("Message", "You do not modify the permissions!");
			return;
		}
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.Msg.alert("Message", "Please select one record");
				return;
			} else if (ids.length > 1) {
				Ext.Msg.alert("Message", "Only select one record")
				return;
			} else {
				obj = ids[0].id;
			}
		}
		openWindowBase(253, 600, 'cotfactory.do?method=addContact&id=' + obj
						+ '&fId=' + factoryId);
	}
});