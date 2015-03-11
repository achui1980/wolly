Ext.onReady(function() {
	var empData;
	var deptData;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeEnName", function(res) {
				deptData = res;
			});
	DWREngine.setAsync(true);
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "addTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "addPerson"
			}, {
				name : "prsNo"
			}, {
				name : "priceNo"
			}, {
				name : "deptId"
			}, {
				name : 'customerStr'
			}, {
				name : 'factoryStr'
			}, {
				name : 'orderNo'
			}, {
				name : "remark"
			}, {
				name : "status"
			},{
				name:'valDate',
				sortType : timeSortType.createDelegate(this)
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 40,
					status : 0
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotpan.do?method=query"
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
							header : "编号",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Prs No.",
							dataIndex : "prsNo",
							width : 100
						}, {
							header : "Title",
							dataIndex : "priceNo",
							width : 100
						}, {
							header : "Intended client",
							width : 80,
							dataIndex : "customerStr"
						}, {
							header : "Date",
							dataIndex : "addTime",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "d/m/Y");
								}
							}
						}, {
							header : "DealLine",
							dataIndex : "valDate",
							width : 80,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "d/m/Y");
								}
							}
						},{
							header : "Department",
							dataIndex : "deptId",
							width : 80,
							renderer : function(value) {
								return deptData[value];
							}
						}, {
							header : "Prefered manufactorer",
							dataIndex : "factoryStr",
							width : 80
						}, {
							header : "Request by",
							width : 80,
							dataIndex : "addPerson",
							renderer : function(value) {
								return empData[value];
							}
						}, {
							header : "Wolly order refefrence",
							dataIndex : "orderNo",
							width : 120
						}, {
							header : "Comment",
							dataIndex : "remark",
							width : 200
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 40,
				store : ds,
				displayInfo : true,
				displaySize : '5|10|15|20|300|500',
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 制单人
	var addPersonBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'addPerson',
				emptyText : "Request By",
				width : 100,
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 10,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'addPerson'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', addPersonBox, {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 90,
							format : "d/m/Y",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 90,
							format : "d/m/Y",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						},{
							xtype : "datefield",
							emptyText : "Start DeadLine",
							width : 90,
							format : "d/m/Y",
							id : 'startDeadLine',
							vtype : 'daterange',
							endDateField : 'endDealLine',
							isSearchField : true,
							searchName : 'startDeadLine'
						},{
							xtype : "datefield",
							emptyText : "End DeadLine",
							width : 90,
							format : "d/m/Y",
							id : 'endDealLine',
							vtype : 'daterange',
							startDateField : 'startDeadLine',
							isSearchField : true,
							searchName : 'endDealLine'
						}, {
							xtype : "textfield",
							width : 120,
							emptyText : "Intended client",
							isSearchField : true,
							searchName : 'customerStr'
						}, {
							xtype : "textfield",
							width : 160,
							emptyText : "Wolly order refefrence",
							isSearchField : true,
							searchName : 'factoryStr'
						}, {
							xtype : 'combo',
							name : 'state',
							editable : false,
							store : new Ext.data.SimpleStore({
										fields : ["id", "name"],
										data : [[0, 'Status'],
												[1, 'Waiting & Approval'],
												[2, 'Approved']]
									}),
							valueField : "id",
							displayField : "name",
							mode : 'local',
							validateOnBlur : true,
							triggerAction : 'all',
							width : 95,
							listWidth : 200,
							emptyText : 'Status',
							hiddenName : 'state',
							selectOnFocus : true,
							isSearchField : true,
							searchName : 'status'
						}, {
							xtype : 'searchcombo',
							width : 95,
							emptyText : "Title",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : windowopenAdd.createDelegate(this, [0])
						}, '-', {
							text : "Update",
							handler : windowopenMod.createDelegate(this, [null,
											null]),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}, '-', {
							text : "Print",
							handler : showPrint,
							iconCls : "page_print",
							cls : "SYSOP_PRINT"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				title : "Inquiry",
				id : "panGrid",
				stripeRows : true,
				region : 'center',
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : summary,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : true,
					getRowClass : function(record, index) {
						if (record.get("status") == 1) {
							return "x-grid-record-qing";
						} else if (record.get("status") == 2) {
							return "x-grid-record-green";
						} else {
							return "x-grid-record-noShen";
						}
					}
				}
			});

	var colorPnl = new Ext.Panel({
		layout : 'hbox',
		region : 'north',
		height : 28,
		frame : true,
		layoutConfig : {
			// padding : '5',
			// pack : 'center',
			align : 'middle'
		},
		items : [{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:darkgray">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 80,
			html : '<font color=blue>In Progress</font>'
		}, {
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:khaki">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 120,
			html : '<font color=blue>Waiting Approval</font>'
		}, {
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:lightgreen">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 80,
			html : '<font color=blue>Complete</font>'
		}]
	})

	// 第一次显示差额页面.再点不刷新
	var freshFlag = false;
	function loadOutInfo() {
		if (freshFlag == false) {
			var frame = window.frames["outInfo"];
			frame.location.href = "cotpan.do?method=showSupplierPan";
		}
		freshFlag = true;
	}

	// 底部标签页
	var tbl = new Ext.TabPanel({
				region : 'center',
				width : "100%",
				activeTab : 0,
				items : [grid, {
							xtype : 'iframepanel',
							title : "Quotation",
							itemId : 'outInfoRec',
							frameConfig : {
								autoCreate : {
									id : 'outInfo'
								}
							},
							loadMask : {
								msg : 'Loading...'
							},
							listeners : {
								"activate" : function(panel) {
									loadOutInfo();
								}
							}
						}]
			});

	// 构造
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [colorPnl,tbl]
			});
	viewport.doLayout();
	// 表格双击
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"), record.get("typePan"));
			});

	// 批量删除
	function deleteBatch() {
		var list = Ext.getCmp("panGrid").getSelectionModel().getSelections();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", "Please select records");
			return;
		}
		var ary = new Array();
		Ext.each(list, function(item) {
					var status = item.data.orderStatus;
					if (status == 2 && loginEmpId != "admin") {
					} else {
						ary.push(item.id);
					}
				});
		if (ary.length == 0) {
			Ext.MessageBox
					.alert('Message',
							"You choose to order have been approved, can not be deleted!");
			return;
		}

		Ext.MessageBox.confirm('Message',
				'You sure to delete the selected records?', function(btn) {
					if (btn == 'yes') {
						cotPanService.deletePans(ary, function(res) {
									if (res) {
										reloadGrid("panGrid");
										Ext.MessageBox.alert('Message',
												"Deleted successfully!");
									}
								});
					}
				});

	}

	// 新增
	function windowopenAdd(flag) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0) {
			Ext.MessageBox
					.alert("Message", "You do not have permission to add");
			return;
		}
		if (flag == 0 || flag == 2) {
			openFullWindow('cotpan.do?method=showEdit&flag=' + flag);
		} else {
			openFullWindow('cotpan.do?method=showEditHw&flag=' + flag);
		}
	}

	// 显示打印面板
	// var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message",
					'You do not have permission to print！');
			return;
		}

		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		}
		var rec = Ext.getCmp("panGrid").getSelectionModel().getSelected();
		var tp = 'pan';
		if (rec.data.typePan == 1) {
			tp = 'panHd';
		}
		if (rec.data.typePan == 2) {
			tp = 'panHhotx';
		}
		if (rec.data.typePan == 3) {
			tp = 'panHhoHd';
		}
		// if (printWin == null) {
		var printWin = new PrintWin({
					type : tp,
					modal : true
				});
		// }
		// if (!printWin.isVisible()) {
		var po = item.getPosition();
		printWin.setPosition(po[0] - 200, po[1] + 25);
		printWin.show();
		// } else {
		// printWin.hide();
		// }
	};

	// 同时给多个询盘主单的厂家联系人发邮件
	function sendEMail() {
		var recs = sm.getSelections();
		var ary = new Array();
		Ext.each(recs, function(rec) {
					if (rec.data.state != 4) {
						ary.push(rec);
					}
				});
		if (ary.length == 0) {
			Ext.MessageBox.alert("Message", "您所选的询盘单已失效!");
		} else {
			var mailWin = new MailWin({
						recs : ary
					});
			mailWin.show();
		}
	}

});
// 获得勾选的记录
function getIds() {
	var list = Ext.getCmp("panGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
// 打开订单编辑页面
function windowopenMod(obj, typePan) {
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		} else {
			obj = ids[0];
		}
	}
	openFullWindow('cotpan.do?method=showEdit&id=' + obj);
}

// 删除
function del(id, status) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", "You do not have permission to delete");
		return;
	}
	// 判断该单是否已被审核
	if (status == 2 && loginEmpId != "admin") {
		Ext.MessageBox
				.alert("Message",
						"Sorry, this one has already been audited, can not be removed!");
		return;
	}
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('Message', 'Delete this order do?', function(btn) {
				if (btn == 'yes') {
					cotPanService.deletePans(list, function(res) {
								if (res) {
									reloadGrid("panGrid");
									Ext.MessageBox.alert('Message',
											"Deleted successfully!");
								}
							})
				}
			});
}
