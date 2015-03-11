Ext.onReady(function() {
	var auditMap = {
		"1" : "空白", 
		"2" : "领单",
		"3" : "交单",
		"4" : "核销",
		"5" : "逾期",
		"6" : "迟交"
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
				name : "auditNo"
			}, {
				name : "receiveDate",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "effectDate",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "auditStatus"
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
							url : "cotaudit.do?method=query"
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
							header : "核销单号",
							dataIndex : "auditNo",
							width : 130
						}, {
							header : "领单日期",
							dataIndex : "receiveDate",
							width : 120,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
								else
									return value;
							}
						}, {
							header : "有效日期",
							dataIndex : "effectDate",
							width : 80,
							renderer : function(value) {
								if (value)
									return Ext.util.Format.date(new Date(
													value.year, value.month,
													value.day), 'Y-m-d');
								else
									return value;
							}
						}, {
							header : "核销状态",
							dataIndex : "auditStatus",
							width : 60,
							renderer : function(value) {
								return auditMap[value]
							}
						}, {
							header : "操作",
							dataIndex : "id",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var ling = '<a href="javascript:windowopen('
										+ value + ')">领单</a>';
								var nbsp = "&nbsp &nbsp &nbsp"
								var jiao = '<a href="javascript:submitAudit('
										+ value + ')">交单</a>';
								var he = '<a href="javascript:audit(' + value
										+ ')">核销</a>';
								return ling + nbsp + jiao + nbsp + he;
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
	// 核销状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [["", "核销状态"], ["1", "空白"], ["2", "领单"], ["3", "交单"],
						["4", "核销"], ["5", "逾期"], ["6", "迟交"]]
			});
	var shenBox = new Ext.form.ComboBox({
				emptyText : '核销状态',
				editable : false,
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width:120,
				hiddenName : 'auditStatus',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'auditStatus'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
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
						}, shenBox,{
							xtype:'searchcombo',
							width:1,
							cls:'hideCombo',
							editable : false,
							isJsonType : false,
							store : ds
						}, '->', {
							text : "生成核销单",
							handler : createaudit,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}

				]
			});

	var grid = new Ext.grid.GridPanel({
				id : "auditGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [grid]
			});

	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				openwindow(record.get("id"), record.get("auditStatus"))
			});
});
function getIds() {
	var list = Ext.getCmp("auditGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotAudit = new CotAudit();
				cotAudit.id = item.id;
				res.push(cotAudit);
			});
	return res;
}
// 生成核销单
function createaudit() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有生成核销单权限!");
		return;
	} else {
		openWindowBase(260, 520, 'cotaudit.do?method=add');
	}
}

// 领单
function windowopen(obj) {

	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "USED");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有领单权限!");
		return;
	}

	cotAuditService.getCotAuditById(parseInt(obj), function(res) {
				if (res.auditStatus == 5 || res.auditStatus == 6) {
					openWindowBase(280, 1024, 'cotaudit.do?method=modify&id='
									+ obj);
					return;
				}
				if (res.auditStatus == 3) {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=3&id=' + obj);
					return;
				}
				if (res.auditStatus == 4) {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=4&id=' + obj);
					return;
				}
				if (res.auditStatus == 2) {
					Ext.Msg.alert("提示信息", "该核销单已被领用!");
					return;
				} else {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=2&id=' + obj);
				}
			});
}

// 交单
function submitAudit(obj) {

	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "SUBMIT");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有交单权限!");
		return;
	}
	cotAuditService.getCotAuditById(parseInt(obj), function(res) {
				if (res.auditStatus == 5 || res.auditStatus == 6) {
					openWindowBase(280, 1024, 'cotaudit.do?method=modify&id='
									+ obj);
					return;
				}
				if (res.auditStatus == 3) {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=3&id=' + obj);
					return;
				}
				if (res.auditStatus == 4) {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=4&id=' + obj);
					return;
				}
				/*
				 * if(res.auditStatus == 2){
				 * openWindowBase(280,1024,'cotaudit.do?method=modify&flag=3&id='+obj);
				 * return; }
				 */
				if (res.auditStatus == 1) {
					Ext.Msg.alert("提示信息", "该核销单还未被领用，不能执行交单动作！!");
					return;
				} else {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=3&id=' + obj);
				}
			});
}
// 核销
function audit(obj) {

	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "AUDIT");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有核销权限!");
		return;
	}
	cotAuditService.getCotAuditById(parseInt(obj), function(res) {
				if (res.auditStatus == 5 || res.auditStatus == 6) {
					openWindowBase(280, 1024, 'cotaudit.do?method=modify&id='
									+ obj);
					return;
				}
				if (res.auditStatus == 4) {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=4&id=' + obj);
					return;
				}
				if (res.auditStatus == 1) {
					Ext.Msg.alert("提示信息", "该核销单还未被领用，不能执行核销动作！!");
					return;
				} else if (res.auditStatus == 2) {
					Ext.Msg.alert("提示信息", "该核销单还未交单，不能执行核销动作！!");
					return;
				} else {
					openWindowBase(280, 1024,
							'cotaudit.do?method=modify&flag=4&id=' + obj);
				}
			});
}
// 根据状态双击打开记录
function openwindow(id, status) {
	if (status == 1) {
		// 领单权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "USED");
		if (isPopedom == 0)// 没有领单权限
		{
			Ext.Msg.alert("提示信息", "您没有领单权限!");
			return;
		} else {
			openWindowBase(280, 1024, 'cotaudit.do?method=modify&flag=2&id='
							+ id);
		}
	}
	if (status == 2) {
		// 交单权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "SUBMIT");
		if (isPopedom == 0)// 没有交单权限
		{
			Ext.Msg.alert("提示信息", "您没有交单权限!");
			return;
		} else {
			openWindowBase(280, 1024, 'cotaudit.do?method=modify&flag=3&id='
							+ id);
		}
	}
	if (status == 3) {
		// 核销权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "SUBMIT");
		if (isPopedom == 0)// 没有核销权限
		{
			Ext.Msg.alert("提示信息", "您没有核销权限!");
			return;
		} else {
			openWindowBase(280, 1024, 'cotaudit.do?method=modify&flag=4&id='
							+ id);
		}
	}
	if (status == 4) {
		openWindowBase(280, 1024, 'cotaudit.do?method=modify&id=' + id);
	}
	if (status == 5 || status == 6) {
		openWindowBase(280, 1024, 'cotaudit.do?method=modify&id=' + id);
	}
}
