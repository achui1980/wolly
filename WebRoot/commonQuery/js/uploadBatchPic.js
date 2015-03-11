 var uploadSetingPic;
 UploadBatchPics = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	
	if(uploadSetingPic==null){
	uploadSetingPic = new Ext.Window({
				title : "上传配置",
				width : 350,
				height : 250,
				layout : "form",
				padding : "10",
				closeAction : "hide",
				titleCollapse : false,
				buttonAlign : "center",
				plain : false,
				fbar : [{
							text : "设置",
							handler : setupUploadConfig
						}],
				items : [{
							xtype : "numberfield",
							fieldLabel : "每次上传图片数",
							id : "uploadLimitPic",
							allowDecimals : false,
							allowNegative : false,
							value : 300,
							maxValue : 1000,
							anchor : "100%"
						}, {
							xtype : "numberfield",
							fieldLabel : "每张图片大小(K)",
							allowDecimals : false,
							allowNegative : false,
							maxValue : 6144,
							value : 300,
							id : "sizeLimitPic",
							anchor : "100%"
						},{ xtype:'panel',
						    baseCls : 'x-plain',
							items:[{
								xtype:'checkbox',
								width :300,
								style:"margin-left:13px",
								id:'compressionBox',
							    boxLabel :'转成PNG格式(供Excel2000用户使用)'
//							    listeners:{
//							    	'check':function(checkbox ,isCheck){
//							    		if(isCheck==true){
//							    			Ext.getCmp('suoChk').disable();
//							    		}else{
//							    			Ext.getCmp('suoChk').enable() ;
//							    		}
//							    	}
//							    }
							}]
							
						} ,{
							xtype : "fieldset",
							title : '图片压缩',
							layout : 'form',
							collapsed : true,
							checkboxToggle : true,
							id : "suoChk",
							labelWidth : 55,
							labelAlign : 'right',
							items : [{
										xtype : "numberfield",
										fieldLabel : "压缩比",
										maxValue : 0.5,
										id:'scale',
										name:'scale',
										decimalPrecision : 2,
										value : 0.5,
										anchor : "100%"
									}, {
										xtype : "panel",
										layout : 'column',
										baseCls : 'x-plain',
										//border:false,
										items : [{
													columnWidth : .5,
													layout : 'form',
													baseCls : 'x-plain',
													labelWidth : 55,
													items : [{
																xtype : "numberfield",
																fieldLabel : "按比例长",
																id:'heightAfter',
																name:'heightAfter',
																maxValue : 2000,
																decimalPrecision : 2,
																value : 400,
																anchor : "100%"
															}]
												}, {
													columnWidth : .5,
													layout : 'form',
													baseCls : 'x-plain',
													labelWidth : 30,
													items : [{
																xtype : "numberfield",
																fieldLabel : "宽",
																id:'widthAfter',
																name:'widthAfter',
																maxValue : 2000,
																decimalPrecision : 2,
																value : 400,
																anchor : "100%"
															}]
												}]
									}]
						}]
			});
		}
	function setupUploadConfig() {
		var swfu = _self.items.items[0].suo;
		var file_upload_limit = parseInt($("uploadLimitPic").value);
		if (isNaN(file_upload_limit) || file_upload_limit == 0) {
			$("uploadLimitPic").value = "300";
			file_upload_limit = 300;
		}
		if (file_upload_limit > 1000) {
			Ext.Msg.alert('Message', "上传图片数过多，最多1000张")
			file_upload_limit = 1000;
			$("uploadLimitPic").value = "1000";
		}
		swfu.setFileUploadLimit(file_upload_limit);
		// 判断输入的内容是否违规
		var file_size_limit = parseInt($("sizeLimitPic").value);
		if (isNaN(file_upload_limit) || file_size_limit == 0) {
			$("sizeLimitPic").value = "300";
			file_size_limit = 300;
		}
		if (file_size_limit > 6144) {
			Ext.Msg.alert('Message', "每张最大不能大于6144KB（6MB），最多6M")
			file_size_limit = 6144;
			$("sizeLimitPic").value = "6144";
		}
		swfu.setFileSizeLimit(file_size_limit);
		//勾选转PNG
		var ckbox=Ext.getCmp('compressionBox');
		var flag=false;
		if(ckbox.checked){
			flag=true;
		}
		//是否压缩,如果有更改上传路径
		var chk = Ext.getCmp('suoChk').checkbox;
		if(chk){
			if(chk.dom.checked){
				var scale = Ext.getCmp('scale').getValue();
				if(height==''){
					scale=0.5;//默认0.5
				}
				var height = Ext.getCmp('heightAfter').getValue();
				if(height==''){
					height=400;//默认400
				}
				var width = Ext.getCmp('widthAfter').getValue();
				if(width==''){
					width=400;//默认400
				}
				var url=defaultUrl+"&scale="+scale+"&height="+height+"&width="+width+"&checked="+flag;
				swfu.setUploadURL(url);
			}else{
				var url=defaultUrl+"&checked="+flag;
				swfu.setUploadURL(url);
		}
		}
		
		Ext.Msg.alert('Message', "设置成功");
		uploadSetingPic.hide();
	}

	// 上传后调用
	function reloadFn(server_data) {
			Ext.MessageBox.alert('Message', 'Upload Successfully!');
			cfg.pds.reload();
			_self.close();
	}
	// 默认上传路径
	var defaultUrl = '../../uploadPanOtherPicture.action?pEId='+cfg.pEId;
	Ext.DomHelper.append(document.body, {
		html : '<div id="tempBrnPic" style="background-color: #AADBF0;"><div id="spanButtonPlaceholderPic"></div>'
	}, true);

	var tb = new Ext.Toolbar({
				items : [{
							text : "上传配置",
							hidden:true,
							handler : function() {
								uploadSetingPic.show();
							},
							iconCls : "page_config",
							cls : "SYSOP_ADD"
						}, '-', {
							xtype : 'panel',
							contentEl : 'tempBrnPic'
						}]
			});

	var pnl = new Ext.ux.SwfUploadPanel({
				width : 480,
				height : 400,
				upload_url : defaultUrl, // 上传文件的URL
				flash_url : "./common/js/swfupload_10f.swf", // 需要调用的flash插件
				button_image_url : "./common/images/AttachButtonUploadText_61x22.png",// 添加文件按钮
				button_width : 80,
				button_height : 22,
				button_action : SWFUpload.BUTTON_ACTION.SELECT_FILES,// 定义是否可以多选文件（flash10的版本修改了改功能用改属性实现多选，而flash9的版本可以通过调用函数来实现）
				button_placeholder_id : "spanButtonPlaceholderPic",// swfupload_10f.swf会去渲染这个Id，使之变成一个按钮，
				button_text_top_padding : 3,
				button_text_left_padding : 0,
				confirm_delete : true,// 删除时，是否需要提示确认
				//file_types : "*.txt;*.doc;*.xsl;",
				file_types_description : "文件",
				file_upload_limit : 300, // 上传数量
				file_size_limit : 2000,
				reloadUrl : window.location.href,
				reloadFn : reloadFn
			});
	// 构造
	var con = {
		title : 'Upload',
		layout : 'fit',
		border : false,
		modal : true,
		width : 500,
		height : 380,
		tbar : tb,
		items : [pnl]
	};

	Ext.apply(con, cfg);
	UploadBatchPics.superclass.constructor.call(this, con);
	
};
Ext.extend(UploadBatchPics, Ext.Window, {});