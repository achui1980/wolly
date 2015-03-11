window.onbeforeunload = function() {
	var ds = Ext.getCmp('packdetailGrid').getStore();
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
	var packingMap;
	DWREngine.setAsync(false);
	//加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotPackingOrderdetail',function(rs){
		packingMap=rs;
	});
	// 加载表格需要关联的外键名
	var boxpackMap;
	baseDataUtil.getBaseDicDataMap("CotBoxPacking", "id", "value",
			function(res) {
				boxpackMap = res;
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
				editable : false,
				value : 9,
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
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory",
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
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&flag=filter",
				cmpId : 'empId',
				fieldLabel : "采购人员",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 10,
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
				title : "包材采购单基本信息",
				labelWidth : 60,
				labelAlign : "right",
				formId : "orderPackForm",
				collapsible : true,
				region : 'north',
				height : 130,
				layout : "form",
				padding : "5px",
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
													id : "packingOrderNo",
													name : "packingOrderNo",
													tabIndex : 1,
													anchor : "100%"
												}, {
													xtype : "textfield",
													fieldLabel : "订单号",
													id : "orderNo",
													name : "orderNo",
													disabled : true,
													disabledClass : 'combo-disabled',
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
													title : "",
													layout : "form",
													columnWidth : 0.2,
													items : [statusBox]
												}, {
													xtype : "panel",
													title : "",
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
			}, {
				name : 'sortNo'
			}, {
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "eleName"
			}, {
				name : "boxObCount"
			}, {
				name : "boxRemark"
			}, {
				name : "boxPackingId"
			}, {
				name : "boxTypeId"
			}, {
				name : "sizeL",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "sizeW",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "sizeH",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "packCount"
			}, {
				name : "packPrice",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "totalAmmount",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "remark"
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
				read : "cotpackingorder.do?method=queryDetail&flag=packOrderDetail&orderpackId="
						+ $('pId').value,
				update : "cotpackingorder.do?method=modifyDetail",
				destroy : "cotpackingorder.do?method=removeDetail"
			},
			listeners : {
				beforeload : function(store, options) {
					ds.removed = [];
					cotPackOrderService.clearPackNoMap(function(res) {
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
									$('packingOrderNo').value, 'packorder', 1,
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
				hiddenCols:packingMap,
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
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
						}, {
							header : "货号",
							dataIndex : "eleId",
							width : 80
						}, {
							header : " 客号",
							dataIndex : "custNo",
							width : 80
						}, {
							header : " 品名",
							dataIndex : "eleName",
							width : 120
						}, {
							header : " 包装率",
							dataIndex : "boxObCount",
							width : 60
						},

						{
							header : " 包装要求",
							dataIndex : "boxRemark",
							width : 200
						}, {
							header : "包装名称",
							dataIndex : "boxPackingId",
							width : 70,
							renderer : function(value) {
								return boxpackMap[value];
							}
						}, {
							header : "包装类型",
							dataIndex : "boxTypeId",
							width : 70,
							renderer : function(value) {
								if (value == 0) {
									return '产品包装'
								}
								if (value == 1) {
									return '内盒包装'
								}
								if (value == 2) {
									return '中盒包装'
								}
								if (value == 3) {
									return '外箱包装'
								}
								if (value == 4) {
									return "插格包装";
								}
							}
						}, {
							header : "<font color='red'>长</font>",
							dataIndex : "sizeL",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 99999,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color='red'>宽</font>",
							dataIndex : "sizeW",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 99999,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color='red'>高</font>",
							dataIndex : "sizeH",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 99999,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color='red'>采购数量</font>",
							dataIndex : "packCount",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 999999,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color='red'>单价</font>",
							dataIndex : "packPrice",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 9999999999,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "金额",
							dataIndex : "totalAmmount",
							width : 100
						}, {
							header : "<font color='red'>备注</font>",
							dataIndex : "remark",
							width : 180,
							editor : new Ext.form.TextArea({
										maxLength : 500
									})
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
				id : "packdetailGrid",
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

	// 单元格点击后,记住当前行
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				// 获得view
				var view = grid.getView();
				// 获得单元格
				var cell = view.getCell(rowIndex, columnIndex);
				// 获得该行高度
				var row = view.getRow(rowIndex);
				var editor = cm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(cell.offsetWidth, row.scrollHeight);
			});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	grid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后
	grid.on("afteredit", function(e) {
				// 长
				if (e.field == 'sizeL') {
					sizeLChange(e.value, e.originalValue);
				}
				// 宽
				if (e.field == 'sizeW') {
					sizeWChange(e.value, e.originalValue);
				}
				// 高
				if (e.field == 'sizeH') {
					sizeHChange(e.value, e.originalValue);
				}
				// 采购数量
				if (e.field == 'packCount') {
					countChange(e.value, e.originalValue);
				}
				// 单价
				if (e.field == 'packPrice') {
					priceChange(e.value, e.originalValue);
				}
				DWREngine.setAsync(false);
				cotPackOrderService.updateMapValueByPackNo(e.record.data.rdm,
						e.field, e.value, function(res) {
						});
				DWREngine.setAsync(true);
			});

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "采购明细",
				items : [grid]
			});

	// 唛图片
	var maiPicPanel = new Ext.Panel({
		layout : 'fit',
		region : 'west',
		width : "30%",
		border : false,
		frame : true,
		items : [{
			xtype : "fieldset",
			bodyStyle : 'padding-left:25px',
			title : "唛标图片",
			layout : "hbox",
			labelWidth : 60,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			buttons : [{
						text : '',
						hidden : true
					}],
			items : [{
				xtype : "panel",
				width : 200,
				buttonAlign : "center",
				html : '<div align="center" style="width: 180px; height: 180px;">'
						+ '<img src="common/images/zwtp.png" id="pack_MB" name="pack_MB"'
						+ 'onload="javascript:DrawImage(this,180,180)" onclick="showBigPicDiv(this)"/></div>',
				buttons : [{
							width : 60,
							text : "更改",
							id : "upmodMai",
							iconCls : "upload-icon",
							handler : showUploadMaiPanel
						}, {
							width : 60,
							text : "删除",
							id : "updelMai",
							iconCls : "upload-icon-del",
							handler : delMBPic
						}]
			}]
		}]
	});

	// 正册唛信息
	var maiInfoPanel = new Ext.form.FormPanel({
		layout : 'form',
		region : 'center',
		border : false,
		items : [{
					layout : 'hbox',
					anchor : "100% 50%",
					border : false,
					layoutConfig : {
						align : 'stretch'
					},
					items : [{
								title : "正唛",
								flex : 1,
								xtype : 'panel',
								layout : 'fit',
								margins : '0 5 0 0',
								border : false,
								items : [{
											xtype : 'textarea',
											id : "orderZMArea",
											name : "orderZMArea",
											maxLength : 500,
											listeners : {
												'afterrender' : function(area) {
													area.setValue(orderZMArea);
												}
											}
										}]
							}, {
								title : "侧唛",
								flex : 1,
								layout : 'fit',
								border : false,
								xtype : 'panel',
								items : [{
											xtype : 'textarea',
											id : "orderCMArea",
											name : "orderCMArea",
											maxLength : 500,
											listeners : {
												'afterrender' : function(area) {
													area.setValue(orderCMArea);
												}
											}
										}]
							}]
				}, {
					layout : 'hbox',
					anchor : "100% 50%",
					border : false,
					layoutConfig : {
						align : 'stretch'
					},
					items : [{
								title : "中盒唛",
								flex : 1,
								layout : 'fit',
								xtype : 'panel',
								margins : '0 5 0 0',
								border : false,
								items : [{
											xtype : 'textarea',
											id : "orderZHMArea",
											name : "orderZHMArea",
											maxLength : 500,
											listeners : {
												'afterrender' : function(area) {
													area.setValue(orderZHMArea);
												}
											}
										}]
							}, {
								title : "内盒唛",
								flex : 1,
								layout : 'fit',
								xtype : 'panel',
								border : false,
								items : [{
											xtype : 'textarea',
											id : "orderNMArea",
											name : "orderNMArea",
											maxLength : 500,
											listeners : {
												'afterrender' : function(area) {
													area.setValue(orderNMArea);
												}
											}
										}]
							}]
				}]
	});

	var tbMai = new Ext.Toolbar({
				items : [{
							text : "从订单导入唛头信息",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : copyMb
						}, '-']
			});

	// 唛明细
	var maiPanel = new Ext.Panel({
				layout : 'border',
				tbar : tbMai,
				border : false,
				items : [maiPicPanel, maiInfoPanel]
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
					id : "maiTab",
					name : "maiTab",
					title : "唛明细",
					layout : 'fit',
					items : [maiPanel]
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

	// 编辑页面时加载审核原因
	tbl.on('tabchange', function(tb, pnl) {
				if (pnl.name == 'maiTab') {
					// 加载麦标
					var id = $('pId').value;
					$('pack_MB').src = "./showPicture.action?flag=packMB&detailId="
							+ id + "&temp=" + Math.random();
				}
			});

	// 审核原因
	var chkReason = "";

	// 正唛
	var orderZMArea = "";
	// 侧唛
	var orderCMArea = "";
	// 中唛
	var orderZHMArea = "";
	// 内唛
	var orderNMArea = "";

	// 初始化
	function initform() {
		$('talPriceDiv').style.display = 'block';
		DWREngine.setAsync(false);
		// 清空orderPackMap
		cotPackOrderService.clearPackNoMap(function(res) {
				});

		// 包材采购单编号
		var id = $('pId').value;

		// 加载包材采购单信息
		if (id != null && id != '') {

			DWREngine.setAsync(false);
			cotPackOrderService.getPackOrderById(parseInt(id), function(res) {
						DWRUtil.setValues(res);
						chkReason = res.checkReason;
						orderZMArea = res.orderZM;
						orderZHMArea = res.orderZHM;
						orderCMArea = res.orderCM;
						orderNMArea = res.orderNM;
						// 初始报价配置值
						cotPackOrderService.getList('CotPriceCfg',
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
						if (res.totalAmount != null) {
							$('totalLab').innerText = res.totalAmount
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
	function getPackDetailIds() {
		var list = Ext.getCmp("packdetailGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var detail = new CotPackingOrderdetail();
					detail.id = item.id;
					res.push(detail);
				});
		return res;
	}
	// 删除
	function onDel() {
		var ids = getPackDetailIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
			return;
		}
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					var rdm = item.data.rdm;
					ds.remove(item);
					cotPackOrderService.delPackNoMapByKey(rdm, function(res) {
							});
				});

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
		// 如果该单没保存不能打印
		if ($('pId').value == 'null' || $('pId').value == '') {
			Ext.MessageBox.alert("提示信息", '该单还没有保存,不能打印！');
			return;
		}

		if (printWin == null) {
			printWin = new PrintWin({
						type : 'packing',
						pId : 'pId',
						pNo : 'packingOrderNo',
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
		cotPackOrderService.updateSortNo(type, sort.field, fieldType, function(
						res) {
					if (res) {
						ds.reload();
						Ext.MessageBox.alert('提示消息', "保存表格排序成功!");
					} else {
						Ext.MessageBox.alert('提示消息', "保存表格排序失败!");
					}
				});
		DWREngine.setAsync(true);
	}

	// 删除
	function del() {
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.Msg.alert("提示框", "您没有删除权限");
			return;
		}
		var id = $('pId').value;
		if (id == null || id == '') {
			return;
		}

		DWREngine.setAsync(false);
		var dealFlag = false;
		cotPackOrderService.getDealNumById(parseInt(id), function(dealNum) {
					if (dealNum != -1) {
						dealFlag = true;
					}
				});

		if (dealFlag) {
			Ext.Msg.alert("提示框", '对不起，该单已有应付帐款记录，不能删除！');
			return;
		}

		var cotPackingOrder = new CotPackingOrder();
		var list = new Array();
		list.push(id);
		Ext.MessageBox.confirm('提示信息', '是否确定删除该包材采购单?', function(btn) {
			if (btn == 'yes') {
				// 查询该主单是否被删除
				cotPackOrderService.getPackOrderById(id, function(res) {
					if (res != null) {
						cotPackOrderService.deletePackOrderList(list, function(
								res) {
							if (res == 1) {
								Ext.Msg.alert("提示框", "该采购单有应付帐.不能删除!");
							} else if (res == 0) {
								Ext.Msg.alert("提示框", "删除成功");
								closeandreflashEC('true', 'packingGrid', false);
							} else {
								Ext.Msg.alert("提示框", "删除失败!");
							}
						})
					} else {
						closeandreflashEC('true', 'packingGrid', false);
					}
				});
			}
		});
		DWREngine.setAsync(true);
	}

	// 关闭页面
	function closeAndClearMap() {
		DWREngine.setAsync(false);
		// 清空Map
		cotPackOrderService.clearPackNoMap(function(res) {
				});

		closeandreflashEC('true', 'packingGrid', false);
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
			Ext.MessageBox.alert('提示消息', '对不起,该包材单已被审核通过不能再修改!');
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
		var packingOrderNo = $('packingOrderNo').value;
		DWREngine.setAsync(false);
		cotPackOrderService.findIsExistPackOrderNo(packingOrderNo, $('pId').value, function(
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
		
		Ext.MessageBox.confirm('提示信息', '是否确定保存该包材采购单？', function(btn) {
			if (btn == 'yes') {
				var orderpack = DWRUtil.getValues('orderPackForm');
				var cotPackingOrder = {};
				// 如果编号存在时先查询出对象,再填充表单
				if ($('pId').value != 'null' && $('pId').value != '') {
					DWREngine.setAsync(false);
					cotPackOrderService.getPackOrderById($('pId').value,
							function(res) {
								for (var p in res) {
									if (p != 'orderDate' && p != 'sendDate') {
										res[p] = orderpack[p];
									}
								}
								cotPackingOrder = res;
								cotPackingOrder.id = $('pId').value;
							});
					DWREngine.setAsync(true);
				} else {
					cotPackingOrder = new CotPackingOrder();
					for (var p in cotPackingOrder) {
						if (p != 'orderDate' && p != 'sendDate') {
							cotPackingOrder[p] = orderpack[p];
						}
					}
				}
				DWREngine.setAsync(false);
				// 审核原因
				if (Ext.getCmp("checkReason").isVisible()) {
					cotPackingOrder.checkReason = $('checkReason').value;
				}
				// 唛标
				if (Ext.getCmp("orderZMArea").isVisible()) {
					cotPackingOrder.orderZM = $('orderZMArea').value;
					cotPackingOrder.orderCM = $('orderCMArea').value;
					cotPackingOrder.orderZHM = $('orderZHMArea').value;
					cotPackingOrder.orderNM = $('orderNMArea').value;
				}
				// 金额
				cotPackingOrder.totalAmount = $('totalLab').innerText;
				cotPackingOrder.realMoney = $('realLab').innerText;
				cotPackOrderService.saveOrUpdate(cotPackingOrder,
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
												$('packingOrderNo').value,
												'packorder', 1,
												$('factoryId').value);

										frame.saveAccountdeal($('pId').value);
									}
								} else {
									// 更改修改action参数
									var urlMod = '&packorderId=' + res;
									ds.proxy.setApi({
										read : "cotpackingorder.do?method=queryDetail&flag=packOrderDetail&orderpackId="
												+ res,
										update : "cotpackingorder.do?method=modifyDetail"
												+ urlMod,
										destroy : "cotpackingorder.do?method=removeDetail&packorderId="
												+ res
									});
									ds.save();
								}
								reflashParent('packingGrid');
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
		cotPackOrderService.updateMapValueByPackNo(rdm, property, value,
				function(res) {
				});
		DWREngine.setAsync(true);
	}

	// 单价改变事件
	function priceChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");
		// 需求数量
		var packCount = rec.get("packCount");
		// 更改单价计算总价
		var totalAmmount = (Number(packCount).mul(newVal)).toFixed(2);
		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");
		rec.set("totalAmmount", totalAmmount);
		updateMapValue(rdm, 'totalAmmount', totalAmmount);
	}

	// 采购数量改变事件
	function countChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");
		// 单价
		var packPrice = rec.get("packPrice");
		// 更改单价计算总价
		var totalAmmount = (Number(newVal).mul(packPrice)).toFixed(2);

		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");
		rec.set("totalAmmount", totalAmmount);
		updateMapValue(rdm, 'totalAmmount', totalAmmount);
	}

	// 规格长改变事件
	function sizeLChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");

		// 规格长
		var sizeW = rec.get("sizeW");
		// 规格高
		var sizeH = rec.get("sizeH");

		// 需求数量
		var packCount = rec.get("packCount");
		// 金额
		var totalAmmount = rec.get("totalAmmount");
		var id = rec.get("id");
		var onePrice = 0;
		DWREngine.setAsync(false);
		cotPackOrderService.calOnePrice(parseInt(id), parseFloat(newVal),
				parseFloat(sizeW), parseFloat(sizeH), function(res) {
					onePrice = res;
				});
		DWREngine.setAsync(true);
		rec.set("packPrice", onePrice.toFixed(2));
		// 更改单价计算总价
		var totalAmmount = (Number(packCount).mul(onePrice)).toFixed(2);

		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");

		rec.set("totalAmmount", totalAmmount);

		updateMapValue(rdm, 'totalAmmount', totalAmmount);
		updateMapValue(rdm, 'packPrice', onePrice);
	}

	// 规格宽改变事件
	function sizeWChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");
		// 规格长
		var sizeL = rec.get("sizeL");
		// 规格高
		var sizeH = rec.get("sizeH");
		// 需求数量
		var packCount = rec.get("packCount");
		var id = rec.get("id");
		var onePrice = 0;
		DWREngine.setAsync(false);
		cotPackOrderService.calOnePrice(parseInt(id), parseFloat(sizeL),
				parseFloat(newVal), parseFloat(sizeH), function(res) {
					onePrice = res;
				});
		DWREngine.setAsync(true);
		rec.set("packPrice", onePrice.toFixed(2));
		// 更改单价计算总价
		var totalAmmount = (Number(packCount).mul(onePrice)).toFixed(2);

		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");

		rec.set("totalAmmount", totalAmmount);

		updateMapValue(rdm, 'totalAmmount', totalAmmount);
		updateMapValue(rdm, 'packPrice', onePrice);
	}

	// 规格高改变事件
	function sizeHChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.get("rdm");
		// 规格长
		var sizeL = rec.get("sizeL");
		// 规格宽
		var sizeW = rec.get("sizeW");
		// 需求数量
		var packCount = rec.get("packCount");
		var id = rec.get("id");
		var onePrice = 0;
		DWREngine.setAsync(false);
		cotPackOrderService.calOnePrice(parseInt(id), parseFloat(sizeL),
				parseFloat(sizeW), parseFloat(newVal), function(res) {
					onePrice = res;
				});
		DWREngine.setAsync(true);
		rec.set("packPrice", onePrice.toFixed(2));
		// 更改单价计算总价
		var totalAmmount = (parseInt(packCount) * parseFloat(onePrice))
				.toFixed(2);

		$('totalLab').innerText = (parseFloat($('totalLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");
		$('realLab').innerText = (parseFloat($('realLab').innerText)
				- rec.data.totalAmmount + parseFloat(totalAmmount))
				.toFixed("2");

		rec.set("totalAmmount", totalAmmount);

		updateMapValue(rdm, 'totalAmmount', totalAmmount);
		updateMapValue(rdm, 'packPrice', onePrice);
	}

	// 请求审核
	function requestCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotpackingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否请求审核?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(3);
						cotPackOrderService.updateOrderStatus($('pId').value,
								3, function(res) {
									Ext.getCmp('requestBtn').hide();
									Ext.getCmp('passBtn').setVisible(true);
									Ext.getCmp('unpassBtn').setVisible(true);
									reflashParent("packingGrid");
								});
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}

	// 通过审核
	function passCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotpackingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否通过审核?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(2);
						cotPackOrderService.updateOrderStatus($('pId').value,
								2, function(res) {
									Ext.getCmp('recheckBtn').setVisible(true);
									Ext.getCmp('passBtn').hide();
									Ext.getCmp('unpassBtn').hide();
									reflashParent("packingGrid");
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
		var isPopedom = getPopedomByOpType('cotpackingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '确定不通过审核?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(1);
						cotPackOrderService.updateOrderStatus($('pId').value,
								1, function(res) {
									Ext.getCmp('requestBtn').setVisible(true);
									Ext.getCmp('passBtn').hide();
									Ext.getCmp('unpassBtn').hide();
									reflashParent("packingGrid");
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
		var isPopedom = getPopedomByOpType('cotpackingorder.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否请求反审?', function(btn) {
					if (btn == 'yes') {
						statusBox.setValue(0);
						cotPackOrderService.updateOrderStatus($('pId').value,
								0, function(res) {
									Ext.getCmp('requestBtn').setVisible(true);
									Ext.getCmp('recheckBtn').hide();
									reflashParent("packingGrid");
								});
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}

	// 打开上传面板,用于上次唛标
	function showUploadMaiPanel() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}
		var id = $('pId').value;
		var win = new UploadWin({
					params : {
						mainId : id,
						mbType : "2"
					},
					waitMsg : "图片上传中......",
					opAction : "modify",
					imgObj : $('pack_MB'),
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=packMB&temp=" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadMBPic.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}

	// 删除唛标图片
	function delMBPic() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!若要更改请反审!');
			return;
		}
		Ext.MessageBox.confirm('提示信息', "您是否确定删除该唛标?", function(btn) {
					if (btn == 'yes') {
						var pId = $('pId').value;
						cotPackOrderService.deleteMBPicImg(pId, function(res) {
									if (res) {
										$('pack_MB').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox.alert('提示消息', '删除唛标失败！');
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
			frame.location.href = "cotpackingorder.do?method=queryFinanceOther"
					+ "&type=1&fkId=" + $('pId').value;
		}
		freshFlag = true;
	}

	// 从订单拷贝唛头信息
	function copyMb() {
		Ext.MessageBox.confirm('提示信息', "您是否确定从订单导入唛头信息?", function(btn) {
					if (btn == 'yes') {
						var pId = $('pId').value;
						cotPackOrderService.updatePackMb(pId, function(res) {
									$('pack_MB').src = "./showPicture.action?detailId="
											+ pId
											+ "&flag=packMB&temp="
											+ Math.random();
									$('orderCMArea').value = res[0];
									$('orderZMArea').value = res[1];
									$('orderZHMArea').value = res[2];
									$('orderNMArea').value = res[3];

									orderCMArea = res[0];
									orderZMArea = res[1];
									orderZHMArea = res[2];
									orderNMArea = res[3];

									Ext.MessageBox.alert('提示消息', '导入成功！');
								});
					}
				});

	}
});