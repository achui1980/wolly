Ext.onReady(function() {
	DWREngine.setAsync(false);
	var curMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);

	//  表格--币种
	var curGridBox = new BindCombox({ 
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Please select',
				hideLabel : true,
				autoLoad : true,
				labelSeparator : " ",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});

	// 加、减
	var flagStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['A', 'Plus '], ['M', 'minus']]
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
				emptyText : 'Please select',
				hiddenName : 'flag',
				selectOnFocus : true
			});
	/** ******************************加减费用项目表格*********************************************** */
	var otherFeeRecord = new Ext.data.Record.create([{
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
				name : "status"
			}, {
				name : "currencyId"
			}, {
				name : "isImport"
			}, {
				name : "outFlag"
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var otherfeeds = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotorderfac.do?method=queryFinanceOther&fkId="
								+ parent.$('pId').value,
						create : "cotorderfac.do?method=addOther",
						update : "cotorderfac.do?method=modifyOther",
						destroy : "cotorderfac.do?method=removeOther"
					},
					listeners : {
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							// mask();
						},
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("Message", "Operation failed！");
							} else {
								otherfeeds.reload();
								// 修改主单的实际金额
								cotOrderFacService.modifyRealMoney(parent
												.$('pId').value, function(res) {
											parent.$('realLab').innerText = res
													.toFixed("2");
										});
							}
						}
					}
				}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, otherFeeRecord),
		writer : writer
	});
	// 创建复选框列
	var otherfeesm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var otherfeecm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [otherfeesm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Fees Name",
							dataIndex : "finaceName",
							width : 260,
							editor : new Ext.form.TextField({
										maxLength : 200,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Plus / minus",
							dataIndex : "flag",
							width : 60,
							renderer : function(value) {
								if (value == 'A') {
									return 'Plus';
								}
								if (value == 'M') {
									return 'minus';
								}
							},
							editor : flagBox
						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 50,
							renderer : function(value) {
								return curMap[value];
							},
							editor : curGridBox
						}, {
							header : "Amount",
							dataIndex : "amount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										allowNegative : false,
										decimalPrecision : 2,
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
							header : "The residual amount",
							dataIndex : "remainAmount",
							width : 80
						}]
			})
	var otherfeetoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : otherfeeds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var otherfeetb = new Ext.Toolbar({
				items : ['->', {
							text : "Create",
							handler : addNewGrid,
							iconCls : "page_add"
						}, '-', {
							text : "Delete",
							handler : onDelFee,
							iconCls : "page_del"
						}, '-', {
							text : "Generate payables",
							handler : addMoreByOtherMoney,
							iconCls : "page_jiao"
						}]
			});
	var otherfeegrid = new Ext.grid.EditorGridPanel({
				title : "Plus / minus cost of project",
				id : "otherfeeGrid",
				flex : 1,
				stripeRows : true,
				margins : "0 5 0 0",
				border : false,
				cls : 'rightBorder',
				store : otherfeeds, // 加载数据源
				cm : otherfeecm, // 加载列
				sm : otherfeesm,
				loadMask : true, // 是否显示正在加载
				tbar : otherfeetb,
				bbar : otherfeetoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** ************************应付帐款表格***************************************************** */
	var dealRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "finaceName"
			}, {
				name : "amount"
			}, {
				name : "currencyId"
			}, {
				name : "amountDate"
			}, {
				name : "finaceOtherId"
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var dealds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				baseParams : {
					orderNo : parent.$('orderNo').value,
					fkId : parent.$('pId').value,
					source : 'orderfac',
					factoryId : parent.$('factoryId').value
				},
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderfac.do?method=queryDeal"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, dealRecord),
				writer : writer
			});
	// 创建复选框列
	var dealsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var dealcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [dealsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Accounts number",
							dataIndex : "finaceNo",
							width : 140
						}, {
							header : "Project name payable",
							dataIndex : "finaceName",
							width : 180
						}, {
							header : "Amount",
							dataIndex : "amount",
							width : 80
						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "Accounts date",
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
	var dealtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : dealds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var dealtb = new Ext.Toolbar({
		items : ['-', {
					xtype : 'label',
					style : 'color:blue;font-weight: bold;',
					text : "Prepaid payment："
				}, {
					xtype : 'label',
					text : "By the total amount of money"
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
								Ext.MessageBox.alert('Message',
										'Please save the purchase order, payment in advance to generate!');
							} else {
								var allMoney = parseFloat(parent.$('totalLab').innerText);
								var orderMoney = allMoney * newVal * 0.01;
								$('prePrice').value = orderMoney.toFixed("2");
							}
						}
					}
				}, {
					xtype : 'label',
					text : "% Amount："
				}, {
					xtype : 'numberfield',
					id : 'prePrice',
					name : 'prePrice',
					decimalPrecision : 2,
					maxValue : 9999999.99,
					value : parent.$('prePriceHid').value,
					width : 80
				}, '-', {
					text : "Generation",
					handler : addByOrderMoney,
					iconCls : "page_application_put"
				}, '-', '->', {
					text : "Delete",
					handler : onDelDeal,
					iconCls : "page_del"
				}]
	});
	var dealgrid = new Ext.grid.GridPanel({
				title : "Accounts payable",
				id : "dealGrid",
				anchor : '100% 50%',
				border:false,
				cls:'leftBorder bottomBorder',
				stripeRows : true,
				store : dealds, // 加载数据源
				cm : dealcm, // 加载列
				sm : dealsm,
				loadMask : true, // 是否显示正在加载
				tbar : dealtb,
				bbar : dealtoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** *********************************付款记录表格*************************************************** */
	var detailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "finaceName"
			}, {
				name : "currentAmount"
			}, {
				name : "currencyId"
			}, {
				name : "finaceDate"
			}, {
				name : "finaceGivenid"
			}]);

	// 创建数据源
	var detailds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							url : "cotorderfac.do?method=queryDealDetail"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, detailRecord)
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
							header : "Payment Order No.",
							dataIndex : "finaceNo",
							width : 140
						}, {
							header : "Project Name",
							dataIndex : "finaceName",
							width : 180
						}, {
							header : "Payments",
							dataIndex : "currentAmount",
							width : 60
						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "Payment Date",
							dataIndex : "finaceDate",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "Paymentid",
							dataIndex : "finaceGivenid",
							hidden : true
						}]
			});
	var detailtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : detailds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var detailgrid = new Ext.grid.EditorGridPanel({
				title : "Payment History",
				anchor : '100% 50%',
				id : "detailGrid",
				stripeRows : true,
				border : false,
				cls : 'leftBorder',
				store : detailds, // 加载数据源
				cm : detailcm, // 加载列
				sm : detailsm,
				loadMask : true, // 是否显示正在加载
				bbar : detailtoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	dealgrid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				detailds.load({
							params : {
								start : 0,
								limit : 10,
								dealId : record.id
							}
						});
			});

	// 付款记录双击事件
	detailgrid.on("rowdblclick", function(grid, rowIndex, columnIndex, e) {
				var record = detailds.getAt(rowIndex);
				openFullWindow('cotfinancegiven.do?method=addFinacegiven&id=' + record.data.finaceGivenid);
			});

	// 让编辑控件textarea适应行高度
	otherfeegrid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var rec = otherfeeds.getAt(rowIndex);
				var dataIndex = otherfeecm.getDataIndex(columnIndex);
				if (rec.data.status == 1) {
					Ext.MessageBox.alert('Message', 'The fee payable has been generated, can not be modified!');
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
				var editor = otherfeecm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(cell.offsetWidth, row.scrollHeight);
			});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	otherfeegrid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后
	otherfeegrid.on("afteredit", function(e) {
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
						editRec.set('finaceName', e.originalValue);
						Ext.MessageBox.alert('Message', 'Cost of the name can not be empty!');
					} else {
						var flag = true;
						// 判断store中是否含有该费用名称
						otherfeeds.each(function(item) {
									if (item.data.finaceName == e.value
											&& item.id != editRec.id) {
										flag = false;
										return false;
									}
								});
						if (flag == false) {
							editRec.set('finaceName', e.originalValue);
							Ext.MessageBox.alert('Message', 'The cost of name already exists!');
						} else {
							var id = parent.$('pId').value;
							if (id != "" && id != "null") {
								var recId = 0;
								if (!isNaN(editRec.id)) {
									recId = editRec.id;
								}
								// 判断是否重复
								cotOrderFacService.findIsExistName(e.value, id,
										recId, function(res) {
											if (res == true) {
												editRec.set('finaceName',
														e.originalValue);
												Ext.MessageBox.alert('Message',
														'The cost of name already exists!');
											}
										});
							}
						}
					}
				}
			});

	/** *****布局********************************************************************************* */
	var panel = new Ext.Panel({
				layout : "anchor",
				border : false,
				flex : 1,
				items : [dealgrid, detailgrid]
			})
	var viewport = new Ext.Viewport({
				layout : "hbox",
				layoutConfig : {
					align : 'stretch'
				},
				items : [otherfeegrid, panel]
			});
	/** ************************************************************************************** */
	otherfeeds.load({
				params : {
					start : 0,
					limit : 15,
					type : 1,
					fkId : $('orderfacId').value
				}
			});

	dealds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	// 添加空白record到表格中
	function addNewGrid() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message', 'Sorry, the single has been reviewed can not be modified!');
			return;
		}

		var u = new otherfeeds.recordType({
					finaceName : "",
					flag : "A",
					amount : "",
					currencyId : parent.$('currencyId').value,
					remainAmount : ""
				});
		otherfeeds.add(u);
		// 货号获得焦点
		var cell = otherfeegrid.getView().getCell(otherfeeds.getCount() - 1, 2);
		if (Ext.isIE) {
			cell.fireEvent("ondblclick");
		} else {
			var e = document.createEvent("Events");
			e.initEvent("dblclick", true, false);
			cell.dispatchEvent(e)
		}
	}

	// 获得其他费用表格选择的记录
	function getOtherFeeIds() {
		var list = Ext.getCmp("otherfeeGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var otherFee = new CotFinaceOther();
					otherFee.id = item.id;
					res.push(otherFee);
				});
		return res;
	}

	// 删除其他费用
	function onDelFee() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('Message', "Sorry, you do not have Authority!");
			return;
		}
		var recs = otherfeesm.getSelections();
		if (recs.length == 0) {
			Ext.Msg.alert("Tip Box", "Please select records!");
			return;
		}
		var temp = 0;
		var idsAry = new Array();

		Ext.each(recs, function(item) {
					if (!isNaN(item.id)) {
						var isImport = item.data.isImport;
						var status = item.data.status;
						var amount = item.data.amount;
						var remainAmount = item.data.remainAmount;
						// 已生成应付帐的不能删除
						if (status != 1) {
							// 如果有金额导入出货,不能删除
							if (parseFloat(remainAmount) == parseFloat(amount)) {
								idsAry.push(item.id);
							}
						}
					} else {
						otherfeeds.remove(item);
						temp++;
					}
				});

		if (idsAry.length == 0 && temp == 0) {
			Ext.MessageBox.alert('Message', "There have been generated or the amount payable into shipping, can not be deleted!");
			return;
		}

		if (idsAry.length > 0) {
			Ext.MessageBox.confirm('Message',
					'Are you sure to delete the selected cost? Has been generated or the cost of accounts payable has to import shipments can not be removed!', function(btn) {
						if (btn == 'yes') {
							cotOrderFacService.deleteByIds(idsAry,
									function(res) {
										if (res != null) {
											// 修改主单的实际金额
											parent.$('realLab').innerText = (parseFloat(parent
													.$('realLab').innerText) - res)
													.toFixed("2");
											Ext.MessageBox.alert('Message',
													"Deleted successfully!");
										} else {
											Ext.MessageBox.alert('Message',
													"Delete failed, please contact the administrator!");
										}
										otherfeeds.reload();
									});
						}
					});
		}
	}

	// 获得应付帐款表格选择的记录
	function getDealIds() {
		var list = Ext.getCmp("dealGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var deal = new CotFinaceAccountdeal();
					deal.id = item.id;
					res.push(deal);
				});
		return res;
	}

	// 删除应付帐款
	function onDelDeal() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message', 'Sorry, the single has been reviewed can not be modified!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('Message', "You do not have permission to delete");
			return;
		}
		var idsAry = new Array();
		var list = dealsm.getSelections();
		// 判断是否有勾选记录
		if (list.length == 0) {
			Ext.MessageBox.alert('Message', "Please check payable to delete!");
			return;
		}

		Ext.each(list, function(item) {
					idsAry.push(item.id);
				});

		var str = "";
		// 查询应付帐是否有导到出货
		cotOrderFacService.checkIsImport(idsAry, function(newIds) {
					if (newIds.length > 0) {
						var str = '';
						if (newIds.length < idsAry.length) {
							str = "Part of the amount payable has to import shipments or contain payment records can not be deleted, to delete the other?";
						} else {
							str = "Are you sure to delete the selected accounts payable?";
						}
						var flag = Ext.Msg.confirm('Message', str, function(btn) {
									if (btn == 'yes') {
										cotOrderFacService.deleteByAccount(
												newIds, function(res) {
													otherfeeds.reload();
													dealds.reload();
													if (res) {
														Ext.MessageBox
																.alert('Message',
																		"Deleted successfully!");
													}
												});
									}
								});
					} else {
						Ext.MessageBox.alert('Message',
								'Accounts payable you select a payment record or the amount of import to shipping, can not be deleted!');
					}
				});

	}

	// 一次导多条加减费用到应付帐款
	function addMoreByOtherMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message', 'Sorry, the single has been reviewed can not be modified!');
			return;
		}
		// 判断其他费用是否有保存
		if (otherfeeds.getModifiedRecords().length != 0) {
			Ext.MessageBox.alert('Message', 'Please save other costs, and then generate accounts payable!');
			return;
		}

		Ext.MessageBox.confirm('Message', "Are you sure you generate accounts payable?", function(btn) {
			if (btn == 'yes') {
				// 先刷新该表格,为了防止在导入出货后,采购单页面没刷新时,outFlag还没即时变成1
				otherfeeds.reload();
				mask();
				var task = new Ext.util.DelayedTask(function() {
					var ids = "";
					otherfeeds.each(function(item) {
								var id = item.id;
								var finaceName = item.data.finaceName;
								var amount = item.data.remainAmount;
								var flag = item.data.flag;
								var chk = item.data.currencyId;
								var status = item.data.status;
								var outFlag = item.data.outFlag;
								// 过滤掉新增条和减项费用和已生成应付帐和已导入出货
								if (!isNaN(id) && flag != 'M' && status != "1"
										&& outFlag != "1") {
									addByOtherMoney(id, finaceName, amount, chk);
									ids += id + ",";
								}
							});

					if (ids != '') {
						// 修改所有其他费用的status为1
						DWREngine.setAsync(false);
						cotOrderFacService.updateStatus(ids, function(res) {
							otherfeeds.reload();
							// 重新设置dealds的查询路径
							dealds.proxy.setApi({
										read : "cotorderfac.do?method=queryDeal&fkId="
												+ parent.$('pId').value
									});
							dealds.reload();
						});
						DWREngine.setAsync(true);
					} else {
						Ext.MessageBox.alert('Message',
								'Generation should not repeat the payment and the cost of imported shipments can not generate payables');
					}
					unmask();
				});
				task.delay(500);
			}
		});
	}

	// 判断应付帐款是否存在
	function checkIsExist(name) {
		var flag = false;
		dealds.each(function(rec) {
					if (rec.data.finaceName == name) {
						flag = true;
					}
				});
		return flag;
	}
	// 将其他费用添加到应付表格
	function addByOtherMoney(id, name, money, currencyId) {
		var detail = new CotFinaceAccountdeal();
		detail.finaceOtherId = id;
		detail.finaceName = name;
		detail.amount = money;
		var mainId = parent.$('pId').value;
		detail.fkId = mainId;
		var orderNo = parent.$('orderNo').value;
		var factoryId = parent.$('factoryId').value;
		var busId = parent.$('businessPerson').value;
		// 生成应付帐款单号
		DWREngine.setAsync(false);
		cotOrderFacService.createDealNo(parseInt(factoryId), function(res) {
					detail.finaceNo = res;
					detail.orderNo = orderNo;
					detail.businessPerson = busId;
					var companyId = parent.$('companyId').value;
					detail.companyId = companyId;
					detail.factoryId = factoryId;
					detail.currencyId = currencyId;
					// 保存应付帐款
					cotOrderFacService.saveAccountdeal(detail, parent
									.$('orderTime').value, "", "", function(
									deal) {
							});
				});
		DWREngine.setAsync(true);
	}

	// 保存其他费用(父页面调用)
	this.saveOther = function(mainId, orderNo, source, type, factoryId) {
		// 更改添加action参数
		var urlAdd = '&'+Ext.urlEncode({'mainId':mainId,'mainNo':orderNo,'facId':factoryId});

		// 更改修改action参数 type 0:应收 1:应付
		var urlMod = '&'+Ext.urlEncode({'orderNo':orderNo});

		// 更改删除action参数
		var urlDel = '&orderPrimId=' + mainId;

		otherfeeds.proxy.setApi({
					read : "cotorderfac.do?method=queryFinanceOther&fkId="
							+ parent.$('pId').value,
					create : "cotorderfac.do?method=addOther" + urlAdd,
					update : "cotorderfac.do?method=modifyOther" + urlMod
				});
		otherfeeds.save();
	}

	// 保存应收涨
	this.saveAccountdeal = function(mainId) {

		// 更改删除action参数
		var urlDel = '&orderPrimId=' + mainId;

		dealds.proxy.setApi({
					read : "cotorderfac.do?method=queryDeal&fkId="
							+ parent.$('pId').value
				});
		dealds.save();
	}

	// 预付货款添加到应付表格(生成预付货款后锁定比例和价格)
	function addByOrderMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message', 'Sorry, the single has been reviewed can not be modified!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 判断该单是否已保存
		var mainId = parent.$('pId').value;
		if (mainId == 'null' || mainId == '') {
			Ext.MessageBox.alert('Message', 'The purchase orders have not saved, can not generate Prepayments!');
			return;
		}
		var totalLab = parent.$('totalLab').innerText;
		if (isNaN(totalLab) || parseFloat(totalLab) == 0) {
			Ext.MessageBox.alert('Message', 'The total amount of the purchase order payment 0!, Can not add a payment in advance!');
			return;
		}
		if ($('prePrice').value == '') {
			Ext.MessageBox.alert('Message', 'Please fill in the amount of advance payment！', function(btn) {
						if (btn == 'ok') {
							$('prePrice').focus();
						}
					});
			return;
		}
		// 判断是否已添加预付货款
		var ck = false;
		dealds.each(function(item) {
					if (item.data.finaceName == 'Prepaid payment') {
						ck = true;
						return false;
					}
				});

		if (ck) {
			Ext.MessageBox.alert('Message', 'You have added a payment in advance, can not be added again');
			return;
		}
		DWREngine.setAsync(false);
		var detail = new CotFinaceAccountdeal();
		var mainId = parent.$('pId').value;
		detail.fkId = mainId;
		detail.finaceName = 'Prepaid payment';
		detail.amount = $('prePrice').value;
		detail.currencyId = parent.$('currencyId').value;
		var orderNo = parent.$('orderNo').value;
		detail.orderNo = orderNo;
		// 添加到表格中
		// setObjToAccountGrid(detail);
		var factoryId = parent.$('factoryId').value;
		var busId = parent.$('businessPerson').value;
		var con = Ext.Msg.confirm('Message', 'Are you sure you generate the prepaid accounts payable, and save to the purchase orders?',
				function(btn) {
					if (btn == 'yes') {
						// 生成应付帐款单号
						cotOrderFacService.createDealNo(parseInt(factoryId),
								function(res) {
									detail.finaceNo = res;
									detail.factoryId = factoryId;
									detail.businessPerson = busId;
									var companyId = parent.$('companyId').value;
									detail.companyId = companyId;
									// 保存应收帐款
									cotOrderFacService.saveAccountDeal(detail,
											parent.$('orderTime').value,
											$('priceScal').value,
											$('prePrice').value,
											function(deal) {
												// 重新设置dealds的查询路径
												dealds.proxy.setApi({
													read : "cotorderfac.do?method=queryDeal&fkId="
															+ parent.$('pId').value
												});
												dealds.reload();
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
			// 加减费用导一部分到出货后,如果再修改其他费用金额,则金额不能小于导出的金额
			var rm = parseFloat(editRec.data.remainAmount);
			if (oldVal == rm) {
				editRec.set('remainAmount', newVal);
			} else {
				if (newVal < oldVal - rm) {
					editRec.set('amount', oldVal);
					var tip = new Ext.ToolTip({
								title : 'Message',
								anchor : 'left',
								html : 'There shipping costs into the amount of not less than the amount exported!'
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