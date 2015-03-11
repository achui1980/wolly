Ext.onReady(function() {

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
				name : "factoryId",
				type : "int"
			}, {
				name : "mainFlag",
				type : "int"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotcontact.do?method=query"
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
							header : "Suppliers",
							dataIndex : "factoryId",
							width : 120,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "Contact",
							dataIndex : "contactPerson",
							width : 100
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
						}, {
							header : "Jobs",
							dataIndex : "contactDuty",
							width : 100
						}, {
							header : "Phone",
							dataIndex : "contactNbr",
							width : 100
						}, {
							header : "Mobile",
							dataIndex : "contactPhone",
							width : 100
						}, {
							header : "Fax",
							dataIndex : "contactFax",
							width : 100
						}, {
							header : "E-mail",
							dataIndex : "contactEmail",
							width : 200
						}, {
							header : "Remarks",
							dataIndex : "contactRemark",
							width : 250
						}, {
							header : "Operation",
							dataIndex : "id",
							renderer : function(value) {
								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:del(' + value
										+ ')">Delete</a>';
								return del + nbsp;
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	var combox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryId',
				emptyText : "Suppliers",
				editable : true,
				sendMethod : "post",
				valueField : "id",
				displayField : "shortName",
				pageSize : 10,
				width : 120,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'factoryId'
			})
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', combox, {
							xtype : 'searchcombo',
							width : 120,
							emptyText : "Contact",
							isSearchField : true,
							searchName : 'contactFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							cls : "SYSOP_ADD",
							iconCls : "page_add",
							handler : windowopenAdd
						}, '-', {
							text : "Update",
							cls : "SYSOP_MOD",
							iconCls : "page_mod",
							handler : windowopenMod
									.createDelegate(this, [null])
						}, '-', {
							text : "Delete",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}]
			});
	var grid = new Ext.grid.GridPanel({
				border : false,
				id : "contactGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				split : true,
				viewConfig : {
					forceFit : false
				}
			});

	var viewport = new Ext.Viewport({
				layout : 'fit',
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
			Ext.Msg.alert("Message", "Sorry, you do not have Authority! ");
			return;
		}

		var config = {
			title : "Confirmation box",
			msg : "Delete this supplier Contact?",
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
									Ext.MessageBox.alert("Message",
											'Deleted successfully！');
								} else {
									Ext.MessageBox.alert("Message",
											"Delete failed!");
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
			Ext.Msg.alert("Message", "Please select records");
			return;
		}

		var config = {
			title : "Confirmation box",
			msg : "Sure to delete the selected contact manufacturers?",
			width : 240,
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					cotContactService.deleteContact(list, function(delres) {
								if (delres) {
									reloadGrid("contactGrid");
									clearForm("contactFormId");
									Ext.MessageBox.alert("Confirmation box",
											'Deleted successfully！');
								} else {
									Ext.MessageBox.alert("Confirmation box",
											"Delete failed!");
								}
							});
				}
			}
		}

		Ext.MessageBox.show(config);

	}
	// 查询
	function queryForm() {
		// 表单提交的数据
		var formData = getFormValues(form, false);
		ds.load({
					params : {
						start : 0,
						limit : 15,
						'contactFind' : formData.contactFind,
						'factoryId' : formData.factoryId
					}
				});
	}

	// 打开新增页面
	function windowopenAdd() {
		openWindowBase(280, 500, 'cotcontact.do?method=modify');
	}

	// 打开编辑页面
	function windowopenMod(obj) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("Message", "Sorry, you do not have Authority! ");
			return;
		}
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.Msg.alert("Message", "Please select a record");
				return;
			} else if (ids.length > 1) {
				Ext.Msg.alert("Message", "Choose only one record!")
				return;
			} else {
				obj = ids[0].id;
			}
		}
		openWindowBase(280, 500, 'cotcontact.do?method=modify&id=' + obj);
	}
});
