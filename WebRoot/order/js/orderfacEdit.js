//window.onbeforeunload = function() {
//	var ds = Ext.getCmp('orderfacGrid').getStore();
//	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
//		if (Ext.isIE) {
//			if (document.body.clientWidth - event.clientX < 170
//					&& event.clientY < 0 || event.altKey) {
//				return "采购明细数据有更改,您确定不保存吗?";
//			} else if (event.clientY > document.body.clientHeight
//					|| event.altKey) { // 用户点击任务栏，右键关闭
//				return "采购明细数据有更改,您确定不保存吗?";
//			} else { // 其他情况为刷新
//			}
//		} else if (Ext.isChrome || Ext.isOpera) {
//			return "采购明细数据有更改,您确定不保存吗?";
//		} else if (Ext.isGecko) {
//			window.open("http://www.g.cn")
//			var o = window.open("index.do?method=logoutAction");
//		}
//	}
//}

// 获取报价和订单的单价要保留几位小数
// var deNum = getDeNum("facPrecision");

// 窗口关闭触发事件
window.onunload = function() {
	cotOrderFacService.clearMap(function(res) {});
}

Ext.onReady(function() {
	// cbm保留几位小数
	var cbmNum = getDeNum("cbmPrecision");
	var cbmStr = getDeStr(cbmNum);
	// -----------------本地下拉框-----------------------------------------
	// 组成方式
	var eleFlagStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'Single'], [1, 'Suite'], [3, 'Components']]
			});
	var eleFlagBox = new Ext.form.ComboBox({
				name : 'eleFlag',
				fieldLabel : 'Composition',
				editable : false,
				store : eleFlagStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 36,
				emptyText : 'Choose',
				hiddenName : 'eleFlag',
				selectOnFocus : true
			});

	// 审核状态
	var orderStatusStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'Non-reviewed'], [1, 'Review without'], [2, 'reviewed'], [3, 'Need to be reviewed'],
						[9, 'Not review']]
			});
	var orderStatusBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				fieldLabel : ' Status',
				editable : false,
				disabledClass : 'combo-disabled',
				store : orderStatusStore,
				valueField : "id",
				displayField : "name",
				value:9,
				mode : 'local',
				validateOnBlur : true,
				disabled : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 11,
				emptyText : 'Choose',
				hiddenName : 'orderStatus',
				selectOnFocus : true
			});
	// -----------------远程下拉框-----------------------------------------

	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				fieldLabel : "<font color='red'>币种</font>",
				emptyText : 'Choose',
				allowBlank : false,
				blankText : "Currency can not be empty！",
				tabIndex : 5,
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});

	// 材质
	var typeLv1Box = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "Material",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 34,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var changeFactory = function() {

		var facFlag = false;

		var orderfacId = $('pId').value;
		var factoryId = $('factoryId').value;
		if (orderfacId == 'null' || orderfacId == '') {
			if (factoryId != 'null' && factoryId != '') {
				cotOrderFacService.checkIsFactory(parseInt(factoryId),
						parseInt($('oId').value), function(bool) {
							facFlag = bool;
						});
			}
		}
		if (facFlag) {
			Ext.Msg.alert("Message", 'The order already exists in the corresponding purchase order to the factory，Pls. add to the procurement contract！');
			facBox.setValue('');
			return;
		}
	}

	// 厂家
	var facBox = new BindCombox({
				dataUrl : "./servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1&validUrl=cotfactory.do",
				cmpId : 'factoryId',
				fieldLabel : "<font color='red'>Supplier</font>",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				allowBlank : false,
				blankText : "Supplier can not be empty！",
				pageSize : 10,
				anchor : "100%",
				tabIndex : 1,
				selectOnFocus : true,
				sendMethod : "post",

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				listeners : {
					"change" : changeFactory
				}
			});

	// 厂家接收人
	var facContactBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotContact&key=contactPerson",
				cmpId : 'facContactId',
				fieldLabel : "Contact person",
				editable : true,
				valueField : "id",
				displayField : "contactPerson",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 10,
				sendMethod : "post",
				mode : 'remote',
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 海关编码
	var eleHsidBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "HS Code",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "cnName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 50,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 业务员
	var empsBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPerson',
				fieldLabel : "<font color=red>业务员</font>",
				editable : true,
				sendMethod : "POST",
				valueField : "id",
				displayField : "empsName",
				mode : 'remote',// 默认local
				// autoLoad:false,//默认自动加载
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,
				allowBlank : false,
				blankText : "Seller can not be empty！",
				emptyText : 'Choose',
				tabIndex : 9,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 出口公司
	var companyBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
		cmpId : 'companyId',
		fieldLabel : "Company",
		editable : true,
		valueField : "id",
		displayField : "companyShortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		tabIndex : 6,
		sendMethod : "post",
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 计算包材价格
	var calPrice = function() {
		var ele = DWRUtil.getValues('rightForm');
		var elements = new CotElementsNew();
		for (var p in elements) {
			if (p != 'eleProTime') {
				elements[p] = ele[p];
			}
		}
		DWREngine.setAsync(false);
		cotElementsService.calPriceAll(elements, function(res) {
					$('boxPbPrice').value = res[0];
					$('boxIbPrice').value = res[1];
					$('boxMbPrice').value = res[2];
					$('boxObPrice').value = res[3];
					$('packingPrice').value = res[4];
					if (res[5] != -1) {
						$('priceFac').value = res[5];
					}
					$('inputGridPrice').value = res[6];
				});
		var rdm = $('rdm').value;

		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxIbPrice',
				$('boxIbPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxMbPrice',
				$('boxMbPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxObPrice',
				$('boxObPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxPbPrice',
				$('boxPbPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'inputGridPrice',
				$('inputGridPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'packingPrice',
				$('packingPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'priceFac',
				$('priceFac').value, function(res) {
				});

		var rec = editRec;
		rec.set("priceFac", $('priceFac').value);

		DWREngine.setAsync(true);

	}

	// 加载包装类型值
	var selectBind = function() {
		var id = $('boxTypeId').value;
		var rdm = $('rdm').value;
		if (rdm == null || rdm == '') {
			return;
		}
		DWREngine.setAsync(false);
		cotElementsService.getBoxTypeById(id, function(res) {

					if (res != null) {
						if (res.boxIName == null) {
							boxIbTypeBox.setValue("");
						} else {
							boxIbTypeBox.bindValue(res.boxIName);
						}
						if (res.boxMName == null) {
							boxMbTypeBox.setValue("");
						} else {
							boxMbTypeBox.bindValue(res.boxMName);
						}
						if (res.boxOName == null) {
							boxObTypeBox.setValue("");
						} else {
							boxObTypeBox.bindValue(res.boxOName);
						}
						if (res.boxPName == null) {
							boxPbTypeBox.setValue("");
						} else {
							boxPbTypeBox.bindValue(res.boxPName);
						}
						if (res.inputGridType == null) {
							inputGridTypeBox.setValue("");
						} else {
							inputGridTypeBox.bindValue(res.inputGridType);
						}
					}
					cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
							'boxPbTypeId', res.boxPName, function(res) {
							});
					cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
							'boxIbTypeId', res.boxIName, function(res) {
							});
					cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
							'boxMbTypeId', res.boxMName, function(res) {
							});
					cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
							'boxObTypeId', res.boxOName, function(res) {
							});
					cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
							'inputGridTypeId', res.inputGridType,
							function(res) {
							});
				});
		DWREngine.setAsync(true);
		calPrice();
	};

	// 包装方案
	var boxPacking = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType",
				valueField : "id",
				fieldLabel : "Packing Way:",
				tabIndex : 51,
				displayField : "typeName",
				cmpId : "boxTypeId",
				emptyText : 'Choose',
				anchor : "100%",
				listeners : {
					"select" : selectBind
				}
			});

	// 插格类型
	var inputGridTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IG",
				valueField : "id",
				fieldLabel : "插格类型",
				tabIndex : 53,
				displayField : "value",
				cmpId : "inputGridTypeId",
				emptyText : '请选择',
				anchor : "100%",
				listeners : {
					'change' : function() {
						calPrice();
					}
				}
			});
	// 产品类型
	var boxPbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=PB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 56,
				displayField : "value",
				cmpId : "boxPbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					'change' : function() {
						calPrice();
					}
				}
			});
	// 内包装类型
	var boxIbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 59,
				displayField : "value",
				cmpId : "boxIbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					'change' : function() {
						calPrice();
					}
				}
			});
	// 中包装类型
	var boxMbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=MB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 62,
				displayField : "value",
				cmpId : "boxMbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					'change' : function() {
						calPrice();
					}
				}
			});
	// 外包装类型
	var boxObTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=OB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 65,
				displayField : "value",
				cmpId : "boxObTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					'change' : function() {
						calPrice();
					}
				}
			});
	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Category",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 38,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 生产价保留小数位
	var facNum = getDeNum("facPrecision");
	var facNumTemp = "0.000";
	if (facNum == 0) {
		facNumTemp = "0";
	}
	if (facNum == 1) {
		facNumTemp = "0.0";
	}
	if (facNum == 2) {
		facNumTemp = "0.00";
	}
	if (facNum == 4) {
		facNumTemp = "0.0000";
	}
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
				name : "sortNo"
			}, {
				name : "eleId"
			}, {
				name : "factoryNo"
			}, {
				name : "custNo"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "priceFac",
				convert : numFormat.createDelegate(this, [facNumTemp], 3)
			}, {
				name : "moldCharge"
			}, {
				name : "taoUnit"
			}, {
				name : "eleUnit"
			}, {
				name : "boxCount"
			}, {
				name : "containerCount"
			}, {
				name : "totalFac",
				convert : numFormat.createDelegate(this, [facNumTemp], 3)
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
			}, {
				name : "boxObL",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObW",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObH",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxCbm",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "boxGrossWeigth",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "boxNetWeigth",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "type"
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
						read : "cotorderfac.do?method=queryDetail",
						create : "cotorderfac.do?method=addDetail",
						update : "cotorderfac.do?method=modifyDetail",
						destroy : "cotorderfac.do?method=removeDetail"
					},
					listeners : {
						beforeload : function(store, options) {
							ds.removed = [];
//							cotOrderFacService.clearMap(function(res) {
//									});
						},
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("Message", "Operation failed！");
							} else {
								
								ds.reload();
							}
							unmask();
						},
						// 保存表格前显示提示消息
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
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [
						sm,// 添加复选框列
						{
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "No.",
							dataIndex : "sortNo",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 40
						}, {
							header : "Art No.：",
							dataIndex : "eleId",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 80,
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "Supplier Art No.:",
							dataIndex : "factoryNo",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 80
						}, {
							header : "Customer Art No.",
							dataIndex : "custNo",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 80
						}, {
							header : "Description:",
							dataIndex : "eleName",
							editor : new Ext.form.TextField({
										width : 400
									}),
							width : 120
						}, {
							header : "Description:",
							dataIndex : "eleNameEn",
							editor : new Ext.form.TextField({
										width : 400
									}),
							width : 120
						}, {
							header : "Purchase Price",
							dataIndex : "priceFac",
							editor : new Ext.form.NumberField({
										width : 400,
										maxLength : 200,
										listeners : {
											"change" : function(txt, newVal,
													oldVal) {
												priceFacChange(newVal, 1);
											}
										}
									}),
							width : 80
						}, {
							header : "模具费用",
							dataIndex : "moldCharge",
							editor : new Ext.form.NumberField({
										width : 400
									}),
							width : 80
						}, {
							header : "报价单位",
							dataIndex : "taoUnit",
							editor : new Ext.form.TextField({
										width : 400
									}),
							width : 80
						}, {
							header : "Unit",
							dataIndex : "eleUnit",
							editor : new Ext.form.TextField({
										width : 400
									}),
							width : 80
						}, {
							header : "Quantity",
							dataIndex : "boxCount",
							editor : new Ext.form.NumberField({
										maxLength : 100,
										listeners : {
											"change" : function(txt, newVal,
													oldVal) {
												boxCountChange(newVal);
											}
										}
									}),
							width : 60,
							summaryType : 'sum'
						}, {
							header : "Cartons",
							dataIndex : "containerCount",
							editor : new Ext.form.NumberField({
										width : 400,
										listeners : {
											"change" : function(txt, newVal,
													oldVal) {
												containerCountChange(newVal);
											}
										}
									}),
							width : 80,
							summaryType : 'sum'
						}, {
							header : "Amount",
							dataIndex : "totalFac",
							editor : new Ext.form.NumberField({
										maxLength : 100
									}),
							width : 90,
							summaryType : 'sum'
						}, {
							header : "Size Description",
							dataIndex : "eleSizeDesc",
							editor : new Ext.form.TextArea({
										width : 400,
										maxLength : 500
									}),
							width : 140
						}, {
							header : "Size Description",
							dataIndex : "eleInchDesc",
							editor : new Ext.form.TextArea({
										width : 400,
										maxLength : 500
									}),
							width : 140
						}, {
							header : "Inner Box",
							dataIndex : "boxIbCount",
							editor : new Ext.form.NumberField({
										width : 400
									}),
							width : 40
						}, {
							header : "中",
							dataIndex : "boxMbCount",
							editor : new Ext.form.NumberField({
										width : 400
									}),
							width : 40
						}, {
							header : "Outer Box",
							dataIndex : "boxObCount",
							editor : new Ext.form.NumberField({
										width : 400
									}),
							width : 40
						}, {
							header : "外箱长",
							dataIndex : "boxObL",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "外箱宽",
							dataIndex : "boxObW",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "外箱高",
							dataIndex : "boxObH",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "CBM",
							dataIndex : "boxCbm",
							width : 60,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								// 总金额=数量*单价
								var boxObL = record.get("boxObL");
								var boxObW = record.get("boxObW");
								var boxObH = record.get("boxObH");
								var res = (boxObL * boxObW * boxObH * 0.000001)
										.toFixed("4");
								record.data.boxCbm = res;
								return res;
							},
							editor : new Ext.form.NumberField({
										maxValue : 999999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "G.W.",
							dataIndex : "boxGrossWeigth",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "N.W.",
							dataIndex : "boxNetWeigth",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "type",
							dataIndex : "type",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							hidden : true
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
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : [{
							text : "Create",
							iconCls : "page_add",
							handler : showOrderDetailDiv
						}, '-', {
							text : "Delete",
							handler : onDel,
							iconCls : "page_del"
						}]
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 寄样明细表格
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "orderfacGrid",
				stripeRows : true,
				// clicksToEdit : 1,
				margins : "0 5 0 0",
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : [summary],
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 右边折叠面板
	var rightForm = new Ext.form.FormPanel({
		autoScroll : true,
		formId : "rightForm",
		padding : "5px",
		height : 500,
		labelWidth : 60,
		labelAlign : "right",
		items : [{
			title : 'Basic information',
			xtype : "fieldset",
			name : "mainPanel",
			layout : "column",
			anchor : '97%',
			items : [{
				xtype : "panel",
				columnWidth : 0.45,
				layout : 'hbox',
				layoutConfig : {
					align : 'middle',
					pack : 'center'
				},
				buttonAlign : "center",
				items : [{
					xtype : "panel",
					width : 100,
					html : '<div align="center" style="width: 100px; height: 100px;">'
							+ '<img src="common/images/zwtp.png" id="picPath" name="picPath"'
							+ 'onload="javascript:DrawImage(this,100,100)" onclick="showBigPicDiv(this)"/></div>'
				}],
				buttons : [{
					width : 40,
					text : "Change",
					handler : showUploadPanel,
					id : "upmod"
						// iconCls : "upload-icon"
					}, {
					width : 40,
					text : "Delete",
					handler : delPic,
					id : "updel"
						// iconCls : "upload-icon-del"
					}]
			}, {
				xtype : "panel",
				columnWidth : 0.55,
				layout : "form",
				labelWidth : 40,
				items : [{
							xtype : "textfield",
							fieldLabel : "Art No.",
							anchor : "100%",
							tabIndex : 31,
							id : "eleId",
							name : "eleId",
							disabled : true,
							disabledClass : 'combo-disabled',
							maxLength : 100
						}, {
							xtype : "textfield",
							fieldLabel : "Supplier's Art No.",
							anchor : "100%",
							tabIndex : 32,
							id : "factoryNo",
							name : "factoryNo",
							maxLength : 200
						}, {
							xtype : "textfield",
							fieldLabel : "Customer Art No.",
							anchor : "100%",
							tabIndex : 33,
							id : "custNo",
							name : "custNo",
							maxLength : 200
						}, typeLv1Box, {
							xtype : "hidden",
							id : "id",
							name : "id"
						}, {
							xtype : "hidden",
							id : "rdm",
							name : "rdm"
						}]
			}, {
				xtype : "panel",
				columnWidth : 0.6,
				layout : "form",
				labelWidth : 60,
				items : [eleFlagBox, typeLv2Box, {
							xtype : "textfield",
							fieldLabel : "Design",
							anchor : "100%",
							tabIndex : 40,
							id : "eleCol",
							name : "eleCol",
							maxLength : 50
						}, {
							xtype : "textfield",
							fieldLabel : "Source:",
							anchor : "100%",
							id : "eleFrom",
							name : "eleFrom",
							tabIndex : 42
						}, {
							xtype : "textfield",
							fieldLabel : "所属年份",
							anchor : "100%",
							id : "eleTypenameLv2",
							name : "eleTypenameLv2",
							tabIndex : 44
						}]
			}, {
				xtype : "panel",
				columnWidth : 0.4,
				layout : "form",
				items : [{
							xtype : "numberfield",
							fieldLabel : "套数",
							anchor : "100%",
							id : "eleUnitNum",
							name : "eleUnitNum",
							tabIndex : 37
						}, {
							xtype : "numberfield",
							fieldLabel : "MOQ",
							anchor : "100%",
							decimalPrecision : 0,
							tabIndex : 39,
							id : "eleMod",
							name : "eleMod",
							maxLength : 99999999999
						}, {
							xtype : "textfield",
							fieldLabel : "Level",
							anchor : "100%",
							id : "eleGrade",
							name : "eleGrade",
							tabIndex : 41
						}, {
							xtype : "numberfield",
							fieldLabel : "模具费用",
							anchor : "100%",
							tabIndex : 43,
							id : "moldCharge",
							name : "moldCharge",
							maxLength : ""
						}, {
							xtype : "numberfield",
							fieldLabel : "Volume",
							anchor : "100%",
							tabIndex : 45,
							id : "cube",
							name : "cube"
						}]
			}, {
				xtype : "panel",
				layout : "form",
				columnWidth : 1,
				items : [{
							xtype : "textarea",
							fieldLabel : "Description:",
							anchor : "100%",
							height : 40,
							tabIndex : 46,
							id : "eleName",
							name : "eleName",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "Description:",
							anchor : "100%",
							height : 40,
							tabIndex : 47,
							id : "eleNameEn",
							name : "eleNameEn",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "Description:",
							anchor : "100%",
							tabIndex : 48,
							height : 40,
							id : "eleDesc",
							name : "eleDesc",
							maxLength : 500
						}, {
							xtype : "textfield",
							fieldLabel : "Barcode",
							anchor : "100%",
							tabIndex : 49,
							id : "barcode",
							name : "barcode",
							maxLength : 30
						}, eleHsidBox, {
							xtype : "textarea",
							fieldLabel : "Remark",
							anchor : "100%",
							height : 40,
							tabIndex : 50,
							id : "eleRemark",
							name : "eleRemark",
							maxLength : 500
						}]
			}]
		}, {
			xtype : "fieldset",
			title : "Packaging Information",
			layout : "column",
			anchor : '97%',
			items : [{
				xtype : "panel",
				title : "",
				columnWidth : 0.72,
				layout : "column",
				items : [{
							xtype : "panel",
							title : "",
							columnWidth : 1,
							layout : "form",
							items : [boxPacking, inputGridTypeBox]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.5,
							layout : "form",
							items : [{
										xtype : "numberfield",
										fieldLabel : "Packing:",
										anchor : "100%",
										tabIndex : 55,
										id : "boxPbCount",
										name : "boxPbCount",
										maxValue : 99999999
									}, {
										xtype : "numberfield",
										fieldLabel : "Packing:",
										anchor : "100%",
										tabIndex : 58,
										id : "boxIbCount",
										name : "boxIbCount",
										maxValue : 99999999
									}, {
										xtype : "numberfield",
										fieldLabel : "Packing:",
										anchor : "100%",
										tabIndex : 61,
										id : "boxMbCount",
										name : "boxMbCount",
										maxLength : 99999999
									}, {
										xtype : "numberfield",
										fieldLabel : "Packing:",
										anchor : "100%",
										tabIndex : 64,
										id : "boxObCount",
										name : "boxObCount",
										maxLength : 99999999,
										listeners : {
											'change' : function() {
												setMapValue(this);
											}
										}
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.5,
							layout : "form",
							labelWidth : 6,
							items : [boxPbTypeBox, boxIbTypeBox, boxMbTypeBox,
									boxObTypeBox]
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.28,
				layout : "form",
				labelWidth : 41,
				items : [{
							xtype : "numberfield",
							fieldLabel : "Unit Price",
							anchor : "100%",
							tabIndex : 52,
							id : "packingPrice",
							name : "packingPrice"
						}, {
							xtype : "numberfield",
							fieldLabel : "Price",
							anchor : "100%",
							tabIndex : 54,
							id : "inputGridPrice",
							name : "inputGridPrice",
							maxValue : 99999999.99,
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPackPriceAndPriceFac();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Price",
							anchor : "100%",
							tabIndex : 57,
							id : "boxPbPrice",
							name : "boxPbPrice",
							maxLength : 99999999.99,
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPackPriceAndPriceFac();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Price",
							anchor : "100%",
							tabIndex : 60,
							id : "boxIbPrice",
							name : "boxIbPrice",
							maxValue : 99999999.99,
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPackPriceAndPriceFac();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Price",
							anchor : "100%",
							tabIndex : 63,
							id : "boxMbPrice",
							name : "boxMbPrice",
							maxLength : 99999999.99,
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPackPriceAndPriceFac();
								}
							}
						}, {
							xtype : "textfield",
							fieldLabel : "Price",
							anchor : "100%",
							id : "boxObPrice",
							name : "boxObPrice",
							tabIndex : 66,
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPackPriceAndPriceFac();
								}
							}
						}, {
							xtype : "hidden",
							id : "priceFac",
							name : "priceFac"
						}]
			}, {
				xtype : "panel",
				title : "",
				layout : "form",
				columnWidth : 0.5,
				items : [{
							xtype : "numberfield",
							fieldLabel : "CBM",
							anchor : "100%",
							tabIndex : 67,
							id : "boxCbm",
							name : "boxCbm"
						}, {
							xtype : "numberfield",
							fieldLabel : "N.W./KG",
							anchor : "100%",
							tabIndex : 69,
							id : "boxNetWeigth",
							name : "boxNetWeigth",
							maxValue : 99999999.99
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				items : [{
							xtype : "numberfield",
							fieldLabel : "Weight/G",
							anchor : "100%",
							id : "boxWeigth",
							name : "boxWeigth",
							tabIndex : 68,
							listeners : {
								'change' : function() {
									calWeighByEleFrame(this.value)
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "G.W./KG",
							anchor : "100%",
							tabIndex : 70,
							id : "boxGrossWeigth",
							name : "boxGrossWeigth",
							maxValue : 99999999.99
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				labelWidth : 70,
				items : [{
							xtype : "numberfield",
							fieldLabel : "摆放方式",
							anchor : "100%",
							tabIndex : 71,
							id : "putL",
							name : "putL",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Product Size(CM)",
							anchor : "100%",
							tabIndex : 74,
							id : "boxL",
							name : "boxL",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 77,
							id : "boxLInch",
							name : "boxLInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Packing Size(CM)",
							anchor : "100%",
							tabIndex : 80,
							id : "boxPbL",
							name : "boxPbL",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 83,
							id : "boxPbLInch",
							name : "boxPbLInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Inner Box(CM)",
							anchor : "100%",
							tabIndex : 86,
							id : "boxIbL",
							name : "boxIbL",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 89,
							id : "boxIbLInch",
							name : "boxIbLInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "中盒包装CM",
							anchor : "100%",
							tabIndex : 92,
							id : "boxMbL",
							name : "boxMbL",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 95,
							id : "boxMbLInch",
							name : "boxMbLInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "Outer Carton(CM)",
							anchor : "100%",
							tabIndex : 98,
							id : "boxObL",
							name : "boxObL",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 101,
							id : "boxObLInch",
							name : "boxObLInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.25,
				layout : "form",
				labelWidth : 8,
				items : [{
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 72,
							id : "putW",
							name : "putW",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 75,
							id : "boxW",
							name : "boxW",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 78,
							id : "boxWInch",
							name : "boxWInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 81,
							id : "boxPbW",
							name : "boxPbW",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 84,
							id : "boxPbWInch",
							name : "boxPbWInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 87,
							id : "boxIbW",
							name : "boxIbW",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 90,
							id : "boxIbWInch",
							name : "boxIbWInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 93,
							id : "boxMbW",
							name : "boxMbW",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 96,
							id : "boxMbWInch",
							name : "boxMbWInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 99,
							id : "boxObW",
							name : "boxObW",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 102,
							id : "boxObWInch",
							name : "boxObWInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.25,
				layout : "form",
				labelWidth : 8,
				items : [{
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 73,
							id : "putH",
							name : "putH",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 76,
							id : "boxH",
							name : "boxH",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 79,
							id : "boxHInch",
							name : "boxHInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 82,
							id : "boxPbH",
							name : "boxPbH",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 85,
							id : "boxPbHInch",
							name : "boxPbHInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 88,
							id : "boxIbH",
							name : "boxIbH",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 91,
							id : "boxIbHInch",
							name : "boxIbHInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 94,
							id : "boxMbH",
							name : "boxMbH",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 97,
							id : "boxMbHInch",
							name : "boxMbHInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 100,
							id : "boxObH",
							name : "boxObH",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 103,
							id : "boxObHInch",
							name : "boxObHInch",
							listeners : {
								'blur' : function() {
									setMapValue(this);
								},
								'change' : function() {
									calPrice();
								}
							}
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 1,
				layout : "form",
				items : [{
							xtype : "textarea",
							fieldLabel : "Size Description",
							anchor : "100%",
							height : 40,
							tabIndex : 104,
							id : "eleSizeDesc",
							name : "eleSizeDesc",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "Size Description",
							anchor : "100%",
							height : 40,
							tabIndex : 105,
							id : "eleInchDesc",
							name : "eleInchDesc",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "口径描述",
							anchor : "100%",
							height : 40,
							tabIndex : 106,
							id : "boxTDesc",
							name : "boxTDesc",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "底径描述",
							anchor : "100%",
							height : 40,
							tabIndex : 107,
							id : "boxBDesc",
							name : "boxBDesc",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "Sales Unit",
							anchor : "100%",
							height : 40,
							tabIndex : 109,
							id : "boxRemarkCn",
							name : "boxRemarkCn",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "Sales Unit",
							anchor : "100%",
							height : 40,
							tabIndex : 109,
							id : "boxRemark",
							name : "boxRemark",
							maxLength : 500
						}]
			}]
		}, {
			xtype : "fieldset",
			title : "Loading information",
			anchor : '97%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 35,
						items : [{
									xtype : "numberfield",
									fieldLabel : "20\"",
									anchor : "100%",
									tabIndex : 109,
									id : "box20Count",
									name : "20' Container:",
									maxLength : 1000000000000000000
								}, {
									xtype : "numberfield",
									fieldLabel : "40HQ\"",
									anchor : "100%",
									tabIndex : 111,
									id : "box40hqCount",
									name : "40 HC Container:",
									maxLength : 1000000000000000000
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 30,
						items : [{
									xtype : "numberfield",
									fieldLabel : "40\"",
									anchor : "100%",
									tabIndex : 110,
									id : "box40Count",
									name : "40' Container:",
									maxLength : 1000000000000000000
								}, {
									xtype : "numberfield",
									fieldLabel : "45\"",
									anchor : "100%",
									tabIndex : 112,
									id : "box45Count",
									name : "45' Container:",
									maxLength : 1000000000000000000
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
		width : "28%",
		collapsible : true,
		collapsed : true,
		listeners : {
			'beforeadd' : function(pnl, component, index) {
				pnl.getEl().mask("Loading...", 'x-mask-loading');
			},
			'afterlayout' : function(pnl) {
				pnl.getEl().unmask();
				imgflag += 1;
				// 获取sm选择行
				var selectRec = sm.getSelected();
				if (selectRec != null) {
					initForm(selectRec);
				}

				// 给表单加事件
				var items = rightForm.getForm().items;
				for (var i = 0; i < items.getCount(); i++) {
					var item = items.get(i);
					// 去掉无名的
					if (!item.getName())
						continue;
					item.on('change', function(txt, newVal, oldVal) {
						if ($('rdm').value != "") {
							updateMapValue($('rdm').value, txt.getName(),
									newVal);
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
					initForm(ds.getAt(rowIndex));
				}
			});

	// 初始化页面,加载报表详细信息
	function initForm(record) {
		var eleId = record.data.eleId;
		var type = record.data.type;
		var rdm = record.data.rdm;
		var flag = true;
		if (isNaN(record.id)) {
			flag = false;
		}
		// flag区分是点击已存在行调用(true)还是新添加(false)
		if (flag) {
			Ext.getCmp("upmod").show();
			Ext.getCmp("updel").show();
		} else {
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
		}
		var isSelPic = getPdmByOtherUrl("cotpicture.do", "SEL");
		var popdom = false;
		if (isSelPic == 0) {// 没有查看图片信息权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
			popdom = true;
		}
		var isMod = getPopedomByOpType("cotorderfac.do", "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
		}
		if (eleId == null || eleId == "") {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
			return;
		}
		DWREngine.setAsync(false);
		cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
			if (res != null) {
				rightForm.getForm().setValues(res);
				if (res.boxTypeId == null) {
					boxPacking.setValue('');
				} else {
					boxPacking.bindValue(res.boxTypeId);
				}
				if (res.boxPbTypeId == null) {
					boxPbTypeBox.setValue('');
				} else {
					boxPbTypeBox.bindValue(res.boxPbTypeId);
				}
				if (res.boxIbTypeId == null) {
					boxIbTypeBox.setValue('');
				} else {
					boxIbTypeBox.bindValue(res.boxIbTypeId);
				}
				if (res.boxMbTypeId == null) {
					boxMbTypeBox.setValue('');
				} else {
					boxMbTypeBox.bindValue(res.boxMbTypeId);
				}
				if (res.boxObTypeId == null) {
					boxObTypeBox.setValue('');
				} else {
					boxObTypeBox.bindValue(res.boxObTypeId);
				}
				if (res.inputGridTypeId == null) {
					inputGridTypeBox.setValue('');
				} else {
					inputGridTypeBox.bindValue(res.inputGridTypeId);
				}

				if (res.eleTypeidLv1 == null) {
					typeLv1Box.setValue('');
				} else {
					typeLv1Box.bindPageValue("CotTypeLv1", "id",
							res.eleTypeidLv1);
				}
				if (res.eleTypeidLv2 == null) {
					typeLv2Box.setValue('');
				} else {
					typeLv2Box.bindPageValue("CotTypeLv2", "id",
							res.eleTypeidLv2);
				}
				if (res.eleHsid == null) {
					eleHsidBox.setValue('');
				} else {
					eleHsidBox.bindPageValue("CotEleOther", "id", res.eleHsid);
				}

				if (imgflag >= 2) {

					if (res.id == null || res.id == '') {
						if (res.type == 'order') {
							$('picPath').src = "./showPicture.action?flag=order&detailId="
									+ res.orderDetailId;
						}
					} else {
						$('picPath').src = "./showPicture.action?detailId="
								+ res.id + "&flag=orderfac";
					}
				} else {
					// $('picPath').src = "./showPicture.action?flag=noPic";
				}
			}
		});
		DWREngine.setAsync(true);
	}

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	grid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格点击后,让单元格的editor适应行高度
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
		DWREngine.setAsync(false);
		// setEleFrameValue(e.field, e.value);
		if (e.field == 'boxCbm') {
			changeCbm(e.record.data.rdm, e.value)
		}
		if (e.field == 'boxObCount') {
			boxOcountNum(e.record.data.rdm, e.value);
		}

		// 如果是产品尺寸CM
		if (e.field == 'boxObL' || e.field == 'boxObW' || e.field == 'boxObH') {
			changeInch(e.field, e.field + 'Inch', 0, e.record.data.rdm, e.value);
		}

		cotOrderFacService.updateOrderFacMapValueByRdm(e.record.data.rdm,
				e.field, e.value, function(res) {
					if (rightForm.isVisible()) {
						var field = rightForm.getForm().findField(e.field);
						if (field != null) {
							field.setValue(e.value);
						}
					}
				});
		DWREngine.setAsync(true);
	});

	// 改变值传入对应值
	function setEleFrameValue(name, value) {
		DWRUtil.setValue(name, value);
	}

	// 生产价改变事件
	function priceFacChange(newVal, flag) {

		if (newVal == '') {
			newVal = 0;
		}
		var rec = editRec;
		var rdm = rec.data.rdm;
		cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
					if (res != null) {

						var boxCount = rec.data.boxCount;

						// 计算金额
						rec.set("totalFac", newVal * boxCount);
						updateMapValue(rdm, 'totalFac', newVal * boxCount);
					}
				});
		if (flag == 2) {
			updateMapValue(rdm, 'priceFac', newVal);
		}
	}

	// 数量值改变事件(改变箱数,金额)
	function boxCountChange(newVal) {
		if (newVal == '') {
			newVal = 0;
		}

		var containerCount = 0;
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			DWREngine.setAsync(false);
			cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
						if (res != null) {

							var boxObCount = rec.data.boxObCount;
							var priceFac = rec.data.priceFac;

							// 计算箱数
							if (newVal % boxObCount != 0) {
								containerCount = parseInt(newVal / boxObCount)
										+ 1;
							} else {
								containerCount = newVal / boxObCount;
							}
							rec.set("containerCount", containerCount);
							// 计算金额
							rec.set("totalFac", newVal * priceFac);

							updateMapValue(rdm, 'containerCount',
									containerCount);
							updateMapValue(rdm, 'totalFac', newVal * priceFac);
						}
					});
			DWREngine.setAsync(true);
		}
	}

	// 箱数值改变事件(改变数量,金额)
	function containerCountChange(newVal) {
		if (newVal == '') {
			newVal = 0;
		}

		var boxCount = 0;
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			DWREngine.setAsync(false);
			cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
						if (res != null) {

							var boxObCount = rec.data.boxObCount;
							var priceFac = rec.data.priceFac;

							boxCount = newVal * boxObCount
							rec.set("boxCount", boxCount);
							// 计算金额
							rec.set("totalFac", boxCount * priceFac);

							updateMapValue(rdm, 'boxCount', boxCount);
							updateMapValue(rdm, 'totalFac', boxCount * priceFac);
						}
					});
			DWREngine.setAsync(true);
		}
	}

	// 外箱数值改变事件
	function boxOcountNum(rdm, boxObCount) {
		if (boxObCount == '' || isNaN(boxObCount)) {
			boxObCount = 0;
		}
		boxObCount = parseInt(boxObCount);
		cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
			if (res != null) {
				var con20 = $('con20').value;
				var con40H = $('con40H').value;
				var con40 = $('con40').value;
				var con45 = $('con45').value;
				// CBM
				var boxCbm = $('boxCbm').value;
				if (boxCbm != '' && boxCbm != 0) {
					var box20Count = Math.floor(con20 / boxCbm) * boxObCount;
					var box40Count = Math.floor(con40 / boxCbm) * boxObCount;
					var box40hqCount = Math.floor(con40H / boxCbm) * boxObCount;
					var box45Count = Math.floor(con45 / boxCbm) * boxObCount;
					res.box20Count = box20Count;
					res.box40Count = box40Count;
					res.box40hqCount = box40hqCount;
					res.box45Count = box45Count;

					setEleFrameValue('box20Count', box20Count);
					setEleFrameValue('box40Count', box40Count);
					setEleFrameValue('box40hqCount', box40hqCount);
					setEleFrameValue('box45Count', box45Count);
				}
				res.boxObCount = boxObCount;
				calWeighByEleFrame($('boxWeigth').value);
				// 将对象储存到后台map中
				cotOrderFacService.setOrderFacMapValueByRdm(rdm, res, function(
								res) {
						});
			}
		});
	}

	// 尺寸和英寸转换
	function changeInch(fromObject, toObject, flag, rdm, value) {
		DWREngine.setAsync(false);
		if (value == '') {
			$(toObject).value = '';
			cotOrderFacService.updateOrderFacMapValueByRdm(rdm, toObject, '',
					function(res) {
					});
			return;
		} else {
			if (isNaN(value)) {
				value = '';
				if (toObject == 'boxObL' || toObject == 'boxObW'
						|| toObject == 'boxObH') {
					$('boxCbm').value = '';
				}
				// setMapValue($('boxCbm'));
				return;
			} else {
				var L = $('boxObL').value;
				var W = $('boxObW').value;
				var H = $('boxObH').value;
				$('boxCbm').value = (L * W * H * 0.000001).toFixed("4");
				changeCbm(rdm, $('boxCbm').value);
			}
		}
		if (flag == 0) {
			$(toObject).value = (value / 2.54).toFixed("2");
		} else {
			$(toObject).value = round((value * 2.54), 2);
		}
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, toObject,
				$(toObject).value, function(res) {
				});
		DWREngine.setAsync(true);
	}

	// cbm的更改事件,计算出装箱数
	function changeCbm(rdm, cbm) {
		DWREngine.setAsync(false);
		cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
					if (res != null) {
						var boxObCount = res.boxObCount;// 外装数

						var con20 = $('con20').value;
						var con40H = $('con40H').value;
						var con40 = $('con40').value;
						var con45 = $('con45').value;
						if (isNaN(boxObCount)) {
							boxObCount = 0;
						}
						if (cbm != '' && cbm != 0) {
							var box20Count = Math.floor(con20 / cbm)
									* boxObCount;
							var box40Count = Math.floor(con40 / cbm)
									* boxObCount;
							var box40hqCount = Math.floor(con40H / cbm)
									* boxObCount;
							var box45Count = Math.floor(con45 / cbm)
									* boxObCount;

							res.box20Count = box20Count;
							res.box40Count = box40Count;
							res.box40hqCount = box40hqCount;
							res.box45Count = box45Count;
						} else {
							res.box20Count = 0;
							res.box40Count = 0;
							res.box40hqCount = 0;
							res.box45Count = 0;
						}
						res.boxCbm = cbm;
						// 计算cuft
						res.boxCuft = (cbm * 35.315).toFixed("3");

						// setEleFrameValue('boxCbm',cbm);
						setEleFrameValue('box20Count', box20Count);
						setEleFrameValue('box40Count', box40Count);
						setEleFrameValue('box40hqCount', box40hqCount);
						setEleFrameValue('box45Count', box45Count);
						// 将对象储存到后台map中
						cotOrderFacService.setOrderFacMapValueByRdm(rdm, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 改变包装方案修改价格
	function modifyPrice(rdm) {

		var elements = new CotElementsNew();

		elements.id = $('id').value;
		// 计算生产价
		var typeId = $('boxTypeId').value;
		var boxObCount = $('boxObCount').value;
		var boxMbCount = $('boxMbCount').value;
		var boxIbCount = $('boxIbCount').value;
		var boxPbCount = $('boxPbCount').value;
		var boxIbPrice = 0;
		var boxMbPrice = 0;
		var boxObPrice = 0;
		var boxPbPrice = 0;
		var packingPrice = 0;
		var inputGridPrice = 0;

		var boxIbTypeId = $('boxIbTypeId').value;
		var boxMbTypeId = $('boxMbTypeId').value;
		var boxObTypeId = $('boxObTypeId').value;
		var boxPbTypeId = $('boxPbTypeId').value;
		var inputGridTypeId = $('inputGridTypeId').value;

		DWREngine.setAsync(false);
		if (boxIbTypeId != null && boxIbTypeId != ''
				&& parseInt(boxIbCount) > 0) {

			elements.boxIbL = $('boxIbL').value;
			elements.boxIbW = $('boxIbW').value;
			elements.boxIbH = $('boxIbH').value;

			cotElementsService.calPrice(elements, parseInt(boxIbTypeId),
					function(ibPrice) {
						if (ibPrice != null) {
							boxIbPrice = (parseFloat(ibPrice)
									* parseInt(boxObCount) / parseInt(boxIbCount))
									.toFixed(3);
							$('boxIbPrice').value = boxIbPrice;
						}
					});
		}
		if (boxMbTypeId != null && boxMbTypeId != ''
				&& parseInt(boxMbCount) > 0) {

			elements.boxMbL = $('boxMbL').value;
			elements.boxMbW = $('boxMbW').value;
			elements.boxMbH = $('boxMbH').value;

			cotElementsService.calPrice(elements, parseInt(boxMbTypeId),
					function(mbPrice) {
						if (mbPrice != null) {
							boxMbPrice = (parseFloat(mbPrice)
									* parseInt(boxObCount) / parseInt(boxMbCount))
									.toFixed(3);
							$('boxMbPrice').value = boxMbPrice;
						}
					});
		}
		if (boxObTypeId != null && boxObTypeId != ''
				&& parseInt(boxObCount) > 0) {

			elements.boxObL = $('boxObL').value;
			elements.boxObW = $('boxObW').value;
			elements.boxObH = $('boxObH').value;

			cotElementsService.calPrice(elements, parseInt(boxObTypeId),
					function(obPrice) {
						if (obPrice != null) {
							boxObPrice = parseFloat(obPrice).toFixed(3);
							$('boxObPrice').value = boxObPrice;
						}
					});
		}
		if (boxPbTypeId != null && boxPbTypeId != ''
				&& parseInt(boxPbCount) > 0) {

			elements.boxPbL = $('boxPbL').value;
			elements.boxPbW = $('boxPbW').value;
			elements.boxPbH = $('boxPbH').value;

			cotElementsService.calPrice(elements, parseInt(boxPbTypeId),
					function(pbPrice) {
						if (pbPrice != null) {
							boxPbPrice = (parseFloat(pbPrice)
									* parseInt(boxObCount) / parseInt(boxPbCount))
									.toFixed(3);
							$('boxPbPrice').value = boxPbPrice;
						}
					});
		}
		if (inputGridTypeId != null && inputGridTypeId != '') {

			elements.putL = $('putL').value;
			elements.putW = $('putW').value;
			elements.putH = $('putH').value;

			cotElementsService.calPrice(elements, parseInt(inputGridTypeId),
					function(igPrice) {
						if (igPrice != null) {
							$('inputGridPrice').value = igPrice;
						}
					});
		} else {
			$('inputGridPrice').value = 0;
		}

		if (boxObCount != 0 && boxObCount != '') {
			packingPrice = ((parseFloat(boxIbPrice) + parseFloat(boxMbPrice)
					+ parseFloat(boxPbPrice) + parseFloat(boxObPrice) + parseFloat($('inputGridPrice').value)) / parseInt(boxObCount))
					.toFixed(3);
		}
		$('packingPrice').value = packingPrice;

		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxIbPrice',
				$('boxIbPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxMbPrice',
				$('boxMbPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxObPrice',
				$('boxObPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxPbPrice',
				$('boxPbPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'inputGridPrice',
				$('inputGridPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'packingPrice',
				$('packingPrice').value, function(res) {
				});
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'priceFac',
				$('priceFac').value, function(res) {
				});

		cotOrderFacService.calPriceFacByPackPrice(rdm, packingPrice, function(
						facprice) {

					$('priceFac').value = facprice;
					var rec = editRec;
					rec.set("priceFac", facprice);

				});

		DWREngine.setAsync(true);

	}

	// 右键菜单
	var rightMenu = new Ext.menu.Menu({
				id : "rightMenu",
				items : [{
							text : "Image Export",
							handler : downPics
						}, {
							text : "Synchronize With The Order",
							handler : showTongDiv
						}, {
							text : "View historical quotes",
							handler : showHisPricePanel
						}]
			});
	function rightClickFn(client, rowIndex, e) {
		e.preventDefault();
		rightMenu.showAt(e.getXY());
	}
	grid.on("rowcontextmenu", rightClickFn);

	// 获得表格选择行
	function getIds() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 采购单同步到订单
	var chooseWin;
	function showTongDiv() {
		var isPopedom = getPopedomByOpType(vaildUrl, "ORDERFACTOORDER");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('Message', "Sorry, you do not have Authority!");
			return;
		}
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Pls. select one record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "you can select only one!")
			return;
		}
		chooseWin = new TongChooseWin({
					flag : 2
				});
		chooseWin.show();
	}

	// 隐藏同步选择层
	this.hideRightMenu = function() {
		chooseWin.close();
	}

	// 更新到样品表
	this.updateToOrder = function(eleStr, boxStr, otherStr, isPic) {

		var mapEle = new Map();
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					if (item.data.eleId.trim() != '') {
						// 判断货号是否重复,重复取后一条去覆盖
						mapEle.put(item.data.eleId, item.data.rdm);
					}
				});

		if (mapEle.getKeys().length == 0) {
			Ext.MessageBox.alert('Message', 'Pls. select the record you wannt to synchronize with the order!');
			return;
		}
		chooseWin.close();
		var key = mapEle.getKeys();
		DWREngine.setAsync(false);
		mask();
		DWREngine.setAsync(true);
		cotOrderFacService.findIsExistInOrder(key, function(res) {
					var sameTemp = '';
					var same = res.same;
					// 获得存在订单明细的货号的随机数
					var sameVal = new Array();
					for (var i = 0; i < same.length; i++) {
						sameTemp = same[0];
						sameVal.push(mapEle.get(same[i]));
					}
					// 获得不存在订单明细的货号的随机数
					var dis = res.dis;
					var disVal = new Array();
					for (var i = 0; i < dis.length; i++) {
						disVal.push(mapEle.get(dis[i]));
					}
					if (same.length != 0) {
						Ext.MessageBox.confirm('Message', 'The order details exists' + sameTemp
										+ "Replace the order details or not?", function(btn) {
									if (btn == 'yes') {
										cotOrderFacService.updateToOrderDetail(
												same, sameVal, dis, disVal,
												eleStr, boxStr, otherStr,
												isPic, function(res) {
													Ext.MessageBox.alert(
															'Message', 'synchronize successful!');
												});
									} else {
										if (dis.length != 0) {
											cotOrderFacService
													.updateToOrderDetail(null,
															null, dis, disVal,
															eleStr, boxStr,
															otherStr, isPic,
															function(res) {
																Ext.MessageBox
																		.alert(
																				'Message',
																				'synchronize successful!');
															});
										}
									}
								});
					} else {
						if (dis.length != 0) {
							cotOrderFacService.updateToOrderDetail(null, null,
									dis, disVal, eleStr, boxStr, otherStr,
									isPic, function(res) {
										Ext.MessageBox.alert('Message', 'synchronize successful!');
									});
						}
					}
					unmask();
				});
	}

	// 查看历史报价
	function showHisPricePanel() {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Pls. select one record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "you can select only one!")
			return;
		}
		var hispriceGrid = new HisPriceGrid({
					facId : $('factoryId').value,
					eleId : sm.getSelected().get("eleId"),
					dblFn : replacePrice
				});
		var win = new Ext.Window({
					id : "hisWin",
					title : "historical price",
					width : 800,
					height : 500,
					modal : true,
					items : [hispriceGrid]
				})
		win.show();
		hispriceGrid.load();
	}
	// 下载多张图片.并且压缩.zip
	function downPics() {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Pls. select one record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "you can select only one!")
			return;
		}
		var temp = "";
		var eleId = $('eleId').value;
		if (eleId != '') {
			temp += eleId + ",";
		}
		var priceNo = $('orderNo').value;
		var form = document.getElementById("rightForm");
		form.action = "./downPics.action?priceNo=" + encodeURIComponent(priceNo)
				+ "&tp=orderfac&rdms=" + temp;
		form.target = "";
		form.method = "post";
		form.submit();
	}

	// 判断单号是否重复参数
	var isExist = true;
	var orderfacForm = new Ext.form.FormPanel({
		title : "Basic information Of Procurement Contracts-(It is Required when the font color is red)",
		labelWidth : 60,
		formId : "orderfacForm",
		labelAlign : "right",
		region : 'north',
		layout : "form",
		width : "100%",
		height : 160,
		frame : true,
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [facBox, curBox, empsBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [{
							xtype : "panel",
							title : "",
							layout : "column",
							items : [{
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.9,
								items : [{
									xtype : "textfield",
									fieldLabel : "<font color='red'>Order No.</font>",
									anchor : "100%",
									id : "orderNo",
									name : "orderNo",
									maxLength : 100,
									allowBlank : false,
									blankText : "Pls. Input the Order No.！",
									tabIndex : 2
								}]
							}, {
								xtype : "panel",
								title : "",
								layout : "form",
								columnWidth : 0.1,
								items : [{
											xtype : "button",
											width : 20,
											text : "",
											cls : "SYSOP_ADD",
											iconCls : "page_add",
											handler : getAutoOrderfacNO,
											listeners : {
												"render" : function(obj) {
													var tip = new Ext.ToolTip({
																target : obj
																		.getEl(),
																anchor : 'top',
																maxWidth : 160,
																minWidth : 160,
																html : 'According to Order Number Configuration Produce Order No. Automatically!'
															});
												}
											}
										}]
							}]
						}, companyBox, facContactBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [{
									xtype : "datefield",
									fieldLabel : "Date",
									anchor : "100%",
									id : "orderTime",
									name : "orderTime",
									format : "Y-m-d",
									allowBlank : true,
									tabIndex : 3
								}, {
									xtype : "textfield",
									fieldLabel : "Signed Location",
									anchor : "100%",
									id : "orderAddress",
									name : "orderAddress",
									maxLength : 200,
									allowBlank : true,
									tabIndex : 7
								}, orderStatusBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						items : [{
									xtype : "datefield",
									fieldLabel : "Delivery Date",
									anchor : "100%",
									format : "Y-m-d",
									id : "sendTime",
									name : "sendTime",
									allowBlank : true,
									tabIndex : 4
								}, {
									xtype : "textfield",
									fieldLabel : "Delivery Location",
									anchor : "100%",
									id : "givenAddress",
									name : "givenAddress",
									maxLength : 200,
									allowBlank : true,
									tabIndex : 8
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "form",
						items : [{
									xtype : "textarea",
									fieldLabel : "Remark",
									anchor : "100%",
									height : 40,
									id : "orderRemark",
									name : "orderRemark",
									maxLength : 1000,
									allowBlank : true,
									tabIndex : 18
								}]
					}]
		}]
	})

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "Details Of Procurement",
				items : [grid, rightPanel]
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
			title : "Mark Photo",
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
						+ '<img src="common/images/zwtp.png" id="order_MB" name="order_MB"'
						+ 'onload="javascript:DrawImage(this,180,180)" onclick="showBigPicDiv(this)"/></div>',
				buttons : [{
							width : 60,
							text : "Change",
							id : "upmodMai",
							iconCls : "upload-icon",
							handler : showUploadMaiPanel
						}
				// , {
				// width : 60,
				// text : "删除",
				// id : "updelMai",
				// iconCls : "upload-icon-del",
				// handler : delMBPic
				// }
				]
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
								title : "Shiping Mark:",
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
								title : "Shiping Mark:",
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
								title : "Shiping Mark:",
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
								title : "Shiping Mark:",
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
							text : "Import The Mark From Order",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : ExtToDoFn
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
				items : [centerPanel, {
							id : "shenTab",
							name : 'shenTab',
							title : "Review Record",
							layout : 'fit',
							items : [{
										xtype : 'htmleditor',// html文本编辑器控件
										enableLinks : false,
										id : 'checkReason',
										name : 'checkReason'
									}]
						}, {
							id : "contractTab",
							name : "contractTab",
							title : "Terms of the contract",
							layout : 'fit',
							items : [{
										xtype : 'htmleditor',// html文本编辑器控件
										enableLinks : false,
										id : 'orderClause',
										name : 'orderClause'
									}]
						}, {
							id : "maiTab",
							name : "maiTab",
							title : "Shipping Mark",
							layout : 'fit',
							items : [maiPanel]
						}],
				buttonAlign : 'center',
				buttons : [{
							text : "Save",
							cls : "SYSOP_ADD",
							id : "saveBtn",
							handler : save,
							iconCls : "page_table_save"
						}, {
							text : "Delete",
							id : "delBtn",
							hidden : true,
							handler : del,
							iconCls : "page_del"
						}, {
							text : "Sort Again",
							iconCls : "page_fen",
							handler : reSort
						}, {
							text : "Print",
							id : "printBtn",
							handler : showPrint,
							iconCls : "page_print"
						}, {
							text : "Cancel",
							id : "cancelBtn",
							iconCls : "page_cancel",
							handler : function() {
								closeandreflashEC(true, 'orderfacGrid', false);
							}
						}, {
							text : "Need to be reviewed",
							id : 'requestBtn',
							handler : requestCheck,
							iconCls : "page_from"
						}, {
							text : "Pass",
							id : 'passBtn',
							handler : passCheck,
							hidden : true,
							iconCls : "page_from"
						}, {
							text : "Not Pass",
							id : 'unpassBtn',
							hidden : true,
							handler : unpassCheck,
							iconCls : "page_from"
						}, {
							text : "Review Again",
							id : 'recheckBtn',
							handler : reCheck,
							hidden : true,
							iconCls : "page_from"
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [tbl, orderfacForm]
			});
	viewport.doLayout();

	// 判断是否修改麦标
	var oldFlag = false;

	// 编辑页面时加载审核原因
	tbl.on('tabchange', function(tb, pnl) {
		if (pnl.name == 'shenTab') {
			Ext.getCmp("checkReason").setValue(chkReason);
		}
		if (pnl.name == 'contractTab') {
			Ext.getCmp("orderClause").setValue(conArea);
		}
		if (pnl.name == 'maiTab') {
			// 加载麦标
			var id = $('pId').value;
			if (id != 'null' && id != '') {
				$('order_MB').src = "./showPicture.action?flag=orderFacMB&detailId="
						+ id + "&temp=" + Math.random();
				Ext.getCmp('upmodMai').show();
			}
		}
	});

	// 审核原因
	var chkReason = "";
	// 合同条款
	var conArea = "";
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
		// 清空Map
//		cotOrderFacService.clearOrderFacMap(function(res) {
//				});
		// 加载集装箱的柜体积
		queryService.getContainerCube(function(res) {
					$('con20').value = res[0];
					$('con40').value = res[1];
					$('con40H').value = res[2];
					$('con45').value = res[3];
				});
		// 送样单编号
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			// 初始化下单时间为当前时间
			var orderTime = Ext.getCmp("orderTime");
			var date = new Date();
			orderTime.setValue(date);

			// 隐藏按钮
			Ext.getCmp('delBtn').hide();
			Ext.getCmp('printBtn').hide();
			Ext.getCmp('requestBtn').hide();

			// 加载默认公司
			cotGivenService.getDefaultCompanyId(function(company) {
						if (company != null) {
							companyBox.bindPageValue("CotCompany", "id",
									company);
						}
					});

			// 是否审核(0:不审核 1：审核)
			cotGivenService.getList('CotPriceCfg', function(cfg) {
						if (cfg.length != 0) {
							if (cfg[0].isCheck == 0) {
								// 隐藏审核记录及审核相关按钮
								Ext.getCmp('shenTab').disable();
								orderStatusBox.setValue(9);
							} else {
								orderStatusBox.setValue(0);
							}
						}
					});

			// 从数据字典中取生产合同合同条款
			cotOrderFacService.getContract(function(res) {
						// if (res != null) {
						// var con = '';
						// for (var i = 0; i < res.length; i++) {
						// con += res[i].contractContent + "；\n";
						// }
						// conArea = con;
						// }
						conArea = res;
					});
		} else {
			// 加载采购单信息
			cotOrderFacService.getOrderFacById(parseInt(id), function(res) {
				DWRUtil.setValues(res);
				chkReason = res.checkReason;
				// 初始配置值
				cotGivenService.getList('CotPriceCfg', function(cfg) {
							if (cfg.length != 0) {
								if (res.orderStatus == 9) {
									// 隐藏审核记录及审核相关按钮
									Ext.getCmp('shenTab').disable();
									Ext.getCmp('requestBtn').hide();
								} else {
									if (res.orderStatus == 0
											|| res.orderStatus == 1) {
										// $('checkyesBtn').style.display='none';
										// $('checknoBtn').style.display='none';
										// $('recheckBtn').style.display='none';
									} else if (res.orderStatus == 2) {
										Ext.getCmp('requestBtn').hide();
										Ext.getCmp('recheckBtn')
												.setVisible(true);
									} else {
										Ext.getCmp('requestBtn').hide();
										Ext.getCmp('passBtn').setVisible(true);
										Ext.getCmp('unpassBtn')
												.setVisible(true);
									}
								}
								orderStatusBox.setValue(res.orderStatus);
								// if (cfg.insertType != null) {
								// $('insertType').value = cfg.insertType;
								// } else {
								// $('insertType').value = 0;
								// }
							}
						});

				// 加载时间
				if (res.orderTime != null && res.orderTime != '') {
					var date = new Date(res.orderTime);
					var orderTime = Ext.getCmp("orderTime");
					orderTime.setValue(date);
				}

				if (res.sendTime != null && res.sendTime != '') {
					var date = new Date(res.sendTime);
					var sendTime = Ext.getCmp("sendTime");
					sendTime.setValue(date);
				}
				// 加载采购单总金额
				cotOrderFacService.findTotalMoney(parseInt(id), function(res) {
							cotOrderFacService.findTotalOtherFee(parseInt(id),
									function(otherfee) {
										$('totalLab').innerText = (res + otherfee)
												.toFixed('2');
									});
						});
				orderZMArea = res.orderZm;
				orderZHMArea = res.orderZhm;
				orderCMArea = res.orderCm;
				orderNMArea = res.orderNm;

				// 合同条款
				conArea = res.orderClause;
				curBox.bindValue(res.currencyId);

				facContactBox.bindPageValue("CotContact", "id",
						res.facContactId);
				facBox.bindPageValue("CotFactory", "id", res.factoryId);
				companyBox.bindPageValue("CotCompany", "id", res.companyId);
				empsBox.bindPageValue("CotEmps", "id", res.businessPerson);
			});
			// 分页基本参数
			ds.load({
						params : {
							start : 0,
							limit : 15,
							orderId : id,
							flag : 'orderFacDetail'
						}
					});
		}
		DWREngine.setAsync(true);

	}
	unmask();
	initform();

	// 添加空白record到表格中
	function addNewGrid() {
	}

	// 删除
	function onDel() {
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					var rdm = item.data.rdm;
					ds.remove(item);
					cotOrderFacService.delOrderFacMapByRdm(rdm, function(res) {
							});
				});
		rightForm.getForm().reset();
		$('picPath').src = "./showPicture.action?flag=noPic";
	}

	// 打开上传面板
	function showUploadPanel() {
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.Msg.alert('Message', 'Pls. choose the product！');
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
						detailId : id,
						flag : "orderfac"
					},
					waitMsg : "uploading...",
					opAction : opAction,
					imgObj : $('picPath'),
					imgUrl : "./showPicture.action?detailId=" + $('id').value
							+ "&flag=orderfac&" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadTempPic.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}

	// 删除图片
	function delPic() {
		if ($('orderStatus').value == 1 || $('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to modify it!');
			return;
		}
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.MessageBox.alert('Message', 'Pls. choose a order detail！');
			return;
		}
		Ext.MessageBox.confirm('Message', "Do you confirm delete the photo?", function(btn) {
					if (btn == 'yes') {
						var detailId = $('id').value;
						cotOrderFacService.deletePicImg(detailId,
								function(res) {
									if (res) {
										$('picPath').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox.alert('Message', 'Delete unsuccessful！');
									}
								})
					}
				});
	}

	// 获取自动分解的采购单号
	function getAutoOrderfacNO() {
		var factoryId = $("factoryId").value
		if (factoryId == 'null' || factoryId == "") {
			Ext.Msg.alert("Message", "Pls. choose the Supplier");
			return;
		}
		var orderId = $('oId').value;
		if (orderId != null && orderId != '' && factoryId != null
				&& factoryId != '') {
			setNoService.getCotOrderfacNO(parseInt(orderId),
					parseInt(factoryId), function(res) {
						$('orderNo').value = res;
					});
		}
		$('orderNo').focus();
	}

	// 删除
	function del() {
		var id = $('pId').value;
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0)// 没有删除权限
		{
			Ext.MessageBox.alert("Message", "Sorry, you do not have Authority!");
			return;
		}

		var checkFlag = false;
		DWREngine.setAsync(false);
		cotOrderFacService.getOrderFacById(id, function(orderfac) {
					if (orderfac.orderStatus == 2 && loginEmpId != "admin") {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			Ext.MessageBox.alert("Message", 'Sorry, the order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}

		var dealFlag = false;
		cotOrderFacService.getDealNumById(parseInt(id), function(dealNum) {
					if (dealNum != -1) {
						dealFlag = true;
					}
				});

		if (dealFlag) {
			Ext.MessageBox.alert("提示框", '对不起，该单已有应付帐款记录，不能删除！');
			return;
		}

		var otherFlag = false;
		cotOrderFacService.getOtherNumById(parseInt(id), function(otherNum) {
					if (otherNum != -1) {
						otherFlag = true;
					}
				});

		if (otherFlag) {
			Ext.MessageBox.alert("提示框", '对不起，该单已有其他费用导入订单，不能删除！');
			return;
		}
		var cotOrderFac = new CotOrderFac();
		var list = new Array();
		cotOrderFac.id = id;
		list.push(cotOrderFac);
		Ext.MessageBox.confirm('Message', 'Do you confirm to delete?', function(btn) {
			if (btn == 'yes') {
				cotOrderFacService.deleteOrderFacs(list, function(res) {
							if (res) {
								Ext.MessageBox.alert("Message", "Delete Successful");
								closeandreflashEC('true', 'orderfacGrid', false);
							} else {
								Ext.MessageBox.alert("Message", "Delete Unsuccessful！");
							}
						})
			}
		})
		DWREngine.setAsync(true);
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message", 'Sorry, you do not have Authority!');
			return;
		}
		// 如果该单没保存不能打印
		if ($('pId').value == 'null' || $('pId').value == '') {
			Ext.MessageBox.alert("Message", 'The order has not been saved,cat not print！');
			return;
		}

		if (printWin == null) {
			printWin = new PrintWin({
						type : 'orderfac',
						pId : 'pId',
						pNo : 'orderNo',
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

	// 编辑页面时判断是否有更改币种
	function checkIsChangeCur() {
		DWREngine.setAsync(false);
		var flag = false;
		var id = $('pId').value
		cotOrderFacService.getOrderFacById(parseInt(id), function(res) {
					if (res.currencyId != $('currencyId').value) {
						flag = true;
					}
				});
		return flag;
		DWREngine.setAsync(true);
	}

	// 保存
	function save() {

		DWREngine.setAsync(false);
		var modFlag = false;
		var orderfacId = $('pId').value;
		if (orderfacId != 'null' && orderfacId != '') {
			cotOrderFacService.getOrderFacById(orderfacId, function(orderfac) {
						if (orderfac.orderStatus == 2 && loginEmpId != "admin") {
							if ($('orderStatus').value == 2) {
								modFlag = true;
							}
						}
					});
		}
		DWREngine.setAsync(true);
		if (modFlag) {
			alert('Sorry, The order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = getFormValues(orderfacForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		Ext.MessageBox.confirm('Message', 'Do you want to save the purchase order？', function(btn) {
			if (btn == 'yes') {
				var form = DWRUtil.getValues('orderfacForm');
				var cotOrderFac = {};
				var checkFlag = false;
				DWREngine.setAsync(false);
				// 如果编号存在时先查询出对象,再填充表单
				if ($('pId').value != 'null' && $('pId').value != '') {
					cotOrderFacService.getOrderFacById($('pId').value,
							function(res) {
								for (var p in res) {
									if (p != 'orderTime' && p != 'sendTime') {
										res[p] = form[p];
									}
								}
								cotOrderFac = res;
								cotOrderFac.id = $('pId').value;
							});
					// 编辑页面时判断是否有更改币种
					checkFlag = checkIsChangeCur();
				} else {
					cotOrderFac = new CotOrderFac();
					for (var p in form) {
						if (p != 'orderTime' && p != 'sendTime') {
							cotOrderFac[p] = form[p];
						}
					}
					// 保存唛标图片路径（新建时）
					// var basePath = $('basePath').value;
					// var filePath = $('order_MB').src;
					// var s = filePath.indexOf(basePath);
					// var fPath = filePath.substring(s + basePath.length,
					// 111111);
					// fPath=decodeURI(fPath);
					// cotOrderFac.orderMb = fPath;
				}
				if (Ext.getCmp("maiTab").isVisible()) {
					cotOrderFac.orderZm = $('orderZMArea').value;// 正唛
					cotOrderFac.orderZhm = $('orderZHMArea').value;// 中唛
					cotOrderFac.orderCm = $('orderCMArea').value;// 侧唛
					cotOrderFac.orderNm = $('orderNMArea').value;// 内唛
				}
				if (Ext.getCmp("contractTab").isVisible()) {
					cotOrderFac.orderClause = $('orderClause').value;// 保存合同条款
				}

				if (Ext.getCmp("shenTab").isVisible()) {
					cotOrderFac.checkReason = $('checkReason').value;// 审核理由
				}
				
				
				DWREngine.setAsync(true);
				DWREngine.setAsync(false);
				cotOrderFacService.saveOrUpdateOrderFac(cotOrderFac,
						$('orderTime').value, $('sendTime').value, checkFlag,
						function(res) {
							if (res != null) {
								$('pId').value = res;

								// 更改添加action参数
								var urlAdd = '&orderId=' + res;

								// 更改修改action参数
								var urlMod = '&orderId=' + res;

								ds.proxy.setApi({
									read : "cotorderfac.do?method=queryDetail&orderId="
											+ res,
									create : "cotorderfac.do?method=addDetail"
											+ urlAdd,
									update : "cotorderfac.do?method=modifyDetail"
											+ urlMod,
									destroy : "cotorderfac.do?method=removeDetail&orderId="
											+ res
								});
								ds.save();
								Ext.Msg.alert("Message", "Save successful！");
								window.location.href = "cotorderfac.do?method=add&id="+res+"&oId="+$('oId').value;
							} else {
								Ext.MessageBox.alert('Message', 'Save unsucessful');
							}
							
						});
				DWREngine.setAsync(true);
			}
		})
	}

	// 显示导入界面
	var _self = this;
	function showOrderDetailDiv() {
		// 验证表单
		var formData = getFormValues(orderfacForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var cfg = {};
		cfg.oId = $('oId').value;
		cfg.bar = _self;
		var detailWin = new OrderDetailWin(cfg);
		detailWin.show();
		detailWin.load();
	}

	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list) {
		insertByBatch(list);
	}

	// 添加选择的样品到可编辑表格
	function insertByBatch(list) {

		var idAry = new Array();
		Ext.each(list, function(item) {
					idAry.push(item.data.id);
				});
		for (var i = 0; i < idAry.length; i++) {
			DWREngine.setAsync(false);

			cotOrderFacService.findIsExistDetail(idAry[i], function(detail) {
						if (detail != null) {
							setObjToGrid(detail);
						}
					});
			DWREngine.setAsync(true);
		}
		unmask();
	}

	// 添加带有数据的record到表格中
	function setObjToGrid(obj) {

		// 获得该行的新序号(该列的最大值+1)
		var sortNo = 0;
		ds.each(function(rec) {
					if (rec.data.sortNo > sortNo)
						sortNo = rec.data.sortNo;
				});
		obj.sortNo = sortNo + 1;

		// 取得随机数
		var rdm = getRandom();
		obj.rdm = rdm;
		obj.type = 'order';
		obj.orderDetailId = obj.id;
		obj.id = null;

		cotOrderFacService.setOrderFacMapValueByRdm(rdm, obj, function(res) {
				});
		var u = new ds.recordType(obj);
		ds.add(u);
	}

	// 更新内存数据
	function updateMapValue(rdm, property, value) {
		DWREngine.setAsync(false);
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, property, value,
				function(res) {
				});
		DWREngine.setAsync(true);
	}

	// 请求审核
	function requestCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotorderfac.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Do you apply for review?', function(btn) {
					if (btn == 'yes') {
						orderStatusBox.setValue(3);
						showBtn();
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
		var isPopedom = getPopedomByOpType('cotorderfac.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Pass or not?', function(btn) {
					if (btn == 'yes') {
						orderStatusBox.setValue(2);
						showBtn();
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
		var isPopedom = getPopedomByOpType('cotorderfac.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('Message', 'Sorry, you do not have Authority！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Do you confirm not pass?', function(btn) {
					if (btn == 'yes') {
						orderStatusBox.setValue(1);
						showBtn();
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}

	// 反核
	function reCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotorderfac.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('Message', 'Sorry, you do not have Authority！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Do you apply for review again?', function(btn) {
					if (btn == 'yes') {
						orderStatusBox.setValue(0);
						showBtn();
					} else {
						checkFlag = true;
					}
				});
		if (checkFlag) {
			return;
		}
	}

	// 显示按钮
	function showBtn() {
		DWREngine.setAsync(false);
		var orderfacid = $('pId').value;
		if (orderfacid != 'null' && orderfacid != '') {
			cotOrderFacService.saveOrderStatus(parseInt(orderfacid),
					parseInt($('orderStatus').value), function(res) {
						if (res != null) {
							// 显示审核按钮
							cotPriceService.getList('CotPriceCfg',
									function(cfg) {
										if (cfg.length != 0) {
											cotOrderFacService.getOrderFacById(
													parseInt(res), function(
															orderfac) {
														if (orderfac != null) {
															if (orderfac.orderStatus != 9) {
																if (orderfac.orderStatus == 3) {
																	Ext
																			.getCmp('requestBtn')
																			.hide();
																	Ext
																			.getCmp('passBtn')
																			.setVisible(true);
																	Ext
																			.getCmp('unpassBtn')
																			.setVisible(true);
																} else if (orderfac.orderStatus == 0) {
																	Ext
																			.getCmp('requestBtn')
																			.setVisible(true);
																	Ext
																			.getCmp('recheckBtn')
																			.hide();
																} else if (orderfac.orderStatus == 2) {
																	Ext
																			.getCmp('recheckBtn')
																			.setVisible(true);
																	Ext
																			.getCmp('passBtn')
																			.hide();
																	Ext
																			.getCmp('unpassBtn')
																			.hide();
																} else {
																	Ext
																			.getCmp('requestBtn')
																			.setVisible(true);
																	Ext
																			.getCmp('passBtn')
																			.hide();
																	Ext
																			.getCmp('unpassBtn')
																			.hide();
																}
															} else {
																Ext
																		.getCmp('requestBtn')
																		.setVisible(true);
															}
														}
													});
										}
									});
						}
					});
		} else {
			Ext.Msg.alert('Message', 'Pls. save the order before！');
		}
		DWREngine.setAsync(true);
	}

	// 打开上传面板,用于上次唛标
	function showUploadMaiPanel() {
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			Ext.MessageBox.alert("Message", "Pls. save the order before！");
			return;
		}
		var win = new UploadWin({
					params : {
						orderfacId : id
					},
					waitMsg : "uploading...",
					opAction : "modify",
					imgObj : $('order_MB'),
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=orderFacMB&temp=" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadPicture.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}

	// 四舍五入(保留b位小数)
	function round(a, b) {
		return ((Math.round(a * Math.pow(10, b)) * Math.pow(10, -b)).toFixed(b));
	}

	// 尺寸和英寸转换(自动填写中文规格和英文规格)
	function changeInchForDesc(fromObject, toObject, flag, rdm) {
		if (fromObject.value == '') {
			$(toObject).value = '';
			cotOrderFacService.updateOrderFacMapValueByRdm(rdm, toObject,
					$(toObject).value, function(res) {
					});
			return;
		} else {
			if (isNaN(fromObject.value)) {
				fromObject.value = '';
			}
		}
		if (flag == 0) {
			$(toObject).value = (fromObject.value / 2.54).toFixed("2");
		} else {
			$(toObject).value = round((fromObject.value * 2.54), 2);
		}
		$('eleInchDesc').value = round($('boxLInch').value, 2) + "*"
				+ round($('boxWInch').value, 2) + "*"
				+ round($('boxHInch').value, 2);
		$('eleSizeDesc').value = round($('boxL').value, 2) + "*"
				+ round($('boxW').value, 2) + "*" + round($('boxH').value, 2);

		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, toObject,
				$(toObject).value, function(res) {
				});
		setMapValue($('eleInchDesc'));
		setMapValue($('eleSizeDesc'));

		if ($('sizeFollow').value == 1) {
			// 产品包装尺寸跟着变化
			if (toObject == 'boxLInch' || toObject == 'boxL') {
				$('boxPbL').value = $('boxL').value;
				$('boxPbLInch').value = $('boxLInch').value;
				cotGivenService.updateMapValueByEleId(eleId, 'boxPbL',
						$('boxPbL').value, function(res) {
						});
				cotGivenService.updateMapValueByEleId(eleId, 'boxPbLInch',
						$('boxPbLInch').value, function(res) {
						});
			}
			if (toObject == 'boxWInch' || toObject == 'boxW') {
				$('boxPbW').value = $('boxW').value;
				$('boxPbWInch').value = $('boxWInch').value;
				cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxPbW',
						$('boxPbW').value, function(res) {
						});
				cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
						'boxPbWInch', $('boxPbWInch').value, function(res) {
						});
			}
			if (toObject == 'boxHInch' || toObject == 'boxH') {
				$('boxPbH').value = $('boxH').value;
				$('boxPbHInch').value = $('boxHInch').value;
				cotOrderFacService.updateOrderFacMapValueByRdm(rdm, 'boxPbH',
						$('boxPbH').value, function(res) {
						});
				cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
						'boxPbHInch', $('boxPbHInch').value, function(res) {
						});
			}
			calPrice();
		}
	}

	// 包装类型的值改变事件
	function showType(boxTypeId, rdm) {
		DWREngine.setAsync(false);

		if (boxTypeId == '') {
			return;
		}
		boxPacking.setValue(boxTypeId);

		cotElementsService.getBoxTypeById(boxTypeId, function(res) {
					if (res != null) {
						if (res.boxIName == null) {
							boxIbTypeBox.setValue('');
						} else {
							boxIbTypeBox.bindValue(res.boxIName);
						}
						if (res.boxMName == null) {
							boxMbTypeBox.setValue('');
						} else {
							boxMbTypeBox.bindValue(res.boxMName);
						}
						if (res.boxOName == null) {
							boxObTypeBox.setValue('');
						} else {
							boxObTypeBox.bindValue(res.boxOName);
						}
						if (res.boxPName == null) {
							boxPbTypeBox.setValue('');
						} else {
							boxPbTypeBox.bindValue(res.boxPName);
						}
						if (res.inputGridType == null) {
							inputGridTypeBox.setValue('');
						} else {
							inputGridTypeBox.bindValue(res.inputGridType);
						}
						cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
								'boxPbTypeId', res.boxPName, function(res) {
								});
						cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
								'boxIbTypeId', res.boxIName, function(res) {
								});
						cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
								'boxMbTypeId', res.boxMName, function(res) {
								});
						cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
								'boxObTypeId', res.boxOName, function(res) {
								});
						cotOrderFacService.updateOrderFacMapValueByRdm(rdm,
								'inputGridTypeId', res.inputGridType, function(
										res) {
								});
					}
				});
		modifyPrice(rdm);
		DWREngine.setAsync(true);
	};

	function setMapValue(e) {

		DWREngine.setAsync(false);
		var rdm = $('rdm').value;
		if (rdm == '')
			return;

		var property = e.name;
		var value = e.value;
		// 如果是包装类型
		// if(e.name=='boxTypeId'){
		// showType(value,rdm);
		// }
		if (e.name == 'boxObCount') {
			boxOcountNum(rdm, value);
		}
		// 如果是产品尺寸CM
		if (e.name == 'boxL' || e.name == 'boxW' || e.name == 'boxH') {
			changeInchForDesc(e, e.name + 'Inch', 0, rdm);
		}
		// 如果是产品尺寸INCH
		if (e.name == 'boxLInch' || e.name == 'boxWInch'
				|| e.name == 'boxHInch') {
			changeInchForDesc(e, e.name.substring(0, 4), 1, rdm);
		}

		// 如果是产品尺寸CM
		if (e.name == 'boxPbL' || e.name == 'boxPbW' || e.name == 'boxPbH'
				|| e.name == 'boxIbL' || e.name == 'boxIbW'
				|| e.name == 'boxIbH' || e.name == 'boxMbL'
				|| e.name == 'boxMbW' || e.name == 'boxMbH'
				|| e.name == 'boxObL' || e.name == 'boxObW'
				|| e.name == 'boxObH') {
			changeInch(e, e.name + 'Inch', 0, rdm, e.value);
		}
		// 如果是产品尺寸INCH
		if (e.name == 'boxPbLInch' || e.name == 'boxPbWInch'
				|| e.name == 'boxPbHInch' || e.name == 'boxIbLInch'
				|| e.name == 'boxIbWInch' || e.name == 'boxIbHInch'
				|| e.name == 'boxMbLInch' || e.name == 'boxMbWInch'
				|| e.name == 'boxMbHInch' || e.name == 'boxObLInch'
				|| e.name == 'boxObWInch' || e.name == 'boxObHInch') {
			changeInch(e, e.name.substring(0, 6), 1, rdm, e.value);
		}
		// 根据改变属性相应的改变Map内的值
		cotOrderFacService.updateOrderFacMapValueByRdm(rdm, property, value,
				function(res) {
				});

		DWREngine.setAsync(true);
	}

	// 手动修改各个包材价格时修改单个价格及生产价
	function calPackPriceAndPriceFac() {

		var packingPrice = $('packingPrice').value;
		var pbPrice = $('boxPbPrice').value;
		var ibPrice = $('boxIbPrice').value;
		var mbPrice = $('boxMbPrice').value;
		var obPrice = $('boxObPrice').value;
		var igPrice = $('inputGridPrice').value;

		var boxObCount = $('boxObCount').value;
		var boxMbCount = $('boxMbCount').value;
		var boxIbCount = $('boxIbCount').value;
		var boxPbCount = $('boxPbCount').value;

		var boxIbPrice = 0;
		var boxMbPrice = 0;
		var boxObPrice = 0;
		var boxPbPrice = 0;
		var inputGridPrice = 0;

		if (boxIbCount > 0 && boxObCount > 0 && ibPrice != ''
				&& ibPrice != null) {
			boxIbPrice = (parseFloat(ibPrice) * parseInt(boxObCount) / parseInt(boxIbCount))
					.toFixed(3);
		}
		if (boxMbCount > 0 && boxObCount > 0 && mbPrice != ''
				&& mbPrice != null) {
			boxMbPrice = (parseFloat(mbPrice) * parseInt(boxObCount) / parseInt(boxMbCount))
					.toFixed(3);
		}
		if (boxObCount > 0 && obPrice != '' && obPrice != null) {
			boxObPrice = parseFloat(obPrice).toFixed(3);
		}
		if (boxPbCount > 0 && boxObCount > 0 && pbPrice != ''
				&& pbPrice != null) {
			boxPbPrice = (parseFloat(pbPrice) * parseInt(boxObCount) / parseInt(boxPbCount))
					.toFixed(3);
		}
		if (boxObCount > 0 && igPrice != '' && igPrice != null) {
			inputGridPrice = parseFloat(igPrice).toFixed(3);
		}

		if (boxObCount != 0 && boxObCount != '') {
			packingPrice = ((parseFloat(boxIbPrice) + parseFloat(boxMbPrice)
					+ parseFloat(boxPbPrice) + parseFloat(boxObPrice) + parseFloat(inputGridPrice)) / parseInt(boxObCount))
					.toFixed(3);
		}
		$('packingPrice').value = packingPrice;
		var rdm = $('rdm').value;
		DWREngine.setAsync(false);

		cotOrderFacService.calPriceFacByPackPrice(rdm, packingPrice, function(
						facprice) {
					$('priceFac').value = facprice;
					var rec = editRec;
					rec.set("priceFac", facprice);
				});
		DWREngine.setAsync(true);
	}

	function calWeighByEleFrame(eleWeigth) {
		if (isNaN(eleWeigth)) {
			eleWeigth = 0;
		}
		var boxObCount = $('boxObCount').value;
		if (isNaN(boxObCount)) {
			boxObCount = 0;
		}
		cotElementsService.getDefaultList(function(res) {
					var gross = eleWeigth * boxObCount / 1000;
					if (res.length != 0) {
						if (res[0].grossNum != null) {
							gross = gross + res[0].grossNum;
						}
					}
					$('boxNetWeigth').value = eleWeigth * boxObCount / 1000;
					$('boxGrossWeigth').value = gross;

					setMapValue($('boxWeigth'));
					setMapValue($('boxNetWeigth'));
					setMapValue($('boxGrossWeigth'));
				});
		var rec = editRec;

		rec.set("boxNetWeigth", $('boxNetWeigth').value);
		rec.set("boxGrossWeigth", $('boxGrossWeigth').value);
	}

	// 重算序列号
	function reSort() {
		Ext.MessageBox.confirm('Message', 'Do you confirm to sort again?(Pls. save new data before,or will lost！)',
				function(btn) {
					if (btn == 'yes') {
						DWREngine.setAsync(false);
						cotOrderFacService.updateSortNo($("pId").value,
								function(res) {
									if (res) {
										// ECSideUtil.reload("orderFacDetail");
									} else {
										Ext.Msg.alert("Message", "Update sort number unsuccessful!");
									}
								});
						DWREngine.setAsync(true);
					}
				})
	}

	// 从订单导入唛头信息
	function ExtToDoFn() {
		var cfg = {};
		var orderfacId = $('pId').value;
		cfg.bar = _self;
		cotOrderFacService.getCotOrderIds(parseInt(orderfacId), function(res) {
					if (res != null) {
						cfg.orderIds = res;
					} else {
						Ext.MessageBox.alert("Message", "There's no content in the Procurement contracts！");
						return;
					}
				});
		var importPanel = new OrderMBGrid(cfg);
		importPanel.show();
	}

	// 双击历史事件表格更新报价事件
	function replacePrice(hisGrid, index) {

		var hisrec = hisGrid.getStore().getAt(index);
		var hisprice = hisrec.get("priceFac");
		var pricerec = sm.getSelected();
		pricerec.set("priceFac", hisprice)
		priceFacChange(hisprice, 2);
		var hisWin = Ext.getCmp("hisWin");
		hisWin.close();

		// 更改添加action参数
		var urlAdd = '&orderId=' + $('pId').value;

		// 更改修改action参数
		var urlMod = '&orderId=' + $('pId').value;

		ds.proxy.setApi({
					read : "cotorderfac.do?method=queryDetail&orderId="
							+ $('pId').value,
					create : "cotorderfac.do?method=addDetail" + urlAdd,
					update : "cotorderfac.do?method=modifyDetail" + urlMod,
					destroy : "cotorderfac.do?method=removeDetail&orderId="
							+ $('pId').value
				});
		ds.save();
	}

});