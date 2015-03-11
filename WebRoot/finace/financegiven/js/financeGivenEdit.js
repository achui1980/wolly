Ext.onReady(function() {
	var _self = this;
	var empData;
	DWREngine.setAsync(false);
	// 加载员工表缓存
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empData = res;
			});
	DWREngine.setAsync(true);

	// -----------------远程下拉框-----------------------------------------
	// 付款人
	var empsBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'givenPerson',
		fieldLabel : "付款人",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		pageSize : 10,
		anchor : "100%",
		selectOnFocus : true,
		emptyText : '请选择',
		tabIndex : 4,
		sendMethod : "post",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 厂家
	var facBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
		cmpId : 'factoryId',
		fieldLabel : "<font color=red>供应商</font>",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : '请选择',
		allowBlank : false,
		blankText : "供应商不能为空！",
		pageSize : 10,
		anchor : "100%",
		tabIndex : 2,
		sendMethod : "post",
		selectOnFocus : true,
		disabledClass : 'combo-disabled',
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		listeners : {
			"select" : function(com, rec, index) {
				if (rec.data.id != '') {
					// 判断是否有选择币种
					if ($('currencyId').value == '') {
						currencyBox.focus();
						Ext.MessageBox.alert('提示消息', '请先选择币种!');
						com.setValue('');
					} else {
						recvDs.load({
									params : {
										start : 0,
										limit : 10000,
										factoryId : rec.data.id,
										currencyId : $('currencyId').value
									}
								});
					}

				}
			}
		}
	});

	// 币种
	var currencyBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				valueField : "id",
				fieldLabel : "<font color=red>币种</font>",
				tabIndex : 6,
				sendMethod : "post",
				disabledClass : 'combo-disabled',
				displayField : "curNameEn",
				cmpId : "currencyId",
				emptyText : '请选择',
				allowBlank : false,
				blankText : "请选择币种！",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						if (rec.data.id != '') {
							if ($('factoryId').value != '') {
								recvDs.load({
											params : {
												start : 0,
												limit : 10000,
												factoryId : $('factoryId').value,
												currencyId : rec.data.id
											}
										});
							}

						}
					}
				}
			});

	// 银行
	var bankBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBank",
				valueField : "id",
				fieldLabel : "银行",
				tabIndex : 9,
				sendMethod : "post",
				displayField : "bankShortName",
				cmpId : "bankId",
				emptyText : '请选择',
				anchor : "100%"
			});

	// 付款方式
	var payTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotPayType",
				valueField : "id",
				fieldLabel : "付款方式",
				tabIndex : 5,
				sendMethod : "post",
				displayField : "payName",
				cmpId : "payTypeid",
				emptyText : '请选择',
				anchor : "100%"
			});
	// -----------------远程下拉框----over-------------------------------------
	// 判断单号是否重复参数
	var isExist = true;
	var form = new Ext.form.FormPanel({
		title : "付款单基本信息-(红色为必填项)",
		labelWidth : 60,
		labelAlign : "right",
		formId : "cotFinacerecvForm",
		region : 'north',
		layout : "form",
		padding : "5px",
		width : "100%",
		autoHeight : true,
		frame : true,
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.2,
						items : [{
									xtype : "datefield",
									fieldLabel : "<font color='red'>付款日期</font>",
									id : "finaceRecvDate",
									name : "finaceRecvDate",
									value : new Date(),
									format : "Y-m-d",
									tabIndex : 1,
									anchor : "100%"
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.2,
						items : [facBox]
					}, {
						xtype : "panel",
						title : "",
						layout : "column",
						columnWidth : 0.2,
						items : [{
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.9,
							labelWidth : 80,
							items : [{
								xtype : "textfield",
								fieldLabel : "<font color=red>付款单号</font>",
								anchor : "100%",
								id : "finaceNo",
								tabIndex : 2,
								blankText : "请输入付款单号！",
								name : "finaceNo",
								maxLength : 100,
								allowBlank : false,
								invalidText : "付款单号已存在,请重新输入！",
								validationEvent : 'change',
								validator : function(thisText) {
									if (thisText != '') {
										cotFinanceGivenService
												.findIsExistFinaceNo(thisText,
														$('pId').value,
														function(res) {
															if (res) {
																isExist = false;
															} else {
																isExist = true;
															}
														});
									}
									return isExist;
								},
								listeners : {
									"render" : function(obj) {
										var tip = new Ext.ToolTip({
													target : obj.getEl(),
													anchor : 'top',
													maxWidth : 160,
													minWidth : 160,
													html : '点击右边按钮可自动生成付款单号!'
												});
									}
								}
							}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.1,
							items : [{
										xtype : "button",
										width : 20,
										text : "",
										cls : "SYSOP_ADD",
										iconCls : "page_calculator",
										handler : setFinaceNo,
										listeners : {
											"render" : function(obj) {
												var tip = new Ext.ToolTip({
															target : obj
																	.getEl(),
															anchor : 'top',
															maxWidth : 160,
															minWidth : 160,
															html : '根据单号配置自动生成付款单号!'
														});
											}
										}
									}]
						}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.2,
						items : [empsBox]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.2,
						items : [payTypeBox]
					}, {
						xtype : "panel",
						title : "",
						layout : "column",
						columnWidth : 1,
						items : [{
									xtype : "panel",
									title : "",
									layout : "form",
									columnWidth : 0.2,
									items : [currencyBox]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.2,
									layout : "form",
									items : [{
										xtype : "numberfield",
										decimalPrecision : 3,
										maxValue : 900000000,
										disabledClass : 'combo-disabled',
										fieldLabel : "<font color='red'>付款金额</font>",
										id : "amount",
										name : "amount",
										tabIndex : 7,
										allowBlank : false,
										blankText : "请输入付款金额！",
										anchor : "100%",
										listeners : {
											'change' : function(txt, newVal,
													oldVal) {
												if ($('remainAmount').value == '') {
													Ext.getCmp('remainAmount')
															.setValue(newVal);
												} else {
													var old = Number($('remainAmount').value);
													var temp = old
															+ (newVal - oldVal);
													if (temp < 0) {
														txt.setValue(oldVal);
														Ext.Msg
																.alert('提示消息',
																		"输入的金额不能小于已冲帐的总金额!");
													} else {
														Ext
																.getCmp('remainAmount')
																.setValue(temp);
													}
												}
											}
										}
									}]
								}, {
									xtype : "panel",
									title : "",
									layout : "form",
									columnWidth : 0.2,
									labelWidth : 80,
									items : [{
												xtype : "numberfield",
												fieldLabel : "未冲帐金额",
												id : "remainAmount",
												name : "remainAmount",
												decimalPrecision : 3,
												maxValue : 900000000,
												tabIndex : 8,
												disabled : true,
												disabledClass : 'combo-disabled',
												anchor : "100%"
											}]
								}, {
									xtype : "panel",
									title : "",
									layout : "form",
									columnWidth : 0.2,
									items : [bankBox]
								}, {
									xtype : "panel",
									title : "",
									layout : "form",
									columnWidth : 0.2,
									items : [{
												xtype : "numberfield",
												fieldLabel : "银行费用",
												id : "bankFee",
												name : "bankFee",
												decimalPrecision : 3,
												maxValue : 900000000,
												tabIndex : 10,
												anchor : "100%"
											}]
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 1,
						items : [{
									xtype : "textarea",
									fieldLabel : "备注",
									id : "finaceRemark",
									name : "finaceRemark",
									tabIndex : 11,
									anchor : "100%"
								}]
					}]
		}]
	})

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var recvRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "finaceNo"
			}, {
				name : "finaceName"
			}, {
				name : "amount"
			}, {
				name : "zhRemainAmount"
			}, {
				name : "currencyId"
			}, {
				name : "realAmount"
			}, {
				name : "amountDate"
			}, {
				name : "source"
			}, {
				name : "orderNo"
			}, {
				name : "businessPerson"
			}, {
				name : "empId"
			}, {
				name : "addDate"
			}]);
	// 创建数据源
	var recvDs = new Ext.data.Store({
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotfinancegiven.do?method=queryDeal"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, recvRecord)
			});
	// 创建复选框列
	var recvSm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var recvCm = new Ext.ux.grid.LockingColumnModel([recvSm, {
				header : "ID",
				dataIndex : "id",
				width : 50,
				hidden : true
			}, {
				header : "帐款单号",
				dataIndex : "finaceNo",
				width : 150
			}, {
				header : "费用名称",
				dataIndex : "finaceName",
				width : 150
			}, {
				header : "帐款金额",
				dataIndex : "amount",
				width : 80
			}, {
				header : "未流转金额",
				dataIndex : "zhRemainAmount",
				width : 80,
				renderer : function(value) {
					return "<span style='color:red;font-weight:bold;'>" + value
							+ "</span>";
				}
			}, {
				header : "已收金额",
				dataIndex : "realAmount",
				width : 80,
				renderer : function(value) {
					return "<span style='color:green;font-weight:bold;'>"
							+ value + "</span>";
				}
			}, {
				header : "账单日期",
				dataIndex : "amountDate",
				width : 80,
				renderer : function(value) {
					if (value != null) {
						return Ext.util.Format.date(new Date(value.time),
								"Y-m-d");
					}
				}
			}, {
				header : "费用来源",
				dataIndex : "source",
				width : 80,
				renderer : function(value) {
					if (value == 'orderfac') {
						return '生产合同';
					}
					if (value == 'orderout') {
						return '出货合同';
					}
					if (value == 'fitorder') {
						return '配件合同';
					}
					if (value == 'packorder') {
						return '包材合同';
					}
				}
			}, {
				header : "来源单号",
				dataIndex : "orderNo",
				width : 150
			}, {
				header : "业务员",
				dataIndex : "businessPerson",
				width : 80,
				renderer : function(value) {
					return empData[value]
				}
			}, {
				header : "制单人",
				dataIndex : "empId",
				width : 80,
				renderer : function(value) {
					return empData[value]
				}
			}, {
				header : "制单日期",
				dataIndex : "addDate",
				width : 80,
				renderer : function(value) {
					if (value != null) {
						return Ext.util.Format.date(new Date(value.time),
								"Y-m-d");
					}
				}
			}]);
	var recvToolBar = new Ext.PagingToolbar({
				pageSize : 10000,
				store : recvDs,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});
	var recvTb = new Ext.Toolbar({
				items : ['->', {
							text : "导入",
							iconCls : "page_add",
							handler : returnIds
						}]
			});

	var recvGrid = new Ext.grid.GridPanel({
				title : "未完成冲帐的应付款",
				flex : 1,
				id : "recvMainGrid",
				stripeRows : true,
				margins : "0 5 0 0",
				cls : 'rightBorder bottomBorder',
				store : recvDs,
				border : false,
				cm : recvCm,
				sm : recvSm,
				loadMask : true,
				tbar : recvTb,
				bbar : recvToolBar,
				view : new Ext.ux.grid.LockingGridView(),
				viewConfig : {
					forceFit : false
				}
			});

	// 表格双击事件
	recvGrid.on("rowdblclick", function(gridpanel, idx) {
				var rec = recvDs.getAt(idx);
				if ($('remainAmount').value == 0) {
					Ext.Msg.alert('提示消息', '未冲帐金额不足!');
				} else {
					insertToGrid(rec);
				}
			});

	// 冲帐明细表格
	var record = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "finaceName"
			}, {
				name : "amount"
			}, {
				name : "currentAmount"
			}, {
				name : "currencyId"
			}, {
				name : "orderNo"
			}, {
				name : "finaceDate"
			}, {
				name : "dealId"
			}, {
				name : "factoryId"
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
						read : "cotfinancegiven.do?method=queryGivenDetail&mainId="+$('pId').value,
						create : "cotfinancegiven.do?method=add",
						update : "cotfinancegiven.do?method=modify",
						destroy : "cotfinancegiven.do?method=remove"
					},
					listeners : {
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							mask();
						},
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								ds.proxy.setApi({
									read : "cotfinancegiven.do?method=queryGivenDetail&mainId="+$('pId').value
								});
								ds.load({
											params : {
												start : 0,
												limit : 10000
											}
										});
								recvDs.reload();
								//刷新出货付款记录
								reflashParent('detailGrid');
							}
							unmask();
						}
					}
				}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, record),
		writer : writer
	});
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.ux.grid.LockingColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				hidden : true
			}, {
				header : "帐款单号",
				dataIndex : "finaceNo",
				width : 130
			}, {
				header : "费用名称",
				dataIndex : "finaceName",
				width : 140
			}, {
				header : "可冲金额",
				dataIndex : "amount",
				width : 80
			}, {
				header : "<font color='red'>本次冲帐金额</font>",
				dataIndex : "currentAmount",
				width : 90,
				editor : new Ext.form.NumberField({
							maxValue : 9999999999,
							decimalPrecision : 3,
							listeners : {
								'focus' : function(txt) {
									txt.selectText();
								},
								"change" : function(txt, newVal, oldVal) {
									var rec = editRec;
									if (newVal > rec.data.amount) {
										txt.setValue(oldVal);
										Ext.MessageBox.alert('提示消息',
												'您输入的冲帐金额大于该单的帐款金额!');
									} else {
										var old = Number($('remainAmount').value);
										Ext.getCmp('remainAmount').setValue(old
												- (newVal - oldVal));
									}
								}
							}
						}),
				renderer : function(value) {
					return "<span style='color:blue;font-weight:bold;'>"
							+ value + "</span>";
				}
			}, {
				header : "来源单号",
				dataIndex : "orderNo",
				width : 130
			}, {
				header : "帐款日期",
				dataIndex : "finaceDate",
				width : 80,
				renderer : function(value) {
					if (value != null) {
						return Ext.util.Format.date(new Date(value.time),
								"Y-m-d");
					}
				}
			}, {
				header : "dealId",
				dataIndex : "dealId",
				hidden : true
			}]);
	var toolBar = new Ext.PagingToolbar({
				pageSize : 10000,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "删除明细",
							iconCls : "page_del",
							handler : onDel
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				title : "冲帐明细",
				flex : 1,
				id : "detailGrid",
				stripeRows : true,
				cls : 'leftBorder bottomBorder',
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				border : false,
				tbar : tb,
				bbar : toolBar,
				view : new Ext.ux.grid.LockingGridView(),
				viewConfig : {
					forceFit : false
				}
			});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	grid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后
	grid.on("afteredit", function(e) {
				if (e.field == 'currentAmount') {
					currentAmountChange(e.value);
				}
			});

	var centerPanel = new Ext.Panel({
				layout : "hbox",
				layoutConfig : {
					align : 'stretch'
				},
				region : "center",
				buttonAlign : 'center',
				// border : false,
				buttons : [{
							text : "保存",
							cls : "SYSOP_ADD",
							id : "saveBtn",
							handler : save,
							iconCls : "page_table_save"
						}, {
							text : "删除",
							id : "delBtn",
							handler : del,
							iconCls : "page_del"
						}, {
							text : "取消",
							id : "cancelBtn",
							iconCls : "page_cancel",
							handler : function() {
								closeandreflashEC(true, 'givenGrid', false);
							}
						}],
				items : [recvGrid, grid]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [centerPanel, form]
			});
	viewport.doLayout();

	// 初始化
	function initform() {
		var id = $('pId').value;
		DWREngine.setAsync(false);
		if (id == 'null' || id == '') {
			
			//如果是出货应付款调用该页面,则选择厂家后并置灰
			if ($('orderFlag').value != '' && $('orderFlag').value != 'null') {
				facBox.bindPageValue("CotFactory", "id", $('orderFlag').value);
				facBox.setDisabled(true);
			}
			
			cotPriceCfgService.getObjList("CotPriceCfg", function(res) {
						currencyBox.bindValue(res[0].currencyId);
						if ($('orderFlag').value != '' && $('orderFlag').value != 'null') {
							recvDs.load({
											params : {
												start : 0,
												limit : 10000,
												factoryId : $('orderFlag').value,
												currencyId : res[0].currencyId
											}
										});
						}
					});
			var currEmp = getCurrentEmp();
			empsBox.bindPageValue("CotEmps", "id", currEmp.id);
		} else {
			// 加载付款记录信息
			DWREngine.setAsync(false);
			cotFinanceGivenService.getFinacegivenById(parseInt(id), function(res) {
						DWRUtil.setValues(res);
						Ext.getCmp("finaceRecvDate")
								.setValue(res.finaceRecvDate);
						empsBox.bindPageValue("CotEmps", "id", res.givenPerson);
						bankBox.bindValue(res.bankId);
						facBox.bindPageValue("CotFactory", "id", res.factoryId);
						currencyBox.bindValue(res.currencyId);
						payTypeBox.bindValue(res.payTypeid);

						// 刷新未完成的应收账款
						recvDs.load({
									params : {
										start : 0,
										limit : 10000,
										factoryId : res.factoryId,
										currencyId : $('currencyId').value
									}
								});
						// 刷新冲帐明细表格
						ds.load({
									params : {
										start : 0,
										limit : 10000
									}
								});
					});
			DWREngine.setAsync(true);
			// 编辑状态不允许在改币种
			currencyBox.setDisabled(true);
			Ext.getCmp('amount').setDisabled(true);

		}
		DWREngine.setAsync(true);
	}
	unmask();
	initform();

	// 删除
	function del() {
		var id = $('pId').value;
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.Msg.alert('提示消息', "您没有删除权限");
			return;
		}
		// "部分付款记录的溢付款有金额转移到出货或者冲帐明细中的预付货款有金额转移到出货,是否删除其他付款记录?";
		// 查询该付款记录的溢付款是否转移到出货,有就不能删除
		cotFinanceGivenService.findIsYiOut(id, $('factoryId').value,
				function(check) {
					if (check == 1) {
						Ext.Msg.alert('提示消息', "该付款记录的溢付款的部分金额转移到出货的其他费用,不能删除!");
					} else if (check == 2) {
						Ext.Msg.alert('提示消息', "冲帐明细中的预付货款有金额转移到出货,不能删除!");
					} else {
						var list = new Array();
						list.push(id);
						Ext.MessageBox.confirm('提示消息', '是否确定删除该付款记录?',
								function(btn) {
									if (btn == 'yes') {
										cotFinanceGivenService.deleteFinacerecvs(
												list, function(res) {
													if (res == 0) {
														Ext.Msg.alert('提示消息',
																"删除成功");
														closeandreflashEC(true,
																'givenGrid',
																false);
													} else {
														Ext.Msg.alert('提示消息',
																"删除失败");
													}
												})
									}
								});
					}
				});
	}

	// 设置主单号
	function setFinaceNo() {
		var factoryId = $('factoryId').value;
		if (factoryId != 'null' && factoryId != '') {

//			cotFinanceGivenService.createFinaceGivenNo(parseInt(factoryId),
//					function(res) {
//						Ext.getCmp('finaceNo').setValue(res);
//					});
			cotSeqService.getFinaceGivenNo(parseInt(factoryId),
					function(res) {
						$('finaceNo').value = res;
					});
		} else {
			Ext.MessageBox.alert("提示消息", '请先选择供应商！', function(btn) {
						if (btn == 'ok') {
							facBox.focus();
						}
					});

		}
	}

	// 本次冲帐金额改变事件
	var tempGivenRemian = 0;
	function currentAmountChange(newVal) {
	}

	// 保存
	function save() {
		var popedom = checkAddMod($('pId').value);
		if (popedom == 1) {
			Ext.Msg.alert("提示框", '对不起,您没有添加权限!请联系管理员!');
			return;
		} else if (popedom == 2) {
			Ext.Msg.alert("提示框", '对不起,您没有修改权限!请联系管理员!');
			return;
		}
		// 验证表单
		var formData = getFormValues(form, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var obj = DWRUtil.getValues('cotFinacerecvForm');
		var cotFinacegiven = new CotFinacegiven();
		for (var p in cotFinacegiven) {
			if (p != 'finaceRecvDate') {
				cotFinacegiven[p] = obj[p];
			}
		}
		if ($('pId').value != 'null' && $('pId').value != '') {
			cotFinacegiven.id = $('pId').value;
		}

		Ext.MessageBox.confirm('提示信息', '您是否确定保存该付款记录？', function(btn) {
			if (btn == 'yes') {
				DWREngine.setAsync(false);
				cotFinanceGivenService.saveOrUpdateRecv(cotFinacegiven,
						$('finaceRecvDate').value, function(res) {
							if (res != null) {
								$('pId').value = res;
								reflashParent('givenGrid');

								// 保存冲帐明细
								// 更改添加action参数
								var urlAdd = '&mainId=' + res + '&curId='
										+ $('currencyId').value;
								ds.proxy.setApi({
									read : "cotfinancegiven.do?method=queryGivenDetail&mainId="+res,
									create : "cotfinancegiven.do?method=add"
											+ urlAdd,
									update : "cotfinancegiven.do?method=modify",
									destroy : "cotfinancegiven.do?method=remove"
								});
								ds.save();

								// 如果主单有剩余金额就生成或修改溢付款,如果没有就删除溢付款
								cotFinanceGivenService.setYiMonry($('pId').value,
										function(com) {
											if (com != null) {
												$('remainAmount').value = com;
											}
										});
								// 编辑状态不允许在改币种
								currencyBox.setDisabled(true);
								Ext.Msg.alert("提示消息", "保存成功！");
							} else {
								Ext.Msg.alert("提示消息", '插入失败');
							}
						});
				DWREngine.setAsync(true);
			}
		});
	}

	// 表格添加事件,调用父页面的方法
	function returnIds() {
		if ($('remainAmount').value == 0) {
			Ext.Msg.alert('提示消息', '未冲帐金额不足!');
		} else {
			var recs = recvSm.getSelections();
			if (recs.length == 0) {
				Ext.Msg.alert('提示消息', "请先选择应付款记录");
				return;
			}
			Ext.each(recs, function(item) {
						insertToGrid(item);
					});
		}
	}

	// 根据产品货号查找样品数据
	function insertToGrid(obj) {
		// 判断该条应付款是否已冲帐
		var flag = false;
		ds.each(function(item) {
					if (item.data.dealId == obj.id) {
						flag = true;
						return false;
					}
				});
		if (flag) {
			return;
		}

		// 新建冲帐明细对象
		var detail = {};
		detail.finaceNo = obj.data.finaceNo;// 帐款单号
		detail.orderNo = obj.data.orderNo;// 订单单号
		detail.currencyId = $('currencyId').value;// 币种
		detail.finaceDate = obj.data.amountDate;// 帐款日期
		detail.finaceName = obj.data.finaceName;// 费用名称
		detail.factoryId = $('factoryId').value;// 厂家
		detail.dealId = obj.data.id;// 应付款id

		// 如果是预付货款.可冲金额=帐款金额-已收金额,否则为未流转金额
		var am = obj.data.zhRemainAmount;
		if (obj.data.finaceName == '预付货款') {
			am = Number(obj.data.amount).sub(obj.data.realAmount);
		}

		detail.amount = am;// 可冲金额
		var remainAmount = $('remainAmount').value;
		if (remainAmount > am) {
			detail.currentAmount = am;// 本次冲帐金额
			$('remainAmount').value = (Number(remainAmount).sub(am)).toFixed("2");
		} else {
			detail.currentAmount = remainAmount;// 本次冲帐金额
			$('remainAmount').value = 0;
		}

		var u = new ds.recordType(detail);
		ds.add(u);
	}

	// 删除
	function onDel() {
		Ext.MessageBox.confirm('提示消息', '是否确定删除选择的冲帐明细?', function(btn) {
			if (btn == 'yes') {
				DWREngine.setAsync(false);
				var check=false;
				var recs = sm.getSelections();
				Ext.each(recs, function(item) {
					var currentAmount = item.data.currentAmount;
					if (!isNaN(item.id)) {
						check=true;
					}
					ds.remove(item);
					// 主单的未冲帐金额
					$('remainAmount').value = (Number($('remainAmount').value) + Number(currentAmount))
							.toFixed("2");
				});
				if(check){
					cotFinanceGivenService.saveFinacerecvByMoney($('remainAmount').value,$('pId').value,
										function(com) {
											if(!com){
												Ext.MessageBox.alert('提示消息','保存失败!');
											}else{
												reflashParent('givenGrid');
												// 保存冲帐明细
												// 更改添加action参数
												var urlAdd = '&mainId=' + $('pId').value + '&curId='
														+ $('currencyId').value;
												ds.proxy.setApi({
													read : "cotfinancegiven.do?method=queryGivenDetail&mainId="+$('pId').value,
													create : "cotfinancegiven.do?method=add"
															+ urlAdd,
													update : "cotfinancegiven.do?method=modify",
													destroy : "cotfinancegiven.do?method=remove"
												});
												ds.save();
											}
										});
				}
				DWREngine.setAsync(true);
			}
		});

	}
});