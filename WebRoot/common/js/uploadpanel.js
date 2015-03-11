/*1、该类需要引入ext/ux/FileUploadField.js与ext/resources/css/file-upload.css
 * cfg支持的值
 * params：需要提交到后台的参数，是一个Object对象
 * waitMsg：上传等待时提示的信息
 * opAction:insert或者modify,操作类型，用于识别是否需要显示图片
 * imgObj:页面上的图片对象,
 * imgUrl:通过流显示图片的Url，
 * uploadType:上传文件的类型 file：普通文件，image:图片
 * loadImgStream:true,false,是否需要以流的方式加载图片，必须配合imgUrl，和imgObj
 * uploadUrl:上传路径
 * validType: *|jpg|doc //文件验证类型，其中*代表所有文件，多个用|符号隔开
 * */
UploadWin = function(cfg) {
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
				waitTitle : "Pls. wait",
				waitMsg : cfg.waitMsg || 'Loading...',
				success : function(fp, o) {
					//上传成功后是否返回标识
					if(cfg.returnFlag){
						$('uploadSuc').value="1";
					}
					
					var uploadType = (cfg.uploadType == null
							? "image"
							: cfg.uploadType);
					if (uploadType == "file") {
						Ext.Msg.show({
									title : "Upload successful",
									msg : "Upload successful",
									minWidth : 200,
									modal : true,
									icon : Ext.Msg.INFO,
									buttons : Ext.Msg.OK,
									fn : function() {
										if (cfg.gridId)
											reloadGrid(cfg.gridId);
										if ($('filePath'))
											$('filePath').value = o.result.fileName
									}
								});
					} else if (uploadType == "image") {
						if (cfg.opAction == "editor") {
							var element = cfg.editor.win.document.createElement("img");
                            element.src = 'upload\\'+o.result.fileName;
                            if (Ext.isIE) {
                                cfg.editor.insertAtCursor(element.outerHTML);
                            } else {
                                var selection = cfg.editor.win.getSelection();
                                if (!selection.isCollapsed) {
                                    selection.deleteFromDocument();
                                }
                                selection.getRangeAt(0).insertNode(element);
                            }
						} 
						else if (cfg.opAction == "insert") {
							//上传完毕后加载图片
							cfg.imgObj.src = o.result.fileName;
						} else {
							if(!cfg.gridId){
								//以流的方式加载图片
								if (loadImgStream)
									cfg.imgObj.src = cfg.imgUrl;
								else
									//直接加载上传完后的路径
									cfg.imgObj.src = o.result.fileName
							}else{
								//刷新表格中的图片
								var a=document.getElementsByTagName("img") 
								for(var i=0;i<a.length;i++){
									var src = a[i].src;
									if(src.indexOf("flag=pan&detailId="+cfg.params.detailId)>-1){
										var rdm = Math.round(Math.random() * 10000);
										var starIndex = src.indexOf("&tmp=");
										var endIndex = src.indexOf("width");
										a[i].src=src.substring(0,starIndex+5)+rdm+src.substring(endIndex-1);
									}
								}
							}
						}
					}
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
		cfg.labelName = "Picture";
	}

	this.form = new Ext.form.FormPanel({
				labelWidth : 60,
				fileUpload : true,
				labelAlign : "right",
				id : "uploadForm",
				autoWidth : true,
				autoHeight : true,
				padding : "10px",
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
						}]
			})

	//生成上传表单
	var loadImgStream = cfg.loadImgStream == null ? true : cfg.loadImgStream;

	this.getFileName = function() {
		var uploadField = Ext.getCmp("fileUpload");
		var file = uploadField.getValue();
		var idx = file.lastIndexOf("\\");
		var filename = file.substring(idx + 1, file.length);
		return filename;
	}
	var con = {
		width : 300,
		title : "Upload",
		modal : true,
		id : "uploadWin",
		items : [this.form]
	}
	Ext.apply(con, cfg);
	UploadWin.superclass.constructor.call(this, con);
};

Ext.extend(UploadWin, Ext.Window, {});