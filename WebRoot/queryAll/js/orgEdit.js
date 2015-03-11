Ext.onReady(function() {
			
	// 上传后的文件名
	var picFile = "";
	var form = new Ext.form.FormPanel({
		labelWidth : 80,
		labelAlign : "right",
		layout : "form",
		padding : "5px",
		frame : true,
		fileUpload : true,
		buttonAlign : "center",
		monitorValid : true,
		fbar : [{
					text : "Save",
					formBind : true,
					iconCls : "page_mod",
					handler : function() {
						if (form.getForm().isValid()) {
							if (oldName == $('filePath').value) {
								mod();
							} else {
								form.getForm().submit({
									url : './uploadOrderPcPdf.action',
									waitTitle : 'waiting',
									waitMsg : 'uploading...',
									success : function(fp, o) {
										picFile = Ext.util.Format
												.htmlDecode(o.result.fileName);
										// 上传成功并保存
										mod();
									},
									failure : function(fp, o) {
										Ext.MessageBox.alert("Message", o.result.msg);
									}
								});
							}
						}
					}
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					handler : function() {
						closeandreflashEC('true', 'orgGrid', false);
					}
				}],
		items : [{
			xtype : 'myfileuploadfield',
			emptyText : 'upload file(pdf/Picture)',
			anchor : '100%',
			allowBlank : false,
			blankText : "Please choose a Pdf or Picture",
			fieldLabel : 'File',
			id : 'filePath',
			name : 'filePath',
			vtype : 'fileType',
			vtypeText : 'Upload format error, File id not a Pdf or Picture!',
			fileTypes : ['pdf','jpg','png','gif','jpeg'],
			enableClearValue : true,
			toolTip : true
		},  {
			xtype : "textarea",
			fieldLabel : "Remark",
			anchor : "100%",
			height : 100,
			id : "remark",
			name : "remark",
			maxLength : 200
		}]
	});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});

	// 初始化页面函数
	var oldName = "";
	function initForm() {
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotOrgService.getOrgById(parseInt(id), function(res) {
				if (res != null) {
					DWRUtil.setValues(res);
					$('filePath').value = res.filePath;
					oldName = res.filePath;
				}
			});
		}
	}
	// 初始化页面
	initForm();

	// 保存
	function mod() {
		var fit = new CotOrg();
		fit.remark = $('remark').value;
		var fPath = "";
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			fit.id = id;
		}
		if (oldName != $('filePath').value) {
			fit.filePath = picFile;
		} else {
			fit.filePath = oldName;
		}
		// 更新
		cotOrgService.saveOrg(fit,function(res) {
					Ext.MessageBox.alert('Message', "保存成功！");
					// 刷新父页面表格
					closeandreflashEC('true', 'orgGrid', false);
				})
	}
});
