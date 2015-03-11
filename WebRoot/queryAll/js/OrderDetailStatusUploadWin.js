OrderStatusDetailUploadGrid = Ext.extend(Ext.grid.GridPanel, {
	title : 'Upload',
	fileType:'All',
	/*
	 * 文件上传路径
	 */
	path:"orderStatus",
	/*
	 * 是否显示upload和delete按钮
	 */
	showOptionBtn:true,
	//width : 450,
	//height : 200,
	//modal : true,
	border : false,
	//selRow:null,
	//stroe:null,
	orderStatus:'SAMPLE',
	id:'filegrid',
	stripeRows : true,
	border : false,
	orderId:0,
	viewConfig : {
		forceFit : true
	},
	initComponent : function() {
		var win = this;
		var roleRecordRight = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "fileName"
				}, {
					name : "fileSize"
				},{
					name : "empsId"
				},{
					name : "orderId"
				},{
					name:'filePath'
				}]);
		var dsRight = new Ext.data.Store({
			autoLoad:{
				params : {
					start : 0,
					limit:20
				}			
			},
			method : 'post',
			proxy : new Ext.data.HttpProxy({
				url : "cotorderstatus.do?method=queryOrderStatusFile&orderId="+win.orderId+"&orderStatus="+win.orderStatus
			}),
			reader : new Ext.data.JsonReader({
						root : "data",
						totalProperty : "totalCount",
						idProperty : "id"
					}, roleRecordRight)
		});

		// 创建复选框列
		var smRight = new Ext.grid.CheckboxSelectionModel();
		// 创建需要在表格显示的列
		var cmRight = new Ext.grid.ColumnModel({
					defaults : {
						sortable : true
					},
					columns : [smRight, {
								header : "编号",
								dataIndex : "id",
								width : 50,
								hidden : true
							}, {
								header : "FileName",
								dataIndex : "fileName",
								width : 120,
								renderer:function(value, metaData, record, rowIndex, colIndex, store){
									return '<a href="./'+record.get('filePath')+'" target="_blank">'+value+'</a>'
								}
							}, {
								header : "Size",
								dataIndex : "fileSize",
								width : 90,
								renderer:function(value){
									return Math.floor((parseInt(value)/1024))+'KB';
								}
							}]
				});
		var toolBarRight = new Ext.PagingToolbar({
					pageSize : 20,
					store : dsRight,
					displayInfo : true,
					displaySize : '5|10|15|20|300|500'
				});
		var tb = new Ext.Toolbar({
			hidden:!win.showOptionBtn,
			items:['->',{
				text:'Upload',
				//iconCls:'email_go',
				handler:win.showUploadWin.createDelegate(win,[])
			},{
				text:'Del',
				iconCls:'page_del',
				handler:win.delFiles.createDelegate(win,[])
			}]
		})
		this.store = dsRight;
		this.cm = cmRight;
		this.sm = smRight;
		this.loadMask = true;
		this.tbar = tb;
		this.bbar = toolBarRight;
				
		//var rightTopGrid = new Ext.grid.GridPanel({});
//		var topPnl = new Ext.Panel({
//					layout : "border",
//					height : 200,
//					border : false,
//					items : [rightTopGrid]
//				});
		//this.layout = "border";
		//this.items = [rightTopGrid];
		OrderStatusDetailUploadGrid.superclass.initComponent.call(this);
	},
	showUploadWin:function(){
		var me = this;
		var win = new Ext.Window({
			title:'Upload',
			closeAction:'close',
			width:300,
			height:120,
			modal : true,
			frame:true,
			layout:'fit',
			buttons:[{
				text:'Save',
				handler:function(){
					var form = Ext.getCmp('fileUrl');
					form.getForm().submit({
						url : './UploadOrderStatusFile.action?fileType='+me.fileType+"&path="+me.path,
						method : 'post',
						waitTitle : 'waiting',
						waitMsg : 'uploading...',
						success : function(fp, o) {
							var fileName = Ext.util.Format.htmlDecode(o.result.fileName);
							var filePath = Ext.util.Format.htmlDecode(o.result.filePath);
							// 上传成功并保存
							var file = new CotOrderStatusFile();
							file.fileName = fileName;
							file.filePath = filePath;
							file.fileSize = Ext.util.Format.htmlDecode(o.result.fileSize);
							console.log(o.result)
							file.orderId = me.orderId;
							var grid = Ext.getCmp('filegrid');
							cotOrderStatusFileService.saveFile(file,me.orderStatus,logId,function(res){
									Ext.Msg.alert('Info',res);
									grid.getStore().reload();
									win.close();
									
							});
						},
						failure : function(fp, o) {
							Ext.MessageBox.alert("Message",o.result.msg);
						}
					});
				}
			}],
			items:[{
				xtype:'form',
				fileUpload:true,
				id:'fileUrl',
				items:[{
					xtype : 'fileuploadfield',
					emptyText : 'upload file',
					anchor : "100%",
					allowBlank : true,
					blankText : "Please choose a file",
					fieldLabel : 'File',
					buttonText : '',
					buttonCfg : {
						iconCls : 'upload-icon'
					}
				}]
			}]
		});
		win.show();
	},
	delFiles:function(){
		var grid = Ext.getCmp("filegrid");
		var rows = grid.getSelectionModel().getSelections();
		var ids = [];
		var filePaths = [];
		if(rows.length == 0){
			Ext.Msg.alert('Info','Please Choose a record');
			return;
		}
		Ext.each(rows,function(row){
			ids.push(row.id);
			filePaths.push(row.data.filePath);
		});
		cotOrderStatusFileService.delFile(ids,filePaths,function(res){
			Ext.Msg.alert('Info','Delete Success');
			grid.getStore().reload();
		})
	}
})