// 删除下拉框行
function delComData(id) {
	var grid = Ext.getCmp("eleIdForReport");
	var store = grid.getStore();
	store.each(function(rec) {
				if (rec.get("id") == id) {
					store.remove(rec);
					return;
				}
			});
}
BatchPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	// 海关编码
	var hsCode = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "Customs Code",
				editable : true,
				valueField : "id",
				allowBlank : true,
				hidden:true,
				hideLabel:true,
				displayField : "cnName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				pageSize : 5,
				anchor : "95%",
				tabIndex : 17,
				sendMethod : "post",
				selectOnFocus : false,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 厂家
	var facComb = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryId',
				fieldLabel : "Supplier",
				editable : true,
//				hidden : hideFac,
//				hideLabel : hideFac,
				hidden:true,
				hideLabel:true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				pageSize : 5,
				anchor : "95%",
				allowBlank : true,
				tabIndex : 5,
				sendMethod : "post",
				selectOnFocus : false,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 材质
	var typeLvComb = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv4&key=typeName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "Dep. Of PRODUCT",
				editable : true,
				valueField : "id",
				allowBlank : true,
				hidden:true,
				hideLabel:true,
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 5,
				anchor : "95%",
				tabIndex : 6,
				sendMethod : "post",
				selectOnFocus : false,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 产品分类
	var typeLv2Comb = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2Find',
				fieldLabel : "Group",
				editable : true,
				valueField : "id",
				allowBlank : true,
				hidden:true,
				hideLabel:true,
				displayField : "typeName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				pageSize : 5,
				anchor : "95%",
				tabIndex : 17,
				sendMethod : "post",
				selectOnFocus : false,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 打印模板
	var rptBox = new BindCombox({
		dataUrl : "../../servlet/DataSelvert?tbname=CotRptFile&key=rptName&typeName=rptType&type=1",
		cmpId : 'reportTemple',
		fieldLabel : "Templates",
		editable : true,
		valueField : "rptfilePath",
		displayField : "rptName",
		emptyText : 'Choose',
		anchor : "90%",
		allowBlank : true,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 设置默认模板
	queryService.getRptDefault(1, function(def) {
				if (def != null) {
					rptBox.bindValue(def.rptfilePath);
				}
			});

	var pageCk = new Ext.form.RadioGroup({
				fieldLabel : "",
				hideLabel : true,
				items : [{
							boxLabel : 'Not paginated',
							inputValue : false,
							style : "marginLeft:25",
							name : 'showHeader',
							id : "singlePage"
						}, {
							boxLabel : 'Show by page',
							id : "allPage",
							inputValue : true,
							name : 'showHeader',
							checked : true
						}]
			});

	// 打印预览面板
	var rptPanel = new Ext.Panel({
				title : "Export",
				id : "rptPanel",
				layout : "fit",
				items : [{
					xtype : "panel",
					layout : "form",
					labelWidth : 65,
					padding:5,
					autoHeight : true,
					labelAlign : 'right',
					ctCls : 'x-panel-mc',
					items : [pageCk, {
								xtype : 'checkboxgroup',
								fieldLabel : "",
								hideLabel : true,
								items : [{
											xtype : 'checkbox',
											boxLabel : 'Export By Sheet',
											style : "marginLeft:25",
											id : 'exlSheet',
											name : 'exlSheet'
										}]
							}, rptBox, {
								xtype : "combo",
								name : 'printType',
								hiddenName : 'printType',
								store : new Ext.data.SimpleStore({
											fields : ["id", "name"],
											data : [['XLS', 'XLS'],
													['PDF', 'PDF']]
										}),
								valueField : "id",
								displayField : "name",
								value : "XLS",
								mode : 'local',
								editable : false,
								triggerAction : "all",
								fieldLabel : "Type",
								anchor : "90%",
								selectOnFocus : false
							}, {
								xtype : "panel",
								buttonAlign : "center",
								buttons : [{
											text : "Export",
											width : 65,
											iconCls : "page_excel",
											handler : exportRpt
										}, {
											text : "Print To Preview",
											width : 80,
											iconCls : "page_print",
											handler : viewRpt
										}]
							}]
				}]

			});

	// 批量更新面板
	var batchUpdate = new Ext.FormPanel({
				title : "Batch delete",
				layout : "border",
				hidden: true,
				id : "batchForm",
				formId : "eleBatchForm",
				buttonAlign : "center",
				bbar : ['->','-',{
							text : "Update",
							width : 65,
							iconCls : "page_mod",
							handler : modBatch
						},'-', {
							text : "Delete",
							width : 65,
							iconCls : "page_del",
							handler : deleteByBatch
						},'-'],
				items : [{
							layout : "form",
							labelWidth : 90,
							region : 'center',
							autoHeight : true,
							labelAlign : "right",
							ctCls : 'x-panel-mc',
							padding:5,
							items : [{
										xtype : "textfield",
										fieldLabel : "Design",
										anchor : "95%",
										id : "eleCol"
									}, {
										xtype : "textfield",
										fieldLabel : "Designer",
										anchor : "95%",
										id : "eleForPerson"
									}, {
										xtype : "textfield",
										fieldLabel : "MOQ",
										anchor : "95%",
										id : "eleMod"
									}, {
										xtype : "textfield",
										fieldLabel : "Material",
										anchor : "95%",
										id : "eleName"
									}, {
										xtype : "textfield",
										fieldLabel : "Description",
										anchor : "95%",
										id : "eleNameEn"
									}, {
										xtype : "textarea",
										fieldLabel : "Sales Unit",
										anchor : "95%",
										height : 38,
										id : "boxRemarkCn"
									}, {
										xtype : "textarea",
										fieldLabel : "boxRemark",
										anchor : "95%",
										hidden : true,
										hideLabel : true,
										height : 38,
										id : "boxRemark"
									}, {
										xtype : "textarea",
										fieldLabel : "Remark",
										anchor : "95%",
										height : 38,
										id : "eleRemark"
									}, typeLvComb, hsCode,
									typeLv2Comb, facComb,{
										xtype : 'checkboxgroup',
										fieldLabel : '',
										style : "marginLeft:10",
										itemCls : 'x-check-group-alt',
										columns : 1,
										hideLabel : true,
										items : [{
													boxLabel : 'Recalculate the cost of accessories',
													hidden:true,
													id : "fitChk"
												}, {
													boxLabel : 'Recalculate the cost of the sample',
													hidden:true,
													id : "priceChk"
												}, {
													boxLabel : 'Recalculate the price of packaging',
													hidden:true,
													id : "packingChk"
												}, {
													boxLabel : 'Production value by the default configuration of the production value is calculated',
													hidden:true,
													id : "facChk"
												}, {
													boxLabel : ' Calculate Sales Price By Formula',
													hidden:true,
													id : "outChk"
												}]
									}]

						}]
			});

	// 盘点机机型
	var panTypeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'DT930'], [1, 'MC550']]
			});
	var panBox = new Ext.form.ComboBox({
				name : 'pantype',
				fieldLabel : 'Type',
				editable : false,
				store : panTypeStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				anchor : "90%",
				value : 0,
				triggerAction : 'all',
				hiddenName : 'pantype',
				selectOnFocus : true
			});
	// 盘点机面板
	var panPanel = new Ext.Panel({
				title : 'Scanner',
				name : "panPanel",
				layout : 'fit',
				items : [{
							layout : "panel",
							layout : 'column',
							ctCls : 'x-panel-mc',
							items : [{
										xtype : 'panel',
										columnWidth : 0.2,
										height : 30
									}, {
										layout : 'form',
										columnWidth : 0.7,
										labelWidth : 40, // 标签宽度
										items : [panBox]
									}, {
										xtype : "button",
										text : "Export",
										width : 65,
										iconCls : "page_excel",
										handler : checkMachineData
									}]
						}]
			});

	// 右键--导出图片
	var picStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['png', 'png'], ['jpg', 'jpg'], ['bmp', 'bmp'],
						['gif', 'gif']]
			});
	var picTypeBox = new Ext.form.ComboBox({
				name : 'picType',
				fieldLabel : 'Pic Type',
				editable : false,
				store : picStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				value : 'png',
				anchor : '90%',
				validateOnBlur : true,
				triggerAction : 'all',
				hiddenName : 'picType',
				selectOnFocus : true
			});

	// 导出图片面板
	var downPicPanel = new Ext.Panel({
				title : 'Export Picture',
				name : "downPicPanel",
				layout : 'fit',
				items : [{
							layout : "panel",
							layout : 'column',
							ctCls : 'x-panel-mc',
							items : [{
										xtype : 'panel',
										columnWidth : 0.2,
										height : 30
									}, {
										layout : 'form',
										columnWidth : 0.7,
										labelWidth : 55, // 标签宽度
										items : [picTypeBox]
									}, {
										xtype : 'button',
										text : "Export",
										width : 65,
										iconCls : "page_img",
										handler : downPics
									}]
						}]
			});

	// 下载多张图片.并且压缩.zip
	function downPics() {
		var ids = '';
		var str = '';
		var type = $('picType').value;
		if (!$('printFlag').checked) {
			var store = Ext.getCmp("eleIdForReport").getStore();
			if (store.getCount() == 0) {
				Ext.MessageBox.alert('Message', 'Please select the sample and then export the images!');
				return;
			}
			if (store.getCount() > 2000) {
				Ext.MessageBox.alert('Message', 'You can only export a maximum of 2,000 pictures!');
				return;
			}
			store.each(function(record) {
						ids += record.get("id") + ',';
					})
			downRpt("../../downPics.action?priceNo=elements"
					+ "&tp=elements&rdms=" + ids + "&type=" + type);
		} else {
			Ext.MessageBox.confirm('Message',
					"Are you sure you want to continue to export？", function(btn) {
						if (btn == 'yes') {
							var str = changeToStr();
							mask();
							downRpt('../../downPics.action?page=all&tp=elements&priceNo=elements&type='
									+ type + str);
							setTimeout('unmask()', 7000);
						}
					});
		}
	}

	// 下拉框层中的导出方法
	function exportRpt() {
		var model = $('reportTemple').value;
		if (model == '') {
			Ext.MessageBox.alert("Message", 'Please select the export template!');
			return;
		}
		var printType = $('printType').value;
		var ids = '';
		var str = '';
		if (!$('printFlag').checked) {
			var store = Ext.getCmp("eleIdForReport").getStore();
			store.each(function(record) {
						ids += record.get("id") + ',';
					})
			if (ids == '') {
				Ext.MessageBox.alert("Message", 'You do not select any records!');
				return;
			}
		} else {
			str = changeToStr();
		}

		var headerFlag = pageCk.getValue().getRawValue();

		var exlSheet = $('exlSheet').checked;
		var url = "../../downloadRpt.action?reportTemple=" + model
				+ "&printType=" + printType + "&exlSheet=" + exlSheet + "&ids="
				+ ids + str + "&headerFlag=" + headerFlag;
		// 为了下载时不弹出新窗口
		downRpt(url);
	}
	// 将左边查询条件转换成字符串
	function changeToStr() {
		var str = "";
		var paramsQuery = queryForm.getForm().getValues();
		for (var p in paramsQuery) {
			if (paramsQuery[p] != '' && p != 'findAjaxZoneAtClient') {
				str += "&" + p + "=" + paramsQuery[p];
			}
		}
		if (str.indexOf("childFind") < 0)
			str += "&childFind=false"
		return str;
	}
	// 下拉框层中的预览方法
	function viewRpt() {
		var _height = window.screen.height;
		var _width = window.screen.width;
		var model = $('reportTemple').value;
		if (model == '') {
			Ext.Msg.alert("Message", 'Please select the export template!');
			return;
		}
		var ids = '';
		var str = '';
		if (!$('printFlag').checked) {
			var store = Ext.getCmp("eleIdForReport").getStore();
			store.each(function(record) {
						ids += record.get("id") + ',';
					});
			if (ids == '') {
				Ext.Msg.alert("Message", 'You do not select any records!');
				return;
			}
		} else {
			str = changeToStr();
		}

		if ($('allPage').checked) {
			headerFlag = true;
		} else {
			headerFlag = false;
		}
		var printType = $('printType').value;
		openMaxWindow(_height, _width,
				"../../previewrpt.do?method=queryEleRpt&reportTemple=" + model
						+ "&printType=" + printType + "&ids=" + ids + str
						+ "&headerFlag=" + headerFlag + "&tbflag=sample");
	}

	// 根据选中的页码导出盘点机文本
	function exportCMData() {
		Ext.MessageBox.confirm('Message', '您要生成表格所有盘点机数据,系统需要时间处理，您确定要继续生成吗？',
				function(btn) {
					if (btn == 'yes') {
						mask("加载数据中...");
						setTimeout(function() {
									var str = changeToStr();
									var flag = $('pantype').value;
									downRpt('../../downCheckMachine.action?flag='
											+ flag + '&page=1' + str);
									unmask();
								}, 500);
					}
				});
	}

	// 从选择的记录中导出盘点机文本
	function checkMachineData() {
		var ids = '';
		var str = '';
		if (!$('printFlag').checked) {
			var store = Ext.getCmp("eleIdForReport").getStore();
			if (store.getCount() == 0) {
				Ext.Msg.alert("Message", '请先选择要导出的样品!');
				return;
			}
			if (store.getCount() > 2000) {
				Ext.Msg.alert("Message", '一次最多只能导出2000条样品!');
				return;
			}
			store.each(function(record) {
						ids += record.get("id") + ',';
					});
		} else {
			exportCMData();
			return;
		}
		var flag = $('pantype').value;
		// /alert(flag);
		// 下载不打开新页面
		downRpt('../../downCheckMachine.action?flag=' + flag + '&ids=' + ids
				+ str)
	}

	// 批量修改
	function modBatch() {
		var elementForm = DWRUtil.getValues("eleBatchForm");
		elementForm.eleTypeidLv2 = elementForm.eleTypeidLv2Find;

		var flag = 0;
		if ($("facChk").checked == true) {
			flag = 1;
		}
		if ($("outChk").checked == true) {
			flag = 2;
		}
		if ($("facChk").checked == true && $("outChk").checked == true) {
			flag = 3;
		}

		// 重新计算样品的总配件成本
		var fit = $("fitChk").checked;

		// 重新计算样品的总成本
		var price = $("priceChk").checked;

		// 重新计算样品的包材成本
		var pack = $("packingChk").checked;

		// 如果什么都没填,也没勾选就不修改
		if ($('eleCol').value == '' && $('eleName').value == ''
				&& $('eleNameEn').value == '' && elementForm.factoryId == ''
				&& elementForm.eleTypeidLv1 == '' && $('eleHsid').value == ''
				&& $('eleForPerson').value == '' && $('eleMod').value == ''
				&& $('boxRemarkCn').value == '' && $('boxRemark').value == ''
				&& $('eleRemark').value == '' && flag == 0
				&& elementForm.eleTypeidLv2 == '' && fit == false
				&& price == false && pack == false) {
			return;
		}
		if (!$('printFlag').checked) {
			// 要修改的编号字符串
			var ids = '';
			var store = Ext.getCmp("eleIdForReport").getStore();
			store.each(function(record) {
						ids += record.get("id") + ',';
					});
			if (ids == '') {
				Ext.MessageBox.alert("Message", "Please select a record");
				return;
			}
			DWREngine.setAsync(false);
			batchUpdate.getEl().mask("Waiting......");
			DWREngine.setAsync(true);
			// 根据选择ids批量修改
			cotElementsService.modifyBatch(ids, elementForm, flag, fit, price,
					pack, function(res) {
						if (res == true) {
							clearForm('batchForm');
							reloadGrid('eleGrid');
							Ext.MessageBox.alert("Message", "Successfully modified!");
						} else {
							Ext.MessageBox.alert("Message", "Modify the failure!");
						}
						batchUpdate.getEl().unmask();
					})
		} else {
			Ext.MessageBox.confirm('Message', 'Are you sure you want to continue to modify?',
					function(btn) {
						if (btn == 'yes') {
							DWREngine.setAsync(false);
							batchUpdate.getEl().mask("Waiting......");
							DWREngine.setAsync(true);
							// 根据表格当前数据批量修改
							var paramsQuery = queryForm.getForm().getValues();
							cotElementsService.modifyBatchAll(paramsQuery,
									elementForm, flag, fit, price, pack,
									function(res) {
										if (res == true) {
											clearForm('batchForm');
											reloadGrid('eleGrid');
											Ext.MessageBox.alert("Message",
													"Successfully modified!");
										} else {
											Ext.MessageBox.alert("Message",
													"Modify the failure!");
										}
										batchUpdate.getEl().unmask();
									});
						}
					});
		}
	}

	// 删除下拉框选择的样品
	function deleteByBatch() {
		if (!$('printFlag').checked) {
			var grid = Ext.getCmp("eleIdForReport");
			var list = new Array();
			grid.getStore().each(function(item) {
						list.push(item.data.id);
					});
			if (list.length == 0) {
				Ext.MessageBox.alert("Message", 'Please select a record');
				return;
			}
			Ext.MessageBox.confirm('Message', 'You sure to delete the selected record?', function(btn) {
						if (btn == 'yes') {
							_self.hide();
							cotElementsService.deleteElements(list, function(
											res) {
										clearForm("queryForm");
										reloadGrid('eleGrid');
										Ext.MessageBox.alert("Message", 'Deleted successfully！');
									});
						}
					});
		} else {
			Ext.MessageBox.confirm('Message', "Are you sure you want to continue to delete it?",
					function(btn) {
						if (btn == 'yes') {
							mask();
							var task = new Ext.util.DelayedTask(function() {
										var paramsQuery = queryForm.getForm()
												.getValues();
										cotElementsService
												.deleteEleByCondition(
														paramsQuery, function(
																res) {
															clearForm("queryForm");
															reloadGrid('eleGrid');
															unmask();
															Ext.MessageBox
																	.alert(
																			'Message',
																			"Deleted successfully！",
																			function(
																					btn) {
																				if (btn == 'ok') {
																					_self
																							.hide();
																				}
																			});

														});
									});
							task.delay(500);
						}
					});
		}
	}

	// 左边下拉框表格面板
	var grid = {
		xtype : "grid",
		region : 'west',
		width : 220,
		margins : "0 5 0 0",
		id : "eleIdForReport",
		viewConfig : {
			forceFit : false
		},
		store : {
			xtype : "arraystore",
			fields : [{
						name : "eleId",
						type : "string"
					}, {
						name : "id",
						type : "int"
					}]
		},
		columns : [{
					header : "ID",
					dataIndex : "id",
					hidden : true
				}, {
					header : "Art No.",
					sortable : true,
					resizable : true,
					dataIndex : "eleId",
					width : 160
				}, {
					header : "Operation",
					dataIndex : "id",
					width : 40,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var del = '<a href="javascript:delComData(' + value
								+ ')">Delete</a>';
						return del;
					}
				}],
		tbar : [{
					xtype : "checkbox",
					fieldLabel : "Lable",
					style : 'margin-top: 0px;',
					boxLabel : "Select Records of Product Table",
					id : "printFlag",
					listeners : {
						"render" : function(obj) {
							var tip = new Ext.ToolTip({
										target : obj.getEl(),
										anchor : 'top',
										maxWidth : 150,
										minWidth : 150,
										html : 'According to Condition Query'
									});
						}
					}
				}],
		bbar : [{
					text : "Del",
					iconCls : "page_del",
					handler : function() {
						var grid = Ext.getCmp("eleIdForReport");
						var store = grid.getStore();
						var arr = grid.getSelectionModel().getSelections();
						Ext.each(arr, function(record) {
									store.remove(record);
								})

					}
				}, '-', {
					text : "Clear",
					iconCls : "page_reset",
					handler : function() {
						var grid = Ext.getCmp("eleIdForReport");
						var store = grid.getStore();
						store.removeAll();
					}
				}]
	};

	// 右边抽屉面板
	var acn = new Ext.Panel({
				region : 'center',
				layout : 'accordion',
				split : true,
				defaults : {
					autoScroll : true,
					bodyStyle : "overflow-x:hidden;"
				},
				items : [rptPanel, batchUpdate, panPanel, downPicPanel]
			});
	// 选择抽屉展开的面板
	this.selectActiveItem = function(num) {
		acn.getLayout().setActiveItem(num);
	}

	var con = {
		title : "OP",
		width : 550,
		height : 470,
		layout : "border",
		closeAction : 'hide',
		border : false,
		items : [grid, acn]
	};
	// 第一次弹出时.如果没printDiv,则新建
	// this.printName = 'batchModDiv';
	// if ($(this.printName) == null) {
	// // window默认在z-index为9000
	// Ext.DomHelper.append(document.body, {
	// html : '<div id="'
	// + this.printName
	// + '" style="position:absolute;z-index:8500;"></div>'
	// }, true);
	// }

	Ext.apply(con, cfg);
	BatchPanel.superclass.constructor.call(this, con);
};
Ext.extend(BatchPanel, Ext.Window, {});
