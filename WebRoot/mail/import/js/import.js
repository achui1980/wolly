Ext.onReady(function(){
	
	
	empsTree = Ext.extend(Ext.tree.TreePanel,{
		title : '员工树',
	    animate: true,
	    enableDD: false,
	    autoScroll: true,
		rootVisible : false,// 隐藏根节点
	    containerScroll: true,
	    initComponent:function(){
			this.root = new Ext.tree.AsyncTreeNode({
				text : '员工邮箱',
				expanded : true,
				draggable : false,
				id : "root_fac"
			});
			this.loader =  new Ext.tree.TreeLoader({
				dataUrl : "./cotmail.do?method=queryMailTree"
			});
			this.tbar=[ ' ',
				new Ext.form.TextField({
				width: 130,
				emptyText:'查找节点',
	            enableKeyEvents: true,
				listeners:{
	                keydown: {
	                    fn: function(t, e){
							var text = t.getValue();
							Ext.each(this.hiddenPkgs, function(n){
								n.ui.show();
							});
							var me = this;
				            me.hiddenPkgs = [];
				            var root = me.root
							var re = new RegExp(Ext.escapeRe(text), 'i');
							findChild(root);
							function findChild(node){
								var isShow = false;
								if(re.test(node.text)){
									return true;	
								}else if(node.expanded){
									Ext.each(node.childNodes,function(childNode){
										var childIsShow = findChild(childNode);
										if(childIsShow)
											isShow = true;
									});
								}
								if(isShow)
									return true;
								else{
									node.ui.hide();
									me.hiddenPkgs.push(node);
									return false;	
								}
							}
						},
	                    buffer: 350,
	                    scope: this
	                },
	                scope: this
				}
			}), ' ', ' ',{
	            iconCls: 'icon-expand-all',
				tooltip: '扩展所有节点',
	            handler: function(){ this.root.expand(true); },
	            scope: this
	        }, '-', {
		        iconCls: 'icon-collapse-all',
		        tooltip: '关闭所有节点',
		        handler: function(){ this.root.collapse(true); },
		        scope: this
	        }];
	        empsTree.superclass.initComponent.call(this);
	        this.getSelectionModel().on('beforeselect',function(selMode,newNode,oldNode){
	        	var flag = newNode.attributes.flag;
	        	if(pnl.isUploading){
					this.getSelectionModel().select(oldNode);
					Ext.Msg.show({
						title:'系统提示',
						msg:'正在上传邮件,选择其它节点时,请先停止上传',
						icon:Ext.Msg.WARNING,
						buttons:Ext.Msg.OK
					});
					return false;
				}else if(Ext.isEmpty(flag)||flag=='null'){
					if(oldNode)
						this.getSelectionModel().select(oldNode);
					else
						this.getSelectionModel().clearSelections(true);
					return false;
				}else{
					defaultUrl = '../../UploadMailEmlFile.action?m=uploadFile&nodeId='+newNode.id;
					var swfu = pnl.suo;
					swfu.setUploadURL(defaultUrl);
				}
	        },this);
	    }
	})
	
	
	function setupUploadConfig() {
		var swfu = pnl.suo;
		var file_upload_limit = parseInt($("uploadLimit").value);
		if (isNaN(file_upload_limit) || file_upload_limit == 0) {
			$("uploadLimit").value = "300";
			file_upload_limit = 300;
		}
		if (file_upload_limit > 500) {
			Ext.Msg.alert('提示消息', "导入邮件数过多，最多500张")
			file_upload_limit = 500;
			$("uploadLimit").value = "500";
		}
		swfu.setFileUploadLimit(file_upload_limit);
		// 判断输入的内容是否违规
		var file_size_limit = parseInt($("sizeLimit").value);
		if (isNaN(file_upload_limit) || file_size_limit == 0) {
			$("sizeLimit").value = "300";
			file_size_limit = 300;
		}
		if (file_size_limit > 100*1024) {
			Ext.Msg.alert('提示消息', "每封最大不能大于（100MB），最多100M")
			file_size_limit = 100*1024;
			$("sizeLimit").value = file_size_limit;
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
				modal:true,
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
							fieldLabel : "每次导入邮件数",
							id : "uploadLimit",
							allowDecimals : false,
							allowNegative : false,
							value : 300,
							maxValue : 500,
							anchor : "100%"
						}, {
							xtype : "numberfield",
							fieldLabel : "每封邮件大小(K)",
							allowDecimals : false,
							allowNegative : false,
							maxValue : 100*1024,
							value : 10*1024,
							id : "sizeLimit",
							anchor : "100%"
						}]
			})
	
	
	//默认上传路径
	var defaultUrl ='';
	Ext.DomHelper.append(document.body, {
				html : '<div id="tempBrn" style="background-color: #AADBF0;"><div id="spanButtonPlaceholder"></div>'
			}, true);
	
	var tb = new Ext.Toolbar({
				items : [{
							text : "上传配置",
							id:'cfgBtn',
							handler : function(){
								uploadSeting.show();
							},
							iconCls : "page_config",
							cls : "SYSOP_ADD"
						},'-',{
							xtype:'panel',
							contentEl:'tempBrn'
						},{
							text:'<span style="color:red;font-weight:bold">一次性最多只能选中500封邮件</span>'
						},'-']
			});
	var pnl = new Ext.ux.SwfUploadPanel({
//				width : 480,
//				height : 400,
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
				file_types : "*.eml",
				isUploading:false,//判断是否正在上传
				file_types_description : "EML文件",
				file_upload_limit : 300, // 上传数量
				file_size_limit : 10*1024,
				reloadUrl : window.location.href,
				reloadFn : function(server_data){
					this.isUploading = false;
					impPanel.doLayout();
				},
				fileDialogComplete:function(file_count){
					if (file_count > 0){
						//显示删除按钮
						this.getTopToolbar().items.get(1).setVisible(true);
						//显示上传按钮
						this.getTopToolbar().items.get(3).setVisible(true);
					}
					impPanel.doLayout();
				},uploadFiles: function() {
					if(!impTree.getSelectionModel().getSelectedNode()){
						Ext.Msg.show({
							title:'系统提示',
							msg:'请选择要导入的节点',
							icon:Ext.Msg.INFO,
							buttons:Ext.Msg.OK
						});
						return;
					}
					this.isUploading = true;
					this.getTopToolbar().items.get(2).setVisible(true);	
					this.suo.startUpload();
				},stopUpload: function( cancel_btn ) {
					this.isUploading = false;
					this.suo.stopUpload();
					this.getStore().each(function() {
						if (this.data.status == 1) {
							this.set('status', 0);
							this.commit();
						}
					});
		
					this.getTopToolbar().items.get(2).setVisible(false);			
					this.progress_bar.reset();
					this.progress_bar.updateText('上传进度');
			
				}
			});
			
			
			 
	var impPanel = new Ext.Panel({
		title : '邮件导入',
		region : 'center',
		tbar : tb,
		layout: 'fit',
		margins:'5 5 5 0',
		items:[pnl]
	
	});
	var impTree = new empsTree({
		width: 200,
	    maxSize:350,
	    split: true,
	    minSize:150,
		region : 'west',
		margins:'5 0 5 5'
	});
	var viewport = new Ext.Viewport({
					layout : 'border',
					items : [impTree, impPanel]
				});
	viewport.doLayout();
});