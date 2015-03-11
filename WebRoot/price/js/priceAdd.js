window.onbeforeunload = function() {
	var ds = Ext.getCmp('priceDetailGrid').getStore();
	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
		if (Ext.isIE) {
			if (document.body.clientWidth - event.clientX < 170
					&& event.clientY < 0 || event.altKey) {
				return "Price quotation data has changed, you sure do not save?";
			} else if (event.clientY > document.body.clientHeight
					|| event.altKey) { // 用户点击任务栏，右键关闭
				return "Price quotation data has changed, you sure do not save?";
			} else { // 其他情况为刷新
			}
		} else if (Ext.isChrome || Ext.isOpera) {
			return "Price quotation data has changed, you sure do not save?";
		} else if (Ext.isGecko) {
			window.open("http://www.g.cn")
			var o = window.open("index.do?method=logoutAction");
		}
	}
}

var checkColumn = null;
// 窗口关闭触发事件
window.onunload = function() {
	cotPriceService.clearMap("price", function(res) {
			});
}
// 获取报价和订单的单价要保留几位小数
var facNum = getDeNum("facPrecision");
var outNum = getDeNum("outPrecision");
var deNum = getDeNum("orderPricePrecision");
// cbm保留几位小数
var cbmNum = getDeNum("cbmPrecision");
// 根据小数位生成"0.000"字符串
var facNumTemp = getDeStr(facNum);
var strNum = getDeStr(deNum);
var cbmStr = getDeStr(cbmNum);

Ext.onReady(function() {
	var _self = this;
	// var hiddenCols = {'eleName':true,'eleNameEn':true};
	// 用于根据模版添加不存在样品时(newEleAdd.js)
	this.parentType = 'price';

	DWREngine.setAsync(false);
	var facData;
	var curData;
	var typeData;
	var priceMap;
	// 加载厂家表缓存
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				facData = res;
			});
	DWREngine.setAsync(true);
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	// 加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotPriceDetail', function(rs) {
				priceMap = rs;
			});
	DWREngine.setAsync(true);
	DWREngine.setAsync(false);
	// 加载材质表缓存
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeName",
			function(res) {
				typeData = res;
			});
	DWREngine.setAsync(true);
	// -----------------下拉框-----------------------------------------
	// 联系人
	var contactBox = new BindCombox({
				cmpId : 'contactId',
				dataUrl : "servlet/DataSelvert?tbname=CotCustContact",
				mode : 'local',
				sendMethod : "post",
				emptyText : 'Choose',
				fieldLabel : "Contact",
				displayField : "contactPerson",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 4
			});

	// 客户名称
	var custBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		fieldLabel : "<font color=red>Customer</font>",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "Please select customers!",
		tabIndex : 1,
		sendMethod : "post",
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		listeners : {
			"select" : function(com, rec, index) {
				if (rec.data.id != '') {
					setCustomerForm(rec.data.id);
					contactBox.reset();
					contactBox.loadValueById('CotCustContact', 'customerId',
							rec.data.id);
				}
			}
		}
	});

	// 出口公司
	var companyBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
				cmpId : 'companyId',
				fieldLabel : "<font color=red>Company</font>",
				editable : true,
				valueField : "id",
				displayField : "companyShortName",
				emptyText : 'Choose',
				mode : 'remote',
				pageSize : 10,
				anchor : "100%",
				allowBlank : false,
				blankText : "Please select the Company！",
				tabIndex : 2,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 业务员
	var empBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'businessPerson',
		fieldLabel : "<font color=red>Sale</font>",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "Please select the sales！",
		tabIndex : 5,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "产品分类",
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
				fieldLabel : "海关编码",
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
	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				fieldLabel : "<font color=red>Currency</font>",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				allowBlank : false,
				blankText : "Please select currency！",
				tabIndex : 7,
				listeners : {
					"select" : function(com, rec, index) {
						composeChange();
						priceBlurByCurreny(rec.data.id);
					}
				}
			});

	// 表格--币种
	var curGridBox = new BindCombox({
				cmpId : 'priceFacUint',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				hideLabel : true,
				labelSeparator : " ",
				autoLoad : true,
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});

	// 条款类型
	var clauseBox = new BindCombox({
				cmpId : 'clauseId',
				dataUrl : "servlet/DataSelvert?tbname=CotClause",
				emptyText : 'Choose',
				fieldLabel : "Delivery Terms",
				displayField : "clauseName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 9,
				listeners : {
					"select" : function(com, rec, index) {
						_self.priceBlur();
						composeChange();
					}
				}
			});
	// 起运港
	var shipPortBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotShipPort&key=shipPortNameEn",
				cmpId : 'shipportId',
				fieldLabel : "Port of Loading",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "shipPortNameEn",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 11,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,
				listeners : {
					"select" : function(com, rec, index) {
						composeChange();
					}
				}
			});
	// 目的港
	var targetPortBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotTargetPort&key=targetPortEnName",

		cmpId : 'targetportId',
		fieldLabel : "Port of destination",
		editable : true,
		valueField : "id",
		allowBlank : true,
		displayField : "targetPortEnName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 12,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		listeners : {
			"select" : function(com, rec, index) {
				composeChange();
			}
		}
	});

	// 集装箱
	var containerBox = new BindCombox({
				cmpId : 'containerTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotContainerType",
				emptyText : 'Choose',
				fieldLabel : "Container",
				displayField : "containerName",
				valueField : "id",
				hidden : true,
				hideLabel : true,
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 12,
				listeners : {
					"select" : function(com, rec, index) {
						_self.priceBlur();
					}
				}
			});

	// 佣金类型
	var commisionBox = new BindCombox({
				cmpId : 'commisionTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotCommisionType",
				emptyText : 'Choose',
				fieldLabel : "佣金类型",
				displayField : "commisionName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 16,
				listeners : {
					"select" : function(com, rec, index) {
						composeChange();
					}
				}
			});

	// 运输方式
	var trafficBox = new BindCombox({
				cmpId : 'trafficId',
				dataUrl : "servlet/DataSelvert?tbname=CotTrafficType",
				emptyText : 'Choose',
				hidden : true,
				hideLabel : true,
				fieldLabel : "Transportation",
				displayField : "trafficNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 17
			});

	// 保险契约
	var insureContractBox = new BindCombox({
				cmpId : 'insureContractId',
				dataUrl : "servlet/DataSelvert?tbname=CotInsureContract",
				emptyText : 'Choose',
				fieldLabel : "保险契约",
				displayField : "insureNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 27
			});

	// 付款方式
	var payTypeBox = new BindCombox({
				cmpId : 'payTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotPayType",
				emptyText : 'Choose',
				fieldLabel : "付款方式",
				displayField : "payName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 28
			});

	// 审核状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, '未审核'], [1, '审核不通过'], [2, '审核通过'], [3, '请求审核'],
						[9, '不审核']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'priceStatus',
				tabIndex : 21,
				disabled : true,
				disabledClass : 'combo-disabled',
				fieldLabel : '审核状态',
				editable : false,
				value : 9,
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'priceStatus',
				selectOnFocus : true
			});
	// 材质
	var typeLv1Box = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "材质",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				sendMethod : "post",
				emptyText : 'Choose',
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
				hideLabel : true,
				labelSeparator : " ",
				valueField : "id",
				displayField : "typeName",
				sendMethod : "post",
				emptyText : 'Choose',
				pageSize : 10,
				autoLoad : true,
				anchor : "100%",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 厂家
	var facBox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1&validUrl=cotfactory.do",
		cmpId : 'factoryId',
		fieldLabel : "Supplier",
		editable : true,
		hidden : hideFac,
		hideLabel : hideFac,
		sendMethod : "post",
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 42,
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
		labelSeparator : " ",
		editable : true,
		sendMethod : "post",
		valueField : "id",
		autoLoad : true,
		hidden : hideFac,
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

	// 产品来源
	var sourceStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['WOLLY', 'WOLLY'], ['SUPPLIER', 'SUPPLIER'],
						['CUSTOMER', 'CUSTOMER']]
			});
	var sourceBox = new Ext.form.ComboBox({
				name : 'eleFrom',
				tabIndex : 48,
				fieldLabel : 'Source',
				editable : false,
				value : 'WOLLY',
				store : sourceStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'eleFrom',
				selectOnFocus : true
			});

	// 包装方案
	var boxPacking = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType&key=typeName",
				valueField : "id",
				// fieldLabel : "Packing Way",
				hidden : true,
				editable : true,
				tabIndex : 52,
				displayField : "typeName",
				cmpId : "boxTypeId",
				emptyText : 'Choose',
				anchor : "95%",
				listeners : {
					"select" : selectBind
				}
			});
	// 产品类型
	var boxPbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=PB",
				valueField : "id",
				fieldLabel : "Material Type",
				editable : true,
				tabIndex : 65,
				displayField : "value",
				cmpId : "boxPbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('rdm').value, "boxPbTypeId",
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
				fieldLabel : "Material Type",
				editable : true,
				tabIndex : 70,
				displayField : "value",
				cmpId : "boxIbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('rdm').value, "boxIbTypeId",
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
				editable : true,
				tabIndex : 63,
				displayField : "value",
				cmpId : "boxMbTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('rdm').value, "boxMbTypeId",
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
				fieldLabel : "Material Type",
				editable : true,
				tabIndex : 75,
				displayField : "value",
				cmpId : "boxObTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				listeners : {
					"select" : function(com, rec, index) {
						DWREngine.setAsync(false);
						updateMapValue($('rdm').value, "boxObTypeId",
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
						updateMapValue($('rdm').value, "inputGridTypeId",
								rec.data.id);
						DWREngine.setAsync(true);
						calPrice();
					}
				}
			});

	// 大类
	var typeBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
		cmpId : 'byId',
		fieldLabel : "Dep. Of PRODUCT",
		editable : true,
		valueField : "id",
		displayField : "typeEnName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : '100%',
		sendMethod : "post",
		selectOnFocus : true,
		// allowBlank : false,
		// blankText : "Choose Dep. Of Sales！",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
			// listeners : {
			// "select" : function(com, rec, index) {
			// if (rec.data.id != '') {
			// type2Box
			// .setDataUrl("./servlet/DataSelvert?tbname=CotTypeLv2&key=typeName&typeName=typeIdLv1&type="
			// + rec.data.id);
			// type2Box.resetData();
			// type3Box.resetData();
			// }
			// }
			// }
		});

	// 中类
	var type2Box = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
		cmpId : 'myId',
		fieldLabel : "Group",
		editable : true,
		valueField : "id",
		displayField : "typeName",
		emptyText : 'Choose',
		anchor : '100%',
		pageSize : 10,
		sendMethod : "post",
		selectOnFocus : true,
		// allowBlank : false,
		// blankText : "Choose Group！",
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
			// listeners : {
			// "select" : function(com, rec, index) {
			// if (rec.data.id != '') {
			// type3Box
			// .setDataUrl("./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName&typeName=typeIdLv2&type="
			// + rec.data.id);
			// type3Box.resetData();
			// }
			// }
			// }
		});

	// 小类
	var type3Box = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : 'syId',
				fieldLabel : "Category",
				editable : true,
				anchor : '100%',
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				// allowBlank : false,
				// blankText : "Choose Category！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 供应厂家
	var facEleBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'facEleId',
				fieldLabel : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : '100%',
				// allowBlank : false,
				// blankText : "Choose a Supplier！",
				tabIndex : 2,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

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
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "eleCol"
			}, {
				name : "pricePrice",
				type : "float",
				convert : numFormat.createDelegate(this, [strNum], 3)
			}, {
				name : "checkFlag"
			}, {
				name : "taoUnit"
			}, {
				name : "eleUnit"
			}, {
				name : "boxWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "liRun",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "tuiLv",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
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
				name : "eleTypeidLv1"
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "boxIbCount",
				type : "int"
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
				name : "boxCbm",
				type : "float",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "boxGrossWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "boxNetWeigth",
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "type"
			}, {
				name : "srcId"
			}, {
				name : "rdm"
			}, {
				name : "importTax"
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
						read : "cotprice.do?method=queryPriceDetail&priceId="
								+ $('pId').value,
						create : "cotprice.do?method=add",
						update : "cotprice.do?method=modify",
						destroy : "cotprice.do?method=remove"
					},
					listeners : {
						beforeload : function(store, options) {
							ds.removed = [];
							// cotPriceService.clearMap("price", function(res) {
							// });
						},
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("Message", "Save Failed！");
							} else {
								// 判断是不是excel导入时保存
								if (_self.exlFlag) {
									// 直接导入excel
									saveReport(_self.eFlag, _self.filename,
											_self.isCover);
								} else {
									ds.reload();
								}
								_self.exlFlag = false;
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

	checkColumn = new Ext.grid.CheckColumn({
				header : '<input type="checkbox" onclick="checkAll(this)">',
				sortable : false,
				hidden : true,
				align : 'center',
				dataIndex : 'checkFlag',
				menuDisabled : true,
				tooltip : "Lock in prices",
				width : 60
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
							if (isNaN(editRec.id)) {
								insertByCustNo(temp);
							}
						}
					}
				}
			});

	// 创建需要在表格显示的列
	var cm = new HideColumnModel({
				defaults : {
					sortable : true
				},
				hiddenCols : priceMap,
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							hidden : true,
							width : 50
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
							header : "Art No.",
							dataIndex : "eleId",
							width : 150,
							editor : new Ext.form.TextField({
										maxLength : 100,
										listeners : {
											'change' : function(txt) {
												if (isNaN(editRec.id)) {
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
									})
						}, {
							header : "Cust.No.",
							dataIndex : "custNo",
							editor : custNoTxt,
							hidden : priceMap['custNo'],
							width : 120
						}, {
							header : "中文名",
							hidden : true,
							dataIndex : "eleName",
							editor : new Ext.form.TextArea({
										maxLength : 200,
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
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 120
						}, {
							header : "颜色",
							hidden : true,
							dataIndex : "eleCol",
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
							header : "Sales Price",
							dataIndex : "pricePrice",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.9999,
										decimalPrecision : 4,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, checkColumn, {
							header : "报价单位",
							hidden : true,
							dataIndex : "taoUnit",
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
							header : "Unit",
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
							header : "单重",
							hidden : true,
							dataIndex : "boxWeigth",
							width : 60,
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Import Tax(%)",
							dataIndex : "importTax",
							width : 100,
							summaryType : 'average'
						}, {
							header : "Profit%",
							dataIndex : "liRun",
							width : 90,
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							renderer : function(value) {
								if (value == null) {
									value = 0;
								}
								if (value <= 20) {
									return "<span style='color:red;font-weight:bold;'>"
											+ value + "</span>";
								} else {
									return value;
								}
							}
						}, {
							header : "退税率％",
							dataIndex : "tuiLv",
							width : 60,
							hidden : true,
							editor : new Ext.form.NumberField({
										maxValue : 99999999.99,
										decimalPrecision : 2,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							editor : facGridBox,
							width : 80,
							editable : !hideFac,
							hidden : true,
							renderer : function(value) {
								if (hideFac == true) {
									return "*";
								} else {
									return facData[value];
								}
							}
						}, {
							header : "Supplier's A.No.",
							dataIndex : "factoryNo",
							width : 110,
							editor : new Ext.form.TextField({
										maxLength : 100,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Purchase Price",
							dataIndex : "priceFac",
							width : 110,
							editable : !hidePri,
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
							renderer : function(value) {
								if (hidePri == true) {
									return "*";
								} else {
									return value;
								}
							}
						}, {
							header : "Currency",
							dataIndex : "priceFacUint",
							editor : curGridBox,
							editable : !hidePri,
							hidden : hidePri,
							width : 90,
							renderer : function(value) {
								if (hidePri == true) {
									return "*";
								} else {
									return curData[value];
								}
							}
						}, {
							header : "材质",
							hidden : true,
							dataIndex : "eleTypeidLv1",
							width : 60,
							editor : typeLv1GridBox,
							renderer : function(value) {
								return typeData[value];
							}
						}, {
							header : "Inner Box",
							dataIndex : "boxIbCount",
							width : 80,
							hidden : true,
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
							hidden : true,
							dataIndex : "boxMbCount",
							width : 30,
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
							header : "PCS/CTN",
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
							header : "外箱长",
							hidden : true,
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
							hidden : true,
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
							hidden : true,
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
							header : "CBM",
							dataIndex : "boxCbm",
							width : 50,
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
							header : "N.W(Kgs)",
							dataIndex : "boxNetWeigth",
							width : 90,
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
							header : "G.W(Kgs)",
							dataIndex : "boxGrossWeigth",
							width : 90,
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
							header : "产品规格(CM)",
							hidden : true,
							dataIndex : "eleSizeDesc",
							editor : new Ext.form.TextArea({
										maxLength : 500,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 120
						}, {
							header : "Size Description",
							dataIndex : "eleInchDesc",
							editor : new Ext.form.TextArea({
										maxLength : 500,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
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
				pageSize : 1000,
				store : ds,
				displayInfo : true,
				// displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : 'NONE',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : [{
							text : "Create",
							handler : addNewGrid,
							iconCls : "page_add"
						}, '-', {
							text : "Del",
							handler : onDel,
							iconCls : "page_del"
						}, '-', {
							text : "Import",
							handler : showImportPanel.createDelegate(this, [0]),
							iconCls : "importEle"
						}, '-', {
							text : "Barcode Machine",
							handler : showPanPanel,
							iconCls : "barcode"
						},
						// '-', {
						// text : "配件及成本信息",
						// handler : showFitPricePanel,
						// iconCls : "gird_list",
						// cls : "SYSOP_PRINT"
						// },
						'-', {
							text : "Inverse margin",
							handler : calLiRun,
							iconCls : "cal"
						}, '-', {
							text : "Create Art.No",
							handler : showEleModel,
							iconCls : "page_add"
						}]
			});

	// 报价明细表格
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "priceDetailGrid",
				stripeRows : true,
				// clicksToEdit : 1,
				margins : "0 5 0 0",
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : checkColumn,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				cls : 'rightBorder',
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 单元格双击事件--1.控制新增一条货号后,货号eleId不能再双击;2.让单元格的editor适应行高度
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		if ($('priceStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert('Message',
							'Sorry, this quotation has been reviewed can not be modified!');
			return false;
		}
		var rec = ds.getAt(rowIndex);
		var dataIndex = cm.getDataIndex(columnIndex);
		if (isNaN(rec.id) && dataIndex == 'eleId' && rec.data.rdm != null
				&& rec.data.rdm != "") {
			return false;
		}
		if (dataIndex == 'factoryId' && hideFac == true) {
			return false;
		}
		if ((dataIndex == 'priceFac' || dataIndex == 'priceFacUint')
				&& hidePri == true) {
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

	// 行点击时加载右边折叠面板表单
	grid.on("rowclick", function(grid, rowIndex, e) {
				// 如果右边折叠面板有展开过才加载数据
				if (rightForm.isVisible()) {
					initForm(ds.getAt(rowIndex));
				}
			});

	// 单元格编辑后,加载右边面板数据
	grid.on("afteredit", function(e) {
				if (e.field == 'eleId') {
					if (isNaN(e.record.id)) {
						insertByCfg(e.value.toUpperCase());
					} else {
						updateMapValue(e.record.data.rdm, e.field, e.value);
					}
				} else if (e.field == 'pricePrice') {// 价格
					priceOutChange(e.value);
				} else if (e.field == 'boxWeigth') {// 单重
					boxWeigthChange(e.value);
				} else if (e.field == 'liRun') {// 利润率
					liRunChange(e.value);
				} else if (e.field == 'tuiLv') {// 退税率
					tuiLvChange(e.value);
				} else if (e.field == 'boxCbm') {// boxCbm
					changeCbm(e.value);
				} else if (e.field == 'priceFac') {// 生产价
					priceFacChange(e.value);
				} else if (e.field == 'priceFacUint') {// 生产价币种
					priceFacUintChange(e.value, e.originalValue);
				} else if (e.field == 'boxIbCount') {// 内
					boxIcountNum(e.value);
				} else if (e.field == 'boxMbCount') {// 中
					boxMcountNum(e.value);
				} else if (e.field == 'boxObCount') {// 外
					boxOcountNum(e.value);
				} else if (e.field == 'boxObL') {// 外箱长
					changeBoxObL(e.value, false);
				} else if (e.field == 'boxObW') {// 外箱宽
					changeBoxObW(e.value, false);
				} else if (e.field == 'boxObH') {// 外箱高
					changeBoxObH(e.value, false);
				} else {
					updateMapValue(e.record.data.rdm, e.field, e.value);
				}
			});

	// 右键--导出图片
	var picStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['png', 'png'], ['jpg', 'jpg'], ['bmp', 'bmp'],
						['gif', 'gif']]
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
					text : "Save the current sort",
					handler : sumSortTable
				}, {
					text : "Exporting pictures",
					menu : {
						layout : 'hbox',
						layoutConfig : {
							align : 'middle'
						},
						width : 200,
						items : [picTypeBox, {
							xtype : 'button',
							text : "Export",
							flex : 1,
							iconCls : 'page_img',
							handler : function() {
								if (picTypeBox.getValue() == '') {
									return;
								}
								if (sm.getCount() == 0) {
									Ext.Msg.alert('Message',
											'Please select a price quotation!');
								} else {
									rightMenu.hide();
									downPics();
								}
							}
						}]
					}
				}, {
					text : "Information View",
					menu : {
						items : [
								// {
								// text : "配件及成本",
								// handler : showFitPricePanel
								// },
								{
							text : "Price History",
							handler : showHisPricePanel
						}, {
							text : "Order Price",
							handler : showModPriceWin
						}]
					}
				}, {
					text : "Data Synchronization",
					menu : {
						items : [{
									text : "Synchronized to the puoduct ",
									handler : showTongDiv
								}, {
									text : "Synchronized to offer puoduct",
									handler : showTongPriceDiv
								}]
					}
				}, {
					text : "Data import",
					menu : {
						items : [{
									text : "Import from quotation",
									handler : showImportPanel.createDelegate(
											this, [1])
								}, {
									text : "Import from order",
									handler : showImportPanel.createDelegate(
											this, [2])
								}
								// , {
								// text : "mport from delivery",
								// handler : showImportPanel.createDelegate(
								// this, [3])
								// }
								, {
									text : "Import from EXCEL",
									handler : showImportPanel.createDelegate(
											this, [0])
								}
								// , {
								// text : "imported from the sub item",
								// handler : showImportPanel.createDelegate(
								// this, [4])
								// }
								, {
									text : "Image into",
									handler : showImportPanel.createDelegate(
											this, [5])
								}]
					}
				}]
	});
	function rightClickFn(client, rowIndex, e) {
		e.preventDefault();
		rightMenu.showAt(e.getXY());
	}
	grid.on("rowcontextmenu", rightClickFn);

	// 右边隐藏的字段
	var hiddenItemsRight = new Ext.Panel({
				layout : 'form',
				hidden : true,
				items : [typeLv1Box, eleFlagBox, {
							xtype : "numberfield",
							fieldLabel : "套数",
							anchor : "100%",
							id : "eleUnitNum",
							name : "eleUnitNum",
							disabledClass : 'combo-disabled',
							tabIndex : 37,
							decimalPrecision : 0,
							maxValue : 1000000000
						}, typeLv2Box, {
							xtype : "numberfield",
							fieldLabel : "模具费用",
							anchor : "100%",
							tabIndex : 43,
							decimalPrecision : 3,
							id : "moldCharge",
							name : "moldCharge",
							maxValue : 99999.999
						}, {
							xtype : "textfield",
							fieldLabel : "所属年份",
							anchor : "100%",
							id : "eleTypenameLv2",
							name : "eleTypenameLv2",
							tabIndex : 44,
							maxLength : 20
						}, {
							xtype : "numberfield",
							fieldLabel : "容积",
							anchor : "100%",
							tabIndex : 45,
							id : "cube",
							name : "cube",
							decimalPrecision : 2,
							maxValue : 999999.99
						}, {
							xtype : "textarea",
							fieldLabel : "产品描述",
							anchor : "100%",
							tabIndex : 48,
							height : 40,
							id : "eleDesc",
							name : "eleDesc",
							maxLength : 500
						}, eleHsidBox, inputGridTypeIdBox, {
							xtype : "numberfield",
							fieldLabel : "中盒装数",
							anchor : "100%",
							tabIndex : 62,
							id : "boxMbCount",
							name : "boxMbCount",
							decimalPrecision : 0,
							maxValue : 1000000000
						}, boxMbTypeBox, {
							xtype : "numberfield",
							fieldLabel : "CUFT",
							anchor : "100%",
							tabIndex : 69,
							id : "boxCuft",
							name : "boxCuft",
							decimalPrecision : 4,
							maxValue : 999999999.999999,
							listeners : {
								"change" : calCbmCube
							}
						}, {
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
							tabIndex : 74,
							id : "putH",
							name : "putH",
							decimalPrecision : 2,
							maxValue : 999.99
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
							tabIndex : 80,
							id : "boxHInch",
							name : "boxHInch",
							decimalPrecision : 4,
							maxValue : 9999.9999
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
							tabIndex : 86,
							id : "boxPbHInch",
							name : "boxPbHInch",
							decimalPrecision : 2,
							maxValue : 999.99
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
							tabIndex : 92,
							id : "boxIbHInch",
							name : "boxIbHInch",
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
							fieldLabel : "INCH",
							anchor : "100%",
							labelSeparator : ":",
							tabIndex : 102,
							id : "boxObLInch",
							name : "boxObLInch",
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
						}, {
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
						}, {
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
							fieldLabel : "英文包装",
							anchor : "100%",
							height : 40,
							tabIndex : 109,
							id : "boxRemark",
							name : "boxRemark",
							maxLength : 500
						}, {
							xtype : 'textfield',
							fieldLabel : '平均价',
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "avgPrice",
							anchor : "98%",
							name : "avgPrice"
						}, {
							xtype : 'textfield',
							fieldLabel : '最低价',
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "minPrice",
							anchor : "98%",
							name : "minPrice"
						}, {
							xtype : 'textfield',
							fieldLabel : '最高价',
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "maxPrice",
							anchor : "98%",
							name : "maxPrice"
						}, {
							xtype : 'textfield',
							fieldLabel : '最新价',
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "newPrice",
							anchor : "95%",
							name : "newPrice"
						}, {
							xtype : 'textfield',
							fieldLabel : '原始外销价',
							disabled : true,
							disabledClass : 'combo-disabled',
							id : "oldPrice",
							anchor : "98%",
							name : "oldPrice"
						}]
			});

	// 右边折叠表单
	var rightForm = new Ext.form.FormPanel({
		autoScroll : true,
		border : false,
		padding : "5px",
		labelWidth : 90,
		layout : "form",
		labelAlign : "right",
		items : [{
			xtype : "panel",
			title : "",
			anchor : '95%',
			layout : "column",
			items : [{
				xtype : "panel",
				columnWidth : 0.4,
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
							+ '<img src="common/images/zwtp.png"  id="picPath" name="picPath"'
							+ ' onload="javascript:DrawImage(this,100,100)" onclick="showBigPicDiv(this)"/></div>'
				}],
				buttons : [{
					width : 40,
					text : "mod",
					handler : showUploadPanel,
					id : "upmod"
						// iconCls : "upload-icon"
					}, {
					width : 40,
					text : "del",
					handler : delPic,
					id : "updel"
						// iconCls : "upload-icon-del"
					}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.6,
				layout : "form",
				labelWidth : 80,
				items : [{
							xtype : "textfield",
							fieldLabel : "Art No.",
							anchor : "100%",
							id : "eleId",
							tabIndex : 41,
							name : "eleId"
						}, facBox, {
							xtype : "textfield",
							fieldLabel : "S.A  No.",
							anchor : "100%",
							id : "factoryNo",
							tabIndex : 43,
							name : "factoryNo"
						}, {
							xtype : "textfield",
							fieldLabel : "Cust.A No.",
							anchor : "100%",
							tabIndex : 44,
							id : "custNo",
							name : "custNo"
						}, {
							xtype : "panel",
							title : "",
							layout : "column",
							items : [{
								xtype : "panel",
								layout : "form",
								columnWidth : 1,
								items : [{
									xtype : "textfield",
									fieldLabel : "Barcode",
									anchor : "100%",
									id : "barcode",
									name : "barcode",
									tabIndex : 45,
									maxLength : 30,
									listeners : {
										"render" : function(obj) {
											var tip = new Ext.ToolTip({
												target : obj.getEl(),
												anchor : 'top',
												maxWidth : 160,
												minWidth : 160,
												html : 'Click the right button to automatically generate the Barcode!'
											});
										}
									}
								}]
							}, {
								xtype : "button",
								width : 20,
								text : "",
								cls : "SYSOP_ADD",
								iconCls : "barcode",
								handler : changeBarcode,
								listeners : {
									"render" : function(obj) {
										var tip = new Ext.ToolTip({
											target : obj.getEl(),
											anchor : 'top',
											maxWidth : 160,
											minWidth : 160,
											html : 'Depending on the configuration automatically generated Barcode!'
										});
									}
								}
							}]
						}, {
							xtype : "hidden",
							id : "id",
							name : "id"
						}, {
							xtype : "hidden",
							id : "rdm",
							name : "rdm"
						}]
			}]
		}, {
			xtype : "panel",
			title : "",
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "Unit",
									anchor : "100%",
									tabIndex : 46,
									id : "eleUnit",
									name : "eleUnit"
								}, sourceBox, {
									xtype : "numberfield",
									fieldLabel : "Weight(g)",
									anchor : "100%",
									tabIndex : 50,
									id : "boxWeigth",
									name : "boxWeigth"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "hidden",
									fieldLabel : "Level",
									anchor : "100%",
									tabIndex : 47,
									id : "eleGrade",
									name : "eleGrade"
								}, {
									xtype : "textfield",
									fieldLabel : "MOQ",
									anchor : "100%",
									tabIndex : 49,
									id : "eleMod",
									name : "eleMod"
								}, {
									xtype : "panel",
									title : "",
									layout : "column",
									items : [{
										xtype : "panel",
										title : "",
										columnWidth : 0.75,
										layout : "form",
										items : [{
													xtype : "numberfield",
													fieldLabel : "N.W/G.W(kgs)",
													anchor : "100%",
													tabIndex : 51,
													id : "boxNetWeigth",
													name : "boxNetWeigth"
												}]
									}, {
										xtype : "panel",
										title : "",
										columnWidth : 0.25,
										layout : "form",
										items : [{
													xtype : "numberfield",
													fieldLabel : "标签",
													anchor : "100%",
													hideLabel : true,
													tabIndex : 52,
													id : "boxGrossWeigth",
													name : "boxGrossWeigth"
												}]
									}]
								}]
					}]
		}, {
			xtype : "textarea",
			fieldLabel : "Description",
			anchor : "95%",
			height : 35,
			tabIndex : 53,
			id : "eleNameEn",
			name : "eleNameEn"
		}, {
			xtype : "textarea",
			fieldLabel : "Material",
			anchor : "95%",
			height : 35,
			tabIndex : 54,
			id : "eleName",
			name : "eleName"
		}, {
			xtype : "textarea",
			fieldLabel : "Design",
			anchor : "95%",
			height : 35,
			tabIndex : 55,
			id : "eleCol",
			name : "eleCol"
		}, {
			xtype : "textarea",
			fieldLabel : "Comment",
			anchor : "95%",
			height : 35,
			tabIndex : 56,
			id : "eleRemark",
			name : "eleRemark"
		}, {
			xtype : "textarea",
			fieldLabel : "Size Description",
			anchor : "95%",
			height : 35,
			tabIndex : 57,
			id : "eleInchDesc",
			name : "eleInchDesc"
		}, {
			xtype : "textarea",
			fieldLabel : "Sales Unit",
			anchor : "95%",
			height : 35,
			tabIndex : 58,
			id : "boxRemarkCn",
			name : "boxRemarkCn"
		}, {
			xtype : "panel",
			title : "",
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Product Size",
									anchor : "100%",
									tabIndex : 60,
									id : "boxL",
									name : "boxL"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									labelSeparator : " ",
									tabIndex : 61,
									id : "boxW",
									name : "boxW"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									labelSeparator : " ",
									tabIndex : 62,
									id : "boxH",
									name : "boxH"
								}]
					}]
		}, boxPacking, {
			xtype : "panel",
			title : "",
			anchor : '95%',
			hidden : true,
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Packing",
									anchor : "100%",
									tabIndex : 64,
									id : "boxPbCount",
									name : "boxPbCount"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [boxPbTypeBox]
					}]
		}, {
			xtype : "panel",
			title : "",
			hidden : true,
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Packing Size",
									anchor : "100%",
									tabIndex : 66,
									id : "boxPbL",
									name : "boxPbL"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									hideLabel : false,
									labelSeparator : " ",
									tabIndex : 67,
									id : "boxPbW",
									name : "boxPbW"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									labelSeparator : " ",
									tabIndex : 68,
									id : "boxPbH",
									name : "boxPbH"
								}]
					}]
		}, {
			xtype : "panel",
			title : "",
			hidden : true,
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Inner Packing",
									anchor : "100%",
									tabIndex : 69,
									id : "boxIbCount",
									name : "boxIbCount"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [boxIbTypeBox]
					}]
		}, {
			xtype : "panel",
			title : "",
			hidden : true,
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Inner Size",
									anchor : "100%",
									tabIndex : 71,
									id : "boxIbL",
									name : "boxIbL"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									labelSeparator : " ",
									tabIndex : 72,
									id : "boxIbW",
									name : "boxIbW"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									labelSeparator : " ",
									tabIndex : 73,
									id : "boxIbH",
									name : "boxIbH"
								}]
					}]
		}, {
			xtype : "panel",
			title : "",
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Pcs/Ctn",
									anchor : "100%",
									tabIndex : 74,
									id : "boxObCount",
									name : "boxObCount"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [boxObTypeBox]
					}]
		}, {
			xtype : "panel",
			title : "",
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "Carton Size",
									anchor : "100%",
									tabIndex : 76,
									id : "boxObL",
									name : "boxObL"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									labelSeparator : " ",
									tabIndex : 77,
									id : "boxObW",
									name : "boxObW"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.25,
						layout : "form",
						labelWidth : 10,
						items : [{
									xtype : "numberfield",
									fieldLabel : "*",
									anchor : "100%",
									labelSeparator : " ",
									tabIndex : 78,
									id : "boxObH",
									name : "boxObH"
								}]
					}]
		}, {
			xtype : "panel",
			title : "",
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "20' Container",
									anchor : "100%",
									tabIndex : 79,
									id : "box20Count",
									name : "box20Count"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 100,
						items : [{
									xtype : "numberfield",
									fieldLabel : "40' Container",
									anchor : "100%",
									tabIndex : 80,
									id : "box40Count",
									name : "box40Count"
								}]
					}]
		}, {
			xtype : "panel",
			title : "",
			anchor : '95%',
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						items : [{
									xtype : "numberfield",
									fieldLabel : "45' Container",
									anchor : "100%",
									tabIndex : 81,
									id : "box45Count",
									name : "box45Count"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.5,
						layout : "form",
						labelWidth : 100,
						items : [{
									xtype : "numberfield",
									fieldLabel : "40 HC Container",
									anchor : "100%",
									tabIndex : 82,
									id : "box40hqCount",
									name : "box40hqCount"
								}]
					}]
		}, hiddenItemsRight]
	});

	// 右边折叠面板
	var rightPanel = new Ext.Panel({
		title : 'DETAIL',
		layout : 'fit',
		frame : true,
		border : false,
		region : 'east',
		width : "30%",
		collapsible : true,
		collapsed : true,
		listeners : {
			'beforeadd' : function(pnl, component, index) {
				pnl.getEl().mask("loading...", 'x-mask-loading');
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
							// 更改表格的退税率,和单价和利润率
							if (txt.getName() == 'eleHsid') {
								if (newVal != '') {
									setTuiLv(newVal);
								}
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

	// 组成方式
	var sortStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, '升序'], [1, '降序']]
			});
	var sortComb = new Ext.form.ComboBox({
				name : 'sortSel',
				tabIndex : 4,
				fieldLabel : '方式',
				emptyText : 'Choose',
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

	// 隐藏的字段
	var hiddenItems = new Ext.Panel({
				layout : 'form',
				hidden : true,
				items : [{
							xtype : "numberfield",
							fieldLabel : "FOB费用",
							anchor : "100%",
							id : "priceFob",
							name : "priceFob",
							maxValue : 999999.99,
							tabIndex : 14,
							allowDecimals : false,
							allowBlank : true,
							listeners : {
								"change" : function(txt, newVal, oldVal) {
									_self.priceBlur();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "海运费",
							anchor : "100%",
							id : "priceCharge",
							name : "priceCharge",
							maxValue : 999999.99,
							tabIndex : 15,
							allowBlank : true,
							listeners : {
								"change" : function(txt, newVal, oldVal) {
									_self.priceBlur();
								}
							}
						}, commisionBox, {
							xtype : "numberfield",
							fieldLabel : "比例%",
							anchor : "100%",
							id : "commisionScale",
							name : "commisionScale",
							tabIndex : 17,
							maxValue : 999.99,
							allowBlank : true,
							listeners : {
								"change" : commisionScaleChange
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "保险费‰",
							anchor : "100%",
							id : "insureRate",
							name : "insureRate",
							tabIndex : 18,
							maxValue : 999.99,
							allowBlank : true,
							listeners : {
								"change" : function(txt, newVal, oldVal) {
									_self.priceBlur();
								}
							}
						}, {
							xtype : "numberfield",
							fieldLabel : "加成率%",
							anchor : "100%",
							id : "insureAddRate",
							name : "insureAddRate",
							tabIndex : 19,
							maxValue : 999.99,
							allowBlank : true,
							listeners : {
								"change" : function(txt, newVal, oldVal) {
									_self.priceBlur();
								}
							}
						}, shenBox, {
							xtype : "textfield",
							fieldLabel : "条款组合",
							anchor : "100%",
							id : "priceCompose",
							name : "priceCompose",
							maxLength : 100,
							tabIndex : 22,
							allowBlank : true
						}, {
							xtype : "textfield",
							fieldLabel : "信用证号",
							anchor : "100%",
							id : "creditNo",
							name : "creditNo",
							tabIndex : 25,
							maxLength : 100,
							allowBlank : true
						}, insureContractBox, payTypeBox]
			});

	// 判断单号是否重复参数
	var isExist = true;
	var priceForm = new Ext.form.FormPanel({
		title : "Base Information-(Red is required)",
		labelWidth : 100,
		collapsible : true,
		formId : "priceForm",
		labelAlign : "right",
		region : 'north',
		layout : "column",
		width : "100%",
		height : 160,
		// frame : true,
		border : false,
		bodyStyle : 'background:#dfe8f6',
		ctCls : 'x-panel-mc',
		padding : 8,
		items : [{
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					items : [custBox, {
								xtype : "datefield",
								fieldLabel : "Date",
								anchor : "100%",
								id : "priceTime",
								value : new Date(),
								name : "priceTime",
								tabIndex : 6,
								format : "Y-m-d H:i:s",
								listeners : {
									'select' : function(field, date) {
										var dt = new Date(date);
										var date = dt.format('Y-m-d');
										var dt2 = new Date();
										var time = dt2.format('H:i:s');
										field.setValue(date + " " + time);
									}
								}
							}, shipPortBox]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 130,
					items : [companyBox, {
						xtype : 'panel',
						layout : 'column',
						items : [{
									xtype : "panel",
									columnWidth : 0.8,
									layout : "form",
									items : [curBox]
								}, {
									xtype : "panel",
									columnWidth : 0.2,
									layout : "form",
									labelWidth : 30,
									items : [{
										xtype : "numberfield",
										fieldLabel : "",
										hideLabel : true,
										anchor : "100%",
										id : 'priceRate',
										name : 'priceRate',
										tabIndex : 8,
										listeners : {
											"change" : function(txt, newVal,
													oldVal) {
												_self.priceBlur();
											}
										}
									}]
								}]
					},targetPortBox]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 120,
					items : [{
						xtype : "panel",
						layout : "column",
						columnWidth : 1,
						items : [{
							xtype : "panel",
							layout : "form",
							columnWidth : 1,
							items : [{
								xtype : "textfield",
								fieldLabel : "<font color=red>Offer No.</font>",
								anchor : "100%",
								id : "priceNo",
								tabIndex : 3,
								blankText : "Please enter the quotation number！",
								name : "priceNo",
								maxLength : 100,
								allowBlank : false,
								// readOnly:true,
								invalidText : "Quotation number already exists, please re-enter！",
								validationEvent : 'change',
								validator : function(thisText) {
									if (thisText != '') {
										cotPriceService.findIsExistPriceNo(
												thisText, $('pId').value,
												function(res) {
													if (res) {
														isExist = false;
													} else {
														isExist = true;
													}
												});
									}
									return isExist;
								},
								listeners : {
									"render" : function(obj) {
										var tip = new Ext.ToolTip({
											target : obj.getEl(),
											anchor : 'top',
											maxWidth : 160,
											minWidth : 160,
											html : 'Click the right button to automatically generate purchase order number!'
										});
									}
								}
							}]
						}, {
							xtype : "button",
							width : 20,
							text : "",
							cls : "SYSOP_ADD",
							iconCls : "cal",
							handler : getPriceNo,
							listeners : {
								"render" : function(obj) {
									var tip = new Ext.ToolTip({
										target : obj.getEl(),
										anchor : 'top',
										maxWidth : 160,
										minWidth : 160,
										html : 'No configuration automatically generated based on the single quotation No.!'
									});
								}
							}
						}]
					}, clauseBox,{
								xtype : "numberfield",
								// fieldLabel : "Production Cycle",
								fieldLabel : "Production Time",
								anchor : "100%",
								id : 'prodPeriod',
								name : 'prodPeriod',
								tabIndex : 13
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth:70,
					items : [contactBox, {
								xtype : "numberfield",
								fieldLabel : "Valid Date",
								anchor : "100%",
								id : 'validMonths',
								name : 'validMonths',
								tabIndex : 10
							}, {
								xtype : "hidden",
								fieldLabel : "Disscount(%)",
								anchor : "100%",
								id : 'zheNum',
								name : 'zheNum',
								tabIndex : 11
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth:50,
					items : [empBox, containerBox, trafficBox]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 1,
					layout : "form",
					items : [{
								xtype : "textarea",
								fieldLabel : "Remark",
								anchor : "100%",
								height : 35,
								id : 'priceRemark',
								name : 'priceRemark',
								tabIndex : 14
							}]
				}, hiddenItems]
	});

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "Quote Details",
				bodyStyle : 'background:#dfe8f6',
				items : [grid, rightPanel]
			});

	// 底部标签页
	var tbl = new Ext.TabPanel({
				region : 'south',
				region : 'center',
				width : "100%",
				activeTab : 0,
				items : [centerPanel
				// {
				// id : "shenTab",
				// name : "shenTab",
				// title : "Audit records",
				// layout : 'fit',
				// items : [{
				// xtype : 'htmleditor',// html文本编辑器控件
				// enableLinks : false,
				// id : 'checkReason',
				// name : 'checkReason',
				// listeners : {
				// 'afterrender' : function(area) {
				// if ($('pId').value != ''
				// && $('pId').value != 'null') {
				// area.setValue(chkReason);
				// }
				// }
				// }
				// }]
				// }
				],
				buttonAlign : 'center',
				buttons : [{
							text : "Save",
							cls : "SYSOP_ADD",
							iconCls : "page_table_save",
							handler : save
						}, {
							text : "Delete",
							iconCls : "page_del",
							hidden : true,
							cls : "SYSOP_DEL",
							handler : del
						}, {
							text : "Reorder",
							iconCls : "page_fen",
							hidden : true,
							handler : showSort
						}, {
							text : "Print",
							iconCls : "page_print",
							cls : "SYSOP_PRINT",
							handler : showPrint
						}, {
							text : "Save As",
							iconCls : "page_from",
							id : 'saveAsBtn',
							cls : "SYSOP_ADD",
							handler : showModel
						}, {
							text : "Cancel",
							iconCls : "page_cancel",
							handler : function() {
								closeandreflashEC(true, 'priceGrid', false);
							}
						}, {
							text : "Need to be reviewed",
							id : 'qingBtn',
							hidden : true,
							iconCls : "page_from",
							handler : qingShen
						}, {
							text : "reviewed",
							id : 'guoBtn',
							hidden : true,
							iconCls : "page_check",
							handler : guoShen
						}, {
							text : "Review without",
							id : 'buBtn',
							hidden : true,
							iconCls : "page_check_error",
							handler : buShen
						}, {
							text : "review again",
							id : 'fanxBtn',
							hidden : true,
							iconCls : "page_from",
							handler : fanShen
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [tbl, priceForm]
			});
	viewport.doLayout();

	// 加载表格数据
	ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});

	// -------------------------------------方法--------------------------------------------------

	// 原价格参数
	var oldPriceMap = {};
	// 单样品利润率
	var liRunCau = '';
	// 审核原因
	var chkReason = "";
	// 样品默认配置,是否自动生成产品包装尺寸
	var sizeFollow = 0;
	// 初始化
	function initform() {
		DWREngine.setAsync(false);
		// 清空Map
		// cotPriceService.clearMap("price", function(res) {
		// });
		// 加载集装箱的柜体积
		queryService.getContainerCube(function(res) {
					$('con20').value = res[0];
					$('con40').value = res[1];
					$('con40H').value = res[2];
					$('con45').value = res[3];
				});
		cotElementsService.getDefaultList(function(res) {

					if (res.length != 0) {
						// 设置是否自动生成包装尺寸标识
						if (res[0].sizeFollow != null) {
							sizeFollow = res[0].sizeFollow;
						}
					}
				});
		// 报价单编号
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			// Ext.getCmp('guoBtn').hide();
			// Ext.getCmp('buBtn').hide();
			// Ext.getCmp('fanxBtn').hide();
			// Ext.getCmp('qingBtn').hide();
			Ext.getCmp('saveAsBtn').hide();
			// 隐藏审核原因
			// Ext.getCmp('shenTab').disable();
			// 初始化币种和价格条款和起运港的默认值
			cotPriceService.getList('CotPriceCfg', function(res) {
				if (res.length != 0) {
					shipPortBox.bindPageValue("CotShipPort", "id",
							res[0].shipPortId);
					clauseBox.bindValue(res[0].caluseId);
					if (res[0].currencyId != null) {
						curBox.bindValue(res[0].currencyId);
						$('oldCur').value = res[0].currencyId;
						// 变更汇率
						sysdicutil.getDicListByName('currency', function(cl) {
									for (var i = 0; i < cl.length; i++) {
										if (cl[i].id == res[0].currencyId) {
											$('priceRate').value = cl[i].curRate;
											break;
										}
									}
								});
					}
					if (res[0].noPrice != null) {
						$('noPrice').value = res[0].noPrice;
					} else {
						$('noPrice').value = 0;
					}
					if (res[0].havePrice != null) {
						$('havePrice').value = res[0].havePrice;
					} else {
						$('havePrice').value = 0;
					}
					if (res[0].priceFob != null) {
						$('priceFob').value = res[0].priceFob;
					}
					// if (res[0].priceProfit != null) {
					// $('priceProfit').value = res[0].priceProfit;
					// }
					containerBox.bindValue(res[0].containerTypeId);
					if (res[0].insureAddRate != null) {
						$('insureAddRate').value = res[0].insureAddRate;
					}
					if (res[0].insureRate != null) {
						$('insureRate').value = res[0].insureRate;
					}
					trafficBox.bindValue(res[0].trafficTypeId);
					companyBox.bindPageValue("CotCompany", "id",
							res[0].companyId);

					if (res[0].insertType != null) {
						$('insertType').value = res[0].insertType;
					} else {
						$('insertType').value = 0;
					}
					// 业务配置中是否审核(0不审核,1审核)
					if (res[0].isCheck == 0) {
						shenBox.setValue(9);
					} else {
						shenBox.setValue(0);
					}
				}
			});
			if ($('cusId').value != 'null' && $('cusId').value != '') {
				custBox.bindPageValue("CotCustomer", "id", $('cusId').value);
			}
			if ($('busiId').value != 'null' && $('busiId').value != '') {
				empBox.bindPageValue("CotEmps", "id", $('busiId').value);
			}
		} else {
			// 初始报价配置值
			cotPriceService.getList('CotPriceCfg', function(res) {
						if (res.length != 0) {
							if (res[0].noPrice != null) {
								$('noPrice').value = res[0].noPrice;
							} else {
								$('noPrice').value = 0;
							}
							if (res[0].havePrice != null) {
								$('havePrice').value = res[0].havePrice;
							} else {
								$('havePrice').value = 0;
							}
							if (res[0].insertType != null) {
								$('insertType').value = res[0].insertType;
							} else {
								$('insertType').value = 0;
							}

							// 业务配置中是否审核(0不审核,1审核)
							if (res[0].isCheck == 0) {
								// Ext.getCmp('guoBtn').hide();
								// Ext.getCmp('buBtn').hide();
								// Ext.getCmp('fanxBtn').hide();
								// Ext.getCmp('qingBtn').hide();
								// Ext.getCmp('shenTab').disable();
							}
						} else {
							// Ext.getCmp('qingBtn').hide();
							// Ext.getCmp('shenTab').disable();
						}
					});
			// 加载报价单信息
			cotPriceService.getPriceById(parseInt(id), function(res) {
				Ext.getCmp("priceTime").setValue(res.priceTime);
				chkReason = res.checkReason;
				DWRUtil.setValues(res);
				// 绑定下拉框
				custBox.bindPageValue("CotCustomer", "id", res.custId);
				empBox.bindPageValue("CotEmps", "id", res.businessPerson);
				curBox.bindValue(res.currencyId);
				clauseBox.bindValue(res.clauseId);
				shipPortBox.bindPageValue("CotShipPort", "id", res.shipportId);
				targetPortBox.bindPageValue("CotTargetPort", "id",
						res.targetportId);
				containerBox.bindValue(res.containerTypeId);
				trafficBox.bindValue(res.trafficId);
				companyBox.bindPageValue("CotCompany", "id", res.companyId);

				commisionBox.bindValue(res.commisionTypeId);
				insureContractBox.bindValue(res.insureContractId);
				payTypeBox.bindValue(res.payTypeId);
				shenBox.setValue(res.priceStatus);
				contactBox.loadValueById('CotCustContact', 'customerId',
						res.custId, res.contactId);

					// if ($('priceStatus').value == 0 || $('priceStatus').value
					// == 2
					// || $('priceStatus').value == 1) {
					// Ext.getCmp('guoBtn').hide();
					// Ext.getCmp('buBtn').hide();
					// }
					// if ($('priceStatus').value == 2) {
					// Ext.getCmp('qingBtn').hide();
					// }
					// if ($('priceStatus').value == 0 || $('priceStatus').value
					// == 1) {
					// Ext.getCmp('fanxBtn').hide();
					// }
					// if ($('priceStatus').value == 3) {
					// Ext.getCmp('fanxBtn').hide();
					// Ext.getCmp('qingBtn').hide();
					// }
					// if ($('priceStatus').value == 9) {
					// Ext.getCmp('guoBtn').hide();
					// Ext.getCmp('buBtn').hide();
					// Ext.getCmp('fanxBtn').hide();
					// }
				});
			oldPriceMap.currencyId = $('currencyId').value;// 币种
			$('oldCur').value = $('currencyId').value;
			// oldPriceMap.clauseId = $('clauseId').value;// 价格条款
			oldPriceMap.clauseId = 1;// 价格条款
			// oldPriceMap.priceProfit = $('priceProfit').value;// 利润率
			oldPriceMap.priceFob = $('priceFob').value;// FOB
			oldPriceMap.priceCharge = $('priceCharge').value;// 整柜运费
			oldPriceMap.insureRate = $('insureRate').value;// 保险费率
			oldPriceMap.insureAddRate = $('insureAddRate').value;// 保险加成率
			oldPriceMap.containerTypeId = $('containerTypeId').value;// 集装箱类型
		}
		// 得到单样品利润公式
		cotPriceService.getLiRunCau(function(cau) {
					liRunCau = cau;
				});
		DWREngine.setAsync(true);
	}
	unmask();
	initform();
	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list, flag) {
		if (!flag) {
			// 删除该货号所在行
			ds.remove(editRec);
		}
		var eleAry = new Array();
		Ext.each(list, function(item) {
					eleAry.push(item.data.eleId);
				});
		var noPrice = $('noPrice').value;
		var havePrice = $('havePrice').value;
		var currencyId = $('currencyId').value;
		var clauseId = $('clauseId').value;
		var priceTime = $('priceTime').value;
		for (var i = 0; i < eleAry.length; i++) {
			// 判断该产品是否对该用户报价过
			DWREngine.setAsync(false);
			cotPriceService.findIsExistDetail(eleAry[i], $('custId').value,
					clauseId, priceTime, function(rtn) {
						// 取得随机数
						var rdm = getRandom();
						if (rtn != null) {
							var result = rtn[1];
							if (rtn[0] == 1) {
								result.type = 'price';
							} else if (rtn[0] == 2) {
								result.pricePrice = result.orderPrice;
								result.type = 'order';
							} else if (rtn[0] == 3) {
								result.pricePrice = result.priceOut;
								result.currencyId = result.priceOutUint;
								result.type = 'given';
							}
							result.srcId = result.id;
							result.id = null;
							result.rdm = rdm;
							// 取对外报价
							if (havePrice == 0) {
								// 判断该产品货号是否存在,有存在的话转成报价明细
								DWREngine.setAsync(false);
								cotPriceService.changeEleToPriceDetail(
										eleAry[i], function(res) {
											if (res != null) {
												res.type = 'ele';
												res.srcId = res.id;
												res.id = null;
												// 重新换算价格(币种不同时)
												DWREngine.setAsync(false);
												queryService.updatePrice(
														res.priceOut,
														res.priceOutUint,
														currencyId, function(
																newPri) {
															res.pricePrice = newPri
																	.toFixed(deNum);
															res.currencyId = currencyId;
															res.rdm = rdm;

															insertToGrid(res,
																	false);

														});
												DWREngine.setAsync(true);
											}
										});
								DWREngine.setAsync(true);

							}
							// 按价格条款计算
							if (havePrice == 1) {
								result.currencyId = currencyId;
								insertToGrid(result, false);
							}
							// 取最近一次报价
							if (havePrice == 2) {
								// 重新换算价格(币种不同时)
								DWREngine.setAsync(false);
								queryService.updatePrice(result.pricePrice,
										result.currencyId, currencyId,
										function(newPri) {
											result.pricePrice = newPri
													.toFixed(deNum);
											result.currencyId = currencyId;
											insertToGrid(result, false);
										});
								DWREngine.setAsync(true);
							}
						} else {
							// 判断该产品货号是否存在,有存在的话转成报价明细
							DWREngine.setAsync(false);
							cotPriceService.changeEleToPriceDetail(eleAry[i],
									function(res) {
										if (res != null) {
											res.type = 'ele';
											res.srcId = res.id;
											res.id = null;
											res.rdm = rdm;
											// 取对外报价
											if (noPrice == 0) {
												// 重新换算价格(币种不同时)
												queryService.updatePrice(
														res.priceOut,
														res.priceOutUint,
														currencyId, function(
																newPri) {
															res.pricePrice = newPri
																	.toFixed(deNum);
															res.currencyId = currencyId;
															insertToGrid(res,
																	false);
														});
											}
											// 按价格条款计算
											if (noPrice == 1) {
												res.currencyId = currencyId;
												insertToGrid(res, true);
											}
										}
									});
							DWREngine.setAsync(true);
						}
					});
			DWREngine.setAsync(true);
		}
		unmask();
	}

	// 获得表格选择行
	function getIds() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 打开编辑页面
	function windowopenMod(obj) {
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.MessageBox.alert("Message", "Please select a record");
				return;
			} else if (ids.length > 1) {
				Ext.MessageBox.alert("Message", "Choose only one record!")
				return;
			} else
				obj = ids[0];

		}
		openWindowBase(555, 800, 'cotprice.do?method=addPrice&id=' + obj);
	}

	// 编辑页面时判断是否有更改价格参数
	function checkIsChangePice() {
		var flag = false;
		// 价格条款
		if (oldPriceMap.clauseId != $('clauseId').value) {
			oldPriceMap.clauseId = $('clauseId').value;
			flag = true;
		}
		// 币种
		if (oldPriceMap.currencyId != $('currencyId').value) {
			oldPriceMap.currencyId = $('currencyId').value;
			flag = true;
		}
		// // 利润率
		// if (oldPriceMap.priceProfit != $('priceProfit').value) {
		// oldPriceMap.priceProfit = $('priceProfit').value;
		// flag = true;
		// }
		// FOB
		if (oldPriceMap.priceFob != $('priceFob').value) {
			oldPriceMap.priceFob = $('priceFob').value;
			flag = true;
		}
		// 整柜运费
		if (oldPriceMap.priceCharge != $('priceCharge').value) {
			oldPriceMap.priceCharge = $('priceCharge').value;
			flag = true;
		}
		// 保险费率
		if (oldPriceMap.insureRate != $('insureRate').value) {
			oldPriceMap.insureRate = $('insureRate').value;
			flag = true;
		}
		// 保险加成率
		if (oldPriceMap.insureAddRate != $('insureAddRate').value) {
			oldPriceMap.insureAddRate = $('insureAddRate').value;
			flag = true;
		}
		// 集装箱类型
		if (oldPriceMap.containerTypeId != $('containerTypeId').value) {
			oldPriceMap.containerTypeId = $('containerTypeId').value;
			flag = true;
		}
		return flag;
	}

	// 保存
	function save() {
		var popedom = checkAddMod($('pId').value);
		if (popedom == 1) {
			Ext.Msg.alert("Message", 'Sorry, you do not have Authority! ');
			return;
		} else if (popedom == 2) {
			Ext.Msg.alert("Message", 'Sorry, you do not have Authority!');
			return;
		}
		// 审核通过不让修改
		if ($('priceStatus').value == 2 && loginEmpId != "admin") {
			Ext.Msg.alert("Message", 'Sorry, you do not have Authority!');
			return;
		}

		// 验证表单
		var formData = getFormValues(priceForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 验证单号是否存在
		var shortflag = false;
		var priceNo = $('priceNo').value;
		DWREngine.setAsync(false);
		cotPriceService.findIsExistPriceNo(priceNo, $('pId').value, function(
						res) {
					if (res != null) {
						shortflag = true;
					}
				});
		if (shortflag) {
			Ext.MessageBox
					.alert("Tip Box",
							"The purchase order number already exists, please re-enter！");
			return;
		}
		DWREngine.setAsync(true);

		Ext.MessageBox.confirm('Message', 'Do you want to save the quotation？',
				function(btn) {
					if (btn == 'yes') {
						var price = DWRUtil.getValues('priceForm');
						var cotPrice = {};
						var checkFlag = false;
						// 如果编号存在时先查询出对象,再填充表单
						if ($('pId').value != 'null' && $('pId').value != '') {
							DWREngine.setAsync(false);
							cotPriceService.getPriceById($('pId').value,
									function(res) {
										for (var p in price) {
											if (p != 'priceTime') {
												res[p] = price[p];
											}
										}
										cotPrice = res;
									});
							DWREngine.setAsync(true);
							checkFlag = checkIsChangePice();
						} else {
							cotPrice = new CotPrice();
							for (var p in cotPrice) {
								if (p != 'priceTime') {
									cotPrice[p] = price[p];
								}
							}
						}
						DWREngine.setAsync(false);
						// 审核原因
						// if (Ext.getCmp("checkReason").isVisible()) {
						// cotPrice.checkReason = $('checkReason').value;
						// }
						cotPriceService.saveOrUpdatePrice(cotPrice,
								$('priceTime').value, checkFlag, oldPriceMap,
								liRunCau, function(res) {
									if (res != null) {
										// 当新建时,如果默认配置需要审核,保存后显示"提请审核"按钮
										if ($('pId').value == 'null') {
											// if ($('priceStatus').value == 0)
											// {
											// Ext.getCmp("qingBtn").show();
											// }
											Ext.getCmp('saveAsBtn').show();
										}
										$('pId').value = res;
										// 更改添加action参数
										var urlAdd = '&pricePrimId=' + res
												+ '&currencyId='
												+ $('currencyId').value
												+ '&custId='
												+ $('custId').value;
										// 更改修改action参数
										var urlMod = '&custId='
												+ $('custId').value
												+ '&currencyId='
												+ $('currencyId').value;
										ds.proxy.setApi({
											read : "cotprice.do?method=queryPriceDetail&priceId="
													+ res,
											create : "cotprice.do?method=add"
													+ urlAdd,
											update : "cotprice.do?method=modify"
													+ urlMod,
											destroy : "cotprice.do?method=remove"
										});
										ds.save();
										reflashParent('priceGrid');
										Ext.Msg.alert("Message",
												"Successfully saved！");
									} else {
										Ext.MessageBox.alert('Message',
												'Save failed');
										unmask();
									}
								});
						DWREngine.setAsync(true);
					}
				});
	}

	// 删除
	function del() {
		// 如果该单没保存直接删除
		if ($('pId').value == 'null' || $('pId').value == '') {
			closeandreflashEC(true, 'priceGrid', false);
			return;
		}

		if ($('priceStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message',
					'Sorry, this one has been audited can not be deleted!');
			return;
		}
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {// 没有删除权限
			Ext.MessageBox.alert('Message',
					"You do not have permission to delete");
			return;
		}
		var list = new Array();
		list.push($('pId').value);
		Ext.MessageBox.confirm('Message',
				'Are you sure you remove the quotation sheets？', function(btn) {
					if (btn == 'yes') {
						DWREngine.setAsync(false);
						cotPriceService.deletePrices(list, function(res) {
							if (res) {
								Ext.MessageBox.alert('Message', "删除成功!");
								closeandreflashEC(true, 'priceGrid', false);
							} else {
								Ext.MessageBox
										.alert('Message',
												"Delete failed, the quote has been used !");
							}
						})
						DWREngine.setAsync(true);
					}
				});
	}

	// 显示导入界面
	function showImportPanel(type) {
		if ($('priceStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message',
					'orry, the single has been reviewed can not be modified!');
			return;
		}
		// 验证表单
		var formData = getFormValues(priceForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var cfg = {};
		cfg.custId = custBox.getValue();
		cfg.currencyId = curBox.getValue();
		cfg.bar = _self;
		cfg.type = 'price';
		cfg.no = $('priceNo').value;

		// 如果只勾选了一个货号,查询该货号的子货号
		var rec = sm.getSelections();
		if (rec.length == 1) {
			cfg.parentEleId = rec[0].data.eleId;
		}
		// 新建的单据不能图片导入
		var id = $('pId').value;
		if (type == 5 && (id == '' || id == 'null')) {
			Ext.MessageBox.alert('Message',
					'The single has not saved, you can not import pictures!');
			return;
		}

		// 图片导入需要pId
		if (id != '' && id != 'null') {
			cfg.pId = id;
		} else {
			cfg.pId = 0;
		}

		var importPanel = new ImportPanel(cfg);
		importPanel.show();
		importPanel.openPnl(type);
	}

	// 显示配件及成本界面
	function showFitPricePanel() {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "只能选择一条记录!")
			return;
		}
		var rdm = sm.getSelected().get("rdm");
		if (rdm == "") {
			Ext.MessageBox.alert("Message", "请输入一个货号!")
			return;
		}
		var cfg = {};
		cfg.custId = ids[0];
		cfg.isHId = sm.getSelected().get("id");
		cfg.eleId = sm.getSelected().get("eleId");
		cfg.eleName = sm.getSelected().get("eleName");
		cfg.pId = $('pId').value;
		cfg.bar = _self;
		cfg.rdm = rdm;
		cfg.reflashFn = updatePriceFacWhenElePriceChange;
		// 获得生产价币种
		cfg.facCur = sm.getSelected().get("priceFacUint");
		cfg.priceStatus = $('priceStatus').value
		var fitPricePanel = new FitPricePanel(cfg);
		fitPricePanel.show();
		fitPricePanel.openPnl(0);
	}

	// 反算利润率
	function calLiRun() {
		if ($('priceStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message',
					'Sorry, the single has been reviewed can not be modified!');
			return;
		}
		if (liRunCau == "") {
			Ext.MessageBox
					.alert("Message",
							"you do not offer the formula in the inverse configuration margin formula!");
			return;
		}
		var list = sm.getSelections();
		var res = new Array();
		// 将计算单价的参数组成一个map
		var mapPrice = {};
		mapPrice.currencyId = $('currencyId').value;// 币种
		// mapPrice.priceProfit = $('priceProfit').value;// 利润率
		mapPrice.priceFob = $('priceFob').value;// FOB
		mapPrice.priceRate = $('priceRate').value;// 汇率
		mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
		mapPrice.insureRate = $('insureRate').value;// 保险费率
		mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
		mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
		Ext.each(list, function(item) {
					res.push(item.data.rdm);
				});
		if (res.length == 0) {
			Ext.MessageBox.alert("Message",
					"Please select inverse margin price quotation!");
			return;
		}
		// 后台统一计算出所有价格.再来页面更改
		cotPriceService.calLiRun(mapPrice, res, function(map) {
					Ext.each(list, function(item) {
								var lirun = map[item.data.rdm];
								if (lirun != null) {
									item.set('liRun', lirun);
								} else {
									item.set('liRun', 0);
								}
							});
					Ext.MessageBox.alert("Message", "Complete inverse margin!");
				});
	}

	// 当配件或成本改变后修改对应的价格
	function updatePriceFacWhenElePriceChange() {
		var isHId = sm.getSelected().get("id");
		var rdm = sm.getSelected().get("rdm");
		// var ck = sm.getSelected().get("checkFlag");
		var liRun = sm.getSelected().get("liRun");
		// 将计算单价的参数组成一个map
		var mapPrice = {};
		// if (ck == null || ck == 0 || ck == false) {
		// mapPrice.clauseId = $('clauseId').value;// 价格条款
		mapPrice.clauseId = 1;// 价格条款
		mapPrice.currencyId = $('currencyId').value;// 币种
		mapPrice.priceProfit = liRun;// 利润率
		mapPrice.priceFob = $('priceFob').value;// FOB
		mapPrice.priceRate = $('priceRate').value;// 汇率
		mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
		mapPrice.insureRate = $('insureRate').value;// 保险费率
		mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
		mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
		// } else {
		// mapPrice = null;
		// }
		// 获取修改后的价格
		DWREngine.setAsync(false);
		cotPriceService.updatePriceFac(isHId, rdm, mapPrice, function(detail) {
					if (detail != null) {
						sm.getSelected().set("pricePrice", detail.pricePrice);
						sm.getSelected().set("priceFac", detail.priceFac);
					}
				});
		DWREngine.setAsync(true);
	}
	function showHisPricePanel() {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		}
		var hispriceGrid = new HisPriceGrid({
					custId : Ext.getDom("custId").value,
					eleId : sm.getSelected().get("eleId"),
					dblFn : replacePrice
				});
		var win = new Ext.Window({
					id : "hisWin",
					title : "Price History",
					width : 800,
					height : 500,
					modal : true,
					layout : 'fit',
					items : [hispriceGrid]
				})
		win.show();
		hispriceGrid.load();
	}

	function showModPriceWin() {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		}
		var rec = sm.getSelections();

		var cfg = {};
		cfg.eleId = rec[0].data.eleId;
		cfg.custId = $('custId').value;
		// 获得主单币种
		cfg.facCur = $('currencyId').value;
		var win = new ModPriceWin(cfg);
		win.show();
	}

	// 获得没勾选单价复选框的行的产品货号字符串
	function getNoCheckIds() {
		var rdmIds = '';
		ds.each(function(rec) {
			// var ck = rec.data.checkFlag;
			// if (ck == null || ck == 0 || ck == false) {
			if (rec.data.rdm != null && rec.data.rdm != "") {
				rdmIds += rec.data.rdm + ",";
			}
				// }
			});
		return rdmIds;
	}

	// 失去焦点事件
	this.priceBlur = function() {
		// 得到没勾选的表格行并且货号不是空字符串的记录
		var rdmIds = getNoCheckIds();
		if (rdmIds == '') {
			return;
		}
		// 判断是否选择价格条款
		if ($('clauseId').value == '') {
			return;
		}
		// 如果选择的是CIF和DDP,按照FOB算
		// 将计算单价的参数组成一个map
		var mapPrice = {};
		// mapPrice.clauseId = $('clauseId').value;// 价格条款
		mapPrice.clauseId = 1;// 价格条款
		mapPrice.currencyId = $('currencyId').value;// 币种
		// mapPrice.priceProfit = $('priceProfit').value;// 利润率
		mapPrice.priceFob = $('priceFob').value;// FOB
		mapPrice.priceRate = $('priceRate').value;// 汇率
		mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
		mapPrice.insureRate = $('insureRate').value;// 保险费率
		mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
		mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
		// 传递主单编号,有值时从报价明细中取,否则从样品表取
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			id = 0;
		}
		cotPriceService.getNewPrice(rdmIds, mapPrice, liRunCau, function(res) {
					if (res != null) {
						ds.each(function(rec) {
							// var ck = rec.data.checkFlag;
							// if (ck == null || ck == 0 || ck == false) {
							var rdm = rec.data.rdm;
							if (rdm != null && rdm != "") {
								rec.set("pricePrice", res[rdm][0]
												.toFixed(deNum));
								rec.set("liRun", res[rdm][1]);
								updateMapValue(rdm, "pricePrice", res[rdm][0]
												.toFixed(deNum));
								updateMapValue(rdm, "liRun", res[rdm][1]);
							}
								// }
							});
					}
				});
	}

	// 失去焦点事件
	function priceBlurByCurreny(val) {
		// 变更汇率
		sysdicutil.getDicListByName('currency', function(res) {
			var newPriceRate = 0;
			for (var i = 0; i < res.length; i++) {
				if (res[i].id == val) {
					// $('priceRate').value = res[i].curRate;
					newPriceRate = res[i].curRate;
					break;
				}
			}
			var oldRate = $('priceRate').value;
			$('priceRate').value = newPriceRate;
			if (newPriceRate != 0 && oldRate != 0) {
				ds.each(function(rec) {
					// var ck = rec.data.checkFlag;
					// if (ck == null || ck == 0 || ck == false) {
					var rdm = rec.data.rdm;
					if (rdm != null && rdm != "") {
						var oldPrice = rec.data.pricePrice;
						var newPrice = oldPrice * oldRate / newPriceRate;
						rec.set("pricePrice", newPrice.toFixed(deNum));
						updateMapValue(rdm, "pricePrice", newPrice
										.toFixed(deNum));
						// 计算表格中的利润率
						var rdm = rec.data.rdm;
						if (rdm != null && rdm != "") {
							// 将计算单价的参数组成一个map
							var mapPrice = {};
							mapPrice.currencyId = $('currencyId').value;// 币种
							// mapPrice.priceProfit = $('priceProfit').value;//
							// 利润率
							mapPrice.priceFob = $('priceFob').value;// FOB
							mapPrice.priceRate = newPriceRate;// 汇率
							mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
							mapPrice.insureRate = $('insureRate').value;// 保险费率
							mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
							mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
							mapPrice.price = newPrice;
							DWREngine.setAsync(false);
							cotPriceService.getNewLiRun(mapPrice, rdm,
									function(res) {
										if (res != null) {
											rec.set("liRun", res);
											updateMapValue(rdm, "liRun", res);
										}
									});
							DWREngine.setAsync(true);
						}
					}
						// }
				});
			}
		});
	}

	// 条款组合改变事件
	function composeChange() {
		// 价格条款
		var clauseIdText = '';
		if ($('clauseId').value != '') {
			clauseIdText = clauseBox.getRawValue();
		}
		// 起运港
		var shipportIdText = '';
		if ($('shipportId').value != '') {
			shipportIdText = " " + shipPortBox.getRawValue();
		}
		// 目的港
		var targetportIdText = '';
		if ($('targetportId').value != '') {
			targetportIdText = " " + targetPortBox.getRawValue();
		}
		// 佣金比例
		var commisionScaleText = '';
		if ($('commisionScale').value != '') {
			commisionScaleText = " c" + $('commisionScale').value;
		}
		// 币种
		var currencyIdText = '';
		if ($('currencyId').value != '') {
			currencyIdText = " in " + curBox.getRawValue();
		}
		// 佣金类型
		var commisionType = commisionBox.getRawValue();
		if (commisionType == '明佣') {
			if (clauseIdText == "FOB") {
				$('priceCompose').value = (clauseIdText + shipportIdText
						+ commisionScaleText + currencyIdText).toUpperCase();
			} else {
				$('priceCompose').value = (clauseIdText + targetportIdText
						+ commisionScaleText + currencyIdText).toUpperCase();
			}
		} else {
			if (clauseIdText == "FOB") {
				$('priceCompose').value = (clauseIdText + shipportIdText + currencyIdText)
						.toUpperCase();
			} else {
				$('priceCompose').value = (clauseIdText + targetportIdText + currencyIdText)
						.toUpperCase();
			}
		}
	}

	// 佣金比例改变事件

	function commisionScaleChange(txt, newVal, oldVal) {
		composeChange();
		var newNum = 0;
		if (newVal != '' && !isNaN(newVal)) {
			newNum = newVal;
		}
		// 将计算单价的参数组成一个map
		var mapPrice = {};
		mapPrice.currencyId = $('currencyId').value;// 币种
		// mapPrice.priceProfit = $('priceProfit').value;// 利润率
		mapPrice.priceFob = $('priceFob').value;// FOB
		mapPrice.priceRate = $('priceRate').value;// 汇率
		mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
		mapPrice.insureRate = $('insureRate').value;// 保险费率
		mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
		mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型

		// 修改表格中的单价
		ds.each(function(rec) {
			// var ck = rec.data.checkFlag;
			// if (ck == null || ck == 0 || ck == false) {
			var rdm = rec.data.rdm;
			if (rdm != null && rdm != "") {
				var pricePrice = rec.data.pricePrice;
				var price = 0;
				if (pricePrice != '') {
					price = pricePrice;
				}
				// 先算出扣除佣金后的原始价
				if (price != 0 && oldVal != 0 && oldVal != '') {
					price = price / (1 + oldVal * 0.01);
				}
				price = (price * (1 + newNum * 0.01)).toFixed(deNum);
				rec.set('pricePrice', price);
				mapPrice.price = price;
				updateMapValue(rdm, "pricePrice", price);
				cotPriceService.getNewLiRun(mapPrice, rdm, function(res) {
							if (res != null) {
								rec.set("liRun", res);
								updateMapValue(rdm, "liRun", res);
							}
						});
			}
				// }
			});
	}

	// 打折
	function checkZheType(txt, newVal, oldVal) {
		var newNum = 100;
		if (newVal != '' && !isNaN(newVal)) {
			newNum = newVal;
		}
		// 将计算单价的参数组成一个map
		var mapPrice = {};
		mapPrice.currencyId = $('currencyId').value;// 币种
		// mapPrice.priceProfit = $('priceProfit').value;// 利润率
		mapPrice.priceFob = $('priceFob').value;// FOB
		mapPrice.priceRate = $('priceRate').value;// 汇率
		mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
		mapPrice.insureRate = $('insureRate').value;// 保险费率
		mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
		mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型

		// 修改表格中的单价
		ds.each(function(rec) {
			// var ck = rec.data.checkFlag;
			// if (ck == null || ck == 0 || ck == false) {
			var rdm = rec.data.rdm;
			if (rdm != null && rdm != "") {
				var pricePrice = rec.data.pricePrice;
				var price = 0;
				if (pricePrice != '') {
					price = pricePrice;
				}
				// 先算出扣除折扣后的原始价
				if (price != 0 && oldVal != 100 && oldVal != '') {
					price = price / (oldVal * 0.01);
				}

				price = (price * newNum * 0.01).toFixed(deNum);
				rec.set('pricePrice', price);
				mapPrice.price = price;
				updateMapValue(rdm, "pricePrice", price);
				cotPriceService.getNewLiRun(mapPrice, rdm, function(res) {
							if (res != null) {
								rec.set("liRun", res);
								updateMapValue(rdm, "liRun", res);
							}
						});
			}
				// }
			});
	}

	// 根据选择的客户编号自动填写表单
	function setCustomerForm(cusId) {
		DWREngine.setAsync(false);
		cotPriceService.getCustomerById(parseInt(cusId), function(res) {
					var ship = $('shipportId').value;
					var ct = $("clauseId").value;
					DWRUtil.setValues(res);
					// 如果客户没价格条款,不加载
					if (res.clauseId == null) {
						$("clauseId").value = ct;
					}
					$('custId').value = res.id;
					$('shipportId').value = ship;// 不更改起运港
					targetPortBox.bindPageValue("CotTargetPort", "id",
							res.trgportId);
					empBox.bindPageValue("CotEmps", "id", res.empId);
					commisionBox.bindValue(res.commisionTypeId);
					payTypeBox.bindValue(res.payTypeId);
				});
		DWREngine.setAsync(true);
		// 延时
		var task = new Ext.util.DelayedTask(function() {
					composeChange();
				});
		task.delay(500);
	}

	// 获取单号
	function getPriceNo() {
		var currDate = $('priceTime').value;
		var custId = $('custId').value;
		var companyId = $('companyId').value;
		var empId = empBox.getValue();
		if (currDate == "" || custId == null) {
			Ext.MessageBox.alert("Message", "Pls select a date");
			return;
		}
		if ($('custId').value == null || $('custId').value == "") {
			Ext.MessageBox.alert("Message", "Please select client");
			return;
		}
		if ($('companyId').value == null || $('companyId').value == "") {
			Ext.MessageBox.alert("Message", "Please select Company");
			return;
		}

		if (empBox.getValue() == null || empBox.getValue() == "") {
			Ext.MessageBox.alert("Message", "Please select a salesman");
			return;
		}
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			cotSeqService.getPriceNo(custId, empId, currDate,companyId, function(ps) {
						$('priceNo').value = ps;
					});
		}
	}

	// 计算包材价格
	var calPrice = function() {
		var rdm = $('rdm').value;
		if (rdm == '' || rdm == 'rdm') {
			return;
		}
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
		cotPriceService.calPriceAll(rdm, function(res) {
					if (res != null) {
						$('boxPbPrice').value = res[0];
						$('boxIbPrice').value = res[1];
						$('boxMbPrice').value = res[2];
						$('boxObPrice').value = res[3];
						$('packingPrice').value = res[4];
						$('inputGridPrice').value = res[6];
						DWREngine.setAsync(false);
						cotPriceService.getPriceMapValue(rdm, function(obj) {
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
										cotPriceService.setPriceMap(rdm, obj,
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
						Ext.MessageBox.alert('Message',
								'Please select a detail!');
					}
				});
	}

	// 加载包装类型值
	function selectBind(com, rec, index) {
		var id = rec.data.id;
		var rdm = $('rdm').value;
		if (rdm == '' || rdm == 'rdm') {
			return;
		}
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(obj) {
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
										cotPriceService.setPriceMap(rdm, obj,
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

	// 根据产品货号查找样品数据
	function insertToGrid(existDetail, type) {
		existDetail.priceId = null;
		// 设置序列号
		// existDetail.sortNo = getNewSortNo("priceDetail", ak);
		// 是否采用上次报价
		if (type == false) {
			// 将添加的样品对象储存到后台map中
			DWREngine.setAsync(false);
			cotPriceService.setPriceMap(existDetail.rdm, existDetail, function(
							res) {
						setObjToGrid(existDetail);
					});
			DWREngine.setAsync(true);
		} else {
			// 将计算单价的参数组成一个map
			var mapPrice = {};
			mapPrice.currencyId = $('currencyId').value;// 币种
			// mapPrice.clauseId = $('clauseId').value;// 价格条款
			mapPrice.clauseId = 1;// 价格条款
			// mapPrice.priceProfit = $('priceProfit').value;// 利润率
			mapPrice.priceFob = $('priceFob').value;// FOB
			mapPrice.priceRate = $('priceRate').value;// 汇率
			mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
			mapPrice.insureRate = $('insureRate').value;// 保险费率
			mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
			mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
			// 根据样品货号查找样品
			DWREngine.setAsync(false);
			cotPriceService.findDetailByEleId(existDetail, mapPrice, liRunCau,
					function(result) {
						if (result != null) {
							// 将添加的样品对象储存到后台map中
							cotPriceService.setPriceMap(existDetail.rdm,
									result, function(res) {
										setObjToGrid(result);
									});
						} else {
							Ext.MessageBox.alert('Message',
									'根据报价配置,您需要先选择价格条款并配置对应的报价公式!');
						}
					});
			DWREngine.setAsync(true);
		}
	}

	// 根据报价配置决定单价的取值
	function insertByCfg(eleId) {
		var noPrice = $('noPrice').value;
		var havePrice = $('havePrice').value;
		var currencyId = $('currencyId').value;
		var clauseId = $('clauseId').value;
		var priceTime = $('priceTime').value;
		// 判断该产品是否对该用户报价过
		cotPriceService.findIsExistDetail(eleId, $('custId').value, clauseId,
				priceTime, function(rtn) {
					// 取得随机数
					var rdm = getRandom();
					if (rtn != null) {
						var result = rtn[1];
						if (rtn[0] == 1) {
							result.type = 'price';
						} else if (rtn[0] == 2) {
							result.pricePrice = result.orderPrice;
							result.type = 'order';
						} else if (rtn[0] == 3) {
							result.pricePrice = result.priceOut;
							result.currencyId = result.priceOutUint;
							result.type = 'given';
						}
						result.srcId = result.id;
						result.id = null;
						result.rdm = rdm;
						// 取对外报价
						if (havePrice == 0) {
							// 判断该产品货号是否存在,有存在的话转成报价明细
							cotPriceService.changeEleToPriceDetail(eleId,
									function(res) {
										if (res != null) {
											res.type = 'ele';
											res.srcId = res.id;
											res.id = null;
											// 重新换算价格(币种不同时)
											queryService.updatePrice(
													res.priceOut,
													res.priceOutUint,
													currencyId,
													function(newPri) {
														res.pricePrice = newPri
																.toFixed(deNum);
														res.currencyId = currencyId;
														res.rdm = rdm;
														// ds.remove(editRec);//
														// 删除行
														insertToGrid(res, false);
														addNewGrid();
													});
										} else {
											// 新货号处理(根据报价配置:逐条询问还是不询问)
											if ($('insertType').value == 0) {
												showEleTable(eleId);
											} else {
												// 直接当新货号加到表格
												_self.noEleAsNew(eleId);
											}
										}
									});

						}
						// 按价格条款计算
						if (havePrice == 1) {
							// ds.remove(editRec);// 删除行
							result.currencyId = currencyId;
							insertToGrid(result, true);
							addNewGrid();
						}
						// 取最近一次报价
						if (havePrice == 2) {
							// 重新换算价格(币种不同时)
							queryService.updatePrice(result.pricePrice,
									result.currencyId, currencyId, function(
											newPri) {
										result.pricePrice = newPri
												.toFixed(deNum);
										result.currencyId = currencyId;
										// ds.remove(editRec);// 删除行
										insertToGrid(result, false);
										addNewGrid();
									});
						}
					} else {
						// 判断该产品货号是否存在,有存在的话转成报价明细
						cotPriceService.changeEleToPriceDetail(eleId, function(
										res) {
									if (res != null) {
										res.type = 'ele';
										res.srcId = res.id;
										res.id = null;
										res.rdm = rdm;
										// 取对外报价
										if (noPrice == 0) {
											// 重新换算价格(币种不同时)
											queryService.updatePrice(
													res.priceOut,
													res.priceOutUint,
													currencyId,
													function(newPri) {
														res.pricePrice = newPri
																.toFixed(deNum);
														res.currencyId = currencyId;
														// ds.remove(editRec);//
														// 删除行
														insertToGrid(res, false);
														addNewGrid();
													});
										}
										// 按价格条款计算
										if (noPrice == 1) {
											// ds.remove(editRec);// 删除行
											res.currencyId = currencyId;
											insertToGrid(res, true);
											addNewGrid();
										}
									} else {
										// 新货号处理(根据报价配置:逐条询问还是不询问)
										if ($('insertType').value == 0) {
											showEleTable(eleId);
										} else {
											// 直接当新货号加到表格
											_self.noEleAsNew(eleId);
										}
									}
								});
					}
				});
	}

	// 显示样品查询面板
	function showEleTable(eleId) {
		var eleQueryWin = new EleQueryWin({
					bar : _self,
					title : 'In the sample data to find "' + eleId
							+ '" t the beginning of the Item:',
					eleIdFind : eleId
				});
		eleQueryWin.show();
	};

	// 添加带有数据的record到表格中
	function setObjToGrid(obj) {
		// 获得该行的新序号(该列的最大值+1)
		var sortNo = 0;
		ds.each(function(rec) {
					if (rec.data.sortNo > sortNo)
						sortNo = rec.data.sortNo;
				});
		obj.sortNo = sortNo + 1;
		obj.checkFlag = 0;// 默认价格锁定
		// obj.checkFlag = 0;
		var u = new ds.recordType(obj);
		ds.add(u);
	}

	// 添加空白record到表格中
	var facPriceUnit = 0;// 默认币种ID
	function addNewGrid() {
		if ($('priceStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message',
					'Sorry, the single has been reviewed can not be modified!');
			return;
		}
		// 验证表单
		var formData = getFormValues(priceForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		if (facPriceUnit == 0) {
			DWREngine.setAsync(false);
			cotPriceService.getList('CotPriceCfg', function(res) {
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

		var u = new ds.recordType({
					eleId : "",
					sortNo : sortNo + 1,
					custNo : "",
					eleName : "",
					eleNameEn : "",
					eleCol : "",
					pricePrice : "",
					checkFlag : "",
					taoUnit : "",
					eleUnit : "",
					liRun : "",
					tuiLv : "",
					factoryId : "",
					factoryNo : "",
					priceFac : "",
					priceFacUint : (facPriceUnit == 0 ? "" : facPriceUnit),
					eleTypeidLv1 : "",
					eleSizeDesc : "",
					eleInchDesc : "",
					boxIbCount : "",
					boxMbCount : "",
					boxObCount : "",
					boxObL : "",
					boxObW : "",
					boxObH : "",
					boxCbm : "",
					boxGrossWeigth : "",
					boxNetWeigth : "",
					type : "",
					srcId : "",
					rdm : ""
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

	// 删除
	function onDel() {
		mask();
		// 延时
		var task = new Ext.util.DelayedTask(function() {
					var cord = sm.getSelections();
					Ext.each(cord, function(item) {
								ds.remove(item);
							});
					if (rightForm.isVisible()) {
						$('picPath').src = "./showPicture.action?flag=noPic";
						clearFormAndSet(rightForm);
					}
					unmask();
				});
		task.delay(100);
	}

	// 初始化页面,加载报表详细信息
	function initForm(record) {
		var eleId = record.data.eleId;
		var rdm = record.data.rdm;
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
		var isMod = getPopedomByOpType("cotprice.do", "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").hide();
			Ext.getCmp("updel").hide();
		}
		if ((rdm == null || rdm == "") && $('picPath') != null) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
			return;
		}
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
			if (res != null) {
				if (res.boxCbm != null) {
					res.boxCbm = res.boxCbm.toFixed(cbmNum);
				}
				rightForm.getForm().setValues(res);
				if (popdom == true) {
					res.picPath = "common/images/noElePicSel.png";
				} else {
					if (!flag) {
						if (type == 'price') {
							res.picPath = "./showPicture.action?flag=price&detailId="
									+ srcId;
						}
						if (type == 'ele') {
							res.picPath = "./showPicture.action?flag=ele&elementId="
									+ srcId;
						}
						if (type == 'order') {
							res.picPath = "./showPicture.action?flag=order&detailId="
									+ srcId;
						}
						if (type == 'given') {
							res.picPath = "./showPicture.action?flag=given&detailId="
									+ srcId;
						}
						if (type == 'panEle') {
							res.picPath = "./showPicture.action?flag=pan&detailId="
									+ srcId;
						}
						if (type == null || type == 'none') {
							res.picPath = "./showPicture.action?flag=noPic";
						}
					} else {
						var rdm = Math.round(Math.random() * 10000);
						res.picPath = "./showPicture.action?flag=price&detailId="
								+ record.id + "&tmp=" + rdm;
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
				// 显示原始外销价,重新换算价格
				queryService.updatePrice(res.priceOut, res.priceOutUint,
						$('currencyId').value, function(newPr) {
							Ext.getCmp("oldPrice").setValue(newPr
									.toFixed(outNum));
						});
			}
		});
		DWREngine.setAsync(true);
		if ($('custId').value != '') {
			cotPriceService.findNewPriceVO(eleId, $('custId').value, function(
							ap) {
						Ext.getCmp("newPrice").setValue(ap.newPrice);
						Ext.getCmp("avgPrice").setValue(ap.avgPrice);
						Ext.getCmp("maxPrice").setValue(ap.maxPrice);
						Ext.getCmp("minPrice").setValue(ap.minPrice);
					});
		}
	}

	// 从报价订单送样表格导入时
	this.insertByBatch = function(res, inputType) {
		var newPrice = 0;
		var currencyId = 0;
		if (inputType == 'price') {
			newPrice = res.pricePrice;
			currencyId = res.currencyId;
		}
		if (inputType == 'order') {
			newPrice = res.orderPrice;
			currencyId = res.currencyId;
		}
		if (inputType == 'given') {
			newPrice = res.priceOut;
			currencyId = res.priceOutUint;
		}
		if (inputType == 'panEle') {
			newPrice = 0;
			currencyId = curBox.getValue();
			res.eleInchDesc=res.size;
			res.eleNameEn=res.eleNameEn;
//			res.containerCount=res.boxCount;
			res.boxObCount=1;
			//给生产价赋值
			res.priceFacUint=res.currencyId;
			res.priceFac=res.panPrice;
		}
		res.pricePrice = newPrice;
		DWREngine.setAsync(false);
		if (currencyId != curBox.getValue()) {
			// 重新换算价格
			queryService.updatePrice(newPrice, currencyId, curBox.getValue(),
					function(newPri) {
						res.pricePrice = newPri.toFixed(deNum);
					});
		}
		res.currencyId = curBox.getValue();
		res.type = inputType;
		res.srcId = res.id;
//		if(inputType == 'panEle')
//			res.srcId = res.panId;
		res.id = null;
		// 取得随机数
		var rdm = getRandom();
		res.rdm = rdm;
		// 将添加的样品对象储存到后台map中
		cotPriceService.setPriceMap(rdm, res, function(res) {
				});
		setObjToGrid(res);
		DWREngine.setAsync(true);
	}

	// 报价发邮件
	function sendMail() {
		var priceId = $('pId').value;
		var priceNo = $('priceNo').value;
		if (priceId == '' || priceId == 'null') {
			Ext.MessageBox.alert("Message", 'The Quotation have not saved! ');
			return;
		}
		// 打印模版
		var reportTemple = $('reportTemple').value;
		if (reportTemple == '') {
			Ext.MessageBox.alert("INFO", 'Please select a Template!');
			return;
		}

		DWREngine.setAsync(false);
		mask();
		DWREngine.setAsync(true);
		cotPriceService.sendMail(priceId, priceNo, reportTemple, function(res) {
					openEleWindow("./saverptfile/price/" + priceNo + ".pdf");
					unmask();
				});
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message",
					'Sorry, you do not have Authority! ');
			return;
		}
		// 如果该单没保存不能打印
		if ($('pId').value == 'null' || $('pId').value == '') {
			Ext.MessageBox.alert("Message",
					'The single has not been preserved, can not print！');
			return;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'price',
						pId : 'pId',
						pNo : 'priceNo',
						mailSendId : 'custId',
						status : 'priceStatus',
						sendMail : sendMail
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

	// 反审
	function fanShen() {
		var isCheck = getPopedomByOpType("cotprice.do", "CHECK");
		if (isCheck == 0) {
			Ext.MessageBox.alert('Message',
					'Sorry, you do not have audit authority!');
			return;
		}
		Ext.MessageBox.confirm('Message',
				"Are you sure you re-examination of the single?",
				function(btn) {
					if (btn == 'yes') {
						cotPriceService.updatePriceStatus($('pId').value, 0,
								function(res) {
									shenBox.setValue(0);
									reflashParent('priceGrid');
									// Ext.getCmp("qingBtn").show();
								// Ext.getCmp("fanxBtn").hide();
							});

					}
				});
	}

	// 请求审核
	function qingShen() {
		Ext.MessageBox.confirm('Message', "Are you sure you request review?",
				function(btn) {
					if (btn == 'yes') {
						cotPriceService.updatePriceStatus($('pId').value, 3,
								function(res) {
									shenBox.setValue(3);
									reflashParent('priceGrid');
									// Ext.getCmp("guoBtn").show();
								// Ext.getCmp("buBtn").show();
								// Ext.getCmp("qingBtn").hide();
								// Ext.getCmp("shenTab").enable();
							});

					}
				});
	}

	// 审核通过
	function guoShen() {
		var isCheck = getPopedomByOpType("cotprice.do", "CHECK");
		if (isCheck == 0) {
			Ext.MessageBox.alert('Message',
					'Sorry, you do not have audit authority!');
			return;
		}
		Ext.MessageBox.confirm('Message',
				"Are you sure you passed the examination of the single?",
				function(btn) {
					if (btn == 'yes') {
						cotPriceService.updatePriceStatus($('pId').value, 2,
								function(res) {
									shenBox.setValue(2);
									reflashParent('priceGrid');
									// Ext.getCmp("fanxBtn").show();
								// Ext.getCmp("guoBtn").hide();
								// Ext.getCmp("buBtn").hide();
							});

					}
				});
	}

	// 审核不通过
	function buShen() {
		var isCheck = getPopedomByOpType("cotprice.do", "CHECK");
		if (isCheck == 0) {
			Ext.MessageBox
					.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}
		Ext.MessageBox.confirm('Message',
				"Are you sure you do not pass the examination of the single?",
				function(btn) {
					if (btn == 'yes') {
						cotPriceService.updatePriceStatus($('pId').value, 1,
								function(res) {
									shenBox.setValue(1);
									reflashParent('priceGrid');
									// Ext.getCmp("qingBtn").show();
								// Ext.getCmp("guoBtn").hide();
								// Ext.getCmp("buBtn").hide();
							})

					}
				});
	}

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
		cotPriceService.updateSortNo($("pId").value, type, field, fieldType,
				function(res) {
					if (res) {
						ds.reload();
						sortPanel.hide();
						Ext.MessageBox.alert('Message', "Successful re-sort!");
					} else {
						Ext.MessageBox.alert('Message', "Failure to re-sort!");
					}
				});
		DWREngine.setAsync(true);
	}

	// 保存排序
	function sumSortTable() {
		var sort = ds.getSortState();
		if (!sort) {
			Ext.MessageBox.alert("Message",
					"Order did not change, no need to save!");
			return;
		}
		if ($("pId").value == '' || $("pId").value == 'null') {
			Ext.MessageBox.alert("Message",
					"Please save quotes, and then change the sorting!");
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
		cotPriceService.updateSortNo($("pId").value, type, sort.field,
				fieldType, function(res) {
					if (res) {
						ds.reload();
						Ext.MessageBox.alert('Message',
								"Sort table successfully saved!");
					} else {
						Ext.MessageBox.alert('Message',
								"Save table sort failure!");
					}
				});
		DWREngine.setAsync(true);
	}

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
						title : 'Reorder',
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
												boxLabel : 'By serial',
												inputValue : 0,
												style : "marginLeft:8",
												name : 'sortType',
												id : "sortXu",
												checked : true
											}, {
												boxLabel : 'By Art No.',
												id : "sortEleId",
												inputValue : 1,
												name : 'sortType'
											}, {
												boxLabel : 'By Cust.Article No',
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

	// 添加数据从excel
	this.insertSelectExcel = function(excelFlag) {
		DWREngine.setAsync(false);
		// 得到excel的内存map的key集合
		cotPriceService.getRdmList(function(result) {
			if (result.length > 0) {
				for (var i = 0; i < result.length; i++) {
					insertToGridExcel(result[i], excelFlag);
				}
				// 清空excel内存
				cotPriceService.removeExcelSession(function(ct) {
						});
				var id = $('pId').value;
				var custId = custBox.getValue();
				var currencyId = curBox.getValue();
				// 保存订单明细
				cotPriceService.saveDetail(id, currencyId, custId,
						function(alt) {
							// 加载厂家表缓存
							baseDataUtil.getBaseDicDataMap("CotFactory", "id",
									"shortName", function(res) {
										facData = res;
									});
							// 加载币种表缓存
							baseDataUtil.getBaseDicDataMap("CotCurrency", "id",
									"curNameEn", function(res) {
										curData = res;
									});
							// 加载材质表缓存
							baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id",
									"typeName", function(res) {
										typeData = res;
									});
							// 刷新表格
							ds.proxy.setApi({
								read : "cotprice.do?method=queryPriceDetail&priceId="
										+ id
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

	// 根据产品货号查找样品数据
	function insertToGridExcel(rdm, excelFlag) {
		// 将计算单价的参数组成一个map
		var mapPrice = {};
		mapPrice.currencyId = $('currencyId').value;// 币种
		// mapPrice.clauseId = $('clauseId').value;// 价格条款
		mapPrice.clauseId = 1;// 价格条款
		// mapPrice.priceProfit = $('priceProfit').value;// 利润率
		mapPrice.priceFob = $('priceFob').value;// FOB
		mapPrice.priceRate = $('priceRate').value;// 汇率
		mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
		mapPrice.insureRate = $('insureRate').value;// 保险费率
		mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
		mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
		// 根据样品货号查找样品
		cotPriceService.findDetailByEleIdExcel(rdm, excelFlag, mapPrice,
				liRunCau, function(result) {
					if (result != null) {
						result.type = 'none';
						result.srcId = 0;
						// 将添加的样品对象储存到后台map中
						cotPriceService.setExcelMap(rdm, result, function(res) {
								});
					}
				});
	}

	// 单价值改变事件
	function firePriceOutChange(rec, newVal) {
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			updateMapValue(rdm, "pricePrice", newVal);
			// 将计算单价的参数组成一个map
			var mapPrice = {};
			mapPrice.currencyId = $('currencyId').value;// 币种
			// mapPrice.priceProfit = $('priceProfit').value;// 利润率
			mapPrice.priceFob = $('priceFob').value;// FOB
			mapPrice.priceRate = $('priceRate').value;// 汇率
			mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
			mapPrice.insureRate = $('insureRate').value;// 保险费率
			mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
			mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
			mapPrice.price = newVal;
			cotPriceService.getNewLiRun(mapPrice, rdm, function(res) {
						if (res != null) {
							rec.set("liRun", res);
							updateMapValue(rdm, "liRun", res);
						}
					});
		}

	}

	// 双击历史事件表格更新报价事件
	function replacePrice(hisGrid, index) {
		var hisrec = hisGrid.getStore().getAt(index);
		var hisprice = hisrec.get("pricePrice");
		var pricerec = sm.getSelected();
		pricerec.set("pricePrice", hisprice.toFixed(deNum));
		firePriceOutChange(pricerec, hisprice.toFixed(deNum));
		var hisWin = Ext.getCmp("hisWin");
		hisWin.close();
		// 修改后不直接保存报价单

		// // 更改添加action参数
		// var urlAdd = '&pricePrimId=' + $('pId').value + '&currencyId='
		// + $('currencyId').value + '&custId=' + $('custId').value;
		// // 更改修改action参数
		// var urlMod = '&custId=' + $('custId').value + '&currencyId='
		// + $('currencyId').value;
		// ds.proxy.setApi({
		// read : "cotprice.do?method=queryPriceDetail&priceId="
		// + $('pId').value,
		// create : "cotprice.do?method=add" + urlAdd,
		// update : "cotprice.do?method=modify" + urlMod,
		// destroy : "cotprice.do?method=remove"
		// });
		// ds.save();
	}

	// 打开盘点机上传面板
	function showPanPanel() {
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			Ext.MessageBox
					.alert('Message',
							'Please save quotations! Machine file and then import the inventory！');
			return;
		}
		// 验证表单
		var formData = getFormValues(priceForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 判断表格是否有未保存的数据
		if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
			Ext.MessageBox
					.alert(
							'Message',
							'Price quotation data to change, please save! Machine file and then import the inventory！');
			return;
		}

		var win = new UploadPan({
					waitMsg : "File upload......",
					uploadUrl : './uploadMachine.action',
					validType : "txt|cvs",
					pWin : _self
				})
		win.show();
	}

	// 打开图片上传面板
	function showUploadPanel() {
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.MessageBox.alert('Message',
					'Please select a record price quotation！');
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
						flag : 'price'
					},
					waitMsg : "Photo upload......",
					opAction : opAction,
					imgObj : $('picPath'),
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=price&temp=" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadTempPic.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}

	// 删除图片
	function delPic() {
		if ($('priceStatus').value == 1 || $('priceStatus').value == 2) {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, the single has been reviewed can not be modified! To change your anti-trial!');
			return;
		}
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.MessageBox.alert('Message',
					'Please select a record price quotation！');
			return;
		}
		Ext.MessageBox.confirm('Message',
				"Are you sure you remove the picture?", function(btn) {
					if (btn == 'yes') {
						var detailId = $('id').value;
						cotPriceService.deletePicImg(detailId, function(res) {
									if (res) {
										$('picPath').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox.alert('Message',
												'删除图片失败！');
									}
								})
					}
				});
	}

	// 更新内存数据,并修改右边表单数据
	function updateMapValue(rdm, property, value) {
		DWREngine.setAsync(false);
		cotPriceService.updateMapValueByEleId(rdm, property, value, function(
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

	// 不修改内存,只修改右边表单数据
	function rightChange(field, value) {
		if (rightForm.isVisible()) {
			var txt = rightForm.getForm().findField(field);
			if (txt != null) {
				txt.setValue(value);
			}
		}
	}

	// 单价值改变事件
	function priceOutChange(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			updateMapValue(rdm, "pricePrice", newVal);
			// 将计算单价的参数组成一个map
			var mapPrice = {};
			mapPrice.currencyId = $('currencyId').value;// 币种
			// mapPrice.priceProfit = $('priceProfit').value;// 利润率
			mapPrice.priceFob = $('priceFob').value;// FOB
			mapPrice.priceRate = $('priceRate').value;// 汇率
			mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
			mapPrice.insureRate = $('insureRate').value;// 保险费率
			mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
			mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
			mapPrice.price = newVal;
			cotPriceService.getNewLiRun(mapPrice, rdm, function(res) {
						if (res != null) {
							if (res <= 999999.99) {
								rec.set("liRun", res);
								updateMapValue(rdm, "liRun", res);
							}
						}
					});
		}

	}

	// 单重改变事件
	function boxWeigthChange(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			updateMapValue(rdm, "boxWeigth", newVal);
			var boxObCount = rec.data.boxObCount;
			if (boxObCount == "") {
				boxObCount = 0;
			}
			cotElementsService.getDefaultList(function(res) {
						var jin = newVal * boxObCount / 1000;
						var boxGrossWeigth = 0;
						if (res.length != 0) {
							if (res[0].grossNum != null) {
								var temp = jin + res[0].grossNum;
								boxGrossWeigth = temp.toFixed(3);
							}
						} else {
							boxGrossWeigth = jin.toFixed(3);
						}
						rec.set("boxNetWeigth", jin.toFixed(3));
						rec.set("boxGrossWeigth", boxGrossWeigth);
						rightChange('boxNetWeigth', jin.toFixed(3));
						rightChange('boxGrossWeigth', boxGrossWeigth);
						updateMapValue(rdm, "boxNetWeigth", jin.toFixed(3));
						updateMapValue(rdm, "boxGrossWeigth", boxGrossWeigth);
					});
		}
	}

	// 单价利润率改变事件
	function liRunChange(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			updateMapValue(rdm, "liRun", newVal);
			// var ck = rec.data.checkFlag;
			// if (ck == null || ck == 0 || ck == false) {
			// 将计算单价的参数组成一个map
			var mapPrice = {};
			// mapPrice.clauseId = $('clauseId').value;// 价格条款
			mapPrice.clauseId = 1;// 价格条款
			mapPrice.currencyId = $('currencyId').value;// 币种
			mapPrice.priceProfit = newVal;// 利润率
			mapPrice.priceFob = $('priceFob').value;// FOB
			mapPrice.priceRate = $('priceRate').value;// 汇率
			mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
			mapPrice.insureRate = $('insureRate').value;// 保险费率
			mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
			mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
			cotPriceService.getNewPriceByLiRun(mapPrice, rdm, function(res) {
						if (res != null) {
							rec.set("pricePrice", res.toFixed(deNum));
							updateMapValue(rdm, "pricePrice", res
											.toFixed(deNum));
						}
					})
			// }
		}
	}

	// 退税率改变事件
	function tuiLvChange(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			updateMapValue(rdm, "tuiLv", newVal);
			// var ck = rec.data.checkFlag;
			// if (ck == null || ck == 0 || ck == false) {
			// 将计算单价的参数组成一个map
			var mapPrice = {};
			// mapPrice.clauseId = $('clauseId').value;// 价格条款
			mapPrice.clauseId = 1;// 价格条款
			mapPrice.currencyId = $('currencyId').value;// 币种
			mapPrice.priceProfit = rec.data.priceProfit;// 利润率
			mapPrice.tuiLv = newVal;// 退税率
			mapPrice.priceFob = $('priceFob').value;// FOB
			mapPrice.priceRate = $('priceRate').value;// 汇率
			mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
			mapPrice.insureRate = $('insureRate').value;// 保险费率
			mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
			mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
			cotPriceService.getNewPriceByTuiLv(mapPrice, rdm, function(res) {
						if (res != null) {
							rec.set("pricePrice", res.toFixed(deNum));
							updateMapValue(rdm, "pricePrice", res
											.toFixed(deNum));
						}
					})
			// }
		}
	}

	// 生产价改变事件
	function priceFacChange(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			updateMapValue(rdm, "priceFac", newVal.toFixed(facNum));
			// var ck = rec.data.checkFlag;
			// if (ck == null || ck == 0 || ck == false) {
			// 将计算单价的参数组成一个map
			var mapPrice = {};
			// mapPrice.clauseId = $('clauseId').value;// 价格条款
			mapPrice.clauseId = 1;// 价格条款
			mapPrice.currencyId = $('currencyId').value;// 币种
			mapPrice.priceProfit = rec.data.liRun;// 利润率
			mapPrice.priceFac = newVal;// 生产价
			mapPrice.priceFob = $('priceFob').value;// FOB
			mapPrice.priceRate = $('priceRate').value;// 汇率
			mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
			mapPrice.insureRate = $('insureRate').value;// 保险费率
			mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
			mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
			// cotPriceService.getNewPriceByPriceFac(mapPrice, rdm, function(
			cotPriceService.getNewLiRun(mapPrice, rdm, function(res) {
						if (res != null) {
							// rec.set("pricePrice", res.toFixed(deNum));
							// updateMapValue(rdm, "pricePrice", res
							// .toFixed(deNum));
							rec.set("liRun", res);
							updateMapValue(rdm, "liRun", res);
						} else {
							// 没有配置价格条款对应的公式时.或计算公式有误返回null
							// 当配件改变时,会调用该方法,必须触发表格修改事件
							var temp = rec.data.rdm;
							rec.set("rdm", temp + "aaa");
							rec.set("rdm", temp);
						}
					})
			// }
		}
	}

	// 生产价币种改变事件
	function priceFacUintChange(newVal, oldVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm != null && rdm != "") {
			updateMapValue(rdm, "priceFacUint", newVal);
			var priceFac = rec.data.priceFac;
			if (!isBlank(priceFac) && !isBlank(newVal) && !isBlank(oldVal)) {
				// 2011如果更改采购币种（currency），则采购价格（Purchase price）必须结合汇率自动计算出新的价格
				DWREngine.setAsync(false);
				// 重新换算价格
				queryService.updatePrice(priceFac, oldVal, newVal, function(
								newPri) {
							rec.set("priceFac", newPri.toFixed(facNum));
							updateMapValue(rdm, "priceFac", newPri
											.toFixed(facNum));
						});
				DWREngine.setAsync(true);
			}

			// var ck = rec.data.checkFlag;
			// if (ck == null || ck == 0 || ck == false) {
			// 将计算单价的参数组成一个map
			// var mapPrice = {};
			// // mapPrice.clauseId = $('clauseId').value;// 价格条款
			// mapPrice.clauseId =1;// 价格条款
			// mapPrice.currencyId = $('currencyId').value;// 币种
			// mapPrice.priceProfit = rec.data.priceProfit;// 利润率
			// mapPrice.priceFacUint = newVal;// 生产价币种
			// mapPrice.priceFob = $('priceFob').value;// FOB
			// mapPrice.priceRate = $('priceRate').value;// 汇率
			// mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
			// mapPrice.insureRate = $('insureRate').value;// 保险费率
			// mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
			// mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
			// cotPriceService.getNewPriceByPriceFacUint(mapPrice, rdm,
			// function(res) {
			// if (res != null) {
			// rec.set("pricePrice", res.toFixed(deNum));
			// updateMapValue(rdm, "pricePrice", res
			// .toFixed(deNum));
			// }
			// });
			// }
		}
	}

	// 内装数改变事件
	function boxIcountNum(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm == null || rdm == "") {
			return;
		}
		updateMapValue(rdm, "boxIbCount", newVal);
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
					if (res != null) {
						// 1.计算包材价格
						DWREngine.setAsync(false);
						cotPriceService.calPriceAll(rdm, function(def) {
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
						cotPriceService.setPriceMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 中装数改变事件
	function boxMcountNum(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm == null || rdm == "") {
			return;
		}
		updateMapValue(rdm, "boxMbCount", newVal);
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
					if (res != null) {
						// 1.计算包材价格
						DWREngine.setAsync(false);
						cotPriceService.calPriceAll(rdm, function(def) {
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
						cotPriceService.setPriceMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 外箱数值改变事件
	function boxOcountNum(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm == null || rdm == "") {
			return;
		}
		updateMapValue(rdm, "boxObCount", newVal);

		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
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
						cotPriceService.calPriceAll(rdm, function(def) {
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
						cotPriceService.setPriceMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 根据明细中的外箱长改变事件
	function changeBoxObL(newVal, type, inch) {
		var rec = null;
		var rdm;
		if (type) {
			rdm = $('rdm').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('rdm', rdm);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			rdm = rec.data.rdm;
			if (rdm == null || rdm == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObL", newVal);
		}
		updateMapValue(rdm, "boxObL", newVal);
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
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
						cotPriceService.calPriceAll(rdm, function(def) {
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
						cotPriceService.setPriceMap(rdm, res, function(res) {
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
			rdm = $('rdm').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('rdm', rdm);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			rdm = rec.data.rdm;
			if (rdm == null || rdm == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObW", newVal);
		}
		updateMapValue(rdm, "boxObW", newVal);
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
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
						cotPriceService.calPriceAll(rdm, function(def) {
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
						cotPriceService.setPriceMap(rdm, res, function(res) {
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
			rdm = $('rdm').value;
			// 在表格中查找rdm所在的行
			var index = ds.find('rdm', rdm);
			if (index != -1) {
				rec = ds.getAt(index);
			} else {
				return;
			}
		} else {
			rec = editRec;
			rdm = rec.data.rdm;
			if (rdm == null || rdm == "") {
				return;
			}
		}
		if (typeof(inch) != 'undefined') {
			rec.set("boxObH", newVal);
		}
		updateMapValue(rdm, "boxObH", newVal);
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
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
						cotPriceService.calPriceAll(rdm, function(def) {
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
						cotPriceService.setPriceMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// cbm的更改事件,计算出装箱数
	function changeCbm(newVal) {
		var rec = editRec;
		var rdm = rec.data.rdm;
		if (rdm == null || rdm == "") {
			return;
		}
		DWREngine.setAsync(false);
		cotPriceService.getPriceMapValue(rdm, function(res) {
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
						cotPriceService.setPriceMap(rdm, res, function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}
	// 下载多张图片.并且压缩.zip
	function downPics() {
		var temp = "";
		var arr = sm.getSelections();
		if (arr.length == 0) {
			Ext.MessageBox.alert('Message', 'Please check the detail records!');
			return;
		}
		Ext.each(arr, function(record) {
					temp += record.get("rdm") + ",";
				})

		var priceNo = $('priceNo').value;
		var type = $('picType').value;
		var url = "./downPics.action?priceNo=" + encodeURIComponent(priceNo)
				+ "&tp=price&rdms=" + temp + "&type=" + type;
		downRpt(url);
	}

	// 盘点机添加到表格
	this.insertAgain = function(res) {
		if (res.currencyId != $('currencyId').value) {
			DWREngine.setAsync(false);
			// 重新换算价格
			queryService.updatePrice(res.pricePrice, res.currencyId,
					$('currencyId').value, function(newPri) {
						res.pricePrice = newPri.toFixed(deNum);
						res.currencyId = $('currencyId').value;
					});
			DWREngine.setAsync(true);
		}
		var rdm = getRandom();
		res.rdm = rdm;
		DWREngine.setAsync(false);
		// 将添加的样品对象储存到后台map中
		cotPriceService.setPriceMap(rdm, res, function(ak) {
				});
		DWREngine.setAsync(true);
		setObjToGrid(res);
	}

	// 不存在样品档案货号直接添加到明细中
	this.noEleAsNew = function(pareEleId) {
		var rdm = getRandom();
		// ds.remove(editRec);// 删除行
		// var rec = editRec;
		var obj = new Object();
		// 设置序列号
		obj.eleId = pareEleId.toUpperCase();
		obj.pricePrice = 0;
		obj.type = 'none';
		obj.srcId = 0;
		obj.eleFlag = 0;
		obj.boxObCount = 1;
		obj.priceFac = 0;
		obj.priceFacUint = facPriceUnit;
		obj.rdm = rdm;
		// rec.set('eleId', pareEleId.toUpperCase());
		// rec.set('pricePrice', 0);
		// rec.set('type', 'none');
		// rec.set('srcId', 0);
		// rec.set('eleFlag', 0);
		// rec.set('boxObCount', 1);
		// rec.set('priceFac', 0);
		// rec.set('priceFacUint', facPriceUnit);
		// rec.set('rdm', rdm);
		setObjToGrid(obj);
		// 将添加的样品对象储存到后台map中
		cotPriceService.setPriceMap(rdm, obj, function(res) {
				});
		// 把焦点放在新增空白行
		// addNewGrid();
	}

	// 根据客号查找货号(如果货号有值不做处理.)
	function insertByCustNo(custNo) {
		var currencyId = $('currencyId').value;
		// 根据客号查找货号(取最近报价时间)
		cotPriceService.findEleByCustNo(custNo, $('custId').value,
				$('clauseId').value, $('priceTime').value, function(rtn) {
					if (rtn != null) {
						var obj = rtn[1];
						var rdm = getRandom();
						if (rtn[0] == 1) {
							obj.type = 'price';
						} else if (rtn[0] == 2) {
							obj.pricePrice = obj.orderPrice;
							obj.type = 'order';
						} else if (rtn[0] == 3) {
							obj.pricePrice = obj.priceOut;
							obj.type = 'given';
						}
						obj.srcId = obj.id;
						obj.rdm = rdm;
						obj.id = null;
						var havePrice = $('havePrice').value;
						// 取对外报价
						if (havePrice == 0) {
							// 判断该产品货号是否存在,有存在的话转成报价明细
							cotPriceService.changeEleToPriceDetail(obj.eleId,
									function(res) {
										if (res != null) {
											res.type = 'ele';
											res.srcId = res.id;
											res.id = null;
											res.custNo = custNo;
											// 重新换算价格(币种不同时)
											queryService.updatePrice(
													res.priceOut,
													res.priceOutUint,
													currencyId,
													function(newPri) {
														res.pricePrice = newPri
																.toFixed(deNum);
														res.currencyId = currencyId;
														res.rdm = rdm;
														ds.remove(editRec);// 删除行
														insertToGrid(res, false);
														addNewGrid();
													});
										} else {
											// 重新换算价格(币种不同时)
											queryService.updatePrice(
													obj.pricePrice,
													obj.currencyId, currencyId,
													function(newPri) {
														obj.pricePrice = newPri
																.toFixed(deNum);
														obj.currencyId = currencyId;
														ds.remove(editRec);// 删除行
														insertToGrid(obj, false);
														addNewGrid();
													});
										}
									});

						}
						// 按价格条款计算
						if (havePrice == 1) {
							ds.remove(editRec);// 删除行
							obj.currencyId = currencyId;
							insertToGrid(obj, true);
							addNewGrid();
						}
						// 取最近一次报价
						if (havePrice == 2) {
							// 重新换算价格(币种不同时)
							queryService.updatePrice(obj.pricePrice,
									obj.currencyId, currencyId,
									function(newPri) {
										obj.pricePrice = newPri.toFixed(deNum);
										obj.currencyId = currencyId;
										ds.remove(editRec);// 删除行
										insertToGrid(obj, false);
										addNewGrid();
									});
						}
					}
				});
	}

	// 供按样品模版添加的页面调用
	this.newEleAdd = function(obj) {
		var rdm = getRandom();
		ds.remove(editRec);// 删除行
		// 设置序列号
		obj.type = 'ele';
		obj.srcId = obj.id;
		obj.rdm = rdm;
		obj.id = null;
		// 重新换算价格
		DWREngine.setAsync(false);
		queryService.updatePrice(obj.priceOut, obj.priceOutUint,
				$('currencyId').value, function(res) {
					// 报价取对外报价
					obj.pricePrice = res.toFixed(deNum);
					setObjToGrid(obj);
				});
		DWREngine.setAsync(true);
		// 转化下时间
		var date = new Date(obj.eleAddTime);
		var date2 = new Date(obj.eleProTime);
		obj.eleAddTime = date;
		obj.eleProTime = date2;
		// 将添加的样品对象储存到后台map中
		cotPriceService.setPriceMap(rdm, obj, function(res) {
				});
		// 把焦点放在新增空白行
		addNewGrid();
	}

	// 按模板
	var addWithMol = function() {
		var model = $('modelEle').value;
		if (model == '') {
			return;
		} else {
			DWREngine.setAsync(false);
			mask();
			DWREngine.setAsync(true);
			cotPriceService.saveAs(model, $('pId').value, function(res) {
						if (res == true) {
							modlPanel.hide();
							Ext.MessageBox.alert('Message',
									"Save the success quotations!");
						} else {
							Ext.MessageBox
									.alert('Message',
											"Quotation already exists, please re-enter!");
							$('modelEle').select();
						}
						unmask();
					});
		}
	}

	// 另存为--获取单号
	function getNewPriceNo() {
		var currDate = $('priceTime').value;
		var custId = $('custId').value;
		var companyId = $('companyId').value;
		var empId = empBox.getValue();
		if (currDate == "" || custId == null) {
			Ext.MessageBox.alert("Message", "Pls select a date");
			return;
		}
		if ($('custId').value == null || $('custId').value == "") {
			Ext.MessageBox.alert("Message", "Pls. select a client");
			return;
		}
		if ($('companyId').value == null || $('companyId').value == "") {
			Ext.MessageBox.alert("Message", "Pls. select a company");
			return;
		}
		if (empBox.getValue() == null || empBox.getValue() == "") {
			Ext.MessageBox.alert("Message", "Please select a sales");
			return;
		}
		cotSeqService.getPriceNo(custId, empId, currDate,companyId, function(ps) {
					$('modelEle').value = ps;
				});
	}

	// 显示模板面板
	var modlPanel;
	function showModel(item, pressed) {
		if ($('importDiv') == null) {
			// window默认在z-index为9000
			Ext.DomHelper.append(document.body, {
				html : '<div id="importDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (modlPanel == null) {
			modlPanel = new Ext.form.FormPanel({
				labelWidth : 70,
				padding : "5px",
				height : 80,
				width : 300,
				layout : 'hbox',
				frame : true,
				title : "Save as",
				labelAlign : "right",
				layoutConfig : {
					padding : '5',
					align : 'middle'
				},
				tools : [{
							id : "close",
							handler : function(event, toolEl, p) {
								p.hide();
							}
						}],
				defaults : {
					margins : '0 5 0 0'
				},
				items : [{
					xtype : "panel",
					layout : "column",
					flex : 3,
					items : [{
						xtype : "panel",
						layout : "form",
						columnWidth : 1,
						items : [{
							xtype : "textfield",
							fieldLabel : "offer No.",
							anchor : "100%",
							id : "modelEle",
							blankText : "Please enter W&C P/I!",
							name : "modelEle",
							maxLength : 100,
							allowBlank : false,
							readOnly : true,
							listeners : {
								"render" : function(obj) {
									var tip = new Ext.ToolTip({
										target : obj.getEl(),
										anchor : 'top',
										maxWidth : 160,
										minWidth : 160,
										html : 'Click the right button to automatically generate the W&C P/I!'
									});
								}
							}
						}]
					}, {
						xtype : "button",
						width : 20,
						text : "",
						cls : "SYSOP_ADD",
						iconCls : "cal",
						handler : getNewPriceNo,
						listeners : {
							"render" : function(obj) {
								var tip = new Ext.ToolTip({
									target : obj.getEl(),
									anchor : 'top',
									maxWidth : 160,
									minWidth : 160,
									html : 'Depending on the configuration automatically generated offer No.!'
								});
							}
						}
					}]
				}, {
					xtype : "button",
					flex : 1,
					text : "save",
					handler : addWithMol
				}]
			});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("importDiv").setLeftTop(left, top - 80);
			modlPanel.render("importDiv");
			$('modelEle').focus();
		} else {
			if (!modlPanel.isVisible()) {
				modlPanel.show();
			} else {
				modlPanel.hide();
			}
		}
	};

	// 样品同步到报价
	var chooseWin;
	function showTongDiv() {
		var isPopedom = getPopedomByOpType(vaildUrl, "PRICETOSAMPLE");
		if (isPopedom == 0) {
			Ext.MessageBox
					.alert('Message',
							"You are not synchronized to the sample from the rights offer");
			return;
		}
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select records");
			return;
		}
		chooseWin = new TongChooseWin({
					flag : 0
				});
		chooseWin.show();
	}
	// 报价同步到样品
	function showTongPriceDiv() {
		var isPopedom = getPopedomByOpType(vaildUrl, "SAMPLETOPRICE");
		if (isPopedom == 0) {
			Ext.MessageBox
					.alert('Message',
							"You do not have to quote from the sample synchronization rights");
			return;
		}
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select records");
			return;
		}
		chooseWin = new TongChooseWin({
					flag : 1
				});
		chooseWin.show();
	}

	// 隐藏同步选择层
	this.hideRightMenu = function() {
		chooseWin.close();
	}

	// 更新到报价明细
	this.updateToPrice = function(eleStr, boxStr, otherStr, isPic) {
		var idsAry = new Array();
		var rdmAry = new Array();
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					idsAry.push(item.data.eleId);
					rdmAry.push(item.data.rdm);
				});

		if (idsAry.length == 0) {
			Ext.MessageBox
					.alert('Message',
							'Please select the offer needs to be synchronized sample detail records！');
			return;
		}
		chooseWin.close();
		DWREngine.setAsync(false);
		mask();
		DWREngine.setAsync(true);
		cotPriceService.updateEleToDetail(idsAry, rdmAry, eleStr, boxStr,
				otherStr, isPic, function(res) {
					if (res.length > 0) {
						for (var i = 0; i < res.length; i++) {
							var detail = res[i];
							Ext.each(cord, function(item) {
										var rdm = item.data.rdm;
										if (rdm == detail.rdm) {
											tongEleToPrice(detail, item,
													eleStr, boxStr, otherStr);
											return false;
										}
									});
						}
					}
					unmask();
					Ext.MessageBox.alert('Message',
							'Synchronization successful！');
				});

	}

	// flag为false时是新增
	function tongEleToPrice(detail, item, eleStr, boxStr, otherStr) {
		var tr = item.data;
		if (eleStr != null && eleStr != '') {
			var eleAry = eleStr.split(",");
			for (var i = 0; i < eleAry.length - 1; i++) {
				var ele = eleAry[i];
				var obj = eval("detail." + ele);
				var temp = eval("tr." + eleAry[i]);
				if ((obj == null && temp == 0) || obj == temp)
					continue;
				item.set(eleAry[i], obj);
			}
		}
		if (boxStr != null && boxStr != '') {
			var eleAry = boxStr.split(",");
			for (var i = 0; i < eleAry.length - 1; i++) {
				var ele = eleAry[i];
				var obj = eval("detail." + ele);
				var temp = eval("tr." + eleAry[i]);
				if ((obj == null && temp == 0) || obj == temp)
					continue;
				item.set(eleAry[i], obj);
				if (ele == 'boxNetWeigth') {
					item.set('boxGrossWeigth', detail.boxGrossWeigth);
				}
			}
		}
		if (otherStr != null && otherStr != '') {
			var eleAry = otherStr.split(",");
			for (var i = 0; i < eleAry.length - 1; i++) {
				var ele = eleAry[i];
				var obj = eval("detail." + ele);
				var temp = eval("tr." + eleAry[i]);
				if ((obj == null && temp == 0) || obj == temp)
					continue;
				item.set(eleAry[i], obj);
			}
		}
	}

	// 更新到样品表
	this.updateToEle = function(eleStr, boxStr, otherStr, isPic) {
		var mapEle = new Map();
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					if (item.data.eleId.trim() != '') {
						// 判断货号是否重复,重复取后一条去覆盖
						mapEle.put(item.data.eleId, item.data.rdm);
					}
				});

		if (mapEle.getKeys().length == 0) {
			Ext.MessageBox
					.alert('Message',
							'Please select the details to be updated to record the sample!');
			return;
		}
		chooseWin.close();
		var key = mapEle.getKeys();
		DWREngine.setAsync(false);
		mask();
		DWREngine.setAsync(true);
		cotPriceService.findIsExistInEle(key, function(res) {
			var sameTemp = '';
			var same = res.same;
			// 获得存在样品档案的货号的随机数
			var sameVal = new Array();
			for (var i = 0; i < same.length; i++) {
				sameTemp += same[i] + ",";
				sameVal.push(mapEle.get(same[i]));
			}
			// 获得不存在样品档案的货号的随机数
			var dis = res.dis;
			var disVal = new Array();
			for (var i = 0; i < dis.length; i++) {
				disVal.push(mapEle.get(dis[i]));
			}
			if (same.length != 0) {
				Ext.MessageBox.confirm('Message',
						'Item already exists in the product' + sameTemp
								+ "covering the original product records?",
						function(btn) {
							if (btn == 'yes') {
								cotPriceService.updateToEle(same, sameVal, dis,
										disVal, eleStr, boxStr, otherStr,
										isPic, function(res) {
											Ext.MessageBox
													.alert('Message',
															'Synchronization successful!');
										});
							} else {
								if (dis.length != 0) {
									cotPriceService.updateToEle(null, null,
											dis, disVal, eleStr, boxStr,
											otherStr, isPic, function(res) {
												Ext.MessageBox
														.alert('Message',
																'Synchronization successful!');
											});
								}
							}
						});
			} else {
				if (dis.length != 0) {
					cotPriceService.updateToEle(null, null, dis, disVal,
							eleStr, boxStr, otherStr, isPic, function(res) {
								Ext.MessageBox.alert('Message',
										'Synchronization successful!');
							});
				}
			}
			unmask();
		});
	}

	// 将合并后的子货号添加到表格
	this.insertChildSelect = function(ary) {
		var noPrice = $('noPrice').value;
		var havePrice = $('havePrice').value;
		var currencyId = $('currencyId').value;
		var clauseId = $('clauseId').value;
		var priceTime = $('priceTime').value;
		// 判断该产品是否对该用户报价过
		cotPriceService.findIsExistDetail(ary[0], custBox.getValue(), clauseId,
				priceTime, function(rtn) {
					// 取得随机数
					var rdm = getRandom();
					if (rtn != null) {
						var result = rtn[1];
						if (rtn[0] == 1) {
							result.type = 'price';
						} else if (rtn[0] == 2) {
							result.pricePrice = result.orderPrice;
							result.type = 'order';
						} else if (rtn[0] == 3) {
							result.pricePrice = result.priceOut;
							result.currencyId = result.priceOutUint;
							result.type = 'given';
						}
						result.srcId = result.id;
						result.id = null;
						result.rdm = rdm;
						if (havePrice == 0) {
							result.eleId = ary[0];
							result.pricePrice = ary[1];
							result.eleSizeDesc = ary[2];
							result.eleInchDesc = ary[3];
							result.boxCbm = ary[4];
							result.boxGrossWeigth = ary[5];
							result.boxNetWeigth = ary[6];
							result.taoUnit = ary[7];
							result.priceFac = ary[8];
							result.eleName = ary[9];
							result.boxIbCount = ary[10];
							result.boxMbCount = ary[11];
							result.boxObCount = ary[12];
							result.boxObL = ary[13];
							result.boxObW = ary[14];
							result.boxObH = ary[15];
							result.eleUnit = ary[16];
							result.eleNameEn = ary[17];
							result.priceFacUint = curBox.getValue();
							insertToGrid(result, false);
						}
						// 按价格条款计算
						if (havePrice == 1) {
							insertToGrid(result, true);
						}
						// 取最近一次报价
						if (havePrice == 2) {
							// 重新换算价格(币种不同时)
							queryService.updatePrice(result.pricePrice,
									result.currencyId, currencyId, function(
											newPri) {
										result.pricePrice = newPri
												.toFixed(deNum);
										result.currencyId = currencyId;
										insertToGrid(result, false);
									});
						}
					} else {
						var priceDetail = new CotPriceDetail();
						priceDetail.eleId = ary[0];
						priceDetail.pricePrice = ary[1];
						priceDetail.eleSizeDesc = ary[2];
						priceDetail.eleInchDesc = ary[3];
						priceDetail.boxCbm = ary[4];
						priceDetail.boxGrossWeigth = ary[5];
						priceDetail.boxNetWeigth = ary[6];
						priceDetail.taoUnit = ary[7];
						priceDetail.priceFac = ary[8];
						priceDetail.eleName = ary[9];
						priceDetail.boxIbCount = ary[10];
						priceDetail.boxMbCount = ary[11];
						priceDetail.boxObCount = ary[12];
						priceDetail.boxObL = ary[13];
						priceDetail.boxObW = ary[14];
						priceDetail.boxObH = ary[15];
						priceDetail.eleUnit = ary[16];
						priceDetail.eleNameEn = ary[17];
						priceDetail.priceFacUint = curBox.getValue();
						var rdm = getRandom();
						priceDetail.rdm = rdm;
						priceDetail.type = 'none';
						priceDetail.srcId = 0;
						if (noPrice == 0) {
							insertToGrid(priceDetail, false);
						}
						if (noPrice == 1) {
							insertToGrid(priceDetail, true);
						}
					}
				});
	}

	// 比对柜最大装箱重量
	function sumContainCount() {
		var rdm = $('rdm').value;
		if (rdm == '') {
			return;
		}
		var gross = $('boxGrossWeigth').value;
		if (gross == '' || isNaN(gross)) {
			Ext.MessageBox.show({
						title : 'Message',
						msg : 'Please enter the gross weight (only number)！',
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
		var rdm = $('rdm').value;
		if (rdm == '') {
			return;
		}
		var cbm = $('boxCbm').value;
		if (cbm == '' || isNaN(cbm)) {
			Ext.MessageBox.show({
						title : 'Message',
						msg : 'Please enter the CBM (only number)！',
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
		var rdm = $('rdm').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
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
		var rdm = $('rdm').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
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
		var rdm = $('rdm').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
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

	// 通过外包装数计算毛净重和装箱数
	function calWandC(txt, newVal, oldVal) {
		var rec = null;
		var rdm = $('rdm').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
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

	// 计算英寸
	function calInch(txt, newVal, oldVal) {
		// var rdm = $('rdm').value;
		// if (rdm == null || rdm == "") {
		// return;
		// }
		// var inch = (newVal / 2.54).toFixed("2");
		// var needId = txt.getName() + "Inch";
		// $(needId).value = inch;
		// updateMapValue(rdm, needId, inch);
		changeSizeTxt();
		// if (sizeFollow == 1) {
		// // 产品包装尺寸跟着变化
		// if (needId == 'boxLInch') {
		// $('boxPbL').value = $('boxL').value;
		// updateMapValue(rdm, "boxPbL", $('boxPbL').value);
		// $('boxPbLInch').value = $('boxLInch').value;
		// updateMapValue(rdm, "boxPbLInch", $('boxPbLInch').value);
		// }
		// if (needId == 'boxWInch') {
		// $('boxPbW').value = $('boxW').value;
		// updateMapValue(rdm, "boxPbW", $('boxPbW').value);
		// $('boxPbWInch').value = $('boxWInch').value;
		// updateMapValue(rdm, "boxPbWInch", $('boxPbWInch').value);
		// }
		// if (needId == 'boxHInch') {
		// $('boxPbH').value = $('boxH').value;
		// updateMapValue(rdm, "boxPbH", $('boxPbH').value);
		// $('boxPbHInch').value = $('boxHInch').value;
		// updateMapValue(rdm, "boxPbHInch", $('boxPbHInch').value);
		// }
		// }
		// calPrice();
	}

	// 计算CM
	function calCm(txt, newVal, oldVal) {
		var rdm = $('rdm').value;
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

	// 中英文规格跟着变化
	function changeSizeTxt() {
		var rdm = $('rdm').value;
		if (rdm == null || rdm == "") {
			return;
		}
		var str = "";
		if ($('boxL').value != '' && $('boxL').value != '0') {
			str += $('boxL').value + "x";
		}
		if ($('boxW').value != '' && $('boxW').value != '0') {
			str += $('boxW').value + "x";
		}
		if ($('boxH').value != '' && $('boxH').value != '0') {
			str += $('boxH').value + "cm";
			$('eleInchDesc').value = str;
		} else {
			if (str == "") {
				$('eleInchDesc').value = "";
			} else {
				$('eleInchDesc').value = str.substring(0, str.length - 1)
						+ "cm";
			}
		}

		updateMapValue(rdm, "eleInchDesc", $('eleInchDesc').value);

	};

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
		var rdm = $('rdm').value;
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
		cotPriceService.calPriceFacByPackPrice(rdm, packPrice, function(
						facprice) {
					if (facprice != -1 && index != -1) {
						var rec = ds.getAt(index);
						rec.set("priceFac", facprice);
					}
				});

	}

	this.exlFlag = false;
	this.eFlag;
	this.filename;
	this.isCover;
	// 保存
	this.saveByExcel = function(eFlag, filename, isCover) {
		_self.eFlag = eFlag;
		_self.filename = filename;
		_self.isCover = isCover;
		var popedom = checkAddMod($('pId').value);
		if (popedom == 1) {
			Ext.MessageBox
					.alert('Message',
							'Sorry, you do not have Authority! Pls. contact the webmaster!');
			return;
		} else if (popedom == 2) {
			Ext.MessageBox
					.alert('Message',
							'Sorry, you do not have Authority! Pls. contact the webmaster!');
			return;
		}
		// 审核通过不让修改
		if ($('priceStatus').value == 2 && loginEmpId != "admin") {
			Ext.Msg
					.alert("Message",
							'Sorry, this quotation has been vetted and approved can not be modified!');
			return;
		}

		// 验证表单
		var formData = getFormValues(priceForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		// 判断是不是需要根据价格公式计算
		if (eFlag) {
			if ($('clauseId').value == '') {
				Ext.MessageBox
						.alert(
								'Message',
								'According to excel configuration, you need to select and configure the corresponding terms of price quotations formula!');
				return;
			}
		}

		var price = DWRUtil.getValues('priceForm');
		var cotPrice = {};
		var checkFlag = false;
		// 如果编号存在时先查询出对象,再填充表单
		if ($('pId').value != 'null' && $('pId').value != '') {
			DWREngine.setAsync(false);
			cotPriceService.getPriceById($('pId').value, function(res) {
						for (var p in price) {
							if (p != 'priceTime') {
								res[p] = price[p];
							}
						}
						cotPrice = res;
					});
			DWREngine.setAsync(true);
			checkFlag = checkIsChangePice();
		} else {
			cotPrice = new CotPrice();
			for (var p in cotPrice) {
				if (p != 'priceTime') {
					cotPrice[p] = price[p];
				}
			}
		}
		DWREngine.setAsync(false);
		// 审核原因
		// if (Ext.getCmp("checkReason").isVisible()) {
		// cotPrice.checkReason = $('checkReason').value;
		// }
		cotPriceService.saveOrUpdatePrice(cotPrice, $('priceTime').value,
				checkFlag, oldPriceMap, liRunCau, function(res) {
					if (res != null) {
						// 当新建时,如果默认配置需要审核,保存后显示"提请审核"按钮
						if ($('pId').value == 'null') {
							// if ($('priceStatus').value == 0) {
							// Ext.getCmp("qingBtn").show();
							// }
							Ext.getCmp('saveAsBtn').show();
						}
						$('pId').value = res;

						// 如果订单明细没有修改
						if (ds.getModifiedRecords().length == 0
								&& ds.removed.length == 0) {
							// 直接导入excel
							saveReport(eFlag, filename, isCover);
						} else {
							// 更改添加action参数
							var urlAdd = '&pricePrimId=' + res + '&currencyId='
									+ $('currencyId').value + '&custId='
									+ $('custId').value;
							// 更改修改action参数
							var urlMod = '&custId=' + $('custId').value
									+ '&currencyId=' + $('currencyId').value;
							ds.proxy.setApi({
								read : "cotprice.do?method=queryPriceDetail&priceId="
										+ res,
								create : "cotprice.do?method=add" + urlAdd,
								update : "cotprice.do?method=modify" + urlMod,
								destroy : "cotprice.do?method=remove"
							});
							_self.exlFlag = true;
							ds.save();
						}
						reflashParent('priceGrid');
					} else {
						Ext.MessageBox.alert('Message', 'Save failed');
						unmask();
					}
				});
		DWREngine.setAsync(true);
	}

	// 新建
	function windowopen() {
		// 验证表单
		var formData = getFormValues(modlElePanel, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var byId = $('byId').value;
		var myId = $('myId').value;
		var syId = $('syId').value;
		var facId = $('facEleId').value;
		var tempoBtn = $('tempoBtn').checked;
		if (tempoBtn) {
			var ps = "Temp_" + Math.round(Math.random() * 10000000);
			openWindowBase(500, 1020,
					'./cotelements.do?method=addElements&eleNo=' + ps
							+ '&byId=' + byId + '&myId=' + myId + '&syId='
							+ syId + '&facId=' + facId + '&chk=0');
			modlElePanel.hide();
		} else {
			if (byId == '' || myId == '' || syId == '' || facId == '') {
				Ext.MessageBox
						.alert('Message',
								'Please choose Dep.of PRODUCT and Group and Category and Supplier!')
			} else {
				cotSeqService.getEleNo(byId, myId, syId, facId, function(ps) {
							openWindowBase(500, 1020,
									'./cotelements.do?method=addElements&eleNo='
											+ ps + '&byId=' + byId + '&myId='
											+ myId + '&syId=' + syId
											+ '&facId=' + facId + '&chk=0');
							modlElePanel.hide();
						});
			}
		}

	}

	// 保存excel
	function saveReport(eFlag, filename, isCover) {
		var infoPanel = Ext.getCmp('infoPanel');
		cotPriceService.saveReport($('pId').value, filename, isCover, curBox
						.getValue(), eFlag, function(msgList) {
					if (msgList == null) {
						infoPanel.body.dom.innerHTML = "File not found or the file is not excel！";
					} else {
						if (msgList.length == 1) {
							if (msgList[0].flag == 1) {
								infoPanel.body.dom.innerHTML = "<font color=red>"
										+ msgList[0].msg + "</font>";
							} else {
								infoPanel.body.dom.innerHTML = "Import successful&nbsp(<label>"
										+ msgList[0].successNum
										+ "</label>)&nbsp，Import failed&nbsp(<label>"
										+ msgList[0].failNum
										+ "</label>)&nbsp，Coverage of successful&nbsp(<label>"
										+ msgList[0].coverNum + "</label>)条。";
								_self.insertSelectExcel(eFlag);
							}
						} else {
							var htm = '<div class="detail_nav"><label>Error Message(Modify the excel file to re-upload)</label></div><div style="height: 360px; overflow: auto;">';
							var temp = '';
							for (var i = 0; i < msgList.length; i++) {
								if (i != msgList.length - 1) {
									htm += "<div id=err"
											+ i
											+ ">第&nbsp("
											+ (msgList[i].rowNum + 1)
											+ ")&nbsp行，第&nbsp("
											+ (msgList[i].colNum + 1)
											+ ")&nbspcolumns Error Message："
											+ msgList[i].msg
											+ "&nbsp;&nbsp;<a href=# onclick=reportload("
											+ i + "," + msgList[i].rowNum
											+ ")>【Re-upload】</a></div><br/>";
								} else {
									susNum = msgList[i].successNum;
									failNum = msgList[i].failNum;
									temp = '导入成功&nbsp(<label id="successNumLab">'
											+ msgList[i].successNum
											+ "</label>)&nbsp条，Import failed&nbsp(<label id='failNumLab'>"
											+ msgList[i].failNum
											+ "</label>)&nbsp条，Coverage of successful&nbsp(<label id='coverNumLab'>"
											+ msgList[i].coverNum
											+ "</label>)条。";
									if (susNum > 0 || msgList[i].coverNum > 0) {
										_self.insertSelectExcel(eFlag);
									}
								}
							}
							infoPanel.body.dom.innerHTML = temp + '</div>'
									+ htm;
						}
					}
				});
	}

	// 设置退税率
	function setTuiLv(hsId) {
		var rec = null;
		var rdm = $('rdm').value;
		if (rdm == null || rdm == "") {
			return;
		}
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
		if (index != -1) {
			rec = ds.getAt(index);
		}
		updateMapValue(rdm, "eleHsid", hsId);
		DWREngine.setAsync(false);
		cotPriceService.getTuiLv(hsId, function(ft) {
					if (ft != null) {
						rec.set("tuiLv", ft);
						updateMapValue(rdm, "tuiLv", ft);
						// 计算价格
						// var ck = rec.data.checkFlag;
						// if (ck == null || ck == 0 || ck == false) {
						// 将计算单价的参数组成一个map
						var mapPrice = {};
						// mapPrice.clauseId = $('clauseId').value;// 价格条款
						mapPrice.clauseId = 1;// 价格条款
						mapPrice.currencyId = $('currencyId').value;// 币种
						mapPrice.priceProfit = rec.data.priceProfit;// 利润率
						mapPrice.tuiLv = ft;// 退税率
						mapPrice.priceFob = $('priceFob').value;// FOB
						mapPrice.priceRate = $('priceRate').value;// 汇率
						mapPrice.priceCharge = $('priceCharge').value;// 整柜运费
						mapPrice.insureRate = $('insureRate').value;// 保险费率
						mapPrice.insureAddRate = $('insureAddRate').value;// 保险加成率
						mapPrice.containerTypeId = $('containerTypeId').value;// 集装箱类型
						cotPriceService.getNewPriceByTuiLv(mapPrice, rdm,
								function(res) {
									if (res != null) {
										rec.set("pricePrice", res
														.toFixed(deNum));
										updateMapValue(rdm, "pricePrice", res
														.toFixed(deNum));
									}
								})
						// }
					}
				});
		DWREngine.setAsync(true);
	}

	function changeBarcode() {
		var rdm = $('rdm').value;
		if (rdm == null || rdm == "") {
			return;
		}
		var bar = setBarcodeNo();
		DWREngine.setAsync(false);
		cotPriceService.updateMapValueByEleId(rdm, "barcode", bar,
				function(res) {
					var index = ds.find('rdm', rdm);
					if (index != -1) {
						var record = ds.getAt(index);
						if (record.data.rdm == rdm) {
							var temp = record.data.rdm;
							record.set("rdm", temp + "aaa");
							record.set("rdm", temp);
						}
					}
				});
		DWREngine.setAsync(true);
	}

	// 显示模板面板
	var modlElePanel;
	function showEleModel(item, pressed) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType("cotelements.do", "ADD");
		if (isPopedom == 0) {
			Ext.Msg.alert("Message", "You do not have permission to add!");
			return;
		}

		if ($('importDiv') == null) {
			// window默认在z-index为9000
			Ext.DomHelper.append(document.body, {
				html : '<div id="importDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (modlElePanel == null) {
			modlElePanel = new Ext.form.FormPanel({
						labelWidth : 90,
						padding : "5px",
						height : 210,
						width : 290,
						layout : 'form',
						frame : true,
						title : "Create New No.",
						labelAlign : "right",
						tools : [{
									id : "close",
									handler : function(event, toolEl, p) {
										p.hide();
									}
								}],
						buttonAlign : 'right',
						buttons : [{
									text : "Create",
									iconCls : "page_table_save",
									handler : windowopen
								}],
						items : [{
									xtype : "checkbox",
									boxLabel : "Temporary Art No.",
									hideLabel : true,
									anchor : "100%",
									id : "tempoBtn"
								}, {
									xtype : 'panel',
									layout : 'form',
									id : 'tempoPnl',
									items : [typeBox, type2Box, type3Box,
											facEleBox]
								}]
					});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("importDiv").setLeftTop(left - 80, top + 30);
			modlElePanel.render("importDiv");
			// $('modelEle').focus();
		} else {
			if (!modlElePanel.isVisible()) {
				modlElePanel.show();
			} else {
				modlElePanel.hide();
			}
		}
	};
});

// 锁定价格列全选/反选
function checkAll(obj) {
	checkColumn.checkAll(obj.checked);
}
