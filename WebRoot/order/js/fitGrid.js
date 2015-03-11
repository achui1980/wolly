// 订单的配件编辑
FitGrid = function(cfg) {
	// 配件单价保留几位小数
	var fitPriceNum = getDeNum("fitPrecision");
	// 根据小数位生成"0.000"字符串
	var fitNumTemp = getDeStr(fitPriceNum);
	var _self = this;
	if (!cfg)
		cfg = {};
	var reflashFn = cfg.reflashFn;
	// 加载币种表缓存
	DWREngine.setAsync(false);
	var curFac;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				curFac = res;
			});
	DWREngine.setAsync(true);
	// 表格-厂家
	var facGridBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2",
		sendMethod : "post",
		cmpId : 'facId',
		hideLabel : true,
		labelSeparator : " ",
		editable : true,
		valueField : "id",
		autoLoad : true,
		displayField : "shortName",
		emptyText : '请选择',
		pageSize : 10,
		listWidth : 350,
		anchor : "100%"
	});
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "fitDesc"
			}, {
				name : "fitUsedCount"
			}, {
				name : "fitCount"
			}, {
				name : "fitUseUnit"
			}, {
				name : "facId"
			}, {
				name : "fitPrice",
				convert : numFormat.createDelegate(this, [fitNumTemp], 3)
			}, {
				name : "fitTotalPrice",
				convert : numFormat.createDelegate(this, [fitNumTemp], 3)
			}, {
				name : "fitRemark"
			}, {
				name : "fittingId"
			}, {
				name : "eleName"
			}, {
				name : "eleId"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ds = new Ext.data.Store({
				autoSave : false,
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorder.do?method=queryOrderFittings"
									+ "&rdm=" + cfg.rdm,
							api : {
								create : "cotorder.do?method=addFitting&pFId="
										+ cfg.isHId + "&oId=" + cfg.pId,
								destroy : "cotorder.do?method=removeFitting",
								update : "cotorder.do?method=modifyFitting&pFId="
										+ cfg.isHId + "&oId=" + cfg.pId
							},
							listeners : {
								exception : function(proxy, type, action,
										options, response, arg) {
									ds.reload();
									reflashFn();
									Ext.MessageBox.alert('提示消息', '保存成功!');
									// Ext.getCmp('winpanel').close();
								}
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord),
				writer : writer
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "配件号",
							dataIndex : "fitNo",
							width : 100,
							summaryRenderer : function(v, params, data) {
								params.css = 'fg';
								return "合计：";
							}
						}, {
							header : "配件名称", // 表头
							dataIndex : "fitName",
							width : 130
						}, {
							header : "<font color=blue>规格型号</font>",
							dataIndex : "fitDesc",
							width : 130,
							editor : new Ext.form.TextArea({
										maxLength : 100,
										height : "100%",
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color=blue>用量</font>",
							dataIndex : "fitUsedCount",
							width : 40,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changeMoneyByUse(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>数量</font>",
							dataIndex : "fitCount",
							width : 40,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changeMoneyByNum(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>单位</font>",
							dataIndex : "fitUseUnit",
							width : 80
						}, {
							header : "<font color=blue>供应商</font>",
							dataIndex : "facId",
							width : 70,
							editor : facGridBox,
							renderer : function(value) {
								return curFac["" + value];
							}
						}, {
							header : "<font color=blue>单价</font>",
							dataIndex : "fitPrice",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 999999.999999,
										decimalPrecision : fitPriceNum,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												fitPriceChange(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>金额</font>",
							dataIndex : "fitTotalPrice",
							summaryType : 'sum',
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 999999.999999,
										decimalPrecision : fitPriceNum,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color=blue>备注</font>",
							dataIndex : "fitRemark",
							width : 100,
							editor : new Ext.form.TextArea({
										maxLength : 500,
										height : "100%",
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "fittingId",
							dataIndex : "fittingId",
							hidden : true
						}]
			});

	// 供应商
	var facBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotFactory&typeName=factroyTypeidLv1&type=2",
		sendMethod : "post",
		cmpId : "facId",
		valueField : "id",
		displayField : "shortName",
		width : 95,
		emptyText : "供应商",
		isSearchField : true,
		searchName : 'facId'
	});

	// 顶部工具栏
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', facBox, {
							xtype : 'textfield',
							emptyText : "配件名称",
							width : 95,
							isSearchField : true,
							searchName : 'fitName'
						}, {
							xtype : 'textfield',
							emptyText : "规格型号",
							width : 95,
							isSearchField : true,
							searchName : 'fitDesc'
						}, {
							xtype : 'searchcombo',
							width : 95,
							emptyText : "配件号",
							isSearchField : true,
							searchName : 'fitNo',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "订单→样品",
							iconCls : "page_upload",
							tooltip : '拷贝订单配件信息到样品档案',
							handler : tongFittingToEle
						}, {
							text : "样品→订单",
							iconCls : "page_import",
							tooltip : '拷贝样品配件信息到订单',
							handler : tongEleFitting
						}, {
							text : "从配件库导入",
							iconCls : "page_add",
							handler : showFittingPanel
						}, '-', {
							text : "删除",
							iconCls : "page_del",
							handler : deleteRow
						}, '-', {
							text : "保存",
							iconCls : 'page_table_save',
							handler : save
						}],
				listeners : {
					'render' : function(tbar) {
						// 如果是新增行时,隐藏顶部工具栏
						if (cfg.isHId == null) {
							tbar.hide();
						}
					}
				}
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.EditorGridPanel({
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				plugins : [summary],
				loadMask : true,
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	var rowCurrent = -1;
	// 单元格点击后,记住当前行
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				rowCurrent = rowIndex;
				// 获得view
				var view = grid.getView();
				// 获得单元格
				var cell = view.getCell(rowIndex, columnIndex);
				// 获得该行高度
				var row = view.getRow(rowIndex);
				var editor = cm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(cell.offsetWidth, row.scrollHeight);
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

	// 添加
	function insert() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		var ary = sm.getSelections();
		cfg.bar.insertSelect(ary);
	}
	// 配件添加/修改/删除/确定按钮事件
	function save() {
		if (cfg.orderStatus == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再保存配件!');
			return;
		}
		var isPopedom = getPopedomByOpType("cotorder.do", "MOD");
		if (isPopedom == 0) {// 没有删除权限
			Ext.MessageBox.alert('提示消息', "您没有订单的修改权限");
			return;
		}
		ds.save();
		// 延迟0.5秒加载
		// reloadDelay.defer(500);
	}
	this.load = function() {
		// 加载表格
		ds.load({
					params : {
						start : 0
					}
				});
	}
	function addGrid(res) {
		var price;
		if ((res.fitPrice != null && res.fitPrice != '')
				&& (res.fitTrans != null && res.fitTrans != '')) {
			if (res.fitTrans != 0) {
				price = (res.fitPrice / res.fitTrans).toFixed(fitPriceNum);
			} else {
				price = 0.0;
			}
		}
		// ds.remove(sm.getSelected());
		// var index = ds.findExact("fitNo", res.fitNo);
		// if (index != -1)
		// return;
		var u = new ds.recordType({
					fitNo : res.fitNo,
					fitName : res.fitName,
					fitDesc : res.fitDesc,
					facId : res.facId,
					fitUsedCount : 1,
					fitCount : 1,
					fitUseUnit : res.useUnit,
					fitPrice : price,
					fitTotalPrice : price,
					fitRemark : res.fitRemark,
					eleName : cfg.eleName,
					eleId : cfg.eleId,
					fittingId : res.id
				});
		ds.add(u);
		// grid.startEditing(ds.getCount()-1,2);
	}
	function deleteRow() {
		// Ext.MessageBox.confirm('提示信息', "是否确定删除选择的配件?", function(btn) {
		// if (btn == 'yes') {
		// var arr = sm.getSelections();
		// Ext.each(arr, function(row) {
		// ds.remove(row);
		// })
		// ds.save();
		// reloadDelay.defer(500);
		// }
		// });
		if (cfg.orderStatus == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}
		var arr = sm.getSelections();
		Ext.each(arr, function(row) {
					ds.remove(row);
				});
	}
	function reloadDelay() {
		ds.reload();
	}

	// 显示配件库
	function showFittingPanel() {
		if (cfg.orderStatus == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}
		var temp = {};
		temp.bar = _self;
		var fittingGrid = new FittingGrid(temp);
		fittingGrid.show();
	}
	// 导入配件表格
	this.onExp = function(arr) {
		var num = 0;
		Ext.each(arr, function(item) {
					// 判断该配件是否已存在新增记录中
					var flag = false;
					var temp = ds.getModifiedRecords();
					Ext.each(temp, function(rec) {
								if (rec.data.fitNo == item.data.fitNo) {
									flag = true;
									return false;
								}
							});
					// 判断该配件是否已存在已有记录中
					if (!flag) {
						DWREngine.setAsync(false);
						queryService.getFittingByOrder(cfg.isHId, item.id,
								function(res) {
									if (res != null) {
										addGrid(res);
										num++;
									}
								});
						DWREngine.setAsync(true);
					}
				});
		if (num == 0) {
			Ext.MessageBox.alert('提示消息', '您要导入的配件已存在!');
			return;
		}
		if (num == arr.length) {
			Ext.MessageBox.alert('提示消息', '导入成功!');
			return;
		}
		if (num != arr.length) {
			Ext.MessageBox.alert('提示消息', '导入成功!部分配件已存在!');
			return;
		}
	}

	// 用量改变时,改变总金额
	function changeMoneyByUse(newVal) {
		var rec = ds.getAt(rowCurrent);
		// 数量
		var fitCount = rec.data.fitCount;
		if (fitCount == '' || isNaN(fitCount)) {
			fitCount = 0;
		}
		// 单价
		var fitPrice = rec.data.fitPrice;
		if (fitPrice == '' || isNaN(fitPrice)) {
			fitPrice = 0;
		}

		// 计算金额
		var temp = (fitCount * newVal * parseFloat(fitPrice))
				.toFixed(fitPriceNum);
		rec.set("fitTotalPrice", temp);
	}

	// 数量改变时,改变总金额
	function changeMoneyByNum(newVal) {
		var rec = ds.getAt(rowCurrent);
		// 用量
		var fitUsedCount = rec.data.fitUsedCount;
		if (fitUsedCount == '' || isNaN(fitUsedCount)) {
			fitUsedCount = 0;
		}
		// 单价
		var fitPrice = rec.data.fitPrice;
		if (fitPrice == '' || isNaN(fitPrice)) {
			fitPrice = 0;
		}

		// 计算金额
		var temp = (fitUsedCount * newVal * parseFloat(fitPrice))
				.toFixed(fitPriceNum);
		rec.set("fitTotalPrice", temp);
	}

	// 单价改变金额
	function fitPriceChange(newVal) {
		var rec = ds.getAt(rowCurrent);
		// 数量
		var fitCount = rec.data.fitCount;
		if (fitCount == '' || isNaN(fitCount)) {
			fitCount = 0;
		}
		// 用量
		var fitUsedCount = rec.data.fitUsedCount;
		if (fitUsedCount == '' || isNaN(fitUsedCount)) {
			fitUsedCount = 0;
		}
		// 计算金额
		var temp = (fitCount * fitUsedCount * parseFloat(newVal))
				.toFixed(fitPriceNum);
		rec.set("fitTotalPrice", temp);
	}

	// 同步订单配件到样品
	function tongFittingToEle() {
		Ext.MessageBox.confirm('提示消息', '是否将所有配件信息同步到样品档案?', function(btn) {
					if (btn == 'yes') {
						cotOrderService.deleteAndTongFitting(cfg.isHId,
								cfg.eleId, function(res) {
									if (res == true) {
										ds.reload();
										cotOrderService.modifyPriceFacByEleId(
												cfg.eleId, function() {
													Ext.MessageBox.alert(
															'提示消息', '同步成功!');
												});
									} else {
										Ext.MessageBox.alert('提示消息',
												'同步失败!请确认样品档案是否存在该货号!');
									}
								});
					}
				});
	}

	// 同步样品配件
	function tongEleFitting() {
		if (cfg.orderStatus == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}
		Ext.MessageBox.confirm('提示消息', '是否确定清空当前订单配件信息,再导入该货号的样品配件信息?',
				function(btn) {
					if (btn == 'yes') {
						cotOrderService.deleteAndTongEleFitting(cfg.isHId,
								cfg.eleId, function(res) {
									if (res == true) {
										ds.reload();
										reflashFn();
										Ext.MessageBox.alert('提示消息', '同步成功!');
									} else {
										Ext.MessageBox.alert('提示消息',
												'同步失败!请确认样品档案是否存在该货号!');
									}
								});
					}
				});
	}
	/** *************************************************************************** */
	// 表单
	var con = {
		layout : 'fit',
		width : 600,
		height : 490,
		border : false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	FitGrid.superclass.constructor.call(this, con);
};
Ext.extend(FitGrid, Ext.Panel, {});
