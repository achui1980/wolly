Ext.onReady(function() {
	var curData;
	var facMap;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	DWREngine.setAsync(true);

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "signTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "signNo"
			}, {
				name : "empsName"
			}, {
				name : "customerShortName"
			}, {
				name : "factoryId"
			}, {
				name : "signRequire"
			}, {
				name : "requireTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "arriveTime",
				sortType : timeSortType.createDelegate(this)
			}

	]);
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
							url : "cotelements.do?method=loadSignInfo&eleId="+$('eleId').value
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
							header : "下单日期",
							dataIndex : "signTime",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return value.year + "-" + (value.month + 1)
											+ "-" + value.day;
								}
							}
						}, {
							header : "征样单号",
							dataIndex : "signNo",
							width : 200
						}, {
							header : "业务员",
							dataIndex : "empsName",
							width : 130
						}, {
							header : "客户简称",
							dataIndex : "customerShortName",
							width : 130
						}, {
							header : "厂家",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "送样日期",
							dataIndex : "requireTime",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return value.year + "-" + (value.month + 1)
											+ "-" + value.day;
								}
							}
						}, {
							header : "到样日期",
							dataIndex : "arriveTime",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return value.year + "-" + (value.month + 1)
											+ "-" + value.day;
								}
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

	// 厂家
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
		cmpId : 'factoryId',
		emptyText : "厂家",
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
	});
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'bussinessPerson',
				emptyText : '业务员',
				width : 120,
				editable : true,
				isSearchField : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'bussinessPerson'
			});
			
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "起始时间",
							width : 90,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "结束时间",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, facBox, busiBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "征样单号",
							isSearchField : true,
							searchName : 'signNoFind',
							isJsonType : false,
							store : ds
						}]
			});
	var grid = new Ext.grid.GridPanel({
				id : "chekGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
	grid.on("rowdblclick", showPWin)

	// 显示客户编辑页面
	function showPWin(gd, rowIndex) {
		// 添加权限判断
		var isPopedom = checkPopedoms('cotsign.do', "MOD");
		if (!isPopedom) {
			alert("您没有修改权限");
			return;
		}
		var rec = gd.getStore().getAt(rowIndex);
		var obj = rec.get("id");
		openCustWindow('cotsign.do?method=add&id=' + obj);
	}
})