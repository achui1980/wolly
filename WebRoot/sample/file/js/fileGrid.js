Ext.onReady(function() {

	var eleId = $('eleid').value;

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fileName"
			}, {
				name : "fileDate"
			}, {
				name : "fileDesc"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotfile.do?method=query" + "&eleId=" + eleId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				width : 50,
				sortable : true,
				hidden : true
			}, {
				header : "File Name",
				dataIndex : "fileName",
				width : 120,
				renderer : function(value, metaData, record, rowIndex,
						colIndex, store) {
					var fileId = record.get("id");
					var str = "<a href=\"./download.action?fileId=" + fileId
							+ "\">" + value + "</a>";
					return str;
				},
				sortable : true
			}, {
				header : "Uploaded Date",
				dataIndex : "fileDate",
				width : 120,
				renderer : function(value) {
					return value.year + "-" + (value.month + 1) + "-"
							+ value.day;
				},
				sortable : true
			}, {
				header : "Remark",
				dataIndex : "fileDesc",
				width : 240,
				sortable : true
			}, {
				header : "Operation",
				dataIndex : "id",
				width : 60,
				renderer : function(value) {
					// var mod = '<a
					// href="javascript:modTypeById('+value+')">修改</a>';

					var nbsp = "&nbsp &nbsp &nbsp"
					var del = '<a href="javascript:del(' + value + ')">del</a>';
					return del + nbsp;
				}
			}]);
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Upload",
							cls : "SYSOP_MOD",
							iconCls : "gird_upload",
							handler : showUploadPanel
						}, '-', {
							text : "Delete",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}]
			});
	var grid = new Ext.grid.GridPanel({
				border : false,
				region : "center",
				id : "fileGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				split : true,
				viewConfig : {
					forceFit : true
				}
			});

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});

	// 获得选择的记录
	var getIds = function() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var file = new CotFile();
					file.id = item.id;
					res.push(file);
				});
		return res;
	}
	// 删除
	this.del = function(id) {
		// 删除权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		var isPopedom = 1
		if (isPopedom == 0)// 没有删除权限
		{
			Ext.MessageBox.alert("Message", "You do not have permission to delete!");
			return;
		}

		var config = {
			title : "Message",
			msg : "Are you sure to delete this file?",
			width : 220,
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					var list = new Array();
					var file = new CotFile();
					file.id = id;
					list.push(file);
					cotFileService.deleteFile(list, function(delres) {
								if (delres) {
									reloadGrid("fileGrid");
									clearForm("fileFormId");
									Ext.MessageBox.alert("Message", 'Deleted successfully！');
								} else {
									Ext.MessageBox.alert("Message", "Deleted Fail");
								}
							});
				}
			}
		}
		Ext.MessageBox.show(config);
	}

	// 打开上传面板
	function showUploadPanel() {
		var win = new Ext.Window({
					width : 500,
					title : "Upload",
					modal : true,
					id : "uploadWin",
					items : [new Ext.form.FormPanel({
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
												win.close();
											}
										}],
								items : [{
											xtype : 'fileuploadfield',
											allowBlank : false,
											blankText : "Choose",
											id : 'form-file',
											emptyText : 'Choose',
											fieldLabel : 'File',
											name : 'file-path',
											buttonText : '',
											buttonCfg : {
												iconCls : 'upload-icon'
											},
											anchor : "100%"
										}, {
											xtype : 'textarea',
											labelWidth : 100,
											fieldLabel : 'Remark',
											style : 'margin-top:5px',
											name : 'fileDesc',
											anchor : "100%"
										}]
							})]
				})
		win.show();
	}
	// 上传操作
	function uploadAction() {
		var form = Ext.getCmp("uploadForm");
		var eleid = $('eleid').value;
		if (form.getForm().isValid()) {
			form.getForm().submit({
						url : './uploadfile.action',
						params : {
							eleId : eleid
						},
						waitMsg : 'Data Uploading......',
						success : function(fp, o) {
							Ext.Msg.show({
										title : "Message",
										msg : "Upload successful！",
										minWidth : 200,
										modal : true,
										buttons : Ext.Msg.OK
									});
							var win = Ext.getCmp("uploadWin");
							reloadGrid("fileGrid");
							win.close();
						},
						failure : function(fp, o) {
							Ext.Msg.show({
										title : "Message",
										msg : "Upload failed！",
										minWidth : 200,
										modal : true,
										icon : Ext.Msg.INFO,
										buttons : Ext.Msg.OK
									});
						}
					})
		}
	}

	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", "Please select records");
			return;
		}

		var config = {
			title : "Message",
			msg : "Are you sure to delete these files?",
			width : 240,
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					cotFileService.deleteFile(list, function(delres) {
								if (delres) {
									reloadGrid("fileGrid");
									clearForm("fileFormId");
									Ext.MessageBox.alert("Message", 'Deleted successfully！');
								} else {
									Ext.MessageBox.alert("Message", "Deleted Fail");
								}
							});
				}
			}
		}
		Ext.MessageBox.show(config);
	}

});