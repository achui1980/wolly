//实达打印面板
PrintWinSD = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	// 打印类型
	var printType = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['XLS', 'XLS'], ['PDF', 'PDF']]
			});
	var typeBox = new Ext.form.ComboBox({
				name : 'printType',
				id : 'printType',
				xtype : 'combo',
				fieldLabel : "导出类型",
				editable : false,
				store : printType,
				valueField : "id",
				value : "XLS",
				displayField : "name",
				mode : 'local',
				anchor : '95%',
				triggerAction : 'all',
				selectOnFocus : false
			});

	// 报价
	var type = 0;
	var exlAction = "./downRptSD.action";
	// 邮件接收人
	var custId = 0;
	var factoryId = 0;
	var mail = {};
	mail.type = cfg.type;

	// 报价
	if (cfg.type == 'price') {
		type = 2;
		exlAction = "./downPrice.action";
	}
	// 订单
	if (cfg.type == 'order') {
		type = 5;
		exlAction = "./downRptSD.action";
	}
	// 送样
	if (cfg.type == 'given') {
		type = 4;
		exlAction = "./downGiven.action";
	}
	// 是否显示厂家
	var sFac = true;
	// 出货
	if (cfg.type == 'orderout') {
		sFac = false;
		type = 9;
		exlAction = "./downOrderOut.action";
	}
	// 配件采购
	if (cfg.type == 'fitting') {
		type = 12;
		exlAction = "./downFitOrder.action";
	}
	// 生产合同
	if (cfg.type == 'orderfac') {
		type = 8;
		exlAction = "./downOrderFac.action";
	}
	// 包材采购
	if (cfg.type == 'packing') {
		type = 13;
		exlAction = "./downPackOrder.action";
	}
	// 排载
	if (cfg.type == 'split') {
		type = 7;
		exlAction = "./downSplit.action";

	}

	// 查询参数
	var querySql = "";
	var tplUrl = "./servlet/DataSelvert?tbname=CotRptFile&key=rptName&typeName=rptType&type="
			+ type;
	
	//审核状态
	var status;

	// 导出模板
	var modelBox = new BindCombox({
				dataUrl : tplUrl,
				cmpId : 'reportTemple',
				fieldLabel : "导出模板",
				editable : true,
				sendMethod : "post",
				valueField : "rptfilePath",
				displayField : "rptName",
				emptyText : '请选择',
				anchor : "95%",
				triggerAction : 'all'
			});

	// 设置默认模板
	queryService.getRptDefault(type, function(def) {
				if (def != null) {
					modelBox.bindValue(def.rptfilePath);
				}
			});

	// 验证
	var check = function() {
		if (!cfg.pId) {
			var grid = Ext.getCmp(cfg.type + 'Grid');
			var list = grid.getSelectionModel().getSelections();
			if (list.length == 0) {
				Ext.MessageBox.alert("提示消息", "请选择一条记录");
				return false;
			} else if (list.length > 1) {
				Ext.MessageBox.alert("提示消息", "只能选择一条记录!")
				return false;
			}
			mail.pId = list[0].id;
			// 报价
			if (cfg.type == 'price') {
				custId = list[0].data.custId;
				mail.pNo = list[0].data.priceNo;
				status = list[0].data.priceStatus;
				querySql = "&priceId=" + list[0].id + "&priceNo="
						+ list[0].data.priceNo;
			}
			// 订单
			if (cfg.type == 'order') {
				status = list[0].data.orderStatus;
				custId = list[0].data.custId;
				mail.pNo = list[0].data.orderNo;
				querySql = "&orderId=" + list[0].id + "&orderNo="
						+ list[0].data.orderNo;
			}
			// 送样
			if (cfg.type == 'given') {
				custId = list[0].data.custId;
				mail.pNo = list[0].data.givenNo;
				status = list[0].data.givenIscheck;
				querySql = "&givenId=" + list[0].id + "&givenNo="
						+ list[0].data.givenNo + "&custName="
						+ list[0].data.customerShortName
			}
			// 出货
			if (cfg.type == 'orderout') {
				custId = list[0].data.custId;
				mail.pNo = list[0].data.orderNo;
				status = list[0].data.orderStatus;
				querySql = "&orderId=" + list[0].id + "&orderOutNo="
						+ list[0].data.orderNo;
			}
			// 配件采购
			if (cfg.type == 'fitting') {
				factoryId = list[0].data.factoryId;
				mail.pNo = list[0].data.fittingOrderNo;
				status = list[0].data.orderStatus;
				querySql = "&flag=FitOrder&fitorderId=" + list[0].id
						+ "&fittingNo=" + list[0].data.fittingOrderNo;
			}
			// 生产合同
			if (cfg.type == 'orderfac') {
				factoryId = list[0].data.factoryId;
				mail.pNo = list[0].data.orderNo;
				status = list[0].data.orderStatus;
				querySql = "&orderId=" + list[0].id + "&orderFacNo="
						+ list[0].data.orderNo;
			}
			// 包材采购
			if (cfg.type == 'packing') {
				factoryId = list[0].data.factoryId;
				mail.pNo = list[0].data.packingOrderNo;
				status = list[0].data.orderStatus;
				querySql = "&flag=PackOrder&packorderId=" + list[0].id
						+ "&packingNo=" + list[0].data.packingOrderNo;
			}
			// 排载
			if (cfg.type == 'split') {
				querySql = "&splitId=" + list[0].id;
			}
		}else{
			var pId = $(cfg.pId).value;
			var pNo = $(cfg.pNo).value;
			var mailSendId = $(cfg.mailSendId).value;
			status = $(cfg.status).value;
			mail.pId = pId;
			mail.pNo = pNo;
			// 报价
			if (cfg.type == 'price') {
				custId = mailSendId;
				querySql = "&priceId=" + pId + "&priceNo=" + pNo;
			}
			// 订单
			if (cfg.type == 'order') {
				custId = mailSendId;
				querySql = "&orderId=" + pId + "&orderNo=" + pNo;
			}
			// 送样
			if (cfg.type == 'given') {
				custId = mailSendId;
				querySql = "&givenId=" + pId + "&givenNo=" + pNo+"&custName="+cfg.custName;
			}
			// 出货
			if (cfg.type == 'orderout') {
				custId = mailSendId;
				querySql = "&orderId=" + pId + "&orderOutNo=" + pNo;
			}
			// 配件采购
			if (cfg.type == 'fitting') {
				factoryId = mailSendId;
				querySql = "&flag=FitOrder&fitorderId=" + pId
						+ "&fittingNo=" + pNo;
			}
			// 生产合同
			if (cfg.type == 'orderfac') {
				factoryId = mailSendId;
				querySql = "&orderId=" + pId + "&orderFacNo=" + pNo;
			}
			// 包材采购
			if (cfg.type == 'packing') {
				factoryId = mailSendId;
				querySql = "&flag=PackOrder&packorderId=" + pId
						+ "&packingNo=" + pNo;
			}
			// 排载
			if (cfg.type == 'split') {
				querySql = "&splitId=" + pId;
			}
		}

		return true;
	}

	// 导出
	var exportRpt = function() {
		if (!check()) {
			return;
		}

		// 如果没有审核通过不让导出
		if (status != 9 && status != 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert("提示消息", '对不起,该单还没审核通过,不能导出!');
			return false;
		}

		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple == '') {
			Ext.MessageBox.alert("提示信息", '请选择导出模板!');
			return;
		}
		var headerFlag = true;
		// 不分页显示
//		if (formData.radioGroup.inputValue == "0") {
//			headerFlag = false
//		}
//		var exlSheet = false;
//		var exlSheetObj = Ext.getCmp('exlSheet');
//		if (exlSheetObj.checked == true) {
//			exlSheet = true;
//		}
		var rptUrl = exlAction + "?reportTemple=" + formData.reportTemple
				+ "&printType=" + formData.printType + "&headerFlag="
				+ headerFlag  + querySql;

		
		var formSD = document.getElementById("printForm");
		formSD.action = rptUrl;
		//打开新页面  
		formSD.target = "new"; 
		formSD.method = "post";
		formSD.submit();
	}
	if (!cfg.exportRptFn)
		cfg.exportRptFn = exportRpt;

	// 默认的预览action
	if (!cfg.viewAction) {
		cfg.viewAction = "./newpreviewrpt.do";
	}

	// 预览方法
	var viewRpt = function() {
		if (!check()) {
			return;
		}

		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple == '') {
			Ext.MessageBox.alert("提示信息", '请选择导出模板!');
			return;
		}

		// 审核通过printFlag为1 其他为0,用于隐藏打印预览页面的"打印"按钮
		var printFlag = 0;
		if (status == 9 || status == 2 || loginEmpId == "admin") {
			printFlag = 1;
		}
		var headerFlag = true;
//		if (formData.radioGroup.inputValue == "0") {
//			headerFlag = false
//		}
		var url = cfg.viewAction + "?method=queryRpt&reportTemple="
				+ formData.reportTemple + "&headerFlag=" + headerFlag
				+ querySql + "&printFlag=" + printFlag;

		var formSD = document.getElementById("printForm");
		formSD.action = url;
		//打开新页面  
		formSD.target = "new"; 
		formSD.method = "post";
		formSD.submit();
		//openFullWindow(url);
	}
	if (!cfg.viewRptFn)
		cfg.viewRptFn = viewRpt;

	// 发邮件
	var sendMail = function() {
		if (!check()) {
			return;
		}

		// 如果没有审核通过不让发邮件
		if (status != 9 && status != 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert("提示消息", '对不起,该单还没审核通过,不能发邮件!');
			return false;
		}

		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple == '') {
			Ext.MessageBox.alert("提示信息", '请选择导出模板!');
			return;
		}

		if (custId == 0 && factoryId == 0) {
			Ext.MessageBox.alert("提示信息", '该单没有客户(厂家),不能发送邮件!');
			return;
		}
		mail.reportTemple = formData.reportTemple;
		mail.printType = formData.printType;

		var headerFlag = true;
		// 不分页显示
//		if (formData.radioGroup.inputValue == "0") {
//			headerFlag = false
//		}
		mail.headerFlag = headerFlag;
		var exlSheet = false;
		var exlSheetObj = Ext.getCmp('exlSheet');
		if (exlSheetObj.checked == true) {
			exlSheet = true;
		}
		mail.exlSheet = exlSheet;

		var str = "";
		if (custId != 0) {
			DWREngine.setAsync(false);
			// 判断客户是否有邮箱
			queryService.getCustById(custId, function(res) {
						str = res.customerEmail;
						mail.custId = res.id;
						mail.custName = res.customerShortName;
					});
			DWREngine.setAsync(true);
			if (str == null || str == '') {
				Ext.MessageBox.alert("提示信息", '该客户还没有邮箱,请先去客户管理中添加!');
				return;
			}
		} else {
			DWREngine.setAsync(false);
			// 判断厂家是否有邮箱
			queryService.getFactoryById(factoryId, function(res) {
						str = res.factoryEmail;
						mail.custId = res.id;
						mail.custName = res.shortName;
					});
			DWREngine.setAsync(true);
			if (str == null || str == '') {
				Ext.MessageBox.alert("提示信息", '该厂家还没有邮箱,请先去厂家管理中添加!');
				return;
			}
		}
		mail.custEmail = str;

		mask('邮件配置中,请稍候......');
		var task = new Ext.util.DelayedTask(function() {
					DWREngine.setAsync(false);
					queryService.saveMail(mail, function(res) {
								unmask();
								if (res == "") {
									Ext.MessageBox.alert("提示信息",
											'邮件配置失败,请联系管理员!');
								} else {
									openWindowBase(535, 800,
											'cotmailsend.do?method=query&excelKey='
													+ res);
								}
							});
					DWREngine.setAsync(true);
				});
		task.delay(500);
	}
	if (!cfg.sendMail)
		cfg.sendMail = sendMail;
		
var formPanel=new Ext.form.FormPanel({
	labelWidth:60,
	labelAlign:"right",
	formId:"printForm",
	layout:"form",
	width:475,
	height:300,
	padding:"5px",
	frame:true,
	items:[
		{
			layout:"column",
			width:464,
			height:291,
			items:[
				{
					layout:"form",
					columnWidth:0.5,
					items:[modelBox]
				},
				{
					layout:"form",
					columnWidth:0.5,
					items:[typeBox]
				},
				{
					layout:"column",
					columnWidth:0.5,
					items:[
						{
							layout:"form",
							columnWidth:0.8,
							labelWidth:100,
							items:[
								{
									xtype:"textfield",
									fieldLabel:"每页打印货号数",
									anchor:"100%",
									name:"eleNum",
									id:"eleNum"
								}
							]
						},
						{
							columnWidth:1,
							layout:"form",
							labelWidth:10,
							items:[
								{
									xtype:"checkbox",
									name:"eleDesc",
									id:"eleDesc",
									boxLabel:"货物描述部分为附页",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"insure",
									id:"insure",
									boxLabel:"打印保险类别",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"bank",
									id:"bank",
									boxLabel:"打印银行资料",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"Sum",
									id:"Sum",
									boxLabel:"打印合计数",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"handlingFee",
									id:"handlingFee",
									boxLabel:"额外费用",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"isTran",
									id:"isTran",
									boxLabel:"允许分批装运或转运",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"clauseType",
									id:"clauseType",
									boxLabel:"付款条款",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"isGiven",
									id:"isGiven",
									boxLabel:"是否寄样",
									anchor:"100%",
									labelSeparator:" "
								}
							]
						}
					]
				},
				{
					columnWidth:0.5,
					layout:"column",
					padding:"10",
					items:[
						{
							xtype:"fieldset",
							title:"输出顺序排序",
							layout:"form",
							columnWidth:1,
							labelWidth:5,
							items:[
								{
									xtype:"checkbox",
									name:"orderByOutEle",
									id:"orderByOutEle",
									boxLabel:"外商货号",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"orderByInEle",
									id:"orderByInEle",
									boxLabel:"我司货号",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"orderByEleType",
									id:"orderByEleType",
									boxLabel:"产品种类",
									anchor:"100%",
									labelSeparator:" "
								}
							]
						},
						{
							xtype:"fieldset",
							title:"输出货号选择",
							layout:"form",
							labelWidth:5,
							columnWidth:1,
							items:[
								{
									xtype:"checkbox",
									name:"outEle",
									id:"outEle",
									boxLabel:"外商货号",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"inEle",
									id:"inEle",
									boxLabel:"我司货号",
									anchor:"100%",
									labelSeparator:" "
								},
								{
									xtype:"checkbox",
									name:"facEle",
									id:"facEle",
									boxLabel:"供商货号",
									anchor:"100%",
									labelSeparator:" "
								}
							]
						},
						{
							xtype:"fieldset",
							title:"唛头信息",
							layout:"column",
							padding:"5",
							columnWidth:1,
							items:[
								{
									columnWidth:0.5,
									layout:"form",
									labelWidth:1,
									items:[
										{
											xtype:"checkbox",
											name:"mHeadText",
											id:"mHeadText",
											boxLabel:"文本唛头",
											anchor:"100%",
											labelSeparator:" "
										}
									]
								},
								{
									layout:"form",
									columnWidth:0.5,
									labelWidth:1,
									items:[
										{
											xtype:"checkbox",
											name:"mHeadPic",
											id:"mHeadPic",
											boxLabel:"图片唛头",
											anchor:"100%",
											labelSeparator:" "
										}
									]
								}
							]
						}
					]
				}
			]
		}
	]
})

	// 表单
	var con = {
		title : "打印及导出",
		buttonAlign : 'right',
		labelAlign : "right",
		layout : 'fit',
		width : 500,
		closeAction : 'hide',
		autoHeight : true,
		frame : true,
		items : [formPanel],
		buttonAlign : 'center',
		buttons : [{
					id : "export",
					text : '导出',
					scope : this,
					width : 65,
					iconCls : "page_excel",
					handler : cfg.exportRptFn
				}, {
					id : "preview",
					text : '打印预览',
					scope : this,
					width : 80,
					iconCls : "page_print",
					handler : cfg.viewRptFn
				}, {
					id : "mail",
					text : '发邮件',
					scope : this,
					width : 70,
					iconCls : "email_go",
					handler : cfg.sendMail
				}]

	};
	Ext.apply(con, cfg);
	PrintWinSD.superclass.constructor.call(this, con);
};
Ext.extend(PrintWinSD, Ext.Window, {});