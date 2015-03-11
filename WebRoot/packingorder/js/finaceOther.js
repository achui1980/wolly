Ext.onReady(function() {
	DWREngine.setAsync(false);
	// 其他费用币种，读取业务配置中的数据
	var otherCurrencyId = ""
	cotPackOrderService.getList('CotPriceCfg', function(res) {
				if (res.length != 0) {
					if (res[0].facPriceUnit != null) {
						otherCurrencyId = res[0].facPriceUnit
					}
				}
			});
	var curMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);

	// 表格--币种
	var curGridBox = new BindCombox({
				autoLoad : true,
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				emptyText : '请选择',
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
				name : "currencyId"
			}, {
				name : "status"
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
						read : "cotpackingorder.do?method=queryFinanceOther&fkId="
								+ parent.$('pId').value,
						create : "cotpackingorder.do?method=addOther",
						update : "cotpackingorder.do?method=modifyOther"
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
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								otherfeeds.reload();
								// 修改主单的实际金额
								cotPackOrderService.modifyRealMoney(parent
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
	// 储存数量单元格获得焦点事件的文本框坐标
	var ckX;
	var ckY;
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
							header : "费用名称",
							dataIndex : "finaceName",
							width : 160,
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
							header : "金额",
							dataIndex : "amount",
							width : 60,
							editor : new Ext.form.TextField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
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
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							},
							editor : curGridBox
						}, {
							header : "剩余金额",
							dataIndex : "remainAmount",
							width : 60
						}]
			});
	var otherfeetoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : otherfeeds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var otherfeetb = new Ext.Toolbar({
				items : ['->', {
							text : "新增",
							handler : addNewGrid,
							iconCls : "page_add"
						}, '-', {
							text : "删除",
							handler : onDelFee,
							iconCls : "page_del"
						}, '-', {
							text : "生成应付款",
							handler : addMoreByOtherMoney,
							iconCls : "page_jiao"
						}]
			});
	var otherfeegrid = new Ext.grid.EditorGridPanel({
				title : "加/减费用项目",
				id : "otherfeeGrid",
				stripeRows : true,
				margins : "0 5 0 0",
				flex : 1,
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
			}

	]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var dealds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				baseParams : {
					orderNo : parent.$('packingOrderNo').value,
					fkId : parent.$('pId').value,
					source : 'packorder',
					factoryId : parent.$('factoryId').value
				},
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotpackingorder.do?method=queryDeal"
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
							header : "帐款单号",
							dataIndex : "finaceNo",
							width : 140
						}, {
							header : "应付项目名称",
							dataIndex : "finaceName",
							width : 180
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							}
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
	var dealtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : dealds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var dealtb = new Ext.Toolbar({
				items : ['->', {
							text : "删除",
							handler : onDelDeal,
							iconCls : "page_del"
						}]
			});
	var dealgrid = new Ext.grid.EditorGridPanel({
				title : "应付帐款",
				id : "dealGrid",
				anchor : '100% 50%',
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
							url : "cotpackingorder.do?method=queryDealDetail"
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
							header : "付款单号",
							dataIndex : "finaceNo",
							width : 140
						}, {
							header : "项目名称",
							dataIndex : "finaceName",
							width : 180
						}, {
							header : "付款金额",
							dataIndex : "currentAmount",
							width : 60
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "付款日期",
							dataIndex : "finaceDate",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "付款id",
							dataIndex : "finaceGivenid",
							hidden : true
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
	var detailgrid = new Ext.grid.EditorGridPanel({
				title : "付款记录",
				anchor : '100% 50%',
				id : "detailGrid",
				stripeRows : true,
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
				openFullWindow('cotfinancegiven.do?method=addFinacegiven&id='
						+ record.data.finaceGivenid);
			});

	otherfeegrid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var rec = otherfeeds.getAt(rowIndex);
				var dataIndex = otherfeecm.getDataIndex(columnIndex);
				if (rec.data.status == 1) {
					Ext.MessageBox.alert('提示消息', '该费用已经生成应付帐款,不能再修改!');
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
						Ext.MessageBox.alert('提示消息', '费用名称不能为空!');
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
							Ext.MessageBox.alert('提示消息', '该费用名称已经存在!');
						} else {
							var id = parent.$('pId').value;
							if (id != "" && id != "null") {
								var recId = 0;
								if (!isNaN(editRec.id)) {
									recId = editRec.id;
								}
								// 判断是否重复
								cotPackOrderService.findIsExistName(e.value,
										id, recId, function(res) {
											if (res == true) {
												editRec.set('finaceName',
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

	/** *****布局********************************************************************************* */
	var panel = new Ext.Panel({
				layout : "anchor",
				flex : 1,
				border : false,
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
					fkId : $('packId').value
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
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var u = new otherfeeds.recordType({
					finaceName : "",
					flag : "A",
					amount : "",
					remainAmount : "",
					currencyId : otherCurrencyId
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
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限!");
			return;
		}
		var recs = otherfeesm.getSelections();
		if (recs.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
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
						// 已生成应收帐的不能删除
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
			Ext.MessageBox.alert('提示消息', "已生成应付帐或者有金额导入出货,不能删除!");
			return;
		}

		if (idsAry.length > 0) {
			Ext.MessageBox.confirm('提示消息',
					'您是否确定删除选择的费用?已生成应付帐或者该费用有导入出货,不能删除!', function(btn) {
						if (btn == 'yes') {
							cotPackOrderService.deleteByIds(idsAry, function(
									res) {
								if (res != null) {
									// 修改主单的实际金额
									parent.$('realLab').innerText = (parseFloat(parent
											.$('realLab').innerText) - res)
											.toFixed("2");
									Ext.MessageBox.alert('提示消息', "删除成功!");
								} else {
									Ext.MessageBox
											.alert('提示消息', "删除失败,请联系管理员!");
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
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限");
			return;
		}
		var idsAry = new Array();
		var list = dealsm.getSelections();
		// 判断是否有勾选记录
		if (list.length == 0) {
			Ext.MessageBox.alert('提示消息', "请先勾选要删除的应付帐款!");
			return;
		}

		Ext.each(list, function(item) {
					idsAry.push(item.id);
				});

		var str = "";
		// 查询应付帐是否有导到出货
		cotPackOrderService.checkIsImport(idsAry, function(newIds) {
					if (newIds.length > 0) {
						var str = '';
						if (newIds.length < idsAry.length) {
							str = "部分应付帐有金额导入出货或者含有付款记录不能删除,是否删除其他?";
						} else {
							str = "是否确实删除选择的应付帐?";
						}
						var flag = Ext.Msg.confirm('提示消息', str, function(btn) {
									if (btn == 'yes') {
										cotPackOrderService.deleteByAccount(
												newIds, function(res) {
													otherfeeds.reload();
													dealds.reload();
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
								'您选择的应付帐有付款记录或有金额导入到出货,不能删除!');
					}
				});

	}

	// 一次导多条加减费用到应付帐款
	function addMoreByOtherMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}
		// 判断其他费用是否有保存
		if (otherfeeds.getModifiedRecords().length != 0) {
			Ext.MessageBox.alert('提示消息', '请先保存其他费用,再生成应付帐款!');
			return;
		}

		Ext.MessageBox.confirm('提示消息', "是否确定生成应付帐款?", function(btn) {
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
						cotPackOrderService.updateStatus(ids, function(res) {
							otherfeeds.reload();
							// 重新设置dealds的查询路径
							dealds.proxy.setApi({
								read : "cotpackingorder.do?method=queryDeal&fkId="
										+ parent.$('pId').value
							});
							dealds.reload();
						});
						DWREngine.setAsync(true);
					} else {
						Ext.MessageBox.alert('提示消息',
								'不能重复生成应付款,并且已导入出货的费用不能生成应付款');
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
		var orderNo = parent.$('packingOrderNo').value;
		var factoryId = parent.$('factoryId').value;
		var busId = parent.$('empId').value;
		// 生成应付帐款单号
		DWREngine.setAsync(false);
		cotPackOrderService.createDealNo(parseInt(factoryId), function(res) {
					detail.finaceNo = res;
					detail.orderNo = orderNo;
					detail.businessPerson = busId;
					var companyId = parent.$('companyId').value;
					detail.companyId = companyId;
					detail.factoryId = factoryId;
					detail.currencyId = currencyId;
					// 保存应付帐款
					cotPackOrderService.saveAccountdeal(detail, parent
									.$('orderDate').value, "", "", function(
									deal) {
							});
				});
		DWREngine.setAsync(true);
	}

	// 保存其他费用(父页面调用)
	this.saveOther = function(mainId, orderNo, source, type, factoryId) {

		// 更改添加action参数
		var urlAdd = '&mainId=' + mainId + '&mainNo=' + encodeURI(orderNo)
				+ '&facId=' + factoryId;

		// 更改修改action参数 type 0:应收 1:应付
		var urlMod = '&fkId=' + mainId + '&orderNo=' + encodeURI(orderNo)
				+ '&facId=' + factoryId;
		+'&source=' + source + '&type=' + type;

		// 更改删除action参数
		var urlDel = '&orderPrimId=' + mainId;

		otherfeeds.proxy.setApi({
					read : "cotpackingorder.do?method=queryFinanceOther&fkId="
							+ parent.$('pId').value,
					create : "cotpackingorder.do?method=addOther" + urlAdd,
					update : "cotpackingorder.do?method=modifyOther" + urlMod
				});
		otherfeeds.save();
	}

	// 保存应收涨
	this.saveAccountdeal = function(mainId) {

		// 更改删除action参数
		var urlDel = '&orderPrimId=' + mainId;

		dealds.proxy.setApi({
					read : "cotpackingorder.do?method=queryDeal&fkId="
							+ parent.$('pId').value
				});
		dealds.save();
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