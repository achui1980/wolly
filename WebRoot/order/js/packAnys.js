Ext.onReady(function() {
	var empData;
	var factoryData;
	var packData;
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
	// 加载包装材料表缓存
	baseDataUtil.getBaseDicDataMap("CotBoxPacking", "id", "value",
			function(res) {
				packData = res;
			});
	DWREngine.setAsync(true);
	
	// 表格-厂家
	var facGridBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=3",
		sendMethod : "post",
		cmpId : "facId",
		pageSize : 10,
		editable : true,
		autoLoad : true,
		minChars : 1,
		valueField : "id",
		displayField : "shortName",
		hideLabel : true,
		labelSeparator : " ",
		anchor : "100%"
	});

	/** ******EXT创建grid步骤******** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "anyFlag"
			}, {
				name : "eleName"
			}, {
				name : "orderCount"
			}, {
				name : "containerCount"
			}, {
				name : "packCount"
			}, {
				name : "boxPbCount"
			}, {
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
			}, {
				name : "boxRemark"
			}, {
				name : "boxTypeId"
			}, {
				name : "boxPackingId"
			}, {
				name : "sizeL"
			}, {
				name : "sizeW"
			}, {
				name : "sizeH"
			}, {
				name : "packPrice"
			}, {
				name : "factoryId"
			}, {
				name : "totalAmount"
			}, {
				name : "packRemark"
			}, {
				name : "totalAmount"
			}, {
				name : "packRemark"
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
						read : "cotpackanys.do?method=query&orderId="
								+ $('orderId').value,
						// create : "cotpackanys.do?method=add",
						update : "cotpackanys.do?method=modify",
						destroy : "cotpackanys.do?method=remove"
					},
					listeners : {
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("Message", "Operation failed！");
							} else {
								ds.reload();
								Ext.Msg.alert("Message", "saved successfully！");
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
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [sm, {
							header : "Sort No.",
							dataIndex : "id",
							width : 50,
							sortable : true,
							hidden : true
						}, {
							header : "Art No.",
							width : 100,
							dataIndex : "eleId"
						}, {
							header : "Cust. No.",
							width : 100,
							dataIndex : "custNo"
						}, {
							header : "Status",
							dataIndex : "anyFlag",
							width : 60,
							renderer : function(value) {
								if (value == 'C') {
									return "<font color='green'>purchased</font>";
								} else if (value == 'U') {
									return "<font color='red'>not purchased</font>";
								}
							}
						}, {
							header : "Description",
							width : 130,
							dataIndex : "eleName"
						}, {
							header : "Quantity",
							width : 80,
							dataIndex : "orderCount"
						}, {
							header : "Cartons",
							width : 70,
							dataIndex : "containerCount"
						}, {
							header : "Packing",
							width : 70,
							dataIndex : "boxPbCount"
						}, {
							header : "Inner Packing",
							dataIndex : "boxIbCount"
						}, {
							header : "Middle Packing",
							dataIndex : "boxMbCount"
						}, {
							header : "Outer Packing",
							dataIndex : "boxObCount"
						}, {
							header : "Sales Unit",
							width : 100,
							dataIndex : "boxRemark"
						}, {
							header : "Packing Assortment",
							width : 70,
							dataIndex : "boxTypeId",
							renderer : function(value) {
								if (value == 0) {
									return "产品包装";
								} else if (value == 1) {
									return "内包装";
								} else if (value == 2) {
									return "中包装";
								} else if (value == 3) {
									return "外包装";
								} else if (value == 4) {
									return "插格包装";
								}
							}
						}, {
							header : "材料名称",
							width : 80,
							dataIndex : "boxPackingId",
							renderer : function(value) {
								return packData["" + value];
							}
						}, {
							header : "<font color=blue>Quantity</font>",
							width : 60,
							dataIndex : "packCount",
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
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
							header : "<font color=blue>Length(CM)</font>",
							width : 50,
							dataIndex : "sizeL",
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changePriceBySize("sizeL",newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Width(CM)</font>",
							width : 50,
							dataIndex : "sizeW",
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changePriceBySize("sizeW",newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Height(CM)</font>",
							width : 50,
							dataIndex : "sizeH",
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changePriceBySize("sizeH",newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Price</font>",
							width : 70,
							dataIndex : "packPrice",
							editor : new Ext.form.NumberField({
										maxValue : 999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												priceChange(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Supplier</font>",
							width : 100,
							editor : facGridBox,
							dataIndex : "factoryId",
							renderer : function(value) {
								return factoryData["" + value];
							}
						}, {
							header : "Amount",
							dataIndex : "totalAmount",
							width : 100
						}, {
							header : "Remark",
							width : 100,
							dataIndex : "packRemark"
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : [{
							text : "Save",
							handler : showOrderWin,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}, '-', {
							text : "Delete",
							handler : onDel,
							iconCls : "page_del"
						}, '-', {
							text : "Yes",
							handler : function() {
								ds.save();
							},
							iconCls : "page_add"
						},  '-', {
							text : "重新分析",
							handler : reAnys,
							iconCls : "page_del"
						},'->', '-', {
							text : "全部生成",
							handler : function() {
								addPackOrders(1);
							},
							iconCls : "gird_exp"
						}, '-', {
							text : "生成选中",
							handler : function() {
								addPackOrders(2);
							},
							iconCls : "page_from"
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "packAnysGrid",
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

	// 单元格点击后,记住当前行
	var rowCurrent = -1;
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		rowCurrent = rowIndex;
		var rec = ds.getAt(rowIndex);
		var dataIndex=cm.getDataIndex(columnIndex);
		// 双击已存在行时,某些列不让执行
		if (!isNaN(rec.id)) {
			if (dataIndex == 'packCount' || dataIndex == 'sizeL' || dataIndex == 'sizeW'
					|| dataIndex == 'sizeH' || dataIndex == 'packPrice' || dataIndex =='factoryId') {
				//是否已采购
				if (rec.data.anyFlag == 'C') {
					Ext.MessageBox.alert('提示消息','该单已采购,不能再修改!');
					return false;
				}
				//没有材料类型不能修改
				if (rec.data.boxTypeId == '') {
					return false;
				}
				//没有材料名称不能修改
				var boxPackingId = rec.data.boxPackingId;;
				if (boxPackingId == '') {
					return false;
				}
			}
		}
	});

	ds.on('beforeload', function() {
				ds.baseParams = DWRUtil.getValues("packForm");
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	// 厂家
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=3",
		sendMethod : "post",
		cmpId : 'factoryIdFind',
		fieldLabel : "Supplier",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "95%",
		tabIndex : 35,
		selectOnFocus : true,
		
		
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	var form = new Ext.FormPanel({
		title : '包材采购分析',
		region : 'north',
		height : 70,
		id : "packFormId",
		formId : "packForm",
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
				cotPackOrderService.getOrderNoByOrdeId($('orderId').value,
						function(res) {
							pnl
									.setTitle('Invoice No.:<span id="orderNoSpan" style="color: red">'
											+ res + '</span>&nbsp;包材采购分析');
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
								fieldLabel : "Art No.",
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
								text : "Search",
								width : 65,
								iconCls : "page_sel",
								handler : function() {
									ds.reload();
								}
							}, {
								xtype : 'button',
								text : "Reset",
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

	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'empId',
				fieldLabel : "Order No.",
				editable : true,
				valueField : "id",
				sendMethod : "post",
				displayField : "empsName",
				pageSize : 5,
				anchor : "95%",
				selectOnFocus : true,
				emptyText : 'Choose',
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 采购查询表单
	var formOrder = new Ext.FormPanel({
				title : '包材采购单',
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
										id : "packingOrderNo",
										name : "packingOrderNo",
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
							items : [busiBox]
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
				name : "factoryId"
			}, {
				name : "packingOrderNo"
			}, {
				name : "orderDate"
			}, {
				name : "sendDate"
			}, {
				name : "orderStatus"
			}, {
				name : "empId"
			}, {
				name : "totalAmount"
			}]);
	// 创建数据源
	var dsOrder = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotpackanys.do?method=queryPackOrder&orderId="
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
							header : "供应商",
							dataIndex : "factoryId",
							renderer : function(value) {
								return factoryData["" + value];
							}
						}, {
							header : "采购单号",
							dataIndex : "packingOrderNo"
						}, {
							header : "采购日期",
							dataIndex : "orderDate",
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "交货日期",
							dataIndex : "sendDate"
						}, {
							header : "审核状态",
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
							header : "采购人员",
							dataIndex : "empId",
							renderer : function(value) {
								return empData["" + value];
							}
						}, {
							header : "总金额",
							dataIndex : "totalAmount"
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
				id:'packingGrid',
				region : "center",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : dsOrder, // 加载数据源
				cm : cmOrder, // 加载列
				sm : smOrder,
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
	
	// 表格双击
	gridOrder.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	// 批量删除
	function deleteBatch() {
		var isPopedom = getPdmByOtherUrl("cotpackingorder.do", "DEL");
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

		Ext.MessageBox.confirm('提示消息', "确定删除选中的包材采购单?", function(btn) {
					if (btn == 'yes') {
						cotPackOrderService.deletePackOrderList(ids,
								function(res) {
									if(res==3){
										Ext.Msg.alert("提示信息", "删除出错!");
										return;
									}
									if(res==1){
										ds.reload();
										dsOrder.reload();
										Ext.Msg.alert("提示信息", "删除成功!部分包材采购单含有应付账款,不能删除!");
										return;
									}
									if(res==2){
										ds.reload();
										dsOrder.reload();
										Ext.Msg.alert("提示信息", "删除成功!部分包材采购单有加/减费用导入到出货,不能删除!");
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
		var win = new OrderWinForPack(cfg);
		win.show();
	}

	// 删除
	function onDel() {
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					ds.remove(item);
				});
	}
	
	// 重新分析数据
	function reAnys() {
		Ext.MessageBox.confirm('提示消息', "该功能将把已生成的相关包材采购合同删除，是否继续？", function(btn) {
					if (btn == 'yes') {
						cotPackOrderService.savePackAnysAgain($('orderId').value,
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

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType("cotpackingorder.do", "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("提示信息", '您没有打印权限！');
			return;
		}

		if (printWin == null) {
			printWin = new PrintWin({
						type:'packing'
					});
		} 
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0]-200,po[1]+25);
			printWin.show();
		} else {
			printWin.hide();
		}
	};

	// 生成包材采购单,type为1是全部生成,2为只生成勾选的
	function addPackOrders(type) {
		var idsStr = "";
		if (type == 2) {
			var rec = sm.getSelections();
			Ext.each(rec, function(item) {
						idsStr += item.id + ",";
					});
			if (idsStr == '') {
				Ext.MessageBox.alert('提示消息', "您还没有勾选包材分析记录!");
				return;
			}
		}
		Ext.MessageBox.confirm('提示消息', "是否生成包材采购单(如果没填供应商不生成采购单)?", function(
						btn) {
					if (btn == 'yes') {
						var orderId = $('orderId').value;
						cotPackOrderService.savePackOrderByAnys(orderId,
								idsStr, function(res) {
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

	// 打开订单编辑页面
	function windowopenMod(obj) {
		// 修改权限判断
		var isPopedom = getPdmByOtherUrl("cotpackingorder.do", "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("提示信息", "您没有包材采购单修改权限!");
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
		openFullWindow('cotpackingorder.do?method=add&id=' + obj);
	}
	
	// 单价改变金额
	function priceChange(txt,newVal,oldVal) {
		var rec = ds.getAt(rowCurrent);
		// 计算金额
		var temp = (rec.data.packCount * parseFloat(newVal)).toFixed('2');
		rec.set("totalAmount", temp);
	}

	// 数量改变时,改变总金额
	function changeMoneyByNum(newVal) {
		var rec = ds.getAt(rowCurrent);
		// 单价
		var packPrice = rec.data.packPrice;
		if (packPrice == '' || isNaN(packPrice)) {
			packPrice = 0;
		}

		// 计算金额
		var temp = (newVal * parseFloat(packPrice)).toFixed('2');
		rec.set("totalAmount", temp);
	}

	// 长宽高改变时,如果该材料的计算公式含有此参数就改变单价和总金额
	function changePriceBySize(txtName,newVal){
		var rec = ds.getAt(rowCurrent);
		// 返回单价
		cotPackOrderService.getNewPrice(rec.id, txtName, newVal, function(res) {
			// 单价金额
			rec.set("packPrice", res);
			// 计算金额
			var temp = (rec.data.packCount * res).toFixed('2');
			rec.set("totalAmount", temp);
		});

	}

	unmask();
});
