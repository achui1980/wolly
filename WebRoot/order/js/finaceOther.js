Ext.onReady(function() {
	DWREngine.setAsync(false);
	var curMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);

	// 价格币种
	var curGridBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				emptyText : '请选择',
				autoLoad : true,
				hideLabel : true,
				labelSeparator : " ",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});

	// 加、减
	var flagStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['A', '加'], ['M', '减']]
			});
	var flagBox = new Ext.form.ComboBox({
				name : 'flag',
				editable : false,
				store : flagStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				triggerAction : 'all',
				anchor : "100%",
				emptyText : '请选择',
				hiddenName : 'flag',
				selectOnFocus : true
			});
	/** ******************************加减费用项目表格*********************************************** */
	var otherRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceName"
			}, {
				name : "flag"
			}, {
				name : "amount"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}, {
				name : "isImport"
			}, {
				name : "status"
			}, {
				name : "outFlag"
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var otherDs = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
			api : {
				read : "cotorder.do?method=queryOther&source=order&fkId="
						+ parent.$('pId').value,
				create : "cotorder.do?method=addOther",
				update : "cotorder.do?method=modifyOther"
			},
			listeners : {
				// 保存表格前显示提示消息
				beforewrite : function(proxy, action, rs, options, arg) {
					mask();
				},
				exception : function(proxy, type, action, options, res, arg) {
					// 从异常中的响应文本判断是否成功
					if (res.status != 200) {
						Ext.Msg.alert("提示消息", "操作失败！");
					} else {
						otherDs.reload();
						// 修改主单的总数量,总箱数,总体积,总金额
						cotOrderService.modifyCotOrderTotalAction(parent
										.$('pId').value, function(res) {
									if (res != null) {
										parent.$('totalLab').innerText = res[0]
												.toFixed("2");
										parent.$('realLab').innerText = res[2]
												.toFixed("2");
										parent.$('totalCbmLab').innerText = res[1]
												.toFixed("3");
										parent.$('totalGrossLab').innerText = res[3]
												.toFixed("3");
									}
								});
					}
					unmask();
				}
			}
		}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, otherRecord),
		writer : writer
	});
	// 创建复选框列
	var otherSm = new Ext.grid.CheckboxSelectionModel();
	// 储存数量单元格获得焦点事件的文本框坐标
	var ckX;
	var ckY;
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [otherSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 320,
							editor : new Ext.form.TextField({
										maxLength : 200,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "加/减",
							dataIndex : "flag",
							width : 60,
							renderer : function(value) {
								if (value == 'A') {
									return '加';
								}
								if (value == 'M') {
									return '减';
								}
							},
							editor : flagBox

						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 50,
							renderer : function(value) {
								return curMap[value];
							},
							editor : curGridBox
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										allowNegative : false,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
												var temp = txt.getPosition();
												ckX = temp[0] + txt.getWidth();
												ckY = temp[1];
											}
										}
									})
						}, {
							header : "剩余金额",
							dataIndex : "remainAmount",
							width : 80
						}]
			});

	var pageBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : otherDs,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var toolBar = new Ext.Toolbar({
				items : ['->', {
							text : "新增",
							handler : addNewGrid,
							iconCls : "page_add"
						}, '-', {
							text : "删除",
							handler : onDel,
							iconCls : "page_del"
						}, '-', {
							text : "导入",
							handler : showImportPanel,
							iconCls : "page_table_row_insert"
						}, '-', {
							text : "生成应收款",
							handler : addMoreByOtherMoney,
							iconCls : "page_lightning_go"
						}]
			});
	var otherGrid = new Ext.grid.EditorGridPanel({
				title : "加/减费用项目",
				id : "otherGrid",
				flex : 1,
				stripeRows : true,
				margins : "0 5 0 0",
				border : false,
				cls : 'rightBorder',
				store : otherDs, // 加载数据源
				cm : cm, // 加载列
				sm : otherSm,
				loadMask : true, // 是否显示正在加载
				tbar : toolBar,
				bbar : pageBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	otherGrid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后,讲修改后的值储存到内存map中,并即时修改右边面板数据
	otherGrid.on("afteredit", function(e) {
				if (e.field == 'amount') {
					changeOtherAmount(e.value, e.originalValue);
				}
				if (e.field == 'currencyId') {
					// 转换金额和剩余金额
					var curAray;
					DWREngine.setAsync(false);
					baseDataUtil.getBaseDicList('CotCurrency', function(res) {
								curAray = res;
							});
					DWREngine.setAsync(true);

					// 将价格转换成主单价格
					var ra;
					var newRa;
					for (var i = 0; i < curAray.length; i++) {
						if (curAray[i].id == e.originalValue) {
							ra = curAray[i].curRate;
						}
						if (curAray[i].id == e.value) {
							newRa = curAray[i].curRate;
						}
					}
					var am = editRec.data.amount;
					var rm = editRec.data.remainAmount;
					editRec.set('amount', (am * ra / newRa).toFixed("2"));
					editRec.set('remainAmount', (rm * ra / newRa).toFixed("2"));
				}

				if (e.field == 'finaceName') {
					if (e.value == "") {
						e.record.set('finaceName', e.originalValue);
						Ext.MessageBox.alert('提示消息', '费用名称不能为空!');
					} else {
						var flag = true;
						// 判断store中是否含有该费用名称
						otherDs.each(function(item) {
									if (item.data.finaceName == e.value
											&& item.id != e.record.id) {
										flag = false;
										return false;
									}
								});
						if (flag == false) {
							e.record.set('finaceName', e.originalValue);
							Ext.MessageBox.alert('提示消息', '该费用名称已经存在!');
						} else {
							var id = parent.$('pId').value;
							if (id != "" && id != "null") {
								var recId = 0;
								if (!isNaN(e.record.id)) {
									recId = e.record.id;
								}
								// 判断是否重复
								cotOrderService.findIsExistName(e.value, id,
										recId, function(res) {
											if (res == true) {
												e.record.set('finaceName',
														e.originalValue);
												Ext.MessageBox.alert('提示消息',
														'该费用名称已经存在!');
											}
										});
							}
						}
					}
				}
			});

	// 单元格点击后,记住当前行
	otherGrid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var rec = otherDs.getAt(rowIndex);
				var dataIndex = cm.getDataIndex(columnIndex);
				if (rec.data.status == 1) {
					Ext.MessageBox.alert('提示消息', '该费用已经生成应收帐款,不能再修改!');
					return false;
				}
				if (dataIndex == 'flag'
						&& rec.data.amount != rec.data.remainAmount) {
					return false;
				}
				// 获得view
				var view = grid.getView();
				// 获得单元格
				var cell = view.getCell(rowIndex, columnIndex);
				// 获得该行高度
				var row = view.getRow(rowIndex);
				var editor = cm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(cell.offsetWidth, row.scrollHeight);
			});

	/** ************************应收帐款表格***************************************************** */
	var recvRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "finaceName"
			}, {
				name : "amount"
			}, {
				name : "realAmount"
			}, {
				name : "currencyId"
			}, {
				name : "amountDate"
			}, {
				name : "finaceOtherId"
			}, {
				name : "zhRemainAmount"
			}]);

	// 创建数据源
	var recvDs = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorder.do?method=queryRecv&fkId="
										+ parent.$('pId').value
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
	var recvCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [recvSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "帐款单号",
							dataIndex : "finaceNo",
							width : 130
						}, {
							header : "应收项目名称",
							dataIndex : "finaceName",
							width : 180
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 50,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "已收金额",
							dataIndex : "realAmount",
							width : 60
						}, {
							header : "帐款日期",
							dataIndex : "amountDate",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}]
			});
	var recvToolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : recvDs,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var recvTb = new Ext.Toolbar({
		items : ['-', {
					xtype : 'label',
					style : 'color:blue;font-weight: bold;',
					text : "预收货款："
				}, {
					xtype : 'label',
					text : "按货款总金额"
				}, {
					xtype : 'numberfield',
					id : 'priceScal',
					name : 'priceScal',
					decimalPrecision : 2,
					maxValue : 999.99,
					width : 80,
					value : parent.$('priceScalHid').value,
					listeners : {
						'change' : function(txt, newVal, oldVal) {
							var id = parent.$('pId').value;
							if (id == 'null' || id == '') {
								$('priceScal').value = '';
								Ext.MessageBox
										.alert('提示消息', '请先保存订单,才能生成预收货款!');
							} else {
								// 查询订单明细的总额
								// cotOrderService.
								// findTotalMoney(id,function(res){});
								var allMoney = parseFloat(parent.$('totalLab').innerText);
								var orderMoney = allMoney * newVal * 0.01;
								$('prePrice').value = orderMoney.toFixed("2");
							}
						}
					}
				}, {
					xtype : 'label',
					text : "%金额："
				}, {
					xtype : 'numberfield',
					id : 'prePrice',
					name : 'prePrice',
					decimalPrecision : 2,
					maxValue : 9999999.99,
					value : parent.$('prePriceHid').value,
					width : 80
				}, '-', {
					text : "生成",
					handler : addByOrderMoney,
					iconCls : "page_application_put"
				}, '-', '->', {
					text : "删除",
					handler : deleteByAccount,
					iconCls : "page_del"
				}]
	});
	var recvGrid = new Ext.grid.GridPanel({
				title : "应收帐款",
				id : "recvOtherGrid",
				anchor : '100% 50%',
				border:false,
				cls:'leftBorder bottomBorder',
				stripeRows : true,
				store : recvDs, // 加载数据源
				cm : recvCm, // 加载列
				sm : recvSm,
				loadMask : true, // 是否显示正在加载
				tbar : recvTb,
				bbar : recvToolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** *********************************收款记录表格*************************************************** */
	var detailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "addTime"
			}, {
				name : "currencyId"
			}, {
				name : "amount"
			}, {
				name : "currentAmount"
			},{
				name:'finaceRecvid'
			}]);

	// 创建数据源
	var detailds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							url : "cotorder.do?method=queryRecvDetail"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, detailRecord)
			});
	// 加上应收款的选择的记录id
	detailds.on('beforeload', function() {
				var cod = recvSm.getSelections();
				if (cod.length == 1) {
					detailds.baseParams.recvId = cod[0].id;
				} else {
					detailds.baseParams.recvId = 0;
				}

			});
	// 创建复选框列
	var detailsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var detailcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [detailsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "收款单号",
							dataIndex : "finaceNo",
							width : 140
						}, {
							header : "冲帐日期",
							dataIndex : "addTime",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "应收金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "本次收款",
							dataIndex : "currentAmount",
							width : 60
						}]
			});
	var detailtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : detailds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var detailTb = new Ext.Toolbar({
				items : ['->', {
							text : "新增",
							handler : windowRecvAdd,
							iconCls : "page_add"
						}, {
							text : "删除",
							handler : deleteByRecDetail,
							iconCls : "page_del"
						}]
			});
	var detailGrid = new Ext.grid.EditorGridPanel({
				title : "收款记录",
				// region : "center",
				anchor : '100% 50%',
				id : "detailGrid",
				stripeRows : true,
				border : false,
				cls : 'leftBorder',
				store : detailds, // 加载数据源
				cm : detailcm, // 加载列
				sm : detailsm,
				loadMask : true, // 是否显示正在加载
				tbar : detailTb,
				bbar : detailtoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** *****布局********************************************************************************* */
	var panel = new Ext.Panel({
				layout : "anchor",
				flex : 1,
				border : false,
				items : [recvGrid, detailGrid]
			})
	var viewport = new Ext.Viewport({
				layout : "hbox",
				layoutConfig : {
					align : 'stretch'
				},
				items : [otherGrid, panel]
			});
	/** ************************************************************************************** */
	otherDs.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	recvDs.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// 如果是"预收货款"并且有金额流动到出货时,存储一下标识
	var clickRecv = false;
	recvGrid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				if (record.data.finaceName == '预收货款'
						&& record.data.zhRemainAmount != record.data.realAmount) {
					clickRecv = true;
				} else {
					clickRecv = false;
				}
				detailds.load({
							params : {
								start : 0,
								limit : 10,
								recvId : record.id
							}
						});
			});

	// 收款记录双击事件
	detailGrid.on("rowdblclick", function(grid, rowIndex, columnIndex, e) {
				var record = detailds.getAt(rowIndex);
				openFullWindow('cotfinacerecv.do?method=addFinacerecv&id=' + record.data.finaceRecvid);
			});

	// 添加空白record到表格中
	function addNewGrid() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var u = new otherDs.recordType({
					finaceName : "",
					flag : "A",
					amount : "",
					currencyId : parent.$('currencyId').value,
					remainAmount : ""
				});
		otherDs.add(u);
		// 货号获得焦点
		var cell = otherGrid.getView().getCell(otherDs.getCount() - 1, 2);
		if (Ext.isIE) {
			cell.fireEvent("ondblclick");
		} else {
			var e = document.createEvent("Events");
			e.initEvent("dblclick", true, false);
			cell.dispatchEvent(e)
		}
	}

	// 删除其他费用
	function onDel() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限!");
			return;
		}
		var temp = 0;
		var idsAry = new Array();
		var isImpAry = new Array();

		var recs = otherSm.getSelections();
		if (recs.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
			return;
		}
		Ext.each(recs, function(item) {
					if (!isNaN(item.id)) {
						var isImport = item.data.isImport;
						var status = item.data.status;
						var amount = item.data.amount;
						var remainAmount = item.data.remainAmount;
						// 已生成应收帐的不能删除
						if (status != 1) {
							// 如果有金额导入出货,不能删除
							if (parseFloat(remainAmount) == parseFloat(amount)) {
								idsAry.push(item.id);
								isImpAry.push(isImport);
							}
						}
					} else {
						otherDs.remove(item);
						temp++;
					}
				});

		if (idsAry.length == 0 && temp == 0) {
			Ext.MessageBox.alert('提示消息', "已生成应收帐或者有金额导入出货,不能删除!");
			return;
		}

		if (idsAry.length > 0) {
			Ext.MessageBox.confirm('提示消息',
					'您是否确定删除选择的费用?已生成应收帐或者该费用有导入出货,不能删除!', function(btn) {
						if (btn == 'yes') {
							var curRate = parent.$('orderRate').value;
							cotOrderService.deleteByIds(idsAry, isImpAry,
									curRate, function(res) {
										if (res != null) {
											// 修改主单的总数量,总箱数,总体积,总金额
											cotOrderService
													.modifyCotOrderTotalAction(
															parent.$('pId').value,
															function(res) {
																if (res != null) {
																	parent
																			.$('totalLab').innerText = res[0]
																			.toFixed("2");
																	parent
																			.$('realLab').innerText = res[2]
																			.toFixed("2");
																	parent
																			.$('totalCbmLab').innerText = res[1]
																			.toFixed("3");
																	parent
																			.$('totalGrossLab').innerText = res[3]
																			.toFixed("3");
																}
															});
											Ext.MessageBox.alert('提示消息',
													"删除成功!");
										}
										otherDs.reload();
									});
						}
					});
		}
	}

	// 打开添加页面
	function windowRecvAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType('cotfinacerecv.do', "ADD");
		if (isPopedom == 0) {
			Ext.Msg.alert("提示信息", "您没有添加收款记录的权限!");
			return;
		}
		var custId = parent.$('custId').value;
		if (custId != '') {
			openFullWindow('cotfinacerecv.do?method=addFinacerecv&orderFlag='
					+ custId);
		} else {
			Ext.Msg.alert("提示信息", "请先选择客户!");
		}
	}

	// 删除收款记录
	function deleteByRecDetail() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限!");
			return;
		}

		var idsAry = new Array();
		var recs = detailsm.getSelections();
		Ext.each(recs, function(item) {
					idsAry.push(item.id);
				});

		if (idsAry.length == 0) {
			Ext.MessageBox.alert('提示消息', "请先勾选要删除的记录!");
			return;
		}

		// 如果是要删除"预收货款"应收账的收款记录,需要判断是否有金额流到出货的其他费用,因为如果预收了金额就能转移到出货其他费用
		if (clickRecv == true) {
			Ext.MessageBox.alert('提示消息', "该预收货款已有货款流转到出货其他费用!不能删除收款记录!");
		} else {
			Ext.MessageBox.confirm('提示消息', '您是否确定删除选择的收款记录?', function(btn) {
						if (btn == 'yes') {
							cotOrderService.deleteByRecvDetail(idsAry,
									function(res) {
										if (res) {
											Ext.MessageBox.alert('提示消息',
													"删除成功!");
										} else {
											Ext.MessageBox.alert('提示消息',
													"删除失败!");
										}
										recvDs.reload();
										detailds.reload();
									})
						}
					});
		}
	}

	// 保存其他费用(父页面调用)
	this.saveOther = function() {
		var mainId = parent.$('pId').value;
		var orderNo = parent.$('orderNo').value;

		// 更改添加action参数
		var urlAdd = '&mainId=' + mainId + '&mainNo=' + encodeURI(orderNo);

		// 更改修改action参数
		var urlMod = '&orderNo=' + encodeURI(orderNo);

		otherDs.proxy.setApi({
					read : "cotorder.do?method=queryOther&source=order&fkId="
							+ parent.$('pId').value,
					create : "cotorder.do?method=addOther" + urlAdd,
					update : "cotorder.do?method=modifyOther" + urlMod
				});
		otherDs.save();
	}

	// 显示导入界面
	function showImportPanel() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var importOtherWin = new ImportOtherWin();
		importOtherWin.show();
	}

	// 将其他费用添加到应收表格
	function addByOtherMoney(id, name, money, currencyId) {
		var detail = new CotFinaceAccountrecv();
		detail.finaceOtherId = id;
		detail.finaceName = name;
		detail.amount = money;
		var mainId = parent.$('pId').value;
		detail.fkId = mainId;
		var orderNo = parent.$('orderNo').value;
		var custId = parent.$('custId').value;
		var busId = parent.$('bussinessPerson').value;
		// 生成应收帐款单号
		DWREngine.setAsync(false);
		cotOrderService.createRecvNo(parseInt(custId), function(res) {
					detail.finaceNo = res;
					detail.orderNo = orderNo;
					detail.custId = custId;
					detail.businessPerson = busId;
					var companyId = parent.$('companyId').value;
					detail.companyId = companyId;
					detail.currencyId = currencyId;
					// 保存应收帐款
					cotOrderService.saveAccountRecv(detail, parent
									.$('orderTime').value, "", "", function(
									deal) {
							});
				});
		DWREngine.setAsync(true);
	}

	// 生成应收帐款
	function addMoreByOtherMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 判断该订单是否已保存
		var mainId = parent.$('pId').value;
		if (mainId == 'null' || mainId == '') {
			Ext.MessageBox.alert('提示消息', '该订单还没保存,不能生成应收帐款!');
			return;
		}

		// 判断其他费用是否有保存
		if (otherDs.getModifiedRecords().length != 0) {
			Ext.MessageBox.alert('提示消息', '请先保存其他费用,再生成应收帐款!');
			return;
		}

		// 判断是否存在采购费用
		var noHave = false;
		var str = "是否确定生成应收帐款?";
		DWREngine.setAsync(false);
		cotOrderService.checkIsHaveFinace(mainId, function(res) {
					noHave = res;
				});
		DWREngine.setAsync(true);
		if (noHave == false) {
			str = '有部分采购费用未导入(可以点击导入按钮查看),您是否要继续生成应收帐款?';
		}

		Ext.MessageBox.confirm('提示消息', str, function(btn) {
			if (btn == 'yes') {
				// 先刷新该表格,以防货款转移到出货后,该页面没刷新
				otherDs.reload();
				mask();
				var task = new Ext.util.DelayedTask(function() {
					var ids = "";
					otherDs.each(function(item) {
						var id = item.id;
						var finaceName = item.data.finaceName;
						var amount = item.data.remainAmount;
						var flag = item.data.flag;
						var chk = item.data.currencyId;
						var status = item.data.status;
						var outFlag = item.data.outFlag;
						// 过滤掉新增条和减项费用和已生成应收帐和已导入出货
						if (!isNaN(id) && flag != 'M' && status != "1"
								&& outFlag != "1") {
							// 为了防止在导入出货后,订单页面没刷新时,outFlag还没即时变成1
							DWREngine.setAsync(false);
							cotOrderService.checkIsOut(id, function(res) {
								if (!res) {
									addByOtherMoney(id, finaceName, amount, chk);
									ids += id + ",";
								}
							});
							DWREngine.setAsync(true);
						}
					});

					if (ids != '') {
						// 修改所有其他费用的status为1
						DWREngine.setAsync(false);
						cotOrderService.updateStatus(ids, function(res) {
								});
						DWREngine.setAsync(true);
						otherDs.reload();
						// 重新设置recvDs的查询路径
						recvDs.proxy.setApi({
									read : "cotorder.do?method=queryRecv&fkId="
											+ parent.$('pId').value
								});
						recvDs.reload();
					} else {
						Ext.MessageBox.alert('提示消息',
								'不能重复生成应收款,并且已导入出货的费用不能生成应收款');
					}
					unmask();
				});
				task.delay(500);
			}
		});
	}

	// 删除应收表格选择的行
	function deleteByAccount() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限");
			return;
		}
		var idsAry = new Array();
		var list = recvSm.getSelections();
		// 判断是否有勾选记录
		if (list.length == 0) {
			Ext.MessageBox.alert('提示消息', "请先勾选要删除的应收帐款!");
			return;
		}

		Ext.each(list, function(item) {
					idsAry.push(item.id);
				});

		var str = "";
		// 查询应收帐是否有导到出货
		cotOrderService.checkIsImport(idsAry, function(newIds) {
					if (newIds.length > 0) {
						var str = '';
						if (newIds.length < idsAry.length) {
							str = "部分应收帐有金额导入出货或者含有收款记录不能删除,是否删除其他?";
						} else {
							str = "是否确实删除选择的应收帐?";
						}
						var flag = Ext.Msg.confirm('提示消息', str, function(btn) {
									if (btn == 'yes') {
										cotOrderService.deleteByAccount(newIds,
												function(res) {
													otherDs.reload();
													recvDs.reload();
													if (res) {
														Ext.MessageBox
																.alert('提示消息',
																		"删除成功!");
													}
												});
									}
								});
					} else {
						Ext.MessageBox.alert('提示消息',
								'您选择的应收帐有收款记录或有金额导入到出货,不能删除!');
					}
				});

	}

	// 预收货款添加到应收表格(生成预收货款后锁定比例和价格)
	function addByOrderMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 判断该订单是否已保存
		var mainId = parent.$('pId').value;
		if (mainId == 'null' || mainId == '') {
			Ext.MessageBox.alert('提示消息', '该订单还没保存,不能生成预收帐款!');
			return;
		}
		var totalLab = parent.$('totalLab').innerText;
		if (isNaN(totalLab) || parseFloat(totalLab) == 0) {
			Ext.MessageBox.alert('提示消息', '该订单货款总金额为0!,不能添加预收货款！');
			return;
		}
		if ($('prePrice').value == '') {
			Ext.MessageBox.alert('提示消息', '请先填写预收货款金额！', function(btn) {
						if (btn == 'ok') {
							$('prePrice').focus();
						}
					});
			return;
		}
		// 判断是否已添加预收货款
		var ck = false;
		recvDs.each(function(item) {
					if (item.data.finaceName == '预收货款') {
						ck = true;
						return false;
					}
				});

		if (ck) {
			Ext.MessageBox.alert('提示消息', '您已经添加了预收货款,不能再次添加!');
			return;
		}
		DWREngine.setAsync(false);
		var detail = new CotFinaceAccountrecv();
		var mainId = parent.$('pId').value;
		detail.fkId = mainId;
		detail.finaceName = '预收货款';
		detail.amount = $('prePrice').value;
		detail.currencyId = parent.$('currencyId').value;
		var orderNo = parent.$('orderNo').value;
		detail.orderNo = orderNo;
		// 添加到表格中
		// setObjToAccountGrid(detail);
		var custId = parent.$('custId').value;
		var busId = parent.$('bussinessPerson').value;
		var con = Ext.Msg.confirm('提示消息', '您是否确定将预收货款生成应收帐,并保存至该订单?', function(
						btn) {
					if (btn == 'yes') {
						// 生成应收帐款单号
						cotOrderService.createRecvNo(parseInt(custId),
								function(res) {
									detail.finaceNo = res;
									detail.orderNo = orderNo;
									detail.custId = custId;
									detail.businessPerson = busId;
									var companyId = parent.$('companyId').value;
									detail.companyId = companyId;
									// 保存应收帐款
									cotOrderService.saveAccountRecv(detail,
											parent.$('orderTime').value,
											$('priceScal').value,
											$('prePrice').value,
											function(deal) {
												// 重新设置recvDs的查询路径
												recvDs.proxy.setApi({
															read : "cotorder.do?method=queryRecv&fkId="
																	+ parent.$('pId').value
														});
												recvDs.reload();
											});
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 其他费用金额改变
	function changeOtherAmount(newVal, oldVal) {
		// 如果是新增
		if (isNaN(editRec.id)) {
			editRec.set('remainAmount', newVal);
		} else {
			// 订单加减费用导一部分到出货后,如果再修改订单其他费用金额,则金额不能小于导出的金额
			var rm = parseFloat(editRec.data.remainAmount);
			if (oldVal == rm) {
				editRec.set('remainAmount', newVal);
			} else {
				if (newVal < oldVal - rm) {
					editRec.set('amount', oldVal);
					var tip = new Ext.ToolTip({
								title : '提示',
								anchor : 'left',
								html : '有费用导入到出货,金额不能小于已导出的金额!'
							});
					tip.showAt([ckX, ckY]);
				} else {
					var temp = Number(rm).add(Number(newVal).sub(oldVal));
					editRec.set('remainAmount', temp);
					if (temp == 0) {
						editRec.set('outFlag', 1);
					} else {
						editRec.set('outFlag', 0);
					}
				}
			}
		}
	}

});