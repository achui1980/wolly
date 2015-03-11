window.onbeforeunload = function() {
	var ds = Ext.getCmp('outDetailGrid').getStore();
	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
		if (Ext.isIE) {
			if (document.body.clientWidth - event.clientX < 170
					&& event.clientY < 0 || event.altKey) {
				return "出货明细数据有更改,您确定不保存吗?";
			} else if (event.clientY > document.body.clientHeight
					|| event.altKey) { // 用户点击任务栏，右键关闭
				return "Shipping detail has some changed,are you sure to save it?";
			} else { // 其他情况为刷新
			}
		} else if (Ext.isChrome || Ext.isOpera) {
			return "Shipping detail has some changed,are you sure not to save it?";
		} else if (Ext.isGecko) {
			window.open("http://www.g.cn")
			var o = window.open("index.do?method=logoutAction");
		}
	}
}

var checkColumn = null;
// 窗口关闭触发事件
window.onunload = function() {
	cotOrderOutService.clearMap("orderout", function(res) {
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
	var _self = this;
	// 用于根据模版添加不存在样品时(newEleAdd.js)
	this.parentType = 'order';

	DWREngine.setAsync(false);
	var facData;
	var curData;
	var typeData;
	var orderOutMap;
	var nationMap;
	var cityMap;
	baseDataUtil.getBaseDicDataMap("CotNation", "id", "nationName", function(
					res) {
				nationMap = res;
			});
	baseDataUtil.getBaseDicDataMap("CotNationCity", "id", "cityName", function(
					res) {
				cityMap = res;
			});
	// 加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotOrderOut', function(rs) {
				orderOutMap = rs;
			});
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
				mode : 'local',
				dataUrl : "servlet/DataSelvert?tbname=CotCustContact",
				emptyText : 'Choose',
				fieldLabel : "Contact",
				displayField : "contactPerson",
				valueField : "id",
				sendMethod : "post",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 3
			});

	// 客户名称
	var custBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		fieldLabel : "<font color=red>Customer</font>",
		editable : true,
		disabled:true,
		disabledClass : 'combo-disabled',
		valueField : "id",
		displayField : "customerShortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "Please select a client！",
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
				var custId=custBox.getValue();
				if(!Ext.isEmpty(custId)){
					cotCustomerService.getCustomerById(custId, function(res) {
								if (res != null) {
									var city='';
									var nation='';
									var fullName='';
									var adr='';
									var post='';
									if(!Ext.isEmpty(res.customerPost)){
										post= res.customerPost+','
									}
									if(!Ext.isEmpty(res.fullNameEn)){
										fullName=res.fullNameEn+' \n'
									}
									if(!Ext.isEmpty(res.customerAddrEn)){
										adr=res.customerAddrEn+' \n'
									}
									if(!Ext.isEmpty(cityMap[res.cityId])){
										city=cityMap[res.cityId]+' \n'
									}
									if(!Ext.isEmpty(nationMap[res.nationId])){
										nation=nationMap[res.nationId];
									}
									var str = fullName + adr + post+city + nation;
									var start=str.indexOf(',');
									var end=str.lastIndexOf(',');
									var strLength=str.length-2;

									if(start==end){
										if(start==strLength){
											str=str.substring(0,strLength);
										}
									}else{
										str =str.substring(0,end);
									}
									Ext.getCmp('buyer').setValue(str);
								}
							});
				}
			}
		}
	});

	// 出口公司
	var companyBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
				cmpId : 'companyId',
				fieldLabel : "Company",
				editable : true,
				valueField : "id",
				displayField : "companyShortName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				tabIndex : 5,
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
		blankText : "Please select a sale！",
		tabIndex : 4,
		sendMethod : "post",
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 集装箱
	var containerBox = new BindCombox({
				cmpId : 'containerTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotContainerType",
				emptyText : 'Choose',
				fieldLabel : "Container",
				displayField : "containerName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 16
			});

	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Category",
				editable : true,
				valueField : "id",
				allowBlank : true,
				disabled : true,
				disabledClass : 'combo-disabled',
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
				disabled : true,
				disabledClass : 'combo-disabled',
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
				disabledClass : 'combo-disabled',
				fieldLabel : "<font color=red>Currency</font>",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				allowBlank : false,
				blankText : "Please select currency！",
				tabIndex : 7,
				listeners : {
					"change" : function(com, newValue, oldValue) {
						composeChange();
						priceBlurByCurreny(newValue, oldValue);
					}
				}
			});

	// 表格--币种
	var curGridBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				hideLabel : true,
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

	// 条款类型
	var clauseBox = new BindCombox({
				cmpId : 'clauseTypeId',
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
						// priceBlur();
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
				tabIndex : 13,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,
				listeners : {
					"select" : function(com, rec, index) {
						composeChange();
						$('shPortName').value = rec.data.shipPortNameEn;
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
		tabIndex : 14,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		listeners : {
			"select" : function(com, rec, index) {
				composeChange();
				$('taPortName').value = rec.data.targetPortEnName;
			}
		}
	});

	// 中转港
	var middlePortBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotTargetPort&key=targetPortEnName",
		cmpId : 'midportId',
		fieldLabel : "Transshipment port",
		editable : true,
		valueField : "id",
		allowBlank : true,
		displayField : "targetPortEnName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 419,
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
	// 佣金类型
	var commisionBox = new BindCombox({
				cmpId : 'commisionTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotCommisionType",
				emptyText : 'Choose',
				fieldLabel : "Commission",
				displayField : "commisionName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 9,
				listeners : {
					"select" : function(com, rec, index) {
						composeChange();
					}
				}
			});

	// 折扣
	var zheStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'Not Discount'], [1, 'Price']]
			});
	var zheBox = new Ext.form.ComboBox({
				name : 'zheType',
				tabIndex : 20,
				fieldLabel : 'Discount',
				editable : false,
				// emptyText : 'Choose',
				store : zheStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				value : 0,// 默认不打折
				hiddenName : 'zheType',
				selectOnFocus : true,
				listeners : {
					"select" : function(com, rec, index) {
						checkZheType();
					},
					"focus" : function(com, rec, index) {
						cunZheNum();
					}
				}
			});

	// 运输方式
	var trafficBox = new BindCombox({
				cmpId : 'trafficId',
				dataUrl : "servlet/DataSelvert?tbname=CotTrafficType",
				emptyText : 'Choose',
				fieldLabel : "Transportation",
				displayField : "trafficNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 17
			});

	// 国别
	var nationBox = new BindCombox({
				cmpId : 'nationId',
				dataUrl : "servlet/DataSelvert?tbname=CotNation&key=nationName",
				emptyText : 'Choose',
				fieldLabel : "<font color=red>Country</font>",
				displayField : "nationName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 16,
				editable : true,
				allowBlank:false,
				emptyText : 'Choose',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350
			});

	// 银行
	var bankBox = new BindCombox({
				cmpId : 'bankId',
				dataUrl : "servlet/DataSelvert?tbname=CotBank",
				emptyText : 'Choose',
				fieldLabel : "Bank",
				displayField : "bankShortName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 17
			});

	// 付款方式
	var payTypeBox = new BindCombox({
				cmpId : 'paTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotPayType",
				emptyText : 'Choose',
				fieldLabel : "Pay.Terms",
				displayField : "payName",
				valueField : "id",
				tabIndex : 12,
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 100
			});

	// 报关行
	var hsCompanyBox = new BindCombox({
				cmpId : 'hsCompanyId',
				dataUrl : "servlet/DataSelvert?tbname=CotHsCompany",
				emptyText : 'Choose',
				fieldLabel : "Customs Broker",
				displayField : "hsCompanyName",
				valueField : "id",
				triggerAction : "all",
				anchor : "98%",
				tabIndex : 426,
				listeners : {
					'select' : hsCompanyBoxSel
				}
			});

	// 拖车行
	var trailCarBox = new BindCombox({
				cmpId : 'trailerId',
				dataUrl : "servlet/DataSelvert?tbname=CotTrailCar",
				emptyText : 'Choose',
				fieldLabel : "Towing company",
				displayField : "name",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 427,
				listeners : {
					'select' : trailCarBoxSel
				}
			});

	// 船公司
	var shipBox = new BindCombox({
				cmpId : 'shipId',
				dataUrl : "servlet/DataSelvert?tbname=CotShipCompany",
				emptyText : 'Choose',
				fieldLabel : "Shipping companies",
				displayField : "companyName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 422,
				listeners : {
					'select' : shipBoxSel
				}
			});

	// 操作货代
	var hsConsignBox = new BindCombox({
				cmpId : 'hsConsignCompanyId',
				dataUrl : "servlet/DataSelvert?tbname=CotConsignCompany",
				emptyText : 'Choose',
				fieldLabel : "Freight",
				displayField : "companyName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 401,
				listeners : {
					'select' : hsConsignBoxSel
				}
			});

	// 指定货代
	var consignBox = new BindCombox({
				cmpId : 'consignCompanyId',
				dataUrl : "servlet/DataSelvert?tbname=CotConsignCompany",
				emptyText : 'Choose',
				fieldLabel : "Freight",
				displayField : "companyName",
				valueField : "id",
				triggerAction : "all",
				anchor : "98%",
				tabIndex : 400,
				listeners : {
					'select' : consignBoxSel
				}
			});

	// 审核状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'Non-reviewed'], [1, 'Review without'],
						[2, 'reviewed'], [3, 'Need to be reviewed'],
						[9, 'Not review ']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				tabIndex : 21,
				disabled : true,
				disabledClass : 'combo-disabled',
				fieldLabel : 'Status',
				editable : false,
				value : 9,
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'orderStatus',
				selectOnFocus : true
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
				disabled : true,
				disabledClass : 'combo-disabled',
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
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,
				sendMethod : "post",
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
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		sendMethod : "post",
		pageSize : 10,
		anchor : "100%",
		disabled : true,
		disabledClass : 'combo-disabled',
		tabIndex : 42,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

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
				disabled : true,
				disabledClass : 'combo-disabled',
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 36,
				emptyText : 'Choose',
				hiddenName : 'eleFlag',
				selectOnFocus : true
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
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxType",
				valueField : "id",
				//fieldLabel : "Packing Way",
				hidden:true,
				disabled : true,
				disabledClass : 'combo-disabled',
				tabIndex : 63,
				displayField : "typeName",
				cmpId : "boxTypeId",
				emptyText : 'Choose',
				anchor : "95%"
			});
	// 产品类型
	var boxPbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=PB",
				valueField : "id",
				fieldLabel : "Material Type",
				tabIndex : 65,
				displayField : "value",
				cmpId : "boxPbTypeId",
				emptyText : 'Choose',
				disabled : true,
				disabledClass : 'combo-disabled',
				labelSeparator : " ",
				anchor : "100%"
			});
	// 内包装类型
	var boxIbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IB",
				valueField : "id",
				fieldLabel : "Material Type",
				tabIndex : 70,
				displayField : "value",
				cmpId : "boxIbTypeId",
				disabled : true,
				disabledClass : 'combo-disabled',
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%"
			});
	// 中包装类型
	var boxMbTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=MB",
				valueField : "id",
				fieldLabel : "/",
				tabIndex : 63,
				displayField : "value",
				cmpId : "boxMbTypeId",
				disabled : true,
				disabledClass : 'combo-disabled',
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%"
			});
	// 外包装类型
	var boxObTypeBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=OB",
				valueField : "id",
				fieldLabel : "Material Type",
				tabIndex : 75,
				displayField : "value",
				cmpId : "boxObTypeId",
				disabled : true,
				disabledClass : 'combo-disabled',
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%"
			});

	// 插格类型
	var inputGridTypeIdBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotBoxPacking&type=IG",
				valueField : "id",
				fieldLabel : "插格类型",
				editable : true,
				tabIndex : 54,
				disabled : true,
				disabledClass : 'combo-disabled',
				displayField : "value",
				cmpId : "inputGridTypeId",
				emptyText : 'Choose',
				anchor : "100%"
			});
	// 大类
	var typeLv1IdBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
				cmpId : 'typeLv1Id',
				fieldLabel : "<font color ='red'>Department</font>",
				editable : true,
				valueField : "id",
				displayField : "typeEnName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor:'100%',
				sendMethod : "post",
				readOnly:true,
				selectOnFocus : true,
				allowBlank : false,
				blankText : "Choose Dep. Of PRODUCT！",
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
				name : "barcode"
			},{
				name : "orderName"
			}, {
				name : "checkJian"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "orderPrice",
				type : "float",
				convert : numFormat.createDelegate(this, [strNum], 3)
			}, {
				name : "taoUnit"
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
				name : "unSendNum",
				type : "int"
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
			}, {
				name : "orderNo"
			}, {
				name : "poNo"
			}, {
				name : "remainBoxCount"
			}, {
				name : "remainTotalCbm"
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
				read : "cotorderout.do?method=queryOrderDetail&orderId="
						+ $('pId').value,
				create : "cotorderout.do?method=add",
				update : "cotorderout.do?method=modify",
				destroy : "cotorderout.do?method=remove"
			},
			listeners : {
				beforeload : function(store, options) {
					ds.removed = [];
//					cotOrderOutService.clearMap("orderout", function(res) {
//							});
				},
				load : function(store, recs, options) {
					options.callback = function(r, ops, success) {
						if (success == false) {
							Ext.Msg
									.alert("Message",
											"Tt goes something wrong ,please contact the webmaster!");
						} else {
							var temp = summary.getSumTypeValue('totalMoney');
							$('totalLab').innerText = Number(temp)
									.toFixed(deNum);
						}
					}
				},
				// 添加和修改后进入
				exception : function(proxy, type, action, options, res, arg) {
					// 从异常中的响应文本判断是否成功
					if (res.status != 200) {
						Ext.Msg.alert("Message", "Operation Failed！");
					} else {
						ds.reload();
						// 保存报关明细
						if (freshFlag == true) {
							var frame = window.frames["outInfo"];
							frame
									.saveCha($('pId').value,
											$('currencyId').value);
						} else {
							// 更改主单的总金额,总数量...
							cotOrderOutService.updateOrderOutTotal(
									$('pId').value, function(ary) {
										if (ary != null) {
											if (Ext.getCmp("totalGross")
													.isVisible()) {
												if (ary[0] != null) {
													$('totalMoney').value = Number(ary[0])
															.toFixed(deNum);
												}
												if (ary[1] != null) {
													$('totalCount').value = ary[1];
												}
												if (ary[2] != null) {
													$('totalContainer').value = ary[2];
												}
												if (ary[3] != null) {
													$('totalCbm').value = ary[3];
												}
												if (ary[4] != null) {
													$('totalNet').value = ary[4];
												}
												if (ary[5] != null) {
													$('totalGross').value = ary[5];
												}
												if (ary[6] != null) {
													$('totalHsMoney').value = Number(ary[6])
															.toFixed(deNum);
												}
												if (ary[7] != null) {
													$('totalHsCount').value = ary[7];
												}
												if (ary[8] != null) {
													$('totalHsContainer').value = ary[8];
												}
												if (ary[9] != null) {
													$('totalHsCbm').value = ary[9];
												}
												if (ary[10] != null) {
													$('totalHsNet').value = ary[10];
												}
												if (ary[11] != null) {
													$('totalHsGross').value = ary[11];
												}
											}
											$('totalLab').innerText = Number(ary[0])
													.toFixed(deNum);
//											$('totalChaLab').innerText = Number(ary[6])
//													.toFixed(deNum);
											loadbar(0);
											reflashParent('orderoutGrid');
										}
									});
						}
						// 保存其他费用
						if (freshRecvFlag == true) {
							var frame = window.frames["recvInfo"];
							frame.saveOther();
						}
						// 保存应付款其他费用
						if (freshDealFlag == true) {
							var frame = window.frames["dealFeeInfo"];
							frame.saveOther();
						}
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
				hidden:true,
				align : 'center',
				dataIndex : 'checkJian',
				menuDisabled : true,
				tooltip : "weather inspection or not",
				width : 35
			});

	// 储存数量单元格获得焦点事件的文本框坐标
	var ckX;
	var ckY;

	// 创建需要在表格显示的列
	var cm = new HideColumnModel({
				defaults : {
					sortable : true,
					editable:false
				},
				hiddenCols : orderOutMap,
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "<font color=blue>Sort</font>",
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
							width : 130,
							summaryRenderer : function(v, params, data) {
								return "Total：";
							}
						}, {
							header : "Cust.No.",
							dataIndex : "custNo",
							width : 90
						},  {
							header : "Barcode",
							dataIndex : "barcode",
							width : 120
						},{
							header : "W&C P/O",
							dataIndex : "orderNo",
							hidden : true,
							width : 120
						}, {
							header : "ClientP/O",
							dataIndex : "poNo",
							hidden : true,
							width : 120
						}, {
							header : "<font color=blue>Des. of Category</font>",
							dataIndex : "orderName",
							hidden : true,
							editor : new Ext.form.TextField({
										maxLength : 50,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									}),
							width : 80
						}, checkColumn, {
							header : "Descripiton",
							hidden : true,
							dataIndex : "eleName",
							width : 100
						}, {
							header : "Descripiton",
							dataIndex : "eleNameEn",
							width : 100
						}, {
							header : "Sales Price",
							dataIndex : "orderPrice",
							width : 80
						}, {
							header : "Unit",
							dataIndex : "eleUnit",
							width : 35
						}, {
							header : "Unit",
							hidden : true,
							dataIndex : "taoUnit",
							width : 60
						}, {
							header : "<font color=blue>Quantity</font>",
							dataIndex : "boxCount",
							width : 60,
							summaryType : 'sum',
							editable:true,
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
												var temp = txt.getPosition();
												ckX = temp[0] + txt.getWidth();
												ckY = temp[1];
											}
										}
									})
						}, {
							header : "<font color=blue>Cartons</font>",
							dataIndex : "containerCount",
							width : 60,
							summaryType : 'sum',
							editable:true,
							editor : new Ext.form.NumberField({
										maxValue : 99999999,
										decimalPrecision : 0,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
												var temp = txt.getPosition();
												ckX = temp[0] + txt.getWidth();
												ckY = temp[1];
											}
										}
									})
						},{
							header : "Size Description",
							dataIndex : "eleInchDesc",
							width : 100
						}, {
							header : "Amount",
							//hidden : true,
							dataIndex : "totalMoney",
							width : 85,
							summaryType : 'sum'
						}, {
							header : "remain Quantity",
							hidden : true,
							dataIndex : "unSendNum",
							width : 100
						}, {
							header : "Unsplited Quatity",
							hidden : true,
							dataIndex : "remainBoxCount",
							editable : false,
							width : 60
						}, {
							header : "Unsplited Cartons",
							hidden : true,
							dataIndex : "remainTotalCount",
							editable : false,
							width : 80,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var remainBoxCount = record
										.get("remainBoxCount");
								var boxObCount = record.get("boxObCount");
								var remainCount = Math.ceil(remainBoxCount
										/ boxObCount);
								return remainCount;
							}
						}, {
							header : "Supplier",
							dataIndex : "factoryId",
							width : 100,
							renderer : function(value) {
								return facData[value];
							}
						}, {
							header : "Supplier's A.No.",
							dataIndex : "factoryNo",
							width : 100
						}, {
							header : "Purchase Price",
							dataIndex : "priceFac",
							width : 100
						}, {
							header : "Currency",
							dataIndex : "priceFacUint",
							width : 80,
							renderer : function(value) {
								return curData[value];
							}
						}, {
							header : "Inner Box",
							dataIndex : "boxIbCount",
							width : 80
//							editor : new Ext.form.NumberField({
//										maxValue : 99999999,
//										decimalPrecision : 0,
//										listeners : {
//											'focus' : function(txt) {
//												txt.selectText();
//											}
//										}
//									})
						}, {
							header : "<font color=blue>中</font>",
							dataIndex : "boxMbCount",
							hidden : true,
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
							header : "<font color=blue>Length Of outer carton</font>",
							dataIndex : "boxObL",
							hidden : true,
							width : 50,
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
							header : "<font color=blue>Width of outer carton</font>",
							dataIndex : "boxObW",
							hidden : true,
							width : 50,
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
							header : "<font color=blue>height of outer carton</font>",
							dataIndex : "boxObH",
							hidden : true,
							width : 50,
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
							width : 60
						}, {
							header : "N.W(Kgs)",
							dataIndex : "boxNetWeigth",
							width : 80
						}, {
							header : "G.W(Kgs)",
							dataIndex : "boxGrossWeigth",
							width : 80
						}, {
							header : "Size Description(CM)",
							dataIndex : "eleSizeDesc",
							hidden : true,
							width : 90
						}, {
							header : "Size Description(INCH)",
							dataIndex : "eleInchDesc",
							hidden : true,
							width : 100
						}, {
							header : "orderDetailId",
							dataIndex : "orderDetailId",
							editor : new Ext.form.TextField({
										maxLength : 100
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
							text : "Create",
							handler : addOrders,
							iconCls : "page_add"
						}, '-', {
							text : "Del",
							handler : onDel,
							iconCls : "page_del"
						}, '-',
						// {
						// text : "Description of Category",
						// handler : showModel,
						// iconCls : "page_tong",
						// cls : "SYSOP_PRINT"
						// }, '-',
						{
							text : "Import",
							handler : excelImport,
							iconCls : "gird_list"
						}]
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	// 订单明细表格
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "outDetailGrid",
				stripeRows : true,
				margins : "0 5 0 0",
				bodyStyle : 'width:100%',
				store : ds,
				cm : cm,
				sm : sm,
				plugins : [checkColumn, summary],
				loadMask : true,
				//tbar : tb,
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
							text : "are you sure to save the current sort?",
							handler : sumSortTable
						}]
			});
	function rightClickFn(client, rowIndex, e) {
		e.preventDefault();
		rightMenu.showAt(e.getXY());
	}
	grid.on("rowcontextmenu", rightClickFn);

	// 单元格双击事件
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'提示消息',
							'Sorry, It has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return false;
		}
	});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	grid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后
	grid.on("afteredit", function(e) {
//				var remainBoxCount = e.record.get("remainBoxCount");
//				var splitCount = e.originalValue - remainBoxCount
				if (e.field == 'boxCount') {// 出货数量
//					if (e.value < splitCount) {
//						e.record.set("boxCount", e.originalValue);
//						var tip = new Ext.ToolTip({
//									title : '超额提示',
//									anchor : 'left',
//									html : '您输入的数量<已排载数量,请修改排载信息！'
//								});
//						tip.showAt([ckX, ckY]);
//						return;
//					}
					countNum(e.value);
//					changeUnSplit(e.record, e.value, e.originalValue);
				} else if (e.field == 'containerCount') {// 出货箱数
//					if (e.value < splitCount) {
//						e.record.set("boxCount", e.value);
//						var tip = new Ext.ToolTip({
//									title : '超额提示',
//									anchor : 'left',
//									html : '您输入的数量<已排载数量,请修改排载信息！'
//								});
//						tip.showAt([ckX, ckY]);
//						return;
//					}
					containNum(e.value);
//					var boxObCount = e.record.get("boxObCount");
//					changeUnSplit(e.record, e.value * boxObCount,
//							e.originalValue * boxObCount);
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
				} else if (e.field == 'boxCbm') {// cbm
					changeCbm(e.value);
				} else {
					updateMapValue(e.record.data.orderDetailId, e.field,
							e.value)
				}
			});
	// 行点击时加载右边折叠面板表单
	grid.on("rowclick", function(grid, rowIndex, e) {
				// 如果右边折叠面板有展开过才加载数据
				if (rightForm.isVisible()) {
					initForm(ds.getAt(rowIndex));
				}
			});

	// 右边隐藏的字段
	var hiddenItemsRight = new Ext.Panel({
				layout : 'form',
				hidden : true,
				items : [typeLv1Box, eleFlagBox, {
							xtype : "numberfield",
							fieldLabel : "Pieces",
							anchor : "100%",
							id : "eleUnitNum",
							name : "eleUnitNum",
							disabledClass : 'combo-disabled',
							tabIndex : 37,
							decimalPrecision : 0,
							maxValue : 1000000000
						}, typeLv2Box, {
							xtype : "numberfield",
							fieldLabel : "Mold Cost",
							anchor : "100%",
							tabIndex : 43,
							decimalPrecision : 3,
							id : "moldCharge",
							name : "moldCharge",
							maxValue : 99999.999
						}, {
							xtype : "textfield",
							fieldLabel : "YY-MM-DD",
							anchor : "100%",
							id : "eleTypenameLv2",
							name : "eleTypenameLv2",
							tabIndex : 44,
							maxLength : 20
						}, {
							xtype : "numberfield",
							fieldLabel : "Cube",
							anchor : "100%",
							tabIndex : 45,
							id : "cube",
							name : "cube",
							decimalPrecision : 2,
							maxValue : 999999.99
						}, {
							xtype : "textarea",
							fieldLabel : "Description",
							anchor : "100%",
							tabIndex : 48,
							height : 40,
							id : "eleDesc",
							name : "eleDesc",
							maxLength : 500
						}, eleHsidBox, inputGridTypeIdBox, {
							xtype : "numberfield",
							fieldLabel : "Packing of middle box",
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
							fieldLabel : "Middle BoxCM",
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
							fieldLabel : "Sales Unit",
							anchor : "100%",
							height : 40,
							tabIndex : 109,
							id : "boxRemark",
							name : "boxRemark",
							maxLength : 500
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
							xtype : "textfield",
							fieldLabel : "Barcode",
							anchor : "100%",
							tabIndex : 45,
							id : "barcode",
							name : "barcode"
						}, {
							xtype : "hidden",
							id : "id",
							name : "id"
						}, {
							xtype : "hidden",
							id : "orderDetailId",
							name : "orderDetailId"
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
													fieldLabel : "Label",
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
			hidden:true,
			anchor : '95%',
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
			hidden:true,
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
			anchor : '95%',
			hidden:true,
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
			anchor : '95%',
			hidden:true,
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
									fieldLabel : "Outer Carton",
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
				pnl.getEl().mask("Loading...", 'x-mask-loading');
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
							if (txt.getName() == 'boxObL'
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
							if (txt.getName() == 'boxObLInch'
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
				data : [[0, 'ASC'], [1, 'Desc']]
			});
	var sortComb = new Ext.form.ComboBox({
				name : 'sortSel',
				tabIndex : 4,
				fieldLabel : 'Ways',
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
				items : [commisionBox, shenBox, {
							xtype : 'textfield',
							fieldLabel : "条款组合",
							anchor : "100%",
							id : 'orderCompose',
							name : 'orderCompose',
							tabIndex : 14,
							maxLength : 500
						}, bankBox, {
							xtype : 'textfield',
							fieldLabel : "L/C NO.",
							anchor : "100%",
							id : 'creditNo',
							name : 'creditNo',
							tabIndex : 19,
							maxLength : 500
						}, {
							xtype : 'textfield',
							fieldLabel : "Invice No.",
							anchor : "100%",
							id : 'orderNumber',
							name : 'orderNumber',
							tabIndex : 18,
							maxLength : 500
						}, {
							xtype : 'textfield',
							fieldLabel : "Verification No.",
							id : 'auditNo',
							name : 'auditNo',
							anchor : "100%",
							tabIndex : 20,
							maxLength : 500
						}]
			});

	// 判断单号是否重复参数
	var isExist = true;
	var orderForm = new Ext.form.FormPanel({
		title : "Base Information-(Red is required)",
		labelWidth : 80,
		collapsible : true,
		formId : "orderForm",
		labelAlign : "right",
		region : 'north',
		layout : "column",
		width : "100%",
		height : 140,
		frame : true,
		listeners : {
			'collapse' : function(pnl) {
				Ext.Element.fly("talPriceDiv").setLeftTop(700, 35);
			},
			'expand' : function(pnl) {
				Ext.Element.fly("talPriceDiv").setLeftTop(700, 143);
			}
		},
		items : [{
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 100,
					items : [custBox, {
								xtype : "datefield",
								fieldLabel : "Date",
								anchor : "100%",
								id : "orderTime",
//								value : new Date(),
								name : "orderTime",
								tabIndex : 6,
								format : "Y-m-d"
//								listeners : {
//									'select' : function(field, date) {
//										var dt = new Date(date);
//										var date = dt.format('Y-m-d');
//										var dt2 = new Date();
//										var time = dt2.format('H:i:s');
//										field.setValue(date + " " + time);
//									}
//								}
							}, shipPortBox]
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
								fieldLabel : "<font color=red>Invoice No</font>",
								anchor : "100%",
								id : "orderNo",
								tabIndex : 2,
								blankText : "Please enter W&C P/O.！",
								name : "orderNo",
								maxLength : 100,
								allowBlank : false,
								//readOnly:true,
								invalidText : "W&C P/O. already exists, please re-enter！",
								validationEvent : 'change',
								validator : function(thisText) {
									if (thisText != '') {
										cotOrderOutService.findIsExistOrderNo(
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
											html : 'Click the right button to automatically generate an No.!'
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
							handler : getOrderNo,
							listeners : {
								"render" : function(obj) {
									var tip = new Ext.ToolTip({
												target : obj.getEl(),
												anchor : 'top',
												maxWidth : 160,
												minWidth : 160,
												html : 'Depending on NO.Rule configuration automatically generated Invoice No.!'
											});
								}
							}
						}]
					}, {
						xtype : "panel",
						title : "",
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : 1,
									layout : "form",
									items : [curBox]
								}, {
									xtype : "panel",
									title : "",
									hidden:true,
									columnWidth : 0.2,
									layout : "form",
									labelWidth : 50,
									items : [{
												xtype : "numberfield",
												fieldLabel : "",
												hideLabel : true,
												anchor : "100%",
												id : "orderRate",
												name : "orderRate",
												tabIndex : 8
											}]
								}]
					}, targetPortBox]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 100,
					items : [contactBox, clauseBox, {
								xtype : "datefield",
								fieldLabel : "Shipment",
								anchor : "100%",
								id : "sendTime",
								name : "sendTime",
								format : "Y-m-d",
								tabIndex : 15
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					items : [empBox, {
								xtype : "numberfield",
								fieldLabel : "Profit(%)",
								anchor : "100%",
								id : "cutScale",
								name : "cutScale",
								tabIndex : 11
							}, containerBox]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					items : [companyBox, payTypeBox, trafficBox]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 100,
					items : [{
								xtype : 'textfield',
								fieldLabel : "W&C P/O",
								anchor : "100%",
								disabled:true,
								disabledClass : 'combo-disabled',
								id : 'orderTxt',
								name : 'orderTxt'
							}]
				},{
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 120,
					items : [typeLv1IdBox]
				},{
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 100,
					items : [{
								xtype : "datefield",
								fieldLabel : "Delivery Date",
								anchor : "100%",
								id : "orderLcDate",
								name : "orderLcDate",
								tabIndex : 17,
								format : "Y-m-d"
							}]
				},{
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					labelWidth : 80,
					items : [{
								xtype : 'textarea',
								fieldLabel : "Comments",
								anchor : "100%",
								tabIndex : 18,
								height : 24,
								id : 'orderRemark',
								name : 'orderRemark',
								maxLength : 500
							}]
				},{
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					layout : "form",
					//labelWidth : 100,
					items : [nationBox]
				},{
					xtype : "panel",
					title : "",
					columnWidth : 0.2,
					hidden:true,
					id:'commisionScalePanel',
					layout : "form",
					//labelWidth : 100,
					items : [{
								xtype:'numberfield',
								allowNegative:false,
								anchor:'100%',
								name:'commisionScale',
								id:'commisionScale',
								fieldLabel:'Commision(%)'
							}]
				}, hiddenItems]
	});

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "Shipping Details",
				items : [grid, rightPanel]
			});

	// 合计及其明细
	var totalForm = new Ext.form.FormPanel({
		labelWidth : 100,
		labelAlign : "right",
		formId : 'rptForm',
		layout : "form",
		padding : 5,
		frame : true,
		items : [{
			xtype : "panel",
			layout : "column",
			items : [{
				xtype : "fieldset",
				title : "Total  "
						+ "<a onclick='findAllPin(0)' title='重新统计品名信息' "
						+ "style='cursor: hand; margin-right: 0px;'>"
						+ "<img src='./common/images/_cal.gif' border=0 "
						+ "height='16px' width='16px'></a>",
				padding : 5,
				layout : "column",
				columnWidth : 0.5,
				items : [{
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "column",
					items : [{
								xtype : "panel",
								title : "",
								columnWidth : 1,
								layout : "form",
								items : [{
											xtype : "numberfield",
											fieldLabel : "Total Cartons",
											anchor : "100%",
											id : "totalContainer",
											name : "totalContainer",
											disabled : true,
											disabledClass : 'combo-disabled',
											allowBlank : true
										}]
							}, {
								xtype : "panel",
								title : "",
								columnWidth : 1,
								layout : "column",
								items : [{
									xtype : "panel",
									title : "",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total G.W.",
												anchor : "98%",
												id : "totalGross",
												name : "totalGross",
												maxValue : 99999999.99,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalGrossChk",
										name : "totalGrossChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalGross')
															.setDisabled(false);
													Ext.getCmp('totalGross')
															.focus();
												} else {
													Ext.getCmp('totalGross')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalGrossChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalGross')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.34,
					layout : "column",
					items : [{
								xtype : "panel",
								title : "",
								columnWidth : 1,
								layout : "form",
								items : [{
											xtype : "numberfield",
											fieldLabel : "Total Quantity",
											anchor : "100%",
											id : "totalCount",
											name : "totalCount",
											disabled : true,
											disabledClass : 'combo-disabled',
											allowBlank : true
										}]
							}, {
								xtype : "panel",
								title : "",
								columnWidth : 1,
								layout : "column",
								items : [{
									xtype : "panel",
									title : "",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total N.W.",
												anchor : "98%",
												id : "totalNet",
												name : "totalNet",
												maxValue : 99999999.99,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalNetChk",
										name : "totalNetChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalNet')
															.setDisabled(false);
													Ext.getCmp('totalNet')
															.focus();
												} else {
													Ext.getCmp('totalNet')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalNetChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalNet')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					items : [{
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total Amount",
												anchor : "98%",
												id : "totalMoney",
												name : "totalMoney",
												maxValue : 99999999.99,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalMoneyChk",
										name : "totalMoneyChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalMoney')
															.setDisabled(false);
													Ext.getCmp('totalMoney')
															.focus();
												} else {
													Ext.getCmp('totalMoney')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalMoneyChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalMoney')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total CBM",
												anchor : "98%",
												id : "totalCbm",
												name : "totalCbm",
												decimalPrecision : cbmNum,
												maxValue : 999999999.999999,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalCbmChk",
										name : "totalCbmChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalCbm')
															.setDisabled(false);
													Ext.getCmp('totalCbm')
															.focus();
												} else {
													Ext.getCmp('totalCbm')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalCbmChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalCbm')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
					}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.67,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "Description",
								anchor : "97%",
								height : 90,
								tabIndex : 301,
								id : "hsName",
								name : "hsName",
								maxLength : 1000,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "Cartons",
								anchor : "100%",
								height : 90,
								tabIndex : 302,
								id : "rptContainerCount",
								name : "rptContainerCount",
								maxLength : 500,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "Quantity",
								anchor : "95%",
								height : 90,
								tabIndex : 303,
								id : "rptBoxCount",
								name : "rptBoxCount",
								maxLength : 500,
								allowBlank : true
							}, {
								xtype : "textarea",
								fieldLabel : "G.W.",
								anchor : "95%",
								height : 90,
								tabIndex : 306,
								id : "rptGrossW",
								name : "rptGrossW",
								maxLength : 500,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.34,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "CBM",
								anchor : "95%",
								height : 90,
								id : "rptCbm",
								tabIndex : 304,
								name : "rptCbm",
								maxLength : 500,
								allowBlank : true
							}, {
								xtype : "textarea",
								fieldLabel : "Sale Price",
								anchor : "95%",
								height : 90,
								tabIndex : 307,
								id : "rptAvgPrice",
								name : "rptAvgPrice",
								maxLength : 500,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "N.W.",
								anchor : "100%",
								height : 90,
								tabIndex : 305,
								id : "rptNetW",
								name : "rptNetW",
								maxLength : 500,
								allowBlank : true
							}, {
								xtype : "textarea",
								fieldLabel : "Amount",
								anchor : "100%",
								height : 90,
								tabIndex : 308,
								id : "rptTotalMoney",
								maxLength : 500,
								allowBlank : true
							}]
				}]
			}, {
				width : 10
			}, {
				xtype : "fieldset",
				title : "Declaration Total  "
						+ "<a onclick='findAllPin(1)' title='重新统计品名信息' "
						+ "style='cursor: hand; margin-right: 0px;'>"
						+ "<img src='./common/images/_cal.gif' border=0 "
						+ "height='16px' width='16px'></a>",
				style : 'margin-left:10',
				layout : "column",
				padding : 5,
				columnWidth : 0.5,
				items : [{
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "column",
					items : [{
								xtype : "panel",
								title : "",
								columnWidth : 1,
								layout : "form",
								items : [{
											xtype : "numberfield",
											fieldLabel : "Total Cartons",
											anchor : "100%",
											id : "totalHsContainer",
											name : "totalHsContainer",
											disabled : true,
											disabledClass : 'combo-disabled',
											allowBlank : true
										}]
							}, {
								xtype : "panel",
								columnWidth : 1,
								layout : "column",
								items : [{
									xtype : "panel",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total G.W.",
												anchor : "98%",
												id : "totalHsGross",
												name : "totalHsGross",
												maxValue : 99999999.99,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalHsGrossChk",
										name : "totalHsGrossChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalHsGross')
															.setDisabled(false);
													Ext.getCmp('totalHsGross')
															.focus();
												} else {
													Ext.getCmp('totalHsGross')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalHsGrossChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalHsGross')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.34,
					layout : "column",
					items : [{
								xtype : "panel",
								title : "",
								columnWidth : 1,
								layout : "form",
								items : [{
											xtype : "numberfield",
											fieldLabel : "Total Quantity",
											anchor : "100%",
											id : "totalHsCount",
											name : "totalHsCount",
											disabled : true,
											disabledClass : 'combo-disabled',
											allowBlank : true
										}]
							}, {
								xtype : "panel",
								title : "",
								columnWidth : 1,
								layout : "column",
								items : [{
									xtype : "panel",
									title : "",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total N.W.",
												anchor : "98%",
												id : "totalHsNet",
												name : "totalHsNet",
												maxLength : 99999999.99,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalHsNetChk",
										name : "totalHsNetChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalHsNet')
															.setDisabled(false);
													Ext.getCmp('totalHsNet')
															.focus();
												} else {
													Ext.getCmp('totalHsNet')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalHsNetChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalHsNet')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					items : [{
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total Amount",
												anchor : "98%",
												id : "totalHsMoney",
												name : "totalHsMoney",
												maxValue : 99999999.99,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalHsMoneyChk",
										name : "totalHsMoneyChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalHsMoney')
															.setDisabled(false);
													Ext.getCmp('totalHsMoney')
															.focus();
												} else {
													Ext.getCmp('totalHsMoney')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalHsMoneyChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalHsMoney')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 1,
						layout : "column",
						items : [{
									xtype : "panel",
									title : "",
									columnWidth : 0.9,
									layout : "form",
									items : [{
												xtype : "numberfield",
												fieldLabel : "Total CBM",
												anchor : "98%",
												id : "totalHsCbm",
												name : "totalHsCbm",
												decimalPrecision : cbmNum,
												maxValue : 999999999.999999,
												disabled : true,
												disabledClass : 'combo-disabled',
												allowBlank : true
											}]
								}, {
									xtype : "panel",
									title : "",
									columnWidth : 0.1,
									layout : "form",
									items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										id : "totalHsCbmChk",
										name : "totalHsCbmChk",
										anchor : "100%",
										hideLabel : true,
										listeners : {
											'check' : function(chk, checked) {
												if (checked) {
													Ext.getCmp('totalHsCbm')
															.setDisabled(false);
													Ext.getCmp('totalHsCbm')
															.focus();
												} else {
													Ext.getCmp('totalHsCbm')
															.setDisabled(true);
												}
											},
											'afterrender' : function(area) {
												if (totalHsCbmChk == 1) {
													area.setValue(true);
													Ext.getCmp('totalHsCbm')
															.setDisabled(false);
												}
											}
										}
									}]
								}]
					}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.67,
					layout : "form",
					labelAlign : "top",
					items : [{
						xtype : "textarea",
						fieldLabel : "Des. of Declaration<span style='float:left;display:none;'><input type='checkbox' id='sizeCon'></span>",
						anchor : "97%",
						height : 90,
						tabIndex : 309,
						id : "hsOutName",
						name : "hsOutName",
						maxLength : 1000,
						allowBlank : true
					}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "Carton",
								anchor : "100%",
								height : 90,
								tabIndex : 310,
								id : "rptOutContainerCount",
								name : "rptOutContainerCount",
								maxLength : 500,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "Quantity",
								anchor : "95%",
								height : 90,
								tabIndex : 311,
								id : "rptOutBoxCount",
								name : "rptOutBoxCount",
								maxLength : 500,
								allowBlank : true
							}, {
								xtype : "textarea",
								fieldLabel : "G.W.",
								anchor : "95%",
								height : 90,
								tabIndex : 314,
								id : "rptOutGrossW",
								name : "rptOutGrossW",
								maxLength : 500,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.34,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "CBM",
								anchor : "95%",
								height : 90,
								tabIndex : 312,
								id : "rptOutCbm",
								name : "rptOutCbm",
								maxLength : 500,
								allowBlank : true
							}, {
								xtype : "textarea",
								fieldLabel : "Sale Price",
								anchor : "95%",
								height : 90,
								tabIndex : 315,
								id : "rptOutAvgPrice",
								name : "rptOutAvgPrice",
								maxLength : 500,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : "form",
					labelAlign : "top",
					items : [{
								xtype : "textarea",
								fieldLabel : "N.W.",
								anchor : "100%",
								height : 90,
								tabIndex : 313,
								id : "rptOutNetW",
								name : "rptOutNetW",
								maxLength : 500,
								allowBlank : true
							}, {
								xtype : "textarea",
								fieldLabel : "Amount",
								anchor : "100%",
								height : 90,
								tabIndex : 316,
								id : "rptOutTotalMoney",
								name : "rptOutTotalMoney",
								maxLength : 500,
								allowBlank : true
							}]
				}]
			}]
		}]
	});
	
	// 抬头及其唛头
	var taiForm = new Ext.form.FormPanel({
		layout : "column",
		formId : 'symbolForm',
		// padding : 5,
		frame : true,
		border : false,
		labelAlign : "right",
		items : [{
			xtype : "panel",
			columnWidth : 0.5,
			layout : "form",
			labelWidth : 70,
			items : [{
				xtype : "textarea",
				fieldLabel : "<a onclick=showCompanyWin() ext:qwidth=100 ext:qtip='Company information is displayed after clicking' style='color:blue;cursor: hand;text-decoration: underline'>Consignor</a>",
				anchor : "95%",
				tabIndex : 201,
				id : "consignSendPerson",
				name : "consignSendPerson",
				maxLength : 500,
				height : 80,
				allowBlank : true
			}, {
				xtype : "textarea",
				fieldLabel : "<a onclick=showCustWin(1) ext:qwidth=100 ext:qtip='Client information is displayed after clicking' style='color:blue;cursor: hand;text-decoration: underline'>Notifier</a>",
				anchor : "95%",
				tabIndex : 203,
				id : "consignNotePerson",
				name : "consignNotePerson",
				maxLength : 500,
				height : 80,
				allowBlank : true
			}, {
				xtype : "textarea",
				fieldLabel : "Des. Of Invoice",
				anchor : "95%",
				tabIndex : 205,
				id : "totalName",
				name : "totalName",
				maxLength : 100,
				allowBlank : true
			}]
		}, {
			xtype : "panel",
			columnWidth : 0.5,
			layout : "form",
			labelWidth : 70,
			items : [{
				xtype : "textarea",
				fieldLabel : "<a onclick=showCustWin(2) ext:qwidth=100 ext:qtip='Client information is displayed after clicking' style='color:blue;cursor: hand;text-decoration: underline'>Consignee</a>",
				anchor : "100%",
				tabIndex : 202,
				id : "consignRecvPerson",
				name : "consignRecvPerson",
				maxLength : 500,
				height : 80,
				allowBlank : true
			}, {
				xtype : "textarea",
				fieldLabel : "Notify party",
				anchor : "100%",
				height : 80,
				tabIndex : 204,
				id : "consignNotePersons",
				name : "consignNotePersons",
				maxLength : 500,
				allowBlank : true
			}, {
				xtype : "textarea",
				fieldLabel : "Des. Of Declaration",
				anchor : "100%",
				id : "hsTotalName",
				tabIndex : 206,
				name : "hsTotalName",
				maxLength : 100,
				allowBlank : true
			}]
		}, {
			xtype : "panel",
			columnWidth : 0.5,
			layout : "form",
			labelWidth : 70,
			items : [{
						xtype : "textarea",
						fieldLabel : "Consignment Remark",
						anchor : "95%",
						id : "recReMark",
						tabIndex : 207,
						name : "recReMark",
						maxLength : 500,
						allowBlank : true,
						height : 200
					}]
		}, {
			xtype : "panel",
			columnWidth : 0.3,
			layout : "form",
			labelWidth : 70,
			items : [{
						xtype : "textarea",
						fieldLabel : "Marks",
						anchor : "95%",
						height : 200,
						tabIndex : 208,
						id : "orderZm",
						name : "orderZm",
						maxLength : 500,
						allowBlank : true
					}]
		}, {
			xtype : "fieldset",
			columnWidth : 0.2,
			title : "Marks Pictur",
			layout : "hbox",
			height : 220,
			labelWidth : 60,
			layoutConfig : {
				padding : '5',
				pack : 'center'
			},
			buttons : [{
						text : '',
						hidden : true
					}],
			items : [{
				xtype : "panel",
				width : 130,
				buttonAlign : "center",
				html : '<div align="center" style="width: 130px; height: 130px;">'
						+ '<img src="common/images/zwtp.png" id="order_MB" name="order_MB"'
						+ 'onload="javascript:DrawImage(this,130,130)" onclick="showBigPicDiv(this)"/></div>',
				buttons : [{
							width : 60,
							text : "Change",
							id : 'upmodMai',
							iconCls : "upload-icon",
							handler : showUploadMaiPanel
						}, {
							width : 60,
							text : "Delete",
							id : 'updelMai',
							iconCls : "upload-icon-del",
							handler : delMBPic
						}]
			}]
		}]
	});

	// 订舱及明细
	var baoGuanForm = new Ext.form.FormPanel({
		labelWidth : 60,
		labelAlign : "right",
		layout : "form",
		formId : 'hsInfoForm',
		autoScroll : true,
		border : false,
		frame : true,
		items : [{
					xtype : "fieldset",
					title : "Booking information",
					anchor : "98%",
					layout : "column",
					collapsible : true,
					items : [{
								xtype : "panel",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "panel",
											layout : "form",
											items : [consignBox, {
														xtype : "textarea",
														fieldLabel : "指定货代的备注信息",
														id : 'consignCompanyStr',
														name : 'consignCompanyStr',
														tabIndex : 402,
														height : 40,
														anchor : "98%"
													}]
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "panel",
											layout : "form",
											items : [hsConsignBox, {
														xtype : "textarea",
														fieldLabel : "操作货代的备注信息",
														height : 40,
														tabIndex : 403,
														id : 'hsConsignCompanyStr',
														name : 'hsConsignCompanyStr',
														anchor : "100%"
													}]
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.2,
								items : [{
											xtype : "textfield",
											fieldLabel : "装柜",
											anchor : "100%",
											id : 'containerNum',
											name : 'containerNum',
											tabIndex : 404,
											allowBlank : true
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.2,
								items : []
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.2,
								items : []
							}, {
								xtype : "panel",
								layout : "form",
								labelWidth : 120,
								columnWidth : 0.3,
								items : [{
											xtype : 'radiogroup',
											fieldLabel : "运费支付方式",
											tabIndex : 407,
											items : [{
														boxLabel : '预付',
														inputValue : 0,
														style : "marginLeft:25",
														name : 'transportPayId',
														checked : true,
														id : "transportPayId_1"
													}, {
														boxLabel : '到付',
														name : "transportPayId",
														inputValue : 1,
														id : 'transportPayId_2'
													}]
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 1,
								items : [{
											xtype : "textarea",
											fieldLabel : "订舱备注",
											anchor : "100%",
											tabIndex : 409,
											id : 'giveRemark',
											name : 'giveRemark',
											height : 25
										}]
							}]
				}, {
					xtype : "fieldset",
					title : "电子排载",
					layout : "column",
					anchor : "98%",
					collapsible : true,
					items : [{
								xtype : "panel",
								layout : "form",
								columnWidth : 0.25,
								items : [{
											xtype : "textfield",
											fieldLabel : "Vessel",
											anchor : "100%",
											tabIndex : 410,
											id : 'shipName',
											name : 'shipName'
										}, {
											xtype : "datefield",
											fieldLabel : "ETD",
											id : 'reclaimDate',
											name : 'reclaimDate',
											anchor : "100%",
											tabIndex : 414,
											format : "Y-m-d",
											vtype : 'daterange',
											endDateField : 'returnDate'
										}, {
											xtype : "textfield",
											fieldLabel : "Port of loading",
											anchor : "100%",
											tabIndex : 418,
											disabled : true,
											disabledClass : 'combo-disabled',
											id : 'shPortName',
											name : 'shPortName'
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.25,
								items : [{
											xtype : "textfield",
											fieldLabel : "Voyage",
											tabIndex : 411,
											id : 'licenceNo',
											name : 'licenceNo',
											anchor : "100%"
										}, {
											xtype : "datefield",
											fieldLabel : "ETA",
											tabIndex : 415,
											anchor : "100%",
											id : 'returnDate',
											name : 'returnDate',
											format : "Y-m-d",
											vtype : 'daterange',
											startDateField : 'reclaimDate'
										}, middlePortBox]
							}, {
								xtype : "panel",
								columnWidth : 0.25,
								layout : "form",
								items : [{
											xtype : "textfield",
											fieldLabel : "Booking NO.",
											tabIndex : 412,
											id : 'backupNo',
											name : 'backupNo',
											anchor : "100%"
										}, {
											xtype : "datefield",
											fieldLabel : "Cut off time",
											anchor : "100%",
											tabIndex : 416,
											format : "Y-m-d",
											id : 'giveTime',
											name : 'giveTime'
										}, {
											xtype : "textfield",
											fieldLabel : "Port of discharge",
											anchor : "100%",
											tabIndex : 420,
											disabled : true,
											disabledClass : 'combo-disabled',
											id : 'taPortName',
											name : 'taPortName'
										}]
							}, {
								xtype : "panel",
								columnWidth : 0.25,
								layout : "form",
								items : [{
											xtype : "textfield",
											fieldLabel : "Booking No.",
											tabIndex : 413,
											anchor : "100%",
											id : 'dingCangNo',
											name : 'dingCangNo',
											allowBlank : true
										}, {
											xtype : "datefield",
											fieldLabel : "进舱时间",
											anchor : "100%",
											tabIndex : 417,
											format : "Y-m-d",
											id : "applyDate",
											name : "applyDate"
										}, {
											xtype : "textfield",
											fieldLabel : "place of delivery",
											anchor : "100%",
											tabIndex : 421,
											id : 'targetArea',
											name : 'targetArea'
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.2,
								items : [shipBox]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.55,
								items : [{
											xtype : "textfield",
											fieldLabel : "",
											id : 'shipCompanyStr',
											name : 'shipCompanyStr',
											anchor : "100%",
											tabIndex : 423,
											hideLabel : true
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 0.25,
								items : [{
											xtype : "textfield",
											fieldLabel : "Dock",
											anchor : "100%",
											tabIndex : 424,
											id : 'resourceArea',
											name : 'resourceArea'
										}]
							}, {
								xtype : "panel",
								layout : "form",
								columnWidth : 1,
								items : [{
											xtype : "textarea",
											fieldLabel : "Split Remark",
											tabIndex : 425,
											id : 'containerNo',
											name : 'containerNo',
											anchor : "100%",
											height : 25
										}]
							}]
				}, {
					xtype : "fieldset",
					title : "报关拖车",
					layout : "column",
					anchor : "98%",
					collapsible : true,
					items : [{
								xtype : "panel",
								layout : "form",
								labelWidth : 80,
								columnWidth : 0.5,
								items : [hsCompanyBox, {
											xtype : "textarea",
											fieldLabel : "Customs Company Details",
											id : 'hsCompanyStr',
											name : 'hsCompanyStr',
											tabIndex : 428,
											height : 40,
											anchor : "98%"
										}, {
											xtype : "textarea",
											fieldLabel : "Customs Company Remark",
											tabIndex : 430,
											id : 'hsConsignRemark',
											name : 'hsConsignRemark',
											height : 40,
											anchor : "98%"
										}]
							}, {
								xtype : "panel",
								layout : "form",
								labelWidth : 80,
								columnWidth : 0.5,
								items : [trailCarBox, {
											xtype : "textarea",
											fieldLabel : "Trailer Company Details",
											tabIndex : 429,
											id : 'trailerStr',
											name : 'trailerStr',
											height : 40,
											anchor : "100%"
										}, {
											xtype : "textarea",
											fieldLabel : "Trailer Company Remark",
											height : 40,
											tabIndex : 431,
											id : 'consignCompanyRemark',
											name : 'consignCompanyRemark',
											anchor : "100%"
										}]
							}]
				}]
	});

	// 报关信息
	// var baoGuanForm = new Ext.form.FormPanel({
	// labelWidth : 60,
	// labelAlign : "right",
	// formId : 'hsInfoForm',
	// layout : "form",
	// padding : "10px",
	// frame : true,
	// items : [{
	// xtype : "panel",
	// layout : "column",
	// items : [{
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [payTypeBox, {
	// xtype : "textfield",
	// fieldLabel : "装柜说明",
	// tabIndex : 405,
	// id : "containerNum",
	// name : "containerNum",
	// maxLength : 50,
	// anchor : "100%"
	// }, {
	// xtype : "datefield",
	// fieldLabel : "装柜日期",
	// id : "applyDate",
	// name : "applyDate",
	// format : "Y-m-d",
	// anchor : "100%",
	// tabIndex : 409
	// }]
	// }, {
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [{
	// xtype : "textfield",
	// fieldLabel : "产地",
	// tabIndex : 402,
	// maxLength : 100,
	// id : "resourceArea",
	// name : "resourceArea",
	// anchor : "100%"
	// }, {
	// xtype : "textfield",
	// fieldLabel : "船名",
	// maxLength : 50,
	// tabIndex : 406,
	// id : "shipName",
	// name : "shipName",
	// anchor : "100%"
	// }, {
	// xtype : "textfield",
	// fieldLabel : "目的地",
	// maxLength : 100,
	// tabIndex : 410,
	// id : "targetArea",
	// name : "targetArea",
	// anchor : "100%"
	// }]
	// }, {
	// xtype : "panel",
	// title : "",
	// columnWidth : 0.25,
	// layout : "form",
	// labelWidth : 70,
	// items : [{
	// xtype : "datefield",
	// fieldLabel : "交货日期",
	// tabIndex : 403,
	// id : "giveTime",
	// name : "giveTime",
	// format : "Y-m-d",
	// anchor : "100%"
	// }, {
	// xtype : "textfield",
	// fieldLabel : "货柜号",
	// tabIndex : 407,
	// maxLength : 100,
	// id : "containerNo",
	// name : "containerNo",
	// anchor : "100%"
	// }, {
	// xtype : "textfield",
	// fieldLabel : "订舱合约号",
	// maxLength : 100,
	// tabIndex : 411,
	// id : "dingCangNo",
	// name : "dingCangNo",
	// anchor : "100%"
	// }]
	// }, {
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [{
	// xtype : "textfield",
	// fieldLabel : "交货说明",
	// maxLength : 200,
	// tabIndex : 404,
	// id : "giveRemark",
	// name : "giveRemark",
	// anchor : "100%"
	// }, {
	// xtype : "textfield",
	// fieldLabel : "封签号",
	// maxLength : 100,
	// tabIndex : 408,
	// id : "plumbumNo",
	// name : "plumbumNo",
	// anchor : "100%"
	// }, {
	// xtype : 'radiogroup',
	// fieldLabel : "运费付款",
	// tabIndex : 412,
	// items : [{
	// boxLabel : '预付',
	// inputValue : 0,
	// style : "marginLeft:25",
	// name : 'transportPayId',
	// checked : true,
	// id : "transportPayId_1"
	// }, {
	// boxLabel : '到付',
	// name : "transportPayId",
	// inputValue : 1,
	// id : 'transportPayId_2'
	// }]
	// }]
	// }]
	// }, {
	// xtype : "panel",
	// layout : "column",
	// items : [{
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [hsCompanyBox]
	// }, {
	// xtype : "panel",
	// columnWidth : 0.75,
	// layout : "form",
	// items : [{
	// xtype : "textarea",
	// hideLabels : true,
	// labelSeparator : " ",
	// id : "hsCompanyStr",
	// name : "hsCompanyStr",
	// tabIndex : 414,
	// maxLength : 100,
	// anchor : "100%"
	// }]
	// }]
	// }, {
	// xtype : "panel",
	// layout : "column",
	// items : [{
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [trailCarBox]
	// }, {
	// xtype : "panel",
	// columnWidth : 0.75,
	// layout : "form",
	// items : [{
	// xtype : "textarea",
	// hideLabels : true,
	// labelSeparator : " ",
	// id : "trailerStr",
	// name : "trailerStr",
	// maxLength : 100,
	// tabIndex : 416,
	// anchor : "100%"
	// }]
	// }]
	// }, {
	// xtype : "panel",
	// layout : "column",
	// items : [{
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [shipBox]
	// }, {
	// xtype : "panel",
	// columnWidth : 0.75,
	// layout : "form",
	// items : [{
	// xtype : "textarea",
	// hideLabels : true,
	// labelSeparator : " ",
	// id : "shipCompanyStr",
	// name : "shipCompanyStr",
	// maxLength : 100,
	// tabIndex : 418,
	// anchor : "100%"
	// }]
	// }]
	// }, {
	// xtype : "panel",
	// layout : "column",
	// items : [{
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [hsConsignBox]
	// }, {
	// xtype : "panel",
	// columnWidth : 0.75,
	// layout : "form",
	// items : [{
	// xtype : "textarea",
	// hideLabels : true,
	// labelSeparator : " ",
	// id : "hsConsignCompanyStr",
	// name : "hsConsignCompanyStr",
	// maxLength : 100,
	// tabIndex : 420,
	// anchor : "100%"
	// }]
	// }]
	// }, {
	// xtype : "panel",
	// layout : "column",
	// items : [{
	// xtype : "panel",
	// columnWidth : 0.25,
	// layout : "form",
	// items : [consignBox]
	// }, {
	// xtype : "panel",
	// columnWidth : 0.75,
	// layout : "form",
	// items : [{
	// xtype : "textarea",
	// hideLabels : true,
	// labelSeparator : " ",
	// id : "consignCompanyStr",
	// name : "consignCompanyStr",
	// maxLength : 100,
	// tabIndex : 422,
	// anchor : "100%"
	// }]
	// }]
	// }]
	// });

	var additional = new Ext.form.FormPanel({
		title : "",
		labelWidth : 100,
		labelAlign : "right",
		layout : "form",
		padding : "5px",
		autoScroll:true,
		frame : true,
		items : [{
			xtype : "fieldset",
			title : "Additional Information",
			layout : "column",
			anchor:'98%',
			items : [{
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				items : [{
							xtype : "textarea",
							fieldLabel : "Quality",
							anchor : "100%",
							id : 'quality',
							name : 'quality',
//							maxLength : 500,
							height : 75,
							listeners : {
								'afterrender' : function(area) {
									if (_self.txtAreaAry.quality) {
										area.setValue(_self.txtAreaAry.quality);
									}
								},
								'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.quality='';
													}
												}
							}
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				items : [{
							xtype : "textarea",
							fieldLabel : "Colours & Design",
							anchor : "100%",
							id : 'colours',
							name : 'colours',
//							maxLength : 500,
							height : 75,
							listeners : {
								'afterrender' : function(area) {
									if (_self.txtAreaAry.colours) {
										area.setValue(_self.txtAreaAry.colours);
									}
								},
								'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.colours='';
													}
												}
							}
						}]
			}]
		}, {
			xtype : "fieldset",
			title : "Packing",
			layout : "column",
			anchor:'98%',
			items : [{
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				items : [{
							xtype : "textarea",
							fieldLabel : "Sale Unit",
							anchor : "100%",
							id : 'saleUnit',
							name : 'saleUnit',
//							maxLength : 500,
							height : 75,
							listeners : {
								'afterrender' : function(area) {
									if (_self.txtAreaAry.saleUnit) {
										area
												.setValue(_self.txtAreaAry.saleUnit);
									}
								},
								'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.saleUnit='';
													}
												}
							}
						}, {
							xtype : "textarea",
							fieldLabel : "Packing Assortment",
							anchor : "100%",
							id : 'assortment',
							name : 'assortment',
//							maxLength : 500,
							height : 75,
							listeners : {
								'afterrender' : function(area) {
									if (_self.txtAreaAry.assortment) {
										area
												.setValue(_self.txtAreaAry.assortment);
									}
								},
								'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.assortment='';
													}
												}
							}
						}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				items : [{
							xtype : "textarea",
							fieldLabel : "Handle Unit",
							anchor : "100%",
							id : 'handleUnit',
							name : 'handleUnit',
//							maxLength : 500,
							height : 75,
							listeners : {
								'afterrender' : function(area) {
									if (_self.txtAreaAry.handleUnit) {
										area
												.setValue(_self.txtAreaAry.handleUnit);
									}
								},
								'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.handleUnit='';
													}
												}
							}
						}, {
							xtype : "textarea",
							fieldLabel : "Comments",
							anchor : "100%",
							id : 'comments',
							name : 'comments',
//							maxLength : 500,
							height : 75,
							listeners : {
								'afterrender' : function(area) {
									if (_self.txtAreaAry.comments) {
										area
												.setValue(_self.txtAreaAry.comments);
									}
								},
								'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.comments='';
													}
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
							fieldLabel : "Shipping Mark",
							anchor : "100%",
							id : 'shippingMark',
							name : 'shippingMark',
//							maxLength : 500,
							height : 75,
							listeners : {
								'afterrender' : function(area) {
									if (_self.txtAreaAry.shippingMark) {
										area
												.setValue(_self.txtAreaAry.shippingMark);
									}
								},
									'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.shippingMark='';
													}
												}
							}
						}]
			}]
		}, {
			xtype : "fieldset",
			title : "Buyer/Seller/Agent",
			layout : "column",
			anchor:'98%',
			items : [{
					xtype : "panel",
					title : "",
					columnWidth : 0.33,
					layout : 'column',
					labelWidth : 30,
					labelAlign : "left",
					items : [{
						xtype : 'panel',
						style : 'padding-left:60px',
						columnWidth : 0.2,
						items : [{
							xtype : 'checkbox',
							name : 'isCheckAgent',
							id:'isCheckAgent',
							listeners : {
								'afterrender':function(){
									var checked =_self.txtAreaAry.isCheckAgent;
									Ext.getCmp('isCheckAgent').setValue(checked);
									if(checked=='1'){
									Ext.getCmp('commisionScalePanel').setVisible(true);
									}else{
										Ext.getCmp('commisionScalePanel').setVisible(false);
									}
								},
								'check' : function(check, bo) {
									// 验证表单
//									var formData = getFormValues(orderForm, true);
//									// 表单验证失败时,返回
//									if (!formData) {
//										check.setValue(0);
//										return;
//									}
									if (bo == true) {
										txtAreaAry.isCheckAgent='1';
										Ext.getCmp('commisionScalePanel').setVisible(true);
										var custId = custBox.getValue();
										if (Ext.isEmpty(custId)) {
											Ext.MessageBox.alert("Message",
													"Please select Customer first");
											return;
										} else {
											cotCustomerService.getCustomerById(
													custId, function(res) {
														if (res != null) {
															var city='';
															var nation='';
															var fullName='';
															var adr='';
															var post='';
															if(!Ext.isEmpty(res.customerPost)){
																post= res.customerPost+','
															}
															if(!Ext.isEmpty(res.fullNameEn)){
																fullName=res.fullNameEn+' \n'
															}
															if(!Ext.isEmpty(res.customerAddrEn)){
																adr=res.customerAddrEn+' \n'
															}
															if(!Ext.isEmpty(cityMap[res.cityId])){
																city=cityMap[res.cityId]+' \n'
															}else{
																if(!Ext.isEmpty(res.customerPost)){
																	post= res.customerPost+' \n'
																}
															}
															if(!Ext.isEmpty(nationMap[res.nationId])){
																nation=nationMap[res.nationId];
															}
															var str = fullName + adr + post+city + nation;
															var start = str.indexOf(',');
															var end = str.lastIndexOf(',');
															var strLength=str.length-2;
															var oneLength=str.length-1;
															var lastStr=str.substring(strLength,strLength+1);
															var oneStr=str.substring(oneLength,oneLength+1);
															if (start == end) {
																if(start==strLength){
																		str=str.substring(0,strLength);
																	}
															} else {
																if(lastStr==' '){
																	str = str.substring(0, end);
																}
																if(oneStr==' '){
																	str = str.substring(0, end);
																}
															}
															Ext.getCmp('buyer').setValue(str);
															_self.txtAreaAry.buyer=str;
														}
													});
											queryService.findCompanyStr($('companyId').value,function(enName){
											Ext.getCmp('agent').setValue(enName);
											_self.txtAreaAry.agent=enName;
										})
											//通过订单Id获得厂家
											var orderId =$('orderId').value ;
										if (orderId!='null' && orderId!='' && orderId !=null) {
												cotOrderOutService.getFactoryByOrderId(orderId,function(res){
													if(res!=null){
														var city='';
														var nation='';
														var fullName='';
														var city='';
														var adr='';
														var post='';
														if(!Ext.isEmpty(res.post)){
															post= res.post+','
														}
														if(!Ext.isEmpty(res.factoryName)){
															fullName=res.factoryName+' \n'
														}
														if(!Ext.isEmpty(res.factoryAddr)){
															adr=res.factoryAddr+' \n'
														}
														if(!Ext.isEmpty(cityMap[res.cityId])){
															city=cityMap[res.cityId]+' \n'
														}else{
															if(!Ext.isEmpty(res.post)){
																post= res.post+' \n'
															}
														}
														if(!Ext.isEmpty(nationMap[res.nationId])){
															nation=nationMap[res.nationId];
														}
														var str = fullName + adr + post+city + nation;
														var start = str.indexOf(',');
														var end = str.lastIndexOf(',');
														var strLength=str.length-2;
														var oneLength=str.length-1;
														var lastStr=str.substring(strLength,strLength+1);
														var oneStr=str.substring(oneLength,oneLength+1);
														if (start == end) {
															if(start==strLength){
																	str=str.substring(0,strLength);
																}
														} else {
															if(lastStr==' '){
																str = str.substring(0, end);
															}
															if(oneStr==' '){
																str = str.substring(0, end);
															}
														}
														Ext.getCmp('seller').setValue(str);
														_self.txtAreaAry.seller=str;
													}
												});
											}
										}
										
									} else {
										txtAreaAry.isCheckAgent='0';
										Ext.getCmp('commisionScalePanel').setVisible(false);
										Ext.getCmp('agent').setValue('     ');
										_self.txtAreaAry.agent='  ';
										// 客户
										var custId = custBox.getValue();
										if (Ext.isEmpty(custId)) {
											Ext.MessageBox.alert("Message","Please select Customer first");
											return;
										} else {
											queryService.findCompanyStr($('companyId').value,function(enName){
											$(seller).value=enName
											_self.txtAreaAry.seller=enName;
										})
											
										}
									}
								}
	
							}
						}]
					}, {
						columnWidth : 0.8,
						layout : 'form',
						items : [{
									xtype : "textarea",
									fieldLabel : 'Agent',
									id : 'agent',
									name : 'agent',
									height : 75,
									maxLength : 500,
									anchor : "100%",
									listeners : {
										'afterrender' : function(area) {
											if ($('pId').value != ''
													&& $('pId').value != 'null') {
												area.setValue(_self.txtAreaAry.agent);
											}
										},
										'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.agent='';
													}
												}
									}
								}]
					}]
	
				},{
						xtype : "panel",
						title : "",
						columnWidth : 0.33,
						layout : "form",
						items : [{
									xtype : "textarea",
									fieldLabel : "<a href='#' onclick='changeBuyer()' title='get Invoice Address form Client'>Buyer</a>",
									id : 'buyer',
									name : 'buyer',
									maxLength : 500,
									height : 75,
									anchor : "100%",
									listeners : {
										'afterrender' : function(area) {
											if (_self.txtAreaAry.buyer) {
												area
														.setValue(_self.txtAreaAry.buyer);
											}
										},
										'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.buyer='';
													}
												}
									}
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.33,
						layout : "form",
						items : [{
									xtype : "textarea",
									fieldLabel : "Seller",
									id : 'seller',
									name : 'seller',
									maxLength : 500,
									height : 75,
									anchor : "100%",
									listeners : {
										'afterrender' : function(area) {
											if (_self.txtAreaAry.seller) {
												area
														.setValue(_self.txtAreaAry.seller);
											}
										},
										'change' : function(area,newVal,oldVal) {
													if (newVal=='') {
														_self.txtAreaAry.seller='';
													}
												}
									}
								}]
					}]
		}]
	});

	// 底部标签页
	var tbl = new Ext.TabPanel({
				region : 'south',
				region : 'center',
				width : "100%",
				id : 'outTab',
				activeTab : 0,
				items : [centerPanel,{
							id : "additionalTab",
							name : "additionalTab",
							title : "Additional Information",
							layout : 'fit',
							items : [additional]
						}],
				buttonAlign : 'center',
				buttons : [{
							text : "Save",
							id : 'saveBtn',
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
							text : "Print",
							id : 'printBtn',
							hidden:true,
							iconCls : "page_print",
							cls : "SYSOP_PRINT",
							handler : showPrint
						}, {
							text : "Cancel",
							iconCls : "page_cancel",
							handler : function() {
								closeandreflashEC(true, 'orderoutGrid', false);
							}
						}, {
							text : "Need to be reviewed",
							id : 'qingBtn',
							hidden:true,
							iconCls : "page_from",
							handler : qingShen
						}, {
							text : "reviewed",
							id : 'guoBtn',
							hidden:true,
							iconCls : "page_check",
							handler : guoShen
						}, {
							text : "Not review",
							id : 'buBtn',
							hidden:true,
							iconCls : "page_check_error",
							handler : buShen
						}, {
							text : "review again",
							id : 'fanxBtn',
							hidden:true,
							iconCls : "page_from",
							handler : fanShen
						}]
			});

	// 判断是否点击过抬头及唛头
	var taiFlag = false;

	// 判断是否点击过合计及明细
	var totalFlag = false;

	// 判断是否点击过报关明细
	var baoGuanFlag = false;

	// 编辑页面时加载审核原因
	tbl.on('tabchange', function(tb, pnl) {
		if (pnl.name == 'taiTab') {
			// 加载麦标
			var id = $('pId').value;
			var custId = $('custId').value;
			if (id == 'null' || id == '') {
				if (custId != 'null' && custId != '') {
					$('order_MB').src = "./showPicture.action?flag=custMb&detailId="
							+ custId;
				}
				Ext.getCmp('upmodMai').hide();
				Ext.getCmp('updelMai').hide();
			} else {
				if (taiFlag == false) {
					$('orderZm').value = orderZm;
					$('totalName').value = totalName;
					$('hsTotalName').value = hsTotalName;
					// 加载抬头信息
					cotOrderOutService.getCotSymbolByOrderOutId(id, function(
									res) {
								if (res != null) {
									DWRUtil.setValues(res);
									$('symbolId').value = res.id;
								}
							});
				}

				if ($('uploadSuc').value == "1" || $('uploadSuc').value == "") {
					$('order_MB').src = "./showPicture.action?flag=orderOutMB&detailId="
							+ id + "&temp=" + Math.random();
				} else {
					if (custId != 'null' && custId != '') {
						$('order_MB').src = "./showPicture.action?flag=custMb&detailId="
								+ custId;
					}
				}
				Ext.getCmp('upmodMai').show();
				Ext.getCmp('updelMai').show();
			}
			taiFlag = true;
		}
		if (pnl.name == 'totalTab') {
			// 加载麦标
			var id = $('pId').value;
			if (id == 'null' || id == '') {

			} else {
				if (totalFlag == false) {
					// 加载合计信息
					cotOrderOutService.getOrderOutById(id, function(cot) {
						if (cot != null) {
							if (cot.totalCbm != null) {
								cot.totalCbm = cot.totalCbm.toFixed(cbmNum);
							}
							if (cot.totalHsCbm != null) {
								cot.totalHsCbm = cot.totalHsCbm.toFixed(cbmNum);
							}

							Ext.getCmp('totalContainer')
									.setValue(cot.totalContainer);
							Ext.getCmp('totalCount').setValue(cot.totalCount);
							Ext.getCmp('totalMoney').setValue(cot.totalMoney);
							Ext.getCmp('totalGross').setValue(cot.totalGross);
							Ext.getCmp('totalNet').setValue(cot.totalNet);
							Ext.getCmp('totalCbm').setValue(cot.totalCbm);

							Ext.getCmp('totalHsContainer')
									.setValue(cot.totalHsContainer);
							Ext.getCmp('totalHsCount')
									.setValue(cot.totalHsCount);
							Ext.getCmp('totalHsMoney')
									.setValue(cot.totalHsMoney);
							Ext.getCmp('totalHsGross')
									.setValue(cot.totalHsGross);
							Ext.getCmp('totalHsNet').setValue(cot.totalHsNet);
							Ext.getCmp('totalHsCbm').setValue(cot.totalHsCbm);
						}
					});
					// 加载合计信息
					cotOrderOutService.getOrderouthsRptByOrderOutId(id,
							function(res) {
								if (res != null) {
									DWRUtil.setValues(res);
									$('orderouthsRptId').value = res.id;
								}
							});
				}
			}
			totalFlag = true;
		}

		if (pnl.name == 'baoGuanTab') {
			// 加载麦标
			var id = $('pId').value;
			if (id == 'null' || id == '') {

			} else {
				if (baoGuanFlag == false) {
					// 加载报关信息
					cotOrderOutService.getCotHsInfoByOrderOutId(id, function(
							res) {
						if (res != null) {
							DWRUtil.setValues(res);
							$('hsInfoId').value = res.id;
							// 运费支付方式
							if (res.transportPayId == 1) {
								$('transportPayId_1').checked = true;
							} else {
								$('transportPayId_2').checked = true;
							}

							hsCompanyBox.bindValue(res.hsCompanyId);
							trailCarBox.bindValue(res.trailerId);
							shipBox.bindValue(res.shipId);
							hsConsignBox.bindValue(res.hsConsignCompanyId);
							consignBox.bindValue(res.consignCompanyId);
							shipPortBox.bindPageValue("CotShipPort", "id",
									res.shipportId);
							targetPortBox.bindPageValue("CotTargetPort", "id",
									res.targetportId);
							middlePortBox.bindPageValue("CotTargetPort", "id",
									res.midportId);
							Ext.getCmp('applyDate').setValue(res.applyDate);
							Ext.getCmp('giveTime').setValue(res.giveTime);
							Ext.getCmp('reclaimDate').setValue(res.reclaimDate);
							Ext.getCmp('returnDate').setValue(res.returnDate);
							// 查询起运港和目的港的名称
							cotOrderOutService.findPortNameById(res.shipportId,
									res.targetportId, function(alt) {
										Ext.getCmp('shPortName')
												.setValue(alt[0]);
										Ext.getCmp('taPortName')
												.setValue(alt[1]);
									});
						}
					});
				}
			}
			baoGuanFlag = true;
		}
	});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [tbl, orderForm]
			});
	viewport.doLayout();

	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list) {
		insertByBatch(list);
	}

	// 加载表格数据
	ds.load({
				params : {
					start : 0,
					limit : 1000
				}
			});

	// -------------------------------------方法--------------------------------------------------
	window.changeBuyer=function(){
		var custId = custBox.getValue();
				if (!Ext.isEmpty(custId)) {
					cotCustomerService.getCustomerById(custId, function(res) {
								if (res != null) {
									var city='';
									var nation='';
									var fullName='';
									var adr='';
									var post='';
									if(!Ext.isEmpty(res.invoiceCustomerPost)){
										post= res.invoiceCustomerPost+','
									}
									if(!Ext.isEmpty(res.invoiceCustomerName)){
										fullName=res.invoiceCustomerName+' \n'
									}
									if(!Ext.isEmpty(res.invoiceCustomerAddress)){
										adr=res.customerAddrEn+' \n'
									}
									if(!Ext.isEmpty(cityMap[res.invoiceCityId])){
										city=cityMap[res.invoiceCityId]+' \n'
									}
									if(!Ext.isEmpty(nationMap[res.invoiceCountryId])){
										nation=nationMap[res.invoiceCountryId];
									}
//									var str = fullName + adr + post+city + nation;
//									var a =str.substring(str.length-1);
//									if(a==','){
//										str=str.substring(0,str.length-1);
//									}
									var str = fullName + adr + post+city + nation;
									var start = str.indexOf(',');
									var end = str.lastIndexOf(',');
									var strLength=str.length-2;
									var oneLength=str.length-1;
									var lastStr=str.substring(strLength,strLength+1);
									var oneStr=str.substring(oneLength,oneLength+1);
									if (start == end) {
										if(start==strLength){
												str=str.substring(0,strLength);
											}
									} else {
										if(lastStr==','){
											str = str.substring(0, end);
										}
										if(oneStr==','){
											str = str.substring(0, end);
										}
									}
									Ext.getCmp('buyer').setValue(str);
								}
							});
				}
	}

	// 单样品利润率
	var liRunCau = '';
	// 审核原因
	var chkReason = "";
	// 合同条款
	var conArea = "";
	// 正唛
	var orderZm = "";
	// 总品名
	var totalName = "";
	// 报关总品名
	var hsTotalName = "";

	var totalGrossChk = 0;// 是否自定义总毛重总净重
	var totalNetChk = 0;
	var totalHsGrossChk = 0;
	var totalHsNetChk = 0;
	var totalMoneyChk = 0;// 是否自定义总金额总CBM
	var totalCbmChk = 0;
	var totalHsMoneyChk = 0;
	var totalHsCbmChk = 0;

	// 存储主单的10个字段
	this.txtAreaAry = {};
	
	var oSt=0;

	// 初始化
	function initform() {
		$('talPriceDiv').style.display = 'block';
		DWREngine.setAsync(false);
		// 清空Map
//		cotOrderOutService.clearMap("orderout", function(res) {
//				});
		// 加载集装箱的柜体积
		queryService.getContainerCube(function(res) {
					$('con20').value = res[0];
					$('con40').value = res[1];
					$('con40H').value = res[2];
					$('con45').value = res[3];
				});
		// 报价单编号
		var id = $('pId').value;
		if (id == 'null' || id == '') {
//			Ext.getCmp('guoBtn').hide();
//			Ext.getCmp('buBtn').hide();
//			Ext.getCmp('fanxBtn').hide();
//			Ext.getCmp('qingBtn').hide();
			// 隐藏审核原因
			// Ext.getCmp('shenTab').disable();
			// 初始化币种和价格条款和起运港的默认值
			cotOrderOutService.getList('CotPriceCfg', function(res) {
				if (res.length != 0) {
					shipPortBox.bindPageValue("CotShipPort", "id",
							res[0].shipPortId);
					clauseBox.bindValue(res[0].caluseId);
					if (res[0].currencyId != null) {
						curBox.bindValue(res[0].currencyId);
						// 变更汇率
						sysdicutil.getDicListByName('currency', function(cl) {
									for (var i = 0; i < cl.length; i++) {
										if (cl[i].id == res[0].currencyId) {
											$('orderRate').value = cl[i].curRate;
											break;
										}
									}
								});
					}
					trafficBox.bindValue(res[0].trafficTypeId);
					companyBox.bindPageValue("CotCompany", "id",
							res[0].companyId);
					// 业务配置中是否审核(0不审核,1审核)
					if (res[0].isCheck == 0) {
						shenBox.setValue(9);
					} else {
						shenBox.setValue(0);
					}
				}
			});
		} else {
			// 初始报价配置值
//			cotOrderOutService.getList('CotPriceCfg', function(res) {
//						if (res.length != 0) {
//							// 业务配置中是否审核(0不审核,1审核)
//							if (res[0].isCheck == 0) {
//								Ext.getCmp('guoBtn').hide();
//								Ext.getCmp('buBtn').hide();
//								Ext.getCmp('fanxBtn').hide();
//								Ext.getCmp('qingBtn').hide();
//								// Ext.getCmp('shenTab').disable();
//							}
//						} else {
//							Ext.getCmp('qingBtn').hide();
//							// Ext.getCmp('shenTab').disable();
//						}
//					});
			// 加载出货单信息
			cotOrderOutService.getOrderOutById(parseInt(id), function(res) {
				$('orderId').value=res.orderId;
				$('orderTxt').value=$('odNo').value;
				if(res.orderNo){
					Ext.getCmp('printBtn').show();
				}
				
				//是否需要加税
				if(res.taxLv==1){
					$('addTaxBtn').style.display='none';
					$('mudTaxBtn').style.display='';
					$('addSpan').style.display='';
					$('taxLab').innerText=Number(res.taxTotalMoney).sub(res.totalMoney);
					$('totalTaxLab').innerText=res.taxTotalMoney;
				}

				orderZm = res.orderZm;
				totalName = res.totalName;
				hsTotalName = res.hsTotalName;
				if(res.orderNo!=null && res.orderNo!=''){
					Ext.getCmp("orderTime").setValue(res.orderTime);
				}
				 Ext.getCmp("sendTime").setValue(res.sendTime);
				 Ext.getCmp("orderLcDate").setValue(res.orderLcDate);
				chkReason = res.checkReason;
				DWRUtil.setValues(res);
				$('isCheckId').value=res.isCheckAgent;				
				// 绑定下拉框
				//department
				typeLv1IdBox.bindPageValue("CotTypeLv1", "id", res.typeLv1Id);
				custBox.bindPageValue("CotCustomer", "id", res.custId);
				empBox.bindPageValue("CotEmps", "id", res.businessPerson);
				curBox.bindValue(res.currencyId);
				curBox.disable();
				clauseBox.bindValue(res.clauseTypeId);
				trafficBox.bindValue(res.trafficId);
				payTypeBox.bindValue(res.paTypeId);
				companyBox.bindPageValue("CotCompany", "id", res.companyId);
				commisionBox.bindValue(res.commisionTypeId);
				
				shipPortBox.bindPageValue("CotShipPort", "id", res.shipportId);
				targetPortBox.bindPageValue("CotTargetPort", "id", res.targetportId);
				
				containerBox.bindValue(res.containerTypeId);
				
				
				shenBox.setValue(res.orderStatus);
				contactBox.loadValueById('CotCustContact', 'customerId',
						res.custId, res.contactId);
				nationBox.bindPageValue("CotNation","id",res.nationId);
				bankBox.bindValue(res.bankId);

				if (res.totalGrossChk == 1) {
					totalGrossChk = res.totalGrossChk;
				}
				if (res.totalNetChk == 1) {
					totalNetChk = res.totalNetChk;
				}
				if (res.totalHsGrossChk == 1) {
					totalHsGrossChk = res.totalHsGrossChk;
				}
				if (res.totalHsNetChk == 1) {
					totalHsNetChk = res.totalHsNetChk;
				}
				if (res.totalMoneyChk == 1) {
					totalMoneyChk = res.totalMoneyChk;
				}
				if (res.totalCbmChk == 1) {
					totalCbmChk = res.totalCbmChk;
				}
				if (res.totalHsMoneyChk == 1) {
					totalHsMoneyChk = res.totalHsMoneyChk;
				}
				if (res.totalHsCbmChk == 1) {
					totalHsCbmChk = res.totalHsCbmChk;
				}

				// 存储主单的10个字段
				_self.txtAreaAry.quality = res.quality;
				_self.txtAreaAry.colours = res.colours;
				_self.txtAreaAry.saleUnit = res.saleUnit;
				_self.txtAreaAry.handleUnit = res.handleUnit;
				_self.txtAreaAry.assortment = res.assortment;
				_self.txtAreaAry.comments = res.comments;
				_self.txtAreaAry.shippingMark = res.shippingMark;
				_self.txtAreaAry.buyer = res.buyer;
				_self.txtAreaAry.seller = res.seller;
				_self.txtAreaAry.agent = res.agent;
				_self.txtAreaAry.isCheckAgent=res.isCheckAgent;
//				if ($('orderStatus').value == 0 || $('orderStatus').value == 2
//						|| $('orderStatus').value == 1) {
//					Ext.getCmp('guoBtn').hide();
//					Ext.getCmp('buBtn').hide();
//				}
//				if ($('orderStatus').value == 2) {
//					Ext.getCmp('qingBtn').hide();
//				}
//				if ($('orderStatus').value == 0 || $('orderStatus').value == 1) {
//					Ext.getCmp('fanxBtn').hide();
//				}
//				if ($('orderStatus').value == 3) {
//					Ext.getCmp('fanxBtn').hide();
//					Ext.getCmp('qingBtn').hide();
//				}
				oSt=res.orderStatus;
				if (res.orderStatus== 2) {
					Ext.getCmp('saveBtn').hide();
				}

				// if (res.totalMoney != null) {
				// $('totalLab').innerText = res.totalMoney.toFixed('2');
				// }
//				if (res.totalHsMoney != null) {
//					$('totalChaLab').innerText = res.totalHsMoney
//							.toFixed(deNum);
//				}
				
				if(res.orderLcDate!=null && res.orderLcDate!='' && res.sendTime!=null && res.sendTime!=''){
					queryService.findDays(res.shipportId,res.targetportId,function(alt){
						var date = Ext.getCmp('sendTime').getValue().add(
										Date.DAY, alt);
						var newDate = new Date(res.orderLcDate);
						if(newDate.getTime()<date.getTime()){
							Ext.getCmp('orderLcDate').el.setStyle('color','red');
						}else{
							var date2 = date.add(Date.DAY, 37);
							if(newDate.getTime()>=date2.getTime()){
								Ext.getCmp('orderLcDate').el.setStyle('color','red');
							}else{
								Ext.getCmp('orderLcDate').el.setStyle('color','black');
							}
						}
					})
				}
			});
		}
		DWREngine.setAsync(true);
	}
	unmask();
	initform();

	// 获得表格选择行
	function getIds() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 得到抬头信息
	function getSymbol() {
		// 如果没有点击过,返回null
		if (taiFlag == false) {
			return null;
		}

		var symbol = DWRUtil.getValues('symbolForm');
		var cotSymbol = new CotSymbol();
		if (symbol != null) {
			for (var p in symbol) {
				cotSymbol[p] = symbol[p];
			}
		}
		var id = $('symbolId').value;
		if (id != '') {
			cotSymbol.id = id;
		} else {
			cotSymbol.id = null;
		}
		return cotSymbol;
	}

	// 得到报关信息
	function getHsInfo() {
		// 如果没有点击过,返回null
		if (baoGuanFlag == false) {
			return null;
		}

		var form = DWRUtil.getValues('hsInfoForm');
		var cotHsInfo = new CotHsInfo();
		if (form != null) {
			for (var p in cotHsInfo) {
				cotHsInfo[p] = form[p];
			}
			if ($('transportPayId_1').checked) {
				cotHsInfo.transportPayId = 1;
			} else {
				cotHsInfo.transportPayId = 0;
			}
			// 进舱时间
			if ($('applyDate').value != '') {
				cotHsInfo.applyDate = getDateType($('applyDate').value);
			} else {
				cotHsInfo.applyDate = null;
			}
			// 截关时间
			if ($('giveTime').value != '') {
				cotHsInfo.giveTime = getDateType($('giveTime').value);
			} else {
				cotHsInfo.giveTime = null;
			}
			// 开船时间
			if ($('reclaimDate').value != '') {
				cotHsInfo.reclaimDate = getDateType($('reclaimDate').value);
			} else {
				cotHsInfo.reclaimDate = null;
			}
			// 到港时间
			if ($('returnDate').value != '') {
				cotHsInfo.returnDate = getDateType($('returnDate').value);
			} else {
				cotHsInfo.returnDate = null;
			}
		}
		var id = $('hsInfoId').value;
		if (id != '') {
			cotHsInfo.id = id;
		} else {
			cotHsInfo.id = null;
		}
		return cotHsInfo;
	}

	// 得到合计信息
	function getOrderouthsRpt() {
		// 如果没有点击过,返回null
		if (totalFlag == false) {
			return null;
		}

		var form = DWRUtil.getValues('rptForm');
		var orderouthsRpt = new CotOrderouthsRpt();
		if (form != null) {
			for (var p in orderouthsRpt) {
				orderouthsRpt[p] = form[p];
			}
		}
		var id = $('orderouthsRptId').value;
		if (id != '') {
			orderouthsRpt.id = id;
		} else {
			orderouthsRpt.id = null;
		}
		return orderouthsRpt;
	}

	// 保存
	function save() {
		var popedom = checkAddMod($('pId').value);
		if (popedom == 1) {
			Ext.MessageBox.alert('Message',
					'Sorry, you do not have Authority!!');
			return;
		} else if (popedom == 2) {
			Ext.MessageBox.alert('Message',
					'Sorry, you do not have Authority!!');
			return;
		}
		// 审核通过不让修改
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}

		// 验证出货主单
		var formData = getFormValues(orderForm, true);
		if (!formData) {
			return;
		}
		
		if(isExist){
			Ext.MessageBox
					.alert('Message', 'Duplicate invoice no,please use another on.');
			return;
		}
		
		// 验证抬头表单
		var formData = getFormValues(taiForm, true);
		if (!formData) {
			return;
		}
		// 验证合计及明细表单
		var formData = getFormValues(totalForm, true);
		if (!formData) {
			return;
		}
		// 验证报关信息表单
		var formData = getFormValues(baoGuanForm, true);
		if (!formData) {
			return;
		}
		Ext.MessageBox.confirm('Message', 'Are you sure to save it？', function(
				btn) {
			if (btn == 'yes') {
				var price = DWRUtil.getValues('orderForm');
				var cotOrder = {};
				// 如果编号存在时先查询出对象,再填充表单
				if ($('pId').value != 'null' && $('pId').value != '') {
					DWREngine.setAsync(false);
					cotOrderOutService.getOrderOutById($('pId').value,
							function(res) {
								for (var p in price) {
									if (p != 'orderTime' && p != 'orderLcDate') {
										res[p] = price[p];
									}
								}
								cotOrder = res;
							});
					DWREngine.setAsync(true);
				} else {
					cotOrder = new CotOrderOut();
					for (var p in cotOrder) {
						if (p != 'orderTime' && p != 'orderLcDate') {
							cotOrder[p] = price[p];
						}
					}
				}

				// 得到抬头信息
				var cotSymbol = getSymbol();
				// 得到报关信息
				var cotHsInfo = getHsInfo();
				// 得到合计信息
				var cotOrderouthsRpt = getOrderouthsRpt();

				// 如果抬头tap有点击过
				if (Ext.getCmp("orderZm").isVisible()) {
					// 正唛
					cotOrder.orderZm = $('orderZm').value;
					// 总品名
					cotOrder.totalName = $('totalName').value;
					// 报关总品名
					cotOrder.hsTotalName = $('hsTotalName').value;
				}
				// 审核原因
				// if (Ext.getCmp("checkReason").isVisible()) {
				// cotOrder.checkReason = $('checkReason').value;
				// }

				// 如果需要自定义总毛重和总净重
				if (Ext.getCmp("totalGross").isVisible()) {
					if ($("totalGrossChk").checked == true) {
						cotOrder.totalGross = $("totalGross").value;
						cotOrder.totalGrossChk = 1;
					} else {
						cotOrder.totalGrossChk = 0;
					}
					if ($("totalNetChk").checked == true) {
						cotOrder.totalNet = $("totalNet").value;
						cotOrder.totalNetChk = 1;
					} else {
						cotOrder.totalNetChk = 0;
					}
					if ($("totalHsGrossChk").checked == true) {
						cotOrder.totalHsGross = $("totalHsGross").value;
						cotOrder.totalHsGrossChk = 1;
					} else {
						cotOrder.totalHsGrossChk = 0;
					}
					if ($("totalHsNetChk").checked == true) {
						cotOrder.totalHsNet = $("totalHsNet").value;
						cotOrder.totalHsNetChk = 1;
					} else {
						cotOrder.totalHsNetChk = 0;
					}
					if ($("totalMoneyChk").checked == true) {
						cotOrder.totalMoney = $("totalMoney").value;
						cotOrder.totalMoneyChk = 1;
					} else {
						cotOrder.totalMoneyChk = 0;
					}
					if ($("totalCbmChk").checked == true) {
						cotOrder.totalCbm = $("totalCbm").value;
						cotOrder.totalCbmChk = 1;
					} else {
						cotOrder.totalCbmChk = 0;
					}
					if ($("totalHsMoneyChk").checked == true) {
						cotOrder.totalHsMoney = $("totalHsMoney").value;
						cotOrder.totalHsMoneyChk = 1;
					} else {
						cotOrder.totalHsMoneyChk = 0;
					}
					if ($("totalHsCbmChk").checked == true) {
						cotOrder.totalHsCbm = $("totalHsCbm").value;
						cotOrder.totalHsCbmChk = 1;
					} else {
						cotOrder.totalHsCbmChk = 0;
					}
				}
				var oldFlag = false;
				if ($('uploadSuc').value == "0") {
					oldFlag = true;
				}

				// 其它信息
				//if (Ext.getCmp("quality").isVisible()) {
//					cotOrder.quality = $('quality').value;
//					cotOrder.colours = $('colours').value;
//					cotOrder.saleUnit = $('saleUnit').value;
//					cotOrder.handleUnit = $('handleUnit').value;
//					cotOrder.assortment = $('assortment').value;
//					cotOrder.comments = $('comments').value;
//					cotOrder.shippingMark = $('shippingMark').value;
//					cotOrder.buyer = $('buyer').value;
//					cotOrder.seller = $('seller').value;
//					cotOrder.agent = $('agent').value;
//					var checked=Ext.getCmp('isCheckAgent').checked
//					if(checked==true){
//						cotOrder.isCheckAgent='1';
//					}else{
//						cotOrder.isCheckAgent='0';
//					}
				
				 if(Ext.isEmpty(Ext.getCmp('quality').getValue())){
			    	cotOrder.quality=_self.txtAreaAry.quality;
			    }else{
			    	cotOrder.quality = Ext.getCmp('quality').getValue();
			    }
				if(Ext.isEmpty(Ext.getCmp('colours').getValue())){
			    	cotOrder.colours =_self.txtAreaAry.colours; 
			    }else{
			    	cotOrder.colours = Ext.getCmp('colours').getValue();
			    }
			    
			   if(Ext.isEmpty(Ext.getCmp('saleUnit').getValue())){
			    	cotOrder.saleUnit =_self.txtAreaAry.saleUnit;
			    }else{
			    	cotOrder.saleUnit = Ext.getCmp('saleUnit').getValue();
			    }
				if(Ext.isEmpty(Ext.getCmp('handleUnit').getValue())){
			    	cotOrder.handleUnit =_self.txtAreaAry.handleUnit;
			    }else{
			    	cotOrder.handleUnit = Ext.getCmp('handleUnit').getValue();
			    }
			    
			    
			    if(Ext.isEmpty(Ext.getCmp('assortment').getValue())){
			    	cotOrder.assortment =_self.txtAreaAry.assortment;
			    }else{
			    	cotOrder.assortment = Ext.getCmp('assortment').getValue();
			    }
				if(Ext.isEmpty(Ext.getCmp('comments').getValue())){
			    	cotOrder.comments = _self.txtAreaAry.comments ;
			    }else{
			    	cotOrder.comments = Ext.getCmp('comments').getValue();
			    }
			    
			    if(Ext.isEmpty(Ext.getCmp('shippingMark').getValue())){
			    	cotOrder.shippingMark =_self.txtAreaAry.shippingMark;
			    }else{
			    	cotOrder.shippingMark = Ext.getCmp('shippingMark').getValue();
			    }
			    
				if(Ext.isEmpty(Ext.getCmp('buyer').getValue())){
			    	cotOrder.buyer=_self.txtAreaAry.buyer ;
			    }else{
			    	cotOrder.buyer=Ext.getCmp('buyer').getValue();
			    }
			   
			    
			    if(Ext.isEmpty(Ext.getCmp('seller').getValue())){
			    	cotOrder.seller =_self.txtAreaAry.seller;
			    }else{
			    	cotOrder.seller =Ext.getCmp('seller').getValue();
			    }
				if(Ext.isEmpty(Ext.getCmp('agent').getValue())){
			    	cotOrder.agent =_self.txtAreaAry.agent;
			    }else{
			    	cotOrder.agent = Ext.getCmp('agent').getValue();
			    }
			    var txt=_self.txtAreaAry.isCheckAgent;
			    if(Ext.isEmpty(txt)){
			    	cotOrder.isCheckAgent = '0';
			    }else{
			    	cotOrder.isCheckAgent=_self.txtAreaAry.isCheckAgent;
			    }
				
				if($('addSpan').style.display==''){
					cotOrder.taxLv=1;
					cotOrder.taxTotalMoney=$('totalTaxLab').innerText;
				}else{
					cotOrder.taxLv=0;
					cotOrder.taxTotalMoney=0;
				}
				
				cotOrder.sendTime= getDateType($('sendTime').value);
				//department
				cotOrder.typeLv1Id = typeLv1IdBox.getValue();
//				alert(cotOrder.commisionScale);
//                return;
				DWREngine.setAsync(false);
				cotOrderOutService.saveOrUpdateOrderOut(cotOrder, cotSymbol,
						cotHsInfo, cotOrderouthsRpt, $('orderTime').value,$('orderLcDate').value,
						oldFlag, function(res) {
							if (res != null) {
								// 当新建时给主单id赋值,如果默认配置需要审核,保存后显示"提请审核"按钮
								if ($('pId').value == 'null') {
//									if ($('orderStatus').value == 0) {
//										Ext.getCmp("qingBtn").show();
//									}
//									if (Ext.getCmp('orderZm').isVisible()) {
//										Ext.getCmp('upmodMai').show();
//										Ext.getCmp('updelMai').show();
//									}
									$('pId').value = res[0];
									curBox.disable();
								}
								Ext.getCmp('printBtn').show();
								// 给tabpanel的id赋值
								if (taiFlag == true) {
									$('symbolId').value = res[1];
								}
								if (totalFlag == true) {
									$('orderouthsRptId').value = res[3];
								}
								if (baoGuanFlag == true) {
									$('hsInfoId').value = res[2];
								}
								// 如果出货明细没有修改,调用报关明细保存方法
								if (ds.getModifiedRecords().length == 0
										&& ds.removed.length == 0) {
									// 保存报关明细
									if (freshFlag == true) {
										var frame = window.frames["outInfo"];
										frame.saveCha($('pId').value,
												$('currencyId').value);
									}
									// 保存应收款其他费用
									if (freshRecvFlag == true) {
										var frame = window.frames["recvInfo"];
										frame.saveOther();
									}
									// 保存应付款其他费用
									if (freshDealFlag == true) {
										var frame = window.frames["dealFeeInfo"];
										frame.saveOther();
									}
								} else {
									// 更改添加action参数
									var urlAdd = '&orderPrimId=' + res[0]
											+ '&currencyId='
											+ $('currencyId').value;
									// 更改修改action参数
									var urlMod = '&orderPrimId=' + res[0];
									ds.proxy.setApi({
										read : "cotorderout.do?method=queryOrderDetail&orderId="
												+ res[0],
										create : "cotorderout.do?method=add"
												+ urlAdd,
										update : "cotorderout.do?method=modify"
												+ urlMod,
										destroy : "cotorderout.do?method=remove"
												+ urlMod
									});
									ds.save();
								}
								Ext.getCmp('saveBtn').hide();
								reloadHome();

								Ext.Msg.alert("Message", "Successfully saved！");
							} else {
								Ext.MessageBox.alert('Message', 'Saved failed');
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
			closeandreflashEC(true, 'orderoutGrid', false);
			return;
		}

		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}
		var isPopedom = getPopedomByOpType("cotorderout.do", "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('Message', "Sorry, you do not have Authority");
			return;
		}

		// 判断出货单是否有应收帐,应付帐,是否被排载(0可删除,1有应收帐,2有应付帐,3应收应付都有,4有排载,5有其他费用)
		cotOrderOutService.checkCanDelete($('pId').value, function(flag) {
			if (flag == 5) {
				Ext.MessageBox.alert('提示消息', '该张出货单含有其他费用,不能删除!');
				return;
			}
			if (flag == 3) {
				Ext.MessageBox.alert('提示消息', '该张出货单含有应收和应付帐,不能删除!');
				return;
			}
			if (flag == 1) {
				Ext.MessageBox.alert('提示消息', '该张出货单含有应收帐,不能删除!');
				return;
			}
			if (flag == 2) {
				Ext.MessageBox.alert('提示消息', '该张出货单含有应付帐,不能删除!');
				return;
			}
			if (flag == 4) {
				Ext.MessageBox.alert('提示消息', '该张出货单已经被排载,不能删除!');
				return;
			}
			if (flag == 0) {
				var list = new Array();
				list.push($('pId').value);
				Ext.MessageBox.confirm('Message', "Are you sure to delete it?",
						function(btn) {
							if (btn == 'yes') {
								cotOrderOutService.deleteOrders(list, function(
												res) {
											if (res) {
												Ext.MessageBox.alert('Message',
														"Deleted successfully");
												closeandreflashEC(true,
														'orderoutGrid', false);
											} else {
												Ext.MessageBox.alert('Message',
														"Deleted Failed");
											}
										})
							}
						});
			}
		})
	}

	// 获得没勾选单价复选框的行的产品货号字符串
	function getNoCheckIds() {
		var rdmIds = '';
		ds.each(function(rec) {
					var ck = rec.data.checkJian;
					if (ck == null || ck == 0 || ck == false) {
						if (rec.data.rdm != null && rec.data.rdm != "") {
							rdmIds += rec.data.rdm + ",";
						}
					}
				});
		return rdmIds;
	}

	// 失去焦点事件
	function priceBlurByCurreny(newVal, oldVal) {
		// 变更汇率
		sysdicutil.getDicListByName('currency', function(res) {
			var newPriceRate = 0;
			var oldRate = 0;
			for (var i = 0; i < res.length; i++) {
				if (res[i].id == newVal) {
					newPriceRate = res[i].curRate;
				}
				if (res[i].id == oldVal) {
					oldRate = res[i].curRate;
				}
			}
			ds.each(function(rec) {
				var orderDetailId = rec.data.orderDetailId;
				if (orderDetailId != null && orderDetailId != "") {
					if (newPriceRate != 0 && oldRate != 0) {
						var oldPrice = rec.data.orderPrice;
						var newPrice = oldPrice * oldRate / newPriceRate;
						var boxCount = rec.data.boxCount;
						var money = boxCount * newPrice;
						// 重新计算总价
						$('totalLab').innerText = (parseFloat($('totalLab').innerText)
								- rec.data.totalMoney + parseFloat(money))
								.toFixed(deNum);
						rec.set("orderPrice", newPrice.toFixed(deNum));
						rec.set("totalMoney", money.toFixed(deNum));
						updateMapValue(orderDetailId, "orderPrice", newPrice
										.toFixed(deNum));
						updateMapValue(orderDetailId, "totalMoney", money
										.toFixed(deNum));
					}
				}
			});
		});
	}

	// 条款组合改变事件
	function composeChange() {
		// 价格条款
		var clauseIdText = '';
		if ($('clauseTypeId').value != '') {
			clauseIdText = clauseBox.getRawValue();
		}
		// 起运港
		var shipportIdText = '';
		if ($('shipportId') != null && $('shipportId').value != '') {
			shipportIdText = " " + shipPortBox.getRawValue();
		}
		// 目的港
		var targetportIdText = '';
		if ($('targetportId') != null && $('targetportId').value != '') {
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
				$('orderCompose').value = (clauseIdText + shipportIdText
						+ commisionScaleText + currencyIdText).toUpperCase();
			} else {
				$('orderCompose').value = (clauseIdText + targetportIdText
						+ commisionScaleText + currencyIdText).toUpperCase();
			}
		} else {
			if (clauseIdText == "FOB") {
				$('orderCompose').value = (clauseIdText + shipportIdText + currencyIdText)
						.toUpperCase();
			} else {
				$('orderCompose').value = (clauseIdText + targetportIdText + currencyIdText)
						.toUpperCase();
			}
		}
	}

	// 根据选择的客户编号自动填写表单
	function setCustomerForm(cusId) {
		DWREngine.setAsync(false);
		cotOrderOutService.getCustomerById(parseInt(cusId), function(res) {
			var ct = $("clauseTypeId").value;
			DWRUtil.setValues(res);
			// 如果客户没价格条款,不加载
			if (res.clauseId == null) {
				$("clauseTypeId").value = ct;
			}
			empBox.bindPageValue("CotEmps", "id", res.empId);
			// 如果唛标可见,设置唛标图片
			if (Ext.getCmp("orderZm").isVisible()) {
				$('order_MB').src = "./showPicture.action?flag=custMb&detailId="
						+ cusId;
			}
			commisionBox.bindValue(res.commisionTypeId);
			// 国别
			nationBox.bindPageValue("CotNation", "id", res.nationId);
			// 唛标在选择客户后更改
			$('uploadSuc').value = "0";
		});
		DWREngine.setAsync(true);
		// 延时
		var task = new Ext.util.DelayedTask(function() {
					composeChange();
				});
		task.delay(500);
	}

	// 获取单号
	function getOrderNo() {
		//INVOICE DATE 的生成规则为：点击INVOICE NO时，自动生成当天日期，可再次更改；
		if($('orderNo').value==''){
			Ext.getCmp('orderTime').setValue(new Date());
		}
		var currDate =$('orderTime').value;
		var custId = $('custId').value;
		var empId = empBox.getValue();
		if (currDate == "" || custId == null) {
			Ext.MessageBox.alert("Message", "Please enter date");
			return;
		}
		if ($('custId').value == null || $('custId').value == "") {
			Ext.MessageBox.alert("Message", "Please enter client");
			return;
		}
		if (empId == null || empId == "") {
			Ext.MessageBox.alert("Message", "Please enter sale");
			return;
		}
		

		// setNoService.getOrderOutNo(custId, currDate, function(ps) {
		// $('orderNo').value = ps;
		// });
		cotSeqService.getOrderOutNo(custId, empId, currDate, function(ps) {
					$('orderNo').value = ps;
				});
	}

	// 计算包材价格
	function calPrice() {
		var rdm = $('orderDetailId').value;
		if (rdm == null || rdm == "") {
			return;
		}
		// 在表格中查找rdm所在的行
		var index = ds.find('rdm', rdm);
		DWREngine.setAsync(false);
		cotOrderOutService.calPriceAllByOrderDetail(rdm, function(res) {
					if (res != null) {
						$('boxPbPrice').value = res[0];
						$('boxIbPrice').value = res[1];
						$('boxMbPrice').value = res[2];
						$('boxObPrice').value = res[3];
						$('packingPrice').value = res[4];
						$('inputGridPrice').value = res[6];
						DWREngine.setAsync(false);
						cotOrderOutService.getOrderMapValue(rdm, function(obj) {
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
										cotOrderOutService.setOrderMap(rdm,
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
						Ext.MessageBox.alert('Message',
								'please select a record!');
					}
				});
		DWREngine.setAsync(true);
	}

	// 删除
	function onDel() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, The order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
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
						clearFormAndSet(rightForm);
					}
					// 货款总金额
					var temp = summary.getSumTypeValue('totalMoney');
					$('totalLab').innerText = Number(temp).toFixed(deNum);
					unmask();
				});
		task.delay(500);
	}

	// 初始化页面,加载报表详细信息
	function initForm(record) {
		var eleId = record.data.eleId;
		var orderDetailId = record.data.orderDetailId;
		var flag = true;
		if (isNaN(record.id)) {
			flag = false;
		}
		// flag区分是点击已存在行调用(true)还是新添加(false)
		if (flag) {
			if(oSt!=2){
				Ext.getCmp("upmod").show();
				Ext.getCmp("updel").show();
			}else{
				Ext.getCmp("upmod").hide();
				Ext.getCmp("updel").hide();
			}
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
		if ((orderDetailId == null || orderDetailId == "")
				&& $('picPath') != null) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
			return;
		}
		DWREngine.setAsync(false);
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
			if (res != null) {
				if (res.boxCbm != null) {
					res.boxCbm = res.boxCbm.toFixed(cbmNum);
				}
				rightForm.getForm().setValues(res);
				if (popdom == true) {
					res.picPath = "common/images/noElePicSel.png";
				} else {
					if (!flag) {
						res.picPath = "./showPicture.action?flag=order&detailId="
								+ orderDetailId;
					} else {
						var rdm = Math.round(Math.random() * 10000);
						res.picPath = "./showPicture.action?flag=orderOut&detailId="
								+ record.id + "&tmp=" + rdm;
					}
				}
				// // 因为出货明细里也有个属性叫orderNo存来源订单主单编号.所以不能修改到出货主单orderNo
				// var temp = $('orderNo').value;
				// DWRUtil.setValues(res);
				// $('orderNo').value = temp;
				DWRUtil.setValue("picPath", res.picPath);
				typeLv1Box.bindPageValue("CotTypeLv1", "id", res.eleTypeidLv1);
				typeLv2Box.bindPageValue("CotTypeLv2", "id", res.eleTypeidLv2);
				eleFlagBox.setValue(res.eleFlag);
				eleHsidBox.bindPageValue("CotEleOther", "id", res.eleHsid);
				boxIbTypeBox.bindValue(res.boxIbTypeId);
				boxMbTypeBox.bindValue(res.boxMbTypeId);
				boxObTypeBox.bindValue(res.boxObTypeId);
				boxPbTypeBox.bindValue(res.boxPbTypeId);
				boxPacking.bindPageValue("CotBoxType", "id", res.boxTypeId);
				facBox.bindPageValue("CotFactory", "id", res.factoryId);
			}
		});
		DWREngine.setAsync(true);
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType("cotorderout.do", "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox
					.alert("Message", 'Sorry, you do not have Authority！');
			return;
		}
		// 如果该单没保存不能打印
		if ($('pId').value == 'null' || $('pId').value == '') {
			Ext.MessageBox.alert("Message", 'Please enter W&C P/O.！');
			return;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'orderout',
						pId : 'pId',
						pNo : 'orderNo',
						mailSendId : 'custId',
						status : 'orderStatus'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0], po[1] - 210);
			var facData = [];
			DWREngine.setAsync(false);
			cotOrderOutService.getFactorysByMainId($('pId').value,
					function(res) {
						for (var p in res)
							facData.push(["" + p, res[p]]);
					});
			DWREngine.setAsync(true);
			printWin.facBox.setValue("");
			printWin.facBox.getStore().loadData(facData);
			printWin.show();
		} else {
			printWin.hide();
		}
	};

	// 反审
	function fanShen() {
		var isCheck = getPopedomByOpType("cotorderout.do", "CHECK");
		if (isCheck == 0) {
			Ext.MessageBox
					.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}
		Ext.MessageBox.confirm('Message', "are you sure to review it?",
				function(btn) {
					if (btn == 'yes') {
						cotOrderOutService.updateOrderStatus($('pId').value, 0,
								function(res) {
									shenBox.setValue(0);
									reflashParent('orderoutGrid');
//									Ext.getCmp("qingBtn").show();
//									Ext.getCmp("fanxBtn").hide();
								});

					}
				});
	}

	// 请求审核
	function qingShen() {
		Ext.MessageBox.confirm('Message', "are you sure to review it?",
				function(btn) {
					if (btn == 'yes') {
						cotOrderOutService.updateOrderStatus($('pId').value, 3,
								function(res) {
									shenBox.setValue(3);
									reflashParent('orderoutGrid');
//									Ext.getCmp("guoBtn").show();
//									Ext.getCmp("buBtn").show();
//									Ext.getCmp("qingBtn").hide();
									// Ext.getCmp("shenTab").enable();
							});

					}
				});
	}

	// 审核通过
	function guoShen() {
		var isCheck = getPopedomByOpType("cotorderout.do", "CHECK");
		if (isCheck == 0) {
			Ext.MessageBox
					.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}
		Ext.MessageBox.confirm('Message', "您是否确定审核该单通过?", function(btn) {
					if (btn == 'yes') {
						cotOrderOutService.updateOrderStatus($('pId').value, 2,
								function(res) {
									shenBox.setValue(2);
									reflashParent('orderoutGrid');
//									Ext.getCmp("fanxBtn").show();
//									Ext.getCmp("guoBtn").hide();
//									Ext.getCmp("buBtn").hide();
								});

					}
				});
	}

	// 审核不通过
	function buShen() {
		var isCheck = getPopedomByOpType("cotorderout.do", "CHECK");
		if (isCheck == 0) {
			Ext.MessageBox
					.alert('Message', 'Sorry, you do not have Authority!');
			return;
		}
		Ext.MessageBox.confirm('Message', "您是否确定审核该单不通过?", function(btn) {
					if (btn == 'yes') {
						cotOrderOutService.updateOrderStatus($('pId').value, 1,
								function(res) {
									shenBox.setValue(1);
									reflashParent('orderoutGrid');
//									Ext.getCmp("qingBtn").show();
//									Ext.getCmp("guoBtn").hide();
//									Ext.getCmp("buBtn").hide();
								})

					}
				});
	}

	// 打开图片上传面板
	function showUploadPanel() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.MessageBox.alert('Message', 'Plesase select a record！');
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
						flag : 'orderout'
					},
					waitMsg : "Picture Loading......",
					opAction : opAction,
					imgObj : $('picPath'),
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=orderOut&temp=" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadTempPic.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}

	// 删除图片
	function delPic() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, some of order has been reviewed.Pls. review again if you want to delete it or contact the webmaster!');
			return;
		}
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.MessageBox.alert('Message', 'Please select a record！');
			return;
		}
		Ext.MessageBox.confirm('Message', "are you sure to delete it?",
				function(btn) {
					if (btn == 'yes') {
						var detailId = $('id').value;
						cotOrderOutService.deletePicImg(detailId,
								function(res) {
									if (res) {
										$('picPath').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox.alert('Message',
												'Deleted failed！');
									}
								})
					}
				});
	}

	// 更新内存数据,并修改右边表单数据
	function updateMapValue(orderDetailId, property, value) {
		DWREngine.setAsync(false);
		cotOrderOutService.updateMapValueByEleId(orderDetailId, property,
				value, function(res) {
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
		if (rdm != null && rdm != "") {
			if (isNaN(newVal) || newVal == "") {
				newVal = 0;
				$('boxCuft').value = 0;
			} else {
				var s = newVal * 35.315;
				$('boxCuft').value = s.toFixed(cbmNum);
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
						msg : 'Please enter G.W.(must be a numeric type))!',
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
		if (rdm == '' || $('eleId').value == '') {
			return;
		}
		var cbm = $('boxCbm').value;
		if (cbm == '' || isNaN(cbm)) {
			Ext.MessageBox.show({
						title : 'Message',
						msg : 'Please enter CBM(must be a numeric type)！',
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
						updateMapValue(rdm, "boxWeigth", newVal);
						updateMapValue(rdm, "boxNetWeigth", jin.toFixed(3));
						updateMapValue(rdm, "boxGrossWeigth", boxGrossWeigth);
					});

		}
	}

	// 报关行选择事件
	function hsCompanyBoxSel(com, rec, index) {
		sysdicutil.getDicListByName('hsCompany', function(res) {
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == rec.data.id) {
							var hsCompanyName = res[i].hsCompanyName;
							var hsCompanyNameEn = res[i].hsCompanyNameEn;
							var hsContactNbr = res[i].hsContactNbr;
							var hsFax = res[i].hsFax;
							var hsContantPerson = res[i].hsContantPerson;
							var hsAdd = res[i].hsAdd;
							$('hsCompanyStr').value = hsCompanyName
									+ "  Phone:" + hsContactNbr + "  Fax:"
									+ hsFax + "  Contact:" + hsContantPerson
									+ "  Address:" + hsAdd;
							break;
						}
					}
				});
	}

	// 拖车行选择事件
	function trailCarBoxSel(com, rec, index) {
		sysdicutil.getDicListByName('trailCar', function(res) {
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == rec.data.id) {
							var name = res[i].name;
							var nameEn = res[i].nameEn;
							var contactPhone = res[i].contactPhone;
							var trailFax = res[i].trailFax;
							var contactPerson = res[i].contactPerson;
							var addrEn = res[i].addrEn;
							$('trailerStr').value = name + "  Phone:"
									+ contactPhone + "  Fax:" + trailFax
									+ "  Contact:" + contactPerson
									+ "  Address:" + addrEn;
							break;
						}
					}
				});
	}

	// 船公司选择事件
	function shipBoxSel(com, rec, index) {
		sysdicutil.getDicListByName('shipCompany', function(res) {
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == rec.data.id) {
							var companyName = res[i].companyName;
							var companyNameEn = res[i].companyNameEn;
							var companyNbr = res[i].companyNbr;
							var companyFax = res[i].companyFax;
							var companyContact = res[i].companyContact;
							var companyAddr = res[i].companyAddr;
							$('shipCompanyStr').value = companyName
									+ "  Phone:" + companyNbr + "  Fax:"
									+ companyFax + "  Contact:"
									+ companyContact + "  Address:"
									+ companyAddr;
							break;
						}
					}
				});
	}

	// 操作货代选择事件
	function hsConsignBoxSel(com, rec, index) {
		sysdicutil.getDicListByName('consignCompany', function(res) {
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == rec.data.id) {
							var companyName = res[i].companyName;
							var companyNameEn = res[i].companyNameEn;
							var companyNbr = res[i].companyNbr;
							var companyFax = res[i].companyFax;
							var companyContact = res[i].companyContact;
							var companyAddr = res[i].companyAddr;
							$('hsConsignCompanyStr').value = companyName
									+ "  Phone:" + companyNbr + "  Fax:"
									+ companyFax + "  Contact:"
									+ companyContact + "  Address:"
									+ companyAddr;
							break;
						}
					}
				});
	}

	// 指定货代选择事件
	function consignBoxSel(com, rec, index) {
		sysdicutil.getDicListByName('consignCompany', function(res) {
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == rec.data.id) {
							var companyName = res[i].companyName;
							var companyNameEn = res[i].companyNameEn;
							var companyNbr = res[i].companyNbr;
							var companyFax = res[i].companyFax;
							var companyContact = res[i].companyContact;
							var companyAddr = res[i].companyAddr;
							$('consignCompanyStr').value = companyName
									+ "  Phone:" + companyNbr + "  Fax:"
									+ companyFax + "  Contact:"
									+ companyContact + "  Address:"
									+ companyAddr;
							break;
						}
					}
				});
	}

	// 添加货号
	function addOrders() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, some of order has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return;
		}
		// 验证表单
		var formData = getFormValues(orderForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var cfg = {};
		cfg.custId = $('custId').value;
		cfg.txtAreaAry = _self.txtAreaAry;

		var orderWin = new OrderWin(cfg);
		orderWin.show();
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
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
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
						cotOrderOutService.setOrderMap(orderDetailId, res,
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
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
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
						cotOrderOutService.setOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
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
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
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
						var box20Count = Math.floor(con20 / boxCbm) * newVal;
						var box40Count = Math.floor(con40 / boxCbm) * newVal;
						var box40hqCount = Math.floor(con40H / boxCbm) * newVal;
						var box45Count = Math.floor(con45 / boxCbm) * newVal;

						res.box20Count = box20Count;
						res.box40Count = box40Count;
						res.box40hqCount = box40hqCount;
						res.box45Count = box45Count;
						// 1.计算毛净重
						DWREngine.setAsync(false);
						cotElementsService.getDefaultList(function(dfg) {
									var gross = res.boxWeigth * newVal / 1000;
									if (dfg.length != 0) {
										if (dfg[0].grossNum != null) {
											gross += dfg[0].grossNum;
										}
									}
									res.boxNetWeigth = res.boxWeigth * newVal
											/ 1000;
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
						cotOrderOutService.setOrderMap(orderDetailId, res,
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
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
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
						cotOrderOutService.setOrderMap(orderDetailId, res,
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
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
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
						cotOrderOutService.setOrderMap(orderDetailId, res,
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
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
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
						cotOrderOutService.setOrderMap(orderDetailId, res,
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
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
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
						cotOrderOutService.setOrderMap(orderDetailId, res,
								function(res) {
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 加载报关明细
	// 第一次显示差额页面.再点不刷新
	var freshFlag = false;
	function loadOutInfo() {
		if (freshFlag == false) {
			var frame = window.frames["outInfo"];
			frame.location.href = "cotorderout.do?method=showCha&orderId="
					+ $('pId').value;
		}
		freshFlag = true;
	}

	// 加载排载明细
	// 第一次显示排载明细页面.再点不刷新
	var freshSplitFlag = false;
	function loadSplitInfo() {
		if (freshSplitFlag == false) {
			var frame = window.frames["outSplit"];
			frame.location.href = "cotsplitinfo.do?method=queryOrderOut&orderId="
					+ $('pId').value;
		}
		freshSplitFlag = true;
	}

	// 验证表单
	this.formVail = function() {
		var formData = getFormValues(orderForm, true);
		// 表单验证失败时,返回
		return formData;
	}

	// 统一更改订单品名
	function updatePin() {
		var cord = sm.getSelections();
		Ext.each(cord, function(rec) {
			rec.set('orderName', $('pin').value);
			updateMapValue(rec.data.orderDetailId, "orderName", $('pin').value);
		});
		modlPanel.hide();
	}

	// 显示分类品名面板
	var modlPanel;
	function showModel(item, pressed) {
		// 判断是否有勾选明细
		var cord = sm.getSelections();
		if (cord.length == 0) {
			Ext.MessageBox.alert("提示消息", "请勾选需要修改分类品名的出货明细!");
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
				height : 80,
				width : 220,
				layout : 'hbox',
				frame : true,
				title : "Please enter des. of Category",
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
							xtype : "label",
							text : "Description:",
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

	// 打开上传面板,用于上次唛标
	function showUploadMaiPanel() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, some of order has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return;
		}
		var id = $('pId').value;
		var win = new UploadWin({
					params : {
						mainId : id,
						mbType : "1"
					},
					waitMsg : "Picture uploading......",
					opAction : "modify",
					imgObj : $('order_MB'),
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=orderOutMB&temp=" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadMBPic.action',
					validType : "jpg|png|bmp|gif",
					returnFlag : true
				})
		win.show();
	}

	// 删除唛标图片
	function delMBPic() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, some of order has been reviewed.Pls. review again if you want to edit it or contact the webmaster!');
			return;
		}
		Ext.MessageBox.confirm('Message',
				"are you sure to delete marks picture?", function(btn) {
					if (btn == 'yes') {
						var pId = $('pId').value;
						cotOrderOutService.deleteMBPicImg(pId, function(res) {
							if (res) {
								$('order_MB').src = "common/images/zwtp.png";
							} else {
								Ext.MessageBox
										.alert('Message',
												'are you sure to delete marks picture！');
							}
						})
					}
				});
	}

	// 导入更新
	function excelImport() {
		if ($('pId').value == '' || $('pId').value == 'null') {
			Ext.MessageBox.alert('Message', '该出货单还没保存,不能更新！');
			return;
		}
		var num = 0;
		ds.each(function(item) {
					if (!isNaN(item.id)) {
						num++;
					}
				});
		if (num == 0) {
			Ext.MessageBox.alert('Message', '该出货单没有明细,不能更新！如果有新增明细,请先保存再导入更新!');
			return;
		}
		var reportWin = new ReportWin();
		reportWin.show();
	}
	// =====================chi-ch=====2010-04-29========start===========
	// 加载应付款
	// 第一次显示其它费用页面.再点不刷新
	var freshDealFlag = false;
	function loadDealFeeInfo() {
		if (freshDealFlag == false) {
			var frame = document.dealFeeInfo;
			frame.location.href = "cotorderout.do?method=queryFinanceDeal"
					+ "&type=1&fkId=" + $('pId').value + "&source=orderout";
		}
		freshDealFlag = true;
	}
	// =====================chi-ch=====2010-04-29======end=============

	// 第一次显示其它费用页面.再点不刷新
	var freshRecvFlag = false;
	function loadRecvInfo() {
		if (freshRecvFlag == false) {
			var frame = document.recvInfo;
			frame.location.href = "cotorderout.do?method=queryRecvOther&fkId="
					+ $('pId').value;
		}
		freshRecvFlag = true;
	}

	// 计算英寸
	function calInch(txt, newVal, oldVal) {
		var rdm = $('orderDetailId').value;
		if (rdm == null || rdm == "") {
			return;
		}
		var inch = (newVal / 2.54).toFixed("2");
		var needId = txt.getName() + "Inch";
		$(needId).value = inch;
		updateMapValue(rdm, needId, inch);
		calPrice();
	}

	// 计算CM
	function calCm(txt, newVal, oldVal) {
		var rdm = $('orderDetailId').value;
		if (rdm == null || rdm == "") {
			return;
		}
		var cm = (newVal * 2.54).toFixed("2");
		var needId = txt.getName().substring(0, txt.getName().length - 4);
		$(needId).value = cm;
		updateMapValue(rdm, needId, cm);
		calPrice();
	}

	// 给其他费用页面调用,验证主表单
	this.checkParent = function() {
		// 验证表单
		var formData = getFormValues(orderForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return false;
		} else {
			return true;
		}
	}
	function changeUnSplit(rec, value, oldValue) {
		if (value == '' || isNaN(value)) {
			value = 0;
		}

		var orderDetailId = rec.data.orderDetailId;
		var remainBoxCount = rec.get("remainBoxCount");
		// 已排载数量
		var splitCount = oldValue - remainBoxCount
		var dif = value - oldValue + remainBoxCount;
		rec.set("remainBoxCount", dif);
		DWREngine.setAsync(false);
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
					if (res != null) {
						res.remainBoxCount = dif;
						cotOrderOutService.setOrderMap(orderDetailId, res,
								function(result) {
								});
					}
				});
		DWREngine.setAsync(true);
	}
	// 数量值改变事件
	function countNum(newVal) {
		if (newVal == '' || isNaN(newVal)) {
			newVal = 0;
		}
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		DWREngine.setAsync(false);
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
			if (res != null) {
				// 未出货数量
//				var unSendNum = rec.data.unSendNum;
//				var nowRes = newVal - res.boxCount;
//				var temp = unSendNum - nowRes;
				// 如果未出货量变成负数.还原为0
//				if (temp < 0) {
//					rec.set('boxCount', res.boxCount);
//					newVal = res.boxCount;
//					var tip = new Ext.ToolTip({
//								title : '超额提示',
//								anchor : 'left',
//								html : '您输入的数量>(出货数量+未出货数)！'
//							});
//					tip.showAt([ckX, ckY]);
//				} else {
					// 单价
					var orderPrice = rec.data.orderPrice;
					// 箱数
					var containerCount = 0;
					// 外箱数
					var boxObCount = rec.data.boxObCount;
					
					if (boxObCount != null && boxObCount != ''
							&& boxObCount != 0) {
						// 计算箱数
						if (newVal % boxObCount != 0) {
							containerCount = parseInt(newVal / boxObCount) + 1;
						} else {
							containerCount = newVal / boxObCount;
						}
					}
					res.containerCount = containerCount;
					rec.set('containerCount', containerCount);
					var totalMoney = (newVal * orderPrice).toFixed(deNum);
					// 重新计算总价
					$('totalLab').innerText = (parseFloat($('totalLab').innerText)
							- res.totalMoney + parseFloat(totalMoney))
							.toFixed(deNum);
					res.totalMoney = totalMoney;
					rec.set('totalMoney', totalMoney);
//					rec.set('unSendNum', temp);
//				}
				res.boxCount = newVal;
				// 将对象储存到后台map中
				cotOrderOutService.setOrderMap(orderDetailId, res, function(
								result) {
						});
			}
		});
		DWREngine.setAsync(true);
	}

	// 箱数改变情况
	function containNum(newVal) {
		if (newVal == '' || isNaN(newVal)) {
			newVal = 0;
		}
		var rec = editRec;
		var orderDetailId = rec.data.orderDetailId;
		DWREngine.setAsync(false);
		cotOrderOutService.getOrderMapValue(orderDetailId, function(res) {
			if (res != null) {
				// 未出货数量
				var unSendNum = rec.data.unSendNum;
				// 外箱数
				var boxObCount = rec.data.boxObCount;
				var boxCount = newVal * boxObCount;
				var nowRes = boxCount - res.boxCount;
				var temp = unSendNum - nowRes;
//				// 如果未出货量变成负数.还原为原来值
//				if (temp < 0) {
//					rec.set('containerCount', res.containerCount);
//					newVal = res.containerCount;
//					var tip = new Ext.ToolTip({
//								title : 'Over Tips',
//								anchor : 'left',
//								html : '未出货数不足您输入的箱数！'
//							});
//					tip.showAt([ckX, ckY]);
//				} else {
					// 单价
					var orderPrice = rec.data.orderPrice;
					var totalMoney = (boxCount * orderPrice).toFixed('2');
					// 重新计算总价
					$('totalLab').innerText = (parseFloat($('totalLab').innerText)
							- res.totalMoney + parseFloat(totalMoney))
							.toFixed(deNum);

					res.unSendNum = temp;
					res.boxCount = boxCount;
					res.totalMoney = totalMoney;

					rec.set('unSendNum', temp);
					rec.set('boxCount', boxCount);
					rec.set('totalMoney', totalMoney);
//				}
				res.containerCount = newVal;
				// 将对象储存到后台map中
				cotOrderOutService.setOrderMap(orderDetailId, res, function(
								result) {
						});
			}
		});
		DWREngine.setAsync(true);
	}

	// 保存排序
	function sumSortTable() {
		var sort = ds.getSortState();
		if (!sort) {
			Ext.MessageBox.alert("Message", "排序没变化,不用再保存!");
			return;
		}
		if ($("pId").value == '' || $("pId").value == 'null') {
			Ext.MessageBox.alert("Message", "请先保存出货单,再更改排序!");
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
		cotOrderOutService.updateSortNo(type, sort.field, fieldType, function(
						res) {
					if (res) {
						ds.reload();
						Ext.MessageBox.alert('Message',
								"The current sort is  saved successfully!");
					} else {
						Ext.MessageBox.alert('Message',
								"The current sort is saved failed!");
					}
				});
		DWREngine.setAsync(true);
	}

});

// 显示公司资料窗体
function showCompanyWin() {
	var com = new CompanyWin();
	com.show();
}

// 显示客户资料窗体
function showCustWin(flag) {
	var cfg = {};
	cfg.flag = flag;
	var com = new CustWin(cfg);
	com.show();
}

// 锁定价格列全选/反选
function checkAll(obj) {
	checkColumn.checkAll(obj.checked);
}

// 统计所有出货明细的订单品名
function findAllPin(flag) {
	var orderId = $('pId').value;
	var hsName = "";
	var rptContainerCount = "";
	var rptBoxCount = "";
	var rptCbm = "";
	var rptNetW = "";
	var rptGrossW = "";
	var rptAvgPrice = "";
	var rptTotalMoney = "";

	DWREngine.setAsync(false);
	if (flag == 0) {
		cotOrderOutService.findAllPin(orderId, function(res) {
					for (var i = 0; i < res.length; i++) {
						if (res[i].orderName != null) {
							hsName += res[i].orderName + "\n";
						}
						if (res[i].totalContainerCount != null) {
							rptContainerCount += res[i].totalContainerCount
									+ "\n";
						}
						if (res[i].totalCount != null) {
							rptBoxCount += res[i].totalCount + "\n";
						}
						if (res[i].totalCbm != null) {
							rptCbm += res[i].totalCbm.toFixed(cbmNum) + "\n";
						}
						if (res[i].totalNet != null) {
							rptNetW += res[i].totalNet + "\n";
						}
						if (res[i].totalGross != null) {
							rptGrossW += res[i].totalGross + "\n";
						}
						if (res[i].avgPrice != null) {
							rptAvgPrice += res[i].avgPrice.toFixed(deNum)
									+ "\n";
						}
						if (res[i].totalMoney != null) {
							rptTotalMoney += res[i].totalMoney.toFixed(deNum)
									+ "\n";
						}
					}
					$('hsName').value = hsName;
					$('rptContainerCount').value = rptContainerCount;
					$('rptBoxCount').value = rptBoxCount;
					$('rptCbm').value = rptCbm;
					$('rptNetW').value = rptNetW;
					$('rptGrossW').value = rptGrossW;
					$('rptAvgPrice').value = rptAvgPrice;
					$('rptTotalMoney').value = rptTotalMoney;
				});
	}
	if (flag == 1) {
		var sizeCon = $('sizeCon').checked;
		cotOrderOutService.findAllOutPin(orderId, function(res) {
					for (var i = 0; i < res.length; i++) {
						if (res[i].orderName != null) {
							if (!sizeCon) {
								hsName += res[i].orderName + "\n";
							} else {
								// 查找相同报关品名的中文规格
								cotOrderOutService.findAllSizeByName(
										res[i].orderName, orderId,
										function(alt) {
											if (alt != '') {
												hsName += res[i].orderName
														+ " size:" + alt + "\n";
											}
										});
							}
						}
						if (res[i].totalContainerCount != null) {
							rptContainerCount += res[i].totalContainerCount
									+ "\n";
						}
						if (res[i].totalCount != null) {
							rptBoxCount += res[i].totalCount + "\n";
						}
						if (res[i].totalCbm != null) {
							rptCbm += res[i].totalCbm.toFixed(cbmNum) + "\n";
						}
						if (res[i].totalNet != null) {
							rptNetW += res[i].totalNet + "\n";
						}
						if (res[i].totalGross != null) {
							rptGrossW += res[i].totalGross + "\n";
						}
						if (res[i].avgPrice != null) {
							rptAvgPrice += res[i].avgPrice.toFixed(deNum)
									+ "\n";
						}
						if (res[i].totalMoney != null) {
							rptTotalMoney += res[i].totalMoney.toFixed(deNum)
									+ "\n";
						}
					}
					$('hsOutName').value = hsName;
					$('rptOutContainerCount').value = rptContainerCount;
					$('rptOutBoxCount').value = rptBoxCount;
					$('rptOutCbm').value = rptCbm;
					$('rptOutNetW').value = rptNetW;
					$('rptOutGrossW').value = rptGrossW;
					$('rptOutAvgPrice').value = rptAvgPrice;
					$('rptOutTotalMoney').value = rptTotalMoney;
				});
	}
	DWREngine.setAsync(true);
}
