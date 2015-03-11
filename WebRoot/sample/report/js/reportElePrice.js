// 成功条数
var susNum;
// 失败条数
var failNum;
// 重新上传报表
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
	cotElePriceService.updateOneReport(filename, rowNum, isCover, function(
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
								+ "&nbsp;&nbsp;&nbsp;<button onclick=reportload(" + errorNum
								+ "," + msgList[0].rowNum
								+ ")>重新上传</button></div>";
					}
				}
			});
}

Ext.onReady(function() {
	var fp = new Ext.FormPanel({
		id : 'uploadFormK',
		fileUpload : true,
		width : 500,
		ctCls:'x-panel-mc',
		cls:'rightBorder',
		padding : 5,
		region : 'center',
		border : false,
		labelWidth : 90,
		// split:true,
		// region:'north',
		// buttonAlign : 'center',
		defaults : {
			allowBlank : false,
			labelAlign : "right",
			msgTarget : 'side'
		},
		items : [{
					xtype : 'checkbox',
					anchor : '80%',
					boxLabel : '覆盖现有货号成本',
					id : 'isCoverChx',
					labelSeparator : " ",
					hideLabel : true,
					name : 'isCoverChx'
				}, {
					xtype : 'fileuploadfield',
					emptyText : '',
					anchor : '80%',
					fieldLabel : '选择Excel文件',
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
						marginTop : '30px'
					},
					layoutConfig : {
						columns : 4
					},
					defaults : {
						frame : true,
						width : 100
					},
					items : [{
								width : 100,
								baseCls : 'x-plain'
							}, {
								layout : 'form',
								width : 100,
								labelWidth : 60,
								baseCls : 'x-plain',
								items : [{
									text : '上传',
									cls : "SYSOP_ADD",
									width : 65,
									iconCls : "page_mod",
									xtype : 'button',
									handler : function() {
										if (fp.getForm().isValid()) {
											fp.getForm().submit({
												url : './uploadExl.action',
												method : 'get',
												waitTitle : '请等待',
												waitMsg : '上传Excel中...',
												success : function(fp, o) {
													var picFile = Ext.util.Format.htmlDecode(o.result.fileName);
													// 上传成功并删除文件
													doAction(picFile);
												},
												failure : function(fp, o) {
													Ext.MessageBox.alert(
															"提示信息",
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
							+ '<li style="margin-left: 10px;"> 导入的数据文件必须是Excel（.xls），文件大小不能超过100M。 </li>'
							+ '<li style="margin-left: 10px;"> 只能导不存在子货号的样品成本。</li>'
							+ '<li style="margin-left: 10px;"> 样品成本的价格币种取默认配置中的厂家币种。</li>'
							+ '<li style="margin-left: 10px;"> 一次最多导入2000条数据，只取Excel文件的第一个工作表数据。</li>'
							+ '<li style="margin-left: 10px;margin-top: 10px;"> <a href="./downloadElePTpl.action" style="cursor: hand;text-decoration: underline;color:blue;">【点击下载样品成本模板】</a>&nbsp;'
							+ '</ul>' + '</div>'
				}]
	});

	// 上传后的信息反馈
	var infoPanel = new Ext.Panel({
				region : 'east',
				width : '60%',
				split:true,
				border:false,
				ctCls:'x-panel-mc',
				cls:'leftBorder',
				title : '上传结果'
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [fp, infoPanel]
			});

	function doAction(filename) {
		mask("正在加载数据中,请稍候... ...");
		var isCover = $('isCoverChx').checked;
		cotElePriceService.saveReport(filename, isCover, function(msgList) {
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
					}
				} else {
					var htm = '<div class="detail_nav"><label>错误信息(修改excel文件后重新上传)</label></div><div style="height: 460px; overflow: auto;">';
					var temp = '';
					for (var i = 0; i < msgList.length; i++) {
						if (i != msgList.length - 1) {
							htm += "<div id=err" + i + ">第&nbsp("
									+ (msgList[i].rowNum + 1)
									+ ")&nbsp行，第&nbsp("
									+ (msgList[i].colNum + 1) + ")&nbsp列的错误信息："
									+ msgList[i].msg
									+ "&nbsp;&nbsp;&nbsp;<button onclick=reportload(" + i + ","
									+ msgList[i].rowNum
									+ ")>重新上传</button></div><br/>";
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
});
