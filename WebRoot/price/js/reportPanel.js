//下载模板页面
function downTpl(){
	downRpt("./downloadOrderTpl.action");
}
// 打开自定义模板页面
function openDefine() {
	openWindowBase(580, 800, "sample/report/defineReport.jsp");
}

//成功条数
var susNum;
//失败条数
var failNum;

// 重新上传
function reportload(errorNum, rowNum) {
	var fp = Ext.getCmp('uploadFormK');
	if (fp.getForm().isValid()) {
		fp.getForm().submit({
					url : './uploadExl.action',
					method : 'get',
					waitTitle : 'Pls. wait',
					waitMsg : 'From Excel...',
					success : function(fp, o) {
						var picFile = Ext.util.Format.htmlDecode(o.result.fileName);
						// 上传成功并删除文件
						doActionAgain(picFile, errorNum, rowNum);
					},
					failure : function(fp, o) {
						Ext.MessageBox.alert("Message", o.result.msg);
					}
				});
	}
}

function doActionAgain(filename, errorNum, rowNum) {
	// 获得上传策略
	var isCover = $('isCoverChx').checked;
	var showErrorDiv = 'err' + errorNum;
	var pId = $("pId").value;
	cotPriceService.updateOneReport(filename, rowNum, pId, isCover, $('currencyId').value, $('excelFlag').checked, function(
					msgList) {
				if (msgList == null) {
					$(showErrorDiv).innerHTML = "File not found or the file is not excel！";
				} else {
					if (msgList.length == 1) {
						$(showErrorDiv).innerHTML = "<font color=green>第&nbsp;("
								+ (rowNum + 1) + ")&nbsp行，Upload successful</font>";
						susNum++;
						failNum--;
						// 设置导出结果
						$('successNumLab').innerText = susNum;
						$('failNumLab').innerText = failNum;
						
						//刷新
						insertSelectExcel($('excelFlag').checked);
						
					} else if (msgList.length == 0) {
						$(showErrorDiv).innerHTML = "<font color=green>Successfully remove the line</font>";
						failNum--;
						// 设置导出结果
						$('failNumLab').innerText = failNum;
					} else {
						$(showErrorDiv).innerHTML = "<div id=err" + errorNum
								+ ">第&nbsp(" + (msgList[0].rowNum + 1)
								+ ")&nbsp行，第&nbsp(" + (msgList[0].colNum + 1)
								+ ")&nbspColumn Error Message：" + msgList[0].msg
								+ "<a href=# onclick=reportload(" + errorNum
								+ "," + msgList[0].rowNum
								+ ")>【Re-upload】</a></div>";
					}
				}
			});
}

// 报价记录表格导入报价单
ReportPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var fp = new Ext.FormPanel({
		id : 'uploadFormK',
		fileUpload : true,
		width : 500,
		//frame : true,
		padding : 5,
		region : 'center',
		margins : '0 5 0 0',
		border : false,
		cls:'rightBorder',
		baseCls : 'x-plain',
		labelWidth : 90,
		defaults : {
			allowBlank : false,
			labelAlign : "right",
			msgTarget : 'side'
		},
		items : [{
					xtype : 'fileuploadfield',
					anchor : '93%',
					fieldLabel : 'Excel',
					id : 'picFile',
					name : 'picFile',
					buttonText : '',
					buttonCfg : {
						iconCls : 'upload-icon'
					}
				}, {
					style : 'padding-left: 80',
					baseCls : 'x-plain',
					layout : 'table',
					style : {
						marginTop : '20px'
					},
					layoutConfig : {
						columns : 4
					},
					defaults : {
						frame : true,
						width : 100
					},
					items : [{
								width : 40,
								baseCls : 'x-plain'
							}, {
								layout : 'form',
								width : 100,
								labelWidth : 60,
								baseCls : 'x-plain',
								items : [{
									text : 'Import',
									cls : "SYSOP_ADD",
									width : 65,
									iconCls : "page_mod",
									xtype : 'button',
									handler : function() {
										if (fp.getForm().isValid()) {
											fp.getForm().submit({
												url : './uploadExl.action',
												method : 'get',
												waitTitle : 'Pls. wait',
												waitMsg : 'Upload Excel ...',
												success : function(fp, o) {
													var picFile = Ext.util.Format.htmlDecode(o.result.fileName);
													// 上传成功并删除文件
													doAction(picFile);
												},
												failure : function(fp, o) {
													Ext.MessageBox.alert(
															"Message",
															o.result.msg);
												}
											});
										}
									}
								}]
							}, {
								layout : 'form',
								width : 100,
								baseCls : 'x-plain',
								items : [{
											text : 'Reset',
											width : 65,
											iconCls : "page_reset",
											xtype : 'button',
											handler : function() {
												fp.getForm().reset();
											}
										}]
							}]
				}, {
					style : 'padding-left: 10',
					baseCls : 'x-plain',
					html : '<div style="margin-top: 10px;"><font color="red">'
							+ 'Import explains:</font><ul style="margin-top: 10px;">'
							+ '<li style="margin-left: 10px;margin-top: 5px;">1、<input type="checkbox" value="" id="isCoverChx" checked="checked"/><label style="margin-left: 5px;">Whether the presence of the same NO coverage</label></li>'
							+ '<li style="margin-left: 10px;margin-top: 5px;">2、<input type="checkbox" id="excelFlag"/><label style="margin-left: 5px;">Check the configuration will be calculated quoted price, do not choose to take excel when the foreign offe</label></li>'
							+ '<li style="margin-left: 10px;margin-top: 5px;">3、<label>Import data file must be Excel (. Xls), uploaded files can not exceed 100M</label></li>'
							+ '<li style="margin-left: 10px;margin-top: 5px;">4、<label>The first column must be NO, must be completed, display a data line, can not change the line</label></li>'
							+ '<li style="margin-left: 10px;margin-top: 5px;">5、<label>Product specifications and quotations must be a number</label></li>'
							+ '<li style="margin-left: 10px;margin-top: 5px;">6、<label><font color=red>When some of the key parameters are not filled, it will be automatically populated with the default configuration to take samples</font></label></li>'
							+ '<li style="margin-left: 10px;margin-top: 5px;">7、<label>Import a maximum of 300 data, just take the first Excel file worksheet data</label></li>'
							+ '<li style="margin-left: 10px;margin-top: 10px;"> <a href=# onclick="javascript:downTpl()" style="cursor: hand;text-decoration: underline;color:blue;">【Product template】</a>&nbsp;'
							+ '<a href=# onclick="javascript:openDefine()" style="cursor: hand;text-decoration: underline;color:blue;">【Customize template】</a></li>'
							+ '</ul>' + '</div>'
				}]
	});

	// 上传后的信息反馈
	var infoPanel = new Ext.Panel({
				id : 'infoPanel',
				region : 'east',
				width : '60%',
				//frame : true,
				border : false,
				cls:'leftBorder',
				ctCls:'x-panel-mc',
				title : 'Upload Results'
			});

	function doAction(filename) {
		mask("Loading... ...");
		var isCover = $('isCoverChx').checked;
		
		//先保存订单,如果明细表格有变更也保存
		cfg.bar.saveByExcel($('excelFlag').checked,filename,isCover);
	}

	// 表单
	var con = {
		layout : 'border',
		width : 600,
		height : 490,
		border : false,
		items : [fp, infoPanel]
	};

	Ext.apply(con, cfg);
	ReportPanel.superclass.constructor.call(this, con);
};
Ext.extend(ReportPanel, Ext.Panel, {});
