Ext.onReady(function() {
	var empMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsId", function(res) {
				empMap = res;
			});
	var typeMap = {
		"0" : "系统登录",
		"1" : "添加",
		"2" : "修改",
		"3" : "删除"
	}
	var moduleMap = {
		"login" : "系统登录",
		"elements" : "样品",
		"price" : "报价",
		"order" : "订单",
		"orderOut" : "出货",
		"orderfac" : "生产合同",
		"given" : "送样",
		"sign" : "征样",
		"split" : "排载"
	}
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
				name : "opModule"
			}, {
				name : "opType",
				type : "int"
			}, {
				name : "opTime"
			}, {
				name : "empId",
				type : "int"
			}, {
				name : "opMessage"
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
							url : "cotsyslog.do?method=query"
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
				columns : [
						sm,// 添加复选框列
						{
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "操作模块",
							dataIndex : "opModule",
							width : 120,
							renderer : function(value) {
								return moduleMap[value];
							}
						}, {
							header : "操作类型",
							dataIndex : "opType",
							width : 130,
							renderer : function(value) {
								return typeMap[value]
							}
						}, {
							header : "操作时间",
							dataIndex : "opTime",
							width : 130,
							renderer : function(value) {
								return Ext.util.Format.date(
										new Date(value.time), "Y-m-d H:i:s")
								// return cityMap[value];
							}
						}, {
							header : "操作员工",
							dataIndex : "empId",
							width : 130,
							renderer : function(value) {
								return empMap[value];
							}
						}, {
							header : "日志内容",
							dataIndex : "opMessage",
							width : 450,
							renderer : function(value, meta, rec, rowIdx,
									colIdx, ds) {
								meta.attr = ' ext:qtip="'
										+ rec.get('opMessage') + '"';
								// ext:qwidth="500"
								return value;
							}
						}, {
							header : "操作",
							dataIndex : "id",
							renderer : function(value) {
								// var mod = '<a
								// href="javascript:modTypeById('+value+')">修改</a>';

								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:deleteBatch()">删除</a>';
								return del + nbsp;
							}
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|300|500',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 操作类型
	var opTypeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['', '操作类型'], [-1, '系统登录'], [1, '添加'], [2, '修改'],
						[3, '删除']]
			});
	var opTypeBox = new Ext.form.ComboBox({
				emptyText : '操作类型',
				editable : false,
				store : opTypeStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 120,
				hiddenName : 'opTypeFind',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'opTypeFind'
			});

	// 操作模块
	var opModuleStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['', '操作模块'], ['login', '系统登录'], ['elements', '样品'],
						['price', '报价'], ['order', '订单'], ['orderOut', '出货'],
						['orderfac', '生产合同'], ['given', '送样'], ['sign', '征样'],
						['split', '排载']]
			});
	var opModuleBox = new Ext.form.ComboBox({
				emptyText : '操作模块',
				editable : false,
				store : opModuleStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 120,
				hiddenName : 'opModuleFind',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'opModuleFind'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "combo",
							triggerAction : "all",
							id : "empIdFind",
							width : 120,
							emptyText : "操作员工",
							isSearchField : true,
							searchName : 'empIdFind'
						}, {
							xtype : "datefield",
							emptyText : "起始时间",
							width : 100,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "结束时间",
							width : 100,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, opModuleBox, opTypeBox, {
							xtype : 'searchcombo',
							width : 1,
							cls : 'hideCombo',
							editable : false,
							isJsonType : false,
							store : ds
						}, '->', {
							text : "删除",
							handler : deleteBatch,
							iconCls : "page_del"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "logGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});
});
function getIds() {
	var list = Ext.getCmp("logGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
// 批量删除
function deleteBatch() {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示信息", "您没有删除权限");
		return;
	}
	var ids = getIds();
	if (ids.length == 0) {
		Ext.Msg.alert("提示信息", "请选择记录");
		return;
	}
	var flag = window.confirm("确定删除?");
	if (flag) {
		cotSysLogService.deleteSyslogByIds(ids, function(res) {
					if (!res) {
						Ext.Msg.alert("提示信息", "已有其它记录使用到该记录,无法删除!");
						return;
					} else {
						Ext.Msg.alert("提示信息", "删除成功!");
						reloadGrid("logGrid");
					}
				})
	} else {
		return;
	}
}