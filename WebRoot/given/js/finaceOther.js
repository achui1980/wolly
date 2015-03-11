Ext.onReady(function() {
	DWREngine.setAsync(false);
	var curMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);
	/** ************************应收帐款表格***************************************************** */
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

	// 创建数据源
	var dealds = new Ext.data.Store({
		method : 'post',
		proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotgiven.do?method=queryRecv&source=given&fkId="
								+ parent.$('pId').value
					}
				}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, dealRecord)
	});
	// 创建复选框列
	var dealsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var dealcm = new Ext.grid.ColumnModel({
				defaults : {
					editor : new Ext.form.TextField(),
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
							header : "应收项目名称",
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
							text : "生成应收款",
							handler : addMoreByOtherMoney,
							iconCls : "page_del"
						}, "-", {
							text : "删除",
							handler : deleteByAccount,
							iconCls : "page_del"
						}]
			});
	var dealgrid = new Ext.grid.GridPanel({
				title : "应收帐款",
				anchor : '100% 50%',
				id : "dealGrid",
				stripeRows : true,
				store : dealds, // 加载数据源
				cm : dealcm, // 加载列
				sm : dealsm,
				loadMask : true, // 是否显示正在加载
				tbar : dealtb,
				bbar : dealtoolBar,
				viewConfig : {
					forceFit : true
				}
			});

	var detailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "detailNo"
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
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							url : "cotgiven.do?method=queryRecvDetail"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, detailRecord)
			});
	//加上应收款的选择的记录id	
	detailds.on('beforeload', function() {
				var cod = dealsm.getSelections();
				if(cod.length==1){
					detailds.baseParams.recvId =cod[0].id;
				}else{
					detailds.baseParams.recvId =0;
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
							dataIndex : "detailNo",
							width : 140
						}, {
							header : "项目名称",
							dataIndex : "finaceName",
							width : 180
						}, {
							header : "收款金额",
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
							header : "收款日期",
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
	var dealtb = new Ext.Toolbar({
				items : ['->', {
							text : "删除",
							handler : deleteByRecDetail,
							iconCls : "page_del"
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
				title : "收款记录",
				anchor : '100% 50%',
				id : "detailGrid",
				stripeRows : true,
				store : detailds, // 加载数据源
				cm : detailcm, // 加载列
				sm : detailsm,
				loadMask : true, // 是否显示正在加载
				bbar : detailtoolBar,
				viewConfig : {
					forceFit : true
				}
			});
	var viewport = new Ext.Viewport({
				layout : "anchor",
				items : [dealgrid, detailgrid]
			});
	dealds.load({
				params : {
					start : 0,
					limit : 15
				}
			});
	dealgrid.on("rowclick", function(grid, idx) {
				var record = dealds.getAt(idx);
				detailds.load({
							params : {
								start : 0,
								limit : 15,
								recvId : record.get("id")
							}
						});
			});
	// 生成应收帐款
	function addMoreByOtherMoney() {
		if (parent.$('givenIscheck').value == 2) {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}
		// 1.判断该单是否已保存
		var mainId = parent.$('pId').value;
		if (mainId == 'null' || mainId == '') {
			Ext.MessageBox.alert('提示消息', '该单还没保存,不能生成应收帐款!');
			return;
		}

		//2.验证主单的表单
		var ck = parent.checkParent();
		if (ck == false) {
			return;
		}

		//是否收取样品费用
		var sampleFeeCheck = parent.$("sampleFeeCheck").value;
		//是否收取快递费用
		var checkFee = parent.$("checkFee").value;

		// 3.判断需不需要生成应收费用
		if (checkFee == 0 && sampleFeeCheck == 0) {
			Ext.MessageBox.alert('提示消息', "该单不向客户收取任何费用!");
			return;
		}

		// 4.判断样品单费用和快递费用是否添加
		var flag = 0;
		DWREngine.setAsync(false);
		cotGivenService.findIsExist(mainId, function(res) {
					flag = res;
				});
		DWREngine.setAsync(true);

		if (flag == 3) {
			Ext.MessageBox.alert('提示消息', "该单已生成样品费用和快递费用,不能重复生成!");
			return;
		}

		//样品费用币种
		var curId = parent.$("curId").value;
		if (curId == '') {
			curId = null;
		}
		//快递费用币种
		var currencyId = parent.$("currencyId").value;
		if (currencyId == '') {
			currencyId = null;
		}
		//样品费用
		var sampleFee = parent.$("sampleFee").value;
		//快递费用
		var signTotalPrice = parent.$("signTotalPrice").value;

		if (sampleFeeCheck == 1) {
			if (curId == null) {
				Ext.MessageBox.alert('提示消息', "请选择样品费用的币种");
				return;
			}
			if (sampleFee == '' || sampleFee == 0) {
				Ext.MessageBox.alert('提示消息', "请输入样品费用");
				return;
			}
		}

		if (checkFee == 1) {
			if (currencyId == null) {
				Ext.MessageBox.alert('提示消息', "请选择快递费用的币种");
				return;
			}
			if (signTotalPrice == '' || signTotalPrice == 0) {
				Ext.MessageBox.alert('提示消息', "请输入快递费用");
				return;
			}
		}
		Ext.MessageBox.confirm('提示消息', "是否确定生成应收账款?", function(btn) {
			if (btn == 'yes') {
				cotGivenService.saveRecv(mainId, flag, curId, currencyId,
						sampleFee, signTotalPrice, sampleFeeCheck, checkFee,
						function(res) {
							if (res == false) {
								Ext.MessageBox.alert('提示消息', "生成应收款失败!请联系管理员!");
							} else {
								//是新建后没关闭页面直接添加应收款,这时原来的dealds的url中的pId是null
								dealds.proxy.setApi({
									read : "cotgiven.do?method=queryRecv&source=given&fkId="
											+ parent.$('pId').value
								});
								reloadGrid('dealGrid');
								Ext.MessageBox.alert('提示消息', "生成成功!");
							}
						});
			}
		});

	}
	// 删除应收表格选择的行
	function deleteByAccount() {
		if (parent.$('givenIscheck').value == 1
				|| parent.$('givenIscheck').value == 2) {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限");
			return;
		}
		var idsAry = new Array();
		var list = dealsm.getSelections();
		Ext.each(list, function(record) {
					idsAry.push(record.id);
				})
		// 判断是否有勾选记录
		if (idsAry.length == 0) {
			Ext.MessageBox.alert('提示消息', "请先勾选要删除的记录!");
			return;
		}

		var str = "";
		// 查询应收帐是否有收款记录
		cotGivenService.checkIsShou(idsAry, function(ck) {
					if (ck == false) {
						Ext.MessageBox.confirm('提示消息', '是否确实删除选择的应收帐?',
								function(btn) {
									if (btn == 'yes') {
										cotGivenService.deleteByAccount(idsAry,
												function(res) {
													reloadGrid('dealGrid');
													if (res) {
														Ext.MessageBox
																.alert('提示消息',
																		"删除成功!");
													}
												});
									}
								});
					} else {
						Ext.MessageBox.alert('提示消息', '您选择的应收帐有收款记录,不能删除!');
					}
				});

	}
	// 删除收款记录
	function deleteByRecDetail() {
		if (parent.$('givenIscheck').value == 1
				|| parent.$('givenIscheck').value == 2) {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限");
			return;
		}
		var idsAry = new Array();
		var list = detailsm.getSelections();
		Ext.each(list, function(record) {
					idsAry.push(record.id);
				})
		if (idsAry.length == 0) {
			Ext.MessageBox.alert('提示消息', "请先勾选要删除的记录!");
			return;
		}
		var flag = window.confirm("您是否确定删除选择的收款记录?");
		if (flag) {
			cotGivenService.deleteByRecvDetail(idsAry, function(res) {
						if (res) {
							Ext.MessageBox.alert('提示消息', "删除成功!");
						} else {
							Ext.MessageBox.alert('提示消息', "删除失败!");
						}
						reloadGrid('detailGrid');
					})
		}
	}
});
