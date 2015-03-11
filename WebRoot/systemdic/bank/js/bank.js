Ext.onReady(function() {

			// 加载表格需要关联的外键名
			var facMap;
			baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName",
					function(res) {
						facMap = res;
					});

			/** ******EXT创建grid步骤******** */
			/* 1、创建数据记录类型类型 Ext.data.Record.create */
			/* 2、创建数据存储对象(数据源) Ext.data.Store */
			/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
			/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "bankName"
					}, {
						name : "bankShortName"
					}, {
						name : "bankContact"
					}, {
						name : "bankPhone"
					}, {
						name : "bankFax"
					}, {
						name : "bankBeneficiary"
					}, {
						name : "bankSwif"
					}, {
						name : "bankTelex"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotbank.do?method=query"
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
									header : "bank name",
									dataIndex : "bankName",
									width : 150
								}, {
									header : "Short name",
									dataIndex : "bankShortName",
									width : 100
								}, {
									header : "Contact",
									dataIndex : "bankContact",
									width : 100
								}, {
									header : "Phone",
									dataIndex : "bankPhone",
									width : 150
								}, {
									header : "Fax",
									dataIndex : "bankFax",
									width : 150
								}, {
									header : "Beneficiary",
									dataIndex : "bankBeneficiary",
									width : 100
								}, {
									header : "SWIFT",
									dataIndex : "bankSwif",
									width : 100
								}, {
									header : "TELEX",
									dataIndex : "bankTelex",
									width : 100
								}, {
									header : "Operation",
									dataIndex : "id",
									width : 60,
									renderer : function(value) {
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
						// //displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all'
						// emptyMsg : "No data to display"
					});
			var tb = new Ext.Toolbar({
						items : ['->', '-', {
									text : "Create",
									cls : "SYSOP_ADD",
									iconCls : "gird_add",
									handler : windowopenAdd
								}, '-', {
									text : "Update",
									cls : "SYSOP_MOD",
									iconCls : "gird_edit",
									handler : windowopenMod.createDelegate(
											this, [null])
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
						id : "bankGrid",
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
						split : false,
						// layout:"fit",
						viewConfig : {
							forceFit : false
						}
					});

			/*----------------------------------------------------*/

			// 将查询面板的条件加到ds中
			ds.on('beforeload', function(store, options) {

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
							var bank = new CotBank();
							bank.id = item.id;
							res.push(bank);
						});
				return res;
			}
			// 删除
			this.del = function(id) {
				// 添加权限判断
				var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
				var isPopedom = 1
				if (isPopedom == 0)// 没有删除权限
				{
					Ext.Msg.alert("Message", "'Sorry, you do not have Authority!");
					return;
				}

				var config = {
					title : "",
					msg : "Are you sure you want to delete this person banking information?",
					width : 240,
					buttons : Ext.MessageBox.YESNO,
					icon : Ext.MessageBox.QUESTION,
					fn : function(btn) {
						if (btn == 'yes') {
							var list = new Array();
							var bank = new CotBank();
							bank.id = id;
							list.push(bank);
							cotBankService.deleteList(list, function(delres) {
										result = delres;
										if (result == -1) {
											Ext.MessageBox.alert("Message",
													"Delete failed, the product category has been in use");
											return;
										} else {
											reloadGrid("bankGrid");
											Ext.MessageBox.alert("Message",
													'Deleted successfully!');
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
					Ext.Msg.alert("Message", "Please select a record");
					return;
				}

				var config = {
					title : "",
					msg : "Are you sure you want to delete the selected bank information from?",
					width : 240,
					buttons : Ext.MessageBox.YESNO,
					icon : Ext.MessageBox.QUESTION,
					fn : function(btn) {
						if (btn == 'yes') {
							cotBankService.deleteList(list, function(delres) {
										result = delres;
										if (result == -1) {
											Ext.MessageBox.alert("Message",
													"Delete failed, the product category has been in use");
											return;
										} else {
											reloadGrid("bankGrid");
											Ext.MessageBox.alert("Message",
													'Deleted successfully！');
										}
									});
						}
					}
				}

				Ext.MessageBox.show(config);

			}

			//打开新增页面
			function windowopenAdd() {
				//添加权限判断
				var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
				if (isPopedom == 0)//没有添加权限
				{
					Ext.MessageBox.alert("Message", " Sorry, you do not have Authority!");
					return;
				}
				openWindowBase(320, 700, 'cotbank.do?method=modify');
			}

			//打开编辑页面
			function windowopenMod(obj) {
				//添加权限判断
				var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
				if (isPopedom == 0)//没有修改权限
				{
					Ext.MessageBox.alert("Message", " Sorry, you do not have Authority!");
					return;
				}
				if (obj == null) {
					var ids = getIds();
					if (ids.length == 0) {
						Ext.MessageBox.alert("Message", "Please select a record");
						return;
					} else if (ids.length > 1) {
						Ext.MessageBox.alert("Message", "You can only select a record!")
						return;
					} else {
						obj = ids[0].id;
					}
				}
				openWindowBase(320, 700, 'cotbank.do?method=modify&id=' + obj);
			}
		});
