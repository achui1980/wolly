var grid = null;
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
						name : "orderNo"
					}, {
						name : "orderStatus",
						type : "int"
					}, {
						name : "orderTime",
						sortType:function(value){
							if(value)
								return value.time;
							else 
								return 0;
						}
					}, {
						name : "sendTime",
						sortType:function(value){
							if(value)
								return value.time;
							else 
								return 0;
						}
					}, {
						name : "customerShortName"
					}, {
						name : "empsName"
					}, {
						name : "totalMoney"
					}, {
						name : "currencyId"
					}, {
						name : "clauseTypeId"
					}, {
						name : "payTypeId"
					}

			]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotcheck.do?method=queryOrder"
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
									header : "Master order number",
									dataIndex : "orderNo",
									width : 200
								}, {
									header : "Approval Status",
									dataIndex : "orderStatus",
									width : 130,
									renderer : function(value) {
										return parent.checkStatusMap[value]
									}
								}, {
									header : "Order date",
									dataIndex : "orderTime",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										if (value)
											return Ext.util.Format.date(
													new Date(value.time),
													"Y-m-d");
										else
											return value;
									}
								}, {
									header : "Delivery Date",
									dataIndex : "sendTime",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										if (value)
											return Ext.util.Format
													.date(
															new Date(
																	value.year,
																	value.month,
																	value.day),
															"Y-m-d");
										else
											return value;
									}
								}, {
									header : "Customer Name",
									dataIndex : "CustomerShortName",
									width : 130
								}, {
									header : "Sales",
									hidden:true,
									dataIndex : "empsName",
									width : 130
								}, {
									header : "Price Terms",
									dataIndex : "clauseTypeId",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										return parent.clauseMap[value]
									}
								}, {
									header : "Total amount",
									dataIndex : "totalMoney",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										return Ext.util.Format.number(value,
												"0.00");
									}
								}, {
									header : "Currency",
									dataIndex : "currencyId",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										return parent.currencyMap[value]
									}
								},

								{
									header : "Payment",
									dataIndex : "payTypeId",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										return parent.payTypeMap[value]
									}
								}

						]
					});
			var toolBar = new Ext.PagingToolbar({
						pageSize : 15,
						store : ds,
						displayInfo : true,
						displayMsg : 'Showing {0} - {1} {2} record total records',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No records"
					});
			grid = new Ext.grid.GridPanel({
						region : "center",
						id : "chekGrid",
						margins : "0 5 0 0",
						stripeRows : true,
						bodyStyle : 'width:100%',
						store : ds, //加载数据源
						cm : cm, //加载列
						sm : sm,
						//selModel: new Ext.grid.RowSelectionModel({singleSelect:false}),
						loadMask : true, //是否显示正在加载
						bbar : toolBar,
						//layout:"fit",
						viewConfig : {
							forceFit : false
						}
					});
			var viewport = new Ext.Viewport({
						layout : "border",
						items : [grid]
					});
			ds.on('beforeload', function() {
						ds.baseParams = parent.form.getForm().getValues();
					});
			//分页基本参数
			ds.load({
						params : {
							start : 0,
							limit : 15
						}
					});
			grid.on("rowdblclick", showPWin)

			//显示客户编辑页面
			function showPWin(gd, rowIndex) {
				//添加权限判断
				var isPopedom = checkPopedoms('cotorder.do', "MOD");
				if (!isPopedom) {
					Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
					return;
				}
				var rec = gd.getStore().getAt(rowIndex);
				var obj = rec.get("id");
				openCustWindow('cotorder.do?method=addOrder&id=' + obj);
			}
		})