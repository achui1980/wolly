// 窗口关闭刷新触发事件
window.onunload = function() {
	cotOrderOutService.clearMap("cha", function(res) {
			});
}
// 获取报价和订单的单价要保留几位小数
var facNum = getDeNum("facPrecision");
var deNum = getDeNum("orderPricePrecision");
// cbm保留几位小数
var cbmNum = getDeNum("cbmPrecision");
// 根据小数位生成"0.000"字符串
var facNumTemp = getDeStr(facNum);
var strNum = getDeStr(deNum);
var cbmStr = getDeStr(cbmNum);
Ext.onReady(function() {
	DWREngine.setAsync(false);
	var facData;
	var curData;
	var hsData;
	var orderOutbgMap;
	//加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotOrderOutBG',function(rs){
		orderOutbgMap=rs;
	});
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	// 加载海关编码表缓存
	baseDataUtil.getBaseDicList('CotEleOther', function(res) {
		hsData = res;
	});
	DWREngine.setAsync(true);

	/** ******EXT创建grid步骤******** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "sortNo"
			}, {
				name : "custNo"
			}, {
				name : "orderName"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "orderPrice",
				type : "float",
				convert : numFormat.createDelegate(this, [strNum], 3)
			}, {
				name : "eleUnit"
			}, {
				name : "boxCount",
				type : "int"
			}, {
				name : "containerCount",
				type : "int"
			}, {
				name : "totalMoney",
				type : "float",
				convert : numFormat.createDelegate(this, [strNum], 3)
			}, {
				name : "boxCbm",
				type : "float",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "factoryId"
			}, {
				name : "factoryNo"
			}, {
				name : "priceFac",
				type : "float",
				convert : numFormat.createDelegate(this, [facNumTemp], 3)
			}, {
				name : "priceFacUint"
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "boxIbCount",
				type : "int"
			},{
				name:'eleHsid'
			}, {
				name : "boxMbCount",
				type : "int"
			}, {
				name : "boxObCount",
				type : "int"
			}, {
				name : "boxObL",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObW",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObH",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxGrossWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "boxNetWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "orderDetailId"
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
				read : "cotorderout.do?method=queryChaInfo&orderId="
						+ $('orderId').value,
				create : "cotorderout.do?method=addCha",
				update : "cotorderout.do?method=modifyCha",
				destroy : "cotorderout.do?method=removeCha"
			},
			listeners : {
				beforeload : function(store, options) {
					ds.removed = [];
					cotOrderOutService.clearMap("cha", function(res) {
							});
				},
				load : function(store, recs, options) {
					options.callback = function(r, ops, success) {
						if (success == false) {
							Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
						} else {
							var temp = summary.getSumTypeValue('totalMoney');
							parent.$('totalChaLab').innerText = Number(temp)
									.toFixed(deNum);
						}
					}
				},
				// 添加和修改后进入
				exception : function(proxy, type, action, options, res, arg) {
					// 从异常中的响应文本判断是否成功
					if (res.status != 200) {
						Ext.Msg.alert("Message", "Operation failed！");
					} else {
						ds.reload();
						// 更改主单的总金额,总数量...
						cotOrderOutService.updateOrderOutTotal(
								$('orderId').value, function(ary) {
									if (ary != null) {
										if (parent.Ext.getCmp("totalGross")
												.isVisible()) {
											if (ary[0] != null) {
												parent.$('totalMoney').value = Number(ary[0])
														.toFixed(deNum);
											}
											if (ary[1] != null) {
												parent.$('totalCount').value = ary[1];
											}
											if (ary[2] != null) {
												parent.$('totalContainer').value = ary[2];
											}
											if (ary[3] != null) {
												parent.$('totalCbm').value = ary[3];
											}
											if (ary[4] != null) {

												parent.$('totalNet').value = ary[4];
											}
											if (ary[5] != null) {
												parent.$('totalGross').value = ary[5];
											}
											if (ary[6] != null) {
												parent.$('totalHsMoney').value = Number(ary[6])
														.toFixed(deNum);
											}
											if (ary[7] != null) {
												parent.$('totalHsCount').value = ary[7];
											}
											if (ary[8] != null) {
												parent.$('totalHsContainer').value = ary[8];
											}
											if (ary[9] != null) {
												parent.$('totalHsCbm').value = ary[9];
											}
											if (ary[10] != null) {
												parent.$('totalHsNet').value = ary[10];
											}
											if (ary[11] != null) {
												parent.$('totalHsGross').value = ary[11];
											}
										}
										// 加载总金额
										parent.$('totalLab').innerText = Number(ary[0])
												.toFixed(deNum);
										parent.$('totalChaLab').innerText = Number(ary[6])
												.toFixed(deNum);
										loadbar(0);
										parent.reflashParent('orderoutGrid');
									}
								});
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

	// 表格-厂家
	var facGridBox = new BindCombox({
				dataUrl : "./servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1&validUrl=cotfactory.do",
				cmpId : 'factoryId',
				hideLabel : true,
				labelSeparator : " ",
				editable : true,
				autoLoad : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 35,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 表格--币种
	var curGridBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				hideLabel : true,
				autoLoad : true,
				labelSeparator : " ",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						priceFacUintChange(com.getValue());
					}
				}
			});

	// 创建需要在表格显示的列
	var cm = new HideColumnModel({
				defaults : {
					sortable : true
				},
				hiddenCols:orderOutbgMap,
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Sort",
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
							header : "W&C Art No.",
							dataIndex : "eleId",
							width : 100,
							editor : new Ext.form.TextField({
										maxLength : 100,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												if (newVal == "") {
													txt.setValue(oldVal);
												} else {
													txt.setValue(newVal
															.toUpperCase());
												}
											}
										}
									}),
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "Cust.No.",
							dataIndex : "custNo",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 120
						}, {
							header : "Des. of Declaration",
							hidden:true,
							dataIndex : "orderName",
							editor : new Ext.form.TextField({
										maxLength : 50
									}),
							width : 120
						}, {
							header : "Description",
							hidden:true,
							dataIndex : "eleName",
							editor : new Ext.form.TextArea({
										maxLength : 200,
										height : 25,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 120
						}, {
							header : "Descripiton",
							dataIndex : "eleNameEn",
							editor : new Ext.form.TextArea({
										maxLength : 200,
										height : 25,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 110
						}, {
							header : "Sales Price",
							dataIndex : "orderPrice",
							width : 110,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Unit",
							dataIndex : "eleUnit",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 45
						}, {
							header : "Quantity",
							dataIndex : "boxCount",
							width : 60,
							summaryType : 'sum',
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Cartons",
							dataIndex : "containerCount",
							width : 60,
							summaryType : 'sum',
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Amount of Declaration",
							hidden:true,
							dataIndex : "totalMoney",
							width : 70,
							summaryType : 'sum'
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							editor : facGridBox,
							width : 100,
							renderer : function(value) {
								return facData[value];
							}
						}, {
							header : "Supplier's A.No.",
							dataIndex : "factoryNo",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 100
						}, {
							header : "Purchase Price",
							dataIndex : "priceFac",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 99999999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Currency",
							dataIndex : "priceFacUint",
							editor : curGridBox,
							width : 80,
							renderer : function(value) {
								return curData[value];
							}
						}, {
							header : "Inner Box",
							dataIndex : "boxIbCount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "中",
							hidden:true,
							dataIndex : "boxMbCount",
							width : 35,
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Outer Box",
							dataIndex : "boxObCount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Length Of Outer Cartons",
							dataIndex : "boxObL",
							width : 50,
							hidden:true,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Width Of Outer Cartons",
							dataIndex : "boxObW",
							width : 50,
							hidden:true,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Height Of Outer Cartons",
							dataIndex : "boxObH",
							width : 50,
							hidden:true,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "CBM",
							dataIndex : "boxCbm",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 99999999.999999,
										decimalPrecision : cbmNum,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						},{
							header : "N.W(Kgs)",
							dataIndex : "boxNetWeigth",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						},  {
							header : "G.W(Kgs)",
							dataIndex : "boxGrossWeigth",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "中文规格",
							hidden:true,
							dataIndex : "eleSizeDesc",
							editor : new Ext.form.TextArea({
										maxLength : 500,
										height : 25,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 90
						}, {
							header : "Size Description",
							hidden:true,
							dataIndex : "eleInchDesc",
							editor : new Ext.form.TextArea({
										maxLength : 500,
										height : 25,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 90
						}, {
							header : "orderDetailId",
							dataIndex : "orderDetailId",
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0
									}),
							hidden : true
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 1000,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : [{
							text : "Import All",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : insertAll
						}, '-', {
							text : "Import Update",
							iconCls : "page_mod",
							cls : "SYSOP_DEL",
							handler : insertUpdate
						}, '-', {
							text : "Create",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : addNewGrid
						}, '-', {
							text : "Del",
							iconCls : "page_del",
							cls : "SYSOP_DEL",
							handler : onDel
						}, '-', 
//							{
//							text : "报关品名",
//							iconCls : "page_excel",
//							cls : "SYSOP_ADD",
//							handler : showModel
//						}, '-', 
							{
							text : "measure",
							iconCls : "gird_list",
							cls : "SYSOP_PRINT",
							handler : showCe
						}]
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 订单明细表格
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "outGrid",
				stripeRows : true,
				margins : "0 5 0 0",
				bodyStyle : 'width:100%',
				store : ds,
				cm : cm,
				sm : sm,
				plugins : summary,
				loadMask : true,
				tbar : tb,
				cls : 'rightBorder',
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 右键菜单
	var rightMenu = new Ext.menu.Menu({
				id : "rightMenu",
				items : [{
							text : "Are you sure to save the current sort",
							handler : sumSortTable
						}]
			});
	function rightClickFn(client, rowIndex, e) {
		e.preventDefault();
		rightMenu.showAt(e.getXY());
	}
	grid.on("rowcontextmenu", rightClickFn);

	// 单元格双击事件--;1.让单元格的editor适应行高度
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

	// 单元格编辑后,讲修改后的值储存到内存map中,并即时修改右边面板数据
	grid.on("afteredit", function(e) {
		var flag = false;
		if (e.field == 'eleId') {
			// 如果是新增的货号,先插入内存map
			if (isNaN(e.record.id)) {
				DWREngine.setAsync(false);
				cotOrderOutService.getChaOrderMapValue(
						e.record.data.orderDetailId, function(res) {
							if (res == null) {
								flag = true;
								var obj = new CotOrderOuthsdetail();
								obj.eleId = e.value.toUpperCase();
								obj.orderDetailId = e.record.data.orderDetailId;
								// 将对象储存到后台map中
								cotOrderOutService.setChaOrderMap(
										e.record.data.orderDetailId, obj,
										function(res) {
										});
							}
						});
				DWREngine.setAsync(true);
			}
		}
		// 报关数量改变事件
		if (e.field == 'boxCount') {
			countNum(e.value, e.originalValue);
		}
		// 报关箱数改变事件
		if (e.field == 'containerCount') {
			containNum(e.value, e.originalValue);
		}
		// 报关单价改变事件
		if (e.field == 'orderPrice') {
			priceOutChange(e.value);
		}
		// 内装数改变事件
		if (e.field == 'boxIbCount') {
			boxIcountNum(e.value);
		}
		// 中装数改变事件
		if (e.field == 'boxMbCount') {
			boxMcountNum(e.value);
		}
		// 外箱数改变事件
		if (e.field == 'boxObCount') {
			boxOcountNum(e.value);
		}
		// 外箱长改变事件
		if (e.field == 'boxObL') {
			changeBoxObL(e.value);
		}
		// 外箱宽改变事件
		if (e.field == 'boxObW') {
			changeBoxObW(e.value);
		}
		// 外箱高改变事件
		if (e.field == 'boxObH') {
			changeBoxObH(e.value);
		}
		// cbm改变事件
		if (e.field == 'boxCbm') {
			changeCbm(e.value);
		}

		if (flag == false) {
			DWREngine.setAsync(false);
			cotOrderOutService.updateChaMapValueByEleId(
					e.record.data.orderDetailId, e.field, e.value,
					function(res) {
						if (rightForm.isVisible()) {
							var field = rightForm.getForm().findField(e.field);
							if (field != null) {
								field.setValue(e.value);
							}
						}
					});
			DWREngine.setAsync(true);
		}
	});
	// 行点击时加载右边折叠面板表单
	grid.on("rowclick", function(grid, rowIndex, e) {
				// 如果右边折叠面板有展开过才加载数据
				if (rightForm.isVisible()) {
					initForm(ds.getAt(rowIndex));
				}
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

	// 厂家
	var facBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
				cmpId : 'factoryId',
				sendMethod : "post",
				fieldLabel : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 35,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 锁定套数
	var lockUnitNum = function() {
		var fly = Ext.getDom('eleFlag').value;
		if (fly == 0) {
			// Ext.getDom('eleFlag').setValue(1);
			Ext.getCmp('eleUnitNum').setValue(1);
			Ext.getCmp('eleUnitNum').setDisabled(true);
		} else {
			Ext.getCmp('eleUnitNum').setDisabled(false);
		}
	};

	// 组成方式
	var eleFlagStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '单件'], [1, '套件'], [3, '组件']]
			});
	var eleFlagBox = new Ext.form.ComboBox({
				name : 'eleFlag',
				fieldLabel : '组成方式',
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
				selectOnFocus : true,
				listeners : {
					"select" : lockUnitNum
				}
			});

	// 包装方案
	var boxPacking = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType",
				valueField : "id",
				fieldLabel : "Packing Way",
				tabIndex : 51,
				displayField : "typeName",
				cmpId : "boxTypeId",
				emptyText : 'Choose',
				anchor : "100%",
				listeners : {
					"select" : selectBind
				}
			});
	// 产品类型
	var boxPbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=PB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 57,
				displayField : "value",
				cmpId : "boxPbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('orderDetailId').value, "boxPbTypeId",
								rec.data.id);
						DWREngine.setAsync(true);
						calPrice();
					}
				}
			});
	// 内包装类型
	var boxIbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 60,
				displayField : "value",
				cmpId : "boxIbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('orderDetailId').value, "boxIbTypeId",
								rec.data.id);
						DWREngine.setAsync(true);
						calPrice();
					}
				}
			});
	// 中包装类型
	var boxMbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=MB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 63,
				displayField : "value",
				cmpId : "boxMbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('orderDetailId').value, "boxMbTypeId",
								rec.data.id);
						DWREngine.setAsync(true);
						calPrice();
					}
				}
			});
	// 外包装类型
	var boxObTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=OB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 66,
				displayField : "value",
				cmpId : "boxObTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('orderDetailId').value, "boxObTypeId",
								rec.data.id);
						DWREngine.setAsync(true);
						calPrice();
					}
				}
			});

	// 插格类型
	var inputGridTypeIdBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IG",
				valueField : "id",
				fieldLabel : "插格类型",
				editable : true,
				tabIndex : 54,
				displayField : "value",
				cmpId : "inputGridTypeId",
				emptyText : 'Choose',
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('orderDetailId').value,
								"inputGridTypeId", rec.data.id);
						DWREngine.setAsync(true);
						calPrice();
					}
				}
			});

	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
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

	// 海关编码
	var eleHsidBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "Customs Code",
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

	// 右边折叠表单
	var rightForm = new Ext.form.FormPanel({
		autoScroll : true,
		border : false,
		padding : "5px",
		labelWidth : 60,
		labelAlign : "right",
		items : [{
			xtype : "fieldset",
			title : "Basic Information",
			layout : "column",
			anchor : '97%',
			padding : "0",
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
							+ ' onload="javascript:DrawImage(this,100,100)" onclick="showBigPicDiv(this)"/></div>'
				}],
				buttons : [{
							width : 40,
							text : "Change",
							// handler : showUploadPanel,
							id : "upmod"
						}, {
							width : 40,
							text : "Delete",
							// handler : delPic,
							id : "updel"
						}]
			}, {
				xtype : "panel",
				title : "",
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
							fieldLabel : "Supplier Art No.",
							anchor : "100%",
							tabIndex : 32,
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "factoryNo",
							name : "factoryNo",
							maxLength : 200
						}, {
							xtype : "textfield",
							fieldLabel : "Client No.",
							anchor : "100%",
							disabled : true,
							disabledClass : 'combo-disabled',
							tabIndex : 33,
							id : "custNo",
							name : "custNo",
							maxLength : 200
						}, typeLv1Box, facBox, {
							xtype : "hidden",
							id : "id",
							name : "id"
						}, {
							xtype : "hidden",
							id : "orderDetailId",
							name : "orderDetailId"
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.6,
				layout : "form",
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
							fieldLabel : "Source",
							anchor : "100%",
							id : "eleFrom",
							name : "eleFrom",
							tabIndex : 42,
							maxLength : 100
						}, {
							xtype : "textfield",
							fieldLabel : "YY-MM-DD",
							anchor : "100%",
							id : "eleTypenameLv2",
							name : "eleTypenameLv2",
							tabIndex : 44,
							maxLength : 20
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.4,
				layout : "form",
				labelWidth : 60,
				items : [{
							xtype : "numberfield",
							fieldLabel : "Pieces",
							anchor : "100%",
							id : "eleUnitNum",
							name : "eleUnitNum",
							disabledClass : 'combo-disabled',
							tabIndex : 37,
							decimalPrecision : 0,
							maxValue : 1000000000
						}, {
							xtype : "numberfield",
							fieldLabel : "MOQ",
							anchor : "100%",
							tabIndex : 39,
							id : "eleMod",
							name : "eleMod",
							decimalPrecision : 0,
							maxValue : 1000000000
						}, {
							xtype : "textfield",
							fieldLabel : "Level",
							anchor : "100%",
							id : "eleGrade",
							name : "eleGrade",
							tabIndex : 41,
							maxLength : 30
						}, {
							xtype : "numberfield",
							fieldLabel : "Mold cost",
							anchor : "100%",
							tabIndex : 43,
							decimalPrecision : 3,
							id : "moldCharge",
							name : "moldCharge",
							maxValue : 99999.999
						}, {
							xtype : "numberfield",
							fieldLabel : "Cube",
							anchor : "100%",
							tabIndex : 45,
							id : "cube",
							name : "cube",
							decimalPrecision : 2,
							maxValue : 999999.99
						}]
			}, {
				xtype : "panel",
				layout : "form",
				columnWidth : 1,
				items : [{
							xtype : "textarea",
							fieldLabel : "中文品名",
							anchor : "100%",
							height : 40,
							tabIndex : 46,
							id : "eleName",
							name : "eleName",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "Description",
							anchor : "100%",
							height : 40,
							tabIndex : 47,
							id : "eleNameEn",
							name : "eleNameEn",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "Des. Of Product",
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
							tabIndex : 51,
							id : "eleRemark",
							name : "eleRemark",
							maxLength : 500
						}]
			}]
		}, {
			xtype : "fieldset",
			title : "Packing Information",
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
							items : [boxPacking, inputGridTypeIdBox]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.5,
							layout : "form",
							items : [{
										xtype : "numberfield",
										fieldLabel : "Packing",
										anchor : "100%",
										tabIndex : 56,
										id : "boxPbCount",
										name : "boxPbCount",
										decimalPrecision : 0,
										maxValue : 1000000000
									}, {
										xtype : "numberfield",
										fieldLabel : "Packing",
										anchor : "100%",
										tabIndex : 59,
										id : "boxIbCount",
										name : "boxIbCount",
										decimalPrecision : 0,
										maxValue : 1000000000
									}, {
										xtype : "numberfield",
										fieldLabel : "Packing",
										anchor : "100%",
										tabIndex : 62,
										id : "boxMbCount",
										name : "boxMbCount",
										decimalPrecision : 0,
										maxValue : 1000000000
									}, {
										xtype : "numberfield",
										fieldLabel : "Packing",
										anchor : "100%",
										tabIndex : 65,
										id : "boxObCount",
										name : "boxObCount",
										decimalPrecision : 0,
										maxValue : 1000000000,
										listeners : {
											'change' : calWandC
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
				columnWidth : 0.28,
				layout : "form",
				labelWidth : 20,
				items : [{
							xtype : "numberfield",
							fieldLabel : "价",
							anchor : "100%",
							tabIndex : 53,
							id : "packingPrice",
							name : "packingPrice",
							decimalPrecision : 3,
							maxValue : 9999999.999
						}, {
							xtype : "numberfield",
							fieldLabel : "价",
							anchor : "100%",
							tabIndex : 55,
							id : "inputGridPrice",
							name : "inputGridPrice",
							decimalPrecision : 2,
							maxValue : 99999999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "价",
							anchor : "100%",
							tabIndex : 58,
							id : "boxPbPrice",
							name : "boxPbPrice",
							decimalPrecision : 2,
							maxValue : 99999999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "价",
							anchor : "100%",
							tabIndex : 61,
							id : "boxIbPrice",
							name : "boxIbPrice",
							decimalPrecision : 2,
							maxLength : 99999999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "价",
							anchor : "100%",
							tabIndex : 64,
							id : "boxMbPrice",
							name : "boxMbPrice",
							decimalPrecision : 2,
							maxValue : 99999999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "价",
							anchor : "100%",
							tabIndex : 67,
							id : "boxObPrice",
							name : "boxObPrice",
							decimalPrecision : 2,
							maxLength : 99999999.99
						}]
			}, {
				xtype : "panel",
				layout : "column",
				columnWidth : 1,
				items : [{
							xtype : 'panel',
							layout : 'form',
							columnWidth : .4,
							items : [{
										xtype : "numberfield",
										fieldLabel : "Weight/G",
										anchor : "100%",
										tabIndex : 68,
										id : "boxWeigth",
										name : "boxWeigth",
										decimalPrecision : 2,
										maxValue : 999999.99,
										listeners : {
											"change" : calWeight
										}
									}]
						}, {
							xtype : 'panel',
							layout : 'form',
							labelWidth : 30,
							columnWidth : .3,
							items : [{
										xtype : "numberfield",
										fieldLabel : "CBM",
										anchor : "100%",
										tabIndex : 69,
										id : "boxCbm",
										name : "boxCbm",
										decimalPrecision : cbmNum,
										maxLength : 99999999.999999,
										listeners : {
											"change" : calCuftCube
										}
									}]
						}, {
							xtype : 'panel',
							layout : 'form',
							labelWidth : 35,
							columnWidth : .3,
							items : [{
										xtype : "numberfield",
										fieldLabel : "CUFT",
										anchor : "100%",
										tabIndex : 69,
										id : "boxCuft",
										name : "boxCuft",
										decimalPrecision : 4,
										maxLength : 999999.9999,
										listeners : {
											"change" : calCbmCube
										}
									}]
						}]
			}, {
				xtype : "panel",
				columnWidth : 1,
				layout : "column",
				items : [{
							xtype : 'panel',
							layout : 'form',
							columnWidth : .5,
							items : [{
										xtype : "numberfield",
										fieldLabel : "G.W./KG",
										anchor : "100%",
										tabIndex : 70,
										id : "boxGrossWeigth",
										name : "boxGrossWeigth",
										decimalPrecision : 4,
										maxValue : 9999.9999
									}]
						}, {
							xtype : 'panel',
							layout : 'form',
							columnWidth : .5,
							items : [{
										xtype : "numberfield",
										fieldLabel : "N.W./KG",
										anchor : "100%",
										tabIndex : 71,
										id : "boxNetWeigth",
										name : "boxNetWeigth",
										decimalPrecision : 4,
										maxValue : 9999.9999
									}]
						}]
			}, {
				xtype : "panel",
				columnWidth : 0.5,
				layout : "form",
				labelWidth : 70,
				items : [{
							xtype : "numberfield",
							fieldLabel : "摆放方式",
							anchor : "100%",
							id : "putL",
							name : "putL",
							tabIndex : 72,
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "Product SizeCM",
							anchor : "100%",
							tabIndex : 75,
							id : "boxL",
							name : "boxL",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 78,
							id : "boxLInch",
							name : "boxLInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "Packing SizeCM",
							anchor : "100%",
							tabIndex : 81,
							id : "boxPbL",
							name : "boxPbL",
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 84,
							id : "boxPbLInch",
							name : "boxPbLInch",
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "Inner BoxCM",
							anchor : "100%",
							tabIndex : 87,
							id : "boxIbL",
							name : "boxIbL",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 90,
							id : "boxIbLInch",
							name : "boxIbLInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "Middle Box CM",
							anchor : "100%",
							tabIndex : 93,
							id : "boxMbL",
							name : "boxMbL",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 96,
							id : "boxMbLInch",
							name : "boxMbLInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "Outer Carton CM",
							anchor : "100%",
							tabIndex : 99,
							id : "boxObL",
							name : "boxObL",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 102,
							id : "boxObLInch",
							name : "boxObLInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
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
							id : "putW",
							name : "putW",
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 76,
							id : "boxW",
							name : "boxW",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 79,
							id : "boxWInch",
							name : "boxWInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 82,
							id : "boxPbW",
							name : "boxPbW",
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 85,
							id : "boxPbWInch",
							name : "boxPbWInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 88,
							id : "boxIbW",
							name : "boxIbW",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 91,
							id : "boxIbWInch",
							name : "boxIbWInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 94,
							id : "boxMbW",
							name : "boxMbW",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 97,
							id : "boxMbWInch",
							name : "boxMbWInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 100,
							id : "boxObW",
							name : "boxObW",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 103,
							id : "boxObWInch",
							name : "boxObWInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
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
							tabIndex : 74,
							id : "putH",
							name : "putH",
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 77,
							id : "boxH",
							name : "boxH",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 80,
							id : "boxHInch",
							name : "boxHInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 83,
							id : "boxPbH",
							name : "boxPbH",
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 86,
							id : "boxPbHInch",
							name : "boxPbHInch",
							decimalPrecision : 2,
							maxValue : 999.99
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 89,
							id : "boxIbH",
							name : "boxIbH",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 92,
							id : "boxIbHInch",
							name : "boxIbHInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 95,
							id : "boxMbH",
							name : "boxMbH",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 98,
							id : "boxMbHInch",
							name : "boxMbHInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 101,
							id : "boxObH",
							name : "boxObH",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}, {
							xtype : "numberfield",
							fieldLabel : "x",
							anchor : "100%",
							labelSeparator : " ",
							tabIndex : 104,
							id : "boxObHInch",
							name : "boxObHInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 1,
				layout : "form",
				items : [{
							xtype : "textarea",
							fieldLabel : "Size Description CM",
							anchor : "100%",
							height : 40,
							tabIndex : 105,
							id : "eleSizeDesc",
							name : "eleSizeDesc",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "Size Description INCH",
							anchor : "100%",
							height : 40,
							tabIndex : 106,
							id : "eleInchDesc",
							name : "eleInchDesc",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "口径描述",
							anchor : "100%",
							height : 40,
							tabIndex : 107,
							id : "boxTDesc",
							name : "boxTDesc",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "底径描述",
							anchor : "100%",
							height : 40,
							tabIndex : 108,
							id : "boxBDesc",
							name : "boxBDesc",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "Packing Description CM",
							anchor : "100%",
							height : 40,
							tabIndex : 109,
							id : "boxRemarkCn",
							name : "boxRemarkCn",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "Packing Description INCH",
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
			title : "装柜信息",
			layout : "column",
			buttonAlign : 'center',
			anchor : '97%',
			buttons : [{
				iconCls : "page_calculator",
				width : 70,
				text : "<span ext:qwidth=100 ext:qtip='按各集装箱的最大可装重量,计算装箱数'>按柜重量</span>",
				handler : sumContainCount
			}, {
				text : "<span ext:qwidth=100 ext:qtip='按各集装箱的最大可容纳体积,计算装箱数'>按柜体积</span>",
				width : 70,
				iconCls : "page_calculator",
				handler : sumContainCountCBM
			}],
			items : [{
						xtype : "panel",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 40,
						items : [{
									xtype : "numberfield",
									fieldLabel : "20\"",
									anchor : "100%",
									disabled : true,
									disabledClass : 'combo-disabled',
									tabIndex : 110,
									id : "box20Count",
									name : "box20Count",
									maxLength : 10000000000
								}, {
									xtype : "numberfield",
									fieldLabel : "40HQ\"",
									anchor : "100%",
									disabled : true,
									disabledClass : 'combo-disabled',
									tabIndex : 112,
									id : "box40hqCount",
									name : "box40hqCount",
									maxLength : 10000000000
								}]
					}, {
						xtype : "panel",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 30,
						items : [{
									xtype : "numberfield",
									fieldLabel : "40\"",
									anchor : "100%",
									disabled : true,
									disabledClass : 'combo-disabled',
									tabIndex : 111,
									id : "box40Count",
									name : "box40Count",
									maxLength : 10000000000
								}, {
									xtype : "numberfield",
									fieldLabel : "45\"",
									anchor : "100%",
									disabled : true,
									disabledClass : 'combo-disabled',
									tabIndex : 113,
									id : "box45Count",
									name : "box45Count",
									maxLength : 10000000000
								}]
					}]
		}]
	});

	// 右边折叠面板
	var rightPanel = new Ext.Panel({
		title : 'Details',
		layout : 'fit',
		frame : true,
		border : false,
		region : 'east',
		width : "30%",
		collapsible : true,
		collapsed : true,
		listeners : {
			'beforeadd' : function(pnl, component, index) {
				pnl.getEl().mask("Loading,wait for a while...", 'x-mask-loading');
			},
			'afterlayout' : function(pnl) {
				pnl.getEl().unmask();
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
						if ($('orderDetailId').value != "") {
							DWREngine.setAsync(false);
							updateMapValue($('orderDetailId').value, txt
											.getName(), newVal);
							DWREngine.setAsync(true);
							// 修改表格数据,如果表格没有这一列,则修改这一行的状态
							ds.each(function(rec) {
								if (rec.data.orderDetailId == $('orderDetailId').value) {
									var cell = rec.data[txt.getName()];
									if (typeof(cell) == 'undefined') {
										if (!isNaN(rec.data.id)) {
											var temp = rec.data.orderDetailId;
											rec.set("orderDetailId", temp
															+ "aaa");
											rec.set("orderDetailId", temp);
										}
									} else {
										rec.set(txt.getName(), newVal);
									}
									return false;
								}
							});
							// 中英文规格的转换
							if (txt.getName() == 'boxL'
									|| txt.getName() == 'boxW'
									|| txt.getName() == 'boxH'
									|| txt.getName() == 'boxPbL'
									|| txt.getName() == 'boxPbW'
									|| txt.getName() == 'boxPbH'
									|| txt.getName() == 'boxIbL'
									|| txt.getName() == 'boxIbW'
									|| txt.getName() == 'boxIbH'
									|| txt.getName() == 'boxMbL'
									|| txt.getName() == 'boxMbW'
									|| txt.getName() == 'boxMbH'
									|| txt.getName() == 'boxObL'
									|| txt.getName() == 'boxObW'
									|| txt.getName() == 'boxObH') {
								calInch(txt, newVal, oldVal);
							}
							if (txt.getName() == 'boxObL') {
								changeBoxObL(newVal, true);
							}
							if (txt.getName() == 'boxObW') {
								changeBoxObW(newVal, true);
							}
							if (txt.getName() == 'boxObH') {
								changeBoxObH(newVal, true);
							}
							if (txt.getName() == 'boxLInch'
									|| txt.getName() == 'boxWInch'
									|| txt.getName() == 'boxHInch'
									|| txt.getName() == 'boxPbLInch'
									|| txt.getName() == 'boxPbWInch'
									|| txt.getName() == 'boxPbHInch'
									|| txt.getName() == 'boxIbLInch'
									|| txt.getName() == 'boxIbWInch'
									|| txt.getName() == 'boxIbHInch'
									|| txt.getName() == 'boxMbLInch'
									|| txt.getName() == 'boxMbWInch'
									|| txt.getName() == 'boxMbHInch'
									|| txt.getName() == 'boxObLInch'
									|| txt.getName() == 'boxObWInch'
									|| txt.getName() == 'boxObHInch') {
								calCm(txt, newVal, oldVal);
							}
							if (txt.getName() == 'boxObLInch') {
								changeBoxObL(Number(newVal).mul(2.54), true, 1);
							}
							if (txt.getName() == 'boxObWInch') {
								changeBoxObW(Number(newVal).mul(2.54), true, 1);
							}
							if (txt.getName() == 'boxObHInch') {
								changeBoxObH(Number(newVal).mul(2.54), true, 1);
							}
							// 某些计算包材价格的字段触发calPrice()
							if (txt.getName() == 'boxPbCount'
									|| txt.getName() == 'boxIbCount'
									|| txt.getName() == 'boxMbCount'
									|| txt.getName() == 'boxObCount') {
								calPrice();
							}
							// 手动修改各个价格时
							if (txt.getName() == 'boxPbPrice'
									|| txt.getName() == 'boxIbPrice'
									|| txt.getName() == 'boxMbPrice'
									|| txt.getName() == 'boxObPrice'
									|| txt.getName() == 'inputGridPrice') {
								calPackPriceAndPriceFac();
							}
							// 手动修改单个价格时
							if (txt.getName() == 'packingPrice') {
								calPriceByPakingPrice(newVal);
							}
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

	this.load = function() {
		ds.load({
					params : {
						start : 0,
						limit : 15
					}
				});
	}

	// 加载包装类型值
	function selectBind(com, rec, index) {
		var id = rec.data.id;
		var rdm = $('orderDetailId').value;
		if (rdm == '') {
			return;
		}
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(rdm, function(obj) {
					if (obj != null) {
						cotElementsService.getBoxTypeById(id, function(res) {
									if (res != null) {
										if (res.boxIName == null) {
											boxIbTypeBox.setValue("");
										} else {
											boxIbTypeBox
													.bindValue(res.boxIName);
										}
										if (res.boxMName == null) {
											boxMbTypeBox.setValue("");
										} else {
											boxMbTypeBox
													.bindValue(res.boxMName);
										}
										if (res.boxOName == null) {
											boxObTypeBox.setValue("");
										} else {
											boxObTypeBox
													.bindValue(res.boxOName);
										}
										if (res.boxPName == null) {
											boxPbTypeBox.setValue("");
										} else {
											boxPbTypeBox
													.bindValue(res.boxPName);
										}
										if (res.inputGridType == null) {
											inputGridTypeIdBox.setValue("");
										} else {
											inputGridTypeIdBox
													.bindValue(res.inputGridType);
										}
										obj.boxTypeId = id;
										obj.boxIbTypeId = res.boxIName;
										obj.boxMbTypeId = res.boxMName;
										obj.boxObTypeId = res.boxOName;
										obj.boxPbTypeId = res.boxPName;
										obj.inputGridTypeId = res.inputGridType;
										// 将对象储存到后台map中
										DWREngine.setAsync(false);
										cotOrderOutService.setChaOrderMap(rdm,
												obj, function(def) {
													calPrice();
												});
										DWREngine.setAsync(true);

									}
								});

					}
				});
		DWREngine.setAsync(true);
	};

	// 比对柜最大装箱重量
	function sumContainCount() {
		var rdm = $('orderDetailId').value;
		if (rdm == '') {
			return;
		}
		var gross = $('boxGrossWeigth').value;
		if (gross == '' || isNaN(gross)) {
			Ext.MessageBox.show({
						title : 'Message',
						msg : 'Please enter G.W.(Must be a numeric type)！',
						height : 300,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.WARNING,
						fn : function() {
							$('boxGrossWeigth').focus();
						}
					});
		} else {
			queryService.getContainerWeigh(function(res) {
						var boxObCount = $('boxObCount').value;
						if (isNaN(boxObCount)) {
							boxObCount = 0;
						}
						var max20 = parseInt((res[0] / gross) * boxObCount);
						var max40 = parseInt((res[1] / gross) * boxObCount);
						var max40hq = parseInt((res[2] / gross) * boxObCount);
						var max45 = parseInt((res[3] / gross) * boxObCount);

						if ($('box20Count').value != ''
								&& $('box20Count').value > max20) {
							$('box20Count').value = max20;
						}
						if ($('box40Count').value != ''
								&& $('box40Count').value > max40) {
							$('box40Count').value = max40;
						}
						if ($('box40hqCount').value != ''
								&& $('box40hqCount').value > max40hq) {
							$('box40hqCount').value = max40hq;
						}
						if ($('box45Count').value != ''
								&& $('box45Count').value > max45) {
							$('box45Count').value = max45;
						}
						updateMapValue(rdm, 'box20Count', $('box20Count').value);
						updateMapValue(rdm, 'box40Count', $('box40Count').value);
						updateMapValue(rdm, 'box40hqCount',
								$('box40hqCount').value);
						updateMapValue(rdm, 'box45Count', $('box45Count').value);
					});
		}
	}

	// 通过cbm和外箱数计算装箱数,获得20/40/40HQ/45的柜体积(默认24/54/68/86)
	function sumContainCountCBM() {
		var rdm = $('orderDetailId').value;
		if (rdm == '') {
			return;
		}
		var cbm = $('boxCbm').value;
		if (cbm == '' || isNaN(cbm)) {
			Ext.MessageBox.show({
						title : 'Message',
						msg : 'Please enter CBM(Must be a numeric type)！',
						height : 300,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.WARNING,
						fn : function() {
							$('boxCbm').focus();
						}
					});
		} else {
			queryService.getContainerCube(function(res) {
						var boxObCount = $('boxObCount').value;
						if (isNaN(boxObCount)) {
							boxObCount = 0;
						}
						var max20 = parseInt(res[0] / cbm) * boxObCount;
						var max40 = parseInt(res[1] / cbm) * boxObCount;
						var max40hq = parseInt(res[2] / cbm) * boxObCount;
						var max45 = parseInt(res[3] / cbm) * boxObCount;

						if ($('box20Count').value != ''
								&& $('box20Count').value > max20) {
							$('box20Count').value = max20;
						}
						if ($('box40Count').value != ''
								&& $('box40Count').value > max40) {
							$('box40Count').value = max40;
						}
						if ($('box40hqCount').value != ''
								&& $('box40hqCount').value > max40hq) {
							$('box40hqCount').value = max40hq;
						}
						if ($('box45Count').value != ''
								&& $('box45Count').value > max45) {
							$('box45Count').value = max45;
						}
						updateMapValue(rdm, 'box20Count', $('box20Count').value);
						updateMapValue(rdm, 'box40Count', $('box40Count').value);
						updateMapValue(rdm, 'box40hqCount',
								$('box40hqCount').value);
						updateMapValue(rdm, 'box45Count', $('box45Count').value);
					});
		}
	}

	// 通过单重计算净重/毛重
	function calWeight(txt, newVal, oldVal) {
		var rec = null;
		var rdm = $('orderDetailId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('orderDetailId', rdm);
		if (index != -1) {
			rec = ds.getAt(index);
		} else {
			return;
		}
		if (rdm != null && rdm != "" && $('eleId').value != "") {
			var boxObCount = rec.data.boxObCount;
			if (boxObCount == "") {
				boxObCount = 0;
			}
			cotElementsService.getDefaultList(function(res) {
						var jin = newVal * boxObCount / 1000;
						$('boxNetWeigth').value = jin.toFixed(3);
						var boxGrossWeigth = 0;
						if (res.length != 0) {
							if (res[0].grossNum != null) {
								var temp = jin + res[0].grossNum;
								boxGrossWeigth = temp.toFixed(3);
							}
						} else {
							boxGrossWeigth = jin.toFixed(3);
						}
						$('boxGrossWeigth').value = boxGrossWeigth;
						rec.set("boxNetWeigth", jin.toFixed(3));
						rec.set("boxGrossWeigth", boxGrossWeigth);
						updateMapValue(rdm, "boxWeigth", newVal);
						updateMapValue(rdm, "boxNetWeigth", jin.toFixed(3));
						updateMapValue(rdm, "boxGrossWeigth", boxGrossWeigth);
					});

		}
	}

	// 通过cbm计算cuft
	function calCuftCube(txt, newVal, oldVal) {
		var rec = null;
		var rdm = $('orderDetailId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('orderDetailId', rdm);
		if (index != -1) {
			rec = ds.getAt(index);
		} else {
			return;
		}
		var boxObCount = rec.data.boxObCount;
		if (rdm != null && rdm != "" && $('eleId').value != "") {
			if (isNaN(newVal) || newVal == "") {
				newVal = 0;
				$('boxCuft').value = 0;
			} else {
				var s = newVal * 35.315;
				$('boxCuft').value = s.toFixed(3);
			}
			updateMapValue(rdm, "boxCbm", newVal);
			updateMapValue(rdm, "boxCuft", $('boxCuft').value);

			if (boxObCount == "") {
				boxObCount = 0;
			}
			if (newVal == 0) {
				newVal = 0.0001;
			}
			queryService.getContainerCube(function(res) {
						$('box20Count').value = parseInt(res[0] / newVal)
								* boxObCount;
						$('box40Count').value = parseInt(res[1] / newVal)
								* boxObCount;
						$('box40hqCount').value = parseInt(res[2] / newVal)
								* boxObCount;
						$('box45Count').value = parseInt(res[3] / newVal)
								* boxObCount;
						updateMapValue(rdm, "box20Count", $('box20Count').value);
						updateMapValue(rdm, "box40Count", $('box40Count').value);
						updateMapValue(rdm, "box40hqCount",
								$('box40hqCount').value);
						updateMapValue(rdm, "box45Count", $('box45Count').value);
					});

		}
	}

	// 通过cuft计算cbm
	function calCbmCube(txt, newVal, oldVal) {
		var rec = null;
		var rdm = $('orderDetailId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('orderDetailId', rdm);
		if (index != -1) {
			rec = ds.getAt(index);
		} else {
			return;
		}
		var boxObCount = rec.data.boxObCount;
		if (rdm != null && rdm != "") {
			if (isNaN(newVal) || newVal == "") {
				newVal = 0;
				$('boxCbm').value = 0;
			} else {
				var s = newVal / 35.315;
				$('boxCbm').value = s.toFixed(cbmNum);
			}
			var cbm = $('boxCbm').value;
			rec.set('boxCbm', cbm.toFixed(cbmNum));
			updateMapValue(rdm, "boxCbm", cbm);
			updateMapValue(rdm, "boxCuft", newVal);

			if (boxObCount == "") {
				boxObCount = 0;
			}
			if (cbm == 0) {
				cbm = 0.0001;
			}
			queryService.getContainerCube(function(res) {
						$('box20Count').value = parseInt(res[0] / cbm)
								* boxObCount;
						$('box40Count').value = parseInt(res[1] / cbm)
								* boxObCount;
						$('box40hqCount').value = parseInt(res[2] / cbm)
								* boxObCount;
						$('box45Count').value = parseInt(res[3] / cbm)
								* boxObCount;
						updateMapValue(rdm, "box20Count", $('box20Count').value);
						updateMapValue(rdm, "box40Count", $('box40Count').value);
						updateMapValue(rdm, "box40hqCount",
								$('box40hqCount').value);
						updateMapValue(rdm, "box45Count", $('box45Count').value);
					});

		}
	}

	// 通过外包装数计算毛净重和装箱数
	function calWandC(txt, newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.data.orderDetailId;
		var boxCbm = rec.data.boxCbm;
		var boxWeigth = $('boxWeigth').value;
		if (rdm != null && rdm != "" && $('eleId').value != "") {
			if (newVal == "") {
				newVal = 0;
			}
			if (boxWeigth == "") {
				boxWeigth = 0;
			}
			if (boxCbm == 0) {
				boxCbm = 0.0001;
			}
			cotElementsService.getDefaultList(function(res) {
						var jin = boxWeigth * newVal / 1000;
						$('boxNetWeigth').value = jin.toFixed(3);
						var boxGrossWeigth = 0;
						if (res.length != 0) {
							if (res[0].grossNum != null) {
								var temp = jin + res[0].grossNum;
								boxGrossWeigth = temp.toFixed(3);
							}
						} else {
							boxGrossWeigth = jin.toFixed(3);
						}
						$('boxGrossWeigth').value = boxGrossWeigth;
						rec.set("boxNetWeigth", jin.toFixed(3));
						rec.set("boxGrossWeigth", boxGrossWeigth);
						updateMapValue(rdm, "boxNetWeigth", jin.toFixed(3));
						updateMapValue(rdm, "boxGrossWeigth", boxGrossWeigth);
					});
			queryService.getContainerCube(function(res) {
						$('box20Count').value = parseInt(res[0] / boxCbm)
								* newVal;
						$('box40Count').value = parseInt(res[1] / boxCbm)
								* newVal;
						$('box40hqCount').value = parseInt(res[2] / boxCbm)
								* newVal;
						$('box45Count').value = parseInt(res[3] / boxCbm)
								* newVal;
						updateMapValue(rdm, "box20Count", $('box20Count').value);
						updateMapValue(rdm, "box40Count", $('box40Count').value);
						updateMapValue(rdm, "box40hqCount",
								$('box40hqCount').value);
						updateMapValue(rdm, "box45Count", $('box45Count').value);
					});

		}
	}

	// 计算英寸
	function calInch(txt, newVal, oldVal) {
		var rdm = $('orderDetailId').value;
		if (rdm == null || rdm == "" || $('eleId').value == "") {
			return;
		}
		var inch = (newVal / 2.54).toFixed("2");
		var needId = txt.getName() + "Inch";
		$(needId).value = inch;
		updateMapValue(rdm, needId, inch);
		changeSizeTxt();
		if (sizeFollow == 1) {
			// 产品包装尺寸跟着变化
			if (needId == 'boxLInch') {
				$('boxPbL').value = $('boxL').value;
				updateMapValue(rdm, "boxPbL", $('boxPbL').value);
				$('boxPbLInch').value = $('boxLInch').value;
				updateMapValue(rdm, "boxPbLInch", $('boxPbLInch').value);
			}
			if (needId == 'boxWInch') {
				$('boxPbW').value = $('boxW').value;
				updateMapValue(rdm, "boxPbW", $('boxPbW').value);
				$('boxPbWInch').value = $('boxWInch').value;
				updateMapValue(rdm, "boxPbWInch", $('boxPbWInch').value);
			}
			if (needId == 'boxHInch') {
				$('boxPbH').value = $('boxH').value;
				updateMapValue(rdm, "boxPbH", $('boxPbH').value);
				$('boxPbHInch').value = $('boxHInch').value;
				updateMapValue(rdm, "boxPbHInch", $('boxPbHInch').value);
			}
		}
		calPrice();
	}

	// 计算CM
	function calCm(txt, newVal, oldVal) {
		var rdm = $('orderDetailId').value;
		if (rdm == null || rdm == "" || $('eleId').value == "") {
			return;
		}
		var cm = (newVal * 2.54).toFixed("2");
		var needId = txt.getName().substring(0, txt.getName().length - 4);
		$(needId).value = cm;
		updateMapValue(rdm, needId, cm);
		changeSizeTxt();
		if (sizeFollow == 1) {
			// 产品包装尺寸跟着变化
			if (needId == 'boxLInch') {
				$('boxPbL').value = $('boxL').value;
				updateMapValue(rdm, "boxPbL", $('boxPbL').value);
				$('boxPbLInch').value = $('boxLInch').value;
				updateMapValue(rdm, "boxPbLInch", $('boxPbLInch').value);
			}
			if (needId == 'boxWInch') {
				$('boxPbW').value = $('boxW').value;
				updateMapValue(rdm, "boxPbW", $('boxPbW').value);
				$('boxPbWInch').value = $('boxWInch').value;
				updateMapValue(rdm, "boxPbWInch", $('boxPbWInch').value);
			}
			if (needId == 'boxHInch') {
				$('boxPbH').value = $('boxH').value;
				updateMapValue(rdm, "boxPbH", $('boxPbH').value);
				$('boxPbHInch').value = $('boxHInch').value;
				updateMapValue(rdm, "boxPbHInch", $('boxPbHInch').value);
			}
		}
		calPrice();
	}

	// 中英文规格跟着变化
	function changeSizeTxt() {
		var rdm = $('orderDetailId').value;
		if (rdm == null || rdm == "" || $('eleId').value == "") {
			return;
		}
		$('eleInchDesc').value = $('boxLInch').value + "*"
				+ $('boxWInch').value + "*" + $('boxHInch').value;
		updateMapValue(rdm, "eleInchDesc", $('eleInchDesc').value);
		$('eleSizeDesc').value = $('boxL').value + "*" + $('boxW').value + "*"
				+ $('boxH').value;
		updateMapValue(rdm, "eleSizeDesc", $('eleSizeDesc').value);
	};

	// 更新内存数据
	function updateMapValue(detailId, property, value) {
		cotOrderOutService.updateChaMapValueByEleId(detailId, property, value,
				function(res) {
				});

	}

	// 计算包材价格
	function calPrice() {
		var rdm = $('orderDetailId').value;
		if (rdm == '' || $('eleId').value == "") {
			return;
		}
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
		cotOrderOutService.calPriceAll(rdm, function(res) {
					if (res != null) {
						$('boxPbPrice').value = res[0];
						$('boxIbPrice').value = res[1];
						$('boxMbPrice').value = res[2];
						$('boxObPrice').value = res[3];
						$('packingPrice').value = res[4];
						$('inputGridPrice').value = res[6];
						DWREngine.setAsync(false);
						cotOrderOutService.getChaOrderMapValue(rdm, function(
										obj) {
									if (obj != null) {
										obj.boxPbPrice = res[0];
										obj.boxIbPrice = res[1];
										obj.boxMbPrice = res[2];
										obj.boxObPrice = res[3];
										obj.packingPrice = res[4];
										if (res[5] != -1) {
											obj.priceFac = res[5];
										}
										obj.inputGridPrice = res[6];

										// 将对象储存到后台map中
										cotOrderOutService.setChaOrderMap(rdm,
												obj, function(def) {
												});
									}
								});
						DWREngine.setAsync(true);
						// 修改表格生产价和根据公式计算
						if (res[5] != -1 && index != -1) {
							var rec = ds.getAt(index);
							rec.set("priceFac", res[5].toFixed(facNum));
						}
					} else {
						Ext.MessageBox.alert('Message', 'please select a record!');
					}
				});
	}

	// 初始化页面,加载报表详细信息
	function initForm(record) {
		var eleId = record.data.eleId;
		var rdm = record.data.orderDetailId;
		var flag = true;
		if (isNaN(record.id)) {
			flag = false;
		}
		// flag区分是点击已存在行调用(true)还是新添加(false)
		if (flag) {
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
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
		var isMod = getPopedomByOpType("cotorderout.do", "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
		}
		if ((rdm == null || rdm == "") && $('picPath')!=null) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
			return;
		}
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(rdm, function(res) {
			if (res != null) {
				if (res.boxCbm != null) {
					res.boxCbm = res.boxCbm.toFixed(cbmNum);
				}
				if(res.eleHsid==0){
					res.eleHsid=null;
				}
				rightForm.getForm().setValues(res);
				if (popdom == true) {
					res.picPath = "common/images/noElePicSel.png";
				} else {
					var p = Math.round(Math.random() * 10000);
					if (!flag) {
						// 如果编号大于20,00000000的取"无图片"图片
						if (rdm >= 2000000000) {
							res.picPath = "common/images/zwtp.png";
						} else {
							res.picPath = "./showPicture.action?flag=order&detailId="
									+ rdm + "&tmp=" + p;
						}
					} else {
						res.picPath = "./showPicture.action?flag=cha&detailId="
								+ record.id + "&tmp=" + p;
					}
				}
				DWRUtil.setValue("picPath", res.picPath);
				typeLv1Box.bindPageValue("CotTypeLv1", "id", res.eleTypeidLv1);
				typeLv2Box.bindPageValue("CotTypeLv2", "id", res.eleTypeidLv2);
				eleFlagBox.setValue(res.eleFlag);
				if (res.eleFlag == 0) {
					Ext.getCmp("eleUnitNum").setDisabled(true);
				}
				eleHsidBox.bindPageValue("CotEleOther", "id", res.eleHsid);
				boxIbTypeBox.bindValue(res.boxIbTypeId);
				boxMbTypeBox.bindValue(res.boxMbTypeId);
				boxObTypeBox.bindValue(res.boxObTypeId);
				boxPbTypeBox.bindValue(res.boxPbTypeId);
				inputGridTypeIdBox.bindValue(res.inputGridTypeId);
				boxPacking.bindPageValue("CotBoxType", "id", res.boxTypeId);
				facBox.bindPageValue("CotFactory", "id", res.factoryId);
			}
		});
		DWREngine.setAsync(true);
	}

	// 全部导入
	function insertAll() {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 出货明细表格
		var outDetailGrid = parent.Ext.getCmp("outDetailGrid");
		var dsDe = outDetailGrid.getStore();
		if (dsDe.getCount() == 0) {
			Ext.MessageBox.alert('Message', '目前还没有出货明细!不能导入!');
			unmask();
			return;
		}

		// 如果表格有数据清空数据
		if (ds.getCount() > 0) {
			Ext.MessageBox.confirm('Message', "导入全部出货明细前,需要先清空报关明细数据,是否继续?",
					function(btn) {
						if (btn == 'yes') {
							DWREngine.setAsync(false);
							mask();
							// ds.removeAll();//不会触发后台删除方法??
							while (ds.getCount() > 0) {
								ds.removeAt(0);
							}
							chkFlag = true;
							DWREngine.setAsync(true);
							var total = 0;
							dsDe.each(function(ac) {
										var aOd = ac.data.orderDetailId;
										total += parseFloat(ac.data.totalMoney);
										DWREngine.setAsync(false);
										cotOrderOutService.getOrderMapValue(
												aOd, function(res) {
													if (res != null) {
														res.id = null;
														var u = new ds.recordType(res);
														ds.add(u);
														// 将对象储存到后台map中
														cotOrderOutService
																.setChaOrderMap(
																		aOd,
																		res,
																		function(
																				def) {
																		});
													}
												});
										DWREngine.setAsync(true);
									});
							// 报关总金额
							parent.$('totalChaLab').innerText = total
									.toFixed(deNum);
							unmask();
						}
					});
		} else {
			DWREngine.setAsync(false);
			mask();
			DWREngine.setAsync(true);
			var total = 0;
			dsDe.each(function(ac) {
						var aOd = ac.data.orderDetailId;
						total += parseFloat(ac.data.totalMoney);
						DWREngine.setAsync(false);
						cotOrderOutService.getOrderMapValue(aOd, function(res) {
									if (res != null) {
										res.id = null;
										var u = new ds.recordType(res);
										ds.add(u);
										// 将对象储存到后台map中
										cotOrderOutService.setChaOrderMap(aOd,
												res, function(def) {
												});
									}
								});
						DWREngine.setAsync(true);
					});
			// 报关总金额
			parent.$('totalChaLab').innerText = total.toFixed(deNum);
			unmask();
		}
	}

	// 导入更新
	function insertUpdate() {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('message', 'Sorry, some of order has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		DWREngine.setAsync(false);
		mask();
		DWREngine.setAsync(true);
		// 出货明细表格
		var outDetailGrid = parent.Ext.getCmp("outDetailGrid");
		var dsDe = outDetailGrid.getStore();
		var mods = dsDe.getModifiedRecords();
		if (mods.length == 0) {
			Ext.MessageBox.alert('提示消息', '目前出货明细没有最新记录,旧记录也没被修改过,不能导入!');
			unmask();
			return;
		}

		// 删除相同的记录
		for (var i = 0; i < mods.length; i++) {
			var ac = mods[i];
			var aOd = ac.data.orderDetailId;
			ds.each(function(bc) {
						var bOd = bc.data.orderDetailId;
						if (bOd == aOd) {
							ds.remove(bc);
							DWREngine.setAsync(false);
							cotOrderOutService.delChaMapByKey(aOd,
									function(def) {
									});
							DWREngine.setAsync(true);
							return false;
						}
					});
			DWREngine.setAsync(false);
			cotOrderOutService.getOrderMapValue(aOd, function(res) {
						if (res != null) {
							res.id = null;
							var u = new ds.recordType(res);
							ds.add(u);
							// 将对象储存到后台map中
							cotOrderOutService.setChaOrderMap(aOd, res,
									function(def) {
									});
						}
					});
			DWREngine.setAsync(true);
			var total = 0;
			ds.each(function(item) {
						total += parseFloat(item.data.totalMoney);
					});
			// 报关总金额
			parent.$('totalChaLab').innerText = total.toFixed(deNum);

		}
		// 更改报关总金额

		unmask();
	}
	// 保存报关明细
	var chkFlag = false;
	this.saveCha = function(mainId, currencyId) {
		if (ds.getModifiedRecords().length == 0 && ds.removed.length == 0) {
			return;
		}
		$('orderId').value = mainId;
		// 更改添加action参数
		var urlAdd = '&orderPrimId=' + mainId + '&currencyId=' + currencyId;
		// 更改修改action参数
		var urlMod = '&orderPrimId=' + mainId;

		// 更改删除action参数(点击全部导入时,在后台删除action中不清空session map)
		var urlDel = '&orderPrimId=' + mainId;
		if (chkFlag == true) {
			urlDel += '&flag=1';
		}
		ds.proxy.setApi({
					read : "cotorderout.do?method=queryChaInfo&orderId="
							+ mainId,
					create : "cotorderout.do?method=addCha" + urlAdd,
					update : "cotorderout.do?method=modifyCha" + urlMod,
					destroy : "cotorderout.do?method=removeCha" + urlDel
				});
		ds.save();
		chkFlag = false;

	}

	// 删除
	function onDel() {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		mask();
		// 延时
		var task = new Ext.util.DelayedTask(function() {
					var cord = sm.getSelections();
					Ext.each(cord, function(item) {
								ds.remove(item);
							});
					if (rightForm.isVisible()) {
						$('picPath').src = "./showPicture.action?flag=noPic";
						rightForm.getForm().reset();
					}
					// 货款总金额
					var temp = summary.getSumTypeValue('totalMoney');
					parent.$('totalChaLab').innerText = Number(temp)
							.toFixed(deNum);
					unmask();
				});
		task.delay(500);
	}

	// 统一更改订单品名
	function updatePin() {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var cord = sm.getSelections();
		Ext.each(cord, function(rec) {
			rec.set('orderName', $('pin').value);
			updateMapValue(rec.data.orderDetailId, "orderName", $('pin').value);
		});
		modlPanel.hide();
	}
	
	// 自动生成报关品名
	function updatePinAuto() {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var cord = sm.getSelections();
		Ext.each(cord, function(rec) {
			var obj=null;
			for (var i = 0; i < hsData.length; i++) {
				if(rec.data.eleHsid==hsData[i].id){
					obj=hsData[i];
					break;
				}
			}
			if(obj!=null){
				var temp = "";
				var name=obj.cnName;
				var nameEn=obj.enName;
				var hs=obj.hscode;
				if(name!=null){
					temp+=name+'/';
				}
				if(nameEn!=null){
					temp+=nameEn;
				}
				var chk=Ext.getCmp('addHsChk').checked;
				if(hs!=null && chk){
					temp+='/'+hs;
				}
				rec.set('orderName', temp);
				updateMapValue(rec.data.orderDetailId, "orderName",temp);
			}			
			
		});
		modlPanel.hide();
	}

	// 显示分类品名面板
	var modlPanel;
	function showModel(item, pressed) {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		// 判断是否有勾选明细
		var cord = sm.getSelections();
		if (cord.length == 0) {
			Ext.MessageBox.alert("Message", "Please select the records first!");
			return;
		}
		if ($('importDiv') == null) {
			// window默认在z-index为9000
			Ext.DomHelper.append(document.body, {
				html : '<div id="importDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (modlPanel == null) {
			modlPanel = new Ext.form.FormPanel({
				labelWidth : 80,
				padding : "5px",
				height : 120,
				width : 340,
				frame : true,
				title : "生成报关品名",
				tools : [{
							id : "close",
							handler : function(event, toolEl, p) {
								p.hide();
							}
						}],
				items : [{
					layout : 'hbox',
					labelAlign : "right",
					layoutConfig : {
						padding : '5',
						align : 'middle'
					},
					defaults : {
						margins : '0 5 0 0'
					},
					items : [{
								xtype : "label",
								labelWidth : 200,
								text : "1.按照格式(中文名/英文名",
								style : "marginTop:5",
								flex : 3
							},{
								xtype:'checkbox',
								boxLabel : "+/HS#):",
								id:'addHsChk',
								style:'margin-top: 0px;',
								listeners : {
								"render" : function(obj) {
									var tip = new Ext.ToolTip({
												target : obj.getEl(),
												anchor : 'top',
												maxWidth : 100,
												minWidth : 100,
												html : '勾选后加上HS#'
											});
									}
								}
							},{
								xtype : "button",
								flex : 1,
								text : "自动生成",
								handler : updatePinAuto
							}]
				}, {
					layout : 'hbox',
					labelAlign : "right",
					layoutConfig : {
						padding : '5',
						align : 'middle'
					},
					defaults : {
						margins : '0 5 0 0'
					},
					items : [{
								xtype : "label",
								text : "2.Customize:",
								style : "marginTop:5",
								flex : 1
							}, {
								xtype : "textfield",
								anchor : "95%",
								maxLength : 100,
								maxLengthText : "",
								flex : 3,
								id : "pin",
								name : "pin",
								listeners : {
									'specialkey' : function(txt, eObject) {
										var temp = txt.getValue();
										if (temp != ""
												&& eObject.getKey() == Ext.EventObject.ENTER) {
											updatePin();
										}
									}
								}
							}, {
								xtype : "button",
								flex : 1,
								text : "OK",
								handler : updatePin
							}]
				}]

			});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("importDiv").setLeftTop(left, top + 20);

			modlPanel.render("importDiv");
			$('pin').focus();
		} else {
			if (!modlPanel.isVisible()) {
				modlPanel.show();
			} else {
				modlPanel.hide();
			}
		}
	};

	// 统一计算价格
	function updatePriceByNum() {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var tyName = $("tyName").value;
		var opDo = $("opDo").value;
		var snum = $("snum").value;
		if (snum == "" || isNaN(snum)) {
			Ext.MessageBox.alert('Message', "请输入价格参数！");
			return;
		}
		var total = 0;
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					var orderDetailId = item.data.orderDetailId;
					var orderPrice = item.data.orderPrice;
					var boxCount = item.data.boxCount;
					if (boxCount == "") {
						boxCount = 0;
					}
					var tyPrice = orderPrice;
					if (tyName == 1) {
						tyPrice = item.data.priceFac;
					}
					if (tyPrice == "") {
						tyPrice = 0;
					}
					if (opDo == 0) {
						var temp = (parseFloat(tyPrice).mul(parseFloat(snum)))
								.toFixed(deNum);
						item.set("orderPrice", temp);
					} else {
						if (snum != 0) {
							var temp = (parseFloat(tyPrice)
									.div(parseFloat(snum))).toFixed(deNum);
							item.set("orderPrice", temp);
						}
					}
					var tP = item.data.orderPrice * boxCount;
					item.set("totalMoney", tP);
					updateMapValue(orderDetailId, "totalMoney", tP);
					updateMapValue(orderDetailId, "orderPrice",
							item.data.orderPrice);
				});
		cePanel.hide();
	}

	// 测算
	var ckStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, '原单价'], [1, '生产价']]
			});
	var ckBox = new Ext.form.ComboBox({
				fieldLabel : '',
				store : ckStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				value : 0,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'tyName',
				selectOnFocus : true,
				hideLabel : true,
				labelSeparator : ' ',
				columnWidth : .38
			});

	// 测算
	var opStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, '×'], [1, '÷']]
			});
	var opBox = new Ext.form.ComboBox({
				fieldLabel : '',
				store : opStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				value : 0,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'opDo',
				selectOnFocus : true,
				hideLabel : true,
				labelSeparator : ' ',
				columnWidth : .25
			});

	// 显示测算面板
	var cePanel;
	function showCe(item, pressed) {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		// 判断是否有勾选明细
		var cord = sm.getSelections();
		if (cord.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record!");
			return;
		}
		if (cePanel == null) {
			cePanel = new Ext.Window({
						labelWidth : 80,
						padding : "5px",
						height : 65,
						width : 250,
						layout : 'column',
						frame : true,
						title : "批量修改单价",
						labelAlign : "right",
						closeAction : 'hide',
						items : [ckBox, opBox, {
									xtype : "numberfield",
									anchor : "95%",
									columnWidth : .3,
									height : 23,
									maxValue : 9999.999,
									decimalPrecision : 3,
									id : "snum",
									name : "snum"
								}, {
									xtype : "button",
									text : "OK",
									width : 60,
									handler : updatePriceByNum
								}]
					});
		}
		if (!cePanel.isVisible()) {
			var po = item.getPosition();
			cePanel.setPosition(po[0] - 50, po[1] + 20);
			cePanel.show();
		} else {
			cePanel.hide();
		}

	};

	// 添加空白record到表格中
	var facPriceUnit = 0;// 默认币种ID
	function addNewGrid() {
		if (parent.$('orderStatus').value == 1
				|| parent.$('orderStatus').value == 2) {
			Ext.MessageBox.alert('Message', 'Sorry, some of order has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return;
		}

		// 验证表单
		var formData = parent.formVail();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		if (facPriceUnit == 0) {
			DWREngine.setAsync(false);
			cotOrderOutService.getList('CotPriceCfg', function(res) {
						if (res.length != 0 && res[0].facPriceUnit != null) {
							facPriceUnit = res[0].facPriceUnit;
						}
					});
			DWREngine.setAsync(true);
		}

		// 获得该行的新序号(该列的最大值+1)
		var sortNo = 0;
		ds.each(function(rec) {
					if (rec.data.sortNo > sortNo)
						sortNo = rec.data.sortNo;
				});

		// 取得"20"+8位随机数存到orderDetailId
		queryService.getRandom(8, function(res) {
					var rdm = "20" + res;
					var u = new ds.recordType({
								eleId : "",
								sortNo : sortNo + 1,
								custNo : "",
								orderName : "",
								eleName : "",
								eleNameEn : "",
								orderPrice : "",
								eleUnit : "",
								boxCount : "",
								containerCount : "",
								totalMoney : "",
								boxCbm : "",
								factoryId : "",
								factoryNo : "",
								priceFac : "",
								priceFacUint : (facPriceUnit == 0
										? ""
										: facPriceUnit),
								eleInchDesc : "",
								eleSizeDesc : "",
								boxIbCount : "",
								boxMbCount : "",
								boxObCount : "",
								boxObL : "",
								boxObW : "",
								boxObH : "",
								boxGrossWeigth : "",
								boxNetWeigth : "",
								orderDetailId : rdm
							});
					ds.add(u);
					// 货号获得焦点
					var cell = grid.getView().getCell(ds.getCount() - 1, 3);
					if (Ext.isIE) {
						cell.fireEvent("ondblclick");
					} else {
						var e = document.createEvent("Events");
						e.initEvent("dblclick", true, false);
						cell.dispatchEvent(e)
					}
				});
	}

	// 手动修改各个包材价格时修改单个价格及生产价
	function calPackPriceAndPriceFac() {
		var packingPrice = $('packingPrice').value;
		var pbPrice = $('boxPbPrice').value;
		var ibPrice = $('boxIbPrice').value;
		var mbPrice = $('boxMbPrice').value;
		var obPrice = $('boxObPrice').value;

		var boxObCount = $('boxObCount').value;
		var boxMbCount = $('boxMbCount').value;
		var boxIbCount = $('boxIbCount').value;
		var boxPbCount = $('boxPbCount').value;

		var boxIbPrice = 0;
		var boxMbPrice = 0;
		var boxObPrice = 0;
		var boxPbPrice = 0;
		var igPrice = 0;
		var igPrice = $('inputGridPrice').value;
		if (igPrice == null || igPrice == '') {
			igPrice = 0;
		}

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

		if (boxObCount != 0 && boxObCount != '') {
			packingPrice = ((parseFloat(boxIbPrice) + parseFloat(boxMbPrice)
					+ parseFloat(boxPbPrice) + parseFloat(boxObPrice) + parseFloat(igPrice)) / parseInt(boxObCount))
					.toFixed(facNum);
		}
		$('packingPrice').value = packingPrice;
		// 计算生产价
		calPriceByPakingPrice(packingPrice);
	}

	// 手动修改单个价格时
	function calPriceByPakingPrice(packPrice) {
		if (packPrice == '' || isNaN(packPrice)) {
			packPrice = 0;
		}
		var rdm = $('orderDetailId').value;
		var rec = editRec;
		cotOrderOutService.calPriceFacByPackPrice(rdm, packPrice, function(
						facprice) {
					if (facprice != -1) {
						rec.set("priceFac", facprice);
					}
				});

	}

	// 样品默认配置,是否自动生成产品包装尺寸
	var sizeFollow = 0;
	// 初始化
	function initform() {
		DWREngine.setAsync(false);
		cotElementsService.getDefaultList(function(res) {
					if (res.length != 0) {
						// 设置是否自动生成包装尺寸标识
						if (res[0].sizeFollow != null) {
							sizeFollow = res[0].sizeFollow;
						}
					}
				});
		// 加载集装箱的柜体积
		queryService.getContainerCube(function(res) {
					$('con20').value = res[0];
					$('con40').value = res[1];
					$('con40H').value = res[2];
					$('con45').value = res[3];
				});
		DWREngine.setAsync(true);
	}
	unmask();
	initform();

	var viewport = new Ext.Viewport({
				layout : "border",
				items : [grid, rightPanel]
			});

	// 加载表格数据
	ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});

	// 保存排序
	function sumSortTable() {
		var sort = ds.getSortState();
		if (!sort) {
			Ext.MessageBox.alert("提示消息", "排序没变化,不用再保存!");
			return;
		}
		if (parent.$("pId").value == '' || parent.$("pId").value == 'null') {
			Ext.MessageBox.alert("提示消息", "请先保存出货单,再更改排序!");
			return;
		}
		var fieldType = "";
		ds.each(function(rec) {
					var temp = rec.fields.get(sort.field).type;
					fieldType = temp.type;
					return false;
				});
		if (fieldType == 'auto') {
			fieldType = 'string'
		}
		var type = 0;
		if (sort.direction == 'DESC') {
			type = 1;
		}
		DWREngine.setAsync(false);
		cotOrderOutService.updateSortNoBao(type, sort.field, fieldType,
				function(res) {
					if (res) {
						ds.reload();
						Ext.MessageBox.alert('提示消息', "保存表格排序成功!");
					} else {
						Ext.MessageBox.alert('提示消息', "保存表格排序失败!");
					}
				});
		DWREngine.setAsync(true);
	}

	// 数量值改变事件
	function countNum(newVal, oldVal) {
		if (newVal == '' || isNaN(newVal)) {
			newVal = 0;
		}
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						// 未出货数量
						var unSendNum = rec.data.unSendNum;
						var nowRes = newVal - res.boxCount;
						// 单价
						var orderPrice = rec.data.orderPrice;
						// 箱数
						var containerCount = 0;
						// 外箱数
						var boxObCount = rec.data.boxObCount;
						// 未出货数量
						var unSend = 0;
						// 计算箱数
						if (newVal % boxObCount != 0) {
							containerCount = parseInt(newVal / boxObCount) + 1;
						} else {
							containerCount = newVal / boxObCount;
						}
						var totalMoney = (newVal * orderPrice).toFixed(deNum);

						// 重新计算总价
						parent.$('totalChaLab').innerText = (parseFloat(parent
								.$('totalChaLab').innerText)
								- res.totalMoney + parseFloat(totalMoney))
								.toFixed(deNum);

						res.containerCount = containerCount;
						res.totalMoney = totalMoney;

						rec.set('containerCount', containerCount);
						rec.set('totalMoney', totalMoney);
						res.boxCount = newVal;
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(result) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 箱数改变情况
	function containNum(newVal, oldVal) {
		if (newVal == '' || isNaN(newVal)) {
			newVal = 0;
		}
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						// 外箱数
						var boxObCount = rec.data.boxObCount;
						var boxCount = newVal * boxObCount;
						var nowRes = boxCount - res.boxCount;
						// 单价
						var orderPrice = rec.data.orderPrice;
						var totalMoney = (boxCount * orderPrice).toFixed('2');
						// 重新计算总价
						parent.$('totalChaLab').innerText = (parseFloat(parent
								.$('totalChaLab').innerText)
								- res.totalMoney + parseFloat(totalMoney))
								.toFixed(deNum);

						res.boxCount = boxCount;
						res.totalMoney = totalMoney;

						rec.set('boxCount', boxCount);
						rec.set('totalMoney', totalMoney);
						res.containerCount = newVal;
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(result) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 单价值改变事件
	function priceOutChange(newVal) {
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		if (orderDetailId != null && orderDetailId != "") {
			if (!rec.data.boxCount) {
				rec.data.boxCount = 0;
			}
			var boxCount = rec.data.boxCount;
			var total = boxCount * newVal;
			// 重新计算总价
			parent.$('totalChaLab').innerText = (parseFloat(parent
					.$('totalChaLab').innerText)
					- rec.data.totalMoney + boxCount * newVal).toFixed(deNum);

			rec.set("totalMoney", total.toFixed(deNum));
			updateMapValue(orderDetailId, "orderPrice", newVal);
			updateMapValue(orderDetailId, "totalMoney", total);
		}

	}

	// 内装数改变事件
	function boxIcountNum(newVal) {
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		if (orderDetailId == null || orderDetailId == "") {
			return;
		}
		updateMapValue(orderDetailId, "boxIbCount", newVal);
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						// 1.计算包材价格
						DWREngine.setAsync(false);
						cotOrderOutService.calPriceAllByOrderDetail(
								orderDetailId, function(def) {
									if (def != null) {
										res.boxIbPrice = def[1];
										res.packingPrice = def[4];
										if (def[5] != -1) {
											res.priceFac = def[5];
											rec.set("priceFac", def[5]);
										}
									}
								});
						DWREngine.setAsync(true);
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 中装数改变事件
	function boxMcountNum(newVal) {
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		if (orderDetailId == null || orderDetailId == "") {
			return;
		}
		updateMapValue(orderDetailId, "boxMbCount", newVal);
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						// 1.计算包材价格
						DWREngine.setAsync(false);
						cotOrderOutService.calPriceAllByOrderDetail(
								orderDetailId, function(def) {
									if (def != null) {
										res.boxMbPrice = def[2];
										res.packingPrice = def[4];
										if (def[5] != -1) {
											res.priceFac = def[5];
											rec.set("priceFac", def[5]);
										}
									}
								});
						DWREngine.setAsync(true);
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 修改右边的值
	function rightChange(field, value) {
		var field = rightForm.getForm().findField(field);
		field.setValue(value);
	}

	// 外箱数值改变事件
	function boxOcountNum(newVal) {
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		if (orderDetailId == null || orderDetailId == "") {
			return;
		}
		updateMapValue(orderDetailId, "boxObCount", newVal);

		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						var con20 = $('con20').value;
						var con40H = $('con40H').value;
						var con40 = $('con40').value;
						var con45 = $('con45').value;
						// 0.计算箱数
						var boxCbm = rec.data.boxCbm;
						if (boxCbm == 0) {
							boxCbm = 0.0001;
						}
						var box20Count = Math.floor(con20 / boxCbm)
								* boxObCount;
						var box40Count = Math.floor(con40 / boxCbm)
								* boxObCount;
						var box40hqCount = Math.floor(con40H / boxCbm)
								* boxObCount;
						var box45Count = Math.floor(con45 / boxCbm)
								* boxObCount;

						res.box20Count = box20Count;
						res.box40Count = box40Count;
						res.box40hqCount = box40hqCount;
						res.box45Count = box45Count;
						// 1.计算毛净重
						DWREngine.setAsync(false);
						cotElementsService.getDefaultList(function(dfg) {
									var gross = res.boxWeigth * boxObCount
											/ 1000;
									if (dfg.length != 0) {
										if (dfg[0].grossNum != null) {
											gross += dfg[0].grossNum;
										}
									}
									res.boxNetWeigth = res.boxWeigth
											* boxObCount / 1000;
									res.boxGrossWeigth = gross;
									rec.set("boxNetWeigth", res.boxNetWeigth);
									rec.set("boxGrossWeigth",
											res.boxGrossWeigth);
									rightChange("boxNetWeigth",
											res.boxNetWeigth);
									rightChange("boxGrossWeigth",
											res.boxGrossWeigth);
								});
						DWREngine.setAsync(true);

						// 2.计算包材价格
						DWREngine.setAsync(false);
						cotOrderOutService.calPriceAllByOrderDetail(
								orderDetailId, function(def) {
									if (def != null) {
										res.boxObPrice = def[3];
										res.packingPrice = def[4];
										if (def[5] != -1) {
											res.priceFac = def[5];
											rec.set("priceFac", def[5]);
										}
									}
								});
						DWREngine.setAsync(true);
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 根据明细中的外箱长改变事件
	function changeBoxObL(newVal, type, inch) {
		var rec = null;
		var orderDetailId;
		if (type) {
			orderDetailId = $('orderDetailId').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('orderDetailId', orderDetailId);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			orderDetailId = rec.data.orderDetailId;
			if (orderDetailId == null || orderDetailId == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObL", newVal);
		}
		updateMapValue(orderDetailId, "boxObL", newVal);
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						var boxObCount = rec.data.boxObCount;// 外装数
						var w = rec.data.boxObW;// 外箱宽
						var h = rec.data.boxObH;// 外箱高
						if (boxObCount == null)
							boxObCount = 0;
						if (w == null)
							w = 0;
						if (h == null)
							h = 0;

						var boxObLInch = (newVal / 2.54).toFixed("2");// 外箱英寸长
						var cbm = (newVal * w * h * 0.000001).toFixed(cbmNum);
						if (cbm == 0) {
							cbm = 0.0001;
						}

						var con20 = $('con20').value;
						var con40H = $('con40H').value;
						var con40 = $('con40').value;
						var con45 = $('con45').value;
						var box20Count = 0;
						var box40Count = 0;
						var box40hqCount = 0;
						var box45Count = 0;
						box20Count = Math.floor(con20 / cbm) * boxObCount;
						box40Count = Math.floor(con40 / cbm) * boxObCount;
						box40hqCount = Math.floor(con40H / cbm) * boxObCount;
						box45Count = Math.floor(con45 / cbm) * boxObCount;

						res.boxObL = newVal;
						res.boxObLInch = boxObLInch;
						res.boxCbm = cbm;
						res.boxCuft = (cbm * 35.315).toFixed("3");
						res.box20Count = box20Count;
						res.box40Count = box40Count;
						res.box40hqCount = box40hqCount;
						res.box45Count = box45Count;
						rec.set("boxCbm", cbm);
						rightChange("boxCbm", cbm);
						rightChange("boxObLInch", boxObLInch);
						rightChange("boxCuft", (cbm * 35.315).toFixed("3"));
						rightChange("box20Count", box20Count);
						rightChange("box40Count", box40Count);
						rightChange("box40hqCount", box40hqCount);
						rightChange("box45Count", box45Count);
						// 计算包材价格
						DWREngine.setAsync(false);
						cotOrderOutService.calPriceAllByOrderDetail(
								orderDetailId, function(def) {
									if (def != null) {
										res.boxPbPrice = def[0];
										res.boxIbPrice = def[1];
										res.boxMbPrice = def[2];
										res.boxObPrice = def[3];
										res.packingPrice = def[4];
										if (def[5] != -1) {
											res.priceFac = def[5];
											rec.set("priceFac", def[5]);
										}
									}
								});
						DWREngine.setAsync(true);
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 根据明细中的外箱宽改变事件
	function changeBoxObW(newVal, type, inch) {
		var rec = null;
		var orderDetailId;
		if (type) {
			orderDetailId = $('orderDetailId').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('orderDetailId', orderDetailId);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			orderDetailId = rec.data.orderDetailId;
			if (orderDetailId == null || orderDetailId == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObW", newVal);
		}
		updateMapValue(orderDetailId, "boxObW", newVal);
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						var boxObCount = rec.data.boxObCount;// 外装数
						var L = rec.data.boxObL;// 外箱长
						var h = rec.data.boxObH;// 外箱高

						if (boxObCount == null)
							boxObCount = 0;
						if (L == null)
							L = 0;
						if (h == null)
							h = 0;

						var boxObWInch = (newVal / 2.54).toFixed("2");// 外箱英寸宽
						var cbm = (newVal * L * h * 0.000001).toFixed(cbmNum);
						if (cbm == 0) {
							cbm = 0.0001;
						}

						var con20 = $('con20').value;
						var con40H = $('con40H').value;
						var con40 = $('con40').value;
						var con45 = $('con45').value;
						var box20Count = 0;
						var box40Count = 0;
						var box40hqCount = 0;
						var box45Count = 0;
						box20Count = Math.floor(con20 / cbm) * boxObCount;
						box40Count = Math.floor(con40 / cbm) * boxObCount;
						box40hqCount = Math.floor(con40H / cbm) * boxObCount;
						box45Count = Math.floor(con45 / cbm) * boxObCount;

						res.boxObW = newVal;
						res.boxObWInch = boxObWInch;
						res.boxCbm = cbm;
						res.boxCuft = (cbm * 35.315).toFixed("3");
						res.box20Count = box20Count;
						res.box40Count = box40Count;
						res.box40hqCount = box40hqCount;
						res.box45Count = box45Count;
						rec.set("boxCbm", cbm);
						rightChange("boxCbm", cbm);
						rightChange("boxObWInch", boxObWInch);
						rightChange("boxCuft", (cbm * 35.315).toFixed("3"));
						rightChange("box20Count", box20Count);
						rightChange("box40Count", box40Count);
						rightChange("box40hqCount", box40hqCount);
						rightChange("box45Count", box45Count);
						// 计算包材价格
						DWREngine.setAsync(false);
						cotOrderOutService.calPriceAllByOrderDetail(
								orderDetailId, function(def) {
									if (def != null) {
										res.boxPbPrice = def[0];
										res.boxIbPrice = def[1];
										res.boxMbPrice = def[2];
										res.boxObPrice = def[3];
										res.packingPrice = def[4];
										if (def[5] != -1) {
											res.priceFac = def[5];
											rec.set("priceFac", def[5]);
										}
									}
								});
						DWREngine.setAsync(true);
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 根据明细中的外箱高改变事件
	function changeBoxObH(newVal, type, inch) {
		var rec = null;
		var orderDetailId;
		if (type) {
			orderDetailId = $('orderDetailId').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('orderDetailId', orderDetailId);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			orderDetailId = rec.data.orderDetailId;
			if (orderDetailId == null || orderDetailId == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObH", newVal);
		}
		updateMapValue(orderDetailId, "boxObH", newVal);
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						var boxObCount = rec.data.boxObCount;// 外装数
						var L = rec.data.boxObL;// 外箱长
						var w = rec.data.boxObW;// 外箱宽

						if (boxObCount == null)
							boxObCount = 0;
						if (L == null)
							L = 0;
						if (w == null)
							w = 0;

						var boxObHInch = (newVal / 2.54).toFixed("2");// 外箱英寸高
						var cbm = (newVal * L * w * 0.000001).toFixed(cbmNum);
						if (cbm == 0) {
							cbm = 0.0001;
						}

						var con20 = $('con20').value;
						var con40H = $('con40H').value;
						var con40 = $('con40').value;
						var con45 = $('con45').value;
						var box20Count = 0;
						var box40Count = 0;
						var box40hqCount = 0;
						var box45Count = 0;
						box20Count = Math.floor(con20 / cbm) * boxObCount;
						box40Count = Math.floor(con40 / cbm) * boxObCount;
						box40hqCount = Math.floor(con40H / cbm) * boxObCount;
						box45Count = Math.floor(con45 / cbm) * boxObCount;

						res.boxObH = newVal;
						res.boxObHInch = boxObHInch;
						res.boxCbm = cbm;
						res.boxCuft = (cbm * 35.315).toFixed("3");
						res.box20Count = box20Count;
						res.box40Count = box40Count;
						res.box40hqCount = box40hqCount;
						res.box45Count = box45Count;
						// 更改CBM单元格
						rec.set("boxCbm", cbm);
						rightChange("boxCbm", cbm);
						rightChange("boxObHInch", boxObHInch);
						rightChange("boxCuft", (cbm * 35.315).toFixed("3"));
						rightChange("box20Count", box20Count);
						rightChange("box40Count", box40Count);
						rightChange("box40hqCount", box40hqCount);
						rightChange("box45Count", box45Count);
						// 计算包材价格
						DWREngine.setAsync(false);
						cotOrderOutService.calPriceAllByOrderDetail(
								orderDetailId, function(def) {
									if (def != null) {
										res.boxPbPrice = def[0];
										res.boxIbPrice = def[1];
										res.boxMbPrice = def[2];
										res.boxObPrice = def[3];
										res.packingPrice = def[4];
										if (def[5] != -1) {
											res.priceFac = def[5];
											rec.set("priceFac", def[5]);
										}
									}
								});
						DWREngine.setAsync(true);
						// 将对象储存到后台map中
						cotOrderOutService.setChaOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// cbm的更改事件,计算出装箱数
	function changeCbm(newVal) {
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		if (orderDetailId == null || orderDetailId == "") {
			return;
		}
		DWREngine.setAsync(false);
		cotOrderOutService.getChaOrderMapValue(orderDetailId, function(res) {
			if (res != null) {
				var boxObCount = rec.data.boxObCount;// 外装数


				var con20 = $('con20').value;
				var con40H = $('con40H').value;
				var con40 = $('con40').value;
				var con45 = $('con45').value;
				if (boxObCount == '' || isNaN(boxObCount)) {
					boxObCount = 0;
				}
				var box20Count =0;
				var box40Count =0;
				var box40hqCount =0;
				var box45Count =0;
				if (newVal != '' && newVal != 0) {
					box20Count = Math.floor(con20 / newVal) * boxObCount;
					box40Count = Math.floor(con40 / newVal) * boxObCount;
					box40hqCount = Math.floor(con40H / newVal) * boxObCount;
					box45Count = Math.floor(con45 / newVal) * boxObCount;

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
				var containerCount = rec.data.containerCount;

				res.boxCbm = newVal;
				res.boxCuft = (newVal * 35.315).toFixed("3");
				res.totalCbm = containerCount * newVal;
				rightChange("boxCuft", res.boxCuft);
				rightChange("boxCbm", newVal);
				rightChange("box20Count", box20Count);
				rightChange("box40Count", box40Count);
				rightChange("box40hqCount", box40hqCount);
				rightChange("box45Count", box45Count);
				// 将对象储存到后台map中
				cotOrderOutService.setChaOrderMap(orderDetailId, res, function(
								res) {
						});
			}
		});
		DWREngine.setAsync(true);
	}
});
