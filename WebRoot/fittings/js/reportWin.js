// 成功条数
var susNum;
// 失败条数
var failNum;

// 重新上传
function reportload(errorNum, rowNum) {
	var fp = Ext.getCmp('uploadFormK');
	if (fp.getForm().isValid()) {
		fp.getForm().submit({
					url : './uploadExl.action',
					method : 'get',
					waitTitle : '请等待',
					waitMsg : '上传Excel中...',
					success : function(fp, o) {
						var picFile = Ext.util.Format.htmlDecode(o.result.fileName);
						// 上传成功并删除文件
						doActionAgain(picFile, errorNum, rowNum);
					},
					failure : function(fp, o) {
						Ext.MessageBox.alert("提示信息", o.result.msg);
					}
				});
	}
}

function doActionAgain(filename, errorNum, rowNum) {
	// 获得上传策略
	var isCover = $('isCoverChx').checked;
	var showErrorDiv = 'err' + errorNum;
	cotFittingsService.updateOneReport(filename, rowNum, isCover, function(
					msgList) {
				if (msgList == null) {
					$(showErrorDiv).innerHTML = "文件没找到或该文件不是excel！";
				} else {
					if (msgList.length == 1) {
						$(showErrorDiv).innerHTML = "<font color=green>第&nbsp;("
								+ (rowNum + 1) + ")&nbsp行，上传成功</font>";
						susNum++;
						failNum--;
						// 设置导出结果
						$('successNumLab').innerText = susNum;
						$('failNumLab').innerText = failNum;
						// 如果是覆盖,要增加覆盖条数
						if (isCover == true) {
							var cov = $('coverNumLab').innerText;
							cov++;
							$('coverNumLab').innerText = cov;
						}
						reloadGrid('fitGrid');
					} else if (msgList.length == 0) {
						$(showErrorDiv).innerHTML = "<font color=green>删除该行成功</font>";
						failNum--;
						// 设置导出结果
						$('failNumLab').innerText = failNum;
					} else {
						$(showErrorDiv).innerHTML = "<div id=err" + errorNum
								+ ">第&nbsp(" + (msgList[0].rowNum + 1)
								+ ")&nbsp行，第&nbsp(" + (msgList[0].colNum + 1)
								+ ")&nbsp列的错误信息：" + msgList[0].msg
								+ "&nbsp;&nbsp;<a href=# onclick=reportload(" + errorNum
								+ "," + msgList[0].rowNum + ")>【重新上传】</a></div>";
					}
				}
			});
}

// 报价记录表格导入报价单
ReportWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var fp = new Ext.FormPanel({
		id : 'uploadFormK',
		fileUpload : true,
		frame : true,
		padding : 5,
		region : 'center',
		margins : '0 5 0 0',
		border : false,
		labelWidth : 90,
		defaults : {
			allowBlank : false,
			labelAlign : "right",
			msgTarget : 'side'
		},
		items : [{
			style : {
				marginBottom : '20px'
			},
			html : '<div style="margin-top: 10px;"><input type="checkbox" value="" id="isCoverChx" /><label style="margin-left: 10px;margin-top: -5px;">覆盖原有记录</label></div>'
		}, {
			xtype : 'fileuploadfield',
			emptyText : '',
			anchor : '90%',
			fieldLabel : '选择Excel文件',
			id : 'picFile',
			name : 'picFile',
			buttonText : '',
			buttonCfg : {
				iconCls : 'upload-icon'
			}
		}, {
			style : 'padding-left: 40',
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
						width : 50,
						baseCls : 'x-plain'
					}, {
						layout : 'form',
						width : 100,
						labelWidth : 60,
						baseCls : 'x-plain',
						items : [{
							text : '导入',
							cls : "SYSOP_ADD",
							width : 65,
							iconCls : "page_mod",
							xtype : 'button',
							handler : function() {
								if (fp.getForm().isValid()) {
									fp.getForm().submit({
										url : './upload.action',
										method : 'get',
										waitTitle : '请等待',
										waitMsg : '上传Excel中...',
										success : function(fp, o) {
											var picFile = Ext.util.Format.htmlDecode(o.result.fileName);
											// 上传成功并删除文件
											doAction(picFile);
										},
										failure : function(fp, o) {
											Ext.MessageBox.alert("提示信息",
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
									text : '重置',
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
			html : '<div style="margin-top: 10px;"><font color="red">'
					+ '导入说明:</font><ul style="margin-top: 10px;">'
					+ '<li style="margin-left: 10px;margin-top: 5px;">1、<label>导入的文件必须是Excel（.xls），文件小于100M</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">2、<label>导入数据的第一列必须为配件编号</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">3、<label>配件编号、配件名称、供应商、采购价格、采购单位、领用单位、换算率不能为空</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">4、<label>一次最多导2000条数据，只取第一个Sheet数据</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 10px;"> <a href="./downloadFitTpl.action" style="cursor: hand;text-decoration: underline;color:blue;">【点击下载配件模板文件】</a>&nbsp;'
					+ '</li>' + '</ul>' + '</div>'
		}]
	});

	// 上传后的信息反馈
	var infoPanel = new Ext.Panel({
				region : 'east',
				width : '60%',
				frame : true,
				title : '上传结果'
			});

	// 加载excel内存数据
	function loadExcelSession() {
		//cfg.bar.changeData();
		reloadGrid('fitGrid');
		_self.close();
	}

	function doAction(filename) {
		mask("正在加载数据中,请稍候... ...");
		var isCover = $('isCoverChx').checked;
		cotFittingsService.saveReport(filename, isCover, function(msgList) {
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
						loadExcelSession();
					}
				} else {
					var htm = '<div class="detail_nav"><label>错误信息(修改excel文件后重新上传)</label></div><div style="height: 295px; overflow: auto;">';
					var temp = '';
					for (var i = 0; i < msgList.length; i++) {
						if (i != msgList.length - 1) {
							htm += "<div id=err" + i + ">第&nbsp("
									+ (msgList[i].rowNum + 1)
									+ ")&nbsp行，第&nbsp("
									+ (msgList[i].colNum + 1) + ")&nbsp列的错误信息："
									+ msgList[i].msg
									+ "&nbsp;&nbsp;<a href=# onclick=reportload(" + i + ","
									+ msgList[i].rowNum
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
						}
					}
					infoPanel.body.dom.innerHTML = temp + '</div>' + htm;
				}
			}
			unmask();
		});
	}

	// 表单
	var con = {
		title : '配件excel导入',
		layout : 'border',
		width : 700,
		height : 400,
		modal : true,
		items : [fp, infoPanel]
	};

	Ext.apply(con, cfg);
	ReportWin.superclass.constructor.call(this, con);
};
Ext.extend(ReportWin, Ext.Window, {});
