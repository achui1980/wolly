Ext.onReady(function() {
	var fp = new Ext.FormPanel({
		fileUpload : true,
		autoWidth : true,
		frame : true,
		padding : 5,
		region : 'center',
		margins : '0 5 0 0',
		border : false,
		labelWidth : 80,
		// contentEl:"uploadTable",
		renderTo : "uploadTable",
		// region:'north',
		// buttonAlign : 'center',
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
			emptyText : '请选择需要导入的Excel文件',
			anchor : '80%',
			fieldLabel : 'Excel文件',
			id : 'picFile',
			name : 'picFile',
			buttonText : '',
			buttonCfg : {
				iconCls : 'upload-icon'
			}
		}, {
			style : 'padding-left: 80',
			html : '<div style="margin-top: 10px;"><font color="red">'
					+ '友情提醒:</font><ul style="margin-top: 10px;">'
					+ '<li> 导入的数据文件必须是Excel（.xls），大小不超过100M </li>'
					+ '<li> 导入数据的第一列必须为配件编号</li>'
					+ '<li> 配件编号、材料名称、供应商、采购价格、采购单位、领用单位、换算率不能为空</li>'
					+ '<li> 一次最多能导2000条数据，只取Excel文件的第一个工作表数据</li>'
					+ '<li> <a href="./downloadFitTpl.action" style="cursor: hand;text-decoration: none;">【点击下载配件模板文件】</a></li>'
					+ '</ul>' + '</div>'
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
										url : './upload.action',
										method : 'get',
										waitTitle : '请等待',
										waitMsg : '上传Excel中...',
										success : function(fp, o) {
											//htmlDecode反转义特殊字符
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
		}]
	});

	// 上传后的信息反馈
	var infoPanel = new Ext.Panel({
				region : 'east',
				width : '40%',
				split : true,
				collapsible : true,
				frame : true,
				title : '上传结果',
				contentEl : 'msgTable'
			});
	function doAction(filename) {
		mask("正在加载数据中,请稍候... ...");
		var isCover = $('isCoverChx').checked;
		cotFittingsService.saveReport(filename, isCover, function(msgList) {

			// parent.$('excelDiv').style.width=790;
			$('msgTable').style.display = 'block';
			if (msgList == null) {
				$('msgTable').innerHTML = "文件没找到或该文件不是excel！";
			} else {
				if (msgList.length == 1) {
					if (msgList[0].flag == 1) {
						$('msgTable').innerHTML = "<font color=red>"
								+ msgList[0].msg + "</font>";
					} else {
						$('msgTable').innerHTML = "导入成功&nbsp(<label>"
								+ msgList[0].successNum
								+ "</label>)&nbsp条，导入失败&nbsp(<label>"
								+ msgList[0].failNum
								+ "</label>)&nbsp条，覆盖成功&nbsp(<label>"
								+ msgList[0].coverNum + "</label>)条。";
					}
				} else {
					var htm = '<div class="detail_nav"><label>错误信息(修改excel文件后重新上传)</label></div><div style="height: 220px; overflow: auto;">';

					var temp = '';
					for (var i = 0; i < msgList.length; i++) {
						if (i != msgList.length - 1) {
							htm += "<div id=err" + i + ">第&nbsp("
									+ (msgList[i].rowNum + 1)
									+ ")&nbsp行，第&nbsp("
									+ (msgList[i].colNum + 1) + ")&nbsp列的错误信息："
									+ msgList[i].msg
									+ "<a href=# onclick=reportload(" + i + ","
									+ msgList[i].rowNum
									+ ")>【重新上传】</a></div><br/>";
						} else {
							susNum = msgList[i].successNum;
							failNum = msgList[i].failNum;
							temp = '<div class="detail_nav"><label>导出结果</label></div>导入成功&nbsp(<label id="successNumLab">'
									+ msgList[i].successNum
									+ "</label>)&nbsp条，导入失败&nbsp(<label id='failNumLab'>"
									+ msgList[i].failNum
									+ "</label>)&nbsp条，覆盖成功&nbsp(<label id='coverNumLab'>"
									+ msgList[i].coverNum + "</label>)条。";
						}
					}
					$('msgTable').innerHTML = temp + '</div>' + htm;
				}
			}
			unmask();
		});
	}
});
