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
						name : "givenIscheck",
						type : "int"
					}, {
						name : "givenTime",
						sortType:function(value){
							if(value)
								return value.time;
							else 
								return 0;
						}
					}, {
						name : "custId"
					}, {
						name : "bussinessPerson"
					}, {
						name : "custRequiretime"
					}, {
						name : "checkComplete"
					}, {
						name : "realGiventime"
					}, {
						name : "givenStatus"
					}]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotcheck.do?method=queryGiven"
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
									header : "主单号",
									dataIndex : "givenNo",
									width : 200
								}, {
									header : "审核状态",
									dataIndex : "givenIscheck",
									width : 130,
									renderer : function(value) {
										return parent.checkStatusMap[value]
									}
								}, {
									header : "下单日期",
									dataIndex : "givenTime",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										if (value)
											return Ext.util.Format
													.date(
															new Date(
																	value.time),
															"Y-m-d");
										else
											return value;
									}
								}, {
									header : "客户",
									dataIndex : "custId",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										return parent.custMap[value];
									}
								}, {
									header : "业务员",
									dataIndex : "bussinessPerson",
									width : 130,
									renderer : function(value) {
										return parent.empsMap[value];
									}
								}, {
									header : "是否送样",
									dataIndex : "checkComplete",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										if (value == "1")
											return "是";
										else
											return "否"
									}
								}, {
									header : "送样日期",
									dataIndex : "realGiventime",
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
									header : "征样完成",
									dataIndex : "givenStatus",
									width : 120,
									renderer : function(value, meta, rec,
											rowIdx, colIdx, ds) {
										if (value == "1")
											return "是";
										else
											return "否"
									}
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
				var isPopedom = checkPopedoms('cotgiven.do', "MOD");
				if (!isPopedom) {
					Ext.Msg.alert("提示信息", "您没有修改权限!");
					return;
				}
				var rec = gd.getStore().getAt(rowIndex);
				var obj = rec.get("id");
				openCustWindow('cotgiven.do?method=add&id=' + obj);
			}
		})