Ext.onReady(function() {
			DWREngine.setAsync(false);
			var nationMap;
			baseDataUtil.getBaseDicDataMap("CotNation", "id", "nationName",
					function(res) {
						nationMap = res;
					});
			var proMap;
			baseDataUtil.getBaseDicDataMap("CotProvince", "id", "provinceName",
					function(res) {
						proMap = res;
					});
			DWREngine.setAsync(true);
			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "factoryNo"
					}, {
						name : "shortName"
					}, {
						name : "factoryNbr"
					}, {
						name : "factoryFax"
					}, {
						name : "factoryAddr"
					}, {
						name : "factroyTypeidLv1"
					},{
						name:'nationId'
					},{
						name:'factoryEmail'
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						autoLoad : {
							params : {
								start : 0
							}
						},
						baseParams : {
							limit : 20,
							facType :1
						},
						proxy : new Ext.data.HttpProxy({
									url : "cotfactory.do?method=query"
								}),
						reader : new Ext.data.JsonReader({
									root : "data",
									totalProperty : "totalCount",
									idProperty : "id"
								}, roleRecord)
					});

			// 加载表格需要关联的外键名
			var currencyMap = {};
			function getDataMap() {
				baseDataUtil.getBaseDicDataMap("CotCurrency", "id",
						"curNameEn", function(res) {
							currencyMap = res;
						});
			}
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
									header : "No.",
									dataIndex : "factoryNo",
									width : 100
								}, {
									header : "Short Title",
									dataIndex : "shortName",
									width : 120
								}, {
									header : "Tel.",
									dataIndex : "factoryNbr",
									width : 120
								}, {
									header : "Fax",
									dataIndex : "factoryFax",
									width : 120
								}, {
									header : "Email",
									dataIndex : "factoryEmail",
									width : 120
								}, {
									header : "Country",
									dataIndex : "nationId",
									width : 90,
									renderer : function(value) {
										return nationMap[value];
									}
								}, {
									header : "Provinces",
									dataIndex : "provinceId",
									width : 90,
									hidden:true,
									renderer : function(value) {
										return proMap[value];
									}
								},{
									header : "Status",
									dataIndex : "factroyTypeidLv1",
									width : 60,
									renderer : function(value) {
										return value==1?'Enable':'Disable'
									}
								},  {
									header : "Address",
									dataIndex : "factoryAddr",
									width : 300
								}, {
									header : "Operation",
									dataIndex : "id",
									width : 80,
									renderer : function(value) {
										var nbsp = "&nbsp &nbsp &nbsp"
										var del = '<a href="javascript:delFactory('
												+ value + ')">Delete</a>';
										return del + nbsp;
									}
								}]
					});

			var toolBar = new Ext.PagingToolbar({
						pageSize : 20,
						store : ds,
						displayInfo : true,
						//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display",
						listeners : {
							beforechange : function(pTbar, params) {
								pTbar.store.setBaseParam('limit', params.limit);
							}
						}
					});

			// 厂家类型
			var shenStore = new Ext.data.SimpleStore({
						fields : ["tp", "name"],
						data : [[1, 'Enable'], [0, 'Disable']]
					});
			var typeBox = new Ext.form.ComboBox({
						emptyText : 'Status',
						editable : false,
						store : shenStore,
						valueField : "tp",
						displayField : "name",
						mode : 'local',
						validateOnBlur : true,
						triggerAction : 'all',
						width : 120,
						value:1,
						hiddenName : 'facType',
						selectOnFocus : true,
						isSearchField : true,
						searchName : 'facType'
					});

			var tb = new Ext.ux.SearchComboxToolbar({
						items : ['-', {
									xtype : 'textfield',
									width : 120,
									emptyText : "No.",
									isSearchField : true,
									searchName : 'factoryNoFind'
								}, {
									xtype : 'textfield',
									width : 120,
									emptyText : "Short Title",
									isSearchField : true,
									searchName : 'shortNameFind'
								}, {
									xtype : 'textfield',
									width : 120,
									emptyText : "Tel.",
									isSearchField : true,
									searchName : 'factoryNbr'
								}, typeBox, {
									xtype : 'searchcombo',
									width : 120,
									emptyText : "Remark",
									isSearchField : true,
									searchName : 'remark',
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
									handler : windowopenMod.createDelegate(
											this, [null])
								}, '-', {
									text : "Delete",
									cls : "SYSOP_DEL",
									iconCls : "page_del",
									handler : deleteBatch
								}]

					});
			var grid = new Ext.grid.GridPanel({
						iconCls : "gird_list",
						region : "center",
						id : "factoryGrid",
						margins : '0 2 0 0',
						stripeRows : true,
						border : false,
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

			var viewport = new Ext.Viewport({
						layout : 'fit',
						items : [grid]
					});

			DWREngine.setAsync(false);
			// 加载表格关联数据
			getDataMap();
			DWREngine.setAsync(true);

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
							res.push(item.id);
						});
				return res;
			}
			// 批量删除
			function deleteBatch() {
				var list = getIds();
				if (list.length == 0) {
					Ext.MessageBox.alert("Message", 'Please select one record！');
					return;
				}
				Ext.MessageBox.confirm('Message', "Are you sure delete these suppliers?", function(btn) {
							if (btn == 'yes') {
								DWREngine.setAsync(false);
								var num = 0;
								for (var i = 0; i < list.length; i++) {
									cotFactoryService.deleteFactoryById(
											list[i], function(res) {
												if (res == -1) {
													num++;
												}
											});
								}
								if (num > 0) {
									Ext.Msg.alert("Message", "Delete Successfully," + num
													+ "nums can not delete!");
								} else {
									Ext.Msg.alert("Message", "Delete Successfully!");
								}
								reloadGrid('factoryGrid');
								DWREngine.setAsync(true);
							}
						});
			}

			// 查询
			function queryForm() {
				// 表单提交的数据
				var formData = getFormValues(form, false);
				ds.load({
							params : {
								start : 0,
								limit : 15,
								'factoryNoFind' : formData.factoryNoFind,
								'shortNameFind' : formData.shortNameFind,
								'textarea' : formData.textarea,
								'factoryNbr' : formData.factoryNbr
							}
						});
			}

			// 打开新增页面
			function windowopenAdd(obj) {
				openFullWindow('cotfactory.do?method=modify');
			}

			// 打开编辑页面
			function windowopenMod(obj) {
				// 添加权限判断
				var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
				if (isPopedom == 0)// 没有修改权限
				{
					Ext.Msg.alert("Message", "您没有修改权限");
					return;
				}
				if (obj == null) {
					var ids = getIds();
					if (ids.length == 0) {
						Ext.Msg.alert("Message", "Please select one record!");
						return;
					} else if (ids.length > 1) {
						Ext.Msg.alert("Message", "Only select one record!")
						return;
					} else {
						obj = ids[0];
					}
				}
				openFullWindow('cotfactory.do?method=modify&id='
								+ obj);
			}

		});

// 删除
function delFactory(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	var isPopedom = 1
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.MessageBox.alert("Message", "You do not have permission to delete!");
		return;
	}
	var config = {
		title : "Message",
		msg : "Are you sure delete the supplier?",
		width : 200,
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {
				var list = new Array();
				var factory = new CotFactory();
				factory.id = id;
				list.push(factory);
				cotFactoryService.deleteFactory(list, function(delres) {
							if (delres) {
								Ext.MessageBox.alert("Message", "Delete Successfully！");
								clearForm("factoryFormId");
								reloadGrid("factoryGrid");
							} else {
								Ext.MessageBox.alert("Message", "Delete Faild！");
							}
						});
			}
		}
	}
	Ext.MessageBox.show(config);
}
