// 打开自定义模板页面
function openDefine() {
	openWindowBase(580, 800, "sample/report/defineReport.jsp");
}

//成功条数
var susNum;
//失败条数
var failNum;

// 重新上传报表
function reportload(errorNum, rowNum) {
	var fp = Ext.getCmp('uploadFormK');
	if (fp.getForm().isValid()) {
		fp.getForm().submit({
					url : './uploadExl.action',
					method : 'get',
					waitTitle : 'Waiting',
					waitMsg : 'Uploading...',
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
function doActionAgain(filename,errorNum, rowNum) {
	// 获得上传策略
	var isCover = $('isCoverChx').checked;
	var showErrorDiv = 'err' + errorNum;
	cotReportService.updateOneReport(filename, rowNum, isCover, function(
					msgList) {
				if (msgList == null) {
					$(showErrorDiv).innerHTML = "File not found or the file is not excel！";
				} else {
					if (msgList.length == 1) {
						$(showErrorDiv).innerHTML = "<font color=green>Row&nbsp;("
								+ (rowNum + 1) + ")&nbsp，Success</font>";
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
						$(showErrorDiv).innerHTML = "<font color=green>Delete Success</font>";
						failNum--;
						// 设置导出结果
						$('failNumLab').innerText = failNum;
					} else {
						$(showErrorDiv).innerHTML = "<div id=err" + errorNum
								+ ">Row&nbsp(" + (msgList[0].rowNum + 1)
								+ ")&nbsp，Column&nbsp(" + (msgList[0].colNum + 1)
								+ ")&nbsp Error：" + msgList[0].msg
								+ "&nbsp;&nbsp;&nbsp;<button onclick=reportload(" + errorNum
								+ "," + msgList[0].rowNum
								+ ")>Re-Upload</button></div>";
					}
				}
			});
}

Ext.onReady(function() {
	var fp = new Ext.FormPanel({
		id:'uploadFormK',
		fileUpload : true,
		width : 500,
		padding : 5,
		region : 'center',
		ctCls:'x-panel-mc',
		cls:'rightBorder',
		labelWidth : 90,
		defaults : {
			allowBlank : false,
			labelAlign : "right",
			msgTarget : 'side'
		},
		items : [{
					xtype : 'checkbox',
					anchor : '80%',
					boxLabel : 'Covering  Existing Sample',
					id : 'isCoverChx',
					labelSeparator : " ",
					hideLabel : true,
					name : 'isCoverChx'
				}, {
					xtype : 'fileuploadfield',
					emptyText : '',
					anchor : '80%',
					fieldLabel : 'Excel file',
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
									text : 'Upload',
									cls : "SYSOP_ADD",
									width : 65,
									iconCls : "page_mod",
									xtype : 'button',
									handler : function() {
										if (fp.getForm().isValid()) {
											fp.getForm().submit({
												url : './uploadExl.action',
												method : 'get',
												waitTitle : 'Waiting',
												waitMsg : 'Uploading...',
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
					html : '<div style="margin-top: 10px;"><font color="red">'
							+ 'Import Help:</font><ul style="margin-top: 10px;">'
							+ '<li style="margin-left: 10px;"> Import data file must be Excel (. Xls), the file size can not exceed 100M。 </li>'
							+ '<li style="margin-left: 10px;"> The first column must be Art No., and can not be empty, display a data line。</li>'
							+ '<li style="margin-left: 10px;"> Import a maximum of 2000 data, just take the first Excel file worksheet data。</li>'
							+ '<li style="margin-left: 10px;margin-top: 10px;"> <a href="./downloadTpl.action" style="cursor: hand;text-decoration: underline;color:blue;">【Download Sample Template】</a>&nbsp;'
							+ '<a  href="javascript:openDefine()" style="cursor: hand;text-decoration: underline;color:blue;">【Customize Sample Template】</a></li>'
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
				title : 'Upload Results'
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [fp, infoPanel]
			});

	function doAction(filename) {
		mask("Loading... ...");
		var isCover = $('isCoverChx').checked;
		cotReportService.saveReport(filename, isCover, function(msgList) {
			if (msgList == null) {
				infoPanel.body.dom.innerHTML = "File not found or the file is not excel！";
			} else {
				if (msgList.length == 1) {
					if (msgList[0].flag == 1) {
						infoPanel.body.dom.innerHTML = "<font color=red>"
								+ msgList[0].msg + "</font>";
					} else {
						infoPanel.body.dom.innerHTML = "Success&nbsp(<label>"
								+ msgList[0].successNum
								+ "</label>)&nbsp，Failed&nbsp(<label>"
								+ msgList[0].failNum
								+ "</label>)&nbsp，Covered&nbsp(<label>"
								+ msgList[0].coverNum + "</label>)。";
					}
				} else {
					var htm = '<div class="detail_nav"><label>Error message (re-upload the modified excel file)</label></div><div style="height: 460px; overflow: auto;">';
					var temp = '';
					for (var i = 0; i < msgList.length; i++) {
						if (i != msgList.length - 1) {
							htm += "<div id=err" + i + ">Row&nbsp("
									+ (msgList[i].rowNum + 1)
									+ ")&nbsp，Column&nbsp("
									+ (msgList[i].colNum + 1) + ")&nbsp："
									+ msgList[i].msg
									+ "&nbsp;&nbsp;&nbsp;" +
									"<button onclick=reportload(" + i + ","
									+ msgList[i].rowNum
									+ ")>Reupload</button>" +
											"</div><br/>";
						} else {
							susNum = msgList[i].successNum;
							failNum = msgList[i].failNum;
							temp = 'Success&nbsp(<label id="successNumLab">'
									+ msgList[i].successNum
									+ "</label>)&nbsp，Failed&nbsp(<label id='failNumLab'>"
									+ msgList[i].failNum
									+ "</label>)&nbsp，Covered&nbsp(<label id='coverNumLab'>"
									+ msgList[i].coverNum + "</label>)。";
						}
					}
					infoPanel.body.dom.innerHTML = temp + '</div>' + htm;
				}
			}
			unmask();
		});
	}
});
