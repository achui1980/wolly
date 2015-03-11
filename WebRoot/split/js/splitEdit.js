window.onbeforeunload = function() {
	var ds = Ext.getCmp('splitdetailGrid').getStore();
	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
		if (Ext.isIE) {
			if (document.body.clientWidth - event.clientX < 170
					&& event.clientY < 0 || event.altKey) {
				return "排载明细数据有更改,您确定不保存吗?";
			} else if (event.clientY > document.body.clientHeight
					|| event.altKey) { // 用户点击任务栏，右键关闭
				return "排载明细数据有更改,您确定不保存吗?";
			} else { // 其他情况为刷新
			}
		} else if (Ext.isChrome || Ext.isOpera) {
			return "排载明细数据有更改,您确定不保存吗?";
		} else if (Ext.isGecko) {
			window.open("http://www.g.cn")
			var o = window.open("index.do?method=logoutAction");
		}
	}
}

Ext.onReady(function() {
	// 全局变量
	var flag = -1;

	// -----------------远程下拉框-----------------------------------------
	// 绑定集装箱体积
	var selectBind = function() {
		var id = $('containerType').value;
		if (id == '' || id == null) {
			$('containerCube').value = '';
			return;
		}
		DWREngine.setAsync(false);
		cotSplitService.getContainerCubeById(id, function(res) {
					if (res != null) {
						$('containerCube').value = res.containerCube;
					}
				});
		DWREngine.setAsync(true);
	};

	// 集装箱类型
	var containerBox = new BindCombox({
				cmpId : 'containerType',
				dataUrl : "./servlet/DataSelvert?tbname=CotContainerType",
				emptyText : '请选择',
				fieldLabel : "<font color='red'>集装箱类型</font>",
				displayField : "containerName",
				valueField : "id",
				triggerAction : "all",
				allowBlank : false,
				blankText : "请选择集装箱类型！",
				anchor : "100%",
				tabIndex : 5,
				listeners : {
					"select" : selectBind
				}
			});

	// 出货单据
	var orderoutBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotOrderOut&flag=filter",
				cmpId : 'orderOutId',
				fieldLabel : "<font color='red'>发票编号</font>",
				editable : true,
				valueField : "id",
				displayField : "orderNo",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				allowBlank : false,
				blankText : "请选择发票编号！",
				tabIndex : 2,
				selectOnFocus : true,
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// -----------------远程下拉框----over-------------------------------------

	var form = new Ext.form.FormPanel({
		title : "排载单基本信息-(红色为必填项)",
		labelWidth : 60,
		labelAlign : "right",
		formId : "splitForm",
		region : 'north',
		layout : "form",
		padding : "5px",
		autoHeight : true,
		autoWidth : true,
		frame : true,
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
					handler : closeAndClearSplitMap
				}],
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.18,
						labelWidth : 60,
						items : [{
									xtype : "datefield",
									fieldLabel : "<font color='red'>排载日期</font>",
									anchor : "100%",
									allowBlank : false,
									blankText : "请选择排载日期!",
									id : "splitDate",
									name : "splitDate",
									format : 'Y-m-d',
									tabIndex : 1
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.2,
						labelWidth : 60,
						items : [orderoutBox]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						labelWidth : 60,
						columnWidth : 0.18,
						items : [{
									xtype : "textfield",
									fieldLabel : "<font color='red'>排载编号</font>",
									anchor : "100%",
									allowBlank : false,
									blankText : "请输入排载编号!",
									id : "containerNo",
									name : "containerNo",
									tabIndex : 3,
									maxLength : 50,
									maxLengthText : "50"
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.17,
						labelWidth : 55,
						items : [{
									xtype : "textfield",
									fieldLabel : "封签号",
									anchor : "100%",
									allowBlank : true,
									id : "labelNo",
									name : "labelNo",
									tabIndex : 4,
									maxLength : 20,
									maxLengthText : "20"
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.17,
						labelWidth : 70,
						items : [containerBox]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.1,
						labelWidth : 40,
						items : [{
									xtype : "numberfield",
									fieldLabel : "<font color='red'>容积</font>",
									anchor : "100%",
									allowBlank : false,
									blankText : "容积不能为空!",
									id : "containerCube",
									name : "containerCube",
									tabIndex : 6,
									maxValue : 99999999.99
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "form",
						labelWidth : 60,
						items : [{
									xtype : "textarea",
									fieldLabel : "备注",
									anchor : "100%",
									id : "splitRemark",
									name : "splitRemark",
									allowBlank : true,
									tabIndex : 7,
									maxLength : 200,
									maxLengthText : "200"
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
			},/* 固定 */
			{
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "orderDetailId"
			}, {
				name : "eleNameEn"
			}, {
				name : "totalBoxCount"
			}, {
				name : "totalContainerCont"
			}, {
				name : "containerCount"
			}, {
				name : "boxCount"
			}, {
				name : "detailRemark"
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
						read : "cotsplitinfo.do?method=querySplitDetail&splitId="
								+ $('pId').value + "&flag=splitDetail",
						create : "cotsplitinfo.do?method=addSplitDetail",
						update : "cotsplitinfo.do?method=modifySplitDetail",
						destroy : "cotsplitinfo.do?method=removeSplitDetail"
					},
					listeners : {
						beforeload : function(store, options) {
							ds.removed = [];
							cotSplitService.clearSplitMap(function(res) {
									});
						},
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								ds.reload();
							}
							unmask();
						},
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							if(action == "destory") return;
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
			sortable : true
		},
		columns : [sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "货号",
					dataIndex : "eleId",
					width : 120
				}, {
					header : " 客号",
					dataIndex : "custNo",
					width : 120
				}, {
					header : " 英文品名",
					dataIndex : "eleNameEn",
					width : 200
				}, {
					header : " 发票总数量",
					dataIndex : "totalBoxCount",
					width : 120
				},

				{
					header : " 发票总箱数",
					dataIndex : "totalContainerCont",
					width : 120
				}, {
					header : "<font color='red'>本柜箱数</font>",
					dataIndex : "containerCount",
					width : 120,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (value == record.data.totalContainerCont) {
							return "<span style='color:green;font-weight:bold;'>"
									+ value + "</span>";
						} else {
							return "<span style='color:red;font-weight:bold;'>"
									+ value + "</span>";
						}
					},
					editor : new Ext.form.NumberField({
								maxValue : 99999999,
								allowDecimals : false,
								listeners : {
									"change" : function(txt, newVal, oldVal) {
										containerCountChange(txt, newVal,oldVal);
									}
								}
							})
				}, {
					header : "<font color='red'>本柜数量</font>",
					dataIndex : "boxCount",
					width : 120,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (value == record.data.totalBoxCount) {
							return "<span style='color:green;font-weight:bold;'>"
									+ value + "</span>";
						} else {
							return "<span style='color:red;font-weight:bold;'>"
									+ value + "</span>";
						}
					},
					editor : new Ext.form.NumberField({
								maxValue : 99999999,
								allowDecimals : false,// 是否允许输入小数
								listeners : {
									"change" : function(txt, newVal, oldVal) {
										boxCountChange(txt, newVal, oldVal);
									}
								}
							})
				}, {
					header : "<font color='red'>备注</font>",
					dataIndex : "detailRemark",
					width : 450,
					editor : new Ext.form.TextArea({
								maxLength : 500
							})
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
							text : "添加货号",
							iconCls : "page_add",
							handler : showImportPanel
						}, '-', {
							text : "删除货号",
							iconCls : "page_del",
							handler : onDel
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "splitdetailGrid",
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
			
	//表格编辑前储存该行,用于editor中的一些事件处理		
	var editRec = {};
	grid.on('beforeedit', function(e) {
				editRec = e.record;
			});

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

	// 单元格编辑后
	grid.on("afteredit", function(e) {

				cotSplitService.updateMapValueByEleId(
						e.record.data.orderDetailId, e.field, e.value,
						function(res) {
						});

			});

	// 本柜箱数改变
	function containerCountChange(txt, newVal, oldVal) {

		var rec = editRec;
		var oldBoxCount = rec.get("boxCount");
		// 每箱的数量
		var count = rec.get("totalBoxCount");
		var container = rec.get("totalContainerCont");
		var res = Math.ceil(count / container);// 每箱的数量

		// 查看剩余数量
		var orderDetailId = rec.get("orderDetailId");
		var orderOutId = $('orderId').value;
		var splitId = $('pId').value;
		if (splitId == 'null' || splitId == '') {
			splitId = -1;
		}

		DWREngine.setAsync(false);
		cotSplitService.getRemainCountAndExist(splitId, orderOutId,
				orderDetailId, function(outcount) {// res为剩余数量+已出货数量
					if (parseInt(outcount) / parseInt(res) < newVal) {
						Ext.Msg.alert("提示框", "未出货数量已不足！");
						txt.setValue(oldVal);
						return;
					} else {
						var boxCount = res * newVal;
						rec.set("boxCount", boxCount);
						cotSplitService.getBoxCBM(orderOutId, orderDetailId,
								function(cbm) {
									var temp = parseFloat($('totalLab').innerText);

									$('totalLab').innerText = (parseFloat($('totalLab').innerText)
											- oldVal * cbm + newVal * cbm)
											.toFixed('2');

									var containerCube = $('containerCube').value
									var totalCbm = parseFloat($('totalLab').innerText);

									if (totalCbm > containerCube) {
										Ext.Msg.alert("提示框", "超过集装箱容积,请修改数据!");
										txt.setValue(oldVal);
										rec.set("boxCount", oldBoxCount);
										$('totalLab').innerText = temp;
										return;
									} else {
										cotSplitService.updateMapValueByEleId(
												rec.data.orderDetailId,
												"boxCount", boxCount, function(
														res) {
												});
									}
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 本柜数量改变
	function boxCountChange(txt, newVal, oldVal) {
		var rec = editRec;
		var oldContainerCount = rec.get("containerCount");
		// 每箱的数量
		var count = rec.get("totalBoxCount");
		var container = rec.get("totalContainerCont");
		var res = Math.ceil(count / container);// 每箱的数量

		// 查看剩余数量
		var orderDetailId = rec.get("orderDetailId");
		var orderOutId = $('orderId').value;
		var splitId = $('pId').value;
		if (splitId == 'null' || splitId == '') {
			splitId = -1;
		}

		DWREngine.setAsync(false);
		cotSplitService.getRemainCountAndExist(splitId, orderOutId,
				orderDetailId, function(outcount) {// res为剩余数量+已出货数量
					if (outcount < newVal) {
						Ext.Msg.alert("提示框", "未出货数量已不足！");
						txt.setValue(oldVal);
						return;
					} else {
						var containerCount = Math.ceil(newVal / res);
						rec.set("containerCount", containerCount);
						cotSplitService.getBoxCBM(orderOutId, orderDetailId,
								function(cbm) {
									var temp = parseFloat($('totalLab').innerText);

									$('totalLab').innerText = (parseFloat($('totalLab').innerText)
											- oldContainerCount * cbm + containerCount
											* cbm).toFixed('2');

									var containerCube = $('containerCube').value
									var totalCbm = parseFloat($('totalLab').innerText);

									if (totalCbm > containerCube) {
										Ext.Msg.alert("提示框", "超过集装箱容积,请修改数据!");
										txt.setValue(oldVal);
										rec.set("containerCount",
												oldContainerCount);
										$('totalLab').innerText = temp;
										return;
									} else {
										cotSplitService.updateMapValueByEleId(
												rec.data.orderDetailId,
												"containerCount",
												containerCount, function(res) {
												});
									}
								});
					}
				});
		DWREngine.setAsync(true);
	}

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid, form]
			});
	viewport.doLayout();

	// 加载表格数据
	ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});

	// 初始化
	function initform() {
		$('cbmDiv').style.display = 'block';
		// 排载单编号
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			// 初始化下单时间为当前时间
			var splitDate = Ext.getCmp("splitDate");
			var date = new Date();
			splitDate.setValue(date);

//			setNoService.getContainerNo($('splitDate').value, function(res) {
//						$('containerNo').value = res;
//					});

			cotSeqService.getContainerNo($('splitDate').value, function(res) {
						$('containerNo').value = res;
					});	
					
			// 加载出货单据编号
			if ($('orderId').value != null && $('orderId').value != '') {
				orderoutBox.bindPageValue("CotOrderOut", "id",
						$('orderId').value);
			}
			// 隐藏按钮
			Ext.getCmp('delBtn').hide();
			Ext.getCmp('printBtn').hide();

		} else {
			DWREngine.setAsync(false);
			// 加载排载单信息
			cotSplitService.getSplitById(parseInt(id), function(res) {
						DWRUtil.setValues(res);

						// 加载时间
						if (res.splitDate != null && res.splitDate != '') {
							var date = new Date(res.splitDate);
							var splitDate = Ext.getCmp("splitDate");
							splitDate.setValue(date);
						}

						// 加载排载单CBM
						if (res.totalCbm != null) {
							$('totalLab').innerText = res.totalCbm.toFixed('2');
						}

						containerBox.bindValue(res.containerType);

						orderoutBox.bindPageValue("CotOrderOut", "id",
								res.orderOutId);

						$('orderId').value = res.orderOutId;
					});
			// 分页基本参数
			// ds.load({
			// params : {
			// start : 0,
			// limit : 15,
			// splitId: id,
			// flag : 'splitDetail'
			// }
			// });
		}
		DWREngine.setAsync(true);
	}
	unmask();
	initform();

	// 删除
	function onDel() {
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
			var orderOutId = $('orderId').value;
			var orderDetailId = item.data.orderDetailId;
			var containerCount = item.data.containerCount;
			DWREngine.setAsync(false);
			cotSplitService.getBoxCBM(orderOutId, orderDetailId, function(res) {
				$('totalLab').innerText = (parseFloat($('totalLab').innerText) - containerCount
						* res).toFixed('2');
			});
			DWREngine.setAsync(true);
			ds.remove(item);
		});
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
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
						type : 'split',
						pId : $('pId').value
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
			Ext.MessageBox.alert("提示信息", "您没有删除权限");
			return;
		}
		var cotSplit = new CotSplitInfo();
		var list = new Array();
		cotSplit.id = $('pId').value;
		list.push(cotSplit);
		Ext.MessageBox.confirm('提示信息', '确定删除该排载明细吗?', function(btn) {
			if (btn == 'yes') {
				DWREngine.setAsync(false);
				cotSplitService.deleteSplit(list, function(res) {
							if (res == 0) {
								Ext.MessageBox.alert("提示信息", "删除成功");
								closeandreflashEC('true', 'splitGrid', false);
							} else {
								Ext.MessageBox.alert("提示信息", "删除失败，该排载单已经被使用中");
							}
						})
				DWREngine.setAsync(true);
			}
		});
	}

	// 关闭页面
	function closeAndClearSplitMap() {
		DWREngine.setAsync(false);
		// 清空SplitMap
		cotSplitService.clearSplitMap(function(res) {
				})
		closeandreflashEC('true', 'splitGrid', false);
		DWREngine.setAsync(true);
	}

	// 显示导入界面
	var _self = this;
	function showImportPanel() {

		// 验证表单
		var formData = getFormValues(form, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var cfg = {};
		cfg.orderOutId = $('orderId').value;
		cfg.bar = _self;

		var importPanel = new ImportPanel(cfg);
		importPanel.show();
	}

	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list) {
		insertByBatch(list);
	}

	// 添加选择的样品到可编辑表格
	function insertByBatch(list) {

		var splitId = $('pId').value;
		var idsAry = new Array();
		Ext.each(list, function(item) {
					idsAry.push(item.data.id);
				});
		for (var i = 0; i < idsAry.length; i++) {
			DWREngine.setAsync(false);
			cotSplitService.findDetail(splitId, parseInt(idsAry[i]), function(
							detail) {
						if (detail != null) {
							insertToGrid(detail);
						}
					});
			DWREngine.setAsync(true);
		}
		unmask();
	}

	// 根据产品货号查找样品数据
	function insertToGrid(detail) {

		var orderDetailId = detail.id;

		DWREngine.setAsync(false);
		var cotSplitDetail = new CotSplitDetail();

		cotSplitDetail.eleId = detail.eleId;
		cotSplitDetail.custNo = detail.custNo;
		cotSplitDetail.eleNameEn = detail.eleNameEn;
		cotSplitDetail.totalBoxCount = detail.boxCount;
		cotSplitDetail.totalContainerCont = detail.containerCount;

		cotSplitDetail.containerCount = Math.ceil((detail.remainBoxCount)
				* detail.containerCount / detail.boxCount);

		cotSplitDetail.boxCount = detail.remainBoxCount;
		cotSplitDetail.detailRemark = detail.eleRemark;
		cotSplitDetail.orderDetailId = detail.id;
		// cotSplitDetail.splitId = $('pId').value;

		// 重新计算总cbm
		var orderOutId = $('orderId').value;
		cotSplitService.getTotalCBM(orderOutId, cotSplitDetail.orderDetailId,
				function(cbm) {
					if (cotSplitDetail.totalContainerCont != 0) {
						$('totalLab').innerText = (parseFloat($('totalLab').innerText) + (cotSplitDetail.containerCount / cotSplitDetail.totalContainerCont)
								* cbm).toFixed('2');
					}
				});
		// 将送样明细对象储存到后台GivenMap中
		cotSplitService.setSplitMap(orderDetailId, cotSplitDetail,
				function(res) {
				});

		setObjToGrid(cotSplitDetail);
		DWREngine.setAsync(true);
	}

	// 添加带有数据的record到表格中
	function setObjToGrid(obj) {
		var u = new ds.recordType(obj);
		ds.add(u);
	}

	// 保存
	function save() {
		// 验证表单
		var formData = getFormValues(form, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		Ext.MessageBox.confirm('提示信息', '您是否要保存该排载单？', function(btn) {
			if (btn == 'yes') {
				var split = DWRUtil.getValues('splitForm');
				var cotSplit = {};
				// 如果编号存在时先查询出对象,再填充表单
				if ($('pId').value != 'null' && $('pId').value != '') {
					DWREngine.setAsync(false);
					cotSplitService.getSplitById($('pId').value, function(res) {
								for (var p in res) {
									if (p != 'splitDate') {
										res[p] = split[p];
									}
								}
								cotSplit = res;
								cotSplit.id = $('pId').value;
							});
					DWREngine.setAsync(true);
				} else {
					cotSplit = new CotSplitInfo();
					for (var p in cotSplit) {
						if (p != 'splitDate') {
							cotSplit[p] = split[p];
						}
					}
				}
				DWREngine.setAsync(false);
				cotSplitService.saveOrUpdateSplit(cotSplit,
						$('splitDate').value, function(res) {
							if (res != null) {
								$('pId').value = res;
								Ext.getCmp('delBtn').setVisible(true);
								Ext.getCmp('printBtn').setVisible(true);
								// 更改添加action参数
								var urlAdd = '&splitPrimId=' + res
										+ '&orderOutId=' + $('orderId').value;
								// 更改修改action参数
								var urlMod = '&splitPrimId=' + res
										+ '&orderOutId=' + $('orderId').value;

								// 更改删除action参数
								var delMod = '&splitPrimId=' + res
										+ '&orderOutId=' + $('orderId').value;
								ds.proxy.setApi({
									read : "cotsplitinfo.do?method=querySplitDetail&splitId="
											+ res + "&flag=splitDetail",
									create : "cotsplitinfo.do?method=addSplitDetail"
											+ urlAdd,
									update : "cotsplitinfo.do?method=modifySplitDetail"
											+ urlMod,
									destroy : "cotsplitinfo.do?method=removeSplitDetail"
											+ delMod
								});
								ds.save();
								
								Ext.Msg.alert("提示消息", "保存成功！");
							} else {
								Ext.MessageBox.alert('提示消息', '保存失败');
								//unmask();
							}
						});
				DWREngine.setAsync(true);
			}
		});
	}
});