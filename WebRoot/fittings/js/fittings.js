Ext.onReady(function() {
	var _self = this;
	var facData;
	var fittingsType;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotTypeLv3", "id", "typeName",
			function(res) {
				fittingsType = res;
			});			
	DWREngine.setAsync(true);
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
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "facId",
				type : "int"
			}, {
				name : "typeLv3Id",
				type : "int"
			}, {
				name : "buyUnit"
			}, {
				name : "useUnit"
			}, {
				name : "fitTrans",
				convert : numFormat.createDelegate(this, ["0.0000"], 3)
			}, {
				name : "fitPrice",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "fitMinCount"
			}, {
				name : "addTime"
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
							url : "cotfittings.do?method=query"
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
							header : "配件编号",
							dataIndex : "fitNo",
							width : 130
						}, {
							header : "配件名称",
							dataIndex : "fitName",
							width : 130
						}, {
							header : "供应商",
							dataIndex : "facId",
							width : 80,
							renderer : function(value) {
								return facData["" + value];
							}
						}, {
							header : "配件类别",
							dataIndex : "typeLv3Id",
							width : 80,
							renderer : function(value) {
								return fittingsType["" + value];
							}
						}, {
							header : "采购单位",
							dataIndex : "buyUnit",
							width : 65
						}, {
							header : "领用单位",
							dataIndex : "useUnit",
							width : 65
						}, {
							header : "换算率",
							dataIndex : "fitTrans",
							width : 80,
							align : "right"
						}, {
							header : "采购价格",
							dataIndex : "fitPrice",
							width : 80,
							align : "right"
						}, {
							header : "最小采购量",
							dataIndex : "fitMinCount",
							width : 85,
							align : "right"
						}, {
							header : "创建日期",
							dataIndex : "addTime",
							width : 85,
							renderer : function(value) {
								// var date = Date.parse(value)
								return Ext.util.Format.date(
										new Date(value.time), "Y-m-d")
								// return formatDate(new
								// Date(value),"yyyy-MM-dd");
							}
						}, {
							header : "操作",
							dataIndex : "id",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var mod = '<a href="javascript:windowopenMod('
										+ value + ')">修改</a>';
								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:del(' + value
										+ ')">删除</a>';
								return mod + nbsp + del;
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
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2",
		cmpId : 'facIdFind',
		emptyText : "供应商",
		editable : true,
		sendMethod : "post",
		valueField : "id",
		displayField : "shortName",
		pageSize : 10,
		width : 100,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'facIdFind'
	});

	// 配件类别
	var typeLvBox = new BindCombox({
				cmpId : 'typeLv3IdFind',
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				emptyText : "配件类别",
				displayField : "typeName",
				valueField : "id",
				triggerAction : "all",
				width : 100,
				pageSize : 10,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				editable : true,
				isSearchField : true,
				searchName : 'typeLv3IdFind'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-',facBox, typeLvBox, {
							xtype : "textfield",
							emptyText : "配件编号",
							width : 95,
							isSearchField : true,
							searchName : 'fitNoFind'
						}, {
							xtype : 'searchcombo',
							width : 95,
							emptyText : "配件名称",
							isSearchField : true,
							searchName : 'fitNameFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "新建",
							handler : windowopenAdd,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}, '-', {
							text : "修改",
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "配件分发",
							handler : sendFitToEle,
							iconCls : "page_fen",
							cls : "SYSOP_FEN"
						},
						// '-', {
						// text : "打印",
						// handler : showPrint,
						// iconCls : "page_print",
						// cls : "SYSOP_PRINT"
						// },
						'-', {
							text : "导入",
							handler : excelInTo1,
							iconCls : "page_excel",
							cls : "SYSOP_EXCEL"
						}, '-', {
							text : "删除",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}

				]
			});

	var grid = new Ext.grid.GridPanel({
				id : "fitGrid",
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

	// excel导入
	function excelInTo1() {
		var cfg = {};
		cfg.bar = _self;
		var reportWin = new ReportWin(cfg);
		reportWin.show();
	}

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});
	viewport.doLayout();
	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});
	var rightMenu = new Ext.menu.Menu({
				id : "rightMenu",
				items : [{
							text : "配件分发",
							handler : sendFitToEle
						}, {
							text : "已分发的货号",
							handler : showPanelFen
						}]
			});
	function rightClickFn(client, rowIndex, e) {
		e.preventDefault();
		rightMenu.showAt(e.getXY());
	}
	grid.on("rowcontextmenu", rightClickFn);

});
// 删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.MessageBox.alert('提示消息', "您没有删除权限");
		return;
	}
	var list = new Array();
	list.push(id);
	var flag = Ext.MessageBox.confirm('提示消息', "是否确定删除选择的配件?", function(btn) {
				if (btn == 'yes') {
					cotFittingsService.deleteFittings(list, function(res) {
								if (res) {
									Ext.MessageBox.alert('提示消息', "删除成功");
									reloadGrid("fitGrid");
								} else {
									Ext.MessageBox.alert('提示消息',
											"删除失败，该配件已经被使用中");
								}
							})
				}
			});
}
function getIds() {
	var list = Ext.getCmp("fitGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert('提示消息', "请选择记录");
		return;
	}
	var flag = Ext.MessageBox.confirm('提示消息', "是否确定删除选择的配件?", function(btn) {
				if (btn == 'yes') {
					cotFittingsService.deleteFittings(list, function(res) {
								Ext.MessageBox.alert('提示消息', "删除成功");
								reloadGrid("fitGrid");
							});
				}
			});
}
function windowopenAdd() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.MessageBox.alert('提示消息', "您没有添加权限");
		return;
	}
	openWindowBase(555, 800, 'cotfittings.do?method=addFittings');
}
// function windowopenMod(obj){
// if(obj == null){
// var ids = getIds();
// if(ids.length == 0)
// {
// alert("请选择一条记录");
// return;
// }
// else if(ids.length > 1)
// {
// alert("只能选择一条记录!")
// return;
// }
// else
// obj = ids[0];
//					
// }
// openWindowBase(555,800,'cotfittings.do?method=addFittings&id='+obj);
// }
// 打开订单编辑页面
function windowopenMod(obj) {
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("提示信息", '您没有修改权限！');
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert('提示消息', "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert('提示消息', "只能选择一条记录!")
			return;
		} else
			obj = ids[0];

	}
	openWindowBase(555, 800, 'cotfittings.do?method=addFittings&id=' + obj);
}

// 显示打印面板
// var printPanel;
// var showPrint = function(item) {
// // 添加权限判断
// var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
// if (isPopedom == 0) {
// Ext.MessageBox.alert("提示信息", '您没有打印权限！');
// return;
// }
// // 获得选择的编号
// var ids = getIds();
// if (ids.length != 1) {
// Ext.MessageBox.alert("提示信息", '请先勾选一条记录!');
// return;
// }
// if (printPanel == null) {
// printPanel = new PrintPanel({
// type : 11,
// exlAction : "/downFit.action",
// querySql : "&fitId=" + ids[0]
// });
// var tag = item.getEl();
// var left = Ext.Element.fly(tag).getX();
// var top = Ext.Element.fly(tag).getY();
//
// Ext.Element.fly(printPanel.printName).setLeftTop(left - 218, top + 20);
//
// printPanel.render($(printPanel.printName));
// var proxy1 = new Ext.dd.DDProxy(printPanel.printName);
// } else {
// if (!printPanel.isVisible()) {
// printPanel.show();
// } else {
// printPanel.hide();
// }
// }
// };

// 配件分发导入
function sendFitToEle() {
	showPanel();
}
var panel;
function showPanel() {
	var list = getIds();
	if (list.length == 0 || list.length > 1) {
		Ext.MessageBox.alert("提示消息", "请先选择一条配件！");
		return;
	}
	var url = "cotfittings.do?method=goFitEle&fitId=" + list[0];
	if (!panel) {
		panel = new Ext.Window({
			title : "配件分发",
			width : 800,
			height : 520,
			modal : true,
			draggable : false,
			closeAction : "hide",
			html : '<iframe name="eleFitFrame" style="margin: 0" frameBorder="0" src="'
					+ url
					+ '"'
					+ 'height=100% width=100% scrolling=no marginheight=0 marginwidth=0></iframe>',
			constrainHeader : true,
			y : 0
		});
	} else {
		var eleFitFrame = window.frames["eleFitFrame"];
		eleFitFrame.location.href = url;
	}
	panel.show();
}

// 配件分发成功后调用
function closeFen() {
	panel.hide();
	Ext.Msg.alert("提示消息", "分发配件成功！");
}

var panelFen;
function showPanelFen() {
	var list = getIds();
	if (list.length == 0 || list.length > 1) {
		Ext.MessageBox.alert("提示消息", "请选择记录！");
		return;
	}
	var cfg = {};
	cfg.fitId = list[0];
	var fenWin = new FenWin(cfg);
	fenWin.show();

}
