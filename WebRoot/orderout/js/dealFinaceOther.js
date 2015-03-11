Ext.onReady(function() {
	DWREngine.setAsync(false);
	var curMap = null;
	var facMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);

	DWREngine.setAsync(false);
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});
	DWREngine.setAsync(true);

	// 表格--币种
	var curGridBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				autoLoad : true,
				hideLabel : true,
				labelSeparator : " ",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});

	// 表格-厂家
	var facGridBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=orderout&type="+parent.$('pId').value,
				cmpId : 'factoryId',
				hideLabel : true,
				labelSeparator : " ",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				allowBlank : false,
				blankText : "Suppliers",
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
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
				emptyText : 'Choose',
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
				name : "orderNo"
			}, {
				name : "flag"
			}, {
				name : "amount"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}, {
				name : "factoryId"
			}, {
				name : "source"
			}, {
				name : "outFlag"
			}, {
				name : "status"
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
						read : "cotorderout.do?method=queryFinanceDeal&fkId="
								+ parent.$('pId').value,
						create : "cotorderout.do?method=addOrderFacOther",
						update : "cotorderout.do?method=modifyOrderFacOther"
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
								Ext.Msg.alert("Message", "Operation failed！");
							} else {
								otherfeeds.reload();
								findNoImportNum();
							}
							unmask();
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
	var otherfeesm = new Ext.grid.CheckboxSelectionModel({
				moveEditorOnEnter : false
			});
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
							header : "<font color=red>Supplier</font>",
							dataIndex : "factoryId",
							width : 110,
							renderer : function(value) {
								return facMap[value];
							},
							editor : facGridBox
						}, {
							header : "采购单号/付款单号",
							dataIndex : "orderNo",
							width : 130
						}, {
							header : "<font color=red>费用名称</font>",
							dataIndex : "finaceName",
							width : 120,
							editor : new Ext.form.TextField({
										height : 25,
										allowBlank : false,
										blankText : "请输入费用名称！",
										maxLength : 200,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color=red>加/减</font>",
							dataIndex : "flag",
							width : 40,
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
							header : "<font color=red>金额</font>",
							dataIndex : "amount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
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
							header : "<font color=red>币种</font>",
							dataIndex : "currencyId",
							width : 50,
							renderer : function(value) {
								return curMap[value];
							},
							editor : curGridBox
						}, {
							header : "来源",
							dataIndex : "source",
							width : 100,
							renderer : function(value) {
								if (value == 'FacOther') {
									return '产品加减费用';
								}
								if (value == 'FitOther') {
									return '配件加减费用';
								}
								if (value == 'PackOther') {
									return '包材加减费用';
								}
								if (value == 'FacDeal') {
									return '产品应付款';
								}
								if (value == 'FitDeal') {
									return '配件应付款';
								}
								if (value == 'PackDeal') {
									return '包材应付款';
								}
								if (value == 'yiDeal') {
									return '厂家溢付款';
								}
								if (value == 'FacMoney') {
									return '产品货款';
								}
								if (value == 'FitMoney') {
									return '配件货款';
								}
								if (value == 'PackMoney') {
									return '包材货款';
								}
							}
						}, {
							header : "中转id",
							dataIndex : "outFlag",
							width : 80,
							hidden : true
						}]
			});
	var otherfeetoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : otherfeeds,
				displayInfo : true,
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});
	var otherfeetb = new Ext.Toolbar({
				items : [{
							text : "新增",
							handler : addNewGrid,
							iconCls : "page_add"
						}, '-', {
							text : "删除",
							handler : onDelFee,
							iconCls : "page_del"
						}, '-', {
							text : "合同分配",
							handler : conToAllocate,
							iconCls : "page_fenpei"
						}, '-', {
							text : "自动分配",
							handler : autoOrderOut,
							iconCls : "page_jiao"
						}, '-', {
							text : "生成应付款",
							handler : addMoreByOtherMoney,
							iconCls : "gird_deal"
						}, '->', {
							text : "全部导入<font color=red>(<span id='noImportNum'>0</span>)</font>",
							handler : importAll,
							iconCls : "gird_exp"
						}, {
							text : "选择导入",
							handler : showImportPanel,
							iconCls : "gird_exp"
						}]
			});
	var otherfeegrid = new Ext.grid.EditorGridPanel({
				title : "加/减费用项目",
				id : "otherfeeGrid",
				flex : 1,
				stripeRows : true,
				border:false,
				cls : 'rightBorder',
				margins : "0 5 0 0",
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
				name : "factoryId"
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
		proxy : new Ext.data.HttpProxy({
			api : {
				read : "cotorderout.do?method=queryDeal&orderNo="
						+ parent.$('orderNo').value + "&fkId=" + $('pId').value,
				destroy : "cotorderout.do?method=removeDeal"
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
							width : 120
						}, {
							header : "应付项目名称",
							dataIndex : "finaceName",
							width : 150
						}, {
							header : "供应商",
							dataIndex : "factoryId",
							width : 120,
							renderer : function(value) {
								return facMap[value];
							}
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 50,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "帐款日期",
							dataIndex : "amountDate",
							width : 80,
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
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
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
				anchor : '100% 50%',
				id : "dealGrid",
				border:false,
				cls : 'leftBorder bottomBorder',
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
							url : "cotorderout.do?method=queryDealDetail"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, detailRecord)
			});
	// 加上应收款的选择的记录id
	detailds.on('beforeload', function() {
				var cod = dealsm.getSelections();
				if (cod.length == 1) {
					detailds.baseParams.dealId = cod[0].id;
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
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});
	var detailTb = new Ext.Toolbar({
				items : ['->', {
							text : "新增",
							handler : windowRecvAdd,
							iconCls : "page_add"
						},{
							text : "删除",
							handler : deleteByRecDetail,
							iconCls : "page_del"
						}]
			});
	var detailgrid = new Ext.grid.EditorGridPanel({
				title : "付款记录",
				anchor : '100% 50%',
				id : "detailGrid",
				stripeRows : true,
				store : detailds, // 加载数据源
				cm : detailcm, // 加载列
				sm : detailsm,
				border : false,
				cls:'leftBorder',
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
				items : [dealgrid, detailgrid]
			})
	// 让编辑控件textarea适应行高度
	otherfeegrid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var rec = otherfeeds.getAt(rowIndex);
				var dataIndex = otherfeecm.getDataIndex(columnIndex);
				if (rec.data.status == 1) {
					Ext.MessageBox.alert('提示消息', '该费用已经生成应付帐款,不能再修改!');
					return false;
				}
				//除了新增,可以更换厂家后,其他不行
				if ((dataIndex == 'flag' || dataIndex == 'factoryId')
						&& rec.data.source != 'orderfacToOut') {
					return false;
				}
				//货款金额不能编辑
				if (rec.data.source == 'FacMoney' || rec.data.source == 'FitMoney' || rec.data.source == 'PackMoney') {
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

	var viewport = new Ext.Viewport({
				layout : "hbox",
				layoutConfig : {
					align : 'stretch'
				},
				items : [otherfeegrid, panel]
			});

	//查询未导入的应收款
	function findNoImportNum(){
		var id=parent.$('pId').value;
		if(id!='' && id!='null'){
			cotOrderOutService.findNoImportNumFac(id,function(res){
				$('noImportNum').innerText=res;
			});
		}
	}
	findNoImportNum();
	/** ************************************************************************************** */
	otherfeeds.load({
				params : {
					start : 0,
					limit : 15,
					type : 1,
					fkId : $('pId').value
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
				var record = grid.getStore().getAt(rowIndex);
				var fkId = record.get("finaceGivenid")
				openFullWindow('cotfinancegiven.do?method=add&id=' + fkId);
			});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	otherfeegrid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后
	otherfeegrid.on("afteredit", function(e) {
				var flag;
				if (e.field == 'factoryId') {
				}

				if (e.field == 'finaceName') {
					if (e.value == "") {
						editRec.set('finaceName', e.originalValue);
						Ext.MessageBox.alert('提示消息', '费用名称不能为空!');
					} else {
						var temp = true;
						// 判断store中是否含有该费用名称
						otherfeeds.each(function(item) {
									if (item.data.finaceName == e.value
											&& item.id != editRec.id) {
										temp = false;
										return false;
									}
								});
						if (temp == false) {
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
								cotOrderOutService.findIsExistNameDeal(e.value,
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
					editRec.set('amount', (am * ra / newRa).toFixed("2"));
				}

				if (e.field == 'amount') {
					changeOtherAmount(e.value);
				}
			});

	dealds.load({
				params : {
					start : 0,
					limit : 20,
					orderNo : parent.$('orderNo').value,
					fkId : $('pId').value
				}
			});

	// 获取有其他费用及应付款采购合同单号
	function findOrderFacIds() {
		var orderfacIds = '';
		var id = parent.$('pId').value;
		if (id == 'null' || id == '') {
			Ext.Msg.alert("提示框", '请先保存出货单!');
		} else {
			// 查询该出货单的所有订单编号
			DWREngine.setAsync(false);
			cotOrderOutService.checkIsHasFac(id, function(res) {
						var strAry = res
						for (var i = 0; i < strAry.length; i++) {
							cotOrderOutService.checkIsHasDeal(strAry[i].id,
									'orderfac', function(orderfacid1) {
										if (orderfacid1 != null) {
											orderfacIds += strAry[i].id + ',';
										} else {
											cotOrderOutService.checkIsHasOther(
													strAry[i].id, 'orderfac',
													function(orderfacid2) {
														if (orderfacid2 != null) {
															orderfacIds += strAry[i].id
																	+ ',';
														}
													});
										}
									});
						}
						$('orderfacIds').value = orderfacIds;
					});
			DWREngine.setAsync(true);
		}
	}

	// 获取有其他费用及应付款配件采购单号
	function findFitOrderIds() {
		var fitorderIds = '';
		var id = parent.$('pId').value;
		if (id == 'null' || id == '') {
			Ext.Msg.alert("提示框", '请先保存出货单!');
		} else {
			// 查询该出货单的所有订单编号
			DWREngine.setAsync(false);
			cotOrderOutService.checkIsHasFitOrders(id, function(res) {
						var strAry = res.split(',');
						for (var i = 0; i < strAry.length - 1; i++) {
							cotOrderOutService.checkIsHasDeal(strAry[i],
									'fitorder', function(orderfacid1) {
										if (orderfacid1 != null) {
											fitorderIds += strAry[i] + ',';
										} else {
											cotOrderOutService.checkIsHasOther(
													strAry[i], 'fitorder',
													function(orderfacid2) {
														if (orderfacid2 != null) {
															fitorderIds += strAry[i]
																	+ ',';
														}
													});
										}
									});
						}
						$('fitorderIds').value = fitorderIds;

					});
			DWREngine.setAsync(true);
		}
	}
	
	// 打开添加页面
	function windowRecvAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType('cotfinacegiven.do', "ADD");
		if (isPopedom == 0){
			Ext.Msg.alert("提示信息", "您没有添加付款记录的权限!");
			return;
		}
		var recs=dealsm.getSelections();
		if(recs.length!=1){
			Ext.Msg.alert("提示信息", "请选勾选一条应付帐款!");
			return;
		}
		openFullWindow('cotfinancegiven.do?method=addFinacegiven&orderFlag='+recs[0].data.factoryId);
	}

	// 删除付款记录
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
		Ext.MessageBox.confirm('提示消息', '您是否确定立即删除选择的付款记录?', function(btn) {
					if (btn == 'yes') {
						cotOrderOutService.deleteByDealDetail(idsAry, function(
										res) {
									if (res) {
										Ext.MessageBox.alert('提示消息', "删除成功!");
									} else {
										Ext.MessageBox.alert('提示消息', "删除失败!");
									}
									detailds.reload();
								})
					}
				});
	}

	// 获取有其他费用及应付款包材采购单号
	function findPackOrderIds() {
		var packorderIds = '';
		var id = parent.$('pId').value;
		if (id == 'null' || id == '') {
			Ext.Msg.alert("提示框", '请先保存出货单!');
		} else {
			// 查询该出货单的所有订单编号
			DWREngine.setAsync(false);
			cotOrderOutService.checkIsHasPackOrders(id, function(res) {
						// var strAry = res.split(',');
						// for (var i = 0; i < strAry.length - 1; i++) {
						// cotOrderOutService.checkIsHasDeal(strAry[i],
						// 'packorder', function(orderfacid1) {
						// if (orderfacid1 != null) {
						// packorderIds += strAry[i] + ',';
						// } else {
						// cotOrderOutService.checkIsHasOther(
						// strAry[i], 'packorder',
						// function(orderfacid2) {
						// if (orderfacid2 != null) {
						// packorderIds += strAry[i]
						// + ',';
						// }
						// });
						// }
						// });
						// }
						// $('packorderIds').value = packorderIds;
						$('packorderIds').value = res;

					});
			DWREngine.setAsync(true);
		}
	}
	
	// 导入一条其他费用到出货其他费用
	function insertOtherByBatchAll(list, type, clickType) {
		var temp = 0;
		Ext.each(list, function(item) {
					var id = item.id;
					var detail = new Object();
					// 添加到表格中
					var flag = checkIsExistOther(id, type, clickType);
					if (flag != "") {
						temp++;
						DWREngine.setAsync(false);
						cotOrderOutService.getCotFinaceOtherById(parseInt(id),
								function(other) {
									if (other != null) {
										detail.factoryId = other.factoryId;
										detail.orderNo = other.orderNo;
										detail.finaceName = other.finaceName;
										if(other.finaceName=='溢付款'){
											detail.amount = other.amount;
										}else{
											detail.amount = other.remainAmount;
											detail.remainAmount = other.remainAmount;
										}
										detail.currencyId = other.currencyId;
										detail.flag = other.flag;
										detail.outFlag = other.id;
										detail.source = flag;
										setObjToGrid(detail);
									}
								})
						DWREngine.setAsync(true);
					}
				});
	}

	// 导入未冲帐完成的应付帐款到出货其他费用
	function insertDealByBatchAll(list, type, clickType) {
		var temp = 0;
		Ext.each(list, function(item) {
					var id = item.id;
					var flag = checkIsExistOther(id, type, clickType);
					if (flag != "") {
						temp++;
						DWREngine.setAsync(false);
						cotOrderOutService.getOrderFacDealById(parseInt(id),
								function(deal) {
									var detail = new Object();
									detail.orderNo = deal.orderNo;
									detail.factoryId = deal.factoryId;
									detail.finaceName = deal.finaceName;
									detail.currencyId = deal.currencyId;
									detail.remainAmount = deal.zhRemainAmount;
									detail.outFlag = id;
									detail.source = flag;
									if (deal.finaceName == '预付货款') {
										detail.remainAmount = deal.zhRemainAmount;
										detail.amount = deal.zhRemainAmount;
										detail.flag = 'M';
									} else {
										detail.amount = deal.zhRemainAmount;
										detail.flag = 'A';
									}
									// 添加到表格中
									setObjToGrid(detail);
								});
						DWREngine.setAsync(true);
					}
				});
	}
	
	//全部导入
	function importAll(){
		var num=$('noImportNum').innerText;
		if(num==0){
			Ext.MessageBox.alert('提示消息', '没有可导入的应收款!');
			return;
		}
		DWREngine.setAsync(false);
		var id=parent.$('pId').value;
		if(id!='' && id!='null'){
			cotOrderOutService.findNoImportAllFac(id,function(res){
				if(res[0]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[0],function(list){
						insertOtherByBatchAll(list, 'other', 'orderfac');				
					});
				}
				if(res[1]!=''){
					cotOrderOutService.getListByTable('CotFinaceAccountdeal',res[1],function(list){
						insertDealByBatchAll(list, 'deal', 'orderfac');						
					});
				}
				if(res[2]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[2],function(list){
						insertOtherByBatchAll(list, 'overfee', '');				
					});
				}
				
				if(res[3]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[3],function(list){
						insertOtherByBatchAll(list, 'other', 'fitorder');				
					});
				}
				if(res[4]!=''){
					cotOrderOutService.getListByTable('CotFinaceAccountdeal',res[4],function(list){
						insertDealByBatchAll(list, 'deal', 'fitorder');						
					});
				}
				if(res[5]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[5],function(list){
						insertOtherByBatchAll(list, 'overfee', '');				
					});
				}
				
				if(res[6]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[6],function(list){
						insertOtherByBatchAll(list, 'other', 'packorder');				
					});
				}
				if(res[7]!=''){
					cotOrderOutService.getListByTable('CotFinaceAccountdeal',res[7],function(list){
						insertDealByBatchAll(list, 'deal', 'packorder');						
					});
				}
				if(res[8]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[8],function(list){
						insertOtherByBatchAll(list, 'overfee', '');				
					});
				}
			});
		}
		DWREngine.setAsync(true);
	}

	// 显示导入界面
	var _self = this;
	function showImportPanel() {
		// 判断出货单是否已保存
		var id = parent.$('pId').value;
		if (id == 'null' || id == '') {
			Ext.MessageBox.alert('提示消息', '请先保存出货单!');
			return;
		}
		var cfg = {};
		cfg.bar = _self;
		var importPanel = new ImportPanel(cfg);
		importPanel.show();
		importPanel.openPnl(0);
	}

	// 记住当前行
	var rowCurrent = -1;
	// 添加空白record到表格中
	function addNewGrid() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}
		var rmbId=0;
		//获得rmb的编号
		for (var p in curMap) {
			if(curMap[p]=='RMB'){
				rmbId=p;
			}
		}
		var u = new otherfeeds.recordType({
					factoryId : "",
					orderNo : "",
					finaceName : "",
					flag : "A",
					amount : "0",
					remainAmount : "0",
					currencyId : rmbId,
					source : "orderfacToOut"
				});
		otherfeeds.add(u);
		// 货号获得焦点
		var cell = otherfeegrid.getView().getCell(otherfeeds.getCount() - 1, 2);
		rowCurrent = otherfeegrid.getStore().getCount() - 1;
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
			Ext.MessageBox.alert('提示消息', "您没有删除权限");
			return;
		}
		var idsAry = new Array();
		var temp = 0;

		var recs = otherfeesm.getSelections();
		Ext.each(recs, function(item) {
					if (!isNaN(item.id)) {
						var isImport = item.data.outFlag;
						var status = item.data.status;
						// 如果状态为1,导过不能删除
						if (status != "1") {
							idsAry.push(item.id);
						}
					} else {
						otherfeeds.remove(item);
						temp++;
					}
				});
		if (idsAry.length == 0 && temp == 0) {
			Ext.MessageBox.alert('提示消息', "您选择的费用已生成应付帐,必须先删除应付帐才能删除!");
			return;
		}

		if (idsAry.length > 0) {
			Ext.MessageBox.confirm('提示消息', '您是否确定删除选择的费用?已生成应付帐不能删除!',
					function(btn) {
						if (btn == 'yes') {
							cotOrderOutService.deleteDealByIds(idsAry,
									function(res) {
										if (res == 0) {
											// 更高总金额
											// parent.$('totalLab').innerText=res.toFixed('2');
											Ext.MessageBox
													.alert('提示消息', "删除成功");
											findNoImportNum();
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
		Ext.MessageBox.confirm('提示消息', '您是否确定删除这些应付帐?', function(btn) {
			if (btn == 'yes') {
				var mainId = parent.$('pId').value;
				DWREngine.setAsync(false);
				cotOrderOutService.checkIsHaveDetail(idsAry, function(res) {
							if (res.length > 0) {
								cotOrderOutService.deleteDealList(res, mainId,
										function(alt) {
											otherfeeds.reload();
											dealds.reload();
											if (idsAry.length != res.length) {
												Ext.MessageBox
														.alert('提示消息',
																"删除成功!部分应付帐款含有冲帐明细不能删除!");
											} else {
												Ext.MessageBox.alert('提示消息',
														"删除成功!");
											}
										});
							} else {
								Ext.MessageBox.alert('提示消息', "请先删除应付帐款的冲帐明细!");
							}
						});
				DWREngine.setAsync(true);

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

	// 保存其他费用(父页面调用)
	this.saveOther = function() {
		var mainId = parent.$('pId').value;
		// 更改添加action参数
		var urlAdd = '&mainId=' + mainId;

		otherfeeds.proxy.setApi({
					read : "cotorderout.do?method=queryFinanceDeal&fkId="
							+ parent.$('pId').value,
					create : "cotorderout.do?method=addOrderFacOther" + urlAdd,
					update : "cotorderout.do?method=modifyOrderFacOther"
				});
		otherfeeds.save();
	}

	// 保存应收涨
	this.saveAccountdeal = function(mainId) {

		// 更改删除action参数
		var urlDel = '&orderPrimId=' + mainId;

		dealds.proxy.setApi({
					read : "cotorderout.do?method=queryDeal",
					destroy : "cotorderout.do?method=removeDeal" + urlDel
				});
		dealds.save();
	}

	// 判断其他费用名称是否存在
	function checkIsExistOther(id, type, clickType) {
		var source = "";
		if (type == 'other') {
			if (clickType == 'orderfac') {
				source = 'FacOther';
			}
			if (clickType == 'fitorder') {
				source = 'FitOther';
			}
			if (clickType == 'packorder') {
				source = 'PackOther';
			}
		}
		if (type == 'overfee') {
			source = 'yiDeal';
		}
		if (type == 'deal') {
			if (clickType == 'orderfac') {
				source = 'FacDeal';
			}
			if (clickType == 'fitorder') {
				source = 'FitDeal';
			}
			if (clickType == 'packorder') {
				source = 'PackDeal';
			}
		}
		otherfeeds.each(function(rec) {
					if (rec.data.outFlag == id && rec.data.source == source) {
						source = "";
						return false;
					}
				});
		return source;
	}

	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list, type, clickType) {
		if (type == 'other' || type == 'overfee') {
			insertOtherByBatch(list, type, clickType);
		}
		if (type == 'deal') {
			insertDealByBatch(list, type, clickType);
		}
	}

	// 导入一条其他费用到出货其他费用
	function insertOtherByBatch(list, type, clickType) {
		var temp = 0;
		Ext.each(list, function(item) {
					var id = item.data.id;
					var detail = new Object();
					// 添加到表格中
					var flag = checkIsExistOther(id, type, clickType);
					if (flag != "") {
						temp++;
						DWREngine.setAsync(false);
						cotOrderOutService.getCotFinaceOtherById(parseInt(id),
								function(other) {
									if (other != null) {
										detail.factoryId = other.factoryId;
										detail.orderNo = other.orderNo;
										detail.finaceName = other.finaceName;
										if(other.finaceName=='溢付款'){
											detail.amount = other.amount;
										}else{
											detail.amount = other.remainAmount;
											detail.remainAmount = other.remainAmount;
										}
										detail.currencyId = other.currencyId;
										detail.flag = other.flag;
										detail.outFlag = other.id;
										detail.source = flag;
										setObjToGrid(detail);
									}
								})
						DWREngine.setAsync(true);
					}
				});
		if (temp != list.length) {
			if (list.length == 1) {
				Ext.Msg.alert("提示消息", '该记录已被导过,不能重复导入!');
			} else {
				Ext.Msg.alert("提示消息", '导入成功,部分记录不能重复导入!');
			}
		} else {
			Ext.Msg.alert("提示消息", '导入成功!');
		}
	}

	// 导入未冲帐完成的应付帐款到出货其他费用
	function insertDealByBatch(list, type, clickType) {
		var temp = 0;
		Ext.each(list, function(item) {
					var id = item.data.id;
					var flag = checkIsExistOther(id, type, clickType);
					if (flag != "") {
						temp++;
						DWREngine.setAsync(false);
						cotOrderOutService.getOrderFacDealById(parseInt(id),
								function(deal) {
									var detail = new Object();
									detail.orderNo = deal.orderNo;
									detail.factoryId = deal.factoryId;
									detail.finaceName = deal.finaceName;
									detail.currencyId = deal.currencyId;
									detail.remainAmount = deal.zhRemainAmount;
									detail.outFlag = id;
									detail.source = flag;
									if (deal.finaceName == '预付货款') {
										detail.remainAmount = deal.zhRemainAmount;
										detail.amount = deal.zhRemainAmount;
										detail.flag = 'M';
									} else {
										detail.amount = deal.zhRemainAmount;
										detail.flag = 'A';
									}
									// 添加到表格中
									setObjToGrid(detail);
								});
						DWREngine.setAsync(true);
					}
				});
		if (temp != list.length) {
			if (list.length == 1) {
				Ext.Msg.alert("提示消息", '该记录已被导过,不能重复导入!');
			} else {
				Ext.Msg.alert("提示消息", '导入成功,部分记录不能重复导入!');
			}
		} else {
			Ext.Msg.alert("提示消息", '导入成功!');
		}
	}

	// 添加带有数据的record到表格中
	function setObjToGrid(obj) {
		var u = new otherfeeds.recordType(obj);
		otherfeeds.add(u);
	}

	// 合同分配
	function conToAllocate() {
		// 判断主单id是否已保存
		var id = parent.$('pId').value;
		if (id == 'null' || id == '') {
			Ext.Msg.alert("提示框", '请先保存!');
		} else {
			// 通过出货主单编号查找明细,在将明细中的订单主单编号及货号
			cotOrderOutService.checkIsHasFacs(id, function(res) {
				if (res == "") {
					Ext.Msg.alert("提示框", '该出货单还没有对应的采购合同!');
				} else {
					openFullWindow('cotorderout.do?method=conToAllocate&mainId='
							+ $('pId').value);
				}
			});
		}
	}

	// 一次导多条加减费用到应付帐款
	function addMoreByOtherMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}
		// 判断该出货单是否已保存
		var mainId = parent.$('pId').value;
		if (mainId == 'null' || mainId == '') {
			Ext.MessageBox.alert('提示消息', '该出货单还没保存,不能生成应付帐款!');
			return;
		}
		// 判断其他费用是否有保存
		if (otherfeeds.getModifiedRecords().length != 0) {
			Ext.MessageBox.alert('提示消息', '请先保存其他费用,再生成应付帐款!');
			return;
		}

		// 判断有没有可以生成应付款的其他费用
		var flag = false;
		otherfeeds.each(function(item) {
					var status = item.data.status;
					// 过滤掉新增条和已生成的费用
					if (status != "1") {
						flag = true;
						return false;
					}
				});
		if (!flag) {
			Ext.MessageBox.alert('提示消息', '所有加减费用已全部生成应付款!不用重复生成!');
			return;
		}

		Ext.MessageBox.confirm('提示消息', '是否确定将所有加减费用生成应付帐款?', function(btn) {
					if (btn == 'yes') {
						cotOrderOutService.saveDeal(mainId, function(res) {
									if (res) {
										dealds.reload();
										otherfeeds.reload();
										Ext.MessageBox.alert('提示消息', '生成成功!');
									} else {
										Ext.MessageBox.alert('提示消息', '生成失败!');
									}
								});
					}
				});

		// var amount = 0;
		// var flag = true;
		// var list = new Array();// 用于存放统计过的厂家
		// var size = otherfeeds.getTotalCount();
		// for (var i = 0; i < size; i++) {
		// var rec = otherfeeds.getAt(i);
		// var amountA = rec.data.amount;
		// var flagA = rec.data.flag;
		// var orderNoA = rec.data.orderNo;
		// var factoryIdA = rec.data.factoryId;
		// var otherIdA = rec.data.id;
		// var currencyIdA = rec.data.currencyId;
		// if (flagA == '减' || flagA == 'M') {
		// amount = -parseFloat(amountA);
		// } else {
		// amount = parseFloat(amountA);
		// }
		// var equalFlag = false;
		// for (var n = 0; n < list.length; n++) {
		// if (factoryIdA == list[n]) { // 与数组中一个相同即直接跳出
		// equalFlag = true;
		// break;
		// }
		// }
		// if (equalFlag) {
		// continue;
		// }
		// for (var j = i + 1; j < size; j++) {
		// var rec2 = otherfeeds.getAt(j);
		// var amountB = rec.data.amount;
		// var flagB = rec.data.flag;
		// var orderNoB = rec.data.orderNo;
		// var factoryIdB = rec.data.factoryId;
		// var otherIdB = rec.data.id;
		//
		// if (otherIdB != 'null' && otherIdB != '') {
		// // 厂家一样即可
		// // ======(orderNoB == orderNoA && factoryIdB ==
		// // factoryIdA)=====isError
		// if (factoryIdB == factoryIdA) {
		// if (flagB == '减' || flagB == 'M') {
		// amount = parseFloat(amount) - parseFloat(amountB);
		// } else {
		// amount = parseFloat(amount) + parseFloat(amountB);
		// }
		// }
		// } else {
		// flag = false;
		// }
		// }
		//
		// list.push(factoryIdA);
		// addByOtherMoney(otherIdA, amount, factoryIdA);
		// }
		// dealds.reload();
	}

	// 判断应付帐款是否存在
	function checkIsExist(factoryId) {

		var flag = 0;
		dealds.each(function(rec) {
					var finaceName = rec.data.finaceName;
					var dealFactoryId = rec.data.factoryId;
					if (finaceName == '出货应付' && factoryId == dealFactoryId) {
						flag++;
					}
				});
		return flag;
	}

	// 将其他费用添加到应付表格
	function addByOtherMoney(otherIdA, money, factoryId) {
		var flag = checkIsExist(factoryId);
		if (flag == 0) {
			var dealDetail = new CotFinaceAccountdeal();

			dealDetail.finaceName = '出货应付';
			dealDetail.amount = money;
			dealDetail.orderNo = parent.$('orderNo').value;
			dealDetail.fkId = parent.$('pId').value;
			dealDetail.businessPerson = parent.$('businessPerson').value;
			dealDetail.companyId = parent.$('companyId').value;

			var date = parent.$('orderTime').value
			if (date != null && date != '') {
				amountDate = parent.$('orderTime').value;
			} else {
				amountDate = getCurrentDate('yyyy-MM-dd');
			}

			DWREngine.setAsync(false);
			// 生成应付帐款单号
			cotOrderOutService.createDealNo(parseInt(otherIdA), function(res) {
						dealDetail.finaceNo = res;
						// 保存应付帐款
						cotOrderOutService.saveAccountdeal(otherIdA,
								dealDetail, amountDate, function(deal) {
								});
					});
			DWREngine.setAsync(true);
		} else {
			// alert('厂家【'+factoryId+'】已存在出货应付帐款,不能再次添加!');
		}
	}

	// 自动分配出货数量,将生产合同,配件采购,包材采购货款金额导入到其他费用中
	function autoOrderOut() {
		// 判断出货单是否已保存
		var id = parent.$('pId').value;
		if (id == 'null' || id == '') {
			Ext.Msg.alert("提示框", '请先保存出货单!');
		} else {
			DWREngine.setAsync(false);
			cotOrderOutService.autoOrderOut(id, function(res) {
					otherfeeds.reload();
					dealds.reload();
				});
			DWREngine.setAsync(true);
		}
	}

	// 其他费用金额改变
	function changeOtherAmount(newVal) {
		// 查询来源的单据还有多少金额可以导入!
		var source = editRec.data.source;
		if (source == 'FacOther' || source == 'FitOther'
				|| source == 'PackOther' || source == 'FacDeal'
				|| source == 'FitDeal' || source == 'PackDeal'
				|| source == 'yiDeal') {
			DWREngine.setAsync(false);
			var cur = editRec.data.currencyId;
			cotOrderOutService.findMaxMoneyDeal(cur, source,
					editRec.data.outFlag, function(res) {
						if (res != null) {
							var max = 0;
							if (isNaN(editRec.id)) {
								max = res.toFixed(2);
							} else {
								cotOrderOutService.findOldVal(cur, editRec.id,
										function(ov) {
											max = (ov + res).toFixed(2);
										});
							}
							if (newVal > max) {
								editRec.set('amount', max);
							}
							var tip = new Ext.ToolTip({
										title : '提示',
										anchor : 'left',
										html : '来源处剩余金额<font color=red>'
												+ res.toFixed(2)
												+ '</font>,可输入范围(0~' + max
												+ ")!"
									});
							tip.showAt([ckX, ckY]);
						}
					});
			DWREngine.setAsync(true);
		}
	}
});
