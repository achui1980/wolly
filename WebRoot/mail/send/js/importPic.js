	function setupUploadConfig() {
		var swfu = pnl.suo;
		var file_upload_limit = parseInt($("uploadLimit").value);	
		if (isNaN(file_upload_limit) || file_upload_limit == 0) {
//			Ext.Msg.alert('提示消息', "请输入每次上传附件数");	
//			return;
			$("uploadLimit").value = "300";
			file_upload_limit = 300;
		}		
		swfu.setFileUploadLimit(file_upload_limit);
		
		// 判断输入的内容是否违规
		var file_size_limit = parseInt($("sizeLimit").value);
		
		if (isNaN(file_upload_limit) || file_size_limit == 0) {
			$("sizeLimit").value = "10240";
			file_size_limit = 10240;
		}
		if (file_size_limit > 10240) {
			Ext.Msg.alert('提示消息', "每张最大不能大于10240KB（10MB），最多10M");		
			file_size_limit = 10240;
			$("sizeLimit").value = "10240";
			return;
		}
		swfu.setFileSizeLimit(file_size_limit);
		
		
		Ext.Msg.alert('提示消息', "设置成功");
		uploadSeting.hide();
	}
	
	var uploadSeting = new Ext.Window({
				title : "上传配置",
				width : 200,
				height : 150,
				layout : "form",
				padding : "10",
				closeAction : "hide",
				modal:true,
				titleCollapse : false,
				buttonAlign : "center",
				plain : false,
				fbar : [{
							text : "设置",
							handler : setupUploadConfig
						}],
				items : [{
							xtype : "numberfield",
							fieldLabel : "每次上传附件数",
							id : "uploadLimit",
							allowDecimals : false,
							allowNegative : false,
							value : 180,
							maxValue : 1000,
							anchor : "100%"
						}, {
							xtype : "numberfield",
							fieldLabel : "每个附件大小(K)",
							allowDecimals : false,
							allowNegative : false,
							value : 1000,
							maxValue : 10240,
							id : "sizeLimit",
							anchor : "100%"
						}]
			})

	var uploaderWin = null;// 上传组件
	function mailShowUploadPanel() {
	   //获得时间戳
		var random =Ext.getCmp(EXT_MAIL_ID_OUTBOUND_FROMPANEL).random;
		if (uploaderWin==null) {
			Ext.DomHelper.append(document.body, {
				html : '<div id="tempBrn" style="background-color: #AADBF0;"><div id="spanButtonPlaceholder"></div>'
			}, true);
			var tb = new Ext.Toolbar({
				items : [{
						text : "上传配置",
						id:'gear',
						iconCls : "page_config",
						handler : function(){
							uploadSeting.show();
						}
					},'-',{
						xtype:'panel',
						contentEl:'tempBrn'
					}]
				});
			pnl = new Ext.mail.SwfUploadPanel({
				width : 480,
				height : 400,
				upload_url : "../../uploadMailFile.action;jsessionid="+$('sessionId').value+"?m=uploadFile&date="+random,
				flash_url : "./common/js/swfupload_10f.swf", // 需要调用的flash插件
				button_image_url : "./common/images/AttachButtonUploadText_61x22.png",// 添加文件按钮
				button_width : 80,
				button_height : 22,
				button_action : SWFUpload.BUTTON_ACTION.SELECT_FILES,// 定义是否可以多选文件（flash10的版本修改了改功能用改属性实现多选，而flash9的版本可以通过调用函数来实现）
				button_placeholder_id : "spanButtonPlaceholder",// swfupload_10f.swf会去渲染这个Id，使之变成一个按钮，
				button_text_top_padding : 3,
				button_text_left_padding : 0,
				confirm_delete : true,// 删除时，是否需要提示确认
		//		file_types : "*.jpg;*.bmp;*.png;*.gif;",
				file_types_description : "附件",
				file_upload_limit : 300, // 上传数量
				file_size_limit : 1000,
				post_params : {
                    "JAVASESSID" : $('sessionId').value
                },
                reloadFn:Ext.emptyFn,

				reloadUrl : window.location.href
			});

			uploaderWin = new Ext.Window({
				title : '附件上传',
				layout:'fit',
				modal:true,
				width:500,
				height:380,
				tbar : tb,
				closeAction:'hide',
				items:[pnl]
			});
		}
		uploaderWin.show();
	}



