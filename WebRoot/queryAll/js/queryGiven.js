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
						name : "givenNo"
					}, {
						name : "eleId"
					}, {
						name : "eleName"
					}, {
						name : "givenTime",
						sortType:timeSortType.createDelegate(this)
					}, {
						name : "givenAddr"
					}, {
						name : "customerShortName"
					}, {
						name : "empsName"
					}, {
						name : "priceOut"
					}, {
						name : "priceOutUint"
					}

			]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotqueryall.do?method=queryGiven"
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
									header : "主单号",
									dataIndex : "givenNo",
									width : 200
								}, {
									header : "货号",
									dataIndex : "eleId",
									width : 130
								},{
									header : "单价",
									dataIndex : "priceOut",
									width : 80
								},{
									header : "币种",
									dataIndex : "priceOutUint",
									width : 80,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										return parent.currencyMap[value]
									}
								},  {
									header : "中文名",
									dataIndex : "eleName",
									width : 130
								}, {
									header : "送样时间",
									dataIndex : "givenTime",
									width : 120,
									renderer : function(value) {
										if (value != null) {
											return Ext.util.Format.date(
													new Date(value.time), "Y-m-d");
										}
									}
								}, {
									header : "送样地点",
									dataIndex : "givenAddr",
									width : 120
								}, {
									header : "客户",
									dataIndex : "customerShortName",
									width : 130
								}, {
									header : "业务员",
									dataIndex : "empsName",
									width : 130
								}

						]
					});
			var toolBar = new Ext.PagingToolbar({
						pageSize : 15,
						store : ds,
						displayInfo : true,
						//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display"
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
				var isPopedom = checkPopedoms('cotgiven.do', "MOD");
				if (!isPopedom) {
					alert("您没有修改权限");
					return;
				}
				var rec = gd.getStore().getAt(rowIndex);
				var obj = rec.get("id");
				openFullWindow('cotgiven.do?method=add&id=' + obj);
			}
		})