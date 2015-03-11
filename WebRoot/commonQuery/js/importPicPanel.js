//批量删除
function delPics(){
//	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
//	if (isPopedom == 0) {
//		Ext.MessageBox.alert("Message", "Sorry, you do not have Authority!");
//		return;
//	}
	
	var list = Ext.getCmp("unImportPicName").getSelectionModel().getSelections();	
	if (list.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record");
			return;
		}
	var res=new Array();
	Ext.each(list,function(item){
		res.push(item.get('filename'));
	});
	Ext.Msg.confirm('Message','Are you sure you want to delete the selected picture?',function(btn){
		if (btn == 'yes'){
			importPicService.deletePic(res, function(){
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

// 图片表格导入
ImportPicPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var typeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['price', 'Quote'], ['order', 'Order'], ['given', '送样单']]
			});

	var typeField = new Ext.form.ComboBox({
				name : 'type',
				fieldLabel : 'Type',
				store : typeStore,
				valueField : "tp",
				disabled : true,
				disabledClass : 'combo-disabled',
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "95%",
				value : cfg.type,
				emptyText : 'Choose',
				hiddenName : 'type',
				selectOnFocus : true
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	var form = new Ext.form.FormPanel({
		labelWidth : 50,
		labelAlign : "left",
		layout : "border",
		border:false,
		items : [{
					xtype : "panel",
					title : "Picture info",
					layout : "border",
					region:'west',
					margins : '0 5 0 0',
					cls:'rightBorder',
					width:"50%",
					border:false,
					tbar: new Ext.Toolbar({
						items:['->', {
									text : "Del",
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
								border:false,
								sm:sm,
								id : "unImportPicName",
								height : 500,
								store : {
									xtype : "arraystore",
									fields : [{
												name : "filename",
												type : "string"
											}]
								},
								columns : [sm,{
											header : "Picture name",
											sortable : false,
											resizable : true,
											dataIndex : "filename",
											width : 260
										},{
											header : "Operation",
											dataIndex : "filename",
											renderer : function(value, metaData, record,
													rowIndex, colIndex, store) {
												var del = '<a href=javascript:delPics()>Delete</a>'		
												return del;
											}												
										}
										
										]
							}]
				}, {
					xtype : "panel",
					title : "Operation",
					layout : "border",
					border:false,
					cls:'leftBorder',
					region:'center',
					items : [{
						xtype : "panel",
						layout : "border",
						region:'north',
						padding : "5",
						height : 110,
						border:false,
						ctCls:'x-panel-mc',
						buttonAlign : "center",
						fbar : [{
									text : "Upload",
									handler : showUploadPanel
								}, {
									text : "Import",
									handler : importPicture
								}],
						items : [{
							region : 'center',
							layout : 'column',
							labelAlign : "right",
							padding : "5",
							border:false,
							anchor : "100%",
							items : [{
										layout : 'form',
										columnWidth : .5,
										labelWidth : 60,
										items : [typeField]
									}, {
										layout : 'form',
										columnWidth : .5,
										labelWidth : 60,
										items : [{
													xtype : "textfield",
													fieldLabel : "No.",
													readOnly : true,
													disabledClass : 'combo-disabled',
													value : cfg.no,
													id : "No",
													name : "No",
													anchor : "95%"
												}]
									}]
						}, {
							xtype : "label",
							region : 'south',
							style : "color:red;padding:10px;",
							text : "Tip: Importing images, please wait!"
						}]
					}, {
						xtype : "panel",
						title : "Results",
						region:'center',
						border:false,
						ctCls:'x-panel-mc',
						items : [{
									xtype : "panel",
									id : "result"
								}]

					}]
				}]
	});
	

	

	// 上传配置窗口
	var uploadSeting = new Ext.Window({
				title : "Upload configuration",
				width : 350,
				height : 250,
				layout : "form",
				padding : "10",
				closeAction : "hide",
				buttonAlign : "center",
				modal:true,
				plain : false,
				listeners : {
					'show' : function(win) {
						Ext.getCmp('upLab').setText('');
					}
				},
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
																fieldLabel : "Length",
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
													labelWidth : 50,
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
						}, {
							xtype : "label",
							id : "upLab",
							style : "color:red;padding:10px;",
							text : ""
						}]
			});

	// 设置上传参数
	function setupUploadConfig() {
		var swfu = uploader.items.items[0].suo;
		var file_upload_limit = parseInt($("uploadLimit").value);
		if (isNaN(file_upload_limit) || file_upload_limit == 0) {
			$("uploadLimit").value = "300";
			file_upload_limit = 300;
		}
		if (file_upload_limit > 1000) {
			Ext.getCmp('upLab').setText('Max 1000');
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
			Ext.Msg.alert('Message', "Max 6144KB（6MB)")
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
				var url=defaultUrl+"&scale="+scale+"&height="+height+"&width="+width;
				swfu.setUploadURL(url);
			}
		}
		
		Ext.getCmp('upLab').setText('Setting success!');
		// uploadSeting.hide();
	}

	// 上传后调用
	function reloadFn(server_data) {
		bindSel();
	}
	
	//默认上传路径
	var defaultUrl = '../../servlet/PicUploadServlet?empNo='+loginEmpId;

	// 显示上传组件
	var uploader = null;
	function showUploadPanel() {
		if (uploader == null) {
			var c={tag: 'div',html: '<div id="spanButtonPlaceholder">'};
			if($('tempBrn')==null){
				Ext.DomHelper.append(document.body, {
					html : '<div id="tempBrn" style="background-color: #AADBF0;"></div>'
				}, true);
				var el= Ext.get('tempBrn');
				el.createChild(c); 
			}else{
				var el= Ext.get('tempBrn');
				Ext.DomHelper.overwrite(el.dom.firstChild,c,true);
			}
			
			var tb = new Ext.Toolbar({
				items : [{
							text : "Upload configuration",
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
		importPicService.getUnImportPicList(function(res) {
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
		var st = null;
		//判断是否有订单明细
		var gridOrder = Ext.getCmp('orderDetailGrid');
		if (gridOrder != null){
			st = gridOrder.getStore();
		}
		//判断是否有报价明细
		var gridPrice = Ext.getCmp('priceDetailGrid');
		if (gridPrice != null){
			st = gridPrice.getStore();
		}
		//判断是否有送样明细
		var gridGiven = Ext.getCmp('givenDetailGrid');
		if (gridGiven != null){
			st = gridGiven.getStore();
		}
//		if(st!=null){
//			if(st.getCount()==0){
//				Ext.Msg.alert('提示消息', "请先添加货号,保存后再导入图片！");
//				return;
//			}
//		}
		
		var panel = Ext.getDom("result");
		mask("Loding...........");
		// 延时
		var task = new Ext.util.DelayedTask(function() {
					DWREngine.setAsync(false);
					importPicService.updatePicture(cfg.pId, typeField
									.getValue(), function(msgList) {
								if (msgList.length == 0) {
									Ext.Msg.alert('Message', "请上传图片后再执行导入图片！");
								}else{
									panel.innerHTML = "<div style='margin-left:20px;'>"
									for (var i = 0; i < msgList.length; i++) {
	//									if (i == msgList.length - 1 && i != 1) {
	//										var successSum = msgList[i].coverSum
	//												+ msgList[i].addSum;
	//										panel.innerHTML += "<br/>图片导入信息：<br/><br/>覆盖"
	//												+ msgList[i].coverSum
	//												+ "张图片<br/>共成功导入"
	//												+ successSum
	//												+ "张图片";
	//									}
	//									if (i == 1) {
	//										var successSum = msgList[0].coverSum
	//												+ msgList[0].addSum;
	//										panel.innerHTML += "<br/>图片导入信息：<br/><br/>覆盖"
	//												+ msgList[0].coverSum
	//												+ "张图片<br/>共成功导入"
	//												+ successSum
	//												+ "张图片";
	//									}
										var successSum = msgList[0].coverSum
												+ msgList[0].addSum;
										panel.innerHTML += "<br/>Picture import：<br/><br/>Add"
												+ msgList[0].addSum
												+ "<br/>"+
												+ msgList[0].coverSum
												+ "<br/>"
												+"Success"
												+ successSum
												+ "";
									}
									panel.innerHTML += "</div>";
									//刷新表格
									st.reload();
								}
								unmask();
							});
					bindSel();
					DWREngine.setAsync(true);
				});
		task.delay(500);
	}
	
	//加载未导入的图片
	bindSel();

	// 表单
	var con = {
		id : 'importPicPanel',
		layout : 'fit',
		width : 600,
		height : 490,
		border : false,
		items : [form]
	};

	Ext.apply(con, cfg);
	ImportPicPanel.superclass.constructor.call(this, con);
};
Ext.extend(ImportPicPanel, Ext.Panel, {});
