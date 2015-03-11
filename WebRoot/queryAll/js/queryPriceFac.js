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
						name : "eleId"
					}, {
						name : "eleName"
					}, {
						name : "eleSizeDesc"
					}, {
						name : "addTime",
						sortType:timeSortType.createDelegate(this)
					}, {
						name : "priceFac",
						type : "float",
						convert : numFormat.createDelegate(this, ["0.00"], 3)
					}, {
						name : "priceUint"
					}, {
						name : "shortName"
					}, {
						name : "priceRemark"
					}, {
						name : "elId"
					}
			]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotqueryall.do?method=queryPriceFac"
								}),
						reader : new Ext.data.JsonReader({
									root : "data",
									totalProperty : "totalCount",
									idProperty : "detailId"
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
									header : "Date",
									dataIndex : "addTime",
									width : 120,
									renderer : function(value) {
										if (value != null) {
											return value.year+"-"+(value.month+1)+"-"+value.day;
										}
									}
								}, {
									header : "Art No.",
									dataIndex : "eleId",
									width : 130
								},  {
									header : "Descripiton",
									dataIndex : "eleName",
									width : 130
								}, {
									header : "Size(cm)",
									dataIndex : "eleSizeDesc",
									width : 130
								},{
									header : "Price",
									dataIndex : "priceFac",
									width : 80
								},{
									header : "Currency",
									dataIndex : "priceUint",
									width : 80,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										return parent.currencyMap[value]
									}
								},{
									header : "Supplier",
									dataIndex : "shortName",
									width : 80
								},{
									header : "Remarks",
									dataIndex : "priceRemark",
									width : 80
								}

						]
					});
			var toolBar = new Ext.PagingToolbar({
						pageSize : 15,
						store : ds,
						displayInfo : true,
						////displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all'
						//emptyMsg : "No data to display"
					});
			grid = new Ext.grid.GridPanel({
						id : "chekGrid",
						stripeRows : true,
						bodyStyle : 'width:100%',
						store : ds, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
						loadMask : true, // 是否显示正在加载
						bbar : toolBar,
						border:false,
						viewConfig : {
							forceFit : false
						}
					});
			var viewport = new Ext.Viewport({
						layout : "fit",
						items : [grid]
					});
			ds.on('beforeload', function() {
						ds.baseParams = parent.form.getForm().getValues();
					});
			// 分页基本参数
			ds.load({
						params : {
							start : 0,
							limit : 15
						}
					});
			grid.on("rowdblclick", showPWin)

			// 显示客户编辑页面
			function showPWin(gd, rowIndex) {
				// 添加权限判断
				var isPopedom = checkPopedoms('cotelements.do', "MOD");
				if (!isPopedom) {
					alert("'Sorry, you do not have Authority!");
					return;
				}
				var rec = gd.getStore().getAt(rowIndex);
				openCustWindow('cotelements.do?method=queryElements&id=' + rec.data.elId);
			}
		})