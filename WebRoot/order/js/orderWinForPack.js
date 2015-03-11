OrderWinForPack = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var factoryData;
	var packData;
	DWREngine.setAsync(false);
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				factoryData = res;
			});
	// 加载包装材料表缓存
	baseDataUtil.getBaseDicDataMap("CotBoxPacking", "id", "value",
			function(res) {
				packData = res;
			});
	DWREngine.setAsync(true);

	// 左边查询订单明细
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "custNo"
			}, {
				name : "boxCount"
			}, {
				name : "containerCount"
			}, {
				name : "boxPbCount"
			}, {
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
			}, {
				name : "orderId"
			}, {
				name : "boxPbL"
			}, {
				name : "boxPbW"
			}, {
				name : "boxPbH"
			}, {
				name : "boxIbL"
			}, {
				name : "boxIbW"
			}, {
				name : "boxIbH"
			}, {
				name : "boxMbL"
			}, {
				name : "boxMbW"
			}, {
				name : "boxMbH"
			}, {
				name : "boxObL"
			}, {
				name : "boxObW"
			}, {
				name : "boxObH"
			}, {
				name : "boxRemark"
			}, {
				name : "putL"
			}, {
				name : "putW"
			}, {
				name : "putH"
			}]);

	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryOrderDetail&orderId="
									+ cfg.orderId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});

	// 创建复选框列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [{
							header : "Sort No.",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Art No.",
							width : 90,
							dataIndex : "eleId"
						}, {
							header : "Description",
							width : 100,
							dataIndex : "eleName"
						}, {
							header : "Quantity",
							width : 80,
							dataIndex : "boxCount"
						}, {
							header : "Packing",
							width : 70,
							dataIndex : "boxPbCount"
						}, {
							header : "Packing of inner box",
							dataIndex : "boxIbCount"
						}, {
							header : "Packing of middle box",
							dataIndex : "boxMbCount"
						}, {
							header : "Packing of outer carton",
							dataIndex : "boxObCount"
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});

	var form = new Ext.FormPanel({
				region : 'north',
				height : 40,
				formId : "packFormFind",
				buttonAlign : 'right', // 按钮居右显示
				labelAlign : "right",
				padding : "5",
				layout : 'column',
				border : false,
				plain : true,
				frame : true,
				items : [{
							layout : 'form',
							columnWidth : .6,
							labelWidth : 30, // 标签宽度
							items : [{
										xtype : "textfield",
										fieldLabel : "Art No.",
										id : "eleIdFindPack",
										name : "eleIdFindPack",
										anchor : "95%"
									}]
						}, {
							layout : 'table',
							columnWidth : .4,
							items : [{
										xtype : 'button',
										text : "Search",
										width : 65,
										iconCls : "page_sel",
										handler : function() {
											ds.reload();
										}
									}]
						}]
			});

	var grid = new Ext.grid.GridPanel({
				region : 'center',
				width : 200,
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	// 表格-厂家
	var facGridBox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=3&validUrl=cotfactory.do",
		sendMethod : "post",
		cmpId : "facId",
		editable : true,
		autoLoad : true,
		pageSize : 10,
		minChars : 1,
		valueField : "id",
		displayField : "shortName",
		emptyText : "Choose",
		hideLabel : true,
		labelSeparator : " ",
		anchor : "100%"
	});

	// 材料类型
	// var shenStore = new Ext.data.SimpleStore({
	// fields : ["tp", "name"],
	// data : [[0, '请选择'], [1, '产品包装'], [2, '内包装'], [3, '中包装'],
	// [4, '外包装'], [5, '插格']]
	// });
	var shenBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				store : new Ext.data.ArrayStore({
							'id' : 0,
							fields : ['value', 'text'],
							data : [],
							autoDestroy : true
						}),
				// store : shenStore,
				valueField : "value",
				displayField : "text",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'orderStatus',
				hideLabel : true,
				labelSeparator : ' ',
				selectOnFocus : true,
				listeners : {
					'select' : function(com, rec, index) {
						var val = rec.data.value;
						// 查询选择的类型是否已存在
						var flag = false;
						dsPk.each(function(item) {
									var boxTypeId = item.data.boxTypeId;
									if (boxTypeId == val && val != -1) {
										flag = true;
										return false;
									}
								});
						if (flag) {
							com.setValue(-1);
							Ext.MessageBox.alert('Message', 'The material Already exists!');
							return;
						}
						// 设置长宽高
						var recPar = ds.getAt(row);
						var rec = dsPk.getAt(rowCurrent);
						if (val == 0) {
							rec.set("sizeL", recPar.data.boxPbL);
							rec.set("sizeW", recPar.data.boxPbW);
							rec.set("sizeH", recPar.data.boxPbH);
							// 设置数量
							var boxCount = recPar.data.boxCount;
							var boxPbCount = recPar.data.boxPbCount;
							var count = Math.ceil(boxCount / boxPbCount);
							rec.set("packCount", count);
						}
						if (val == 1) {
							rec.set("sizeL", recPar.data.boxIbL);
							rec.set("sizeW", recPar.data.boxIbW);
							rec.set("sizeH", recPar.data.boxIbH);
							// 设置数量
							var boxCount = recPar.data.boxCount;
							var boxIbCount = recPar.data.boxIbCount;
							var count = Math.ceil(boxCount / boxIbCount);
							rec.set("packCount", count);
						}
						if (val == 2) {
							rec.set("sizeL", recPar.data.boxMbL);
							rec.set("sizeW", recPar.data.boxMbW);
							rec.set("sizeH", recPar.data.boxMbH);
							// 设置数量
							var boxCount = recPar.data.boxCount;
							var boxMbCount = recPar.data.boxMbCount;
							var count = Math.ceil(boxCount / boxMbCount);
							rec.set("packCount", count);
						}
						if (val == 3) {
							rec.set("sizeL", recPar.data.boxObL);
							rec.set("sizeW", recPar.data.boxObW);
							rec.set("sizeH", recPar.data.boxObH);
							// 设置数量
							rec.set("packCount", recPar.data.containerCount);
						}
						if (val == 4) {
							rec.set("sizeL", recPar.data.putL);
							rec.set("sizeW", recPar.data.putW);
							rec.set("sizeH", recPar.data.putH);
							// 设置数量
							rec.set("packCount", recPar.data.containerCount);
						}
					}
				}
			});

	// 材料名称
	var caiBox = new Ext.form.ComboBox({
				xtype : "combo",
				store : new Ext.data.ArrayStore({
							'id' : 0,
							fields : ['value', 'text'],
							data : [],
							autoDestroy : true
						}),
				mode : 'local',
				valueField : 'value',
				displayField : 'text',
				triggerAction : "all",
				hiddenName : "facIdFind",
				hideLabel : true,
				labelSeparator : ' ',
				anchor : "100%",
				listeners : {
					'select' : function(com, rec, index) {
						var val = rec.data.value;
						var recPar = ds.getAt(row);
						var rec = dsPk.getAt(rowCurrent);

						// 返回单价和厂家
						cotPackOrderService.getBoxPackingById(recPar.id, val,
								function(res) {
									if (res != null) {
										// 单价
										rec.set("packPrice", res[0]);
										// 计算金额
										rec.set("totalAmount",
												(res[0] * rec.data.packCount)
														.toFixed("2"));
										// 包材厂家
										rec.set("factoryId", res[1]);
									}
								});
					}
				}
			});

	// -------------------包材明细-------------------
	var roleRecordPk = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "custNo"
			}, {
				name : "orderCount"
			}, {
				name : "containerCount"
			}, {
				name : "boxPbCount"
			}, {
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
			}, {
				name : "boxRemark"
			}, {
				name : "boxTypeId"
			}, {
				name : "boxPackingId"
			}, {
				name : "packCount"
			}, {
				name : "packPrice"
			}, {
				name : "sizeL"
			}, {
				name : "sizeW"
			}, {
				name : "sizeH"
			}, {
				name : "factoryId"
			}, {
				name : "orderId"
			}, {
				name : "orderdetailId"
			}, {
				name : "totalAmount"
			}, {
				name : "flag"
			}, {
				name : "anyFlag"
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var dsPk = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotpackanys.do?method=query&orderId="
								+ cfg.orderId,
						create : "cotpackanys.do?method=add",
						update : "cotpackanys.do?method=modify",
						destroy : "cotpackanys.do?method=remove"
					},
					listeners : {
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("Message", "Operation failed！");
							} else {
								reloadGrid('packAnysGrid');
								dsPk.reload();
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
				}, roleRecordPk),
		writer : writer
	});

	// 创建复选框列
	var smPk = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
	// 创建需要在表格显示的列
	var cmPk = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [{
							header : "Sort No.",
							dataIndex : "id",
							hidden : true
						}, {
							header : "Material",
							width : 70,
							editor : shenBox,
							dataIndex : "boxTypeId",
							renderer : function(value) {
								if (value == -1) {
									return "Choose";
								}
								if (value == 0) {
									return "Packing Size";
								}
								if (value == 1) {
									return "Inner Box";
								}
								if (value == 2) {
									return "Middle Box";
								}
								if (value == 3) {
									return "Outer Carton";
								}
								if (value == 4) {
									return "插格包装";
								}
							}
						}, {
							header : "Material",
							width : 80,
							editor : caiBox,
							dataIndex : "boxPackingId",
							renderer : function(value) {
								return packData["" + value];
							}
						}, {
							header : "<font color=blue>Supplier</font>",
							width : 80,
							editor : facGridBox,
							dataIndex : "factoryId",
							renderer : function(value) {
								return factoryData["" + value];
							}
						}, {
							header : "<font color=blue>Quantity</font>",
							width : 60,
							dataIndex : "packCount",
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changeMoneyByNum(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Length(CM)</font>",
							width : 50,
							dataIndex : "sizeL",
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changePriceBySizeL(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Width(CM)</font>",
							width : 50,
							dataIndex : "sizeW",
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changePriceBySizeW(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Height(CM)</font>",
							width : 50,
							dataIndex : "sizeH",
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												changePriceBySizeH(newVal);
											}
										}
									})
						}, {
							header : "<font color=blue>Price</font>",
							width : 70,
							dataIndex : "packPrice",
							editor : new Ext.form.NumberField({
										maxValue : 999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											},
											"change" : function(txt, newVal,
													oldVal) {
												priceChange(newVal);
											}
										}
									})
						}, {
							header : "Amount",
							width : 70,
							dataIndex : "totalAmount"
						}, {
							header : "orderdetailId",
							hidden : true,
							dataIndex : "orderdetailId"
						}]
			});
	var tbPk = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Create",
							handler : addToGrid,
							iconCls : "page_add"
						}, '-', {
							text : "Delete",
							handler : onDel,
							iconCls : "page_del"
						}, '-', {
							text : "Yes",
							handler : function() {
								dsPk.save();
							},
							iconCls : "page_mod"
						}]
			});

	var gridPk = new Ext.grid.EditorGridPanel({
				region : 'center',
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : dsPk, // 加载数据源
				cm : cmPk, // 加载列
				sm : smPk,
				loadMask : true, // 是否显示正在加载
				tbar : tbPk,
				viewConfig : {
					forceFit : false
				}
			});

	// 储存订单信息
	var bc = 0;
	var pc = 0;
	var ic = 0;
	var mc = 0;
	var oc = 0;
	var row = -1;
	// 行点击加载该货号的包材资料
	grid.on("rowclick", function(grid, rowIndex, e) {
				row = rowIndex;
				// 查询该货号的所有包材信息
				var rec = ds.getAt(rowIndex);
				bc = rec.data.boxCount;
				pc = rec.data.boxPbCount;
				ic = rec.data.boxIbCount;
				mc = rec.data.boxMbCount;
				oc = rec.data.boxObCount;
				dsPk.load({
							params : {
								start : 0,
								limit : 20,
								orderDetailId : rec.id
							}
						});
			});

	// 双击单元格时加载材料名称
	var rowCurrent = -1;
	gridPk.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				rowCurrent = rowIndex;
				var rec = dsPk.getAt(rowIndex);
				var dataIndex=cmPk.getDataIndex(columnIndex);
				if (!isNaN(rec.id) && dataIndex != 'factoryId' && dataIndex != 'packCount'
						&& dataIndex != 'sizeL' && dataIndex != 'sizeW'
						&& dataIndex != 'sizeH' && dataIndex != 'packPrice') {
					return false;
				}

				if (columnIndex == 2) {
					var type = rec.data.boxTypeId;
					if (type == '') {
						Ext.MessageBox.alert('message', "Please select product's Material!");
						return false;
					} else {
						var temp = '';
						if (type == 0) {
							temp = 'PB';
						}
						if (type == 1) {
							temp = 'IB';
						}
						if (type == 2) {
							temp = 'MB';
						}
						if (type == 3) {
							temp = 'OB';
						}
						if (type == 4) {
							temp = 'IG';
						}
						var data = [];
						baseDataUtil.getBaseDicMapById("CotBoxPacking", "id",
								"value", "type", temp, function(res) {
									for (var p in res)
										data.push(["" + p, res[p]]);
									caiBox.getStore().loadData(data);
								});
					}

				}
			});
	// -------------------包材明细-------------------

	// 添加到分析表格中
	function addToGrid() {
		// 判断有没有选择一条订单明细
		if (row == -1) {
			Ext.MessageBox.alert('Message', 'Please select left side of page!');
			return;
		}
		if (bc == 0) {
			Ext.MessageBox.alert('Message', '该订单记录没有数量,不能添加!');
			return;
		}
		var data = [0, 1, 2, 3, 4];
		dsPk.each(function(item) {
					var type = item.data.boxTypeId;
					data.remove(type);
				});
		for (var i = 0; i < data.length; i++) {
			if (data[i] == 0 && pc == 0) {
				data.remove(0);
			}
			if (data[i] == 1 && ic == 0) {
				data.remove(1);
			}
			if (data[i] == 2 && mc == 0) {
				data.remove(2);
			}
			if (data[i] == 3 && oc == 0) {
				data.remove(3);
			}
		}

		var temp = [];
		temp.push(["-1", '请选择']);
		for (var i = 0; i < data.length; i++) {
			if (data[i] == 0) {
				temp.push(["0", 'Packing Size']);
			}
			if (data[i] == 1) {
				temp.push(["1", 'Inner Box']);
			}
			if (data[i] == 2) {
				temp.push(["2", 'Middle Box']);
			}
			if (data[i] == 3) {
				temp.push(["3", 'Outer Carton']);
			}
			if (data[i] == 4) {
				temp.push(["4", '插格包装']);
			}
		}
		if (temp.length == 1) {
			Ext.MessageBox.alert('提示消息', '没有可添加的材料类型!');
			return;
		} else {
			shenBox.getStore().loadData(temp);

			var recPar = ds.getAt(row);

			var u = new dsPk.recordType({
						eleId : recPar.data.eleId,
						eleName : recPar.data.eleName,
						custNo : recPar.data.custNo,
						orderCount : recPar.data.boxCount,
						containerCount : recPar.data.containerCount,
						boxPbCount : recPar.data.boxPbCount,
						boxIbCount : recPar.data.boxIbCount,
						boxMbCount : recPar.data.boxMbCount,
						boxObCount : recPar.data.boxObCount,
						boxRemark : recPar.data.boxRemark,
						boxTypeId : -1,
						boxPackingId : "",
						packCount : "",
						packPrice : 0,
						sizeL : "",
						sizeW : "",
						sizeH : "",
						factoryId : "",
						orderId : recPar.data.orderId,
						orderdetailId : recPar.id,
						totalAmount : 0,
						flag : 1,
						anyFlag : 'U'
					});
			dsPk.add(u);
		}

	}

	// 删除
	function onDel() {
		var cord = smPk.getSelections();
		Ext.each(cord, function(item) {
					dsPk.remove(item);
				});
	}

	// 单价改变金额
	function priceChange(newVal) {
		var rec = dsPk.getAt(rowCurrent);
		if (rec.data.boxTypeId == -1) {
			return;
		}
		// 计算金额
		var temp = (rec.data.packCount * parseFloat(newVal)).toFixed('2');
		rec.set("totalAmount", temp);
	}

	// 数量改变时,改变总金额
	function changeMoneyByNum(newVal) {
		var rec = dsPk.getAt(rowCurrent);
		if (rec.data.boxTypeId == -1) {
			return;
		}
		// 单价
		var packPrice = rec.data.packPrice;
		if (packPrice == '' || isNaN(packPrice)) {
			packPrice = 0;
		}

		// 计算金额
		var temp = (newVal * parseFloat(packPrice)).toFixed('2');
		rec.set("totalAmount", temp);
	}

	// 长宽高改变时,如果该材料的计算公式含有此参数就改变单价和总金额
	function changePriceBySizeL(newVal) {
		var rec = dsPk.getAt(rowCurrent);
		if (rec.data.boxTypeId == -1) {
			return;
		}
		// 获得材料
		var boxPackingId = rec.data.boxPackingId;;
		if (boxPackingId == '') {
			return;
		}

		// 获得订单明细
		var detail = ds.getAt(row).data;
		// 获得材料类型,对应填充该类型的长宽高
		var boxTypeId = rec.data.boxTypeId;
		var sizeW = rec.data.sizeW;
		var sizeH = rec.data.sizeH;
		if (newVal == '' || isNaN(newVal)) {
			newVal = 0;
		}
		if (sizeW == '' || isNaN(sizeW)) {
			sizeW = 0;
		}
		if (sizeH == '' || isNaN(sizeH)) {
			sizeH = 0;
		}
		if (boxTypeId == 0) {
			detail.boxPbL = newVal;
			detail.boxPbW = sizeW;
			detail.boxPbH = sizeH;
		}
		if (boxTypeId == 1) {
			detail.boxIbL = newVal;
			detail.boxIbW = sizeW;
			detail.boxIbH = sizeH;
		}
		if (boxTypeId == 2) {
			detail.boxMbL = newVal;
			detail.boxMbW = sizeW;
			detail.boxMbH = sizeH;
		}
		if (boxTypeId == 3) {
			detail.boxObL = newVal;
			detail.boxObW = sizeW;
			detail.boxObH = sizeH;
		}
		if (boxTypeId == 4) {
			detail.putL = newVal;
			detail.putW = sizeW;
			detail.putH = sizeH;
		}

		// 返回单价和厂家
		cotPackOrderService.getPackPriceByType(detail, boxPackingId, function(
						res) {
					// 单价金额
					rec.set("packPrice", res);
					// 计算金额
					var temp = (rec.data.packCount * res).toFixed('2');
					rec.set("totalAmount", temp);
				});

	}

	// 长宽高改变时,如果该材料的计算公式含有此参数就改变单价和总金额
	function changePriceBySizeW(newVal) {
		var rec = dsPk.getAt(rowCurrent);
		if (rec.data.boxTypeId == -1) {
			return;
		}
		// 获得材料
		var boxPackingId = rec.data.boxPackingId;;
		if (boxPackingId == '') {
			return;
		}

		// 获得订单明细
		var detail = ds.getAt(row).data;
		// 获得材料类型,对应填充该类型的长宽高
		var boxTypeId = rec.data.boxTypeId;
		var sizeL = rec.data.sizeL;
		var sizeH = rec.data.sizeH;
		if (sizeL == '' || isNaN(sizeL)) {
			sizeL = 0;
		}
		if (newVal == '' || isNaN(newVal)) {
			newVal = 0;
		}
		if (sizeH == '' || isNaN(sizeH)) {
			sizeH = 0;
		}
		if (boxTypeId == 0) {
			detail.boxPbL = sizeL;
			detail.boxPbW = newVal;
			detail.boxPbH = sizeH;
		}
		if (boxTypeId == 1) {
			detail.boxIbL = sizeL;
			detail.boxIbW = newVal;
			detail.boxIbH = sizeH;
		}
		if (boxTypeId == 2) {
			detail.boxMbL = sizeL;
			detail.boxMbW = newVal;
			detail.boxMbH = sizeH;
		}
		if (boxTypeId == 3) {
			detail.boxObL = sizeL;
			detail.boxObW = newVal;
			detail.boxObH = sizeH;
		}
		if (boxTypeId == 4) {
			detail.putL = sizeL;
			detail.putW = newVal;
			detail.putH = sizeH;
		}

		// 返回单价和厂家
		cotPackOrderService.getPackPriceByType(detail, boxPackingId, function(
						res) {
					// 单价金额
					rec.set("packPrice", res);
					// 计算金额
					var temp = (rec.data.packCount * res).toFixed('2');
					rec.set("totalAmount", temp);
				});

	}

	// 长宽高改变时,如果该材料的计算公式含有此参数就改变单价和总金额
	function changePriceBySizeH(newVal) {
		var rec = dsPk.getAt(rowCurrent);
		if (rec.data.boxTypeId == -1) {
			return;
		}
		// 获得材料
		var boxPackingId = rec.data.boxPackingId;;
		if (boxPackingId == '') {
			return;
		}

		// 获得订单明细
		var detail = ds.getAt(row).data;
		// 获得材料类型,对应填充该类型的长宽高
		var boxTypeId = rec.data.boxTypeId;
		var sizeL = rec.data.sizeL;
		var sizeW = rec.data.sizeW;
		if (sizeL == '' || isNaN(sizeL)) {
			sizeL = 0;
		}
		if (sizeW == '' || isNaN(sizeW)) {
			sizeW = 0;
		}
		if (newVal == '' || isNaN(newVal)) {
			newVal = 0;
		}
		if (boxTypeId == 0) {
			detail.boxPbL = sizeL;
			detail.boxPbW = sizeW;
			detail.boxPbH = newVal;
		}
		if (boxTypeId == 1) {
			detail.boxIbL = sizeL;
			detail.boxIbW = sizeW;
			detail.boxIbH = newVal;
		}
		if (boxTypeId == 2) {
			detail.boxMbL = sizeL;
			detail.boxMbW = sizeW;
			detail.boxMbH = newVal;
		}
		if (boxTypeId == 3) {
			detail.boxObL = sizeL;
			detail.boxObW = sizeW;
			detail.boxObH = newVal;
		}
		if (boxTypeId == 4) {
			detail.putL = sizeL;
			detail.putW = sizeW;
			detail.putH = newVal;
		}

		// 返回单价和厂家
		cotPackOrderService.getPackPriceByType(detail, boxPackingId, function(
						res) {
					// 单价金额
					rec.set("packPrice", res);
					// 计算金额
					var temp = (rec.data.packCount * res).toFixed('2');
					rec.set("totalAmount", temp);
				});

	}

	// 左边订单明细
	var leftPanel = new Ext.Panel({
				layout : 'border',
				region : 'west',
				border : false,
				width : 200,
				items : [form, grid]
			});

	var con = {
		width : 800,
		height : 400,
		title : "新增包材",
		modal : true,
		layout : 'border',
		items : [leftPanel, gridPk],
		listeners : {
			'render' : function() {
				ds.on('beforeload', function() {
							ds.baseParams = DWRUtil.getValues("packFormFind");
						});
			}
		}
	}
	Ext.apply(con, cfg);
	OrderWinForPack.superclass.constructor.call(this, con);
};

Ext.extend(OrderWinForPack, Ext.Window, {});