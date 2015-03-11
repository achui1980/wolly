PrintWinSign = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	// 打印类型
	var printType2 = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['XLS', 'XLS'], ['PDF', 'PDF']]
			});
	var typeBox = new Ext.form.ComboBox({
				name : 'printType2',
				id : 'printType2',
				xtype : 'combo',
				fieldLabel : "导出类型",
				editable : false,
				store : printType2,
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
	var exlAction = "./downGiven.action";
	// 邮件接收人
	var custId = 0;
	var factoryId = 0;
	var mail = {};
	mail.type = cfg.type;
	
	//征样
	if (cfg.type == 'sign') {
		exlAction = "./downSign.action";
		type = 3;
	}
	// 查询参数
	var querySql = "";
	var tplUrl = "./servlet/DataSelvert?tbname=CotRptFile&key=rptName&typeName=rptType&type="
			+ type;

	// 导出模板
	var modelBox = new BindCombox({
				dataUrl : tplUrl,
				cmpId : 'reportTemple2',
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

	//验证
	var check=function(){
		if (!cfg.pId) {
			var grid = Ext.getCmp(cfg.type+'Grid');
			var list = grid.getSelectionModel().getSelections();
			if (list.length == 0) {
				Ext.MessageBox.alert("提示消息", "请选择一条记录");
				return false;
			} else if (list.length > 1) {
				Ext.MessageBox.alert("提示消息", "只能选择一条记录!")
				return false;
			}
			mail.pId = list[0].data.givenId;
			// 征样
			if (cfg.type == 'sign') {
				factoryId = list[0].data.factoryId;
				mail.pNo = list[0].data.signNo;
				querySql = "&gId=" + list[0].data.givenId+ "&signNo="
						+ list[0].data.signNo+"&factoryId="+factoryId;
			}
		}else{
			var pId = $(cfg.pId).value;
			var pNo = $(cfg.pNo).value;
			var mailSendId = $(cfg.mailSendId).value;
			mail.pId = pId;
			mail.pNo = pNo;
			if (cfg.type == 'sign') {
				factoryId = mailSendId;
				querySql = "&gId=" + pId+"&signNo=" + pNo+"&factoryId="+factoryId;
			}
		}
		return true;
	}

	// 导出
	var exportRpt = function() {
		if(!check()){
			return;
		}
		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple2 == '') {
			Ext.MessageBox.alert("提示信息", '请选择导出模板!');
			return;
		}
		var headerFlag = true;
		// 不分页显示
		if (formData.radioGroup2.inputValue == "0") {
			headerFlag = false
		}
		var exlSheet = false;
		var exlSheetObj = Ext.getCmp('exlSheet2');
		if (exlSheetObj.checked == true) {
			exlSheet = true;
		}

		var rptUrl = exlAction + "?reportTemple=" + formData.reportTemple2
				+ "&printType=" + formData.printType2 + "&headerFlag="
				+ headerFlag + "&exlSheet=" + exlSheet + querySql;
		// 下载不打开新页面
		downRpt(rptUrl);
	}
	if (!cfg.exportRptFn)
		cfg.exportRptFn = exportRpt;

	// 默认的预览action
	if (!cfg.viewAction) {
		cfg.viewAction = "./previewrpt.do";
	}

	// 预览方法
	var viewRpt = function() {
		if(!check()){
			return;
		}

		var formData = getFormValues(formPanel, true);
		if (formData.reportTemple2 == '') {
			Ext.MessageBox.alert("提示信息", '请选择导出模板!');
			return;
		}
		var headerFlag = true;
		if (formData.radioGroup2.inputValue == "0") {
			headerFlag = false
		}
		var _height = window.screen.height;
		var _width = window.screen.width;
		var url = cfg.viewAction + "?method=queryEleRpt&reportTemple="
				+ formData.reportTemple2 + "&headerFlag=" + headerFlag
				+ querySql;
		openFullWindow(url);
	}
	if (!cfg.viewRptFn)
		cfg.viewRptFn = viewRpt;
		
	// 发邮件
	var sendMail = function() {
		if (!check()) {
			return;
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
		mail.reportTemple = formData.reportTemple2;
		mail.printType = formData.printType2;

		var headerFlag = true;
		// 不分页显示
		if (formData.radioGroup2.inputValue == "0") {
			headerFlag = false
		}
		mail.headerFlag = headerFlag;
		var exlSheet = false;
		var exlSheetObj = Ext.getCmp('exlSheet2');
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
							id : "radioGroup2",
							items : [{
										boxLabel : '不分页显示',
										inputValue : 0,
										name : 'showHeader2',
										id : "singlePage2"
									}, {
										boxLabel : '分页显示',
										id : "allPage2",
										inputValue : 1,
										name : 'showHeader2',
										checked : true
									}]
						}, {
							xtype : 'checkboxgroup',
							fieldLabel : "",
							hideLabel : true,
							items : [{
										xtype : 'checkbox',
										boxLabel : '分Sheet导出',
										id : 'exlSheet2',
										name : 'exlSheet2'
									}]
						}, modelBox, typeBox]

			})

	// 表单
	var con = {
		title : "征样打印及导出",
		buttonAlign : 'right',
		labelAlign : "right",
		layout : 'fit',
		width : 250,
		closeAction : 'hide',
		autoHeight : true,
		frame : true,
		items : [formPanel],
		buttonAlign : 'center',
		buttons : [{
					text : '导出',
					width : 65,
					iconCls : "page_excel",
					handler : cfg.exportRptFn
				}, {
					text : '打印预览',
					width : 80,
					iconCls : "page_print",
					handler : cfg.viewRptFn
				}, {
					text : '发邮件',
					scope : this,
					width : 70,
					iconCls : "email_go",
					handler : cfg.sendMail
				}]

	};
	Ext.apply(con, cfg);
	PrintWinSign.superclass.constructor.call(this, con);
};
Ext.extend(PrintWinSign, Ext.Window, {});