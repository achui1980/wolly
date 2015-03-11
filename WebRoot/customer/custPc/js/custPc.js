Ext.onReady(function() {
	var curData;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotTypeLv3", "id", "typeName", function(
					res) {
				curData = res;
			});
	DWREngine.setAsync(true);
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "categoryId"
			}, {
				name : "pcRemark"
			},{
				name : "tempLate"
			},{
				name : "custId"
			},{
				name : "filePath"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotcustomer.do?method=queryCustPc&custId="
									+ $('custId').value
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
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "Category",
					dataIndex : "categoryId",
					width : 110,
					renderer : function(value) {
						return curData[value];
					}
				},{
					header : "TempLate",
					dataIndex : "tempLate",
					width : 200
				},{
					header : "Photo",
					dataIndex : "id",
					width : 100,
					renderer : function(value) {
						var rdm = Math.round(Math.random() * 10000);
						return '<img src="./showPicture.action?flag=custpc&detailId='
								+ value
								+ '&tmp='
								+ rdm
								+ ' width="70" height="70px" onload="javascript:DrawImage(this,70,70)"/>'
					}
				},{
					header : "PDF",
					dataIndex : "filePath",
					width : 200,
					renderer : function(value) {
						var pdf="<a onclick=openPdf('artWorkPdf/client/"+value+"') style='cursor: hand;text-decoration: underline;'><font color=blue>"+value+"</font></a>";
						var tmp="artWorkPdf/client/"+value;
						var download="<img style='cursor:hand;' src='common/ext/resources/images/extend/page_white_acrobat.png' title='DownLoad' onmouseover=max(this) onmouseout=min(this) onclick=downRpt('./downPc.action?fileName="+value+"&filePath="+tmp+"')>";
						return pdf+"&nbsp;&nbsp;&nbsp;"+download;
					}
				},{
					header : "Remark",
					dataIndex : "pcRemark",
					width : 340
				}]
	});

	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Create",
							handler : windowopenAdd,
							iconCls : "page_add"
						}, '-', {
							text : "Update",
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod"
						}, '-', {
							text : "Delete",
							iconCls : 'page_del',
							handler : deleteBatch
						}, '-', {
							text : "DownLoad",
							iconCls : 'page_import',
							handler : downPdfs
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var grid = new Ext.grid.GridPanel({
				border : true,
				region : "center",
				id : "custPcGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});
	// 双击行显示编辑页面
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	// 添加
	function windowopenAdd() {
		openWindowBase(430, 400, 'cotcustomer.do?method=custPcEdit&custId='
						+ $('custId').value);
	}

	// 获得勾选的记录编号
	function getIds() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 修改
	function windowopenMod(obj) {
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.MessageBox.alert('Message', "Please choose one record!");
				return;
			} else if (ids.length > 1) {
				Ext.MessageBox.alert('Message', "Only choose one record!")
				return;
			} else
				obj = ids[0];

		}
		openWindowBase(430, 400, 'cotcustomer.do?method=custPcEdit&eId=' + obj
						+ '&custId=' + $('custId').value);
	}

	//批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert('Message', "Please choose one record!");
			return;
		}
		var flag = Ext.MessageBox.confirm('Message', "Are you sure delete those phones?",
				function(btn) {
					if (btn == 'yes') {
						cotCustomerService.deleteCustPcs(list, function(res) {
									Ext.MessageBox.alert('Message', "Suceess Delete!");
									reloadGrid("custPcGrid");
								});
					}
				});
	}
	
	//批量下载pdf
	function downPdfs(){
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert('Message', "Please choose one record!");
			return;
		}
		var flag = Ext.MessageBox.confirm('Message', "Are you sure download those pdfs?",
				function(btn) {
					if (btn == 'yes') {
						//如果只是选择了一张,就不压缩成zip
						if(list.length==1){
							var record = sm.getSelected();
							var value=record.data.filePath;
							var tmp="artWorkPdf/client/"+value;
							downRpt("./downPc.action?fileName="+value+"&filePath="+tmp);
						}else{
							var sels = sm.getSelections();
							var str = "";
							Ext.each(sels, function(item) {
										str+=item.data.filePath+",";
									});
							var url = "./downPics.action?tp=custpc&str=" + str+"&customerNo="+$('customerNo').value;
							downRpt(url);
						}
					}
				});
	}
	
	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message", '您没有打印权限！');
			return;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'orderPic',
						pId : 'custId',
						pNo : 'custId',
						mailSendId : 'custId',
						status : 'custId'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0] - 200, po[1] + 25);
			printWin.show();
		} else {
			printWin.hide();
		}
	};

});
