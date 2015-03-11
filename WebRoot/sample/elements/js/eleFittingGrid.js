// 配件单价保留几位小数
var fitPriceNum = getDeNum("fitPrecision");
// 根据小数位生成"0.000"字符串
var fitNumTemp = getDeStr(fitPriceNum);
Ext.onReady(function() {
	var eId = $('eleid').value;
	var eleName = $('eleName').value;
	var record_start = 0;
	// 加载表格需要关联的外键名
	var facMap;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facMap = res;
			});

	// 厂家 1:产品，2：配件,3:包材
	var combox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2&validUrl=cotfactory.do",
		cmpId : "facId",
		sendMethod : 'POST',
		fieldLabel : "Supplier",
		editable : true,
		autoLoad : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		pageSize : 5,
		anchor : "100%",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		allowBlank : false,
		blankText : 'Supplier must not be null！',
		triggerAction : 'all'

	});

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "eleChild"
			}, {
				name : "facId",
				type : 'int'
			}, {
				name : "fitDesc"
			}, {
				name : "fitUsedCount",
				type : 'float'
			}, {
				name : "fitUseUnit"
			}, {
				name : "fitCount",
				type : 'float'
			}, {
				name : "fitPrice",
				type : 'float',
				convert : numFormat.createDelegate(this, [fitNumTemp], 3)
			}, {
				name : "fitTotalPrice",
				type : 'float',
				convert : numFormat.createDelegate(this, [fitNumTemp], 3)
			}, {
				name : "fitRemark"
			}, {
				name : "eleId",
				type : 'int'
			}, {
				name : "fittingId",
				type : 'int'
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var ds = new Ext.data.Store({
				autoSave:false,
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotelements.do?method=loadFittingInfo" +
									"&Flag="+$('Flag').value
									+"&childId="+$('childId').value
									+"&mainId="+$('eleid').value,
							create : "cotelements.do?method=addEleFitting",
							update : "cotelements.do?method=modifyEleFitting"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord),
				listeners : {
					exception : function(proxy, type, action, options,
							response, arg) {
						ds.reload();
						refresh();

					}
				},
				writer : writer
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [sm, new Ext.grid.RowNumberer({
							header : "Sort No",
							width : 35,
							renderer : function(value, metadata, record,
									rowIndex) {
								return record_start + 1 + rowIndex;
							}
						}), {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "Fitting No",
					dataIndex : "fitNo",
					width : 120,
					summaryRenderer : function(v, params, data) {
						params.css = 'fg';
						return "Total：";
					}
				}, {
					header : "<font color=blue>Fitting</font>",
					dataIndex : "fitName",
					width : 120,
					editor : new Ext.form.TextField({
								maxLength : 200,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							})
				}, {
					header : "Article No",
					dataIndex : "eleChild",
					width : 90
				}, {
					header : "<font color=blue>Supplier</font>",
					dataIndex : "facId",
					width : 90,
					editable : true,
					editor : combox,
					renderer : function(value) {
						return facMap[value];
					}
				}, {
					header : "<font color=blue>Fitting Size</font>",
					dataIndex : "fitDesc",
					width : 200,
					editor : new Ext.form.TextField({
								maxLength : 500,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							})
				}, {
					header : "<font color=blue>Count</font>",
					dataIndex : "fitUsedCount",
					width : 50,
					editor : new Ext.form.NumberField({
								maxValue : 9999999.999,
								decimalPrecision : 3,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									},
									"change" : function(txt, newVal, oldVal) {
										changeMoneyByUse(newVal);
									}
								}
							})
				}, {
					header : "<font color=blue>Unit</font>",
					dataIndex : "fitUseUnit",
					width : 60,
					editor : new Ext.form.TextField({
								maxLength : 10,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							})
				}, {
					header : "<font color=blue>Quantity</font>",
					dataIndex : "fitCount",
					width : 50,
					editor : new Ext.form.NumberField({
								maxValue : 9999999.999,
								decimalPrecision : 3,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									},
									"change" : function(txt, newVal, oldVal) {
										changeMoneyByNum(newVal);
									}
								}
							})
				}, {
					header : "<font color=blue>Fitting Price</font>",
					dataIndex : "fitPrice",
					width : 68,
					editor : new Ext.form.NumberField({
								maxValue : 999999.999999,
								decimalPrecision : fitPriceNum,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									},
									"change" : function(txt, newVal, oldVal) {
										fitPriceChange(newVal);
									}
								}
							})
				}, {
					header : "Amount",
					dataIndex : "fitTotalPrice",
					width : 80,
					// renderer : function(value, metaData, record, rowIndex,
					// colIndex, store) {
					// // 总金额=数量*单价
					// var price = record.get("fitPrice");
					// var count = record.get("fitUsedCount");
					// var fitCount = record.get("fitCount");
					// var res = (price * count * fitCount).toFixed("2");
					// record.data.fitTotalPrice = res;
					// return res;
					// },
					summaryType : 'sum'
				}, {
					header : "<font color=blue>备注</font>",
					dataIndex : "fitRemark",
					width : 300,
					editor : new Ext.form.TextArea({
								maxLength : 500,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							})
				}, {
					header : "Article id",
					dataIndex : "eleId",
					width : 80,
					hidden : true
				}, {
					header : "Fitting id",
					dataIndex : "fittingId",
					width : 80,
					hidden : true
				}, {
					header : "Opration",
					dataIndex : "id",
					width : 80,
					renderer : function(value) {
						// var mod = '<a
						// href="javascript:modTypeById('+value+')">修改</a>';

						var nbsp = "&nbsp &nbsp &nbsp"
						var del = '<a href="javascript:del(' + value
								+ ')">Delete</a>';
						return del + nbsp;
					}
				}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	// 厂家 1:产品，2：配件,3:包材
	var facbox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1",
		cmpId : "facIdFind",
		sendMethod : 'POST',
		emptyText : "Supplier",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		autoLoad : false,// 默认自动加载,会为get提交
		pageSize : 5,
		width : 120,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'facIdFind'
	});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', facbox, {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 100,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "The end of time",
							width : 100,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						},{
							xtype : 'searchcombo',
							width : 120,
							emptyText : "fitting no.",
							isSearchField : true,
							searchName : 'fnFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "import from the fitting's library",
							cls : "SYSOP_ADD",
							iconCls : "page_add",
							handler : Show
						}, '-', {
							text : "Delete",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : onDel
						}, '-', {
							text : "Save",
							cls : "SYSOP_ADD",
							iconCls : "page_table_save",
							handler : onSave
						}]
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.EditorGridPanel({
				border : false,
				id : "elefittingGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : [summary],
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				split : true,
				viewConfig : {
					forceFit : false
				}
			});

	var rowCurrent = -1;
	// 单元格点击后,记住当前行
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				rowCurrent = rowIndex;
				// 获得view
				var view = grid.getView();
				// 获得单元格
				var cell = view.getCell(rowIndex, columnIndex);
				// 获得该行高度
				var row = view.getRow(rowIndex);
				var editor = cm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(cell.offsetWidth, row.scrollHeight);
			});

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});
	
	function onDel() {
		var cord = grid.getSelectionModel().getSelections();
		var list = new Array();
		for (i = 0; i < cord.length; i++) {
			var id = cord[i].get('id');
			if (id != null && id != 'undefined') {
				list.push(id);
			} else {
				Ext.each(cord, function(item) {
							ds.remove(item);
						});
			}
		}
		if (list.length > 0) {
			Ext.MessageBox.confirm('Message', 'are you sure to delete the selected items?', function(btn) {
						if (btn == 'yes') {
							cotElementsService.deleteEleFittingByIds(list,
									function(res) {
										if (res) {
											reloadGrid("elefittingGrid");
											refresh();
											Ext.MessageBox
													.alert("Message", 'Deleted successfully!');
										} else {
											Ext.MessageBox
													.alert("Message", "Deleted failed!");
										}
									});
						}
					});
		}

	}

	// 保存
	function onSave() {
		ds.proxy.setApi({
			create : "cotelements.do?method=addEleFitting" + '&mainId='
					+ $('eleid').value + '&Flag=' + $('Flag').value + '&eleNo='
					+ parent.$('eleId').value + '&eId=' + parent.$('eId').value,
			update : "cotelements.do?method=modifyEleFitting"
		});
		ds.save();
		// Ext.MessageBox.alert("提示框", "保存成功！")
		// setTimeout("ds.reload()", 1000);
	}

	function refresh() {
		// 刷新主页面的生产价
		cotElementsService.modifyPriceFacByEleId($('eleid').value,
				function(res) {
					// 刷新父货号的生产价
					var flag = $('Flag').value;
					if (flag == 'parent') {
						parent.$('priceFac').value = res[2];
						parent.fitMoney = res[0];
					} else {
						parent.window.opener.parent.$('priceFac').value = res[2];
					}
				});

	}

	// 删除
	this.del = function(id) {
		if (id != null && id != 'undefined') {
			var config = {
				title : "Message",
				msg : "are you sure to delete it?",
				width : 220,
				buttons : Ext.MessageBox.YESNO,
				icon : Ext.MessageBox.QUESTION,
				fn : function(btn) {
					if (btn == 'yes') {
						var list = new Array();
						list.push(id);
						cotElementsService.deleteEleFittingByIds(list,
								function(delres) {
									if (delres) {
										reloadGrid("elefittingGrid");
										Ext.MessageBox.alert("Message", 'Deleted successfully!');
									} else {
										Ext.MessageBox.alert("Message", "Deleted failed!");
									}
								});
					}
				}
			}
			Ext.MessageBox.show(config);
		} else {
			var cord = grid.getSelectionModel().getSelections();
			Ext.each(cord, function(item) {
						ds.remove(item);
					});
		}
	}

	// 用量改变时,改变总金额
	function changeMoneyByUse(newVal) {
		var rec = ds.getAt(rowCurrent);
		// 数量
		var fitCount = rec.data.fitCount;
		if (fitCount == '' || isNaN(fitCount)) {
			fitCount = 0;
		}
		// 单价
		var fitPrice = rec.data.fitPrice;
		if (fitPrice == '' || isNaN(fitPrice)) {
			fitPrice = 0;
		}

		// 计算金额
		var temp = (fitCount * newVal * parseFloat(fitPrice))
				.toFixed(fitPriceNum);
		rec.set("fitTotalPrice", temp);
	}

	// 数量改变时,改变总金额
	function changeMoneyByNum(newVal) {
		var rec = ds.getAt(rowCurrent);
		// 用量
		var fitUsedCount = rec.data.fitUsedCount;
		if (fitUsedCount == '' || isNaN(fitUsedCount)) {
			fitUsedCount = 0;
		}
		// 单价
		var fitPrice = rec.data.fitPrice;
		if (fitPrice == '' || isNaN(fitPrice)) {
			fitPrice = 0;
		}

		// 计算金额
		var temp = (fitUsedCount * newVal * parseFloat(fitPrice))
				.toFixed(fitPriceNum);
		rec.set("fitTotalPrice", temp);
	}

	// 单价改变金额
	function fitPriceChange(newVal) {
		var rec = ds.getAt(rowCurrent);
		// 数量
		var fitCount = rec.data.fitCount;
		if (fitCount == '' || isNaN(fitCount)) {
			fitCount = 0;
		}
		// 用量
		var fitUsedCount = rec.data.fitUsedCount;
		if (fitUsedCount == '' || isNaN(fitUsedCount)) {
			fitUsedCount = 0;
		}
		// 计算金额
		var temp = (fitCount * fitUsedCount * parseFloat(newVal))
				.toFixed(fitPriceNum);
		rec.set("fitTotalPrice", temp);
	}

});
// 插入表格
function insertToGrid(res) {
	var price;
	var flag = false;
	if ((res.fitPrice != null && res.fitPrice != '')
			&& (res.fitTrans != null && res.fitTrans != '')) {
		if (res.fitTrans != 0) {
			price = (res.fitPrice / res.fitTrans).toFixed(fitPriceNum);
		} else {
			price = 0.0;
		}
	}
	var grid = Ext.getCmp('elefittingGrid');
	var ds = grid.getStore();
	ds.each(function(rec) {
				if (rec.data.fitNo == res.fitNo) {
					flag = true;
				}
			});
	if (flag) {
		return;
	}
	var u = new ds.recordType({
				fitNo : res.fitNo,
				fitName : res.fitName,
				eleChild : $('eleName').value,
				facId : res.facId,
				fitDesc : res.fitDesc,
				fitUsedCount : 1,
				fitUseUnit : res.useUnit,
				fitCount : 1,
				fitPrice : price,
				fitTotalPrice : price,
				fitRemark : res.fitRemark,
				eleId : $('eleid').value,
				fittingId : res.id
			})
	ds.add(u);
}

// 添加选择的配件到可编辑表格
function insertSelect(ids) {

	for (var i = 0; i < ids.length; i++) {
		cotElementsService.getFittingById(parseInt(ids[i].id), function(res) {
					insertToGrid(res);
				});
	}
}

function Show() {
	var fitInputGrid = new FitInputGrid();
	fitInputGrid.show();
}
function Hide() {
	var dialog = Ext.getCmp("fitting");
	dialog.hide();
}