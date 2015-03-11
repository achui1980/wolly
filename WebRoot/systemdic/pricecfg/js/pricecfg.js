Ext.onReady(function() {
	var _self = this;

	// 运输方式
	var trafficType = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTrafficType",
				valueField : "id",
				hidden : true,
				hideLabel : true,
				fieldLabel : "Transportation",
				displayField : "trafficNameEn",
				cmpId : "trafficTypeId",
				emptyText : 'Choose',
				tabIndex : 1,
				triggerAction : 'all',
				anchor : "100%"
			});

	// 出口公司
	var company = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCompany&flag=pic",
				valueField : "id",
				fieldLabel : "Export Company",
				displayField : "companyShortName",
				cmpId : "companyId",
				emptyText : 'Choose',
				tabIndex : 2,
				anchor : "100%"
			});

	// 价格条款
	var caluse = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotClause",
				valueField : "id",
				fieldLabel : "Price Terms",
				displayField : "clauseName",
				cmpId : "caluseId",
				emptyText : 'Choose',
				tabIndex : 3,
				anchor : "100%"
			});

	// 业务币种
	var currency = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				valueField : "id",
				fieldLabel : "Currency",
				displayField : "curNameEn",
				cmpId : "currencyId",
				emptyText : 'Choose',
				tabIndex : 4,
				anchor : "100%"
			});

	// 生产合同币种
	var facPriceUnit = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				valueField : "id",
				fieldLabel : "Currency",
				displayField : "curNameEn",
				cmpId : "facPriceUnit",
				emptyText : 'Choose',
				tabIndex : 19,
				anchor : "100%"
			});

	// 报价场合
	var situation = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotPriceSituation",
				valueField : "id",
				hidden : true,
				hideLabel : true,
				fieldLabel : "Quote occasions",
				displayField : "situationName",
				cmpId : "situationId",
				emptyText : 'Choose',
				tabIndex : 5,
				anchor : "100%"
			});

	// 集装箱类型
	var containerType = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotContainerType",
				valueField : "id",
				hidden : true,
				hideLabel : true,
				fieldLabel : "Container type",
				displayField : "containerName",
				cmpId : "containerTypeId",
				emptyText : 'Choose',
				tabIndex : 6,
				anchor : "100%"
			});

	// 起运港
	var shipPort = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotShipPort&key=shipPortNameEn",
		cmpId : 'shipPortId',
		fieldLabel : "Port of shipment",
		editable : true,
		valueField : "id",
		allowBlank : true,
		displayField : "shipPortNameEn",
		emptyText : 'Choose',
		mode : 'remote',// 默认local
		pageSize : 10,
		anchor : "100%",
		tabIndex : 10,
		sendMethod : "post",
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	/***************************************************************************
	 * 描述: 本地下拉列表
	 **************************************************************************/
	var insertTypeStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'Asked one by one'],
						[1, 'Do not ask, add records directly']]
			});

	var insertTypeField = new Ext.form.ComboBox({
				name : 'insertType',
				fieldLabel : 'Entry way of asking',
				editable : false,
				hidden : true,
				hideLabel : true,
				store : insertTypeStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				emptyText : 'Choose',
				tabIndex : 7,
				hiddenName : 'insertType',
				selectOnFocus : true
			});

	var noPriceStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'Get Product Info'],
						[1, 'Formula and Get Product Info']]
			});

	var noPriceField = new Ext.form.ComboBox({
				name : 'noPrice',
				fieldLabel : 'No History',
				editable : false,
				hidden : true,
				hideLabel : true,
				store : noPriceStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				emptyText : 'Choose',
				tabIndex : 8,
				hiddenName : 'noPrice',
				selectOnFocus : true
			});

	var havePriceStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'Get Product Info'],
						[1, 'Formula and Get History Info'],
						[2, 'Get Last Info']]
			});

	var havePriceField = new Ext.form.ComboBox({
				name : 'havePrice',
				fieldLabel : 'Has History',
				editable : false,
				hidden : true,
				hideLabel : true,
				store : havePriceStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				emptyText : 'Choose',
				tabIndex : 9,
				hiddenName : 'havePrice',
				selectOnFocus : true
			});

	var isCheckStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[1, 'Yes'], [0, 'No']]
			});

	var isCheckField = new Ext.form.ComboBox({
				name : 'isCheck',
				fieldLabel : 'Whether the audit',
				editable : false,
				store : isCheckStore,
				valueField : "id",
				hidden : true,
				hideLabel : true,
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				emptyText : 'Choose',
				tabIndex : 15,
				hiddenName : 'isCheck',
				selectOnFocus : true
			});

	var orderTipStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[1, 'Yes'], [0, 'No']]
			});

	var orderTipField = new Ext.form.ComboBox({
				name : 'orderTip',
				fieldLabel : 'Whether automatically adjust Quantity',
				editable : false,
				hidden : true,
				hideLabel : true,
				store : orderTipStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				emptyText : 'Choose',
				tabIndex : 16,
				hiddenName : 'orderTip',
				selectOnFocus : true
			});

	var priceCfgForm = new Ext.form.FormPanel({
		labelWidth : 120,
		labelAlign : "right",
		layout : "form",
		width : 420,
		formId : "priceCfgForm",
		id : "priceCfgFormId",
		autoHeight : true,
		buttonAlign : "center",
		fbar : [{
					iconCls : "page_mod",
					handler : mod,
					width : 65,
					text : "Save"
				}, {
					xtype : 'button',
					text : "Reset",
					iconCls : "page_reset",
					width : 65,
					handler : function() {
						priceCfgForm.getForm().reset()
					}
				}],
		items : [{
			xtype : "fieldset",
			title : "Business configuration",
			layout : "form",
			columnWidth : 1,
			padding : "0",
			items : [trafficType, company, caluse, currency, situation,
					containerType, insertTypeField, noPriceField,
					havePriceField, shipPort, {
						xtype : "numberfield",
						name : 'priceProfit',
						fieldLabel : "Profit%",
						hidden:true,
						hideLabel:true,
						tabIndex : 11,
						anchor : "100%"
					}, {
						xtype : "numberfield",
						name : 'insureAddRate',
						fieldLabel : "Insurance%",
						hidden : true,
						hideLabel : true,
						tabIndex : 12,
						anchor : "100%"
					}, {
						xtype : "numberfield",
						name : 'insureRate',
						fieldLabel : "Insurance‰",
						tabIndex : 13,
						hidden : true,
						hideLabel : true,
						anchor : "100%"
					}, {
						xtype : "numberfield",
						name : 'priceFob',
						hidden : true,
						hideLabel : true,
						fieldLabel : "FOB Fees",
						tabIndex : 14,
						anchor : "100%"
					}, isCheckField, {
						xtype : "panel",
						title : "",
						layout : "form",
						labelAlign : "right",
						labelWidth : 240,
						items : [orderTipField]
					}]
		}, {
			xtype : "fieldset",
			title : "P.O configuration",
			layout : "form",
			columnWidth : 1,
			padding : "0",
			items : [{
						xtype : "textfield",
						fieldLabel : "Signed at",
						name : 'orderAddress',
						hidden : true,
						hideLabel : true,
						tabIndex : 17,
						anchor : "100%"
					}, {
						xtype : "textfield",
						name : 'givenAddress',
						fieldLabel : "Place of delivery",
						tabIndex : 18,
						hidden : true,
						hideLabel : true,
						anchor : "100%"
					}, facPriceUnit, {
						xtype : "panel",
						title : "",
						layout : "form",
						hidden : true,
						labelAlign : "right",
						labelWidth : 280,
						items : [{
							xtype : "numberfield",
							name : 'appendDay',
							fieldLabel : "P.O delivery date earlier than the days of the P.I delivery date(Default 10 days)",
							tabIndex : 20,
							anchor : "100%"
						}]
					}, {
						xtype : 'hidden',
						name : 'id',
						id : 'id'
					}]
		}]
	})

	var mainPnl = new Ext.Panel({
				layout : "hbox",
				width : "100%",
				autoScroll : true,
				border : false,
				ctCls : 'x-panel-mc',
				height : Ext.getBody().getHeight(),
				layoutConfig : {
					pack : 'center',
					align : "top"
				},
				items : [priceCfgForm]
			})

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [mainPnl]
			});

	// 初始化页面
	function initForm() {

		// 加载主样品表单
		DWREngine.setAsync(false);
		cotPriceCfgService.getPriceCfg(function(res) {
					if (res != null) {
						var obj = res;
						DWRUtil.setValues(obj);
						trafficType.bindValue(obj.trafficTypeId);
						company.bindValue(obj.companyId);
						caluse.bindValue(obj.caluseId);
						currency.bindValue(obj.currencyId);
						situation.bindValue(obj.situationId);
						containerType.bindValue(obj.containerTypeId);
						shipPort.bindPageValue("CotShipPort", "id",
								obj.shipPortId);
						facPriceUnit.bindValue(obj.facPriceUnit);
						insertTypeField.setValue(obj.insertType);
						noPriceField.setValue(obj.noPrice);
						havePriceField.setValue(obj.havePrice);
						isCheckField.setValue(obj.isCheck);
						orderTipField.setValue(obj.orderTip);
					}
				})
		DWREngine.setAsync(true);
	}
	// 初始化页面
	initForm();

	// 保存
	function mod() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("Message", "Sorry, you do not have Authority!");
			return;
		}

		var obj = DWRUtil.getValues("priceCfgForm");
		var cotPriceCfg = new CotPriceCfg();
		for (var p in cotPriceCfg) {
			cotPriceCfg[p] = obj[p];
		}
		cotPriceCfg.id = Ext.getDom('id').value;
		cotPriceCfgService.modifyPriceCfg(cotPriceCfg, function(res) {
					Ext.MessageBox.alert("Message", "Successfully modified!");
				})
	}
})
