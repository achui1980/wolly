PrintWin = function(cfg) {
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
				fieldLabel : "File Type",
				editable : false,
				store : printType,
				valueField : "id",
				value : "PDF",
				displayField : "name",
				mode : 'local',
				anchor : '95%',
				triggerAction : 'all',
				selectOnFocus : false
			});

	// 报价
	var type = 0;
	var exlAction = "./downRpt.action";
	// 邮件接收人
	var custId = 0;
	var factoryId = 0;
	var contactId = 0;
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
		exlAction = "./downOrder.action";
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
		//sFac = false;
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
	
	// 出货作废
	if (cfg.type == 'orderoutdel') {
		type = 11;
		exlAction = "./downOrderOutDel.action";
	}
	
	// pi包装图片
	if (cfg.type == 'orderPic') {
		type = 12;
		exlAction = "./downOrderPic.action";
	}
	// po包装图片
	if (cfg.type == 'orderfacPic') {
		type = 13;
		exlAction = "./downOrderPic.action";
	}
	
	// 询盘
	if (cfg.type == 'pan') {
		type = 14;
		exlAction = "./downPan.action";
	}
	// 询盘
	if (cfg.type == 'panHd') {
		type = 16;
		exlAction = "./downPan.action";
	}
	
	// 厂家询盘
	if (cfg.type == 'prs') {
		type = 15;
		exlAction = "./downPrs.action";
	}
	// 厂家询盘
	if (cfg.type == 'prsHd') {
		type = 17;
		exlAction = "./downPrs.action";
	}
	
	// HHO询盘
	if (cfg.type == 'panHhotx') {
		type = 18;
		exlAction = "./downPan.action";
	}
	// HHO询盘
	if (cfg.type == 'panHhoHd') {
		type = 19;
		exlAction = "./downPan.action";
	}
	
	// HHO厂家询盘
	if (cfg.type == 'prsHho') {
		type = 20;
		exlAction = "./downPrs.action";
	}
	// HHO厂家询盘
	if (cfg.type == 'prsHhoHd') {
		type = 21;
		exlAction = "./downPrs.action";
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
				fieldLabel : "Template",
				editable : true,
				sendMethod : "post",
				valueField : "rptfilePath",
				displayField : "rptName",
				emptyText : 'Choose',
				anchor : "95%",
				triggerAction : 'all'
			});

	// 设置默认模板
	queryService.getRptDefault(type, function(def) {
				if (def != null) {
					modelBox.bindValue(def.rptfilePath);
				}
			});

	// 厂家
	this.facBox = new Ext.form.ComboBox({
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
				hiddenName : "printFacId",
				fieldLabel : "Factory",
				emptyText : "Choose",
				anchor : "95%",
				hidden : sFac,
				hideLabel : sFac
			});

	// 验证
	var check = function() {
		if (!cfg.pId) {
			var tb=cfg.type + 'Grid';
			if(cfg.type=='panHd'){
				tb="panGrid";
			}
			if(cfg.type=='prsHd'){
				tb="prsGrid";
			}
			var grid = Ext.getCmp(tb);
			var list = grid.getSelectionModel().getSelections();
			if (list.length == 0) {
				Ext.MessageBox.alert("INFO", "Please select a record!");
				return false;
			} else if (list.length > 1) {
				Ext.MessageBox.alert("INFO", "You must only one record!")
				return false;
			}
			if(cfg.type=='orderoutdel'){
				var rec=list[0].data.orderNo;
				if(rec==''){
					Ext.MessageBox.alert("Message", "Sorry,The Invoice do not have Invoice No.!")
					return false;
				}
			}
			
			mail.pId = list[0].id;
			// 报价
			if (cfg.type == 'price') {
				custId = list[0].data.custId;
				mail.pNo = list[0].data.priceNo;
				status = list[0].data.priceStatus;
				querySql = "&priceId=" + list[0].id + "&priceNo="
						+ encodeURIComponent(list[0].data.priceNo);
			}
			// 订单
			if (cfg.type == 'order') {
				status = list[0].data.orderStatus;
				custId = list[0].data.custId;
				mail.pNo = list[0].data.orderNo;
				querySql = "&orderId=" + list[0].id + "&orderNo="
						+ encodeURIComponent(list[0].data.orderNo)+"&custom=1";
			}
			// 送样
			if (cfg.type == 'given') {
				custId = list[0].data.custId;
				mail.pNo = list[0].data.givenNo;
				status = list[0].data.givenIscheck;
				querySql = "&givenId=" + list[0].id + "&givenNo="
						+ encodeURIComponent(list[0].data.givenNo) + "&custName="
						+ list[0].data.customerShortName
			}
			// 出货
			if (cfg.type == 'orderoutdel') {
				custId = list[0].data.custId;
				mail.pNo = list[0].data.orderNo;
				status = list[0].data.orderStatus;
				querySql = "&orderId=" + list[0].id + "&orderOutNo="
						+ encodeURIComponent(list[0].data.orderNo)+"&custom=4";
			}
			if (cfg.type == 'orderout') {
				custId = list[0].data.custId;
				mail.pNo = list[0].data.orderNo;
				status = list[0].data.orderStatus;
				querySql = "&orderId=" + list[0].id + "&orderOutNo="
						+ encodeURIComponent(list[0].data.orderNo)+"&custom=3";
			}
			// 配件采购
			if (cfg.type == 'fitting') {
				factoryId = list[0].data.factoryId;
				mail.pNo = list[0].data.fittingOrderNo;
				status = list[0].data.orderStatus;
				querySql = "&flag=FitOrder&fitorderId=" + list[0].id
						+ "&fittingNo=" + encodeURIComponent(list[0].data.fittingOrderNo);
			}
			// 生产合同
			if (cfg.type == 'orderfac') {
				factoryId = list[0].data.factoryId;
				mail.pNo = list[0].data.orderNo;
				status = list[0].data.orderStatus;
				querySql = "&orderId=" + list[0].id + "&orderFacNo="
						+ encodeURIComponent(list[0].data.orderNo)+"&custom=2";
			}
			// 包材采购
			if (cfg.type == 'packing') {
				factoryId = list[0].data.factoryId;
				mail.pNo = list[0].data.packingOrderNo;
				status = list[0].data.orderStatus;
				querySql = "&flag=PackOrder&packorderId=" + list[0].id
						+ "&packingNo=" + encodeURIComponent(list[0].data.packingOrderNo);
			}
			// 排载
			if (cfg.type == 'split') {
				querySql = "&splitId=" + list[0].id;
			}
			// 询盘
			if (cfg.type == 'pan' || cfg.type == 'panHd') {
				mail.pNo = list[0].data.priceNo;
				querySql = "&panId=" + list[0].id + "&priceNo="
						+ encodeURIComponent(list[0].data.priceNo);
			}
			// quotation
			if (cfg.type == 'prs' || cfg.type == 'prsHd') {
				mail.pNo = list[0].data.priceNo;
				querySql = "&panId=" + list[0].data.panId+"&facId=" + list[0].data.facId + "&priceNo="
						+ encodeURIComponent(list[0].data.priceNo);
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
				querySql = "&orderId=" + pId + "&orderNo=" + pNo+"&custom=1";
			}
			// 送样
			if (cfg.type == 'given') {
				custId = mailSendId;
				querySql = "&givenId=" + pId + "&givenNo=" + pNo+"&custName="+cfg.custName;
			}
			// 出货
			if (cfg.type == 'orderout') {
				custId = mailSendId;
				querySql = "&orderId=" + pId + "&orderOutNo=" + pNo+"&custom=3";
			}
			if (cfg.type == 'orderoutdel') {
				custId = mailSendId;
				querySql = "&orderId=" + pId + "&orderOutNo=" + pNo+"&custom=4";
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
				querySql = "&orderId=" + pId + "&orderFacNo=" + pNo+"&custom=2";
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
			
			// 包装图片
			if (cfg.type == 'orderPic' || cfg.type == 'orderfacPic') {
				querySql = "&orPicId=" + pId+ "&orderNo=" + parent.$('orderNo').value;
			}
			// 询盘
			if (cfg.type == 'pan' || cfg.type == 'panHd') {
				querySql = "&panId=" + pId+ "&priceNo=" + pNo;
			}
			// quation
			if (cfg.type == 'prs' || cfg.type == 'prsHd') {
				var facId = $(cfg.facId).value;
				querySql = "&panId=" + pId+"&facId=" + facId + "&priceNo=" + pNo;
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
//		if (status != 9 && status != 2 && loginEmpId != "admin") {
//			Ext.MessageBox.alert("INFO", 'Sorry, the single has not reviewed, can not export!');
//			return false;
//		}

		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple == '') {
			Ext.MessageBox.alert("INFO", 'Please select a Template!');
			return;
		}
		
		var headerFlag = true;
		// 不分页显示
		if (formData.radioGroup.inputValue == "0") {
			headerFlag = false
		}
		var exlSheet = false;
		var exlSheetObj = Ext.getCmp('exlSheet');
		if (exlSheetObj.checked == true) {
			exlSheet = true;
		}
		var rptUrl = exlAction + "?reportTemple=" + formData.reportTemple
				+ "&printType=" + formData.printType + "&headerFlag="
				+ headerFlag + "&exlSheet=" + exlSheet + querySql;

		if (formData.printFacId != "") {
			rptUrl += "&factoryId=" + formData.printFacId;
		}
		if(cfg.type=='orderfacPic'){
			rptUrl+='&facFlag=1';
		}
		// 下载不打开新页面
		downRpt(rptUrl);
//		if(formData.printType == "PDF"){
//			Ext.Msg.alert("信息提示","导出PDF完成")
//		}
	}
	if (!cfg.exportRptFn)
		cfg.exportRptFn = exportRpt;

	// 默认的预览action
	if (!cfg.viewAction) {
		cfg.viewAction = "./previewrpt.do";
	}

	// 预览方法
	var viewRpt = function() {
		if (!check()) {
			return;
		}
		
		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple == '') {
			Ext.MessageBox.alert("INFO", 'Please select a Template!');
			return;
		}
		
		// 审核通过printFlag为1 其他为0,用于隐藏打印预览页面的"打印"按钮
//		var printFlag = 0;
//		if (status == 9 || status == 2 || loginEmpId == "admin") {
//			printFlag = 1;
//		}
		var headerFlag = true;
		if (formData.radioGroup.inputValue == "0") {
			headerFlag = false
		}
		var _height = window.screen.height;
		var _width = window.screen.width;
		var url = cfg.viewAction + "?method=queryEleRpt&reportTemple="
				+ formData.reportTemple + "&headerFlag=" + headerFlag
				+ querySql + "&printFlag=1&isPreview=true";

		if (formData.printFacId != "") {
			url += "&factoryId=" + formData.printFacId;
		}
		openFullWindow(url);
	}
	if (!cfg.viewRptFn)
		cfg.viewRptFn = viewRpt;

	// 询盘发邮件
	var sendPanMail = function() {
		if (!check()) {
			return;
		}

		// 如果没有审核通过不让发邮件
		if (status != 9 && status != 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert("Message", '对不起,该单还没审核通过,不能发邮件!');
			return false;
		}

		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple == '') {
			Ext.MessageBox.alert("Message", '请选择导出模板!');
			return;
		}

		if (contactId == 0) {
			Ext.MessageBox.alert("Message", '该单没有联系人,不能发送邮件!');
			return;
		}

		var str = "";
		DWREngine.setAsync(false);
		// 判断联系人是否有邮箱
		queryService.getCotContactById(contactId, function(res) {
					str = res.contactEmail;
					mail.custId = res.id;
					mail.custName = res.contactPerson;
				});
		DWREngine.setAsync(true);
		if (str == null || str == '') {
			Ext.MessageBox.alert("Message", '该联系人还没有邮箱,请先去联系人管理中添加!');
			return;
		}
		mail.custEmail = str;
		mask('邮件配置中,请稍候......');
		var task = new Ext.util.DelayedTask(function() {
					DWREngine.setAsync(false);
					queryService.savePanMail(mail, function(res) {
								unmask();
								if (res == "") {
									Ext.MessageBox.alert("Message",
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
		
	// 询盘发邮件
	var sendMail = function() {
		if (!check()) {
			return;
		}
		// 如果没有审核通过不让发邮件
		if (status != 9 && status != 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert("Message", '对不起,该单还没审核通过,不能发邮件!');
			return false;
		}

		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple == '') {
			Ext.MessageBox.alert("Message", '请选择导出模板!');
			return;
		}

		if (custId == 0 && factoryId == 0) {
			Ext.MessageBox.alert("Message", '该单没有客户(厂家),不能发送邮件!');
			return;
		}
		mail.reportTemple = formData.reportTemple;
		mail.printType = formData.printType;

		var headerFlag = true;
		// 不分页显示
		if (formData.radioGroup.inputValue == "0") {
			headerFlag = false
		}
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
				Ext.MessageBox.alert("Message", '该客户还没有邮箱,请先去客户管理中添加!');
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
				Ext.MessageBox.alert("Message", '该厂家还没有邮箱,请先去厂家管理中添加!');
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
									Ext.MessageBox.alert("Message",
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
		
	if(type ==14){
		cfg.sendMail = sendPanMail;
	}

	var formPanel = new Ext.form.FormPanel({
				layout : 'form',
				labelWidth : 60,
				frame : true,
				border : false,
				autoHeight : true,
				items : [{
							xtype : 'radiogroup',
							fieldLabel : "",
							hideLabel : true,
							id : "radioGroup",
							items : [{
										boxLabel : 'No page preview',
										inputValue : 0,
										name : 'showHeader',
										id : "singlePage"
									}, {
										boxLabel : 'page preview',
										id : "allPage",
										inputValue : 1,
										name : 'showHeader',
										checked : true
									}]
						}, {
							xtype : 'checkboxgroup',
							fieldLabel : "",
							hideLabel : true,
							items : [{
										xtype : 'checkbox',
										boxLabel : 'Sheet',
										hidden:true,
										id : 'exlSheet',
										name : 'exlSheet'
									}]
						}, modelBox, typeBox, _self.facBox]

			})
	var hide='hide';
	if(!cfg.pId && (cfg.type == 'pan' || cfg.type == 'panHd' || cfg.type == 'prs' || cfg.type == 'prsHd')){
		hide='close';
	}

	// 表单
	var con = {
		title : "Print and export",
		buttonAlign : 'right',
		labelAlign : "right",
		layout : 'fit',
		width : 250,
		closeAction : hide,
		autoHeight : true,
		frame : true,
		items : [formPanel],
		buttonAlign : 'center',
		buttons : [{
					id : "export",
					text : 'Export',
					scope : this,
					hidden:(type==5 || type==8)?true:false,
					width : 65,
					iconCls : "page_excel",
					handler : cfg.exportRptFn
				}, {
					id : "preview",
					text : 'Preview',
					scope : this,
					width : 80,
					iconCls : "page_print",
					handler : cfg.viewRptFn
				}, {
					id : "mail",
					text : 'Email',
					hidden:type==2?false:true,
					scope : this,
					width : 70,
					iconCls : "email_go",
					handler : cfg.sendMail
				}]

	};
	Ext.apply(con, cfg);
	PrintWin.superclass.constructor.call(this, con);
};
Ext.extend(PrintWin, Ext.Window, {});