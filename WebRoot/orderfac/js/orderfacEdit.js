window.onbeforeunload = function() {
	var ds = Ext.getCmp('orderfacGrid').getStore();
	if (ds.getModifiedRecords().length != 0 || ds.removed.length != 0) {
		if (Ext.isIE) {
			if (document.body.clientWidth - event.clientX < 170
					&& event.clientY < 0 || event.altKey) {
				return "Purchase detail has changed, you sure do not save?";
			} else if (event.clientY > document.body.clientHeight
					|| event.altKey) { // 用户点击任务栏，右键关闭
				return "Purchase detail has changed, you sure do not save?";
			} else { // 其他情况为刷新
			}
		} else if (Ext.isChrome || Ext.isOpera) {
			return "Purchase detail has changed, you sure do not save?";
		} else if (Ext.isGecko) {
			window.open("http://www.g.cn")
			var o = window.open("index.do?method=logoutAction");
		}
	}
}

// 获取报价和订单的单价要保留几位小数
// var deNum = getDeNum("facPrecision");

Ext.onReady(function() {
	var orderfacMap;
	var nationMap;
	var cityMap;
	var companyRemarkMap;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotConsignCompany", "id", "companyRemark",
			function(res) {
				companyRemarkMap = res;
			});
	baseDataUtil.getBaseDicDataMap("CotNation", "id", "nationName", function(
					res) {
				nationMap = res;
			});
	baseDataUtil.getBaseDicDataMap("CotNationCity", "id", "cityName", function(
					res) {
				cityMap = res;
			});
	// 加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotOrderFacdetail', function(
					rs) {
				orderfacMap = rs;
			});
	DWREngine.setAsync(true);

	// -----------------本地下拉框-----------------------------------------
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
				anchor : '100%',
				readOnly : true,
				sendMethod : "post",
				selectOnFocus : true,
				allowBlank : false,
				blankText : "Choose Dep. Of PRODUCT！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var thStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['Yes', 'Yes'], ['No', 'No']]
			});
	var themesBox = new Ext.form.ComboBox({
				name : 'themes',
				fieldLabel : 'Themes',
				editable : false,
				store : thStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 18,
				hiddenName : 'themes',
				selectOnFocus : true
			});

	// 货代
	var companyCodeBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotConsignCompany&key=companyNameEn",
		cmpId : 'forwardt',
		id : 'forwardingAgent',
		fieldLabel : 'Forwarding Agent',
		labelSeparator : ":",
		editable : true,
		sendMethod : "post",
		autoLoad : true,
		valueField : "id",
		displayField : "companyNameEn",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 35,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		listeners : {
			'select' : function(com, rs, index) {
				if (!Ext.isEmpty(com.getValue())) {
					var companyId = com.getValue();
					Ext.getCmp('booking').setValue(companyRemarkMap[companyId]);
				}
			},
			'afterrender' : function(area) {
				if ($('pId').value != '' && $('pId').value != 'null') {
					// alert(txtAreaAry.forwardingAgent);
					companyCodeBox.bindPageValue('CotConsignCompany', 'id',
							txtAreaAry.forwardingAgent);
				}
			}
		}
	});
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
				emptyText : 'Please select',
				hiddenName : 'eleFlag',
				selectOnFocus : true
			});

	// 审核状态
	var orderStatusStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'Waiting'], [1, 'No Pass'], [2, 'Approved'],
						[3, 'Approval'], [9, 'No Audit']]
			});
	var orderStatusBox = new Ext.form.ComboBox({
				name : 'orderStatus',
				fieldLabel : 'Status',
				value : 9,
				editable : false,
				disabledClass : 'combo-disabled',
				store : orderStatusStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				disabled : true,
				triggerAction : 'all',
				anchor : "100%",
				tabIndex : 11,
				emptyText : 'Please select',
				hiddenName : 'orderStatus',
				selectOnFocus : true
			});
	// -----------------远程下拉框-----------------------------------------
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
				disabled : true,
				disabledClass : 'combo-disabled',
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,
				listeners : {
					"select" : function(com, rec, index) {
						// composeChange();
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
		disabled : true,
		disabledClass : 'combo-disabled',
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
				// composeChange();
			}
		}
	});
	// 付款方式
	var payTypeBox = new BindCombox({
				cmpId : 'payTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotPayType",
				emptyText : 'Choose',
				fieldLabel : "Pay.Terms",
				displayField : "payName",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				tabIndex : 14
			});
	// 产品来源
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['WOLLY', 'WOLLY'], ['SUPPLIER', 'SUPPLIER'],
						['CUSTOMER', 'CUSTOMER']]
			});
	var sourceBox = new Ext.form.ComboBox({
				name : 'eleForm',
				tabIndex : 48,
				fieldLabel : 'Source',
				editable : false,
				value : 'WOLLY',
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				disabled : true,
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				hiddenName : 'eleForm',
				selectOnFocus : true
			});
	// 条款类型
	var clauseBox = new BindCombox({
				cmpId : 'clauseTypeId',
				dataUrl : "servlet/DataSelvert?tbname=CotClause",
				emptyText : 'Choose',
				fieldLabel : "<font color=red>Delivery Terms</font>",
				displayField : "clauseName",
				valueField : "id",
				triggerAction : "all",
				allowBlank : false,
				blankText : "Please choose the Delivery Terms!",
				anchor : "100%",
				tabIndex : 15,
				listeners : {
					"select" : function(com, rec, index) {
						// priceBlur();
						// composeChange();
					}
				}
			});
	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				fieldLabel : "Currency",
				emptyText : 'Choose',
				allowBlank : false,
				disabled : true,
				disabledClass : 'combo-disabled',
				blankText : "Currency is required！",
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
				emptyText : 'Please select',
				pageSize : 10,
				sendMethod : "post",
				anchor : "100%",
				tabIndex : 34,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

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
		blankText : "Supplie is requered！",
		pageSize : 10,
		anchor : "100%",
		tabIndex : 1,
		selectOnFocus : true,
		sendMethod : "post",
		disabled : true,
		disabledClass : 'combo-disabled',
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		listeners : {
			"select" : function(com, rec, index) {
				facContactBox.reset();
				facContactBox.loadValueById('CotContact', 'factoryId', rec
								.get("id"));
				cotFactoryService.getFactoryById(facId, function(res) {
							if (res != null) {
								// TODO:
								var city = '';
								var post = '';
								var nation = '';
								var fullName = '';
								var adr = '';
								if (!Ext.isEmpty(res.factoryName)) {
									fullName = res.factoryName + ' \n'
								}
								if (!Ext.isEmpty(res.factoryAddr)) {
									adr = res.factoryAddr + ' \n'
								}
								if (!Ext.isEmpty(res.post)) {
									post = res.post + ','
								}
								if (!Ext.isEmpty(cityMap[res.cityId])) {
									city = cityMap[res.cityId] + ' \n'
								} else {
									if (!Ext.isEmpty(res.post)) {
										post = res.post + ' \n'
									}
								}
								if (!Ext.isEmpty(nationMap[res.nationId])) {
									nation = nationMap[res.nationId];
								}
								var str = fullName + adr + post + city + nation;
								var start = str.indexOf(' ');
								var end = str.lastIndexOf(' ');
								var strLength = str.length - 2;
								if (start == end) {
									if (start == strLength) {
										str = str.substring(0, strLength);
									}
								} else {
									str = str.substring(0, end);
								}
								Ext.getCmp('seller').setValue(str);
							}
						});
			}

		}

	});

	// 厂家接收人
	var facContactBox = new BindCombox({
				cmpId : 'facContactId',
				mode : 'local',
				dataUrl : "servlet/DataSelvert?tbname=CotContact",
				emptyText : 'Choose',
				fieldLabel : "Contac",
				displayField : "contactPerson",
				valueField : "id",
				sendMethod : "post",
				triggerAction : "all",
				anchor : "100%"
			});

	// 海关编码
	var eleHsidBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "HS code",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "cnName",
				emptyText : 'Please select',
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
				fieldLabel : "<font color=red>Sale</font>",
				editable : true,
				sendMethod : "POST",
				valueField : "id",
				readOnly : true,
				displayField : "empsName",
				mode : 'remote',// 默认local
				// autoLoad:false,//默认自动加载
				pageSize : 10,
				anchor : "100%",
				selectOnFocus : true,
				allowBlank : false,
				blankText : "Sale is requered！",
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
		disabled : true,
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
				disabled : true,
				disabledClass : 'combo-disabled',
				anchor : "100%",
				tabIndex : 13,
				listeners : {
					"select" : function(com, rec, index) {
						priceBlur();
					}
				}
			});

	// 审核人
	var empChkBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'checkPerson',
				fieldLabel : "Approve Person",
				disabled : true,
				disabledClass : 'combo-disabled',
				valueField : "id",
				displayField : "empsName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 4,
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
				// fieldLabel : "Packing Way",
				hidden : true,
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
				fieldLabel : "Cell type plug",
				tabIndex : 53,
				displayField : "value",
				cmpId : "inputGridTypeId",
				emptyText : 'Please select',
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
				fieldLabel : "Material Type",
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
				fieldLabel : "Material Type",
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
				fieldLabel : "Material Type",
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
				fieldLabel : "Material Type",
				tabIndex : 65,
				displayField : "value",
				cmpId : "boxObTypeId",
				emptyText : 'Choose',
				labelSeparator : " ",
				anchor : "100%",
				disabled : true,
				listeners : {
					'change' : function() {
						calPrice();
					}
				}
			});

	// 审核人
	var empShenBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&typeName=shenFlag&type=1&logId="
				+ logId,
		cmpId : 'shenPerson',
		fieldLabel : "Audit",
		editable : true,
		flex : 2,
		valueField : "id",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "Please choose Sale!",
		tabIndex : 4,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Classification of products",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "typeName",
				emptyText : 'Please select',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 38,
				sendMethod : "post",
				selectOnFocus : true,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 创建人
	var addEmpBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'addPerson',
		fieldLabel : "Add Person",
		editable : true,
		disabled : true,
		disabledClass : 'combo-disabled',
		valueField : "id",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "Please choose add person!",
		tabIndex : 23,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 修改人
	var modEmpBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'modPerson',
		fieldLabel : "Revise Person",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		disabled : true,
		disabledClass : 'combo-disabled',
		blankText : "Please choose modify person!",
		tabIndex : 24,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	var facNum = getDeNum("facPrecision");
	// cbm保留几位小数
	var cbmNum = getDeNum("cbmPrecision");
	// 根据小数位生成"0.000"字符串
	var facNumTemp = getDeStr(facNum);
	var cbmStr = getDeStr(cbmNum);
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
				name : "barcode"
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
			}, {
				name : "eleMod"
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
						read : "cotorderfac.do?method=queryDetail&orderId="
								+ $('pId').value + "&flag=orderFacDetail",
						create : "cotorderfac.do?method=addDetail",
						update : "cotorderfac.do?method=modifyDetail",
						destroy : "cotorderfac.do?method=removeDetail"
					},

					listeners : {
						beforeload : function(store, options) {
							ds.removed = [];
							cotOrderFacService.clearMap(function(res) {
									});
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
							if (action == 'destroy')
								return;
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
			sortable : true,
			editable : false
		},
		hiddenCols : orderfacMap,
		columns : [
				sm,// 添加复选框列
				{
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
					editor : new Ext.form.TextField({
								maxLength : 100,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									},
									"change" : function(txt, newVal, oldVal) {
										if (newVal == "") {
											txt.setValue(oldVal);
										} else {
											txt.setValue(newVal.toUpperCase());
										}
									}
								}
							}),
					width : 120,
					summaryRenderer : function(v, params, data) {
						return "Total：";
					}
				}, {
					header : "Supplier's Art No.",
					dataIndex : "factoryNo",
					editor : new Ext.form.TextField({
								maxLength : 100
							}),
					width : 140
				}, {
					header : "Client Art No.",
					dataIndex : "custNo",
					editor : new Ext.form.TextField({
								maxLength : 100
							}),
					width : 100
				}, {
					header : "Barcode",
					dataIndex : "barcode",
					editor : new Ext.form.TextField({
								maxLength : 100
							}),
					width : 120
				}, {
					header : "Chinese name",
					dataIndex : "eleName",
					editor : new Ext.form.TextArea({
								maxLength : 200
							}),
					hidden : true,
					width : 100
				}, {
					header : "Descripiton",
					dataIndex : "eleNameEn",
					editor : new Ext.form.TextArea({
								maxLength : 500
							}),
					width : 100
				}, {
					header : "Unit Price",
					dataIndex : "taoUnit",
					editor : new Ext.form.TextField({
								width : 400
							}),
					hidden : true,
					width : 60
				}, {
					header : "Unit",
					dataIndex : "eleUnit",
					editor : new Ext.form.TextField({
								width : 400
							}),
					width : 40
				}, {
					header : "Quantity",
					dataIndex : "boxCount",
					editor : new Ext.form.NumberField({
								maxLength : 100,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
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
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							}),
					width : 60,
					summaryType : 'sum'
				}, {
					header : "Size Description",
					dataIndex : "eleInchDesc",
					width : 100
				}, {
					header : "Purchase Price",
					dataIndex : "priceFac",
					editable : false,
					editor : new Ext.form.NumberField({
								maxValue : 99999999.9999,
								decimalPrecision : facNum,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							}),
					width : 100
				}, {
					header : "Total Purchase Price",
					dataIndex : "totalFac",
					width : 140,
					hidden : getPopedomByOpType("cotorder.do", "SELPIPRICE") == 0
							? true
							: false,
					summaryType : 'sum'
				}, {
					header : "MOQ.",
					dataIndex : "eleMod",
					editor : new Ext.form.NumberField({
								maxValue : 99999999,
								decimalPrecision : facNum,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							}),
					width : 60
				}, {
					header : "Chinese Size",
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
					hidden : true,
					width : 140
				}, {
					header : "English sizs",
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
					hidden : true,
					width : 140
				}, {
					header : "Inner Box",
					dataIndex : "boxIbCount",
					editor : new Ext.form.NumberField({
								width : 400,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							}),
					width : 80
				}, {
					header : "Center",
					dataIndex : "boxMbCount",
					editor : new Ext.form.NumberField({
								width : 400,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							}),
					hidden : true,
					width : 40
				}, {
					header : "Outer Box",
					dataIndex : "boxObCount",
					editor : new Ext.form.NumberField({
								width : 400,
								listeners : {
									'focus' : function(txt) {
										txt.selectText();
									}
								}
							}),
					width : 80
				}, {
					header : "Carton Length",
					dataIndex : "boxObL",
					width : 50,
					hidden : true,
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
					header : "Carton width",
					dataIndex : "boxObW",
					width : 50,
					hidden : true,
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
					header : "Carton high",
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
					header : "G.W(Kgs)",
					dataIndex : "boxGrossWeigth",
					width : 80,
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
					header : "N.W(Kgs)",
					dataIndex : "boxNetWeigth",
					width : 80,
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
							text : "Add Art No.",
							iconCls : "page_add",
							handler : showImportPanel,
							hidden : true
						}, '-', {
							text : "Delete Art No.",
							handler : onDel,
							iconCls : "page_del",
							hidden : true
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
	// 计划时间表单
	var timeForm = new Ext.form.FormPanel({
		title : "",
		autoScroll : true,
		region : "center",
		formId : "timeForm1",
		labelWidth : 100,
		labelAlign : "right",
		layout : "form",
		padding : "5px",
		frame : true,
		items : [{
					xtype : "fieldset",
					title : "Pre-production",
					layout : "column",
					anchor : "98%",
					items : [{
								xtype : "panel",
								title : "Dealline",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Colour Approval/Swatch ",
											id : "simpleSampleDeadline",
											// //value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											fieldLabel : "Pre-production",
											id : "completeSampleDeadline",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}, {
								xtype : "panel",
								title : "Approval",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Colour Approval/Swatch ",
											id : "simpleSampleApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											fieldLabel : "Pre-production",
											id : "completeSampleApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}]
				}, {
					xtype : "fieldset",
					title : "Artwork Approval",
					layout : "column",
					anchor : "98%",
					items : [{
								xtype : "panel",
								title : "Dealline",
								labelWidth : 160,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Artwork Approval",
											id : "facDeadline",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											fieldLabel : "Artwork Physical Approval",
											id : "pcaketDeadline",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}, {
								xtype : "panel",
								title : "Approval",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Artwork Approval",
											id : "facApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											fieldLabel : "Client Approval",
											id : "pcaketApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}]
				}, {
					xtype : "fieldset",
					title : "Shipment Samples",
					layout : "column",
					anchor : "98%",
					items : [{
								xtype : "panel",
								title : "Dealline",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Photo Samples",
											id : "samplePicDeadline",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											fieldLabel : "Shipment Sample",
											id : "sampleOutDeadline",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}, {
								xtype : "panel",
								title : "Approval",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Approval",
											id : "samplePicApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											fieldLabel : "Approval",
											id : "sampleOutApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}]
				}, {
					xtype : "fieldset",
					title : "Quality Control",
					layout : "column",
					anchor : "98%",
					items : [{
								xtype : "panel",
								title : "Dealline",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Expected QC",
											// value:new Date(),
											format : 'Y-m-d',
											id : "qcDeadline",
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : 'textfield',
											fieldLabel : 'QC Location',
											name : 'qcLocation',
											id : 'qcLocation',
											anchor : "100%"
										}]
							}, {
								xtype : "panel",
								title : "Approval",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "QC Approval",
											id : "qcApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}]
				}, {
					xtype : "fieldset",
					title : "Shipment",
					layout : "column",
					anchor : "98%",
					items : [{
								xtype : "panel",
								title : "Dealline",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Shipment Check",
											id : "shippingDeadline",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											fieldLabel : "Shipping Advice",
											id : "loadingDeadline",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}, {
								xtype : "panel",
								title : "Approval",
								labelWidth : 150,
								labelAlign : "left",
								layout : "form",
								columnWidth : 0.5,
								items : [{
											xtype : "cdatefield",
											fieldLabel : "Shipment Check",
											id : "shippingApproval",
											// value:new Date(),
											format : 'Y-m-d',
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}, {
											xtype : "cdatefield",
											// fieldLabel : "Shipping Advice",
											fieldLabel : 'Client Sent',
											// value:new Date(),
											format : 'Y-m-d',
											id : "loadingApproval",
											anchor : "100%",
											listeners : {
												'focus' : function(com) {
													if (Ext.isEmpty(com
															.getValue())) {
														com
																.setValue(new Date());
													}
												}
											}
										}]
							}]
				}]
	});
	// 右边折叠面板
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
					hidden : true,
					handler : showUploadPanel,
					id : "upmod"
						// iconCls : "upload-icon"
					}, {
					width : 40,
					text : "del",
					handler : delPic,
					hidden : true,
					id : "updel"
						// iconCls : "upload-icon-del"
					}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.6,
				layout : "form",
				defaults : {
					readOnly : false
				},
				labelWidth : 80,
				items : [{
							xtype : "textfield",
							fieldLabel : "Art No.",
							anchor : "100%",
							id : "eleId",
							tabIndex : 41,
							readOnly : true,
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
									readOnly : true,
									name : "eleUnit"
								}, sourceBox, {
									xtype : "numberfield",
									fieldLabel : "Weight(g)",
									anchor : "100%",
									readOnly : true,
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
									readOnly : true,
									tabIndex : 47,
									id : "eleGrade",
									name : "eleGrade"
								}, {
									xtype : "textfield",
									fieldLabel : "MOQ",
									readOnly : true,
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
													readOnly : true,
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
													readOnly : true,
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
			readOnly : true,
			id : "eleNameEn",
			name : "eleNameEn"
		}, {
			xtype : "textarea",
			fieldLabel : "Material",
			anchor : "95%",
			readOnly : true,
			height : 35,
			tabIndex : 54,
			id : "eleName",
			name : "eleName"
		}, {
			xtype : "textarea",
			fieldLabel : "Design",
			anchor : "95%",
			readOnly : true,
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
			readOnly : true,
			id : "eleRemark",
			name : "eleRemark"

		}, {
			xtype : "textarea",
			fieldLabel : "Size Description",
			anchor : "95%",
			height : 35,
			tabIndex : 57,
			readOnly : true,
			id : "eleInchDesc",
			name : "eleInchDesc"
		}, {
			xtype : "textarea",
			fieldLabel : "Sales Unit",
			anchor : "95%",
			height : 35,
			tabIndex : 58,
			readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									fieldLabel : "Packing Size",
									anchor : "100%",
									tabIndex : 66,
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
			hidden : true,
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
									readOnly : true,
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
			hidden : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
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
									readOnly : true,
									id : "box40hqCount",
									name : "box40hqCount"
								}]
					}]
		},{
			xtype:"hidden",
			name:"nationId"
		}]
	});

	// 右边折叠面板
	var rightPanel = new Ext.Panel({
		title : 'Details',
		layout : 'fit',
		frame : true,
		region : 'east',
		width : "28%",
		collapsible : true,
		collapsed : true,
		listeners : {
			'beforeadd' : function(pnl, component, index) {
				pnl.getEl().mask("Loading, please wait...", 'x-mask-loading');
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

							// 中英文规格的转换
							if (txt.getName() == 'boxL'
									|| txt.getName() == 'boxW'
									|| txt.getName() == 'boxH') {
								changeSizeTxt();
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
		if ((eleId == null || eleId == "") && $('picPath') != null) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
			return;
		}
		DWREngine.setAsync(false);
		cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
			if (res != null) {
				if (res.boxCbm != null) {
					res.boxCbm = res.boxCbm.toFixed(cbmNum);
				}
				rightForm.getForm().setValues(res);
				if (popdom == true) {
					res.picPath = "common/images/noElePicSel.png";
				} else {
					if (!flag) {
						if (type == 'order') {
							res.picPath = "./showPicture.action?flag=order&detailId="
									+ res.orderDetailId;
						}
					} else {
						var rdm = Math.round(Math.random() * 10000);
						res.picPath = "./showPicture.action?flag=orderfac&detailId="
								+ record.id + "&tmp=" + rdm;
					}
				}
				DWRUtil.setValue("picPath", res.picPath);

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
			}
		});
		DWREngine.setAsync(true);
	}

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

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	grid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后
	grid.on("afteredit", function(e) {
				DWREngine.setAsync(false);
				// setEleFrameValue(e.field, e.value);
				if (e.field == 'boxCbm') {
					changeCbm(e.record.data.rdm, e.value)
				}
				if (e.field == 'priceFac') {
					priceFacChange(e.value, 1);
				}
				if (e.field == 'boxCount') {
					boxCountChange(e.value);
				}
				if (e.field == 'containerCount') {
					containerCountChange(e.value);
				}
				if (e.field == 'boxObCount') {
					boxOcountNum(e.record.data.rdm, e.value);
				}
				if (e.field == 'boxObL') {
					var boxCbm = Number(Number(e.value)
							.mul(e.record.data.boxObW))
							.mul(e.record.data.boxObH);
					boxCbm = (boxCbm * 0.000001).toFixed(cbmNum);
					e.record.set("boxCbm", boxCbm);
					changeCbm(e.record.data.rdm, boxCbm);
				}
				if (e.field == 'boxObW') {
					var boxCbm = Number(Number(e.value)
							.mul(e.record.data.boxObL))
							.mul(e.record.data.boxObH);
					boxCbm = (boxCbm * 0.000001).toFixed(cbmNum);
					e.record.set("boxCbm", boxCbm);
					changeCbm(e.record.data.rdm, boxCbm);
				}
				if (e.field == 'boxObH') {
					var boxCbm = Number(Number(e.value)
							.mul(e.record.data.boxObW))
							.mul(e.record.data.boxObL);
					boxCbm = (boxCbm * 0.000001).toFixed(cbmNum);
					e.record.set("boxCbm", boxCbm);
					changeCbm(e.record.data.rdm, boxCbm);
				}
				cotOrderFacService.updateOrderFacMapValueByRdm(
						e.record.data.rdm, e.field, e.value, function(res) {
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
						var totalAmmount = Number(newVal).mul(boxCount);
						$('totalLab').innerText = (parseFloat($('totalLab').innerText)
								- rec.data.totalFac + totalAmmount)
								.toFixed("2");
						$('realLab').innerText = (parseFloat($('realLab').innerText)
								- rec.data.totalFac + totalAmmount)
								.toFixed("2");
						// 计算金额
						rec.set("totalFac", totalAmmount);
						updateMapValue(rdm, 'totalFac', totalAmmount);
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
					if (boxObCount != null && boxObCount != ''
							&& boxObCount != 0) {
						var priceFac = rec.data.priceFac;

						// 计算箱数
						if (newVal % boxObCount != 0) {
							containerCount = parseInt(newVal / boxObCount) + 1;
						} else {
							containerCount = newVal / boxObCount;
						}
						rec.set("containerCount", containerCount);
						var totalAmmount = Number(newVal).mul(priceFac);
						$('totalLab').innerText = (parseFloat($('totalLab').innerText)
								- rec.data.totalFac + totalAmmount)
								.toFixed("2");
						$('realLab').innerText = (parseFloat($('realLab').innerText)
								- rec.data.totalFac + totalAmmount)
								.toFixed("2");

						// 计算金额
						rec.set("totalFac", totalAmmount);

						updateMapValue(rdm, 'containerCount', containerCount);
						updateMapValue(rdm, 'totalFac', totalAmmount);
					}
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
					boxCount = Number(newVal).mul(boxObCount);
					rec.set("boxCount", boxCount);
					var totalAmmount = Number(boxCount).mul(priceFac);
					$('totalLab').innerText = (parseFloat($('totalLab').innerText)
							- rec.data.totalFac + totalAmmount).toFixed("2");
					$('realLab').innerText = (parseFloat($('realLab').innerText)
							- rec.data.totalFac + totalAmmount).toFixed("2");
					// 计算金额
					rec.set("totalFac", totalAmmount);
					updateMapValue(rdm, 'boxCount', boxCount);
					updateMapValue(rdm, 'totalFac', totalAmmount);
				}
			});
			DWREngine.setAsync(true);
		}
	}

	// 外箱数值改变事件
	function boxOcountNum(rdm, boxObCount) {
		var rec = editRec;
		if (boxObCount == '' || isNaN(boxObCount)) {
			boxObCount = 0;
		}
		boxObCount = parseInt(boxObCount);
		cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
			if (res != null) {
				var containerCount = rec.data.containerCount;
				var priceFac = rec.data.priceFac;
				var boxCount = Number(containerCount).mul(boxObCount);
				var totalMoney = Number(priceFac).mul(boxCount);
				res.boxCount = boxCount;
				res.totalMoney = totalMoney;
				rec.set("boxCount", boxCount);
				// 重新计算总价
				$('totalLab').innerText = (parseFloat($('totalLab').innerText)
						- rec.data.totalFac + totalMoney).toFixed("2");

				$('realLab').innerText = (parseFloat($('realLab').innerText)
						- rec.data.totalFac + totalMoney).toFixed("2");
				rec.set("totalFac", totalMoney);

				var con20 = $('con20').value;
				var con40H = $('con40H').value;
				var con40 = $('con40').value;
				var con45 = $('con45').value;
				// CBM
				var boxCbm = res.boxCbm;
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
				// calWeighByEleFrame($('boxWeigth').value);
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
				$('boxCbm').value = (L * W * H * 0.000001).toFixed(cbmNum);
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
											Ext.Msg
													.alert('Message',
															'Please select Order Details!');
										} else {
											rightMenu.hide();
											downPics();
										}
									}
								}]
							}
						}, {
							text : "Synchronized to the order",
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
			Ext.MessageBox
					.alert('Message', "Sorry, you do not have Authority!");
			return;
		}
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
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
			Ext.MessageBox
					.alert('Message',
							'Please select the details to be updated to record the order!');
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
				Ext.MessageBox.confirm('Message', 'Existing Order Details'
								+ sameTemp
								+ "covering the original order details?",
						function(btn) {
							if (btn == 'yes') {
								cotOrderFacService.updateToOrderDetail(same,
										sameVal, dis, disVal, eleStr, boxStr,
										otherStr, isPic, function(res) {
											Ext.MessageBox
													.alert('Message',
															'Synchronization successful!');
										});
							} else {
								if (dis.length != 0) {
									cotOrderFacService.updateToOrderDetail(
											null, null, dis, disVal, eleStr,
											boxStr, otherStr, isPic, function(
													res) {
												Ext.MessageBox
														.alert('Message',
																'Synchronization successful!');
											});
								}
							}
						});
			} else {
				if (dis.length != 0) {
					cotOrderFacService.updateToOrderDetail(null, null, dis,
							disVal, eleStr, boxStr, otherStr, isPic, function(
									res) {
								Ext.MessageBox.alert('Message',
										'Synchronization successful!');
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
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		}
		var hispriceGrid = new HisPriceGrid({
					facId : $('factoryId').value,
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
	// 下载多张图片.并且压缩.zip
	function downPics() {
		var temp = "";
		var arr = sm.getSelections();
		if (arr.length == 0) {
			alert('Please check the detail records!');
			return;
		}
		Ext.each(arr, function(record) {
					temp += record.get("rdm") + ",";
				})

		var orderNo = $('orderNo').value;
		var type = $('picType').value;
		var url = "./downPics.action?priceNo=" + encodeURIComponent(orderNo)
				+ "&tp=orderfac&rdms=" + temp + "&type=" + type;
		downRpt(url);
	}

	// 判断单号是否重复参数
	var isExist = true;
	var orderfacForm = new Ext.form.FormPanel({
		title : "Base Infomation-(Red is required)",
		labelWidth : 95,
		formId : "orderfacForm",
		collapsible : true,
		labelAlign : "right",
		region : 'north',
		layout : "form",
		height : 200,
		frame : true,
		listeners : {
			'collapse' : function(pnl) {
				Ext.Element.fly("talPriceDiv").setLeftTop(420, 35);
			},
			'expand' : function(pnl) {
				Ext.Element.fly("talPriceDiv").setLeftTop(420, 203);
			}
		},
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.20,
						layout : "form",
						labelWidth : 100,
						items : [facBox, {
									xtype : "datefield",
									// labelWidth : 100,
									fieldLabel : "Purchase Date",
									anchor : "100%",
									id : "orderTime",
									name : "orderTime",
									format : "Y-m-d",
									allowBlank : true,
									tabIndex : 3
								}, shipPortBox, {
									xtype : "datefield",
									fieldLabel : "Date",
									anchor : "100%",
									format : "Y-m-d H:i:s",
									id : "addTime",
									name : "addTime",
									value : new Date(),
									tabIndex : 21,
									disabled : true,
									disabledClass : 'combo-disabled'
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.20,
						layout : "form",
						labelWidth : 120,
						items : [facContactBox, {
									xtype : "datefield",
									fieldLabel : "Delivery Date",
									anchor : "100%",
									format : "Y-m-d",
									id : "sendTime",
									name : "sendTime",
									readOnly : true,
									allowBlank : true,
									tabIndex : 4
								}, targetPortBox, addEmpBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.20,
						layout : "form",
						items : [empsBox, {
									xtype : "datefield",
									fieldLabel : "Shipment",
									anchor : "100%",
									id : "shipmentDate",
									name : "shipmentDate",
									format : "Y-m-d",
									allowBlank : true,
									readOnly : true,
									tabIndex : 7
								}, payTypeBox, {
									xtype : "datefield",
									fieldLabel : "Revise Date",
									anchor : "100%",
									format : "Y-m-d H:i:s",
									id : "modTime",
									name : "modTime",
									tabIndex : 22,
									disabled : true,
									disabledClass : 'combo-disabled'
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.20,
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
									fieldLabel : "<font color='red'>W&C P/O</font>",
									anchor : "100%",
									id : "orderNo",
									name : "orderNo",
									maxLength : 100,
									readOnly : true,
									allowBlank : false,
									blankText : "W&C P/O is required！",
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
									iconCls : "cal",
									handler : getOrderFacNo,
									listeners : {
										"render" : function(obj) {
											var tip = new Ext.ToolTip({
												target : obj.getEl(),
												anchor : 'top',
												maxWidth : 160,
												minWidth : 160,
												html : 'Automatic generation of production of the contract number!'
											});
										}
									}
								}]
							}]
						}, {
							xtype : "textfield",
							fieldLabel : "ClientP/O",
							anchor : "100%",
							id : "poNo",
							name : "poNo",
							maxLength : 100,
							allowBlank : true,
							readOnly : true,
							tabIndex : 8
						}, clauseBox, modEmpBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.20,
						layout : "form",
						items : [companyBox, containerBox, {
									xtype : "textfield",
									fieldLabel : "<font color=red>Product</font>",
									anchor : "100%",
									id : "allPinName",
									name : "allPinName",
									allowBlank : false,
									blankText : "Please enter Product!",
									maxLength : 200,
									allowBlank : true,
									readOnly : true,
									tabIndex : 8
								}, empChkBox]

					}]
		}, {
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 100,
						items : [curBox]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 120,
						items : [{
									xtype : "hidden",
									fieldLabel : "Disscount(%)",
									anchor : "100%",
									maxValue : 99.99,
									decimalPrecision : 2,
									id : "zheNum",
									name : "zheNum",
									allowBlank : true,
									tabIndex : 4
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 120,
						items : [{
									xtype : "numberfield",
									fieldLabel : "TotalCBM",
									anchor : "100%",
									readOnly : true,
									id : "orderEarnest"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.2,
						layout : "form",
						items : [{
									xtype : "datefield",
									fieldLabel : "ETA",
									anchor : "100%",
									format : "Y-m-d",
									id : "orderLcDelay",
									name : "orderLcDelay"
								},{
									xtype : "datefield",
									fieldLabel : "Design",
									hidden:true,
									hideLabel:true,
									anchor : "100%",
									format : "Y-m-d",
									id : "designTime",
									name : "designTime"
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.2,
						layout : "form",
						items : [orderStatusBox]
					},{
						xtype : "panel",
						title : "",
						columnWidth : 0.2,
						layout : "form",
						items : [{xtype : "textfield",
								fieldLabel : "Remark",
								readOnly:true,
								anchor : "100%",
								id : "newRemark",
								name : "newRemark",
								tabIndex : 20
						}]
					}, {
						xtype : "panel",
						title : "",
						hidden : true,
						columnWidth : 0.2,
						id : 'commisionScalePanel',
						layout : "form",
						items : [{
									xtype : "hidden",
									maxValue : 999.99,
									fieldLabel : 'Commision(%)',
									anchor : "100%",
									id : "commisionScale",
									name : "commisionScale"
								}]

					}]
		}, {
			xtype : "panel",
			layout : "column",
			labelWidth : 140,
			items : [{
						xtype : "panel",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 100,
						items : [typeLv1IdBox]
					}, {
						xtype : "panel",
						columnWidth : 0.2,
						layout : "form",
						items : [{
									xtype : "datefield",
									fieldLabel : "Sent Client Approval",
									anchor : "100%",
									id : "oderFacText",
									format : "Y-m-d",
									name : "oderFacText",
									allowBlank : true
								}]
					}, {
						xtype : "panel",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 95,
						items : [themesBox]

					}, {
						xtype : "panel",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 95,
						items : [{
									xtype : "textarea",
									fieldLabel : "Themes Name",
									anchor : "100%",
									height : 25,
									id : "themeStr",
									name : "themeStr",
									maxLength : 1000,
									allowBlank : true,
									tabIndex : 19
								}]
					}, {
						xtype : "panel",
						columnWidth : 0.2,
						layout : "form",
						labelWidth : 95,
						items : [{
									xtype : "textarea",
									fieldLabel : "Comments",
									anchor : "100%",
									height : 25,
									id : "orderRemark",
									name : "orderRemark",
									// readOnly:true,
									maxLength : 1000,
									allowBlank : true,
									tabIndex : 20
								}]
					}]
		}, {
			xtype : 'hidden',
			id : "preNum",
			name : "preNum"
		}, {
			xtype : 'hidden',
			id : "preText",
			name : "preText"
		}, {
			xtype : 'hidden',
			id : "artNum",
			name : "artNum"
		}, {
			xtype : 'hidden',
			id : "artText",
			name : "artText"
		}, {
			xtype : 'hidden',
			id : "samNum",
			name : "samNum"
		}, {
			xtype : 'hidden',
			id : "samText",
			name : "samText"
		}, {
			xtype : 'hidden',
			id : "qcNum",
			name : "qcNum"
		}, {
			xtype : 'hidden',
			id : "qcText",
			name : "qcText"
		}, {
			xtype : 'hidden',
			id : "shipNum",
			name : "shipNum"
		}, {
			xtype : 'hidden',
			id : "shipText",
			name : "shipText"
		}, {
			xtype : 'hidden',
			id : "preMan",
			name : "preMan"
		}, {
			xtype : 'hidden',
			id : "artMan",
			name : "artMan"
		}, {
			xtype : 'hidden',
			id : "samMan",
			name : "samMan"
		}, {
			xtype : 'hidden',
			id : "qcMan",
			name : "qcMan"
		}, {
			xtype : 'hidden',
			id : "shipMan",
			name : "shipMan"
		},{
			xtype : 'hidden',
			id : "nationId",
			name : "nationId"
		}]
	})

	// 第一页
	var centerPanel = new Ext.Panel({
				layout : 'border',
				title : "Detail",
				items : [grid, rightPanel]
			});

	// 产品标图片
	var productPicPanel = new Ext.Panel({
		layout : 'anchor',
		region : 'west',
		width : 300,
		border : false,
		items : [{
			xtype : "panel",
			// bodyStyle : 'padding-left:25px',
			title : "Product standard picture",
			anchor : "100% 50%",
			layout : "hbox",
			labelWidth : 60,
			frame : true,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			// buttons : [{
			// text : '',
			// hidden : true
			// }],
			items : [{
				xtype : "panel",
				width : 160,
				bodyStyle : 'padding-left:20px',
				buttonAlign : "center",
				html : '<div align="center" style="width: 120px; height: 120px;">'
						+ '<img src="common/images/zwtp.png" id="product_MB" name="product_MB"'
						+ 'onload="javascript:DrawImage(this,120,120)" onclick="showBigPicDiv(this)"/></div>',
				buttons : [{
							width : 60,
							text : "Update",
							id : "upmodProMai",
							iconCls : "upload-icon",
							handler : showUploadProductPanel
						}, {
							width : 60,
							text : "Delete",
							id : "updelProMai",
							iconCls : "upload-icon-del",
							handler : delProductMBPic
						}],
				listeners : {
					'afterrender' : function(pnl) {
						var id = $('pId').value;
						if (id != 'null' && id != '') {
							$('product_MB').src = "./showPicture.action?flag=productFacMB&detailId="
									+ id + "&temp=" + Math.random();
						} else {
							Ext.getCmp('upmodProMai').hide();
							Ext.getCmp('updelProMai').hide();
						}
					}
				}
			}]
		}, {
			xtype : "panel",
			// bodyStyle : 'padding-left:25px',
			title : "Mark standard picture",
			layout : "hbox",
			anchor : "100% 50%",
			labelWidth : 60,
			frame : true,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			// buttons : [{
			// text : '',
			// hidden : true
			// }],
			items : [{
				xtype : "panel",
				width : 160,
				bodyStyle : 'padding-left:20px',
				buttonAlign : "center",
				html : '<div align="center" style="width: 120px; height: 120px;">'
						+ '<img src="common/images/zwtp.png" id="order_MB" name="order_MB"'
						+ 'onload="javascript:DrawImage(this,120,120)" onclick="showBigPicDiv(this)"/></div>',
				buttons : [{
							width : 60,
							text : "Update",
							id : "upmodMai",
							iconCls : "upload-icon",
							handler : showUploadMaiPanel
						}, {
							width : 60,
							text : "Delete",
							id : "updelMai",
							iconCls : "upload-icon-del",
							handler : delMBPic
						}]
			}]
		}]
	});

	// 产品标
	var maiPicPanel = new Ext.Panel({
				layout : 'fit',
				region : 'west',
				width : 300,
				border : false,
				title : "Product Mark",
				items : [{
							xtype : 'textarea',
							id : "productArea",
							name : "productArea",
							maxLength : 500,
							listeners : {
								'afterrender' : function(area) {
									area.setValue(productArea);
								}
							}
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
								title : "Positive Mark",
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
								title : "Side Mark",
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
								title : "Mark in the box",
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
								title : "Mark the box",
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
							text : "Mark header information from the order ",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : showOrderMb
						}, '-']
			});

	// 唛明细
	var tbPanel = new Ext.Panel({
				layout : 'border',
				border : false,
				region : 'center',
				items : [maiPicPanel, maiInfoPanel]
			});

	var maiPanel = new Ext.Panel({
				layout : 'border',
				border : false,
				tbar : tbMai,
				items : [productPicPanel, tbPanel]
			});

	var additional = new Ext.form.FormPanel({
		title : "",
		labelWidth : 100,
		labelAlign : "right",
		layout : "form",
		padding : "5px",
		autoScroll : true,
		frame : true,
		items : [{
			xtype : "fieldset",
			title : "Additional Information",
			layout : "column",
			anchor : '98%',
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
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.quality);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.quality = '';
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
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.colours);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.colours = '';
							}
						}
					}
				}]
			}]
		}, {
			xtype : "fieldset",
			title : "Packing",
			layout : "column",
			anchor : '98%',
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
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.saleUnit);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.saleUnit = '';
							}
						}
					}
				}, {
					xtype : "textarea",
					fieldLabel : "Packing Assortment",
					anchor : "100%",
					id : 'assortment',
					name : 'assortment',
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.assortment);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.assortment = '';
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
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.handleUnit);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.handleUnit = '';
							}
						}
					}
				}, {
					xtype : "textarea",
					fieldLabel : "Comments",
					anchor : "100%",
					id : 'comments',
					name : 'comments',
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.comments);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.comments = '';
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
					fieldLabel : "Shipping Mark",
					anchor : "100%",
					id : 'shippingMark',
					name : 'shippingMark',
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.shippingMark);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.shippingMark = '';
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
					fieldLabel : "Consignee",
					anchor : "100%",
					id : 'consignee',
					name : 'consignee',
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.consignee);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.consignee = '';
							}
						}
					}
				}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				items : [companyCodeBox]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.5,
				layout : "form",
				items : [{
					xtype : "textarea",
					fieldLabel : "Booking",
					anchor : "100%",
					id : 'booking',
					name : 'booking',
					// maxLength : 500,
					height : 75,
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.booking);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.booking = '';
							}
						}
					}
				}]
			}]
		}, {
			xtype : "fieldset",
			title : "Buyer/Seller/Agent",
			layout : "column",
			anchor : '98%',
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
						disabled : true,
						id : 'isCheckAgent',
						listeners : {
							'afterrender' : function() {
								var checked = txtAreaAry.isCheckAgent;
								Ext.getCmp('isCheckAgent').setValue(checked);
								if (checked == '1') {
									Ext.getCmp('commisionScalePanel')
											.setVisible(true);
								} else {
									Ext.getCmp('commisionScalePanel')
											.setVisible(false);
								}
							},
							'check' : function(check, bo) {
								// 验证表单
								var formData = getFormValues(orderfacForm, true);
								// 表单验证失败时,返回
								if (!formData) {
									check.setValue(0);
									return;
								}
								if (bo == true) {
									txtAreaAry.isCheckAgent = '1';
									Ext.getCmp('commisionScalePanel')
											.setVisible(true);
									var facId = facBox.getValue();
									if (Ext.isEmpty(facId)) {
										Ext.MessageBox.alert("Message",
												"Please select Supplier first");
										return;
									} else {

										cotFactoryService.getFactoryById(facId,
												function(res) {
													if (res != null) {
														// TODO:
														var nation = '';
														var fullName = '';
														var city = '';
														var adr = '';
														var post = '';
														if (!Ext
																.isEmpty(res.post)) {
															post = res.post
																	+ ','
														}
														if (!Ext
																.isEmpty(res.factoryName)) {
															fullName = res.factoryName
																	+ ' \n'
														}
														if (!Ext
																.isEmpty(res.factoryAddr)) {
															adr = res.factoryAddr
																	+ ' \n'
														}
														if (!Ext
																.isEmpty(cityMap[res.cityId])) {
															city = cityMap[res.cityId]
																	+ ' \n'
														} else {
															if (!Ext
																	.isEmpty(res.post)) {
																post = res.post
																		+ ' \n'
															}
														}
														if (!Ext
																.isEmpty(nationMap[res.nationId])) {
															nation = nationMap[res.nationId];
														}
														var str = fullName
																+ adr + post
																+ city + nation;
														var start = str
																.indexOf(',');
														var end = str
																.lastIndexOf(',');
														var strLength = str.length
																- 2;
														var oneLength = str.length
																- 1;
														var lastStr = str
																.substring(
																		strLength,
																		strLength
																				+ 1);
														var oneStr = str
																.substring(
																		oneLength,
																		oneLength
																				+ 1);
														if (start == end) {
															if (start == strLength) {
																str = str
																		.substring(
																				0,
																				strLength);
															}
														} else {
															if (lastStr == ',') {
																str = str
																		.substring(
																				0,
																				end);
															}
															if (oneStr == ',') {
																str = str
																		.substring(
																				0,
																				end);
															}
														}
														Ext.getCmp('seller')
																.setValue(str);
														txtAreaAry.seller = str;
													}
												});
										
										queryService.findCompanyStr($('companyId').value,function(enName){
											Ext.getCmp('agent')
													.setValue(enName);
											txtAreaAry.agent = enName;
										})
										
										var orderId = $('orderId').value;
										if (!Ext.isEmpty(orderId)) {
											cotOrderFacService
													.getCustByOrderId(orderId,
															function(res) {
																if (res != null) {
																	var city = '';
																	var nation = '';
																	var fullName = '';
																	var adr = '';
																	var post = '';
																	if (!Ext
																			.isEmpty(res.customerPost)) {
																		post = res.customerPost
																				+ ','
																	}
																	if (!Ext
																			.isEmpty(res.fullNameEn)) {
																		fullName = res.fullNameEn
																				+ ' \n'
																	}
																	if (!Ext
																			.isEmpty(res.customerAddrEn)) {
																		adr = res.customerAddrEn
																				+ ' \n'
																	}
																	if (!Ext
																			.isEmpty(cityMap[res.cityId])) {
																		city = cityMap[res.cityId]
																				+ ' \n'
																	} else {
																		if (!Ext
																				.isEmpty(res.customerPost)) {
																			post = res.customerPost
																					+ ' \n'
																		}
																	}
																	if (!Ext
																			.isEmpty(nationMap[res.nationId])) {
																		nation = nationMap[res.nationId];
																	}
																	var str = fullName
																			+ adr
																			+ post
																			+ city
																			+ nation;
																	var start = str
																			.indexOf(' ');
																	var end = str
																			.lastIndexOf(' ');
																	var strLength = str.length
																			- 2;
																	var oneLength = str.length
																			- 1;
																	var lastStr = str
																			.substring(
																					strLength,
																					strLength
																							+ 1);
																	var oneStr = str
																			.substring(
																					oneLength,
																					oneLength
																							+ 1);
																	if (start == end) {
																		if (start == strLength) {
																			str = str
																					.substring(
																							0,
																							strLength);
																		}
																	} else {
																		if (lastStr == ' ') {
																			str = str
																					.substring(
																							0,
																							end);
																		}
																		if (oneStr == ' ') {
																			str = str
																					.substring(
																							0,
																							end);
																		}
																	}
																	Ext
																			.getCmp('buyer')
																			.setValue(str);
																	txtAreaAry.buyer = str;
																}
															});

										}
									}

								} else {
									txtAreaAry.isCheckAgent = '0';
									Ext.getCmp('commisionScalePanel')
											.setVisible(false);
									txtAreaAry.seller = "    ";
									// 供应商
									var facId = facBox.getValue();
									if (Ext.isEmpty(facId)) {
										Ext.MessageBox.alert("Message",
												"Please select Supplier first");
										return;
									} else {
										Ext.getCmp('agent').setValue('      ');
										
										queryService.findCompanyStr($('companyId').value,function(enName){
											$(buyer).value = enName;
											txtAreaAry.buyer = enName;
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
						maxLength : 500,
						height : 75,
						anchor : "100%",
						listeners : {
							'afterrender' : function(area) {
								if ($('pId').value != ''
										&& $('pId').value != 'null') {
									area.setValue(txtAreaAry.agent);
								}
							},
							'change' : function(area, newVal, oldVal) {
								if (newVal == '') {
									txtAreaAry.agent = '';
								}
							}
						}
					}]
				}]
			}, {
				xtype : "panel",
				title : "",
				columnWidth : 0.33,
				layout : "form",
				items : [{
					xtype : "textarea",
					fieldLabel : "Buyer",
					id : 'buyer',
					name : 'buyer',
					maxLength : 500,
					height : 75,
					anchor : "100%",
					listeners : {
						'afterrender' : function(area) {
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.buyer);
							} else {
								queryService.findCompanyStr($('companyId').value,function(enName){
											area.setValue(enName);
										})
								
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.buyer = '';
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
							if ($('pId').value != ''
									&& $('pId').value != 'null') {
								area.setValue(txtAreaAry.seller);
							}
						},
						'change' : function(area, newVal, oldVal) {
							if (newVal == '') {
								txtAreaAry.seller = '';
							}
						}
					}
				}]
			}]
		}]
	});

	// 加载图片记录
	function loadOrderPhone() {
		var id = $("pId").value;
		if (id == '' || id == 'null') {
			Ext.MessageBox.alert('Message', 'Please save po first!');
		} else {
			var frame = window.frames["orderPhone"];
			frame.location.href = "cotorderfac.do?method=queryArtWork&orderId="
					+ id;
		}
	}

	// 底部标签页
	var tbl = new Ext.TabPanel({
				region : 'south',
				region : 'center',
				width : "100%",
				activeTab : 0,
				items : [centerPanel,
						// {
						// id : "shenTab",
						// name : 'shenTab',
						// title : "审核记录",
						// layout : 'fit',
						// hidden:true,
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
						// },
						// {
						// id : "contractTab",
						// name : "contractTab",
						// title : "合同条款",
						// hidden:true,
						// layout : 'fit',
						// items : [{
						// xtype : 'textarea',// html文本编辑器控件
						// enableLinks : false,
						// id : 'orderClause',
						// name : 'orderClause',
						// listeners : {
						// 'afterrender' : function(area) {
						// if ($('pId').value == '' || $('pId').value == 'null')
						// {
						// // 设置合同条款
						// sysdicutil.getCotContractList(2, function(res) {
						// if (res != null) {
						// var con = '';
						// for (var i = 0; i < res.length; i++) {
						// con += res[i].contractContent
						// + "；\n";
						// }
						// area.setValue(con);
						// }
						// });
						// } else {
						// area.setValue(conArea);
						// }
						// }
						// }
						// }]
						// },
						// {
						// id : "maiTab",
						// name : "maiTab",
						// title : "唛11明细",
						// hidden:true,
						// layout : 'fit',
						// items : [maiPanel]
						// },
						// {
						// xtype : 'iframepanel',
						// title : "其他费用",
						// hidden:true,
						// itemId : 'otherFeeInfoRec',
						// frameConfig : {
						// autoCreate : {
						// id : 'otherFeeInfo'
						// }
						// },
						// loadMask : {
						// msg : 'Loading...'
						// },
						// listeners : {
						// "activate" : function(panel) {
						// loadOtherFeeInfo();
						// }
						// }
						// },
						{
					id : "additionalTab",
					name : "additionalTab",
					title : "Additional Information",
					layout : 'fit',
					items : [additional]
				}, {
					id : "timeFromTab",
					name : "timeFromTab",
					title : "Project",
					layout : 'fit',
					items : [timeForm]
				}, {
					xtype : 'iframepanel',
					title : "Art Work",
					id : 'orderPhoneTab',
					frameConfig : {
						autoCreate : {
							id : 'orderPhone'
						}
					},
					loadMask : {
						msg : 'Loading...'
					},
					listeners : {
						"activate" : function(panel) {
							loadOrderPhone();
						}
					}
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
							text : "Rearrange the order ",
							hidden : true,
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
							text : "Approval",
							hidden : true,
							id : 'qingBtn',
							handler : showShenPnl,
							iconCls : "page_from"
						}, {
							text : "Approved",
							id : 'guoBtn',
							handler : checkShen,
							hidden : true,
							iconCls : "page_check"
						}, {
							text : "No Pass",
							id : 'buBtn',
							hidden : true,
							handler : buShen,
							iconCls : "page_check_error"
						}, {
							text : "Mail",
							hidden : true,
							id : 'showPdfBtn',
							iconCls : "page_excel",
							handler : showPdf
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
		if (pnl.name == 'maiTab') {
			// 加载麦标
			var id = $('pId').value;
			if (id != 'null' && id != '') {
				$('order_MB').src = "./showPicture.action?flag=orderFacMB&detailId="
						+ id + "&temp=" + Math.random();
				Ext.getCmp('upmodMai').show();
				Ext.getCmp('updelMai').show();
			} else {
				Ext.getCmp('upmodMai').hide();
				Ext.getCmp('updelMai').hide();
			}
		}
	});

	// 加载表格数据
	ds.load({
				params : {
					start : 0,
					limit : 1000
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
	// 产品标
	var productArea = "";
	// 存储主单的10个字段
	var txtAreaAry = {};
	// 初始化
	function initform() {
		$('talPriceDiv').style.display = 'block';
		DWREngine.setAsync(false);
		// 清空Map
		cotOrderFacService.clearOrderFacMap(function(res) {
				});
		// 加载集装箱的柜体积
		queryService.getContainerCube(function(res) {
					$('con20').value = res[0];
					$('con40').value = res[1];
					$('con40H').value = res[2];
					$('con45').value = res[3];
				});
		// 送样单编号
		var id = $('pId').value;
		if (id != 'null' && id != '') {
			// 加载采购单信息
			cotOrderFacService.getOrderFacById(parseInt(id), function(res) {
				DWRUtil.setValues(res);
				chkReason = res.checkReason;
				// 初始配置值
				cotGivenService.getList('CotPriceCfg', function(cfg) {
							if (cfg.length != 0) {
								orderStatusBox.setValue(res.orderStatus);
							}
						});

				// 加载下单时间
				Ext.getCmp("orderTime").setValue(res.orderTime);
				// 加载发货时间
				Ext.getCmp("sendTime").setValue(res.sendTime);
				// 加载船期
				Ext.getCmp("shipmentDate").setValue(res.shipmentDate);
				Ext.getCmp("oderFacText").setValue(res.oderFacText);
				// 加载创建日期
				Ext.getCmp("addTime").setValue(res.addTime);
				// 加载修改日期
				Ext.getCmp("modTime").setValue(res.modTime);
				Ext.getCmp("orderLcDelay").setValue(res.orderLcDelay);
				Ext.getCmp("designTime").setValue(res.designTime);

				// 加载下拉框
				facBox.bindPageValue("CotFactory", "id", res.factoryId);
				facContactBox.loadValueById("CotContact", "factoryId",
						res.factoryId, res.facContactId);
				$('isCheckId').value = res.isCheckAgent;
				empsBox.bindPageValue("CotEmps", "id", res.businessPerson);
				companyBox.bindPageValue("CotCompany", "id", res.companyId);
				containerBox.bindValue(res.containerTypeId);
				shipPortBox.bindPageValue("CotShipPort", "id", res.shipportId);
				targetPortBox.bindPageValue("CotTargetPort", "id",
						res.targetportId);
				payTypeBox.bindValue(res.payTypeId);
				clauseBox.bindValue(res.clauseTypeId);
				addEmpBox.bindPageValue("CotEmps", "id", res.addPerson);
				modEmpBox.bindPageValue("CotEmps", "id", res.modPerson);
				empChkBox.bindPageValue("CotEmps", "id", res.checkPerson);
				curBox.bindValue(res.currencyId);
				// department
				typeLv1IdBox.bindPageValue("CotTypeLv1", "id", res.typeLv1Id);

				themesBox.setValue(res.themes);

				setProject(res);
				// 加载货款金额
				if (res.totalMoney != null) {
					$('totalLab').innerText = res.totalMoney.toFixed("2");
				} else {
					$('totalLab').innerText = 0;
				}

				// 存储主单的10个字段
				txtAreaAry.quality = res.quality;
				txtAreaAry.colours = res.colours;
				txtAreaAry.saleUnit = res.saleUnit;
				txtAreaAry.handleUnit = res.handleUnit;
				txtAreaAry.assortment = res.assortment;
				txtAreaAry.comments = res.comments;
				txtAreaAry.shippingMark = res.shippingMark;
				txtAreaAry.buyer = res.buyer;
				txtAreaAry.seller = res.seller;
				txtAreaAry.agent = res.agent;
				txtAreaAry.forwardingAgent = res.forwardingAgent;
				txtAreaAry.consignee = res.consignee;
				txtAreaAry.booking = res.booking;
				txtAreaAry.isCheckAgent = res.isCheckAgent;
				// txtAreaAry.forwardingAgent=res.forwardingAgent;
				// txtAreaAry.booking=res.booking;

				// 如果是请求审核状态时,登录人是admin或者是该单的审核人时才显示审核通过按钮
				if (res.orderStatus == 3) {
					if (loginEmpId == "admin" || logId == res.checkPerson) {
						Ext.getCmp("guoBtn").show();
					}
				}
				// 如果是未审核或者审核通过时,才显示请求审核按钮
				if ((res.orderStatus == 0 || res.orderStatus == 2)) {
					Ext.getCmp("qingBtn").show();
				}
				// 如果是审核通过时,并且登录人是审核人或者登录人是admin,才显示发送邮件按钮
				if ((res.orderStatus == 2)
						&& (loginEmpId == "admin" || logId == res.checkPerson)) {
					Ext.getCmp("showPdfBtn").show();
				}

				cotOrderFacService.getCanOutByOrderId(res.orderId,
						function(ck) {
							// 出货填过单号后,隐藏保存按钮
							if (ck == 2) {
								Ext.getCmp('saveBtn').hide();
								Ext.getCmp('qingBtn').hide();
								Ext.getCmp('guoBtn').hide();
								tb.hide();
							}
						})

				if (res.shipmentDate != null && res.shipmentDate != ''
						&& res.sendTime != null && res.sendTime != '') {
					queryService.findDays(res.shipportId, res.targetportId,
							function(alt) {
								var date = Ext.getCmp('shipmentDate')
										.getValue().add(Date.DAY, alt);
								var newDate = new Date(res.sendTime);
								if (newDate.getTime() < date.getTime()) {
									Ext.getCmp('sendTime').el.setStyle('color',
											'red');
								} else {
									var date2 = date.add(Date.DAY, 37);
									if (newDate.getTime() >= date2.getTime()) {
										Ext.getCmp('sendTime').el.setStyle(
												'color', 'red');
									} else {
										Ext.getCmp('sendTime').el.setStyle(
												'color', 'black');
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

	// 添加空白record到表格中
	function addNewGrid() {
	}

	// 删除
	function onDel() {
		var cord = sm.getSelections();
		if (cord.length == 0) {
			Ext.Msg.alert("Message:", "Please select records");
			return;
		}
		Ext.each(cord, function(item) {
					var rdm = item.data.rdm;
					ds.remove(item);
					cotOrderFacService.delOrderFacMapByRdm(rdm, function(res) {
							});
				});
		if ($('picPath')) {
			$('picPath').src = "./showPicture.action?flag=noPic";
			clearFormAndSet(rightForm);
		}
	}

	// 打开上传面板
	function showUploadPanel() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, the order has been reviewed can not be modified! To change pls. review again!');
			return;
		}
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.Msg.alert('Tip Box', 'Please select product！');
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
					waitMsg : "Photo upload......",
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
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, the order has been reviewed can not be modified! To change pls. review again!');
			return;
		}
		var eleId = $('eleId').value;
		if (eleId == '') {
			Ext.MessageBox.alert('Message',
					'Please select a record order details！');
			return;
		}
		Ext.MessageBox.confirm('Message',
				"Are you sure you remove the picture?", function(btn) {
					if (btn == 'yes') {
						var detailId = $('id').value;
						cotOrderFacService.deletePicImg(detailId,
								function(res) {
									if (res) {
										$('picPath').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox.alert('Message',
												'Failed to delete pictures！');
									}
								})
					}
				});
	}

	// 获取单号
	function getOrderFacNo() {
		var currDate = $('orderTime').value;
		var factoryId = $('factoryId').value;
		var empId = $('businessPerson').value
		if (currDate == "" || currDate == null) {
			Ext.Msg.alert("Tip Box", "Select Date");
			return;
		}
		if (factoryId == 'null' || factoryId == "") {
			Ext.Msg.alert("Tip Box", "Please select the vendor");
			return;
		}
		if (empId == null || empId == "") {
			Ext.MessageBox.alert("Message", "Please select a sales");
			return;
		}
		// setNoService.getOrderFacNo(factoryId, currDate, function(res) {
		// $('orderNo').value = res;
		// });
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			cotSeqService.getOrderFacNo(factoryId, empId, currDate, function(
							res) {
						$('orderNo').value = res;
					});
			$('orderNo').focus();
		}
	}

	// 删除
	function del() {
		var id = $('pId').value;
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0)// 没有删除权限
		{
			Ext.MessageBox
					.alert("Tip Box", "Sorry, you do not have Authority!");
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
			Ext.MessageBox
					.alert(
							"Tip Box",
							'Sorry, this one has been reviewed, and if deleted! Please review again or contact the webmaster!');
			return;
		}

		var dealFlag = false;
		cotOrderFacService.getDealNumById(parseInt(id), function(dealNum) {
					if (dealNum != -1) {
						dealFlag = true;
					}
				});

		if (dealFlag) {
			Ext.MessageBox
					.alert("Tip Box",
							'Sorry, the order has been recorded in accounts payable, can not be deleted！');
			return;
		}

		var otherFlag = false;
		cotOrderFacService.getOtherNumById(parseInt(id), function(otherNum) {
					if (otherNum != -1) {
						otherFlag = true;
					}
				});

		if (otherFlag) {
			Ext.MessageBox
					.alert("Tip Box",
							'Sorry, there are other costs into the  order, can not be removed！');
			return;
		}
		var cotOrderFac = new CotOrderFac();
		var list = new Array();
		cotOrderFac.id = id;
		list.push(cotOrderFac);
		Ext.MessageBox.confirm('Message', 'Are you sure to delete?', function(
				btn) {
			if (btn == 'yes') {
				cotOrderFacService.deleteOrderFacs(list, function(res) {
					if (res) {
						Ext.MessageBox.alert("Tip Box", "Deleted successfully");
						closeandreflashEC('true', 'orderfacGrid', false);
					} else {
						Ext.MessageBox
								.alert("Tip Box",
										"Delete failed, the purchase order is already in use！");
					}
				})
			}
		})
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
					"Please save the purchase order, then change the sorting!");
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
		cotOrderFacService.updateSortNo($("pId").value, type, sort.field,
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

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox
					.alert("Message", 'Sorry, you do not have Authority！');
			return;
		}
		// 如果该单没保存不能打印
		if ($('pId').value == 'null' || $('pId').value == '') {
			Ext.MessageBox.alert("Message",
					'The order has not been preserved, can not print！');
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

		// DWREngine.setAsync(false);
		// var modFlag = false;
		var orderfacId = $('pId').value;
		// if (orderfacId != 'null' && orderfacId != '') {
		// cotOrderFacService.getOrderFacById(orderfacId, function(orderfac) {
		// if (orderfac.orderStatus == 2 && loginEmpId != "admin") {
		// if ($('orderStatus').value == 2) {
		// modFlag = true;
		// }
		// }
		// });
		// }
		// DWREngine.setAsync(true);
		// if (modFlag) {
		// Ext.Msg
		// .alert(
		// 'Message',
		// 'Sorry, this one has been reviewed, and if edit! Please review again
		// or contact the webmaster!');
		// return;
		// }

		// 验证表单
		var formData = getFormValues(orderfacForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		//判断是否需要themes
		var themes = $('themes').value;
		var themeStr = $('themeStr').value;
		var tg = false;
		if (themes == 'No' && themeStr == '') {
			tg = true;
		}
		var ck="";
		if (tg) {
			ck=" without themes Name";
		}

		// 验证单号是否存在
		var shortflag = false;
		var orderNo = $('orderNo').value;
		DWREngine.setAsync(false);
		cotOrderFacService.findIsExistOrderNo(orderNo, $('pId').value,
				function(res) {
					if (res != null) {
						shortflag = true;
					}
				});
		if (shortflag) {
			Ext.MessageBox.alert("Tip Box",
					"The contract number already exists, please re-enter！");
			return;
		}
		DWREngine.setAsync(true);

		Ext.MessageBox
				.confirm(
						'Message',
						'Do you want to save the purchase order'+ck+'？(Need Approved Again To Create New PDF)',
						function(btn) {
							if (btn == 'yes') {
								var form = DWRUtil.getValues('orderfacForm');
								var cotOrderFac = {};
								var checkFlag = false;
								DWREngine.setAsync(false);
								// 如果编号存在时先查询出对象,再填充表单
								if ($('pId').value != 'null'
										&& $('pId').value != '') {
									cotOrderFacService.getOrderFacById(
											$('pId').value, function(res) {
												for (var p in res) {
													if (p != 'orderTime'
															&& p != 'sendTime'
															&& p != 'shipmentDate'
															&& p != 'oderFacText'
															&& p != 'addTime'
															&& p != 'modTime') {
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
										if (p != 'orderTime' && p != 'sendTime'
												&& p != 'shipmentDate'
												&& p != 'oderFacText'
												&& p != 'addTime'
												&& p != 'modTime') {
											cotOrderFac[p] = form[p];
										}
									}
								}
								if (Ext.getCmp("orderZMArea").isVisible()) {
									cotOrderFac.orderZm = $('orderZMArea').value;// 正唛
									cotOrderFac.orderZhm = $('orderZHMArea').value;// 中唛
									cotOrderFac.orderCm = $('orderCMArea').value;// 侧唛
									cotOrderFac.orderNm = $('orderNMArea').value;// 内唛
									cotOrderFac.productM = $('productArea').value;
								}
								if (Ext.getCmp("orderClause")
										&& Ext.getCmp("orderClause")
												.isVisible()) {
									cotOrderFac.orderClause = $('orderClause').value;// 保存合同条款
								} else {
									if ($('pId').value == 'null'
											|| $('pId').value == '') {
										// 设置合同条款
										DWREngine.setAsync(false);
										sysdicutil.getCotContractList(2,
												function(res) {
													if (res != null) {
														var con = '';
														for (var i = 0; i < res.length; i++) {
															con += res[i].contractContent
																	+ "；\n";
														}
														cotOrderFac.orderClause = con;
													}
												});
										DWREngine.setAsync(true);
									}
								}

								// if
								// (Ext.getCmp("shenTab")&&Ext.getCmp("shenTab").isVisible())
								// {
								// cotOrderFac.checkReason =
								// $('checkReason').value;//
								// 审核理由
								// }
								// 其它信息
								// if (Ext.getCmp("quality").isVisible()) {
								if (Ext.isEmpty(Ext.getCmp('quality')
										.getValue())) {
									cotOrderFac.quality = txtAreaAry.quality;
								} else {
									cotOrderFac.quality = Ext.getCmp('quality')
											.getValue();
								}
								if (Ext.isEmpty(Ext.getCmp('colours')
										.getValue())) {
									cotOrderFac.colours = txtAreaAry.colours;
								} else {
									cotOrderFac.colours = Ext.getCmp('colours')
											.getValue();
								}

								if (Ext.isEmpty(Ext.getCmp('saleUnit')
										.getValue())) {
									cotOrderFac.saleUnit = txtAreaAry.saleUnit;
								} else {
									cotOrderFac.saleUnit = Ext
											.getCmp('saleUnit').getValue();
								}
								if (Ext.isEmpty(Ext.getCmp('handleUnit')
										.getValue())) {
									cotOrderFac.handleUnit = txtAreaAry.handleUnit;
								} else {
									cotOrderFac.handleUnit = Ext
											.getCmp('handleUnit').getValue();
								}

								if (Ext.isEmpty(Ext.getCmp('assortment')
										.getValue())) {
									cotOrderFac.assortment = txtAreaAry.assortment;
								} else {
									cotOrderFac.assortment = Ext
											.getCmp('assortment').getValue();
								}
								if (Ext.isEmpty(Ext.getCmp('comments')
										.getValue())) {
									cotOrderFac.comments = txtAreaAry.comments;
								} else {
									cotOrderFac.comments = Ext
											.getCmp('comments').getValue();
								}

								if (Ext.isEmpty(Ext.getCmp('shippingMark')
										.getValue())) {
									cotOrderFac.shippingMark = txtAreaAry.shippingMark;
								} else {
									cotOrderFac.shippingMark = Ext
											.getCmp('shippingMark').getValue();
								}

								if (Ext.isEmpty(Ext.getCmp('buyer').getValue())) {
									cotOrderFac.buyer = txtAreaAry.buyer;
								} else {
									cotOrderFac.buyer = Ext.getCmp('buyer')
											.getValue();
								}

								if (Ext
										.isEmpty(Ext.getCmp('seller')
												.getValue())) {
									cotOrderFac.seller = txtAreaAry.seller;
								} else {

									cotOrderFac.seller = Ext.getCmp('seller')
											.getValue();
								}
								// TODO:
								if (Ext.isEmpty(Ext.getCmp('agent').getValue())) {
									cotOrderFac.agent = txtAreaAry.agent;
								} else {
									cotOrderFac.agent = Ext.getCmp('agent')
											.getValue();
								}
								// 1表示选中
								var txt = txtAreaAry.isCheckAgent;
								if (txt == '0') {
									cotOrderFac.isCheckAgent = '0';
								} else {
									cotOrderFac.isCheckAgent = '1';
								}

								if (Ext.isEmpty(Ext.getCmp('forwardingAgent')
										.getValue())) {
									cotOrderFac.forwardingAgent = txtAreaAry.forwardingAgent;
								} else {
									cotOrderFac.forwardingAgent = Ext
											.getCmp('forwardingAgent')
											.getValue();
								}

								if (Ext.isEmpty(Ext.getCmp('booking')
										.getValue())) {
									cotOrderFac.booking = txtAreaAry.booking;
								} else {
									cotOrderFac.booking = Ext.getCmp('booking')
											.getValue();
								}
								if (Ext.isEmpty(Ext.getCmp('consignee')
										.getValue())) {
									cotOrderFac.consignee = txtAreaAry.consignee;
								} else {
									cotOrderFac.consignee = Ext
											.getCmp('consignee').getValue();
								}

								// }
								DWREngine.setAsync(true);
								DWREngine.setAsync(false);
								// 金额
								cotOrderFac.totalMoney = $('totalLab').innerText;
								cotOrderFac.realMoney = $('realLab').innerText;
								cotOrderFac.orderId = $('orderId').value;

								cotOrderFac.qcLocation = Ext
										.getCmp('qcLocation').getValue();

								cotOrderFac = setOrderFac(cotOrderFac);

								var eta = Ext.getCmp('orderLcDelay').getValue();
								if (Ext.isEmpty(eta)) {
									delete cotOrderFac.orderLcDelay;
								} else {
									cotOrderFac.orderLcDelay = new Date(Date
											.parse(eta));
								}
								var eta2 = Ext.getCmp('designTime').getValue();
								if (Ext.isEmpty(eta2)) {
									delete cotOrderFac.designTime;
								} else {
									cotOrderFac.designTime = new Date(Date
											.parse(eta2));
								}
								// department
								cotOrderFac.typeLv1Id = typeLv1IdBox.getValue();

								// printObject(cotOrderFac);
								cotOrderFacService.saveOrUpdateOrderFac(
										cotOrderFac, $('orderTime').value,
										$('sendTime').value,
										$('shipmentDate').value,
										$('addTime').value, checkFlag,$('oderFacText').value,
										function(res) {
											if (res != null) {
												if ($('pId').value == ''
														|| $('pId').value == 'null') {
													if ($('orderStatus').value == 0) {
														Ext.getCmp("qingBtn")
																.show();
													}
												} else {
													// 加载修改时间和修改人
													Ext
															.getCmp("modTime")
															.setValue(new Date());
													modEmpBox.bindPageValue(
															"CotEmps", "id",
															logId);
												}
												$('pId').value = res;
												// Ext.getCmp('upmodMai').show();
												// Ext.getCmp('updelMai').show();
												// Ext.getCmp('upmodProMai').show();
												// Ext.getCmp('updelProMai').show();

												// 更改添加action参数
												var urlAdd = '&orderId=' + res;

												// 更改修改action参数
												var urlMod = '&orderId=' + res;

												ds.proxy.setApi({
													read : "cotorderfac.do?method=queryDetail&orderId="
															+ res
															+ "&con="
															+ "&flag=orderFacDetail",
													create : "cotorderfac.do?method=addDetail"
															+ urlAdd,
													update : "cotorderfac.do?method=modifyDetail"
															+ urlMod,
													destroy : "cotorderfac.do?method=removeDetail&orderId="
															+ res
												});
												ds.save();
												cotOrderFacService.saveDetailCopy($('pId').value,function(res){});
												// 调用存储过程，更新单据状态
												cotOrderFacService
														.updateOrderStatusCon(
																res,
																$("orderId").value,
																function(code) {
																	reloadHome();
																});
												Ext.Msg.alert("Message",
														"Successfully saved！");

												// if (freshFlag == true) {
												// var frame =
												// window.frames["otherFeeInfo"];
												// // 保存加减费用
												// frame.saveOther($('pId').value,
												// $('orderNo').value,
												// 'orderfac', 1,
												// $('factoryId').value);
												//
												// // 保存应付帐款
												// frame
												// .saveAccountdeal($('pId').value);
												// }

											} else {
												Ext.MessageBox.alert('Message',
														'Save failed');
											}
											// Ext.getCmp('printBtn').show();
										});
								DWREngine.setAsync(true);
							}
						})
	}

	// 显示导入界面
	var _self = this;
	function showImportPanel() {
		// 验证表单
		var formData = getFormValues(orderfacForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var cfg = {};
		cfg.orderfacId = $('pId').value;
		cfg.factoryId = $('factoryId').value;
		cfg.businessPerson = $('businessPerson').value;
		cfg.bar = _self;

		var orderWin = new OrderWin(cfg);
		orderWin.show();
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
		var currencyId = $('currencyId').value;
		for (var i = 0; i < idAry.length; i++) {
			DWREngine.setAsync(false);
			cotOrderFacService.findIsExistDetail(idAry[i], function(detail) {
						if (detail != null) {
							// 币种转换,换算成主单币种值
							DWREngine.setAsync(false);
							queryService.updatePrice(detail.priceFac,
									detail.priceFacUint, currencyId, function(
											newPri) {
										detail.priceFac = newPri
												.toFixed(facNum);
										detail.priceFacUint = currencyId;
										setObjToGrid(detail);

									});
							DWREngine.setAsync(true);

						}
					});
			DWREngine.setAsync(true);
		}
		// 显示总金额
		var totalFac = summary.getSumTypeValue('totalFac');
		$('totalLab').innerText = (Number($('totalLab').innerText)
				.add(totalFac)).toFixed(facNum);
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
			Ext.Msg.alert('Tip Box', 'You do not have permission to review！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Whether to request review?',
				function(btn) {
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
			Ext.Msg.alert('Tip Box', 'You do not have permission to review！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Whether has been reviewed?',
				function(btn) {
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
			Ext.Msg.alert('Tip Box', 'You do not have permission to review');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Determining non-approved?',
				function(btn) {
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
			Ext.Msg.alert('Tip Box', 'You do not have permission to review！');
			return;
		}
		var checkFlag = false;
		Ext.MessageBox.confirm('Message', 'Whether to review again?', function(
						btn) {
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
														// if (orderfac != null)
														// {
														// if
														// (orderfac.orderStatus
														// != 9) {
														// if
														// (orderfac.orderStatus
														// == 3) {
														// Ext
														// .getCmp('requestBtn')
														// .hide();
														// Ext
														// .getCmp('passBtn')
														// .setVisible(true);
														// Ext
														// .getCmp('unpassBtn')
														// .setVisible(true);
														// } else if
														// (orderfac.orderStatus
														// == 0) {
														// Ext
														// .getCmp('requestBtn')
														// .setVisible(true);
														// Ext
														// .getCmp('recheckBtn')
														// .hide();
														// } else if
														// (orderfac.orderStatus
														// == 2) {
														// Ext
														// .getCmp('recheckBtn')
														// .setVisible(true);
														// Ext
														// .getCmp('passBtn')
														// .hide();
														// Ext
														// .getCmp('unpassBtn')
														// .hide();
														// } else {
														// Ext
														// .getCmp('requestBtn')
														// .setVisible(true);
														// Ext
														// .getCmp('passBtn')
														// .hide();
														// Ext
														// .getCmp('unpassBtn')
														// .hide();
														// }
														// } else {
														// Ext
														// .getCmp('requestBtn')
														// .setVisible(true);
														// }
														// }
													});
										}
									});
						}
					});
		} else {
			Ext.Msg.alert('Tip Box', 'Please save the main one！');
		}
		DWREngine.setAsync(true);
	}

	// 打开上传面板,用于上次唛标
	function showUploadMaiPanel() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, the order has been reviewed can not be modified! To change pls. review again!');
			return;
		}
		var id = $('pId').value;
		if (id == 'null' || id == '') {
			Ext.MessageBox.alert("Tip Box", "Please save the main one！");
			return;
		}
		var win = new UploadWin({
					params : {
						orderfacId : id
					},
					waitMsg : "Photo upload......",
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

	// 打开上传面板,用于上次产品标
	function showUploadProductPanel() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, the order has been reviewed can not be modified! To change pls. review again!');
			return;
		}
		var id = $('pId').value;
		var win = new UploadWin({
					params : {
						mainId : id,
						mbType : "1"
					},
					waitMsg : "Photo upload......",
					opAction : "modify",
					imgObj : $('product_MB'),
					imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=productFacMB&temp=" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadProMBPic.action',
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
		Ext.MessageBox
				.confirm(
						'Message',
						'Are you sure the serial number on the reorder？(Please save the new data, or will be lost！)',
						function(btn) {
							if (btn == 'yes') {
								DWREngine.setAsync(false);
								cotOrderFacService.updateSortNo($("pId").value,
										function(res) {
											if (res) {
												// ECSideUtil.reload("orderFacDetail");
											} else {
												Ext.Msg
														.alert("Message",
																"Update sequence number failed!");
											}
										});
								DWREngine.setAsync(true);
							}
						})
	}

	// 从订单导入唛头信息
	function showOrderMb() {
		var cfg = {};
		var orderfacId = $('pId').value;
		if (orderfacId == 'null' || orderfacId == '') {
			Ext.Msg.alert("Tip Box", "Please save the main one！");
			return;
		}
		cfg.bar = _self;
		cotOrderFacService.getCotOrderIds(parseInt(orderfacId), function(res) {
					if (res != null) {
						cfg.orderIds = res;
						var importPanel = new OrderMBGrid(cfg);
						importPanel.show();
					} else {
						Ext.MessageBox.alert("Tip Box",
								"No details of the procurement contract！");
						return;
					}
				});

	}

	// 生产价改变事件
	function firePriceFacChange(rec, newVal, flag) {
		if (newVal == '') {
			newVal = 0;
		}
		var rdm = rec.data.rdm;
		cotOrderFacService.getOrderFacMapValueByRdm(rdm, function(res) {
					if (res != null) {

						var boxCount = rec.data.boxCount;
						var totalAmmount = Number(newVal).mul(boxCount);
						$('totalLab').innerText = (parseFloat($('totalLab').innerText)
								- rec.data.totalFac + totalAmmount)
								.toFixed("2");
						$('realLab').innerText = (parseFloat($('realLab').innerText)
								- rec.data.totalFac + totalAmmount)
								.toFixed("2");
						// 计算金额
						rec.set("totalFac", totalAmmount);
						updateMapValue(rdm, 'totalFac', totalAmmount);
					}
				});
		if (flag == 2) {
			updateMapValue(rdm, 'priceFac', newVal);
		}
	}

	// 双击历史事件表格更新报价事件
	function replacePrice(hisGrid, index) {

		var hisrec = hisGrid.getStore().getAt(index);
		var hisprice = hisrec.get("priceFac");
		var pricerec = sm.getSelected();
		pricerec.set("priceFac", hisprice)
		firePriceFacChange(pricerec, hisprice, 2);
		var hisWin = Ext.getCmp("hisWin");
		hisWin.close();

		// // 更改添加action参数
		// var urlAdd = '&orderId=' + $('pId').value;
		//
		// // 更改修改action参数
		// var urlMod = '&orderId=' + $('pId').value;
		//
		// ds.proxy.setApi({
		// read : "cotorderfac.do?method=queryDetail&orderId="
		// + $('pId').value + "&flag=orderFacDetail",
		// create : "cotorderfac.do?method=addDetail" + urlAdd,
		// update : "cotorderfac.do?method=modifyDetail" + urlMod,
		// destroy : "cotorderfac.do?method=removeDetail&orderId="
		// + $('pId').value
		// });
		// ds.save();
	}

	// 加载其他费用
	// 第一次显示差额页面.再点不刷新
	var freshFlag = false;
	function loadOtherFeeInfo() {
		if (freshFlag == false) {
			var frame = window.frames["otherFeeInfo"];
			frame.location.href = "cotorderfac.do?method=queryFinanceOther"
					+ "&type=1&fkId=" + $('pId').value;
		}
		freshFlag = true;
	}

	// 删除产品标图片
	function delProductMBPic() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, the order has been reviewed can not be modified! To change pls. review again!');
			return;
		}
		Ext.MessageBox.confirm('Message',
				"Are you sure you remove the product mark?", function(btn) {
					if (btn == 'yes') {
						var pId = $('pId').value;
						cotOrderFacService.deleteProductMBPicImg(pId, function(
										res) {
									if (res) {
										$('product_MB').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox
												.alert('Message',
														'Failed to delete product mark！');
									}
								})
					}
				});
	}

	// 删除唛标图片
	function delMBPic() {
		if ($('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox
					.alert(
							'Message',
							'Sorry, the order has been reviewed can not be modified! To change pls. review again!');
			return;
		}
		Ext.MessageBox.confirm('Message', "Are you sure to remove the mark?",
				function(btn) {
					if (btn == 'yes') {
						var pId = $('pId').value;
						cotOrderFacService.deleteMBPicImg(pId, function(res) {
									if (res) {
										$('order_MB').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox.alert('Message',
												'Mark  failed to delete！');
									}
								})
					}
				});
	}

	// 给其他费用页面调用,验证主表单
	this.checkParent = function() {
		// 验证表单
		var formData = getFormValues(orderfacForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return false;
		} else {
			return true;
		}
	}
	function setProject(obj) {
		if (obj.simpleSampleDeadline != null && obj.simpleSampleDeadline != '') {
			var date = new Date(obj.simpleSampleDeadline);
			var cmp = Ext.getCmp("simpleSampleDeadline");
			cmp.setValue(obj.simpleSampleDeadline)
		}
		if (obj.simpleSampleApproval != null && obj.simpleSampleApproval != '') {
			var date = new Date(obj.simpleSampleApproval);
			var cmp = Ext.getCmp("simpleSampleApproval");
			cmp.setValue(date)
		}
		if (obj.completeSampleDeadline != null
				&& obj.completeSampleDeadline != '') {
			var date = new Date(obj.completeSampleDeadline);
			var cmp = Ext.getCmp("completeSampleDeadline");
			cmp.setValue(date)
		}
		if (obj.completeSampleApproval != null
				&& obj.completeSampleApproval != '') {
			var date = new Date(obj.completeSampleApproval);
			var cmp = Ext.getCmp("completeSampleApproval");
			cmp.setValue(date)
		}

		if (obj.facDeadline != null && obj.facDeadline != '') {
			var date = new Date(obj.facDeadline);
			var cmp = Ext.getCmp("facDeadline");
			cmp.setValue(date)
		}
		if (obj.facApproval != null && obj.facApproval != '') {
			var date = new Date(obj.facApproval);
			var cmp = Ext.getCmp("facApproval");
			cmp.setValue(date)
		}
		if (obj.pcaketDeadline != null && obj.pcaketDeadline != '') {
			var date = new Date(obj.pcaketDeadline);
			var cmp = Ext.getCmp("pcaketDeadline");
			cmp.setValue(date)
		}
		if (obj.pcaketApproval != null && obj.pcaketApproval != '') {
			var date = new Date(obj.pcaketApproval);
			var cmp = Ext.getCmp("pcaketApproval");
			cmp.setValue(date)
		}
		if (obj.samplePicDeadline != null && obj.samplePicDeadline != '') {
			var date = new Date(obj.samplePicDeadline);
			var cmp = Ext.getCmp("samplePicDeadline");
			cmp.setValue(date)
		}
		if (obj.samplePicApproval != null && obj.samplePicApproval != '') {
			var date = new Date(obj.samplePicApproval);
			var cmp = Ext.getCmp("samplePicApproval");
			cmp.setValue(date)
		}
		if (obj.sampleOutDeadline != null && obj.sampleOutDeadline != '') {
			var date = new Date(obj.sampleOutDeadline);
			var cmp = Ext.getCmp("sampleOutDeadline");
			cmp.setValue(date)
		}
		if (obj.sampleOutApproval != null && obj.sampleOutApproval != '') {
			var date = new Date(obj.sampleOutApproval);
			var cmp = Ext.getCmp("sampleOutApproval");
			cmp.setValue(date)
		}
		if (obj.qcDeadline != null && obj.qcDeadline != '') {
			var date = new Date(obj.qcDeadline);
			var cmp = Ext.getCmp("qcDeadline");
			cmp.setValue(date)
		}
		if (obj.qcApproval != null && obj.qcApproval != '') {
			var date = new Date(obj.qcApproval);
			var cmp = Ext.getCmp("qcApproval");
			cmp.setValue(date)
		}
		if (obj.shippingDeadline != null && obj.shippingDeadline != '') {
			var date = new Date(obj.shippingDeadline);
			var cmp = Ext.getCmp("shippingDeadline");
			cmp.setValue(date)
		}
		if (obj.shippingApproval != null && obj.shippingApproval != '') {
			var date = new Date(obj.shippingApproval);
			var cmp = Ext.getCmp("shippingApproval");
			cmp.setValue(date)
		}
		if (obj.loadingDeadline != null && obj.loadingDeadline != '') {
			var date = new Date(obj.loadingDeadline);
			var cmp = Ext.getCmp("loadingDeadline");
			cmp.setValue(date)
		}
		if (obj.loadingApproval != null && obj.loadingApproval != '') {
			var date = new Date(obj.loadingApproval);
			var cmp = Ext.getCmp("loadingApproval");
			cmp.setValue(date)
		}
		if (obj.qcLocation != null && obj.qcLocation != '') {
			var cmp = Ext.getCmp("qcLocation");
			cmp.setValue(obj.qcLocation);
		}
	}
	function setOrderFac(obj) {
		var time = DWRUtil.getValues('timeForm');
		if (time == null) {
			// alert(Ext.getCmp("facDeadline").getValue())
			obj.simpleSampleDeadline = Ext.getCmp("simpleSampleDeadline")
					.getValue() == "" ? "null" : Ext
					.getCmp("simpleSampleDeadline").getValue();
			obj.simpleSampleApproval = Ext.getCmp("simpleSampleApproval")
					.getValue() == "" ? "null" : Ext
					.getCmp("simpleSampleApproval").getValue();
			obj.completeSampleDeadline = Ext.getCmp("completeSampleDeadline")
					.getValue() == "" ? "null" : Ext
					.getCmp("completeSampleDeadline").getValue();
			obj.completeSampleApproval = Ext.getCmp("completeSampleApproval")
					.getValue() == "" ? "null" : Ext
					.getCmp("completeSampleApproval").getValue();
			obj.facDeadline = Ext.getCmp("facDeadline").getValue() == ""
					? "null"
					: Ext.getCmp("facDeadline").getValue();
			obj.facApproval = Ext.getCmp("facApproval").getValue() == ""
					? "null"
					: Ext.getCmp("facApproval").getValue();
			obj.pcaketDeadline = Ext.getCmp("pcaketDeadline").getValue() == ""
					? "null"
					: Ext.getCmp("pcaketDeadline").getValue();
			obj.pcaketApproval = Ext.getCmp("pcaketApproval").getValue() == ""
					? "null"
					: Ext.getCmp("pcaketApproval").getValue();
			obj.samplePicDeadline = Ext.getCmp("samplePicDeadline").getValue() == ""
					? "null"
					: Ext.getCmp("samplePicDeadline").getValue();
			obj.samplePicApproval = Ext.getCmp("samplePicApproval").getValue() == ""
					? "null"
					: Ext.getCmp("samplePicApproval").getValue();
			obj.sampleOutDeadline = Ext.getCmp("sampleOutDeadline").getValue() == ""
					? "null"
					: Ext.getCmp("sampleOutDeadline").getValue();
			obj.sampleOutApproval = Ext.getCmp("sampleOutApproval").getValue() == ""
					? "null"
					: Ext.getCmp("sampleOutApproval").getValue();
			obj.qcDeadline = Ext.getCmp("qcDeadline").getValue() == ""
					? "null"
					: Ext.getCmp("qcDeadline").getValue();
			obj.qcApproval = Ext.getCmp("qcApproval").getValue() == ""
					? "null"
					: Ext.getCmp("qcApproval").getValue();
			obj.shippingDeadline = Ext.getCmp("shippingDeadline").getValue() == ""
					? "null"
					: Ext.getCmp("shippingDeadline").getValue();
			obj.shippingApproval = Ext.getCmp("shippingApproval").getValue() == ""
					? "null"
					: Ext.getCmp("shippingApproval").getValue();
			obj.loadingDeadline = Ext.getCmp("loadingDeadline").getValue() == ""
					? "null"
					: Ext.getCmp("loadingDeadline").getValue();
			obj.loadingApproval = Ext.getCmp("loadingApproval").getValue() == ""
					? "null"
					: Ext.getCmp("loadingApproval").getValue();
		} else {
			obj.simpleSampleDeadline = getDateType(time.simpleSampleDeadline);
			obj.simpleSampleApproval = getDateType(time.simpleSampleApproval);
			obj.completeSampleDeadline = getDateType(time.completeSampleDeadline);
			obj.completeSampleApproval = getDateType(time.completeSampleApproval);
			obj.facDeadline = getDateType(time.facDeadline);
			obj.facApproval = getDateType(time.facApproval);
			obj.pcaketDeadline = getDateType(time.pcaketDeadline);
			obj.pcaketApproval = getDateType(time.pcaketApproval);
			obj.samplePicDeadline = getDateType(time.samplePicDeadline);
			obj.samplePicApproval = getDateType(time.samplePicApproval);
			obj.sampleOutDeadline = getDateType(time.sampleOutDeadline);
			obj.sampleOutApproval = getDateType(time.sampleOutApproval);
			obj.qcDeadline = getDateType(time.qcDeadline);
			obj.qcApproval = getDateType(time.qcApproval);
			obj.shippingDeadline = getDateType(time.shippingDeadline);
			obj.shippingApproval = getDateType(time.shippingApproval);
			obj.loadingDeadline = getDateType(time.loadingDeadline);
			obj.loadingApproval = getDateType(time.loadingApproval);
		}

		return obj;
	}
	// 显示计划日期页面
	var timeflag = false;
	function loadTimeInfo() {
		if ($('pId').value != '' && $('pId').value != 'null') {
			if (timeflag == false) {
				var frame = document.timeInfo;
				frame.location.href = "cotorderfac.do?method=queryTime&orderId="
						+ $('pId').value;
				// frame.location.href = 'http://www.baidu.com';
			}
			timeflag = true;
		} else {
			Ext.MessageBox.alert("Message",
					"Please save the order, then the completion date set!");
		}
	}

	// 请求审核
	function qingShen() {
		DWREngine.setAsync(false);
		var isInvoiceExist = false;
		//判断Invoice是否已经生成，如果生成不能返审
		cotOrderOutService.isInvoiceExistByPIId($("orderId").value,function(res){
			isInvoiceExist = res;
		});
		if(isInvoiceExist){
			Ext.Msg.alert("Message", "Invoice had been created,Can't be approval! Please create creditNote.");
			return;
		}
		//2013-12-05 如果 shipsample已经approve，就不能返审
		var sampleOut = 0;
		cotOrderFacService.getOrderFacById($('pId').value,function(res){
			sampleOut = res.sampleOutStatus;
		});
		if(sampleOut == 1){
			Ext.Msg.alert("Message", "Shipment Sample had been approved,Can't be approval!");
			return;
		}
		// 如果没有选择审核人
		if ($('shenPerson').value == '') {
			Ext.Msg.alert("Message", "Please Choose Approve Person!");
			return;
		}
		//Ext.MessageBox.confirm('Message', "Do you want Approval By Mox?",
		Ext.MessageBox.confirm('Message', "Do you want Approval?",
				function(btn) {
					if (btn == 'yes') {
						//2012-12-3审核人只能是id为47的名称为MOX员工审核
						//2013-07-29可以选择审核人
						cotOrderFacService.updateOrderStatus($('pId').value, 3,
								$('shenPerson').value, function(res) {
//								47, function(res) {
									orderStatusBox.setValue(3);
									reflashParent('orderFacGrid');
									// 修改人
									Ext.getCmp("modTime").setValue(res);
									modEmpBox.bindPageValue("CotEmps", "id",
											logId);
									// 审核人
									empChkBox.bindPageValue("CotEmps", "id",
											$('shenPerson').value);
//									empChkBox.bindPageValue("CotEmps", "id",
//											47);
									shenPanel.hide();
									Ext.getCmp("qingBtn").hide();
									Ext.getCmp("showPdfBtn").hide();
									// 如果登录人是admin或者是审核人才显示"审核通过"按钮
									if (loginEmpId == "admin"
											|| $('shenPerson').value == logId) {
//											|| 47 == logId) {
										Ext.getCmp("guoBtn").show();
									}
									//2012-12-11 提请审核后把制单人改成当前登录人
									cotOrderFacService.updateAddPerson(function(){
										addEmpBox.bindPageValue("CotEmps", "id",logId);
									})
								});

					}
				});
		DWREngine.setAsync(true);
	}
	function checkShen(){
		cotOrderFacService.getApprove($('pId').value,function(canApprove){
			if(canApprove){
				guoShen();
			}else{
				Ext.Msg.alert("Message","Please approve PI first!");
			}
		});
	}
	// 审核通过
	function guoShen() {
		Ext.MessageBox.confirm('Message', "Are you sure Approved the PO?",
				function(btn) {
					if (btn == 'yes') {
						mask();

						// 延时
						var task = new Ext.util.DelayedTask(function() {
									DWREngine.setAsync(false);
									cotOrderFacService.updateOrderStatus(
											$('pId').value, 2, null, function(
													res) {
												orderStatusBox.setValue(2);

												// 修改人
												Ext.getCmp("modTime")
														.setValue(res);
												modEmpBox.bindPageValue(
														"CotEmps", "id", logId);

												reflashParent('orderFacGrid');
												Ext.getCmp("showPdfBtn").show();
												Ext.getCmp("guoBtn").hide();
												Ext.getCmp("qingBtn").show();
												reloadHome();
												//生成副本
												cotOrderFacService.saveDetailCopy($('pId').value,function(res){});
												// 调用存储过程，更新单据状态
												cotOrderFacService
														.updateOrderStatusCon(
																$('pId').value,
																$("orderId").value,
																function(code) {
																	unmask();
																});
											});
									DWREngine.setAsync(true);
								});
						task.delay(500);

					}
				});
	}

	//在浏览器打开pdf
	function showPdf() {
		var rdm = Math.round(Math.random() * 10000);
		openEleWindow("./saverptfile/orderfac/PO-" + $('orderNo').value
				+ ".pdf?tmp=" + rdm);
	}

	// 审核不通过
	function buShen() {
		Ext.MessageBox.confirm('Message', "Are you sure Non-review the order?",
				function(btn) {
					if (btn == 'yes') {
						cotOrderFacService.updateOrderStatus($('pId').value, 1,
								null, function(res) {
									orderStatusBox.setValue(1);
									// 修改人
									Ext.getCmp("modTime").setValue(res);
									modEmpBox.bindPageValue("CotEmps", "id",
											logId);
									reflashParent('orderFacGrid');
									Ext.getCmp("guoBtn").hide();
									//Ext.getCmp("buBtn").hide();
							})

					}
				});
	}

	// 显示模板面板
	var shenPanel;
	function showShenPnl(item, pressed) {
		if ($('shenDiv') == null) {
			// window默认在z-index为9000
			Ext.DomHelper.append(document.body, {
				html : '<div id="shenDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (shenPanel == null) {
			shenPanel = new Ext.form.FormPanel({
						labelWidth : 90,
						padding : "5px",
						height : 80,
						width : 280,
						layout : 'hbox',
						frame : true,
						title : "Request Audit",
						labelAlign : "right",
						layoutConfig : {
							padding : '5',
							align : 'middle'
						},
						defaults : {
							margins : '0 5 0 0'
						},
						tools : [{
									id : "close",
									handler : function(event, toolEl, p) {
										p.hide();
									}
								}],
						items : [{
									xtype : "label",
									text : "Approve Person:",
									width : 100
								}, empShenBox, {
									xtype : "button",
									flex : 1,
									text : "OK",
									iconCls : "page_table_save",
									handler : qingShen
								}]
					});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("shenDiv").setLeftTop(left - 80, top - 90);
			shenPanel.render("shenDiv");
		} else {
			if (!shenPanel.isVisible()) {
				shenPanel.show();
			} else {
				shenPanel.hide();
			}
		}
	};
	
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

});