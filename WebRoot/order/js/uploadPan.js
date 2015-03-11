/*1、该类需要引入ext/ux/FileUploadField.js与ext/resources/css/file-upload.css
 * cfg支持的值
 * params：需要提交到后台的参数，是一个Object对象
 * waitMsg：上传等待时提示的信息
 * uploadUrl:上传路径
 * validType: *|jpg|doc //文件验证类型，其中*代表所有文件，多个用|符号隔开
 * */
UploadPan = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	var validType = cfg.validType == null ? "*" : cfg.validType;
	//触发上传事件		
	var uploadAction = function() {
		if (_self.form.getForm().isValid()) {
			var uploadField = Ext.getCmp("fileUpload");
			var file = uploadField.getValue();
			var idx = file.lastIndexOf(".");
			var suffix = file.substring(idx + 1, file.length);
			//上传类型验证
			if ("*" != validType
					&& validType.indexOf(suffix.toLowerCase()) == -1) {
				Ext.Msg.show({
							title : "Incorrect type",
							msg : "Can only upload" + validType + " File type",
							minWidth : 200,
							modal : true,
							icon : Ext.Msg.INFO,
							buttons : Ext.Msg.OK
						});
				return;
			}
			//文件上传操作
			_self.form.getForm().submit({
				url : cfg.uploadUrl,
				params : cfg.params || {},
				waitTitle : "Please Waiting",
				waitMsg : cfg.waitMsg || 'File Uploading.....',
				success : function(fp, o) {
					//查询上传的盘点文件流水号
					cotOrderService.getMachineNum(function(res) {
						if (res.length <= 0) {
							Ext.Msg.alert('Message', 'The format is wrong! Please check and re-import!');
							return;
						}
						var win = new PanWin(cfg);
						win.show();
						//清空下拉框
						Ext.getCmp('checkNo').store.removeAll();
						//加载
						for (var i = 0; i < res.length; i++) {
							var obj = new Object();
							obj.value = res[i];
							obj.text = res[i];
							var record = new Ext.data.Record(obj);
							Ext.getCmp('checkNo').view.store.add(record);
						}
						Ext.getCmp('checkNo').view.on('click', function(
								dataView, index, node, e) {
							var rec = dataView.getStore().getAt(index);
							cotOrderService.getMachineDetails(rec.data.value,
									function(dk) {
										// 清空下拉框
										Ext.getCmp('checkDetail').store
												.removeAll();
										// 加载
										for (var i = 0; i < dk.length; i++) {
											var obj = new Object();
											obj.value = dk[i];
											obj.text = dk[i];
											var record = new Ext.data.Record(obj);
											Ext.getCmp('checkDetail').view.store
													.add(record);
										}
									});
						});
					});
					_self.close();
				},
				failure : function(fp, o) {
					Ext.Msg.show({
								title : "Upload failed",
								msg : o.result.msg,
								minWidth : 200,
								modal : true,
								icon : Ext.Msg.INFO,
								buttons : Ext.Msg.OK
							});
				}
			})
		}
	}
	if (!cfg.labelName) {
		cfg.labelName = "File";
	}

	this.form = new Ext.form.FormPanel({
		labelWidth : 40,
		fileUpload : true,
		labelAlign : "right",
		id : "uploadForm",
		autoWidth : true,
		autoHeight : true,
		padding : "10px",
		//frame : true,
		border:false,
		baseCls : 'x-plain',
		buttonAlign : "center",
		fbar : [{
					text : "Upload",
					iconCls : "page_upload",
					handler : uploadAction
				}, {
					text : "Close",
					iconCls : "page_cancel",
					handler : function() {
						_self.close();
					}
				}],
		items : [{
					xtype : 'fileuploadfield',
					allowBlank : false,
					blankText : "Choose",
					id : 'fileUpload',
					emptyText : 'Choose',
					fieldLabel : cfg.labelName,
					name : 'photo-path',
					buttonText : '',
					buttonCfg : {
						iconCls : 'upload-icon'
					},
					anchor : "100%"
				}, {
					xtype : 'panel',
					baseCls : 'x-plain',
					bodyStyle : 'marginTop:10px',
					html : 'Can only upload cvs or txt file, the default currency for the price<span style="color: red">"USD"</span>'
				}]
	})

	this.getFileName = function() {
		var uploadField = Ext.getCmp("fileUpload");
		var file = uploadField.getValue();
		var idx = file.lastIndexOf("\\");
		var filename = file.substring(idx + 1, file.length);
		return filename;
	}
	var con = {
		width : 300,
		title : "Barcode Machine",
		modal : true,
		id : "uploadWin",
		items : [this.form]
	}
	Ext.apply(con, cfg);
	UploadPan.superclass.constructor.call(this, con);
};

Ext.extend(UploadPan, Ext.Window, {});