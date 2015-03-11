window.onbeforeunload = function() {
	var ds = Ext.getCmp('signDetailGrid').getStore();
	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
		if (Ext.isIE) {
			if (document.body.clientWidth - event.clientX < 170
					&& event.clientY < 0 || event.altKey) {
				return "征样明细数据有更改,您确定不保存吗?";
			} else if (event.clientY > document.body.clientHeight
					|| event.altKey) { // 用户点击任务栏，右键关闭
				return "征样明细数据有更改,您确定不保存吗?";
			} else { // 其他情况为刷新
			}
		} else if (Ext.isChrome || Ext.isOpera) {
			return "征样明细数据有更改,您确定不保存吗?";
		} else if (Ext.isGecko) {
			window.open("http://www.g.cn")
			var o = window.open("index.do?method=logoutAction");
		}
	}
}

Ext.onReady(function() {
	// cbm保留几位小数
	var cbmNum = getDeNum("cbmPrecision");
	var cbmStr = getDeStr(cbmNum);
	var packData;
	var typelv1Data;
	var signMap;
	DWREngine.setAsync(false);

	DWREngine.setAsync(false);
	// 加载包装方案表缓存
	baseDataUtil.getBaseDicDataMap("CotBoxType", "id", "typeName",
			function(res) {
				packData = res;
			});
	DWREngine.setAsync(true);

	DWREngine.setAsync(false);
	// 加载材质表缓存
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeName",
			function(res) {
				typelv1Data = res;
			});
	DWREngine.setAsync(true);
	
	DWREngine.setAsync(false);
	//加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotSign',function(rs){
		signMap=rs;
	});
	DWREngine.setAsync(true);
	
	// -----------------本地下拉框-----------------------------------------

	// 锁定套数
	var lockUnitNum = function() {
		var fly = Ext.getDom('eleFlag').value;
		if (fly == 0) {
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
				id : 'eleFlag',
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
				emptyText : '请选择',
				hiddenName : 'eleFlag',
				selectOnFocus : true,
				listeners : {
					"select" : lockUnitNum
				}
			});

	// 是否到样
	var checkSignStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '否'], [1, '是']]
			});
	var checkSignBox = new Ext.form.ComboBox({
				name : 'checkSign',
				fieldLabel : '是否到样',
				editable : false,
				store : checkSignStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 9,
				emptyText : '请选择',
				hiddenName : 'checkSign',
				selectOnFocus : true
			});

	// -----------------远程下拉框-----------------------------------------
	// 材质
	var typeLv1Box = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "材质",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 34,
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 表格--材质
	var typeLv1GridBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				editable : true,
				hideLabel : true,
				labelSeparator : " ",
				valueField : "id",
				displayField : "typeName",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,
				sendMethod : "post",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 加载包装类型值
	var selectBind = function() {
		var id = $('boxTypeId').value;
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
				});
		calPrice();
		DWREngine.setAsync(true);
	};

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
		var eleId = elements.eleId;
		cotGivenService.updateMapValueByEleId(eleId, 'boxIbPrice',
				$('boxIbPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'boxMbPrice',
				$('boxMbPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'boxObPrice',
				$('boxObPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'boxPbPrice',
				$('boxPbPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'inputGridPrice',
				$('inputGridPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'packingPrice',
				$('packingPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'priceFac',
				$('priceFac').value, function(res) {
				});
		DWREngine.setAsync(true);
	}

	// 包装方案
	var boxPacking = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType",
				valueField : "id",
				fieldLabel : "包装方案",
				tabIndex : 51,
				displayField : "typeName",
				cmpId : "boxTypeId",
				emptyText : '请选择',
				anchor : "100%",
				listeners : {
					"select" : selectBind
				}
			});

	// 表格--包装方案
	var boxTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType",
				cmpId : 'boxTypeId',
				hideLabel : true,
				labelSeparator : " ",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 客户
	var customerBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
		name : 'custId',
		cmpId : 'custId',
		fieldLabel : "<font color=red>客户</font>",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		mode : 'remote',// 默认local
		autoLoad : false,// 默认自动加载
		pageSize : 10,
		anchor : "100%",
		selectOnFocus : true,
		allowBlank : false,
		sendMethod : "post",
		blankText : "客户不能为空！",
		emptyText : '请选择',
		tabIndex : 2,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 业务员
	var empsBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		name : 'bussinessPerson',
		cmpId : 'bussinessPerson',
		fieldLabel : "<font color=red>业务员</font>",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		mode : 'remote',// 默认local
		autoLoad : false,// 默认自动加载
		pageSize : 10,
		anchor : "100%",
		selectOnFocus : true,
		allowBlank : false,
		sendMethod : "post",
		blankText : "业务员不能为空！",
		emptyText : '请选择',
		tabIndex : 3,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 厂家
	var facBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
				cmpId : 'factoryId',
				fieldLabel : "<font color=red>厂家</font>",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : '请选择',
				allowBlank : false,
				blankText : "厂家不能为空！",
				pageSize : 10,
				anchor : "100%",
				tabIndex : 4,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				listeners : {
					"select" : function(com, rec, index) {
						facContactBox.reset();
						facContactBox.loadValueById('CotContact', 'factoryId',
								rec.data.id);
					}
				}
			});

	// 厂家联系人
	var facContactBox = new BindCombox({
				cmpId : "contactId",
				mode : 'local',
				sendMethod : "post",
				dataUrl : "./servlet/DataSelvert?tbname=CotContact",
				fieldLabel : "联系人",
				tabIndex : 5,
				displayField : "contactPerson",
				valueField : "id",
				emptyText : '请选择',
				triggerAction : "all",
				anchor : "100%"
			});

	// 海关编码
	var eleHsidBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "海关编码",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "cnName",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 50,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
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
				emptyText : '请选择',
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
				emptyText : '请选择',
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
				emptyText : '请选择',
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
				emptyText : '请选择',
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
				fieldLabel : "产品分类",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 38,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

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
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "eleName"
			}, {
				name : "givenCount",
				type : "int"
			}, {
				name : "signCount",
				type : "int"
			}, {
				name : "priceFac",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "signRequire"
			}, {
				name : "boxTypeId"
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
				name : "eleUnit"
			}, {
				name : "eleTypeidLv1"
			}, {
				name : "boxGrossWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "boxNetWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "boxCbm",
				type : "float",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "type"
			}, {
				name : "srcId"
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
						read : "cotsign.do?method=querySignDetail",
						create : "cotsign.do?method=addSignDetail",
						update : "cotsign.do?method=modifySignDetail",
						destroy : "cotsign.do?method=removeSignDetail"
					},
					listeners : {
						beforeload : function(store, options) {
							ds.removed = [];
							cotSignService.clearSignMap(function(res) {
									});
						},
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								ds.load({
											params : {
												start : 0,
												limit : 15,
												pId : $('sId').value,
												factoryId : $('factoryId').value,
												givenId : $('givenId').value,
												flag : 'signDetail'
											}
										});
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
				hiddenCols:signMap,
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
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							dataIndex : "eleId",
							width : 80
						}, {
							header : "客号",
							dataIndex : "custNo",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 80
						}, {
							header : "中文名",
							dataIndex : "eleName",
							editor : new Ext.form.TextArea({
										width : 400
									}),
							width : 100
						}, {
							header : "送样数量",
							dataIndex : "givenCount",
							width : 60
						}, {
							header : "征样数量",
							dataIndex : "signCount",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 60
						}, {
							header : "单价",
							dataIndex : "priceFac",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3
									})
						}, {
							header : "改样要求",
							dataIndex : "signRequire",
							editor : new Ext.form.TextArea({
										width : 400,
										maxLength : 200
									}),
							width : 200
						}, {
							header : "包装方式",
							dataIndex : "boxTypeId",
							renderer : function(value) {
								return packData[value];
							},
							editor : boxTypeBox,
							width : 80
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
							header : "单位",
							dataIndex : "eleUnit",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							width : 60
						}, {
							header : "材质",
							dataIndex : "eleTypeidLv1",
							width : 100,
							renderer : function(value) {
								return typelv1Data[value];
							},
							editor : typeLv1GridBox
						}, {
							header : "毛重",
							dataIndex : "boxGrossWeigth",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 9999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "净重",
							dataIndex : "boxNetWeigth",
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
										.toFixed(cbmNum);
								record.data.boxCbm = res;
								return res;
							},
							editor : new Ext.form.NumberField({
										maxValue : 999999.9999,
										decimalPrecision : 4
									})
						}, {
							header : "产品规格(CM)",
							dataIndex : "eleSizeDesc",
							editor : new Ext.form.TextArea({
										width : 400,
										maxLength : 500
									}),
							width : 120
						}, {
							header : "产品规格(INCH)",
							dataIndex : "eleInchDesc",
							editor : new Ext.form.TextArea({
										width : 400,
										maxLength : 500
									}),
							width : 120
						}, {
							header : "type",
							dataIndex : "type",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							hidden : true
						}, {
							header : "srcId",
							dataIndex : "srcId",
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
							handler : onDel,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}, '-', {
							text : "导入货号",
							iconCls : "gird_exp",
							handler : showImportPanel,
							cls : "SYSOP_EXP"
						}]
			});

	// 征样明细表格
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "signDetailGrid",
				stripeRows : true,
				// clicksToEdit : 1,
				margins : "0 5 0 0",
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

	// 右边折叠面板
	var rightForm = new Ext.form.FormPanel({
		autoScroll : true,
		formId : "rightForm",
		padding : "5px",
		height : 500,
		labelWidth : 60,
		labelAlign : "right",
		items : [{
			title : '基本信息',
			xtype : "fieldset",
			name : "mainPanel",
			anchor : '97%',
			layout : "column",
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
							text : "更改",
							handler : showUploadPanel,
							id : "upmod"
						}, {
							width : 40,
							text : "删除",
							handler : delPic,
							id : "updel"
						}]
			}, {
				xtype : "panel",
				columnWidth : 0.55,
				layout : "form",
				labelWidth : 40,
				items : [{
							xtype : "textfield",
							fieldLabel : "货号",
							anchor : "100%",
							tabIndex : 31,
							id : "eleId",
							name : "eleId",
							disabled : true,
							disabledClass : 'combo-disabled',
							maxLength : 100
						}, {
							xtype : "textfield",
							fieldLabel : "厂号",
							anchor : "100%",
							tabIndex : 32,
							id : "factoryNo",
							name : "factoryNo",
							maxLength : 200
						}, {
							xtype : "textfield",
							fieldLabel : "客号",
							anchor : "100%",
							tabIndex : 33,
							id : "custNo",
							name : "custNo",
							maxLength : 200
						}, typeLv1Box, {
							xtype : "hidden",
							id : "id",
							name : "id"
						}]
			}, {
				xtype : "panel",
				columnWidth : 0.6,
				layout : "form",
				labelWidth : 60,
				items : [eleFlagBox, typeLv2Box, {
							xtype : "textfield",
							fieldLabel : "产品颜色",
							anchor : "100%",
							tabIndex : 40,
							id : "eleCol",
							name : "eleCol",
							maxLength : 50
						}, {
							xtype : "textfield",
							fieldLabel : "产品来源",
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
							fieldLabel : "最小订量",
							anchor : "100%",
							decimalPrecision : 0,
							tabIndex : 39,
							id : "eleMod",
							name : "eleMod",
							maxLength : 99999999999
						}, {
							xtype : "textfield",
							fieldLabel : "产品等级",
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
							decimalPrecision : 3,
							maxValue : 99999.999
						}, {
							xtype : "numberfield",
							fieldLabel : "容积",
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
							fieldLabel : "中文品名",
							anchor : "100%",
							height : 40,
							tabIndex : 46,
							id : "eleName",
							name : "eleName",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "英文品名",
							anchor : "100%",
							height : 40,
							tabIndex : 47,
							id : "eleNameEn",
							name : "eleNameEn",
							maxLength : 200
						}, {
							xtype : "textarea",
							fieldLabel : "产品描述",
							anchor : "100%",
							tabIndex : 48,
							height : 40,
							id : "eleDesc",
							name : "eleDesc",
							maxLength : 500
						}, {
							xtype : "textfield",
							fieldLabel : "电脑标",
							anchor : "100%",
							tabIndex : 49,
							id : "barcode",
							name : "barcode",
							maxLength : 30
						}, eleHsidBox, {
							xtype : "textarea",
							fieldLabel : "备注",
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
			title : "历史报价情况",
			layout : "form",
			anchor : '97%',
			items : [{
						layout : "hbox",
						layoutConfig : {
							align : 'middle'
						},
						items : [{
									layout : 'form',
									flex : 1,
									labelWidth : 50,
									items : [{
												xtype : 'textfield',
												fieldLabel : '平均价',
												readOnly : true,
												id : "avgPrice",
												anchor : "98%",
												name : "avgPrice"
											}]
								}, {
									layout : 'form',
									flex : 1,
									labelWidth : 50,
									items : [{
												xtype : 'textfield',
												fieldLabel : '最低价',
												readOnly : true,
												id : "minPrice",
												anchor : "98%",
												name : "minPrice"
											}]
								}, {
									layout : 'form',
									flex : 1,
									labelWidth : 50,
									items : [{
												xtype : 'textfield',
												fieldLabel : '最高价',
												readOnly : true,
												id : "maxPrice",
												anchor : "98%",
												name : "maxPrice"
											}]
								}]
					}, {
						layout : "hbox",
						layoutConfig : {
							align : 'middle'
						},
						items : [{
									layout : 'form',
									flex : 1,
									labelWidth : 50,
									items : [{
												xtype : 'textfield',
												fieldLabel : '最新价',
												readOnly : true,
												id : "newPrice",
												anchor : "95%",
												name : "newPrice"
											}]
								}, {
									layout : 'form',
									flex : 1,
									labelWidth : 70,
									items : [{
												xtype : 'textfield',
												fieldLabel : '原始外销价',
												readOnly : true,
												id : "oldPrice",
												anchor : "98%",
												name : "oldPrice"
											}]
								}]
					}

			]
		}, {
			xtype : "fieldset",
			title : "包装信息",
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
										fieldLabel : "产品装数",
										anchor : "100%",
										tabIndex : 55,
										id : "boxPbCount",
										name : "boxPbCount",
										maxValue : 99999999,
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
										fieldLabel : "内盒装数",
										anchor : "100%",
										tabIndex : 58,
										id : "boxIbCount",
										name : "boxIbCount",
										maxValue : 99999999,
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
										fieldLabel : "中盒装数",
										anchor : "100%",
										tabIndex : 61,
										id : "boxMbCount",
										name : "boxMbCount",
										maxLength : 99999999,
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
										fieldLabel : "外箱装数",
										anchor : "100%",
										tabIndex : 64,
										id : "boxObCount",
										name : "boxObCount",
										maxLength : 99999999,
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
				labelWidth : 20,
				items : [{
							xtype : "numberfield",
							fieldLabel : "价",
							anchor : "100%",
							tabIndex : 52,
							id : "packingPrice",
							name : "packingPrice",
							listeners : {
								'change' : function() {
									setMapValue(this);
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "价",
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
							fieldLabel : "价",
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
							fieldLabel : "价",
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
							fieldLabel : "价",
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
							fieldLabel : "价",
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
							name : "boxCbm",
							decimalPrecision : cbmNum,
							maxLength : 99999999.999999
						}, {
							xtype : "numberfield",
							fieldLabel : "净重/KG",
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
							fieldLabel : "单重/G",
							anchor : "100%",
							tabIndex : 68,
							id : "boxWeigth",
							name : "boxWeigth",
							listeners : {
								'change' : function() {
									calWeighByEleFrame(this.value)
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "毛重/KG",
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
							fieldLabel : "产品规格CM",
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
							fieldLabel : "产品包装CM",
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
							fieldLabel : "内盒包装CM",
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
							fieldLabel : "外箱包装CM",
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
							fieldLabel : "中文规格",
							anchor : "100%",
							height : 40,
							tabIndex : 104,
							id : "eleSizeDesc",
							name : "eleSizeDesc",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "英文规格",
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
							fieldLabel : "中文包装",
							anchor : "100%",
							height : 40,
							tabIndex : 109,
							id : "boxRemarkCn",
							name : "boxRemarkCn",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "英文包装",
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
			anchor : '97%',
			buttonAlign : 'center',
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
						title : "",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 35,
						items : [{
									xtype : "numberfield",
									fieldLabel : "20\"",
									disabled : true,
									disabledClass : 'combo-disabled',
									anchor : "100%",
									tabIndex : 109,
									id : "box20Count",
									name : "box20Count",
									readOnly : true,
									maxLength : 1000000000000000000
								}, {
									xtype : "numberfield",
									fieldLabel : "40HQ\"",
									disabled : true,
									disabledClass : 'combo-disabled',
									anchor : "100%",
									tabIndex : 111,
									id : "box40hqCount",
									name : "box40hqCount",
									readOnly : true,
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
									disabled : true,
									disabledClass : 'combo-disabled',
									anchor : "100%",
									tabIndex : 110,
									id : "box40Count",
									name : "box40Count",
									readOnly : true,
									maxLength : 1000000000000000000
								}, {
									xtype : "numberfield",
									fieldLabel : "45\"",
									disabled : true,
									disabledClass : 'combo-disabled',
									anchor : "100%",
									tabIndex : 112,
									id : "box45Count",
									name : "box45Count",
									readOnly : true,
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
		width : "30%",
		collapsible : true,
		collapsed : true,
		listeners : {
			'beforeadd' : function(pnl, component, index) {
				pnl.getEl().mask("正在加载中,请稍候...", 'x-mask-loading');
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
						if ($('eleId').value != "") {
							updateMapValue($('eleId').value, txt.getName(),
									newVal);
							// 修改表格数据,如果表格没有这一列,则修改这一行的状态
							ds.each(function(rec) {
										if (rec.data.eleId == $('eleId').value) {
											var cell = rec.data[txt.getName()];
											if (typeof(cell) == 'undefined') {
												if (!isNaN(rec.data.id)) {
													var temp = rec.data.eleId;
													rec.set("eleId", temp
																	+ "aaa");
													rec.set("eleId", temp);
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
	var rx = -1;
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
		var srcId = record.data.srcId;
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
		var isMod = getPopedomByOpType("cotsign.do", "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
		}
		if ((eleId == null || eleId == "") && $('picPath')!=null) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			rightForm.getForm().reset();
			return;
		}
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(eleId, function(res) {
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
				if (res.eleFlag == 0) {
					Ext.getCmp("eleUnitNum").setDisabled(true);
				}

				if (popdom == true) {
					res.picPath = "common/images/noElePicSel.png";
				} else {
					if (!flag) {
						if (res.type == 'price') {
							res.picPath = "./showPicture.action?flag=price&detailId="
									+ res.srcId;
						}
						if (res.type == 'order') {
							res.picPath = "./showPicture.action?flag=order&detailId="
									+ res.srcId;
						}
						if (res.type == 'given') {
							res.picPath = "./showPicture.action?flag=given&detailId="
									+ res.srcId;
						}
					} else {
						res.picPath = "./showPicture.action?detailId=" + res.id
								+ "&flag=given&temp=" + Math.random();
					}
				}
				DWRUtil.setValue("picPath", res.picPath);
			}
		});
		if ($('custId').value != '') {
			cotOrderService.findNewPriceVO(eleId, $('custId').value, function(
							ap) {
						Ext.getCmp("newPrice").setValue(ap.newPrice);
						Ext.getCmp("avgPrice").setValue(ap.avgPrice);
						Ext.getCmp("maxPrice").setValue(ap.maxPrice);
						Ext.getCmp("minPrice").setValue(ap.minPrice);
					});
		}
		DWREngine.setAsync(true);
	}

	// 表格编辑前储存该行,用于editor中的一些事件处理
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
				DWREngine.setAsync(false);

				// setEleFrameValue(e.field, e.value);

				if (e.field == 'boxCbm') {
					changeCbm(e.record.data.eleId, e.value)
				}

				if (e.field == 'boxTypeId') {
					showType(e.value, e.record.data.eleId);
				}

				if (e.field == 'eleTypeidLv1') {
					typeLv1Box.bindPageValue("CotTypeLv1", "id", e.value);
				}

				// 如果是产品尺寸CM
				if (e.field == 'boxObL' || e.field == 'boxObW'
						|| e.field == 'boxObH') {
					changeInch(e.field, e.field + 'Inch', 0,
							e.record.data.eleId, e.value);
				}

				cotGivenService.updateMapValueByEleId(e.record.data.eleId,
						e.field, e.value, function(res) {
							if (rightForm.isVisible()) {
								var field = rightForm.getForm()
										.findField(e.field);
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

	// 尺寸和英寸转换
	function changeInch(fromObject, toObject, flag, eleId, value) {
		DWREngine.setAsync(false);
		if (value == '') {
			$(toObject).value = '';
			cotGivenService.updateMapValueByEleId(eleId, toObject, '',
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
				$('boxCbm').value = (L * W * H * 0.000001).toFixed(cbmNum);
				changeCbm(eleId, $('boxCbm').value);
			}
		}
		if (flag == 0) {
			$(toObject).value = (value / 2.54).toFixed("2");
		} else {
			$(toObject).value = round((value * 2.54), 2);
		}
		// cotGivenService.updateMapValueByEleId(eleId,fromObject,value,function(res){});
		cotGivenService.updateMapValueByEleId(eleId, toObject,
				$(toObject).value, function(res) {
				});
		DWREngine.setAsync(true);
	}

	// 更新内存数据
	function updateMapValue(eleId, property, value) {
		DWREngine.setAsync(false);
		cotGivenService.updateMapValueByEleId(eleId, property, value, function(
						res) {
				});
		DWREngine.setAsync(true);
	}

	// 比对柜最大装箱重量
	function sumContainCount() {
		var rdm = $('eleId').value;
		if (rdm == '') {
			return;
		}
		var gross = $('boxGrossWeigth').value;
		if (gross == '' || isNaN(gross)) {
			Ext.MessageBox.show({
						title : '提示信息',
						msg : '请输入毛重(只能是数字)！',
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
		var rdm = $('eleId').value;
		if (rdm == '') {
			return;
		}
		var cbm = $('boxCbm').value;
		if (cbm == '' || isNaN(cbm)) {
			Ext.MessageBox.show({
						title : '提示信息',
						msg : '请输入CBM(只能是数字)！',
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

	// cbm的更改事件,计算出装箱数
	function changeCbm(eleId, cbm) {

		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(eleId, function(res) {
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
						cotGivenService.setGivenMap(eleId.toLowerCase(), res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 包装类型的值改变事件
	function showType(boxTypeId, eleId) {
		if (boxTypeId == '') {
			return;
		}
		DWREngine.setAsync(false);

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
						cotGivenService.updateMapValueByEleId(eleId,
								'boxPbTypeId', res.boxPName, function(res) {
								});
						cotGivenService.updateMapValueByEleId(eleId,
								'boxIbTypeId', res.boxIName, function(res) {
								});
						cotGivenService.updateMapValueByEleId(eleId,
								'boxMbTypeId', res.boxMName, function(res) {
								});
						cotGivenService.updateMapValueByEleId(eleId,
								'boxObTypeId', res.boxOName, function(res) {
								});
						cotGivenService.updateMapValueByEleId(eleId,
								'inputGridTypeId', res.inputGridType, function(
										res) {
								});
					}
				});
		modifyPrice(eleId);
		DWREngine.setAsync(true);
	};

	// 改变包装方案修改价格
	function modifyPrice(eleId) {
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

		cotGivenService.updateMapValueByEleId(eleId, 'boxIbPrice',
				$('boxIbPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'boxMbPrice',
				$('boxMbPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'boxObPrice',
				$('boxObPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'boxPbPrice',
				$('boxPbPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'inputGridPrice',
				$('inputGridPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'packingPrice',
				$('packingPrice').value, function(res) {
				});
		cotGivenService.updateMapValueByEleId(eleId, 'priceFac',
				$('priceFac').value, function(res) {
				});

		cotGivenService.calPriceFacByPackPrice(eleId, packingPrice, function(
						facprice) {

					$('priceFac').value = facprice;

					var rec = editRec;
					rec.set("priceFac", facprice);

				});

		DWREngine.setAsync(true);
	}

	// 判断单号是否重复参数
	var isExist = true;
	var signForm = new Ext.form.FormPanel({
				title : "征样单基本信息-(红色为必填项)",
				labelWidth : 60,
				formId : "signForm",
				labelAlign : "right",
				region : 'north',
				layout : "form",
				width : "100%",
				height : 110,
				frame : true,
				items : [{
					xtype : "panel",
					title : "",
					layout : "column",
					items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 60,
						items : [{
									xtype : "datefield",
									fieldLabel : "<font color=red>征样日期</font>",
									anchor : "100%",
									allowBlank : false,
									blankText : "请选择征样日期！",
									id : "signTime",
									name : "signTime",
									format : "Y-m-d",
									tabIndex : 1
								}, {
									xtype : "textfield",
									fieldLabel : "<font color=red>征样单号</font>",
									anchor : "100%",
									allowBlank : false,
									blankText : "请输入征样单号！",
									id : "signNo",
									name : "signNo",
									maxLength : 50,
									maxLengthText : "50",
									tabIndex : 6,
									invalidText : "征样单号已存在,请重新输入！",
									validationEvent : 'change',
									validator : function(thisText) {
										if (thisText != '') {
											cotSignService.findIsExistSignNo(
													thisText, $('sId').value,
													function(res) {
														if (res) {
															isExist = false;
														} else {
															isExist = true;
														}
													});
										}
										return true;
									},
									listeners : {
										'focus' : function() {
											getSignNo();
										}
									}
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.22,
						labelWidth : 80,
						items : [customerBox, {
									xtype : "datefield",
									fieldLabel : "要样日期",
									format : "Y-m-d",
									anchor : "100%",
									clearCls : "",
									allowBlank : true,
									id : "custTime",
									name : "custTime",
									tabIndex : 7
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						labelWidth : 60,
						columnWidth : 0.2,
						items : [empsBox, {
									xtype : "datefield",
									fieldLabel : "送样日期",
									format : "Y-m-d",
									anchor : "100%",
									allowBlank : true,
									id : "requireTime",
									name : "requireTime",
									tabIndex : 8
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.18,
						labelWidth : 60,
						items : [facBox, checkSignBox]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						columnWidth : 0.2,
						labelWidth : 60,
						items : [facContactBox, {
									xtype : "datefield",
									fieldLabel : "到样日期",
									format : "Y-m-d",
									anchor : "100%",
									allowBlank : true,
									id : "arriveTime",
									name : "arriveTime",
									tabIndex : 10
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						labelWidth : 60,
						columnWidth : 0.42,
						items : [{
									xtype : "textfield",
									fieldLabel : "送样地点",
									anchor : "100%",
									allowBlank : true,
									id : "givenAddr",
									name : "givenAddr",
									maxLength : 100,
									maxLengthText : "100",
									tabIndex : 11
								}]
					}, {
						xtype : "panel",
						title : "",
						layout : "form",
						labelWidth : 60,
						columnWidth : 0.58,
						id : "MyPanel7",
						name : "",
						height : 22,
						items : [{
									xtype : "textarea",
									fieldLabel : "备注",
									anchor : "100%",
									height : 20,
									tabIndex : 12,
									id : "signRemark",
									name : "signRemark",
									maxLength : 300,
									maxLengthText : "300"
								}]
					}]
				}]
			})

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "征样明细",
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
				items : [centerPanel],
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
							handler : function() {
								closeandreflashEC(true, 'signGrid', false);
							}
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [tbl, signForm]
			});
	viewport.doLayout();

	// 打开上传面板
	function showUploadPanel() {
		var eleId = $('eleId').value;
		if (eleId == '') {
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
							+ "&flag=given&" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadGivenDetailPic.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}

	// 删除图片
	function delPic() {

		DWREngine.setAsync(false);

		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.Msg.alert('提示框', '请选择产品！');
			return;
		}
		var detailId = $('id').value;
		Ext.MessageBox.confirm('提示信息', '是否确定删除图片?', function(btn) {
					if (btn == 'yes') {
						cotGivenService.deletePicImg(detailId, function(res) {
									if (res) {
										$('picPath').src = "common/images/zwtp.png";
									} else {
										Ext.Msg.alert('提示框', "对不起，删除图片失败!");
									}
								})
					}
				});
		DWREngine.setAsync(true);
	}

	// 初始化
	function initSignForm() {
		DWREngine.setAsync(false);
		// 清空GivenMap
		cotGivenService.clearGivenMap(function(res) {
				});
		// 加载集装箱的柜体积
		queryService.getContainerCube(function(res) {
					$('con20').value = res[0];
					$('con40').value = res[1];
					$('con40H').value = res[2];
					$('con45').value = res[3];
				});

		// 加载样品默认配置
		cotElementsService.getDefaultList(function(res) {
					if (res.length != 0) {
						// 设置是否自动生成包装尺寸标识
						if (res[0].sizeFollow != null) {
							$('sizeFollow').value = res[0].sizeFollow;
						}
					}
				});

		// 送样单编号
		var id = $('sId').value;
		if (id == 'null' || id == '') {
			// 初始化下单时间为当前时间
			var signTime = Ext.getCmp("signTime");
			var date = new Date();
			signTime.setValue(date);

			var givenId = $('givenId').value;
			if (givenId != 'null' || givenId != '') {
				
				cotGivenService.getGivenById(parseInt(givenId),
						function(given) {
							if (given.custId != null && given.custId != '') {
								customerBox.bindPageValue("CotCustomer", "id",
										given.custId);
							}
							if (given.bussinessPerson != null
									&& given.bussinessPerson != '') {
								empsBox.bindPageValue("CotEmps", "id",
										given.bussinessPerson);
							}

							if (given.custRequiretime != null) {
								var custTime = Ext.getCmp("custTime");
								var date = new Date(given.custRequiretime);
								custTime.setValue(date);
							}
							if (given.givenAddr != null) {
								$('givenAddr').value = given.givenAddr;
							}
						});
			}
			// 隐藏按钮
			Ext.getCmp('delBtn').hide();
			Ext.getCmp('printBtn').hide();

		} else {
			var obj = new Object();
			// 加载征样单信息
			cotSignService.getSignById(parseInt(id), function(res) {
						DWRUtil.setValues(res);
						obj = res;
						// 加载时间
						if (res.signTime != null && res.signTime != '') {
							var date = new Date(res.signTime);
							var signTime = Ext.getCmp("signTime");
							signTime.setValue(date);
						}

						if (res.custTime != null && res.custTime != '') {
							var date = new Date(res.custTime);
							var custTime = Ext.getCmp("custTime");
							custTime.setValue(date);
						}

						if (res.requireTime != null && res.requireTime != '') {
							var date = new Date(res.requireTime);
							var requireTime = Ext.getCmp("requireTime");
							requireTime.setValue(date);
						}

						if (res.arriveTime != null && res.arriveTime != '') {
							var date = new Date(res.arriveTime);
							var arriveTime = Ext.getCmp("arriveTime");
							arriveTime.setValue(date);
						}

						// 加载下拉框
						if (res.checkSign != null) {
							checkSignBox.setValue(res.checkSign);
						}

						facBox.bindPageValue("CotFactory", "id", res.factoryId);
						facContactBox.loadValueById('CotContact', 'factoryId',
								res.factoryId, res.contactId);
						customerBox.bindPageValue("CotCustomer", "id",
								res.custId);
						empsBox.bindPageValue("CotEmps", "id",
								res.bussinessPerson);
					});
			// 分页基本参数
			ds.load({
						params : {
							start : 0,
							limit : 15,
							pId : id,
							factoryId : obj.factoryId,
							givenId : obj.givenId,
							flag : 'signDetail'
						}
					});
		}
		DWREngine.setAsync(true);
	}
	unmask();
	initSignForm();

	// 显示打印面板
	var printWinSign;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("提示信息", '您没有打印权限！');
			return;
		}
		// 如果该单没保存不能打印
		if ($('sId').value == 'null' || $('sId').value == '') {
			Ext.MessageBox.alert("提示信息", '该单还没有保存,不能打印！');
			return;
		}

		if (printWinSign == null) {
			printWinSign = new PrintWinSign({
						type : 'sign',
						pId : 'givenId',
						pNo : 'signNo',
						mailSendId : 'factoryId'
					});
		}
		if (!printWinSign.isVisible()) {
			var po = item.getPosition();
			printWinSign.setPosition(po[0], po[1] - 185);
			printWinSign.show();
		} else {
			printWinSign.hide();
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

		var id = $('sId').value;

		DWREngine.setAsync(false);
		var cotSign = new CotSign();
		var list = new Array();
		cotSign.id = id;
		list.push(cotSign);
		Ext.MessageBox.confirm('提示信息', '是否确定删除?', function(btn) {
			if (btn == 'yes') {
				// 查询该主送样单是否被删除
				cotSignService.getSignById(id, function(res) {
					if (res != null) {
						cotGivenService.deleteSignList(list, function(res) {
									if (res) {
										cotGivenService.modifyGivenStatus(
												parseInt($('givenId').value),
												'new', function(res) {
												});
										Ext.Msg.alert("提示框", "删除成功");
										closeandreflashEC('true', 'signGrid',
												false);
									} else {
										Ext.Msg.alert("提示框", "删除失败，该征样单已经被使用中");
									}
								})
					} else {
						closeandreflashEC('true', 'signGrid', false);
					}
				});
			}
		});
		DWREngine.setAsync(true);
	}

	// 保存
	function save() {
		var popedom = checkAddMod($('sId').value);
		if (popedom == 1) {
			Ext.Msg.alert("提示信息", '对不起,您没有添加权限!请联系管理员!');
			return;
		} else if (popedom == 2) {
			Ext.Msg.alert("提示信息", '对不起,您没有修改权限!请联系管理员!');
			return;
		}

		// 验证表单
		var formData = getFormValues(signForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		
		//验证单号是否存在
		var shortflag = false;
		var signNo = $('signNo').value;
		DWREngine.setAsync(false);
		cotSignService.findIsExistSignNo(signNo, $('sId').value, function(
				res) {
			if (res != null) {
				shortflag = true;
			}
		});
		if (shortflag) {
			Ext.MessageBox.alert("提示框", "该征样单号已存在，请重新输入！");
			return;
		}
		DWREngine.setAsync(true);
		
		var givenId;
		var givenNo;
		Ext.MessageBox.confirm('提示信息', '您是否要保存该征样单？', function(btn) {
			if (btn == 'yes') {
				var sign = DWRUtil.getValues('signForm');
				var cotSign = {};
				// 如果编号存在时先查询出对象,再填充表单
				if ($('sId').value != 'null' && $('sId').value != '') {
					DWREngine.setAsync(false);
					cotSignService.getSignById($('sId').value, function(res) {
								for (var p in res) {
									if (p != 'signTime' && p != 'arriveTime'
											&& p != 'custTime'
											&& p != 'requireTime'
											&& p != 'givenId' && p != 'givenNo') {
										res[p] = sign[p];
									}
								}
								cotSign = res;
								givenId = res.givenId;
								givenNo = res.givenNo;
								cotSign.id = $('sId').value;
							});
					DWREngine.setAsync(true);
				} else {
					cotSign = new CotSign();
					for (var p in cotSign) {
						if (p != 'signTime' && p != 'arriveTime'
								&& p != 'custTime' && p != 'requireTime') {
							cotSign[p] = sign[p];
						}
					}
					givenId = $('givenId').value;
					givenNo = $('givenNo').value;
				}
				DWREngine.setAsync(false);

				if ($('signTime').value != '') {
					cotSign.signTime = getDateType($('signTime').value);
				} else {
					cotSign.signTime = null;
				}
				if ($('custTime').value != '') {
					cotSign.custTime = getDateType($('custTime').value);
				} else {
					cotSign.custTime = null;
				}
				if ($('requireTime').value != '') {
					cotSign.requireTime = getDateType($('requireTime').value);
				} else {
					cotSign.requireTime = null;
				}
				if ($('arriveTime').value != '') {
					cotSign.arriveTime = getDateType($('arriveTime').value);
				} else {
					cotSign.arriveTime = null;
				}

				cotSignService.saveOrUpdateSign(cotSign, givenId, givenNo,
						function(res) {
							if (res != null) {
								$('sId').value = res;

								// 更改添加action参数
								var urlSel = '&givenId=' + givenId
										+ '&factoryId=' + $('factoryId').value;

								// 更改添加action参数
								var urlAdd = '&givenPrimId=' + givenId
										+ '&custId=' + $('custId').value
										+ '&factoryId=' + $('factoryId').value;

								// 更改修改action参数
								var urlMod = '&custId=' + $('custId').value;
								ds.proxy.setApi({
									read : "cotsign.do?method=querySignDetail"
											+ urlSel,
									create : "cotsign.do?method=addSignDetail"
											+ urlAdd,
									update : "cotsign.do?method=modifySignDetail"
											+ urlMod,
									destroy : "cotsign.do?method=removeSignDetail"
								});
								ds.save();
								Ext.Msg.alert("提示消息", "保存成功！");
								reflashParent('signGrid');	
								
							} else {
								Ext.MessageBox.alert('提示消息', '保存失败');
								// unmask();
							}
						});
				DWREngine.setAsync(true);
			}
		});
	}

	// 获得表格选择的记录
	function getSignDetailIds() {
		var list = Ext.getCmp("signDetailGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var cotGivenDetail = new CotGivenDetail();
					cotGivenDetail.id = item.id;
					res.push(cotGivenDetail);
				});
		return res;
	}

	// 删除
	function onDel() {
		var ids = getSignDetailIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
			return;
		}

		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					ds.remove(item);
				});
		rightForm.getForm().reset();
		$('picPath').src = "./showPicture.action?flag=noPic";
	}

	// 显示导入界面
	var _self = this;
	function showImportPanel() {
		// 验证表单
		var formData = getFormValues(signForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var cfg = {};
		cfg.givenId = $('givenId').value;
		cfg.bar = _self;

		var givenWin = new GivenWin(cfg);
		givenWin.show();
	}

	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list, type) {
		insertByBatch(list, type);
	}

	// 添加选择的样品到可编辑表格
	function insertByBatch(list, type) {

		var eleAry = new Array();
		var idsAry = new Array();
		Ext.each(list, function(item) {
					eleAry.push(item.data.eleId);
					idsAry.push(item.data.id);
				});
		for (var i = 0; i < eleAry.length; i++) {
			DWREngine.setAsync(false);
			cotGivenService.findIsExistDetail(eleAry[i], idsAry[i], type,
					function(detail) {
						if (detail != null) {
							insertToGrid(detail, type);
						}
					});
			DWREngine.setAsync(true);
		}
		unmask();
	}

	// 根据产品货号查找样品数据
	function insertToGrid(detail, type) {

		var eleId = detail.eleId;
		DWREngine.setAsync(false);
		var cotGivenDetail = new CotGivenDetail();

		cotGivenDetail = detail;
		cotGivenDetail.type = type;
		cotGivenDetail.srcId = detail.id;

		// cotGivenDetail.id = null;

		// 通过货号获取客号
		var custId = $('custId').value;
		cotElementsService.getCustNoByEleId(eleId, parseInt(custId), function(
						custNo) {
					cotGivenDetail.custNo = custNo;
					// 将送样明细对象储存到后台GivenMap中
					cotGivenService.setGivenMap(eleId.toLowerCase(),
							cotGivenDetail, function(res) {
							});
				});
		setObjToGrid(cotGivenDetail);
		DWREngine.setAsync(true);
	}

	// 添加带有数据的record到表格中
	function setObjToGrid(obj) {
		var u = new ds.recordType(obj);
		ds.add(u);
	}

	// 获取单号
	function getSignNo() {
		var currDate = $('signTime').value;
		var custId = $('custId').value;
		var givenNo = $('givenNo').value;
		var factoryId =facBox.getValue();
		var empId =empsBox.getValue();
		if (currDate == "" || custId == null) {
			Ext.MessageBox.alert("提示消息", "请先选择下单日期");
			return;
		}
		if ($('custId').value == null || $('custId').value == "") {
			Ext.MessageBox.alert("提示消息", "请先选择客户");
			return;
		}
		if (factoryId == null || factoryId == "") {
			Ext.MessageBox.alert("提示消息", "请先选择厂家");
			return;
		} 
//		setNoService.getSignNo(givenNo, parseInt(custId), currDate, function(
//						res) {
//					$('signNo').value = res;
//				});
		var id = $('sId').value;
		if (id == 'null' || id == '') {
			cotSeqService.getSignNo(custId,empId,factoryId, currDate, function(
							res) {
						$('signNo').value = res;
					});
		}
	}

	// 四舍五入(保留b位小数)
	function round(a, b) {
		return ((Math.round(a * Math.pow(10, b)) * Math.pow(10, -b)).toFixed(b));
	}

	// 尺寸和英寸转换(自动填写中文规格和英文规格)
	function changeInchForDesc(fromObject, toObject, flag, eleId) {
		if (fromObject.value == '') {
			$(toObject).value = '';
			cotGivenService.updateMapValueByEleId(eleId, toObject,
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

		cotGivenService.updateMapValueByEleId(eleId, toObject,
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
				cotGivenService.updateMapValueByEleId(eleId, 'boxPbW',
						$('boxPbW').value, function(res) {
						});
				cotGivenService.updateMapValueByEleId(eleId, 'boxPbWInch',
						$('boxPbWInch').value, function(res) {
						});
			}
			if (toObject == 'boxHInch' || toObject == 'boxH') {
				$('boxPbH').value = $('boxH').value;
				$('boxPbHInch').value = $('boxHInch').value;
				cotGivenService.updateMapValueByEleId(eleId, 'boxPbH',
						$('boxPbH').value, function(res) {
						});
				cotGivenService.updateMapValueByEleId(eleId, 'boxPbHInch',
						$('boxPbHInch').value, function(res) {
						});
			}
			calPrice();
		}
	}

	// 外箱数值改变事件
	function boxOcountNum(eleId, boxObCount) {
		if (boxObCount == '' || isNaN(boxObCount)) {
			boxObCount = 0;
		}
		boxObCount = parseInt(boxObCount);
		cotGivenService.getGivenMapValue(eleId, function(res) {
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
				}
				res.boxObCount = boxObCount;
				calWeighByEleFrame($('boxWeigth').value);
				// 将对象储存到后台map中
				cotGivenService.setGivenMap(eleId.toLowerCase(), res, function(
								res) {
						});
			}
		});
	}

	function setMapValue(e) {

		DWREngine.setAsync(false);
		var eleId = $('eleId').value;
		if (eleId == '')
			return;

		var property = e.name;
		var value = e.value;
		// 如果是包装类型
		if (e.name == 'boxTypeId') {
			showType(value, eleId);
		}
		if (e.name == 'packingPrice') {
			cotGivenService.calPriceFacByPackPrice(eleId, value, function(
							facprice) {

						$('priceFac').value = facprice;

						var rec = editRec;
						rec.set("priceFac", facprice);

					});
		}
		if (e.name == 'boxObCount') {
			boxOcountNum(eleId, value);
		}
		// 如果是产品尺寸CM
		if (e.name == 'boxL' || e.name == 'boxW' || e.name == 'boxH') {
			changeInchForDesc(e, e.name + 'Inch', 0, eleId);
		}
		// 如果是产品尺寸INCH
		if (e.name == 'boxLInch' || e.name == 'boxWInch'
				|| e.name == 'boxHInch') {
			changeInchForDesc(e, e.name.substring(0, 4), 1, eleId);
		}

		// 如果是产品尺寸CM
		if (e.name == 'boxPbL' || e.name == 'boxPbW' || e.name == 'boxPbH'
				|| e.name == 'boxIbL' || e.name == 'boxIbW'
				|| e.name == 'boxIbH' || e.name == 'boxMbL'
				|| e.name == 'boxMbW' || e.name == 'boxMbH'
				|| e.name == 'boxObL' || e.name == 'boxObW'
				|| e.name == 'boxObH') {
			changeInch(e, e.name + 'Inch', 0, eleId, e.value);
		}
		// 如果是产品尺寸INCH
		if (e.name == 'boxPbLInch' || e.name == 'boxPbWInch'
				|| e.name == 'boxPbHInch' || e.name == 'boxIbLInch'
				|| e.name == 'boxIbWInch' || e.name == 'boxIbHInch'
				|| e.name == 'boxMbLInch' || e.name == 'boxMbWInch'
				|| e.name == 'boxMbHInch' || e.name == 'boxObLInch'
				|| e.name == 'boxObWInch' || e.name == 'boxObHInch') {
			changeInch(e, e.name.substring(0, 6), 1, eleId, e.value);
		}
		// 根据改变属性相应的改变Map内的值
		cotGivenService.updateMapValueByEleId(eleId, property, value, function(
						res) {
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
		var eleId = $('eleId').value;
		DWREngine.setAsync(false);

		cotGivenService.calPriceFacByPackPrice($('eleId').value, packingPrice,
				function(facprice) {
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
		DWREngine.setAsync(false);
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
		DWREngine.setAsync(true);
		var rec = editRec;

		rec.set("boxNetWeigth", $('boxNetWeigth').value);
		rec.set("boxGrossWeigth", $('boxGrossWeigth').value);
	}

});