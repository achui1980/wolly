window.onbeforeunload = function() {
	var ds = Ext.getCmp('givenDetailGrid').getStore();
	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
		if (Ext.isIE) {
			if (document.body.clientWidth - event.clientX < 170
					&& event.clientY < 0 || event.altKey) {
				return "送样明细数据有更改,您确定不保存吗?";
			} else if (event.clientY > document.body.clientHeight
					|| event.altKey) { // 用户点击任务栏，右键关闭
				return "送样明细数据有更改,您确定不保存吗?";
			} else { // 其他情况为刷新
			}
		} else if (Ext.isChrome || Ext.isOpera) {
			return "送样明细数据有更改,您确定不保存吗?";
		} else if (Ext.isGecko) {
			window.open("http://www.g.cn")
			var o = window.open("index.do?method=logoutAction");
		}
	}
}

// 窗口关闭触发事件
window.onunload = function() {
	cotGivenService.clearGivenMap("given", function(res) {
			});
}
var facNum = getDeNum("facPrecision");
var outNum = getDeNum("outPrecision");
var outNumTemp = getDeStr(outNum);
// cbm保留几位小数
var cbmNum = getDeNum("cbmPrecision");
var cbmStr = getDeStr(cbmNum);

Ext.onReady(function() {

	// 用于根据模版添加不存在样品时(newEleAdd.js)
	this.parentType = 'given';

	var facData;
	var packData;
	var typelv1Data;
	var curData;
	var givenMap;
	DWREngine.setAsync(false);
	//加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotGiven',function(rs){
		givenMap=rs;
	});
	DWREngine.setAsync(true);
	DWREngine.setAsync(false);
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	DWREngine.setAsync(true);

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
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
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

	// 送样完成
	var givenCompleteStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '否'], [1, '是']]
			});
	var givenCompleteBox = new Ext.form.ComboBox({
				name : 'checkComplete',
				fieldLabel : '是否送样',
				editable : false,
				store : givenCompleteStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 6,
				emptyText : '请选择',
				hiddenName : 'checkComplete',
				selectOnFocus : true
			});

	// 征样完成
	var givenStatusStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '未完成'], [1, '已完成']]
			});
	var givenStatusBox = new Ext.form.ComboBox({
				name : 'givenStatus',
				fieldLabel : '征样完成',
				editable : false,
				store : givenStatusStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 22,
				emptyText : '请选择',
				hiddenName : 'givenStatus',
				selectOnFocus : true
			});

	// 快递费收取
	var checkFeeStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '否'], [1, '是']]
			});
	var checkFeeBox = new Ext.form.ComboBox({
				fieldLabel : '快递费收取',
				editable : false,
				store : checkFeeStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				value : 0,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 19,
				emptyText : '请选择',
				hiddenName : 'checkFee',
				selectOnFocus : true
			});

	// 样品费收取
	var sampleFeeCheckStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '否'], [1, '是']]
			});
	var sampleFeeCheckBox = new Ext.form.ComboBox({
				fieldLabel : '样品费收取',
				editable : false,
				store : sampleFeeCheckStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				value : 0,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 13,
				emptyText : '请选择',
				hiddenName : 'sampleFeeCheck',
				selectOnFocus : true
			});

	// 支付方式
	var payTypeStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '预付'], [1, '到付'], [2, '垫付']]
			});
	var payTypeBox = new Ext.form.ComboBox({
				name : 'payType',
				fieldLabel : '支付方式',
				editable : false,
				store : payTypeStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 14,
				emptyText : '请选择',
				hiddenName : 'payType',
				selectOnFocus : true
			});

	// 审核状态
	var givenIscheckStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '未审核'], [1, '审核不通过'], [2, '审核通过'], [3, '请审核'],
						[9, '不审核']]
			});
	var givenIscheckBox = new Ext.form.ComboBox({
				name : 'givenIscheck',
				fieldLabel : '审核状态',
				value : 9,
				editable : false,
				disabledClass : 'combo-disabled',
				store : givenIscheckStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				disabled : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 23,
				emptyText : '请选择',
				hiddenName : 'givenIscheck',
				selectOnFocus : true
			});
	// 右键--批量修改厂家
	var batFacBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
		cmpId : 'bacFactoryId',
		fieldLabel : "厂家",
		editable : true,
		valueField : "id",
		sendMethod : "post",
		mode : "remote",
		displayField : "shortName",
		emptyText : '请选择',
		pageSize : 10,
		getListParent : function() {
			return this.el.up('.x-menu');
		},
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		flex : 2
	});
	// -----------------远程下拉框-----------------------------------------

	// 表格--币种
	var curGridBox = new BindCombox({
				cmpId : 'priceOutUint',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				emptyText : '请选择',
				hideLabel : true,
				autoLoad : true,
				labelSeparator : " ",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});

	// 材质
	var typeLv1Box = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "材质",
				editable : true,
				sendMethod : "post",
				valueField : "id",
				displayField : "typeName",
				emptyText : '请选择',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 34,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 表格--材质
	var typeLv1GridBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				editable : true,
				sendMethod : "post",
				hideLabel : true,
				labelSeparator : " ",
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

	// 厂家
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
		cmpId : 'factoryId',
		fieldLabel : "厂家",
		editable : true,
		hidden : hideFac,
		hideLabel : hideFac,
		sendMethod : "post",
		valueField : "id",
		displayField : "shortName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 35,
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	// 表格-厂家
	var facGridBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
		cmpId : 'factoryId',
		hideLabel : true,
		hidden : hideFac,
		labelSeparator : " ",
		editable : true,
		sendMethod : "post",
		autoLoad : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 35,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
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

	// 客户
	var customerBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
		cmpId : 'custId',
		fieldLabel : "<font color=red>客户简称</font>",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
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
		triggerAction : 'all',
		listeners : {
			"select" : function(com, rec, index) {
				if (rec.data.id != '') {
					setCustomerForm(rec.data.id);
				}
			}
		}
	});

	// 业务员
	var empsBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'bussinessPerson',
		fieldLabel : "<font color=red>业务员</font>",
		editable : true,
		valueField : "id",
		displayField : "empsName",
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

	// 出口公司
	var companyBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
		cmpId : 'companyId',
		fieldLabel : "公司",
		editable : true,
		valueField : "id",
		displayField : "companyShortName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		tabIndex : 9,
		sendMethod : "post",
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 快递费用
	var currencyBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				cmpId : 'currencyId',
				emptyText : '请选择',
				fieldLabel : "快递费用",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 17
			});

	// 样品费用
	var curBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				cmpId : 'curId',
				emptyText : '请选择',
				fieldLabel : "样品费用",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 11
			});

	// 银行
	var bankBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBank",
				cmpId : 'bankId',
				emptyText : '请选择',
				fieldLabel : "银行",
				displayField : "bankName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 10
			});

	// 快递公司
	var expressCompanyBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotExpCompany",
				cmpId : 'expressCompany',
				emptyText : '请选择',
				fieldLabel : "快递公司",
				displayField : "expCompanyNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 15
			});

	// 计算包材价格
	var calPrice = function() {
		var rdm = $('eleId').value;
		if (rdm == '' || rdm == '') {
			return;
		}
		// 在表格中查找rdm所在的行
		var index = ds.find('eleId', rdm);
		cotGivenService.calPriceAll(rdm, function(res) {
					if (res != null) {
						$('boxPbPrice').value = res[0];
						$('boxIbPrice').value = res[1];
						$('boxMbPrice').value = res[2];
						$('boxObPrice').value = res[3];
						$('packingPrice').value = res[4];
						$('inputGridPrice').value = res[6];
						DWREngine.setAsync(false);
						cotGivenService.getGivenMapValue(rdm, function(obj) {
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
										cotGivenService.setGivenMap(rdm, obj,
												function(def) {
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
						Ext.MessageBox.alert('提示消息', '请先选中一条明细!');
					}
				});
	}

	// 加载包装类型值
	var selectBind = function(com, rec, index) {
		var id = rec.data.id;
		var rdm = $('eleId').value;
		if (rdm == '' || rdm == '') {
			return;
		}
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(rdm, function(obj) {
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
										cotGivenService.setGivenMap(rdm, obj,
												function(def) {
													calPrice();
												});
										DWREngine.setAsync(true);
									}
								});

					}
				});
		DWREngine.setAsync(true);
	};

	// 包装方案
	var boxPacking = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType&key=typeName",
				valueField : "id",
				fieldLabel : "包装方案",
				editable : true,
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
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType&key=typeName",
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

	// 插格类型
	var inputGridTypeIdBox = new BindCombox({
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
				name : "boxMbTypeId",
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
			}, {
				name : "eleId"
			}, {
				name : "sortNo"
			}, {
				name : "custNo"
			}, {
				name : "eleName"
			}, {
				name : "signRequire"
			}, {
				name : "givenCount",
				type : "int"
			}, {
				name : "priceOut",
				type : "float",
				convert : numFormat.createDelegate(this, [outNumTemp], 3)
			}, {
				name : "totalMoney",
				type : "float",
				convert : numFormat.createDelegate(this, [outNumTemp], 3)
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
				name : "factoryId"
			}, {
				name : "priceOutUint"
			}, {
				name : "signCount",
				type : "int"
			}, {
				name : "priceFac",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "type"
			}, {
				name : "signFlag"
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
						read : "cotgiven.do?method=queryGivenDetail&pId="
								+ $('pId').value + "&flag=givenDetail",
						create : "cotgiven.do?method=addGivenDetail",
						update : "cotgiven.do?method=modifyGivenDetail",
						destroy : "cotgiven.do?method=removeGivenDetail"
					},
					listeners : {
						beforeload : function(store, options) {
							ds.removed = [];
							cotGivenService.clearGivenMap("given",
									function(res) {
									});
						},
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								// 判断是不是excel导入时保存
								if (_self.exlFlag) {
									// 直接导入excel
									saveReport(_self.filename, _self.isCover);
								} else {
									ds.reload();
								}
							}
							unmask();
						},
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							if (action == "destroy") {
								return;
							}
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

	// 表格中的客号文本框
	var custNoTxt = new Ext.form.TextField({
				maxLength : 100,
				listeners : {
					'focus' : function(txt) {
						txt.selectText();
					},
					"specialkey" : function(txt, eObject) {
						var temp = txt.getValue();
						if (temp != ""
								&& eObject.getKey() == Ext.EventObject.ENTER) {
							// ds.remove(editRec);
							// var task = new Ext.util.DelayedTask(function() {
							// insertByCustNo(temp);
							// // addNewGrid();
							// });
							// task.delay(5)
							if (isNaN(editRec.id)) {
								insertByCustNo(temp);
							}
						}
					}
				}
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm =  new HideColumnModel({
				defaults : {
					sortable : true
				},
				hiddenCols:givenMap,
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
							editor : new Ext.form.TextField({
										maxLength : 100,
										listeners : {
											'change' : function(txt) {
												if(isNaN(editRec.id)){
													ds.remove(editRec);// 删除行
												}
											},
											"specialkey" : function(field, e) {
												if (e.getKey() == Ext.EventObject.TAB
														&& field.getValue() != "") {
													ds.remove(editRec);// 删除行
												}
											}
										}
									}),
							width : 80
						}, {
							header : "客号",
							dataIndex : "custNo",
							editor : new Ext.form.TextField({
										maxLength : 100
									}),
							editor : custNoTxt,
							width : 80
						}, {
							header : "中文名",
							dataIndex : "eleName",
							editor : new Ext.form.TextArea({
										maxLength : 200,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 100
						}, {
							header : "改样要求",
							dataIndex : "signRequire",
							editor : new Ext.form.TextArea({
										maxLength : 200,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 200
						}, {
							header : "数量",
							dataIndex : "givenCount",
							editor : new Ext.form.NumberField({
										maxValue : 9999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 60
						}, {
							header : "单价",
							dataIndex : "priceOut",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "金额",
							dataIndex : "totalMoney",
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								// 总金额=数量*单价
								var count = record.get("givenCount");
								var price = record.get("priceOut");
								var res = (price * count).toFixed(outNum);
								record.data.totalMoney = res;
								return res;
							},
							width : 80
						}, {
							header : "币种",
							dataIndex : "priceOutUint",
							editor : curGridBox,
							width : 60,
							renderer : function(value) {
								return curData[value];
							}
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
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "外箱宽",
							dataIndex : "boxObW",
							width : 60,
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
							header : "外箱高",
							dataIndex : "boxObH",
							width : 60,
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
							header : "单位",
							dataIndex : "eleUnit",
							editor : new Ext.form.TextField({
										maxLength : 100,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
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
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "净重",
							dataIndex : "boxNetWeigth",
							width : 60,
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
						}, {
							header : "产品规格(CM)",
							dataIndex : "eleSizeDesc",
							editor : new Ext.form.TextArea({
										width : 400,
										maxLength : 500,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 100
						}, {
							header : "产品规格(INCH)",
							dataIndex : "eleInchDesc",
							editor : new Ext.form.TextArea({
										width : 400,
										maxLength : 500,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 100
						}, {
							header : "厂家",
							dataIndex : "factoryId",
							width : 120,
							editor : facGridBox,
							editable : !hideFac,
							hidden : hideFac,
							renderer : function(value) {
								if (hideFac == true) {
									return "*";
								} else {
									return facData[value];
								}
							}
						}, {
							header : "征样数量",
							dataIndex : "signCount",
							hidden : true,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var givenCount = record.get("givenCount");
								var res = givenCount;
								record.data.signCount = res;
								return res;
							},
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
							header : "生产价",
							dataIndex : "priceFac",
							hidden : true,
							hidden : hidePri,
							editor : new Ext.form.NumberField({
										maxValue : 99999999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							editable : !hidePri,
							renderer : function(value) {
								if (hidePri == true) {
									return "*";
								} else {
									return value;
								}
							}
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
							text : "添加货号",
							iconCls : "page_add",
							handler : addNewGrid,
							cls : "SYSOP_ADD"
						}, '-', {
							text : "删除货号",
							handler : onDel,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}, '-', {
							text : "导入货号",
							iconCls : "importEle",
							handler : showImportPanel,
							cls : "SYSOP_ADD"
						}, '-', {
							text : "盘点机",
							handler : showPanPanel,
							iconCls : "monitor",
							cls : "SYSOP_PRINT"
						}]
			});

	// 送样明细表格
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "givenDetailGrid",
				stripeRows : true,
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
		border : false,
		padding : "5px",
		labelWidth : 60,
		labelAlign : "right",
		items : [{
			xtype : "fieldset",
			title : "基本信息",
			anchor : '97%',
			layout : "column",
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
				title : "",
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
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "factoryNo",
							name : "factoryNo",
							maxLength : 200
						}, {
							xtype : "textfield",
							fieldLabel : "客号",
							anchor : "100%",
							tabIndex : 33,
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "custNo",
							name : "custNo",
							maxLength : 200
						}, typeLv1Box, facBox, {
							xtype : "hidden",
							id : "id",
							name : "id"
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.6,
				layout : "form",
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
							tabIndex : 42,
							maxLength : 100
						}, {
							xtype : "textfield",
							fieldLabel : "所属年份",
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
							fieldLabel : "套数",
							anchor : "100%",
							id : "eleUnitNum",
							name : "eleUnitNum",
							tabIndex : 37,
							disabledClass : 'combo-disabled',
							decimalPrecision : 0,
							maxValue : 1000000000
						}, {
							xtype : "numberfield",
							fieldLabel : "最小订量",
							anchor : "100%",
							tabIndex : 39,
							id : "eleMod",
							name : "eleMod",
							decimalPrecision : 0,
							maxValue : 1000000000
						}, {
							xtype : "textfield",
							fieldLabel : "产品等级",
							anchor : "100%",
							id : "eleGrade",
							name : "eleGrade",
							tabIndex : 41,
							maxLength : 30
						}, {
							xtype : "numberfield",
							fieldLabel : "模具费用",
							anchor : "100%",
							tabIndex : 43,
							decimalPrecision : 3,
							id : "moldCharge",
							name : "moldCharge",
							maxValue : 99999.999
						}, {
							xtype : "numberfield",
							fieldLabel : "容积",
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
							tabIndex : 51,
							id : "eleRemark",
							name : "eleRemark",
							maxLength : 500
						}]
			}]
		}, {
			xtype : "fieldset",
			title : "历史报价情况",
			anchor : '97%',
			layout : "form",
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
												disabled : true,
												disabledClass : 'combo-disabled',
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
												disabled : true,
												disabledClass : 'combo-disabled',
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
												disabled : true,
												disabledClass : 'combo-disabled',
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
												disabled : true,
												disabledClass : 'combo-disabled',
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
												disabled : true,
												disabledClass : 'combo-disabled',
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
			anchor : '97%',
			layout : "column",
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
										fieldLabel : "产品装数",
										anchor : "100%",
										tabIndex : 56,
										id : "boxPbCount",
										name : "boxPbCount",
										decimalPrecision : 0,
										maxValue : 1000000000
									}, {
										xtype : "numberfield",
										fieldLabel : "内盒装数",
										anchor : "100%",
										tabIndex : 59,
										id : "boxIbCount",
										name : "boxIbCount",
										decimalPrecision : 0,
										maxValue : 1000000000
									}, {
										xtype : "numberfield",
										fieldLabel : "中盒装数",
										anchor : "100%",
										tabIndex : 62,
										id : "boxMbCount",
										name : "boxMbCount",
										decimalPrecision : 0,
										maxValue : 1000000000
									}, {
										xtype : "numberfield",
										fieldLabel : "外箱装数",
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
										fieldLabel : "单重/G",
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
										fieldLabel : "毛重/KG",
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
										fieldLabel : "净重/KG",
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
							fieldLabel : "产品规格CM",
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
							fieldLabel : "产品包装CM",
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
							fieldLabel : "内盒包装CM",
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
							fieldLabel : "中盒包装CM",
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
							fieldLabel : "外箱包装CM",
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
							fieldLabel : "中文规格",
							anchor : "100%",
							height : 40,
							tabIndex : 105,
							id : "eleSizeDesc",
							name : "eleSizeDesc",
							maxLength : 500
						}, {
							xtype : "textarea",
							fieldLabel : "英文规格",
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
			anchor : '97%',
			layout : "column",
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
									maxLength : 10000000000
								}, {
									xtype : "numberfield",
									fieldLabel : "45\"",
									disabled : true,
									disabledClass : 'combo-disabled',
									anchor : "100%",
									tabIndex : 113,
									id : "box45Count",
									name : "box45Count",
									readOnly : true,
									maxLength : 10000000000
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
							DWREngine.setAsync(false);
							updateMapValue($('eleId').value, txt.getName(),
									newVal);
							DWREngine.setAsync(true);
							// 修改表格数据,如果表格没有这一列,则修改这一行的状态
							ds.each(function(rec) {
										if (rec.data.eleId == $('eleId').value) {
											var cell = rec.data[txt.getName()];
											if (typeof(cell) == 'undefined') {
												if (!isNaN(rec.data.id)) {
													var temp = rec.data.type;
													rec.set("type", temp
																	+ "aaa");
													rec.set("type", temp);
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
		var isMod = getPopedomByOpType("cotgiven.do", "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
		}
		if ((eleId == null || eleId == "") && $('picPath') != null) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
			return;
		}
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(eleId, function(res) {
			if (res != null) {
				if (res.boxCbm != null) {
					res.boxCbm = res.boxCbm.toFixed(cbmNum);
				}
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
					inputGridTypeIdBox.setValue('');
				} else {
					inputGridTypeIdBox.bindValue(res.inputGridTypeId);
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
				if (res.factoryId == null) {
					facBox.setValue('');
				} else {
					facBox.bindPageValue("CotFactory", "id", res.factoryId);
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

						if (res.type == 'ele') {
							res.picPath = "./showPicture.action?flag=ele&elementId="
									+ res.srcId;
						}
						if (res.type == null || res.type == 'none') {
							res.picPath = "./showPicture.action?flag=noPic";
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

	// 单元格点击后,让单元格的editor适应行高度
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		var rec = ds.getAt(rowIndex);
		if (columnIndex == 3 && rec.data.eleId != null && rec.data.eleId != "") {
			return false;
		}
		var dataIndex = cm.getDataIndex(columnIndex);
		if (dataIndex == 'factoryId' && hideFac == true) {
			return false;
		}
		if (dataIndex == 'priceFac' && hidePri == true) {
			return false;
		}
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
				if (e.field == 'eleId') {
					if (isNaN(e.record.id)) {
						insertByCfg(e.value.toUpperCase());
					} else {
						updateMapValue(e.record.data.eleId, e.field, e.value);
					}
				} else if (e.field == 'boxCbm') {// boxCbm
					changeCbm(e.value)
				} else if (e.field == 'boxTypeId') {// 包装方案
					showType(e.value, e.record.data.eleId);
				} else if (e.field == 'eleTypeidLv1') {// 材质
					typeLv1Box.bindPageValue("CotTypeLv1", "id", e.value);
					updateMapValue(e.record.data.eleId, e.field, e.value);
				} else if (e.field == 'factoryId') {// 厂家
					facBox.bindPageValue("CotFactory", "id", e.value);
					updateMapValue(e.record.data.eleId, e.field, e.value);
				} else if (e.field == 'boxObL') {// 外箱长
					changeBoxObL(e.value, false);
				} else if (e.field == 'boxObW') {// 外箱宽
					changeBoxObW(e.value, false);
				} else if (e.field == 'boxObH') {// 外箱高
					changeBoxObH(e.value, false);
				} else {
					updateMapValue(e.record.data.eleId, e.field, e.value);
				}
			});

	// 不修改内存,只修改右边表单数据
	function rightChange(field, value) {
		if (rightForm.isVisible()) {
			var txt = rightForm.getForm().findField(field);
			if (txt != null) {
				txt.setValue(value);
			}
		}
	}

	// 根据明细中的外箱长改变事件
	function changeBoxObL(newVal, type, inch) {
		var rec = null;
		var rdm;
		if (type) {
			rdm = $('eleId').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('eleId', rdm);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			rdm = rec.data.eleId;
			if (rdm == null || rdm == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObL", newVal);
		}
		updateMapValue(rdm, "boxObL", newVal);
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(rdm, function(res) {
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
						var boxObLInch = (Number(newVal).div(2.54))
								.toFixed("2");// 外箱英寸长
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
						cotGivenService.calPriceAllByEleId(rdm, function(def) {
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
						cotGivenService.setGivenMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 根据明细中的外箱宽改变事件
	function changeBoxObW(newVal, type, inch) {
		var rec = null;
		var rdm;
		if (type) {
			rdm = $('eleId').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('eleId', rdm);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			rdm = rec.data.eleId;
			if (rdm == null || rdm == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObW", newVal);
		}
		updateMapValue(rdm, "boxObW", newVal);
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(rdm, function(res) {
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

						var boxObWInch = (Number(newVal).div(2.54))
								.toFixed("2");// 外箱英寸宽
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
						cotGivenService.calPriceAllByEleId(rdm, function(def) {
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
						cotGivenService.setGivenMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 根据明细中的外箱高改变事件
	function changeBoxObH(newVal, type, inch) {
		var rec = null;
		var rdm;
		if (type) {
			rdm = $('eleId').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('eleId', rdm);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			rdm = rec.data.eleId;
			if (rdm == null || rdm == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObH", newVal);
		}
		updateMapValue(rdm, "boxObH", newVal);
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(rdm, function(res) {
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

						var boxObHInch = (Number(newVal).div(2.54))
								.toFixed("2");// 外箱英寸高
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
						cotGivenService.calPriceAllByEleId(rdm, function(def) {
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
						cotGivenService.setGivenMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

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
		cotGivenService.updateMapValueByEleId(eleId, toObject,
				$(toObject).value, function(res) {
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
	function changeCbm(newVal) {
		var rec = editRec;
		var rdm = rec.data.eleId;
		if (rdm == null || rdm == "") {
			return;
		}
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(rdm, function(res) {
					if (res != null) {
						var boxObCount = rec.data.boxObCount;// 外装数

						var con20 = $('con20').value;
						var con40H = $('con40H').value;
						var con40 = $('con40').value;
						var con45 = $('con45').value;
						if (boxObCount == '' || isNaN(boxObCount)) {
							boxObCount = 0;
						}

						var box20Count = 0;
						var box40Count = 0;
						var box40hqCount = 0;
						var box45Count = 0;
						if (newVal != '' && newVal != 0) {
							box20Count = Math.floor(con20 / newVal)
									* boxObCount;
							box40Count = Math.floor(con40 / newVal)
									* boxObCount;
							box40hqCount = Math.floor(con40H / newVal)
									* boxObCount;
							box45Count = Math.floor(con45 / newVal)
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
						res.boxCbm = newVal;
						res.boxCuft = (newVal * 35.315).toFixed("3");
						rightChange("boxCuft", res.boxCuft);
						rightChange("boxCbm", newVal);

						rightChange("box20Count", box20Count);
						rightChange("box40Count", box40Count);
						rightChange("box40hqCount", box40hqCount);
						rightChange("box45Count", box45Count);
						// 将对象储存到后台map中
						cotGivenService.setGivenMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 包装类型的值改变事件
	function showType(id, eleId) {
		if (eleId == '' || eleId == '') {
			return;
		}
		DWREngine.setAsync(false);
		cotGivenService.getGivenMapValue(eleId, function(obj) {
					if (obj != null) {
						cotElementsService.getBoxTypeById(id, function(res) {
									if (res != null) {
										boxPacking.bindValue(id);
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
										cotGivenService.setGivenMap(eleId, obj,
												function(def) {
													calPrice();
												});
										DWREngine.setAsync(true);
									}
								});

					}
				});
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

	// 右键--导出图片
	var picStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['png', 'png格式'], ['jpg', 'jpg格式'], ['bmp', 'bmp格式'],
						['gif', 'gif格式']]
			});
	var picTypeBox = new Ext.form.ComboBox({
				name : 'picType',
				labelSeparator : ' ',
				hideLabel : true,
				editable : false,
				store : picStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				getListParent : function() {
					return this.el.up('.x-menu');
				},
				value : 'png',
				flex : 2,
				validateOnBlur : true,
				triggerAction : 'all',
				hiddenName : 'picType',
				selectOnFocus : true
			});

	// 右键菜单
	var rightMenu = new Ext.menu.Menu({
				id : "rightMenu",
				items : [{
							text : "保存当前排序",
							handler : sumSortTable
						}, {
							text : "导出图片",
							menu : {
								layout : 'hbox',
								layoutConfig : {
									align : 'middle'
								},
								width : 200,
								items : [picTypeBox, {
											xtype : 'button',
											text : "导出",
											flex : 1,
											iconCls : 'page_img',
											handler : function() {
												if (picTypeBox.getValue() == '') {
													return;
												}
												if (sm.getCount() == 0) {
													Ext.Msg.alert('提示消息',
															'请先选择送样明细!');
												} else {
													rightMenu.hide();
													downPics();
												}
											}
										}]
							}
						}, {
							text : "批量指定厂家",
							hidden : hideFac,
							menu : {
								layout : 'hbox',
								layoutConfig : {
									align : 'middle'
								},
								width : 200,
								items : [batFacBox, {
									xtype : 'button',
									text : "指定",
									flex : 1,
									iconCls : 'page_mod',
									handler : function() {
										if (batFacBox.getValue() == '') {
											return;
										}
										if (sm.getCount() == 0) {
											Ext.Msg.alert('提示消息', '请先选择订单明细!');
										}else{
											rightMenu.hide();
											Ext.MessageBox.confirm('提示消息',
													'是否将选择的送样明细的厂家指定为"'
															+ batFacBox
																	.getRawValue()
															+ '"', function(btn) {
														if (btn == 'yes') {
															updateFacBatch();
														}
													});
										}
									}
								}]
							}
						}, {
							text : "数据同步",
							menu : {
								items : [{
											text : "同步到样品档案",
											handler : showTongPriceDiv
										}]
							}
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

	// 下载多张图片.并且压缩.zip
	function downPics() {
		var temp = "";
		var arr = sm.getSelections();
		if (arr.length == 0) {
			alert('请先勾选明细记录!');
			return;
		}
		Ext.each(arr, function(record) {
					temp += record.get("eleId") + ",";
				})

		var givenNo = $('givenNo').value;
		var type = $('picType').value;
		var url = "./downPics.action?priceNo=" + encodeURIComponent(givenNo) + "&tp=given&rdms="
				+ temp + "&type=" + type;
		downRpt(url);
	}

	// 判断单号是否重复参数
	var isExist = true;
	var givenForm = new Ext.form.FormPanel({
		title : "送样单基本信息-(红色为必填项)",
		labelWidth : 60,
		formId : "givenForm",
		labelAlign : "right",
		collapsible : true,
		region : 'north',
		layout : "form",
		width : "100%",
		height : 180,
		frame : true,
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
				xtype : "panel",
				title : "",
				layout : "column",
				columnWidth : 0.2,
				labelWidth : 65,
				items : [{
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 1,
							labelWidth : 60,
							items : [{
										xtype : "datefield",
										fieldLabel : "<font color=red>下单日期</font>",
										anchor : "100%",
										allowBlank : false,
										blankText : "请选择下单日期！",
										id : "givenTime",
										name : "givenTime",
										value : new Date(),
										format : "Y-m-d H:i:s",
										tabIndex : 1,
										listeners : {
											'select' : function(field, date) {
												var dt = new Date(date);
												var date = dt.format('Y-m-d');
												var dt2 = new Date();
												var time = dt2.format('H:i:s');
												field.setValue(date + " "
														+ time);
											}
										}
									}, givenCompleteBox]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.7,
							labelWidth : 60,
							items : [curBox, currencyBox]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							labelWidth : 60,
							columnWidth : 0.3,
							items : [{
										xtype : "numberfield",
										fieldLabel : "",
										anchor : "100%",
										id : "sampleFee",
										name : "sampleFee",
										hideLabel : true,
										labelSeparator : " ",
										maxValue : 999999999.99,
										allowBlank : true,
										tabIndex : 12
									}, {
										xtype : "numberfield",
										fieldLabel : "",
										anchor : "100%",
										id : "signTotalPrice",
										name : "signTotalPrice",
										hideLabel : true,
										labelSeparator : " ",
										maxValue : 999999.99,
										allowBlank : true,
										tabIndex : 18
									}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 1,
							labelWidth : 60,
							items : [givenIscheckBox]
						}]
			}, {
				xtype : "panel",
				title : "",
				layout : "column",
				columnWidth : 0.8,
				items : [{
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.25,
					labelWidth : 70,
					items : [{
						xtype : "panel",
						title : "",
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : 1,
									layout : "form",
									items : [customerBox]
								}, {
									xtype : "panel",
									title : "",
									items : [{
										xtype : "button",
										width : 20,
										text : "",
										cls : "SYSOP_ADD",
										iconCls : "page_add",
										handler : addCust,
										listeners : {
											"render" : function(obj) {
												var tip = new Ext.ToolTip({
															target : obj
																	.getEl(),
															anchor : 'top',
															maxWidth : 90,
															minWidth : 90,
															html : '新增客户资料!'
														});
											}
										}
									}]
								}]
					}, {
						xtype : "datefield",
						fieldLabel : "送样日期",
						anchor : "100%",
						allowBlank : true,
						id : "realGiventime",
						name : "realGiventime",
						format : "Y-m-d",
						tabIndex : 7
					}, sampleFeeCheckBox, checkFeeBox]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.25,
					labelWidth : 60,
					items : [empsBox, {
								xtype : "textfield",
								fieldLabel : "送样地点",
								anchor : "100%",
								allowBlank : true,
								id : "givenAddr",
								name : "givenAddr",
								tabIndex : 8,
								maxLength : 100,
								maxLengthText : "100"
							}, payTypeBox, {
								xtype : "textfield",
								fieldLabel : "样箱规格",
								anchor : "100%",
								allowBlank : true,
								id : "containerDesc",
								name : "containerDesc",
								tabIndex : 20,
								maxLength : 500,
								maxLengthText : "500"
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.25,
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
										fieldLabel : "<font color='red'>送样单号</font>",
										anchor : "100%",
										id : "givenNo",
										name : "givenNo",
										maxLength : 100,
										allowBlank : false,
										blankText : "请输入送样单号！",
										tabIndex : 4,
										listeners : {
											'change' : function(thisText) {
												if (thisText != '') {
													val();
												}
											}
										}
									}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.1,
							items : [{
										xtype : "button",
										width : 25,
										text : "",
										cls : "SYSOP_ADD",
										iconCls : "cal",
										handler : getGivenNo,
										listeners : {
											"render" : function(obj) {
												var tip = new Ext.ToolTip({
															target : obj
																	.getEl(),
															anchor : 'top',
															maxWidth : 160,
															minWidth : 160,
															html : '根据单号配置自动生成送样单号!'
														});
											}
										}
									}]
						}]
					}, companyBox, expressCompanyBox, {
						xtype : "textfield",
						fieldLabel : "样箱毛重",
						anchor : "100%",
						allowBlank : true,
						id : "grossWeightDesc",
						name : "grossWeightDesc",
						tabIndex : 21,
						maxLength : 500,
						maxLengthText : "500"
					}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 0.25,
					labelWidth : 70,
					items : [{
								xtype : "datefield",
								fieldLabel : "最迟送样期",
								anchor : "100%",
								allowBlank : true,
								id : "custRequiretime",
								name : "custRequiretime",
								format : "Y-m-d",
								tabIndex : 5
							}, bankBox, {
								xtype : "textfield",
								fieldLabel : "快递单号",
								anchor : "100%",
								allowBlank : true,
								id : "emsNo",
								name : "emsNo",
								tabIndex : 16,
								maxLength : 50,
								maxLengthText : "50"
							}, givenStatusBox]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					columnWidth : 1,
					labelWidth : 70,
					items : [{
								xtype : "textarea",
								fieldLabel : "备注",
								anchor : "100%",
								height : 20,
								allowBlank : true,
								id : "givenRemark",
								name : "givenRemark",
								tabIndex : 24,
								maxLength : 300,
								maxLengthText : "300"
							}]
				}]
			}]
		}]
	})

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "送样明细",
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
				items : [centerPanel, {
					id : "shenTab",
					name : 'shenTab',
					title : "审核记录",
					layout : 'fit',
					items : [{
						xtype : 'htmleditor',// html文本编辑器控件
						enableLinks : false,
						id : 'givenCheckreason',
						name : 'givenCheckreason',
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
					title : "应收款",
					itemId : 'otherTab',
					frameConfig : {
						autoCreate : {
							id : 'otherInfo'
						}
					},
					loadMask : {
						msg : 'Loading...'
					},
					listeners : {
						"activate" : function(panel) {
							loadOtherInfo();
						}
					}
				}],
				buttonAlign : 'center',
				buttons : [{
							text : "保存",
							cls : "SYSOP_ADD",
							id : "saveBtn",
							handler : save,
							iconCls : "page_table_save"
						}, {
							text : "重新排序",
							hidden : true,
							iconCls : "page_fen",
							handler : showSort
						}, {
							text : "删除",
							id : "delBtn",
							hidden : true,
							handler : del,
							iconCls : "page_del"
						}, {
							text : "分解",
							id : "changeBtn",
							handler : checkIsToSign,
							iconCls : "page_fen"
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
								closeandreflashEC(true, 'givenGrid', false);
							}
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
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [tbl, givenForm]
			});
	viewport.doLayout();

	// 加载表格数据
	ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});

	// 审核原因
	var chkReason = "";

	// 初始化
	function initGiveForm() {
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
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			// 初始化下单时间为当前时间
			// var givenTime = Ext.getCmp("givenTime");
			// var date = new Date();
			// givenTime.setValue(date);

			// 隐藏按钮
			// Ext.getCmp('delBtn').hide();
			// Ext.getCmp('changeBtn').hide();
			// Ext.getCmp('printBtn').hide();
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
								givenIscheckBox.setValue(9);
							} else {
								givenIscheckBox.setValue(0);
							}
							if (cfg[0].havePrice != null) {
								$('havePrice').value = cfg[0].havePrice;
							} else {
								$('havePrice').value = 2;
							}
							if (cfg[0].insertType != null) {
								$('insertType').value = cfg[0].insertType;
							} else {
								$('insertType').value = 0;
							}
						}
					});
			if ($('cusId').value != 'null' && $('cusId').value != '') {
				customerBox
						.bindPageValue("CotCustomer", "id", $('cusId').value);
			}
			if ($('busiId').value != 'null' && $('busiId').value != '') {
				empsBox.bindPageValue("CotEmps", "id", $('busiId').value);
			}
		} else {
			// 加载送样单信息
			cotGivenService.getGivenById(parseInt(id), function(res) {
				DWRUtil.setValues(res);
				chkReason = res.givenCheckreason;
				// 初始配置值
				cotGivenService.getList('CotPriceCfg', function(cfg) {
					if (cfg.length != 0) {
						// if(cfg[0].isCheck == 0){
						// //隐藏按钮
						// Ext.getCmp('shenTab').disable();
						// Ext.getCmp('requestBtn').hide();
						// }else{
						if (res.givenIscheck == 9) {
							// 隐藏审核记录及审核相关按钮
							Ext.getCmp('shenTab').disable();
							Ext.getCmp('requestBtn').hide();
						} else {
							if (res.givenIscheck == 0 || res.givenIscheck == 1) {
								// $('checkyesBtn').style.display='none';
								// $('checknoBtn').style.display='none';
								// $('recheckBtn').style.display='none';
							} else if (res.givenIscheck == 2) {
								Ext.getCmp('requestBtn').hide();
								Ext.getCmp('recheckBtn').setVisible(true);
							} else {
								Ext.getCmp('requestBtn').hide();
								Ext.getCmp('passBtn').setVisible(true);
								Ext.getCmp('unpassBtn').setVisible(true);
							}
						}
						givenIscheckBox.setValue(res.givenIscheck);
						// }
						if (cfg[0].insertType != null) {
							$('insertType').value = cfg[0].insertType;
						} else {
							$('insertType').value = 0;
						}
						if (cfg[0].havePrice != null) {
							$('havePrice').value = cfg[0].havePrice;
						} else {
							$('havePrice').value = 2;
						}
					}
				});

				// 加载时间
				if (res.givenTime != null && res.givenTime != '') {
					var date = new Date(res.givenTime);
					var givenTime = Ext.getCmp("givenTime");
					givenTime.setValue(date);
				}

				if (res.custRequiretime != null && res.custRequiretime != '') {
					var date = new Date(res.custRequiretime);
					var custRequiretime = Ext.getCmp("custRequiretime");
					custRequiretime.setValue(date);
				}

				if (res.realGiventime != null && res.realGiventime != '') {
					var date = new Date(res.realGiventime);
					var realGiventime = Ext.getCmp("realGiventime");
					realGiventime.setValue(date);
				}

				// 加载下拉框
				if (res.checkComplete != null) {
					givenCompleteBox.setValue(res.checkComplete);
				}
				if (res.givenStatus != null) {
					givenStatusBox.setValue(res.givenStatus);
				}
				if (res.checkFee != null) {
					checkFeeBox.setValue(res.checkFee);
				} else {
					checkFeeBox.setValue(0);
				}
				if (res.payType != null) {
					payTypeBox.setValue(res.payType);
				}
				if (res.sampleFeeCheck != null) {
					sampleFeeCheckBox.setValue(res.sampleFeeCheck);
				} else {
					sampleFeeCheckBox.setValue(0);
				}

				bankBox.bindValue(res.bankId);
				expressCompanyBox.bindValue(res.expressCompany);
				curBox.bindValue(res.curId);
				currencyBox.bindValue(res.currencyId);

				companyBox.bindPageValue("CotCompany", "id", res.companyId);
				customerBox.bindPageValue("CotCustomer", "id", res.custId);
				empsBox.bindPageValue("CotEmps", "id", res.bussinessPerson);
			});
			// 分页基本参数
			// ds.load({
			// params : {
			// start : 0,
			// limit : 15,
			// pId: id,
			// flag : 'givenDetail'
			// }
			// });
		}
		DWREngine.setAsync(true);
	}
	unmask();
	initGiveForm();

	// 添加空白record到表格中
	function addNewGrid() {

		if ($('givenIscheck').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}
		// 验证表单
		var formData = getFormValues(givenForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 获得该行的新序号(该列的最大值+1)
		var sortNo = 0;
		ds.each(function(rec) {
					if (rec.data.sortNo > sortNo)
						sortNo = rec.data.sortNo;
				});
		var u = new ds.recordType({
					eleId : "",
					sortNo : sortNo + 1,
					custNo : "",
					eleName : "",
					signRequire : "",
					givenCount : "1",
					priceOut : "0.0",
					totalMoney : "",
					boxTypeId : "",
					boxObL : "",
					boxObW : "",
					boxObH : "",
					eleUnit : "",
					eleTypeidLv1 : "",
					boxGrossWeigth : "",
					boxNetWeigth : "",
					boxCbm : "",
					eleSizeDesc : "",
					eleInchDesc : "",
					factoryId : "",
					signCount : "1",
					type : "",
					srcId : ""
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
	}

	// 获得表格选择的记录
	function getGivenDetailIds() {
		var list = Ext.getCmp("givenDetailGrid").getSelectionModel()
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
		var cord = sm.getSelections();
		if (cord.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
			return;
		}
		DWREngine.setAsync(false);
		Ext.each(cord, function(item) {
					var signFlag = item.get("signFlag");// 判断是否已经分解
					if (signFlag == "1") {
						Ext.Msg.confirm("消息提示", "货号：" + item.get("eleId")
										+ "已经分解，是否确定删除？", function(btn) {
									if (btn == 'yes') {
										// 清除内存对象
										cotGivenService.delGivenMapByKey(item
														.get("eleId"),
												function(res) {
													ds.remove(item);
												});
									}
								});
					} else {
						// 清除内存对象
						cotGivenService.delGivenMapByKey(item.get("eleId"),
								function(res) {
									ds.remove(item);
								});
					}
				});
		DWREngine.setAsync(true);
		if (rightForm.isVisible()) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
		}
	}

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
		var gid = $('pId').value;
		DWREngine.setAsync(false);
		var insertFlag = false;
		if (gid != 'null' && gid != '') {
			cotGivenService.getGivenById(parseInt(gid), function(cotgiven) {
						if (cotgiven.givenIscheck == 2 && loginEmpId != "admin") {
							insertFlag = true;
						}
					});
		}
		if (insertFlag) {
			Ext.Msg.alert('提示框', '对不起,该单已审核通过，如需修改!请先执行反审操作或联系管理员!');
			return;
		}

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

	// 获取单号
	function getGivenNo() {
		var currDate = $('givenTime').value;
		var custId = $('custId').value;
		var empId = empsBox.getValue();
		if (currDate == "" || custId == null) {
			Ext.MessageBox.alert("提示消息", "请先选择下单日期");
			return;
		}
		if ($('custId').value == null || $('custId').value == "") {
			Ext.MessageBox.alert("提示消息", "请先选择客户");
			return;
		}
		if (empsBox.getValue() == null || empsBox.getValue() == "") {
			Ext.MessageBox.alert("提示消息", "请先选择业务员");
			return;
		}
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			cotSeqService.getGivenNo(custId, empId, currDate, function(res) {
						$('givenNo').value = res;
					});
			$('givenNo').focus();
		}
	}

	// 删除
	function del() {
		// 如果该单没保存直接删除
		if ($('pId').value == 'null' || $('pId').value == '') {
			closeandreflashEC(true, 'givenGrid', false);
			return;
		}
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0)// 没有删除权限
		{
			Ext.Msg.alert("提示框", "您没有删除权限");
			return;
		}

		var id = $('pId').value;
		var insertFlag = false;
		DWREngine.setAsync(false);
		cotGivenService.getGivenById(parseInt(id), function(cotgiven) {
					if (cotgiven.givenIscheck == 2 && loginEmpId != "admin") {
						insertFlag = true;
					}
				});
		if (insertFlag) {
			Ext.Msg.alert("提示框", '对不起,该单已审核通过，如需删除!请执行反审操作或联系管理员!');
			return;
		}
		var cotGiven = new CotGiven();
		var list = new Array();
		cotGiven.id = id;
		list.push(cotGiven);
		var dealFlag = false;
		cotGivenService.getDealNumById(id, function(res) {
					if (res != -1)
						dealFlag = true;
				})
		if (dealFlag) {
			Ext.MessageBox.alert("提示框", '对不起，该单已有应付帐款记录，不能删除！');
			return;
		}
		Ext.MessageBox.confirm('提示信息', '是否确定删除?', function(btn) {
			if (btn == 'yes') {
				// 查询该主送样单是否被删除
				cotGivenService.getGivenById(id, function(res) {
					if (res != null) {
						cotGivenService.deleteGivenList(list, function(res) {
									if (res) {
										Ext.Msg.alert("提示框", "删除成功");
										closeandreflashEC('true', 'givenGrid',
												false);
									} else {
										Ext.Msg.alert("提示框", "删除失败，该送样单已经被使用中");
									}
								})
					} else {
						closeandreflashEC('true', 'givenGrid', false);
					}
				});
			}
		});
		DWREngine.setAsync(true);
	}

	// 单据状态及权限判断
	function checkIsToSign() {
		// 如果该单没保存
		if ($('pId').value == 'null' || $('pId').value == '') {
			Ext.MessageBox.alert("提示框", "该单还未保存，不能分解！")
			return;
		}
		DWREngine.setAsync(false);
		var givenId = $('pId').value;
		cotGivenService.getGivenById(parseInt(givenId), function(cotgiven) {
					if (cotgiven.givenIscheck != 2 && loginEmpId != "admin") {
						if (cotgiven.givenIscheck != 9) {
							Ext.Msg.alert("提示框", '该送样单还未通过审核，不能分解！');
							return;
						} else if (cotgiven.givenStatus == 1) {
							Ext.Msg.alert("提示框", '该送样单已分解过！');
							return;
						} else {
							givenToSign();
						}
					} else if (cotgiven.givenStatus == 1) {
						Ext.Msg.alert("提示框", '该送样单已分解过！');
						return;
					} else {
						givenToSign();
					}
				});
	}

	// 分解送样单到征样单
	function givenToSign() {
		DWREngine.setAsync(false);
		var givenId = $('pId').value;
		// 通过主单id获取明细的集合
		cotGivenService.getDetailListByGivenId(parseInt(givenId),
				function(res) {
					if (res == null) {
						Ext.Msg.alert("提示框", "此送样单还未添加样品！");
						return;
					} else {
						var factoryIds = ''; // 厂家ids列表
						var nullEleIds = ''; // 空厂家的货号
						for (var i = 0; i < res.length; i++) {
							if (res[i].factoryId == null) {
								nullEleIds += res[i].eleId + ",";
							}
							factoryIds += res[i].factoryId + ",";
						}
						if (nullEleIds != '') {
							Ext.Msg.alert("提示框", "货号" + nullEleIds
											+ "的厂家为空，请指定后再分解！");
							return;
						}
						var factoryIdAry = factoryIds.split(',');// 字符串转化为数组
						// 分解生成主征样单
						cotGivenService.saveSign(factoryIdAry,
								parseInt(givenId), function(flag) {
									if (flag) {
										Ext.Msg.alert("提示框", "分解成功！");
										var flg = 'separate';
										cotGivenService.modifyGivenStatus(
												parseInt(givenId), flg,
												function(status) {
												});
										reflashParent('givenGrid');
										reflashParent('signGrid');
									} else {
										Ext.Msg.alert("提示框", "分解失败！");
									}
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 如果该单没保存直接删除
		if ($('pId').value == 'null' || $('pId').value == '') {
			Ext.Msg.alert("提示框", "该单还未保存，不能打印")
			return;
		}
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("提示信息", '您没有打印权限！');
			return;
		}

		if (printWin == null) {
			printWin = new PrintWin({
						type : 'given',
						pId : 'pId',
						pNo : 'givenNo',
						status : 'givenIscheck',
						custName : customerBox.getRawValue(),
						mailSendId : 'custId'
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

	// 保存
	function save() {
		var popedom = checkAddMod($('pId').value);
		if (popedom == 1) {
			Ext.Msg.alert("提示框", '对不起,您没有添加权限!请联系管理员!');
			return;
		} else if (popedom == 2) {
			Ext.Msg.alert("提示框", '对不起,您没有修改权限!请联系管理员!');
			return;
		}
		// 审核通过不能修改
		if ($('givenIscheck').value == 2 && loginEmpId != "admin") {
			Ext.Msg.alert("提示框", '对不起,该送样单已被审核通过不能再修改!');
			return;
		}

		// 验证表单
		var formData = getFormValues(givenForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 验证单号是否存在
		var shortflag = false;
		var givenNo = $('givenNo').value;
		DWREngine.setAsync(false);
		cotGivenService.findIsExistGivenNo(givenNo, $('pId').value, function(
						res) {
					if (res != null) {
						shortflag = true;
					}
				});
		if (shortflag) {
			Ext.MessageBox.alert("提示框", "该送样单号已存在，请重新输入！");
			return;
		}
		DWREngine.setAsync(true);

		var shortflag = false;
		var givenNo = $('givenNo').value;
		DWREngine.setAsync(false);
		cotGivenService.findIsExistGivenNo(givenNo, $('pId').value, function(
						res) {
					if (res != null) {
						shortflag = true;
					}
				});
		if (shortflag) {
			Ext.MessageBox.alert("提示框", "该送样单号已存在，请重新输入！");
			return;
		}
		DWREngine.setAsync(true);

		Ext.MessageBox.confirm('提示信息', '您是否要保存该送样单？', function(btn) {
			if (btn == 'yes') {
				var given = DWRUtil.getValues('givenForm');
				var cotGiven = {};
				// 如果编号存在时先查询出对象,再填充表单
				if ($('pId').value != 'null' && $('pId').value != '') {
					DWREngine.setAsync(false);
					cotGivenService.getGivenById($('pId').value, function(res) {
								for (var p in res) {
									if (p != 'givenTime'
											&& p != 'custRequiretime'
											&& p != 'realGiventime') {
										res[p] = given[p];
									}
								}
								cotGiven = res;
								cotGiven.id = $('pId').value;
							});
					DWREngine.setAsync(true);
				} else {
					cotGiven = new CotGiven();
					for (var p in cotGiven) {
						if (p != 'givenTime' && p != 'custRequiretime'
								&& p != 'realGiventime') {
							cotGiven[p] = given[p];
						}
					}
				}
				DWREngine.setAsync(false);
				// 审核原因
				if (Ext.getCmp("givenCheckreason").isVisible()) {
					cotGiven.givenCheckreason = $('givenCheckreason').value;
				}
				cotGivenService.saveOrUpdateGiven(cotGiven,
						$('givenTime').value, $('custRequiretime').value,
						$('realGiventime').value, function(res) {
							if (res != null) {
								// 当新建时,如果默认配置需要审核,保存后显示"提请审核"按钮
								if ($('pId').value == 'null') {
									if ($('givenIscheck').value == 0) {
										Ext.getCmp('requestBtn')
												.setVisible(true);
									}
								}
								$('pId').value = res;

								// 更改添加action参数
								var urlAdd = '&givenPrimId=' + res + '&custId='
										+ $('custId').value;
								// 更改修改action参数
								var urlMod = '&givenPrimId=' + res + '&custId='
										+ $('custId').value;
								ds.proxy.setApi({
									read : "cotgiven.do?method=queryGivenDetail&pId="
											+ res + "&flag=givenDetail",
									create : "cotgiven.do?method=addGivenDetail"
											+ urlAdd,
									update : "cotgiven.do?method=modifyGivenDetail"
											+ urlMod,
									destroy : "cotgiven.do?method=removeGivenDetail"
								});
								ds.save();
								Ext.Msg.alert("提示消息", "保存成功！");
								reflashParent('givenGrid');
							} else {
								Ext.MessageBox.alert('提示消息', '保存失败');
								// 
							}
							unmask();
						});
				DWREngine.setAsync(true);
			}
		});
	}

	// 显示导入界面
	var _self = this;
	function showImportPanel() {

		if ($('givenIscheck').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = getFormValues(givenForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		// 验证单号是否存在
		var shortflag = false;
		var givenNo = $('givenNo').value;
		DWREngine.setAsync(false);
		cotGivenService.findIsExistGivenNo(givenNo, $('pId').value, function(
						res) {
					if (res != null) {
						shortflag = true;
					}
				});
		if (shortflag) {
			Ext.MessageBox.alert("提示框", "该送样单号已存在，请重新输入！");
			return;
		}
		DWREngine.setAsync(true);

		var cfg = {};
		cfg.custId = $('custId').value;
		cfg.bar = _self;
		cfg.type = 'given';
		cfg.no = $('givenNo').value;

		// 图片导入需要pId
		var id = $('pId').value;
		if (id != '' && id != 'null') {
			cfg.pId = id;
		} else {
			cfg.pId = 0;
		}

		var importPanel = new ImportPanel(cfg);
		importPanel.show();
		// 为了加快加载速度，默认打开Excel导入标签
		importPanel.openPnl(0);
	}

	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list, type) {
		insertByBatch(list, type);
	}

	// 添加选择的样品到可编辑表格
	function insertByBatch(list, type) {
		// 删除该货号所在行
		if (type == 'ele') {
			ds.remove(editRec);
		}
		var eleAry = new Array();
		var idAry = new Array();
		Ext.each(list, function(item) {
					eleAry.push(item.data.eleId);
					idAry.push(item.data.id);
				});
		for (var i = 0; i < eleAry.length; i++) {
			DWREngine.setAsync(false);
			cotGivenService.findIsExistDetail(eleAry[i], idAry[i], type,
					function(detail) {
						if (detail != null) {
							detail.srcId = idAry[i];
							insertToGrid(detail, type);
						}
					});
			DWREngine.setAsync(true);
		}
		unmask();
	}

	// 添加数据从excel
	this.insertSelectExcel = function() {

		DWREngine.setAsync(false);
		// 判断该产品货号是否存在
		cotGivenService.checkExistByExcel(function(result) {
			if (result==true) {
				var id = $('pId').value;
				var custId = customerBox.getValue();
				var currencyId = curBox.getValue();
				// 保存订单明细
				cotGivenService.saveDetail(id, currencyId, custId,
						function(alt) {
							// 加载厂家表缓存
							baseDataUtil.getBaseDicDataMap("CotFactory", "id",
									"shortName", function(res) {
										facData = res;
									});
							// 加载包装方案表缓存
							baseDataUtil.getBaseDicDataMap("CotBoxType", "id",
									"typeName", function(res) {
										packData = res;
									});
							// 加载材质表缓存
							baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id",
									"typeName", function(res) {
										typelv1Data = res;
									});
							// 加载币种表缓存
							baseDataUtil.getBaseDicDataMap("CotCurrency", "id",
									"curNameEn", function(res) {
										curData = res;
									});
							// 刷新表格
							ds.proxy.setApi({
								read : "cotgiven.do?method=queryGivenDetail&pId="
										+ id + "&flag=givenDetail"
							});
							ds.load({
										params : {
											start : 0,
											limit : 1000
										}
									});
							unmask();
						});
			}
		});
		DWREngine.setAsync(true);
	}

	// 单样品利润率
	var liRunCau = '';

	var excel = false;
	// 根据产品货号查找样品数据
	function insertToGridExcel(eleId) {
		// 将计算单价的参数组成一个map
		var mapGiven = {};

		// 根据样品货号查找样品
		cotGivenService.findDetailByEleIdExcel(eleId, mapGiven, liRunCau,
				function(result) {
					if (result != null) {
						result.type = 'none';
						result.srcId = 0;
						result.givenCount = 1;
						result.signCount = 1;
						// 将添加的样品对象储存到后台map中
						cotGivenService.setExcelMap(eleId.toLowerCase(),
								result, function(res) {
								});
					}
				});
	}

	// 根据产品货号查找样品数据
	function insertToGrid(detail, type) {

		var eleId = detail.eleId;
		DWREngine.setAsync(false);
		var cotGivenDetail = new CotGivenDetail();

		cotGivenDetail = detail;
		cotGivenDetail.type = type;
		if (type == 'ele') {
			cotGivenDetail.srcId = detail.srcId;
		} else {
			cotGivenDetail.srcId = detail.id;
		}
		cotGivenDetail.id = null;

		cotGivenDetail.givenCount = 1;
		cotGivenDetail.signCount = 1;

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
		// 获得该行的新序号(该列的最大值+1)
		var sortNo = 0;
		ds.each(function(rec) {
					if (rec.data.sortNo > sortNo)
						sortNo = rec.data.sortNo;
				});
		obj.sortNo = sortNo + 1;
		var u = new ds.recordType(obj);
		ds.add(u);
	}

	// 根据报价配置决定单价的取值
	function insertByCfg(eleId) {
		cotGivenService.checkExist(eleId.toUpperCase(), function(checkRes) {
			if (checkRes == true) {
				alert('货号"' + eleId + '"已经添加过了!请重新输入!');
				return;
			} else {
				var havePrice = $('havePrice').value;
				var givenTime = $('givenTime').value;
				if (havePrice == 2) {
					cotGivenService.findDetail(eleId, $('custId').value,
							givenTime, function(rtn) {
								// 取得随机数
								var rdm = getRandom();
								if (rtn != null) {
									var result = rtn[1];
									if (rtn[0] == 1) {
										result.type = 'price';
										result.priceOut = result.pricePrice;
										result.priceOutUint = result.currencyId;
									} else if (rtn[0] == 2) {
										result.type = 'order';
										result.priceOut = result.orderPrice;
										result.priceOutUint = result.currencyId;

									} else if (rtn[0] == 3) {
										result.type = 'given';
									}
									result.srcId = result.id;

									//ds.remove(editRec);// 删除行
									var task = new Ext.util.DelayedTask(
											function() {
												insertToGrid(result,
														result.type);
												addNewGrid();
											});
									task.delay(5);
								} else {
									insertByCfgBlur(eleId);
								}
							});
				} else {
					insertByCfgBlur(eleId);
				}

			}
		});
	}

	// 根据报价配置决定单价的取值
	function insertByCfgBlur(eleId) {
		// DWREngine.setAsync(false);
		// 判断该产品货号是否存在,有存在的话转成报价明细
		cotGivenService.changeEleToGivenDetail(eleId, function(res) {
					if (res != null) {
						res.type = 'ele';
						res.srcId = res.id;
						res.id = null;
						//ds.remove(editRec);// 删除行
						var task = new Ext.util.DelayedTask(function() {
									insertToGrid(res, 'ele');
									addNewGrid();
								});
						task.delay(5);
					} else {
						if ($('insertType').value == 0) {
							showEleTable(eleId);
						} else {
							// 直接当新货号加到表格
							noEleAsNewByBlur(eleId);
							//addNewGrid();
						}
					}
				});
		// DWREngine.setAsync(true);
	}

	// 直接添加到明细中
	function noEleAsNewByBlur(eleId) {
		// 将添加的样品对象储存到后台map中
		var objDetail = new Object();
		objDetail.eleId = eleId;
		objDetail.type = 'none';
		objDetail.givenCount = 1;
		objDetail.signCount = 1;
		objDetail.srcId = 0;
		objDetail.priceOut = 0;
		setObjToGrid(objDetail);
		cotGivenService.setGivenMap(eleId.toLowerCase(), objDetail,
				function(res) {
				});
		// 把焦点放在新增空白行
		// addNewGrid();
	}

	// 不存在样品档案货号直接添加到明细中
	this.noEleAsNew = function(pareEleId) {
		//ds.remove(editRec);// 删除行
		// var rec = editRec;
		// 将添加的样品对象储存到后台map中
		var objDetail = new Object();
		objDetail.eleId = pareEleId.toUpperCase();;
		objDetail.type = 'none';
		objDetail.givenCount = 1;
		objDetail.signCount = 1;
		objDetail.srcId = 0;
		objDetail.priceOut = 0;
		// rec.set('eleId', pareEleId.toUpperCase());
		// rec.set('type', 'none');
		// rec.set('srcId', 0);
		// rec.set('givenCount', 1);
		// rec.set('signCount', 1);
		setObjToGrid(objDetail);
		cotGivenService.setGivenMap(pareEleId.toLowerCase(), objDetail,
				function(res) {
				});
		// 把焦点放在新增空白行
		// addNewGrid();
	}

	// 供按样品模版添加的页面调用
	this.newEleAdd = function(obj) {
		// var rdm = getRandom();
		ds.remove(editRec);// 删除行
		// 设置序列号
		obj.type = 'ele';
		obj.srcId = obj.id;
		obj.id = '';
		// obj.rdm = rdm;

		setObjToGrid(obj);

		// 转化下时间
		var date = new Date(obj.eleAddTime);
		var date2 = new Date(obj.eleProTime);
		obj.eleAddTime = date;
		obj.eleProTime = date2;
		// 将添加的样品对象储存到后台map中
		cotGivenService.setGivenMap(obj.eleId.toLowerCase(), obj,
				function(res) {
				});
		// 把焦点放在新增空白行
		addNewGrid();
	}

	// 显示样品查询面板
	var _self = this;
	function showEleTable(eleId) {
		var eleQueryWin = new EleQueryWin({
					bar : _self,
					title : '在样品资料中查找到以 "' + eleId + '" 开头的货号:',
					eleIdFind : eleId
				});
		eleQueryWin.show();
	};

	// 更新内存数据
	function updateMapValue(eleId, property, value) {
		DWREngine.setAsync(false);
		cotGivenService.updateMapValueByEleId(eleId, property, value, function(
						res) {
					if (rightForm.isVisible()) {
						var field = rightForm.getForm().findField(property);
						if (field != null) {
							field.setValue(value);
						}
					}
				});
		DWREngine.setAsync(true);
	}

	// 请求审核
	function requestCheck() {
		// 审核权限判断
		var isPopedom = getPopedomByOpType('cotgiven.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否请求审核?', function(btn) {
					if (btn == 'yes') {
						givenIscheckBox.setValue(3);
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
		var isPopedom = getPopedomByOpType('cotgiven.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否通过审核?', function(btn) {
					if (btn == 'yes') {
						givenIscheckBox.setValue(2);
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
		var isPopedom = getPopedomByOpType('cotgiven.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '确定不通过审核?', function(btn) {
					if (btn == 'yes') {
						givenIscheckBox.setValue(1);
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
		var isPopedom = getPopedomByOpType('cotgiven.do', "CHECK");
		if (isPopedom == 0)// 有审核权限
		{
			Ext.Msg.alert('提示框', '您没有审核的权限！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('提示信息', '是否请求反审?', function(btn) {
					if (btn == 'yes') {
						givenIscheckBox.setValue(0);
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
		var gid = $('pId').value;
		if (gid != 'null' && gid != '') {
			cotGivenService.saveGivenStatus(parseInt(gid),
					parseInt($('givenIscheck').value), function(res) {
						if (res != null) {
							// 显示审核按钮
							cotGivenService.getList('CotPriceCfg',
									function(cfg) {
										if (cfg.length != 0) {
											// if(cfg[0].isCheck == 0){
											// Ext.getCmp('shenTab').disable();
											// Ext.getCmp('requestBtn').hide();
											// }else{
											cotGivenService.getGivenById(
													parseInt(res), function(
															given) {
														if (given != null) {
															if (given.givenIscheck != 9) {
																if (given.givenIscheck == 3) {
																	Ext
																			.getCmp('requestBtn')
																			.hide();
																	Ext
																			.getCmp('passBtn')
																			.setVisible(true);
																	Ext
																			.getCmp('unpassBtn')
																			.setVisible(true);
																} else if (given.givenIscheck == 0) {
																	Ext
																			.getCmp('requestBtn')
																			.setVisible(true);
																	Ext
																			.getCmp('recheckBtn')
																			.hide();
																} else if (given.givenIscheck == 2) {
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
											// }
										}
									});
						}
					});
		} else {
			Ext.Msg.alert('提示框', '请先保存主单！');
		}
		DWREngine.setAsync(true);
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
		var rdm = $('eleId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('eleId', rdm);
		cotGivenService.calPriceFacByPackPrice(rdm, packPrice, function(
						facprice) {
					if (facprice != -1 && index != -1) {
						var rec = ds.getAt(index);
						rec.set("priceFac", facprice);
					}
				});

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

	// 保存排序
	function sumSortTable() {
		var sort = ds.getSortState();
		if (!sort) {
			Ext.MessageBox.alert("提示消息", "排序没变化,不用再保存!");
			return;
		}
		if ($("pId").value == '' || $("pId").value == 'null') {
			Ext.MessageBox.alert("提示消息", "请先保存送样单,再更改排序!");
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
		cotGivenService.updateSortNo($("pId").value, type, sort.field,
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

	// 验证
	function val() {
		var shortflag = false;
		var givenNo = $('givenNo').value;
		DWREngine.setAsync(false);
		cotGivenService.findIsExistGivenNo(givenNo, $('pId').value, function(
						res) {
					if (res != null) {
						shortflag = true;
					}
				});
		if (shortflag) {
			Ext.MessageBox.alert("提示框", "该送样单号已存在，请重新输入！");
			return;
		}
		DWREngine.setAsync(true);
	}

	// 根据客号查找货号(如果货号有值不做处理.)
	function insertByCustNo(custNo) {
		// if (event.keyCode != 13) {
		// return;
		// }

		var rec = editRec;
		if (rec != null && rec.data.eleId != null && rec.data.eleId != '') {
			return;
		}
		// 根据客号查找货号(取最近报价时间)
		cotGivenService.findEleByCustNo(custNo, $('custId').value,
				$('givenTime').value, function(rtn) {
					if (rtn != null) {
						var obj = rtn[1];
						if (rtn[0] == 1) {
							obj.priceOutUint = obj.currencyId;
							obj.priceOut = obj.pricePrice;
							obj.type = 'price';
						} else if (rtn[0] == 2) {
							obj.priceOutUint = obj.currencyId;
							obj.priceOut = obj.orderPrice;
							obj.type = 'order';
						} else if (rtn[0] == 3) {
							obj.type = 'given';
						}
						obj.srcId = obj.id;
						obj.id = null;

						// 取最近一次报价
						// insertToGrid(obj, obj.type);
						obj.givenCount = 1;
						obj.signCount = 1;
						// 查找该货号是否已存在
						var eleId = obj.eleId.toUpperCase();
						var index = ds.find('eleId', eleId);
						if (index == -1) {
							// DWREngine.setAsync(false);
							// cotGivenService.checkExist(eleId,
							// function(checkRes) {
							// if (checkRes == true) {
							// Ext.MessageBox.alert('提示消息','该客号对应的货号"'
							// + eleId + '"已经添加过了!请重新输入客号!');
							// } else {
							// cotGivenService.setGivenMap(eleId
							// .toLowerCase(), obj,
							// function(res) {
							// ds.remove(editRec);// 删除行
							// setObjToGrid(obj);
							// });
							// }
							// });
							// DWREngine.setAsync(true);
							cotGivenService.setGivenMap(eleId.toLowerCase(),
									obj, function(res) {
										ds.remove(editRec);// 删除行
										setObjToGrid(obj);
									});
						} else {
							Ext.MessageBox.alert('提示消息', '该客号对应的货号"' + eleId
											+ '"已经添加过了!请重新输入客号!');
						}

					}
				});
	}

	// 打开盘点机上传面板
	function showPanPanel() {
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			Ext.MessageBox.alert('提示消息', '请先保存送样单！再导入盘点机文件！');
			return;
		}
		// 验证表单
		var formData = getFormValues(givenForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 判断表格是否有未保存的数据
		if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
			Ext.MessageBox.alert('提示消息', '送样明细数据有更改，请先保存！再导入盘点机文件！');
			return;
		}

		var win = new UploadPan({
					waitMsg : "File uploading......",
					uploadUrl : './uploadGivenMachine.action',
					validType : "txt|cvs",
					// saveByExcelFn : saveByExcel
					pWin : _self
				})
		win.show();
	}
	// 组成方式
	var sortStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, '升序'], [1, '降序']]
			});
	var sortComb = new Ext.form.ComboBox({
				name : 'sortSel',
				tabIndex : 4,
				fieldLabel : '方式',
				emptyText : '请选择',
				store : sortStore,
				value : 0,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 60,
				hiddenName : 'sortSel',
				selectOnFocus : true
			});
	// 显示模板面板
	var sortPanel;
	function showSort(item, pressed) {
		if ($('sortDiv') == null) {
			Ext.DomHelper.append(document.body, {
				html : '<div id="sortDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (sortPanel == null) {
			sortPanel = new Ext.form.FormPanel({
						title : '重新排序',
						layout : 'form',
						width : 250,
						frame : true,
						labelWidth : 60,
						labelAlign : "right",
						tools : [{
									id : "close",
									handler : function(event, toolEl, p) {
										p.hide();
									}
								}],
						items : [{
									xtype : 'radiogroup',
									fieldLabel : "",
									hideLabel : true,
									items : [{
												boxLabel : '按序号',
												inputValue : 0,
												style : "marginLeft:8",
												name : 'sortType',
												id : "sortXu",
												checked : true
											}, {
												boxLabel : '按货号',
												id : "sortEleId",
												inputValue : 1,
												name : 'sortType'
											}, {
												boxLabel : '按客号',
												id : "sortCustNo",
												inputValue : 2,
												name : 'sortType'

											}]
								}, {
									layout : 'hbox',
									layoutConfig : {
										padding : '5',
										align : 'middle'
									},
									defaults : {
										margins : '0 5 0 0'
									},
									items : [{
												xtype : 'panel',
												flex : 1
											}, sortComb, {
												text : '生成',
												xtype : 'button',
												flex : 1,
												iconCls : 'page_add',
												handler : sumSort
											}, {
												xtype : 'panel',
												flex : 1
											}]
								}]
					});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("sortDiv").setLeftTop(left, top - 110);

			sortPanel.render("sortDiv");
		} else {
			if (!sortPanel.isVisible()) {
				sortPanel.show();
			} else {
				sortPanel.hide();
			}
		}
	};
	// 保存排序
	function sumSort() {
		var type = $("sortSel").value;
		var field = "";
		var fieldType = 'int';
		if ($("sortXu").checked == true) {
			field = "sortNo";
		}
		if ($("sortEleId").checked == true) {
			field = "eleId";
			fieldType = 'string';
		}
		if ($("sortCustNo").checked == true) {
			field = "custNo";
			fieldType = 'string';
		}
		DWREngine.setAsync(false);
		cotGivenService.updateSortNo($("pId").value, type, field, fieldType,
				function(res) {
					if (res) {
						ds.reload();
						sortPanel.hide();
						Ext.MessageBox.alert('提示消息', "重新排序成功!");
					} else {
						Ext.MessageBox.alert('提示消息', "重新排序失败!");
					}
				});
		DWREngine.setAsync(true);
	}
	// 同步到样品
	function showTongPriceDiv() {
		// var isPopedom = getPopedomByOpType("cotorder.do", "SAMPLETOORDER");
		// if (isPopedom == 0) {
		// Ext.MessageBox.alert('提示消息', "您没有从样品同步到订单的权限");
		// return;
		// }
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("提示消息", "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("提示消息", "只能选择一条记录!")
			return;
		}
		chooseWin = new TongChooseWin({
					flag : 0
				});
		chooseWin.show();
	}
	// 成批指定订单明细的厂家
	function updateFacBatch() {
		var bacFactoryId = batFacBox.getValue();
		var recs = sm.getSelections();
		Ext.each(recs, function(item) {
					item.set('factoryId', bacFactoryId);
					cotGivenService.updateMapValueByEleId(item.data.eleId,
							'factoryId', bacFactoryId, function(res) {
							});
				});
	}
	// 更新到样品表
	this.updateToEle = function(eleStr, boxStr, otherStr, isPic) {
		var cord = sm.getSelections();
		var key = new Array();
		Ext.each(cord, function(item) {
					if (item.data.eleId.trim() != '') {
						key.push(item.data.eleId);
					}
				});

		if (key.length == 0) {
			Ext.MessageBox.alert('提示消息', '请选择要更新到样品的明细记录!');
			return;
		}
		chooseWin.close();
		DWREngine.setAsync(false);
		mask();
		DWREngine.setAsync(true);
		cotGivenService.findIsExistInEle(key, function(res) {
					var sameTemp = '';
					var same = res.same;
					// 获得存在样品档案的货号的随机数
					var sameVal = new Array();
					for (var i = 0; i < same.length; i++) {
						sameTemp += same[i] + ",";
					}
					// 获得不存在样品档案的货号的随机数
					var dis = res.dis;
					if (same.length != 0) {
						Ext.MessageBox.confirm('提示信息', '已存在样品货号' + sameTemp
										+ "是否覆盖原样品记录?", function(btn) {
									if (btn == 'yes') {
										cotGivenService.updateToEle(same, dis,
												eleStr, boxStr, otherStr,
												isPic, function(res) {
													Ext.MessageBox.alert(
															'提示消息', '同步成功!');
												});
									} else {
										if (dis.length != 0) {
											cotGivenService.updateToEle(null,
													dis, eleStr, boxStr,
													otherStr, isPic, function(
															res) {
														Ext.MessageBox
																.alert('提示消息',
																		'同步成功!');
													});
										}
									}
								});
					} else {
						if (dis.length != 0) {
							cotGivenService.updateToEle(null, dis, eleStr,
									boxStr, otherStr, isPic, function(res) {
										Ext.MessageBox.alert('提示消息', '同步成功!');
									});
						}
					}
					unmask();
				});
	}
	// 应收款
	var otherflag = false;// 是否要重新调用frame
	function loadOtherInfo() {
		var isMod = getPopedomByOpType(vaildUrl, "OTHER");
		if (isMod == 0) {
			alert('对不起,您没有操作应收款的权限!');
		} else {
			if (!otherflag) {
				var frame = window.frames["otherInfo"];
				frame.location.href = "cotgiven.do?method=queryRecv&source=given&fkId="
						+ $('pId').value;
				otherflag = true;
			}
		}
	}
	// 给应收款页面调用,验证主表单
	this.checkParent = function() {
		// 验证表单
		var formData = getFormValues(givenForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return false;
		} else {
			return true;
		}
	}
	// 根据选择的客户编号自动填写表单
	function setCustomerForm(cusId) {
		DWREngine.setAsync(false);
		cotOrderService.getCustomerById(parseInt(cusId), function(res) {
					DWRUtil.setValues(res);
					// 如果客户没价格条款,不加载
					$('custId').value = res.id;
					empsBox.bindPageValue("CotEmps", "id", res.empId);
				});
		DWREngine.setAsync(true);
	}

	this.exlFlag = false;
	this.filename;
	this.isCover;
	// 保存
	this.saveByExcel = function(filename, isCover) {
		_self.filename = filename;
		_self.isCover = isCover;
		var popedom = checkAddMod($('pId').value);
		if (popedom == 1) {
			Ext.MessageBox.alert('提示消息', '对不起,您没有添加权限!请联系管理员!');
			return;
		} else if (popedom == 2) {
			Ext.MessageBox.alert('提示消息', '对不起,您没有修改权限!请联系管理员!');
			return;
		}
		// 审核通过不让修改
		if ($('givenIscheck').value == 2 && loginEmpId != "admin") {
			Ext.Msg.alert("提示框", '对不起,该送样单已被审核通过不能再修改!');
			return;
		}

		// 验证表单
		var formData = getFormValues(givenForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var shortflag = false;
		var givenNo = $('givenNo').value;
		DWREngine.setAsync(false);
		cotGivenService.findIsExistGivenNo(givenNo, $('pId').value, function(
						res) {
					if (res != null) {
						shortflag = true;
					}
				});
		DWREngine.setAsync(true);
		if (shortflag) {
			Ext.MessageBox.alert("提示框", "该送样单号已存在，请重新输入！");
			return;
		}
		var given = DWRUtil.getValues('givenForm');
		var cotGiven = {};
		// 如果编号存在时先查询出对象,再填充表单
		if ($('pId').value != 'null' && $('pId').value != '') {
			DWREngine.setAsync(false);
			cotGivenService.getGivenById($('pId').value, function(res) {
						for (var p in res) {
							if (p != 'givenTime' && p != 'custRequiretime'
									&& p != 'realGiventime') {
								res[p] = given[p];
							}
						}
						cotGiven = res;
						cotGiven.id = $('pId').value;
					});
			DWREngine.setAsync(true);
		} else {
			cotGiven = new CotGiven();
			for (var p in cotGiven) {
				if (p != 'givenTime' && p != 'custRequiretime'
						&& p != 'realGiventime') {
					cotGiven[p] = given[p];
				}
			}
		}
		DWREngine.setAsync(false);
		// 审核原因
		if (Ext.getCmp("givenCheckreason").isVisible()) {
			cotGiven.givenCheckreason = $('givenCheckreason').value;
		}
		DWREngine.setAsync(false);
		cotGivenService.saveOrUpdateGiven(cotGiven, $('givenTime').value,
				$('custRequiretime').value, $('realGiventime').value, function(
						res) {
					if (res != null) {
						// 当新建时,如果默认配置需要审核,保存后显示"提请审核"按钮
						if ($('pId').value == 'null') {
							if ($('givenIscheck').value == 0) {
								Ext.getCmp('requestBtn').setVisible(true);
							}
						}
						$('pId').value = res;

						// 如果订单明细没有修改
						if (ds.getModifiedRecords().length == 0
								&& ds.removed.length == 0) {
							// 直接导入excel
							saveReport(filename, isCover);
						} else {
							// 更改添加action参数
							var urlAdd = '&givenPrimId=' + res + '&custId='
									+ $('custId').value;
							// 更改修改action参数
							var urlMod = '&givenPrimId=' + res + '&custId='
									+ $('custId').value;
							ds.proxy.setApi({
								read : "cotgiven.do?method=queryGivenDetail&pId="
										+ res + "&flag=givenDetail",
								create : "cotgiven.do?method=addGivenDetail"
										+ urlAdd,
								update : "cotgiven.do?method=modifyGivenDetail"
										+ urlMod,
								destroy : "cotgiven.do?method=removeGivenDetail"
							});
							_self.exlFlag = true;
							ds.save();
						}
						reflashParent('givenGrid');
					} else {
						Ext.MessageBox.alert('提示消息', '保存失败');
						unmask();
					}
				});
		DWREngine.setAsync(true);
	}

	// 保存excel
	function saveReport(filename, isCover) {
		var infoPanel = Ext.getCmp('infoPanel');
		cotGivenService.saveReport(filename, isCover, $('pId').value, function(
				msgList) {
			if (msgList == null) {
				infoPanel.body.dom.innerHTML = "文件没找到或该文件不是excel！";
			} else {
				if (msgList.length == 1) {
					if (msgList[0].flag == 1) {
						infoPanel.body.dom.innerHTML = "<font color=red>"
								+ msgList[0].msg + "</font>";
					} else {
						infoPanel.body.dom.innerHTML = "导入成功&nbsp(<label>"
								+ msgList[0].successNum
								+ "</label>)&nbsp条，导入失败&nbsp(<label>"
								+ msgList[0].failNum
								+ "</label>)&nbsp条，覆盖成功&nbsp(<label>"
								+ msgList[0].coverNum + "</label>)条。";
						_self.insertSelectExcel();
					}
				} else {
					var htm = '<div class="detail_nav"><label>错误信息(修改excel文件后重新上传)</label></div><div style="height: 360px; overflow: auto;">';
					var temp = '';
					for (var i = 0; i < msgList.length; i++) {
						if (i != msgList.length - 1) {
							htm += "<div id=err"
									+ i
									+ ">第&nbsp("
									+ (msgList[i].rowNum + 1)
									+ ")&nbsp行，第&nbsp("
									+ (msgList[i].colNum + 1)
									+ ")&nbsp列的错误信息："
									+ msgList[i].msg
									+ "&nbsp;&nbsp;<a href=# onclick=reportload("
									+ i + "," + msgList[i].rowNum
									+ ")>【重新上传】</a></div><br/>";
						} else {
							susNum = msgList[i].successNum;
							failNum = msgList[i].failNum;
							temp = '导入成功&nbsp(<label id="successNumLab">'
									+ msgList[i].successNum
									+ "</label>)&nbsp条，导入失败&nbsp(<label id='failNumLab'>"
									+ msgList[i].failNum
									+ "</label>)&nbsp条，覆盖成功&nbsp(<label id='coverNumLab'>"
									+ msgList[i].coverNum + "</label>)条。";
							if (susNum > 0 || msgList[i].coverNum > 0) {
								_self.insertSelectExcel();
							}
						}
					}
					infoPanel.body.dom.innerHTML = temp + '</div>' + htm;
				}
			}
		});
	}

	// 通过外包装数计算毛净重和装箱数
	function calWandC(txt, newVal, oldVal) {
		var rec = null;
		var rdm = $('eleId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('eleId', rdm);
		if (index != -1) {
			rec = ds.getAt(index);
		} else {
			return;
		}
		var boxCbm = rec.data.boxCbm;
		var boxWeigth = $('boxWeigth').value;
		if (rdm != null && rdm != "") {
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
			calPrice();

		}
	}

	// 通过单重计算净重/毛重
	function calWeight(txt, newVal, oldVal) {
		var rec = null;
		var rdm = $('eleId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('eleId', rdm);
		if (index != -1) {
			rec = ds.getAt(index);
		} else {
			return;
		}
		if (rdm != null && rdm != "") {
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
						updateMapValue(rdm, "boxNetWeigth", jin.toFixed(3));
						updateMapValue(rdm, "boxGrossWeigth", boxGrossWeigth);
					});

		}
	}

	// 通过cbm计算cuft
	function calCuftCube(txt, newVal, oldVal) {
		var rec = null;
		var rdm = $('eleId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('eleId', rdm);
		if (index != -1) {
			rec = ds.getAt(index);
		} else {
			return;
		}
		var boxObCount = rec.data.boxObCount;
		if (rdm != null && rdm != "") {
			if (isNaN(newVal) || newVal == "") {
				newVal = 0;
				$('boxCuft').value = 0;
			} else {
				var s = newVal * 35.315;
				$('boxCuft').value = s.toFixed(3);
			}
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
		var rdm = $('eleId').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('eleId', rdm);
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
			rec.set('boxCbm', cbm);
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
	// 中英文规格跟着变化
	function changeSizeTxt() {
		var rdm = $('eleId').value;
		if (rdm == null || rdm == "") {
			return;
		}
		$('eleInchDesc').value = $('boxLInch').value + "*"
				+ $('boxWInch').value + "*" + $('boxHInch').value;
		updateMapValue(rdm, "eleInchDesc", $('eleInchDesc').value);
		$('eleSizeDesc').value = $('boxL').value + "*" + $('boxW').value + "*"
				+ $('boxH').value;
		updateMapValue(rdm, "eleSizeDesc", $('eleSizeDesc').value);
	};
	// 计算英寸
	function calInch(txt, newVal, oldVal) {
		var rdm = $('eleId').value;
		if (rdm == null || rdm == "") {
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
		var rdm = $('eleId').value;
		if (rdm == null || rdm == "") {
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
});