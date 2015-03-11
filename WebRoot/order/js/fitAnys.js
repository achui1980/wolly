Ext.onReady(function() {
	var empData;
	var factoryData;
	var comData;
	DWREngine.setAsync(false);
	// 加载业务员表缓存
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empData = res;
			});
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				factoryData = res;
			});
	// 加载采购公司表缓存
	baseDataUtil.getBaseDicDataMap("CotCompany", "id", "value", function(res) {
				comData = res;
			});
	// 配件单价保留几位小数
	var fitPriceNum = getDeNum("fitPrecision");
	// 根据小数位生成"0.000"字符串
	var fitNumTemp = getDeStr(fitPriceNum);
	DWREngine.setAsync(true);
	

	/** ******EXT创建grid步骤******** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "anyFlag"
			}, {
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "fitDesc"
			}, {
				name : "boxCount"
			}, {
				name : "fitUsedCount"
			}, {
				name : "fitCount"
			}, {
				name : "fitBuyUnit"
			}, {
				name : "orderFitCount"
			}, {
				name : "facId"
			}, {
				name : "fitPrice",
				type : 'float',
				convert : numFormat.createDelegate(this, [fitNumTemp], 3)
			}, {
				name : "totalAmount",
				type : 'float',
				convert : numFormat.createDelegate(this, [fitNumTemp], 3)
			}, {
				name : "remark"
			}, {
				name : "orderId"
			}, {
				name : "orderdetailId"
			}, {
				name : "fittingId"
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ds = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotfitanys.do?method=query&orderId="
								+ $('orderId').value,
						create : "cotfitanys.do?method=add",
						update : "cotfitanys.do?method=modify",
						destroy : "cotfitanys.do?method=remove"
					},
					listeners : {
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								ds.reload();
								Ext.Msg.alert("提示消息", "保存成功！");
							}
							unmask();
						},
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							mask();
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

	// 表格-厂家
	var facGridBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2",
		sendMethod : "post",
		cmpId : "facId",
		editable : true,
		sendMethod : "post",
		pageSize : 10,
		autoLoad : true,
		minChars : 1,
		valueField : "id",
		displayField : "shortName",
		emptyText : "请选择",
		hideLabel : true,
		labelSeparator : " ",
		listWidth : 250,
		anchor : "100%"
	});

	// 供应商2
	var facIdBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2",
		cmpId : 'facIdFind',
		sendMethod : "post",
		fieldLabel : "供应商",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "95%",
		tabIndex : 35,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 初始化
	function init() {
		// 如果没有配件合同管理的修改权限,隐藏确定按钮和生成按钮
		var ck = getPopedomByOpType("cotfittingorder.do", "MOD");
		if (ck == 0) {
			tb.hide();
		}
	}
	init();

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel({
				moveEditorOnEnter : false
			});
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 50
		},
		columns : [sm, {
					header : "编号",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "货号",
					width : 100,
					dataIndex : "eleId"
				}, {
					header : "中文名称",
					width : 130,
					dataIndex : "eleName"
				}, {
					header : "状态",
					dataIndex : "anyFlag",
					width : 60,
					renderer : function(value) {
						if (value == 'C') {
							return "<font color='green'>已采购</font>";
						} else if (value == 'U') {
							return "<font color='red'>未采购</font>";
						}
					}
				}, {
					header : "<font color=blue>配件号</font>",
					width : 130,
					dataIndex : "fitNo",
					editor : new Ext.form.TextField({
						maxLength : 20,
						listeners : {
							'focus' : function(txt) {
								txt.selectText();
							},
							"specialkey" : function(txt, eObject) {
								var temp = txt.getValue();
								if (temp != ""
										&& eObject.getKey() == Ext.EventObject.ENTER) {
									findFit(txt);
								}
							}
						}
					})
				}, {
					header : "配件名称",
					width : 130,
					dataIndex : "fitName"
				}, {
					header : "<font color=blue>规格型号</font>",
					width : 130,
					dataIndex : "fitDesc",
					editor : new Ext.form.TextArea({
								maxLength : 100,
								height:25,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							})
				}, {
					header : "订单数",
					width : 80,
					dataIndex : "boxCount"
				}, {
					header : "<font color=blue>用量</font>",
					width : 70,
					dataIndex : "fitUsedCount",
					editor : new Ext.form.NumberField({
								maxValue : 9999999.999,
								decimalPrecision : 3,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									},
									"change" : function(txt, newVal, oldVal) {
										changeMoneyByUse(newVal);
									}
								}
							})
				}, {
					header : "<font color=blue>数量</font>",
					width : 70,
					dataIndex : "fitCount",
					editor : new Ext.form.NumberField({
								maxValue : 9999999.999,
								decimalPrecision : 3,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									},
									"change" : function(txt, newVal, oldVal) {
										changeMoneyByNum(newVal);
									}
								}
							})
				}, {
					header : "<font color=blue>单位</font>",
					width : 70,
					dataIndex : "fitBuyUnit",
					editor : new Ext.form.TextField({
								maxLength : 10,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							})
				}, {
					header : "订单需求",
					width : 70,
					dataIndex : "orderFitCount"
				}, {
					header : "<font color=blue>供应商</font>",
					width : 100,
					dataIndex : "facId",
					editor : facGridBox,
					renderer : function(value) {
						return factoryData["" + value];
					}
				}, {
					header : "<font color=blue>单价</font>",
					dataIndex : "fitPrice",
					editor : new Ext.form.NumberField({
								maxValue : 999999.999999,
								decimalPrecision : fitPriceNum,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									},
									"change" : function(txt, newVal, oldVal) {
										fitPriceChange(newVal);
									}
								}
							})
				}, {
					header : "<font color=blue>金额</font>",
					dataIndex : "totalAmount",
					width : 100,
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
					width : 100,
					dataIndex : "remark",
					editor : new Ext.form.TextArea({
								maxLength : 500,
								height : 25,
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
				}, {
					header : "orderdetailId",
					dataIndex : "orderdetailId",
					hidden : true
				}, {
					header : "orderId",
					dataIndex : "orderId",
					hidden : true
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : [{
							text : "新建",
							handler : showOrderWin,
							iconCls : "page_add"
						}, '-', {
							text : "删除",
							handler : onDel,
							iconCls : "page_del"
						}, '-', {
							text : "确定",
							handler : function() {
								ds.save();
							},
							iconCls : "page_add"
						}, '-', {
							text : "重新分析",
							handler : reAnys,
							iconCls : "page_del"
						}, '->', {
							xtype : "checkbox",
							hideLabel : true,
							labelSeparator : ' ',
							boxLabel : "合并采购",
							style:'margin-top: 0px;',
							id : "chkType",
							name : "chkType",
							listeners : {
								"render" : function(obj) {
									var tip = new Ext.ToolTip({
												target : obj.getEl(),
												anchor : 'top',
												maxWidth : 150,
												minWidth : 150,
												html : '勾选时合并相同配件记录(配件号、配件名称、规格型号、供应商一致)。'
											});
								}
							}
						}, '-', {
							text : "全部生成",
							handler : function() {
								addFitOrders(1);
							},
							iconCls : "gird_exp"
						}, '-', {
							text : "生成选中",
							handler : function() {
								addFitOrders(2);
							},
							iconCls : "page_from"
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "fitAnysGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	var rowCurrent = -1;
	// 单元格点击后,记住当前行
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		rowCurrent = rowIndex;
		var rec = ds.getAt(rowIndex);
		var dataIndex = cm.getDataIndex(columnIndex);
		// 双击已存在行时,某些列不让执行
		if (!isNaN(rec.id)) {
			if (dataIndex == 'eleId' || dataIndex == 'eleName'
					|| dataIndex == 'anyFlag' || dataIndex == 'fitName'
					|| dataIndex == 'boxCount' || dataIndex == 'orderFitCount') {
				return false;
			}
		}
		//获得view
		var view = grid.getView();
		//获得单元格
		var cell=view.getCell(rowIndex,columnIndex);
		// 获得该行高度
		var row = view.getRow(rowIndex);
		var editor = cm.getCellEditor(columnIndex, rowIndex);
		editor.setSize(cell.offsetWidth, row.scrollHeight);
	});

	ds.on('beforeload', function() {
				ds.baseParams = DWRUtil.getValues("fitForm");
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	// 供应商
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2",
		cmpId : 'factoryIdFind',
		sendMethod : "post",
		fieldLabel : "供应商",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "95%",
		tabIndex : 35,
		selectOnFocus : true,
		
		
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	var form = new Ext.FormPanel({
		title : '配件采购分析',
		region : 'north',
		height : 70,
		id : "fitFormId",
		formId : "fitForm",
		buttonAlign : 'right', // 按钮居右显示
		labelAlign : "right",
		padding : "5",
		layout : 'column',
		border : false,
		plain : true,
		frame : true,
		listeners : {
			'render' : function(pnl) {
				DWREngine.setAsync(false);
				cotFitOrderService.getOrderNoByOrdeId($('orderId').value,
						function(res) {
							pnl
									.setTitle('订单号:<span id="orderNoSpan" style="color: red">'
											+ res + '</span>&nbsp;配件采购分析');
						});
				DWREngine.setAsync(true);
			}
		},
		items : [{
					layout : 'form',
					columnWidth : .2,
					labelWidth : 60, // 标签宽度
					items : [{
								xtype : "textfield",
								fieldLabel : "货号",
								id : "eleIdFind",
								name : "eleIdFind",
								anchor : "95%"
							}]
				}, {
					layout : 'form',
					columnWidth : .2,
					labelWidth : 60, // 标签宽度
					items : [facBox]
				}, {
					layout : 'table',
					columnWidth : .15,
					layoutConfig : {
						columns : 2
					},
					items : [{
								xtype : 'button',
								text : "查询",
								width : 65,
								iconCls : "page_sel",
								handler : function() {
									ds.reload();
								}
							}, {
								xtype : 'button',
								text : "重置",
								iconCls : "page_reset",
								style : {
									marginLeft : '10px'
								},
								width : 65,
								handler : function() {
									form.getForm().reset()
								}
							}]
				}]
	});

	// 采购查询表单
	var formOrder = new Ext.FormPanel({
				title : '配件采购单',
				region : 'north',
				height : 70,
				id : "orderFormId",
				formId : "orderForm",
				buttonAlign : 'right', // 按钮居右显示
				labelAlign : "right",
				padding : "5",
				layout : 'column',
				border : false,
				plain : true,
				frame : true,
				items : [{
							layout : 'form',
							columnWidth : .2,
							labelWidth : 60, // 标签宽度
							items : [{
										xtype : "textfield",
										fieldLabel : "采购单号",
										id : "orderNo",
										name : "orderNo",
										anchor : "95%"
									}]
						}, {
							layout : 'form',
							columnWidth : .15,
							labelWidth : 60, // 标签宽度
							items : [{
										xtype : "datefield",
										fieldLabel : "采购日期",
										anchor : "100%",
										format : "Y-m-d",
										id : 'startTime',
										name : 'startTime',
										vtype : 'daterange',
										endDateField : 'endTime'
									}]
						}, {
							layout : 'form',
							columnWidth : .12,
							labelWidth : 20, // 标签宽度
							items : [{
										xtype : "datefield",
										fieldLabel : "--",
										anchor : "100%",
										format : "Y-m-d",
										id : 'endTime',
										name : 'endTime',
										vtype : 'daterange',
										labelSeparator : " ",
										startDateField : 'startTime'
									}]
						}, {
							layout : 'form',
							columnWidth : .2,
							labelWidth : 60, // 标签宽度
							items : [facIdBox]
						}, {
							layout : 'table',
							columnWidth : .15,
							layoutConfig : {
								columns : 2
							},
							items : [{
										xtype : 'button',
										text : "查询",
										width : 65,
										iconCls : "page_sel",
										handler : function() {
											dsOrder.reload();
										}
									}, {
										xtype : 'button',
										text : "重置",
										iconCls : "page_reset",
										style : {
											marginLeft : '10px'
										},
										width : 65,
										handler : function() {
											formOrder.getForm().reset();
										}
									}]
						}]
			});

	// 采购表格
	var roleRecordOrder = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fittingOrderNo"
			}, {
				name : "factoryId"
			}, {
				name : "orderDate",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "sendDate",
				sortType:timeSortType.createDelegate(this)
			}, {
				name : "orderStatus"
			}, {
				name : "signAddr"
			}, {
				name : "sendAddr"
			}, {
				name : "totalAmmount"
			}, {
				name : "empId"
			}, {
				name : "orderNo"
			}, {
				name : "companyId"
			}, {
				name : "remark"
			}]);
	// 创建数据源
	var dsOrder = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotfitanys.do?method=queryFitOrder&orderId="
									+ $('orderId').value
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecordOrder)
			});
	// 创建复选框列
	var smOrder = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cmOrder = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 150
				},
				columns : [smOrder, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "采购单号",
							width : 180,
							dataIndex : "fittingOrderNo"
						}, {
							header : "供应商",
							width : 100,
							dataIndex : "factoryId",
							renderer : function(value) {
								return factoryData["" + value];
							}
						}, {
							header : "采购日期",
							width : 100,
							dataIndex : "orderDate",
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "交货日期",
							width : 100,
							dataIndex : "sendDate",
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "审核状态",
							width : 100,
							dataIndex : "orderStatus",
							renderer : function(value) {
								if (value == 0) {
									return "<font color='green'>未审核</font>";
								} else if (value == 1) {
									return "<font color='red'>审核不通过</font>";
								} else if (value == 2) {
									return "<font color='blue'>审核通过</font>";
								} else if (value == 3) {
									return "<font color='#10418C'>请求审核</font>";
								} else if (value == 9) {
									return "<font color='green'>不审核</font>";
								}
							}
						}, {
							header : "签约地点",
							dataIndex : "signAddr"
						}, {
							header : "送货地点",
							dataIndex : "sendAddr"
						}, {
							header : "总金额",
							dataIndex : "totalAmmount"
						}, {
							header : "采购人员",
							dataIndex : "empId",
							renderer : function(value) {
								return empData["" + value];
							}
						}, {
							header : "订单号",
							width : 180,
							dataIndex : "orderNo"
						}, {
							header : "采购公司",
							dataIndex : "companyId",
							renderer : function(value) {
								return comData["" + value];
							}
						}, {
							header : "备注",
							dataIndex : "remark"
						}]
			});
	var toolBarOrder = new Ext.PagingToolbar({
				pageSize : 15,
				store : dsOrder,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tbOrder = new Ext.Toolbar({
				items : ['->', {
							text : "修改",
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod"
						}, '-', {
							text : "删除",
							handler : deleteBatch,
							iconCls : "page_del"
						}, '-', {
							text : "打印",
							handler : showPrint,
							iconCls : "page_print"
						}]
			});
	var gridOrder = new Ext.grid.GridPanel({
				id:'fittingGrid',
				region : "center",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : dsOrder, // 加载数据源
				cm : cmOrder, // 加载列
				sm : smOrder,
				// selModel: new
				// Ext.grid.RowSelectionModel({singleSelect:false}),
				loadMask : true, // 是否显示正在加载
				tbar : tbOrder,
				bbar : toolBarOrder,
				viewConfig : {
					forceFit : true
				}
			});

	dsOrder.on('beforeload', function() {
				dsOrder.baseParams = DWRUtil.getValues("orderForm");
			});

	// 表格双击
	gridOrder.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});
	// 分页基本参数
	dsOrder.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	var anyGrid = new Ext.Panel({
				region : 'north',
				height : 350,
				layout : 'border',
				border : false,
				items : [grid, form]
			});

	var orderGrid = new Ext.Panel({
				region : 'center',
				layout : 'border',
				border : false,
				items : [gridOrder, formOrder]
			});

	// 构造
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [anyGrid, orderGrid]
			});
	viewport.doLayout();
	// 右键菜单
	var rightMenu = new Ext.menu.Menu({
				id : "rightMenu",
				items : [{
							text : "产品采购"
						}, {
							text : "配件采购"
						}, {
							text : "配件采购"
						}, {
							text : "同步唛头至采购单"
						}]
			});


	// 批量删除
	function deleteBatch() {
		var isPopedom = getPdmByOtherUrl("cotfittingorder.do", "DEL");
		if (isPopedom == 0) {
			Ext.Msg.alert("提示信息", "您没有删除权限");
			return;
		}
		var list = smOrder.getSelections();
		if (list.length == 0) {
			Ext.Msg.alert("提示信息", "请选择记录");
			return;
		}
		var ids = new Array();
		Ext.each(list,function(item){
			ids.push(item.id);
		});

		Ext.MessageBox.confirm('提示消息', "确定删除选中的配件采购单?", function(btn) {
					if (btn == 'yes') {
						cotFitOrderService.deleteFitOrderList(ids,
								function(res) {
									if(res==3){
										Ext.Msg.alert("提示信息", "删除出错!");
										return;
									}
									if(res==1){
										ds.reload();
										dsOrder.reload();
										Ext.Msg.alert("提示信息", "删除成功!部分配件采购单含有应付账款,不能删除!");
										return;
									}
									if(res==2){
										ds.reload();
										dsOrder.reload();
										Ext.Msg.alert("提示信息", "删除成功!部分配件采购单有加/减费用导入到出货,不能删除!");
										return;
									}
									if(res==0){
										ds.reload();
										dsOrder.reload();
										Ext.Msg.alert("提示信息", "删除成功");
									}
								});
					}
				});
	}

	// 添加空白record到表格中
	function showOrderWin() {
		var cfg = {};
		cfg.orderId = $('orderId').value;
		var win = new OrderWin(cfg);
		win.show();
	}

	// 查询配件
	function findFit(txt) {
		cotFitOrderService.findFittingByFitNo(txt.getValue(), function(res) {
			if (res == null) {
				Ext.MessageBox.alert('提示消息', '找不到含有<font color=red>\"'
								+ txt.getValue() + '\"</font>配件号的配件资料!');
				return;
			}
			// 只有一条类型的配件时,直接修改
			if (res != null && res.id != null) {
				var rec = ds.getAt(rowCurrent);
				txt.setValue(res.fitNo);
				var flag = false;
				ds.each(function(dt) {
					if (dt.data.fittingId == res.id
							&& rec.data.orderdetailId == dt.data.orderdetailId) {
						flag = true;
						return false;
					}
				});
				if (!flag) {
					rec.set("fitName", res.fitName);
					rec.set("fitDesc", res.fitDesc);
					rec.set("fitUsedCount", 1);
					rec.set("fitCount", 1);
					rec.set("fitBuyUnit", res.useUnit);
					rec.set("orderFitCount", rec.data.boxCount);
					rec.set("facId", res.facId);
					var fitPrice = (res.fitPrice / res.fitTrans).toFixed(fitPriceNum);
					rec.set("fitPrice", fitPrice);
					var totalAmount = (fitPrice * rec.data.boxCount)
							.toFixed(fitPriceNum);
					rec.set("totalAmount", totalAmount);
					rec.set("remark", res.fitRemark);
					rec.set("fittingId", res.id);
				} else {
					txt.setValue("");
					Ext.MessageBox.alert('提示消息', '只找到一条配件<font color=red>\"'
									+ res.fitNo + '\"</font>,但是该货号已添加此配件!');
				}
			} else {
				// 显示配件表格
				var cfg = {};
				cfg.fitNo = txt.getValue();
				cfg.rowCurrent = rowCurrent;
				var fitsWin = new FitsWin(cfg);
				fitsWin.show();
				fitsWin.load();
			}
		});
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType("cotfittingorder.do", "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("提示信息", '您没有打印权限！');
			return;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'fitting'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0] - 200, po[1] + 25);
			printWin.show();
		} else {
			printWin.hide();
		}
	};

	// 删除
	function onDel() {
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					ds.remove(item);
				});
	}

	// 用量改变时,改变总金额
	function changeMoneyByUse(newVal) {
		var rec = ds.getAt(rowCurrent);
		if (rec.data.fittingId == '') {
			return;
		}
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
		// 订单需求
		var orderFitCount = fitCount * newVal * rec.data.boxCount;
		rec.set("orderFitCount", orderFitCount);

		// 计算金额
		var temp = (orderFitCount * parseFloat(fitPrice)).toFixed(fitPriceNum);
		rec.set("totalAmount", temp);
	}

	// 数量改变时,改变总金额
	function changeMoneyByNum(newVal) {
		var rec = ds.getAt(rowCurrent);
		if (rec.data.fittingId == '') {
			return;
		}
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

		// 订单需求
		var orderFitCount = fitUsedCount * newVal * rec.data.boxCount;
		rec.set("orderFitCount", orderFitCount);

		// 计算金额
		var temp = (orderFitCount * parseFloat(fitPrice)).toFixed(fitPriceNum);
		rec.set("totalAmount", temp);
	}

	// 单价改变金额
	function fitPriceChange(newVal) {
		var rec = ds.getAt(rowCurrent);
		if (rec.data.fittingId == '') {
			return;
		}
		// 计算金额
		var temp = (rec.data.orderFitCount * parseFloat(newVal)).toFixed(fitPriceNum);
		rec.set("totalAmount", temp);
	}

	// 重新分析配件数据(可能在做完订单后,再添加或修改新的样品配件)
	function reAnys() {
		Ext.MessageBox.confirm('提示消息', "该功能将把已生成的相关配件采购合同删除，是否继续？", function(btn) {
					if (btn == 'yes') {
						cotFitOrderService.saveFitAnysAgain($('orderId').value,
								function(res) {
									if (res == true) {
										ds.reload();
										dsOrder.reload();
										Ext.MessageBox.alert('提示消息', '重新分析成功!');
									} else {
										Ext.MessageBox.alert('提示消息', '重新分析失败!');
									}
								});
					}
				});
	}

	// 生成配件采购单,type为1是全部生成,2为只生成勾选的
	function addFitOrders(type) {
		var idsStr = "";
		if (type == 2) {
			var rec = sm.getSelections();
			Ext.each(rec, function(item) {
						idsStr += item.id + ",";
					});
			if (idsStr == '') {
				Ext.MessageBox.alert('提示消息', "您还没有勾选配件分析记录!");
				return;
			}
		}
		Ext.MessageBox.confirm('提示消息', "是否生成配件采购单(如果没填供应商不生成采购单)?", function(
						btn) {
					if (btn == 'yes') {
						// 是否合并相同配件记录
						var temp = $('chkType').checked;
						var orderId = $('orderId').value;
						cotFitOrderService.saveFitOrderByAnys(orderId, idsStr,
								temp, function(res) {
									if (res) {
										ds.reload();
										dsOrder.reload();
										Ext.MessageBox
												.alert('提示消息', '生成采购单成功!');
										
										
									} else {
										Ext.MessageBox
												.alert('提示消息', '生成采购单失败!');
									}
								});
					}
				});
	}

	// 修改
	function windowopenMod(obj) {
		// 修改权限判断
		var isPopedom = getPdmByOtherUrl("cotfittingorder.do", "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("提示信息", "您没有配件采购单修改权限!");
			return;
		}
		if (obj == null) {
			var list = smOrder.getSelections();
			if (list.length == 0) {
				Ext.Msg.alert("提示信息", "请选择一条记录!");
				return;
			} else if (list.length > 1) {
				Ext.Msg.alert("提示信息", "只能选择一条记录!")
				return;
			} else
				obj = list[0].id;
		}
		openFullWindow('cotfittingorder.do?method=add&id=' + obj);
	}

	unmask();
});
