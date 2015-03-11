window.onbeforeunload = function() {
	var ds = Ext.getCmp('fitdetailGrid').getStore();
	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
		if (Ext.isIE) {
			if (document.body.clientWidth - event.clientX < 170
					&& event.clientY < 0 || event.altKey) {
				return "采购明细数据有更改,您确定不保存吗?";
			} else if (event.clientY > document.body.clientHeight
					|| event.altKey) { // 用户点击任务栏，右键关闭
				return "采购明细数据有更改,您确定不保存吗?";
			} else { // 其他情况为刷新
			}
		} else if (Ext.isChrome || Ext.isOpera) {
			return "采购明细数据有更改,您确定不保存吗?";
		} else if (Ext.isGecko) {
			window.open("http://www.g.cn")
			var o = window.open("index.do?method=logoutAction");
		}
	}
}

Ext.onReady(function() {
	var fittingMap;
	DWREngine.setAsync(false);
	//加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotFittingsOrderdetail',function(rs){
		fittingMap=rs;
	});
	DWREngine.setAsync(true);
	// 审核状态
	var statusStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '未审核'], [1, '审核不通过'], [2, '审核通过'], [3, '请审核'],
						[9, '不审核']]
			});
	var statusBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				fieldLabel : '审核状态',
				value : 9,
				editable : false,
				disabledClass : 'combo-disabled',
				store : statusStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				disabled : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 11,
				emptyText : '请选择',
				hiddenName : 'orderStatus',
				selectOnFocus : true
			});

	// -----------------远程下拉框-----------------------------------------
	// 供应商
	var facBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryId',
				fieldLabel : "供应商",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				disabledClass : 'combo-disabled',
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 2,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				disabled : true,
				triggerAction : 'all'
			});

	// 厂家联系人
	var facContactBox = new BindCombox({
				cmpId : "facContactId",
				mode : 'local',
				sendMethod : "post",
				dataUrl : "./servlet/DataSelvert?tbname=CotContact",
				fieldLabel : "联系人",
				tabIndex : 7,
				displayField : "contactPerson",
				valueField : "id",
				emptyText : '请选择',
				triggerAction : "all",
				anchor : "100%"
			});

	// 采购公司
	var companyBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCompany",
				valueField : "id",
				fieldLabel : "采购公司",
				tabIndex : 8,
				sendMethod : "post",
				displayField : "companyShortName",
				cmpId : "companyId",
				emptyText : '请选择',
				anchor : "100%"
			});

	// 采购人员
	var empsBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'empId',
				fieldLabel : "采购人员",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 10,
				sendMethod : "post",
				anchor : "100%",
				selectOnFocus : true,
				emptyText : '请选择',
				tabIndex : 12,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// -----------------远程下拉框----over-------------------------------------

	var form = new Ext.form.FormPanel({
				title : "配件采购单基本信息",
				labelWidth : 60,
				labelAlign : "right",
				collapsible : true,
				formId : "orderFitForm",
				region : 'north',
				layout : "form",
				padding : "5px",
				autoHeight : true,
				frame : true,
				listeners : {
					'collapse' : function(pnl) {
						Ext.Element.fly("talPriceDiv").setLeftTop(400, 35);
					},
					'expand' : function(pnl) {
						Ext.Element.fly("talPriceDiv").setLeftTop(400, 133);
					}
				},
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
													xtype : "textfield",
													fieldLabel : "采购单号",
													id : "fittingOrderNo",
													name : "fittingOrderNo",
													tabIndex : 1,
													anchor : "100%"
												}, {
													xtype : "textfield",
													fieldLabel : "订单号",
													disabledClass : 'combo-disabled',
													disabled : true,
													id : "orderNo",
													name : "orderNo",
													tabIndex : 6,
													anchor : "100%"
												}]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.2,
										items : [facBox, facContactBox]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.2,
										items : [{
													xtype : "datefield",
													fieldLabel : "采购日期",
													id : "orderDate",
													name : "orderDate",
													format : "Y-m-d",
													tabIndex : 3,
													anchor : "100%"
												}, companyBox]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.2,
										items : [{
													xtype : "datefield",
													fieldLabel : "交货日期",
													id : "sendDate",
													name : "sendDate",
													format : "Y-m-d",
													tabIndex : 4,
													anchor : "100%"
												}, {
													xtype : "textfield",
													fieldLabel : "交货地点",
													id : "sendAddr",
													name : "sendAddr",
													tabIndex : 9,
													anchor : "100%"
												}]
									}, {
										xtype : "panel",
										title : "",
										layout : "form",
										columnWidth : 0.2,
										items : [{
													xtype : "textfield",
													fieldLabel : "签约地点",
													id : "signAddr",
													name : "signAddr",
													tabIndex : 5,
													anchor : "100%"
												}, empsBox]
									}, {
										xtype : "panel",
										title : "",
										layout : "column",
										columnWidth : 1,
										items : [{
													xtype : "panel",
													layout : "form",
													columnWidth : 0.2,
													items : [statusBox]
												}, {
													xtype : "panel",
													layout : "form",
													columnWidth : 0.8,
													items : [{
																xtype : "textarea",
																fieldLabel : "备注",
																id : "remark",
																name : "remark",
																anchor : "100%",
																tabIndex : 13,
																height : 25
															}, {
																xtype : "hidden",
																id : "orderId",
																name : "orderId"
															}]
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
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			},{
				name : "sortNo"
			}, {
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "fitDesc"
			}, {
				name : "useUnit"
			}, {
				name : "requeirCount"
			}, {
				name : "fitTrans"
			}, {
				name : "orderCount"
			}, {
				name : "buyUnit"
			}, {
				name : "fitPrice",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "totalAmmount",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "rdm"
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
				read : "cotfittingorder.do?method=queryDetail&flag=fitOrderDetail&orderfitId="
						+ $('pId').value,
				update : "cotfittingorder.do?method=modifyDetail",
				destroy : "cotfittingorder.do?method=removeDetail"
			},
			listeners : {
				beforeload : function(store, options) {
					ds.removed = [];
					cotFitOrderService.clearFitNoMap(function(res) {
							});
				},
				exception : function(proxy, type, action, options, res, arg) {
					// 从异常中的响应文本判断是否成功
					if (res.status != 200) {
						Ext.Msg.alert("提示消息", "保存采购明细失败！");
					} else {
						ds.reload();
						// 如果"其他费用"没有展开,直接计算总金额
						if (freshFlag == true) {
							var frame = window.frames["otherFeeInfo"];
							frame.saveOther($('pId').value,
									$('fittingOrderNo').value, 'fitorder', 1,
									$('factoryId').value);
							frame.saveAccountdeal($('pId').value);
						}
					}
					unmask();
				},
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
	var cm = new HideColumnModel({
				defaults : {
					sortable : true
				},
				hiddenCols:fittingMap,
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						},{
							header : "序",
							align : 'center',
							dataIndex : "sortNo",
							menuDisabled : true,
							width : 40,
							editor : new Ext.form.NumberField({
										maxValue : 999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						},  {
							header : "配件编号",
							dataIndex : "fitNo",
							width : 120,
							editor : new Ext.form.TextField({
										maxLength : 50,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "配件名称",
							dataIndex : "fitName",
							width : 120,
							editor : new Ext.form.TextField({
										maxLength : 100,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "规格型号",
							dataIndex : "fitDesc",
							width : 160,
							editor : new Ext.form.TextField({
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "领用单位",
							dataIndex : "useUnit",
							width : 100,
							editor : new Ext.form.TextField({
										maxLength : 10,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "需求数量",
							dataIndex : "requeirCount",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 999999,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "换算率",
							dataIndex : "fitTrans",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 999999,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "采购数量",
							dataIndex : "orderCount",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 999999,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "采购单位",
							dataIndex : "buyUnit",
							width : 100,
							editor : new Ext.form.TextField({
										maxLength : 10,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "采购单价",
							dataIndex : "fitPrice",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 999999,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "总价",
							dataIndex : "totalAmmount",
							width : 100
						}, {
							header : "rdm",
							dataIndex : "rdm",
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0
									}),
							hidden : true
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : [{
							text : "删除货号",
							iconCls : "page_del",
							handler : onDel
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "fitdetailGrid",
				stripeRows : true,
				margins : '0 5 0 0',
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
			
	// 右键菜单
	var rightMenu = new Ext.menu.Menu({
				id : "rightMenu",
				items : [{
							text : "保存当前排序",
							handler : sumSortTable
						}]
			});
	function rightClickFn(client, rowIndex, e) {
		e.preventDefault();
		rightMenu.showAt(e.getXY());
	}
	grid.on("rowcontextmenu", rightClickFn);

	// 右边折叠面板
	var rightForm = new Ext.form.FormPanel({
		autoScroll : true,
		bodyStyle : "overflow-x:hidden;",
		width : "95%",
		formId : "rightForm",
		padding : "5px",
		height : 500,
		labelWidth : 60,
		labelAlign : "right",
		items : [{
			title : '基本信息',
			xtype : "fieldset",
			name : "mainPanel",
			layout : "column",
			items : [{
				xtype : "panel",
				columnWidth : 1,
				layout : 'hbox',
				layoutConfig : {
					align : 'middle',
					pack : 'center'
				},
				buttonAlign : "center",
				items : [{
					xtype : "panel",
					width : 140,
					html : '<div align="center" style="width: 140px; height: 140px;">'
							+ '<img src="common/images/zwtp.png" id="picPath" name="picPath"'
							+ 'onload="javascript:DrawImage(this,140,140)" onclick="showBigPicDiv(this)"/></div>'
				}],
				buttons : [{
							width : 60,
							text : "更改",
							handler : showUploadPanel,
							id : "upmod",
							iconCls : "upload-icon"
						}, {
							width : 60,
							text : "删除",
							handler : delPic,
							id : "updel",
							iconCls : "upload-icon-del"
						}]
			}, {
				xtype : "panel",
				columnWidth : 1,
				layout : "form",
				labelWidth : 60,
				items : [{
							xtype : "textfield",
							fieldLabel : "配件号",
							anchor : "100%",
							tabIndex : 1,
							id : "fitNo",
							name : "fitNo",
							// readOnly : true,
							maxLength : 50
						}, {
							xtype : "textfield",
							fieldLabel : "产品货号",
							anchor : "100%",
							id : "eleId",
							name : "eleId",
							// readOnly : true,
							tabIndex : 2
						}, {
							xtype : "textfield",
							fieldLabel : "配件名称",
							anchor : "100%",
							id : "fitName",
							name : "fitName",
							// readOnly : true,
							tabIndex : 3
						}, {
							xtype : "textfield",
							fieldLabel : "规格型号",
							anchor : "100%",
							id : "fitDesc",
							name : "fitDesc",
							// readOnly : true,
							tabIndex : 4
						}, {
							xtype : "textfield",
							fieldLabel : "质量标准",
							anchor : "100%",
							id : "fitQualityStander",
							name : "fitQualityStander",
							tabIndex : 5
						}, {
							xtype : "textarea",
							fieldLabel : "备注",
							anchor : "100%",
							id : "fitRemark",
							name : "fitRemark",
							tabIndex : 6
						}, {
							xtype : "hidden",
							id : "rdm",
							name : "rdm"
						}, {
							xtype : "hidden",
							id : "id",
							name : "id"
						}]
			}]
		}]
	})

	// 右边折叠面板
	var rightPanel = new Ext.Panel({
		title : '详细信息',
		layout : 'fit',
		frame : true,
		region : 'east',
		width : "30%",
		collapsible : true,
		collapsed : true,
		listeners : {
			'beforeadd' : function(pnl, component, index) {
				pnl.getEl().mask("正在加载中,请稍候...", 'x-mask-loading');
			},
			'afterlayout' : function(pnl) {
				pnl.getEl().unmask();
				// 获取sm选择行
				var selectRec = sm.getSelected();
				if (selectRec != null) {
					init(selectRec);
				}
				// 给表单加事件
				var items = rightForm.getForm().items;
				for (var i = 0; i < items.getCount(); i++) {
					var item = items.get(i);
					// 去掉无名的和一些下拉框
					if (!item.getName())
						continue;
					item.on('change', function(txt, newVal, oldVal) {
						if ($('rdm').value != "") {
							DWREngine.setAsync(false);
							updateMapValue($('rdm').value, txt.getName(),
									newVal);
							DWREngine.setAsync(true);
							// 修改表格数据,如果表格没有这一列,则修改这一行的状态
							ds.each(function(rec) {
										if (rec.data.rdm == $('rdm').value) {
											var cell = rec.data[txt.getName()];
											if (typeof(cell) == 'undefined') {
												if (!isNaN(rec.data.id)) {
													var temp = rec.data.rdm;
													rec
															.set(
																	"rdm",
																	temp
																			+ "aaa");
													rec.set("rdm", temp);
												}
											} else {
												rec.set(txt.getName(), newVal);
											}
											return false;
										}
									});
						}
					});
				}
			},
			'expand' : function(pnl) {
				if (!rightForm.isVisible()) {
					pnl.add(rightForm);
				}
			}
		},
		items : []
	});

	// 图片加载标志
	var imgflag = 0;
	// 行点击时加载右边折叠面板表单
	grid.on("rowclick", function(grid, rowIndex, e) {
				// 如果右边折叠面板有展开过才加载数据
				if (rightForm.isVisible()) {
					init(ds.getAt(rowIndex));
				}
			});

	// 初始化页面,加载报表详细信息
	function init(record) {
		var eleId = record.data.eleId;
		var fitNo = record.data.fitNo;
		var rdm = record.data.rdm;

		var isSelPic = getPdmByOtherUrl("cotpicture.do", "SEL");
		var popdom = false;
		if (isSelPic == 0) {// 没有查看图片信息权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
			popdom = true;
		}
		var isMod = getPopedomByOpType("cotfittingorder.do", "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
		}

		DWREngine.setAsync(false);
		if (fitNo != null && fitNo != "") {
			cotFitOrderService.getFitNoMapValue(rdm, function(res) {
						rightForm.getForm().setValues(res);
						if (popdom == true) {
							res.picPath = "common/images/noElePicSel.png";
						} else {
							res.picPath = "./showPicture.action?detailId="
									+ res.id + "&flag=fitorder&temp="
									+ Math.random();
						}

						DWRUtil.setValue("picPath", res.picPath);
					})
		}
		DWREngine.setAsync(true);

	}

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	grid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 改变值传入对应值
	function setEleFrameValue(name, value) {
		DWRUtil.setValue(name, value);
	}

	// 单元格编辑后
	grid.on("afteredit", function(e) {
				// 需求数量
				if (e.field == 'requeirCount') {
					countCustChange(e.value, e.originalValue);
				}
				// 采购数量
				if (e.field == 'orderCount') {
					countFacChange(e.value, e.originalValue);
				}
				// 单价
				if (e.field == 'fitPrice') {
					priceChange(e.value, e.originalValue);
				}
				DWREngine.setAsync(false);
				cotFitOrderService.updateMapValueByFitNo(e.record.data.rdm,
						e.field, e.value, function(res) {
							setEleFrameValue(e.field, e.value);
						});
				DWREngine.setAsync(true);
			});

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "采购明细",
				items : [grid, rightPanel]
			});
	// 底部标签页
	var tbl = new Ext.TabPanel({
				region : 'south',
				region : 'center',
				width : "100%",
				activeTab : 0,
				defaults : {
					autoScroll : false
				},
				buttonAlign : 'center',
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
							hidden : true,
							iconCls : "page_del"
						}, {
							text : "打印",
							id : "printBtn",
							handler : showPrint,
							iconCls : "page_print"
						}, {
							text : "取消",
							id : "cancelBtn",
							iconCls : "page_cancel",
							handler : closeAndClearMap
						}, {
							text : "请求审核",
							id : 'requestBtn',
							handler : requestCheck,
							iconCls : "page_from"
						}, {
							text : "通过",
							id : 'passBtn',
							handler : passCheck,
							hidden : true,
							iconCls : "page_from"
						}, {
							text : "不通过",
							id : 'unpassBtn',
							hidden : true,
							handler : unpassCheck,
							iconCls : "page_from"
						}, {
							text : "反审",
							id : 'recheckBtn',
							handler : reCheck,
							hidden : true,
							iconCls : "page_from"
						}],
				items : [centerPanel, {
					id : "shenTab",
					name : 'shenTab',
					title : "审核记录",
					layout : 'fit',
					items : [{
						xtype : 'htmleditor',// html文本编辑器控件
						enableLinks : false,
						id : 'checkReason',
						name : 'checkReason',
						listeners : {
							'afterrender' : function(area) {
								if ($('pId').value != ''
										&& $('pId').value != 'null') {
									area.setValue(chkReason);
								}
							}
						}
					}]
				}, {
					xtype : 'iframepanel',
					title : "其他费用",
					itemId : 'otherFeeInfoRec',
					frameConfig : {
						autoCreate : {
							id : 'otherFeeInfo'
						}
					},
					loadMask : {
						msg : 'Loading...'
					},
					listeners : {
						"activate" : function(panel) {
							loadOtherFeeInfo();
						}
					}
				}]
			});
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [tbl, form]
			});
	viewport.doLayout();

	// 审核原因
	var chkReason = "";
	// 初始化
	function initform() {
		$('talPriceDiv').style.display = 'block';
		DWREngine.setAsync(false);
		// 清空FitNoMap
		cotFitOrderService.clearFitNoMap(function(res) {
				})

		// 配件采购单编号
		var id = $('pId').value;

		// 加载配件采购单信息
		if (id != null && id != '') {
			DWREngine.setAsync(false);
			cotFitOrderService.getFitOrderById(parseInt(id), function(res) {
						DWRUtil.setValues(res);
						chkReason = res.checkReason;
						// 初始报价配置值
						cotFitOrderService.getList('CotPriceCfg',
								function(cfg) {
									if (cfg.length != 0) {
										if (res.orderStatus == 9) {
											// 隐藏审核记录及审核相关按钮
											Ext.getCmp('shenTab').disable();
											Ext.getCmp('requestBtn').hide();
										} else {
											if (res.orderStatus == 2) {
												Ext.getCmp('requestBtn').hide();
												Ext.getCmp('recheckBtn')
														.setVisible(true);
											} else {
												Ext.getCmp('requestBtn').hide();
												Ext.getCmp('passBtn')
														.setVisible(true);
												Ext.getCmp('unpassBtn')
														.setVisible(true);
											}
										}
										statusBox.setValue(res.orderStatus);
									}
								});
						// 加载时间
						if (res.orderDate != null && res.orderDate != '') {
							var date = new Date(res.orderDate);
							var orderDate = Ext.getCmp("orderDate");
							orderDate.setValue(date);
						}

						if (res.sendDate != null && res.sendDate != '') {
							var date = new Date(res.sendDate);
							var sendDate = Ext.getCmp("sendDate");
							sendDate.setValue(date);
						}
						// 加载货款金额
						if (res.totalAmmount != null) {
							$('totalLab').innerText = res.totalAmmount
									.toFixed("2");
						} else {
							$('totalLab').innerText = 0;
						}
						// 加载实际金额
						if (res.realMoney != null) {
							$('realLab').innerText = res.realMoney.toFixed("2");
						} else {
							$('realLab').innerText = 0;
						}

						empsBox.bindPageValue("CotEmps", "id", res.empId);
						facBox.bindPageValue("CotFactory", "id", res.factoryId);
						facContactBox.loadValueById('CotContact', 'factoryId',
								res.factoryId, res.facContactId);

						companyBox.bindPageValue("CotCompany", "id",
								res.companyId);

						// 加载配件采购明细信息
						ds.load({
									params : {
										start : 0,
										limit : 15
									}
								});
					});
			DWREngine.setAsync(true);
		}
	}
	unmask();
	initform();

	// 获得表格选择的记录
	function getFitDetailIds() {

		var list = Ext.getCmp("fitdetailGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var detail = new CotFittingsOrderdetail();
					detail.id = item.id;
					res.push(detail);
				});
		return res;
	}
	// 删除
	function onDel() {
		var ids = getFitDetailIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
			return;
		}
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					var rdm = item.data.rdm;
					ds.remove(item);
					cotFitOrderService.delFitNoMapByKey(rdm, function(res) {
							});
				});
		rightForm.getForm().reset();
		$('picPath').src = "./showPicture.action?flag=noPic";
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 打印权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("提示信息", '您没有打印权限！');
			return;
		}

		if (printWin == null) {
			printWin = new PrintWin({
						type : 'fitting',
						pId : 'pId',
						pNo : 'fittingOrderNo',
						mailSendId : 'factoryId',
						status : 'orderStatus'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0], po[1] - 185);
			printWin.show();
		} else {
			printWin.hide();
		}

	};

	// 删除
	function del() {
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0)// 没有删除权限
		{
			Ext.Msg.alert("提示框", "您没有删除权限");
			return;
		}
		var id = $('pId').value;
		if (id == null || id == '') {
			return;
		}

		DWREngine.setAsync(false);
		var dealFlag = false;
		cotFitOrderService.getDealNumById(parseInt(id), function(dealNum) {
					if (dealNum != -1) {
						dealFlag = true;
					}
				});

		if (dealFlag) {
			Ext.Msg.alert("提示框", '对不起，该单已有应付帐款记录，不能删除！');
			return;
		}

		var list = new Array();
		list.push(id);
		Ext.MessageBox.confirm('提示信息', '是否确定删除该配件采购单?', function(btn) {
			if (btn == 'yes') {
				// 查询该主单是否被删除
				cotFitOrderService.getFitOrderById(id, function(res) {
					if (res != null) {
						cotFitOrderService.deleteFitOrderList(list, function(
								res) {
							if (res == 1) {
								Ext.Msg.alert("提示框", "该采购单有应付帐.不能删除!");
							} else if (res == 0) {
								Ext.Msg.alert("提示框", "删除成功");
								closeandreflashEC('true', 'fittingGrid', false);
							} else {
								Ext.Msg.alert("提示框", "删除失败!");
							}
						})
					} else {
						closeandreflashEC('true', 'fittingGrid', false);
					}
				});
			}
		});
		DWREngine.setAsync(true);
	}

	// 关闭页面
	function closeAndClearMap() {
		DWREngine.setAsync(false);
		// 清空FitNoMap
		cotFitOrderService.clearFitNoMap(function(res) {
				})
		closeandreflashEC('true', 'fittingGrid', false);
		DWREngine.setAsync(true);
	}

	// 保存
	function save() {
		var popedom = checkAddMod($('pId').value);
		if (popedom == 1) {
			Ext.MessageBox.alert('提示消息', '对不起,您没有添加权限!请联系管理员!');
			return;
		} else if (popedom == 2) {
			Ext.MessageBox.alert('提示消息', '对不起,您没有修改权限!请联系管理员!');
			return;
		}
		// 审核通过不能修改
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该配件采购单已被审核通过不能再修改!');
			return;
		}

		// 验证表单
		var formData = getFormValues(form, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		//验证单号是否存在
		var shortflag = false;
		var fittingOrderNo = $('fittingOrderNo').value;
		DWREngine.setAsync(false);
		cotFitOrderService.findIsExistFitOrderNo(fittingOrderNo, $('pId').value, function(
				res) {
			if (res != null) {
				shortflag = true;
			}
		});
		if (shortflag) {
			Ext.MessageBox.alert("提示框", "该报价单号已存在，请重新输入！");
			return;
		}
		DWREngine.setAsync(true);
		
		Ext.MessageBox.confirm('提示信息', '是否确定保存该配件采购单？', function(btn) {
			if (btn == 'yes') {
				DWREngine.setAsync(false);

				var fit = DWRUtil.getValues('orderFitForm');
				var cotFittingOrder = new CotFittingOrder();
				for (var p in cotFittingOrder) {
					// && p!='prePrice' && p!='priceScal'到时需去除
					if (p != 'orderDate' && p != 'sendDate' && p != 'prePrice'
							&& p != 'priceScal' && p != 'op') {
						cotFittingOrder[p] = fit[p];
					}
				}
				if ($('pId').value != 'null' && $('pId').value != '') {
					cotFittingOrder.id = $('pId').value;
				}

				// 审核原因
				if (Ext.getCmp("checkReason").isVisible()) {
					cotFittingOrder.checkReason = $('checkReason').value;
				}
				// 金额
				cotFittingOrder.totalAmmount = $('totalLab').innerText;
				cotFittingOrder.realMoney = $('realLab').innerText;
				cotFitOrderService.saveOrUpdate(cotFittingOrder,
						$('orderDate').value, $('sendDate').value,
						function(res) {
							if (res != null) {
								$('pId').value = res;
								if (ds.getModifiedRecords().length == 0
										&& ds.removed.length == 0) {
									// 保存加减费用
									if (freshFlag == true) {
										var frame = window.frames["otherFeeInfo"];
										frame.saveOther($('pId').value,
												$('fittingOrderNo').value,
												'fitorder', 1,
												$('factoryId').value);
										frame.saveAccountdeal($('pId').value);
									}
								} else {
									// 更改修改action参数
									var urlMod = '&fitorderId=' + res;
									ds.proxy.setApi({
										read : "cotfittingorder.do?method=queryDetail&flag=fitOrderDetail&orderfitId="
												+ res,
										update : "cotfittingorder.do?method=modifyDetail"
												+ urlMod,
										destroy : "cotfittingorder.do?method=removeDetail&fitorderId="
												+ res
									});
									ds.save();
								}
								reflashParent('fittingGrid');
								Ext.Msg.alert("提示消息", "保存成功！");
							} else {
								Ext.MessageBox.alert('提示消息', '保存失败');
							}
						});
				DWREngine.setAsync(true);
			}
		});
	}

	// 更新内存数据
	function updateMapValue(rdm, property, value) {
		DWREngine.setAsync(false);
		cotFitOrderService.updateMapValueByFitNo(rdm, property, value,
				function(res) {
				});
		DWREngine.setAsync(true);
	}

	// 需求数量改变事件
	function countCustChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");
		// 换算率
		var fitTrans = rec.get("fitTrans");
		// 单价
		var fitPrice = rec.get("fitPrice");
		var newOrderCount = Math.ceil(newVal / parseFloat(fitTrans));
		rec.set("orderCount", newOrderCount);

		// 更改总价
		var totalAmmount = newOrderCount * parseFloat(fitPrice);

		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + totalAmmount).toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + totalAmmount).toFixed("2");

		rec.set("totalAmmount", totalAmmount);

		updateMapValue(rdm, 'totalAmmount', totalAmmount);
		updateMapValue(rdm, 'orderCount', newOrderCount);

	}

	// 采购数量改变事件
	function countFacChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");
		// 换算率
		var fitTrans = rec.get("fitTrans");
		// 单价
		var fitPrice = rec.get("fitPrice");

		// 更改需求数量
		var requeirCount = newVal * parseFloat(fitTrans);
		rec.set("requeirCount", requeirCount);

		// 更改金额
		var totalAmmount = newVal * parseFloat(fitPrice);
		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + totalAmmount).toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + totalAmmount).toFixed("2");

		rec.set("totalAmmount", totalAmmount);
		updateMapValue(rdm, 'totalAmmount', totalAmmount);
		updateMapValue(rdm, 'requeirCount', requeirCount);
	}

	// 单价改变事件
	function priceChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");
		// 采购数量
		var orderCount = rec.get("orderCount");
		var totalAmmount = Number(newVal).mul(orderCount);

		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + totalAmmount).toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + totalAmmount).toFixed("2");
		rec.set("totalAmmount", totalAmmount);
		updateMapValue(rdm, 'totalAmmount', totalAmmount);
	}

	// 请求审核
	function requestCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotfittingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否请求审核?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(3);
						cotFitOrderService.updateOrderStatus($('pId').value, 3,
								function(res) {
									Ext.getCmp('requestBtn').hide();
									Ext.getCmp('passBtn').setVisible(true);
									Ext.getCmp('unpassBtn').setVisible(true);
									reflashParent("fittingGrid");
								});
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}
	
	// 保存排序
	function sumSortTable() {
		var sort = ds.getSortState();
		if (!sort) {
			Ext.MessageBox.alert("提示消息", "排序没变化,不用再保存!");
			return;
		}
		var fieldType = "";
		ds.each(function(rec) {
					var temp = rec.fields.get(sort.field).type;
					fieldType = temp.type;
					return false;
				});
		if (fieldType == 'auto') {
			fieldType = 'string';
		}
		var type = 0;
		if (sort.direction == 'DESC') {
			type = 1;
		}
		DWREngine.setAsync(false);
		cotFitOrderService.updateSortNo(type, sort.field,
				fieldType, function(res) {
					if (res) {
						ds.reload();
						Ext.MessageBox.alert('提示消息', "保存表格排序成功!");
					} else {
						Ext.MessageBox.alert('提示消息', "保存表格排序失败!");
					}
				});
		DWREngine.setAsync(true);
	}

	// 通过审核
	function passCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotfittingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否通过审核?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(2);
						cotFitOrderService.updateOrderStatus($('pId').value, 2,
								function(res) {
									Ext.getCmp('recheckBtn').setVisible(true);
									Ext.getCmp('passBtn').hide();
									Ext.getCmp('unpassBtn').hide();
									reflashParent("fittingGrid");
								});
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}

	// 不通过审核
	function unpassCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotfittingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '确定不通过审核?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(1);
						cotFitOrderService.updateOrderStatus($('pId').value, 1,
								function(res) {
									Ext.getCmp('requestBtn').setVisible(true);
									Ext.getCmp('passBtn').hide();
									Ext.getCmp('unpassBtn').hide();
									reflashParent("fittingGrid");
								});
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}

	// 反审
	function reCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotfittingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否请求反审?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(0);
						cotFitOrderService.updateOrderStatus($('pId').value, 0,
								function(res) {
									Ext.getCmp('requestBtn').setVisible(true);
									Ext.getCmp('recheckBtn').hide();
									reflashParent("fittingGrid");
								});
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}

	// 打开上传面板
	function showUploadPanel() {
		var fitNo = $('fitNo').value;
		if (fitNo == '') {
			Ext.Msg.alert('提示框', '请选择产品！');
			return;
		}

		var id = $('id').value;
		var opAction = "insert";
		if (id == 'null' || id == '')
			opAction = "insert";
		else
			opAction = "modify";
		var win = new UploadWin({
					params : {
						detailId : id
					},
					waitMsg : "图片上传中......",
					opAction : opAction,
					imgObj : $('picPath'),
					imgUrl : "./showPicture.action?detailId=" + $('id').value
							+ "&flag=fitorder&" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadFitDetailPic.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}

	// 删除图片
	function delPic() {

		var fitNo = $('fitNo').value;
		if (fitNo == '') {
			Ext.Msg.alert('提示框', '请选择产品！');
			return;
		}
		Ext.MessageBox.confirm('提示信息', '是否确定删除图片?', function(btn) {
					if (btn == 'yes') {
						var detailId = $('id').value;
						cotFitOrderService.deletePicImg(detailId,
								function(res) {
									if (res) {
										$('picPath').src = "common/images/zwtp.png";
									} else {
										Ext.Msg.alert('提示框', "对不起，删除图片失败!");
									}
								})
					}
				});
	}

	// 加载其他费用
	// 第一次显示差额页面.再点不刷新
	var freshFlag = false;
	function loadOtherFeeInfo() {
		if (freshFlag == false) {
			var frame = window.frames["otherFeeInfo"];
			frame.location.href = "cotfittingorder.do?method=queryFinanceOther"
					+ "&type=1&fkId=" + $('pId').value;
		}
		freshFlag = true;
	}
});