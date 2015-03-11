//批量删除
function delPics(){
//	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
//	if (isPopedom == 0) {
//		Ext.MessageBox.alert("Message", "You do not have permission to delete!");
//		return;
//	}
	
	var list = Ext.getCmp("unImportPicName").getSelectionModel().getSelections();	
	if (list.length == 0) {
			Ext.MessageBox.alert("Message", "Please select records!");
			return;
		}
	var res=new Array();
	Ext.each(list,function(item){
		res.push(item.get('filename'));
	});
	Ext.Msg.confirm('Message','Are you sure delete these pictures？',function(btn){
		if (btn == 'yes'){
			importPictureService.deletePic(res, function(){
				Ext.Msg.show({
				   title:'Message',
				   msg:'Deleted successfully!',
				   buttons:Ext.Msg.OK,
				   icon: Ext.Msg.INFO
				});
				Ext.getCmp("unImportPicName").getStore().remove(list);
			});
		}
		
	});
	
}


// 单个删除
//function del(file,rowId) {
//	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
//	if (isPopedom == 0) {
//		Ext.MessageBox.alert("Message", "您没有删除权限");
//		return;
//	}
//	var list = new Array();
//	list.push(file);
//	importPictureService.deletePic(list, function(){
//		Ext.Msg.show({
//		   title:'系统提示',
//		   msg:'删除成功',
//		   buttons:Ext.Msg.OK,
//		   icon: Ext.Msg.INFO
//		});
//	Ext.getCmp("unImportPicName").getStore().removeAt(rowId);
//	});
//	
//}

Ext.onReady(function() {
	var sm = new Ext.grid.CheckboxSelectionModel();
	var form = new Ext.form.FormPanel({
		labelWidth : 50,
		labelAlign : "left",
		layout : "border",
		padding : "5px",
		border : false,
		items : [{
					xtype : "panel",
					title : "Pictures",
					height : 510,
					region : 'west',
					border : false,
					margins : '0 5 0 0',
					cls : 'rightBorder',
					width : "50%",
					layout : "border",
					tbar : new Ext.Toolbar({
						items : ['->', {
									text : "Delete",
									handler : delPics,
									iconCls : "page_del"
									//cls : "SYSOP_DEL"
								}]
					}),
					items : [{
								region : 'north',
								xtype : "label",
								style : "color:red;padding:10px",
								text : "Tip: Importing image file format must bejpg、gif、png、bmp、img！"
							}, {
								xtype : "grid",
								region : 'center',
								sm:sm,
								id : "unImportPicName",
								height : 500,
								border : false,
								store : {
									xtype : "arraystore",
									fields : [{
												name : "filename",
												type : "string"
											}]
								},
								columns : [sm,
									    {
											header : "Picture Name",
											sortable : false,
											resizable : true,
											dataIndex : "filename",
											width : 400
										},
										{
											header : "Operation",
											dataIndex : "filename",
											renderer : function(value, metaData, record,
													rowIndex, colIndex, store) {
												var re = /\\/g ;		
												value=value.replace(re,"/");
//												var del = '<a href=javascript:del("' + value +'",'+rowIndex 
//														+ ')>删除</a>';
												var del = '<a href=javascript:delPics()>Delete</a>'		
												return del;
											}												
										}
										]
							}]
				}, {
					xtype : "panel",
					title : "Import Setting",
					layout : "border",
					border : false,
					cls : 'leftBorder',
					region : 'center',
					items : [{
								xtype : "panel",
								layout : "form",
								region : 'north',
								height : 150,
								padding : "5",
								//frame : true,
								ctCls:'x-panel-mc',
								buttonAlign : "center",
								defaults : {
									width : 400
								},
								fbar : [{
											text : "Upload",
											handler : showUploadPanel
										}, {
											text : "Import",
											handler : importPicture
										}],
								items : [{
											xtype : "checkbox",
											width : 300,
											id : "isCover",
											checked : true,
											inputValue : "1",
											boxLabel : "Images Name samed with Article, whether cover image",
											anchor : "100%",
											hideLabel : true
										}, {
											xtype : "checkbox",
											id : "isAdd",
											inputValue : "2",
											checked : true,
											boxLabel : "Images Name differ with Article, whether as new Article",
											anchor : "100%",
											hideLabel : true
										}, {
											xtype : "label",
											style : "color:red",
											text : "Tips:This may take some time ，Please wait！"
										}]
							}, {
								xtype : "panel",
								title : "Import Result",
								region : 'center',
								ctCls:'x-panel-mc',
								items : [{
											xtype : "panel",
											id : "result"
										}]

							}]
				}]
	});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [form]
			});
	var uploadSeting = new Ext.Window({
				title : "Upload Setting",
				width : 350,
				height : 250,
				layout : "form",
				padding : "10",
				closeAction : "hide",
				titleCollapse : false,
				buttonAlign : "center",
				plain : false,
				fbar : [{
							text : "Set",
							handler : setupUploadConfig
						}],
				items : [{
							xtype : "numberfield",
							fieldLabel : "Max Pic Nums",
							id : "uploadLimit",
							allowDecimals : false,
							allowNegative : false,
							value : 300,
							maxValue : 1000,
							anchor : "100%"
						}, {
							xtype : "numberfield",
							fieldLabel : "Max Pic Size(K)",
							allowDecimals : false,
							allowNegative : false,
							maxValue : 6144,
							value : 300,
							id : "sizeLimit",
							anchor : "100%"
						}, {
							xtype : "fieldset",
							title : 'Image compression',
							layout : 'form',
							collapsed : true,
							checkboxToggle : true,
							id : "suoChk",
							labelWidth : 140,
							labelAlign : 'right',
							items : [{
										xtype : "numberfield",
										fieldLabel : "Compression ratio(%)",
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
																fieldLabel : "Lenght",
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
													labelWidth :50,
													items : [{
																xtype : "numberfield",
																fieldLabel : "Width",
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
			})
	function setupUploadConfig() {
		var swfu = uploader.items.items[0].suo;
		var file_upload_limit = parseInt($("uploadLimit").value);
		if (isNaN(file_upload_limit) || file_upload_limit == 0) {
			$("uploadLimit").value = "300";
			file_upload_limit = 300;
		}
		if (file_upload_limit > 1000) {
			Ext.Msg.alert('Message', "Upload a few too many, up to 1000!")
			file_upload_limit = 1000;
			$("uploadLimit").value = "1000";
		}
		swfu.setFileUploadLimit(file_upload_limit);
		// 判断输入的内容是否违规
		var file_size_limit = parseInt($("sizeLimit").value);
		if (isNaN(file_upload_limit) || file_size_limit == 0) {
			$("sizeLimit").value = "300";
			file_size_limit = 300;
		}
		if (file_size_limit > 6144) {
			Ext.Msg.alert('Message', "Can not exceed the maximum per 6144KB (6MB), up to 6M!")
			file_size_limit = 6144;
			$("sizeLimit").value = "6144";
		}
		swfu.setFileSizeLimit(file_size_limit);
		
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
				var url=defaultUrl+"?scale="+scale+"&height="+height+"&width="+width;
				swfu.setUploadURL(url);
			}
		}
		
		Ext.Msg.alert('Message', "Successfully set!");
		uploadSeting.hide();
	}
	
	// 上传后调用
	function reloadFn(server_data) {
		bindSel();
	}
	
	//默认上传路径
	var defaultUrl = '../../servlet/TestServletUpload';

	var uploader = null;// 上传组件
	function showUploadPanel() {
		if (uploader == null) {
			Ext.DomHelper.append(document.body, {
				html : '<div id="tempBrn" style="background-color: #AADBF0;"><div id="spanButtonPlaceholder"></div>'
			}, true);
			
			var tb = new Ext.Toolbar({
				items : [{
							text : "Upload Setting",
							id:'cfgBtn',
							handler : function(){
								uploadSeting.show();
							},
							iconCls : "page_config",
							cls : "SYSOP_ADD"
						},'-',{
							xtype:'panel',
							contentEl:'tempBrn'
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
				button_placeholder_id : "spanButtonPlaceholder",// swfupload_10f.swf会去渲染这个Id，使之变成一个按钮，
				button_text_top_padding : 3,
				button_text_left_padding : 0,
				confirm_delete : true,// 删除时，是否需要提示确认
				file_types : "*.jpg;*.bmp;*.png;*.gif;",
				file_types_description : "图片文件",
				file_upload_limit : 300, // 上传数量
				file_size_limit : 300,
				reloadUrl : window.location.href,
				reloadFn : reloadFn
			});
			uploader = new Ext.Window({
				title : 'Bulk Upload',
				layout:'fit',
				modal:true,
				width:500,
				height:380,
				tbar : tb,
				closeAction:'hide',
				items:[pnl]
			});
			
		}
		uploader.show();
	}
	

	// 加载未导入图片信息下拉列表
	function bindSel() {
		importPictureService.getUnImportPicList(function(res) {

					var store = Ext.getCmp("unImportPicName").getStore();
					if (res.length > 0) {
						store.removeAll();
						Ext.each(res, function(cotPicture) {
									var u = new store.recordType({
												filename : cotPicture.picName
											})
									store.add(u);
								})
					}
				})

	}

	// 导入图片
	function importPicture() {
		var panel = Ext.getDom("result");
		DWRUtil.useLoadingMessage("Loading...........");
		DWREngine.setAsync(false);

		// 获得导入策略
		var isCover = $('isCover').checked;
		var isAdd = $('isAdd').checked;

		importPictureService.moveFile(isCover, isAdd, function(msgList) {
					if (msgList.length == 0) {
						Ext.Msg.alert('Message', "Please upload a photo and then the import picture!");
					}
					panel.innerHTML = "<div style='margin-left:20px;'>"
					for (var i = 0; i < msgList.length; i++) {
//						if (i == msgList.length - 1 && i != 1) {
//							var successSum = msgList[i].coverSum
//									+ msgList[i].addSum;
//							panel.innerHTML += "<br/>Info：<br/><br/>Covered---<font color=red>"
//									+ msgList[i].coverSum + "</font>nums<br/>Added---<font color=red>"
//									+ msgList[i].addSum + "</font>nums<br/>Skip---<font color=red>"
//									+ msgList[i].crossSum + "</font>nums<br/>Imports---<font color=red>"
//									+ successSum + "</font>nums"+"<br/>Skip："
//									+ msgList[i].crossMsg;
//						}
						if (i == 1) {
							var successSum = msgList[0].coverSum
									+ msgList[0].addSum;
							panel.innerHTML += "<br/><br/><br/>Covered---<font color=red>"
									+ msgList[0].coverSum + "</font>&nbsp;nums<br/>Added---<font color=red>"
									+ msgList[0].addSum + "</font>&nbsp;nums<br/>Skip---<font color=red>"
									+ msgList[0].crossSum + "</font>&nbsp;nums<br/>Imports---<font color=red>"
									+ successSum + "</font>&nbsp;nums";
						}
					}
					panel.innerHTML += "</div>";
				})
		bindSel();
		DWREngine.setAsync(true);

	}

	// 提示并导入图片
	function importPic() {
		// 获得导入策略
		var isCover = $('isCover').checked;
		var isAdd = $('isAdd').checked;
		if (!isCover && !isAdd) {
			Ext.Msg.alert('Message', "Please select the Import Setting!")
		} else {
			importPicture();
			// $("importBtn").disabled="disabled";
		}
	}
	bindSel();
});

//加载未导入图片信息下拉列表
