Ext.onReady(function() {

	// 报表类型
	var rpttypeComb = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotRptType&key=rptName",
				cmpId : "rptType",
				valueField : "id",
				editable : true,
				sendMethod : "post",
				displayField : "rptName",
				allowBlank : false,
				blankText : "Please select a type",
				emptyText : "Choose",
				fieldLabel : "Type",
				anchor : "95%"
			});
			
	//上传后的文件名	
	var picFile="";
	var from = new Ext.form.FormPanel({
				labelWidth : 60,
				labelAlign : "right",
				layout : "form",
				fileUpload : true,
				padding : "10px",
				frame : true,
				buttonAlign : 'center',
				buttons : [{
					width : 60,
					text : "Save",
					iconCls : "page_table_save",
					handler : function() {
						if (from.getForm().isValid()) {
							if(oldName==$('filePath').value){
								saveOrupdate();
							}else{
								from.getForm().submit({
											url : './uploadRptFile.action',
											method : 'get',
											waitTitle : 'waiting',
											waitMsg : 'uploading...',
											success : function(fp, o) {
												picFile = Ext.util.Format.htmlDecode(o.result.fileName);
												// 上传成功并保存
												saveOrupdate();
											},
											failure : function(fp, o) {
												Ext.MessageBox.alert("Message",
														o.result.msg);
											}
										});
							}
						}
					}
				}, {
					text : "Cancel",
					pressed : false,
					handler : function() {
						closeandreflashEC(true, "rptGrid", false);
					},
					iconCls : "page_cancel"
				}],
				items : [rpttypeComb, {
							xtype : "textfield",
							fieldLabel : "Name",
							allowBlank : false,
							blankText : "Please enter Name",
							name : "rptName",
							id : "rptName",
							anchor : "95%"
						}, {
							xtype : 'fileuploadfield',
							emptyText : 'upload file(.jrxml)',
							anchor : '95%',
							allowBlank : false,
							blankText : "Please choose a file",
							fieldLabel : 'File',
							id : 'filePath',
							name : 'filePath',
							buttonText : '',
							buttonCfg : {
								iconCls : 'upload-icon'
							}
						},{
							xtype : 'checkbox',
							id : 'flag',
							name : 'flag',
							fieldLabel : "",
							checked:true,
							anchor : '95%',
							boxLabel : "Is set Default"
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [from]
			});
	viewport.doLayout();

	// 初始化表单
	var oldName="";
	function initForm() {
		DWREngine.setAsync(false);
		var id = $("rptfileid").value;
		if (id == "" || id == "null" || id == null)
			return;
		cotRptFileService.getRptFileById(parseInt(id), function(res) {
					DWRUtil.setValues(res);
					rpttypeComb.bindPageValue("CotRptType", "id", res.rptType);
					$('filePath').value = res.rptfilePath;
					oldName = res.rptfilePath;
				});
		DWREngine.setAsync(true);
	}
	initForm();

	//保存
	function saveOrupdate() {
		// 表单验证
		var cotRptFile = new CotRptFile();
		cotRptFile.rptType = DWRUtil.getValue("rptType");
		cotRptFile.rptName = DWRUtil.getValue("rptName");
		var id = $("rptfileid").value;
		if (id != "" && id != "null" && id != null) {
			cotRptFile.id = id;
		}
		if ($('flag').checked) {
			cotRptFile.flag=1;
		}else{
			cotRptFile.flag=0;
		}
		if(oldName!=$('filePath').value){
			cotRptFile.rptfilePath = "reportfile/"+picFile;
		}else{
			cotRptFile.rptfilePath =oldName;
		}
		DWREngine.setAsync(false);
		cotRptFileService.saveOrUpdateRptFile(cotRptFile, function(res) {
					Ext.MessageBox.alert("Message", "Successfully saved！");
					closeandreflashEC(true, "rptGrid", false);
				})
		DWREngine.setAsync(true);
	}
});
